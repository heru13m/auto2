import Controller.Auto;
import View.MainFrame;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.security.Key;

import javax.swing.JFrame;
//import javax.swing.text.Document;

import Data.Podroz;
import View.MainFrame;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import org.bson.Document;

public class Main {

    public void addPodroz(Podroz podroz){

    }

    public static void main(String[] args) throws InterruptedException {
       // new KeyListenerExample();
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



        Auto samochodzik = new Auto();
        new MainFrame(samochodzik);
        samochodzik.start();
        //zuzycie na postoju
        for ( int i =0; i<30;i++)
        {

            Thread.sleep(1000);
            samochodzik.uaktualnij();

        }
        samochodzik.zmienPredkosc(-2);
        samochodzik.setPedkoscAktualna(150);
        samochodzik.getPredkoscAktualna();
        samochodzik.zmienPredkosc(-10);
        for ( int i =0; i<10;i++)
        {

            Thread.sleep(1000);
            samochodzik.uaktualnij();

        }
        samochodzik.stop();
        System.out.println(samochodzik.getPrzebiegCalkowity());
        System.out.println(samochodzik.getPrzebieg1());
        System.out.println(samochodzik.getPrzebieg2());
        System.out.println(samochodzik.getPredkoscSrednia());
        System.out.println(samochodzik);
        System.out.println(samochodzik.getSwiatla().toString());

        System.out.println("Hello World!");

    }
}
