package me.adventurepandah.makeitday.listeners;

import me.adventurepandah.makeitday.Main;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;

import java.io.IOException;

public class BedEnterListener implements Listener {
    private final Main plugin = Main.getInstance();

    public static int sleepingPlayerAmount = 0;

    @EventHandler
    public void onPlayerBedEnter(PlayerBedEnterEvent e) {
        if (Integer.parseInt(plugin.minorVer) >= 11) {
            Bukkit.getServer().getScheduler().runTask(plugin, () -> {
                if(e.getPlayer().isSleeping()) {
                    e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(""));
                }
            });
        }

        Bukkit.getServer().getScheduler().runTaskLater(plugin, () -> {
            if(e.getPlayer().isSleeping() && e.getPlayer().getWorld().getTime() >= 13000L && e.getPlayer().hasPermission("makeitday.day")){
                int amountOnlinePlayers = Bukkit.getOnlinePlayers().size();

                sleepingPlayerAmount++;
                if (!plugin.getConfig().getBoolean("usePercentage")) {
                    if (sleepingPlayerAmount >= plugin.getConfig().getInt("amountOfSleepingPlayers")) {
                        makeItDay(e.getPlayer().getWorld(), e.getPlayer());
                    } else if(amountOnlinePlayers < plugin.getConfig().getInt("amountOfSleepingPlayers")) {
                        if(sleepingPlayerAmount == amountOnlinePlayers) {
                            makeItDay(e.getPlayer().getWorld(), e.getPlayer());
                        } else {
                            for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                                onlinePlayers.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("amountPlayerSleepMsg")).replace("%amountPlayers%", String.valueOf(sleepingPlayerAmount)).replace("%neededPlayers%", String.valueOf(amountOnlinePlayers)));
                            }
                        }
                    } else {
                        if(plugin.getConfig().getBoolean("broadcast")){
                            for(Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                                onlinePlayers.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("amountPlayerSleepMsg")).replace("%amountPlayers%", String.valueOf(sleepingPlayerAmount)).replace("%neededPlayers%", String.valueOf(plugin.getConfig().getInt("amountOfSleepingPlayers"))));
                            }
                        }
                    }
                } else {
                    int percentageOfSleepingPlayers = sleepingPlayerAmount*100/amountOnlinePlayers;

                    if(percentageOfSleepingPlayers >= plugin.getConfig().getInt("percentageOfSleepingPlayers")) {
                        makeItDay(e.getPlayer().getWorld(), e.getPlayer());
                    } else {
                        if(plugin.getConfig().getBoolean("broadcast")){
                            for(Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                                onlinePlayers.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("amountPlayerSleepPercentageMsg")).replace("%currentPercentage%", String.valueOf(percentageOfSleepingPlayers)).replace("%neededPercentage%", String.valueOf(plugin.getConfig().getInt("percentageOfSleepingPlayers"))));
                            }
                        }
                    }

                }
            }
        }, plugin.getConfig().getInt("playerSleepTime") * 20);
    }

    public void makeItDay(World world, Player player) {
        if(player.isSleeping()) {
            world.setTime(0L);
            world.setStorm(false);
            world.setThundering(false);
            if (plugin.getConfig().getBoolean("broadcast")) {
                for(Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                    onlinePlayers.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("broadcastMsg")).replace("%player%", player.getName()));
                }
            }
        }
    }
}
