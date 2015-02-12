package net.kaoriya.sparkey_endurance;

import java.io.File;
import com.spotify.sparkey.Sparkey;
import com.spotify.sparkey.SparkeyReader;
import com.spotify.sparkey.SparkeyWriter;
import java.util.Random;

public class EnduranceTest {

    private File dataDir = new File("./var/sparkey-data");

    private int setCount = 100;
    private int setSize = 100000;
    private int keyLen = 128;
    private int valueLen = 128;

    private int keySeed = -1;
    private byte[][] keyArray = null;

    private int valueSeed = -1;
    private int valueIndex = -1;
    private Random valueRandom = null;


    public EnduranceTest() {
    }

    public void run(String[] args) throws Exception {
        if (!isPrepared()) {
            System.out.println("preparing...");
            prepare();
            System.out.println("completed prepare.  try run again to test.");
        } else {
            System.out.println("start test");
            test();
        }
    }

    private boolean isPrepared() {
        for (int i = 0; i < setCount; ++i) {
            for (File f : getFiles(i)) {
                if (!f.exists()) {
                    return false;
                }
            }
        }
        return true;
    }

    private void prepare() throws Exception {
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
        // Remove existing files.
        for (int i = 0; i < setCount; ++i) {
            File[] files = getFiles(i);
            for (File f : files) {
                if (f.exists()) {
                    f.delete();
                }
            }
        }
        // Create sparkey data files.
        for (int i = 0; i < setCount; ++i) {
            createSet(i);
        }
    }

    private void test() {
        // TODO:
        System.out.println("Test");
    }

    private File getBaseFile(int n) {
        String name = "set-" + Integer.toString(n);
        return new File(dataDir, name);
    }

    private File[] getFiles(int n) {
        File base = getBaseFile(n);
        return new File[]{
            Sparkey.getIndexFile(base),
            Sparkey.getLogFile(base),
        };
    }

    private void createSet(int n) throws Exception {
        SparkeyWriter w = Sparkey.createNew(getBaseFile(n));
        try {
            writeData(w, n);
            w.writeHash();
        } finally {
            w.close();
        }
    }

    private void writeData(SparkeyWriter w, int n) throws Exception {
        for (int i = 0; i < setSize; ++i) {
            byte[] key = getKey(0, i);
            byte[] value = getValue(n + 1, i);
            w.put(key, value);
        }
    }

    private byte[] getKey(int seed, int n) {
        if (keyArray == null || keySeed != seed) {
            keyArray = new byte[setSize][];
            Random r = new Random(seed);
            for (int i = 0; i < setSize; ++i) {
                keyArray[i] = Utils.randomBytes(r, keyLen);
            }
            keySeed = seed;
        }
        return keyArray[n];
    }

    private byte[] getValue(int seed, int n) {
        if (valueRandom == null || valueSeed != seed || valueIndex != n) {
            valueRandom = new Random(seed);
            valueSeed = seed;
            valueIndex = 0;
            while (valueIndex < n) {
                Utils.randomBytes(valueRandom, valueLen);
                ++valueIndex;
            }
        }
        byte[] b = Utils.randomBytes(valueRandom, valueLen);
        ++valueIndex;
        return b;
    }
}
