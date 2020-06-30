package Data;

import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import org.bson.Document;

/**
 * Klasa odpowiedzialna za połączenie z bazą danych
 **
 * @version 1.0
 * @author Maciej Ksiezak
 * @author Mateusz Mus
 *
 */

public class Database {

    private String connectionUrl;
    private Statement statement;
    private Connection connection;
    MongoClient mongo = new MongoClient( "localhost" , 27017 );
    MongoDatabase db = mongo.getDatabase("Rejestr");

    MongoCollection<Document> kolekcjaPodrozy = db.getCollection("Podroze");

    /**
     * Konstruktor łączy się z bazą danych
     */
    public Database()
    {
        MongoClient mongo = new MongoClient( "localhost" , 27017 );
        MongoDatabase db = mongo.getDatabase("Rejestr");
        MongoCollection<Document> kolekcjaPodrozy = db.getCollection("Podroze");

    }

    /**
     * Dodaje wpis do bazy danych opisujacy podróż
     * @param podroz obiekt klasy Podroz zawierający informacje o aktualnej podróży
     */
    public void addPodroz(Podroz podroz){
        Document p = new Document("Dystans",podroz.getDystans())
                .append("Przebieg",podroz.getPrzebieg())
                .append("Poczatek",podroz.getCzasPoczatkowyString())
                .append("Koniec",podroz.getCzasKoncowyString());

        kolekcjaPodrozy.insertOne(p);

    }

    /**
     * Pobiera z bazy danych wszystkie wpisy
     * @return lista objektów klasy Podroz
     */

    public ArrayList<Document> odzyskajPodroze(){
        ArrayList<Document> databasePodroze = new ArrayList<Document>();
        ArrayList<Podroz> databasePodroze2 = new ArrayList<Podroz>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        FindIterable<Document> cursor = kolekcjaPodrozy.find();
        MongoCursor<Document> iterator = cursor.iterator();
        while(iterator.hasNext())
            {

                Document document = iterator.next();
              databasePodroze.add(document);

            }

        for(int i=0; i < databasePodroze.size();i++)
        {
            double a = databasePodroze.get(i).getDouble("Dystans");
            double b = databasePodroze.get(i).getDouble("Przebieg");
            String c = databasePodroze.get(i).get("Poczatek","-");
            String d = databasePodroze.get(i).get("Koniec","-");
            Podroz aktualnaPodroz = new Podroz((float)a,(float)b,LocalDateTime.parse((c), formatter),LocalDateTime.parse((d), formatter));
            databasePodroze2.add(aktualnaPodroz);
        }

        System.out.println("..................------...........----------........");
        System.out.println(databasePodroze2);
        System.out.println("..................");
        return databasePodroze;
    }

}