package Controller;

public class Predkosciomierz
{
    private double maksymalnaPredkosc;
    private double predkosc;
    private double przyspieszenie;
    private boolean tempomat;
    private double predkoscTempomatu;


    public Predkosciomierz() //TODO tutaj updatdowanie?
    {
        this.maksymalnaPredkosc = 200.0;
        this.przyspieszenie = 5.0;
    }

    public void setPredkoscTempomatu(double predkoscTempomatu) {
        this.predkoscTempomatu = predkoscTempomatu;
    }

    public double getPredkoscTempomatu() {
        return predkoscTempomatu;
    }

    public double getMaksymalnaPredkosc() {
        return maksymalnaPredkosc;
    }

    public double getPredkosc() {
        return predkosc;
    }

    public double getPrzyspieszenie() {
        return przyspieszenie;
    }

    public boolean getTempomat() {
        return tempomat;
    }

    public void setMaksymalnaPredkosc(double maksymalnaPredkosc) {
        this.maksymalnaPredkosc = maksymalnaPredkosc;
    }

    public void setPredkosc(double predkosc) {
        this.predkosc = predkosc;
    }

    public void setPrzyspieszenie(double przyspieszenie) {
        this.przyspieszenie = przyspieszenie;
    }

    public void setTempomat(boolean tempomat) {
        this.tempomat = tempomat;
    }
    public boolean statusTempomatu()
    {
        return tempomat;
    }
    public void wlaczTempomat()  //TODO zmiany tutaj?
    {
        if (predkosc >= 60)
        {
            if (tempomat)
            {
                predkoscTempomatu = predkosc;
            }
        }
    }
    //TODO update() ??

//    @Override
//    public String toString() //TODO to zmienic trzeba strukture
//    {
//        return "-- PRĘDKOŚCIOMIERZ --" + System.lineSeparator() +
//                "Prędkość: " + (int) predkosc + " km/h" + System.lineSeparator() +
//                "Tempomat: " + (tempomat ? "aktywny (" + (int) predkoscTempomatu + " km/h)" : "nieaktywny");
//    }
}
