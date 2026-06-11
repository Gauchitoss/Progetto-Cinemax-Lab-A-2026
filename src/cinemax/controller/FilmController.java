package cinemax.controller;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import cinemax.CineMax;
import cinemax.CostantiForm.Campi;
import cinemax.MenuManager.StatoMenu;
import cinemax.model.Proiezione;
import cinemax.util.GestoreProiezione;

/**
 * Questa classe controlla tutto quello che riguarda la proiezione.
 * Serve per inserire una nuova proiezione, cercare una proiezione con i filtri e cancellare una proiezione.
 * @author Modena Matteo (Matricola: 765099) - VA
 * @author Baroncelli Luca (Matricola: 761582) - VA
 * @author Bin Alessio (Matricola: 762387) - VA
 */
public class FilmController {

    // Campi utili per la gestione della TUI
    public static List<Proiezione> filmPaginaTmp = new ArrayList<>();
    public static boolean esistenzaPaginaSuccessiva;
    public static boolean esistenzaPrecedente;
    public static int paginaCorrente = 0;
    public static final int ELEMENTI_PAGINA = 10; 
    public static List<Proiezione> proiezioniTrovate = new ArrayList<>();
    public static Proiezione filmSelezionatoTmp;

    private static final int ANNO_MIN = 1888;
    private static final int ANNO_MAX_FUTURO = 5;
    private static final int DURATA_MAX = 600;
    private static final int DURATA_MIN = 10;
    private static final int ETA_MAX = 21;
    private static final double PREZZO_MAX = 999.99;
    private static final int POSTI_MAX = 200;

    /**
     * Questo metodo riceve una lista pronta di proiezioni e la prepara per essere mostrata nella pagina.
     * @param lista la lista contenente le proiezioni da caricare e mostrare
     */
    public static void gestisciCercaFilm(List<Proiezione> lista){
        try {
            proiezioniTrovate = lista;
            paginaCorrente = 0;
            aggiornaProiezioniPerPagina();
            CineMax.stackRecord.push(StatoMenu.VISUALIZZA_PROGRAMMAZAIONE);
        } catch (Exception e) {
            cinemax.LogicaStatiManager.messaggioErroreCorrente = "Errore generico durante il caricamento del programma.";
            CineMax.stackRecord.push(StatoMenu.STATO_ERRORE);
        }
    }

/**
     * Legge i filtri scritti dall'utente nel form e cerca le proiezioni corrispondenti.
     * @param datiFormTmp l'array con tutti i testi scritti nel form per filtrare la proiezione
     */
    public static void gestisciCercaFilmDaArray(String[] datiFormTmp){
        try{
            CineMax.stackRecord.pop();
            GestoreProiezione.leggiProiezioni();

            String  titolo          = datiFormTmp[Campi.CERCA_TITOLO.i];
            String  genere          = datiFormTmp[Campi.CERCA_GENERE.i];
            String  costo           = datiFormTmp[Campi.CERCA_COSTO.i];
            LocalDate dataDa = parseData(datiFormTmp[Campi.CERCA_GIORNO_1.i], datiFormTmp[Campi.CERCA_MESE_1.i], datiFormTmp[Campi.CERCA_ANNO_1.i]);
            LocalDate dataA = parseData(datiFormTmp[Campi.CERCA_GIORNO_2.i], datiFormTmp[Campi.CERCA_MESE_2.i], datiFormTmp[Campi.CERCA_ANNO_2.i]);
            Double prezzoMax = null;
            if(costo != null && !costo.isEmpty()){
                try{
                    prezzoMax = Double.parseDouble(costo.replace(",", "."));
                }catch (NumberFormatException e){
                    cinemax.LogicaStatiManager.messaggioErroreCorrente = "Formsto prezzo non valido.";
                    CineMax.stackRecord.push(StatoMenu.STATO_ERRORE);
                    return;
                }
            }    
            proiezioniTrovate = GestoreProiezione.cercaProiezione(titolo, dataDa, dataA, prezzoMax, genere);
            paginaCorrente = 0;
            aggiornaProiezioniPerPagina();
            CineMax.stackRecord.push(StatoMenu.VISUALIZZA_PROGRAMMAZAIONE);
        }catch(DateTimeException e){
            cinemax.LogicaStatiManager.messaggioErroreCorrente = "Intervallo di date non valido.";
            CineMax.stackRecord.push(StatoMenu.STATO_ERRORE);
        }
    }

    /**
     * Calcola quali proiezioni inserire nella pagina attuale in base al numero della pagina.
     * Prende solo il gruppo di proiezioni corretto e attiva o disattiva i tasti per andare avanti e indietro.
     */
    public static void aggiornaProiezioniPerPagina(){
        filmPaginaTmp.clear();
        esistenzaPaginaSuccessiva = false;
        esistenzaPrecedente = false;
        int indiceInizio = ELEMENTI_PAGINA * paginaCorrente;
        for(int i = indiceInizio; i < indiceInizio + ELEMENTI_PAGINA && i <proiezioniTrovate.size(); i++){
            filmPaginaTmp.add(proiezioniTrovate.get(i));
        }
        if(indiceInizio + ELEMENTI_PAGINA < proiezioniTrovate.size()) esistenzaPaginaSuccessiva = true;
        if(paginaCorrente > 0) esistenzaPrecedente = true;
    }
    
    /**
     * Gestisce la scelta dell'utente dentro la lista delle proiezioni.
     * Permette di andare avanti di pagina, tornare indietro, uscire oppure selezionare una proiezione specifica.
     * @param scelta la stringa che rappresenta il comando digitato (numero della proiezione, N, B, o C)
     */
    public static void gestisciVisualizzaProiezione(String scelta){
        try{
           if(scelta.trim().isEmpty() || scelta == null || scelta.equalsIgnoreCase("C")){
                CineMax.stackRecord.pop();
                return;
            }
            if(scelta.equalsIgnoreCase("N") && esistenzaPaginaSuccessiva){
                paginaCorrente++;
                aggiornaProiezioniPerPagina();
                CineMax.stackRecord.pop();
                CineMax.stackRecord.push(StatoMenu.VISUALIZZA_PROGRAMMAZAIONE);
                return;
            }
            if(scelta.equalsIgnoreCase("B")&& esistenzaPrecedente){
                paginaCorrente--;
                aggiornaProiezioniPerPagina();
                CineMax.stackRecord.pop();
                CineMax.stackRecord.push(StatoMenu.VISUALIZZA_PROGRAMMAZAIONE);
                return;
            }
            int numeroFilm = Integer.parseInt(scelta);
            filmSelezionatoTmp = filmPaginaTmp.get(numeroFilm-1);
            CineMax.stackRecord.push(StatoMenu.MENU_INFO_FILM);
    
        }catch(NumberFormatException | IndexOutOfBoundsException e){
            //scelta non valida, schermata rirendirizzata
        }

    }

    /**
     * Controlla i pulsanti premuti dentro la schermata delle informazioni di una proiezione.
     * Cambia i comandi disponibili in base al ruolo di chi sta usando il programma (cliente, proiezionista o bigliettaio).
     * @param scelta il numero del comando inserito dall'utente per la proiezione corrente
     */
    public static void gestisciMenuInfoFilm(String scelta){
        String ruolo = CineMax.ruolo.getEtichetta();
        switch(ruolo){
            case "cliente registrato": 
                if("1".equals(scelta)) {
                    if(filmSelezionatoTmp.getData().isBefore(LocalDate.now())){
                        cinemax.LogicaStatiManager.messaggioErroreCorrente = "Impossibile prenotare: la proiezione è già passata.";
                        CineMax.stackRecord.push(StatoMenu.STATO_ERRORE);
                    }
                    else
                        CineMax.stackRecord.push(StatoMenu.PRENOTA_POSTI);  
                }  
                else if("2".equals(scelta)) CineMax.stackRecord.pop();    
                break;
            case "proiezionista": 
                if("1".equals(scelta)) {
                    GestoreProiezione.elimina(filmSelezionatoTmp);
                    cinemax.LogicaStatiManager.messaggioConfermaCorrente = "Proiezione eliminata con successo.";
                    CineMax.stackRecord.pop();
                    CineMax.stackRecord.pop();
                    CineMax.stackRecord.push(StatoMenu.STATO_CONFERMA);   
                }
                else if("2".equals(scelta))
                    CineMax.stackRecord.push(StatoMenu.GESTISCI_PROIEZIONE);
                else if("3".equals(scelta))
                    CineMax.stackRecord.pop();
                break;
            case "bigliettaio":
                if("1".equals(scelta)){
                    if(filmSelezionatoTmp.getData().isBefore(LocalDate.now())){
                        cinemax.LogicaStatiManager.messaggioErroreCorrente = "Impossibile vendere biglietti: la proiezione è già passata.";
                        CineMax.stackRecord.push(StatoMenu.STATO_ERRORE);
                    }
                else
                    CineMax.stackRecord.push(StatoMenu.VENDITA_DIRETTA);
                }
                else if("2".equals(scelta))
                    CineMax.stackRecord.pop();
                break;
            default:
                CineMax.stackRecord.pop();
        }
    }
    
    /**
     * Questo metodo controlla che tutti i valori inseriti per una nuova proiezione siano corretti e accettabili.
     * Controlla che i numeri non siano negativi e che non superino i limiti massimi decisi per la proiezione.
     * @param annoProduzione l'anno in cui è stata prodotta la proiezione
     * @param durata la durata in minuti della proiezione
     * @param etaMinima l'età minima per vedere la proiezione
     * @param costo il prezzo del biglietto per la proiezione
     * @param posti il numero di posti della sala per la proiezione
     * @param dataProiezione il giorno in cui si tiene la proiezione
     * @throws IllegalArgumentException se uno dei valori inseriti viola le regole della proiezione
     */
    private static void validaDatiProiezione(int annoProduzione, int durata, int etaMinima, double costo, int posti, LocalDate dataProiezione) {
        int annoCorrente = LocalDate.now().getYear();

        if(annoProduzione < ANNO_MIN)
            throw new IllegalArgumentException("Anno di produzione non valido: il cinema esiste dal " + ANNO_MIN + ".");
        if(annoProduzione > annoCorrente + ANNO_MAX_FUTURO)
            throw new IllegalArgumentException("Anno di produzione non può superare " + (annoCorrente + ANNO_MAX_FUTURO) + ".");
        if(durata > DURATA_MAX && durata > DURATA_MIN)
            throw new IllegalArgumentException("La durata non può superare " + DURATA_MAX + " minuti e deve essere maggiore di ." + DURATA_MIN + " minuti");
        if(etaMinima < 0)
            throw new IllegalArgumentException("L'età consigliata non può essere negativa.");
        if(etaMinima > ETA_MAX)
            throw new IllegalArgumentException("L'età consigliata non può superare " + ETA_MAX + " anni.");
        if(costo < 0)
            throw new IllegalArgumentException("Il prezzo non può essere negativo.");
        if(costo > PREZZO_MAX)
            throw new IllegalArgumentException("Il prezzo non può superare " + PREZZO_MAX + " euro.");
        if(posti <= 0)
            throw new IllegalArgumentException("I posti in sala devono essere maggiori di 0.");
        if(posti > POSTI_MAX)
            throw new IllegalArgumentException("I posti in sala non possono superare " + POSTI_MAX + ".");
        if(dataProiezione.isBefore(LocalDate.now()))
            throw new IllegalArgumentException("La data della proiezione non può essere nel passato.");
    }

    /**
     * Questo metodo controlla che il testo inserito per il nome del regista non sia vuoto e contenga solo lettere.
     * @param regista il nome del regista da controllare per la proiezione
     * @throws IllegalArgumentException se il testo è vuoto o contiene caratteri non validi
     */
    private static void validaRegista(String regista) {
        if(regista == null || regista.trim().isEmpty())
            throw new IllegalArgumentException("Il nome del regista è obbligatorio.");
        if(!regista.matches("[\\p{L} '.\\-]+"))
            throw new IllegalArgumentException("Il nome del regista può contenere solo lettere, spazi, apostrofi e trattini.");
    }

    /**
     * Questo metodo prende tutti i testi scritti nel form per una nuova proiezione, li controlla e li salva nel file.
     * * @param datiFormTmp l'array con tutti i valori in formato testo inviati dal form della proiezione
     */
    public static void gestisciInserimentoProiezione(String[] datiFormTmp){
        try {
            GestoreProiezione.leggiProiezioni();
            String  titolo          = datiFormTmp[Campi.ADD_TITOLO.i];
            String  genere          = datiFormTmp[Campi.ADD_GENERE.i];
            String  regista         = datiFormTmp[Campi.ADD_REGISTA.i];
            String  annoProduzione  = datiFormTmp[Campi.ADD_ANNO_PRODUZIONE.i];
            String  durata          = datiFormTmp[Campi.ADD_DURATA.i];
            String  etaMinima       = datiFormTmp[Campi.ADD_ETA.i];
            String  costo           = datiFormTmp[Campi.ADD_COSTO.i];
            String  posti           = datiFormTmp[Campi.ADD_POSTI.i];
            String  giorno          = datiFormTmp[Campi.ADD_GIORNO.i];
            String  mese            = datiFormTmp[Campi.ADD_MESE.i];
            String  anno            = datiFormTmp[Campi.ADD_ANNO.i];
            String  orario          = datiFormTmp[Campi.ADD_ORA.i];


            if(titolo == null || titolo.trim().isEmpty())
                throw new IllegalArgumentException("Il titolo è obbligatorio.");
            validaRegista(regista);
            int annoProduzioneInt = Integer.parseInt(annoProduzione);
            int durataInt = Integer.parseInt(durata);
            int etaMinimaInt = Integer.parseInt(etaMinima);
            double costoDouble = Double.parseDouble(costo.replace(",", "."));
            int postiInt = Integer.parseInt(posti);
            LocalDate data = LocalDate.of(Integer.parseInt(anno), Integer.parseInt(mese), Integer.parseInt(giorno));
            validaDatiProiezione(annoProduzioneInt, durataInt, etaMinimaInt, costoDouble, postiInt, data);
            Proiezione nuovaProiezione = new Proiezione(data, orario, titolo, genere, regista, Integer.parseInt(annoProduzione), Integer.parseInt(durata), Integer.parseInt(etaMinima), Double.parseDouble(costo.replace(",", ".")), Integer.parseInt(posti), Integer.parseInt(posti));
            GestoreProiezione.inserisci(nuovaProiezione);
            CineMax.stackRecord.pop();
            cinemax.LogicaStatiManager.messaggioConfermaCorrente = "Proiezione inserita.";
            CineMax.stackRecord.push(StatoMenu.STATO_CONFERMA);
        } catch (IllegalArgumentException e) {
            cinemax.LogicaStatiManager.messaggioErroreCorrente = e.getMessage();
            CineMax.stackRecord.push(StatoMenu.STATO_ERRORE);
        } catch (DateTimeException e) {
            cinemax.LogicaStatiManager.messaggioErroreCorrente = "Dati non validi, controlla numeri e date.";
            CineMax.stackRecord.push(StatoMenu.STATO_ERRORE);
        } catch(Exception e){
            cinemax.LogicaStatiManager.messaggioErroreCorrente = "Errore generico del programma.";
            CineMax.stackRecord.push(StatoMenu.STATO_ERRORE);            
        }
    }

    /**
     * Questo metodo legge i campi modificati dall'utente per una proiezione esistente e aggiorna i dati salvati.
     * Se l'utente lascia dei campi vuoti, il metodo mantiene i valori vecchi già presenti nella proiezione.
     * @param p la proiezione originale scelta che si vuole modificare
     * @param datiFormTmp l'array contenente i nuovi testi scritti dentro i campi del form
     */
    public static void gestisciModificaProiezione(Proiezione p, String[] datiFormTmp){
        try {
            GestoreProiezione.leggiProiezioni();

            // Leggi e valida solo i campi non nulli
            String nuovoRegista = datiFormTmp[Campi.ADD_REGISTA.i];
            if(nuovoRegista != null) validaRegista(nuovoRegista);

            int annoProduzioneInt = p.getAnno();
            int durataInt = p.getDurata();
            int etaMinimaInt = p.getEtaMin();
            double costoDouble = p.getPrezzo();
            int postiInt = p.getPostiSala();
            LocalDate data = p.getData();

            if(datiFormTmp[Campi.ADD_ANNO_PRODUZIONE.i] != null) annoProduzioneInt = Integer.parseInt(datiFormTmp[Campi.ADD_ANNO_PRODUZIONE.i]);
            if(datiFormTmp[Campi.ADD_DURATA.i] != null) durataInt = Integer.parseInt(datiFormTmp[Campi.ADD_DURATA.i]);
            if(datiFormTmp[Campi.ADD_ETA.i] != null) etaMinimaInt = Integer.parseInt(datiFormTmp[Campi.ADD_ETA.i]);
            if(datiFormTmp[Campi.ADD_COSTO.i] != null) costoDouble = Double.parseDouble(datiFormTmp[Campi.ADD_COSTO.i].replace(",", "."));
            if(datiFormTmp[Campi.ADD_POSTI.i] != null) postiInt = Integer.parseInt(datiFormTmp[Campi.ADD_POSTI.i]);

            String giorno = datiFormTmp[Campi.ADD_GIORNO.i];
            String mese = datiFormTmp[Campi.ADD_MESE.i];
            String anno = datiFormTmp[Campi.ADD_ANNO.i];
            if(giorno != null && mese != null && anno != null){
                data = LocalDate.of(Integer.parseInt(anno), Integer.parseInt(mese), Integer.parseInt(giorno));
            }

            validaDatiProiezione(annoProduzioneInt, durataInt, etaMinimaInt, costoDouble, postiInt, data);

            if(datiFormTmp[Campi.ADD_TITOLO.i] != null) p.setTitolo(datiFormTmp[Campi.ADD_TITOLO.i]);
            if(datiFormTmp[Campi.ADD_GENERE.i] != null) p.setGenere(datiFormTmp[Campi.ADD_GENERE.i]);
            if(nuovoRegista != null) p.setRegista(nuovoRegista);
            p.setAnno(annoProduzioneInt);
            p.setDurata(durataInt);
            p.setEtaMin(etaMinimaInt);
            p.setPrezzo(costoDouble);
            p.setPostiSala(postiInt);
            p.setData(data);
            if(datiFormTmp[Campi.ADD_ORA.i] != null) p.setOra(datiFormTmp[Campi.ADD_ORA.i]);

            GestoreProiezione.salvaSuFile(); 
            CineMax.stackRecord.pop();
            cinemax.LogicaStatiManager.messaggioConfermaCorrente = "Proiezione modificata con successo.";
            CineMax.stackRecord.push(StatoMenu.STATO_CONFERMA);
        } catch(IllegalArgumentException e){
            cinemax.LogicaStatiManager.messaggioErroreCorrente = e.getMessage();
            CineMax.stackRecord.push(StatoMenu.STATO_ERRORE);
        } catch(Exception e){
            cinemax.LogicaStatiManager.messaggioErroreCorrente = "Errore durante la modifica: " + e.getMessage();
            CineMax.stackRecord.push(StatoMenu.STATO_ERRORE);
        }
    }

    // ======================================================
    //                  HELPER
    // ======================================================
    /**
     * Questo metodo di supporto riceve le stringhe separate di giorno, mese e anno e crea un oggetto data.
     * Se uno dei testi manca o è vuoto, restituisce un valore nullo.
     * @param gg la stringa che contiene il giorno del mese
     * @param mm la stringa che contiene il numero del mese
     * @param aaaa la stringa che contiene l'anno con quattro cifre
     * @return un oggetto {@link LocalDate} che rappresenta la data calcolata, oppure null se i dati sono incompleti
     */
    private static LocalDate parseData(String gg, String mm, String aaaa){
        if(gg == null || mm == null || aaaa == null)
            return null;
        if(gg.isEmpty() || mm.isEmpty() || aaaa.isEmpty())
            return null;
        return LocalDate.of(Integer.parseInt(aaaa), Integer.parseInt(mm), Integer.parseInt(gg));
    }
}
