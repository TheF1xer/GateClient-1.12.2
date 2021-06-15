package com.thef1xer.gateclient.gui.clickgui;

import com.thef1xer.gateclient.GateClient;
import com.thef1xer.gateclient.gui.clickgui.components.CategoryComponent;
import com.thef1xer.gateclient.modules.EnumModuleCategory;
import com.thef1xer.gateclient.modules.hud.ClickGuiModule;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClickGui extends GuiScreen {
    private final List<CategoryComponent> categoryComponents = new ArrayList<>();

    /*
    Edit initGui()
    New move(x, y) function in ClickComponent
    Update pos inside of initGui()
     */

    public void init() {
        this.categoryComponents.add(new CategoryComponent(EnumModuleCategory.COMBAT, 20, 20));
        this.categoryComponents.add(new CategoryComponent(EnumModuleCategory.HUD, 146, 20));
        this.categoryComponents.add(new CategoryComponent(EnumModuleCategory.MOVEMENT, 272, 20));
        this.categoryComponents.add(new CategoryComponent(EnumModuleCategory.PLAYER, 398, 20));
        this.categoryComponents.add(new CategoryComponent(EnumModuleCategory.RENDER, 524, 20));
        this.onUpdate();
    }

    public void onUpdate() {
        for (CategoryComponent component : this.categoryComponents) {
            component.onUpdate();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        GlStateManager.pushMatrix();
        GlStateManager.enableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        for (CategoryComponent category : this.categoryComponents) {
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

        for (CategoryComponent component : this.categoryComponents) {
            component.keyTyped(typedChar, keyCode);
        }

        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (CategoryComponent component : this.categoryComponents) {
            component.mouseClicked(mouseX, mouseY, mouseButton);
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        for (CategoryComponent component : this.categoryComponents) {
            component.mouseReleased(mouseX, mouseY, state);
        }
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public void onGuiClosed() {
        ClickGuiModule.INSTANCE.setEnabled(false);
        GateClient.getGate().presetManager.saveActivePreset();
        super.onGuiClosed();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
