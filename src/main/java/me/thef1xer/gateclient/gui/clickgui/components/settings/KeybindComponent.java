package me.thef1xer.gateclient.gui.clickgui.components.settings;

import me.thef1xer.gateclient.gui.clickgui.ClickComponent;
import me.thef1xer.gateclient.modules.Module;
import me.thef1xer.gateclient.util.RenderUtil;
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
        // Background
        RenderUtil.draw2DRect(posX, posY, width, height, 0F, 0F, 0F, 0.6F);
        fontRenderer.drawString("Keybind", posX + padding, posY + padding, 0xFFFFFFFF, true);

        // Key
        if (listening) {
            fontRenderer.drawString("...", posX + width - padding - fontRenderer.getStringWidth("..."), posY + padding, 0xFFFFFFFF, true);
        } else {
            String key = Keyboard.getKeyName(module.getKeyBind());
            fontRenderer.drawString(key, posX + width - padding - fontRenderer.getStringWidth(key), posY + padding, 0xFFFFFFFF, true);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (this.listening) {
            if (isMouseHover(mouseX, mouseY) && mouseButton == 1) {
                this.module.setKeyBind(0);
            }
            this.listening = false;
        } else {
            if (isMouseHover(mouseX, mouseY)) {
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
