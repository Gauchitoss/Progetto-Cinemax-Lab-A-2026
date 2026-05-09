package cinemax;
/**
 * 
 */

public class CinemaxUI {

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
     * 
     * @param logo
     * @param opzioni
     */
    public static void renderizzaMenu(String logo,String[] opzioni){
        clearConsole();
        System.out.println("╔════════════════════════════════════════════════════════════════════════════════╗"); //82 caratteri di larghezza
        System.out.println("║                                                                                ║");
        sceltaLogo(logo);
        System.out.println("║                                                                                ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════════════════╣");
        System.out.println("║                                                                                ║");
        System.out.println("║                                                                                ║");
        for (String opzione : opzioni) {
            formattaOpzione(opzione);
        }
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
        String pad= "";

        for(int i = 0; i < padding; i++)
            pad += " ";

        for (String linea : logo) {
            System.out.println("║"+ pad + ((80-lunghezza)%2 !=0? " ":"" ) + linea+ pad +"║");
        }      

        return;
    }

// ======================================================    



// ====================================================== 
//                   SEZIONE OPZIONI
// ====================================================== 
    public static void formattaOpzione(String opzione){

    }





    public static void main(String[] args) {

        renderizzaMenu("cinemax", args);
        renderizzaMenu("film", args);

    }
}
