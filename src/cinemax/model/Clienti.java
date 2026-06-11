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

    /**
	 * Costruttore completo per la classe Clienti.
	 * @param nome           il nome del cliente
	 * @param cognome        il cognome del cliente
	 * @param username       l'username del cliente
	 * @param password       la password del cliente
	 * @param dataDiNascita  la data di nascita del cliente
	 * @param domicilio      il domicilio del cliente
	 * @param ruolo          il ruolo specifico (es. OSPITE o REGISTRATO)
	 */
    public Clienti(String nome, String cognome, String username, String password, String dataDiNascita, String domicilio, Ruolo ruolo) {
        super(nome, cognome, username, password, dataDiNascita, domicilio, ruolo);
    }

// ======================================================
//          COSTRUTTORE SENZA CAMPI FACOLTATIVI
// ======================================================

    /**
	 * Costruttore compatto per la classe Clienti senza la data di nascita facoltativa.
	 * @param nome      il nome del cliente
	 * @param cognome   il cognome del cliente
	 * @param username  l'username del cliente
	 * @param password  la password del cliente
	 * @param domicilio il domicilio del cliente
	 * @param ruolo     il ruolo specifico
	 */
    public Clienti(String nome, String cognome, String username, String password, String domicilio, Ruolo ruolo) {
        super(nome, cognome, username, password, null, domicilio, ruolo);
    }
    
}
