package cinemax.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

//Contiene il BufferedReader e il FileReader per il file proiezioni

    public class Proiezioni {

// ====================================================== 
//                   Campi
// ======================================================

        private String dataOra;
        private String titolo;
        private String genere;
        private String regista;
        private int anno;
        private int durata;
        private int etaMin;
        private double prezzo;
        private static List<Proiezioni> listaProiezioni = new ArrayList<>(); //lista per memorizzare le proiezioni lette dal file proiezioni.csv
        private static final String FILE_PATH="data/proiezioni.csv"; // persorso del file proiezioni.csv


// ====================================================== 
//                   costruttore
// ======================================================

        public Proiezioni(String dataOra, String titolo, String genere, String regista, int anno, int durata, int etaMin, double prezzo) {
            this.dataOra = dataOra;
            this.titolo = titolo;
            this.genere = genere;
            this.regista = regista;
            this.anno = anno;
            this.durata = durata;
            this.etaMin = etaMin;
            this.prezzo = prezzo;
        }

// ====================================================== 
//                   metodo leggiProiezioni
// ======================================================

        public static void salvaSuFile(){
            try(PrintWriter pw = new PrintWriter(new FileWriter(FILE_PATH))){ // creazione PrintWriter e FileWriter
                pw.println("data_ora_proiezione,titolo_film,genere,regista,anno,durata_minuti,eta_minima,prezzo_biglietto");
                for(Proiezioni p: listaProiezioni){
                    pw.printf(java.util.Locale.US, "%s,%s,%s,%s,%d,%d,%d,%.2f%n", p.dataOra, p.titolo, p.genere, p.regista, p.anno, p.durata, p.etaMin, p.prezzo); /* uso printf al posto di println per poter sfruttare i sgenaposto
                    e imposto il formato americano in modo da impedire che il prezzo possa essere scritto con la virgola */
                }
            } catch (IOException e) {
                System.err.println("Errore durante il salvataggio: " + e.getMessage());
            }
        }

// ====================================================== 
//                   metodo leggiProiezioni
// ======================================================

        public static void leggiProiezioni() {
            listaProiezioni.clear(); // pulisce la lista prima di leggere i dati dal file
            File file = new File(FILE_PATH);
            if(!file.exists()) return; //se non esiste esce dal metodo
            try(BufferedReader br = new BufferedReader(new FileReader(file))){ //Creazione BufferdReader e FileReader
                String riga;
                br.readLine(); // Rimuove la prima riga che contiene l'intestazione
                while((riga = br.readLine()) != null){ //lettura del file riga per riga
                    try{
                        String[] colonna = riga.split(","); //spezzetta la riga usando come separatore la virgola
                        for(int i=0; i<colonna.length; i++){
                            colonna[i] = colonna[i].replace("\"", ""); // rimuovere virgolette se presenti
                        }
                        int anno = Integer.parseInt(colonna[4].trim()); //converte la stringa in intero
                        int durata = Integer.parseInt(colonna[5].trim()); 
                        int etaMin = Integer.parseInt(colonna[6].trim()); 
                        double prezzo = Double.parseDouble(colonna[7].trim().replace(",",".")); /*rimpiazo la virgola con il punto in modo da non generare 
                        confuzione con le virgole usate come separatori*/
                        Proiezioni proiezione = new Proiezioni(colonna[0], colonna[1], colonna[2], colonna[3], anno, durata, etaMin, prezzo); //creazione oggetto proiezione
                        listaProiezioni.add(proiezione); // aggiunta la proiezione alla lista in memoria
                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e){
                        System.err.println("Salto riga corrotta " + riga);
                    }
                }
            } catch (IOException e) {
                System.err.println("Errore lettura file: " + e.getMessage());
            }
        }

// ====================================================== 
//                   metodi modifica
// ======================================================

        public static void inserisci(Proiezioni p){ //inserimento di una nuova proiezione nella lista e nel file
            listaProiezioni.add(p);
            salvaSuFile();
        }  

        @Override
        public boolean equals(Object o){
            if(this==o) return true;
            if(o==null || getClass() != o.getClass()) return false;
            Proiezioni that = (Proiezioni) o;
            return titolo.equals(that.titolo) && dataOra.equals(that.dataOra);
        }

        @Override
        public int hashCode(){
            return java.util.Objects.hash(titolo, dataOra);
        }

        public static void elimina(Proiezioni p){ //rimozione di una proiezione in lista se presente
            if(listaProiezioni.remove(p)){
                salvaSuFile();
            }
        }

// ====================================================== 
//                   metodo stampa
// ======================================================
        
        @Override
        public String toString(){ //ritorna la proiezione
            return "Proiezione: " + titolo + " del " + dataOra + " a " + prezzo + "€ (minimo anni: " + etaMin + ")";
        }

// ====================================================== 
//                   metodi setter
// ======================================================

        public void setDataOra(String dataOra){
            this.dataOra = dataOra;
        }

        public void setTitolo(String titolo){
            this.titolo = titolo;
        }

        public void setGenere(String genere){
            this.genere = genere;
        }

        public void setRegista(String regista){
            this.regista = regista;
        }

        public void setAnno(int anno){
            this.anno = anno;
        }

        public void setDurata(int durata){
            this.durata = durata;
        }

        public void setEtaMin(int etaMin){
            this.etaMin = etaMin;
        }

        public void setPrezzo(double prezzo){
            this.prezzo = prezzo;
        }

// ====================================================== 
//                   metodi getter
// ======================================================

        public String getDataOra() {
            return dataOra;
        }

        public String getTitolo() {
            return titolo;
        }

        public String getGenere() {
            return genere;
        }

        public String getRegista() {
            return regista;
        }

        public int getAnno() {
            return anno;
        }

        public int getDurata() {
            return durata;
        }

        public int getEtaMin() {
            return etaMin;
        }

        public double getPrezzo() {
            return prezzo;
        }

        public static List<Proiezioni> getListaProiezioni(){
            return new ArrayList<>(listaProiezioni); //restituisce una copia della lista, non l'originale
        }

    }

