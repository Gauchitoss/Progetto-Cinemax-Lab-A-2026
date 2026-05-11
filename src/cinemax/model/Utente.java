package cinemax.model;

/**
 * Rappresenta un utente registrato nel sistema CineMax.
 * Gestisce le informazioni anagrafiche, le credenziali di accesso
 * e il ruolo gerarchico all'interno dell'applicazione. 
 * @author Modena Matteo (Matricola: 765099 ) - VA
 * @author Baroncelli Luca (Matricola: 765099 ) - VA
 * @author Bin Alessio (Matricola: 762387 ) - VA
 */

public class Utente {

// ======================================================
//          			CAMPI
// ======================================================

	private String nome;
	private String cognome;
	private String username;
	private String password;//verrà successivamente cifrata  
	private String dataDiNascita; //sarà (facoltativa)
	private String domicilio;
	private String ruolo; //(cliente/proiezionista/bigliettaio)
		
	
// ======================================================
//          		  COSTRUTTORE
// ======================================================

	public Utente (String nome, String cognome, String username, String password, String dataDiNascita, String domicilio,String ruolo) {
		this.nome =nome;
		this.cognome =cognome;
		this.username =username;
		this.password =password;
		this.dataDiNascita =dataDiNascita;
		this. domicilio=domicilio;
		this. ruolo=ruolo;
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
	public String getRuolo(){
		return ruolo;
	}

// ======================================================
// ======================================================
}
