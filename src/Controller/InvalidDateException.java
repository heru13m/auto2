package Controller;

/**
 * Klasa wyjątku niepoprawnej daty.
 * Rozszerzenie klasy Exception.
 */
public class InvalidDateException extends Exception {

   // private static final long serialVersionUID = 5357130290575686090L;

    /**
     * Konstruktor wyjątku z ustawioną wiadomością.
     */
    public InvalidDateException() {
        super("Dana data jest niepoprawna");
    }

    /**
     * Konstruktor wyjątku z wiadomością podaną jako parametr.
     * @param msg wiadomość do rzucanego wyjątku
     */
    public InvalidDateException(String msg) {
        super(msg);
    }

}
