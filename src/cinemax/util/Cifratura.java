package cinemax.util;

import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

/**
 * Componente di utilità preposto alla sicurezza dei dati del sistema CineMax.
 * Fornisce algoritmi di hashing crittografico per la protezione e l'offuscamento
 * delle password degli utenti prima del salvataggio su file permanente.
 * @author Modena Matteo (Matricola: 765099) - VA
 * @author Baroncelli Luca (Matricola: 765099) - VA
 * @author Bin Alessio (Matricola: 762387) - VA
 */
public class Cifratura {
	
	/**
	 * Converte una stringa di testo in chiaro (password) in un'impronta digitale 
	 * crittografica univoca standard SHA-256 rappresentata in formato esadecimale.
	 * @param password la stringa di testo in chiaro da cifrare
	 * @return la stringa esadecimale a 64 caratteri risultante dall'hashing
	 * @throws RuntimeException se l'algoritmo SHA-256 non risulta installato o disponibile nella JVM locale
	 */
	public static String cifra (String password) {
		try { 
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			//uso lo standard SHA-256
			byte[] hash= md.digest(password.getBytes(StandardCharsets.UTF_8));
			// creo un array che conterrà un insieme di 32 byte 
			StringBuilder sb= new StringBuilder();
			for (byte b: hash) {
				sb.append(String.format("%02x",b));
				// trasformo i byte in testo 
				//0x2 significa: x indica esadecimali, 2 indica le cifre , 0 indica aggiungi lo 0 se serve
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("SHA-256 non disponibile nella JVM corrente: " + e.getMessage());
			}
		}
	}
