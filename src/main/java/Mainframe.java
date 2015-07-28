/*
 * 
 */
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

// TODO: Auto-generated Javadoc
/**
 * The Class Mainframe starts the application with a GUI
 * Different to the Class Scanner which starts it as CLI
 */
public class Mainframe extends JFrame {

	private JPanel contentPane;
	private JTextField textBarcode;
	private JTable table;

	private DataBase freezer = new DataBase();
	
	/** Mode whether things are added or subtracted. 
	 * 1 - add
	 * 0 - subtract
	 * */
	private int mode = 1;

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				try {
					Mainframe frame = new Mainframe();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Mainframe() {
		super("BarcodeScanLib");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JPanel buttonPane = new JPanel();

		JPanel inputPane = new JPanel();

		String[] titles = new String[] { "Barcode", "Name", "Anzahl" };
		final DefaultTableModel model = new DefaultTableModel(titles, 0);

		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					freezer.deleteAll();
					print(model);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnClear.setBounds(315, 157, 117, 25);

		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Close");
				System.exit(0);
			}
		});
		btnClose.setBounds(315, 231, 117, 25);
		contentPane.setLayout(new BorderLayout(0, 0));

		JLabel lblBarcode = new JLabel("Barcode");
		inputPane.add(lblBarcode);
		/*
		 * Hinzufügen der Buttons in die ContentPane
		 */
		contentPane.add(buttonPane, BorderLayout.SOUTH);
		buttonPane.setLayout(new GridLayout(0, 2, 0, 0));

		JButton btnPrint = new JButton("Print");
		btnPrint.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				print(model);
			}
		});
		btnPrint.setBounds(315, 117, 68, 25);
		buttonPane.add(btnPrint);
		buttonPane.add(btnClear);

		JToggleButton tglbtnInout = new JToggleButton("In/Out");
		tglbtnInout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changeMode();
			}
		});
		tglbtnInout.setBounds(315, 194, 97, 25);
		buttonPane.add(tglbtnInout);
		buttonPane.add(btnClose);

		table = new JTable(model);
		table.setRowHeight(23);
		JScrollPane scroll = new JScrollPane(table);
		contentPane.add(scroll);

		textBarcode = new JTextField();
		textBarcode.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == 10) {
					insert();
					print(model);
					textBarcode.setText("");
				}
			}
		});
		textBarcode.setColumns(10);
		inputPane.add(textBarcode);
		contentPane.add(inputPane, BorderLayout.NORTH);
		contentPane.validate();
		print(model);
	}

	/**
	 * Checks if String is numeric.
	 *
	 * @param String input 
	 * @return true, if it is numeric
	 */
	private static boolean isNumeric(String input) {
		for (int i = 0; i < input.length(); i++) {
			if (input.charAt(i) < 48 || input.charAt(i) > 57) {
				return false;
			}
		}
		return true;

	}

	/**
	 * Prints/draws the current table
	 * (GUI Operation).
	 *
	 * @param model
	 */
	private void print(final DefaultTableModel model) {
		try {
			// freezer.printTable();
			deleteAllData(model);
			ArrayList<Vector> data = freezer.dataVector();
			Iterator itr = data.iterator();

			while (itr.hasNext()) {
				Object element = itr.next();
				model.addRow((Vector) element);
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/** 
	 * Method for inserting goods into the freezer
	 */
	private void insert() {

		String barcode = textBarcode.getText();

		if (barcode != "" && isNumeric(barcode)) {
			try {
				if (freezer.isOfflineAvail(barcode)) {
					Out.println("Offline Data Acquisition");
					freezer.offlineInsert(barcode);
				} else {
					Out.println("Online Data Acquisition");
					Groceries groc = Codecheck.loadInformation(barcode);
					if (groc == null) {

						System.out.println("Offline Codeerfassung deaktiviert!");
						/*
						 * System.out.println(""); System.out.println(
						 * "Geben Sie den Namen des Produktes ein:");
						 * In.readLine(); String name = In.readLine(); groc =
						 * new Groceries(barcode, name, 1, "", "");
						 */
					}
					freezer.insert(groc);
				}
				freezer.printTable();

			} catch (SQLException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

	}

	/**
	 * Change mode.
	 */
	private void changeMode() {
		if (mode == 1)
			mode = 0;
		else if (mode == 0)
			mode = 1;

		freezer.setMode(mode);
	}

	/**
	 * Delete all data in the TableModel. 
	 * @param DefaultTableModel model
	 */
	private static void deleteAllData(DefaultTableModel model) {
 
		int size = model.getRowCount();
		for (int i = size - 1; i >= 0; i--) {
			model.removeRow(i);
		}
	}
}
