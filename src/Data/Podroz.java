package Data;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import Controller.InvalidDateException;
import Controller.InvalidNumberException;


public class Podroz implements Cloneable, Serializable {

    private float dystans;
    private float przebieg;
    private float srednieZuzyciePaliwa;
    private LocalDateTime czasPoczatkowy;
    private LocalDateTime czasKoncowy;
    private DateTimeFormatter formatCzasu = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public Podroz(float dystans, float przebieg, float zuzyciePaliwa, int rok, int miesiac, int dzien) {
        this.dystans = dystans;
        this.przebieg = przebieg;
        this.srednieZuzyciePaliwa = zuzyciePaliwa;
        this.czasPoczatkowy = LocalDateTime.of(rok, miesiac, dzien, 0, 0);
    }


    public Podroz(float dystans, float przebieg, float zuzyciePaliwa, String data) {
        this.dystans = dystans;
        this.przebieg = przebieg;
        this.srednieZuzyciePaliwa = zuzyciePaliwa;
        this.czasPoczatkowy = LocalDateTime.parse(data);
    }


    public Podroz(float dystans, float przebieg, float zuzyciePaliwa, LocalDateTime data) {
        this.dystans = dystans;
        this.przebieg = przebieg;
        this.srednieZuzyciePaliwa = zuzyciePaliwa;
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
        return "Start time: " + czasPoczatkowy.format(formatCzasu) + "\n" +
                "End time: " + czasKoncowy.format(formatCzasu) + "\n" +
                "Distance: " + dystans + "\n" +
                "Total przebieg: " + przebieg + "\n" +
                "Avg. fuel consumption: " + srednieZuzyciePaliwa;
    }


    public void setLength(float newLength) throws InvalidNumberException {
        if(newLength < 0)
            throw new InvalidNumberException(String.valueOf(newLength) + " długość podróży nie może być ujemna");
        this.dystans = newLength;
    }


    public void setPrzebieg(float newPrzebieg) {
        this.przebieg = newPrzebieg;
    }

    public void setSrednieZuzyciePaliwa(float newSrednieZuzyciePaliwa) {
        this.srednieZuzyciePaliwa = newSrednieZuzyciePaliwa;
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


    public float getSrednieZuzyciePaliwa() {
        return this.srednieZuzyciePaliwa;
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
