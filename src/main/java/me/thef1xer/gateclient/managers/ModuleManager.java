package me.thef1xer.gateclient.managers;

import me.thef1xer.gateclient.modules.Module;
import me.thef1xer.gateclient.modules.combat.Criticals;
import me.thef1xer.gateclient.modules.combat.KillAura;
import me.thef1xer.gateclient.modules.combat.Surround;
import me.thef1xer.gateclient.modules.hud.*;
import me.thef1xer.gateclient.modules.player.AutoArmor;
import me.thef1xer.gateclient.modules.combat.AutoTotem;
import me.thef1xer.gateclient.modules.player.AutoDisconnect;
import me.thef1xer.gateclient.modules.player.Freecam;
import me.thef1xer.gateclient.modules.movement.*;
import me.thef1xer.gateclient.modules.player.Scaffold;
import me.thef1xer.gateclient.modules.render.*;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    public final List<Module> MODULE_LIST = new ArrayList<>();

    public void init() {
        // Combat
        MODULE_LIST.add(AutoTotem.INSTANCE);
        MODULE_LIST.add(Criticals.INSTANCE);
        MODULE_LIST.add(KillAura.INSTANCE);
        MODULE_LIST.add(Surround.INSTANCE);
        // HUD
        MODULE_LIST.add(ArmorHUD.INSTANCE);
        MODULE_LIST.add(ClickGuiModule.INSTANCE);
        MODULE_LIST.add(Coords.INSTANCE);
        MODULE_LIST.add(ModuleList.INSTANCE);
        MODULE_LIST.add(Watermark.INSTANCE);
        // Movement
        MODULE_LIST.add(Flight.INSTANCE);
        MODULE_LIST.add(GuiMove.INSTANCE);
        MODULE_LIST.add(Jesus.INSTANCE);
        MODULE_LIST.add(NoFall.INSTANCE);
        MODULE_LIST.add(NoSlow.INSTANCE);
        MODULE_LIST.add(SafeWalk.INSTANCE);
        MODULE_LIST.add(Speed.INSTANCE);
        MODULE_LIST.add(Sprint.INSTANCE);
        MODULE_LIST.add(Velocity.INSTANCE);
        // Player
        MODULE_LIST.add(AutoArmor.INSTANCE);
        MODULE_LIST.add(AutoDisconnect.INSTANCE);
        MODULE_LIST.add(Freecam.INSTANCE);
        MODULE_LIST.add(Scaffold.INSTACE);
        // Render
        MODULE_LIST.add(EntityESP.INSTANCE);
        MODULE_LIST.add(StorageESP.INSTANCE);
        MODULE_LIST.add(FullBright.INSTANCE);
        MODULE_LIST.add(Nametags.INSTANCE);
        MODULE_LIST.add(NoOverlay.INSTANCE);
        MODULE_LIST.add(ShulkerViewer.INSTANCE);
        MODULE_LIST.add(Tracers.INSTANCE);
        MODULE_LIST.add(XRay.INSTANCE);
    }
}
