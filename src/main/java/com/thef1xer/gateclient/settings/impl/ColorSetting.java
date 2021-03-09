package com.thef1xer.gateclient.settings.impl;

import com.thef1xer.gateclient.settings.Setting;

public class ColorSetting{
    public static class RGB extends Setting {
        private int red;
        private int green;
        private int blue;

        public RGB(String name, String id, int red, int green, int blue) {
            super(name, id);
            this.red = red;
            this.green = green;
            this.blue = blue;
        }

        public int getRed() {
            return red;
        }

        public void setRed(int red) {
            this.red = red;
        }

        public int getGreen() {
            return green;
        }

        public void setGreen(int green) {
            this.green = green;
        }

        public int getBlue() {
            return blue;
        }

        public void setBlue(int blue) {
            this.blue = blue;
        }
    }

    public static class RGBA extends RGB {
        private float alpha;

        public RGBA(String name, String id, int red, int green, int blue, float alpha) {
            super(name, id, red, green, blue);
            this.alpha = alpha;
        }

        public float getAlpha() {
            return alpha;
        }

        public void setAlpha(float alpha) {
            this.alpha = alpha;
        }
    }
}
