
import java.io.Serializable;
public class Przebieg implements Serializable
{
    private double dystans;

    public double getDystans() {
        return dystans;
    }
    public void set(double dystans)
    {
        if (dystans >=0)
        this.dystans = dystans;
    }
    public void add(double dystans)
    {
        if (dystans >= 0)
        this.dystans += dystans;
    }

    @Override
    public String toString()
    {
        return String.valueOf(dystans);
    }
    public Przebieg()
    {
        this.dystans = 0.0;
    }
    public Przebieg(double dystans)
    {
        if (dystans >=0)
            this.dystans = dystans;
    }
}
