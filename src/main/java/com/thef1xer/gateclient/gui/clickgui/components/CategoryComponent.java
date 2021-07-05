package com.thef1xer.gateclient.gui.clickgui.components;

import com.thef1xer.gateclient.GateClient;
import com.thef1xer.gateclient.gui.clickgui.ClickComponent;
import com.thef1xer.gateclient.modules.Module;
import com.thef1xer.gateclient.util.RenderUtil;

import java.util.ArrayList;
import java.util.List;

public class CategoryComponent extends ClickComponent {
    private final String displayName;
    private final List<ModuleComponent> modules = new ArrayList<>();

    private boolean expanded = true;
    private final float fontX;

    public CategoryComponent(Module.ModuleCategory category, float posX, float posY) {
        super(posX, posY);
        this.border = 5;
        this.height = 19;

        this.displayName = category.getName();
        for (Module module : GateClient.getGate().moduleManager.MODULE_LIST) {
            if (module.getModuleCategory() == category) {
                this.modules.add(new ModuleComponent(module, posX, 0));
            }
        }

        this.fontX = (this.width - fontRenderer.getStringWidth(displayName)) / 2F;
    }

    public void onUpdate() {
        if (this.expanded) {
            this.updateChildren(this.modules);
        }
    }

    @Override
    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        RenderUtil.draw2DRect(this.posX, this.posY, this.posX + width, this.posY + height, 0.18F, 0.38F, 0.9F, 1F);
        fontRenderer.drawString(this.displayName, this.posX + this.fontX, this.posY + this.border, 0xFFFFFFFF, true);
        if (this.expanded) {
            for (ModuleComponent module : this.modules) {
                module.drawComponent(mouseX, mouseY, partialTicks);
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (this.isMouseHover(mouseX, mouseY)) {
            this.expanded = !this.expanded;
            this.updateHierarchy();
        }

        if (this.expanded) {
            for (ModuleComponent module : this.modules) {
                module.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (this.expanded) {
            for (ModuleComponent module : this.modules) {
                module.mouseReleased(mouseX, mouseY, state);
            }
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        for (ModuleComponent module : this.modules) {
            module.keyTyped(typedChar, keyCode);
        }
    }
}
