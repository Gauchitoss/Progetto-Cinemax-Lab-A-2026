package cinemax.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Proiezione {

// ====================================================== 
//                   Campi
// ======================================================

        private LocalDate data;
        private String ora;
        private String titolo;
        private String genere;
        private String regista;
        private int anno;
        private int durata;
        private int etaMin;
        private int postiSala;
        private double prezzo;
        private static final DateTimeFormatter FORMATTA_DATA = DateTimeFormatter.ofPattern("dd-MM-yyyy");

// ====================================================== 
//                   costruttore
// ======================================================

        public Proiezione(LocalDate data, String ora, String titolo, String genere, String regista, int anno, int durata, int etaMin, double prezzo, int postiSala) {
            this.data = data;
            this.ora = ora;
            this.titolo = titolo;
            this.genere = genere;
            this.regista = regista;
            this.anno = anno;
            this.durata = durata;
            this.etaMin = etaMin;
            this.prezzo = prezzo;
            this.postiSala = postiSala;
        }

// ====================================================== 
//                   metodi getter
// ======================================================

    public LocalDate getData()    { return data; }
    public String getOra()        { return ora; }
    public String getTitolo()     { return titolo; }
    public String getGenere()     { return genere; }
    public String getRegista()    { return regista; }

    public int getAnno()          { return anno; }
    public int getDurata()        { return durata; }
    public int getEtaMin()        { return etaMin; }
    public int getPostiSala()     { return postiSala; }

    public double getPrezzo()     { return prezzo; }

// ====================================================== 
//                   metodi setter
// ======================================================

    public void setData(LocalDate data)       { this.data = data; }
    public void setOra(String ora)            { this.ora = ora; }
    public void setTitolo(String titolo)      { this.titolo = titolo; }
    public void setGenere(String genere)      { this.genere = genere; }
    public void setRegista(String regista)    { this.regista = regista; }

    public void setAnno(int anno)             { this.anno = anno; }
    public void setDurata(int durata)         { this.durata = durata; }
    public void setEtaMin(int etaMin)         { this.etaMin = etaMin; }
    public void setPostiSala(int postiSala)   { this.postiSala = postiSala; }

    public void setPrezzo(double prezzo)      { this.prezzo = prezzo; }

// ======================================================

        @Override
        public boolean equals(Object o){
            if(this==o) return true;
            if(o==null || getClass() != o.getClass()) return false;
            Proiezione that = (Proiezione) o;
            return titolo.equals(that.titolo) && data.equals(that.data) && ora.equals(that.ora);
        }

        @Override
        public int hashCode(){
            return java.util.Objects.hash(titolo, data, ora);
        }

        @Override
        public String toString(){ //ritorna la proiezione

            return titolo + " ( " + data.format(FORMATTA_DATA) + " : " + ora + " ) |" + prezzo + " euro | età consigliata "  + etaMin + ")";
        }
}