package com.thef1xer.gateclient.modules.player;

import com.thef1xer.gateclient.events.EventSendPackage;
import com.thef1xer.gateclient.events.EventSetOpaqueCube;
import com.thef1xer.gateclient.modules.EnumModuleCategory;
import com.thef1xer.gateclient.modules.Module;
import com.thef1xer.gateclient.settings.FloatSetting;
import com.thef1xer.gateclient.settings.GroupSetting;
import com.thef1xer.gateclient.settings.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

public class Freecam extends Module {
    public int lastThirdPerson;
    public EntityOtherPlayerMP camera;

    public FloatSetting verticalSpeed = new FloatSetting("Vertical Speed", "vertical", 3F);
    public FloatSetting horizontalSpeed = new FloatSetting("Horizontal Speed", "horizontal", 3F);

    public GroupSetting speed = new GroupSetting("Speed", "speed") {
        @Override
        public Setting[] getSettings() {
            return new Setting[]{verticalSpeed, horizontalSpeed};
        }
    };

    public Freecam() {
        super("Freecam", EnumModuleCategory.PLAYER, Keyboard.KEY_R);
        this.addSettings(speed);
    }

    @Override
    public void onEnabled() {
        lastThirdPerson = Minecraft.getMinecraft().gameSettings.thirdPersonView;

        camera = new EntityOtherPlayerMP(Minecraft.getMinecraft().world, Minecraft.getMinecraft().getSession().getProfile());
        Minecraft.getMinecraft().world.addEntityToWorld(333333333, camera);
        camera.copyLocationAndAnglesFrom(Minecraft.getMinecraft().player);
        Minecraft.getMinecraft().setRenderViewEntity(camera);
        camera.noClip = true;
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void onDisabled() {
        MinecraftForge.EVENT_BUS.unregister(this);
        Minecraft.getMinecraft().setRenderViewEntity(Minecraft.getMinecraft().player);
        Minecraft.getMinecraft().gameSettings.thirdPersonView = lastThirdPerson;
        Minecraft.getMinecraft().world.removeEntity(camera);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        Minecraft.getMinecraft().gameSettings.thirdPersonView = 0;
        camera.inventory = Minecraft.getMinecraft().player.inventory;
        camera.setHealth(Minecraft.getMinecraft().player.getHealth());

        float forward = (Minecraft.getMinecraft().player.movementInput.forwardKeyDown ? horizontalSpeed.getValue() : 0) -
                (Minecraft.getMinecraft().player.movementInput.backKeyDown ? horizontalSpeed.getValue() : 0);
        float strafe = (Minecraft.getMinecraft().player.movementInput.leftKeyDown ? horizontalSpeed.getValue() : 0) -
                (Minecraft.getMinecraft().player.movementInput.rightKeyDown ? horizontalSpeed.getValue() : 0);
        float vertical = (Minecraft.getMinecraft().player.movementInput.jump ? verticalSpeed.getValue() : 0) -
                (Minecraft.getMinecraft().player.movementInput.sneak ? verticalSpeed.getValue() : 0);

        Vec3d vector = new Vec3d(strafe, vertical, forward).rotateYaw( (float) -Math.toRadians(camera.rotationYaw));
        camera.setPositionAndRotationDirect(camera.posX + vector.x, camera.posY + vector.y, camera.posZ + vector.z, camera.rotationYaw, camera.rotationPitch, 3, false);
    }

    @SubscribeEvent
    public void onOpaqueCube(EventSetOpaqueCube event) {
        event.setCanceled(true);
    }

    @SubscribeEvent
    public void onPackage(EventSendPackage event) {

    }

}
