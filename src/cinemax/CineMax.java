package cinemax;

/**
 * Main Class del progetto.
 * @author Modena Matteo (Matricola: 765099 ) - VA
 * @author Baroncelli Luca (Matricola: 765099 ) - VA
 * @author Bin Alessio (Matricola: 762387 ) - VA
 */

import java.util.Stack;
import cinemax.MenuMangaer.StatoMenu;

public class CineMax {

    public static Stack<StatoMenu> stackRecord = new Stack<>();
    public static void main(String[] args) {

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
            LogicaStatiManager.gestisciStati();
            // cambio stato
            // aggiungere o togliere stato allo stack
            // fine iterazione
            System.out.println(stackRecord);

        }
    }
}
