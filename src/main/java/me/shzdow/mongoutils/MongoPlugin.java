package me.shzdow.mongoutils;

import org.bukkit.plugin.java.JavaPlugin;

public final class MongoPlugin extends JavaPlugin {
    private static MongoPlugin plugin;

    @Override
    public void onEnable() {
        //- Meant to be left empty so that the main core plugin can take an instance of the MongoLoader class!
        plugin = this;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static MongoPlugin getPlugin() {
        return plugin;
    }
}
