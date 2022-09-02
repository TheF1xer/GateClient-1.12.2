package me.thef1xer.gateclient.managers;

import me.thef1xer.gateclient.modules.Module;
import me.thef1xer.gateclient.modules.combat.*;
import me.thef1xer.gateclient.modules.hud.*;
import me.thef1xer.gateclient.modules.player.*;
import me.thef1xer.gateclient.modules.movement.*;
import me.thef1xer.gateclient.modules.render.*;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    public final List<Module> moduleList = new ArrayList<>();

    public void init() {
        // Combat
        moduleList.add(AutoTotem.INSTANCE);
        moduleList.add(Criticals.INSTANCE);
        moduleList.add(CrystalAura.INSTANCE);
        moduleList.add(KillAura.INSTANCE);
        moduleList.add(Surround.INSTANCE);

        // HUD
        moduleList.add(ArmorHUD.INSTANCE);
        moduleList.add(ClickGuiModule.INSTANCE);
        moduleList.add(Coords.INSTANCE);
        moduleList.add(ModuleList.INSTANCE);
        moduleList.add(Watermark.INSTANCE);

        // Movement
        moduleList.add(Flight.INSTANCE);
        moduleList.add(GuiMove.INSTANCE);
        moduleList.add(Jesus.INSTANCE);
        moduleList.add(NoFall.INSTANCE);
        moduleList.add(NoSlow.INSTANCE);
        moduleList.add(SafeWalk.INSTANCE);
        moduleList.add(Speed.INSTANCE);
        moduleList.add(Sprint.INSTANCE);
        moduleList.add(Velocity.INSTANCE);

        // Player
        moduleList.add(AntiHunger.INSTANCE);
        moduleList.add(AutoArmor.INSTANCE);
        moduleList.add(AutoDisconnect.INSTANCE);
        moduleList.add(FakePlayer.INSTANCE);
        moduleList.add(Freecam.INSTANCE);
        moduleList.add(Notifications.INSTANCE);
        moduleList.add(Scaffold.INSTANCE);

        // Render
        moduleList.add(EntityESP.INSTANCE);
        moduleList.add(StorageESP.INSTANCE);
        moduleList.add(FullBright.INSTANCE);
        moduleList.add(HoleESP.INSTANCE);
        moduleList.add(Nametags.INSTANCE);
        moduleList.add(NoOverlay.INSTANCE);
        moduleList.add(Search.INSTANCE);
        moduleList.add(ShulkerViewer.INSTANCE);
        moduleList.add(Tracers.INSTANCE);
        moduleList.add(XRay.INSTANCE);


        ModuleList.INSTANCE.sortList();
    }
}
