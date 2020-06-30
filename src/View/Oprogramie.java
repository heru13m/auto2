package View;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Klasa odpowiedzialna za okno z instrukcją obsługi
 */

public class Oprogramie extends JFrame {

    /**
     * Tworzy informacyjne okienko dialogowe.
     */
    public Oprogramie(String nazwa) {
        super(nazwa);
        setSize(500, 400);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2 - this.getWidth()/2, dim.height/2 - this.getHeight()/2);
        setResizable(false);

        JLabel html = new JLabel("<html>"
                + "<h3>Instrukcja:</h3>\r\n" +
                "<ul style=\"list-style-type: disc;\">\r\n" +
                "<li>spacja - włącza/wyłącza silnik\r\n" +
                "<li>strzałki - odpowiadaja za przyśpieszanie/hamowanie i światła migaczy</li>\r\n" +
                "<li>1 - włącza/wyłącza swiatła pozycyjne</li>\r\n" +
                "<li>2 - włącza/wyłącza światła mijania</li>\r\n" +
                "<li>3 - włącza/wyłącza światła drogowe</li>\r\n" +
                "<li>4 - włącza/wyłącza światła przeciwgielne przednie</li>\r\n" +
                "<li>5 - włącza/wyłącza światła przeciwmgielne tylne</li>\r\n" +
                "<li>T - włącza/wyłącza tempomat</li>\r\n" +
                "</ul>"
                + "</html>");
        html.setHorizontalAlignment(JLabel.CENTER);
        add(html);
    }

}