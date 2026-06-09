package cinemax.controller;

import cinemax.CineMax;
import cinemax.LogicaStatiManager;
import cinemax.CostantiForm.Campi;
import cinemax.MenuMangaer.StatoMenu;
import cinemax.model.Utente;
import cinemax.util.GestoreUtenti;

public class AutenticazioniController {

    public static Utente utente;

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
            case PROIEZIONISTA:       CineMax.stackRecord.push(StatoMenu.MENU_PROEZIONISTA);  break;
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

    public static void gestisciRegistrazione(String[] datiFormTmp){
        try{
            String  nome                = datiFormTmp[Campi.REG_NOME.i];
            String  cognome             = datiFormTmp[Campi.REG_COGNOME.i];
            String  username            = datiFormTmp[Campi.REG_USERNAME.i];
            String  password            = datiFormTmp[Campi.REG_PASSWORD.i];
            String  confermaPassword    = datiFormTmp[Campi.REG_CONFERMA_PASSWORD.i];
            String  dataNascita         = datiFormTmp[Campi.REG_DATA.i];
            String  domicilio           = datiFormTmp[Campi.REG_DOMICILIO.i];
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
