package cinemax.controller;

import java.util.ArrayList;
import java.util.List;

import cinemax.CineMax;
import cinemax.CostantiForm.Campi;
import cinemax.MenuMangaer.StatoMenu;
import cinemax.model.Prenotazione;
import cinemax.model.Proiezione;
import cinemax.util.GestorePrenotazione;

/**
 * Controller per la gestione della logica di business relativa alle prenotazioni.
 * Gestisce l'interazione tra i dati del modello (Prenotazione) e l'interfaccia utente (TUI),
 * occupandosi di paginazione, visualizzazione, ricerca e navigazione.
 * * @author Modena Matteo (Matricola: 765099) - VA
 * @author Baroncelli Luca (Matricola: 761582) - VA
 * @author Bin Alessio (Matricola: 762387) - VA
 */

public class PrenotazioniController {
    
//======================================================
// VARIABILI GLOBALI PER LA STAMPA
// ======================================================
    public static List<Prenotazione> prenotazioniPaginaTmp = new ArrayList<>();
    public static boolean esistenzaPaginaSuccessiva;
    public static boolean esistenzaPrecedente;
    public static final int ELEMENTI_PAGINA = 10; 
    public static int paginaCorrente = 0;

    public static List<Proiezione> proiezioniTrovate = new ArrayList<>();
    public static List<Prenotazione> prenotazioniTrovate = new ArrayList<>();
    public static Prenotazione prenotazioneSelezionataTmp;

    /**
     * Carica e mostra tutte le prenotazioni effettuate dall'utente attualmente loggato.
     * Inizializza la paginazione e cambia lo stato del menu in MIE_PRENOTAZIONI.
     */
    public static void gestisciMiePrenotazioni(){
        try {
            String usernameUtente = AutenticazioniController.utente.getUsername();
            prenotazioniTrovate = GestorePrenotazione.getListaPrenotzioniUtente(usernameUtente);

            paginaCorrente = 0;
            aggiornaPrenotazioniPerPagina();

            CineMax.stackRecord.push(StatoMenu.MIE_PRENOTAZIONI);
        } catch (Exception e) {
            cinemax.LogicaStatiManager.messaggioErroreCorrente = "Errore durante il caricamento delle prenotazioni";
            CineMax.stackRecord.push(StatoMenu.STATO_ERRORE);
        }
    }

    /**
     * Aggiorna la lista temporanea {@code prenotazioniPaginaTmp} calcolando gli elementi 
     * da mostrare in base all'indice della {@code paginaCorrente}.
     * Calcola inoltre se i comandi 'N' (Next) e 'B' (Back) devono essere resi disponibili.
     */
    public static void aggiornaPrenotazioniPerPagina(){
        prenotazioniPaginaTmp.clear();
        esistenzaPaginaSuccessiva = false;
        esistenzaPrecedente = false;

        int indiceInizio = ELEMENTI_PAGINA * paginaCorrente;
        int indiceFine = indiceInizio + ELEMENTI_PAGINA;

        for(int i = indiceInizio; i < indiceFine && i <prenotazioniTrovate.size(); i++){
            prenotazioniPaginaTmp.add(prenotazioniTrovate.get(i));
        }

        if(indiceInizio + ELEMENTI_PAGINA < prenotazioniTrovate.size()) esistenzaPaginaSuccessiva = true;
        if(paginaCorrente > 0) esistenzaPrecedente = true;
    }

    /**
     * Gestisce l'input dell'utente durante la visualizzazione paginata delle prenotazioni.
     * Permette di navigare tra le pagine (N = Next, B = Back) o di annullare (C = Cancel).
     * * @param scelta       L'input digitato dall'utente.
     * @param statoAttuale Lo stato in cui ci si trova, necessario per "ricaricare" la pagina (pop e push).
     */
    public static void gestisciVisualizzaPrenotazione(String scelta, StatoMenu statoAttuale) {
        try {
            if(scelta == null || scelta.trim().isEmpty() || scelta.equalsIgnoreCase("C")) {
                CineMax.stackRecord.pop();
                return;
            }

            if(scelta.toUpperCase().equals("N") && esistenzaPaginaSuccessiva){
                paginaCorrente++;
                aggiornaPrenotazioniPerPagina();
                CineMax.stackRecord.pop();
                CineMax.stackRecord.push(statoAttuale);
                return;
            }
            if(scelta.toUpperCase().equals("B") && esistenzaPrecedente){
                paginaCorrente--;
                aggiornaPrenotazioniPerPagina();
                CineMax.stackRecord.pop();
                CineMax.stackRecord.push(statoAttuale);
                return;
            }
            
        } catch(Exception e) {
            // TODO EX
        }
    }

    /**
     * Metodo dedicato al Bigliettaio. Raccoglie i parametri inseriti tramite form testuale
     * e richiede a GestorePrenotazione di filtrare le prenotazioni nel sistema.
     * * @param datiFormTmp Array di stringhe contenente i dati grezzi digitati nel form.
     */
    public static void gestisciCercaPrenotazione(String[] datiFormTmp){
        try {
            // Parametri anagrafici e univoci
            String codice = datiFormTmp[Campi.PRENOTAZIONE_CODICE.i];
            String nome = datiFormTmp[Campi.PRENOTAZIONE_NOME.i];
            String cognome = datiFormTmp[Campi.PRENOTAZIONE_COGNOME.i];
            String titolo = datiFormTmp[Campi.PRENOTAZIONE_TITOLO.i];
            String username = datiFormTmp[Campi.PRENOTAZIONE_USERNAME.i];
            
            // Parsing dataInizio
            String g1 = datiFormTmp[Campi.PRENOTAZIONE_GIORNO1.i];
            String m1 = datiFormTmp[Campi.PRENOTAZIONE_MESE1.i];
            String a1 = datiFormTmp[Campi.PRENOTAZIONE_ANNO1.i];
            String dataInizio = null;
            if (g1 != null && m1 != null && a1 != null) {
                dataInizio = String.format("%02d-%02d-%04d", Integer.parseInt(g1), Integer.parseInt(m1), Integer.parseInt(a1));
            }
            
            // Parsing dataFine
            String g2 = datiFormTmp[Campi.PRENOTAZIONE_GIORNO2.i];
            String m2 = datiFormTmp[Campi.PRENOTAZIONE_MESE2.i];
            String a2 = datiFormTmp[Campi.PRENOTAZIONE_ANNO2.i];
            String dataFine = null;
            if (g2 != null && m2 != null && a2 != null) {
                dataFine = String.format("%02d-%02d-%04d", Integer.parseInt(g2), Integer.parseInt(m2), Integer.parseInt(a2));
            }


            prenotazioniTrovate = GestorePrenotazione.cercaPrenotazioni(codice, nome, cognome, titolo, dataInizio, dataFine, username); 
            
            paginaCorrente = 0;
            aggiornaPrenotazioniPerPagina();

            CineMax.stackRecord.pop();
            CineMax.stackRecord.push(StatoMenu.MIE_PRENOTAZIONI);
        } catch(NumberFormatException e){
            cinemax.LogicaStatiManager.messaggioErroreCorrente = "hai inserito parametri non numerici in un campo data";
            CineMax.stackRecord.push(StatoMenu.STATO_ERRORE);
        } catch (Exception e) {
            cinemax.LogicaStatiManager.messaggioErroreCorrente = "Errore durante la ricerca delle prenotazioni: " + e.getMessage();
            CineMax.stackRecord.push(StatoMenu.STATO_ERRORE);
        }
    }

    /**
     * Gestisce la vendita diretta dei biglietti al banco da parte del bigliettaio.
     * @param datiFormTmp Array contenente l'input del form (numero di biglietti).
     */
    public static void gestisciVenditaDiretta(String[] datiFormTmp){
        try {
            int numBiglietti = Integer.parseInt(datiFormTmp[0]);
            
            // Effettua la vendita registrandola a nome dell'account staff loggato
            boolean successo = GestorePrenotazione.inserisciPrenotazione(
                AutenticazioniController.utente, 
                FilmController.filmSelezionatoTmp, 
                numBiglietti
            );
            
            CineMax.stackRecord.pop(); // Rimuovo il form dalla cronologia
            
            if (successo) {
                cinemax.LogicaStatiManager.messaggioConfermaCorrente = "Vendita diretta completata con successo!";
                CineMax.stackRecord.push(StatoMenu.STATO_CONFERMA);
            } else {
                cinemax.LogicaStatiManager.messaggioErroreCorrente = "Errore: posti in sala insufficienti.";
                CineMax.stackRecord.push(StatoMenu.STATO_ERRORE);
            }
            
        } catch (NumberFormatException e) {
            CineMax.stackRecord.pop(); // Rimuovo il form
            cinemax.LogicaStatiManager.messaggioErroreCorrente = "Errore: Devi inserire un numero valido di biglietti.";
            CineMax.stackRecord.push(StatoMenu.STATO_ERRORE);
        } catch (Exception e) {
            CineMax.stackRecord.pop(); // Rimuovo il form
            cinemax.LogicaStatiManager.messaggioErroreCorrente = "Errore imprevisto durante la vendita: " + e.getMessage();
            CineMax.stackRecord.push(StatoMenu.STATO_ERRORE);
        }
    }
}
