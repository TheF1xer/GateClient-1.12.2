package me.thef1xer.gateclient.mixin.impl;

import me.thef1xer.gateclient.events.GetLiquidCollisionBoundingBoxEvent;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockLiquid.class)
public abstract class MixinBlockLiquid {

    @Inject(method = "getCollisionBoundingBox", at = @At("RETURN"), cancellable = true)
    public void getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos, CallbackInfoReturnable<AxisAlignedBB> returnable) {
        GetLiquidCollisionBoundingBoxEvent event = new GetLiquidCollisionBoundingBoxEvent(pos);
        MinecraftForge.EVENT_BUS.post(event);
        returnable.setReturnValue(event.getCollisionBoundingBox());
    }
}
