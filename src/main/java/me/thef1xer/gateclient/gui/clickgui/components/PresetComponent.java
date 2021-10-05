package me.thef1xer.gateclient.gui.clickgui.components;

import me.thef1xer.gateclient.GateClient;
import me.thef1xer.gateclient.gui.TextField;
import me.thef1xer.gateclient.gui.clickgui.ClickComponent;
import me.thef1xer.gateclient.managers.PresetManager;
import me.thef1xer.gateclient.util.DirectoryUtil;
import me.thef1xer.gateclient.util.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;

import java.io.File;

public class PresetComponent extends ClickComponent {

    private final float fontX = (this.width - fontRenderer.getStringWidth("Presets")) / 2F;
    private final PresetManager presetManager = GateClient.getGate().presetManager;

    private File hoveredPreset;
    private final TextField textField;
    private boolean presetListExpanded = false;
    private boolean createButtonExpanded = false;

    public PresetComponent(float posX, float posY) {
        super(posX, posY);
        this.padding = 5;
        this.height = 19;
        this.expanded = true;
        this.textField = new TextField(posX + padding + 4, posY + height + 7 * padding + 58, width - 2 * padding - 5);
    }

    @Override
    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        //Header
        RenderUtil.draw2DRect(posX, posY, width, height, 0.18F, 0.38F, 0.9F, 1F);
        fontRenderer.drawString("Presets", posX + fontX, posY + padding, 0xFFFFFFFF, true);

        if(!expanded) return;

        //Background
        if (createButtonExpanded) {
            RenderUtil.draw2DRect(posX, posY + height, width, 9 * padding + 80, 0.1F, 0.1F, 0.1F, 1F);
        } else {
            RenderUtil.draw2DRect(posX, posY + height, width, 7 * padding + 56, 0.1F, 0.1F, 0.1F, 1F);
        }

        renderAutoSave();

        GlStateManager.glLineWidth(2);

        renderLoadButton(mouseX, mouseY);

        renderSaveButton(mouseX, mouseY);

        renderRemoveButton(mouseX, mouseY);

        renderCreate(mouseX, mouseY);

        //This must go last!
        renderPresetList(mouseX, mouseY);

        GlStateManager.glLineWidth(1);
    }

    private void renderAutoSave() {
        if (presetManager.isAutoSave()) {
            RenderUtil.draw2DRect(posX + padding, posY + height + 2 * padding + 12, 8, 8, 0.85F, 0.43F, 0F, 1F);
        }
        RenderUtil.draw2DRectLines(posX + padding, posY + height + 2 * padding + 12, 8, 8, 0.8F, 0.8F, 0.8F, 0.8F);
        fontRenderer.drawString("Auto-Save", posX + 2 * padding + 8, posY + height + 2 * padding + 12, 0xFFFFFFFF, true);
    }

    private void renderLoadButton(int mouseX, int mouseY) {
        //11 was calculated using ((width - 3 * border) / 2F - fontRenderer.getStringWidth("Load")) / 2 and rounding

        if (isHovering(mouseX, mouseY, posX + padding, posY + height + 3 * padding + 20, posX + (width - padding) / 2F, posY + height + 3 * padding + 32)) {
            RenderUtil.draw2DRect(posX + padding, posY + height + 3 * padding + 20, (width - 3 * padding) / 2F, 12, 0.2F, 0.2F, 0.2F, 1F);
        }

        RenderUtil.draw2DRectLines(posX + padding, posY + height + 3 * padding + 20, (width - 3 * padding) / 2F, 12, 0.85F, 0.43F, 0F, 1F);
        fontRenderer.drawString("Load", posX + padding + 11, posY + height + 3 * padding + 22, 0xFFFFFFFF, true);
    }

    private void renderSaveButton(int mouseX, int mouseY) {
        //35 was calculated using ((width - 3 * border) / 2 + fontRenderer.getStringWidth("Save")) / 2 and rounding

        if (isHovering(mouseX, mouseY, posX + (width + padding) / 2F, posY + height + 3 * padding + 20,
                posX + width - padding, posY + height + 3 * padding + 32)) {
            RenderUtil.draw2DRect(posX + (width + padding) / 2F, posY + height + 3 * padding + 20, (width - 3 * padding) / 2F, 12, 0.2F, 0.2F, 0.2F, 1F);
        }

        RenderUtil.draw2DRectLines(posX + (width + padding) / 2F, posY + height + 3 * padding + 20, (width - 3 * padding) / 2F, 12, 0.85F, 0.43F, 0F, 1F);
        fontRenderer.drawString("Save", posX + width - padding - 35, posY + height + 3 * padding + 22, 0xFFFFFFFF, true);
    }

    private void renderRemoveButton(int mouseX, int mouseY) {
        if (isHovering(mouseX, mouseY, posX + padding, posY + height + 4 * padding + 32,
                posX + width - padding, posY + height + 4 * padding + 44)) {
            RenderUtil.draw2DRect(posX + padding, posY + height + 4 * padding + 32, width - 2 * padding, 12, 0.2F, 0.2F, 0.2F, 1F);
        }

        RenderUtil.draw2DRectLines(posX + padding, posY + height + 4 * padding + 32, width - 2 * padding, 12, 0.85F, 0.43F, 0F, 1F);

        fontRenderer.drawString("Remove", posX + (width - fontRenderer.getStringWidth("Remove")) / 2F, posY + height + 4 * padding + 34, 0xFFFFFFFF, true);
    }

    private void renderCreate(int mouseX, int mouseY) {
        if (isHovering(mouseX, mouseY, posX + padding, posY + height + 6 * padding + 44,
                posX + width - padding, posY + height + 6 * padding + 56)) {
            RenderUtil.draw2DRect(posX + padding, posY + height + 6 * padding + 44, width - 2 * padding, 12, 0.2F, 0.2F, 0.2F, 1F);
        }

        RenderUtil.draw2DRectLines(posX + padding, posY + height + 6 * padding + 44, width - 2 * padding, 12, 0.85F, 0.43F, 0F, 1F);

        if (createButtonExpanded) {
            fontRenderer.drawString("Cancel", posX + (width - fontRenderer.getStringWidth("Cancel")) / 2F, posY + height + 6 * padding + 46, 0xFFFFFFFF, true);

            //Text Field
            RenderUtil.draw2DRectLines(posX + padding, posY + height + 7 * padding + 56, width - 2 * padding, 12, 0.8F, 0.8F, 0.8F, 1F);
            textField.drawField();

            //Confirm button
            if (isHovering(mouseX, mouseY, posX + padding, posY + height + 8 * padding + 68,
                    posX + width - padding, posY + height + 8 * padding + 80)) {
                RenderUtil.draw2DRect(posX + padding, posY + height + 8 * padding + 68, width - 2 * padding, 12, 0.2F, 0.2F, 0.2F, 1F);
            }

            RenderUtil.draw2DRectLines(posX + padding, posY + height + 8 * padding + 68, width - 2 * padding, 12, 0.85F, 0.43F, 0F, 1F);
            fontRenderer.drawString("Confirm", posX + (width - fontRenderer.getStringWidth("Confirm")) / 2F, posY + height + 8 * padding + 70, 0xFFFFFFFF, true);

        } else {
            fontRenderer.drawString("Create", posX + (width - fontRenderer.getStringWidth("Create")) / 2F, posY + height + 6 * padding + 46, 0xFFFFFFFF, true);
        }
    }

    private void renderPresetList(int mouseX, int mouseY) {
        RenderUtil.draw2DRect(posX + padding, posY + height + padding, width - 2 * padding, 12, 0.3F, 0.3F, 0.3F, 1F);
        fontRenderer.drawString(DirectoryUtil.removeExtension(presetManager.getActivePreset().getName()), posX + padding + 2, posY + height + padding + 2, 0xFFFFFFFF, true);

        if (this.presetListExpanded) {
            int offset = 12;
            this.hoveredPreset = null;

            for (File preset : presetManager.PRESET_LIST) {
                if (preset.equals(presetManager.getActivePreset())) continue;

                //Mouse hovering a preset
                if (isHovering(mouseX, mouseY, posX + padding, posY + height + padding + offset,
                        posX + width - padding, posY + height + padding + 12 + offset)) {

                    RenderUtil.draw2DRect(posX + padding, posY + height + padding + offset, width - 2 * padding, 12, 0.4F, 0.4F, 0.4F, 1F);
                    this.hoveredPreset = preset;

                } else {
                    RenderUtil.draw2DRect(posX + padding, posY + height + padding + offset, width - 2 * padding, 12, 0.3F, 0.3F, 0.3F, 1F);
                }

                fontRenderer.drawString(DirectoryUtil.removeExtension(preset.getName()), posX + padding + 2, posY + height + padding + 2 + offset, 0xFFFFFFFF, true);
                offset += 12;
            }
            RenderUtil.draw2DTriangleRight(posX + width - 2 * padding - 6, posY + height + padding + 2, 4, 8, 1F, 1F, 1F, 1F);
        } else {
            RenderUtil.draw2DTriangleDown(posX + width - 2 * padding - 8, posY + height + padding + 4, 8, 4, 1F, 1F, 1F, 1F);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (this.isMouseHover(mouseX, mouseY)) {
            //Full expand
            expanded = !expanded;

        } else if (expanded) {

            //Preset selection
            if (this.presetListExpanded) {
                if (hoveredPreset != null) {
                    presetManager.setActivePreset(hoveredPreset);
                    presetManager.loadActivePreset();
                    presetListExpanded = false;
                    return;
                }

            }

            if (isHovering(mouseX, mouseY, posX, posY + height + 2 * padding + 12,
                    posX + width, posY + height + 2 * padding + 20)) {
                //Auto-Save toggle
                presetManager.setAutoSave(!presetManager.isAutoSave());

            } else if (isHovering(mouseX, mouseY, posX + padding, posY + height + 3 * padding + 20,
                    posX + (width - padding) / 2F, posY + height + 3 * padding + 32)) {
                //Load button
                presetManager.loadActivePreset();

            } else if (isHovering(mouseX, mouseY, posX + (width + padding) / 2F, posY + height + 3 * padding + 20,
                    posX + width - padding, posY + height + 3 * padding + 32)) {
                //Save button
                presetManager.saveActivePreset();

            } else if (isHovering(mouseX, mouseY, posX + padding, posY + height + 4 * padding + 32,
                    posX + width - padding, posY + height + 4 * padding + 44)) {
                //Remove button
                presetManager.removeActivePreset();

            } else if (createButtonExpanded) {
                //If Create Button is expanded

                if (isHovering(mouseX, mouseY, posX + padding, posY + height + 6 * padding + 44,
                        posX + width - padding, posY + height + 6 * padding + 56)) {
                    //Cancel button
                    createButtonExpanded = false;
                    textField.setText("");
                    textField.setFocused(false);

                } else if (isHovering(mouseX, mouseY, posX + padding, posY + height + 8 * padding + 68,
                        posX + width - padding, posY + height + 8 * padding + 80)) {
                    //Confirm button
                    String newPreset = textField.getText().trim();
                    if (!newPreset.isEmpty() && GateClient.getGate().presetManager.createNewPreset(newPreset + ".json")) {
                        createButtonExpanded = false;
                        textField.setText("");
                    }

                }
            } else if (isHovering(mouseX, mouseY, posX + padding, posY + height + 6 * padding + 44,
                    posX + width - padding, posY + height + 6 * padding + 56)) {
                createButtonExpanded = true;
                textField.setFocused(true);

            } else if (isHovering(mouseX, mouseY, posX + padding, posY + height + padding,
                    posX + width - padding, posY + height + padding + 12)) {
                //Preset List
                presetManager.updatePresetList();
                this.presetListExpanded = !presetListExpanded;
            }
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (createButtonExpanded && textField.isFocused()) {
            if (keyCode == 1) {
                //Close the window
                createButtonExpanded = false;

            } else if (keyCode == Keyboard.KEY_RETURN || keyCode == Keyboard.KEY_NUMPADENTER) {
                //Create the preset
                String newPreset = textField.getText().trim();
                if (!newPreset.isEmpty() && GateClient.getGate().presetManager.createNewPreset(newPreset + ".json")) {
                    createButtonExpanded = false;
                    textField.setText("");
                }

            } else {
                //Type the character
                textField.keyTyped(typedChar, keyCode);

            }
        }

    }

    private boolean isHovering(int mouseX, int mouseY, float minX, float minY, float maxX, float maxY) {
        return mouseX > minX && mouseX < maxX && mouseY > minY && mouseY < maxY;
    }
}
