package me.thef1xer.gateclient.modules.render;

import me.thef1xer.gateclient.modules.Module;
import me.thef1xer.gateclient.settings.impl.FloatSetting;
import me.thef1xer.gateclient.settings.impl.RGBSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class StorageESP extends Module {
    public static final StorageESP INSTANCE = new StorageESP();

    public final RGBSetting chestColor = new RGBSetting("Chest Color", "chestcolor", 50, 50, 190);
    public final RGBSetting shulkerColor = new RGBSetting("Shulker Color", "shulkercolor", 255, 80, 240);
    public final RGBSetting enderChestColor = new RGBSetting("E-Chest Color", "echestcolor", 130, 0, 150);
    public final FloatSetting colorAlpha = new FloatSetting("Color Alpha", "coloralpha", 1F, 0F, 1F);

    public StorageESP() {
        super("Storage ESP", "storageesp", Module.ModuleCategory.RENDER);
        chestColor.setParent("Color");
        shulkerColor.setParent("Color");
        enderChestColor.setParent("Color");
        this.addSettings(chestColor, shulkerColor, enderChestColor, colorAlpha);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        //TODO: Fix weird interaction with Tracers
        RenderManager rm = Minecraft.getMinecraft().getRenderManager();

        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.disableDepth();
        for (TileEntity entity : Minecraft.getMinecraft().world.loadedTileEntityList) {
            AxisAlignedBB bb;
            if (entity instanceof TileEntityChest) {
                bb = new AxisAlignedBB(entity.getPos()).offset(-rm.viewerPosX, -rm.viewerPosY, -rm.viewerPosZ);

                RenderGlobal.drawSelectionBoundingBox(bb,
                        (float) chestColor.getRed()/255, (float) chestColor.getGreen()/255, (float) chestColor.getBlue()/255, colorAlpha.getValue());
                RenderGlobal.renderFilledBox(bb,
                        (float) chestColor.getRed()/255, (float) chestColor.getGreen()/255, (float) chestColor.getBlue()/255, colorAlpha.getValue()/3);

            } else if (entity instanceof TileEntityShulkerBox) {
                bb = new AxisAlignedBB(entity.getPos()).offset(-rm.viewerPosX, -rm.viewerPosY, -rm.viewerPosZ);

                RenderGlobal.drawSelectionBoundingBox(bb,
                        (float) shulkerColor.getRed()/255, (float) shulkerColor.getGreen()/255, (float) shulkerColor.getBlue()/255, colorAlpha.getValue());
                RenderGlobal.renderFilledBox(bb,
                        (float) shulkerColor.getRed()/255, (float) shulkerColor.getGreen()/255, (float) shulkerColor.getBlue()/255, colorAlpha.getValue()/3);

            } else if (entity instanceof TileEntityEnderChest) {
                bb = new AxisAlignedBB(entity.getPos()).offset(-rm.viewerPosX, -rm.viewerPosY, -rm.viewerPosZ);

                RenderGlobal.drawSelectionBoundingBox(bb,
                        (float) enderChestColor.getRed()/255, (float) enderChestColor.getGreen()/255, (float) enderChestColor.getBlue()/255, colorAlpha.getValue());
                RenderGlobal.renderFilledBox(bb,
                        (float) enderChestColor.getRed()/255, (float) enderChestColor.getGreen()/255, (float) enderChestColor.getBlue()/255, colorAlpha.getValue()/3);

            }
        }
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        GlStateManager.enableAlpha();
        GlStateManager.popMatrix();
    }
}
