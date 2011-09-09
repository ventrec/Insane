package no.insane.insane.handlers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;

import no.insane.insane.Insane;

public class BlockProtectHandler {
	
	private InsaneMySQLHandler sqlHandler;
	private Connection conn;
	private PreparedStatement add;
	private PreparedStatement delete;
	private PreparedStatement update;
	private PreparedStatement isProtected;
	private PreparedStatement getOwner;
	
	public BlockProtectHandler(Insane instance) {
		this.sqlHandler = instance.getSqlHandler();
	}
	
	public void initialize() {
		this.conn = this.sqlHandler.getConnection();
		try {
			this.add = this.conn.prepareStatement("REPLACE INTO `blocks` (`x`, `y`, `z`, `world`, `player`, `time`) VALUES (?, ?, ?, ?, ?, UNIX_TIMESTAMP())");
			this.delete = this.conn.prepareStatement("DELETE FROM blocks WHERE x=? AND y=? AND z=? AND world=?");
			this.update = this.conn.prepareStatement("UPDATE blocks SET time=UNIX_TIMESTAMP(), player=? WHERE x=? AND y=? AND z=? AND world=?");
			this.isProtected = this.conn.prepareStatement("SELECT uid FROM blocks WHERE x=? AND y=? AND z=? AND world=?");
			this.getOwner = this.conn.prepareStatement("SELECT uid FROM blocks WHERE x=? AND y=? AND z=? AND world=?");
		} catch (SQLException e) {
			Insane.log.log(Level.SEVERE, "[Insane] Kunne ikke initialisere prepared statements for BlockProtectHandler.", e);
		}
	}
}