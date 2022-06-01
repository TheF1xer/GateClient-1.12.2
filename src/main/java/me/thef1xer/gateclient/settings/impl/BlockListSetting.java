package me.thef1xer.gateclient.settings.impl;

import me.thef1xer.gateclient.settings.Setting;
import net.minecraft.block.Block;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlockListSetting extends Setting {
    private final List<Block> blockList = new ArrayList<>();

    public BlockListSetting(String name, String id, Block[] defaultBlocks) {
        super(name, id);
        blockList.addAll(Arrays.asList(defaultBlocks));
    }

    public List<Block> getBlockList() {
        return blockList;
    }

    @Override
    public String getCommandSyntax() {
        return TextFormatting.GOLD.toString() + TextFormatting.ITALIC + getName() + ": " +
                TextFormatting.RESET + getId() + " <add / remove / list> <block id to add or remove>";
    }
}
