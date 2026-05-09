package cinemax;


//definisco la classe utente e i vari attributi

public class Utente {
	private String nome;
	private String cognome;
	private String username;
	private String password;//verrà successivamente cifrata  
	private String dataDiNascita; //sarà (facoltativa)
	private String domicilio;
	private String ruolo; //(cliente/proiezionista/bigliettaio)
	
//definisco il costrutture con i paremetri in ingresso e li assegno ai vari attributi 	
	
public Utente (String nome, String cognome, String username, String password, String dataDiNascita, String domicilio,String ruolo) {
	this.nome =nome;
	this.cognome =cognome;
	this.username =username;
	this.password =password;
	this.dataDiNascita =dataDiNascita;
	this. domicilio=domicilio;
	this. ruolo=ruolo;
}

//creo i vari getter per ogni attributo (ci servirà per dopo)

public String getNome() {return nome;}
public String getCognome() {return cognome;}
public String getUsername() {return username;}
public String getPassword() {return password;}
public String getDataDiNascita() {return dataDiNascita;}
public String getDomicilio() {return domicilio;}
public String getRuolo() {return ruolo;}
}
