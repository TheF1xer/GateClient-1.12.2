package me.thef1xer.gateclient.modules.player;

import com.mojang.authlib.GameProfile;
import me.thef1xer.gateclient.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FakePlayer extends Module {
    public static FakePlayer INSTANCE = new FakePlayer();

    private EntityOtherPlayerMP fakePlayer;

    private final Minecraft mc = Minecraft.getMinecraft();

    public FakePlayer() {
        super("Fake Player", "fakeplayer", ModuleCategory.PLAYER);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        MinecraftForge.EVENT_BUS.register(this);

        // Disable module if not in-game
        if (mc.player == null || mc.world == null) {
            setEnabled(false);
            return;
        }

        // Spawn Fake Player
        fakePlayer = new EntityOtherPlayerMP(mc.world, new GameProfile(mc.player.getUniqueID(), "TheF1xer"));

        fakePlayer.copyLocationAndAnglesFrom(mc.player);
        fakePlayer.inventory.copyInventory(mc.player.inventory);
        fakePlayer.setHealth(mc.player.getHealth());
        fakePlayer.setAbsorptionAmount(mc.player.getAbsorptionAmount());

        mc.world.addEntityToWorld(1333333337, fakePlayer);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        MinecraftForge.EVENT_BUS.unregister(this);

        if (fakePlayer != null) {
            mc.world.removeEntity(fakePlayer);
        }
    }

    @SubscribeEvent
    public void onUnloadWorld(WorldEvent.Unload event) {
        setEnabled(false);
    }
}
