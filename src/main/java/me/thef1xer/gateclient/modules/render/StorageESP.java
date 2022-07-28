package me.thef1xer.gateclient.modules.render;

import me.thef1xer.gateclient.modules.Module;
import me.thef1xer.gateclient.settings.impl.BooleanSetting;
import me.thef1xer.gateclient.settings.impl.FloatSetting;
import me.thef1xer.gateclient.settings.impl.RGBSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.tileentity.*;
import net.minecraft.util.math.AxisAlignedBB;

public class StorageESP extends Module {
    public static final StorageESP INSTANCE = new StorageESP();

    public final BooleanSetting chest = new BooleanSetting("Chest", "chest", true);
    public final BooleanSetting shulker = new BooleanSetting("Shulker", "shulker", true);
    public final BooleanSetting enderChest = new BooleanSetting("Ender Chest", "enderchest", true);
    public final BooleanSetting furnace = new BooleanSetting("Furnace", "furnace", true);
    public final BooleanSetting dropper = new BooleanSetting("Dropper", "dropper", true);
    public final BooleanSetting dispenser = new BooleanSetting("Dispenser", "dispenser", true);
    public final BooleanSetting hopper = new BooleanSetting("Hopper", "hopper", true);

    public final RGBSetting chestColor = new RGBSetting("Chest Color", "chestcolor", 50, 50, 190);
    public final RGBSetting shulkerColor = new RGBSetting("Shulker Color", "shulkercolor", 255, 80, 240);
    public final RGBSetting enderChestColor = new RGBSetting("E-Chest Color", "echestcolor", 130, 0, 150);
    public final RGBSetting furnaceColor = new RGBSetting("Furnace Color", "furnacecolor", 100, 100, 100);
    public final RGBSetting dropperColor = new RGBSetting("Dropper Color", "droppercolor", 100, 100, 100);
    public final RGBSetting dispenserColor = new RGBSetting("Dispenser Color", "dispensercolor", 100, 100, 100);
    public final RGBSetting hopperColor = new RGBSetting("Hopper Color", "hoppercolor", 100, 100, 100);

    public final FloatSetting colorAlpha = new FloatSetting("Color Alpha", "coloralpha", 1F, 0F, 1F);

    public StorageESP() {
        super("Storage ESP", "storageesp", Module.ModuleCategory.RENDER);
        this.addSettings(chest, shulker, enderChest, furnace, dropper, dispenser, hopper, chestColor, shulkerColor, enderChestColor, furnaceColor, dropperColor, dispenserColor, hopperColor, colorAlpha);
    }

    public void onRenderWorldLast() {
        RenderManager rm = Minecraft.getMinecraft().getRenderManager();

        for (TileEntity entity : Minecraft.getMinecraft().world.loadedTileEntityList) {
            AxisAlignedBB bb = new AxisAlignedBB(entity.getPos()).offset(-rm.viewerPosX, -rm.viewerPosY, -rm.viewerPosZ);

            if (chest.getValue() && entity instanceof TileEntityChest) {

                RenderGlobal.drawSelectionBoundingBox(bb,  chestColor.getRed()/255F,  chestColor.getGreen()/255F,  chestColor.getBlue()/255F, colorAlpha.getValue());
                RenderGlobal.renderFilledBox(bb,chestColor.getRed()/255F,  chestColor.getGreen()/255F,  chestColor.getBlue()/255F, colorAlpha.getValue()/3);

            } else if (shulker.getValue() && entity instanceof TileEntityShulkerBox) {

                RenderGlobal.drawSelectionBoundingBox(bb, shulkerColor.getRed()/255F,  shulkerColor.getGreen()/255F,  shulkerColor.getBlue()/255F, colorAlpha.getValue());
                RenderGlobal.renderFilledBox(bb, shulkerColor.getRed()/255F,  shulkerColor.getGreen()/255F,  shulkerColor.getBlue()/255F, colorAlpha.getValue()/3);

            } else if (enderChest.getValue() && entity instanceof TileEntityEnderChest) {

                RenderGlobal.drawSelectionBoundingBox(bb, enderChestColor.getRed()/255F,  enderChestColor.getGreen()/255F,  enderChestColor.getBlue()/255F, colorAlpha.getValue());
                RenderGlobal.renderFilledBox(bb, enderChestColor.getRed()/255F,  enderChestColor.getGreen()/255F,  enderChestColor.getBlue()/255F, colorAlpha.getValue()/3);

            } else if (furnace.getValue() && entity instanceof TileEntityFurnace) {

                RenderGlobal.drawSelectionBoundingBox(bb, furnaceColor.getRed()/255F,  furnaceColor.getGreen()/255F,  furnaceColor.getBlue()/255F, colorAlpha.getValue());
                RenderGlobal.renderFilledBox(bb, furnaceColor.getRed()/255F,  furnaceColor.getGreen()/255F,  furnaceColor.getBlue()/255F, colorAlpha.getValue()/3);

            } else if (dropper.getValue() && entity instanceof TileEntityDropper) {

                RenderGlobal.drawSelectionBoundingBox(bb, dropperColor.getRed()/255F,  dropperColor.getGreen()/255F,  dropperColor.getBlue()/255F, colorAlpha.getValue());
                RenderGlobal.renderFilledBox(bb, dropperColor.getRed()/255F,  dropperColor.getGreen()/255F,  dropperColor.getBlue()/255F, colorAlpha.getValue()/3);

            } else if (dispenser.getValue() && entity instanceof TileEntityDispenser) {

                RenderGlobal.drawSelectionBoundingBox(bb, dispenserColor.getRed()/255F,  dispenserColor.getGreen()/255F,  dispenserColor.getBlue()/255F, colorAlpha.getValue());
                RenderGlobal.renderFilledBox(bb, dispenserColor.getRed()/255F,  dispenserColor.getGreen()/255F,  dispenserColor.getBlue()/255F, colorAlpha.getValue()/3);

            } else if (hopper.getValue() && entity instanceof TileEntityHopper) {

                RenderGlobal.drawSelectionBoundingBox(bb, hopperColor.getRed()/255F,  hopperColor.getGreen()/255F,  hopperColor.getBlue()/255F, colorAlpha.getValue());
                RenderGlobal.renderFilledBox(bb, hopperColor.getRed()/255F,  hopperColor.getGreen()/255F,  hopperColor.getBlue()/255F, colorAlpha.getValue()/3);

            }
        }
    }
}
