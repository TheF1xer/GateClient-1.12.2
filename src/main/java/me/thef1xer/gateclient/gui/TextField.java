package me.thef1xer.gateclient.gui;

import me.thef1xer.gateclient.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ChatAllowedCharacters;

public class TextField {
    private final float posX;
    private final float posY;
    private final int width;
    private String text;
    private boolean focused;
    private int tickCounter = 0;

    private final FontRenderer fr = Minecraft.getMinecraft().fontRenderer;

    public TextField(float posX, float posY, int width, String text) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.text = text;
        this.focused = false;
    }

    public TextField(float posX, float posY, int width) {
        this(posX, posY, width, "");
    }

    public void drawField() {
        String trimmedMessage = fr.trimStringToWidth(text, width, true);
        int stringEnd = fr.drawString(trimmedMessage, posX, posY, 0xFFFFFFFF, true);

        //Cursor draw
        if (focused && tickCounter <= 15) {
            RenderUtil.draw2DRect(stringEnd, posY, 1, 8, 1F, 1F, 1F, 1F);
        }

        updateTickCounter();
    }

    public void keyTyped(char typedChar, int keyCode) {
        System.out.println(typedChar + " " + keyCode);
        if (keyCode == 14) {
            //Backspace key
            if (!text.isEmpty()) {
                text = text.substring(0, text.length() - 1);
            }
        } else {
            writeChar(typedChar);
        }
    }

    public void writeChar(char c) {
        if (ChatAllowedCharacters.isAllowedCharacter(c)) {
            text = text.concat(String.valueOf(c));
        }
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

    public boolean isFocused() {
        return focused;
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
    }
}
