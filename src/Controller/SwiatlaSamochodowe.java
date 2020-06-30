package Controller;

/**
 * Klasa reprezentująca swiatła samochodowe, zapewnia możliwość właczenia swiateł, wyłaczenia oraz sprawdzenia ich stanu.
 *
 *
 * @version 1.0
 * @author Maciej Ksiezak
 * @author Mateusz Mus
 *
 */
public class SwiatlaSamochodowe implements SwiatlaInerfejs {
    private boolean lewyKierunek;
    private boolean prawyKierunek;
    private boolean pozycyjne;
    private boolean mijania;
    private boolean drogowe;
    private boolean przeciwmgielnePrzod;
    private boolean przeciwmgielneTyl;

    public SwiatlaSamochodowe() {
        lewyKierunek = false;
        prawyKierunek = false;
        pozycyjne = false;
        mijania = false;
        drogowe = false;
        przeciwmgielnePrzod = false;
        przeciwmgielneTyl = false;
    }

    public void wylaczLewyKierunek(){
        lewyKierunek = false;
    }

    public void wlaczLewyKierunek(){
        lewyKierunek = true;
    }

    public void wylaczPrawyKierunek(){
        prawyKierunek = false;
    }

    public void wlaczPrawyKierunek(){
        prawyKierunek = true;
    }

    public void wylaczPozycje(){
        pozycyjne = false;
    }

    public void wlaczPozycyjne(){
        pozycyjne = true;
    }

    public void wylaczMijania(){
        mijania = false;
    }

    public void wlaczMijania(){
        if (pozycyjne)
            mijania = true;
    }

    public void wylaczDrogowe(){
        drogowe = false;
    }

    public void wlaczDrogowe(){
        if (mijania)
            drogowe = true;
    }
    public void wylaczPrzeciwmgielnePrzod(){
        przeciwmgielnePrzod = false;
    }

    public void wlaczPrzeciwmgielnePrzod(){
        przeciwmgielnePrzod = true;
    }

    public void wylaczPrzeciwmgielneTyl(){
        przeciwmgielneTyl = false;
    }

    public void wlaczPrzeciwmgielneTyl(){
        przeciwmgielneTyl = true;
    }
    public boolean isLewyKierunek() {
        return lewyKierunek;
    }

    public boolean isPrawyKierunek() {
        return prawyKierunek;
    }

    public boolean isPozycyjne() {
        return pozycyjne;
    }

    public boolean isMijania() {
        return mijania;
    }

    public boolean isDrogowe() {
        return drogowe;
    }

    public boolean isPrzeciwmgielnePrzod() {
        return przeciwmgielnePrzod;
    }

    public boolean isPrzeciwmgielneTyl() {
        return przeciwmgielneTyl;
    }

    @Override
    public void wylacz() {
        lewyKierunek = false;
        prawyKierunek = false;
        pozycyjne = false;
        mijania = false;
        drogowe = false;
        przeciwmgielnePrzod = false;
        przeciwmgielneTyl = false;

    }

    @Override
    public void wlacz() {
        lewyKierunek = true;
        prawyKierunek = true;
        pozycyjne = true;
        mijania = true;
        drogowe = true;
        przeciwmgielnePrzod = true;
        przeciwmgielneTyl = true;

    }
}
