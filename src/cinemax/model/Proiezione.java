package cinemax.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Proiezione {

    // ====================================================== 
    //                   Campi
    // ======================================================

        private LocalDate data;
        private LocalTime ora;
        private String titolo;
        private String genere;
        private String regista;
        private int anno;
        private int durata;
        private int etaMin;
        private int postiSala;
        private double prezzo;
        private int postiLiberi;

        private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        private static final DateTimeFormatter FORMATO_ORA = DateTimeFormatter.ofPattern("HH:mm");

        // ====================================================== 
        //                   costruttore
        // ======================================================
    
        /**
         * Costruttore completo per istanziare una proiezione con posti liberi pari ai posti sala
         * @param data          data dello spettacolo
         * @param oraStrs       ora dello spettacolo
         * @param titolo        titolo dello spettacolo
         * @param genere        genere dello spettacolo
         * @param regista       regista dello spettacolo
         * @param anno          anno registrazione dello spettacolo
         * @param durata        durata dello spettacolo
         * @param etaMin        eta minima dello spettacolo
         * @param prezzo        prezzo dello spettacolo
         * @param postiSala     posti della sala dello spettacolo
         * @param poastiLiberi  posti liberi sala dello spettacolo
         */
        public Proiezione(LocalDate data, String oraStr, String titolo, String genere, String regista, int anno, int durata, int etaMin, double prezzo, int postiSala, int postiLiberi) {
            this.data = data;
            this.ora = LocalTime.parse(oraStr.trim(), FORMATO_ORA);
            this.titolo = titolo;
            this.genere = genere;
            this.regista = regista;
            this.anno = anno;
            this.durata = durata;
            this.etaMin = etaMin;
            this.prezzo = prezzo;
            this.postiSala = postiSala;
            this.postiLiberi = postiLiberi;
        }

    // ====================================================== 
    //                   metodi getter
    // ======================================================
    /**
     * Restituisce la data programmata per la proiezione del film.
     * * @return un oggetto {@link LocalDate} che rappresenta la data della proiezione
     */
    public LocalDate getData()    { return data; }

    /**
     * Restituisce l'orario programmato per la proiezione del film.
     * * @return un oggetto {@link LocalTime} che rappresenta l'orario di inizio
     */
    public LocalTime getOra()        { return ora; }

    /**
     * Restituisce il titolo del film.
     * * @return il titolo del film come stringa
     */
    public String getTitolo()     { return titolo; }

    /**
     * Restituisce il genere cinematografico del film.
     * * @return il genere del film come stringa
     */
    public String getGenere()     { return genere; }

    /**
     * Restituisce il nome del regista del film.
     * * @return il regista del film come stringa
     */
    public String getRegista()    { return regista; }

    /**
     * Restituisce l'anno di uscita o di produzione del film.
     * * @return l'anno del film come valore intero
     */
    public int getAnno()          { return anno; }

    /**
     * Restituisce la durata complessiva del film espressa in minuti.
     * * @return la durata del film in minuti
     */
    public int getDurata()        { return durata; }

    /**
     * Restituisce l'età minima consigliata o richiesta per la visione del film.
     * * @return la soglia di età minima espressa in anni
     */
    public int getEtaMin()        { return etaMin; }

    /**
     * Restituisce la capienza totale di posti a sedere previsti all'interno della sala.
     * * @return il numero totale di posti della sala
     */
    public int getPostiSala()     { return postiSala; }

    /**
     * Restituisce il prezzo base del biglietto intero per assistere alla proiezione.
     * * @return il prezzo del biglietto come valore numerico reale
     */
    public double getPrezzo()     { return prezzo; }

    /**
     * Restituisce l'orario della proiezione formattato in formato testuale leggibile.
     * Utilizza il pattern standard definito dalla costante FORMATO_ORA.
     * * @return la stringa rappresentante l'orario formattato
     */
    public String getOraString()  { return ora.format(FORMATO_ORA); }

    /**
     * Restituisce il quantitativo residuo di posti ancora liberi e prenotabili per lo spettacolo.
     * * @return il numero di posti rimasti disponibili
     */
    public int getPostiLiberi() { return postiLiberi;}

    // ====================================================== 
    //                  metodi setter
    // ======================================================

    /**
     * Imposta o aggiorna la data programmata per la proiezione del film.
     * * @param data il nuovo oggetto {@link LocalDate} da associare allo spettacolo
     */
    public void setData(LocalDate data)       { this.data = data; }

    /**
     * Imposta o aggiorna l'orario della proiezione effettuando il parsing di una stringa.
     * La stringa viene ripulita da spazi superflui e convertita seguendo le specifiche di FORMATO_ORA.
     * * @param oraStr la stringa testuale contenente l'orario da convertire
     * @throws java.time.format.DateTimeParseException se la stringa non è conforme al formato atteso
     */
    public void setOra(String oraStr)          { this.ora = LocalTime.parse(oraStr.trim(), FORMATO_ORA); }

    /**
     * Imposta o aggiorna il titolo del film.
     * * @param titolo il nuovo titolo da assegnare
     */
    public void setTitolo(String titolo)      { this.titolo = titolo; }

    /**
     * Imposta o aggiorna il genere cinematografico del film.
     * * @param genere la stringa descrittiva del genere da impostare
     */
    public void setGenere(String genere)      { this.genere = genere; }

    /**
     * Imposta o aggiorna il nome del regista del film.
     * * @param regista il nome del regista da impostare
     */
    public void setRegista(String regista)    { this.regista = regista; }

    /**
     * Imposta o aggiorna l'anno di produzione del film.
     * * @param anno il valore numerico dell'anno da salvare
     */
    public void setAnno(int anno)              { this.anno = anno; }

    /**
     * Imposta o aggiorna la durata complessiva del film.
     * * @param durata il valore espresso in minuti da assegnare al film
     */
    public void setDurata(int durata)         { this.durata = durata; }

    /**
     * Imposta o aggiorna il vincolo di età minima per la visione della pellicola.
     * * @param etaMin la soglia di età minima da impostare
     */
    public void setEtaMin(int etaMin)         { this.etaMin = etaMin; }

    /**
     * Imposta o aggiorna la capienza complessiva di posti della sala cinematografica.
     * * @param postiSala il numero massimo di posti totali della sala
     */
    public void setPostiSala(int postiSala)   { this.postiSala = postiSala; }

    /**
     * Imposta o aggiorna il prezzo del biglietto stabilito per la visione del film.
     * * @param prezzo il valore monetario da assegnare al costo del biglietto
     */
    public void setPrezzo(double prezzo)      { this.prezzo = prezzo; }

    /**
     * Aggiorna lo stato dei posti ancora liberi disponibili alla vendita.
     * Viene ricalcolato o sovrascritto al variare delle prenotazioni attive.
     * * @param postiLiberi il nuovo quantitativo di posti attualmente liberi
     */
    public void setPostiLiberi(int postiLiberi) {this.postiLiberi = postiLiberi;}
    // ======================================================

        @Override
        public boolean equals(Object o){
            if(this==o) return true;
            if(!(o instanceof Proiezione)) return false;
            Proiezione that = (Proiezione) o;  
            return titolo.equalsIgnoreCase(that.titolo) && data.equals(that.data) && ora.equals(that.ora);
        }

        @Override
        public int hashCode(){
            return java.util.Objects.hash(titolo.toLowerCase(), data, ora);
        }

        @Override
        public String toString(){ //ritorna la proiezione

            return titolo + " ( " + data.format(FORMATO_DATA) + " : " + getOraString() + " ) |" + prezzo + " euro | età consigliata "  + etaMin + ")";
        }
}