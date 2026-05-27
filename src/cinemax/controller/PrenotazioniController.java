package cinemax.controller;

import java.util.ArrayList;
import java.util.List;

import cinemax.CineMax;
import cinemax.MenuMangaer.StatoMenu;
import cinemax.model.Prenotazione;
import cinemax.model.Proiezione;
import cinemax.util.GestorePrenotazione;

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
     * Carica e mostra le prenotazioni eseguite dall'utente
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
     *
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
     * 
     * @param scelta
     * @param statoAttuale
     */
    public static void gestisciVisualizzaPrenotazione(String scelta, StatoMenu statoAttuale) {
        try {
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
            
        } catch(Exception e) {}
    }
}
