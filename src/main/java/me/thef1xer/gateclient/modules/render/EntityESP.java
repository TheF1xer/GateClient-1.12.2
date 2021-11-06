package me.thef1xer.gateclient.modules.render;

import me.thef1xer.gateclient.modules.Module;
import me.thef1xer.gateclient.settings.impl.BooleanSetting;
import me.thef1xer.gateclient.settings.impl.FloatSetting;
import me.thef1xer.gateclient.settings.impl.RGBSetting;
import me.thef1xer.gateclient.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EntityESP extends Module {
    public static final EntityESP INSTANCE = new EntityESP();

    public final BooleanSetting targetPlayer = new BooleanSetting("Players", "players", true);
    public final BooleanSetting targetHostile = new BooleanSetting("Monsters", "monsters", true);
    public final BooleanSetting targetPassive = new BooleanSetting("Passive", "passive", true);

    public final RGBSetting playerColor = new RGBSetting("Player Color", "playercolor", 255, 0, 0);
    public final RGBSetting hostileColor = new RGBSetting("Monster Color", "hostilecolor", 255, 255, 0);
    public final RGBSetting passiveColor = new RGBSetting("Passive Color", "passivecolor", 0, 255, 0);
    public final FloatSetting colorAlpha = new FloatSetting("Color Alpha", "coloralpha", 1F, 0F, 1F);

    public EntityESP() {
        super("Entity ESP", "entityesp", Module.ModuleCategory.RENDER);
        targetPlayer.setParent("Target");
        targetHostile.setParent("Target");
        playerColor.setParent("Color");
        hostileColor.setParent("Color");
        this.addSettings(targetPlayer, targetHostile, targetPassive, playerColor, hostileColor, passiveColor, colorAlpha);
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
    public void onRenderWorld(RenderWorldLastEvent event) {
        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.disableDepth();

        for (Entity entity : Minecraft.getMinecraft().world.loadedEntityList) {
            if (targetPlayer.getValue() && entity instanceof EntityPlayer && entity != Minecraft.getMinecraft().getRenderViewEntity()) {
                RenderUtil.renderEntityBoundingBox(entity, playerColor, colorAlpha.getValue());
                RenderUtil.renderEntityFilledBoundingBox(entity, playerColor, colorAlpha.getValue()/3);
            } else if (targetHostile.getValue() && entity.isCreatureType(EnumCreatureType.MONSTER, false)) {
                RenderUtil.renderEntityBoundingBox(entity, hostileColor, colorAlpha.getValue());
                RenderUtil.renderEntityFilledBoundingBox(entity, hostileColor, colorAlpha.getValue()/3);
            } else if (targetPassive.getValue() && (entity.isCreatureType(EnumCreatureType.AMBIENT, false) || entity.isCreatureType(EnumCreatureType.WATER_CREATURE, false) || entity.isCreatureType(EnumCreatureType.CREATURE, false))) {
                RenderUtil.renderEntityBoundingBox(entity, passiveColor, colorAlpha.getValue());
                RenderUtil.renderEntityFilledBoundingBox(entity, passiveColor, colorAlpha.getValue()/3);
            }
        }

        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        GlStateManager.enableAlpha();
        GlStateManager.popMatrix();
    }
}
