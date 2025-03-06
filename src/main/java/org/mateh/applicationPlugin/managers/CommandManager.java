package org.mateh.applicationPlugin.managers;

import org.mateh.applicationPlugin.Main;
import org.mateh.applicationPlugin.commands.*;

public class CommandManager {

    private final Main main;
    private final TpaManager tpaManager;

    public CommandManager(Main main) {
        this.main = main;
        this.tpaManager = new TpaManager();
    }

    public void registerCommands() {
        main.getCommand("gamemode").setExecutor(new GamemodeCommand(main));
        main.getCommand("gamemode").setTabCompleter(new GamemodeCommand(main));

        main.getCommand("god").setExecutor(new GodCommand(main));
        main.getCommand("god").setTabCompleter(new GodCommand(main));

        main.getCommand("openinv").setExecutor(new OpenInvCommand(main));
        main.getCommand("openinv").setTabCompleter(new OpenInvCommand(main));

        main.getCommand("enderchest").setExecutor(new EnderChestCommand(main));
        main.getCommand("enderchest").setTabCompleter(new EnderChestCommand(main));

        main.getCommand("fix").setExecutor(new FixCommand(main));

        main.getCommand("tpa").setExecutor(new TpaCommand(main, tpaManager));
        main.getCommand("tpa").setTabCompleter(new TpaCommand(main, tpaManager));

        main.getCommand("trash").setExecutor(new TrashCommand(main));
    }
}
