import java.io.IOException;
import java.sql.SQLException;

import org.apache.derby.tools.sysinfo;

public class Scanner {

	public static void main(String[] args) throws IOException, SQLException {

		instruc();
		DataBase freezer = new DataBase();

		while (true) {
			System.out.println("Geben Sie einen Barcode ein: "+ "x" + freezer.getMult());
			String input = In.readWord();

			String barcode = interpretInput(input, freezer);

			if (barcode != null) {
				if (freezer.isOfflineAvail(barcode)) {
					Out.println("Offline Data Acquisition");
					freezer.offlineInsert(barcode);
				} else {
					Out.println("Online Data Acquisition");
					Groceries groc = Codecheck.loadInformation(barcode);
					if (groc == null) { 
						
						
						System.out.println("Offline Codeerfassung deaktiviert!");
						/*
						System.out.println("");
						System.out.println("Geben Sie den Namen des Produktes ein:");
						In.readLine();
						String name = In.readLine();
						groc = new Groceries(barcode, name, 1, "", "");
						*/
						
					}
					freezer.insert(groc);
				}
			}
			print(freezer);
			System.out.println();
		}
	}

	private static void print(DataBase data) throws SQLException {
		data.printTable();
	}

	private static void instruc() {
		Out.println("This is a barcode scanner application");
	}

	private static String interpretInput(String input, DataBase freezer) throws SQLException {

		if (input.equals("deleteAll")) {
			freezer.deleteAll();
			return null;
		}
		if (input.equals("o")) {
			freezer.setMode(0);
			Out.println("Aktueller Modus 0 - Ausbuchen");
			return null;
		}
		if (input.equals("i")) {
			freezer.setMode(1);
			Out.println("Aktueller Modus 1 - Einbuchen");
			return null;
		}

		if (isNumeric(input)) {
			if (input.length() <= 2) {
				freezer.setMult(Integer.parseInt(input));
				System.out.println(input);
				return null;
			} else {
				return input;
			}
		}

		return null;
	}

	private static boolean isNumeric(String input) {
		for (int i = 0; i < input.length(); i++) {
			if (input.charAt(i) < 48 || input.charAt(i) > 57) {
				return false;
			}
		}
		return true;

	}
}
