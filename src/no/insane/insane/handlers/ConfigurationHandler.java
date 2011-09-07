package no.insane.insane.handlers;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import no.insane.insane.Insane;

import org.bukkit.World;
import org.bukkit.util.config.Configuration;


public class ConfigurationHandler {
    
    private Insane plugin;
    
    private Map<String, WorldConfigurationHandler> worlds;
    

    public ConfigurationHandler(Insane plugin) {
        this.plugin = plugin;
        this.worlds = new HashMap<String, WorldConfigurationHandler>();
    }
    
    // Config vars
	public static	String 	dbuser;
	public static  	String 	dbpass;
	public static  	String 	dbname;
	public static  	String 	dbhost;
	public static  	int		dbport;

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
        
        dbuser = config.getString("db-user", "username");
		dbpass = config.getString("db-pass", "password");
		dbname = config.getString("db-name", "database");
		dbhost = config.getString("db-host", "localhost");
		dbport = config.getInt("db-port", 3306);

        
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