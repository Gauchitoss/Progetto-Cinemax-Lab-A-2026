package cinemax;

import java.util.Scanner;

import cinemax.controller.AutenticazioniController;
import cinemax.controller.FilmController;
import cinemax.util.GestorePrenotazione;
import cinemax.util.GestoreProiezione;

/**
 * Classe che gestisce la logica di navigazione e gli stati del sistema CineMax.
 * Utilizza il pattern State tramite un'enumerazione per definire le schermate 
 * dell'applicazione, le opzioni disponibili e le transizioni tra di esse.
 *
 * @author Modena Matteo (Matricola: 765099) - VA
 * @author Baroncelli Luca (Matricola: 761582) - VA
 * @author Bin Alessio (Matricola: 762387) - VA
 */
public class MenuMangaer {

    static Scanner input = new Scanner(System.in);

    /**
     * Rappresenta gli stati possibili (schermate) dell'interfaccia utente.
     * Ogni stato definisce le proprie opzioni testuali, l'interfaccia grafica (logo)
     * e la logica da eseguire al momento dell'interazione dell'utente.
     */
    public enum StatoMenu {

        // ======================================================
        // 1. AVVIO E NAVIGAZIONE DI BASE
        // ======================================================

        BENVENUTO(new String[]{"accedi", "entra come guest", "registrati", "esci"}, true, "cinemax", "centro"){
            @Override public StatoMenu[] prossimi() { return new StatoMenu[]{LOGIN, MENU_GUEST, REGISTRAZIONE};}
            @Override
            public void eseguiLogicaAssociata(){
                String scelta = input.nextLine();
                if(scelta.equals("4"))  System.exit(0);
                LogicaStatiManager.statoMenuSuccessivo(scelta);
            }
        },

        MENU_GUEST( new String[]{"cerca film", "accedi", "indietro"}, true, "cinemax", "centro"){
            @Override public StatoMenu[] prossimi() { return new StatoMenu[]{CERCA_FILM, LOGIN, BENVENUTO};}
            @Override public void eseguiLogicaAssociata(){LogicaStatiManager.statoMenuSuccessivo(input.nextLine());}
        },

        // ======================================================
        // 2. AUTENTICAZIONE
        // ======================================================

        LOGIN(new String[]{"username", "password"}, false, "accesso", "centro"){
            @Override public StatoMenu[] prossimi() { return new StatoMenu[]{MENU_CLIENTI, MENU_PROEZIONISTA, MENU_BIGLIETTAIO, BENVENUTO};}
            @Override 
            public void eseguiLogicaAssociata(){
                String[] datiFormTmp = new String[getOpzioni().length];
                if(!LogicaStatiManager.prendiDatiForm(datiFormTmp, getOpzioni())) { CineMax.stackRecord.pop(); return; };
                AutenticazioniController.gestisciLogin(datiFormTmp);
            }
        },

        REGISTRAZIONE(new String[]{"nome", "cognome", "username", "password", "conferma password", "data di nascita", "domicilio"}, false, "registrazione", "centro"){
            @Override public StatoMenu[] prossimi() {return new StatoMenu[]{LOGIN, BENVENUTO};}
            @Override 
            public void eseguiLogicaAssociata(){
                String[] datiFormTmp = new String[getOpzioni().length];
                if(!LogicaStatiManager.prendiDatiForm(datiFormTmp, getOpzioni())) { CineMax.stackRecord.pop(); return; };
                AutenticazioniController.gestisciRegistrazione(datiFormTmp);
            }
        },

        // ======================================================
        // 3. MENU PRINCIPALI PER RUOLO
        // ======================================================

        MENU_CLIENTI( new String[]{"cerca film", "mie prenotazioni", "logout"}, true, "cinemax", "centro"){
            @Override public StatoMenu[] prossimi() { return new StatoMenu[]{CERCA_FILM, MIE_PRENOTAZIONI, BENVENUTO};}
            @Override public void eseguiLogicaAssociata(){
                String scelta = input.nextLine();
                if(scelta.equals("2")) cinemax.controller.PrenotazioniController.gestisciMiePrenotazioni();
                else LogicaStatiManager.statoMenuSuccessivo(scelta);
            }
        },
        
        MENU_PROEZIONISTA(new String[]{"inserisci film", "cerca e modifica poiezione", "logout"}, true, "sezione proezionisti", "centro"){
            @Override public StatoMenu[] prossimi() {return new StatoMenu[]{INSERISCI_PROEZIONE,CERCA_FILM, BENVENUTO};}
            @Override public void eseguiLogicaAssociata(){LogicaStatiManager.statoMenuSuccessivo(input.nextLine());}
        },

        MENU_BIGLIETTAIO(new String[]{"proiezioni del giorno", "cerca prenotazione", "cerca proiezione", "logout"}, true, "sezione bigliettaio", "centro"){
            @Override public StatoMenu[] prossimi() { return new StatoMenu[]{VISUALIZZA_PROGRAMMAZAIONE, CERCA_PRENOTAZIONE, CERCA_FILM, BENVENUTO};}
            @Override public void eseguiLogicaAssociata(){
                String scelta = input.nextLine();
                if(scelta.equals("1")) FilmController.gestisciCercaFilm(GestoreProiezione.proiezioniDelGiorno());
                else LogicaStatiManager.statoMenuSuccessivo(scelta);}
        }, 
        
        // ======================================================
        // 4. RICERCA E VISUALIZZAZIONE FILM
        // ======================================================

        CERCA_FILM(new String[]{"titolo","dataInizio","dataFine", "costo", "genere"}, false, "cerca film", "centro"){
            @Override public StatoMenu[] prossimi() {return new StatoMenu[]{VISUALIZZA_PROGRAMMAZAIONE, null};}
            @Override
            public void eseguiLogicaAssociata(){
                String[] datiFormTmp = new String[9];
                if(!LogicaStatiManager.prendiDatiForm(datiFormTmp, getOpzioni())) { CineMax.stackRecord.pop(); return; };
                FilmController.gestisciCercaFlm(datiFormTmp);
            }
        },

        VISUALIZZA_PROGRAMMAZAIONE(new String[]{}, true, "film", "sinistra") {
            @Override public StatoMenu[] prossimi() { return new StatoMenu[]{};}
            @Override public void eseguiLogicaAssociata(){FilmController.gestisciVisualizzaProiezione(input.nextLine());}
        },

        MENU_INFO_FILM(new String[]{}, true, "informazioni aggiuntive", "centro"){
            @Override public StatoMenu[] prossimi() {return new StatoMenu[]{};}
            @Override public void eseguiLogicaAssociata(){FilmController.gestisciMenuInfoFilm(input.nextLine());}
        }, 
        
        // ======================================================
        // 5. GESTIONE PRENOTAZIONI E VENDITE
        // ======================================================

        PRENOTA_POSTI(new String[]{"numeoro biglietti"}, false, "prenota biglietti", "centro") {
            @Override public StatoMenu[] prossimi() { return new StatoMenu[]{MENU_CLIENTI};}
            @Override public void eseguiLogicaAssociata(){
                String[] datiFormTmp = new String[getOpzioni().length];
                if(!LogicaStatiManager.prendiDatiForm(datiFormTmp, getOpzioni())) { CineMax.stackRecord.pop(); return; }
                
                boolean successo = GestorePrenotazione.inserisciPrenotazione(cinemax.controller.AutenticazioniController.utente, cinemax.controller.FilmController.filmSelezionatoTmp, Integer.parseInt(datiFormTmp[0]));
                CineMax.stackRecord.pop();
                
                if (successo) {
                    LogicaStatiManager.messaggioConfermaCorrente = "Prenotazione completata con successo!";
                    CineMax.stackRecord.push(StatoMenu.STATO_CONFERMA);
                } else {
                    LogicaStatiManager.messaggioErroreCorrente = "Errore durante la prenotazione: posti insufficienti.";
                    CineMax.stackRecord.push(StatoMenu.STATO_ERRORE);
                }
            }
        },

        MIE_PRENOTAZIONI(new String[]{}, true, "le mie prenotazioni", "sinistra") {
            @Override public StatoMenu[] prossimi() { return new StatoMenu[]{MENU_CLIENTI};}
            @Override public void eseguiLogicaAssociata(){
                cinemax.controller.PrenotazioniController.gestisciVisualizzaPrenotazione(input.nextLine(), this);
            }
        },
        
        CERCA_PRENOTAZIONE(new String[]{"nome","cognome","titolo","dataInizio","dataFine","username","codice prenotazione"}, false, "cerca una prenotazione", "centro") {
            @Override public StatoMenu[] prossimi() { return new StatoMenu[]{MENU_BIGLIETTAIO};}
            @Override public void eseguiLogicaAssociata(){
                String[] datiFormTmp =new String[11];
                if(!LogicaStatiManager.prendiDatiForm(datiFormTmp, getOpzioni())) { CineMax.stackRecord.pop(); return; }
                cinemax.controller.PrenotazioniController.gestisciCercaPrenotazione(datiFormTmp);
            }
        },

        VENDITA_DIRETTA(new String[]{"numero biglietti"}, false, "vendita diretta", "centro") {
            @Override public StatoMenu[] prossimi() { return new StatoMenu[]{MENU_BIGLIETTAIO};}
            @Override public void eseguiLogicaAssociata(){
                String[] datiFormTmp = new String[getOpzioni().length];                
                if(!LogicaStatiManager.prendiDatiForm(datiFormTmp, getOpzioni())) { 
                    CineMax.stackRecord.pop(); 
                    return; 
                }            
                cinemax.controller.PrenotazioniController.gestisciVenditaDiretta(datiFormTmp);
            }
        },

        // ======================================================
        // 6. GESTIONE PROIEZIONI (PROIEZIONISTA)
        // ======================================================

        INSERISCI_PROEZIONE(new String[]{"titolo","genere","regista","anno","durata minuti","eta minima", "costo", "posti sala", "dataInizio", "orario"}, false, "inserisci proiezione", "centro") {
            @Override public StatoMenu[] prossimi() { return new StatoMenu[]{MENU_PROEZIONISTA};}
            @Override public void eseguiLogicaAssociata(){
                String[] datiFormTmp = new String[12];
                if(!LogicaStatiManager.prendiDatiForm(datiFormTmp, getOpzioni())) { CineMax.stackRecord.pop(); return; };
                FilmController.gestisciInserimentoProiezione(datiFormTmp);
            }
        },
    
        GESTISCI_PROEZIONE(new String[]{"titolo","genere","regista","anno","durata minuti","eta minima", "costo", "posti sala", "dataInizio", "orario"}, false, "modifica proiezione", "centro") {
            @Override public StatoMenu[] prossimi() { return new StatoMenu[]{MENU_PROEZIONISTA};}
            @Override public void eseguiLogicaAssociata(){
                String[] datiFormTmp = new String[12];
                if(!LogicaStatiManager.prendiDatiForm(datiFormTmp, getOpzioni())) { CineMax.stackRecord.pop(); return; };
                FilmController.gestisciModificaProiezione(cinemax.controller.FilmController.filmSelezionatoTmp, datiFormTmp);}
        },

        // ======================================================
        // 7. STATI DI FEEDBACK VISIVO (SISTEMA)
        // ======================================================

        STATO_ERRORE(new String[]{"premi invio per tornare indietro"}, true, "error","centro"){
            @Override public StatoMenu[] prossimi() {return new StatoMenu[]{null};}
            @Override
            public void eseguiLogicaAssociata(){
                input.nextLine();
                CineMax.stackRecord.pop();
            }
        },

        STATO_CONFERMA(new String[]{"premi invio per continuare"}, true, "conferma","centro"){
            @Override public StatoMenu[] prossimi() {return new StatoMenu[]{null};}
            @Override
            public void eseguiLogicaAssociata(){
                input.nextLine();
                CineMax.stackRecord.pop();
            }
        };


        // ======================================================
        // CAMPI, COSTRUTTORE E GETTER
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

        /**
         * Definisce gli stati accessibili a partire dallo stato corrente, 
         * mappati in base all'input numerico dell'utente.
         */
        public abstract StatoMenu[] prossimi();

        /**
         * Contiene la logica di business o l'acquisizione di input specifica per lo stato.
         */
        public void eseguiLogicaAssociata(){}

        public String[] getOpzioni()            { return opzioni; }
        public boolean  getVisualizzaNumeri()   { return visualizzaNumeri; }
        public String   getNomeLogo()           { return nomeLogo; }    
        public String   getPosizione()          { return posizione;}
    }
}