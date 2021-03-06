package no.insane.insane.handlers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;

import no.insane.insane.Insane;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class UserHandler {

	private HashMap<Player, PlayerData> users;
	private InsaneMySQLHandler sqlHandler;
	private PreparedStatement getUserPS;
	private PreparedStatement getUserFromUIDPS;
	private Insane plugin;
	private Connection conn;

	public UserHandler(Insane instance) {
		this.plugin = instance;
		this.sqlHandler = instance.getSqlHandler();
		this.users = new HashMap<Player, PlayerData>();
	}

	public boolean initialize() {
		this.conn = this.sqlHandler.getConnection();
		try {
			this.getUserPS = this.conn.prepareStatement("SELECT * FROM users WHERE `name` = ?");
			this.getUserFromUIDPS = this.conn.prepareStatement("SELECT name FROM users WHERE `id` = ?");
		} catch (SQLException e) {
			Insane.log.log(Level.SEVERE, "[Insane] Feil initialisering av prepared statements i UserHandler: ",	e);
			return false;
		}
		return true;
	}
	
	public void login(Player p) {
		if(userExists(p)) {
			if(this.users.containsKey(p)) {
				this.users.remove(p);
			}
			this.users.put(p, getPlayerData(p));
		} else {
			if(register(p)) {
				this.users.put(p, getPlayerData(p));
			}
		}
	}
	
	public void logout(Player p) {
		this.users.remove(p);
	}
	
	public boolean register(Player p) {
		if (sqlHandler.update("REPLACE INTO `users` (`id`, `name`, `status`, `active`, `last_login`) VALUES (NULL, '"+ p.getName() +"', 0, UNIX_TIMESTAMP(), UNIX_TIMESTAMP())")) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean register(String name) {
		if (sqlHandler.update("REPLACE INTO `users` (`id`, `name`, `status`, `active`, `last_login`) VALUES (NULL, '"+name+"', '0', UNIX_TIMESTAMP(), UNIX_TIMESTAMP())")) {
			return true;
		} else {
			return false;
		}
	}
	
	public PlayerData getPlayerData(Player p) {
		PlayerData pd = new PlayerData();
			try {
				this.getUserPS.setString(1, p.getName());
				ResultSet rs = this.getUserPS.executeQuery();
				
				while(rs.next()) {
					pd.setUID(rs.getInt(1));
					pd.setStatus(rs.getInt(3));
				}
			} catch (SQLException e) {
				Insane.log.log(Level.SEVERE, "[Insane] MySQL Error: "+ Thread.currentThread().getStackTrace()[0].getMethodName(), e);
			}
		return pd;
	}
	
	public PlayerData getPlayerData(String name) {
		PlayerData pd = new PlayerData();
		try {
			this.getUserPS.setString(1, name);
			ResultSet rs = this.getUserPS.executeQuery();
			
			while(rs.next()) {
				pd.setUID(rs.getInt(1));
				pd.setStatus(rs.getInt(3));
			}
		} catch (SQLException e) {
			Insane.log.log(Level.SEVERE, "[Insane] MySQL Error: "+ Thread.currentThread().getStackTrace()[0].getMethodName(), e);
		}
		return pd;
	}
	
	public void reloadUser(String name) {
		Player p = this.plugin.playerMatch(name);
		if(p != null) {
			this.users.remove(p);
			this.users.put(p, getPlayerData(p));
		}
	}
	
	public void reloadUser(Player p) {
		if(this.users.containsKey(p)) {
			this.users.remove(p);
		}
		this.users.put(p, getPlayerData(p));
	}
	
	public boolean userExists(Player p) {
		boolean exist = false;
		try {
			this.getUserPS.setString(1, p.getName());

			ResultSet rs = this.getUserPS.executeQuery();

			while (rs.next()) {
				exist = true;
			}
		} catch (SQLException e) {
			Insane.log.log(Level.SEVERE, "[Insane] MySQL Error: "+ Thread.currentThread().getStackTrace()[0].getMethodName(), e);
		}
		return exist;
	}
	
	public boolean userExists(String name) {
		boolean exist = false;
		try {
			this.getUserPS.setString(1, name);

			ResultSet rs = this.getUserPS.executeQuery();

			while (rs.next()) {
				exist = true;
			}
		} catch (SQLException e) {
			Insane.log.log(Level.SEVERE, "[Insane] MySQL Error: "+ Thread.currentThread().getStackTrace()[0].getMethodName(), e);
		}
		return exist;
	}
	
	public boolean setStatus(String name, int status) {
		if(sqlHandler.update("UPDATE `users` SET status='"+status+"' WHERE name='"+name+"'")) {
			return true;
		} else {
			return false;
		}
	}
	
	public int getUserStatus(Player p) {
		return this.users.get(p).getStatus();
	}
	
	public int getUID(Player p) {
		return this.users.get(p).getUID();
	}
	
	public String getNameFromUID(int uid) {
		String name = null;
		try {
			this.getUserFromUIDPS.setInt(1, uid);

			ResultSet rs = this.getUserFromUIDPS.executeQuery();

			while (rs.next()) {
				name = rs.getString(1);
			}
		} catch (SQLException e) {
			Insane.log.log(Level.SEVERE, "[Insane] MySQL Error: "+ Thread.currentThread().getStackTrace()[0].getMethodName(), e);
		}
		return name;
	}
		
	public boolean canBuild(Player p, World w) {
		ConfigurationHandler cfg = this.plugin.getGlobalStateManager();
		WorldConfigurationHandler wcfg = cfg.get(p.getWorld());
		
		if((getUserStatus(p) < 1) && (wcfg.BuildPermission)) {
			p.sendMessage(ChatColor.RED + "Du har ikke tillatelse til å bygge på denne serveren.");
			p.sendMessage(ChatColor.RED + "Gå til http://insane.no for å få tillatelse til å bygge.");
			return false;
		} else {
			return true;
		}
	}
	
	public String getChatName(Player p) {
		if(getUserStatus(p) == Insane.ADMIN) {
			return Insane.ADMIN_COLOR + p.getName() + ":" + ChatColor.WHITE;
		} else if(getUserStatus(p) == Insane.STAB) {
			return Insane.STAB_COLOR + p.getName() + ":" + ChatColor.WHITE;
		} else if(getUserStatus(p) == Insane.VAKT) {
			return Insane.VAKT_COLOR + p.getName() + ":" + ChatColor.WHITE;
		} else if(getUserStatus(p) == Insane.BRUKER) {
			return Insane.BRUKER_COLOR + p.getName() + ":" + ChatColor.WHITE;
		} else if(getUserStatus(p) == Insane.GJEST) {
			return Insane.GJEST_COLOR + p.getName() + ":" + ChatColor.WHITE;
		} else {
			return p.getName() + ":";
		}
	}

}