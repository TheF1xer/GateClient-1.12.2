package me.thef1xer.gateclient.modules.render;

import me.thef1xer.gateclient.GateClient;
import me.thef1xer.gateclient.events.CheckBlockInChunkEvent;
import me.thef1xer.gateclient.events.CheckChunkEvent;
import me.thef1xer.gateclient.events.SetBlockStateEvent;
import me.thef1xer.gateclient.modules.Module;
import me.thef1xer.gateclient.settings.impl.BlockListSetting;
import me.thef1xer.gateclient.settings.impl.BooleanSetting;
import me.thef1xer.gateclient.settings.impl.FloatSetting;
import me.thef1xer.gateclient.settings.impl.RGBSetting;
import me.thef1xer.gateclient.util.RenderUtil;
import me.thef1xer.gateclient.util.WorldUtil;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import java.util.ArrayList;
import java.util.List;

public class Search extends Module {
    public static final Search INSTANCE = new Search();

    public final BlockListSetting searchedBlocks = new BlockListSetting("Search Blocks", "searchblocks", new Block[]{
            Blocks.PORTAL, Blocks.END_PORTAL
    });
    public final BooleanSetting box = new BooleanSetting("Box", "box", true);
    public final BooleanSetting tracer = new BooleanSetting("Tracer", "tracer", true);
    public final RGBSetting color = new RGBSetting("Color", "color", 255, 255, 255);
    public final FloatSetting alpha = new FloatSetting("Alpha", "alpha", 1F, 0F, 1F);

    private final List<BlockPos> foundBlocksPos = new ArrayList<>();
    private final Minecraft mc = Minecraft.getMinecraft();

    public Search() {
        super("Search", "search", ModuleCategory.RENDER);

        addSettings(searchedBlocks, box, tracer, color, alpha);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        foundBlocksPos.clear();

        if (mc.world != null) {
            GateClient.getGate().threadManager.chunksToCheck.addAll(mc.world.getChunkProvider().chunkMapping.values());
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        foundBlocksPos.clear();
    }

    public void onSetBlockState(SetBlockStateEvent event) {
        Block block = event.getBlockState().getBlock();
        BlockPos pos = event.getBlockPos();

        // Add Block
        if (searchedBlocks.getBlockList().contains(block) && !foundBlocksPos.contains(pos)) {
            foundBlocksPos.add(pos);
            return;
        }

        // Remove Block
        if (foundBlocksPos.contains(pos) && !searchedBlocks.getBlockList().contains(block)) {
            foundBlocksPos.remove(pos);
        }
    }

    public void onCheckBlockInChunkEvent(CheckBlockInChunkEvent event) {
        BlockPos pos = event.getBlockPos();
        Chunk chunk = event.getChunk();

        if (searchedBlocks.getBlockList().contains(mc.world.getBlockState(pos).getBlock()) && !foundBlocksPos.contains(pos)) {
            foundBlocksPos.add(pos);
        }
    }

    public void onCheckChunk(CheckChunkEvent event) {
        if (event.isChunkLoaded()) {

            // Check if block is still there
            int index = 0;
            while (index < foundBlocksPos.size()) {
                BlockPos foundPos = foundBlocksPos.get(index);

                if (WorldUtil.isBlockPosInChunk(event.getChunk(), foundPos)) {
                    if (!searchedBlocks.getBlockList().contains(mc.world.getBlockState(foundPos).getBlock())) {
                        foundBlocksPos.remove(index);
                    }
                } else {
                    index++;
                }

            }

            return;
        }

        // Just remove pos in that chunk
        int index = 0;
        while (index < foundBlocksPos.size()) {
            BlockPos foundPos = foundBlocksPos.get(index);

            if (WorldUtil.isBlockPosInChunk(event.getChunk(), foundPos)) {
                foundBlocksPos.remove(index);
            } else {
                index++;
            }

        }
    }

    public void onRenderWorldLast(RenderWorldLastEvent event) {
        RenderManager rm = mc.getRenderManager();

        // Set color for tracer
        GlStateManager.color(color.getRed()/255F, color.getGreen()/255F, color.getBlue()/255F, alpha.getValue());

        boolean bobbing = mc.gameSettings.viewBobbing;

        // DO NOT use enhanced for loop
        for (int i = 0; i < foundBlocksPos.size(); i++) {
            BlockPos pos = foundBlocksPos.get(i);

            GlStateManager.pushMatrix();
            if (tracer.getValue()) {

                // Cancel bobbing
                mc.gameSettings.viewBobbing = false;
                GlStateManager.loadIdentity();
                mc.entityRenderer.orientCamera(event.getPartialTicks());

                RenderUtil.drawTracerFromPlayer(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F);
            }
            GlStateManager.popMatrix();

            if (box.getValue()) {
                AxisAlignedBB blockBB = new AxisAlignedBB(pos).offset(-rm.viewerPosX, -rm.viewerPosY, -rm.viewerPosZ);

                RenderGlobal.drawSelectionBoundingBox(blockBB, color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, alpha.getValue());
                RenderGlobal.renderFilledBox(blockBB, color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, alpha.getValue() / 3);
            }
        }

        // Restore previous bobbing setting
        mc.gameSettings.viewBobbing = bobbing;
    }
}
