package com.thef1xer.gateclient.util;

import net.minecraft.client.Minecraft;

import java.io.File;

public class DirectoryUtil {
    public static final File GATE_FOLDER = new File(Minecraft.getMinecraft().mcDataDir, "Gate Client");
    public static final File PRESET_FOLDER = new File(GATE_FOLDER, "Presets");

    public static boolean isJson(File file) {
        String name = file.getName();
        int index = name.lastIndexOf(".");
        return name.substring(index).equals(".json");
    }

    public static String removeExtension(String stringIn) {
        StringBuilder newString = new StringBuilder();
        for (char n : stringIn.toCharArray()) {
            if (n == '.') {
                break;
            }
            newString.append(n);
        }
        return newString.toString();
    }
}
