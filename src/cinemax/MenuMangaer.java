package cinemax;
/**
 * Classe che gestisce la logica di navigazione e gli stati del sistema CineMax.
 * Utilizza un'enumerazione per definire le fasi dell'applicazione e le relative opzioni.
 *
 * @author Modena Matteo (Matricola: 765099 ) - VA
 * @author Baroncelli Luca (Matricola: 765099 ) - VA
 * @author Bin Alessio (Matricola: 762387 ) - VA
*/

public class MenuMangaer {
    
    /**
     * Rappresenta gli stati possibili del menu.
     * Ogni stato ha associato una serie di campi essenziali per il rendere e per l'avanzamento del programma
     */
    public enum StatoMenu{
        BENVENUTO(
            new String[]{"accedi", "entra come guest", "registrati", "esci"},
            true,
            "cinemax",
            "centro"
        ),
        LOGIN(
            new String[]{"username","password","annulla"},
            false,
            "custom",
            "sinistra"
        ),
        REGISTRAZIONE(
            new String[]{"nome","cognome","username","password","conferma password","data di nascita", "domicilio*", "codice ruolo*", "Annulla"},
            false,
            "custom",
            "sinistra"
        ),

        MENU_GUEST(
            new String[]{"visualizza programmazione", "cerca film", "indietro"},
            true,
            "custom",
            "centro"
        ),
        MENU_CLIENTI(
            new String[]{"visualizza programmazione", "cerca film", "prenota posti", "mie prenotazioni", "logout"},
            true,
            "custom",
            "centro"
        ),
        MENU_PROEZIONISTA(
            new String[]{"inserisci film", "rimuovi Film", "gestisci orari", "logout"},
            true,
            "custom",
            "centro"
        ),
        MENU_BIGLIETTAIO(
            new String[]{"visualizza programmazione","cerca prenotazione", "vendita diretta", "stato sala", "logout"},
            true,
            "custom",
            "centro"
        );

// ======================================================
//                      CAMPI
// ======================================================

        private final String[] opzioni;
        private final boolean visualizzaNumeri;
        private final String nomeLogo;
        private final String posizione;

// ======================================================
//                   COSTRUTTORE
// ======================================================

        StatoMenu(String[] opzioni, boolean visualizzaNumeri, String nomeLogo,String posizione){
            this.opzioni = opzioni;
            this.visualizzaNumeri = visualizzaNumeri;
            this.nomeLogo = nomeLogo;
            this.posizione = posizione;

        }

// ======================================================
//                   GETTER
//======================================================
        public String[] getOpzioni(){
            return opzioni;
        }
        public boolean getVisualizzaNumeri(){
            return visualizzaNumeri;
        }
        public String getNomeLogo(){
            return nomeLogo;
        }    
        public String getPosizione(){
            return posizione;
        }
    }

// ======================================================
// ======================================================

}
