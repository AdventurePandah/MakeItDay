package me.adventurepandah.makeitday.managers;

import me.adventurepandah.makeitday.Main;
import me.adventurepandah.makeitday.SubCommand;
import me.adventurepandah.makeitday.commands.HelpCommand;
import me.adventurepandah.makeitday.commands.ReloadCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandManager implements CommandExecutor, TabExecutor {

    private final ArrayList<SubCommand> commands = new ArrayList<>();

    private final Main plugin = Main.getInstance();

    public String main;
    public String help;
    public String reload;

    public CommandManager() {
        main = "makeitday";
        help = "help";
        reload = "reload";
    }

    public void setup() {
        plugin.getCommand(main).setExecutor(this);
        commands.add(new HelpCommand());
        commands.add(new ReloadCommand());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(command.getName().equalsIgnoreCase(main)) {
            if(args.length == 0) {
                if(sender.hasPermission("makeitday.help")) {
                    sender.sendMessage(ChatColor.AQUA + "[MakeItDay] " + ChatColor.GOLD  + "Available commands:");
                    sender.sendMessage(ChatColor.GOLD + "/makeitday reload");
                } else {
                    sender.sendMessage(ChatColor.RED + "You don't have the permission to run this command.");
                }
                return true;
            }
        }

        SubCommand target = get(args[0]);
        if(target == null) {
            sender.sendMessage(ChatColor.AQUA + "[MakeItDay] " + ChatColor.RED + "Command not found! Try /makeitday help for a list with available commands.");
            return true;
        }
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.addAll(Arrays.asList(args));
        arrayList.remove(0);

        target.onCommand(sender, args);

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 1) {
            List<String> subCommands = new ArrayList<>();
            subCommands.add("help");
            subCommands.add("reload");
            return subCommands;
        }
        return null;
    }

    private SubCommand get(String name) {
        for (SubCommand sc : commands) {
            if(sc.name().equalsIgnoreCase(name)) return sc;
            String[] aliases;
            int length = (aliases = sc.aliases()).length;
            for (int i = 0; i < length; i++) {
                String alias = aliases[i];
                if(name.equalsIgnoreCase(alias)) return sc;
            }
        }
        return null;
    }
}
