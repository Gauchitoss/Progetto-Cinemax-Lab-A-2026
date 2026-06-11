package cinemax.model;

import java.time.format.DateTimeFormatter;
import java.time.LocalTime;
import java.time.LocalDate;
/**
 * Rappresenta una prenotazione effettuata nel sistema CineMax.
 * Collega logicamente un Utente a una specifica Proiezione.
 * @author Modena Matteo (Matricola: 765099) - VA
 * @author Baroncelli Luca (Matricola: 765099) - VA
 * @author Bin Alessio (Matricola: 762387) - VA
 */
public class Prenotazione {

    // ====================================================== 
    //                  CAMPI
    // ======================================================

    private final String codiceUnivoco;
    private final Utente cliente;
    private Proiezione proiezione;
    private final int numeroBiglietti;
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    // ====================================================== 
    //                  COSTRUTTORE
    // ======================================================

    /**
     * Costruttore completo per istanziare un oggetto Prenotazione.
     * @param codiceUnivoco   il codice alfanumerico univoco della prenotazione
     * @param cliente         l'utente acquirente associato
     * @param proiezione      la proiezione cinematografica scelta
     * @param numeroBiglietti il quantitativo di biglietti da riservare
     * @throws IllegalArgumentException se il numero di biglietti richiesto è minore o uguale a zero
     */
    public Prenotazione(String codiceUnivoco, Utente cliente, Proiezione proiezione, int numeroBiglietti) {
        this.codiceUnivoco = codiceUnivoco;
        this.cliente = cliente;
        this.proiezione = proiezione;
        if(numeroBiglietti <= 0)
            throw new IllegalArgumentException("Il numero di biglietti deve essere mggiore di 0.");
        this.numeroBiglietti = numeroBiglietti;
    }

    // ====================================================== 
    //                  Metodi Getter
    // ======================================================

/**
     * Restituisce il codice univoco della prenotazione.
     * * @return il codice alfanumerico identificativo
     */
    public String getCodiceUnivoco() { return codiceUnivoco; }

    /**
     * Restituisce l'oggetto utente cliente che ha effettuato la prenotazione.
     * * @return l'istanza di {@link Utente} associata
     */
    public Utente getCliente() { return cliente; }

    /**
     * Restituisce la proiezione cinematografica prenotata.
     * * @return l'istanza di {@link Proiezione} associata
     */
    public Proiezione getProiezione() { return proiezione; }

    /**
     * Restituisce la quantità di biglietti riservati.
     * * @return il numero di posti/biglietti prenotati
     */
    public int getNumeroBiglietti() { return numeroBiglietti; }

    // Metodi di delega per facilitare le stampe e i filtri

    /**
     * Restituisce l'username dell'utente associato, ereditato tramite delega.
     * * @return l'username del cliente proprietario
     */
    public String getUsernameCliente() { return cliente.getUsername(); }

    /**
     * Restituisce il nome dell'utente associato, ereditato tramite delega.
     * * @return il nome proprio del cliente
     */
    public String getNomeCliente() { return cliente.getNome(); }

    /**
     * Restituisce il cognome dell'utente associato, ereditato tramite delega.
     * * @return il cognome del cliente
     */
    public String getCognomeCliente() { return cliente.getCognome(); }

    /**
     * Restituisce il titolo del film della proiezione acquistata, ereditato tramite delega.
     * * @return il titolo del film in programmazione
     */
    public String getTitoloFilm() { return proiezione.getTitolo(); }

    /**
     * Restituisce l'orario di inizio dello spettacolo associato, ereditato tramite delega.
     * * @return un oggetto {@link LocalTime} con l'ora dello spettacolo
     */
    public LocalTime getOra() {return proiezione.getOra();}

    /**
     * Restituisce la data dello spettacolo associato, ereditato tramite delega.
     * * @return un oggetto {@link LocalDate} con la data dello spettacolo
     */
    public LocalDate getData() {return proiezione.getData();}

    /**
     * Calcola e restituisce il costo totale complessivo della prenotazione.
     * Il valore è ottenuto moltiplicando la quantità dei biglietti per il prezzo della singola proiezione.
     * * @return il costo complessivo totale della prenotazione come valore decimale
     */
    public double getCostoTotale() { 
        return numeroBiglietti * proiezione.getPrezzo(); 
    }

    // ====================================================== 
    //                  Metodo Setter
    // ======================================================

    /**
     * Sostituisce o aggiorna la proiezione associata a questa specifica prenotazione.
     * * @param nuovaProiezione la nuova istanza di {@link Proiezione} da impostare
     */
    public void setProiezione(Proiezione nuovaProiezione) {
        this.proiezione = nuovaProiezione;
    }

    // ====================================================== 
    //               Metodo di Persistenza CSV
    // ======================================================
    
    /**
     * Serializza l'oggetto usando il separatore barretta verticale '|' 
     * per essere uniformi con GestoreProiezione.
     * * @return la stringa formattata in formato CSV adatta al salvataggio su file
     */
    public String toCSV() {
        return codiceUnivoco + "|" + getNomeCliente() + "|" + getCognomeCliente() + "|" + getUsernameCliente() + "|" + proiezione.getData().format(FORMATO_DATA) + "|" + proiezione.getOraString() + "|" + proiezione.getTitolo() + "|" + numeroBiglietti;
    }

    @Override
    public String toString() {
        return "[" + codiceUnivoco + "] " + getTitoloFilm() + 
               " | " + getUsernameCliente() + 
               " | " + proiezione.getData() + " " + proiezione.getOra() + 
               " | Q.ta: " + numeroBiglietti + " | " + getCostoTotale() + "EURO";
    }
}