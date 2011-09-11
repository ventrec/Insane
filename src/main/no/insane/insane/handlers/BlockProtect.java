package no.insane.insane.handlers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import no.insane.insane.Insane;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class BlockProtect {
	
	private InsaneMySQLHandler sqlHandler;
	private UserHandler userHandler;
	
	private Connection conn;
	private PreparedStatement add;
	private PreparedStatement isProtected;
	private PreparedStatement getOwner;
	
	public BlockProtect(Insane instance) {
		this.sqlHandler = instance.getSqlHandler();
		this.userHandler = instance.getUserHandler();
	}
	
	public void initialize() {
		this.conn = this.sqlHandler.getConnection();
		try {
			this.add = this.conn.prepareStatement("REPLACE INTO `blocks` (`uid`, `x`, `y`, `z`, `world`) VALUES (?, ?, ?, ?, ?)");
			this.isProtected = this.conn.prepareStatement("SELECT uid FROM blocks WHERE x=? AND y=? AND z=? AND world=?");
			this.getOwner = this.conn.prepareStatement("SELECT uid FROM blocks WHERE x=? AND y=? AND z=? AND world=?");
		} catch (SQLException e) {
			Insane.log.log(Level.SEVERE, "[Insane] Kunne ikke initialisere prepared statements for BlockProtectHandler.", e);
		}
	}
	
	public boolean add(Player p, Block b) {
		int uid = this.userHandler.getUID(p);
		try {
			this.add.setInt(1, uid);
			this.add.setShort(2, (short) b.getX());
			this.add.setShort(3, (short) b.getY());
			this.add.setShort(4, (short) b.getZ());
			this.add.setString(5, b.getWorld().getName());
			this.add.executeQuery();
			return true;
		} catch (SQLException e) {
			Insane.log.log(Level.SEVERE, "[Insane] MySQL Error: "+ Thread.currentThread().getStackTrace()[0].getMethodName(), e);
			p.sendMessage("Kunne ikke beskytte blokken, prøv igjen eller kontakt Admin!");
			return false;
		}
	}
	
	public boolean delete(Player p, Block b) {
		boolean deleted = false;
		int uid = this.userHandler.getUID(p);
		if(isProtected(b)) {
			int owner = getOwner(b);
			Insane.log.info("Owner: " + owner);
			if((owner == uid) && (owner != -1)) {
				if(sqlHandler.update("DELETE FROM blocks WHERE x='"+b.getX()+"' AND y='"+b.getY()+"' AND z='"+b.getZ()+"' AND world='"+b.getWorld().getName()+"'")) {
					deleted = true;
				} else {
					deleted = false;
				}
			} else if (owner == -1) {
				// Blokken er ikke beskyttet så vi sender klarmelding om at den skal fjernes
				// Husk: Legge inn logg her etterhvert.
				deleted = true;
			} else {
				String name = this.userHandler.getNameFromUID(uid);
				p.sendMessage("Denne blokken eies av: " + name);
			}
		}
		return deleted;
	}
	
	public int getOwner(Block b) {
		int uid = -1;
		try {
			this.getOwner.setShort(1, (short) b.getX());
			this.getOwner.setShort(2, (short) b.getY());
			this.getOwner.setShort(3, (short) b.getZ());
			this.getOwner.setString(4, b.getWorld().getName());
			ResultSet rs = this.getOwner.executeQuery();
			
			while(rs.next()) {
				uid = rs.getInt(1);
			}
		} catch (SQLException e) {
			Insane.log.log(Level.SEVERE, "[Insane] MySQL Error: "+ Thread.currentThread().getStackTrace()[0].getMethodName(), e);
		}
		return uid;
	}
	
	public boolean isProtected(Block b) {
		boolean prot = false;
		try {
			this.isProtected.setShort(1, (short) b.getX());
			this.isProtected.setShort(2, (short) b.getY());
			this.isProtected.setShort(3, (short) b.getZ());
			this.isProtected.setString(4, b.getWorld().getName());
			ResultSet rs = this.isProtected.executeQuery();
			
			while(rs.next()) {
				prot = true;
			}
		} catch (SQLException e) {
			Insane.log.log(Level.SEVERE, "[Insane] MySQL Error: "+ Thread.currentThread().getStackTrace()[0].getMethodName(), e);
		}
		return prot;
	}
}