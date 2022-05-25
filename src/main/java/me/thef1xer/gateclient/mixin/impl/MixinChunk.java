package me.thef1xer.gateclient.mixin.impl;

import me.thef1xer.gateclient.events.SetBlockStateEvent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Chunk.class)
public abstract class MixinChunk {

    @Inject(method = "setBlockState", at = @At("HEAD"))
    public void setBlockState(BlockPos pos, IBlockState state, CallbackInfoReturnable<IBlockState> returnable) {
        MinecraftForge.EVENT_BUS.post(new SetBlockStateEvent(pos, state));
    }
}
