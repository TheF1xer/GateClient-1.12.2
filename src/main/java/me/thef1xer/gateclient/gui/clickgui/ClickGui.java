package me.thef1xer.gateclient.gui.clickgui;

import me.thef1xer.gateclient.GateClient;
import me.thef1xer.gateclient.gui.clickgui.components.CategoryComponent;
import me.thef1xer.gateclient.gui.clickgui.components.PresetComponent;
import me.thef1xer.gateclient.modules.Module;
import me.thef1xer.gateclient.modules.hud.ClickGuiModule;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClickGui extends GuiScreen {
    private final List<ClickComponent> components = new ArrayList<>();

    public void init() {
        this.components.add(new CategoryComponent(Module.ModuleCategory.COMBAT, 20, 20));
        this.components.add(new CategoryComponent(Module.ModuleCategory.HUD, 146, 20));
        this.components.add(new CategoryComponent(Module.ModuleCategory.MOVEMENT, 272, 20));
        this.components.add(new CategoryComponent(Module.ModuleCategory.PLAYER, 398, 20));
        this.components.add(new CategoryComponent(Module.ModuleCategory.RENDER, 524, 20));
        this.components.add(new PresetComponent(650, 20));
        this.onUpdate();
    }

    public void onUpdate() {
        for (ClickComponent component : this.components) {
            if (component.isExpanded()) {
                component.updateChildren();
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.enableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.glLineWidth(1);
        for (ClickComponent category : this.components) {
            category.drawComponent(mouseX, mouseY, partialTicks);
        }
        GlStateManager.disableTexture2D();
        GlStateManager.popMatrix();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == ClickGuiModule.INSTANCE.getKeyBind()) {
            mc.displayGuiScreen(null);
            if (this.mc.currentScreen == null) {
                this.mc.setIngameFocus();
            }
        }

        for (ClickComponent component : this.components) {
            component.keyTyped(typedChar, keyCode);
        }

        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (ClickComponent component : this.components) {
            component.mouseClicked(mouseX, mouseY, mouseButton);
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        for (ClickComponent component : this.components) {
            component.mouseReleased(mouseX, mouseY, state);
        }
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public void onGuiClosed() {
        ClickGuiModule.INSTANCE.setEnabled(false);
        if (GateClient.getGate().presetManager.isAutoSave()) {
            GateClient.getGate().presetManager.saveActivePreset();
        }
        super.onGuiClosed();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
