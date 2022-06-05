package me.thef1xer.gateclient.commands.impl;

import me.thef1xer.gateclient.GateClient;
import me.thef1xer.gateclient.commands.Command;
import me.thef1xer.gateclient.modules.Module;
import me.thef1xer.gateclient.util.ChatUtil;

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
        for (Module module : GateClient.getGate().MODULE_MANAGER.MODULE_LIST) {
            if (module.getId().equalsIgnoreCase(name)) {
                module.toggle();
                ChatUtil.clientMessage(module.getName() + " module toggled");
                if (GateClient.getGate().PRESET_MANAGER.isAutoSave()) {
                    GateClient.getGate().PRESET_MANAGER.saveActivePreset();
                }
                return true;
            }
        }
        return false;
    }
}
