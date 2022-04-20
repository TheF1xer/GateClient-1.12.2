package me.thef1xer.gateclient.commands.impl;

import me.thef1xer.gateclient.GateClient;
import me.thef1xer.gateclient.commands.Command;
import me.thef1xer.gateclient.modules.render.XRay;
import me.thef1xer.gateclient.util.ChatUtil;
import net.minecraft.block.Block;
import net.minecraft.util.text.TextFormatting;

public class XrayCommand extends Command {
    public XrayCommand() {
        super("xray", "Interacts with the XRay module", "xray add <block>", "xray remove <block>", "xray list");
    }

    @Override
    public void onCommand(String[] args) {

        if (args.length == 2) {

            // xray list
            if (args[1].equalsIgnoreCase("list")) {
                ChatUtil.clientMessage(TextFormatting.BOLD + "XRay Blocks List:");

                for (Block block : XRay.INSTANCE.XRAY_BLOCKS) {
                    ChatUtil.clientMessage(TextFormatting.GOLD + block.getLocalizedName() + TextFormatting.WHITE + " (" + Block.REGISTRY.getNameForObject(block) + ")");
                }
                return;
            }

            syntaxError();
            return;
        }

        if (args.length == 3) {

            // xray add <block>
            if (args[1].equalsIgnoreCase("add")) {

                Block newBlock = Block.getBlockFromName(args[2]);

                if (newBlock != null) {
                    XRay.INSTANCE.XRAY_BLOCKS.add(newBlock);
                    ChatUtil.clientMessage(TextFormatting.GOLD + newBlock.getLocalizedName() + TextFormatting.WHITE + " added to the XRay Block List");

                    if (GateClient.getGate().presetManager.isAutoSave()) {
                        GateClient.getGate().presetManager.saveActivePreset();
                    }

                    return;
                }

                ChatUtil.clientMessage("That block does not exist");
                return;
            }

            // xray remove <block>
            if (args[1].equalsIgnoreCase("remove")) {
                Block blockToRemove = Block.getBlockFromName(args[2]);

                if (blockToRemove == null) {
                    ChatUtil.clientMessage("That block does not exist");
                    return;
                }

                for (Block block : XRay.INSTANCE.XRAY_BLOCKS) {
                    if (block == blockToRemove) {

                        XRay.INSTANCE.XRAY_BLOCKS.remove(block);
                        ChatUtil.clientMessage(TextFormatting.GOLD + blockToRemove.getLocalizedName() + TextFormatting.WHITE + " removed from the XRay Block List");

                        if (GateClient.getGate().presetManager.isAutoSave()) {
                            GateClient.getGate().presetManager.saveActivePreset();
                        }

                        return;
                    }
                }

                ChatUtil.clientMessage(TextFormatting.GOLD + blockToRemove.getLocalizedName() + TextFormatting.WHITE + " is not in the XRay Block List");
                return;
            }
        }

        syntaxError();
    }
}
