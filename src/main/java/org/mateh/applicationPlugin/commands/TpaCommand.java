package org.mateh.applicationPlugin.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.mateh.applicationPlugin.Main;
import org.mateh.applicationPlugin.managers.TpaManager;

import java.util.*;

public class TpaCommand implements CommandExecutor, TabCompleter {

    private final Main main;
    private final TpaManager tpaManager;

    public TpaCommand(Main main, TpaManager tpaManager) {
        this.main = main;
        this.tpaManager = tpaManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("applicationPlugin.tpa")) {
            sender.sendMessage(main.getConfig().getString("messages.noPermission"));
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by a player.");
            return true;
        }
        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(main.getConfig().getString("messages.usageTpa"));
            return true;
        }

        String subcommand = args[0].toLowerCase();

        if (subcommand.equals("accept")) {
            if (!tpaManager.hasRequest(player)) {
                player.sendMessage(main.getConfig().getString("messages.noPendingTpa"));
                return true;
            }
            Player requester = tpaManager.getRequester(player);
            if (requester == null) {
                player.sendMessage(main.getConfig().getString("messages.requesterOffline"));
                tpaManager.removeRequest(player);
                return true;
            }
            tpaManager.removeRequest(player);
            requester.teleport(player.getLocation());
            requester.sendMessage(main.getConfig().getString("messages.tpaAccepted").replace("{player}", player.getName()));
            player.sendMessage(main.getConfig().getString("messages.tpaAcceptedReceiver").replace("{player}", requester.getName()));
            return true;
        }
        else if (subcommand.equals("deny")) {
            if (!tpaManager.hasRequest(player)) {
                player.sendMessage(main.getConfig().getString("messages.noPendingTpa"));
                return true;
            }
            Player requester = tpaManager.getRequester(player);
            tpaManager.removeRequest(player);
            if (requester != null) {
                requester.sendMessage(main.getConfig().getString("messages.tpaDenied").replace("{player}", player.getName()));
            }
            player.sendMessage(main.getConfig().getString("messages.tpaDeniedReceiver"));
            return true;
        }
        else {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage(main.getConfig().getString("messages.playerNotFound"));
                return true;
            }
            if (target.equals(player)) {
                player.sendMessage(ChatColor.RED + "You cannot send a teleport request to yourself.");
                return true;
            }
            tpaManager.addRequest(target, player);
            player.sendMessage(main.getConfig().getString("messages.tpaSent").replace("{player}", target.getName()));
            target.sendMessage(main.getConfig().getString("messages.tpaReceived").replace("{player}", player.getName()));

            TextComponent acceptComponent = new TextComponent("[ACCEPT]");
            acceptComponent.setColor(ChatColor.GREEN);
            acceptComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpa accept"));

            TextComponent denyComponent = new TextComponent(" [DENY]");
            denyComponent.setColor(ChatColor.RED);
            denyComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpa deny"));

            target.spigot().sendMessage(acceptComponent, denyComponent);
            return true;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            String toComplete = args[0].toLowerCase();
            if ("accept".startsWith(toComplete)) {
                completions.add("accept");
            }
            if ("deny".startsWith(toComplete)) {
                completions.add("deny");
            }
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getName().toLowerCase().startsWith(toComplete)) {
                    completions.add(p.getName());
                }
            }
        }
        return completions;
    }
}
