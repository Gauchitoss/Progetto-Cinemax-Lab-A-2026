package cinemax;

import java.util.Stack;
import cinemax.MenuManager.StatoMenu;
import cinemax.model.Utente;
import cinemax.util.GestorePrenotazione;
import cinemax.util.GestoreProiezione;
import cinemax.util.GestoreUtenti;

/**
 * Main Class del progetto CineMax.
 * Punto di ingresso dell'applicazione, gestisce il ciclo di vita principale
 * attraverso una macchina a stati implementata tramite Stack (State Machine).
 * @author Modena Matteo (Matricola: 765099) - VA
 * @author Baroncelli Luca (Matricola: 761582) - VA
 * @author Bin Alessio (Matricola: 762387) - VA
 */
public class CineMax {

    /**
     * Stack globale che mantiene la cronologia di navigazione dei menu.
     * Permette di implementare logiche di push (avanti) e pop (indietro).
     */
    public static final Stack<StatoMenu> stackRecord = new Stack<>();
    public static Utente.Ruolo ruolo = Utente.Ruolo.CLIENTE_OSPITE;
    
    /**
     * Metodo principale (Main) dell'applicazione.
     * Carica i record dai file CSV di utenti, proiezioni e prenotazioni all'interno della memoria centrale,
     * inserisce lo stato iniziale di BENVENUTO nello stack ed esegue il loop principale della macchina a stati.
     * @param args gli argomenti passati da riga di comando (non utilizzati)
     */
    public static void main(String[] args) {

        try{
            GestoreUtenti.caricaUtenti();
            GestoreProiezione.leggiProiezioni();
            GestorePrenotazione.leggiPrenotazioni();
        }catch(Exception e){
            System.out.println("Errore durante il caricamento dei file CSV: "+ e.getMessage());
        }
        stackRecord.push(StatoMenu.BENVENUTO);
        while (!stackRecord.isEmpty()) {   
            // Prendi stato attuale
            StatoMenu statoAttuale = stackRecord.peek();
            // Renderizza la schermata
            CinemaxTUI.renderizzaMenu(statoAttuale);
            // Esegue le azioni associate allo stato attuale
            statoAttuale.eseguiLogicaAssociata();
        }
        System.out.println("Chiusura sistema...");
    }
}
