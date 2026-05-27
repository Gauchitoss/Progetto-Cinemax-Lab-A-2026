package cinemax;

public class CostantiForm {

    public enum Campi{
        // CAMPI ACCESSO
        LOGIN_USER(0),
        LOGIN_PASSWORD(1),
        // CAMPI REGISTRAZIOINE
        REG_NOME(0),
        REG_COGNOME(1),
        REG_USERNAME(2),
        REG_PASSWORD(3),
        REG_CONFERMA_PASSWORD(4),
        REG_DATA(5),
        REG_DOMICILIO(6),
        // CAMPI CERCA FILM
        CERCA_TITOLO(0),
        CERCA_GIORNO_1(1),
        CERCA_MESE_1(2),
        CERCA_ANNO_1(3),
        CERCA_GIORNO_2(4),
        CERCA_MESE_2(5),
        CERCA_ANNO_2(6),
        CERCA_COSTO(7),
        CERCA_GENERE(8),
        // CAMPI AGGUNGI PROIEZIONE
        ADD_TITOLO(0),
        ADD_GENERE(1),
        ADD_REGISTA(2),
        ADD_ANNO_PRODUZIONE(3),
        ADD_DURATA(4),
        ADD_ETA(5),
        ADD_COSTO(6),
        ADD_POSTI(7),
        ADD_GIORNO(8),
        ADD_MESE(9),
        ADD_ANNO(10),
        ADD_ORA(11),
        // CAMPI CERCA PRENOTAZIONE
        PRENOTAZIONE_NOME(0),
        PRENOTAZIONE_COGNOME(1),
        PRENOTAZIONE_TITOLO(2),
        PRENOTAZIONE_GIORNO1(3),
        PRENOTAZIONE_MESE1(4),
        PRENOTAZIONE_ANNO1(5),
        PRENOTAZIONE_GIORNO2(6),
        PRENOTAZIONE_MESE2(7),
        PRENOTAZIONE_ANNO2(8),
        PRENOTAZIONE_USERNAME(9),
        PRENOTAZIONE_CODICE(10)
        ;


        public final int i;

        Campi(int indice){
            this.i = indice;
        }
    }
    
}
