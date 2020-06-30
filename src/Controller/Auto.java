package Controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import Data.DanePodstawowe;
import Data.UstawieniaSamochodu;
import Data.Podroz;
import Data.Database;
//import Data.XMLFileManager;

/**
 * Klasa reprezentująca samochód oraz komputer pokładowy.
 *
 * Zapewnia funkcjonalnosć komputera pokładowego
 *
 * @version 1.0
 * @author Maciej Ksiezak
 * @author Mateusz Mus
 *
 */

public class Auto {

    private float przebiegCalkowity;
    private float przebieg1;
    private float przebieg2;
    private float dystans;
    private float srednieZuzyciePaliwa;
    private float zuzytePaliwo;         // na trasie
    private float predkoscMaksymalna;
    private float predkoscTempomatu;
    private float predkoscAktualna;
    private float predkoscSrednia;
    private boolean tempomat;
    private SwiatlaSamochodowe swiatla;
    private LocalDateTime czasStartu;
    private LocalDateTime czasZatrzymania;
    private LocalTime czasCalkowity;
    private DateTimeFormatter formatCzasu;
    private boolean czyJestWlaczony;
    private short bieg; // 0 neutral, 1-6 normal
    private ArrayList<Podroz> podroze;
    private Podroz aktualnaPodroz;
  //  private Database db;
    private long czasWsekundach;
    private UstawieniaSamochodu ustawienia;
    private float benzynaPelna;
    private Database db;

    /**
     * Konstruktor klasy Auto. Ustawia domyślne wartości oraz tworzy obiekt klasy Database do którego będą później zapisywane
     * dane z podróży. Wczytuje z pliku xml dane dotyczace przebiegu.
     */
    public Auto() {
        dystans = 0;
        predkoscMaksymalna = 0;
        predkoscAktualna = 0;
        przebiegCalkowity = 0;
        przebieg1 = 0;
        przebieg2 = 0;
        zuzytePaliwo=0;
        bieg = 0;
        predkoscTempomatu = 0;
        tempomat = false;
        swiatla = new SwiatlaSamochodowe();
        formatCzasu = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        podroze = new ArrayList<>();
        benzynaPelna = 60;


        db = new Database();
//        // Connecting to database
//        try {
//            db = new Database();
//        } catch (SQLException e) {
//            System.out.println("Connecting to database failed");
//        }
//
//        // LoadizmienpPredkosng backup
        DanePodstawowe dane = new DanePodstawowe();
        UstawieniaSamochodu ustawieniaSamochodu = dane.wczytajPodstawoweDane();
        this.przywrocUstawienia(ustawieniaSamochodu);
    }

    /**
     * Przywraca ustawienia z odczytanego przedniej obiektu UstawieniaSamochodu.
     *
     * @param backup obiekt klasy UstawieniaSamochodu z danymi które chcemy przywrócić
     */
    public void przywrocUstawienia(UstawieniaSamochodu backup) {
        this.przebiegCalkowity = backup.getPrzebiegCalkowity();
        this.przebieg1 = backup.getPrzebieg1();
        this.przebieg2 = backup.getPrzebieg2();
    }

    public SwiatlaSamochodowe wezSwiatla(){
        return swiatla;
    }


    /**
     * Ustawia datę i czas uruchomienia silnika, uruchamia silnik,
     * Tworzy obiekt aktualnaPodroz klasy Podroz który przechowuje dane o aktualnej podróży.
     */
    public void start() {
        predkoscSrednia = 0;
        predkoscMaksymalna = 0;
        zuzytePaliwo =0;
        srednieZuzyciePaliwa=0;
        dystans = 0;
        setCzasStartu(LocalDateTime.now());
        setWlaczony(true);
        aktualnaPodroz = new Podroz(0, przebiegCalkowity, LocalDateTime.now());
    }



    /**
     * Wyłącza silnik, zapisuje przebyty dystans, średnie zużycie paliwa oraz datę i czas zatrzymania silnika do obiektu aktualnaPodroz.
     * Dodaje dystans do przebiegu przechowywanego w pliku xml
     */
    public void stop() {
        if(!czyJestWlaczony()) return;
        if(getPredkoscAktualna() >= 0) {
            predkoscTempomatu = 0;
            setWlaczony(false);
            setCzasZatrzymania(LocalDateTime.now());

            try {
                aktualnaPodroz.setDystans(dystans);
            } catch (InvalidNumberException e) {
                e.printStackTrace();
            }
            aktualnaPodroz.setPrzebieg(przebiegCalkowity);
            aktualnaPodroz.setCzasKoncowy(LocalDateTime.now());

            System.out.println(aktualnaPodroz.toString());
            podroze.add(aktualnaPodroz);

            DanePodstawowe dane = new DanePodstawowe();
            dane.zapiszUstawienia(this);

//            // Updating database
//            try {
              //db.addPodroz(aktualnaPodroz);
              //db.odzyskajPodroze();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//
//            // Saving ustawienia
//            przelaczNaUstawienia();
//            zapiszUstawienia("bac/backup.dat");
            return;
        }
    }


    /**
     * Metoda aktualizuje parametry samochodu, wywoływana co sekundę
      */
    public void uaktualnij() {
        try {
            if (czyJestWlaczony) {
                System.out.println("aktualizuje auto");
                if (predkoscAktualna > predkoscMaksymalna && czyJestWlaczony)
                    predkoscMaksymalna = predkoscAktualna;
                // 1 sec dystans difference
                double dystansDiff = predkoscAktualna / 3600.0;
                dystans += dystansDiff;
                przebieg1 += dystansDiff;
                przebieg2 += dystansDiff;
                przebiegCalkowity += dystansDiff;
                if (dystansDiff > 0) {
                    zuzytePaliwo += (liczSpalanie(predkoscAktualna) * dystansDiff / 100);
                } else
                    zuzytePaliwo += (liczSpalanie(predkoscAktualna) / 3600);


                Duration czasPracy = Duration.between(czasStartu, LocalDateTime.now());
                if (czasPracy.getSeconds() < 0) throw new InvalidDateException();
                double czasPracyWsekundach = czasPracy.getSeconds();

                if (dystans == 0)
                    srednieZuzyciePaliwa = (float) (zuzytePaliwo * (3600f / czasPracyWsekundach));
                else
                    srednieZuzyciePaliwa = zuzytePaliwo * (100 / dystans);


                if (czyJestWlaczony)
                    predkoscSrednia = dystans * 1000 / (float) czasPracyWsekundach * 3.6f;
            }
        }
        catch (Exception e){
            System.out.println(e);
        }

    }

    /**
     * Metoda aktualizuje parametry samochodu, w trybie konsolowym
     */
    public void uaktualnij2(Temporal czasOstatniegoSprawdzenia) {
        try {
            Duration czasOdOstatniegoSprawdzenie = Duration.between( czasOstatniegoSprawdzenia, LocalDateTime.now());
            if (czasOdOstatniegoSprawdzenie.getSeconds() < 0) throw new InvalidDateException();
            double czasOdOstatniegoSprawdzeniaSekundy = czasOdOstatniegoSprawdzenie.getSeconds();
            if (czyJestWlaczony) {
                System.out.println("aktualizuje auto");
                if (predkoscAktualna > predkoscMaksymalna && czyJestWlaczony)
                    predkoscMaksymalna = predkoscAktualna;
                // 1 sec dystans difference
                double dystansDiff = predkoscAktualna * czasOdOstatniegoSprawdzeniaSekundy / 3600.0;
                dystans += dystansDiff;
                przebieg1 += dystansDiff;
                przebieg2 += dystansDiff;
                przebiegCalkowity += dystansDiff;
                if (dystansDiff > 0) {
                    zuzytePaliwo += (liczSpalanie(predkoscAktualna) * dystansDiff / 100);
                } else
                    zuzytePaliwo += (liczSpalanie(predkoscAktualna) / 3600);


                Duration czasPracy = Duration.between(czasStartu, LocalDateTime.now());
                if (czasPracy.getSeconds() < 0) throw new InvalidDateException();
                double czasPracyWsekundach = czasPracy.getSeconds();

                if (dystans == 0)
                    srednieZuzyciePaliwa = (float) (zuzytePaliwo * (3600f / czasPracyWsekundach));
                else
                    srednieZuzyciePaliwa = zuzytePaliwo * (100 / dystans);


                if (czyJestWlaczony)
                    predkoscSrednia = dystans * 1000 / (float) czasPracyWsekundach * 3.6f;
            }
        }
        catch (Exception e){
            System.out.println(e);
        }

    }

    public float getSrednieZuzyciePaliwa() {
        return srednieZuzyciePaliwa;
    }

    public void setSrednieZuzyciePaliwa(float srednieZuzyciePaliwa) {
        this.srednieZuzyciePaliwa = srednieZuzyciePaliwa;
    }

    public float getZuzytePaliwo() {
        return zuzytePaliwo;
    }

    public void setZuzytePaliwo(float zuzytePaliwo) {
        this.zuzytePaliwo = zuzytePaliwo;
    }

    public float liczSpalanie(float predkosc){
        if (predkosc==0){
            return 2f;
        }
        if (predkosc>0 && predkosc < 30){
            return (float) (20f* (Math.random()*0.7)+0.3);
        }
        if (predkosc>=30 && predkosc < 90){
            return (float) (10f* (Math.random()*0.7)+0.3);
        }
        if (predkosc>=90 && predkosc < 200){
            return (float) (18f* (Math.random()*0.7)+0.3);
        }
        return 10f;
    }

    /**
     * Zwraca prędkość średnią od początku podróży
     * @return prędkość średnia
     */
    public float getPredkoscSrednia() {
        return predkoscSrednia;
    }


    /**
     * Metoda zmieniajaca predkosc pojazdu, używana podczas przyśpieszania lub hamowania.
     */
    // czyli gdy auto hamuje np -10 km/h na sekunde hamowania gdy jedzie luzem -1 gdy przyspiesza +5 km/h na sekunde
    public void zmienPredkosc(float roznicaPredkosciWSekundzie) {
        if (predkoscAktualna + roznicaPredkosciWSekundzie> 0f && predkoscAktualna + roznicaPredkosciWSekundzie< 210);
            predkoscAktualna= predkoscAktualna + roznicaPredkosciWSekundzie;
        if (predkoscAktualna + roznicaPredkosciWSekundzie<0f)
            predkoscAktualna= 0f;
        if (predkoscAktualna + roznicaPredkosciWSekundzie> 210)
        predkoscAktualna= 210f;

    }




    /**
     * Zwraca ustawienie akutalnie zachowane w obiekcie klasy Auto.
     * @return zachowane ustawienia
     */
    public UstawieniaSamochodu getUstawienia() {
        return ustawienia;
    }


    /**
     * Zeruje wszystkie wartości zachowanych ustawień samochodu.
     *
     */
    public void wyzerujUstawienia() {
        this.ustawienia = new UstawieniaSamochodu();
    }

    /**
     * Oblicza czas pomiędzy uruchomieniem i zatrzymaniem silnika.
     * Zapisuje czas trwania do zmiennej czasCalkowity
     * @throws InvalidDateException wyjątek niepoprawnej daty
     */
    public void getAktualnyCzasWlaczeniaSamochodu() throws InvalidDateException {
        if(czasStartu == null) return;
        if(czyJestWlaczony)czasZatrzymania = LocalDateTime.now();
        Duration czasPracy = Duration.between(czasStartu, czasZatrzymania);
        if(czasPracy.getSeconds() < 0) throw new InvalidDateException();
        czasWsekundach = czasPracy.getSeconds();
        czasCalkowity = LocalTime.of((int)czasWsekundach / 360 % 24, (int)czasWsekundach / 60 % 60, (int)czasWsekundach % 60);
    }




    /**
     * Zwraca przebieg całkowity.
     * @return zwraca ilość przebytych kilometrów
     */
    public float getPrzebiegTotal() {
        return przebiegCalkowity;
    }


    public String getCzasCalkowity() {
        return czasCalkowity.toString();
    }

    /**
     * Dodaje do przebiegu podaną wartość.
     */
    public void uaktualnijPrzebiegCalkowity(float ekstraPrzebieg) {
        this.przebiegCalkowity += ekstraPrzebieg;
    }

    /**
     * Zwraca wartość pierwszego licznika dziennego.
     * @return zwraca ilość przebytych kilometrów
     */
    public float getPrzebieg1() {
        return przebieg1;
    }

    /**
     * Dodaje do pierwszego licznika dziennego podaną wartość.
     */
    public void uaktualnijPrzebieg1(float ekstraPrzebieg) {
        this.przebieg1 += ekstraPrzebieg;
    }

    /**
     * Resetuje wartość pierwszego licznika dziennego.
     */
    public void wyzerujPrzebieg1() {
        this.przebieg1 = 0;
    }

    /**
     * Zwraca wartość drugiego licznika dziennego.
     * @return zwraca ilość przebytych kilometrów
     */
    public float getPrzebieg2() {
        return przebieg2;
    }

    /**
     * Dodaje do drugiego licznika dziennego podaną wartość.
     */
    public void uaktualnijPrzebieg2(float ekstraPrzebieg) {
        this.przebieg2 += ekstraPrzebieg;
    }

    /**
     * Resetuje wartość drugiego licznika dziennego.
     */
    public void wyzerujPrzebieg2() {
        this.przebieg2 = 0;
    }

    /**
     * Zwraca przebyty dystans w danej podróży
     * @return zwraca długość przebytego dystansu
     */
    public float getDystans() {
        return dystans;
    }

    /**
     * Dodaje do odległości podróży podaną zmienną.
     */
    public void dodajDystansDoPodrozy(float a) {
        this.dystans += a;
    }




    public float getPrzebiegCalkowity() {
        return przebiegCalkowity;
    }

    /**
     * Zwraca maksymalną prędkość.
     * @return zwraca maksymalną prędkość
     */
    public float getPedkoscMaksymalna() {
        return predkoscMaksymalna;
    }

    /**
     * Ustawia maksynalną prędkość.
     * @param predkoscMaksymalna maksymalną prędkość
     */
    public void setPedkoscMaksymalna(float predkoscMaksymalna) {
        this.predkoscMaksymalna = predkoscMaksymalna;
    }


    public float getPredkoscAktualna() {
        return this.predkoscAktualna;
    }

    /**
     * Ustawia aktualną prędkość.
     * @param predkoscAktualna aktualna prędkość
     */
    public void setPedkoscAktualna(float predkoscAktualna) {
        this.predkoscAktualna = predkoscAktualna;
    }




    /**
     * Stan silnika (włączony/wyłączony).
     * @return Zwraca aktualny stan silnika jako zmienna boolowska
     */
    public boolean czyJestWlaczony() {
        return czyJestWlaczony;
    }

    /**
     * Ustawia stan silnika ( wlaczony/ wylaczony )
     * @param czyJestWlaczony stan silnika jako true/false
     */
    public void setWlaczony(boolean czyJestWlaczony) {
        this.czyJestWlaczony = czyJestWlaczony;
    }

    /**
     * Zwraca czas uruchomienia silnika.
     * @return czas początkowy jako String
     */
    public String getCzasStartu() {
        return czasStartu.format(formatCzasu);
    }

    /**
     * Ustawia czas startu silnika.
     * @param czasStartu czas początkowy
     */
    public void setCzasStartu(LocalDateTime czasStartu) {
        this.czasStartu = czasStartu;
    }

    /**
     * Zwraca czas wylaczenia silnika.
     * @return czas zatrzymania jako String
     */
    public String getCzasZatrzymania() {
        return czasZatrzymania.format(formatCzasu);
    }

    /**
     * Ustawia czas wylaczenia silnika.
     * @param czasZatrzymania czas zatrzymania
     */
    public void setCzasZatrzymania(LocalDateTime czasZatrzymania) {
        this.czasZatrzymania = czasZatrzymania;
    }

    /**
     * Zwraca obiekt swiatla.
     * @return obiekt swiatla klasy SwiatlaSamochodowe
     */
    public SwiatlaSamochodowe getSwiatla() {
        return swiatla;
    }

    /**
     * Zwraca prędkość tempomatu
     * @return prędkość ustawiona na tempomacie jako float
     */
    public float getPredkoscTempomatu() {
        return predkoscTempomatu;
    }

    /**
     * Ustawia prędkość na tempomacie.
     * @param predkoscTempomatu prędkość na tempomacie
     */
    public void setPredkoscTempomatu(float predkoscTempomatu) {
        this.predkoscTempomatu = predkoscTempomatu;
    }



    /**
     * Zwraca listę zapisanych podróży.
     * @return zapisane podróże
     */
    public ArrayList<Podroz> getPodroze() {
        return podroze;
    }

    /**
     * Zwraca czas jaki upłynął od uruchomienia silnika.
     * @return czasWsekundach czas w sekundach
     */
    public long getCzasWsekundach() {
        return czasWsekundach;
    }

    /**
     * Zwraca obiekt klasy Databse, reprezentujący bazę danych.
     * @return obiekt klasy Database
     */
//    public Database getDb() {
//        return db;
//    }




    @Override
    public String toString() {
        try {
            Duration czasPracy = Duration.between(czasStartu, LocalDateTime.now());
            if (czasPracy.getSeconds() < 0) throw new InvalidDateException();
            double czasPracyWsekundach = czasPracy.getSeconds();
            double czasPracyWMinutach = czasPracyWsekundach / 60;

            return "Auto{" +
                    "przebiegCalkowity=" + przebiegCalkowity +
                    ", przebieg1=" + przebieg1 +
                    ", przebieg2=" + przebieg2 +
                    ", dystans=" + dystans +
                    ", srednieZuzyciePaliwa=" + srednieZuzyciePaliwa +
                    ", zuzytePaliwo=" + zuzytePaliwo +
                    ", predkoscMaksymalna=" + predkoscMaksymalna +
                    ", predkoscAktualna=" + predkoscAktualna +
                    ", predkoscSrednia=" + predkoscSrednia +
                    ", tempomat=" + tempomat +
                    ", czasCalkowity=" + czasPracyWMinutach + " min" +
                    '}';
        }
        catch (Exception e){
            System.out.println(e);
            return "Awaria";
        }
    }

}
