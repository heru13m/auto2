package Data;



import Controller.Auto;


public class UstawieniaSamochodu {


    private float przebiegCalkowity;
    private float przebieg1;
    private float przebieg2;

    public UstawieniaSamochodu(Auto auto) {
        this.przebiegCalkowity = auto.getPrzebiegCalkowity();
        this.przebieg1 = auto.getPrzebieg1();
        this.przebieg2 = auto.getPrzebieg2();
    }


    public UstawieniaSamochodu() {
        this.przebiegCalkowity = 0;
        this.przebieg1 = 0;
        this.przebieg2 = 0;
    }

    public UstawieniaSamochodu(float przebieg, float przebieg1, float przebieg2) {
        this.przebiegCalkowity = przebieg;
        this.przebieg1 = przebieg1;
        this.przebieg2 = przebieg2;
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



}
