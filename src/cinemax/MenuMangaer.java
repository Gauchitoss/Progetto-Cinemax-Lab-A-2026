package cinemax;

import cinemax.MenuMangaer.StatoMenu;

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
    public enum StatoMenu {
        BENVENUTO(
            new String[]{"accedi", "entra come guest", "registrati", "esci"},
            true, "cinemax", "centro"
        ) {
            @Override
            public StatoMenu[] prossimi() {
                return new StatoMenu[]{LOGIN, MENU_GUEST, REGISTRAZIONE};
            }
        },

        LOGIN(
            new String[]{"username", "password"},
            false, "custom", "sinistra"
        ) {
            @Override
            public StatoMenu[] prossimi() {
                // Dopo il login, la logica di controllo deciderà se mandare a CLIENTE, PROIEZIONISTA o BIGLIETTAIO
                return new StatoMenu[]{MENU_CLIENTI, MENU_PROEZIONISTA, MENU_BIGLIETTAIO, BENVENUTO};
            }
        },

        REGISTRAZIONE(
            new String[]{"nome", "cognome", "username", "password", "conferma password", "data di nascita", "domicilio*"},
            false, "custom", "sinistra"
        ) {
            @Override
            public StatoMenu[] prossimi() {
                return new StatoMenu[]{LOGIN, BENVENUTO};
            }
        },

        MENU_GUEST(
            new String[]{"cerca film", "accedi", "indietro"},
            true, "custom", "centro"
        ) {
            @Override
            public StatoMenu[] prossimi() {
                return new StatoMenu[]{CERCA_FILM, LOGIN, BENVENUTO};
            }
        },

        MENU_CLIENTI(
            new String[]{"cerca film", "mie prenotazioni", "logout"},
            true, "custom", "centro"
        ) {
            @Override
            public StatoMenu[] prossimi() {
                return new StatoMenu[]{CERCA_FILM, MIE_PRENOTAZIONI, BENVENUTO};
            }
        },

        MENU_PROEZIONISTA(
            new String[]{"inserisci film", "rimuovi Film", "gestisci orari", "logout"},
            true, "custom", "centro"
        ) {
            @Override
            public StatoMenu[] prossimi() {
                return new StatoMenu[]{INSERISCI_PROEZIONE, RIMUOVI_PROEZIONE, GESTISCI_PROEZIONE, BENVENUTO};
            }
        },

        MENU_BIGLIETTAIO(
            new String[]{"visualizza programmazione", "cerca prenotazione", "vendita diretta", "stato sala", "logout"},
            true, "custom", "centro"
        ) {
            @Override
            public StatoMenu[] prossimi() {
                return new StatoMenu[]{VISUALIZZA_PROGRAMMAZAIONE, CERCA_PRENOTAZIONE, VENDITA_DIRETTA, STATO_SALA, BENVENUTO};
            }
        },

        CERCA_FILM(
            new String[]{"titolo", "data", "costo", "durata", "genere"},
            false, "custom", "sinistra"
        ) {
            @Override
            public StatoMenu[] prossimi() {
                return new StatoMenu[]{VISUALIZZA_PROGRAMMAZAIONE, VISUALIZZA_PROGRAMMAZAIONE, VISUALIZZA_PROGRAMMAZAIONE, VISUALIZZA_PROGRAMMAZAIONE, VISUALIZZA_PROGRAMMAZAIONE, null};
            }
        },

        VISUALIZZA_PROGRAMMAZAIONE(new String[]{}, true, "custom", "sinistra") {
            @Override public StatoMenu[] prossimi() { return new StatoMenu[]{PRENOTA_POSTI, BENVENUTO}; }
        },

        PRENOTA_POSTI(new String[]{}, true, "custom", "sinistra") {
            @Override public StatoMenu[] prossimi() { return new StatoMenu[]{MENU_CLIENTI}; }
        },

        MIE_PRENOTAZIONI(new String[]{}, true, "custom", "sinistra") {
            @Override public StatoMenu[] prossimi() { return new StatoMenu[]{MENU_CLIENTI}; }
        },

        INSERISCI_PROEZIONE(new String[]{}, true, "custom", "sinistra") {
            @Override public StatoMenu[] prossimi() { return new StatoMenu[]{MENU_PROEZIONISTA}; }
        },

        RIMUOVI_PROEZIONE(new String[]{}, true, "custom", "sinistra") {
            @Override public StatoMenu[] prossimi() { return new StatoMenu[]{MENU_PROEZIONISTA}; }
        },

        GESTISCI_PROEZIONE(new String[]{}, true, "custom", "sinistra") {
            @Override public StatoMenu[] prossimi() { return new StatoMenu[]{MENU_PROEZIONISTA}; }
        },

        CERCA_PRENOTAZIONE(new String[]{}, true, "custom", "sinistra") {
            @Override public StatoMenu[] prossimi() { return new StatoMenu[]{MENU_BIGLIETTAIO}; }
        },

        VENDITA_DIRETTA(new String[]{}, true, "custom", "sinistra") {
            @Override public StatoMenu[] prossimi() { return new StatoMenu[]{MENU_BIGLIETTAIO}; }
        },

        STATO_SALA(new String[]{}, true, "custom", "sinistra") {
            @Override public StatoMenu[] prossimi() { return new StatoMenu[]{MENU_BIGLIETTAIO}; }
    };


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


        public abstract StatoMenu[] prossimi();
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
