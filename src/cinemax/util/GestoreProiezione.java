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
import java.time.LocalTime;

/**
 * Gestore logico e di persistenza per il palinsesto delle proiezioni del cinema.
 * Amministra il file CSV proiezioni e offre funzioni di inserimento, rimozione, 
 * ricerca e aggiornamento della capienza dei posti liberi per ogni singola sala.
 * @author Modena Matteo (Matricola: 765099) - VA
 * @author Baroncelli Luca (Matricola: 765099) - VA
 * @author Bin Alessio (Matricola: 762387) - VA
 */
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

        /**
         * Serializza la lista delle proiezioni attive scrivendola all'interno del file CSV.
         * Configura il Locale in formato statunitense per prevenire discrepanze sui decimali separati da virgole.
         */
        public static void salvaSuFile(){
            new File("data").mkdirs();
            try(PrintWriter pw = new PrintWriter(new FileWriter(FILE_PATH))){ // creazione PrintWriter e FileWriter
                pw.println("data_proiezione|ora_proiezione|titolo_film|genere|regista|anno|durata_minuti|eta_minima|prezzo_biglietto|posti_sala|posti_liberi");
                for(Proiezione p: listaProiezioni){
                    pw.printf(java.util.Locale.US, "%s|%s|%s|%s|%s|%d|%d|%d|%.2f|%d|%d%n", p.getData().format(FORMATO_DATA),p.getOraString(), p.getTitolo(), p.getGenere(), p.getRegista(), p.getAnno(), p.getDurata(), p.getEtaMin(), p.getPrezzo(), p.getPostiSala(), p.getPostiLiberi()); /* uso printf al posto di println per poter sfruttare i sgenaposto
                    e imposto il formato americano in modo da impedire che il prezzo possa essere scritto con la virgola */
                }
            } catch (IOException e) {
                System.err.println("Errore durante il salvataggio delle proiezioni: " + e.getMessage());
            }
        }

        // ====================================================== 
        //                   metodo leggiProiezioni
        // ======================================================

        /**
         * Esegue il caricamento delle proiezioni dal database CSV ripopolando la cache in memoria.
         * Salta la riga di intestazione iniziale, gestisce eventuali anomalie e ripulisce le stringhe.
         */
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

        /**
         * Aggiunge permanentemente una nuova proiezione programmata all'elenco in memoria e aggiorna il file CSV.
         * @param p l'istanza della nuova {@link Proiezione} da salvare
         */
        public static void inserisci(Proiezione p){ //inserimento di una nuova proiezione nella lista e nel file
            listaProiezioni.add(p);
            salvaSuFile();
        }  

        /**
         * Rimuove una proiezione pianificata dall'elenco del palinsesto e aggiorna la persistenza.
         * @param p l'istanza della proiezione da depennare
         */
        public static void elimina(Proiezione p){ //rimozione di una proiezione in lista se presente
            if(listaProiezioni.remove(p)){
                salvaSuFile();
            }
        }

        /**
         * Aggiorna selettivamente le proprietà di una proiezione esistente elaborando l'array temporaneo inviato da un form TUI.
         * Gestisce i cambi di stato e reindirizza la navigazione verso menu di conferma o errore.
         * @param p           la proiezione originaria da sottoporre a revisione
         * @param datiFormTmp l'array contenente i nuovi valori testuali immessi dall'operatore
         */
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

        /**
         * Decrementa il quantitativo di posti liberi residui di una proiezione a fronte di una vendita o di una nuova prenotazione.
         * @param p        la proiezione interessata dalla variazione
         * @param numPosti il numero di sedute da scalare e bloccare
         */
        public static void modificaPostiLiberi(Proiezione p, int numPosti){
            try{
                int indicePoiezioneDaModificare = listaProiezioni.indexOf(p);
                p.setPostiLiberi(p.getPostiLiberi() - numPosti);
                listaProiezioni.set(indicePoiezioneDaModificare, p);
                salvaSuFile();
            }catch(Exception e){
                cinemax.LogicaStatiManager.messaggioErroreCorrente = "Errore durante la prenotazione dei posti: " + e.getMessage();
                CineMax.stackRecord.push(StatoMenu.STATO_ERRORE);
            }

        }

        /**
         * Incrementa nuovamente il quantitativo di posti liberi residui a seguito di storno cassa o di annullamento prenotazione.
         * * @param p        la proiezione interessata dal ripristino
         * @param numPosti il numero di sedute da riaggiungere e sbloccare
         */
        public static void annullaModificaPostiLiberi(Proiezione p, int numPosti){
            try{
                int indicePoiezioneDaModificare = listaProiezioni.indexOf(p);
                p.setPostiLiberi(p.getPostiLiberi() + numPosti);
                listaProiezioni.set(indicePoiezioneDaModificare, p);
                salvaSuFile();
            }catch(Exception e){
                cinemax.LogicaStatiManager.messaggioErroreCorrente = "Errore durante la prenotazione dei posti: " + e.getMessage();
                CineMax.stackRecord.push(StatoMenu.STATO_ERRORE);
            }

        }

        // ======================================================
        //               Metodo cercaProiezione
        // ======================================================

        /**
         * Esegue un'interrogazione avanzata multi-criterio ad ampio raggio sulla lista proiezioni mediante Stream API.
         * Qualsiasi filtro impostato a null o stringa vuota viene saltato senza condizionare l'esito della ricerca.
         * @param titolo    ricerca parziale sul titolo della proiezione
         * @param dataDa    filtro temporale di partenza (limite inferiore)
         * @param dataA     filtro temporale di scadenza (limite superiore)
         * @param prezzoMax soglia economica massima tollerata per il biglietto
         * @param genere    genere cinematografico esatto ricercato
         * @return List un elenco selezionato di istanze di {@link Proiezione} corrispondenti ai parametri di ricerca
         */
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

        /**
         * Identifica puntualmente una e una sola proiezione a palinsesto attravrso il codice prenotazione.
         * @param titolo il titolo esatto del film
         * @param data   la data precisa dello spettacolo
         * @param ora    l'orario preciso d'inizio dello spettacolo
         * @return Proiezione l'istanza corrispondente trovata, oppure null se la ricerca restituisce zero o molteplici corrispondenze
         */
        public static Proiezione individuaProiezione(String titolo, LocalDate data, LocalTime ora){
            List<Proiezione> listaProiezioniTrovate = new ArrayList<>();
            listaProiezioniTrovate = listaProiezioni.stream()
            .filter(p -> p.getTitolo().equalsIgnoreCase(titolo))
            .filter(p -> p.getData().isEqual(data))
            .filter(p -> p.getOra().equals(ora))
            .collect(Collectors.toList());
            if(listaProiezioniTrovate.size() != 1)
                return null;
            Proiezione unica = listaProiezioniTrovate.get(0);
            return unica;
        }

        // ====================================================== 
        //                   metodi getter
        // ======================================================

        /**
         * Estrae la sottolista contenente unicamente le proiezioni pianificate per il giorno corrente.
         * @return List l'elenco delle proiezioni del giorno
         */
        public static List<Proiezione> proiezioniDelGiorno(){
            LocalDate dataOggi = LocalDate.now();
            return cercaProiezione(null, dataOggi, dataOggi, null, null);
        }

        /**
         * Restituisce una copia dell'elenco proiezioni complessivo.
         * @return List una copia shallow indipendente della lista delle proiezioni globali
         */
        public static List<Proiezione> getListaProiezioni(){
            return new ArrayList<>(listaProiezioni); //restituisce una copia della lista, non l'originale
        }

    }

