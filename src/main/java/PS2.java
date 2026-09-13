public class PS2 extends PS3{
    int a;

    public PS2(int a) {
        super(a); //access the properties of parent class
        this.a = a;

    }

    public int increment() {
        a = a + 1;
        return a;
    }

    public int decrement() {
        a = a - 1;
        return a;
    }
}
