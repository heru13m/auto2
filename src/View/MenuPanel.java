package View;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import Controller.Auto;
/**
 * Odpowiada za stworzenie menu
 * @version 1.0
 * @author Maciej Ksiezak
 * @author Mateusz Mus
 *
 */
public class MenuPanel extends JPanel implements ActionListener {

    private Auto auto;
    private Oprogramie oprogramie;
    private Autorzy autorzy;
    private JMenuBar menubar;
    private JMenu helpMenu;
    private JMenuItem miAbout, miInfo;


    public MenuPanel(Auto auto) {
        this.auto = auto;
        setLayout(new GridLayout(1, 1));

        // Menu bar
        menubar = new JMenuBar();
        add(menubar);
        helpMenu = new JMenu("Menu");
        helpMenu.setMnemonic(KeyEvent.VK_H);

        miInfo = new JMenuItem("Autorzy");
        miAbout = new JMenuItem("O programie");
        miInfo.addActionListener(this);
        miAbout.addActionListener(this);
        helpMenu.add(miInfo);
        helpMenu.add(miAbout);
        menubar.add(helpMenu);

        oprogramie = new Oprogramie("O programie");
        oprogramie.setSize(oprogramie.getSize());

        autorzy = new Autorzy("Info");
        autorzy.setSize(autorzy.getSize());
    }

    /**
     * Obsługa zdarzeń menu.
     * @param e zdarzenie
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource() == miAbout) {
            oprogramie.setVisible(!oprogramie.isVisible());
        }
        if(e.getSource() == miInfo) {
            autorzy.setVisible(!autorzy.isVisible());
        }

    }

}
