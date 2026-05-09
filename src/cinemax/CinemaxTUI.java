package cinemax;

/**
 * Classe che gestisce l'interfaccia testuale (TUI - Terminal User Interface).
 * Si occupa della formattazione grafica, della stampa dei loghi e della 
 * renderizzazione dei menu in base allo stato corrente dell'applicazione.
 * @author Modena Matteo (Matricola: 765099 ) - VA
 * @author Baroncelli Luca (Matricola: ) - VA
 * @author Bin Alessio (Matricola: ) - VA
 */

public class CinemaxTUI {

// ======================================================
//          SEZIONE CREAZIONE INTERFACCIA
// ======================================================

    /**
     * 
     */
    public static void clearConsole() {
        System.out.println("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * Renderizza la parte visibile a schermo in base allo stato del programma ti trovi.
     * 
     * @param logo String, contienene il nome del logo, che servirà per decidere quale logo mndare a schermo
     * @param opzioni Array di stringhe, contenente tutte le possibili opzioni di scelta che l'utente può fare
     * in base allo stato del programma in cui si trova.
     * @param posizione String, necessaria a decidere come allineare il testo
     */
    public static void renderizzaMenu(String logo,String[] opzioni, String posizione){
        clearConsole();
        System.out.println("╔════════════════════════════════════════════════════════════════════════════════╗"); //82 caratteri di larghezza
        System.out.println("║                                                                                ║");
        sceltaLogo(logo);
        System.out.println("║                                                                                ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════════════════╣");
        System.out.println("║                                                                                ║");
        System.out.println("║                                                                                ║");
        formattaOpzione(opzioni, posizione);
        System.out.println("║                                                                                ║");
        System.out.println("║                                                                                ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════════════════╝");
        System.out.print("\nSELEZIONA UN'OPZIONE PER INIZIARE: ");
        
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
     * @param logo stringa contenente il nome del logo da far stampare.
     */
    public static void sceltaLogo(String logo){

        if(logo.equals("cinemax"))
            formattaLogo(logoCinemax);

        if(logo.equals("film"))
            formattaLogo(logoFilm);

    }

    /**
     * Centra e stampa un logo testuale all'interno di una cornice di 80 caratteri.
     * Gestisce automaticamente il calcolo delgi spazi (padding) da lasciare, al fine
     * di compensare la lunghezza.
     * 
     * @param logo array contenente le linee del logo da stampare,
     */
    public static void formattaLogo(String[] logo){
        int lunghezza = logo[1].length();
        int padding = (80 - lunghezza)/2;
        String pad= " ".repeat(padding);

        for (String linea : logo) {
            System.out.println("║"+ pad + ((80-lunghezza)%2 !=0? " ":"" ) + linea+ pad +"║");
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
     */
    public static void formattaOpzione(String[] opzioni, String posizione){

        int cont = 1;

        for(String tmpOpzione : opzioni){
            
            String testoOpzioneNumerata = "["+ cont++ +"] " + tmpOpzione;

            switch (posizione.toLowerCase()) {
                case "centro":
                    System.out.println(formattaPosizione(testoOpzioneNumerata, 30));
                    break;
                case "sinistra":
                    System.out.println(formattaPosizione(testoOpzioneNumerata, 5));
                    break;
                default:
                    System.out.println(formattaPosizione(testoOpzioneNumerata, 5));
            }
        }
    }

    /**
     * Calcola lo spazio da lasciare a destra per allinare il contenuto.
     * 
     * @param opzione Stringa contentenete il nome dell'opzione
     * @param padding int, numero di caselle vuote lasciare per allineare il tutto
     * @return String, contenente la linea da stampare a schermo.
     */
    public static String formattaPosizione(String opzione, int padding){
        String paddingFisso = " ".repeat(padding);
        String paddingDestro = " ".repeat(80-padding-opzione.length());
        return "║"+ paddingFisso + opzione + paddingDestro + "║";
    }

// ======================================================
// ======================================================


}
