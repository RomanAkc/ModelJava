package fss.model;
import java.util.Random;

public class RandomWrapper {
    public static long initInstanceNum = 0;
    private static Random random;
    private static Random getInstance() {
        if(random == null) {
            if(initInstanceNum == 0) {
                initInstanceNum = System.currentTimeMillis();
            }

            random = new Random(initInstanceNum);
        }

        return random;
    }

    public static int getRandom(int max) {
        return getInstance().nextInt(max + 1);
    }

    public static int getRandom(int min, int max) {
        return min + getInstance().nextInt(max - min + 1);
    }
}
