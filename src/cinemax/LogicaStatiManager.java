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

// ======================================================
//              GESTIONE STATI GENERALE
// ======================================================
    
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

    public static void statoSuccesivo(Stack<StatoMenu> stack, String indice){

        int scelta = Integer.parseInt(indice);
        StatoMenu statoAttuale = stack.peek();

        StatoMenu[] possibiliStatiSucc = statoAttuale.prossimi();
        StatoMenu statoSuccessivo = possibiliStatiSucc[scelta-1];
        // ULTIMO ELEMENTO ARRAY CONTIENE L'INDIETRO

        if(statoSuccessivo == null){
            stack.pop();
            return;
        }
        if(statoSuccessivo == StatoMenu.BENVENUTO){
            stack.clear();
        }

        stack.push(statoSuccessivo);
    }


// ======================================================
//              GESTISCI MENU PRICIPALI
// ======================================================
    /**
     * Gestisce logica pagina iniziale
     */
    public static void gestisciBenvenuto(Stack<StatoMenu> stack){
        String scelta = input.nextLine();
        if(scelta.equals("4"))
            System.exit(0);
        statoSuccesivo(stack, scelta);
    }

    /**
    * 
    */
    public static void gestisciMenuGuest(Stack<StatoMenu> stack){
        String scelta = input.nextLine();
        statoSuccesivo(stack, scelta);
    }

    /**
    * 
    * @param stack
    */
    public static void gestisciMenuClienti(Stack<StatoMenu> stack){
        String scelta = input.nextLine();
        statoSuccesivo(stack, scelta);
    }

    /**
     * 
     */
    public static void gestisciMenuProezionisti(Stack<StatoMenu> stack){
        String scelta = input.nextLine();
        statoSuccesivo(stack, scelta);
    }  
    
    /**
     * 
     * @param stack
     */
    public static void gestisciMenuBigliettai(Stack<StatoMenu> stack){
        String scelta = input.nextLine();
        statoSuccesivo(stack, scelta);
    }
    
// ======================================================
//                    GESTISCI FORM
// ======================================================

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

// ======================================================
//              GESTISCI RICERCA FILM
// ======================================================
    
    /**
     * 
     * @param stack
     */
    public static void gestisciCercaFlm(Stack<StatoMenu> stack){
        String scelta = input.nextLine();
        statoSuccesivo(stack, scelta);

        /**
         * IDEA PER RICERCA FILM
         * 
         * OPZIONE 1
         * 1) chiedo utente se vuole cercare con più filtri
         * 2) seleziona fino a un massimo di 6 filtri (numero caratteri di un film), può anche metterne solo due
         * 3) salvo questi filtri in un array
         * 4) renderizzo un nuovo form dove richiede i seguenti dati
         * 5) ciclo for itera ogni azione e crea lo spazio e mette campi a un possibile costruttore (gestire il fatto che non sai bene il nome del dato
         * 6) mostra soluzione
         * 
         * OPZIONE 2
         * 1) stampo il form contenente tutti i campi
         * 2) utente libero di compilare quelli che vuole
         * 3) se preme invio salta il filtro aggiuntivo
         * 4) mostra soluzione
         * implementazione più semplice e forse anche più bella e user-friendly
         */

    }

// ======================================================
//              GESTISCI PRENOTAZIONI
// ======================================================
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

// ======================================================
// ======================================================
}

    
