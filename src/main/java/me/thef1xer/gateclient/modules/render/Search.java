package me.thef1xer.gateclient.modules.render;

import it.unimi.dsi.fastutil.objects.ObjectCollection;
import me.thef1xer.gateclient.events.SetBlockStateEvent;
import me.thef1xer.gateclient.modules.Module;
import me.thef1xer.gateclient.settings.impl.BlockListSetting;
import me.thef1xer.gateclient.settings.impl.BooleanSetting;
import me.thef1xer.gateclient.settings.impl.FloatSetting;
import me.thef1xer.gateclient.settings.impl.RGBSetting;
import me.thef1xer.gateclient.util.RenderUtil;
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
import net.minecraftforge.event.world.ChunkEvent;

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
            SearchBlocksInChunksThread searchBlocksInChunksThread = new SearchBlocksInChunksThread(
                    mc.world.getChunkProvider().chunkMapping.values(), searchedBlocks.getBlockList(), foundBlocksPos
            );
            System.out.println("Thread");
            searchBlocksInChunksThread.start();
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

    public void onLoadChunk(ChunkEvent.Load event) {
        SearchBlocksInChunksThread searchBlocksInChunksThread = new SearchBlocksInChunksThread(
                new Chunk[] {event.getChunk()}, searchedBlocks.getBlockList(), foundBlocksPos
        );
        System.out.println("Thread");
        searchBlocksInChunksThread.start();
    }

    public void onUnLoadChunk(ChunkEvent.Unload event) {
        // Search chunks when they are unloaded

        Chunk chunk = event.getChunk();
        int index = 0;

        while (index < foundBlocksPos.size()) {
            BlockPos foundPos = foundBlocksPos.get(index);

            if (mc.world.getChunkFromBlockCoords(foundPos) == chunk) {
                foundBlocksPos.remove(index);
            } else {
                index++;
            }
        }
    }

    public void onRenderWorldLast(RenderWorldLastEvent event) {
        RenderManager rm = mc.getRenderManager();

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();

        // Set color for tracer
        GlStateManager.color(color.getRed()/255F, color.getGreen()/255F, color.getBlue()/255F, alpha.getValue());

        boolean bobbing = mc.gameSettings.viewBobbing;

        for (BlockPos pos : foundBlocksPos) {

            if (tracer.getValue()) {

                // Cancel bobbing
                mc.gameSettings.viewBobbing = false;
                GlStateManager.loadIdentity();
                mc.entityRenderer.orientCamera(event.getPartialTicks());

                RenderUtil.drawTracerFromPlayer(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F);
            }

            if (box.getValue()) {

                // Re-add bobbing
                mc.gameSettings.viewBobbing = bobbing;
                GlStateManager.loadIdentity();
                mc.entityRenderer.orientCamera(event.getPartialTicks());

                AxisAlignedBB blockBB = new AxisAlignedBB(pos).offset(-rm.viewerPosX, -rm.viewerPosY, -rm.viewerPosZ);

                RenderGlobal.drawSelectionBoundingBox(blockBB, color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, alpha.getValue());
                RenderGlobal.renderFilledBox(blockBB, color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, alpha.getValue() / 3);
            }
        }

        // Restore previous bobbing setting
        mc.gameSettings.viewBobbing = bobbing;

        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    private static class SearchBlocksInChunksThread extends Thread {
        private final Chunk[] chunksToSearch;
        private final List<Block> searchedBlocks;
        private final List<BlockPos> foundBlocksPos;

        public SearchBlocksInChunksThread(Chunk[] chunksToSearch, List<Block> searchedBlocks, List<BlockPos> foundBlocksPos) {
            this.chunksToSearch = chunksToSearch;
            this.searchedBlocks = searchedBlocks;
            this.foundBlocksPos = foundBlocksPos;
        }

        public SearchBlocksInChunksThread(ObjectCollection<Chunk> chunksToSearchCollection, List<Block> searchedBlocks, List<BlockPos> foundBlocksPos) {
            Chunk[] chunksToSearch = new Chunk[chunksToSearchCollection.size()];
            chunksToSearchCollection.toArray(chunksToSearch);

            this.chunksToSearch = chunksToSearch;
            this.searchedBlocks = searchedBlocks;
            this.foundBlocksPos = foundBlocksPos;
        }

        @Override
        public void run() {
            for (Chunk chunk : chunksToSearch) {

                // Loop through every block and add them if they are being searched
                for (int x = chunk.getPos().getXStart(); x <= chunk.getPos().getXEnd(); x++) {
                    for (int y = 1; y <= 256; y++) {
                        for (int z = chunk.getPos().getZStart(); z <= chunk.getPos().getZEnd(); z++) {

                            BlockPos pos = new BlockPos(x, y, z);

                            if (searchedBlocks.contains(chunk.getBlockState(pos).getBlock())) {
                                foundBlocksPos.add(pos);
                            }
                        }
                    }
                }
            }
        }
    }
}
