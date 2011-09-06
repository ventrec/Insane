package no.insane.insane.handlers;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import no.insane.insane.Insane;

import org.bukkit.World;
import org.bukkit.util.config.Configuration;


public class ConfigurationHandler {
    
    private Insane plugin;
    
    /**
     * Holds configurations for different worlds.
     */
    private Map<String, WorldConfigurationHandler> worlds;
    

    public ConfigurationHandler(Insane plugin) {
        this.plugin = plugin;
        this.worlds = new HashMap<String, WorldConfigurationHandler>();
    }

    public void load() {
        // Create the default configuration file
        Insane.createDefaultConfiguration(
                new File(plugin.getDataFolder(), "config.yml"), "config.yml");
        
        Configuration config = plugin.getConfiguration();
        config.load();

        // Load configurations for each world
        for (World world : plugin.getServer().getWorlds()) {
            get(world);
        }

        config.save();
    }

    public void unload() {
        worlds.clear();
    }

    public WorldConfigurationHandler get(World world) {
        String worldName = world.getName();
        WorldConfigurationHandler config = worlds.get(worldName);
        
        if (config == null) {
            config = new WorldConfigurationHandler(plugin, worldName);
            worlds.put(worldName, config);
        }

        return config;
    }
}