package cinemax.controller;

import cinemax.CineMax;
import cinemax.CostantiForm.Campi;
import cinemax.MenuMangaer.StatoMenu;
import cinemax.util.GestoreProiezione;

public class FilmController {

    public static void gestisciCercaFlm(String[] datiFormTmp){

        try{

            String  titolo          = datiFormTmp[Campi.CERCA_TITOLO.i];
            String  giornoInizio    = datiFormTmp[Campi.CERCA_GIORNO_1.i];
            String  meseInizio      = datiFormTmp[Campi.CERCA_MESE_1.i];
            String  annoInizio      = datiFormTmp[Campi.CERCA_ANNO_1.i];
            String  giornoFine      = datiFormTmp[Campi.CERCA_GIORNO_2.i];
            String  meseFine        = datiFormTmp[Campi.CERCA_MESE_2.i];
            String  annoFine        = datiFormTmp[Campi.CERCA_ANNO_2.i];
            String  costo           = datiFormTmp[Campi.CERCA_COSTO.i];
            String  durata          = datiFormTmp[Campi.CERCA_DURATA.i];
            String  genere          = datiFormTmp[Campi.CERCA_GENERE.i];

            GestoreProiezione.visualizzaProiezione(titolo, giornoInizio, meseInizio, annoInizio, giornoFine, meseFine, annoFine, durata, genere);

        }catch(IllegalArgumentException e){

        }

    }
    
}
