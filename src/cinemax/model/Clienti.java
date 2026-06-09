package cinemax.model;

/**
 * Rappresenta la categoria di utenti clienti nel sistema CineMax.
 * Gestisce le informazioni anagrafiche, le credenziali di accesso
 * @author Modena Matteo (Matricola: 765099 ) - VA
 * @author Baroncelli Luca (Matricola: 765099 ) - VA
 * @author Bin Alessio (Matricola: 762387 ) - VA
 */

public abstract class Clienti extends Utente{
    
// ======================================================
//          		  COSTRUTTORE
// ======================================================

    public Clienti(String nome, String cognome, String username, String password, String dataDiNascita, String domicilio, Ruolo ruolo) {
        super(nome, cognome, username, password, dataDiNascita, domicilio, ruolo);
    }

// ======================================================
//          		  COSTRUTTORE SENZA CAMPI FACOLTATIVI
// ======================================================

    public Clienti(String nome, String cognome, String username, String password, String domicilio, Ruolo ruolo) {
        super(nome, cognome, username, password, null, domicilio, ruolo);
    }
    
}
