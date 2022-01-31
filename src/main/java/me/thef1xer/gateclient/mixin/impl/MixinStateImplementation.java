package me.thef1xer.gateclient.mixin.impl;

import me.thef1xer.gateclient.events.GetAmbientOcclusionLightValueEvent;
import me.thef1xer.gateclient.events.ShouldSideBeRenderedEvent;
import net.minecraft.block.state.BlockStateBase;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockStateContainer.StateImplementation.class)
public abstract class MixinStateImplementation extends BlockStateBase {

    @Inject(method = "shouldSideBeRendered", at = @At("RETURN"), cancellable = true)
    public void shouldSideBeRendered(IBlockAccess blockAccess, BlockPos pos, EnumFacing facing, CallbackInfoReturnable<Boolean> returnable) {
        ShouldSideBeRenderedEvent event = new ShouldSideBeRenderedEvent(this, returnable.getReturnValue());
        MinecraftForge.EVENT_BUS.post(event);
        returnable.setReturnValue(event.getShouldBeRendered());
    }

    @Inject(method = "getAmbientOcclusionLightValue", at = @At("RETURN"), cancellable = true)
    public void getAmbientOcclusionLightValue(CallbackInfoReturnable<Float> returnable) {
        GetAmbientOcclusionLightValueEvent event = new GetAmbientOcclusionLightValueEvent(this);
        MinecraftForge.EVENT_BUS.post(event);
        returnable.setReturnValue(event.getLightValue());
    }
}
