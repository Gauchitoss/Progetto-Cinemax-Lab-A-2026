package cinemax;

/**
 * Main Class del progetto.
 * @author Modena Matteo (Matricola: 765099 ) - VA
 * @author Baroncelli Luca (Matricola: ) - VA
 * @author Bin Alessio (Matricola: ) - VA
 */

import java.util.Scanner;
import cinemax.MenuManger.StatoMenu;

public class CineMax {
    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        String[] opzioniIniziali = StatoMenu.BENVENUTO.getOpzioni();
        CinemaxTUI.renderizzaMenu("cinemax", opzioniIniziali, "centro" );
        String scelta = input.nextLine();

        switch (scelta) {
            case "1":
                CinemaxTUI.renderizzaMenu("accedi", StatoMenu.LOGIN.getOpzioni(), "sinistra");
                break;
            case "2":
                break;
            case "3":
                break;
            case "4":
                break;
        
            default:
                System.out.println("scelta non valida");
                break;
        }

        input.close();
    }
}
