
package cinemax;

import java.util.*;
import cinemax.MenuManager.StatoMenu;
import cinemax.model.Utente;

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
    public static String messaggioErroreCorrente = "Spiacenti si è verificato un errore.";
    public static String messaggioConfermaCorrente = "Operazione eseguita con successo.";
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
                CineMax.ruolo = Utente.Ruolo.CLIENTE_OSPITE;
                messaggioErroreCorrente = "Spiacenti, si è verificato un errore.";
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
     * In caso una delle opzioni debba generare un triplo form in linea, ne calcola lo spostamento
     * anche laterale, utilizzato per la richiesta della data.
     * @param campiForm Array dove salvare cosa ha scritto l'utente
     * @param opzioniStatoMenu Array contenente tutte le opzioni dello stato del programma
     * @return boolean true se il form è stato compilato con successo, false se l'utente ha inserito :q per annullare
     */
    public static boolean prendiDatiForm(String[] campiForm, String[] opzioniStatoMenu){
        // CONFIGURAZIONE DEL CURSORE
        int cursoreY = 11; // riga di partenza dall'alto
        boolean dueColonne = opzioniStatoMenu.length >= 8; // attivazione della doppia colonna
        
        int larghezzaMeu = CinemaxTUI.LARGHEZZA_MENU; 
        int larghezzaBox = CinemaxTUI.LARGHEZZA_BOX_INPUT; 
        int spazioCentrale = 6;

        // CALCOLO DELLE X
        int paddingCentrale = Math.max(0, (larghezzaMeu - larghezzaBox) / 2);
        int paddingDueColonne = Math.max(0, (larghezzaMeu - (larghezzaBox * 2 + spazioCentrale)) / 2);
        
        // CALCOLO POSIZIONI CURSORE IN COLONNE
        int offsetInternoBox = 5;
        int colCentro = 1 + paddingCentrale + offsetInternoBox; 
        int colSx = 1 + paddingDueColonne + offsetInternoBox;
        int colDx = colSx + larghezzaBox + spazioCentrale;
        
        String campoTmp = "";

        // CICLO LETTURA DEI CAMPI
        for(int indiceOpzioni = 0, indiceRisultati = 0; indiceOpzioni < opzioniStatoMenu.length; indiceOpzioni ++){
            
            // Determina in quale colonna posizionare il cursore
            int cursoreX = colCentro;
            if (dueColonne) {
                cursoreX = (indiceOpzioni % 2 == 0)? colSx : colDx;
            }
            
            String nomeCampo = opzioniStatoMenu[indiceOpzioni].toUpperCase();
            
            // Prendita ei vari input
            if(nomeCampo.startsWith("DATAINIZIO") || nomeCampo.startsWith("DATAFINE")){
                // formato speciale per le date 3 box vicini
                for(int i = 0; i<3; i++){
                    System.out.print("\033[" + cursoreY + ";" + cursoreX + "H");
                    campoTmp = input.nextLine();

                    String ris = convalidaInput(campoTmp);
                    if(ris != null && ris.equals(":q")) return false;

                    campiForm[indiceRisultati++] = ris;
                    cursoreX += 13; // Spostamento laterale per i boxini GG-MM-AAAA
                }
            } else {
                System.out.print("\033[" + cursoreY + ";" + cursoreX + "H");
                campoTmp = input.nextLine();
                
                String ris = convalidaInput(campoTmp);
                if(ris != null && ris.equals(":q")) return false;
                    
                campiForm[indiceRisultati++] = ris;
            }

            // Aggiornamento della riga Y.
            // scendiamo di 4 righe solo se:
            // - modalita 1 colonna
            // - oppure appena compilata la colonna di destra %2 != 0
            // - oppure utlimo campo nella lista
            if (!dueColonne || indiceOpzioni % 2 != 0 || indiceOpzioni == opzioniStatoMenu.length - 1) {
                cursoreY += 4;
            }
        }
        return true;
    }

    /**
     * Convalida e sanifica l'input dell'utente per evitare un crash della TUI
     * In base a l'inserimento o meno dell'utente del campo controlla se deve annulla o compilare con null
     * @param campoTmp Input dell'utente che aspetta di essere convalidato 
     * @return Input dell'utente convalidato
     */
    public static String convalidaInput(String campoTmp){
        if(campoTmp == null) return null;

        // Rimozione eventuali a-capo (copia-incolla multiriga accidentale)
        campoTmp = campoTmp.replace("\n", "").replace("\r", "");

        // PROTEZIONE DATABASE: Rimuovo i caratteri usati come separatori nei file CSV
        campoTmp = campoTmp.replace("|", "-").replace(",", ".");

        campoTmp = campoTmp.trim();

        if(campoTmp.trim().equals(":q")){
            return ":q";
        }
        if(campoTmp.isEmpty()){
            return null;
        }

        // PROTEZIONE DELL'INTERFACCIA, tronca stringhe troppo lunghe
        if(campoTmp.length() > 45){
            campoTmp = campoTmp.substring(0, 45);
        }
        return campoTmp;
    }
    
// =========================================================================
// =========================================================================
}