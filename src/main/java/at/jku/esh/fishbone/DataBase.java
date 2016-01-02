package at.jku.esh.fishbone;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import at.jku.esh.fishbone.goods.Groceries;

// TODO: Auto-generated Javadoc
/**
 * The Class DataBase.
 */
public class DataBase {

	/**
	 * The db settings currently these settings are hardcoded
	 */
	// TODO: include some kind of configuration

//	  SETTINGS FOR DERBY
	 private String dbURL = "jdbc:derby:/home/richard/MyDB;create=true";
	
	 /** The user. */
	 private String user = "admin";
	
	 /** The password. */
	 private String password = "password";

	// SETTINGS FOR DERBY Raspi
	// private String dbURL = "jdbc:derby:/home/pi/MyDB;create=true";
	// private String user = "admin";
	// private String password = "password";

	// SETTINGS FOR MySQL XAMPP
//	private String dbURL = "jdbc:mysql://127.0.0.1:3306/";
//	private String user = "root";
//	private String password = "";

	// SETTINGS FOR Networked MySQL RaspberryPi
	// private String dbURL = "jdbc:mysql://193.171.36.49:3306/BARCODESCANLIB";
	// private String user = "dbuser";
	// private String password = "password";

	/** The mode. */
	private int mode = 1; // 1 - Increase | 0 - Decrease

	/** The mult. */
	private int mult = 1; // Multiplier for products

	/**
	 * Instantiates a new data base.
	 */
	public DataBase() {

		// try {
		// Class.forName("com.mysql.jdbc.Driver");
		// } catch (ClassNotFoundException e) {
		// System.out.println("Where is your MySQL JDBC Driver?");
		// e.printStackTrace();
		// return;
		// }

	}

	/**
	 * Prints the table to the console.
	 *
	 * @throws SQLException
	 *             the SQL exception
	 */
	public void printTable() throws SQLException {

		Statement stmt = null;
		Connection con = null;
		try {
			con = getDBConnection();
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM BARCODESCANLIB.BARCODES");
			System.out.println(
					String.format("%-15s %-40s %-3s %-25s %-60s", "Barcode", "Name", "#", "Beschreibung", "Bild"));
			System.out.println(
					"--------------------------------------------------------------------------------------------");
			while (rs.next()) {
				String ISBN = rs.getString("BARCODE");
				String name = rs.getString("NAME");
				int count = rs.getInt("COUNT");
				String info = rs.getString("INFO");
				String picture = rs.getString("PICTURE");
				System.out.println(String.format("%-15s %-40s %-3d %-25s %-60s", ISBN, trimToSize(name, 39), count,
						trimToSize(info, 24), picture));
			}

		} catch (SQLException e) {
		} finally {
			if (stmt != null) {
				stmt.close();
				con.close();
			}
		}

	}

	/**
	 * Data vector.
	 *
	 * @return the list
	 * @throws SQLException
	 *             the SQL exception
	 */

	public List<List<String>> dataVector() throws SQLException {

		List<List<String>> matrix = new ArrayList<List<String>>();

		Statement stmt = null;
		Connection con = null;
		try {
			con = getDBConnection();
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM BARCODESCANLIB.BARCODES");
			System.out.println(
					String.format("%-15s %-40s %-3s %-25s %-60s", "Barcode", "Name", "#", "Beschreibung", "Bild"));
			System.out.println(
					"--------------------------------------------------------------------------------------------");
			while (rs.next()) {

				String ISBN = rs.getString("BARCODE");
				String name = rs.getString("NAME");
				String count = rs.getString("COUNT");
				String info = rs.getString("INFO");
				String picture = rs.getString("PICTURE");

				Vector<String> vector = new Vector<>(3);
				vector.add(ISBN);
				vector.add(name);
				vector.add(count);
				matrix.add(vector);

				System.out.println(String.format("%-15s %-40s %-3s %-25s %-60s", ISBN, trimToSize(name, 39), count,
						trimToSize(info, 24), picture));
			}

		} catch (SQLException e) {
		} finally {
			if (stmt != null) {
				stmt.close();
				con.close();
			}
		}
		return matrix;
	}

	/**
	 * Exists.
	 *
	 * @param barcode
	 *            the barcode
	 * @return True if Barcode is already in the SQL Database Table
	 * @throws SQLException
	 *             the SQL exception
	 */
	public boolean exists(String barcode) throws SQLException {
		Statement stmt = null;
		Connection con = null;
		String query = "SELECT * FROM BARCODESCANLIB.BARCODES WHERE BARCODE =";
		query = query + "'" + barcode + "'";
		boolean flag = false;

		try {
			con = getDBConnection();
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			if (rs.next()) {
				flag = true;
			}

		} catch (SQLException e) {
		} finally {
			if (stmt != null) {
				stmt.close();
				con.close();
			}
		}

		return flag;
	}

	/**
	 * Deletes all entries in the table.
	 *
	 * @throws SQLException
	 *             the SQL exception
	 */
	public void deleteAll() throws SQLException {
		Statement stmt = null;
		Connection con = null;

		try {
			con = getDBConnection();
			stmt = con.createStatement();
			stmt.executeUpdate("DELETE FROM BARCODESCANLIB.BARCODES");

		} catch (SQLException e) {
		} finally {
			if (stmt != null) {
				stmt.close();
				con.close();
			}
		}
	}

	/**
	 * Inserts a Grocerie groc.
	 *
	 * @param groc
	 *            the groc
	 * @throws SQLException
	 *             the SQL exception
	 */
	public void insert(Groceries groc) throws SQLException {
		if (groc != null) {

			if (!exists(groc.getISBN()) && mode == 1) {
				Statement stmt = null;
				Connection con = null;
				try {
					con = getDBConnection();
					stmt = con.createStatement();
					String query = String.format(
							"INSERT INTO BARCODESCANLIB.BARCODES(barcode, name,count,info, picture) VALUES('%s','%s',%d,'%s','%s')",
							groc.getISBN(), groc.getName(), groc.getCount(), groc.getInfo255(), groc.getPicSource());

					stmt.executeUpdate(query);

				} catch (SQLException e) {
				} finally {
					if (stmt != null) {
						stmt.close();
						con.close();
					}

				}
				if (!isOfflineAvail(groc.getISBN())) {
					try {
						con = getDBConnection();
						stmt = con.createStatement();
						String query = String.format(
								"INSERT INTO BARCODESCANLIB.BARCODESOFFL(barcode, name,count,info, picture) VALUES('%s','%s',1,'%s','%s')",
								groc.getISBN(), groc.getName(), groc.getInfo255(), groc.getPicSource());

						stmt.executeUpdate(query);

					} catch (SQLException e) {
					} finally {
						if (stmt != null) {
							stmt.close();
							con.close();
						}

					}
				}
			} else { // In-/ Decrease count field

				Statement stmt = null;
				Connection con = null;
				String query = "SELECT * FROM BARCODESCANLIB.BARCODES WHERE BARCODE =";
				query = query + "'" + groc.getISBN() + "'";

				try {
					con = getDBConnection();
					stmt = con.createStatement();
					stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
					ResultSet rs = stmt.executeQuery(query);

					while (rs.next()) {
						int i = rs.getInt("COUNT");
						if (mode == 1) { // Increase
							i = i + mult;
						} else if (mode == 0) { // Decrease
							i = i - mult;
							if (i <= 0) {
								rs.deleteRow();
								break;
							}
						}
						rs.updateInt("count", i);
						rs.updateRow();
						rs.updateRow();
					}

				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					if (stmt != null) {
						stmt.close();
						con.close();
					}
				}
			}

		}
	}

	/**
	 * Gets the DB connection.
	 *
	 * @return Database Connection
	 */
	private Connection getDBConnection() {

		try {

			Connection con = DriverManager.getConnection(dbURL, user, password);
			return con;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	/**
	 * Sets the mode.
	 *
	 * @param mode
	 *            the new mode
	 */
	public void setMode(int mode) {
		if (mode == 1 || mode == 0) {
			this.mode = mode;
		}
	}

	/**
	 * Trim to size.
	 *
	 * @param s
	 *            the s
	 * @param size
	 *            the size
	 * @return the string
	 */
	private static String trimToSize(String s, int size) {

		if (s.length() >= size) {
			s = s.substring(0, size);
		}

		return s;
	}

	/**
	 * Checks for offline availability of the product.
	 *
	 * @param barcode
	 *            the barcode
	 * @return true, if is offline avail
	 * @throws SQLException
	 *             the SQL exception
	 */
	public boolean isOfflineAvail(String barcode) throws SQLException {

		Statement stmt = null;
		Connection con = null;
		String query = "SELECT * FROM BARCODESCANLIB.BARCODESOFFL WHERE BARCODE =";
		query = query + "'" + barcode + "'";
		boolean flag = false;

		try {
			con = getDBConnection();
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			if (rs.next()) {
				flag = true;
			}

		} catch (SQLException e) {
		} finally {
			if (stmt != null) {
				stmt.close();
				con.close();
			}
		}

		return flag;
	}

	/**
	 * Offline insert copies the product from the offline data source into the
	 * fridge.
	 *
	 * @param barcode
	 *            the barcode
	 * @throws SQLException
	 *             the SQL exception
	 */
	public void offlineInsert(String barcode) throws SQLException {

		Statement stmt = null;
		Connection con = null;
		String query = "SELECT * FROM BARCODESCANLIB.BARCODESOFFL WHERE BARCODE =";
		query = query + "'" + barcode + "'";

		try {
			con = getDBConnection();
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {
				String ISBN = rs.getString("BARCODE");
				String name = rs.getString("NAME");
				int count = 1;
				String info = rs.getString("INFO");
				String picture = rs.getString("PICTURE");

				Groceries groc = new Groceries(ISBN, name, count, picture, info);
				insert(groc);
			}

		} catch (SQLException e) {
		} finally {
			if (stmt != null) {
				stmt.close();
				con.close();
			}
		}

	}

	/**
	 * Gets the multiplier of this product.
	 *
	 * @return the multiplier
	 */
	public int getMult() {
		return mult;
	}

	/**
	 * Sets the multiplier.
	 *
	 * @param mult
	 *            the mult to set
	 */
	public void setMult(int mult) {
		this.mult = mult;
	}
}
