package com.thef1xer.gateclient.managers;

import com.thef1xer.gateclient.modules.EnumModuleCategory;
import com.thef1xer.gateclient.modules.Module;
import com.thef1xer.gateclient.modules.combat.Criticals;
import com.thef1xer.gateclient.modules.combat.KillAura;
import com.thef1xer.gateclient.modules.movement.Jesus;
import com.thef1xer.gateclient.modules.movement.NoFall;
import com.thef1xer.gateclient.modules.movement.Sprint;
import com.thef1xer.gateclient.modules.player.AutoArmor;
import com.thef1xer.gateclient.modules.combat.AutoTotem;
import com.thef1xer.gateclient.modules.player.Freecam;
import com.thef1xer.gateclient.modules.render.*;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    public List<Module> moduleList = new ArrayList<>();

    public void init() {
        //Combat
        moduleList.add(Criticals.INSTANCE);
        moduleList.add(KillAura.INSTANCE);
        //Movement
        moduleList.add(Jesus.INSTANCE);
        moduleList.add(NoFall.INSTANCE);
        moduleList.add(Sprint.INSTANCE);
        //Player
        moduleList.add(AutoArmor.INSTANCE);
        moduleList.add(AutoTotem.INSTANCE);
        moduleList.add(Freecam.INSTANCE);
        //Render
        moduleList.add(EntityESP.INSTANCE);
        moduleList.add(StorageESP.INSTANCE);
        moduleList.add(FullBright.INSTANCE);
        moduleList.add(HUDModule.INSTANCE);
        moduleList.add(Tracers.INSTANCE);
        moduleList.add(XRay.INSTANCE);
    }

    public List<Module> getModulesByCategory(EnumModuleCategory category) {
        List<Module> list = new ArrayList<>();
        for (Module module : moduleList) {
            if (module.getModuleCategory() == category) {
                list.add(module);
            }
        }
        return list;
    }
}
