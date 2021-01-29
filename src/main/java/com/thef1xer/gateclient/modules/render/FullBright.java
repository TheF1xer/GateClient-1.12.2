package com.thef1xer.gateclient.modules.render;

import com.thef1xer.gateclient.modules.EnumModuleCategory;
import com.thef1xer.gateclient.modules.Module;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class FullBright extends Module {
    private float lastGamma;

    public FullBright() {
        super("Full Bright", "fullbright", EnumModuleCategory.RENDER, Keyboard.KEY_N);
    }

    @Override
    public void onEnabled() {
        super.onEnabled();
        lastGamma = Minecraft.getMinecraft().gameSettings.gammaSetting;
        Minecraft.getMinecraft().gameSettings.gammaSetting = 10000F;
    }

    @Override
    public void onDisabled() {
        super.onDisabled();
        Minecraft.getMinecraft().gameSettings.gammaSetting = lastGamma > 1F ? 1F : lastGamma;
    }
}
