package org.mateh.applicationPlugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.mateh.applicationPlugin.Main;

import java.util.Collections;
import java.util.List;

public class TrashCommand implements CommandExecutor {

    private final Main main;
    private final String trashTitle;
    private final int inventorySize;

    public TrashCommand(Main main) {
        this.main = main;
        this.trashTitle = main.getConfig().getString("messages.trashTitle", "Trash");
        this.inventorySize = main.getConfig().getInt("trash.inventorySize", 9);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("applicationPlugin.trash")) {
            sender.sendMessage(main.getConfig().getString("messages.noPermission"));
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by a player.");
            return true;
        }
        Player player = (Player) sender;
        Inventory trashInventory = Bukkit.createInventory(null, inventorySize, trashTitle);
        player.openInventory(trashInventory);
        player.sendMessage(main.getConfig().getString("messages.trashOpened"));
        return true;
    }
}
