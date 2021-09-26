package com.thef1xer.gateclient.gui;

import com.thef1xer.gateclient.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public class TextField {
    private final float posX;
    private final float posY;
    private final int width;
    private String text;
    private int tickCounter = 0;

    private final FontRenderer fr = Minecraft.getMinecraft().fontRenderer;

    public TextField(float posX, float posY, int width, String message) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.text = message;
    }

    public TextField(float posX, float posY, int width) {
        this(posX, posY, width, "");
    }

    public void drawField() {
        String trimmedMessage = fr.trimStringToWidth(text, width, true);
        int stringEnd = fr.drawString(trimmedMessage, posX, posY, 0xFFFFFFFF, true);

        if (tickCounter <= 15) {
            RenderUtil.draw2DRect(stringEnd, posY, stringEnd + 1, posY + 8, 1F, 1F, 1F, 1F);
        }

        updateTickCounter();
    }

    public void keyTyped(char typedChar, int keyCode) {
        System.out.println(typedChar);
        writeChar(typedChar);
    }

    public void writeChar(char c) {
        text = text.concat(String.valueOf(c));
    }

    private void updateTickCounter() {
        if (tickCounter == 30) {
            tickCounter = 0;
        } else {
            tickCounter++;
        }
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
