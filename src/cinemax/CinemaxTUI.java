package cinemax;

import java.util.List;
import cinemax.MenuManager.StatoMenu;
import cinemax.model.Proiezione;
import cinemax.model.Utente;

/**
 * Classe che gestisce l'interfaccia testuale (TUI - Terminal User Interface).
 * Si occupa della formattazione grafica, della stampa dei loghi e della 
 * renderizzazione dei menu in base allo stato corrente dell'applicazione.
 * @author Modena Matteo (Matricola: 765099 ) - VA
 * @author Baroncelli Luca (Matricola: 765099 ) - VA
 * @author Bin Alessio (Matricola: 762387 ) - VA
 */
public class CinemaxTUI {

    public static final int LARGHEZZA_MENU = 130;
    public static final int LARGHEZZA_BOX_INPUT = 38;
    
    private static String bordoSuperiore = "╔"+"═".repeat(LARGHEZZA_MENU)+"╗";
    private static String rigaVuota =      "║"+" ".repeat(LARGHEZZA_MENU)+"║";
    private static String bordoMezzo =     "╠"+"═".repeat(LARGHEZZA_MENU)+"╣";
    private static String bordoInferiore = "╚"+"═".repeat(LARGHEZZA_MENU)+"╝";

// ======================================================
//          SEZIONE CREAZIONE INTERFACCIA
// ======================================================

    /**
     * Ripulisce la console di sistema e forza la ridimensione della finestra 
     * del terminale a dimensioni fisse tramite sequenze speciali di escape ANSI.
     */
    public static void clearConsole() {
        // Forza la finestra a 45 righe di altezza e 135 di larghezza
        System.out.print("\033[8;45;135t");
        System.out.println("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * Renderizza l'intera interfaccia grafica a schermo calcolando i posizionamenti in base allo stato corrente.
     * Proietta sequenzialmente intestazioni, loghi dedicati,box di form o stringhe di errore e conferma.
     * * @param statoMenu Oggetto enum che contiene tutte le informazioni per costruire il menu
     */
    public static void renderizzaMenu(StatoMenu statoMenu){
        clearConsole();
        System.out.println(bordoSuperiore);
        System.out.println(rigaVuota);
        formattaIntestazione(statoMenu.getNomeLogo());
        System.out.println(rigaVuota);
        System.out.println(bordoMezzo);
        System.out.println(rigaVuota);
        System.out.println(rigaVuota);
        if(statoMenu == StatoMenu.STATO_ERRORE){
            formattaTesto(LogicaStatiManager.messaggioErroreCorrente.toUpperCase(), "centro", true);
        } else if(statoMenu == StatoMenu.STATO_CONFERMA){
            formattaTesto(LogicaStatiManager.messaggioConfermaCorrente.toUpperCase(), "centro", true);
        }else if(statoMenu == StatoMenu.VISUALIZZA_PROGRAMMAZAIONE || statoMenu == StatoMenu.MIE_PRENOTAZIONI){
            stampaListaPagina();
        }else if(statoMenu == StatoMenu.MENU_INFO_FILM){
            gestisciInformazioniFilm(cinemax.controller.FilmController.filmSelezionatoTmp);
        }else if(statoMenu == StatoMenu.DETTAGLIO_PRENOTAZIONE){
            gestisciDettaglioPrenotazione(cinemax.controller.PrenotazioniController.prenotazioneSelezionataTmp);
        }else
            formattaTesto(statoMenu.getOpzioni(), statoMenu.getPosizione(), statoMenu.getVisualizzaNumeri());

        System.out.println(rigaVuota);
        System.out.println(rigaVuota);
        System.out.println(bordoInferiore);
        if(CineMax.stackRecord.peek().getVisualizzaNumeri())System.out.print("\nSELEZIONA UN'OPZIONE: ");
        //System.out.println(CineMax.stackRecord);

    }

// ======================================================



// ======================================================
//                  SEZIONE LOGHI
// ======================================================

    static String[] logoCinemax = {
        " ██████╗ ██╗███╗   ██╗███████╗███╗   ███╗ █████╗ ██╗  ██╗",
        "██╔════╝ ██║████╗  ██║██╔════╝████╗ ████║██╔══██╗╚██╗██╔╝",
        "██║      ██║██╔██╗ ██║█████╗  ██╔████╔██║███████║ ╚███╔╝ ",
        "██║      ██║██║╚██╗██║██╔══╝  ██║╚██╔╝██║██╔══██║ ██╔██╗ ",
        "╚██████╗ ██║██║ ╚████║███████╗██║ ╚═╝ ██║██║  ██║██╔╝ ██╗",
        " ╚═════╝ ╚═╝╚═╝  ╚═══╝╚══════╝╚═╝     ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝"
    };

    static String[] logoFilm = {
        "███████╗██╗██╗     ███╗   ███╗",
        "██╔════╝██║██║     ████╗ ████║",
        "█████╗  ██║██║     ██╔████╔██║",
        "██╔══╝  ██║██║     ██║╚██╔╝██║",
        "██║     ██║███████╗██║ ╚═╝ ██║",
        "╚═╝     ╚═╝╚══════╝╚═╝     ╚═╝"
    };
    static String[] logoErrore = {
        " ███████╗██████╗ ██████╗  ██████╗ ██████╗ ",
        " ██╔════╝██╔══██╗██╔══██╗██╔═══██╗██╔══██╗",
        " █████╗  ██████╔╝██████╔╝██║   ██║██████╔╝",
        " ██╔══╝  ██╔══██╗██╔══██╗██║   ██║██╔══██╗",
        " ███████╗██║  ██║██║  ██║╚██████╔╝██║  ██║",
        " ╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═╝"
    };

    /**
     * Intercetta la stringa identificativa passata come argomento e indirizza la stampa
     * del logo corretto provvedendo a formattarlo e centrarlo.
     * @param logo la stringa testuale chiave (es. "cinemax", "film", "error")
     */
    public static void formattaIntestazione(String logo){
        if(logo.equals("cinemax"))  {formattaLogo(logoCinemax); return;}
        if(logo.equals("film"))     {formattaLogo(logoFilm); return;}
        if(logo.equals("error"))    {formattaLogo(logoErrore); return;}
        formattaTesto(logo, "centro", true);
    }

    /**
     * Centra e stampa un logo testuale multilinea all'interno della cornice del menu.
     * Gestisce automaticamente il calcolo degli spazi vuoti (padding) laterali per compensarne la larghezza.
     * @param righelogo array contenente le linee del logo da stampare
     */
    public static void formattaLogo(String[] righelogo){
        int lunghezzaRigaLogo = righelogo[1].length();
        // Utilizzo di Math.max() per evitare il cso in cui il programma va in crash se il logo è troppo largo
        int padding = Math.max(0,(LARGHEZZA_MENU - lunghezzaRigaLogo)/2);
        String pad= " ".repeat(padding);
        for (String linea : righelogo) {
            int extraSpazio = Math.max(0, (LARGHEZZA_MENU - lunghezzaRigaLogo) % 2);
            System.out.println("║"+ pad + (extraSpazio != 0 ? " " : "") + linea + pad +"║");
        }      
    }

// ======================================================    
// ======================================================    



// ====================================================== 
//                   SEZIONE OPZIONI
// ====================================================== 


    /**
     * Dispone le scritte del menu o i box dei form calcolando la disposizione ideale.
     * Gestisce in autonomia la suddivisione asimmetrica delle colonne e le etichette per l'input.
     * @param possibiliOpzioni Array contenente le voci testuali da visualizzare
     * @param posizione        Stringa descrittiva dell'orientamento richiesto
     * @param visualizzaNumeri Booleano che discrimina tra un menu numerato (true) o campi form (false)
     */
    public static void formattaTesto(String[] possibiliOpzioni, String posizione, boolean visualizzaNumeri){
        
        if(possibiliOpzioni == null || possibiliOpzioni.length == 0) return;
        if(visualizzaNumeri){

            // =========================================================
            // MENU NUMERATI (1 Colonna, allineati al centro)
            // =========================================================

            int lunghezzaMax = 0;
            String[] righeMenu = new String[possibiliOpzioni.length];

            for(int i = 0; i < possibiliOpzioni.length; i++){
                String testoMenu = "["+ (i+1) +"] " + possibiliOpzioni[i];
                
                // Se il testo è troppo lungo aggiunge i puntini
                if (testoMenu.length() > LARGHEZZA_MENU - 6) {
                    testoMenu = testoMenu.substring(0, LARGHEZZA_MENU - 10) + "...";
                }
                
                righeMenu[i] = testoMenu;
                if(righeMenu[i].length() > lunghezzaMax) lunghezzaMax = righeMenu[i].length();
            }

            int paddingSinistra = Math.max(0, (LARGHEZZA_MENU - lunghezzaMax) / 2);
            String spaziaturaSinstra = " ".repeat(paddingSinistra);

            for (String testoTmp : righeMenu) {
                int paddingDestra = Math.max(0, LARGHEZZA_MENU - spaziaturaSinstra.length() - testoTmp.length());
                System.out.println("║" + spaziaturaSinstra + testoTmp.toUpperCase() + " ".repeat(paddingDestra) + "║");
            }
        } else {
            // =========================================================
            // FORM DI INPUT (1 o 2 colonne in base al numero di campi)
            // =========================================================
            boolean dueColonne = possibiliOpzioni.length >= 8; 
            int boxWidth = LARGHEZZA_BOX_INPUT; // vale 38 caratteri
            int spazioCentrale = dueColonne ? 6 : 0;
            
            boolean primoCampo = true;

            // Il ciclo fa passi da 2 se siamo in due colonne, sennò di 1
            for (int i = 0; i < possibiliOpzioni.length; i += (dueColonne ? 2 : 1)) {
                String campo1 = possibiliOpzioni[i];
                String campo2 = (dueColonne && i + 1 < possibiliOpzioni.length) ? possibiliOpzioni[i+1] : null;

                // 1. GENERAZIONE DELLE ISTRUZIONI SOPRA I BOX
                String istr1 = ottieniIstruzione(campo1, primoCampo);
                primoCampo = false;
                String istr2 = campo2 != null ? ottieniIstruzione(campo2, false) : "";
                
                String rigaIstr1 = centraTesto(istr1, boxWidth);
                String rigaIstr2 = campo2 != null ? centraTesto(istr2, boxWidth) : (dueColonne ? " ".repeat(boxWidth) : "");
                
                // Unisco le stringhe delle istruzioni
                String rigaIstruzioneTotale = dueColonne ? (rigaIstr1 + " ".repeat(spazioCentrale) + rigaIstr2) : rigaIstr1;
                
                // Stampo la riga con il padding perfetto
                stampaRigaCentrataForm(rigaIstruzioneTotale);

                // 2. GENERAZIONE DELLA GRAFICA DEI BOX
                String[] box1 = generaBoxAvanzato(campo1);
                String[] box2 = campo2 != null ? generaBoxAvanzato(campo2) : (dueColonne ? generaBoxVuoto(boxWidth) : null);

                // Stampo le 3 righe che compongono il box (o i due box affiancati)
                for (int r = 0; r < 3; r++) {
                    String rigaBoxTotale = dueColonne ? (box1[r] + " ".repeat(spazioCentrale) + box2[r]) : box1[r];
                    stampaRigaCentrataForm(rigaBoxTotale);
                }
            }
        }    
    }

    /**
     * Metodo di supporto per l'allineamento dei blocchi form centrandoli perfettamente.
     * Impedisce ai bordi destri dell'interfaccia di subire disallineamenti o sfasamenti.
     * @param contenuto il testo complessivo dei box formati da stampare
     */
    private static void stampaRigaCentrataForm(String contenuto) {
        int spazioRimanente = LARGHEZZA_MENU - contenuto.length();
        int padSx = Math.max(0, spazioRimanente / 2);
        int padDx = Math.max(0, spazioRimanente - padSx);
        System.out.println("║" + " ".repeat(padSx) + contenuto + " ".repeat(padDx) + "║");
    }

    /**
     * Stampa a schermo una singola stringa testuale all'interno delle cornici verticali.
     * Ne calcola gli spazi in base alla posizione (es. "centro" o allineamento sinistro).
     * @param opzione          il testo da visualizzare
     * @param posizione        criterio di posizionamento ("centro" o "sinistra")
     * @param visualizzaNumeri parametro di controllo ausiliario
     */
    public static void formattaTesto(String opzione, String posizione, boolean visualizzaNumeri){
        if (opzione.length() > LARGHEZZA_MENU - 4) {
            opzione = opzione.substring(0, LARGHEZZA_MENU - 7) + "...";
        }

        int padding = (posizione.equals("centro")) ? Math.max(0, (LARGHEZZA_MENU - opzione.length())/2) : 5;
        String spaziaturaSinistra = " ".repeat(padding);

        int padDestroCalc = Math.max(0, LARGHEZZA_MENU - opzione.length() - spaziaturaSinistra.length());
        String spaziaturaDestra = " ".repeat(padDestroCalc);

        System.out.println("║"+ spaziaturaSinistra + opzione.toUpperCase() + spaziaturaDestra + "║");
    }

    /**
     * Genera un box decorativo rettangolare integrando la stringa dell'opzione richiesta 
     * nel bordo superiore mediante l'ausilio di caratteri speciali Unicode Box-Drawing.
     * @param opzione il nome della proprietà o campo richiesto (es. "USERNAME")
     * @return un array di 3 stringhe rappresentanti la scomposizione orizzontale del box grafico
     */
    public static String[] generaBoxInputUnicode(String opzione){
        String[] boxInput = new String[3];
        
        // SICUREZZA: Proteggo il calcolo della larghezza della riga orizzontale
        int lunghezzaLinea = Math.max(0, LARGHEZZA_BOX_INPUT - 6 - opzione.length());
        String lineaSuperioreDinamica = "─".repeat(lunghezzaLinea);
        
        boxInput[0]= "┌─ "+opzione.toUpperCase()+" "+lineaSuperioreDinamica+"┐";
        boxInput[1]= "│ >                                 │";
        boxInput[2]= "└───────────────────────────────────┘";
        
        return boxInput;
    }
    /**
     * Genera e compone la grafica di tre piccoli box adiacenti in linea per facilitare
     * l'inserimento separato dei parametri di una data (Giorno, Mese, Anno).
     * * @return un array di 3 stringhe configurate graficamente per l'acquisizione della data
     */
    public static String[] chiediData(){
        String[] dataInput = new String[3];

        dataInput[0] = "┌─ GIORNO ─┐ ┌─ MESE ──┐ ┌─ ANNO ───┐";
        dataInput[1] = "│ >        │ │ >       │ │ >        │";
        dataInput[2] = "└──────────┘ └─────────┘ └──────────┘";

        return dataInput;
    }

    /**
     * Renderizza la pagina in base allo stato in cui ci si trova.
     * Proietta una lista di elementi, che possono essere prenotazioni o proiezioni.
     * Un massimo di 10 elmeni per pagina.
     * Possibilità di navigare avanti e indietro.
     */
    public static void stampaListaPagina(){

        // Inizializzazione delle variabili
        List<?> listaPagina;
        int elementiPagina;
        int paginaCorrente;
        int totaleElementi;
        boolean esistenzaPaginaSuccessiva;
        String messaggioVuoto;
    
        // Controllo stato attuale, per capire quali dati leggere
        StatoMenu statoAttuale = cinemax.CineMax.stackRecord.peek();

        if(statoAttuale == StatoMenu.MIE_PRENOTAZIONI || statoAttuale == StatoMenu.CERCA_PRENOTAZIONE){
            // LETTURA DA PRENOTAZIONICONTROLLER
            listaPagina = cinemax.controller.PrenotazioniController.prenotazioniPaginaTmp;
            elementiPagina = cinemax.controller.PrenotazioniController.ELEMENTI_PAGINA;
            paginaCorrente = cinemax.controller.PrenotazioniController.paginaCorrente;
            totaleElementi = cinemax.controller.PrenotazioniController.prenotazioniTrovate.size();
            esistenzaPaginaSuccessiva = cinemax.controller.PrenotazioniController.esistenzaPaginaSuccessiva;
            messaggioVuoto = "NESSUNA PRENOTAZIOE TROVATA";
        } else{
            // LETTURA DA FILMCONTROLLER
            listaPagina = cinemax.controller.FilmController.filmPaginaTmp;
            elementiPagina = cinemax.controller.FilmController.ELEMENTI_PAGINA;
            paginaCorrente = cinemax.controller.FilmController.paginaCorrente;
            totaleElementi = cinemax.controller.FilmController.proiezioniTrovate.size();
            esistenzaPaginaSuccessiva = cinemax.controller.FilmController.esistenzaPaginaSuccessiva;
            messaggioVuoto = "NESSUNA PROIEZIONE TROVATA";
        }


        if(listaPagina.isEmpty()){
            formattaTesto(messaggioVuoto, "centro", true);
            formattaTesto("PREMI INVIO PER TORNARE INDIETRO", "centro", true);
            return;
        }

        //INFO PAGINA
        int totalePagine = (totaleElementi + elementiPagina - 1) / elementiPagina;
        String infoPagina = "PAGINA " + (paginaCorrente + 1) + " DI " + totalePagine;
        formattaTesto(infoPagina, "sinistra", true);

        // SEZIONE GRAFICA PROIEZIONI
        System.out.println(bordoMezzo);
        System.out.println(rigaVuota);

        int indiceSceltaMenu = 1;
        // Utilizzo di Object in modo che accetti sia prenotazioni che proiezioni
        for (Object elemento : listaPagina) {
            String rigaFilm = "["+ indiceSceltaMenu++ +"] " + elemento.toString(); 
            formattaTesto(rigaFilm, "sinistra", true);
        }

        System.out.println(rigaVuota);
        System.out.println(bordoMezzo);
        System.out.println(rigaVuota);

        
        // PAGINA SUCCESSIVA
        if(esistenzaPaginaSuccessiva)
            formattaTesto("[N] PAGINA SUCCESSIVA", "centro", true);
        // PAGINA PRECEDENTE
        if(paginaCorrente>0)
            formattaTesto("[B] PAGINA PRECEDENTE", "centro", true);
        // ANNULLA
        formattaTesto("[C] ANNULLA / ESCI", "centro", true);

    }
    /**
     * Renderizza il prospetto informativo completo legato a una specifica proiezione.
     * Allinea i metadati descrittivi del film (regista, durata, prezzo, posti) ed espone
     * le scelte operative permesse in base ai privilegi del ruolo correntemente attivo.
     * * @param p l'istanza della {@link Proiezione} da esaminare
     */
    public static void gestisciInformazioniFilm(Proiezione p){

        formattaTesto("TITOLO: " + p.getTitolo(), "centro", true);

        System.out.println(rigaVuota);
        System.out.println(bordoMezzo);
        System.out.println(rigaVuota);

        String formatoColonne = "║  %-56s │ %-69s║"; // '-', testo a sinistra. 's' stringa, 'numeoro' larghezza fissa colonna

        int postiLiberi = p.getPostiLiberi();

        System.out.println(String.format(formatoColonne, "REGISTA: " + p.getRegista(), "DURATA: " + p.getDurata() + " MINUTI"));
        System.out.println(String.format(formatoColonne, "GENERE: " + p.getGenere(), "ETA' CONSIGLIATA: " + p.getEtaMin() + "+"));
        System.out.println(String.format(formatoColonne, "ANNO DI PRODUZIONE: " + p.getAnno(), "PREZZO BIGLIETTO: " + String.format(java.util.Locale.US, "%.2f", p.getPrezzo()) + " EURO"));
        System.out.println(String.format(formatoColonne, "DATA PROIEZIONE: " + p.getData(), "POSTI IN SALA: " + p.getPostiSala() + "  |  POSTI LIBERI: " + postiLiberi));
        System.out.println(String.format(formatoColonne, "ORARIO: " + p.getOra(), ""));

        System.out.println(rigaVuota);
        System.out.println(rigaVuota);

        if(CineMax.ruolo.equals(Utente.Ruolo.CLIENTE_REGISTRATO)){
            formattaTesto("[1] PRENOTA BIGLIETTI", "centro", true);
            formattaTesto("[2] TORNA INDIETRO", "centro", true);
        }else if(CineMax.ruolo.equals(Utente.Ruolo.PROIEZIONISTA)){
            formattaTesto("[1] ELIMINA PROIEZIONE", "centro", true);
            formattaTesto("[2] MODIFICA PROIEZIONE", "centro", true);
            formattaTesto("[3] TORNA INDIETRO", "centro", true);
        } else if(CineMax.ruolo.equals(Utente.Ruolo.BIGLIETTAIO)){
            formattaTesto("[1] VENDITA DIRETTA BIGLIETTI", "centro", true);
            formattaTesto("[2] TORNA INDIETRO", "centro", true);
        } else
            formattaTesto("INVIO PER TORNARE INDIETRO", "centro", true);
    }

    /**
     * Renderizza lo specchietto dettagliato relativo a una singola prenotazione.
     * Estrae e formatta il codice alfanumerico univoco, l'intestatario, i biglietti e il costo totale.
     * @param pre l'istanza di {@link cinemax.model.Prenotazione} da visualizzare
     */
    public static void gestisciDettaglioPrenotazione(cinemax.model.Prenotazione pre){
        if(pre == null) {
            formattaTesto("ERRORE: NESSUNA PRENOTAZIONE SELEZIONATA", "centro", true);
            return;
        }

        formattaTesto("DETTAGLIO PRENOTAZIONE: " + pre.getCodiceUnivoco(), "centro", true);
        System.out.println(rigaVuota);
        System.out.println(bordoMezzo);
        System.out.println(rigaVuota);

        String fmt = "║  %-56s │ %-69s║";
        System.out.println(String.format(fmt, "FILM: " + pre.getTitoloFilm(), "DATA: " + pre.getProiezione().getData()));
        System.out.println(String.format(fmt, "UTENTE: " + pre.getUsernameCliente(), "ORARIO: " + pre.getProiezione().getOra()));
        System.out.println(String.format(fmt, "BIGLIETTI: " + pre.getNumeroBiglietti(), "IMPORTO TOTALE: " + String.format(java.util.Locale.US, "%.2f", pre.getCostoTotale()) + " EURO"));

        System.out.println(rigaVuota);
        System.out.println(rigaVuota);

        formattaTesto("[1] RIMUOVI PRENOTAZIONE", "centro", true);
        formattaTesto("[2] TORNA INDIETRO", "centro", true);
    }
    
    // ======================================================
    // HELPER PER I FORM (Supporto a 2 colonne)
    // ======================================================

    /**
     * Determina la stringa di suggerimento informativo o vincolo testuale da stampare sopra i box form.
     * *@param campo       il nome del parametro richiesto
     * @param primoCampo  booleano che segnala se si tratta del primo campo assoluto del form
     * @return String la stringa di istruzioni specifiche da proiettare
     */
    private static String ottieniIstruzione(String campo, boolean primoCampo){
        if(primoCampo) return "(Digita :q per annullare)";
        String c = campo.toUpperCase();
        if(c.startsWith("DATAINIZIO")&& CineMax.stackRecord.peek()==StatoMenu.REGISTRAZIONE)
            return "Data di nascita (facoltativo)";
        else if(c.startsWith("DATAINIZIO"))
            return "(Inserisci la data inziiale)";
        if(c.startsWith("DATAFINE")) return "(Inserisci data finale)";
        if(c.startsWith("DOMICILIO")) return "(Campo facoltativo)";
        if(c.startsWith("DATA DI NASCITA")) return "(Campo facoltativo)";
        if(c.startsWith("ORARIO")) return "(Es. 20:30)";
        return "";
    }

    /**
     * Centra un testo inserendo spazi a sinistra e a destra per pareggiare la larghezza di colonna impostata.
     * @param testo la stringa da allineare
     * @param width il limite di caratteri orizzontali disponibili della colonna
     * @return String la stringa centrata e completata dal padding
     */
    private static String centraTesto(String testo, int width){
        if (testo.length() >= width) return testo.substring(0, width);
        int padSx = (width - testo.length()) / 2;
        int padDx = width - testo.length() - padSx;
        return " ".repeat(padSx) + testo + " ".repeat(padDx);
    }

    /**
     * Risolve quale tipologia di box stampare discriminando l'inserimento generico o i tripli box data.
     * @param campo l'etichetta del campo da esaminare
     * @return un array di 3 stringhe rappresentanti le righe del box grafico appropriato
     */
    private static String[] generaBoxAvanzato(String campo) {
        if (campo.toUpperCase().startsWith("DATAINIZIO") || campo.toUpperCase().startsWith("DATAFINE")) {
            return chiediData();
        }
        return generaBoxInputUnicode(campo);
    }
    /**
     * Genera righe interamente vuote utilizzate per bilanciare visivamente i form asimmetrici a due colonne.
     * @param width il quantitativo di spazi vuoti orizzontali richiesti
     * @return un array di stringhe vuote spaziate
     */
    private static String[] generaBoxVuoto(int width) {
        String vuoto = " ".repeat(width);
        return new String[]{vuoto, vuoto, vuoto};
    }
// ======================================================
// ======================================================

}
