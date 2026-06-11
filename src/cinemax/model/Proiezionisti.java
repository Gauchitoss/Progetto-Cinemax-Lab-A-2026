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
    /**
     * Costruttore completo per istanziare un Proiezionista con anche la data di nascita.
     * Imposta automaticamente il ruolo gerarchico su PROIEZIONISTA.
     * @param nome          il nome del proiezionista
     * @param cognome       il cognome del proiezionista
     * @param username      l'username univoco per l'accesso del proiezionista
     * @param password      la password dell'account (memorizzata cifrata)
     * @param dataDiNascita la data di nascita del proiezionista (campo opzionale)
     * @param domicilio     il domicilio del proiezionista
     */
    public Proiezionisti(String nome, String cognome, String username, String password, String dataDiNascita, String domicilio) {
        super(nome, cognome, username, password, dataDiNascita, domicilio, Ruolo.PROIEZIONISTA);
    }

// ======================================================
//          COSTRUTTORE SENZA CAMPI FACOLTATIVI
// ======================================================
    /**
     * Costruttore compatto per istanziare un Proiezionista senza specificare la data di nascita.
     * Imposta automaticamente il ruolo gerarchico su PROIEZIONISTA.
     * @param nome      il nome del proiezionista
     * @param cognome   il cognome del proiezionista
     * @param username  l'username univoco per l'accesso del proiezionista
     * @param password  la password dell'account
     * @param domicilio il domicilio del proiezionista
     */
    public Proiezionisti(String nome, String cognome, String username, String password, String domicilio) {
        super(nome, cognome, username, password, domicilio, Ruolo.PROIEZIONISTA);
    }

}