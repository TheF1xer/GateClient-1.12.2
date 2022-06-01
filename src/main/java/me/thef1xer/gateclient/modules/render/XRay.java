package me.thef1xer.gateclient.modules.render;

import me.thef1xer.gateclient.events.GetAmbientOcclusionLightValueEvent;
import me.thef1xer.gateclient.events.RenderBlockEvent;
import me.thef1xer.gateclient.events.SetOpaqueCubeEvent;
import me.thef1xer.gateclient.events.ShouldSideBeRenderedEvent;
import me.thef1xer.gateclient.modules.Module;
import me.thef1xer.gateclient.settings.impl.BlockListSetting;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;

public class XRay extends Module {
    public static final XRay INSTANCE = new XRay();

    public final BlockListSetting xrayBlocks = new BlockListSetting("XRay Blocks", "xrayblocks", new Block[]{
            Blocks.DIAMOND_ORE, Blocks.EMERALD_ORE, Blocks.GOLD_ORE, Blocks.IRON_ORE, Blocks.REDSTONE_ORE, Blocks.LIT_REDSTONE_ORE, Blocks.LAPIS_ORE, Blocks.COAL_ORE
    });

    private float lastGamma;
    private final Minecraft mc = Minecraft.getMinecraft();

    public XRay() {
        super("XRay", "xray", Module.ModuleCategory.RENDER);

        addSettings(xrayBlocks);

        lastGamma = mc.gameSettings.gammaSetting;
    }

    @Override
    public void onEnable() {
        super.onEnable();

        mc.renderGlobal.loadRenderers();
        lastGamma = mc.gameSettings.gammaSetting;
        mc.gameSettings.gammaSetting = 10000;
    }

    @Override
    public void onDisable() {
        super.onDisable();

        mc.renderGlobal.loadRenderers();
        if (!FullBright.INSTANCE.isEnabled()) {
            mc.gameSettings.gammaSetting = lastGamma;
        }
    }

    public void onRenderBlock(RenderBlockEvent event) {
        if (!xrayBlocks.getBlockList().contains(event.getState().getBlock())) {
            event.setCanceled(true);
        }
    }

    public void onSetOpaqueCube(SetOpaqueCubeEvent event) {
        event.setCanceled(true);
    }

    public void onShouldSideBeRendered(ShouldSideBeRenderedEvent event) {
        event.setShouldBeRendered(xrayBlocks.getBlockList().contains(event.getBlockState().getBlock()));
    }

    public void onGetAmbientOcclusionLightValue(GetAmbientOcclusionLightValueEvent event) {
        // If this isn't active, the blocks will look black
        event.setLightValue(1.0F);
    }
}
