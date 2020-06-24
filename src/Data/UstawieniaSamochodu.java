package Data;



import Controller.Auto;


public class UstawieniaSamochodu {


    private float przebiegCalkowity;
    private float przebieg1;
    private float przebieg2;
    private float benzyna;

    public UstawieniaSamochodu(Auto auto) {
        this.przebiegCalkowity = auto.getPrzebiegCalkowity();
        this.przebieg1 = auto.getPrzebieg1();
        this.przebieg2 = auto.getPrzebieg2();
        this.benzyna = auto.getBenzyna();
    }


    public UstawieniaSamochodu() {
        this.przebiegCalkowity = 0;
        this.przebieg1 = 0;
        this.przebieg2 = 0;
        this.benzyna = 0;
    }


    public float getPrzebiegCalkowity() {
        return przebiegCalkowity;
    }


    public float getPrzebieg1() {
        return przebieg1;
    }


    public float getPrzebieg2() {
        return przebieg2;
    }


    public float getBenzyna() {
        return benzyna;
    }

}
