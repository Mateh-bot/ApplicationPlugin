package org.mateh.applicationPlugin;

import org.bukkit.plugin.java.JavaPlugin;
import org.mateh.applicationPlugin.commands.*;
import org.mateh.applicationPlugin.listeners.GodListener;
import org.mateh.applicationPlugin.listeners.TrashListener;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();

        getCommand("gamemode").setExecutor(new GamemodeCommand(this));
        getCommand("gamemode").setTabCompleter(new GamemodeCommand(this));

        getCommand("god").setExecutor(new GodCommand(this));
        getCommand("god").setTabCompleter(new GodCommand(this));

        getCommand("openinv").setExecutor(new OpenInvCommand(this));
        getCommand("openinv").setTabCompleter(new OpenInvCommand(this));

        getCommand("enderchest").setExecutor(new EnderChestCommand(this));
        getCommand("enderchest").setTabCompleter(new EnderChestCommand(this));

        getCommand("fix").setExecutor(new FixCommand(this));

        getCommand("tpa").setExecutor(new TpaCommand(this));
        getCommand("tpa").setTabCompleter(new TpaCommand(this));

        getCommand("trash").setExecutor(new TrashCommand(this));

        getServer().getPluginManager().registerEvents(new GodListener(), this);
        getServer().getPluginManager().registerEvents(new TrashListener(this), this);
    }
}
