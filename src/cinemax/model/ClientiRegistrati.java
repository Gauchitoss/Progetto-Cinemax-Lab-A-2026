package cinemax.model;

/**
 * Rappresenta la categoria di utenti clienti registrati nel sistema CineMax.
 * Gestisce le informazioni anagrafiche, le credenziali di accesso
 * * e le funzionalità principali che possono usare. 
 * @author Modena Matteo (Matricola: 765099 ) - VA
 * @author Baroncelli Luca (Matricola: 765099 ) - VA
 * @author Bin Alessio (Matricola: 762387 ) - VA
 */

public class ClientiRegistrati extends Clienti {
    
// ======================================================
//          		  COSTRUTTORE
// ======================================================

    public ClientiRegistrati(String nome, String cognome, String username, String password, String dataDiNascita, String domicilio) {
        super(nome, cognome, username, password, dataDiNascita, domicilio, Ruolo.CLIENTE_REGISTRATO);
    }

// ======================================================
//          		  COSTRUTTORE SENZA CAMPI FACOLTATIVI
// ======================================================

    public ClientiRegistrati(String nome, String cognome, String username, String password, String domicilio) {
        super(nome, cognome, username, password, domicilio, Ruolo.CLIENTE_REGISTRATO);
    }
    
}
