package cinemax.model;

/**
 * Rappresenta la categoria di utenti proiezionisti registrati nel sistema CineMax.
 * Gestisce le informazioni anagrafiche, le credenziali di accesso
 * e le funzionalità principali che possono usare. 
 * @author Modena Matteo (Matricola: 765099 ) - VA
 * @author Baroncelli Luca (Matricola: 765099 ) - VA
 * @author Bin Alessio (Matricola: 762387 ) - VA
 */

public class Proiezionisti extends Utente{

// ======================================================
//          		  COSTRUTTORE
// ======================================================

    public Proiezionisti(String nome, String cognome, String username, String password, String dataDiNascita, String domicilio) {
        super(nome, cognome, username, password, dataDiNascita, domicilio, "proiezionista");
    }

// ======================================================
//          		  COSTRUTTORE SENZA CAMPI FACOLTATIVI
// ======================================================

    public Proiezionisti(String nome, String cognome, String username, String password, String domicilio) {
        super(nome, cognome, username, password, domicilio, "proiezionista");
    }

}