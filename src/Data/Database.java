package Data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class Database {

    private String connectionUrl;
    private Statement statement;
    private Connection connection;

    /**
     * Konstruktor łączy się z bazą danych oraz tworzy obiekt zapytania które zostanie wysłane do bazy danych.
     * @throws SQLException wyjatek nieudanego połączenia z bazą danych
     */
    public Database() throws SQLException  {
        connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=JDBC_database;";
        connection = DriverManager.getConnection(connectionUrl, "JDBC" , "Java1234");
        statement = connection.createStatement();
    }

    /**
     * Dodaje wpis do bazy danych zawierający długość, średnie zużycie paliwa itp.
     * @param podroz obiekt klasy Podroz zawierający informacje o aktualnej podróży
     * @throws SQLException wyjatek nieudanej operacji na bazie danych
     */
    public void addPodroz(Podroz podroz) throws SQLException {
        PreparedStatement pstmt = null;

        pstmt = connection.prepareStatement("INSERT INTO Podroze VALUES (?, ?, ?, ?, ?);");

        pstmt.setFloat(1, podroz.getDystans());
        pstmt.setFloat(2, podroz.getPrzebieg());
        pstmt.setFloat(3, podroz.getSrednieZuzyciePaliwa());
        pstmt.setString(4, podroz.getCzasPoczatkowyString());
        pstmt.setString(5, podroz.getCzasKoncowyString());

        pstmt.executeUpdate();

        if (pstmt != null) pstmt.close();
    }

    /**
     * Usuwa wpis od podanym identyfikatorze.
     * @param id numer wpisu ktry chcemy usunać
     * @throws SQLException wyjatek nieudanej operacji na bazie danych
     */
    public void usunPodroz(int id) throws SQLException {
        PreparedStatement pstmt = null;
        pstmt = connection.prepareStatement("DELETE FROM Podroze WHERE id=?;");
        pstmt.setInt(1, id);
        pstmt.executeUpdate();
        if (pstmt != null) pstmt.close();
    }

    public int getPodrozId(String start, String stop) {
        try {
            ResultSet rs = statement.executeQuery("SELECT id FROM Podroze WHERE dataPoczatkowa='" + start + "' AND dataKoncowa='" + stop + "';");
            int id = -1;
            while (rs.next()) {
                id = rs.getInt(1);
            }
            return id;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    /**
     * Usuwa z bazy danych wpisy których przebyty dystans lub średnie spalanie wynosi 0.
     * Historia podróży przechowuje wszystkie uruchchomienia pojazdu, nawet gdy dystans równy jest zeru.
     * Możliwość usunięcia takich wpisów pozostaje w rękach użytkownika.
     * @throws SQLException wyjatek nieudanej operacji na bazie danych
     */
    public void usunEmptyPodroze() throws SQLException {
        PreparedStatement pstmt = null;
        pstmt = connection.prepareStatement("DELETE FROM Podroze WHERE dystans='0' OR srednieZuzyciePaliwa='0';");
        pstmt.executeUpdate();
        if (pstmt != null) pstmt.close();
    }

    /**
     * Aktualizuje bazę danych o wpisy z listy.
     * @param podroze lista objektow klasy Podroz
     * @throws SQLException wyjatek nieudanej operacji na bazie danych
     */
    public void updatePodroze(ArrayList<Podroz> podroze) throws SQLException {
        for(Podroz t : podroze) addPodroz(t);
    }

    /**
     * Pobiera z bazy danych wszystkie wpisy jako listę.
     * @return lista objektów klasy Podroz zawierająca wpisy z bazy danych
     * @throws SQLException wyjatek nieudanej operacji na bazie danych
     */
    public ArrayList<Podroz> odzyskajPodroze() throws SQLException {
        ArrayList<Podroz> databasePodroze = new ArrayList<Podroz>();
        ResultSet rs = statement.executeQuery("SELECT * FROM Podroze ORDER BY dataPoczatkowa DESC");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.0");

        while (rs.next()) {
            float dystans = rs.getFloat(2);
            float przebiegCalkowity = rs.getFloat(3);
            float srednieZuzyciePaliwa = rs.getFloat(4);
            LocalDateTime dataPoczatkowa = LocalDateTime.parse(rs.getString(5), formatter);
            LocalDateTime dataKoncowa = LocalDateTime.parse(rs.getString(6), formatter);

            Podroz newPodroz = new Podroz(dystans, przebiegCalkowity, srednieZuzyciePaliwa, dataPoczatkowa);
            newPodroz.setCzasKoncowy(dataKoncowa);
            databasePodroze.add(newPodroz);
        }

        return databasePodroze;
    }
}
