package com.thef1xer.gateclient.gui.clickgui.components;

import com.thef1xer.gateclient.GateClient;
import com.thef1xer.gateclient.gui.clickgui.ClickComponent;
import com.thef1xer.gateclient.managers.PresetManager;
import com.thef1xer.gateclient.util.DirectoryUtil;
import com.thef1xer.gateclient.util.RenderUtil;

import java.io.File;

public class PresetComponent extends ClickComponent {
    private final float fontX = (this.width - fontRenderer.getStringWidth("Presets")) / 2F;
    private final PresetManager presetManager = GateClient.getGate().presetManager;
    private boolean presetListExpanded = false;
    private File hoveredPreset;

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

        //Auto-Save button
        if (presetManager.isAutoSave()) {
            RenderUtil.draw2DRect(posX + border, posY + height + 2 * border + 12, posX + border + 8, posY + height + 2 * border + 20, 0.85F, 0.43F, 0F, 1F);
        }
        RenderUtil.draw2DRectLines(posX + border, posY + height + 2 * border + 12, posX + border + 8, posY + height + 2 * border + 20, 0.8F, 0.8F, 0.8F, 0.8F);
        fontRenderer.drawString("Auto-Save", posX + 2 * border + 8, posY + height + 2 * border + 12, 0xFFFFFFFF, true);

        //Load button
        //11 was calculated using ((width - 3 * border) / 2F - fontRenderer.getStringWidth("Load")) / 2 and rounding
        if (mouseX > posX + border && mouseX < posX + (width - border) / 2F
                && mouseY > posY + height + 3 * border + 20 && mouseY < posY + height + 3 * border + 32) {
            RenderUtil.draw2DRect(posX + border, posY + height + 3 * border + 20, posX + (width - border) / 2F, posY + height + 3 * border + 32, 0.2F, 0.2F, 0.2F, 1F);
        }

        RenderUtil.draw2DRectLines(posX + border, posY + height + 3 * border + 20, posX + (width - border) / 2F, posY + height + 3 * border + 32, 0.85F, 0.43F, 0F, 1F);
        fontRenderer.drawString("Load", posX + border + 11, posY + height + 3 * border + 22, 0xFFFFFFFF, true);

        //Save button
        //35 was calculated using ((width - 3 * border) / 2 + fontRenderer.getStringWidth("Save")) / 2 and rounding
        if (mouseX > posX + (width + border) / 2F && mouseX < posX + width - border
                && mouseY > posY + height + 3 * border + 20 && mouseY < posY + height + 3 * border + 32) {
            RenderUtil.draw2DRect(posX + (width + border) / 2F, posY + height + 3 * border + 20, posX + width - border, posY + height + 3 * border + 32, 0.2F, 0.2F, 0.2F, 1F);
        }

        RenderUtil.draw2DRectLines(posX + (width + border) / 2F, posY + height + 3 * border + 20, posX + width - border, posY + height + 3 * border + 32, 0.85F, 0.43F, 0F, 1F);
        fontRenderer.drawString("Save", posX + width - border - 35, posY + height + 3 * border + 22, 0xFFFFFFFF, true);

        //Create
        if (mouseX > posX + border && mouseX < posX + width - border
                && mouseY > posY + height + 4 * border + 32 && mouseY < posY + height + 4 * border + 44) {
            RenderUtil.draw2DRect(posX + border, posY + height + 4 * border + 32, posX + width - border, posY + height + 4 * border + 44, 0.2F, 0.2F, 0.2F, 1F);
        }

        RenderUtil.draw2DRectLines(posX + border, posY + height + 4 * border + 32, posX + width - border, posY + height + 4 * border + 44, 0.85F, 0.43F, 0F, 1F);
        fontRenderer.drawString("Create", posX + (width - fontRenderer.getStringWidth("Create")) / 2, posY + height + 4 * border + 34, 0xFFFFFFFF, true);

        //Preset List (This must go last!)
        RenderUtil.draw2DRect(posX + border, posY + height + border, posX + width - border, posY + height + border + 12, 0.3F, 0.3F, 0.3F, 1F);
        fontRenderer.drawString(DirectoryUtil.removeExtension(presetManager.getActivePreset().getName()), posX + border + 2, posY + height + border + 2, 0xFFFFFFFF, true);

        if (this.presetListExpanded) {
            int offset = 12;
            this.hoveredPreset = null;
            for (File preset : presetManager.PRESET_LIST) {

                if (preset.equals(presetManager.getActivePreset())) continue;

                //Mouse hovering a preset
                if (mouseX > posX + border && mouseX < posX + width - border
                        && mouseY > posY + height + border + offset && mouseY < posY + height + border + 12 + offset) {
                    RenderUtil.draw2DRect(posX + border, posY + height + border + offset, posX + width - border, posY + height + border + 12 + offset, 0.4F, 0.4F, 0.4F, 1F);
                    this.hoveredPreset = preset;
                } else {
                    RenderUtil.draw2DRect(posX + border, posY + height + border + offset, posX + width - border, posY + height + border + 12 + offset, 0.3F, 0.3F, 0.3F, 1F);
                }

                fontRenderer.drawString(DirectoryUtil.removeExtension(preset.getName()), posX + border + 2, posY + height + border + 2 + offset, 0xFFFFFFFF, true);
                offset += 12;
            }
            RenderUtil.draw2DTriangleRight(posX + width - 2 * border - 6, posY + height + border + 2, posX + width - 2 * border - 2, posY + height + border + 10, 1F, 1F, 1F, 1F);
        } else {
            RenderUtil.draw2DTriangleDown(posX + width - 2 * border - 8, posY + height + border + 4, posX + width - 2 * border, posY + height + border + 8, 1F, 1F, 1F, 1F);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (this.isMouseHover(mouseX, mouseY)) {
            //Full expand
            this.expanded = !expanded;

        } else if (this.expanded) {

            //Preset selection
            if (this.presetListExpanded) {
                if (this.hoveredPreset != null) {
                    this.presetManager.setActivePreset(this.hoveredPreset);
                    this.presetManager.loadActivePreset();
                    this.presetListExpanded = false;
                    return;
                }

            }

            if (mouseX > posX && mouseX < posX + width
                    && mouseY > posY + height + 2 * border + 12 && mouseY < posY + height + 2 * border + 20) {
                //Auto-Save toggle
                presetManager.setAutoSave(!presetManager.isAutoSave());

            } else if (mouseX > posX + border && mouseX < posX + (width - border) / 2F
                    && mouseY > posY + height + 3 * border + 20 && mouseY < posY + height + 3 * border + 32) {
                //Load button
                presetManager.loadActivePreset();

            } else if (mouseX > posX + (width + border) / 2F && mouseX < posX + width - border
                    && mouseY > posY + height + 3 * border + 20 && mouseY < posY + height + 3 * border + 32) {
                //Save button
                presetManager.saveActivePreset();

            } else if (mouseX > posX + border && mouseX < posX + width - border
                    && mouseY > posY + height + 4 * border + 32 && mouseY < posY + height + 4 * border + 44) {
                //Create button

            } else if (mouseX > posX + border && mouseX < posX + width - border
                    && mouseY > posY + height + border && mouseY < posY + height + border + 12) {
                //Preset List
                presetManager.updatePresetList();
                this.presetListExpanded = !presetListExpanded;
            }
        }
    }
}
