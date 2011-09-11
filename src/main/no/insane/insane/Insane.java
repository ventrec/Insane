package no.insane.insane;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import no.insane.insane.commands.BrukerCommand;
import no.insane.insane.commands.KickCommand;
import no.insane.insane.handlers.BlockProtect;
import no.insane.insane.handlers.ConfigurationHandler;
import no.insane.insane.handlers.ConsoleHandler;
import no.insane.insane.handlers.InsaneMySQLHandler;
import no.insane.insane.handlers.JSONAPICallHandler;
import no.insane.insane.handlers.LogHandler;
import no.insane.insane.handlers.PlayerData;
import no.insane.insane.handlers.UserHandler;
import no.insane.insane.listeners.InsaneBlockListener;
import no.insane.insane.listeners.InsaneEntityListener;
import no.insane.insane.listeners.InsaneInventoryListener;
import no.insane.insane.listeners.InsanePlayerListener;
import no.insane.insane.listeners.InsaneVehicleListener;
import no.insane.insane.listeners.InsaneWeatherListener;
import no.insane.insane.listeners.InsaneWorldListener;
import no.insane.insane.sql.sqlConnector;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.ramblingwood.minecraft.jsonapi.api.APIMethodName;


	public class Insane extends JavaPlugin implements JSONAPICallHandler {
		
		// Configuration
		public final ConfigurationHandler configuration;
		
		public Insane() {
			configuration = new ConfigurationHandler(this);
		}
		
		// SQL
		private InsaneMySQLHandler sqlHandler = new InsaneMySQLHandler();
		
		// Handlers
		private UserHandler userHandler = new UserHandler(this);
		private BlockProtect blockProtect = new BlockProtect(this);
		
		// Logger
		public static final Logger log = Logger.getLogger("Minecraft");
		private LogHandler logHandler = new LogHandler();
		
		
		
		// Statuser og farger
		public static final int 		ADMIN 			= 15;
		public static final ChatColor 	ADMIN_COLOR		= ChatColor.RED;
		public static final int 		STAB 			= 10;
		public static final ChatColor 	STAB_COLOR		= ChatColor.GOLD;
		public static final int 		VAKT 			= 5;
		public static final ChatColor 	VAKT_COLOR		= ChatColor.BLUE;
		public static final int			TRUSTED			= 3;
		public static final ChatColor	TRUSTED_COLOR	= ChatColor.GREEN;
		public static final int 		BRUKER 			= 1;
		public static final ChatColor 	BRUKER_COLOR	= ChatColor.WHITE;
		public static final int 		GJEST 			= 0;
		public static final ChatColor 	GJEST_COLOR		= ChatColor.GRAY;
		
		// Listeners
		public InsaneEntityListener entityListener = new InsaneEntityListener(this);
		public InsaneBlockListener blockListener = new InsaneBlockListener(this);
		public InsanePlayerListener playerListener = new InsanePlayerListener(this);
		public InsaneVehicleListener vehicleListener = new InsaneVehicleListener(this);
		public InsaneWeatherListener weatherListener = new InsaneWeatherListener(this);
		public InsaneInventoryListener inventoryListener = new InsaneInventoryListener(this);
		public InsaneWorldListener worldListener = new InsaneWorldListener(this);
		

		public void onDisable() {
			
			log.log(Level.INFO, "[Insane] Plugin stoppet.");
		}
		
		public void onEnable() {
			
			Plugin checkplugin = this.getServer().getPluginManager().getPlugin("JSONAPI");
	        if(checkplugin != null) {
	            log.info("[Insane] JsonAPI er lastet.");
	        }
	        else {
	        	log.info("[Insane] JsonAPI er ikke lastet.");
	        }
			
			// Log filter
			Logger.getLogger("Minecraft").setFilter(new ConsoleHandler());
						
			getDataFolder().mkdirs();
			configuration.load();
									
			registerEvents();
			
			sqlConnection();
			this.userHandler.initialize();
			
			registerCommands();
			
			for (Player p : getServer().getOnlinePlayers()) {
				this.userHandler.login(p);
			}
			
			log.log(Level.INFO, "[Insane] Plugin lastet.");
			
		}
		
		public void registerEvents() {

			PluginManager pm = getServer().getPluginManager();

			// Block Events
			pm.registerEvent(Event.Type.BLOCK_BREAK, this.blockListener,Event.Priority.Normal, this);
			pm.registerEvent(Event.Type.BLOCK_DAMAGE, this.blockListener,Event.Priority.Normal, this);
			pm.registerEvent(Event.Type.BLOCK_IGNITE, this.blockListener,Event.Priority.Normal, this);
			pm.registerEvent(Event.Type.BLOCK_PLACE, this.blockListener,Event.Priority.Normal, this);
			pm.registerEvent(Event.Type.BLOCK_PHYSICS, this.blockListener,Event.Priority.Normal, this);
			pm.registerEvent(Event.Type.BLOCK_FROMTO, this.blockListener,Event.Priority.Normal, this);
			pm.registerEvent(Event.Type.BLOCK_BURN, this.blockListener,Event.Priority.Normal, this);
			pm.registerEvent(Event.Type.BLOCK_FORM, this.blockListener,Event.Priority.Normal, this);
			pm.registerEvent(Event.Type.REDSTONE_CHANGE, this.blockListener,Event.Priority.Normal, this);
			pm.registerEvent(Event.Type.BLOCK_PISTON_EXTEND, this.blockListener,Event.Priority.Normal, this);
			pm.registerEvent(Event.Type.BLOCK_PISTON_RETRACT, this.blockListener,Event.Priority.Normal, this);
			pm.registerEvent(Event.Type.SIGN_CHANGE, this.blockListener,Event.Priority.Normal, this);

			// Player Events
			pm.registerEvent(Event.Type.PLAYER_LOGIN, this.playerListener,Event.Priority.Normal, this);
			pm.registerEvent(Event.Type.PLAYER_JOIN, this.playerListener,Event.Priority.Normal, this);
			pm.registerEvent(Event.Type.PLAYER_MOVE, this.playerListener,Event.Priority.Normal, this);
			pm.registerEvent(Event.Type.PLAYER_QUIT, this.playerListener,Event.Priority.Normal, this);
			pm.registerEvent(Event.Type.PLAYER_INTERACT, this.playerListener,Event.Priority.Normal, this);
			pm.registerEvent(Event.Type.PLAYER_CHAT, this.playerListener,Event.Priority.Normal, this);
			pm.registerEvent(Event.Type.PLAYER_DROP_ITEM, this.playerListener,Event.Priority.Normal, this);
			pm.registerEvent(Event.Type.PLAYER_PICKUP_ITEM, this.playerListener,Event.Priority.Normal, this);
			pm.registerEvent(Event.Type.PLAYER_KICK, this.playerListener,Event.Priority.Normal, this);
			pm.registerEvent(Event.Type.PLAYER_INTERACT_ENTITY,this.playerListener, Event.Priority.Normal, this);
			pm.registerEvent(Event.Type.PLAYER_BUCKET_EMPTY, this.playerListener,Event.Priority.Normal, this);
			pm.registerEvent(Event.Type.PLAYER_BUCKET_FILL, this.playerListener,Event.Priority.Normal, this);
			pm.registerEvent(Event.Type.PLAYER_ANIMATION, this.playerListener,Event.Priority.Normal, this);
			pm.registerEvent(Event.Type.PLAYER_BED_ENTER, this.playerListener,Event.Priority.Normal, this);
			pm.registerEvent(Event.Type.PLAYER_BED_LEAVE, this.playerListener,Event.Priority.Normal, this);
			pm.registerEvent(Event.Type.PLAYER_EGG_THROW, this.playerListener,Event.Priority.Normal, this);
			pm.registerEvent(Event.Type.PLAYER_ITEM_HELD, this.playerListener,Event.Priority.Normal, this);
			pm.registerEvent(Event.Type.PLAYER_PORTAL, this.playerListener,Event.Priority.Normal, this);
			pm.registerEvent(Event.Type.PLAYER_PRELOGIN, this.playerListener,Event.Priority.Normal, this);
			pm.registerEvent(Event.Type.PLAYER_RESPAWN, this.playerListener,Event.Priority.Normal, this);
			pm.registerEvent(Event.Type.PLAYER_TELEPORT, this.playerListener,Event.Priority.Normal, this);
			pm.registerEvent(Event.Type.PLAYER_TOGGLE_SNEAK, this.playerListener,Event.Priority.Normal, this);
			
			//WorldListener
			pm.registerEvent(Event.Type.CHUNK_UNLOAD, this.worldListener,Event.Priority.Normal, this);
			pm.registerEvent(Event.Type.CHUNK_LOAD, this.worldListener,Event.Priority.Normal, this);
			pm.registerEvent(Event.Type.WORLD_INIT, this.worldListener,Event.Priority.Normal, this);

			// Entity Events
			pm.registerEvent(Event.Type.CREATURE_SPAWN, this.entityListener,Event.Priority.Normal, this);
			pm.registerEvent(Event.Type.ENTITY_COMBUST, this.entityListener,Event.Priority.Normal, this);
			pm.registerEvent(Event.Type.ENTITY_DAMAGE, this.entityListener,Event.Priority.Normal, this);
			pm.registerEvent(Event.Type.ENTITY_DEATH, this.entityListener,Event.Priority.Normal, this);
			pm.registerEvent(Event.Type.ENTITY_EXPLODE, this.entityListener,Event.Priority.Normal, this);
			pm.registerEvent(Event.Type.ENTITY_INTERACT, this.entityListener,Event.Priority.Normal, this);
			pm.registerEvent(Event.Type.ENTITY_TARGET, this.entityListener,Event.Priority.Normal, this);
			pm.registerEvent(Event.Type.PIG_ZAP, this.entityListener,Event.Priority.Normal, this); // Has to be casted to this location

			// Vehicle Events
			pm.registerEvent(Event.Type.VEHICLE_UPDATE, this.vehicleListener,Event.Priority.Normal, this);
			pm.registerEvent(Event.Type.VEHICLE_EXIT, this.vehicleListener,Event.Priority.High, this);
			pm.registerEvent(Event.Type.VEHICLE_CREATE, this.vehicleListener,Event.Priority.Normal, this);
			pm.registerEvent(Event.Type.VEHICLE_ENTER, this.vehicleListener,Event.Priority.Normal, this);
			pm.registerEvent(Event.Type.VEHICLE_MOVE, this.vehicleListener,Event.Priority.Normal, this);
			pm.registerEvent(Event.Type.VEHICLE_DAMAGE, this.vehicleListener,Event.Priority.Normal, this);
			pm.registerEvent(Event.Type.VEHICLE_COLLISION_BLOCK,this.vehicleListener, Event.Priority.Normal, this);
			pm.registerEvent(Event.Type.VEHICLE_COLLISION_ENTITY,this.vehicleListener, Event.Priority.Normal, this);
			
			// Weather Events
			pm.registerEvent(Event.Type.WEATHER_CHANGE, this.weatherListener,Event.Priority.Normal, this);
			pm.registerEvent(Event.Type.THUNDER_CHANGE, this.weatherListener,Event.Priority.Normal, this);
			pm.registerEvent(Event.Type.LIGHTNING_STRIKE, this.weatherListener,Event.Priority.Normal, this);
			
			// Inventory Events
			pm.registerEvent(Event.Type.FURNACE_BURN, this.inventoryListener, Event.Priority.Normal, this);
			
			// Spout Events

		}
		
		public void registerCommands() {
			getCommand("bruker").setExecutor(new BrukerCommand(this));
			getCommand("kick").setExecutor(new KickCommand(this));
		}
		
		public void sqlConnection() {
			Connection conn = sqlConnector.createConnection();

			if (conn == null) {
				log.log(Level.SEVERE, "[Insane] Kunne ikke opprette forbindelse til mysql, disabler plugin.");
				getServer().getPluginManager().disablePlugin(this);
				return;
			} else {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
					log.log(Level.SEVERE, "[Insane] Feil under lukking av mysql tilkobling.", e);
				}
			}
			sqlHandler.initialize();
		}
		
		public InsaneMySQLHandler getSqlHandler() {
			return sqlHandler;
		}
		
		public LogHandler getLogHandler() {
			return logHandler;
		}
		
		public UserHandler getUserHandler() {
			return userHandler;
		}
		
		public BlockProtect getBlockProtectHandler() {
			return blockProtect;
		}
		
		public static String getPlayerIP(Player player) {
			String address = player.getAddress().toString();
			address = address.substring(1);
			address = address.split(":")[0];
			return address;
		}
		
	    public static void createDefaultConfiguration(File actual, String defaultName) {
	        
	        // Make parent directories
	        File parent = actual.getParentFile();
	        if (!parent.exists()) {
	            parent.mkdirs();
	        }

	        if (actual.exists()) {
	            return;
	        }

	        InputStream input = Insane.class.getResourceAsStream("/defaults/" + defaultName);
	        
	        if (input != null) {
	            FileOutputStream output = null;

	            try {
	                output = new FileOutputStream(actual);
	                byte[] buf = new byte[8192];
	                int length = 0;
	                while ((length = input.read(buf)) > 0) {
	                    output.write(buf, 0, length);
	                }

	                log.info("[Insane] Standardkonfigurasjon skrevet: " + actual.getAbsolutePath());
	                
	            } catch (IOException e) {
	                e.printStackTrace();
	            } finally {
	                try {
	                    if (input != null) {
	                        input.close();
	                    }
	                } catch (IOException e) {
	                }

	                try {
	                    if (output != null) {
	                        output.close();
	                    }
	                } catch (IOException e) {
	                }
	            }
	        }
	    }
	    
	    public ConfigurationHandler getGlobalStateManager() {
	        return configuration;
	    }
	    
	    public Player playerMatch(String name) {
			if (this.getServer().getOnlinePlayers().length < 1) {
				return null;
			}

			Player[] online = this.getServer().getOnlinePlayers();
			Player lastPlayer = null;

			for (Player player : online) {
				String playerName = player.getName();
				String playerDisplayName = player.getDisplayName();

				if (playerName.equalsIgnoreCase(name)) {
					lastPlayer = player;
					break;
				} else if (playerDisplayName.equalsIgnoreCase(name)) {
					lastPlayer = player;
					break;
				}

				if (playerName.toLowerCase().indexOf(name.toLowerCase()) != -1) {
					if (lastPlayer != null) {
						return null;
					}

					lastPlayer = player;
				} else if (playerDisplayName.toLowerCase().indexOf(
						name.toLowerCase()) != -1) {
					if (lastPlayer != null) {
						return null;
					}

					lastPlayer = player;
				}
			}

			return lastPlayer;
		}
	    
	    public boolean willHandle(APIMethodName methodName) {
	        if(methodName.matches("getUserStatus")) {
	            return true;
	        } else if(methodName.matches("registerUser")) {
	        	return true;
	        } else if(methodName.matches("isPublic")) {
	        	return true;
	        }  else {
	        	return false;
	        }
	    }

	    public Object handle(APIMethodName methodName, Object[] args) {
	        if(methodName.matches("getUserStatus")) {
	        	return true;
	        } else if(methodName.matches("registerUser")) {
	        	return true;
	        } else if(methodName.matches("isPublic")) {
	        	return true;
	        }  else {
	        	return false;
	        }
	    }
	    
	    public int getUserStatus(String name) {
	    	PlayerData pd = this.userHandler.getPlayerData(name);
	    	return pd.getStatus();
	    }
	    
	    public boolean registerUser(String name) {
	    	return this.userHandler.register(name);
	    }
	    
	    public boolean isPublic() {
	    	return ConfigurationHandler.ispublic;
	    }
	    
	    public boolean setUserStatus(String name, int status) {
	    	if(this.userHandler.userExists(name)) {
		    	if(this.userHandler.setStatus(name, status)) {
		    		this.userHandler.reloadUser(name);
		    		return true;
		    	} else {
		    		return false;
		    	}
	    	} else {
	    		this.userHandler.register(name);
	    		if(this.userHandler.setStatus(name, status)) {
		    		this.userHandler.reloadUser(name);
		    		return true;
		    	} else {
		    		return false;
		    	}
	    	}
	    }
	    
	}