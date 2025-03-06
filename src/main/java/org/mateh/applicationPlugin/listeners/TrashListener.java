package org.mateh.applicationPlugin.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.mateh.applicationPlugin.Main;

public class TrashListener implements Listener {

    private final Main main;
    private final String trashTitle;

    public TrashListener(Main main) {
        this.main = main;
        this.trashTitle = main.getConfig().getString("messages.trashTitle", "Trash");
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Inventory inv = event.getInventory();
        if (event.getView().getTitle().equals(trashTitle)) {
            inv.clear();
            if (event.getPlayer() instanceof org.bukkit.entity.Player) {
                event.getPlayer().sendMessage(main.getConfig().getString("messages.trashCleared"));
            }
        }
    }
}
