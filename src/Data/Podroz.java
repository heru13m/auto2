package Data;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import Controller.InvalidDateException;
import Controller.InvalidNumberException;

/**
 * Klasa reprezentująca pojedyńczą podróż
 **
 * @version 1.0
 * @author Maciej Ksiezak
 * @author Mateusz Mus
 *
 */
public class Podroz implements Cloneable, Serializable {

    private float dystans;
    private float przebieg;
    private LocalDateTime czasPoczatkowy;
    private LocalDateTime czasKoncowy;
    private DateTimeFormatter formatCzasu = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public Podroz(float dystans, float przebieg, int rok, int miesiac, int dzien) {
        this.dystans = dystans;
        this.przebieg = przebieg;
        this.czasPoczatkowy = LocalDateTime.of(rok, miesiac, dzien, 0, 0);
    }



    public Podroz(float dystans, float przebieg, String data) {
        this.dystans = dystans;
        this.przebieg = przebieg;
        this.czasPoczatkowy = LocalDateTime.parse(data);
    }
    public Podroz(float dystans, float przebieg, String data, String data2) {
        this.dystans = dystans;
        this.przebieg = przebieg;
        this.czasPoczatkowy = LocalDateTime.parse(data);
        this.czasKoncowy = LocalDateTime.parse(data2);
    }
    public Podroz(float dystans, float przebieg, LocalDateTime data, LocalDateTime data2) {
        this.dystans = dystans;
        this.przebieg = przebieg;
        this.czasPoczatkowy = data;
        this.czasKoncowy = data2;
    }


    public Podroz(float dystans, float przebieg, LocalDateTime data) {
        this.dystans = dystans;
        this.przebieg = przebieg;
        this.czasPoczatkowy = data;
    }

    /** TODO
     * Klonuje obiekt.

     @Override
     public Object clone() throws CloneNotSupportedException {
     return super.clone();
     }
     */

    public String toString() {
        return "Poczatek podrozy " + czasPoczatkowy.format(formatCzasu) + "\n" +
                "Koniec podrozy: " + czasKoncowy.format(formatCzasu) + "\n" +
                "Dystans: " + dystans + "\n" +
                "Calkowity przebieg: " + przebieg + "\n\n";
    }


    public void setDystans(float newDystans) throws InvalidNumberException {
        if(newDystans < 0)
            throw new InvalidNumberException(String.valueOf(newDystans) + " długość podróży nie może być ujemna");
        this.dystans = newDystans;
    }


    public void setPrzebieg(float newPrzebieg) {
        this.przebieg = newPrzebieg;
    }



    public void setCzasPoczatkowy(int rok, int miesiac, int dzien) throws InvalidDateException {
        LocalDateTime newDate = LocalDateTime.of(rok, miesiac, dzien, 0, 0);
        if(LocalDateTime.now().compareTo(newDate) < 0) {
            throw new InvalidDateException(newDate.toString() + " podana data jest z przyszłości");
        }
        this.czasPoczatkowy = newDate;
    }


    public void setCzasPoczatkowy(String data) throws InvalidDateException {
        LocalDateTime newDate = LocalDateTime.parse(data);
        if(LocalDateTime.now().compareTo(newDate) < 0) {
            throw new InvalidDateException(newDate.toString() + " podana data jest z przyszłości");
        }
        this.czasPoczatkowy = newDate;
    }


    public float getDystans() {
        return this.dystans;
    }


    public float getPrzebieg() {
        return this.przebieg;
    }



    public String getCzasPoczatkowyString() {
        return this.czasPoczatkowy.format(formatCzasu);
    }


    public String getCzasKoncowyString() {
        return this.czasKoncowy.format(formatCzasu);
    }


    public LocalDateTime getCzasPoczatkowy() {
        return this.czasPoczatkowy;
    }


    public LocalDateTime getCzasKoncowy() {
        return czasKoncowy;
    }


    public void setCzasKoncowy(LocalDateTime czasKoncowy) {
        this.czasKoncowy = czasKoncowy;
    }
}
