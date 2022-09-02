package me.thef1xer.gateclient.commands.impl;

import me.thef1xer.gateclient.GateClient;
import me.thef1xer.gateclient.commands.Command;
import me.thef1xer.gateclient.modules.Module;
import me.thef1xer.gateclient.util.ChatUtil;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Keyboard;

public class ModuleListCommand extends Command {
    public ModuleListCommand() {
        super("modulelist", "Shows the name, id and keybind of every Module in the Client", "modulelist");
        this.addAliases("list", "modules");
    }

    @Override
    public void onCommand(String[] args) {
        ChatUtil.clientMessage(TextFormatting.BOLD + "Module List:");
        for (Module module : GateClient.getGate().moduleManager.moduleList) {
            ChatUtil.clientMessage(TextFormatting.GOLD + module.getName() + ": " + TextFormatting.RESET + module.getId() + " [" + Keyboard.getKeyName(module.getKeyBind()) + "]");
        }
    }
}
