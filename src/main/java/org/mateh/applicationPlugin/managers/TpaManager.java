package org.mateh.applicationPlugin.managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TpaManager {
    private final Map<UUID, UUID> pendingTPA = new HashMap<>();

    public void addRequest(Player target, Player requester) {
        pendingTPA.put(target.getUniqueId(), requester.getUniqueId());
    }

    public boolean hasRequest(Player target) {
        return pendingTPA.containsKey(target.getUniqueId());
    }

    public Player getRequester(Player target) {
        UUID requesterUUID = pendingTPA.get(target.getUniqueId());
        return requesterUUID != null ? Bukkit.getPlayer(requesterUUID) : null;
    }

    public void removeRequest(Player target) {
        pendingTPA.remove(target.getUniqueId());
    }
}
