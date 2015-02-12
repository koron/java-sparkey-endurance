package net.kaoriya.sparkey_endurance;

import java.util.Random;

public class Utils {
    public static byte[] randomBytes(Random r, int len) {
        byte[] b = new byte[len];
        r.nextBytes(b);
        return b;
    }
}
