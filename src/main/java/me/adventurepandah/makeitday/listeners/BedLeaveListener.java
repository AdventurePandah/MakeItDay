package me.adventurepandah.makeitday.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedLeaveEvent;

public class BedLeaveListener implements Listener {

    @EventHandler
    public void onBedLeaveEvent(PlayerBedLeaveEvent e) {
        if(BedEnterListener.sleepingPlayerAmount > 0 && e.getPlayer().hasPermission("makeitday.day")) {
            BedEnterListener.sleepingPlayerAmount--;
        }
    }
}
