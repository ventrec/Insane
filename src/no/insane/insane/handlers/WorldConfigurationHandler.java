package no.insane.insane.handlers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import no.insane.insane.Insane;

import org.bukkit.util.config.Configuration;


public class WorldConfigurationHandler {
    @SuppressWarnings("unused")
	private Insane plugin;

    private String worldName;
    private Configuration parentConfig;
    private Configuration config;
    private File configFile;

    public boolean BrukerCommand;
    public boolean KickCommand;

    public WorldConfigurationHandler(Insane plugin, String worldName) {
        File baseFolder = new File(plugin.getDataFolder(), "worlds/" + worldName);
        configFile = new File(baseFolder, "config.yml");

        this.plugin = plugin;
        this.worldName = worldName;
        this.parentConfig = plugin.getConfiguration();

        Insane.createDefaultConfiguration(configFile, "config_world.yml");

        
        config = new Configuration(this.configFile);
        loadConfiguration();

        Insane.log.info("[Insane] Lastet konfigurasjon for: '" + worldName + '"');
    }

    @SuppressWarnings("unused")
	private boolean getBoolean(String node, boolean def) {
        boolean val = parentConfig.getBoolean(node, def);

        if (config.getProperty(node) != null) {
            return config.getBoolean(node, def);
        } else {
            return val;
        }
    }

    @SuppressWarnings("unused")
	private String getString(String node, String def) {
        String val = parentConfig.getString(node, def);

        if (config.getProperty(node) != null) {
            return config.getString(node, def);
        } else {
            return val;
        }
    }

    @SuppressWarnings("unused")
	private int getInt(String node, int def) {
        int val = parentConfig.getInt(node, def);

        if (config.getProperty(node) != null) {
            return config.getInt(node, def);
        } else {
            return val;
        }
    }

    @SuppressWarnings("unused")
	private double getDouble(String node, double def) {
        double val = parentConfig.getDouble(node, def);

        if (config.getProperty(node) != null) {
            return config.getDouble(node, def);
        } else {
            return val;
        }
    }

    @SuppressWarnings("unused")
	private List<Integer> getIntList(String node, List<Integer> def) {
        List<Integer> res = parentConfig.getIntList(node, def);

        if (res == null || res.size() == 0) {
            parentConfig.setProperty(node, new ArrayList<Integer>());
        }

        if (config.getProperty(node) != null) {
            res = config.getIntList(node, def);
        }

        return res;
    }

    @SuppressWarnings("unused")
	private List<String> getStringList(String node, List<String> def) {
        List<String> res = parentConfig.getStringList(node, def);

        if (res == null || res.size() == 0) {
            parentConfig.setProperty(node, new ArrayList<String>());
        }

        if (config.getProperty(node) != null) {
            res = config.getStringList(node, def);
        }

        return res;
    }

     private void loadConfiguration() {
        config.load();

        BrukerCommand = config.getBoolean("Kommandoer.Bruker", true);
        KickCommand = config.getBoolean("Kommandoer.Kick", true);
		
        config.save();
    }
     
    public String getWorldName() {
        return this.worldName;
    }
    
}