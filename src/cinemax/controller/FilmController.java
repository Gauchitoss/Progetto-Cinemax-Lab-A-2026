package cinemax.controller;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import cinemax.CineMax;
import cinemax.CostantiForm.Campi;
import cinemax.MenuManager.StatoMenu;
import cinemax.model.Proiezione;
import cinemax.util.GestoreProiezione;

/**
 * Gestisce la logica di business relativa ai film e alle proiezioni.
 * Si occupa dell'impaginazione, della ricerca e dell'aggiunta di proiezioni.
 */
public class FilmController {

    public static List<Proiezione> filmPaginaTmp = new ArrayList<>();
    public static boolean esistenzaPaginaSuccessiva;
    public static boolean esistenzaPrecedente;
    public static int paginaCorrente = 0;
    public static final int ELEMENTI_PAGINA = 10; 
    public static List<Proiezione> proiezioniTrovate = new ArrayList<>();
    public static Proiezione filmSelezionatoTmp;

    /**
     * RICERCA TRAMITE UN SOLO PARAMETRO SPECIFO PROGRAMMAZIONE
     * @param dataOggi
     */
    public static void gestisciCercaFilm(List<Proiezione> lista){
        try {
            CineMax.stackRecord.pop();
            proiezioniTrovate = lista;
            paginaCorrente = 0;
            aggiornaProiezioniPerPagina();
            CineMax.stackRecord.push(StatoMenu.VISUALIZZA_PROGRAMMAZAIONE);
        } catch (Exception e) {
            cinemax.LogicaStatiManager.messaggioErroreCorrente = "Errore generico durante il caricamento del programma.";
            CineMax.stackRecord.push(StatoMenu.STATO_ERRORE);
        }
    }

    /**
     * 
     * @param datiFormTmp
     */
    public static void gestisciCercaFilmDaArray(String[] datiFormTmp){
        try{
            CineMax.stackRecord.pop();
            GestoreProiezione.leggiProiezioni();

            String  titolo          = datiFormTmp[Campi.CERCA_TITOLO.i];
            String  genere          = datiFormTmp[Campi.CERCA_GENERE.i];
            String  costo           = datiFormTmp[Campi.CERCA_COSTO.i];
            LocalDate dataDa = parseData(datiFormTmp[Campi.CERCA_GIORNO_1.i], datiFormTmp[Campi.CERCA_MESE_1.i], datiFormTmp[Campi.CERCA_ANNO_1.i]);
            LocalDate dataA = parseData(datiFormTmp[Campi.CERCA_GIORNO_2.i], datiFormTmp[Campi.CERCA_MESE_2.i], datiFormTmp[Campi.CERCA_ANNO_2.i]);
            Double prezzoMax = null;
            if(costo != null && !costo.isEmpty()){
                try{
                    prezzoMax = Double.parseDouble(costo.replace(",", "."));
                }catch (NumberFormatException e){
                    cinemax.LogicaStatiManager.messaggioErroreCorrente = "Formsto prezzo non valido.";
                    CineMax.stackRecord.push(StatoMenu.STATO_ERRORE);
                    return;
                }
            }    
            proiezioniTrovate = GestoreProiezione.cercaProiezione(titolo, dataDa, dataA, prezzoMax, genere);
            paginaCorrente = 0;
            aggiornaProiezioniPerPagina();
            CineMax.stackRecord.push(StatoMenu.VISUALIZZA_PROGRAMMAZAIONE);
        }catch(DateTimeException e){
            cinemax.LogicaStatiManager.messaggioErroreCorrente = "Intervallo di date non valido.";
            CineMax.stackRecord.push(StatoMenu.STATO_ERRORE);
        }
    }

    public static void aggiornaProiezioniPerPagina(){
        filmPaginaTmp.clear();
        esistenzaPaginaSuccessiva = false;
        esistenzaPrecedente = false;
        int indiceInizio = ELEMENTI_PAGINA * paginaCorrente;
        for(int i = indiceInizio; i < indiceInizio + ELEMENTI_PAGINA && i <proiezioniTrovate.size(); i++){
            filmPaginaTmp.add(proiezioniTrovate.get(i));
        }
        if(indiceInizio + ELEMENTI_PAGINA < proiezioniTrovate.size()) esistenzaPaginaSuccessiva = true;
        if(paginaCorrente > 0) esistenzaPrecedente = true;
    }
    
    public static void gestisciVisualizzaProiezione(String scelta){
        try{
           if(scelta.trim().isEmpty() || scelta == null || scelta.equalsIgnoreCase("C")){
                CineMax.stackRecord.pop();
                return;
            }
            if(scelta.equalsIgnoreCase("N") && esistenzaPaginaSuccessiva){
                paginaCorrente++;
                aggiornaProiezioniPerPagina();
                CineMax.stackRecord.pop();
                CineMax.stackRecord.push(StatoMenu.VISUALIZZA_PROGRAMMAZAIONE);
                return;
            }
            if(scelta.equalsIgnoreCase("B")&& esistenzaPrecedente){
                paginaCorrente--;
                aggiornaProiezioniPerPagina();
                CineMax.stackRecord.pop();
                CineMax.stackRecord.push(StatoMenu.VISUALIZZA_PROGRAMMAZAIONE);
                return;
            }
            int numeroFilm = Integer.parseInt(scelta);
            filmSelezionatoTmp = filmPaginaTmp.get(numeroFilm-1);
            CineMax.stackRecord.push(StatoMenu.MENU_INFO_FILM);
    
        }catch(NumberFormatException | IndexOutOfBoundsException e){
            //scelta non valida, schermata rirendirizzata
        }

    }

    public static void gestisciMenuInfoFilm(String scelta){
        String ruolo = CineMax.ruolo.getEtichetta();
        switch(ruolo){
            case "cliente registrato": 
                if("1".equals(scelta)) CineMax.stackRecord.push(StatoMenu.PRENOTA_POSTI);    
                else if("2".equals(scelta)) CineMax.stackRecord.pop();    
                break;
            case "proiezionista": 
                if("1".equals(scelta)) {
                    GestoreProiezione.elimina(filmSelezionatoTmp);
                    cinemax.LogicaStatiManager.messaggioConfermaCorrente = "Proiezione eliminata con successo.";
                    CineMax.stackRecord.pop();
                    CineMax.stackRecord.pop();
                    CineMax.stackRecord.push(StatoMenu.STATO_CONFERMA);   
                }
                else if("2".equals(scelta))
                    CineMax.stackRecord.push(StatoMenu.GESTISCI_PROIEZIONE);
                else if("3".equals(scelta))
                    CineMax.stackRecord.pop();
                break;
            case "bigliettaio":
                if("1".equals(scelta))
                    CineMax.stackRecord.push(StatoMenu.VENDITA_DIRETTA);
                else if("2".equals(scelta))
                    CineMax.stackRecord.pop();
                break;
            default:
                CineMax.stackRecord.pop();
        }
    }
/**
     * Metodo che si occupa di formattare i dati presi dal form per istanziare
     * una nuova Proiezione. Gestisce il parsing assicurandosi che l'utente non
     * abbia inserito testo al posto di numeri.
     */
    public static void gestisciInserimentoProiezione(String[] datiFormTmp){
        try {
            GestoreProiezione.leggiProiezioni();
            String  titolo          = datiFormTmp[Campi.ADD_TITOLO.i];
            String  genere          = datiFormTmp[Campi.ADD_GENERE.i];
            String  regista         = datiFormTmp[Campi.ADD_REGISTA.i];
            String  annoProduzione  = datiFormTmp[Campi.ADD_ANNO_PRODUZIONE.i];
            String  durata          = datiFormTmp[Campi.ADD_DURATA.i];
            String  etaMinima       = datiFormTmp[Campi.ADD_ETA.i];
            String  costo           = datiFormTmp[Campi.ADD_COSTO.i];
            String  posti           = datiFormTmp[Campi.ADD_POSTI.i];
            String  giorno          = datiFormTmp[Campi.ADD_GIORNO.i];
            String  mese            = datiFormTmp[Campi.ADD_MESE.i];
            String  anno            = datiFormTmp[Campi.ADD_ANNO.i];
            String  orario          = datiFormTmp[Campi.ADD_ORA.i];
            LocalDate data = LocalDate.of(Integer.parseInt(anno), Integer.parseInt(mese), Integer.parseInt(giorno));
            Proiezione nuovaProiezione = new Proiezione(data, orario, titolo, genere, regista, Integer.parseInt(annoProduzione), Integer.parseInt(durata), Integer.parseInt(etaMinima), Double.parseDouble(costo.replace(",", ".")), Integer.parseInt(posti));
            GestoreProiezione.inserisci(nuovaProiezione);
            CineMax.stackRecord.pop();
            cinemax.LogicaStatiManager.messaggioConfermaCorrente = "Proiezione inserita.";
            CineMax.stackRecord.push(StatoMenu.STATO_CONFERMA);
        } catch (NumberFormatException | DateTimeException e) {
            cinemax.LogicaStatiManager.messaggioErroreCorrente = "Dati non validi, controlla numeri e date.";
            CineMax.stackRecord.push(StatoMenu.STATO_ERRORE);
        } catch(Exception e){
            cinemax.LogicaStatiManager.messaggioErroreCorrente = "Errore generico del programma.";
            CineMax.stackRecord.push(StatoMenu.STATO_ERRORE);            
        }
    }

    public static void gestisciModificaProiezione(Proiezione p, String[] datiFormTmp){
        // cntrollare se salava anche in modod corretto su file o meno
        try {
            GestoreProiezione.leggiProiezioni();

            if(datiFormTmp[Campi.ADD_TITOLO.i] != null) p.setTitolo(datiFormTmp[Campi.ADD_TITOLO.i]);
            if(datiFormTmp[Campi.ADD_GENERE.i] != null) p.setGenere(datiFormTmp[Campi.ADD_GENERE.i]);
            if(datiFormTmp[Campi.ADD_REGISTA.i] != null) p.setRegista(datiFormTmp[Campi.ADD_REGISTA.i]);
            if(datiFormTmp[Campi.ADD_ANNO_PRODUZIONE.i] != null) p.setAnno(Integer.parseInt(datiFormTmp[Campi.ADD_ANNO_PRODUZIONE.i]));
            if(datiFormTmp[Campi.ADD_DURATA.i] != null) p.setDurata(Integer.parseInt(datiFormTmp[Campi.ADD_DURATA.i]));
            if(datiFormTmp[Campi.ADD_ETA.i] != null) p.setEtaMin(Integer.parseInt(datiFormTmp[Campi.ADD_ETA.i]));
            if(datiFormTmp[Campi.ADD_COSTO.i] != null) p.setPrezzo(Double.parseDouble(datiFormTmp[Campi.ADD_COSTO.i].replace(",", ".")));
            if(datiFormTmp[Campi.ADD_POSTI.i] != null) p.setPostiSala(Integer.parseInt(datiFormTmp[Campi.ADD_POSTI.i]));
            if(datiFormTmp[Campi.ADD_ORA.i] != null) p.setOra(datiFormTmp[Campi.ADD_ORA.i]);
            String giorno = datiFormTmp[Campi.ADD_GIORNO.i];
            String mese = datiFormTmp[Campi.ADD_MESE.i];
            String anno = datiFormTmp[Campi.ADD_ANNO.i];
            if(giorno != null && mese != null && anno != null){
                p.setData(LocalDate.of(Integer.parseInt(anno), Integer.parseInt(mese), Integer.parseInt(giorno)));
            }
            GestoreProiezione.salvaSuFile(); 
            CineMax.stackRecord.pop();
            cinemax.LogicaStatiManager.messaggioConfermaCorrente = "Proiezione modificata.";
            CineMax.stackRecord.push(StatoMenu.STATO_CONFERMA);
        }catch(Exception e){
            cinemax.LogicaStatiManager.messaggioErroreCorrente = "Errore durante la modifica: " + e.getMessage();
            CineMax.stackRecord.push(StatoMenu.STATO_ERRORE);
        }
    }

    // ======================================================
    //                  HELPER
    // ======================================================

    private static LocalDate parseData(String gg, String mm, String aaaa){
        if(gg == null || mm == null || aaaa == null)
            return null;
        if(gg.isEmpty() || mm.isEmpty() || aaaa.isEmpty())
            return null;
        return LocalDate.of(Integer.parseInt(aaaa), Integer.parseInt(mm), Integer.parseInt(gg));
    }
}
