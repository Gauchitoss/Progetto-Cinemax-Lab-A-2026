package cinemax.util;
import java.security.MessageDigest;



public class Cifratura {
	
	
	public static String cifra (String password) {
		try { 
			
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			//uso lo standard SHA-256
			
			
			byte[] array= md.digest(password.getBytes());
			// creo un array che conterrà un insieme di 32 byte 
	
			StringBuilder sb= new StringBuilder();
			for (byte b: array) {
				sb.append(String.format("%02x",b));
				// trasformo i byte in testo 
				//0x2 significa: x indica esadecimali, 2 indica le cifre , 0 indica aggiungi lo 0 se serve
			}
			return sb.toString();
		} catch (Exception e) {
			System.err.println("Errore durante la cifratura:" + e.getMessage());
			return null;
			}
		}
	}
