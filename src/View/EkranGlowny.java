package View;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import Controller.Auto;

/**
 * Klasa odpowiada za otwarcie głównego okna JFrame.
 **
 * @version 1.0
 * @author Maciej Ksiezak
 * @author Mateusz Mus
 *
 */

public class EkranGlowny extends JFrame {


   private DeskaRozdzielcza deskaRozdzielcza;
    private MenuPanel menuPanel;

    /**
     * Otwiera główne okno JFrame i wywołuje klasy DeskaRozdzielcza,Autorzy oraz Oprogramie
     * @param auto obiekt klasy Auto
     */
    public EkranGlowny(Auto auto) {
        super("EkranGlowny rozdzielcza");
        setResizable(false);
        setLayout(new BorderLayout());
        setSize(1000, 805);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2 - this.getWidth()/2, dim.height/2 - this.getHeight()/2);

        menuPanel = new MenuPanel(auto);
        deskaRozdzielcza = new DeskaRozdzielcza(auto);


       add(menuPanel, BorderLayout.PAGE_START);
       add(deskaRozdzielcza, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
