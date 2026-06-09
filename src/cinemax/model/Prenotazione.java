package cinemax.model;

import java.time.format.DateTimeFormatter;
/**
 * Rappresenta una prenotazione effettuata nel sistema CineMax.
 * Collega logicamente un Utente a una specifica Proiezione.
 * * @author Modena Matteo (Matricola: 765099) - VA
 * @author Baroncelli Luca (Matricola: 765099) - VA
 * @author Bin Alessio (Matricola: 762387) - VA
 */
public class Prenotazione {

    private final String codiceUnivoco;
    private final Utente cliente;
    private Proiezione proiezione;
    private final int numeroBiglietti;
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd-MM-yyyy");

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
    public String getCodiceUnivoco() { return codiceUnivoco; }
    public Utente getCliente() { return cliente; }
    public Proiezione getProiezione() { return proiezione; }
    public int getNumeroBiglietti() { return numeroBiglietti; }
    // Metodi di delega per facilitare le stampe e i filtri
    public String getUsernameCliente() { return cliente.getUsername(); }
    public String getNomeCliente() { return cliente.getNome(); }
    public String getCognomeCliente() { return cliente.getCognome(); }
    public String getTitoloFilm() { return proiezione.getTitolo(); }
    public double getCostoTotale() { 
        return numeroBiglietti * proiezione.getPrezzo(); 
    }

    // ====================================================== 
    //                  Metodo Setter
    // ======================================================
    public void setProiezione(Proiezione nuovaProiezione) {
        this.proiezione = nuovaProiezione;
    }

    // ====================================================== 
    //               Metodo di Persistenza CSV
    // ======================================================
    /**
     * Serializza l'oggetto usando il separatore barretta verticale '|' 
     * per essere uniformi con GestoreProiezione.
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