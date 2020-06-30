package View;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JLabel;
/**
 * Klasa odpwowiedzialna za okno z informacjami o autorach
 * @version 1.0
 * @author Maciej Ksiezak
 * @author Mateusz Mus
 *
 */
public class Autorzy extends JFrame {

    public Autorzy(String nazwa) {
        super(nazwa);
        setSize(400, 400);
        setResizable(false);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2 - this.getWidth()/2, dim.height/2 - this.getHeight()/2);

        JLabel html = new JLabel("<html><h2>Projekt deski rozdzielczej</h2>"
                + "<p style='font-size:11px;'>Prowadzący: </p> <p>dr inż. Marcin Kacprowicz</p><br>"
                + "<p style='font-size:11px;'><p>Programowanie komponentowe 2020</p><br>"
                + "<p style='font-size:11px;'>Czas zajęć: </p> <p>Wtorek 17:45</p><br>"
                + "<p style='font-size:11px;'>Autorzy: </p>"
                + "Mateusz Mus 210282<br>"
                + "Maciej Księżak 210237</html>");
        html.setHorizontalAlignment(JLabel.CENTER);
        add(html);
    }

}
