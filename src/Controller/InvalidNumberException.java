package Controller;

/**
 * Klasa wyjątku niepoprawnej wartości liczbowej
 */
public class InvalidNumberException extends Exception {

    /**
     * Konstruktor wyjątku z wiadomością.
     */
    public InvalidNumberException() {
        super("Niepoprawna liczba");
    }

    public  InvalidNumberException(String msg) {
        super(msg);
    }

}
