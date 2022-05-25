package me.thef1xer.gateclient.events;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Event;

public class SetBlockStateEvent extends Event {
    private final BlockPos blockPos;
    private final IBlockState blockState;


    public SetBlockStateEvent(BlockPos blockPos, IBlockState blockState) {
        this.blockPos = blockPos;
        this.blockState = blockState;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public IBlockState getBlockState() {
        return blockState;
    }
}
