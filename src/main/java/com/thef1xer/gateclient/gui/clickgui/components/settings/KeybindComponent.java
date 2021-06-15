package com.thef1xer.gateclient.gui.clickgui.components.settings;

import com.thef1xer.gateclient.gui.clickgui.ClickComponent;
import com.thef1xer.gateclient.modules.Module;
import com.thef1xer.gateclient.util.RenderUtil;
import org.lwjgl.input.Keyboard;

public class KeybindComponent extends ClickComponent {
    private final Module module;
    private boolean listening = false;

    public KeybindComponent(Module module, float posX, float posY) {
        super(posX, posY);
        this.module = module;
    }

    @Override
    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        RenderUtil.draw2DRect(this.posX, this.posY, this.posX + this.width, this.posY + this.height, 0.15F, 0.15F, 0.15F, 1F);
        fontRenderer.drawString("Keybind", posX + border, posY + border, 0xFFFFFFFF, true);
        if (listening) {
            fontRenderer.drawString("...", posX + width - border - fontRenderer.getStringWidth("..."), posY + border, 0xFFFFFFFF, true);
        } else {
            String key = Keyboard.getKeyName(module.getKeyBind());
            fontRenderer.drawString(key, posX + width - border - fontRenderer.getStringWidth(key), posY + border, 0xFFFFFFFF, true);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (this.isMouseHover(mouseX, mouseY)) {
            if (this.listening) {
                if (mouseButton == 1) {
                    this.module.setKeyBind(0);
                }
                this.listening = false;
            } else {
                this.listening = true;
            }
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (this.listening) {
            this.module.setKeyBind(keyCode);
            this.listening = false;
        }
    }
}
