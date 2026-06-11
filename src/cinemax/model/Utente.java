package cinemax.model;

/**
 * Rappresenta un utente registrato nel sistema CineMax.
 * Gestisce le informazioni anagrafiche, le credenziali di accesso
 * e il ruolo gerarchico all'interno dell'applicazione. 
 * Il ruolo è modellato tramite enum per eliminare la dipendenza da stringhe che possono portare a errori indesiderati
 * @author Modena Matteo (Matricola: 765099 ) - VA
 * @author Baroncelli Luca (Matricola: 765099 ) - VA
 * @author Bin Alessio (Matricola: 762387 ) - VA
 */

public abstract class Utente {

	// ======================================================
	//          			ENUM PER I RUOLI 
	// ======================================================

	/**
     * Enumerazione dei ruoli disponibili all'interno del sistema CineMax.
     * Associa a ciascun ruolo un'etichetta testuale descrittiva.
     */
	public enum Ruolo {
		CLIENTE_OSPITE("cliente ospite"), 
		CLIENTE_REGISTRATO("cliente registrato"),
		BIGLIETTAIO("bigliettaio"),
		PROIEZIONISTA("proiezionista");

		private final String etichetta;

		/**
		 * Costruttore dell'enum Ruolo.
		 * @param etichetta l'etichetta testuale del ruolo
		 */
		Ruolo(String etichetta) {this.etichetta = etichetta;}

		/**
		 * Restituisce l'etichetta testuale associata al ruolo.
		 * @return l'etichetta del ruolo come stringa
		 */
		public String getEtichetta() {return etichetta;}

		/**
		 * Converte una stringa nell'istanza corrispondente dell'enum Ruolo.
		 * @param s la stringa da convertire
		 * @return l'istanza di {@link Ruolo} corrispondente
		 * @throws IllegalArgumentException se la stringa è null o non corrisponde a nessun ruolo noto
		 */
		public static Ruolo daString(String s){
			if(s == null)
				throw new IllegalArgumentException("Ruolo non può essere null.");
			for(Ruolo r : values()){
				if(r.etichetta.equalsIgnoreCase(s.trim())) return r;
			}
			throw new IllegalArgumentException("Ruolo sconosciuto: " + s);
		}

		@Override 
		public String toString() {return etichetta;}
	}
	// ======================================================
	//          			CAMPI
	// ======================================================

	private final String nome;
	private final String cognome;
	private final String username;
	private String password;	//memorizzata in formato cifrato
	private final String dataDiNascita;	//facoltativo
	private final String domicilio;
	private final Ruolo ruolo; //(cliente/proiezionista/bigliettaio)
		
	
	// ======================================================
	//          		  COSTRUTTORE
	// ======================================================

	/**
	 * Costruttore completo per inizializzare un utente con tutti i parametri anagrafici.
	 * @param nome           il nome dell'utente
	 * @param cognome        il cognome dell'utente
	 * @param username       l'username univoco dell'utente
	 * @param password       la password 
	 * @param dataDiNascita  la data di nascita 
	 * @param domicilio      il domicilio dell'utente
	 * @param ruolo          il ruolo gerarchico dell'utente
	 * @throws IllegalArgumentException se uno dei campi obbligatori (nome, cognome, username, password, ruolo) è null
	 */
	public Utente (String nome, String cognome, String username, String password, String dataDiNascita, String domicilio, Ruolo ruolo) {
		if(nome == null || cognome == null || username == null || password == null || ruolo == null)
			throw new IllegalArgumentException("Nome, Cognome, username, password e ruolo sono obbligatori.");
		this.nome = nome;
		this.cognome = cognome;
		this.username = username;
		this.password = password;
		this.dataDiNascita = dataDiNascita;
		this. domicilio = domicilio;
		this. ruolo = ruolo;
	}

	// ======================================================
	//          COSTRUTTORE SENZA CAMPI FACOLTATIVI
	// ======================================================

	/**
	 * Costruttore compatto senza il campo facoltativo del domicilio.
	 * @param nome      il nome dell'utente
	 * @param cognome   il cognome dell'utente
	 * @param username  l'username univoco dell'utente
	 * @param password  la password dell'utente
	 * @param dataDiNascita la data di anscita dell'utente
	 * @param ruolo     il ruolo gerarchico dell'utente
	 */
	public Utente(String nome, String cognome, String username, String password, String dataDiNascita, Ruolo ruolo) {
		this(nome, cognome, username, password, dataDiNascita, null, ruolo);
	}

	// ======================================================
	//          		SEZIONE GETTER
	// ======================================================

	/**
	 * Restituisce il nome dell'utente.
	 * @return il nome dell'utente
	 */
	public String getNome(){ return nome; }
	/**
	 * Restituisce il cognome dell'utente.
	 * @return il cognome dell'utente
	 */
	public String getCognome(){ return cognome; }
	/**
	 * Restituisce l'username dell'utente.
	 * @return l'username dell'utente
	 */
	public String getUsername(){ return username; }
	/**
	 * Restituisce la password cifrata dell'utente.
	 * @return la password cifrata
	 */
	public String getPassword(){ return password; }
	/**
	 * Restituisce la data di nascita dell'utente.
	 * @return la data di nascita
	 */
	public String getDataDiNascita(){ return dataDiNascita; }
    /**
	 * Restituisce il domicilio dell'utente.
	 * @return il domicilio dell'utente, oppure <code>null</code> se non specificata
	 */
	public String getDomicilio(){ return domicilio; }
	/**
	 * Restituisce il ruolo dell'utente.
	 * @return il ruolo dell'utente come istanza dell'enum {@link Ruolo}
	 */
	public Ruolo getRuolo(){ return ruolo; }


	// ======================================================
	//                   UTILI
	// ======================================================

	@Override
	public String toString(){
		return String.format("[%s] %s %s (%s)", ruolo, nome, cognome, username);
	}
}
