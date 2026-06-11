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

    /**
	 * Costruttore completo per istanziare un nuovo cliente registrato.
	 * Imposta in automatico il ruolo a {@link Utente.Ruolo#CLIENTE_REGISTRATO}.
	 * @param nome           il nome del cliente
	 * @param cognome        il cognome del cliente
	 * @param username       l'username scelto dall'utente
	 * @param password       la password dell'utente (già cifrata o da cifrare)
	 * @param dataDiNascita  la data di nascita del cliente
	 * @param domicilio      il domicilio del cliente
	 */
    public ClientiRegistrati(String nome, String cognome, String username, String password, String dataDiNascita, String domicilio) {
        super(nome, cognome, username, password, dataDiNascita, domicilio, Ruolo.CLIENTE_REGISTRATO);
    }

    // ======================================================
    //          COSTRUTTORE SENZA CAMPI FACOLTATIVI
    // ======================================================
    
    /**
	 * Costruttore compatto per istanziare un cliente registrato senza data di nascita.
	 * Imposta in automatico il ruolo a {@link Utente.Ruolo#CLIENTE_REGISTRATO}.
	 * @param nome      il nome del cliente
	 * @param cognome   il cognome del cliente
	 * @param username  l'username scelto dall'utente
	 * @param password  la password dell'utente
	 * @param domicilio il domicilio del cliente
	 */
    public ClientiRegistrati(String nome, String cognome, String username, String password, String domicilio) {
        super(nome, cognome, username, password, domicilio, Ruolo.CLIENTE_REGISTRATO);
    }
    
}