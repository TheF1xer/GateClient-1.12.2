package me.thef1xer.gateclient.modules.player;

import me.thef1xer.gateclient.events.PlayerIsUserEvent;
import me.thef1xer.gateclient.events.SendPacketEvent;
import me.thef1xer.gateclient.events.SetOpaqueCubeEvent;
import me.thef1xer.gateclient.modules.Module;
import me.thef1xer.gateclient.settings.impl.FloatSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.MovementInput;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.InputUpdateEvent;

public class Freecam extends Module {
    public static final Freecam INSTANCE = new Freecam();

    public final FloatSetting verticalSpeed = new FloatSetting("Vertical Speed", "verticalspeed", 3F);
    public final FloatSetting horizontalSpeed = new FloatSetting("Horizontal Speed", "horizontalspeed", 3F);

    private final Minecraft mc = Minecraft.getMinecraft();
    private int lastThirdPerson;
    public EntityOtherPlayerMP camera;
    private boolean activeThisSession = false;

    public Freecam() {
        super("Freecam", "freecam", Module.ModuleCategory.PLAYER);
        addSettings(verticalSpeed, horizontalSpeed);
    }

    @Override
    public void onEnable() {
        super.onEnable();

        activeThisSession = true;
        mc.renderGlobal.loadRenderers();
    }

    @Override
    public void onDisable() {
        super.onDisable();

        if (mc.world != null && mc.world.isRemote) {
            mc.setRenderViewEntity(mc.player);
            if (activeThisSession) {
                mc.gameSettings.thirdPersonView = lastThirdPerson;
                mc.world.removeEntity(camera);
            }
        }

        camera = null;
        activeThisSession = false;
    }

    public void onClientTick() {
        // TODO: Try to fix weird hand movement that sometimes happens
        // TODO: This still doesn't work with baritone xd

        if (mc.world == null || !mc.world.isRemote) {
            camera = null;
            return;
        }

        // We use a "camera" entity here so that it doesn't interact with Baritone
        if (camera == null) {
            lastThirdPerson = mc.gameSettings.thirdPersonView;

            camera = new EntityOtherPlayerMP(mc.world, mc.getSession().getProfile());
            mc.world.addEntityToWorld(333393333, camera);
            camera.copyLocationAndAnglesFrom(mc.player);
            mc.setRenderViewEntity(camera);
            camera.noClip = true;
        }

        mc.gameSettings.thirdPersonView = 0;
        camera.inventory = mc.player.inventory;
        camera.setHealth(mc.player.getHealth());
    }

    public void onInputUpdate(InputUpdateEvent event) {
        if (camera == null) {
            return;
        }

        MovementInput input = event.getMovementInput();

        float forward = (input.forwardKeyDown ? horizontalSpeed.getValue() : 0) -
                (input.backKeyDown ? horizontalSpeed.getValue() : 0);
        float strafe = (input.leftKeyDown ? horizontalSpeed.getValue() : 0) -
                (input.rightKeyDown ? horizontalSpeed.getValue() : 0);
        float vertical = (input.jump ? verticalSpeed.getValue() : 0) -
                (input.sneak ? verticalSpeed.getValue() : 0);

        Vec3d vector = new Vec3d(strafe, vertical, forward).rotateYaw((float) -Math.toRadians(camera.rotationYaw));
        camera.setPositionAndRotationDirect(camera.posX + vector.x, camera.posY + vector.y, camera.posZ + vector.z, camera.rotationYaw, camera.rotationPitch, 3, false);

        // This should stop the player from moving if Baritone isn't being used, needs some testing
        if (input instanceof MovementInputFromOptions) {
            mc.player.moveVertical = 0;
            mc.player.moveStrafing = 0;
            mc.player.moveForward = 0;
            mc.player.setJumping(false);
            mc.player.movementInput.sneak = false;
        }
    }

    public void onPlayerIsUser(PlayerIsUserEvent event) {
        // Allows you to watch yourself while Freecam is active
        event.setCanceled(true);
    }

    public void onSetOpaqueCube(SetOpaqueCubeEvent event) {
        event.setCanceled(true);
    }

    public void onSendPacket(SendPacketEvent event) {
        // Prevents the player from hitting themselves
        if (event.getPacket() instanceof CPacketUseEntity) {
            CPacketUseEntity useEntity = (CPacketUseEntity) event.getPacket();
            if (useEntity.getEntityFromWorld(mc.world) == mc.player) {
                event.setCanceled(true);
            }
        }
    }
}
