package cinemax.util;

import cinemax.model.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.DateTimeException;

//Contiene il BufferedReader e il FileReader per il file proiezioni

    public class GestoreProiezione {

// ====================================================== 
//                   Campi
// ======================================================

        private static List<Proiezione> listaProiezioni = new ArrayList<>(); //lista per memorizzare le proiezioni lette dal file proiezioni.csv
        private static final String FILE_PATH="data/proiezioni.csv"; // persorso del file proiezioni.csv
        private static final DateTimeFormatter FORMATTA_DATA = DateTimeFormatter.ofPattern("dd-MM-yyyy");

// ====================================================== 
//                   metodo salvaSulFile
// ======================================================

        public static void salvaSuFile(){
            try(PrintWriter pw = new PrintWriter(new FileWriter(FILE_PATH))){ // creazione PrintWriter e FileWriter
                pw.println("data_proiezione|ora_proiezione|titolo_film|genere|regista|anno|durata_minuti|eta_minima|prezzo_biglietto|posti_sala");
                for(Proiezione p: listaProiezioni){
                    pw.printf(java.util.Locale.US, "%s|%s|%s|%s|%s|%d|%d|%d|%.2f|%d%n", p.getData().format(FORMATTA_DATA),p.getOra(), p.getTitolo(), p.getGenere(), p.getRegista(), p.getAnno(), p.getDurata(), p.getEtaMin(), p.getPrezzo(), p.getPostiSala()); /* uso printf al posto di println per poter sfruttare i sgenaposto
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
                        String[] colonna = riga.split("\\|"); //spezzetta la riga usando come separatore la barretta  verticale
                        if (colonna.length < 10) { //controllo di sicurezza
                            System.err.println("Salto riga incompleta: " + riga);
                            continue;
                        }
                        for(int i=0; i<colonna.length; i++){
                            colonna[i] = colonna[i].replace("\"", "").trim(); // rimuovere virgolette se presenti e eventuali spazi vuoti
                        }
                        int anno = Integer.parseInt(colonna[5]);
                        int durata = Integer.parseInt(colonna[6]); 
                        int etaMin = Integer.parseInt(colonna[7]); 
                        int postiSala = Integer.parseInt(colonna[9]);
                        double prezzo = Double.parseDouble(colonna[8].replace(",",".")); /*rimpiazo la virgola con il punto in modo da non generare 
                        confuzione con le virgole usate come separatori*/
                        LocalDate data = LocalDate.parse(colonna[0], FORMATTA_DATA);
                        Proiezione proiezione = new Proiezione(data, colonna[1], colonna[2], colonna[3], colonna[4], anno, durata, etaMin, prezzo, postiSala); //creazione oggetto proiezione
                        listaProiezioni.add(proiezione); // aggiunta la proiezione alla lista in memoria
                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException | DateTimeException e){
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

        public static void inserisci(Proiezione p){ //inserimento di una nuova proiezione nella lista e nel file
            listaProiezioni.add(p);
            salvaSuFile();
        }  

        public static void elimina(Proiezione p){ //rimozione di una proiezione in lista se presente
            if(listaProiezioni.remove(p)){
                salvaSuFile();
            }
        }

// ======================================================
//                   metodo cercaProiezione
// ======================================================

        public static List<Proiezione> cercaProiezione(String titolo, int daGiorno, int daMese, int daAnno, int aGiorno, int aMese, int aAnno, String prezzoStr, String genere){
            LocalDate daData = null;
            LocalDate aData = null;
            try{
                if(daAnno != 0 && daMese != 0 && daGiorno != 0){
                    daData = LocalDate.of(daAnno, daMese, daGiorno);
                }
                if(aAnno != 0 && aMese != 0 && aGiorno != 0){
                    aData = LocalDate.of(aAnno, aMese, aGiorno);
                }
            } catch(DateTimeException e){
                System.err.println("L'intervallo di date inserito per la ricerca non risulta valido");
                return new ArrayList<>();
            }
            double limitePrezzo = Double.MAX_VALUE;
            if (prezzoStr != null && !prezzoStr.isEmpty()) {
                try {
                    limitePrezzo = Double.parseDouble(prezzoStr.replace(",", "."));
                } catch (NumberFormatException e) {
                System.err.println("Formato prezzo non valido nella ricerca.");
            }
        }
            final LocalDate finalDaData = daData;
            final LocalDate finalAData = aData;
            final double finalLimitePrezzo = limitePrezzo;
            return listaProiezioni.stream()
            .filter(p -> {
                if (titolo == null || titolo.trim().isEmpty()) return true; 
                if (p.getTitolo() == null) return false;
                return p.getTitolo().toLowerCase().contains(titolo.trim().toLowerCase());
            })
            .filter(p -> {
                if (genere == null || genere.isEmpty()) return true;
                if (p.getGenere() == null) return false;
                return (p.getGenere().equalsIgnoreCase(genere.trim()));
             })
            .filter(p -> p.getPrezzo() <= finalLimitePrezzo)
            .filter(p -> {
                LocalDate pData = p.getData();
                if(pData == null) return false;
                if(finalDaData != null && finalAData == null){
                    return pData.isEqual(finalDaData);
                }
                else if(finalDaData != null && finalAData != null){
                    return !pData.isBefore(finalDaData) && !pData.isAfter(finalAData);
                }
                else if(finalDaData == null && finalAData != null){
                    return !pData.isAfter(finalAData);
                }
                else
                    return true;
            })
            .collect(Collectors.toList());
        }

// ====================================================== 
//                   metodo visualizzaProiezioni
// ======================================================

        public static void visualizzaProiezione(String titolo, int daGiorno, int daMese, int daAnno, int aGiorno, int aMese, int aAnno, String prezzoStr, String genere){
            List<Proiezione> risultato = cercaProiezione(titolo, daGiorno, daMese, daAnno, aGiorno, aMese, aAnno, prezzoStr, genere);
            if(risultato.isEmpty()){
                System.out.println("Nessuna proiezione trovata con i filtri inseriti");
                return;
            }
            System.out.println("Il risultato della ricerca sono : " + risultato.size() + " proiezioni");
            for(Proiezione p : risultato){
                System.out.println("Titolo: " + p.getTitolo());
                System.out.println("Data: " + p.getData().format(FORMATTA_DATA));
                System.out.println("Ora: " + p.getOra());
                System.out.println("Genere: " + p.getGenere());
                System.out.println("Regista: " + p.getRegista());
                System.out.println("Anno: " + p.getAnno());
                System.out.println("Durata: " + p.getDurata());
                System.out.println("Eta minima: " + p.getEtaMin());
                System.out.println("Prezzo: " + p.getPrezzo());    
                System.out.println("Posti in sala: " + p.getPostiSala()); 
            }
        }

// ====================================================== 
//                   metodi getter
// ======================================================

        public static List<Proiezione> getListaProiezioni(){
            return new ArrayList<>(listaProiezioni); //restituisce una copia della lista, non l'originale
        }

    }

