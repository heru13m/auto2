package View;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.geom.AffineTransform;
import Controller.Auto;

/**
 * Odpowiada rysowanie deski rozdzielczej oraz jej elementów
 * @version 1.0
 * @author Maciej Ksiezak
 * @author Mateusz Mus
 *
 */

public class DeskaRozdzielcza extends JPanel {
    private Image deska;
    private Image lewyMigacz;
    private Image prawyMigacz;
    private Image mgielnePrzod;
    private Image mgielneTyl;
    private Image mijania;
    private Image pozycyjne;
    private Image drogowe;

    private Auto auto;
    private int interval;
    private Image wskaznikPredkosci1;
    private JButton przebieg1Button;
    private JButton przebieg2Button;
    private JLabel srednieSpalanie;
    private JLabel przebieg1;
    private JLabel przebieg2;
    private JLabel aktualnyCzasPracyAuta;
    private JLabel przebiegCalkowity;
    private JLabel predkoscSrednia;
    private JLabel predkoscMaksymalna;
    private JLabel dystans;

    private JLabel srednieSpalanieNapis;
    private JLabel przebieg1Napis;
    private JLabel przebieg2Napis;
    private JLabel aktualnyCzasPracyAutaNapis;
    private JLabel przebiegCalkowityNapis;
    private JLabel predkoscSredniaNapis;
    private JLabel predkoscMaksymalnaNapis;
    private JLabel dystansNapis;



    private float speed = 0;
    private boolean speedingUp = false;
    private boolean braking = false;
    private boolean turn = false;

    private boolean tempomat = false;


    long lastTime = System.currentTimeMillis();
    long lastUpdateTime = System.currentTimeMillis();

    private void updateSpeed() {
        if(auto.czyJestWlaczony()==true){

            if(!tempomat){
            if(!speedingUp && speed > 0) {
            speed -= (System.currentTimeMillis() - lastTime) * 0.01;
            if(speed < 0) {
                speed = 0;
            }

            }}
            lastTime = System.currentTimeMillis();

            if((System.currentTimeMillis() - lastUpdateTime) > 1000) {
                auto.uaktualnij();
                lastUpdateTime = System.currentTimeMillis();
            }

        auto.setPedkoscAktualna(speed);


        System.out.println("Predkosc: " + speed);
        }
    }

    public DeskaRozdzielcza(Auto auto) {

        setLayout(null);
        this.auto = auto;
        interval = 1;

        lastUpdateTime = System.currentTimeMillis();

        // obrazy
        try {
            deska = ImageIO.read(new File("img/deska1.png"));
            wskaznikPredkosci1=ImageIO.read(new File("img/arrow3.png"));
            lewyMigacz=ImageIO.read(new File("img/lewy.png"));
            prawyMigacz=ImageIO.read(new File("img/prawy.png"));
            mgielnePrzod=ImageIO.read(new File("img/mgielne.png"));
            mgielneTyl=ImageIO.read(new File("img/mgielneTyl.png"));
            drogowe=ImageIO.read(new File("img/drogowe.png"));
            mijania=ImageIO.read(new File("img/mijania.png"));
            pozycyjne=ImageIO.read(new File("img/pozycyjne.png"));

            przebieg1Button = new JButton("Reset P1");
            przebieg1Button.setBounds(580, 333, 115, 50);
            this.add(przebieg1Button);

            przebieg1Button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    auto.setPrzebieg1(0);
                }
            });

            przebieg2Button = new JButton("Reset P2");
            przebieg2Button.setBounds(710, 333, 115, 50);
            this.add(przebieg2Button);

            przebieg2Button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    auto.setPrzebieg2(0);
                }
            });
// przyspieszanie
            this.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    super.keyPressed(e);
                    if(e.getKeyCode() == KeyEvent.VK_UP) {
                        speedingUp = true;
                        if(speed <198)
                        speed += 0.78;
                        else speed = 200;
                    }
                }
                @Override
                public void keyReleased(KeyEvent e) {
                    super.keyPressed(e);
                    if(e.getKeyCode() == KeyEvent.VK_UP) {
                        speedingUp = false;
                        lastTime = System.currentTimeMillis();
                    }
                }
            });
 // hamowanie
            this.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    super.keyPressed(e);
                    if(e.getKeyCode() == KeyEvent.VK_DOWN) {
                        braking = true;
                        if(speed>10)
                            speed -= 5;
                        else speed =0;
                    }
                }
                @Override
                public void keyReleased(KeyEvent e) {
                    super.keyPressed(e);
                    if(e.getKeyCode() == KeyEvent.VK_DOWN) {
                        braking = false;
                        lastTime = System.currentTimeMillis();
                    }
                }
            });
// wlaczenie/wylaczenie auta
            this.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    super.keyPressed(e);
                    if(e.getKeyCode() == KeyEvent.VK_SPACE) {
                        if(auto.czyJestWlaczony()==true && speed ==0) {

                            auto.stop();
                            auto.setSrednieZuzyciePaliwa(0);
                        }
                        else
                            auto.start();
                    }
                }
                @Override
                public void keyReleased(KeyEvent e) {
                    super.keyPressed(e);
                    if(e.getKeyCode() == KeyEvent.VK_SPACE) {
                        lastTime = System.currentTimeMillis();
                    }
                }
            });


            // skret w lewo
            this.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    super.keyPressed(e);
                    if(e.getKeyCode() == KeyEvent.VK_LEFT) {
                      auto.getSwiatla().wlaczLewyKierunek();

                    }
                }
                @Override
                public void keyReleased(KeyEvent e) {
                    super.keyPressed(e);
                    if(e.getKeyCode() == KeyEvent.VK_LEFT) {
                        auto.getSwiatla().wylaczLewyKierunek();
                    }
                }
            });
            // skret w prawo
            this.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    super.keyPressed(e);
                    if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
                        auto.getSwiatla().wlaczPrawyKierunek();

                    }
                }
                @Override
                public void keyReleased(KeyEvent e) {
                    super.keyPressed(e);
                    if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
                        auto.getSwiatla().wylaczPrawyKierunek();
                    }
                }
            });

            // mijania
            this.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    super.keyPressed(e);
                    if(e.getKeyCode() == KeyEvent.VK_2 ) {
                       if(auto.getSwiatla().isMijania()==false)
                        auto.getSwiatla().wlaczMijania();
                       else
                           auto.getSwiatla().wylaczMijania();

                    }
                }
                @Override
                public void keyReleased(KeyEvent e) {
                    super.keyPressed(e);
                    if(e.getKeyCode() == KeyEvent.VK_2) {

                    }
                }
            });

            // pozycyjne
            this.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    super.keyPressed(e);
                    if(e.getKeyCode() == KeyEvent.VK_1 ) {
                        if(auto.getSwiatla().isPozycyjne()==false)
                            auto.getSwiatla().wlaczPozycyjne();
                       else
                           auto.getSwiatla().wylaczPozycje();

                    }
                }
                @Override
                public void keyReleased(KeyEvent e) {
                    super.keyPressed(e);
                    if(e.getKeyCode() == KeyEvent.VK_1) {

                    }
                }
            });

            // drogowe
            this.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    super.keyPressed(e);
                    if(e.getKeyCode() == KeyEvent.VK_3 ) {
                        if(auto.getSwiatla().isDrogowe()==false)
                            auto.getSwiatla().wlaczDrogowe();
                        else
                            auto.getSwiatla().wylaczDrogowe();

                    }
                }
                @Override
                public void keyReleased(KeyEvent e) {
                    super.keyPressed(e);
                    if(e.getKeyCode() == KeyEvent.VK_3) {

                    }
                }
            });

            // mgielnePrzod
            this.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    super.keyPressed(e);
                    if(e.getKeyCode() == KeyEvent.VK_4 ) {
                        if(auto.getSwiatla().isPrzeciwmgielnePrzod()==false)
                            auto.getSwiatla().wlaczPrzeciwmgielnePrzod();
                        else
                            auto.getSwiatla().wylaczPrzeciwmgielnePrzod();

                    }
                }
                @Override
                public void keyReleased(KeyEvent e) {
                    super.keyPressed(e);
                    if(e.getKeyCode() == KeyEvent.VK_4) {

                    }
                }
            });

            // mgielnePrzod
            this.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    super.keyPressed(e);
                    if(e.getKeyCode() == KeyEvent.VK_5 ) {
                        if(auto.getSwiatla().isPrzeciwmgielneTyl()==false)
                            auto.getSwiatla().wlaczPrzeciwmgielneTyl();
                        else
                            auto.getSwiatla().wylaczPrzeciwmgielneTyl();

                    }
                }
                @Override
                public void keyReleased(KeyEvent e) {
                    super.keyPressed(e);
                    if(e.getKeyCode() == KeyEvent.VK_5) {

                    }
                }
            });

            // tempomat
            this.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    super.keyPressed(e);
                    if(e.getKeyCode() == KeyEvent.VK_T ) {
                     if(!tempomat){
                         tempomat=true;
                     auto.setPredkoscTempomatu(auto.getPredkoscAktualna());}

                     else
                         tempomat=false;

                    }
                }
                @Override
                public void keyReleased(KeyEvent e) {
                    super.keyPressed(e);
                    if(e.getKeyCode() == KeyEvent.VK_T) {

                    }
                }
            });



            this.setFocusable(true);
            this.requestFocusInWindow();

        } catch (IOException e1) {
            e1.printStackTrace();
        }

        przebiegCalkowity = new JLabel("0", JLabel.CENTER);
        przebiegCalkowity.setBounds(655, 120, 105, 66);
        add(przebiegCalkowity);

        przebiegCalkowityNapis = new JLabel("Przebieg C:", JLabel.CENTER);
        przebiegCalkowityNapis.setBounds(580, 120, 105, 66);
        add(przebiegCalkowityNapis);


        srednieSpalanie = new JLabel("0.0", JLabel.CENTER);
        srednieSpalanie.setBounds(655, 160, 105, 66);
        add(srednieSpalanie);

        srednieSpalanieNapis = new JLabel("Sr.spalanie:", JLabel.CENTER);
        srednieSpalanieNapis.setBounds(580, 160, 105, 66);
        add(srednieSpalanieNapis);

        przebieg1 = new JLabel("0.0", JLabel.CENTER);
        przebieg1.setBounds(615, 280, 105, 66);
        add(przebieg1);

        przebieg1Napis = new JLabel("Przebieg1:", JLabel.CENTER);
        przebieg1Napis.setBounds(560, 280, 105, 66);
        add(przebieg1Napis);

        przebieg2 = new JLabel("0.0", JLabel.CENTER);
        przebieg2.setBounds(750, 280, 105, 66);
        add(przebieg2);

        przebieg2Napis = new JLabel("Przebieg2:", JLabel.CENTER);
        przebieg2Napis.setBounds(690, 280, 105, 66);
        add(przebieg2Napis);

        predkoscSrednia= new JLabel("0", JLabel.CENTER);
        predkoscSrednia.setBounds(655, 180, 105, 66);
        add(predkoscSrednia);

        predkoscSredniaNapis= new JLabel("Pręd. SREDN:", JLabel.CENTER);
        predkoscSredniaNapis.setBounds(580, 180, 105, 66);
        add(predkoscSredniaNapis);

        predkoscMaksymalna = new JLabel("0", JLabel.CENTER);
        predkoscMaksymalna.setBounds(655, 200, 105, 66);
        add(predkoscMaksymalna);

        predkoscMaksymalnaNapis = new JLabel("Pręd. MAKS:", JLabel.CENTER);
        predkoscMaksymalnaNapis.setBounds(580, 200, 105, 66);
        add(predkoscMaksymalnaNapis);

        dystans = new JLabel("0", JLabel.CENTER);
        dystans.setBounds(655, 220, 105, 66);
        add(dystans);

        dystansNapis = new JLabel("Dystans:", JLabel.CENTER);
        dystansNapis.setBounds(580, 220, 105, 66);
        add(dystansNapis);

        aktualnyCzasPracyAuta = new JLabel("0.0", JLabel.CENTER);
        aktualnyCzasPracyAuta.setBounds(655, 240, 105, 66);
        add(aktualnyCzasPracyAuta);

        aktualnyCzasPracyAutaNapis = new JLabel("Czas podrozy:", JLabel.CENTER);
        aktualnyCzasPracyAutaNapis.setBounds(580, 240, 105, 66);
        add(aktualnyCzasPracyAutaNapis);


    }

    @Override
    public void paintComponent(Graphics g) {

        updateSpeed();

        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g.drawImage(deska, 0, 0, null);
//gdzie rysowac
        int drawLocationX = 300;
        int drawLocationY = 300;

        AffineTransform backup = g2d.getTransform();

        float angle = 360 * speed / 265;
        double rotationRequired = Math.toRadians (angle + 135);
        double locationX = wskaznikPredkosci1.getWidth(null) / 2;
        double locationY = wskaznikPredkosci1.getHeight(null) / 2;
        AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        g2d.drawImage(op.filter((BufferedImage) wskaznikPredkosci1, null), 210, 153, null);
        srednieSpalanie.setText(String.valueOf(String.format("%.1f", auto.getSrednieZuzyciePaliwa())) + " l/h");
        przebieg1.setText(String.valueOf(String.format("%.1f", auto.getPrzebieg1())) + " km");
        przebieg2.setText(String.valueOf(String.format("%.1f", auto.getPrzebieg2())) + " km");
        przebiegCalkowity.setText(String.valueOf(String.format("%.1f", auto.getPrzebiegCalkowity())) + " km");
        predkoscSrednia.setText(String.valueOf(String.format("%.1f", auto.getPredkoscSrednia())) + " km/h");
        predkoscMaksymalna.setText(String.valueOf(String.format("%.1f", auto.getPredkoscMaksymalna())) + " km/h");
        aktualnyCzasPracyAuta.setText(String.valueOf(String.format("%.1f", auto.getCzasWMinutach())) + " min");
        dystans.setText(String.valueOf(String.format("%.1f", auto.getDystans())) + " km");
        if(auto.getSwiatla().isLewyKierunek()==true)
            g2d.drawImage(lewyMigacz, 240, 430, null);
        if(auto.getSwiatla().isPrawyKierunek()==true)
            g2d.drawImage(prawyMigacz, 703, 430, null);
        if(auto.getSwiatla().isDrogowe()==true)
            g2d.drawImage(drogowe, 350, 435, null);
        if(auto.getSwiatla().isPrzeciwmgielneTyl()==true)
            g2d.drawImage(mgielneTyl, 630, 435, null);
        if(auto.getSwiatla().isPrzeciwmgielnePrzod()==true)
            g2d.drawImage(mgielnePrzod, 580, 435, null);
        if(auto.getSwiatla().isMijania()==true)
            g2d.drawImage(mijania, 300, 435, null);
        if(auto.getSwiatla().isPozycyjne()==true)
            g2d.drawImage(pozycyjne, 450, 435, null);

        repaint();
    }
}
