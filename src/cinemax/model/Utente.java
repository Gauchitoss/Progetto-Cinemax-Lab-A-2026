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
	public enum Ruolo {
		CLIENTE_OSPITE("cliente ospite"), 
		CLIENTE_REGISTRATO("cliente registrato"),
		BIGLIETTAIO("bigliettaio"),
		PROIEZIONISTA("proiezionista");

		private final String etichetta;

		Ruolo(String etichetta) {this.etichetta = etichetta;}

		public String getEtichetta() {return etichetta;}

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
	private String password;//verrà successivamente cifrata  
	private final String dataDiNascita; //sarà (facoltativa)
	private final String domicilio;
	private final Ruolo ruolo; //(cliente/proiezionista/bigliettaio)
		
	
// ======================================================
//          		  COSTRUTTORE
// ======================================================

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
//          		  COSTRUTTORE SENZA CAMPI FACOLTATIVI
// ======================================================

	public Utente(String nome, String cognome, String username, String password, String domicilio, Ruolo ruolo) {
		this(nome, cognome, username, password, null, domicilio, ruolo);
	}

// ======================================================
//          		SEZIONE GETTER
// ======================================================

	public String getNome(){
		return nome;
}
	public String getCognome(){
		return cognome;
	}
	public String getUsername(){
		return username;
	}
	public String getPassword(){
		return password;
	}
	public String getDataDiNascita(){
		return dataDiNascita;
	}
	public String getDomicilio(){
		return domicilio;
	}
	public Ruolo getRuolo(){
		return ruolo;
	}

// ======================================================
//          		SEZIONE SETTER
// ======================================================

	public void setPassword(String nuovaPassword){
		if(nuovaPassword == null || nuovaPassword.isEmpty())
			throw new IllegalArgumentException("La nuova password non può essere vuota.");
		this.password = nuovaPassword;
	}

// ======================================================
//                   UTILI
// ======================================================

	@Override
	public String toString(){
		return String.format("[%s] %s %s (%s)", ruolo, nome, cognome, username);
	}
}
