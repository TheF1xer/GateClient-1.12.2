package com.thef1xer.gateclient.managers;

import com.thef1xer.gateclient.modules.Module;
import com.thef1xer.gateclient.modules.combat.Criticals;
import com.thef1xer.gateclient.modules.combat.KillAura;
import com.thef1xer.gateclient.modules.hud.ClickGuiModule;
import com.thef1xer.gateclient.modules.hud.Coords;
import com.thef1xer.gateclient.modules.hud.ModuleList;
import com.thef1xer.gateclient.modules.movement.*;
import com.thef1xer.gateclient.modules.player.AutoArmor;
import com.thef1xer.gateclient.modules.combat.AutoTotem;
import com.thef1xer.gateclient.modules.player.Freecam;
import com.thef1xer.gateclient.modules.render.*;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    public final List<Module> MODULE_LIST = new ArrayList<>();

    public void init() {
        //Combat
        MODULE_LIST.add(AutoTotem.INSTANCE);
        MODULE_LIST.add(Criticals.INSTANCE);
        MODULE_LIST.add(KillAura.INSTANCE);
        //HUD
        MODULE_LIST.add(ClickGuiModule.INSTANCE);
        MODULE_LIST.add(Coords.INSTANCE);
        MODULE_LIST.add(ModuleList.INSTANCE);
        //Movement
        MODULE_LIST.add(GuiMove.INSTANCE);
        MODULE_LIST.add(Jesus.INSTANCE);
        MODULE_LIST.add(NoFall.INSTANCE);
        MODULE_LIST.add(NoSlow.INSTANCE);
        MODULE_LIST.add(SafeWalk.INSTANCE);
        MODULE_LIST.add(Speed.INSTANCE);
        MODULE_LIST.add(Sprint.INSTANCE);
        //Player
        MODULE_LIST.add(AutoArmor.INSTANCE);
        MODULE_LIST.add(Freecam.INSTANCE);
        //Render
        MODULE_LIST.add(EntityESP.INSTANCE);
        MODULE_LIST.add(StorageESP.INSTANCE);
        MODULE_LIST.add(FullBright.INSTANCE);
        MODULE_LIST.add(NoOverlay.INSTANCE);
        MODULE_LIST.add(Tracers.INSTANCE);
        MODULE_LIST.add(XRay.INSTANCE);
    }
}
