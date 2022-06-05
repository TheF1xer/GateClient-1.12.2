package me.thef1xer.gateclient.commands.impl;

import me.thef1xer.gateclient.GateClient;
import me.thef1xer.gateclient.commands.Command;
import me.thef1xer.gateclient.util.ChatUtil;
import net.minecraft.util.text.TextFormatting;

public class FriendsCommand extends Command {
    private final GateClient gate = GateClient.getGate();

    public FriendsCommand() {
        super("friends", "Manages friends", "friends add <name>", "friends remove <name>", "friends list");
    }

    @Override
    public void onCommand(String[] args) {
        if (args.length == 2) {

            // friends list
            if (args[1].equalsIgnoreCase("list")) {
                ChatUtil.clientMessage(TextFormatting.BOLD + "Friend List:");

                for (String friendName : gate.FRIENDS_MANAGER.FRIENDS_NAME_LIST) {
                    ChatUtil.clientMessage(friendName);
                }

                return;
            }

            syntaxError();
            return;
        }

        if (args.length == 3) {

            // friends add <name>
            if (args[1].equalsIgnoreCase("add")) {

                // Check if player is already in the list
                if (isFriend(args[2])) {
                    ChatUtil.clientMessage(TextFormatting.GOLD + args[2] + TextFormatting.WHITE + " is already in the Friends List");
                    return;
                }

                gate.FRIENDS_MANAGER.FRIENDS_NAME_LIST.add(args[2]);
                gate.FRIENDS_MANAGER.saveFriendList();

                ChatUtil.clientMessage(TextFormatting.GOLD + args[2] + TextFormatting.WHITE + " added to the Friends List");

                return;
            }

            // friends remove <name>
            if (args[1].equalsIgnoreCase("remove")) {

                // This makes it not case-sensitive
                for (String friendName : gate.FRIENDS_MANAGER.FRIENDS_NAME_LIST) {

                    // Remove if friend was found
                    if (friendName.equalsIgnoreCase(args[2])) {
                        gate.FRIENDS_MANAGER.FRIENDS_NAME_LIST.remove(friendName);
                        gate.FRIENDS_MANAGER.saveFriendList();

                        ChatUtil.clientMessage(TextFormatting.GOLD + args[2] + TextFormatting.WHITE + " removed from the Friends List");
                        return;
                    }
                }

                ChatUtil.clientMessage(TextFormatting.GOLD + args[2] + TextFormatting.WHITE + " is not in the Friends List");
                return;
            }

            syntaxError();
            return;
        }

        syntaxError();
    }

    private boolean isFriend(String name) {
        for (String friendName : gate.FRIENDS_MANAGER.FRIENDS_NAME_LIST) {
            if (friendName.equalsIgnoreCase(name)) {
                return true;
            }
        }

        return false;
    }
}
