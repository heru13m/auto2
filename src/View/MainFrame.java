package View;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import Controller.Auto;

/**
 * Klasa odpowiada za otwarcie głównego okna JFrame oraz wywołanie klas MenuPanel, DashboardPanel i ControlsPanel.
 * @version 1.0
 * @author Adam Kalisz
 * @author Kamil Rojszczak
 *
 */
public class MainFrame extends JFrame {

    private MenuPanel menuPanel;
    private DashboardPanel dashboardPanel;
    private ControlsPanel controlsPanel;

    /**
     * Otwiera główne okno JFrame oraz wywołuje klas MenuPanel, DshboardPanel i ControlsPanel.
     * @param car obiekt klasy Car
     */
    public MainFrame(Auto auto) {
        super("Car dashboard");
        setResizable(false);
        setLayout(new BorderLayout());
        setSize(1000, 805);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2 - this.getWidth()/2, dim.height/2 - this.getHeight()/2);

//        menuPanel = new MenuPanel(auto);
//        dashboardPanel = new DashboardPanel(auto);
//        controlsPanel = new ControlsPanel(auto);
//
//        add(menuPanel, BorderLayout.PAGE_START);
//        add(dashboardPanel, BorderLayout.CENTER);
//        add(controlsPanel, BorderLayout.PAGE_END);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
