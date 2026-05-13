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
        CERCA_DATA(1),
        CERCA_COSTO(2),
        CERCA_DURATA(3),
        CERCA_GENERE(4);


        public final int i;

        Campi(int indice){
            this.i = indice;
        }
    }
    
}
