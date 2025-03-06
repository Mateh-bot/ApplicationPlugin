package org.mateh.applicationPlugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.mateh.applicationPlugin.Main;

public class FixCommand implements CommandExecutor {
    private final Main main;

    public FixCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("applicationPlugin.fix")) {
            sender.sendMessage(main.getConfig().getString("messages.noPermission"));
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by a player.");
            return true;
        }
        Player player = (Player) sender;
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || item.getType().isAir()) {
            player.sendMessage(main.getConfig().getString("messages.noItemInHand"));
            return true;
        }

        if (item.hasItemMeta() && item.getItemMeta() instanceof Damageable) {
            Damageable meta = (Damageable) item.getItemMeta();
            meta.setDamage(0);
            item.setItemMeta(meta);
        }
        player.sendMessage(main.getConfig().getString("messages.itemFixed"));
        return true;
    }
}
