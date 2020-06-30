package Controller;

/**
 * Klasa wyjątku niepoprawnej wartości numerycznej.
 */
public class InvalidNumberException extends Exception {

    /**
     * Konstruktor wyjątku z ustawioną wiadomością.
     */
    public InvalidNumberException() {
        super("Dana liczba jest niepoprawna");
    }

    public  InvalidNumberException(String msg) {
        super(msg);
    }

}
