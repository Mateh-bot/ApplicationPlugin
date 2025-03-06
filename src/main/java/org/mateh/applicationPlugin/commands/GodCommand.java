package org.mateh.applicationPlugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.mateh.applicationPlugin.Main;

import java.util.*;

public class GodCommand implements CommandExecutor, TabCompleter {

    private final Main main;
    private static final Set<UUID> godModePlayers = new HashSet<>();

    public GodCommand(Main main) {
        this.main = main;
    }

    public static boolean isInGodMode(Player player) {
        return godModePlayers.contains(player.getUniqueId());
    }

    public static void toggleGodMode(Player player) {
        UUID uuid = player.getUniqueId();
        if (godModePlayers.contains(uuid)) {
            godModePlayers.remove(uuid);
        } else {
            godModePlayers.add(uuid);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("applicationPlugin.god")) {
            sender.sendMessage(main.getConfig().getString("messages.noPermission"));
            return true;
        }

        Player target;
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("This command can only be executed by a player.");
                return true;
            }
            target = (Player) sender;
        } else {
            target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(main.getConfig().getString("messages.playerNotFound"));
                return true;
            }
        }

        toggleGodMode(target);
        boolean enabled = isInGodMode(target);
        sender.sendMessage(
                main.getConfig().getString("messages.godModeToggled")
                        .replace("{player}", target.getName())
                        .replace("{state}", enabled ? "enabled" : "disabled")
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
