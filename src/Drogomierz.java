import java.io.Serializable;
public class Drogomierz implements Serializable
{

    private final Przebieg[] dziennePrzebiegi;
    private double calkowityPrzebieg;
    public Drogomierz()
    {
        this.dziennePrzebiegi = new Przebieg[5];   //TODO tutaj Przebieg[2]; ??
        for (int i = 0; i < this.dziennePrzebiegi.length; i++)
        {
            this.dziennePrzebiegi[i] = new Przebieg();
        }
    }
     // TODO tutaj return dziennePrzebiegi.clone() ?
    public Przebieg[] getDziennePrzebiegi()
    {
        return dziennePrzebiegi;
    }
    public double getCalkowityPrzebieg()
    {
        return calkowityPrzebieg;
    }
    public void addDystans(double dystans)
    {
        for (int i = 0; i < dziennePrzebiegi.length; i++)
        {
            dziennePrzebiegi[i].add(dystans);
        }
        calkowityPrzebieg += dystans;
    }

    //TODO @override toString()

}
