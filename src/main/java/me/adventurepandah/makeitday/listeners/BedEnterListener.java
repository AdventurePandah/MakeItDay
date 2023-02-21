package me.adventurepandah.makeitday.listeners;

import me.adventurepandah.makeitday.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;

public class BedEnterListener implements Listener {
    private final Main plugin = Main.getInstance();

    @EventHandler
    public void onPlayerBedEnter(PlayerBedEnterEvent e) {
        Bukkit.getServer().getScheduler().runTaskLater(plugin, () -> {
            if (e.getPlayer().isSleeping() && e.getPlayer().getWorld().getTime() >= 13000L && e.getPlayer().hasPermission("makeitday.day")) {
                e.getPlayer().getWorld().setTime(0L);
                e.getPlayer().getWorld().setStorm(false);
                e.getPlayer().getWorld().setThundering(false);
                if(plugin.getConfig().getBoolean("broadcast")) {
                    for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                        onlinePlayers.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("broadcastMsg")).replace("%player%", e.getPlayer().getName()));
                    }
                }
            }
        }, plugin.getConfig().getInt("playerSleepTime") * 20);
    }

}
