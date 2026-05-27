package cinemax.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import cinemax.CineMax;
import cinemax.CostantiForm.Campi;
import cinemax.MenuMangaer.StatoMenu;
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
    public static void gestisciCercaFilm(List<Proiezione> dataOggi){
        try {
            CineMax.stackRecord.pop();
            GestoreProiezione.leggiProiezioni();

            proiezioniTrovate = dataOggi;
            aggiornaProezioniPerPagina();
            paginaCorrente = 0;
            CineMax.stackRecord.push(StatoMenu.VISUALIZZA_PROGRAMMAZAIONE);
        } catch (Exception e) {
            cinemax.LogicaStatiManager.messaggioErroreCorrente = "Errore generico durante il caricamento del programma";
            CineMax.stackRecord.push(StatoMenu.STATO_ERRORE);
        }
    }
    /**
     * 
     * @param datiFormTmp
     */
    public static void gestisciCercaFlm(String[] datiFormTmp){
        try{
            CineMax.stackRecord.pop();
            GestoreProiezione.leggiProiezioni();

            String  titolo          = datiFormTmp[Campi.CERCA_TITOLO.i];
            String  giornoInizio    = datiFormTmp[Campi.CERCA_GIORNO_1.i];
            String  meseInizio      = datiFormTmp[Campi.CERCA_MESE_1.i];
            String  annoInizio      = datiFormTmp[Campi.CERCA_ANNO_1.i];
            String  giornoFine      = datiFormTmp[Campi.CERCA_GIORNO_2.i];
            String  meseFine        = datiFormTmp[Campi.CERCA_MESE_2.i];
            String  annoFine        = datiFormTmp[Campi.CERCA_ANNO_2.i];
            String  costo           = datiFormTmp[Campi.CERCA_COSTO.i];
            String  genere          = datiFormTmp[Campi.CERCA_GENERE.i];

            proiezioniTrovate = GestoreProiezione.cercaProiezione(titolo, giornoInizio, meseInizio, annoInizio, giornoFine, meseFine, annoFine, costo, genere);
            aggiornaProezioniPerPagina();
            // Reset della pagina inziale
            paginaCorrente = 0;
            // Cambiamento stato
            CineMax.stackRecord.push(StatoMenu.VISUALIZZA_PROGRAMMAZAIONE);
        }catch(IllegalArgumentException e){
            cinemax.LogicaStatiManager.messaggioErroreCorrente = "Errore durante la ricerca di proiezioni";
            CineMax.stackRecord.push(StatoMenu.STATO_ERRORE);
        }
    }

    public static void aggiornaProezioniPerPagina(){

        filmPaginaTmp.clear();

        esistenzaPaginaSuccessiva = false;
        esistenzaPrecedente = false;

        int indiceInizio = ELEMENTI_PAGINA * paginaCorrente;
        int indiceFine = indiceInizio + ELEMENTI_PAGINA;

        for(int i = indiceInizio; i < indiceFine && i <proiezioniTrovate.size(); i++){
            filmPaginaTmp.add(proiezioniTrovate.get(i));
        }

        if(indiceInizio + ELEMENTI_PAGINA < proiezioniTrovate.size()) esistenzaPaginaSuccessiva = true;
        if(paginaCorrente > 0) esistenzaPrecedente = true;
    }
    
    public static void gestisciVisualizzaProiezione(String scelta){
        try{
           if(scelta.trim().isEmpty()){
                CineMax.stackRecord.pop();
                return;
            }
            if(scelta.toUpperCase().equals("C")){
                CineMax.stackRecord.pop();
                return;
            }
            if(scelta.toUpperCase().equals("N") && esistenzaPaginaSuccessiva){
                paginaCorrente++;
                aggiornaProezioniPerPagina();
                CineMax.stackRecord.pop();
                CineMax.stackRecord.push(StatoMenu.VISUALIZZA_PROGRAMMAZAIONE);
                return;
            }
            if(scelta.toUpperCase().equals("B")&& esistenzaPrecedente){
                paginaCorrente--;
                aggiornaProezioniPerPagina();
                CineMax.stackRecord.pop();
                CineMax.stackRecord.push(StatoMenu.VISUALIZZA_PROGRAMMAZAIONE);
                return;
            }

            int numeroFilm = Integer.parseInt(scelta);
            filmSelezionatoTmp = filmPaginaTmp.get(numeroFilm-1);
            CineMax.stackRecord.push(StatoMenu.MENU_INFO_FILM);
    
        }catch(NumberFormatException e){
            // TODO gestione
        }catch(ArrayIndexOutOfBoundsException e){
            // TODO gestione
        }catch(Exception e){

        }

    }

    public static void gestisciMenuInfoFilm(String scelta){
        try{
            String ruolo = CineMax.ruolo;
            
            if(ruolo.equals("cliente registrato")){
                switch(scelta){
                    case "1": CineMax.stackRecord.push(StatoMenu.PRENOTA_POSTI);    break;
                    case "2": CineMax.stackRecord.pop();    break; 
                }
            }else if(ruolo.equals("proiezionista")){
                switch(scelta){
                    case "1": {
                        GestoreProiezione.elimina(filmSelezionatoTmp);
                        // TODO aggiungere conferma di rimozione  
                        CineMax.stackRecord.pop(); // singolo back per uscire pagina film, 
                        // TODO aggiornare la schemrata dei film senza il film appena cancelato
                    } break;
                    case "2": CineMax.stackRecord.push(StatoMenu.GESTISCI_PROEZIONE);   break;
                    case "3": CineMax.stackRecord.pop();    break; 
                }
            } else if(ruolo.equals("bigliettaio")){
                switch(scelta){
                    case "1": CineMax.stackRecord.push(StatoMenu.VENDITA_DIRETTA);   break;
                    case "2": CineMax.stackRecord.pop();   break; 
                }
            }else
                CineMax.stackRecord.pop();
        }catch(Exception e){

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
            // TODO aggiungere conferma di inserimento
        } catch (IllegalArgumentException e) {
            cinemax.LogicaStatiManager.messaggioErroreCorrente = "Errore disastroso durante l'aggiunta di una proiezione";
            CineMax.stackRecord.push(StatoMenu.STATO_ERRORE);
        } catch(Exception e){
            cinemax.LogicaStatiManager.messaggioErroreCorrente = "Errore generico del programma";
            CineMax.stackRecord.push(StatoMenu.STATO_ERRORE);            
        }
    }

    public static void gestisciModificaProiezione(Proiezione p, String[] datiFormTmp){
    
        // cntrollare se salava anche in modod corretto su file o meno
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

            if(titolo != null) p.setTitolo(titolo);
            if(genere!= null) p.setGenere(genere);
            if(regista != null) p.setRegista(regista);
            if(annoProduzione != null) p.setAnno(Integer.parseInt(annoProduzione));
            if(durata != null) p.setDurata(Integer.parseInt(durata));
            if(etaMinima != null) p.setEtaMin(Integer.parseInt(etaMinima));
            if(posti != null) p.setPostiSala(Integer.parseInt(posti));
            if(etaMinima != null) p.setPrezzo(Double.parseDouble(costo.replace(",", ".")));
            // TODO: modifica data
            if(orario !=null) p.setOra(orario);

            GestoreProiezione.salvaSuFile(); //non funziona
            CineMax.stackRecord.pop();
        }catch(Exception e){

        }

    }
}
