package cinemax.util;

import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

public class Cifratura {
	
	
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
