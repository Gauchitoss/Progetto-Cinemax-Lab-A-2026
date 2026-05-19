package cinemax;

/**
 * Main Class del progetto.
 * @author Modena Matteo (Matricola: 765099 ) - VA
 * @author Baroncelli Luca (Matricola: 765099 ) - VA
 * @author Bin Alessio (Matricola: 762387 ) - VA
 */

import java.util.Stack;
import cinemax.MenuMangaer.StatoMenu;
import cinemax.util.GestoreUtenti;

public class CineMax {

    public static Stack<StatoMenu> stackRecord = new Stack<>();
    public static boolean accessoUtente = false; 
    public static void main(String[] args) {

        GestoreUtenti.caricaUtenti();

        stackRecord.push(StatoMenu.BENVENUTO);
        StatoMenu statoAttuale;

        //boolean accessoAutorizzato = false;
        boolean running = true;

        while (running) {   

            // prendi stato attuale
            statoAttuale = stackRecord.peek();
            //  render(statoAttuale)
            CinemaxTUI.renderizzaMenu(statoAttuale);

            // azioni statoAttule
            statoAttuale.eseguiLogicaAssociata();
            // cambio stato
            // aggiungere o togliere stato allo stack
            // fine iterazione
        }
    }
}
