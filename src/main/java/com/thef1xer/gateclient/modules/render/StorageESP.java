package com.thef1xer.gateclient.modules.render;

import com.thef1xer.gateclient.modules.EnumModuleCategory;
import com.thef1xer.gateclient.modules.Module;
import com.thef1xer.gateclient.settings.ColorSetting;
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
import org.lwjgl.input.Keyboard;

public class StorageESP extends Module {
    public ColorSetting chestColor = new ColorSetting("Chest Color", "chestcolor", 255, 255, 255, 255);
    public ColorSetting shulkerColor = new ColorSetting("Shulker Color", "shulkercolor", 255, 255, 255, 255);
    public ColorSetting enderChestColor = new ColorSetting("Ender Chest Color", "endercolor", 255, 255, 255, 255);

    public StorageESP() {
        super("Storage ESP", "storageesp", EnumModuleCategory.RENDER, Keyboard.KEY_C);
        chestColor.setParent("Color");
        shulkerColor.setParent("Color");
        enderChestColor.setParent("Color");
        this.addSettings(chestColor, shulkerColor, enderChestColor);
    }

    @Override
    public void onEnabled() {
        super.onEnabled();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void onDisabled() {
        super.onDisabled();
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
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
                RenderGlobal.drawSelectionBoundingBox(bb, (float) chestColor.getRed()/255, (float) chestColor.getGreen()/255, (float) chestColor.getBlue()/255, (float) chestColor.getAlpha()/255);
            } else if (entity instanceof TileEntityShulkerBox) {
                bb = new AxisAlignedBB(entity.getPos()).offset(-rm.viewerPosX, -rm.viewerPosY, -rm.viewerPosZ);
                RenderGlobal.drawSelectionBoundingBox(bb, (float) shulkerColor.getRed()/255, (float) shulkerColor.getGreen()/255, (float) shulkerColor.getBlue()/255, (float) shulkerColor.getAlpha()/255);
            } else if (entity instanceof TileEntityEnderChest) {
                bb = new AxisAlignedBB(entity.getPos()).offset(-rm.viewerPosX, -rm.viewerPosY, -rm.viewerPosZ);
                RenderGlobal.drawSelectionBoundingBox(bb, (float) enderChestColor.getRed()/255, (float) enderChestColor.getGreen()/255, (float) enderChestColor.getBlue()/255, (float) enderChestColor.getAlpha()/255);
            }
        }
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        GlStateManager.enableAlpha();
        GlStateManager.popMatrix();
    }
}
