package me.thef1xer.gateclient.util;

public class ColorUtil {
    public static int RGBtoHex(int red, int green, int blue) {
        return (red << 16) | (green << 8) | (blue);
    }

    public static int[] getRainbow(int cycle, float offset) {
        int r = 0, g = 0, b = 0;
        long i = (System.currentTimeMillis() - Math.round(offset * 1000)) % (cycle * 1000);
        float j = (float) 6 * i / (cycle * 1000);
        float k = j - (float) Math.floor(j);
        int l = Math.round(255 * k);

        if (j < 1) {
            r = 255;
            g = l;
        } else if (j < 2) {
            r = 255 - l;
            g = 255;
        } else if (j < 3) {
            g = 255;
            b = l;
        } else if (j < 4) {
            g = 255 - l;
            b = 255;
        } else if (j < 5) {
            r = l;
            b = 255;
        } else if (j < 6) {
            r = 255;
            b = 255 - l;
        }
        return new int[] {r, g, b};
    }
}
