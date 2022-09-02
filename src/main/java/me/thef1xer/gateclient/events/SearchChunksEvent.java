package me.thef1xer.gateclient.events;

import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class SearchChunksEvent extends Event {
    // If this event is canceled, the client thread will loop through the newly loaded chunks and check every
    // BlockPos, only cancel it if a module needs it

}
