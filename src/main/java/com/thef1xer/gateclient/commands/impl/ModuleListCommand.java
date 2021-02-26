package com.thef1xer.gateclient.commands.impl;

import com.thef1xer.gateclient.GateClient;
import com.thef1xer.gateclient.commands.Command;
import com.thef1xer.gateclient.modules.Module;
import com.thef1xer.gateclient.util.ChatUtil;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Keyboard;

public class ModuleListCommand extends Command {
    public ModuleListCommand() {
        super("modulelist", "Shows the name, id and keybind of every Module in the Client", "modulelist");
        this.addAliases("list", "modules");
    }

    @Override
    public void onCommand(String[] args) {
        for (Module module : GateClient.gate.moduleManager.moduleList) {
            ChatUtil.clientMessage(TextFormatting.GOLD + module.getName() + ": " + TextFormatting.RESET + module.getId() + " [" + Keyboard.getKeyName(module.getKeyBind()) + "]");
        }
    }
}
