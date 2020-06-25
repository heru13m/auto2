package Controller;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import Data.DanePodstawowe;
import Data.UstawieniaSamochodu;
import Data.Database;
import Data.Podroz;
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
    private float zuzytePaliwo;
    private float predkoscMaksymalna;
    private float predkoscTempomatu;
    private float predkoscAktualna;
    private float predkoscSrednia;
    private float rpms;
    private int rpmMax;
    private LocalDateTime czasStartu;
    private LocalDateTime czasZatrzymania;
    private LocalTime czasCalkowity;
    private DateTimeFormatter formatCzasu;
    private boolean czyJestWlaczony;
    private SwiatlaSamochodowe swiatla;
    private float[] PrzelozeniaBiegow = {0.f, 0.0056f, 0.011f, 0.017f, 0.0232f, 0.029f, 0.036f};
    private short bieg; // 0 neutral, 1-6 normal
    private float temperaturaWody, benzyna;
    private ArrayList<Podroz> podroze;
    private Podroz aktualnaPodroz;
    private Database db;
    private long czasWsekundach;
    private UstawieniaSamochodu ustawienia;
    private float benzynaPelna;

    /**
     * Konstruktor klasy Car. Ustawia domyślne wartości oraz tworzy obiekt klasy Database do którego będą później zapisywane
     * dane z podróży. Jeżeli istnieje plik "bac/backup.dat" z ustawieniami wczytuje go i wykorzystuje zapisane w nim dane.
     */
    public Auto() {
        dystans = 0;
        srednieZuzyciePaliwa = 0;
        predkoscMaksymalna = 0;
        predkoscAktualna = 0;
        rpms = 0;
        przebiegCalkowity = 0;
        przebieg1 = 0;
        przebieg2 = 0;
        bieg = 0;
        predkoscTempomatu = 0;
        rpmMax = 7000;
        temperaturaWody = 0;
        benzyna = 0;
        zuzytePaliwo = 0;
        swiatla = new SwiatlaSamochodowe();
        formatCzasu = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        podroze = new ArrayList<>();
        benzynaPelna = 60;

//        // Connecting to database
//        try {
//            db = new Database();
//        } catch (SQLException e) {
//            System.out.println("Connecting to database failed");
//        }
//
//        // Loading backup
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
        aktualnaPodroz = new Podroz(0, przebiegCalkowity, 0, LocalDateTime.now());
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
                aktualnaPodroz.setLength(dystans);
            } catch (InvalidNumberException e) {
                e.printStackTrace();
            }
            aktualnaPodroz.setPrzebieg(przebiegCalkowity);
            aktualnaPodroz.setSrednieZuzyciePaliwa(srednieZuzyciePaliwa);
            aktualnaPodroz.setCzasKoncowy(LocalDateTime.now());

            System.out.println(aktualnaPodroz.toString());
            podroze.add(aktualnaPodroz);

            DanePodstawowe dane = new DanePodstawowe();
            dane.zapiszUstawienia(this);

//            // Updating database
//            try {
//                db.addPodroz(aktualnaPodroz);
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
     * Zmienia bieg na wyższy.
     * Sprawdza czy możliwa jest zmiana biegu na wyższy, jeśli tak to go zmienia oraz odpowiednio zmniejsza obroty silnika.
     */
    public void zmienBiegNaWyzszy() {
        if(bieg < 6) {
            bieg++;
            rpms -= 700;
        }
    }

    /**
     * Zmienia bieg na niższy.
     * Sprawdza czy możliwa jest zmiana biegu na niższy, jeśli tak to go zmienia oraz odpowiednio zwiększa obroty silnika.
     */
    public void zmienBiegNaNizszy() {
        if(bieg > 0) {
            bieg--;
            if(czyJestWlaczony) rpms += 600;
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
        if(predkoscAktualna > predkoscMaksymalna && czyJestWlaczony) predkoscMaksymalna = predkoscAktualna;
        // 1 sec dystans difference
        double dystansDiff = predkoscAktualna / 3.6 / 1000;
        float benzynaDiff = 0;
        dystans += dystansDiff;
        przebieg1 += dystansDiff;
        przebieg2 += dystansDiff;
        przebiegCalkowity += dystansDiff;
        if(czyJestWlaczony) predkoscSrednia = dystans * 1000 / czasWsekundach * 3.6f;
        if(temperaturaWody < 20 && rpms > 100) temperaturaWody += 0.1;
        if(rpms > 0) {
            if(rpms < 2000) benzynaDiff = (float) (9.5 / 3600);
            else if(rpms > 5000) benzynaDiff = (float) (15.7 / 3600);
            else benzynaDiff = (float) (6.2 / 3600);
            zuzytePaliwo += benzynaDiff;
        }
        srednieZuzyciePaliwa = zuzytePaliwo * 3600 / czasWsekundach;
        benzyna -= benzynaDiff;
        if(benzyna <= 0) stop();
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

    /**
     * Przywraca ustawienia z odczytanego przedniej obiektu UstawieniaSamochodu.
     *
     * @param backup obiekt klasy UstawieniaSamochodu z danymi które chcemy przywrócić
     */
    public void przywrocUstawienia(UstawieniaSamochodu backup) {
        this.przebiegCalkowity = backup.getPrzebiegCalkowity();
        this.przebieg1 = backup.getPrzebieg1();
        this.przebieg2 = backup.getPrzebieg2();
        this.benzyna = backup.getBenzyna();
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
     * Zwraca aktualny biegu samochodu.
     * @return wartość atualnego biegu samochodu jako String
     */
    public String podajAktualnyBiegWstringu() {
        return bieg == 0 ? "N" : String.valueOf(this.bieg);
    }

    /**
     * Ustawia ilość paliwa.
     * @param benzyna ilość paliwa jako float
     */
    public void setBenzyna(float benzyna) {
        if(benzyna > benzynaPelna) return;
        this.benzyna = benzyna;
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

    /**
     * Zwraca średnie zużycie paliwa.
     * @return zwraca średnie zużycie paliwa jako float
     */
    public float getSrednieZuzyciePaliwa() {
        return srednieZuzyciePaliwa;
    }

    /**
     * Tankuje samochód.
     * Dodaje paliwo o ile pojemność baku na to pozwala.
     * @param ekstraBenzyna wartość o jaką chcemy zwięszkyć ilość paliwa
     * @return status wykonanej operacji: 1 - nieudana, 0 - udana
     */
    public boolean dodajBenzyne(float ekstraBenzyna) {
        if(benzyna + ekstraBenzyna > benzynaPelna) return true;
        benzyna += ekstraBenzyna;
        return false;
    }

    /**
     * Ustawia średnie zużycie paliwa.
     * @param srednieZuzyciePaliwa średnie zużycie paliwa jako float
     */
    public void setSrednieZuzyciePaliwa(float srednieZuzyciePaliwa) {
        this.srednieZuzyciePaliwa = srednieZuzyciePaliwa;
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

    /**
     * Oblicza aktualną prędkość.
     * Iloczyn obrotów silnika i wartości odpowiadającej biegu samochodu.
     * @return zwraca aktualną prędkość jako float
     */
    public float getPredkoscAktualna() {
        return rpms * PrzelozeniaBiegow[bieg];
    }

    /**
     * Ustawia aktualną prędkość.
     * @param predkoscAktualna aktualna prędkość
     */
    public void setPedkoscAktualna(float predkoscAktualna) {
        this.predkoscAktualna = predkoscAktualna;
    }

    /**
     * Zwraca obroty silnika.
     * @return  obroty silnika jako float
     */
    public float getRpms() {
        return rpms;
    }

    /**
     * Ustawia obroty silnika.
     * @param rpms obroty silnika jako int
     */
    public void setRpms(int rpms) {
        this.rpms = rpms;
    }

    /**
     * Zwraca maksymalne obroty silnika.
     * @return maksymalne obroty silnika jako float
     */
    public int getRpmMax() {
        return rpmMax;
    }

    /**
     * Ustawia maksymalne obroty silnika.
     * @param rpmMax obroty silnika jako int
     */
    public void setRpmMax(int rpmMax) {
        this.rpmMax = rpmMax;
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
     * Zwraca temperaturę płynu chłodniczego.
     * @return temperatura płynu chłodniczego jako float
     */
    public float getTemperaturaWody() {
        return temperaturaWody;
    }

    /**
     * Ustawia temperaturę płynu chłodniczego.
     * @param temperaturaWody temperatura płynu chłodniczego jako float
     */
    public void setTemperaturaWody(float temperaturaWody) {
        this.temperaturaWody = temperaturaWody;
    }

    /**
     * Zwraca ilość paliwa.
     * @return ilość paliwa jako float
     */
    public float getBenzyna() {
        return benzyna;
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
    public Database getDb() {
        return db;
    }

    /**
     * Zwraca pojemność baku.
     * @return pojemność baku w litrach
     */
    public float getBenzynaPelna() {
        return benzynaPelna;
    }

    /**
     * Zwraca tablice z przełożeniami biegów.
     * Aby otrzymać konkretne przełożenie odwołujemy się do elementu tablicy o indeksie równym aktualnemu biegowi.
     * Wartość w tablicy o indeksie 0 wynosi 0 - jest to bieg neutralny.
     * @return tablica przełożeń biegów podanych jako float
     */
    public float[] getPrzelozeniaBiegow() {
        return PrzelozeniaBiegow;
    }

    /**
     * Ustawia przełożenia biegów z tablicy {@link #getPrzelozeniaBiegow()}
     * @param PrzelozeniaBiegow tablica z przełożeniami biegów
     */
    public void setPrzelozeniaBiegow(float[] PrzelozeniaBiegow) {
        this.PrzelozeniaBiegow = PrzelozeniaBiegow;
    }
}
