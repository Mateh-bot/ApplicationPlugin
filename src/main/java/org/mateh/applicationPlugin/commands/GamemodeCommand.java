package org.mateh.applicationPlugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.mateh.applicationPlugin.Main;

import java.util.ArrayList;
import java.util.List;

public class GamemodeCommand implements CommandExecutor, TabCompleter {

    private final Main main;

    public GamemodeCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("applicationPlugin.gamemode")) {
            sender.sendMessage(main.getConfig().getString("messages.noPermission"));
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(main.getConfig().getString("messages.usageGamemode"));
            return true;
        }

        GameMode mode;
        try {
            mode = GameMode.valueOf(args[0].toUpperCase());
        } catch (IllegalArgumentException ex) {
            sender.sendMessage(main.getConfig().getString("messages.invalidGamemode"));
            return true;
        }

        Player target;
        if (args.length >= 2) {
            target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(main.getConfig().getString("messages.playerNotFound"));
                return true;
            }
        } else {
            if (!(sender instanceof Player)) {
                sender.sendMessage("This command can only be executed by a player.");
                return true;
            }
            target = (Player) sender;
        }

        target.setGameMode(mode);
        sender.sendMessage(main.getConfig().getString("messages.gamemodeChanged")
                .replace("{player}", target.getName())
                .replace("{mode}", mode.toString()));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            for (GameMode mode : GameMode.values()) {
                String modeName = mode.toString().toLowerCase();
                if (modeName.startsWith(args[0].toLowerCase())) {
                    completions.add(modeName);
                }
            }
        } else if (args.length == 2) {
            String toComplete = args[1].toLowerCase();
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getName().toLowerCase().startsWith(toComplete)) {
                    completions.add(player.getName());
                }
            }
        }
        return completions;
    }
}
