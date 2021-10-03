package me.thef1xer.gateclient.gui.hud;

import me.thef1xer.gateclient.gui.hud.components.CoordsComponent;
import me.thef1xer.gateclient.gui.hud.components.ModuleListComponent;
import me.thef1xer.gateclient.modules.render.NoOverlay;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HUD {
    public ModuleListComponent modulesComponent = new ModuleListComponent();
    public CoordsComponent coordsComponent = new CoordsComponent();

    public void init() {
        modulesComponent.init();
    }

    @SubscribeEvent
    public void onOverlayRender(RenderGameOverlayEvent event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
            modulesComponent.renderComponent(event.getResolution());
            coordsComponent.renderComponent(event.getResolution());
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
