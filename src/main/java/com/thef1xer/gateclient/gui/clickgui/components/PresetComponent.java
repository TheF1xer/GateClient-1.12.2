package com.thef1xer.gateclient.gui.clickgui.components;

import com.thef1xer.gateclient.GateClient;
import com.thef1xer.gateclient.gui.clickgui.ClickComponent;
import com.thef1xer.gateclient.managers.PresetManager;
import com.thef1xer.gateclient.util.DirectoryUtil;
import com.thef1xer.gateclient.util.RenderUtil;

public class PresetComponent extends ClickComponent {
    private final float fontX = (this.width - fontRenderer.getStringWidth("Presets")) / 2F;
    private final PresetManager presetManager = GateClient.getGate().presetManager;

    public PresetComponent(float posX, float posY) {
        super(posX, posY);
        this.border = 5;
        this.height = 19;
        this.expanded = true;
    }

    @Override
    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        //Header
        RenderUtil.draw2DRect(this.posX, this.posY, this.posX + width, this.posY + height, 0.18F, 0.38F, 0.9F, 1F);
        fontRenderer.drawString("Presets", this.posX + this.fontX, this.posY + this.border, 0xFFFFFFFF, true);

        if(!this.expanded) return;

        //Background
        RenderUtil.draw2DRect(posX, posY + height, posX + width, posY + height + 5 * border + 44, 0.1F, 0.1F, 0.1F, 1F);

        //Preset List
        RenderUtil.draw2DRect(posX + border, posY + height + border, posX + width - border, posY + height + border + 12, 0.3F, 0.3F, 0.3F, 1F);
        fontRenderer.drawString(DirectoryUtil.removeExtension(presetManager.getActivePreset().getName()), posX + border + 2, posY + height + border + 2, 0xFFFFFFFF, true);
        RenderUtil.draw2DTriangleDown(posX + width - 2 * border - 8, posY + height + border + 4 , posX + width - 2 * border, posY + height + border + 8, 1F, 1F, 1F, 1F);

        //Auto-Save button
        if (GateClient.getGate().presetManager.isAutoSave()) {
            RenderUtil.draw2DRect(posX + border, posY + height + 2 * border + 12, posX + border + 8, posY + height + 2 * border + 20, 0.85F, 0.43F, 0F, 1F);
        }
        RenderUtil.draw2DRectLines(posX + border, posY + height + 2 * border + 12, posX + border + 8, posY + height + 2 * border + 20, 0.8F, 0.8F, 0.8F, 0.8F);
        fontRenderer.drawString("Auto-Save", posX + 2 * border + 8, posY + height + 2 * border + 12, 0xFFFFFFFF, true);

        //Load button
        //11 was calculated using ((width - 3 * border) / 2F - fontRenderer.getStringWidth("Load")) / 2 and rounding
        fontRenderer.drawString("Load", posX + border + 11, posY + height + 3 * border + 22, 0xFFFFFFFF, true);
        RenderUtil.draw2DRectLines(posX + border, posY + height + 3 * border + 20, posX + (width - border) / 2F, posY + height + 3 * border + 32, 0.85F, 0.43F, 0F, 1F);

        //Save button
        //35 was calculated using ((width - 3 * border) / 2 + fontRenderer.getStringWidth("Save")) / 2 and rounding
        fontRenderer.drawString("Save", posX + width - border - 35, posY + height + 3 * border + 22, 0xFFFFFFFF, true);
        RenderUtil.draw2DRectLines(posX + (width + border) / 2F, posY + height + 3 * border + 20, posX + width - border, posY + height + 3 * border + 32, 0.85F, 0.43F, 0F, 1F);

        //Create
        fontRenderer.drawString("Create", posX + (width - fontRenderer.getStringWidth("Create")) / 2, posY + height + 4 * border + 34, 0xFFFFFFFF, true);
        RenderUtil.draw2DRectLines(posX + border, posY + height + 4 * border + 32, posX + width - border, posY + height + 4 * border + 44, 0.85F, 0.43F, 0F, 1F);
    }
}
