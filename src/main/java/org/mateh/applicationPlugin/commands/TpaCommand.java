package org.mateh.applicationPlugin.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.mateh.applicationPlugin.Main;

import java.util.*;

public class TpaCommand implements CommandExecutor, TabCompleter {

    private final Main main;
    public static Map<String, String> pendingTPA = new HashMap<>();

    public TpaCommand(Main main) {
        this.main = main;
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
            String targetUUID = player.getUniqueId().toString();
            if (!pendingTPA.containsKey(targetUUID)) {
                player.sendMessage(main.getConfig().getString("messages.noPendingTpa"));
                return true;
            }
            String requesterUUID = pendingTPA.remove(targetUUID);
            Player requester = Bukkit.getPlayer(UUID.fromString(requesterUUID));
            if (requester == null) {
                player.sendMessage(main.getConfig().getString("messages.requesterOffline"));
                return true;
            }
            requester.teleport(player.getLocation());
            requester.sendMessage(main.getConfig().getString("messages.tpaAccepted").replace("{player}", player.getName()));
            player.sendMessage(main.getConfig().getString("messages.tpaAcceptedReceiver").replace("{player}", requester.getName()));
            return true;
        } else if (subcommand.equals("deny")) {
            String targetUUID = player.getUniqueId().toString();
            if (!pendingTPA.containsKey(targetUUID)) {
                player.sendMessage(main.getConfig().getString("messages.noPendingTpa"));
                return true;
            }
            String requesterUUID = pendingTPA.remove(targetUUID);
            Player requester = Bukkit.getPlayer(UUID.fromString(requesterUUID));
            if (requester != null) {
                requester.sendMessage(main.getConfig().getString("messages.tpaDenied").replace("{player}", player.getName()));
            }
            player.sendMessage(main.getConfig().getString("messages.tpaDeniedReceiver"));
            return true;
        } else {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage(main.getConfig().getString("messages.playerNotFound"));
                return true;
            }
            pendingTPA.put(target.getUniqueId().toString(), player.getUniqueId().toString());
            player.sendMessage(main.getConfig().getString("messages.tpaSent").replace("{player}", target.getName()));
            target.sendMessage(main.getConfig().getString("messages.tpaReceived").replace("{player}", player.getName()));

            TextComponent accept = new TextComponent("[Accept]");
            accept.setColor(ChatColor.GREEN);
            accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpa accept"));
            accept.setHoverEvent(new net.md_5.bungee.api.chat.HoverEvent(
                    net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT,
                    new ComponentBuilder("Click to accept the teleport request").create()));

            TextComponent deny = new TextComponent("[Deny]");
            deny.setColor(ChatColor.RED);
            deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpa deny"));
            deny.setHoverEvent(new net.md_5.bungee.api.chat.HoverEvent(
                    net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT,
                    new ComponentBuilder("Click to deny the teleport request").create()));

            TextComponent message = new TextComponent(" ");
            message.addExtra(accept);
            message.addExtra(" or ");
            message.addExtra(deny);

            target.spigot().sendMessage(message);
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
