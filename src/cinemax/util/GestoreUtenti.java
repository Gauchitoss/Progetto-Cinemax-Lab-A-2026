package cinemax.util;

import cinemax.model.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;  

public class GestoreUtenti {
        
        private static final List<Utente> listaUtenti = new ArrayList<>();
        private static final String PERCORSO_FILE = "data/utenti.csv";
        
        
        public static void caricaUtenti() {
            listaUtenti.clear(); // pulisce la lista prima di caricare i dati dal file
            File file = new File(PERCORSO_FILE);
            if (!file.exists()) {
               inizializzaStaffDefault(); 
               return; // se il file non esiste, esce dal metodo
            }
            try (BufferedReader br = new BufferedReader ( new FileReader(file)) ){
                br.readLine(); // legge e ignora l'intestazione del file
                String riga;
                while((riga = br.readLine()) != null) {
                    String[] dati = riga.split ("\\|");//usa la | come separatore
                    if (dati.length != 7 ) { // verifica che ci siano esattamente 7 campi
                        System.err.println("Riga utenti ignorata (formato errato): " + riga);
                        continue;
                    }
                    String nome = dati[0].trim();
                    String cognome = dati[1].trim();
                    String username = dati[2].trim().toUpperCase();
                    String password = dati[3].trim(); // Già cifrata
                    String dataDiNascita = dati[4].equals("null") ? null : dati[4].trim();
                    String domicilio = dati[5].trim();
                    Utente.Ruolo ruolo;
                    try{
                        ruolo = Utente.Ruolo.daString(dati[6].trim());
                    } catch(IllegalArgumentException e){
                        System.err.println("Ruolo sconosciuto, riga ignorata: " + riga);
                        continue;
                    }
                     
                    Utente u = creaUtente(nome, cognome, username, password, dataDiNascita, domicilio, ruolo);
                    if(u != null) listaUtenti.add(u);
                }
            } catch (IOException e) {
                System.err.println("Errore durante la lettura del file utenti: " + e.getMessage());
            }

        }

        private static Utente creaUtente(String nome, String cognome, String username, String password, String dataDiNascita, String domicilio, Utente.Ruolo ruolo){
            switch(ruolo){
                case PROIEZIONISTA: 
                    return new Proiezionisti(nome, cognome, username, password, dataDiNascita, domicilio);
                case BIGLIETTAIO:
                    return new Bigliettai(nome, cognome, username, password, dataDiNascita, domicilio);
                case CLIENTE_REGISTRATO: 
                    return new ClientiRegistrati(nome, cognome, username, password, dataDiNascita, domicilio);
                default:
                    System.err.println("Ruolo non gestibile nel CSV: " + ruolo);
                    return null;
            }
        }

        
        public static void salvaUtenti() {
            new File("data").mkdir();
            try (PrintWriter pw = new PrintWriter(new FileWriter(PERCORSO_FILE))) {
                pw.println("nome|cognome|username|password|dataDiNascita|domicilio|ruolo"); // intestazione del file
                for (Utente u : listaUtenti) {
                    pw.printf("%s|%s|%s|%s|%s|%s|%s%n", u.getNome(), u.getCognome(), u.getUsername(), u.getPassword(),u.getDataDiNascita(), u.getDomicilio(), u.getRuolo());
                  
                }
            } catch (IOException e) {
                System.err.println("Errore durante il salvataggio del file utenti: " + e.getMessage());
            }
        }
        public static void inizializzaStaffDefault() {
         //inserisco due proiezionisti 
            listaUtenti.add(new Proiezionisti("Admin", "Uno", "proiezionista1", Cifratura.cifra("pass1"), "1990-01-01", "Sede"));
            listaUtenti.add(new Proiezionisti("Admin", "Due", "proiezionista2", Cifratura.cifra("pass2"), "1992-01-01", "Sede"));
         //inserisco cinque bigliettai
            listaUtenti.add(new Bigliettai("Staff", "uno", "bigliettaio1", Cifratura.cifra("pass3"), "1990-01-01", "Sede"));
            listaUtenti.add(new Bigliettai("Staff", "due", "bigliettaio2", Cifratura.cifra("pass4"), "1992-01-01", "Sede"));
            listaUtenti.add(new Bigliettai("Staff", "tre", "bigliettaio3", Cifratura.cifra("pass5"), "1990-01-01", "Sede"));
            listaUtenti.add(new Bigliettai("Staff", "quattro", "bigliettaio4", Cifratura.cifra("pass6"), "1992-01-01", "Sede"));
            listaUtenti.add(new Bigliettai("Staff", "cinque", "bigliettaio5", Cifratura.cifra("pass7"), "1990-01-01", "Sede"));
            salvaUtenti(); // salva i dati iniziali nel file   
        }
        
        public static Utente login(String username, String password) {
            if(username ==  null || password == null) return null;
            String usernameUpperCase = username.toUpperCase();
            String passwordCifrata =  Cifratura.cifra(password); // cifra la password inserita
            for (Utente u : listaUtenti) {
                if (u.getUsername().equals(usernameUpperCase) && u.getPassword().equals(passwordCifrata)) {
                    return u; // restituisce l'utente se le credenziali sono corrette
                }
            }
            return null; // restituisce null se le credenziali sono errate
        }

        public static void registraUtente(String nome, String cognome, String username, String password, String confermaPassword, String dataDiNascita, String domicilio)throws IllegalArgumentException{
            if(nome == null || nome.isEmpty() || cognome == null || cognome.isEmpty() || username == null || username.isEmpty() || password == null || password.isEmpty() || dataDiNascita == null || dataDiNascita.isEmpty())
                throw new IllegalArgumentException("Tutti i campi obbligatori devono essere compilati.");
            if(!nome.matches("[\\p{L} '\\-]+"))
                throw new IllegalArgumentException("Il nome può contenere solo lettere, spazi, apostrofi e trattini.");
            if(!cognome.matches("[\\p{L} '\\-]+"))
                throw new IllegalArgumentException("Il cognome può contenere solo lettere, spazi, apostrofi e trattini.");
            if(username.length() > 12 && username.length() < 3)
                throw new IllegalArgumentException("L'username deve essere lungo dai 3 ai 12 caratteri.");
            if(password.length() < 6)
                throw new IllegalArgumentException("La password deve essere lunga almeno 6 caratteri.");
            if(!password.equals(confermaPassword))
                throw new IllegalArgumentException("Le password inserite non corrispondono.");
            validaDataNascita(dataDiNascita);
            String usernameUpperCase = username.trim().toUpperCase();
            for(Utente u : listaUtenti){
                if(u.getUsername().toUpperCase().equals(usernameUpperCase))
                    throw new IllegalArgumentException("Username già esistente.");
            }
            String domicilioEffettivo = (domicilio==null || domicilio.isEmpty())? "N/D" : domicilio;
            listaUtenti.add(new ClientiRegistrati(nome, cognome, username, Cifratura.cifra(confermaPassword), dataDiNascita, domicilioEffettivo));
            salvaUtenti();
        }

        private static void validaDataNascita(String dataDiNascita) {
            if(dataDiNascita == null || dataDiNascita.trim().isEmpty())
                throw new IllegalArgumentException("La data di nascita non può essere vuota.");
            LocalDate nascita = null;
            String[] formati = {"yyyy-MM-dd", "dd-MM-yyyy", "dd/MM/yyyy"};
            for(String fmt : formati){
                try {
                    nascita = LocalDate.parse(dataDiNascita.trim(), DateTimeFormatter.ofPattern(fmt));
                    break;
                } catch(DateTimeParseException e) {
                
                }
            }
            if(nascita == null)
                throw new IllegalArgumentException("Data di nascita non valida. Usa il formato GG-MM-AAAA o AAAA-MM-GG.");

            LocalDate oggi = LocalDate.now();
            if(nascita.isAfter(oggi))
                throw new IllegalArgumentException("La data di nascita non può essere nel futuro.");

            // Età minima 5 anni (per evitare dati assurdi), nessun massimo
            if(nascita.isAfter(oggi.minusYears(5)))
                throw new IllegalArgumentException("La data di nascita non è valida.");
        }

        // ====================================================== 
        //  Metodo di supporto per cercare un utente tramite username
        // ======================================================
        public static Utente cercaPerUsername(String username) {
            if(username == null) return null;
            String usernameUpperCase = username.toUpperCase();
            for (Utente u : listaUtenti) {
                if (u.getUsername().toUpperCase().equals(usernameUpperCase)) {
                    return u;
                }
            }
            return null; // Ritorna null se lo username non esiste
        }

        public static List<Utente> getListaUtenti(){
            return new ArrayList<>(listaUtenti);
        }

}