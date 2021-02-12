package com.thef1xer.gateclient.modules.render;

import com.thef1xer.gateclient.modules.EnumModuleCategory;
import com.thef1xer.gateclient.modules.Module;
import net.minecraft.client.Minecraft;

public class FullBright extends Module {
    private float lastGamma;

    public FullBright() {
        super("Full Bright", "fullbright", EnumModuleCategory.RENDER);
        this.lastGamma = 1F;
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
        Minecraft.getMinecraft().gameSettings.gammaSetting = Math.min(lastGamma, 1F);
    }
}
