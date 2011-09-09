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
    public boolean Damage;
    public boolean PVPDamage;
    public boolean CreatureDamage;
    public boolean DrowningDamage;
    public boolean FireDamage;
    public boolean LavaDamage;
    public boolean ContactDamage;
    public boolean TNTDamage;
    public boolean FallDamage;
    public boolean BlockDamage;
    public boolean VoidDamage;
    public boolean VoidTeleport;
    
    public boolean CreeperBlockDamage;
    public boolean TNTBlockDamage;
    public boolean IceRegen;
    public boolean SnowRegen;
    
    // Plugin
    public boolean BuildPermission;    
    
    public boolean Chickens;
    public boolean Pigs;
    public boolean Cows;
    public boolean Sheeps;
    public boolean Squids;
    public boolean Wolves;
    
    public boolean Creepers;
    public boolean Skeletons;
    public boolean Slimes;
    public boolean Spiders;
    public boolean Zombies;
    public boolean PigZombies;
    public boolean Ghasts;

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

        // Kommandoer
        BrukerCommand = config.getBoolean("Kommandoer.Bruker", true);
        KickCommand = config.getBoolean("Kommandoer.Kick", true);
        
        // Bruker Skade
        PVPDamage = config.getBoolean("Bruker.Skade.PVP-skade", true);
        Damage = config.getBoolean("Bruker.Skade.Skade", true);
        CreatureDamage = config.getBoolean("Bruker.Skade.Monster-mot-spiller-skade", true);
        DrowningDamage = config.getBoolean("Bruker.Skade.Miste-liv-ved-drukning", true);
        FireDamage = config.getBoolean("Bruker.Skade.Miste-liv-ved-brann", true);
        LavaDamage = config.getBoolean("Bruker.Skade.Miste-liv-i-lava", true);
        ContactDamage = config.getBoolean("Bruker.Skade.Miste-liv-ved-kontakt-med-blokk", true);
        TNTDamage = config.getBoolean("Bruker.Skade.Miste-liv-i-eksplosjon", true);
        FallDamage = config.getBoolean("Bruker.Skade.Miste-liv-ved-fall", true);
        BlockDamage = config.getBoolean("Bruker.Skade.Miste-liv-inni-blokk", true);
        VoidDamage = config.getBoolean("Bruker.Skade.Miste-liv-i-void", true);
        VoidTeleport = config.getBoolean("Bruker.Skade.Teleporter-ut-av-void", true);
        
        // Bruker Handlinger
        BuildPermission = config.getBoolean("Bruker.Handlinger.Krev-byggetillatelse-for-bygging", true);
        
        // Verden
        CreeperBlockDamage = config.getBoolean("Verden.Blokker-blir-ekslodert-av-Creeper", true);
        TNTBlockDamage = config.getBoolean("Verden.Blokker-blir-eksplodert-av-TNT", true);
        SnowRegen = config.getBoolean("Verden.Snow-regenereres-av-vaer", true);
        IceRegen = config.getBoolean("Verden.Is-regenereres-av-vaer", true);
        
        // Dyr
        Chickens = config.getBoolean("Dyr.Kyllinger", true);
        Pigs = config.getBoolean("Dyr.Griser", true);
        Cows = config.getBoolean("Dyr.Kuer", true);
        Sheeps = config.getBoolean("Dyr.Sauer", true);
        Squids = config.getBoolean("Dyr.Blekkspruter", true);
        Wolves = config.getBoolean("Dyr.Ulver", true);
        
        // Monstre
        Creepers = config.getBoolean("Monstre.Creepere", true);
        Skeletons = config.getBoolean("Monstre.Skeletons", true);
        Slimes = config.getBoolean("Monstre.Slimes", true);
        Spiders = config.getBoolean("Monstre.Edderkopper", true);
        Zombies = config.getBoolean("Monstre.Zombier", true);
        PigZombies = config.getBoolean("Monstre.Grisezombier", true);
        Ghasts = config.getBoolean("Monstre.Ghasts", true);
		
        config.save();
    }
     
    public String getWorldName() {
        return this.worldName;
    }
    
}