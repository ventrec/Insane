package no.insane.insane.handlers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;

import no.insane.insane.Insane;
import no.insane.insane.sql.sqlConnector;

public class InsaneMySQLHandler {

	private Connection sqlConnection = null;

	public InsaneMySQLHandler() {
		
	}

	public boolean initialize() {
		if (this.sqlConnection == null) {
			this.sqlConnection = sqlConnector.getConnection();
			if (this.sqlConnection == null) {
				Insane.log.log(Level.SEVERE, "[Insane] Feil ved tilkobling til databasen.");
			}
		}

		return true;
	}

	public Connection getConnection() {
		if (this.sqlConnection == null) {
			Insane.log.log(Level.INFO,
					"[Insane] Tilkoblingen returnerte null, oppretter ny kobling.");
			initialize();
		}
		return this.sqlConnection;
	}

	public void closeConnection() {
		try {
			this.sqlConnection.close();
		} catch (SQLException e) {
			Insane.log.log(Level.SEVERE,
					"[Insane] Kunne ikke lukke tilkoblingen til databasen.", e);
			e.printStackTrace();
		}
	}

	public void checkWarnings() {
		try {
			SQLWarning warning = this.sqlConnection.getWarnings();
			while (warning != null) {
				Insane.log
				.log(Level.WARNING,
						"[Insane] SQL-Advarsel: ", warning);
				warning = warning.getNextWarning();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Insane.log.log(Level.SEVERE, "[Insane] Fikk ikke hentet warnings fra databasetilkobling! ",	e);
		}
	}

	/**
	 * Oppdaterer tabellen med gitte sp�rring og verdiene i arrayet. Verdiene m�
	 * castes om til object og puttes i arrayet for at ting skal fungere
	 * korrekt.
	 * 
	 * @param query
	 *            (String) Sp�rringen som skal utf�res. Det er viktig at det er
	 *            ? hvor verdiene skal v�re
	 * @param array
	 *            (Object[]) Her lagres verdiene som skal inn i sp�rringa. Det
	 *            kj�res sjekk p� hvilken type verdi det er, som igjen kaster de
	 *            om til korrekt object.
	 * @return (boolean) True/False alt ettersom om sp�rringen lykkes
	 */
	public boolean update(String query, Object[] array) {
		Connection conn = null;
		PreparedStatement ps = null;
		int counter = 1;
		long time = System.currentTimeMillis();
		if (array != null) {
			try {
				// conn = sqlConnector.getConnection();
				conn = this.sqlConnection;
				ps = conn.prepareStatement(query);

				// Her kj�rer man inn variablene
				for (Object o : array) {
					if (o instanceof Integer) {
						ps.setInt(counter, (Integer) o); // indeks og object som
															// blir castet
					} else if (o instanceof String) {
						ps.setString(counter, (String) o);
					} else {
						Insane.log.log(Level.SEVERE,
								"[Insane] Ukjent objekt i mysql-handler.(" + o.getClass().toString() + ")");
						Insane.log.log(Level.SEVERE, Arrays.toString(array));
						return false;
					}
					counter++; // �ker indeks
				}
				array = null;
				ps.setEscapeProcessing(true);
				ps.executeUpdate();
				ps.close();
				// conn.close();

				long newTime = System.currentTimeMillis();
				if (newTime - time > 30) {
					Insane.log.log(Level.INFO,
							"[Insane] En sp�rring tok veldig lang tid( "
									+ (newTime - time)
									+ " ms). Sp�rringen var: " + query);
				}
				checkWarnings();
				;
				return true;
			} catch (SQLException ex) {
				Insane.log.log(Level.SEVERE, "[Insane] SQL Exception", ex);
				return false;
			}
		} else { // Arrayet er tomt, ergo har man brukt funksjonen feil
			Insane.log.log(Level.SEVERE,
					"[Insane] Empty array in mysqlhandler.update");
			return false;
		}
	}

	/**
	 * Metode som kj�rer en update uten preparedStatement.
	 * 
	 * @param query
	 *            (String) Sp�rringen
	 * @return True/False alt ettersom om sp�rringen lyktes
	 */

	public boolean update(String query) {
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = this.sqlConnection;
			stmt = conn.createStatement();
			stmt.executeUpdate(query);
			stmt.close();
			checkWarnings();
			return true;
		} catch (SQLException ex) {
			Insane.log.log(Level.SEVERE, "[Insane] SQL Exception", ex);
			return false;
		}
	}

	/**
	 * Oppdaterer databasen med din sp�rring samtidig som den returnerer den
	 * genererte iden
	 * 
	 * @param query
	 *            Selve sp�rringen (String)
	 * @return int. Returnerer 0 hvis det ikke ble generert id.
	 */
	public int insert(String query) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int id = -1;
		try {
			conn = this.sqlConnection;
			ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setEscapeProcessing(true);
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			if (rs.next()) {
				id = rs.getInt(1);
			}
			rs.close();
			ps.close();		
			checkWarnings();
			return id;
		} catch (SQLException ex) {
			Insane.log.log(Level.SEVERE, "[Insane] SQL Exception", ex);
			return 0;
		}
	}

	/**
	 * Henter ut en gitt kolonne fra tabellen. Funker kun s� lenge sp�rringen
	 * ber om kun en kolonne.
	 * 
	 * @param query
	 *            Selve sp�rringen (String)
	 * @param c
	 *            Kolonnen som skal returneres (String)
	 * @return Kolonnen (String) eller null
	 */
	public String getColumn(String query, String c) {
		Connection conn = null;
		Statement ps = null;
		ResultSet rs = null;
		String column = "";
		try {
			// conn = sqlConnector.getConnection();
			conn = this.sqlConnection;
			ps = conn.createStatement();
			rs = ps.executeQuery(query);

			if (rs.next()) {
				column = rs.getString(c);
			}
			
			rs.close();
			ps.close();
			checkWarnings();
			return column;
		} catch (SQLException ex) {
			Insane.log.log(Level.SEVERE, "[Insane] SQL Exception", ex);
			return null;
		}
	}

	/**
	 * Henter ut en gitt kolonne fra tabellen. Funker kun s� lenge sp�rringen
	 * ber om kun en kolonne.
	 * 
	 * @param query
	 *            Selve sp�rringen (String)
	 * @param c
	 *            Kolonnen som skal returneres (String)
	 * @return Kolonnen (String) eller null
	 */
	public String getColumn(String query, String c, Object[] array) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String column = "";
		int counter = 1; // Definerer indeks som blir brukt i sp�rringen. Skal
							// starte p� 1.
		if (array != null) {
			try {
				conn = this.sqlConnection;
				ps = conn.prepareStatement(query);

				// Her kj�rer man inn variablene
				for (Object o : array) {
					if (o instanceof Integer) {
						ps.setInt(counter, (Integer) o); // indeks og object som
															// blir castet
					} else if (o instanceof String) {
						ps.setString(counter, (String) o);
					} else {
						Insane.log
								.log(Level.SEVERE,
										"[Insane] Nullobjekt i mysql-handler. (getColumn)");
						return null;
					}
					counter++; // �ker indeks
				}
				array = null;
				rs = ps.executeQuery();

				if (rs.next()) {
					column = rs.getString(c);
				}
				rs.close();
				ps.close();
				checkWarnings();
			} catch (SQLException ex) {
				Insane.log.log(Level.SEVERE, "[Insane] SQL Exception", ex);
				return null;
			}
		}
		return column;
	}

	/**
	 * Utestet! Metode for � hente ut flere rader og kolonner fra en database
	 * 
	 * @param query
	 *            (String) Sp�rringen
	 * @param array
	 *            (Object[]) Et array som inneholder alle variabler som skal
	 *            settes i sp�rringen
	 * @return ArrayList<ArrayList<String>> som inneholder all data
	 */
	public ArrayList<ArrayList<String>> getRows(String query, Object[] array) {
		ArrayList<ArrayList<String>> rows = new ArrayList<ArrayList<String>>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int counter = 1; // Definerer indeks som blir brukt i sp�rringen. Skal
							// starte p� 1.
		int rowCounter = 0;

		if (array != null) {
			try {
				// conn = sqlConnector.getConnection();
				conn = this.sqlConnection;
				ps = conn.prepareStatement(query);

				// Her kj�rer man inn variablene
				for (Object o : array) {
					if (o instanceof Integer) {
						ps.setInt(counter, (Integer) o); // indeks og object som
															// blir castet
					} else if (o instanceof String) {
						ps.setString(counter, (String) o);
					} else {
						Insane.log
								.log(Level.SEVERE,
										"[Insane] Nullobjekt i mysql-handler. (getRows)");
						return null;
					}
					counter++; // �ker indeks
				}
				
				rs = ps.executeQuery();

				if (rs.next()) {
					rows.add(new ArrayList<String>());
					for (int i = 1; i <= array.length; i++) {
						rows.get(rowCounter).add(rs.getString(i));
					}
					rowCounter++;
				}
				
				array = null;
				rs.close();
				ps.close();
				checkWarnings();
				// conn.close();
			} catch (SQLException ex) {
				Insane.log.log(Level.SEVERE, "[Insane] SQL Exception", ex);
				return null;
			}
		} else {
			Insane.log.log(Level.SEVERE, Thread.currentThread()
					.getStackTrace()[0].getMethodName() + " - array is null");
			return null;
		}
		return rows;
	}

	/**
	 * Checks if the string contains any non-alphabet characters or _
	 * 
	 * @param string
	 *            The string to check.
	 * @return true if it doesn't any non-alphabet characters or _, false if it does.
	 */
	public static boolean checkString(String string) {
		
		  for(int i = 0; i < string.length(); i++) {
		  if(!Character.isLetterOrDigit(string.codePointAt(i))) { 
				  if(string.charAt(i) != '_') {
					  return false; 
				  }
			  } 
		  }
		 
		return true;
	}

}