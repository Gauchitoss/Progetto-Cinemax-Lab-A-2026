package cinemax.util;

import cinemax.model.*;
import java.io.*;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class GestorePrenotazione {

    private static List<Prenotazione> listaPrenotazioni = new ArrayList<>();
    private static final String FILE_PATH = "data/prenotazioni.csv";
    private static final DateTimeFormatter FORMATTA_DATA = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    // ====================================================== 
    //                  Metodo salvaSuFile
    // ======================================================
    public static void salvaSuFile() {
        File folder = new File("data");
        if (!folder.exists()) {
            folder.mkdirs();
        }
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_PATH))) {
            pw.println("codice_prenotazione|username_cliente|data_proiezione|ora_proiezione|titolo_film|numero_biglietti");
            for (Prenotazione p : listaPrenotazioni) {
                pw.println(p.toCSV(FORMATTA_DATA));
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
                    if (colonna.length < 6) continue;

                    for (int i = 0; i < colonna.length; i++) {
                        colonna[i] = colonna[i].replace("\"", "").trim();
                    }

                    String codice = colonna[0];
                    String username = colonna[1];
                    LocalDate dataProiezione = LocalDate.parse(colonna[2], FORMATTA_DATA);
                    String oraProiezione = colonna[3];
                    String titoloFilm = colonna[4];
                    int numeroBiglietti = Integer.parseInt(colonna[5]);

                    // 1. Recupero l'utente sfruttando il nuovo metodo aggiunto in GestoreUtenti
                    Utente cliente = GestoreUtenti.cercaPerUsername(username);

                    // 2. Recupero la proiezione corretta dalla vostra lista di GestoreProiezione
                    Proiezione proiezioneScelta = null;
                    for (Proiezione pr : GestoreProiezione.getListaProiezioni()) {
                        if (pr.getTitolo().equals(titoloFilm) && 
                            pr.getData().equals(dataProiezione) && 
                            pr.getOra().equals(oraProiezione)) {
                            proiezioneScelta = pr;
                            break;
                        }
                    }

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
        int postiOccupati = 0;
        for (Prenotazione pre : listaPrenotazioni) {
            Proiezione pr = pre.getProiezione();
            if (pr.getTitolo().equals(p.getTitolo()) && pr.getData().equals(p.getData()) && pr.getOra().equals(p.getOra())) {
                postiOccupati += pre.getNumeroBiglietti();
            }
        }
        return p.getPostiSala() - postiOccupati;
    }

    /**
     * Requisito Inserimento: Controlla la disponibilità ed emette un codice unico alfanumerico.
     */
    public static boolean inserisciPrenotazione(Utente cliente, Proiezione proiezione, int bigliettiRichiesti) {
        int postiLiberi = getPostiLiberi(proiezione);
        if (bigliettiRichiesti > postiLiberi) {
            System.err.println("Errore: Posti insufficienti per lo spettacolo. Disponibili: " + postiLiberi);
            return false;
        }

        // Genera una stringa casuale unica di 8 caratteri maiuscoli
        String codice = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Prenotazione nuova = new Prenotazione(codice, cliente, proiezione, bigliettiRichiesti);
        listaPrenotazioni.add(nuova);
        salvaSuFile();
        System.out.println("Prenotazione salvata! Codice ricevuta: " + codice);
        return true;
    }

    /**
     * Requisito Cliente: Consente lo spostamento ad un'altra proiezione 
     * a patto che la data di partenza e quella di arrivo siano entrambe nel futuro.
     */
    public static boolean modificaDataPrenotazione(String codice, Proiezione nuovaProiezione) {
    for (Prenotazione pre : listaPrenotazioni) {
        if (pre.getCodiceUnivoco().equalsIgnoreCase(codice)) {
            
            // 1. Prendiamo la data e l'ora attuali del sistema
            java.time.LocalDateTime adesso = java.time.LocalDateTime.now();
            
            // 2. Convertiamo la stringa dell'ora della vecchia proiezione (es. "15:00") in LocalTime
            java.time.LocalTime oraVecchia = java.time.LocalTime.parse(pre.getProiezione().getOra());
            // Uniamo la data e l'ora della vecchia proiezione in un unico LocalDateTime
            java.time.LocalDateTime dataOraVecchia = java.time.LocalDateTime.of(pre.getProiezione().getData(), oraVecchia);

            // 3. Facciamo la stessa identica cosa per la NUOVA proiezione
            java.time.LocalTime oraNuova = java.time.LocalTime.parse(nuovaProiezione.getOra());
            java.time.LocalDateTime dataOraNuova = java.time.LocalDateTime.of(nuovaProiezione.getData(), oraNuova);
            
            // 4. Controllo di sicurezza: nessuna delle due proiezioni deve essere antecedente a questo esatto momento
            if (dataOraVecchia.isBefore(adesso) || dataOraNuova.isBefore(adesso)) {
                System.err.println("Errore: Non si possono modificare spettacoli già passati o spostarsi su orari passati.");
                return false;
            }

            // Controllo della disponibilità dei posti nella nuova sala
            if (getPostiLiberi(nuovaProiezione) < pre.getNumeroBiglietti()) {
                System.err.println("Errore: La nuova proiezione selezionata non ha abbastanza posti liberi.");
                return false;
            }

            // Aggiornamento e persistenza
            pre.setProiezione(nuovaProiezione);
            salvaSuFile();
            System.out.println("Spostamento della prenotazione eseguito con successo.");
            return true;
        }
    }
    System.err.println("Errore: Il codice inserito non corrisponde a nessuna prenotazione attiva.");
    return false;
}

    /**
     * Requisito Bigliettaio: Ricerca avanzata incrociata tramite Stream (Speculare a GestoreProiezione)
     */
    public static List<Prenotazione> cercaPrenotazioni(String codice, String nomeCognome, String titolo, String dataInizioStr, String dataFineStr) {
        LocalDate dataInizio = null;
        LocalDate dataFine = null;
        try {
            if (dataInizioStr != null && !dataInizioStr.isEmpty()) dataInizio = LocalDate.parse(dataInizioStr, FORMATTA_DATA);
            if (dataFineStr != null && !dataFineStr.isEmpty()) dataFine = LocalDate.parse(dataFineStr, FORMATTA_DATA);
        } catch (DateTimeException e) {
            System.err.println("Formato intervallo date di ricerca non valido.");
            return new ArrayList<>();
        }

        final LocalDate finalDa = dataInizio;
        final LocalDate finalA = dataFine;

        return listaPrenotazioni.stream()
            .filter(p -> codice == null || codice.isEmpty() || p.getCodiceUnivoco().equalsIgnoreCase(codice.trim()))
            .filter(p -> titolo == null || titolo.isEmpty() || p.getTitoloFilm().toLowerCase().contains(titolo.trim().toLowerCase()))
            .filter(p -> {
                if (nomeCognome == null || nomeCognome.isEmpty()) return true;
                String completo = p.getNomeCliente() + " " + p.getCognomeCliente();
                return completo.toLowerCase().contains(nomeCognome.trim().toLowerCase());
            })
            .filter(p -> {
                LocalDate pData = p.getProiezione().getData();
                if (finalDa != null && finalA == null) return pData.isEqual(finalDa);
                if (finalDa != null && finalA != null) return !pData.isBefore(finalDa) && !pData.isAfter(finalA);
                if (finalDa == null && finalA != null) return !pData.isAfter(finalA);
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
}