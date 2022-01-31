package me.thef1xer.gateclient.mixin.impl;

import com.mojang.authlib.GameProfile;
import me.thef1xer.gateclient.events.PlayerIsUserEvent;
import me.thef1xer.gateclient.events.PlayerMoveEvent;
import me.thef1xer.gateclient.events.UpdateWalkingPlayerEvent;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.MoverType;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayerSP.class)
public abstract class MixinEntityPlayerSP extends AbstractClientPlayer {

    public MixinEntityPlayerSP(World worldIn, GameProfile playerProfile) {
        super(worldIn, playerProfile);
    }

    @Shadow
    protected abstract void updateAutoJump(float p_189810_1_, float p_189810_2_);

    @Inject(method = "isUser", at = @At("RETURN"), cancellable = true)
    public void isUser(CallbackInfoReturnable<Boolean> returnable) {
        if (MinecraftForge.EVENT_BUS.post(new PlayerIsUserEvent())) {
            returnable.setReturnValue(false);
        }
    }

    @Inject(method = "move", at = @At("HEAD"), cancellable = true)
    public void move(MoverType type, double x, double y, double z, CallbackInfo callbackInfo) {
        PlayerMoveEvent event = new PlayerMoveEvent(x, y, z);
        MinecraftForge.EVENT_BUS.post(event);

        double d0 = this.posX;
        double d1 = this.posZ;
        super.move(type, event.x, event.y, event.z);
        this.updateAutoJump((float)(this.posX - d0), (float)(this.posZ - d1));

        callbackInfo.cancel();
    }

    @Inject(method = "onUpdateWalkingPlayer", at = @At("HEAD"), cancellable = true)
    public void onUpdateWalkingPlayer(CallbackInfo callbackInfo) {
        if (MinecraftForge.EVENT_BUS.post(new UpdateWalkingPlayerEvent())) {
            callbackInfo.cancel();
        }
    }
}
