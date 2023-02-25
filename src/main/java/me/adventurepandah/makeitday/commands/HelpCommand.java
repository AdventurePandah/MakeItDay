package me.adventurepandah.makeitday.commands;

import me.adventurepandah.makeitday.Main;
import me.adventurepandah.makeitday.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class HelpCommand extends SubCommand {

    private final Main plugin = Main.getInstance();

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if (sender.hasPermission("makeitday.help")) {
            sender.sendMessage(ChatColor.AQUA + "[MakeItDay] " + ChatColor.GOLD + "Available commands:");
            sender.sendMessage(ChatColor.GOLD + "/makeitday reload");
        } else {
            sender.sendMessage(ChatColor.RED + "You don't have the permission to run this command.");
        }
    }

    public String name(){
        return plugin.commandManager.help;
    }

    public String info() {
        return null;
    }

    public String[] aliases() {
        return new String[0];
    }
}
