package cinemax;

/**
 * Main Class del progetto.
 * @author Modena Matteo (Matricola: 765099 ) - VA
 * @author Baroncelli Luca (Matricola: 765099 ) - VA
 * @author Bin Alessio (Matricola: 762387 ) - VA
 */

import java.util.Scanner;
import cinemax.MenuMangaer.StatoMenu;

public class CineMax {
    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        boolean accessoAutorizzato = false;

        CinemaxTUI.renderizzaMenu(StatoMenu.BENVENUTO);
        String scelta = input.nextLine();

        switch (scelta) {
            case "1":
                //while(accessoNegato || comando annulla)
                    CinemaxTUI.renderizzaMenu(StatoMenu.LOGIN);
                    System.out.print("\033[9;11H");
                    String username = input.nextLine();
                    System.out.print("\033[12;11H");
                    String password = input.nextLine();
                    // if(controlloCredenziali(username, password)){
                    //     //accesso autorizzato renderizza menu stato menu clienti
                    // }

                
                break;
            case "2":
                CinemaxTUI.renderizzaMenu(StatoMenu.MENU_GUEST);
                break;
            case "3":
                CinemaxTUI.renderizzaMenu(StatoMenu.REGISTRAZIONE);
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
