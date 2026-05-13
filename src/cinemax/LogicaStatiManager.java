/**
 * Classe LogicaStatiManager
 * Funge da Controller centrale per la gestione degli stati e della navigazione.
 * Coordina l'interazione tra l'input dell'utente e il cambiamento del contesto applicativo
 * manipolando direttamente lo stack globale dei record.
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
//                 GESTORE DEL FLUSSO
// ======================================================
    
    /**
     * Punto di ingresso principale per la logica di ogni stato.
     * Legge lo stato in cima allo stack e delega l'esecuzione al metodo specifico.
     */
    public static void gestisciStati(){
        StatoMenu statoMenu = CineMax.stackRecord.peek();

        switch(statoMenu){
            case BENVENUTO:             gestisciBenvenuto();        break;
            case LOGIN:                 gestisciLogin();            break;
            case REGISTRAZIONE:         gestisciRegistrazione();    break;
            case MENU_GUEST:            gestisciMenuGuest();        break;
            case MENU_CLIENTI:          gestisciMenuClienti();      break;
            case MENU_PROEZIONISTA:     gestisciMenuProezionisti(); break;
            case MENU_BIGLIETTAIO:      gestisciMenuBigliettai();   break;
            case CERCA_FILM:            gestisciCercaFlm();         break;
            case STATO_ERRORE:          gestisciErrore();           break;
            default:                                                break;
        }
    }

    /**
     * Gestisce la logica di transizione tra i vari stati del menu.
     * Questa funzione crea una corrispondenza diretta tra l'input numerico dell'utente 
     * e l'array degli stati successivi definiti nell'enum StatoMenu.
     * * Nota: È progettata per menu a selezione numerica e non per i form di inserimento dati.
     * @param indice La stringa inserita dall'utente che rappresenta la scelta nel menu
     */
    public static void statoMenuSuccessivo(String indice){
        try{
            int scelta = Integer.parseInt(indice);
            StatoMenu statoAttuale = CineMax.stackRecord.peek();
            StatoMenu[] possibiliStatiSucc = statoAttuale.prossimi();
            StatoMenu statoSuccessivo = possibiliStatiSucc[scelta-1];

            if(statoSuccessivo == null){
                CineMax.stackRecord.pop();
                return;
            }
            if(statoSuccessivo == StatoMenu.BENVENUTO){
                CineMax.stackRecord.clear();
            }

            CineMax.stackRecord.push(statoSuccessivo);
        }catch(NumberFormatException | ArrayIndexOutOfBoundsException e){
            CineMax.stackRecord.push(StatoMenu.STATO_ERRORE);
        }
    }

// ======================================================
//              GESTORE DEI MENU
// ======================================================

    public static void gestisciBenvenuto(){
        String scelta = input.nextLine();
        if(scelta.equals("4"))
            System.exit(0);
        statoMenuSuccessivo(scelta);
    }

    public static void gestisciMenuGuest()          { statoMenuSuccessivo(input.nextLine()); }
    public static void gestisciMenuClienti()        { statoMenuSuccessivo(input.nextLine()); }
    public static void gestisciMenuProezionisti()   { statoMenuSuccessivo(input.nextLine()); } 
    public static void gestisciMenuBigliettai()     { statoMenuSuccessivo(input.nextLine()); }

    /**
     * Gestione dello stato di errore.
     * Blocca l'esecuzione finché l'utente non preme Invio, permettendo la lettura del messaggio,
     * quindi rimuove lo stato di errore per tornare al menu precedente.
     */
    public static void gestisciErrore(){
        input.nextLine();
        CineMax.stackRecord.pop();
    }

// ======================================================
//                    GESTORE INPUT
// ======================================================

    /**
     * Gestisce la logica di un login.
     * Attraverso la funziozione `prendiDatiForm()` riceve i vari imput dell'utente.
     * Viene poi eseguito un controllo degli input dell'utente.
     * Infine, in base al ruolo viene renderizzato al menu dedicato.
     */
    public static void gestisciLogin(){
        String[] datiFormTmp = new String[CineMax.stackRecord.peek().getOpzioni().length];
        prendiDatiForm(datiFormTmp);

        String username = datiFormTmp[Campi.LOGIN_USER.i];
        String password = datiFormTmp[Campi.LOGIN_PASSWORD.i];

        if(username == null || password == null){
            // TODO: STAMPA SCHERMATA DI ERRORE
        }else{
            // TODO: CONTROLLO VALIDITà CREDENZIALI
            // TODO: ASSEGNAZIONE A MENU DEDICATO
            CineMax.stackRecord.push(StatoMenu.MENU_CLIENTI);
        }   
    }

    /**
     * Gestisce la validazione dei campi di registrazione.
     */
    public static void gestisciRegistrazione(){
        String[] datiFormTmp = new String[CineMax.stackRecord.peek().getOpzioni().length];
        prendiDatiForm(datiFormTmp);

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
            CineMax.stackRecord.push(StatoMenu.MENU_CLIENTI);
        }else{

        }
    }

    public static void gestisciCercaFlm(){
        String[] datiFormTmp = new String[CineMax.stackRecord.peek().getOpzioni().length];
        prendiDatiForm(datiFormTmp);

        //titolo", "data", "costo", "durata", "genere"
        String titolo = datiFormTmp[Campi.CERCA_TITOLO.i];
        String data = datiFormTmp[Campi.CERCA_DATA.i];
        String costo = datiFormTmp[Campi.CERCA_COSTO.i];
        String durata = datiFormTmp[Campi.CERCA_DURATA.i];
        String genere = datiFormTmp[Campi.CERCA_GENERE.i];

        CineMax.stackRecord.push(StatoMenu.VISUALIZZA_PROGRAMMAZAIONE);
    }

// ======================================================
//              GESTISCI PRENOTAZIONI
// ======================================================
    /**
     * 
     * @param stack
     */
    public static void gestisciPrenotazionePosti(){
        
    }

    /**
     * 
     * @param stack
     */
    public static void gestisciLeMieProezioni(){
        
    }

// ======================================================
// LOGICA RICHIESTA DINAMICA DEI CAMPI NE FORM
// ======================================================

    /**
     * Metodo pruncipale per la gestione dei Form.
     * Utilizza sequenze di escape ANSI per posizionare il cursore all'interno dei box grafici
     * disegnati dalla TUI e popola l'array passato come parametro.
     */
    public static void prendiDatiForm(String[] campiForm){
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
                CineMax.stackRecord.pop();
                return;
            }
            campiForm[indiceCampo] = campoTmp;
        }

        System.out.println(java.util.Arrays.toString(campiForm));
    }
    
// ======================================================
// ======================================================
}
