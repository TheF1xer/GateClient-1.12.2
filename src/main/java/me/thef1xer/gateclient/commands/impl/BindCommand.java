package me.thef1xer.gateclient.commands.impl;

import me.thef1xer.gateclient.GateClient;
import me.thef1xer.gateclient.commands.Command;
import me.thef1xer.gateclient.modules.Module;
import me.thef1xer.gateclient.util.ChatUtil;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Keyboard;

public class BindCommand extends Command {

    public BindCommand() {
        super("bind", "Binds a module to a key", "bind <module> <key>", "bind <module>", "bind clear", "bind list");
        this.addAliases("b");
    }

    @Override
    public void onCommand(String[] args) {
        //Maybe future rework?

        if (args.length == 2) {
            if (args[1].equalsIgnoreCase("clear")) {
                for (Module module : GateClient.getGate().MODULE_MANAGER.MODULE_LIST) {
                    module.setKeyBind(Keyboard.KEY_NONE);
                }
                ChatUtil.clientMessage("Key Binds cleared");
                if (GateClient.getGate().PRESET_MANAGER.isAutoSave()) {
                    GateClient.getGate().PRESET_MANAGER.saveActivePreset();
                }
                return;
            }

            if (args[1].equalsIgnoreCase("list")) {
                ChatUtil.clientMessage(TextFormatting.BOLD + "Key Bind List:");
                for (Module module : GateClient.getGate().MODULE_MANAGER.MODULE_LIST) {
                    if (module.getKeyBind() != Keyboard.KEY_NONE) {
                        ChatUtil.clientMessage(module.getName() + " is bound to " + Keyboard.getKeyName(module.getKeyBind()));
                    }
                }
                return;
            }

            for (Module module : GateClient.getGate().MODULE_MANAGER.MODULE_LIST) {
                if (module.getId().equalsIgnoreCase(args[1].toUpperCase())) {
                    ChatUtil.clientMessage(module.getName() + " is bound to " + Keyboard.getKeyName(module.getKeyBind()));
                    return;
                }
            }
        }

        if (args.length != 3 || args[2].length() != 1) {
            this.syntaxError();
            return;
        }

        for (Module module : GateClient.getGate().MODULE_MANAGER.MODULE_LIST) {
            if (module.getId().equalsIgnoreCase(args[1])) {
                module.setKeyBind(Keyboard.getKeyIndex(args[2].toUpperCase()));
                ChatUtil.clientMessage(module.getName() + " bound to " + args[2].toUpperCase());
                if (GateClient.getGate().PRESET_MANAGER.isAutoSave()) {
                    GateClient.getGate().PRESET_MANAGER.saveActivePreset();
                }
                return;
            }
        }

        this.syntaxError();
    }
}
