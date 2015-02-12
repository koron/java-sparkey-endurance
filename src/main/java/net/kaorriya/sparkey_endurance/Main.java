package net.kaoriya.sparkey_endurance;

public class Main {
    public static void main(String[] args) {
        EnduranceTest t = new EnduranceTest();
        try {
            t.run(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
