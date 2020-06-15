import java.awt.*;
import java.awt.event.*;
// tutaj sprawdzalem wczesniej eventy z przyciskaniem klawiszy
public class KeyListenerExample extends Frame implements KeyListener{
    Label l;
    TextArea area;
    KeyListenerExample(){

        l=new Label();
        l.setBounds(20,50,100,20);
        area=new TextArea();
        area.setBounds(20,80,300, 300);
        area.addKeyListener(this);

        add(l);add(area);
        setSize(400,400);
        setLayout(null);
        setVisible(true);
    }
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch( keyCode ) {
            case KeyEvent.VK_UP:
               // l.setText("Przyspieszasz");
                System.out.println("Przyspieszasz");
                break;
            case KeyEvent.VK_DOWN:
             //   l.setText("Zwalniasz");
                System.out.println("Zwalniasz");
                break;
            case KeyEvent.VK_LEFT:
               // l.setText("Skrecasz w lewo");
                System.out.println("Skrecasz w lewo");
                break;
            case KeyEvent.VK_RIGHT :
               // l.setText("Skrecasz prawo");
                System.out.println("Skrecasz prawo");
                break;
        }
    }
    public void keyReleased(KeyEvent e) {
      //  l.setText("Key Released");
    }
    public void keyTyped(KeyEvent e) {
       // l.setText("Key Typed");
    }

    public static void main(String[] args) {
        new KeyListenerExample();
    }
}  