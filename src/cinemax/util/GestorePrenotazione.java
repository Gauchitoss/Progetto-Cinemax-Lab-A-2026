package cinemax.util;

import cinemax.model.*;
import java.io.*;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class GestorePrenotazione {

    private static final List<Prenotazione> listaPrenotazioni = new ArrayList<>();
    private static final String FILE_PATH = "data/prenotazioni.csv";
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    // ====================================================== 
    //                  Metodo salvaSuFile
    // ======================================================
    public static void salvaSuFile() {
        new File("data").mkdir();
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_PATH))) {
            pw.println("codice_prenotazione|nome_cliente|cognome_cliente|username_cliente|data_proiezione|ora_proiezione|titolo_film|numero_biglietti");
            for (Prenotazione p : listaPrenotazioni) {
                pw.println(p.toCSV());
            }
        } catch (IOException e) {
            System.err.println("Errore durante il salvataggio delle prenotazioni: " + e.getMessage());
        }
    }

    // ====================================================== 
    //                  Metodo leggiPrenotazioni
    // ======================================================
    public static void leggiPrenotazioni() {
        listaPrenotazioni.clear();
        File file = new File(FILE_PATH);
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine(); // Salta l'intestazione
            String riga;
            while ((riga = br.readLine()) != null) {
                try {
                    String[] colonna = riga.split("\\|");
                    if (colonna.length < 8) continue;
                    for (int i = 0; i < colonna.length; i++) {
                        colonna[i] = colonna[i].replace("\"", "").trim();
                    }
                    String codice = colonna[0];
                    // String nome = colonna[1];       
                    // String cognome = colonna[2];    
                    String username = colonna[3];
                    LocalDate dataProiezione = LocalDate.parse(colonna[4], FORMATO_DATA);
                    String oraProiezione = colonna[5];
                    String titoloFilm = colonna[6];
                    int numeroBiglietti = Integer.parseInt(colonna[7]);
                    // 1. Recupero l'utente sfruttando il nuovo metodo aggiunto in GestoreUtenti
                    Utente cliente = GestoreUtenti.cercaPerUsername(username);
                    // 2. Recupero la proiezione corretta dalla vostra lista di GestoreProiezione
                    Proiezione proiezioneScelta = GestoreProiezione.getListaProiezioni().stream()
                        .filter(p -> p.getTitolo().trim().equalsIgnoreCase(titoloFilm)
                                    && p.getData().equals(dataProiezione)
                                    && p.getOraString().equals(oraProiezione))
                        .findFirst().orElse(null);
                    // Ricostruisco il legame ad oggetti solo se i riferimenti incrociati sono validi
                    if (cliente != null && proiezioneScelta != null) {
                        listaPrenotazioni.add(new Prenotazione(codice, cliente, proiezioneScelta, numeroBiglietti));
                    }
                } catch (NumberFormatException | DateTimeException e) {
                    System.err.println("Salto riga prenotazione corrotta o non allineata: " + riga);
                }
            }
        } catch (IOException e) {
            System.err.println("Errore lettura file prenotazioni: " + e.getMessage());
        }
    }

    // ====================================================== 
    //                  Metodi di Business Logic
    // ======================================================
    
    /**
     * Calcola dinamicamente quanti posti sono effettivamente disponibili 
     * sottraendo le prenotazioni esistenti dalla capienza massima memorizzata nella proiezione.
     */
    public static int getPostiLiberi(Proiezione p) {
        int postiOccupati = listaPrenotazioni.stream()
                .filter(pre -> pre.getProiezione().equals(p))
                .mapToInt(Prenotazione::getNumeroBiglietti)
                .sum();
        return p.getPostiSala() - postiOccupati;
    }

    /**
     * Requisito Inserimento: Controlla la disponibilità ed emette un codice unico alfanumerico.
     */
    public static Boolean inserisciPrenotazione(Utente cliente, Proiezione proiezione, int bigliettiRichiesti) {
        if(bigliettiRichiesti <= 0)
            throw new IllegalArgumentException("Il numero di biglietti deve essrere maggiore a 0.");
        if(proiezione.getData().isBefore(LocalDate.now()))
            throw new IllegalStateException("Impossibile prenotare, la proiezione è già passata.");
        int postiLiberi = getPostiLiberi(proiezione);
        if (bigliettiRichiesti > postiLiberi) 
            throw new IllegalStateException("Posti insufficienti, posti dispondibili: " + postiLiberi);
        if(bigliettiRichiesti > proiezione.getPostiSala())
            throw new IllegalArgumentException("Il numero di biglietti (" + bigliettiRichiesti + 
                ") supera la capienza della sala (" + proiezione.getPostiSala() + ").");
        // Genera una stringa casuale unica di 8 caratteri maiuscoli
        String codice = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Prenotazione nuova = new Prenotazione(codice, cliente, proiezione, bigliettiRichiesti);
        listaPrenotazioni.add(nuova);
        salvaSuFile();
        return true;
    }

    public static double venditaDiretta(Proiezione proiezione, int bigliettiRichiesti) {
        if(bigliettiRichiesti <= 0)
            throw new IllegalArgumentException("Il numero di biglietti deve essere maggiore di 0.");
        if(proiezione.getData().isBefore(LocalDate.now()))
            throw new IllegalStateException("Impossibile vendere biglietti: la proiezione è già passata.");
        int postiLiberi = getPostiLiberi(proiezione);
        // FIX: eccezione se si tenta di vendere più biglietti dei posti disponibili
        if (bigliettiRichiesti > postiLiberi)
            throw new IllegalStateException("Posti insufficienti. Posti disponibili: " + postiLiberi);
        if (bigliettiRichiesti > proiezione.getPostiSala())
            throw new IllegalArgumentException("Il numero di biglietti (" + bigliettiRichiesti + 
                ") supera la capienza della sala (" + proiezione.getPostiSala() + ").");
        // FIX: riduce i posti disponibili aggiornando direttamente postiSala nella proiezione
        // (usiamo un "segnaposto" nel CSV riducendo il campo posti_sala)
        proiezione.setPostiSala(proiezione.getPostiSala() - bigliettiRichiesti);
        GestoreProiezione.salvaSuFile();
        return bigliettiRichiesti * proiezione.getPrezzo();
    }

    public static void rimuoviPrenotazione(String codice) {
        boolean rimossa = listaPrenotazioni.removeIf(p -> p.getCodiceUnivoco().equalsIgnoreCase(codice));
        if(!rimossa)
            throw new IllegalArgumentException("Nessuna prenotazione trovata con codice: " + codice);
        salvaSuFile();
    }

    /**
     * Requisito Cliente: Consente lo spostamento ad un'altra proiezione 
     * a patto che la data di partenza e quella di arrivo siano entrambe nel futuro.
     */
    public static void modificaDataPrenotazione(String codice, Proiezione nuovaProiezione) {
    for (Prenotazione pre : listaPrenotazioni) {
        if (pre.getCodiceUnivoco().equalsIgnoreCase(codice)) {
            // 1. Prendiamo la data e l'ora attuali del sistema
            LocalDateTime adesso = LocalDateTime.now();
            // 2. Convertiamo la stringa dell'ora della vecchia proiezione (es. "15:00") in LocalTime
            LocalDateTime dataOraVecchia = LocalDateTime.of(pre.getProiezione().getData(), pre.getProiezione().getOra());
            // Uniamo la data e l'ora della vecchia proiezione in un unico LocalDateTime
            LocalDateTime dataOraNuova = LocalDateTime.of(nuovaProiezione.getData(), nuovaProiezione.getOra());
            // 4. Controllo di sicurezza: nessuna delle due proiezioni deve essere antecedente a questo esatto momento
            if (dataOraVecchia.isBefore(adesso) || dataOraNuova.isBefore(adesso)) {
                throw new IllegalStateException("Errore: Non si possono modificare spettacoli già passati o spostarsi su orari passati.");
            }
            // Controllo della disponibilità dei posti nella nuova sala
            if (getPostiLiberi(nuovaProiezione) < pre.getNumeroBiglietti()) {
                throw new IllegalStateException("Errore: La nuova proiezione selezionata non ha abbastanza posti liberi.");
            }
            // Aggiornamento e persistenza
            pre.setProiezione(nuovaProiezione);
            salvaSuFile();
            System.out.println("Spostamento della prenotazione eseguito con successo.");
            return ;
        }
    }
    throw new IllegalArgumentException("Errore: Il codice inserito non corrisponde a nessuna prenotazione attiva.");
}

    /**
     * Requisito Bigliettaio: Ricerca avanzata incrociata tramite Stream (Speculare a GestoreProiezione)
     */
    public static List<Prenotazione> cercaPrenotazioni(String codice, String nome, String cognome, String titolo, LocalDate dataInizio, LocalDate dataFine, String username) {
        return listaPrenotazioni.stream()
            // Filtro Username
            .filter(p -> username == null || username.trim().isEmpty() ||
                         p.getUsernameCliente().equalsIgnoreCase(username.trim()))

            // Filtro Codice Univoco
            .filter(p -> codice == null || codice.trim().isEmpty() || 
                         p.getCodiceUnivoco().equalsIgnoreCase(codice.trim()))  

            // Filtro Nome
            .filter(p -> nome == null || nome.trim().isEmpty() || 
                         p.getNomeCliente().toLowerCase().contains(nome.trim().toLowerCase()))

            // Filtro Cognome
            .filter(p -> cognome == null || cognome.trim().isEmpty() || 
                         p.getCognomeCliente().toLowerCase().contains(cognome.trim().toLowerCase()))
            
            // Filtro titolo
            .filter(p -> titolo == null || titolo.trim().isEmpty() || p.getTitoloFilm().toLowerCase().contains(titolo.trim().toLowerCase()))
            
            // Filtro Date (Inizio, Fine o Intervallo)
            .filter(p -> {
                LocalDate pData = p.getProiezione().getData();
                if (dataInizio != null && dataFine == null) return pData.isEqual(dataInizio);
                if (dataInizio != null && dataFine != null) return !pData.isBefore(dataInizio) && !pData.isAfter(dataFine);
                if (dataInizio == null && dataFine != null) return !pData.isAfter(dataFine);
                
                return true;
            })
            .collect(Collectors.toList());
    }

    /**
     * Requisito Bigliettaio: Filtro immediato per le proiezioni che hanno luogo oggi
     */
    public static List<Prenotazione> getPrenotazioniOdierne() {
        LocalDate oggi = LocalDate.now();
        return listaPrenotazioni.stream()
            .filter(p -> p.getProiezione().getData().isEqual(oggi))
            .collect(Collectors.toList());
    }

    public static List<Prenotazione> getListaPrenotazioni() {
        return new ArrayList<>(listaPrenotazioni);
    }

    public static List<Prenotazione> getPrenotzioniUtente(String username){
        List<Prenotazione> prenotzioniUtente = new ArrayList<>();
        for(Prenotazione p: listaPrenotazioni){
            if(p.getUsernameCliente().equals(username))
                prenotzioniUtente.add(p);
        }
        return prenotzioniUtente;
    }
}