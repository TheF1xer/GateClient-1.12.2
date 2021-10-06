package me.thef1xer.gateclient.managers;

import me.thef1xer.gateclient.modules.hud.Coords;
import me.thef1xer.gateclient.modules.hud.ModuleList;
import me.thef1xer.gateclient.modules.render.NoOverlay;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HUDManager {
    public void init() {
        ModuleList.INSTANCE.sortList();
    }

    @SubscribeEvent
    public void onOverlayRender(RenderGameOverlayEvent event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
            if (ModuleList.INSTANCE.isEnabled()) {
                ModuleList.INSTANCE.drawList(event.getResolution());
            }

            if (Coords.INSTANCE.isEnabled()) {
                Coords.INSTANCE.drawCoords(event.getResolution());
            }
        }

        if (NoOverlay.INSTANCE.isEnabled()) {
            if (event instanceof RenderGameOverlayEvent.Pre) {
                if (event.getType() == RenderGameOverlayEvent.ElementType.BOSSINFO || event.getType() == RenderGameOverlayEvent.ElementType.POTION_ICONS) {
                    event.setCanceled(false);
                    event.setCanceled(false);
                }
            }
        }
    }
}
