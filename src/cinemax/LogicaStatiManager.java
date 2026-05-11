/**
 * Classe che gestisce l'interfaccia testuale (TUI - Terminal User Interface).
 * Si occupa della formattazione grafica, della stampa dei loghi e della 
 * renderizzazione dei menu in base allo stato corrente dell'applicazione.
 * @author Modena Matteo (Matricola: 765099 ) - VA
 * @author Baroncelli Luca (Matricola: 765099 ) - VA
 * @author Bin Alessio (Matricola: 762387 ) - VA
 */

package cinemax;

import java.util.*;
import cinemax.MenuMangaer.StatoMenu;

public class LogicaStatiManager {
    static Scanner input = new Scanner(System.in);

   /**
     * Gestisce lo stato auttuale in cui l'utente si trova
     * 
     * @param stack contenente tutti i record, viene modificato dinamicamente
     */
    public static void gestisciStati(Stack<StatoMenu> stack){
        StatoMenu statoMenu = stack.peek();

        switch(statoMenu){
            case BENVENUTO:
                gestisciBenvenuto(stack);
                break;
            case LOGIN:
                gestisciLogin(stack);
                break;
            case REGISTRAZIONE:
                gestisciRegistrazione(stack);
                break;
            case MENU_GUEST:
                gestisciMenuGuest(stack);
                break;
            case MENU_CLIENTI:
                gestisciMenuClienti(stack);
                break;
            case MENU_PROEZIONISTA:
                gestisciMenuProezionisti(stack);
                break;
            case MENU_BIGLIETTAIO:
                gestisciMenuBigliettai(stack);
                break;
            case CERCA_FILM:
                gestisciCercaFlm(stack);
                break;
            case VISUALIZZA_PROGRAMMAZAIONE:
                break;
            case PRENOTA_POSTI:
                break;
            case MIE_PRENOTAZIONI:
                break;
            case INSERISCI_PROEZIONE:
                break;
            case RIMUOVI_PROEZIONE:
                break;
            case GESTISCI_PROEZIONE:
                break;
            case CERCA_PRENOTAZIONE:
                break;
            case VENDITA_DIRETTA:
                break;
            case STATO_SALA:
                break;
        }
    }



    /**
     * Gestisce logica pagina iniziale
     */
    public static void gestisciBenvenuto(Stack<StatoMenu> stack){
        String scelta = input.nextLine();
        
        switch (scelta) {
            case "1":
                stack.push(StatoMenu.LOGIN);
                break;
            case "2":
                stack.push(StatoMenu.MENU_GUEST);
                break;
            case "3":
                stack.push(StatoMenu.REGISTRAZIONE);
                break;
            case "4":
                System.exit(0);
                break;
            default:
                break;
        }
    }

    /**
     * Gestisce logica de login
     */
    public static void gestisciLogin(Stack<StatoMenu> stack){

        System.out.print("\033[9;11H");
        String username = input.nextLine();
        if(username.equals(":q")){
            stack.pop();
            return;
        }


        System.out.print("\033[12;11H");
        String password = input.nextLine();
        if(password.equals(":q")){
            stack.pop();
            return;
        }
        stack.push(StatoMenu.MENU_CLIENTI);
    }

    /**
     * 
     * @param stack
     */
    public static void gestisciRegistrazione(Stack<StatoMenu> stack){
        int salto = 4;

        System.out.print("\033["+salto+";11H");
        salto+=3;
        String nome = input.nextLine();
        if(nome.equals(":q")){
            stack.pop();
            return;
        }


        System.out.print("\033["+salto+";11H");
        salto+=3;
        String cognome = input.nextLine();
        if(cognome.equals(":q")){
            stack.pop();
            return;
        }

        System.out.print("\033["+salto+";11H");
        salto+=3;
        String username = input.nextLine();
        if(username.equals(":q")){
            stack.pop();
            return;
        }
        System.out.print("\033["+salto+";11H");
        salto+=3;
        String password = input.nextLine();
        if(password.equals(":q")){
            stack.pop();
            return;
        }
        System.out.print("\033["+salto+";11H");
        salto+=3;
        String confermaPassword = input.nextLine();
        if(confermaPassword.equals(":q")){
            stack.pop();
            return;
        }
        System.out.print("\033["+salto+";11H");
        salto+=3;
        String dataNascita = input.nextLine();
        if(dataNascita.equals(":q")){
            stack.pop();
            return;
        }
        System.out.print("\033["+salto+";11H");
        salto+=3;
        String domicilio = input.nextLine();
        if(domicilio.equals(":q")){
            stack.pop();
            return;
        }

        /**
         * switch(ruolo){
         *  case proezionista:
         *      stack.push(StatoMenu.Proezionista);
         *      break;
         * }
        */
        stack.push(StatoMenu.MENU_CLIENTI);
    }

    /**
     * 
    */
    public static void gestisciMenuGuest(Stack<StatoMenu> stack){
        String scelta = input.nextLine();
        
        switch (scelta) {
            case "1":
                stack.push(StatoMenu.CERCA_FILM);
                break;
            case "2":
                stack.push(StatoMenu.LOGIN);
                break;
            case "3":
                stack.pop();
                break;
            default:
                break;
        }
    }

    /**
     * 
     * @param stack
     */
    public static void gestisciMenuClienti(Stack<StatoMenu> stack){
        String scelta = input.nextLine();

        switch (scelta) {
            case "1":
                stack.push(StatoMenu.CERCA_FILM);
                break;
            case "2":
                stack.push(StatoMenu.MIE_PRENOTAZIONI);
            case "3":
                stack.clear();
                stack.push(StatoMenu.BENVENUTO);
            default:
                break;
        }
    }
    public static void gestisciMenuProezionisti(Stack<StatoMenu> stack){
        String scelta = input.nextLine();

        switch (scelta) {
            case "1":
                stack.push(StatoMenu.INSERISCI_PROEZIONE);
                break;
            case "2":
                stack.push(StatoMenu.RIMUOVI_PROEZIONE);
                break;
            case "3":
                stack.push(StatoMenu.GESTISCI_PROEZIONE);
                break;
            case "4":
                stack.clear();
                stack.push(StatoMenu.BENVENUTO);
                break;
            default:
                break;
        }
    }

    public static void gestisciMenuBigliettai(Stack<StatoMenu> stack){
        String scelta = input.nextLine();

        switch (scelta) {
            case "1":
                stack.push(StatoMenu.CERCA_FILM);
                break;
            case "2":
                stack.push(StatoMenu.CERCA_PRENOTAZIONE);
                break;
            case "3":
                stack.push(StatoMenu.VENDITA_DIRETTA);
                break;
            case "4":
                stack.push(StatoMenu.STATO_SALA);
            case "5":
                stack.clear();
                stack.push(StatoMenu.BENVENUTO);
                break;
            default:
                break;        
    }
    }

    /**
     * 
     * @param stack
     */
    public static void gestisciCercaFlm(Stack<StatoMenu> stack){
        String scelta = input.nextLine();
        //String ricerca;

        switch (scelta) {
            case "1":
                //ricercaTitolo(ricerca);
                break;
            case "2":
                //RicercaData(ricerca);
                break;
            case "3":
                //ricercaCosto(ricerca)
                break;
            case "4":
                //ricercaDurata(ricerca)
                break;
            case "5":
                //ricercaGenere(ricerca)
                break;
            case "6":
                stack.pop();
                break;
            default:
                break;
        }
    }


    /**
     * 
     * @param stack
     */
    public static void gestisciPrenotazionePosti(Stack<StatoMenu> stack){
        
    }

    /**
     * 
     * @param stack
     */
    public static void gestisciLeMieProezioni(Stack<StatoMenu> stack){
        
    }

    /**
     * 
     * @param stack
     */
    public static void gestisciLogout(Stack<StatoMenu> stack){
        
    }
}

    
