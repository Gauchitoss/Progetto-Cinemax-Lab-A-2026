package cinemax.model;

/**
 * Rappresenta la categoria di utenti clienti non registrati nel sistema CineMax.
 * Gestisce le informazioni anagrafiche, le credenziali di accesso
 * * e le funzionalità principali che possono usare. 
 * @author Modena Matteo (Matricola: 765099 ) - VA
 * @author Baroncelli Luca (Matricola: 765099 ) - VA
 * @author Bin Alessio (Matricola: 762387 ) - VA
 */

public class ClientiOspiti extends Clienti{
        
    // ======================================================
    //          		  COSTRUTTORE
    // ======================================================

    /**
	 * Costruttore completo per definire un cliente ospite con data di nascita.
	 * Inizializza automaticamente il ruolo a {@link Utente.Ruolo#CLIENTE_OSPITE}.
	 * @param nome          il nome attribuito all'ospite
	 * @param cognome       il cognome attribuito all'ospite
	 * @param username      l'username per la sessione ospite
	 * @param password      la password fittizia per l'ospite
	 * @param dataDiNascita la data di nascita
	 * @param domicilio     il domicilio dell'ospite
	 */
    public ClientiOspiti(String nome, String cognome, String username, String password, String dataDiNascita, String domicilio) {
        super(nome, cognome, username, password, dataDiNascita, domicilio, Ruolo.CLIENTE_OSPITE);
    }

    // ======================================================
    //          		  COSTRUTTORE SENZA CAMPI FACOLTATIVI
    // ======================================================

    /**
	 * Costruttore compatto per definire un cliente ospite senza data di nascita.
	 * Inizializza automaticamente il ruolo a {@link Utente.Ruolo#CLIENTE_OSPITE}.
	 * @param nome      il nome attribuito all'ospite
	 * @param cognome   il cognome attribuito all'ospite
	 * @param username  l'username per la sessione ospite
	 * @param password  la password fittizia per l'ospite
	 * @param domicilio il domicilio dell'ospite
	 */
    public ClientiOspiti(String nome, String cognome, String username, String password, String domicilio) {
        super(nome, cognome, username, password, domicilio, Ruolo.CLIENTE_OSPITE);
    }
    
}
