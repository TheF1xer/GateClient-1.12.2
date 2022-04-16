package me.thef1xer.gateclient.modules.render;

import me.thef1xer.gateclient.modules.Module;
import net.minecraft.client.Minecraft;

public class FullBright extends Module {
    public static final FullBright INSTANCE = new FullBright();

    private float lastGamma;

    public FullBright() {
        super("Full Bright", "fullbright", Module.ModuleCategory.RENDER);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        lastGamma = Minecraft.getMinecraft().gameSettings.gammaSetting;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Minecraft.getMinecraft().gameSettings.gammaSetting = Math.min(lastGamma, 1F);
    }

    public void onClientTick() {
        Minecraft.getMinecraft().gameSettings.gammaSetting = 10000F;
    }
}
