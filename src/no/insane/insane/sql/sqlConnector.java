package no.insane.insane.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import no.insane.insane.Insane;

public class sqlConnector {
	public static ResultSet result;
		
	public static synchronized Connection getConnection() {
		try {
			
			Connection conn = DriverManager.getConnection("jdbc:mysql://"+Insane.dbhost+":"+Insane.dbport+"/"+Insane.dbname+"", Insane.dbuser, Insane.dbpass);
			
			Logger.getLogger("Minecraft").log(Level.INFO, "[Insane] Koblet til mysql databasen");
			return conn;
		} catch (SQLException e) {
			Logger.getLogger("Minecraft").log(Level.SEVERE, "[Insane] Kunne ikke koble til databasen", e);
			return null;
		}
	}
	
	public static Connection createConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection ret = DriverManager.getConnection("jdbc:mysql://"+Insane.dbhost+":"+Insane.dbport+"/"+Insane.dbname+"", Insane.dbuser, Insane.dbpass);
			return ret;
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}