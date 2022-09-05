package me.thef1xer.gateclient.modules.render;

import me.thef1xer.gateclient.GateClient;
import me.thef1xer.gateclient.events.CheckBlockInChunkEvent;
import me.thef1xer.gateclient.events.CheckChunkEvent;
import me.thef1xer.gateclient.events.SetBlockStateEvent;
import me.thef1xer.gateclient.modules.Module;
import me.thef1xer.gateclient.settings.impl.FloatSetting;
import me.thef1xer.gateclient.util.WorldUtil;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;

import java.util.ArrayList;
import java.util.List;

public class HoleESP extends Module {
    public static final HoleESP INSTANCE = new HoleESP();

    public final FloatSetting radius = new FloatSetting("Radius", "radius", 10, 0, 35, 1);

    private final Minecraft mc = Minecraft.getMinecraft();
    private final List<Hole> holeList = new ArrayList<>();

    public HoleESP() {
        super("Hole ESP", "holeesp", ModuleCategory.RENDER);
        addSettings(radius);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        holeList.clear();

        if (mc.world != null) {
            GateClient.getGate().threadManager.chunksToCheck.addAll(mc.world.getChunkProvider().chunkMapping.values());
        }
    }

    public void onSetBlockState(SetBlockStateEvent event) {
        if (mc.world == null) return;

        Block block = event.getBlockState().getBlock();
        BlockPos pos = event.getBlockPos();

        // Update hole status for updated block
        updateHole(block, pos);

        // Update surrounding blocks
        for (EnumFacing facing : EnumFacing.values()) {
            updateHole(mc.world.getBlockState(pos.offset(facing)).getBlock(), pos.offset(facing));
        }
    }

    public void onCheckChunk(CheckChunkEvent event) {
        if (event.isChunkLoaded()) {

            // Check if block is still there
            for (int i = 0; i < holeList.size(); i++) {
                BlockPos holePos = holeList.get(i).getPos();

                if (WorldUtil.isBlockPosInChunk(event.getChunk(), holePos)) {
                    Block holeBlock = mc.world.getBlockState(holePos).getBlock();
                    updateHole(holeBlock, holePos);
                }
            }

            return;
        }

        // Just remove pos in that chunk
        int index = 0;
        while (index < holeList.size()) {
            BlockPos holePos = holeList.get(index).getPos();

            if (WorldUtil.isBlockPosInChunk(event.getChunk(), holePos)) {
                holeList.remove(index);
            } else {
                index++;
            }

        }
    }

    public void onCheckBlockInChunkEvent(CheckBlockInChunkEvent event) {
        BlockPos pos = event.getBlockPos();
        updateHole(mc.world.getBlockState(pos).getBlock(), pos);
    }

    public void onRenderWorldLast() {
        RenderManager rm = mc.getRenderManager();

        // DO NOT use enhanced for loop
        for (int i = 0; i < holeList.size(); i++) {
            Hole hole = holeList.get(i);

            // If radius is 0, don't apply restrictions
            if (radius.getValue() != 0) {
                if (mc.player.getDistanceSqToCenter(hole.getPos()) > radius.getValue() * radius.getValue()) {
                    continue;
                }
            }

            AxisAlignedBB holeBB = new AxisAlignedBB(hole.pos).offset(-rm.viewerPosX, -rm.viewerPosY, -rm.viewerPosZ);

            RenderGlobal.drawSelectionBoundingBox(holeBB, hole.getColorRGB()[0], hole.getColorRGB()[1], hole.getColorRGB()[2], 1F);
            RenderGlobal.renderFilledBox(holeBB, hole.getColorRGB()[0], hole.getColorRGB()[1], hole.getColorRGB()[2], 1/3F);
        }
    }

    private void removeHoleIfInList(BlockPos pos) {
        for (int i = 0; i < holeList.size(); i++) {
            if (holeList.get(i) == null) {
                continue;
            }

            if (holeList.get(i).getPos().equals(pos)) {
                holeList.remove(i);
                return;
            }
        }
    }

    private void updateHole(Block block, BlockPos pos) {
        Block blockUp = mc.world.getBlockState(pos.offset(EnumFacing.UP)).getBlock();
        final EnumFacing[] resistantFacings = {
                EnumFacing.DOWN, EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST
        };

        // Check if player fits in the hole
        if (block == Blocks.AIR && blockUp == Blocks.AIR) {
            int bedrock = 0;

            // Check if surrounding blocks are resistant
            for (EnumFacing facing : resistantFacings) {
                BlockPos offsetPos = pos.offset(facing);
                Block offsetBlock = mc.world.getBlockState(offsetPos).getBlock();

                if (offsetBlock == Blocks.BEDROCK) {
                    bedrock++;
                    continue;
                }

                if (offsetBlock == Blocks.OBSIDIAN) {
                    continue;
                }

                // Remove if on of the surrounding blocks is not resistant
                removeHoleIfInList(pos);
                return;
            }

            // Only add block if it is not already present in the Hole List
            for (Hole hole : holeList) {
                if (hole.getPos().equals(pos)) {
                    return;
                }
            }

            // Select color for hole
            float[] color;

            if (bedrock == 5) {
                color = new float[] {0F, 1F, 0F};
            } else {
                color = new float[] {1F, 1F, 0F};
            }

            holeList.add(new Hole(pos, color));

        } else {

            // Remove since a player no longer fits in the hole
            removeHoleIfInList(pos);
        }
    }

    private static class Hole {
        private final BlockPos pos;
        private final float[] colorRGB;

        public Hole(BlockPos pos, float[] colorRGB) {
            this.pos = pos;
            this.colorRGB = colorRGB;
        }

        public BlockPos getPos() {
            return pos;
        }

        public float[] getColorRGB() {
            return colorRGB;
        }
    }
}
