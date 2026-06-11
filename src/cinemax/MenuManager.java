package cinemax;

import java.util.Scanner;
import cinemax.controller.AutenticazioniController;
import cinemax.controller.FilmController;
import cinemax.controller.PrenotazioniController;
import cinemax.util.GestorePrenotazione;
import cinemax.util.GestoreProiezione;

/**
 * Classe che gestisce la logica di navigazione e gli stati del sistema CineMax.
 * Modella l'intero comportamento logico del programma mediante una macchina a stati finiti (FSM),
 * mappando ogni schermata visibile in una costante di tipo enum e associandovi l'esecuzione della relativa logica.
 * @author Modena Matteo (Matricola: 765099) - VA
 * @author Baroncelli Luca (Matricola: 761582) - VA
 * @author Bin Alessio (Matricola: 762387) - VA
 */
public class MenuManager {
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

        // Schermata iniziale di benvenuto
        BENVENUTO(new String[]{"accedi", "entra come guest", "registrati", "esci"}, true, "cinemax", "centro"){
            @Override public StatoMenu[] prossimi() { return new StatoMenu[]{LOGIN, MENU_GUEST, REGISTRAZIONE};}
            @Override
            public void eseguiLogicaAssociata(){
                String scelta = input.nextLine();
                if(scelta.equals("4"))  System.exit(0);
                LogicaStatiManager.messaggioErroreCorrente = "Spiacenti, si è verificato un errore.";
                LogicaStatiManager.statoMenuSuccessivo(scelta);
            }
        },

        // Menu in caso di mancato accesso
        MENU_GUEST( new String[]{"cerca film", "accedi", "indietro"}, true, "cinemax", "centro"){
            @Override public StatoMenu[] prossimi() { return new StatoMenu[]{CERCA_FILM, LOGIN, BENVENUTO};}
            @Override public void eseguiLogicaAssociata(){LogicaStatiManager.statoMenuSuccessivo(input.nextLine());}
        },

        // ======================================================
        // 2. AUTENTICAZIONE
        // ======================================================

        // Grafica accesso 
        LOGIN(new String[]{"username", "password"}, false, "accesso", "centro"){
            @Override public StatoMenu[] prossimi() { return new StatoMenu[]{MENU_CLIENTI, MENU_PROIEZIONISTA, MENU_BIGLIETTAIO, BENVENUTO};}
            @Override 
            public void eseguiLogicaAssociata(){
                LogicaStatiManager.messaggioErroreCorrente = "Spiacenti, si è verificato un errore.";
                String[] datiFormTmp = new String[getOpzioni().length];
                if(!LogicaStatiManager.prendiDatiForm(datiFormTmp, getOpzioni())) { CineMax.stackRecord.pop(); return; };
                AutenticazioniController.gestisciLogin(datiFormTmp);
            }
        },

        // Grafica registrazione
        REGISTRAZIONE(new String[]{"nome", "cognome", "username", "password", "conferma password", "dataInizio", "domicilio"}, false, "registrazione", "centro"){
            @Override public StatoMenu[] prossimi() {return new StatoMenu[]{LOGIN, BENVENUTO};}
            @Override 
            public void eseguiLogicaAssociata(){
                LogicaStatiManager.messaggioErroreCorrente = "Spiacenti, si è verificato un errore.";
                String[] datiFormTmp = new String[getOpzioni().length+2];
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
                LogicaStatiManager.messaggioErroreCorrente = "Spiacenti, si è verificato un errore.";
                String scelta = input.nextLine();
                if(scelta.equals("2")) cinemax.controller.PrenotazioniController.gestisciMiePrenotazioni();
                else LogicaStatiManager.statoMenuSuccessivo(scelta);
            }
        },
        
        MENU_PROIEZIONISTA(new String[]{"inserisci film", "cerca e modifica poiezione", "logout"}, true, "sezione proezionisti", "centro"){
            @Override public StatoMenu[] prossimi() {return new StatoMenu[]{INSERISCI_PROIEZIONE,CERCA_FILM, BENVENUTO};}
            @Override public void eseguiLogicaAssociata(){
                LogicaStatiManager.messaggioErroreCorrente = "Spiacenti, si è verificato un errore.";
                LogicaStatiManager.statoMenuSuccessivo(input.nextLine());}
        },

        MENU_BIGLIETTAIO(new String[]{"proiezioni del giorno", "cerca prenotazione", "cerca proiezione", "logout"}, true, "sezione bigliettaio", "centro"){
            @Override public StatoMenu[] prossimi() { return new StatoMenu[]{VISUALIZZA_PROGRAMMAZAIONE, CERCA_PRENOTAZIONE, CERCA_FILM, BENVENUTO};}
            @Override public void eseguiLogicaAssociata(){
                LogicaStatiManager.messaggioErroreCorrente = "Spiacenti, si è verificato un errore.";
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
                FilmController.gestisciCercaFilmDaArray(datiFormTmp);
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
                try {
                    int numBiglietti = Integer.parseInt(datiFormTmp[0]);
                    Boolean successo = GestorePrenotazione.inserisciPrenotazione(
                        cinemax.controller.AutenticazioniController.utente,
                        cinemax.controller.FilmController.filmSelezionatoTmp,
                        numBiglietti);
                    CineMax.stackRecord.pop();
                    if (successo) {
                        LogicaStatiManager.messaggioConfermaCorrente = "Prenotazione completata con successo!";
                        CineMax.stackRecord.push(StatoMenu.STATO_CONFERMA);
                    }
                } catch(NumberFormatException e) {
                    CineMax.stackRecord.pop();
                    LogicaStatiManager.messaggioErroreCorrente = "Inserisci un numero valido di biglietti.";
                    CineMax.stackRecord.push(StatoMenu.STATO_ERRORE);
                } catch(IllegalArgumentException | IllegalStateException e) {
                    CineMax.stackRecord.pop();
                    LogicaStatiManager.messaggioErroreCorrente = e.getMessage();
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
        
        DETTAGLIO_PRENOTAZIONE(new String[]{"rimuovi prenotazione", "torna indietro"}, true, "dettaglio prenotazione", "centro") {
            @Override public StatoMenu[] prossimi() { return new StatoMenu[]{MIE_PRENOTAZIONI};}
            @Override public void eseguiLogicaAssociata(){
                PrenotazioniController.gestisciDettaglioPrenotazione(input.nextLine());
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

        CERCA_PRENOTAZIONE_RISULTATI(new String[]{}, true, "risultati prenotazioni", "sinistra") {
            @Override public StatoMenu[] prossimi() { return new StatoMenu[]{MENU_BIGLIETTAIO};}
            @Override public void eseguiLogicaAssociata(){
                cinemax.controller.PrenotazioniController.gestisciVisualizzaPrenotazione(input.nextLine(), this);
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

        INSERISCI_PROIEZIONE(new String[]{"titolo","genere","regista","anno","durata minuti","eta minima", "costo", "posti sala", "dataInizio", "orario"}, false, "inserisci proiezione", "centro") {
            @Override public StatoMenu[] prossimi() { return new StatoMenu[]{MENU_PROIEZIONISTA};}
            @Override public void eseguiLogicaAssociata(){
                String[] datiFormTmp = new String[12];
                if(!LogicaStatiManager.prendiDatiForm(datiFormTmp, getOpzioni())) { CineMax.stackRecord.pop(); return; };
                FilmController.gestisciInserimentoProiezione(datiFormTmp);
            }
        },
    
        GESTISCI_PROIEZIONE(new String[]{"titolo","genere","regista","anno","durata minuti","eta minima", "costo", "posti sala", "dataInizio", "orario"}, false, "modifica proiezione", "centro") {
            @Override public StatoMenu[] prossimi() { return new StatoMenu[]{MENU_PROIEZIONISTA};}
            @Override public void eseguiLogicaAssociata(){
                String[] datiFormTmp = new String[12];
                if(!LogicaStatiManager.prendiDatiForm(datiFormTmp, getOpzioni())) { CineMax.stackRecord.pop(); return; };
                GestoreProiezione.modificaProiezione(FilmController.filmSelezionatoTmp, datiFormTmp);}
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
                LogicaStatiManager.messaggioErroreCorrente = "Spiacenti, si è verificato un errore.";
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