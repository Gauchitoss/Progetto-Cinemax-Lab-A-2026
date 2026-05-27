package cinemax;

import java.util.List;

import cinemax.MenuMangaer.StatoMenu;
import cinemax.model.Proiezione;

/**
 * Classe che gestisce l'interfaccia testuale (TUI - Terminal User Interface).
 * Si occupa della formattazione grafica, della stampa dei loghi e della 
 * renderizzazione dei menu in base allo stato corrente dell'applicazione.
 * @author Modena Matteo (Matricola: 765099 ) - VA
 * @author Baroncelli Luca (Matricola: 765099 ) - VA
 * @author Bin Alessio (Matricola: 762387 ) - VA
 */

public class CinemaxTUI {

    private static final int LARGHEZZA_MENU = 130;
    private static final int LARGHEZZA_BOX_INPUT = 38;
    
    private static String bordoSuperiore = "╔"+"═".repeat(LARGHEZZA_MENU)+"╗";
    private static String rigaVuota =      "║"+" ".repeat(LARGHEZZA_MENU)+"║";
    private static String bordoMezzo =     "╠"+"═".repeat(LARGHEZZA_MENU)+"╣";
    private static String bordoInferiore = "╚"+"═".repeat(LARGHEZZA_MENU)+"╝";

// ======================================================
//          SEZIONE CREAZIONE INTERFACCIA
// ======================================================

    /**
     * Pulisce la console
     */
    public static void clearConsole() {
        // Forza la finestra a 45 righe di altezza e 135 di larghezza
        System.out.print("\033[8;45;135t");

        System.out.println("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * Renderizza l'intera interfaccia a schermo in base allo stato corrente del programma
     * 
     * @param statoMenu Oggetto enum che contiene tutte le informazioni per costruire il menu
     * (logo da mostrare, opzioni disponibili, allinemamento, stile di lista)
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
        }else if(statoMenu == StatoMenu.VISUALIZZA_PROGRAMMAZAIONE || statoMenu == StatoMenu.MIE_PRENOTAZIONI){
            stampaListaPagina();
        }else if(statoMenu == StatoMenu.MENU_INFO_FILM){
            gestisciInformazioniFilm(cinemax.controller.FilmController.filmSelezionatoTmp);
        }else
            formattaTesto(statoMenu.getOpzioni(), statoMenu.getPosizione(), statoMenu.getVisualizzaNumeri());

        System.out.println(rigaVuota);
        System.out.println(rigaVuota);
        System.out.println(bordoInferiore);
        if(CineMax.stackRecord.peek().getVisualizzaNumeri())System.out.print("\nSELEZIONA UN'OPZIONE: ");

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


    public static void formattaIntestazione(String logo){

        if(logo.equals("cinemax"))  {formattaLogo(logoCinemax); return;}
        if(logo.equals("film"))     {formattaLogo(logoFilm); return;}
        if(logo.equals("error"))    {formattaLogo(logoErrore); return;}

        formattaTesto(logo, "centro", true);
    }

    /**
     * Centra e stampa un logo testuale all'interno del menu.
     * Gestisce automaticamente il calcolo delgi spazi (padding) da lasciare, al fine
     * di compensare la lunghezza.
     * 
     * @param logo array contenente le linee del logo da stampare,
     */
    public static void formattaLogo(String[] righelogo){
        int lunghezzaRigaLogo = righelogo[1].length();
        int padding = (LARGHEZZA_MENU - lunghezzaRigaLogo)/2;
        String pad= " ".repeat(padding);

        for (String linea : righelogo) {
            System.out.println("║"+ pad + ((LARGHEZZA_MENU-lunghezzaRigaLogo)%2 !=0? " ":"" ) + linea+ pad +"║");
        }      

        return;
    }

// ======================================================    
// ======================================================    



// ====================================================== 
//                   SEZIONE OPZIONI
// ====================================================== 


    /**
     * Costruisce l'intera riga del menu completa di bordi `║`, calcolando gli spazi mancanti a destra.
     * 
     * @param opzione Stringa contenente il testo finale dell'opzione da inserire
     * @param padding Int, numero di caselle vuote lasciare per allineare il tutto
     * @param visualizzaNumeri Booleano che indica se è un menu a scelta (true) o un form di input (false)
     * @return Stringa formattata (con eventuali \n), pronta per essere stampata a schermo
     */
    public static void formattaTesto(String[] possibiliOpzioni, String posizione, boolean visualizzaNumeri){
        
        if(possibiliOpzioni == null || possibiliOpzioni.length == 0) return;

        // idea quello di creare una singola riga per i meni in modo poi di appciccicarla e averr gia una lunghezza
        if(visualizzaNumeri){
            int lunghezzaMax = 0;
            String[] righeMenu = new String[possibiliOpzioni.length];

            for(int i = 0; i < possibiliOpzioni.length; i++){
                righeMenu[i] = "["+ (i+1) +"] " + possibiliOpzioni[i];
                if(righeMenu.length > lunghezzaMax) lunghezzaMax = righeMenu[i].length();
            }
            String spaziaturaSinstra = " ".repeat((LARGHEZZA_MENU-lunghezzaMax)/2);

            for (String testoTmp : righeMenu) {
                System.out.println("║" + spaziaturaSinstra + testoTmp.toUpperCase() + " ".repeat(LARGHEZZA_MENU-spaziaturaSinstra.length()-testoTmp.length()) + "║");
            }
        }else{
            String[] boxInput;
            String padding = " ".repeat((LARGHEZZA_MENU-LARGHEZZA_BOX_INPUT)/2);

            for (String campo : possibiliOpzioni) {
                if(campo.toUpperCase().equals("DATAINIZIO")||
                   campo.toUpperCase().equals("DATAFINE")){
                    boxInput = chiediData();
                }else
                    boxInput = generaBoxInputUnicode(campo);

                for (String rigaInput : boxInput) {
                    System.out.println("║" + padding + rigaInput + padding + " ║");
                }
            }

        }
    }
    public static void formattaTesto(String opzione, String posizione, boolean visualizzaNumeri){
        int padding = (posizione.equals("centro"))? (LARGHEZZA_MENU - opzione.length())/2: 5;
        String spaziaturaSinistra = " ".repeat(padding);
        String spaziaturaDestra = "";

        spaziaturaDestra = " ".repeat(LARGHEZZA_MENU-opzione.length()-spaziaturaSinistra.length());
        System.out.println("║"+ spaziaturaSinistra + opzione.toUpperCase() + spaziaturaDestra + "║");
        
    }

    /**
     * Genera un box decorativo in caratteri Unicode, per l'inserimento dei dati da parte dell'utente
     * 
     * @param opzione Nome del dato richiesto
     * @return Array di 3 stringhe rappresentanti la riga superiore, centrale e inferiore.
     */
    public static String[] generaBoxInputUnicode(String opzione){
        String[] boxInput = new String[3];
        // Lunghezza totale del box (38) - i 6 caratteri fissi ("┌─ ", " ", "┐") - lunghezza della stringa.
        String lineaSuperioreDinamica = "─".repeat(LARGHEZZA_BOX_INPUT-6-opzione.length());
        boxInput[0]= "┌─ "+opzione.toUpperCase()+" "+lineaSuperioreDinamica+"┐";
        boxInput[1]= "│ >                                 │";
        boxInput[2]= "└───────────────────────────────────┘";
        
        return boxInput;
    }

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

    public static void gestisciInformazioniFilm(Proiezione p){

        formattaTesto("TITOLO: " + p.getTitolo(), "centro", true);

        System.out.println(rigaVuota);
        System.out.println(bordoMezzo);
        System.out.println(rigaVuota);

        String formatoColonne = "║  %-56s │ %-69s║";

        System.out.println(String.format(formatoColonne, "REGISTA: " + p.getRegista(), "DURATA: " + p.getDurata() + " MINUTI"));
        System.out.println(String.format(formatoColonne, "GENERE: " + p.getGenere(), "ETA' CONSIGLIATA: " + p.getEtaMin() + "+"));
        System.out.println(String.format(formatoColonne, "ANNO DI PRODUZIONE: " + p.getAnno(), "PREZZO BIGLIETTO: " + String.format(java.util.Locale.US, "%.2f", p.getPrezzo()) + " EURO"));
        System.out.println(String.format(formatoColonne, "DATA PROIEZIONE: " + p.getData(), "POSTI IN SALA: " + p.getPostiSala()));
        System.out.println(String.format(formatoColonne, "ORARIO: " + p.getOra(), ""));

        System.out.println(rigaVuota);
        System.out.println(rigaVuota);

        if(CineMax.ruolo.equals("cliente registrato")){
            formattaTesto("[1] PRENOTA BIGLIETTI", "centro", true);
            formattaTesto("[2] TORNA INDIETRO", "centro", true);
        }else if(CineMax.ruolo.equals("proiezionista")){
            formattaTesto("[1] ELIMINA PROIEZIONE", "centro", true);
            formattaTesto("[2] MODIFICA PROIEZIONE", "centro", true);
            formattaTesto("[3] TORNA INDIETRO", "centro", true);
        } else if(CineMax.ruolo.equals("bigliettaio")){
            formattaTesto("[1] PRENOTA BIGLIETTI", "centro", true);
            formattaTesto("[2] TORNA INDIETRO", "centro", true);
        } else
            formattaTesto("INVIO PER TORNARE INDIETRO", "centro", true);
    }

// ======================================================
// ======================================================

}
