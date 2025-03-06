package org.mateh.applicationPlugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.mateh.applicationPlugin.Main;

import java.util.ArrayList;
import java.util.List;

public class OpenInvCommand implements CommandExecutor, TabCompleter {

    private final Main main;

    public OpenInvCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("applicationPlugin.openinv")) {
            sender.sendMessage(main.getConfig().getString("messages.noPermission"));
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by a player.");
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(main.getConfig().getString("messages.usageOpenInv"));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(main.getConfig().getString("messages.playerNotFound"));
            return true;
        }

        Player player = (Player) sender;
        player.openInventory(target.getInventory());
        sender.sendMessage(
                main.getConfig().getString("messages.openInvOpened")
                        .replace("{player}", target.getName())
        );
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            String toComplete = args[0].toLowerCase();
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getName().toLowerCase().startsWith(toComplete)) {
                    completions.add(player.getName());
                }
            }
        }
        return completions;
    }
}
