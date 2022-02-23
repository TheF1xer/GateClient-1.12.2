package me.thef1xer.gateclient.mixin.impl;

import me.thef1xer.gateclient.events.RenderToolTipEvent;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiScreen.class)
public abstract class MixinGuiScreen {

    @Inject(method = "renderToolTip", at = @At("HEAD"), cancellable = true)
    public void renderToolTip(ItemStack stack, int x, int y, CallbackInfo callbackInfo) {
        if (MinecraftForge.EVENT_BUS.post(new RenderToolTipEvent(stack, x, y))) {
            callbackInfo.cancel();
        }
    }
}
