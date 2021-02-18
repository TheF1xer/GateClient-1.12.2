package com.thef1xer.gateclient.commands;

import com.thef1xer.gateclient.GateClient;
import com.thef1xer.gateclient.modules.Module;
import com.thef1xer.gateclient.util.ChatUtil;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Keyboard;

public class BindCommand extends Command {

    public BindCommand() {
        super("bind", "Binds a module to a key", "bind <module> <key> | clear | list", new String[]{"b"});
    }

    @Override
    public void onCommand(String[] args) {
        //Maybe future rework?

        if (args.length == 2) {
            if (args[1].equalsIgnoreCase("clear")) {
                for (Module module : GateClient.gate.moduleManager.moduleList) {
                    module.setKeyBind(Keyboard.KEY_NONE);
                }
                ChatUtil.clientMessage("Key Binds cleared");
                return;
            }

            if (args[1].equalsIgnoreCase("list")) {
                ChatUtil.clientMessage("Key Bind List:");
                for (Module module : GateClient.gate.moduleManager.moduleList) {
                    if (module.getKeyBind() != Keyboard.KEY_NONE) {
                        ChatUtil.userMessage(TextFormatting.GRAY + module.getName() + " is bound to " + Keyboard.getKeyName(module.getKeyBind()));
                    }
                }
                return;
            }

            for (Module module : GateClient.gate.moduleManager.moduleList) {
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

        for (Module module : GateClient.gate.moduleManager.moduleList) {
            if (module.getId().equalsIgnoreCase(args[1])) {
                module.setKeyBind(Keyboard.getKeyIndex(args[2].toUpperCase()));
                ChatUtil.clientMessage(module.getName() + " bound to " + args[2].toUpperCase());
                return;
            }
        }

        this.syntaxError();
    }
}
