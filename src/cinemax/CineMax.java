package cinemax;

import java.util.Stack;
import cinemax.MenuMangaer.StatoMenu;
import cinemax.model.Utente;
import cinemax.util.GestorePrenotazione;
import cinemax.util.GestoreProiezione;
import cinemax.util.GestoreUtenti;

/**
 * Main Class del progetto CineMax.
 * Punto di ingresso dell'applicazione, gestisce il ciclo di vita principale
 * attraverso una macchina a stati implementata tramite Stack (State Machine).
 * * @author Modena Matteo (Matricola: 765099) - VA
 * @author Baroncelli Luca (Matricola: 761582) - VA
 * @author Bin Alessio (Matricola: 762387) - VA
 */
public class CineMax {

    /**
     * Stack globale che mantiene la cronologia di navigazione dei menu.
     * Permette di implementare logiche di push (avanti) e pop (indietro).
     */
    public static Stack<StatoMenu> stackRecord = new Stack<>();
    public static Utente.Ruolo ruolo = Utente.Ruolo.CLIENTE_OSPITE;
    public static void main(String[] args) {

        try{
            GestoreUtenti.caricaUtenti();
            GestoreProiezione.leggiProiezioni();
            GestorePrenotazione.leggiPrenotazioni();
        }catch(Exception e){
            System.out.println("Errore durante il caricamento dei file CSV: "+ e.getMessage());
        }

        stackRecord.push(StatoMenu.BENVENUTO);
        StatoMenu statoAttuale;

        boolean running = true;

        while (running) {   
            // Se lo stack si svuota accidentalmente, previene il crash e chiude pulito
            if(stackRecord.isEmpty()){
                System.out.println("Chiusura sistema...");
                break;
            }
            // Prendi stato attuale
            statoAttuale = stackRecord.peek();

            // Renderizza la schermata
            CinemaxTUI.renderizzaMenu(statoAttuale);

            // Esegue le azioni associate allo stato attuale
            statoAttuale.eseguiLogicaAssociata();

        }
    }
}
