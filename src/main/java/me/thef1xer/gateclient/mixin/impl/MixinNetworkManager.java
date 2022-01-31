package me.thef1xer.gateclient.mixin.impl;

import io.netty.channel.ChannelHandlerContext;
import me.thef1xer.gateclient.events.ReceivePacketEvent;
import me.thef1xer.gateclient.events.SendPacketEvent;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetworkManager.class)
public abstract class MixinNetworkManager {

    @Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
    public void sendPacket(Packet<?> packetIn, CallbackInfo callbackInfo) {
        if (MinecraftForge.EVENT_BUS.post(new SendPacketEvent(packetIn))) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
    public void channelRead0(ChannelHandlerContext p_channelRead0_1_, Packet<?> p_channelRead0_2_, CallbackInfo callbackInfo) {
        if (MinecraftForge.EVENT_BUS.post(new ReceivePacketEvent(p_channelRead0_2_))) {
            callbackInfo.cancel();
        }
    }
}
