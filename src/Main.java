import Controller.Auto;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.security.Key;

import javax.swing.JFrame;

public class Main {

    public static void main(String[] args) throws InterruptedException {
       // new KeyListenerExample();

        Auto samochodzik = new Auto();
        samochodzik.start();
        samochodzik.setPedkoscAktualna(150);
        samochodzik.setBenzyna(50);
        for ( int i =0; i<3600;i++)
        {
            samochodzik.uaktualnij();
        }
        samochodzik.stop();
        System.out.println(samochodzik.getPrzebiegCalkowity());
        System.out.println(samochodzik);
        System.out.println(samochodzik.getSwiatla().toString());

        System.out.println("Hello World!");

    }
}
