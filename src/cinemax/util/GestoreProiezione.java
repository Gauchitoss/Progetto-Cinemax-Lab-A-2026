package cinemax.util;

import cinemax.CineMax;
import cinemax.CostantiForm.Campi;
import cinemax.MenuManager.StatoMenu;
import cinemax.model.Proiezione;
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

        private static final List<Proiezione> listaProiezioni = new ArrayList<>(); //lista per memorizzare le proiezioni lette dal file proiezioni.csv
        private static final String FILE_PATH = "data/proiezioni.csv"; // persorso del file proiezioni.csv
        private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd-MM-yyyy");

// ====================================================== 
//                   metodo salvaSulFile
// ======================================================

        public static void salvaSuFile(){
            new File("data").mkdirs();
            try(PrintWriter pw = new PrintWriter(new FileWriter(FILE_PATH))){ // creazione PrintWriter e FileWriter
                pw.println("data_proiezione|ora_proiezione|titolo_film|genere|regista|anno|durata_minuti|eta_minima|prezzo_biglietto|posti_sala|posti_liberi");
                for(Proiezione p: listaProiezioni){
                    pw.printf(java.util.Locale.US, "%s|%s|%s|%s|%s|%d|%d|%d|%.2f|%d|%d%n", p.getData().format(FORMATO_DATA),p.getOraString(), p.getTitolo(), p.getGenere(), p.getRegista(), p.getAnno(), p.getDurata(), p.getEtaMin(), p.getPrezzo(), p.getPostiSala(), p.getPostiSala()); /* uso printf al posto di println per poter sfruttare i sgenaposto
                    e imposto il formato americano in modo da impedire che il prezzo possa essere scritto con la virgola */
                }
            } catch (IOException e) {
                System.err.println("Errore durante il salvataggio delle proiezioni: " + e.getMessage());
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
                        if (colonna.length < 11) { //controllo di sicurezza
                            System.err.println("Salto riga incompleta: " + riga);
                            continue;
                        }
                        for(int i=0; i<colonna.length; i++){
                            colonna[i] = colonna[i].replace("\"", "").trim(); // rimuovere virgolette se presenti e eventuali spazi vuoti
                        }
                        LocalDate data = LocalDate.parse(colonna[0], FORMATO_DATA);
                        String ora = colonna[1];
                        int anno = Integer.parseInt(colonna[5]);
                        int durata = Integer.parseInt(colonna[6]); 
                        int etaMin = Integer.parseInt(colonna[7]); 
                        double prezzo = Double.parseDouble(colonna[8].replace(",",".")); /*rimpiazo la virgola con il punto in modo da non generare 
                        confuzione con le virgole usate come separatori*/
                        int postiSala = Integer.parseInt(colonna[9]);
                        int postiLiberi = Integer.parseInt(colonna[10]);
                        listaProiezioni.add(new Proiezione(data, ora, colonna[2], colonna[3], colonna[4], anno, durata, etaMin, prezzo, postiSala, postiLiberi)); // aggiunta la proiezione alla lista in memoria
                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException | DateTimeException e){
                        System.err.println("Salto riga corrotta: " + riga);
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

        public static void modificaProiezione(Proiezione p, String[] datiFormTmp){
            try{
                int indicePoiezioneDaModificare = listaProiezioni.indexOf(p);
                //modifica della proiezione
                if(datiFormTmp[Campi.ADD_TITOLO.i] != null) p.setTitolo(datiFormTmp[Campi.ADD_TITOLO.i]);
                if(datiFormTmp[Campi.ADD_GENERE.i] != null) p.setGenere(datiFormTmp[Campi.ADD_GENERE.i]);
                if(datiFormTmp[Campi.ADD_REGISTA.i] != null) p.setRegista(datiFormTmp[Campi.ADD_REGISTA.i]);
                if(datiFormTmp[Campi.ADD_ANNO_PRODUZIONE.i] != null) p.setAnno(Integer.parseInt(datiFormTmp[Campi.ADD_ANNO_PRODUZIONE.i]));
                if(datiFormTmp[Campi.ADD_DURATA.i] != null) p.setDurata(Integer.parseInt(datiFormTmp[Campi.ADD_DURATA.i]));
                if(datiFormTmp[Campi.ADD_ETA.i] != null) p.setEtaMin(Integer.parseInt(datiFormTmp[Campi.ADD_ETA.i]));
                if(datiFormTmp[Campi.ADD_COSTO.i] != null) p.setPrezzo(Double.parseDouble(datiFormTmp[Campi.ADD_COSTO.i].replace(",", ".")));
                if(datiFormTmp[Campi.ADD_POSTI.i] != null) p.setPostiSala(Integer.parseInt(datiFormTmp[Campi.ADD_POSTI.i]));
                if(datiFormTmp[Campi.ADD_ORA.i] != null) p.setOra(datiFormTmp[Campi.ADD_ORA.i]);
                if(datiFormTmp[Campi.ADD_POSTI_LIBERI.i] != null) p.setPostiLiberi(Integer.parseInt(datiFormTmp[Campi.ADD_POSTI_LIBERI.i]));
                String giorno = datiFormTmp[Campi.ADD_GIORNO.i];
                String mese = datiFormTmp[Campi.ADD_MESE.i];
                String anno = datiFormTmp[Campi.ADD_ANNO.i];
                if(giorno != null && mese != null && anno != null){
                    p.setData(LocalDate.of(Integer.parseInt(anno), Integer.parseInt(mese), Integer.parseInt(giorno)));
                }
                // salvataggio della proiezione nella lista originale
                listaProiezioni.set(indicePoiezioneDaModificare, p);
                salvaSuFile();
                CineMax.stackRecord.pop();
                cinemax.LogicaStatiManager.messaggioConfermaCorrente = "Proiezione modificata.";
                CineMax.stackRecord.push(StatoMenu.STATO_CONFERMA);
            }catch(Exception e){
                cinemax.LogicaStatiManager.messaggioErroreCorrente = "Errore durante la modifica: " + e.getMessage();
                CineMax.stackRecord.push(StatoMenu.STATO_ERRORE);
            }
        }

// ======================================================
//                   metodo cercaProiezione
// ======================================================

        public static List<Proiezione> cercaProiezione(String titolo, LocalDate dataDa, LocalDate dataA, Double prezzoMax, String genere){
            return listaProiezioni.stream()
            .filter(p -> titolo == null || titolo.trim().isEmpty() || p.getTitolo().toLowerCase().contains(titolo.trim().toLowerCase()))
            .filter(p -> genere == null || genere.isEmpty() || p.getGenere().equalsIgnoreCase(genere.trim()))
            .filter(p -> prezzoMax == null || p.getPrezzo() <= prezzoMax)
            .filter(p -> {
                LocalDate data = p.getData();
                if(dataDa != null && dataA == null)
                    return data.isEqual(dataDa);
                if(dataDa != null && dataA != null)
                    return !data.isBefore(dataDa) && !data.isAfter(dataA);
                if(dataDa == null && dataA != null)
                    return !data.isAfter(dataA);
                return true;
            })
            .collect(Collectors.toList());
        }

// ====================================================== 
//                   metodi getter
// ======================================================
        public static List<Proiezione> proiezioniDelGiorno(){
            LocalDate dataOggi = LocalDate.now();
            return cercaProiezione(null, dataOggi, dataOggi, null, null);
        }

        public static List<Proiezione> getListaProiezioni(){
            return new ArrayList<>(listaProiezioni); //restituisce una copia della lista, non l'originale
        }

    }

