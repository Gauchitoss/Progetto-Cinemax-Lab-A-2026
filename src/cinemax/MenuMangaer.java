package cinemax;

/**
 * Classe che gestisce la logica di navigazione e gli stati del sistema CineMax.
 * Utilizza un'enumerazione per definire le fasi dell'applicazione e le relative opzioni.
 *
 * @author Modena Matteo (Matricola: 765099 ) - VA
 * @author Baroncelli Luca (Matricola: 765099 ) - VA
 * @author Bin Alessio (Matricola: 762387 ) - VA
*/
import java.util.Scanner;

import cinemax.controller.AutenticazioniController;
import cinemax.controller.FilmController;
import cinemax.util.GestoreProiezione;

public class MenuMangaer {
    static Scanner input = new Scanner(System.in);
    /**
     * Rappresenta gli stati possibili del menu.
     * Ogni stato ha associato una serie di campi essenziali per il rendere e per l'avanzamento del programma
     */
    public enum StatoMenu {

        // FATTO
        BENVENUTO(new String[]{"accedi", "entra come guest", "registrati", "esci"}, true, "cinemax", "centro"){
            @Override public StatoMenu[] prossimi() { return new StatoMenu[]{LOGIN, MENU_GUEST, REGISTRAZIONE};}
            @Override
            public void eseguiLogicaAssociata(){
                String scelta = input.nextLine();
                if(scelta.equals("4"))  System.exit(0);
                LogicaStatiManager.statoMenuSuccessivo(scelta);
            }
        },
        //FATTO
        MENU_GUEST( new String[]{"cerca film", "accedi", "indietro"}, true, "cinemax", "centro"){
            @Override public StatoMenu[] prossimi() { return new StatoMenu[]{CERCA_FILM, LOGIN, BENVENUTO};}
            @Override public void eseguiLogicaAssociata(){LogicaStatiManager.statoMenuSuccessivo(input.nextLine());}
        },
        //FATTO
        MENU_CLIENTI( new String[]{"cerca film", "mie prenotazioni", "logout"}, true, "cinemax", "centro"){
            @Override public StatoMenu[] prossimi() { return new StatoMenu[]{CERCA_FILM, MIE_PRENOTAZIONI, BENVENUTO};}
            @Override public void eseguiLogicaAssociata(){LogicaStatiManager.statoMenuSuccessivo(input.nextLine());}
        },
        // FATTO
        MENU_PROEZIONISTA(new String[]{"inserisci film", "cerca e modifica poiezione", "logout"}, true, "sezione proezionisti", "centro"){
            @Override public StatoMenu[] prossimi() {return new StatoMenu[]{INSERISCI_PROEZIONE,CERCA_FILM, BENVENUTO};}
            @Override public void eseguiLogicaAssociata(){LogicaStatiManager.statoMenuSuccessivo(input.nextLine());}
        },
        // FATTO
        MENU_BIGLIETTAIO(new String[]{"proiezioni del giorno", "cerca prenotazione", "cerca proiezione", "logout"}, true, "sezione bigliettaio", "centro"){
            @Override public StatoMenu[] prossimi() { return new StatoMenu[]{VISUALIZZA_PROGRAMMAZAIONE, CERCA_PRENOTAZIONE, CERCA_FILM, BENVENUTO};}
            @Override public void eseguiLogicaAssociata(){
                String scelta = input.nextLine();
                if(scelta.equals("1"))
                    FilmController.gestisciCercaFilm(GestoreProiezione.proiezioniDelGiorno());
                else
                    LogicaStatiManager.statoMenuSuccessivo(scelta);}
        }, 
        // FATTO
        LOGIN(new String[]{"username", "password"}, false, "accesso", "centro"){
            @Override public StatoMenu[] prossimi() { return new StatoMenu[]{MENU_CLIENTI, MENU_PROEZIONISTA, MENU_BIGLIETTAIO, BENVENUTO};}
            @Override 
            public void eseguiLogicaAssociata(){
                String[] datiFormTmp = new String[getOpzioni().length];
                if(!LogicaStatiManager.prendiDatiForm(datiFormTmp, getOpzioni())) return;
                AutenticazioniController.gestisciLogin(datiFormTmp);
            }
        },
        // FATTO
        REGISTRAZIONE(new String[]{"nome", "cognome", "username", "password", "conferma password", "data di nascita", "domicilio"}, false, "registrazione", "centro"){
            @Override public StatoMenu[] prossimi() {return new StatoMenu[]{LOGIN, BENVENUTO};}
            @Override 
            public void eseguiLogicaAssociata(){
                String[] datiFormTmp = new String[getOpzioni().length];
                if(!LogicaStatiManager.prendiDatiForm(datiFormTmp, getOpzioni())) return;
                AutenticazioniController.gestisciRegistrazione(datiFormTmp);
            }
        },
        // FATTO
        CERCA_FILM(new String[]{"titolo","dataInizio","dataFine", "costo", "genere"}, false, "cerca film", "centro"){
            @Override public StatoMenu[] prossimi() {return new StatoMenu[]{VISUALIZZA_PROGRAMMAZAIONE, null};}
            @Override
            public void eseguiLogicaAssociata(){
                String[] datiFormTmp = new String[9];
                if(!LogicaStatiManager.prendiDatiForm(datiFormTmp, getOpzioni())) return;
                FilmController.gestisciCercaFlm(datiFormTmp);
            }
        },
        // FATTO
        VISUALIZZA_PROGRAMMAZAIONE(new String[]{}, true, "film", "sinistra") {
            @Override public StatoMenu[] prossimi() { return new StatoMenu[]{};}
            @Override public void eseguiLogicaAssociata(){FilmController.gestisciVisualizzaProiezione(input.nextLine());}
        },
        // FATTO
        MENU_INFO_FILM(new String[]{}, true, "informazioni aggiuntive", "centro"){
            @Override public StatoMenu[] prossimi() {return new StatoMenu[]{};}
            @Override public void eseguiLogicaAssociata(){FilmController.gestisciMenuInfoFilm(input.nextLine());}
        }, 
        //
        PRENOTA_POSTI(new String[]{}, true, "custom", "sinistra") {
            @Override public StatoMenu[] prossimi() { return new StatoMenu[]{MENU_CLIENTI};}
            @Override public void eseguiLogicaAssociata(){LogicaStatiManager.statoMenuSuccessivo(input.nextLine());}
        },
        //
        MIE_PRENOTAZIONI(new String[]{}, true, "custom", "sinistra") {
            @Override public StatoMenu[] prossimi() { return new StatoMenu[]{MENU_CLIENTI};}
            @Override public void eseguiLogicaAssociata(){LogicaStatiManager.statoMenuSuccessivo(input.nextLine());}
        },
        // FATTO
        INSERISCI_PROEZIONE(new String[]{"titolo","genere","regista","anno","durata minuti","eta minima", "costo", "posti sala", "dataInizio", "orario"}, false, "inserisci proiezione", "centro") {
            @Override public StatoMenu[] prossimi() { return new StatoMenu[]{MENU_PROEZIONISTA};}
            @Override public void eseguiLogicaAssociata(){
                String[] datiFormTmp = new String[12];
                if(!LogicaStatiManager.prendiDatiForm(datiFormTmp, getOpzioni())) return;
                FilmController.gestisciInserimentoProiezione(datiFormTmp);
            }
        },
        //
        GESTISCI_PROEZIONE(new String[]{}, true, "custom", "sinistra") {
            @Override public StatoMenu[] prossimi() { return new StatoMenu[]{MENU_PROEZIONISTA};}
            @Override public void eseguiLogicaAssociata(){LogicaStatiManager.statoMenuSuccessivo(input.nextLine());}
        },
        //
        CERCA_PRENOTAZIONE(new String[]{"titolo film","dataInizio","nome","cognome","username"}, false, "cerca una prenotazione", "centro") {
            @Override public StatoMenu[] prossimi() { return new StatoMenu[]{MENU_BIGLIETTAIO};}
            @Override public void eseguiLogicaAssociata(){
                String[] datiFormTmp =new String[8];    // opzione dataInizio vale 3
                LogicaStatiManager.prendiDatiForm(datiFormTmp, getOpzioni());
            }
        },
        //
        VENDITA_DIRETTA(new String[]{}, true, "custom", "sinistra") {
            @Override public StatoMenu[] prossimi() { return new StatoMenu[]{MENU_BIGLIETTAIO};}
            @Override public void eseguiLogicaAssociata(){LogicaStatiManager.statoMenuSuccessivo(input.nextLine());}
        },
        //
        STATO_SALA(new String[]{}, true, "custom", "sinistra") {
            @Override public StatoMenu[] prossimi() { return new StatoMenu[]{MENU_BIGLIETTAIO};}
            @Override public void eseguiLogicaAssociata(){LogicaStatiManager.statoMenuSuccessivo(input.nextLine());}
        },
        // FATTO
        STATO_ERRORE(new String[]{"premi invio per tornare indietro"}, true, "error","centro"){
            @Override public StatoMenu[] prossimi() {return new StatoMenu[]{null};}
            @Override
            public void eseguiLogicaAssociata(){
                input.nextLine();
                CineMax.stackRecord.pop();
            }
        };


// ======================================================
//            CAMPI + COSTRUTTORE + GETTER
// ======================================================

        private final String[] opzioni;
        private final boolean visualizzaNumeri;
        private final String nomeLogo;
        private final String posizione;

        StatoMenu(String[] opzioni, boolean visualizzaNumeri, String nomeLogo, String posizione){
            this.opzioni = opzioni;
            this.visualizzaNumeri = visualizzaNumeri;
            this.nomeLogo = nomeLogo;
            this.posizione = posizione;
        }

        public abstract StatoMenu[] prossimi();

        public void eseguiLogicaAssociata(){}

        public String[] getOpzioni()            { return opzioni; }
        public boolean  getVisualizzaNumeri()   { return visualizzaNumeri; }
        public String   getNomeLogo()           { return nomeLogo; }    
        public String   getPosizione()          { return posizione;}
    }

// ======================================================
// ======================================================

}