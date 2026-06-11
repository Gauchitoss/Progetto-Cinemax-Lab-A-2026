package cinemax.model;

/**
 * Rappresenta la categoria di utenti bigliettai registrati nel sistema CineMax.
 * Gestisce le informazioni anagrafiche, le credenziali di accesso
 * e le funzionalità principali che possono usare. 
 * @author Modena Matteo (Matricola: 765099 ) - VA
 * @author Baroncelli Luca (Matricola: 765099 ) - VA
 * @author Bin Alessio (Matricola: 762387 ) - VA
 */

public class Bigliettai extends Utente{

    // ======================================================
    //          		  COSTRUTTORE
    // ======================================================

    /**
     * Costruttore completo per istanziare un Bigliettaio.
     * Imposta automaticamente il ruolo gerarchico su BIGLIETTAIO.
     * @param nome          il nome del bigliettaio
     * @param cognome       il cognome del bigliettaio
     * @param username      l'username univoco per l'accesso del bigliettaio
     * @param password      la password dell'account (memorizzata cifrata)
     * @param dataDiNascita la data di nascita del bigliettaio (campo opzionale)
     * @param domicilio     il domicilio del bigliettaio
     */
    public Bigliettai(String nome, String cognome, String username, String password, String dataDiNascita, String domicilio) {
        super(nome, cognome, username, password, dataDiNascita, domicilio, Ruolo.BIGLIETTAIO);
    }

// ======================================================
//          	COSTRUTTORE SENZA CAMPI FACOLTATIVI
// ======================================================
/**
     * Costruttore compatto per istanziare un Bigliettaio senza specificare la data di nascita.
     * Imposta automaticamente il ruolo gerarchico su BIGLIETTAIO.
     * @param nome      il nome del bigliettaio
     * @param cognome   il cognome del bigliettaio
     * @param username  l'username univoco per l'accesso del bigliettaio
     * @param password  la password dell'account
     * @param domicilio il domicilio del bigliettaio
     */
    public Bigliettai(String nome, String cognome, String username, String password, String domicilio) {
        super(nome, cognome, username, password, domicilio, Ruolo.BIGLIETTAIO);
    }
    
}
