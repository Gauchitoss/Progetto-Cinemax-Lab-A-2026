package cinemax.model;

/**
 * Rappresenta una prenotazione effettuata nel sistema CineMax.
 * Collega logicamente un Utente a una specifica Proiezione.
 * * @author Modena Matteo (Matricola: 765099) - VA
 * @author Baroncelli Luca (Matricola: 765099) - VA
 * @author Bin Alessio (Matricola: 762387) - VA
 */
public class Prenotazione {

    private String codiceUnivoco;
    private Utente cliente;
    private Proiezione proiezione;
    private int numeroBiglietti;

    public Prenotazione(String codiceUnivoco, Utente cliente, Proiezione proiezione, int numeroBiglietti) {
        this.codiceUnivoco = codiceUnivoco;
        this.cliente = cliente;
        this.proiezione = proiezione;
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
        return this.numeroBiglietti * proiezione.getPrezzo(); 
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
    public String toCSV(java.time.format.DateTimeFormatter formatter) {
        return codiceUnivoco + "|" + 
               cliente.getUsername() + "|" + 
               proiezione.getData().format(formatter) + "|" + 
               proiezione.getOra() + "|" + 
               proiezione.getTitolo() + "|" + 
               numeroBiglietti;
    }

    @Override
    public String toString() {
        return "Prenotazione [" + codiceUnivoco + "] - " + getTitoloFilm() + 
               " del " + proiezione.getData() + " ore " + proiezione.getOra() + 
               " | Biglietti: " + numeroBiglietti + " | Totale: " + getCostoTotale() + "€";
    }
}