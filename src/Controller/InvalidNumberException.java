package Controller;

/**
 * Klasa wyjątku niepoprawnej wartości numerycznej.
 * Rozszerzenie klasy Exception.
 */
public class InvalidNumberException extends Exception {

   // private static final long serialVersionUID = -5079022391455771671L;

    /**
     * Konstruktor wyjątku z ustawioną wiadomością.
     */
    public InvalidNumberException() {
        super("Dana liczba jest niepoprawna");
    }

    /**
     * Konstruktor wyjątku z wiadomością podaną jako parametr.
     * @param msg wiadomość do rzucanego wyjątku
     */
    public  InvalidNumberException(String msg) {
        super(msg);
    }

}
