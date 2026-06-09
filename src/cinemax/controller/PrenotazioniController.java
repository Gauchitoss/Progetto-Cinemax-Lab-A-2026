package cinemax.controller;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import cinemax.CineMax;
import cinemax.CostantiForm.Campi;
import cinemax.MenuManager.StatoMenu;
import cinemax.model.Prenotazione;
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
    public static List<Prenotazione> prenotazioniTrovate = new ArrayList<>();
    public static Prenotazione prenotazioneSelezionataTmp;

    /**
     * Carica e mostra tutte le prenotazioni effettuate dall'utente attualmente loggato.
     * Inizializza la paginazione e cambia lo stato del menu in MIE_PRENOTAZIONI.
     */
    public static void gestisciMiePrenotazioni(){
        try {
            String usernameUtente = AutenticazioniController.utente.getUsername();
            prenotazioniTrovate = GestorePrenotazione.getPrenotzioniUtente(usernameUtente);
            paginaCorrente = 0;
            aggiornaPrenotazioniPerPagina();
            CineMax.stackRecord.push(StatoMenu.MIE_PRENOTAZIONI);
        } catch (Exception e) {
            cinemax.LogicaStatiManager.messaggioErroreCorrente = "Errore durante il caricamento delle prenotazioni.";
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
        for(int i = indiceInizio; i < indiceInizio + ELEMENTI_PAGINA && i <prenotazioniTrovate.size(); i++){
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
            if(scelta == null || scelta.trim().isEmpty() || scelta.equalsIgnoreCase("C")) {
                CineMax.stackRecord.pop();
                return;
            }
            if(scelta.equalsIgnoreCase("N") && esistenzaPaginaSuccessiva){
                paginaCorrente++;
                aggiornaPrenotazioniPerPagina();
                CineMax.stackRecord.pop();
                CineMax.stackRecord.push(statoAttuale);
                return;
            }
            if(scelta.equalsIgnoreCase("B") && esistenzaPrecedente){
                paginaCorrente--;
                aggiornaPrenotazioniPerPagina();
                CineMax.stackRecord.pop();
                CineMax.stackRecord.push(statoAttuale);
                return;
            }
            try{
                int indice = Integer.parseInt(scelta);
                if(indice >= 1 && indice <= prenotazioniPaginaTmp.size()){
                    prenotazioneSelezionataTmp = prenotazioniPaginaTmp.get(indice-1);
                    CineMax.stackRecord.push(StatoMenu.DETTAGLIO_PRENOTAZIONE);
                }
            } catch(NumberFormatException e){

            }
    }

    public static void gestisciDettaglioPrenotazione(String scelta){
        if("1".equals(scelta)){
            // Rimuovi prenotazione
            try {
                GestorePrenotazione.rimuoviPrenotazione(prenotazioneSelezionataTmp.getCodiceUnivoco());
                CineMax.stackRecord.pop(); // Esce dal dettaglio
                // Ricarica le prenotazioni aggiornate
                String usernameUtente = AutenticazioniController.utente.getUsername();
                prenotazioniTrovate = GestorePrenotazione.getPrenotzioniUtente(usernameUtente);
                paginaCorrente = 0;
                aggiornaPrenotazioniPerPagina();
                cinemax.LogicaStatiManager.messaggioConfermaCorrente = "Prenotazione rimossa con successo.";
                CineMax.stackRecord.push(StatoMenu.STATO_CONFERMA);
            } catch(Exception e){
                cinemax.LogicaStatiManager.messaggioErroreCorrente = "Errore nella rimozione: " + e.getMessage();
                CineMax.stackRecord.push(StatoMenu.STATO_ERRORE);
            }
        } else if("2".equals(scelta)){
            CineMax.stackRecord.pop(); // torna alla lista
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
            LocalDate dataDa = parseData(datiFormTmp[Campi.PRENOTAZIONE_GIORNO1.i], datiFormTmp[Campi.PRENOTAZIONE_MESE1.i], datiFormTmp[Campi.PRENOTAZIONE_ANNO1.i]);
            LocalDate dataA  = parseData(datiFormTmp[Campi.PRENOTAZIONE_GIORNO2.i], datiFormTmp[Campi.PRENOTAZIONE_MESE2.i], datiFormTmp[Campi.PRENOTAZIONE_ANNO2.i]);
            prenotazioniTrovate = GestorePrenotazione.cercaPrenotazioni(codice, nome, cognome, titolo, dataDa, dataA, username); 
            paginaCorrente = 0;
            aggiornaPrenotazioniPerPagina();
            CineMax.stackRecord.pop();
            CineMax.stackRecord.push(StatoMenu.MIE_PRENOTAZIONI);
        } catch(NumberFormatException | DateTimeException e){
            cinemax.LogicaStatiManager.messaggioErroreCorrente = "Hai inserito parametri non numerici in un campo data.";
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
            double importo = cinemax.util.GestorePrenotazione.venditaDiretta(FilmController.filmSelezionatoTmp, numBiglietti);
            CineMax.stackRecord.pop();
            cinemax.LogicaStatiManager.messaggioConfermaCorrente = "IMPORTO TOTALE DA PAGARE: " + String.format(java.util.Locale.US, "%.2f", importo) + " EURO  (" + numBiglietti + " x " + String.format(java.util.Locale.US, "%.2f", FilmController.filmSelezionatoTmp.getPrezzo()) + ")";
            CineMax.stackRecord.push(StatoMenu.STATO_CONFERMA);
        } catch (NumberFormatException e) {
            CineMax.stackRecord.pop(); // Rimuovo il form
            cinemax.LogicaStatiManager.messaggioErroreCorrente = "Errore: Devi inserire un numero valido di biglietti.";
            CineMax.stackRecord.push(StatoMenu.STATO_ERRORE);
        } catch (IllegalArgumentException | IllegalStateException e) {
            CineMax.stackRecord.pop(); // Rimuovo il form
            cinemax.LogicaStatiManager.messaggioErroreCorrente =  e.getMessage();
            CineMax.stackRecord.push(StatoMenu.STATO_ERRORE);
        } catch (Exception e){
            CineMax.stackRecord.pop();
            cinemax.LogicaStatiManager.messaggioErroreCorrente = "Errore imprevisto: " + e.getMessage();
            CineMax.stackRecord.push(StatoMenu.STATO_ERRORE);
        }
    }

    private static LocalDate parseData(String gg, String mm, String aaaa) {
        if (gg == null || mm == null || aaaa == null) return null;
        if (gg.isEmpty() || mm.isEmpty() || aaaa.isEmpty()) return null;
        return LocalDate.of(Integer.parseInt(aaaa), Integer.parseInt(mm), Integer.parseInt(gg));
    }
}
