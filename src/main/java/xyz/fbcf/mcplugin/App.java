package xyz.fbcf.mcplugin;

import org.bukkit.command.*;
import org.bukkit.plugin.java.JavaPlugin;

public class App extends JavaPlugin {
    @Override
    public void onEnable() {
        // runs on startup, reload, plugin reload
        getLogger().info("Hello, SpigotMC!");
    }
    @Override
    public void onDisable() {
        //runs on shutdown, reload, plugin reload
        getLogger().info("See you again, SpigotMC!");
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("lavarain")) {
            
        }
        return true;
    }
}