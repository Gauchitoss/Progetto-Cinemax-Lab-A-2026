package cinemax;

/**
 * Main Class del progetto.
 * @author Modena Matteo (Matricola: 765099 ) - VA
 * @author Baroncelli Luca (Matricola: 765099 ) - VA
 * @author Bin Alessio (Matricola: 762387 ) - VA
 */

import java.util.Scanner;
import java.util.Stack;
import cinemax.MenuMangaer.StatoMenu;

public class CineMax {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        Stack<StatoMenu> stackRecord = new Stack<>();
        stackRecord.push(StatoMenu.BENVENUTO);

        boolean accessoAutorizzato = false;
        boolean running = true;

        while (running) {    

            // prendi stato attuale
            //  render(statoAttuale)
            // azioni statoAttule
            // cambio stato
            // aggiungere o togliere stato allo stack
            // fine iterazione

            stackRecord.push(StatoMenu.BENVENUTO);
            CinemaxTUI.renderizzaMenu(StatoMenu.BENVENUTO);
            String scelta = input.nextLine();
            switch (scelta) {
                case "1":
                    stackRecord.push(StatoMenu.LOGIN);
                    //while(accessoNegato || comando annulla)
                        CinemaxTUI.renderizzaMenu(StatoMenu.LOGIN);

                        System.out.print("\033[9;11H");
                        String username = input.nextLine();
                        if(username.equals(":q")){
                            stackRecord.pop();
                            CinemaxTUI.renderizzaMenu(stackRecord.firstElement());
                            
                        }

                        System.out.print("\033[12;11H");
                        String password = input.nextLine();
                        if(password.equals(":q")){
                            stackRecord.pop();
                            CinemaxTUI.renderizzaMenu(stackRecord.firstElement());
                        }
                        // if(controlloCredenziali(username, password)){
                        //     //accesso autorizzato renderizza menu stato menu clienti
                        // }

                    
                    break;
                case "2":
                    stackRecord.push(StatoMenu.MENU_GUEST);
                    CinemaxTUI.renderizzaMenu(StatoMenu.MENU_GUEST);
                    break;
                case "3":
                    stackRecord.push(StatoMenu.REGISTRAZIONE);
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
}
