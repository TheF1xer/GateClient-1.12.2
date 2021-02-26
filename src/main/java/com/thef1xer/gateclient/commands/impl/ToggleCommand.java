package com.thef1xer.gateclient.commands.impl;

import com.thef1xer.gateclient.GateClient;
import com.thef1xer.gateclient.commands.Command;
import com.thef1xer.gateclient.modules.Module;
import com.thef1xer.gateclient.util.ChatUtil;

public class ToggleCommand extends Command {
    public ToggleCommand() {
        super("toggle", "Toggles a Module", "toggle <module>");
        this.addAliases("t");
    }

    @Override
    public void onCommand(String[] args) {
        if (args.length != 2) {
            this.syntaxError();
            return;
        }

        if (!isModule(args[1])) {
            ChatUtil.clientMessage("Module not found");
        }
    }

    public boolean isModule(String name) {
        for (Module module : GateClient.gate.moduleManager.moduleList) {
            if (module.getId().equalsIgnoreCase(name)) {
                module.toggle();
                ChatUtil.clientMessage(module.getName() + " module toggled");
                return true;
            }
        }
        return false;
    }
}
