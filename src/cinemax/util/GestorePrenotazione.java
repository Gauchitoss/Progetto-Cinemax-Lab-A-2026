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

/**
 * Gestore logico e di persistenza per le prenotazioni del sistema CineMax.
 * Si occupa del caricamento, del salvataggio su database CSV e dell'esecuzione
 * delle operazioni di business logic legate ai biglietti (inserimento, rimozione, modifiche).
 * @author Modena Matteo (Matricola: 765099) - VA
 * @author Baroncelli Luca (Matricola: 765099) - VA
 * @author Bin Alessio (Matricola: 762387) - VA
 */
public class GestorePrenotazione {

    // elenc in memoria centrale di tutte le prenotazioni
    private static final List<Prenotazione> listaPrenotazioni = new ArrayList<>();
    private static final String FILE_PATH = "data/prenotazioni.csv";
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    // ====================================================== 
    //                  Metodo salvaSuFile
    // ======================================================

    /**
     * Serializza l'intera lista delle prenotazioni correnti e la scrive su file CSV.
     * Crea in automatico la cartella di destinazione se mancante e inserisce una riga d'intestazione.
     * @return void
     */
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

    /**
     * Svuota la cache in memoria e ripopola l'elenco delle prenotazioni leggendole dal file CSV.
     * Effettua il parsing riga per riga, sanifica i dati e ricostruisce i collegamenti ad oggetti 
     * con le istanze di Utente e Proiezione corrispondenti.
     * @return void
     */
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
                    // Recupero l'utente sfruttando il nuovo metodo aggiunto in GestoreUtenti
                    Utente cliente = GestoreUtenti.cercaPerUsername(username);
                    // Recupero la proiezione corretta dalla vostra lista di GestoreProiezione
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
    //                  Metodi di Logica
    // ======================================================
    
/**
     * Valida i requisiti inserimento ed emette una nuova prenotazione nel sistema con codice unico di 8 caratteri.
     * Effettua controlli sulla quantità richiesta, sulla capienza complessiva della sala e sulla disponibilità residua dei posti.
     * @param cliente           l'utente che richiede la prenotazione
     * @param proiezione        lo spettacolo cinematografico selezionato
     * @param bigliettiRichiesti la quantità di biglietti desiderata
     * @return Boolean true se l'inserimento e il salvataggio vanno a buon fine
     * @throws IllegalArgumentException se i bigliettiRichiesti sono minori o uguali a zero o superano la capienza massima della sala
     * @throws IllegalStateException se lo spettacolo è già iniziato/passato oppure se i posti residui sono insufficienti
     */
    public static Boolean inserisciPrenotazione(Utente cliente, Proiezione proiezione, int bigliettiRichiesti) {
        if(bigliettiRichiesti <= 0)
            throw new IllegalArgumentException("Il numero di biglietti deve essrere maggiore a 0.");
        if(proiezione.getData().isBefore(LocalDate.now()))
            throw new IllegalStateException("Impossibile prenotare, la proiezione è già passata.");
        int postiLiberi = proiezione.getPostiLiberi();
        if (bigliettiRichiesti > postiLiberi) 
            throw new IllegalStateException("Posti insufficienti, posti dispondibili: " + postiLiberi);
        if(bigliettiRichiesti > proiezione.getPostiSala())
            throw new IllegalArgumentException("Il numero di biglietti (" + bigliettiRichiesti + 
                ") supera la capienza della sala (" + proiezione.getPostiSala() + ").");
        // Genera una stringa casuale unica di 8 caratteri maiuscoli
        String codice = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Prenotazione nuova = new Prenotazione(codice, cliente, proiezione, bigliettiRichiesti);
        GestoreProiezione.modificaPostiLiberi(proiezione, bigliettiRichiesti);
        listaPrenotazioni.add(nuova);
        salvaSuFile();
        return true;
    }
    /**
     * Registra un'operazione di sbigliettamento/vendita al banco cassa senza creare una prenotazione utente.
     * Decrementa immediatamente i posti liberi della proiezione scelta e calcola l'importo economico dovuto.
     * @param proiezione        lo spettacolo selezionato per l'acquisto diretto
     * @param bigliettiRichiesti il quantitativo di biglietti acquistati al bancone
     * @return double il costo totale complessivo dell'operazione di vendita
     * @throws IllegalArgumentException se il numero di biglietti è minore o uguale a zero o eccede la capienza strutturale della sala
     * @throws IllegalStateException se la data dello spettacolo è nel passato o se i posti liberi residui non bastano
     */
    public static double venditaDiretta(Proiezione proiezione, int bigliettiRichiesti) {
        if(bigliettiRichiesti <= 0)
            throw new IllegalArgumentException("Il numero di biglietti deve essere maggiore di 0.");
        if(proiezione.getData().isBefore(LocalDate.now()))
            throw new IllegalStateException("Impossibile vendere biglietti: la proiezione è già passata.");
        int postiLiberi = proiezione.getPostiLiberi();
        // FIX: eccezione se si tenta di vendere più biglietti dei posti disponibili
        if (bigliettiRichiesti > postiLiberi)
            throw new IllegalStateException("Posti insufficienti. Posti disponibili: " + postiLiberi);
        if (bigliettiRichiesti > proiezione.getPostiSala())
            throw new IllegalArgumentException("Il numero di biglietti (" + bigliettiRichiesti + 
                ") supera la capienza della sala (" + proiezione.getPostiSala() + ").");
        GestoreProiezione.modificaPostiLiberi(proiezione, bigliettiRichiesti);
        salvaSuFile();
        return bigliettiRichiesti * proiezione.getPrezzo();
    }

    /**
     * Registra un'operazione di sbigliettamento/vendita al banco cassa senza creare una prenotazione utente.
     * Decrementa immediatamente i posti liberi della proiezione scelta e calcola l'importo economico dovuto.
     * @param proiezione        lo spettacolo selezionato per l'acquisto diretto
     * @param bigliettiRichiesti il quantitativo di biglietti acquistati al bancone
     * @return double il costo totale complessivo dell'operazione di vendita
     * @throws IllegalArgumentException se il numero di biglietti è minore o uguale a zero o eccede la capienza strutturale della sala
     * @throws IllegalStateException se la data dello spettacolo è nel passato o se i posti liberi residui non bastano
     */
    public static void rimuoviPrenotazione(String codice) {
        List<Prenotazione> listaPrenotazioniTrovate = new ArrayList<>();
        listaPrenotazioniTrovate = listaPrenotazioni.stream()
        .filter(p -> p.getCodiceUnivoco().equals(codice))
        .collect(Collectors.toList());
        if(listaPrenotazioniTrovate.size() != 1)
            throw new IllegalArgumentException("Nessuna prenotazione trovata con codice: " + codice);
        Prenotazione unica = listaPrenotazioniTrovate.get(0);
        boolean rimossa = listaPrenotazioni.removeIf(p -> p.getCodiceUnivoco().equalsIgnoreCase(codice));
        if(!rimossa)
            throw new IllegalArgumentException("Nessuna prenotazione trovata con codice: " + codice);
        GestoreProiezione.annullaModificaPostiLiberi(GestoreProiezione.individuaProiezione(unica.getTitoloFilm(), unica.getData(), unica.getOra()), unica.getNumeroBiglietti());
        salvaSuFile();
    }

    /**
     * Consente la modifica o lo spostamento di una prenotazione esistente verso un'altra proiezione del palinsesto.
     * Questa funzione esegue controlli rigorosi affinché sia lo spettacolo originale che quello nuovo si trovino nel futuro.
     * @param codice           il codice identificativo della prenotazione da variare
     * @param nuovaProiezione   la nuova istanza di proiezione su cui trasferire i biglietti
     * @throws IllegalStateException se la vecchia o la nuova proiezione sono passate, oppure se la nuova non ha posti liberi a sufficienza
     * @throws IllegalArgumentException se il codice inserito non corrisponde a nessuna prenotazione in memoria
     */
    public static void modificaDataPrenotazione(String codice, Proiezione nuovaProiezione) {
    for (Prenotazione pre : listaPrenotazioni) {
        if (pre.getCodiceUnivoco().equalsIgnoreCase(codice)) {
            LocalDateTime adesso = LocalDateTime.now();
            LocalDateTime dataOraVecchia = LocalDateTime.of(pre.getProiezione().getData(), pre.getProiezione().getOra());
            LocalDateTime dataOraNuova = LocalDateTime.of(nuovaProiezione.getData(), nuovaProiezione.getOra());
            if (dataOraVecchia.isBefore(adesso) || dataOraNuova.isBefore(adesso)) {
                throw new IllegalStateException("Errore: Non si possono modificare spettacoli già passati o spostarsi su orari passati.");
            }
            // Controllo della disponibilità dei posti nella nuova sala
            if (nuovaProiezione.getPostiLiberi() < pre.getNumeroBiglietti()) {
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
     * Esegue una ricerca multi-filtro flessibile e avanzata incrociando i dati tramite stream.
     * Qualsiasi parametro passato come null o vuoto viene automaticamente ignorato dal criterio di filtraggio.
     * @param codice     filtro sul codice prenotazione esatto
     * @param nome       filtro sul nome del titolare (ricerca parziale case-insensitive)
     * @param cognome    filtro sul cognome del titolare (ricerca parziale case-insensitive)
     * @param titolo     filtro sul titolo del film (ricerca parziale case-insensitive)
     * @param dataInizio limite temporale inferiore per la data dello spettacolo
     * @param dataFine   limite temporale superiore per la data dello spettacolo
     * @param username   filtro sull'username esatto del cliente registrato
     * @return List una lista filtrata di oggetti {@link Prenotazione} corrispondenti ai parametri forniti
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
     * Restituisce un elenco filtrato di tutte le prenotazioni abbinate a spettacoli previsti per la giornata odierna.
     * @return List un elenco di prenotazioni odierne
     */
    public static List<Prenotazione> getPrenotazioniOdierne() {
        LocalDate oggi = LocalDate.now();
        return listaPrenotazioni.stream()
            .filter(p -> p.getProiezione().getData().isEqual(oggi))
            .collect(Collectors.toList());
    }

/**
     * Restituisce una copia shallow di sicurezza della lista completa di tutte le prenotazioni.
     * @return List la lista contenente tutte le istanze delle prenotazioni globali
     */
    public static List<Prenotazione> getListaPrenotazioni() {
        return new ArrayList<>(listaPrenotazioni);
    }

    /**
     * Estrae ed raggruppa in una lista dedicata esclusivamente le prenotazioni effettuate da uno specifico utente cliente.
     * @param username l'username identificativo del cliente
     * @return List l'elenco di prenotazioni intestate a quel determinato username
     */
    public static List<Prenotazione> getPrenotzioniUtente(String username){
        List<Prenotazione> prenotzioniUtente = new ArrayList<>();
        for(Prenotazione p: listaPrenotazioni){
            if(p.getUsernameCliente().equals(username))
                prenotzioniUtente.add(p);
        }
        return prenotzioniUtente;
    }
}