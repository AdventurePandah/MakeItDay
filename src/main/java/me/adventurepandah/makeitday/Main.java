package me.adventurepandah.makeitday;

import me.adventurepandah.makeitday.listeners.BedEnterListener;
import me.adventurepandah.makeitday.listeners.BedLeaveListener;
import me.adventurepandah.makeitday.managers.CommandManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public final class Main extends JavaPlugin {

    private static Main instance;

    public CommandManager commandManager;

    public static Main getInstance() {
        return instance;
    }

    public static void setInstance(Main instance) {
        Main.instance = instance;
    }

    PluginManager pm = getServer().getPluginManager();

    @Override
    public void onEnable() {
        // Plugin startup logic
        setInstance(this);
        versionChecker();
        loadConfig();

        commandManager = new CommandManager();
        commandManager.setup();

        pm.registerEvents(new BedEnterListener(), this);
        pm.registerEvents(new BedLeaveListener(), this);

        // Making sure that Minecraft won't override plugin settings
        String[] split = Bukkit.getBukkitVersion().split("-")[0].split("\\.");
        String minorVer = split[1];

        if (Integer.parseInt(minorVer) >= 17) {
            for(World world : Bukkit.getWorlds()) {
                world.setGameRule(GameRule.PLAYERS_SLEEPING_PERCENTAGE, 101);
            }
        }
    }

    public void loadConfig() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
    }

    private void versionChecker() {
        try {
            HttpURLConnection connection = (HttpURLConnection) (new URL("https://api.spigotmc.org/legacy/update.php?resource=69247")).openConnection();

            connection.setDoOutput(true);
            connection.setRequestMethod("GET");

            String responseVersion = (new BufferedReader(new InputStreamReader(connection.getInputStream()))).readLine();
            String currentVersion = getDescription().getVersion();

            if(!responseVersion.equals(currentVersion)) {
                getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "[MakeItDay] There is a new update available. New version: " + responseVersion + ", current version: " + currentVersion);
            } else {
                getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[MakeItDay] MakeItDay is up to date!");
            }

        } catch (IOException e){
            getServer().getConsoleSender().sendMessage(ChatColor.RED + "[MakeItDay] Failed to check for updates!");
            e.printStackTrace();
        }
    }

}
