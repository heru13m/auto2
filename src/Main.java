import Controller.Auto;
import View.EkranGlowny;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;

//import javax.swing.text.Document;

import Data.Podroz;

import javax.swing.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import org.bson.Document;



public class Main {

    public void addPodroz(Podroz podroz){

    }

    public static void main(String[] args) throws InterruptedException {
        if (args.length == 0) {


            Auto samochodzik = new Auto();
            //  new EkranGlowny(samochodzik);
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    new EkranGlowny(samochodzik);
                }
            });
        } else if (args[0].compareTo("g") == 0 || args[0].compareTo("graficzny") == 0) {
            try {
                Temporal czasOstatniegoSprawdzenia =LocalDateTime.now();

                Auto samochodzik = new Auto();

                System.out.println("Witaj, wybierz co chcesz zrobić:");
                System.out.println("S: Uruchom silnik");
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                System.out.print("Twój wybór: ");
                String s = null;
                s = br.readLine();

                if (s.equals("S")) {
                    samochodzik.start();
                    czasOstatniegoSprawdzenia= LocalDateTime.now();
                    while (s.equals("E") == false) {
                        if (samochodzik.czyJestWlaczony() == false)
                            System.out.println("S: Uruchom silnik");
                        System.out.println("D: Sprawdz dane samochodu");
                        System.out.println("W: Przyspiesz");
                        System.out.println("S: Zwolnij");
                        System.out.println("P: Zmien status swatel postojowych");
                        System.out.println("P: Zmien status swatel mijania");
                        System.out.println("O: Zakoncz podroz");
                        System.out.println("E: Wyjdz");

                        System.out.print("Twój wybór: ");
                        s = br.readLine();

                        if (s.equals("D")) {
                            samochodzik.uaktualnij2(czasOstatniegoSprawdzenia);
                            czasOstatniegoSprawdzenia= LocalDateTime.now();
                            System.out.println(samochodzik.toString());
                        }
                        if (s.equals("W")) {
                            System.out.println("Przyspieszam z predkosci "+ Float.toString(samochodzik.getPredkoscAktualna()));
                            samochodzik.zmienPredkosc(5);
                            System.out.println("aktualna predkosc "+ Float.toString(samochodzik.getPredkoscAktualna()));
                            samochodzik.uaktualnij2(czasOstatniegoSprawdzenia);
                            czasOstatniegoSprawdzenia= LocalDateTime.now();
                        }
                        if (s.equals("S")) {
                            System.out.println("Zwalniam z predkosci "+ Float.toString(samochodzik.getPredkoscAktualna()));
                            samochodzik.zmienPredkosc(-10);
                            System.out.println("aktualna predkosc "+ Float.toString(samochodzik.getPredkoscAktualna()));
                            samochodzik.uaktualnij2(czasOstatniegoSprawdzenia);
                            czasOstatniegoSprawdzenia= LocalDateTime.now();

                        }

                        if (s.equals("P")) {
                            if (samochodzik.wezSwiatla().isPozycyjne()==false) {
                                samochodzik.wezSwiatla().wlaczPozycyjne();
                                System.out.println("Właczono swiatla pozycyjne");
                            }
                            else{
                                samochodzik.wezSwiatla().wylaczPozycje();
                            System.out.println("Wylaczono swiatla pozycyjne");}

                        }

                        if (s.equals("M")) {
                            if (samochodzik.wezSwiatla().isMijania()==false) {
                                samochodzik.wezSwiatla().wlaczMijania();
                                System.out.println("Właczono swiatla mijania");
                            }
                            else{
                                samochodzik.wezSwiatla().wylaczMijania();
                                System.out.println("Wylaczono swiatla mijania");}

                        }

                        if (s.equals("O"))
                            samochodzik.stop();
                        Thread.sleep(1000);
                    }


                }
            }catch (Exception e){
                System.out.println(e);
            }

        } else {
            System.out.println("Opcja nieznana");
        }

//         // Creating a Mongo client
//         MongoClient mongo = new MongoClient( "localhost" , 27017 );
//         // Creating Credentials
//         MongoCredential credential;
//         credential = MongoCredential.createCredential("sampleUser", "Rejestr", "password".toCharArray());
//         System.out.println("Connected to the database successfully");
//         //Accessing the database
//         MongoDatabase db = mongo.getDatabase("Rejestr");
//         //Creating a collection
//         db.createCollection("Podroze");
//         System.out.println("Collection created successfully");




   //     samochodzik.start();
//        //zuzycie na postoju
//        for ( int i =0; i<5;i++)
//        {
//
//            Thread.sleep(1000);
//            samochodzik.uaktualnij();
//
//        }
//        samochodzik.zmienPredkosc(-2);
//        samochodzik.setPedkoscAktualna(150);
//        samochodzik.getPredkoscAktualna();
//        samochodzik.zmienPredkosc(-10);
//        for ( int i =0; i<10;i++)
//        {
//
//            Thread.sleep(1000);
//            samochodzik.uaktualnij();
//
//        }
//        samochodzik.stop();
//        System.out.println(samochodzik.getPrzebiegCalkowity());
//        System.out.println(samochodzik.getPrzebieg1());
//        System.out.println(samochodzik.getPrzebieg2());
//        System.out.println(samochodzik.getPredkoscSrednia());
//        System.out.println(samochodzik);
//
//        System.out.println(samochodzik.getSwiatla().toString());
//        System.out.println(samochodzik.getZuzytePaliwo());
//
//        System.out.println("Hello World!");

    }
}
