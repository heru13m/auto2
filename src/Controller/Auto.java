package Controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;

import Data.DanePodstawowe;
import Data.UstawieniaSamochodu;
import Data.Podroz;
import Data.Database;
//import Data.XMLFileManager;

/**
 * Klasa reprezentująca samochód i komputer pokładowy.
 *
 * Wykonuje operacje na takich wartościach jak przebieg całkowity, dystans, średnie spalanie, prędkość, itd.
 * Pozwala operować silnikiem, zmianą biegów, włączaniem i wyłączaniem świateł.
 * Rejestruje podróże, które zaczynają się po wywołaniu metody {@link #start()}, a kończą {@link #stop()}
 *
 * @version 1.2
 * @author Adam Kalisz
 * @author Kamil Rojszczak
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
    private LocalDateTime czasStartu;
    private LocalDateTime czasZatrzymania;
    private LocalTime czasCalkowity;
    private DateTimeFormatter formatCzasu;
    private boolean czyJestWlaczony;
    private SwiatlaSamochodowe swiatla;
    private short bieg; // 0 neutral, 1-6 normal
    private ArrayList<Podroz> podroze;
    private Podroz aktualnaPodroz;
  //  private Database db;
    private long czasWsekundach;
    private UstawieniaSamochodu ustawienia;
    private float benzynaPelna;
    private Database db;

    /**
     * Konstruktor klasy Car. Ustawia domyślne wartości oraz tworzy obiekt klasy Database do którego będą później zapisywane
     * dane z podróży. Jeżeli istnieje plik "bac/backup.dat" z ustawieniami wczytuje go i wykorzystuje zapisane w nim dane.
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
     * Ustawia datę i czas uruchomienia silnika, uruchamia silnik poprzez ustawienie flagi czyJestWlaczony.
     * Tworzy obiekt aktualnaPodroz klasy Podroz który przechowuje dane o aktualnej podróży.
     */
    public void start() {
        predkoscSrednia = 0;
        predkoscMaksymalna = 0;
        dystans = 0;
        setCzasStartu(LocalDateTime.now());
        setWlaczony(true);
        aktualnaPodroz = new Podroz(0, przebiegCalkowity, LocalDateTime.now());
    }

    /**
     * Wyłącza silnik, zapisuje przebyty dystans, średnie zużycie paliwa oraz datę i czas zatrzymania silnika do obiektu aktualnaPodroz.
     * Dodaje ostatnią podróż do bazy danych zapisuje dane do obiektu ustawień i zapisuje te ustawienia do pliku "backup.dat".
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
              db.addPodroz(aktualnaPodroz);
              db.odzyskajPodroze();
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
     * Aktualizuje wartości komputera pokładowego.
     * Zmienia wartości prędkości maksymalnej, oblicza przebyty dystans w ciągu jednej sekundy,
     * zwiększa liczniki samochodu zgodnie z przebytym dystansem, na podstawie dystansu podróży i czasu trwania oblicza średnią prędkość,
     * zwiększa odpowiednio temperaturę płynu chłodniczego, w zależności od ilości obrotów na min. oblicza ilość zużytego paliwa.
     * Pod koniec aktualizuje stan paliwa i średnie spalanie.
     */
    public void uaktualnij() {
        try {
            if (predkoscAktualna > predkoscMaksymalna && czyJestWlaczony)
                predkoscMaksymalna = predkoscAktualna;
            // 1 sec dystans difference
            double dystansDiff = predkoscAktualna / 3600.0;
            dystans += dystansDiff;
            przebieg1 += dystansDiff;
            przebieg2 += dystansDiff;
            przebiegCalkowity += dystansDiff;
            if (dystansDiff>0) {
                zuzytePaliwo += (liczSpalanie(predkoscAktualna) * dystansDiff / 100);
            }
            else
                zuzytePaliwo+= (liczSpalanie(predkoscAktualna)/3600);


            Duration czasPracy = Duration.between(czasStartu, LocalDateTime.now());
            if (czasPracy.getSeconds() < 0) throw new InvalidDateException();
            double czasPracyWsekundach = czasPracy.getSeconds();

            if (dystans ==0)
                srednieZuzyciePaliwa = (float) (zuzytePaliwo * (3600f/czasPracyWsekundach));
            else
                srednieZuzyciePaliwa = zuzytePaliwo * (100/dystans);



            if (czyJestWlaczony)
                predkoscSrednia = dystans * 1000 / (float) czasPracyWsekundach * 3.6f;
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
     * Wykonywana cyklicznie, zwiększa lub zminiejsza obroty silnika
     * w zależności od ustawionej prędkości, podobnie jak w tempomacie.
     *
     * @param predkoscDoOsiagniecia docelowa prędkość którą chcemy osiągnąć
     */
    /*
    public void przyspiesz(float predkoscDoOsiagniecia) {
        this.predkoscAktualna = this.getPredkoscAktualna();
        if(predkoscAktualna >= predkoscDoOsiagniecia) {
            rpms--;
            return;
        }
        if(rpms >= rpmMax) {
            rpms = rpmMax - 150;
        }
        float diff = predkoscDoOsiagniecia > predkoscAktualna ? 0.9f : -1.9f;
        rpms += diff;
        if(rpms < 0) rpms = 0;
    }

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
     * Przywraca ustawienia z odczytanego przedniej obiektu UstawieniaSamochodu.
     *
     * @param backup obiekt klasy UstawieniaSamochodu z danymi które chcemy przywrócić
     */
    public void przywrocUstawienia(UstawieniaSamochodu backup) {
        this.przebiegCalkowity = backup.getPrzebiegCalkowity();
        this.przebieg1 = backup.getPrzebieg1();
        this.przebieg2 = backup.getPrzebieg2();
    }

    /**
     * Zapisuje ustawienia zapisane w obiekcie klasy UstawieniaSamochodu.
     * @param sciezka ścieżka zapisu
     */
//    public void zapiszUstawienia(String sciezka) {
//        XMLFileManager fm = new XMLFileManager();
//        try {
//            fm.saveToFile(ustawienia, sciezka);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * Zwraca ustawienie akutalnie zachowane w obiekcie klasy Car.
     * @return zachowane ustawienia
     */
    public UstawieniaSamochodu getUstawienia() {
        return ustawienia;
    }

    /**
     * Ustawia aktualne wartości przebiegu i paliwa do zmiennej UstawieniaSamochodu.
     */
    public void przelaczNaUstawienia() {
        ustawienia = new UstawieniaSamochodu(this);
    }

    /**
     * Zeruje wszystkie wartości zachowanych ustawień samochodu.
     *
     */
    public void wyzerujUstawienia() {
        this.ustawienia = new UstawieniaSamochodu();
    }

    /**
     * Oblicza czas pomiędzy uruchomieniem i zatrzymaniem silnika (aktualnym czasem jeśli silnik jest uruchomiony).
     * Zapisuje czas trwania do zmiennej czasCalkowity w formacie godziny:minuty:sekundy.
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
     * @return zwraca ilość przebytych kilometrów jako float
     */
    public float getPrzebiegTotal() {
        return przebiegCalkowity;
    }

    /**
     * Zwraca wartość czasu jaki został uprzednio obliczony poprzez {@link #getAktualnyCzasWlaczeniaSamochodu()}.
     * @return zwraca wartość czasu jaki upłynął jako string w formacie: godziny:minuty:sekundy.
     */
    public String getCzasCalkowity() {
        return czasCalkowity.toString();
    }

    /**
     * Dodaje do przebiegu podaną wartość.
     * @param ekstraPrzebieg zmienna typu float która zostanie dodana do przebiegu całkowitego
     */
    public void uaktualnijPrzebiegCalkowity(float ekstraPrzebieg) {
        this.przebiegCalkowity += ekstraPrzebieg;
    }

    /**
     * Zwraca wartość pierwszego licznika dziennego.
     * @return zwraca ilość przebytych kilometrów jako float
     */
    public float getPrzebieg1() {
        return przebieg1;
    }

    /**
     * Dodaje do pierwszego licznika dziennego podaną wartość.
     * @param ekstraPrzebieg zmienna typu float która zostanie dodana
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
     * @return zwraca ilość przebytych kilometrów jako float
     */
    public float getPrzebieg2() {
        return przebieg2;
    }

    /**
     * Dodaje do drugiego licznika dziennego podaną wartość.
     * @param ekstraPrzebieg zmienna typu float która zostanie dodana
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
     * Zwraca przebyty dystans od uruchomienia silnika.
     * @return zwraca długość przebytego dystansu jako float
     */
    public float getDystans() {
        return dystans;
    }

    /**
     * Dodaje do odległości podróży podaną zmienną.
     * @param a zmienna typu float która zostanie dodana do dystansu aktualnej podróży
     */
    public void dodajDystansDoPodrozy(float a) {
        this.dystans += a;
    }




    public float getPrzebiegCalkowity() {
        return przebiegCalkowity;
    }

    /**
     * Zwraca maksymalną prędkość.
     * @return zwraca maksymalną prędkość jako float
     */
    public float getPedkoscMaksymalna() {
        return predkoscMaksymalna;
    }

    /**
     * Ustawia maksynalną prędkość.
     * @param predkoscMaksymalna maksymalną prędkość jako float
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
     * @return Zwraca aktualny stan silnika jako true/false.
     */
    public boolean czyJestWlaczony() {
        return czyJestWlaczony;
    }

    /**
     * Ustawia stan silnika.
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
     * Zwraca czas zatrzymania silnika.
     * @return czas zatrzymania jako String
     */
    public String getCzasZatrzymania() {
        return czasZatrzymania.format(formatCzasu);
    }

    /**
     * Ustawia czas zatrzymania silnika.
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
     * Zwraca prędkość ustawioną na tempomacie.
     * @return prędkość ustawiona na tempomacie jako float
     */
    public float getPredkoscTempomatu() {
        return predkoscTempomatu;
    }

    /**
     * Ustawia prędkość na tempomacie.
     * @param predkoscTempomatu prędkość na tempomacie jako float
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

    /**
     * Zwraca pojemność baku.
     * @return pojemność baku w litrach
     */
    public float getBenzynaPelna() {
        return benzynaPelna;
    }


}
