package cinemax.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import cinemax.CineMax;
import cinemax.LogicaStatiManager;
import cinemax.CostantiForm.Campi;
import cinemax.MenuManager.StatoMenu;
import cinemax.model.Utente;
import cinemax.util.GestoreUtenti;

/**
 * Controller utilzzato per gestione dei flussi di autenticazione e registrazione del sistema CineMax.
 * Coordina l'interazione tra i form dell'interfaccia utente (TUI) e le funzioni di logica
 * per il login e la creazione di nuovi account di tipo cliente.
 * @author Modena Matteo (Matricola: 765099) - VA
 * @author Baroncelli Luca (Matricola: 761582) - VA
 * @author Bin Alessio (Matricola: 762387) - VA
 */
public class AutenticazioniController {

    public static Utente utente;

    /**
     * Elabora i dati immessi nel form di login per autenticare un utente all'interno del sistema.
     * In caso di credenziali corrette, aggiorna lo stato corrente del menu e il ruolo globale; 
     * in caso di errore, imposta il relativo messaggio testuale e reindirizza alla schermata di errore.
     * @param datiForm un array di stringhe contenente l'username (indice 0) e la password (indice 1)
     */
    public static void gestisciLogin(String[] datiFormTmp) {
        String username = datiFormTmp[Campi.LOGIN_USER.i];
        String password = datiFormTmp[Campi.LOGIN_PASSWORD.i];
        Utente u = GestoreUtenti.login(username, password);
        if(u == null){
            LogicaStatiManager.messaggioErroreCorrente = "Credenziali non valide o utente inesistente.";
            CineMax.stackRecord.push(StatoMenu.STATO_ERRORE);
            return;
        }
        switch (u.getRuolo()) {
            case PROIEZIONISTA:       CineMax.stackRecord.push(StatoMenu.MENU_PROIEZIONISTA);  break;
            case CLIENTE_REGISTRATO:  CineMax.stackRecord.push(StatoMenu.MENU_CLIENTI);       break;
            case BIGLIETTAIO:         CineMax.stackRecord.push(StatoMenu.MENU_BIGLIETTAIO);   break;
            default:
                LogicaStatiManager.messaggioErroreCorrente = "Ruolo non supportato.";
                CineMax.stackRecord.push(StatoMenu.STATO_ERRORE);
                return;
        }
        CineMax.ruolo = u.getRuolo();
        utente = u;
    }

    /**
     * Elabora i dati immessi nel form di registrazione per iscrivere un nuovo cliente registrato.
     * Valida le informazioni tramite il gestore di persistenza e reindirizza l'utente allo stato di conferma o di errore.
     * * @param datiForm un array di stringhe contenente nell'ordine: nome, cognome, username, password, conferma password, data di nascita e domicilio
     */
    public static void gestisciRegistrazione(String[] datiFormTmp){
        try{
            String  nome                = datiFormTmp[Campi.REG_NOME.i];
            String  cognome             = datiFormTmp[Campi.REG_COGNOME.i];
            String  username            = datiFormTmp[Campi.REG_USERNAME.i];
            String  password            = datiFormTmp[Campi.REG_PASSWORD.i];
            String  confermaPassword    = datiFormTmp[Campi.REG_CONFERMA_PASSWORD.i];
            String  giornoNascita       = datiFormTmp[Campi.REG_GIORNO_NASCITA.i];
            String  meseNascita         = datiFormTmp[Campi.REG_MESE_NASCITA.i];
            String  annoNascita         = datiFormTmp[Campi.REG_ANNO_NASCITA.i];
            String  domicilio           = datiFormTmp[Campi.REG_DOMICILIO.i];

            String dataNascita = null; //all'inizio viene impostata come null

            if (giornoNascita != null && meseNascita != null && annoNascita != null) {
                //rimozion e spazi extra
                String g = giornoNascita.trim();
                String m = meseNascita.trim();
                String a = annoNascita.trim();

                // Pad a 2 cifre per giorno e mese se inseriti come "1" anziché "01"
                if (g.length() == 1) g = "0" + g;
                if (m.length() == 1) m = "0" + m;
                
                String dataNascitaTmp = g + "-" + m + "-" + a;

                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    LocalDate.parse(dataNascitaTmp, formatter);

                    dataNascita = dataNascitaTmp;
                } catch (DateTimeParseException e) {
                    throw new IllegalArgumentException("La data di nascita inserita non è valida (es. usa GG-MM-AAAA).");
                }
            }
            GestoreUtenti.registraUtente(nome, cognome, username, password, confermaPassword, dataNascita, domicilio);
            //Auto login dopo la registrazione
            utente = GestoreUtenti.cercaPerUsername(username);
            CineMax.ruolo = Utente.Ruolo.CLIENTE_REGISTRATO;
            CineMax.stackRecord.clear();
            CineMax.stackRecord.push(StatoMenu.BENVENUTO);      // Reset di sicurezza
            CineMax.stackRecord.push(StatoMenu.MENU_CLIENTI);

        

        }catch(IllegalArgumentException e){
            LogicaStatiManager.messaggioErroreCorrente = e.getMessage();
            CineMax.stackRecord.push(StatoMenu.STATO_ERRORE);
        }catch(Exception e){
            LogicaStatiManager.messaggioErroreCorrente = "Errore imprevisto durante la registrazione.";
            CineMax.stackRecord.push(StatoMenu.STATO_ERRORE);
        }
    }
    
}
