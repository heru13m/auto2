package Controller;

/**
 * Klasa wyjątku niepoprawnej daty.
 */
public class InvalidDateException extends Exception {

    /**
     * Konstruktor wyjątku z wiadomoscia
     */
    public InvalidDateException() {
        super("Niepoprawna data");
    }

    public InvalidDateException(String msg) {
        super(msg);
    }

}
