package cinemax.controller;

import java.util.ArrayList;
import java.util.List;

import cinemax.CineMax;
import cinemax.CostantiForm.Campi;
import cinemax.MenuMangaer.StatoMenu;
import cinemax.model.Proiezione;
import cinemax.util.GestoreProiezione;

public class FilmController {

    public static List<Proiezione> filmPaginaTmp = new ArrayList<>();
    public static boolean esistenzaPaginaSuccessiva;
    public static boolean esistenzaPrecedente;
    public static int paginaCorrente = 0;
    public static final int ELEMENTI_PAGINA = 10; 
    public static List<Proiezione> proiezioniTrovate = new ArrayList<>();

    public static void gestisciCercaFlm(String[] datiFormTmp){

        try{
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

    }
}
