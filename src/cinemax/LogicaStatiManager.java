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
import cinemax.CostantiForm.Campi;

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

    public static void statoMenuSuccessivo(Stack<StatoMenu> stack, String indice){

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
        statoMenuSuccessivo(stack, scelta);
    }

    /**
    * 
    */
    public static void gestisciMenuGuest(Stack<StatoMenu> stack){
        String scelta = input.nextLine();
        statoMenuSuccessivo(stack, scelta);
    }

    /**
    * 
    * @param stack
    */
    public static void gestisciMenuClienti(Stack<StatoMenu> stack){
        String scelta = input.nextLine();
        statoMenuSuccessivo(stack, scelta);
    }

    /**
     * 
     */
    public static void gestisciMenuProezionisti(Stack<StatoMenu> stack){
        String scelta = input.nextLine();
        statoMenuSuccessivo(stack, scelta);
    }  
    
    /**
     * 
     * @param stack
     */
    public static void gestisciMenuBigliettai(Stack<StatoMenu> stack){
        String scelta = input.nextLine();
        statoMenuSuccessivo(stack, scelta);
    }
    
// ======================================================
//                    GESTISCI FORM
// ======================================================

    /**
     * Gestisce la logica di un login.
     * Attraverso la funziozione `prendiDatiForm()` riceve i vari imput dell'utente.
     * Viene poi eseguito un controllo degli input dell'utente.
     * Infine, in base al ruolo viene renderizzato al menu dedicato.
     * 
     * @param stack utilizzato per istanziare l'array che contiene gli input.
     */
    public static void gestisciLogin(Stack<StatoMenu> stack){
        String[] datiFormTmp = new String[stack.peek().getOpzioni().length];
        prendiDatiForm(datiFormTmp, stack);

        String username = datiFormTmp[Campi.LOGIN_USER.i];
        String password = datiFormTmp[Campi.LOGIN_PASSWORD.i];

        if(username == null || password == null){
            // STAMPA SCHERMATA DI ERRORE
        }else{
            // CONTROLLO VALIDITà CREDENZIALI
            // ASSEGNAZIONE A MENU DEDICATO
            stack.push(StatoMenu.MENU_CLIENTI);
        }
        
    }

    /**
     * 
     * @param stack
     */
    public static void gestisciRegistrazione(Stack<StatoMenu> stack){
        String[] datiFormTmp = new String[stack.peek().getOpzioni().length];
        prendiDatiForm(datiFormTmp, stack);

        String  nome = datiFormTmp[Campi.REG_NOME.i];
        String  cognome = datiFormTmp[Campi.REG_COGNOME.i];
        String  username = datiFormTmp[Campi.REG_USERNAME.i];
        String  password = datiFormTmp[Campi.REG_PASSWORD.i];
        String  confermaPassword = datiFormTmp[Campi.REG_CONFERMA_PASSWORD.i];
        String  dataNascita = datiFormTmp[Campi.REG_DATA.i];
        String  domicilio = datiFormTmp[Campi.REG_DOMICILIO.i];

        boolean valido = true;
        
        for (String campo : datiFormTmp) {
            if(campo == null)
                valido = false;
        }
        
        if(valido){
            stack.push(StatoMenu.MENU_CLIENTI);
        }else{

        }
    }

// ======================================================
//              GESTISCI RICERCA FILM
// ======================================================
    
    /**
     * 
     * @param stack
     */
    public static void gestisciCercaFlm(Stack<StatoMenu> stack){
        String[] datiFormTmp = new String[stack.peek().getOpzioni().length];
        prendiDatiForm(datiFormTmp, stack);

        //titolo", "data", "costo", "durata", "genere"
        String titolo = datiFormTmp[Campi.CERCA_TITOLO.i];
        String data = datiFormTmp[Campi.CERCA_DATA.i];
        String costo = datiFormTmp[Campi.CERCA_COSTO.i];
        String durata = datiFormTmp[Campi.CERCA_DURATA.i];
        String genere = datiFormTmp[Campi.CERCA_GENERE.i];

        stack.push(StatoMenu.VISUALIZZA_PROGRAMMAZAIONE);
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
// LOGICA RICHIESTA DINAMICA DEI CAMPI NE FORM
// ======================================================
    public static void prendiDatiForm(String[] campiForm, Stack<StatoMenu> stack){
        // ciclo for che crea ciclicamente i le richieste del form in base all'iterazione lo salva in un array di stringhe
        int altezzaLineaRichiesta = 10;
        String campoTmp="";
        for(int indiceCampo = 0; indiceCampo < campiForm.length; indiceCampo++){
            System.out.print("\033["+altezzaLineaRichiesta+";11H");
            altezzaLineaRichiesta += 3;
            campoTmp = input.nextLine();
            if(campoTmp.equals("")){
                campiForm[indiceCampo] = null;
                continue;
            }
            if(campoTmp.equals(":q")){
                stack.pop();
                return;
            }
            campiForm[indiceCampo] = campoTmp;
        }

        System.out.println(java.util.Arrays.toString(campiForm));
    }
// ======================================================
// ======================================================
}

    
