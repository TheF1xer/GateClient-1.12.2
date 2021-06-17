package com.thef1xer.gateclient.commands.impl;

import com.thef1xer.gateclient.commands.Command;
import com.thef1xer.gateclient.util.ChatUtil;
import com.thef1xer.gateclient.util.MathUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

public class ClipCommand extends Command {
    private final Minecraft mc = Minecraft.getMinecraft();

    public ClipCommand() {
        super("clip", "Allows you to clip in a direction", "clip <v/h> <blocks>", "clip <blocks horizontal> <blocks vertical>");
    }

    @Override
    public void onCommand(String[] args) {
        if (args.length == 3) {

            if (args[1].equalsIgnoreCase("h")) {

                try {
                    double distance = Double.parseDouble(args[2]);
                    double[] vector = MathUtil.getDirection(mc.player.rotationYaw);
                    Entity entity = mc.player.isRiding() ? mc.player.getRidingEntity() : mc.player;

                    if (entity == null) return;

                    entity.setPosition(entity.posX + vector[0] * distance, entity.posY, entity.posZ + vector[1] * distance);

                    ChatUtil.clientMessage("Teleported you " + args[2] + " blocks forward");

                } catch (Exception e) {
                    this.syntaxError();
                }

            } else if (args[1].equalsIgnoreCase("v")) {

                try {
                    double distance = Double.parseDouble(args[2]);
                    Entity entity = mc.player.isRiding() ? mc.player.getRidingEntity() : mc.player;

                    if (entity == null) return;

                    entity.setPosition(entity.posX, entity.posY + distance, entity.posZ);

                    ChatUtil.clientMessage("Teleported you " + args[2] + " blocks up");

                } catch (Exception e) {
                    this.syntaxError();
                }

            } else {

                try {
                    double dh = Double.parseDouble(args[1]);
                    double dv = Double.parseDouble(args[2]);
                    double[] vector = MathUtil.getDirection(mc.player.rotationYaw);
                    Entity entity = mc.player.isRiding() ? mc.player.getRidingEntity() : mc.player;

                    if (entity == null) return;

                    entity.setPosition(entity.posX + vector[0] * dh, entity.posY + dv, entity.posZ + vector[1] * dh);

                    ChatUtil.clientMessage("Teleported you " + args[1] + " blocks forward and " + args[2] + " blocks up");


                } catch (Exception e) {
                    this.syntaxError();
                }

            }
            return;
        }
        this.syntaxError();
    }
}
