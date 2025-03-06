package org.mateh.applicationPlugin;

import org.bukkit.plugin.java.JavaPlugin;
import org.mateh.applicationPlugin.commands.*;
import org.mateh.applicationPlugin.listeners.GodListener;
import org.mateh.applicationPlugin.listeners.TrashListener;
import org.mateh.applicationPlugin.managers.CommandManager;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();

        CommandManager commandManager = new CommandManager(this);
        commandManager.registerCommands();

        getServer().getPluginManager().registerEvents(new GodListener(), this);
        getServer().getPluginManager().registerEvents(new TrashListener(this), this);
    }
}
