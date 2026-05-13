package cinemax;

import cinemax.MenuMangaer.StatoMenu;

/**
 * Classe che gestisce l'interfaccia testuale (TUI - Terminal User Interface).
 * Si occupa della formattazione grafica, della stampa dei loghi e della 
 * renderizzazione dei menu in base allo stato corrente dell'applicazione.
 * @author Modena Matteo (Matricola: 765099 ) - VA
 * @author Baroncelli Luca (Matricola: 765099 ) - VA
 * @author Bin Alessio (Matricola: 762387 ) - VA
 */

public class CinemaxTUI {

    private static final int LARGHEZZA_MENU = 80;
    private static final int LARGHEZZA_BOX_INPUT = 38;

// ======================================================
//          SEZIONE CREAZIONE INTERFACCIA
// ======================================================

    /**
     * Pulisce la console
     */
    public static void clearConsole() {
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

        // Lunghezza totale del menu 82 caratteri
        System.out.println("╔════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                                                ║");
        sceltaLogo(statoMenu.getNomeLogo());
        if(statoMenu.getNomeLogo().equals("custom"))
        System.err.println("║"+statoMenu.getNomeLogo().toUpperCase()+"║");  // ricalcolo righe
        System.out.println("║                                                                                ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════════════════╣");
        System.out.println("║                                                                                ║");
        System.out.println("║                                                                                ║");
        formattaOpzione(statoMenu.getOpzioni(), statoMenu.getPosizione(), statoMenu.getVisualizzaNumeri());
        System.out.println("║                                                                                ║");
        System.out.println("║                                                                                ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════════════════╝");
        System.out.print("\nSELEZIONA UN'OPZIONE: ");
        
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

// ======================================================
// LOGICA DI STAMPA DEI LOGI
// ======================================================

    /**
     * Funzione preliminare che decide quale logo stampare.
     * 
     * @param logo Identificatore testuale del logo da visualizzare ("cinemax" o "film")
     */
    public static void sceltaLogo(String logo){
        if(logo.equals("cinemax"))
            formattaLogo(logoCinemax);

        if(logo.equals("film"))
            formattaLogo(logoFilm);
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
     * Stampa le opzioni del menu formattate in base allo stato in cui ci si trova.
     * 
     * @param opzioni Array di stringhe che contiene le voci del menu
     * @param posizione String che indica come allineare le opzioni
     * @param visualizzaNumeri boolenao, dice se stampare i numeri o meno, inoltre il mancato utilizzo di stampare
     * i nuemeri, detta che l'utente debba inserire qualche campo
     */
    public static void formattaOpzione(String[] opzioni, String posizione, boolean visualizzaNumeri){

        int indiceOpzione = 1;

        for (String tmpOpzione : opzioni) {
                String testoOpzionePronto = ((visualizzaNumeri)?"["+ indiceOpzione++ +"] ":"") + tmpOpzione;
                testoOpzionePronto = testoOpzionePronto.toUpperCase();

                System.out.println(creaRigaFormattata(testoOpzionePronto, ((visualizzaNumeri)? 30 : 5), visualizzaNumeri));
            }

    }

    /**
     * Costruisce l'intera riga del menu completa di bordi `║`, calcolando gli spazi mancanti a destra.
     * 
     * @param opzione Stringa contenente il testo finale dell'opzione da inserire
     * @param padding Int, numero di caselle vuote lasciare per allineare il tutto
     * @param visualizzaNumeri Booleano che indica se è un menu a scelta (true) o un form di input (false)
     * @return Stringa formattata (con eventuali \n), pronta per essere stampata a schermo
     */
    public static String creaRigaFormattata(String opzione, int padding, boolean visualizzaNumeri){
        String paddingSinistro = " ".repeat(padding);

        if(visualizzaNumeri){
            String paddingDestro = " ".repeat(LARGHEZZA_MENU-padding-opzione.length());
            return "║"+ paddingSinistro + opzione + paddingDestro + "║";
        }
        else{
            String[] righeBoxInput = generaBoxInputUnicode(opzione);
            String paddingDestroUnicoede = " ".repeat(LARGHEZZA_MENU-padding-righeBoxInput[0].length());
            return  "║"+ paddingSinistro + righeBoxInput[0] + paddingDestroUnicoede + "║\n"+
                    "║"+ paddingSinistro + righeBoxInput[1] + paddingDestroUnicoede + "║\n"+
                    "║"+ paddingSinistro + righeBoxInput[2] + paddingDestroUnicoede + "║";
        }
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
        boxInput[0]= "┌─ "+opzione+" "+lineaSuperioreDinamica+"┐";
        boxInput[1]= "│ >                                 │";
        boxInput[2]= "└───────────────────────────────────┘";

        return boxInput;
    }

// ======================================================
// ======================================================


}







