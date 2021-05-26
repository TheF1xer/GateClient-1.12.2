package com.thef1xer.gateclient.gui.clickgui;

import com.thef1xer.gateclient.GateClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import java.util.List;

public class ClickComponent {
    public float posX;
    public float posY;
    public int border = 3;
    public float width = 106;
    public float height = 15;

    public ClickComponent(float posX, float posY) {
        this.posX = posX;
        this.posY = posY;
    }

    protected FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;

    public void drawComponent(int mouseX, int mouseY, float partialTicks) {

    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

    }

    public void mouseReleased(int mouseX, int mouseY, int state) {

    }

    protected boolean isMouseHover(int mouseX, int mouseY) {
        return mouseX > this.posX && mouseX < this.posX + this.width &&
                mouseY > this.posY && mouseY < this.posY + this.height;
    }

    public float updatedByParent(float offsetY) {
        this.posY = offsetY;
        return offsetY + this.height;
    }

    public float updateChildren(List<? extends ClickComponent> children) {
        float offsetY = this.posY + this.height;
        for (ClickComponent component : children) {
            offsetY = component.updatedByParent(offsetY);
        }
        return offsetY;
    }

    protected void updateHierarchy() {
        GateClient.gate.guiManager.CLICK_GUI.onUpdate();
    }
}
