package cinemax.util;

import cinemax.model.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;  

public class GestoreUtenti {
        
        private static List<Utente> listaUtenti = new ArrayList<>();
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
                    String username = dati[2].trim();
                    String password = dati[3].trim(); // Già cifrata
                    String dataDiNascita = dati[4].equals("null") ? null : dati[4].trim();
                    String domicilio = dati[5].trim();
                    String ruolo = dati[6].trim();
                     
                    Utente u = creaUtente(nome, cognome, username, password, dataDiNascita, domicilio, ruolo);
                    if(u != null) listaUtenti.add(u);
                }
            } catch (IOException e) {
                System.err.println("Errore durante la lettura del file utenti: " + e.getMessage());
            }

        }

        
        public static void salvaUtenti() {
            File folder = new File("data");
            if (!folder.exists()) {
                folder.mkdirs(); // crea la cartella se non esiste
            }
            try (PrintWriter pw = new PrintWriter(new FileWriter(percorsoFile))) {
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
            String passwordCifrata =  Cifratura.cifra(password); // cifra la password inserita
            for (Utente u : listaUtenti) {
                if (u.getUsername().equals(username) && u.getPassword().equals(passwordCifrata)) {
                    return u; // restituisce l'utente se le credenziali sono corrette
                }
            }
            return null; // restituisce null se le credenziali sono errate
        }

        public static void registraUtente(String nome, String cognome, String username, String password, String confermaPassword, String dataDiNascita, String domicilio)throws IllegalArgumentException{
            
            if(nome == null || cognome == null || username == null || password == null || dataDiNascita == null)
                throw new IllegalArgumentException("tutti i campi obbligatori devono essere compilati");

            if(!password.equals(confermaPassword))
                throw new IllegalArgumentException("le password inserite non corrispondono");

            for(Utente u : listaUtenti){
                if(u.getUsername().equals(username))
                    throw new IllegalArgumentException("username già esistente");
            }

            String domicilioEffettivo = (domicilio==null)? "N/D" : domicilio;

            listaUtenti.add(new ClientiRegistrati(nome, cognome, username, Cifratura.cifra(confermaPassword), dataDiNascita, domicilioEffettivo));
            salvaUtenti();
        }
        // ====================================================== 
        //  Metodo di supporto per cercare un utente tramite username
        // ======================================================
        public static Utente cercaPerUsername(String username) {
            for (Utente u : listaUtenti) {
                if (u.getUsername().equals(username)) {
                    return u;
                }
            }
            return null; // Ritorna null se lo username non esiste
        }

}