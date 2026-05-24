
package cinemax;

import java.util.*;
import cinemax.MenuMangaer.StatoMenu;

/**
 * Classe LogicaStatiManager
 * Gestisce la logica di cambiamento di stati dei menu.
 * Si occupa di prendere in input i dati dell'utente nei form.
 * @author Modena Matteo (Matricola: 765099) - VA
 * @author Baroncelli Luca (Matricola: 765099) - VA
 * @author Bin Alessio (Matricola: 762387) - VA
 */
public class LogicaStatiManager {

    //Stringa globale di errore, modificabile in ogni punto del codice in base all'occornza
    public static String messaggioErroreCorrente = "spiacenti si è verificato un errore";
    static Scanner input = new Scanner(System.in);

// =========================================================================
//                 GESTORE DEL FLUSSO
// =========================================================================

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
                CineMax.ruolo = "cliente ospite";
            }

            CineMax.stackRecord.push(statoSuccessivo);
        }catch(NumberFormatException | ArrayIndexOutOfBoundsException e){
            CineMax.stackRecord.push(StatoMenu.STATO_ERRORE);
        }
    }

// =========================================================================
//             LOGICA RICHIESTA DINAMICA DEI CAMPI NEI FORM
// =========================================================================

    /**
     * Metodo principale per la gestione dei Form.
     * Utilizza sequenze di escape ANSI per posizionare il cursore all'interno dei box grafici
     * disegnati dalla TUI e popola l'array passato come parametro.
     * In caso una delle opzione deve generare un triplo form in linea, ne calcola lo spostamento
     * anche laterale, utilizzato per la richista data
     * @param campiForm Array dove salvare cosa ha scritto l'utente
     * @param opzioniStatoMenu Array contente tutte le opzioni dello stato del programma
     */
    public static boolean prendiDatiForm(String[] campiForm, String[] opzioniStatoMenu){
        int altezzaLineaRichiesta = 10;     // Altezza di base
        int altezzaColonnaRichiesta = 52;   // Longitudine di base
        String campoTmp="";

        // indiceOpzioni        scorre array di stringhe della classe MenuManager
        // indicieRisultati     scorre array dove vengono salvati gli input
        for(int indiceOpzioni = 0, indiceRisultati = 0; indiceOpzioni < opzioniStatoMenu.length; indiceOpzioni ++){
            
            if(opzioniStatoMenu[indiceOpzioni].toUpperCase().equals("DATAINIZIO")||opzioniStatoMenu[indiceOpzioni].toUpperCase().equals("DATAFINE")){
                for(int i = 0; i<3; i++){
                    System.out.print("\033["+altezzaLineaRichiesta+";"+altezzaColonnaRichiesta+"H");
                    campoTmp = input.nextLine();

                    String ris = convalidaInput(campoTmp);
                    if(ris != null && ris.equals(":q"))
                        return false;

                    campiForm[indiceRisultati++] = ris;
                    altezzaColonnaRichiesta += 12;
                }

                altezzaColonnaRichiesta = 52;
                altezzaLineaRichiesta += 3;
            }else{
                System.out.print("\033["+altezzaLineaRichiesta+";"+altezzaColonnaRichiesta+"H");
                campoTmp = input.nextLine();
                
                String ris = convalidaInput(campoTmp);
                if(ris != null && ris.equals(":q"))
                        return false;
                    
                campiForm[indiceRisultati++] = ris;

                altezzaLineaRichiesta += 3;
            }
        }
        return true;
    }

    /**
     * In base a l'inserimento o meno dell'utente del campo controlla se deve annulla o compilare con null
     * @param campoTmp Input dell'utente che aspetta di essere convalidato 
     * @return Input dell'utente convalidato
     */
    public static String convalidaInput(String campoTmp){
        
        if(campoTmp.equals(":q")){
            CineMax.stackRecord.pop();
            return ":q";
        }

        if(campoTmp.equals("")){
            return null;
        }

        return campoTmp;
    }
    
// =========================================================================
// =========================================================================
}