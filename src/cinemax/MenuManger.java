package cinemax;
/**
 * Classe che gestisce la logica di navigazione e gli stati del sistema CineMax.
 * Utilizza un'enumerazione per definire le fasi dell'applicazione e le relative opzioni.
 *
 * @author Modena Matteo (Matricola: 765099 ) - VA
 * @author Baroncelli Luca (Matricola: ) - VA
 * @author Bin Alessio (Matricola: ) - VA
*/

public class MenuManger {
    
    /**
     * Rappresenta gli stati possibili del menu.
     * Ogni stato ha associato un array di stringhe che rappresenta le opzioni disponibili.
     */
    public enum StatoMenu{
        BENVENUTO(new String[]{"Accedi", "Entra come Guest", "Registrati", "Esci"}),
        LOGIN(new String[]{"Inserisci credenziali", "Annulla"}),
        REGISTRAZIONE(new String[]{"Crea Account", "Annulla"}),

        MENU_GUEST(new String[]{"Visualizza programmazione", "Cerca Film", "Indietro"}),
        MENU_CLIENTI(new String[]{"Visualizza programmazione", "Cerca Film", "Prenota posti", "Mie Prenotazioni", "Logout"}),
        MENU_PROEZIONISTA(new String[]{"Inserisci Film", "Rimuovi Film", "Gestisci orari", "Logout"}),
        MENU_BIGLIETTAIO(new String[]{"Visualizza programmazione","Cerca prenotazione", "Vendita diretta", "Stato sala", "Logout"});

        private final String[] opzioni;

        StatoMenu(String[] opzioni){
            this.opzioni = opzioni;
        }

        public String[] getOpzioni(){
            return opzioni;
        }
    }

}
