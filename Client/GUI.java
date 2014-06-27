import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.javaswingcomponents.calendar.JSCCalendar;
import com.javaswingcomponents.calendar.cellrenderers.CalendarCellRenderer;
import com.javaswingcomponents.calendar.cellrenderers.CellRendererComponentParameter;
import com.javaswingcomponents.calendar.listeners.CalendarSelectionEvent;
import com.javaswingcomponents.calendar.listeners.CalendarSelectionEventType;
import com.javaswingcomponents.calendar.listeners.CalendarSelectionListener;
import com.javaswingcomponents.calendar.model.AbstractCalendarModel;
import com.javaswingcomponents.calendar.model.DayOfWeek;
import com.javaswingcomponents.calendar.model.Holiday;
import com.javaswingcomponents.datepicker.JSCDatePicker;

import de.shaoranlaos.dbAPI.Gruppe;
import de.shaoranlaos.dbAPI.Person;
import de.shaoranlaos.dbAPI.Termin;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class GUI {

	// werte fr die maximal zulässigen längen der eingaben
	int laenge_nickname = 15;
	int laenge_email = 40;
	int laenge_name = 20;
	int laenge_vname = 20;
	int laenge_gruppenname = 15;
	int laenge_ort = 15;
	int laenge_essen = 50;

	private JFrame frmMeetEat;
	private JPanel panelAnmelden;
	private JPanel panelMain;
	private JLabel lblWillkommen;
	private JTabbedPane tabbedPane;
	private JTextField txtName;
	private JTextField txtNickname;
	private JTextField txtVorname;
	private JTextField txtName3;
	private JTextField txtNachname;
	private JTextField txtOrt;
	private JTextField txtOrt2;
	private JTextField txtNickname2;
	private JTextField txtVorname2;
	private JTextField txtNachname2;
	private JTextField txtOrt3;
	private JTextField txtDatumZeit;
	private JTextField txtGruppe;
	private JTextField txtEssen;
	private JTextField txtEssen2;
	private JTextField txtEssen3;
	private JPasswordField pwdPasswort;
	private JPasswordField pwdPasswort2;
	private JPasswordField pwdPasswortWdh;
	private JPasswordField pwdPasswort3;
	private JPasswordField pwdPasswortWdh2;
	private JComboBox comboTermin;
	private JComboBox comboNutzer;
	private JComboBox comboLeiter;
	private JComboBox comboRechte;
	private JButton btnAnmelden;
	private JButton btnAbmelden;
	private JButton btnAendern3;
	private JButton btnAnfragen;
	private JButton btnErstellen3;
	private JButton btnHinzufuegen;
	private JButton btnAendern;
	private JButton btnLoeschen;
	private JButton btnAendern2;
	private JButton btnErstellen;
	private JButton btnErstellen2;
	private JButton btnLoeschen2;
	private JButton btnAendern4;
	private JButton btnUeberspringen;
	private JButton btnAussetzen;
	private JButton btnAendern5;
	private JButton btnLoeschen3;
	private JSCCalendar kalender;
	private JSCCalendar geburtstag;
	private JSCCalendar geburtstag2;
	private JSCDatePicker dateGeburtstag;
	private JSCDatePicker dateGeburtstag2;
	private JSpinner spinDatumZeit;
	private JSpinner spinDatumZeit2;
	private JList listTermine;
	private JList listMitglieder;
	private JList listPersonen;
	private JList listGruppen;
	private Client c;
	private int rechte = 3;
	private JList listTermine2;
	private JTextField txtEMail;
	private JTextField txtEMail2;
	private JComboBox comboGruppe;
	private JLabel lblGruppe2;
	private JScrollPane scrollPane_1;
	private JScrollPane scrollPane_2;
	private JScrollPane scrollPane_3;
	private JScrollPane scrollPane_4;
	private JTextField txtReihenfolge;
	private JTextField txtGruppenname;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frmMeetEat.setLocationRelativeTo(null);
					window.frmMeetEat.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		c = new Client();

		if (!c.Init())
			JOptionPane
					.showMessageDialog(
							frmMeetEat,
							"Es konnte keine Verbindung zum Server hergestellt werden.",
							"Anmelden", JOptionPane.PLAIN_MESSAGE);

		ladeTermine();

		frmMeetEat = new JFrame();
		frmMeetEat.setFont(new Font("Dialog", Font.PLAIN, 12));
		frmMeetEat.setTitle("Meet & Eat");
		frmMeetEat.setResizable(false);
		frmMeetEat.setBounds(100, 100, 900, 550);
		frmMeetEat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmMeetEat.getContentPane().setLayout(new CardLayout(0, 0));
		frmMeetEat.getContentPane().setPreferredSize(new Dimension(894, 522));
		frmMeetEat.pack();

		// Anmelden

		panelAnmelden = new JPanel();
		frmMeetEat.getContentPane().add(panelAnmelden, "name_17458504149082");
		panelAnmelden.setLayout(null);

		txtName = new JTextField();
		txtName.setBounds(359, 169, 200, 25);
		panelAnmelden.add(txtName);
		txtName.setColumns(10);

		JLabel lblName = new JLabel("Name");
		lblName.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblName.setBounds(241, 174, 100, 15);
		panelAnmelden.add(lblName);

		JLabel lblPasswort = new JLabel("Passwort");
		lblPasswort.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblPasswort.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPasswort.setBounds(241, 206, 100, 15);
		panelAnmelden.add(lblPasswort);

		btnAnmelden = new JButton("Anmelden");
		btnAnmelden.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnAnmelden.setBounds(439, 238, 120, 25);
		panelAnmelden.add(btnAnmelden);

		pwdPasswort = new JPasswordField();
		pwdPasswort.setBounds(359, 201, 200, 25);
		panelAnmelden.add(pwdPasswort);

		// Main

		panelMain = new JPanel();
		frmMeetEat.getContentPane().add(panelMain, "name_18399420798280");
		panelMain.setVisible(false);
		panelMain.setLayout(null);

		lblWillkommen = new JLabel("Angemeldet als *name* (*rechte*)");
		lblWillkommen.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblWillkommen.setHorizontalAlignment(SwingConstants.RIGHT);
		lblWillkommen.setBounds(532, 7, 250, 15);
		panelMain.add(lblWillkommen);

		btnAbmelden = new JButton("Abmelden");
		btnAbmelden.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnAbmelden.setBounds(790, 5, 100, 19);
		panelMain.add(btnAbmelden);

		// Tabbed Pane

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setFont(new Font("Dialog", Font.PLAIN, 12));
		tabbedPane.setBounds(0, 7, 894, 515);
		panelMain.add(tabbedPane);

		// Uebersicht

		JPanel panelUebersicht = new JPanel();
		tabbedPane.addTab("\u00DCbersicht", null, panelUebersicht, null);
		panelUebersicht.setLayout(null);

		kalender = new JSCCalendar();
		kalender.setFont(new Font("Dialog", Font.PLAIN, 12));
		kalender.setBorder(new TitledBorder(null, "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		kalender.setBounds(12, 12, 570, 463);
		panelUebersicht.add(kalender);

		// Muss auskommentiert werden, um Designer zu benutzen
		/*changeTheAppearanceOfTheCells(kalender);
		addBusinessRules(kalender);
		listenToChangesOnTheCalendar(kalender);*/

		JPanel panelTermin = new JPanel();
		panelTermin.setBorder(new TitledBorder(null, "Termin",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelTermin.setBounds(594, 12, 283, 463);
		panelUebersicht.add(panelTermin);
		panelTermin.setLayout(null);

		JLabel lblDatumZeit3 = new JLabel("Datum/Zeit");
		lblDatumZeit3.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblDatumZeit3.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDatumZeit3.setBounds(12, 31, 75, 15);
		panelTermin.add(lblDatumZeit3);

		JLabel lblOrt3 = new JLabel("Ort");
		lblOrt3.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblOrt3.setHorizontalAlignment(SwingConstants.RIGHT);
		lblOrt3.setBounds(12, 68, 75, 15);
		panelTermin.add(lblOrt3);

		txtOrt3 = new JTextField();
		txtOrt3.setEditable(false);
		txtOrt3.setColumns(10);
		txtOrt3.setBounds(105, 63, 166, 25);
		panelTermin.add(txtOrt3);

		txtDatumZeit = new JTextField();
		txtDatumZeit.setEditable(false);
		txtDatumZeit.setColumns(10);
		txtDatumZeit.setBounds(105, 26, 166, 25);
		panelTermin.add(txtDatumZeit);

		txtGruppe = new JTextField();
		txtGruppe.setEditable(false);
		txtGruppe.setColumns(10);
		txtGruppe.setBounds(105, 100, 166, 25);
		panelTermin.add(txtGruppe);

		JLabel lblGruppe = new JLabel("Gruppe");
		lblGruppe.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblGruppe.setHorizontalAlignment(SwingConstants.RIGHT);
		lblGruppe.setBounds(12, 105, 75, 15);
		panelTermin.add(lblGruppe);

		txtEssen = new JTextField();
		txtEssen.setEditable(false);
		txtEssen.setColumns(10);
		txtEssen.setBounds(105, 137, 166, 25);
		panelTermin.add(txtEssen);

		JLabel lblEssen = new JLabel("Essen");
		lblEssen.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblEssen.setHorizontalAlignment(SwingConstants.RIGHT);
		lblEssen.setBounds(12, 142, 75, 15);
		panelTermin.add(lblEssen);

		// Terminverwaltung

		// if (rechte >= 1) {

		JPanel panelTerminverwaltung = new JPanel();
		tabbedPane
				.addTab("Terminverwaltung", null, panelTerminverwaltung, null);
		panelTerminverwaltung.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 14, 400, 461);
		panelTerminverwaltung.add(scrollPane);

		listTermine = new JList();
		scrollPane.setViewportView(listTermine);
		updateTerminList();
		listTermine.setFont(new Font("Dialog", Font.PLAIN, 12));
		listTermine.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JPanel panelTerminAendern = new JPanel();
		panelTerminAendern.setBorder(new TitledBorder(null,
				"Termin \u00E4ndern", TitledBorder.LEADING, TitledBorder.TOP,
				null, null));
		panelTerminAendern.setBounds(424, 14, 453, 183);
		panelTerminverwaltung.add(panelTerminAendern);
		panelTerminAendern.setLayout(null);

		JLabel lblDatumZeit = new JLabel("Datum/Zeit");
		lblDatumZeit.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblDatumZeit.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDatumZeit.setBounds(12, 31, 75, 15);
		panelTerminAendern.add(lblDatumZeit);

		txtOrt = new JTextField();
		txtOrt.setColumns(10);
		txtOrt.setBounds(105, 63, 200, 25);
		panelTerminAendern.add(txtOrt);

		JLabel lblOrt = new JLabel("Ort");
		lblOrt.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblOrt.setHorizontalAlignment(SwingConstants.RIGHT);
		lblOrt.setBounds(12, 68, 75, 15);
		panelTerminAendern.add(lblOrt);

		spinDatumZeit = new JSpinner();
		spinDatumZeit.setFont(new Font("Dialog", Font.PLAIN, 12));
		spinDatumZeit.setModel(new SpinnerDateModel(new Date(-3600000L), null,
				null, Calendar.DAY_OF_YEAR));
		spinDatumZeit.setEditor(new JSpinner.DateEditor(spinDatumZeit,
				"dd.MM.yyyy HH:mm"));
		spinDatumZeit.setBounds(105, 26, 200, 25);
		panelTerminAendern.add(spinDatumZeit);

		btnAendern3 = new JButton("\u00C4ndern");
		btnAendern3.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnAendern3.setBounds(185, 137, 120, 25);
		panelTerminAendern.add(btnAendern3);

		txtEssen2 = new JTextField();
		txtEssen2.setColumns(10);
		txtEssen2.setBounds(105, 100, 200, 25);
		panelTerminAendern.add(txtEssen2);

		JLabel lblEssen2 = new JLabel("Essen");
		lblEssen2.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblEssen2.setHorizontalAlignment(SwingConstants.RIGHT);
		lblEssen2.setBounds(12, 105, 75, 15);
		panelTerminAendern.add(lblEssen2);

		// if (rechte >= 2) {

		JPanel panelAusserzyklischenTerminErstellen = new JPanel();
		panelAusserzyklischenTerminErstellen.setBorder(new TitledBorder(
				new LineBorder(new Color(184, 207, 229)),
				"Au\u00DFerzyklischen Termin erstellen", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panelAusserzyklischenTerminErstellen.setBounds(424, 291, 453, 183);
		panelTerminverwaltung.add(panelAusserzyklischenTerminErstellen);
		panelAusserzyklischenTerminErstellen.setLayout(null);

		txtOrt2 = new JTextField();
		txtOrt2.setColumns(10);
		txtOrt2.setBounds(105, 63, 200, 25);
		panelAusserzyklischenTerminErstellen.add(txtOrt2);

		spinDatumZeit2 = new JSpinner();
		spinDatumZeit2.setFont(new Font("Dialog", Font.PLAIN, 12));
		spinDatumZeit2.setModel(new SpinnerDateModel(new Date(-3600000L), null,
				null, Calendar.DAY_OF_YEAR));
		spinDatumZeit2.setEditor(new JSpinner.DateEditor(spinDatumZeit2,
				"dd.MM.yyyy HH:mm"));
		spinDatumZeit2.setBounds(105, 26, 200, 25);
		panelAusserzyklischenTerminErstellen.add(spinDatumZeit2);

		JLabel lblDatumZeit2 = new JLabel("Datum/Zeit");
		lblDatumZeit2.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblDatumZeit2.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDatumZeit2.setBounds(12, 31, 75, 15);
		panelAusserzyklischenTerminErstellen.add(lblDatumZeit2);

		JLabel lblOrt2 = new JLabel("Ort");
		lblOrt2.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblOrt2.setHorizontalAlignment(SwingConstants.RIGHT);
		lblOrt2.setBounds(12, 68, 75, 15);
		panelAusserzyklischenTerminErstellen.add(lblOrt2);

		btnErstellen3 = new JButton("Erstellen");
		btnErstellen3.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnErstellen3.setBounds(185, 137, 120, 25);
		panelAusserzyklischenTerminErstellen.add(btnErstellen3);

		txtEssen3 = new JTextField();
		txtEssen3.setColumns(10);
		txtEssen3.setBounds(105, 100, 200, 25);
		panelAusserzyklischenTerminErstellen.add(txtEssen3);

		JLabel lblEssen3 = new JLabel("Essen");
		lblEssen3.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblEssen3.setHorizontalAlignment(SwingConstants.RIGHT);
		lblEssen3.setBounds(12, 105, 75, 15);
		panelAusserzyklischenTerminErstellen.add(lblEssen3);

		JPanel panelTauschanfrage = new JPanel();
		panelTauschanfrage.setBorder(new TitledBorder(new LineBorder(new Color(
				184, 207, 229)), "Tauschanfrage", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panelTauschanfrage.setBounds(424, 209, 453, 70);
		panelTerminverwaltung.add(panelTauschanfrage);
		panelTauschanfrage.setLayout(null);

		comboTermin = new JComboBox();
		updateTerminComboBox();
		comboTermin.setFont(new Font("Dialog", Font.PLAIN, 12));
		comboTermin.setBounds(75, 26, 250, 25);
		panelTauschanfrage.add(comboTermin);

		btnAnfragen = new JButton("Anfragen");
		btnAnfragen.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnAnfragen.setBounds(337, 26, 104, 25);
		panelTauschanfrage.add(btnAnfragen);

		JLabel lblTermin = new JLabel("Termin");
		lblTermin.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblTermin.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTermin.setBounds(12, 30, 45, 15);
		panelTauschanfrage.add(lblTermin);
		// }
		// }

		// Gruppenverwaltung

		// if (rechte >= 2) {

		JPanel panelGruppenverwaltung = new JPanel();
		tabbedPane.addTab("Gruppenverwaltung", null, panelGruppenverwaltung,
				null);
		panelGruppenverwaltung.setLayout(null);

		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(12, 44, 400, 431);
		panelGruppenverwaltung.add(scrollPane_1);

		comboGruppe = new JComboBox();
		updateGruppeComboBox();
		comboGruppe.setFont(new Font("Dialog", Font.PLAIN, 12));
		comboGruppe.setBounds(75, 12, 337, 25);
		panelGruppenverwaltung.add(comboGruppe);

		listMitglieder = new JList();
		scrollPane_1.setViewportView(listMitglieder);
		updateMitgliedList();
		listMitglieder.setFont(new Font("Dialog", Font.PLAIN, 12));
		listMitglieder.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JPanel panelMitgliedHinzufuegen = new JPanel();
		panelMitgliedHinzufuegen.setBorder(new TitledBorder(null,
				"Mitglied hinzuf\u00FCgen", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panelMitgliedHinzufuegen.setBounds(424, 94, 453, 70);
		panelGruppenverwaltung.add(panelMitgliedHinzufuegen);
		panelMitgliedHinzufuegen.setLayout(null);

		comboNutzer = new JComboBox();
		comboNutzer.setFont(new Font("Dialog", Font.PLAIN, 12));
		updatePersonComboBox(comboNutzer);
		comboNutzer.setBounds(12, 26, 250, 25);
		panelMitgliedHinzufuegen.add(comboNutzer);

		btnHinzufuegen = new JButton("Hinzuf\u00FCgen");
		btnHinzufuegen.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnHinzufuegen.setBounds(274, 26, 120, 25);
		panelMitgliedHinzufuegen.add(btnHinzufuegen);

		JPanel panelRechteAendern = new JPanel();
		panelRechteAendern.setBorder(new TitledBorder(null,
				"Rechte \u00E4ndern", TitledBorder.LEADING, TitledBorder.TOP,
				null, null));
		panelRechteAendern.setBounds(424, 178, 453, 70);
		panelGruppenverwaltung.add(panelRechteAendern);
		panelRechteAendern.setLayout(null);

		comboRechte = new JComboBox();
		comboRechte.setFont(new Font("Dialog", Font.PLAIN, 12));
		comboRechte.setModel(new DefaultComboBoxModel(
				new String[] { "Mitglied", "Ansprechpartner", "Gruppenleiter",
						"Administrator" }));
		comboRechte.setSelectedIndex(0);
		comboRechte.setBounds(12, 26, 250, 25);
		panelRechteAendern.add(comboRechte);

		btnAendern = new JButton("\u00C4ndern");
		btnAendern.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnAendern.setBounds(274, 26, 120, 25);
		panelRechteAendern.add(btnAendern);

		JPanel panelMitgliedLoeschen = new JPanel();
		panelMitgliedLoeschen.setBorder(new TitledBorder(null,
				"Mitglied l\u00F6schen", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panelMitgliedLoeschen.setBounds(424, 260, 453, 70);
		panelGruppenverwaltung.add(panelMitgliedLoeschen);
		panelMitgliedLoeschen.setLayout(null);

		btnLoeschen = new JButton("L\u00F6schen");
		btnLoeschen.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnLoeschen.setBounds(12, 26, 120, 25);
		panelMitgliedLoeschen.add(btnLoeschen);

		lblGruppe2 = new JLabel("Gruppe");
		lblGruppe2.setHorizontalAlignment(SwingConstants.RIGHT);
		lblGruppe2.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblGruppe2.setBounds(12, 17, 55, 15);
		panelGruppenverwaltung.add(lblGruppe2);

		JPanel panelGruppenname = new JPanel();
		panelGruppenname.setLayout(null);
		panelGruppenname.setBorder(new TitledBorder(new LineBorder(new Color(
				184, 207, 229)), "Name", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panelGruppenname.setBounds(424, 12, 453, 70);
		panelGruppenverwaltung.add(panelGruppenname);

		btnAendern5 = new JButton("Ändern");
		btnAendern5.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnAendern5.setBounds(274, 26, 120, 25);
		panelGruppenname.add(btnAendern5);

		txtGruppenname = new JTextField();
		txtGruppenname.setColumns(10);
		txtGruppenname.setBounds(12, 26, 250, 25);
		panelGruppenname.add(txtGruppenname);
		// }

		// Account

		JPanel panelAccount = new JPanel();
		tabbedPane.addTab("Account", null, panelAccount, null);
		panelAccount.setLayout(null);

		JPanel panelAccountdatenAendern = new JPanel();
		panelAccountdatenAendern.setBorder(new TitledBorder(new LineBorder(
				new Color(184, 207, 229)), "Accountdaten \u00E4ndern",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelAccountdatenAendern.setBounds(12, 12, 325, 463);
		panelAccount.add(panelAccountdatenAendern);
		panelAccountdatenAendern.setLayout(null);

		txtNickname = new JTextField();
		txtNickname.setBounds(105, 26, 200, 25);
		panelAccountdatenAendern.add(txtNickname);
		txtNickname.setColumns(10);

		JLabel lblNickname = new JLabel("Nickname");
		lblNickname.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblNickname.setBounds(12, 31, 75, 15);
		panelAccountdatenAendern.add(lblNickname);
		lblNickname.setHorizontalAlignment(SwingConstants.RIGHT);

		JLabel lblVorname = new JLabel("Vorname");
		lblVorname.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblVorname.setBounds(12, 179, 75, 15);
		panelAccountdatenAendern.add(lblVorname);
		lblVorname.setHorizontalAlignment(SwingConstants.RIGHT);

		txtVorname = new JTextField();
		txtVorname.setBounds(105, 174, 200, 25);
		panelAccountdatenAendern.add(txtVorname);
		txtVorname.setColumns(10);

		btnAendern2 = new JButton("\u00C4ndern");
		btnAendern2.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnAendern2.setBounds(185, 285, 120, 25);
		panelAccountdatenAendern.add(btnAendern2);

		JLabel lblGeburtstag = new JLabel("Geburtstag");
		lblGeburtstag.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblGeburtstag.setHorizontalAlignment(SwingConstants.RIGHT);
		lblGeburtstag.setBounds(12, 253, 75, 15);
		panelAccountdatenAendern.add(lblGeburtstag);

		geburtstag = new JSCCalendar();
		dateGeburtstag = new JSCDatePicker(geburtstag);
		dateGeburtstag.setBounds(105, 248, 200, 25);
		panelAccountdatenAendern.add(dateGeburtstag);

		txtNachname = new JTextField();
		txtNachname.setColumns(10);
		txtNachname.setBounds(105, 211, 200, 25);
		panelAccountdatenAendern.add(txtNachname);

		JLabel lblNachname = new JLabel("Nachname");
		lblNachname.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblNachname.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNachname.setBounds(12, 216, 75, 15);
		panelAccountdatenAendern.add(lblNachname);

		pwdPasswort3 = new JPasswordField();
		pwdPasswort3.setBounds(105, 100, 200, 25);
		panelAccountdatenAendern.add(pwdPasswort3);

		JLabel lblPasswort3 = new JLabel("Passwort");
		lblPasswort3.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblPasswort3.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPasswort3.setBounds(12, 105, 75, 15);
		panelAccountdatenAendern.add(lblPasswort3);

		JLabel lblPasswortWdh2 = new JLabel("Passwort");
		lblPasswortWdh2.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblPasswortWdh2.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPasswortWdh2.setBounds(12, 142, 75, 15);
		panelAccountdatenAendern.add(lblPasswortWdh2);

		pwdPasswortWdh2 = new JPasswordField();
		pwdPasswortWdh2.setBounds(105, 137, 200, 25);
		panelAccountdatenAendern.add(pwdPasswortWdh2);

		txtEMail = new JTextField();
		txtEMail.setColumns(10);
		txtEMail.setBounds(105, 63, 200, 25);
		panelAccountdatenAendern.add(txtEMail);

		JLabel lblEMail = new JLabel("E-Mail");
		lblEMail.setHorizontalAlignment(SwingConstants.RIGHT);
		lblEMail.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblEMail.setBounds(12, 68, 75, 15);
		panelAccountdatenAendern.add(lblEMail);

		// Administration

		// if (rechte == 3) {

		JPanel panelAdministration = new JPanel();
		tabbedPane.addTab("Administration", null, panelAdministration, null);
		panelAdministration.setLayout(null);

		JPanel panelNutzer = new JPanel();
		panelNutzer.setBorder(new TitledBorder(new LineBorder(new Color(184,
				207, 229)), "Nutzer", TitledBorder.LEADING, TitledBorder.TOP,
				null, null));
		panelNutzer.setBounds(12, 12, 325, 463);
		panelAdministration.add(panelNutzer);
		panelNutzer.setLayout(null);

		btnErstellen = new JButton("Erstellen");
		btnErstellen.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnErstellen.setBounds(185, 285, 120, 25);
		panelNutzer.add(btnErstellen);

		txtNickname2 = new JTextField();
		txtNickname2.setColumns(10);
		txtNickname2.setBounds(105, 26, 200, 25);
		panelNutzer.add(txtNickname2);

		txtVorname2 = new JTextField();
		txtVorname2.setColumns(10);
		txtVorname2.setBounds(105, 174, 200, 25);
		panelNutzer.add(txtVorname2);

		txtNachname2 = new JTextField();
		txtNachname2.setColumns(10);
		txtNachname2.setBounds(105, 211, 200, 25);
		panelNutzer.add(txtNachname2);

		geburtstag2 = new JSCCalendar();
		dateGeburtstag2 = new JSCDatePicker(geburtstag2);
		dateGeburtstag2.setBounds(105, 248, 200, 25);
		panelNutzer.add(dateGeburtstag2);

		JLabel label = new JLabel("Geburtstag");
		label.setFont(new Font("Dialog", Font.PLAIN, 12));
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		label.setBounds(12, 253, 75, 15);
		panelNutzer.add(label);

		JLabel label_1 = new JLabel("Nachname");
		label_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		label_1.setHorizontalAlignment(SwingConstants.RIGHT);
		label_1.setBounds(12, 216, 75, 15);
		panelNutzer.add(label_1);

		JLabel label_2 = new JLabel("Vorname");
		label_2.setFont(new Font("Dialog", Font.PLAIN, 12));
		label_2.setHorizontalAlignment(SwingConstants.RIGHT);
		label_2.setBounds(12, 179, 75, 15);
		panelNutzer.add(label_2);

		JLabel label_3 = new JLabel("Nickname");
		label_3.setFont(new Font("Dialog", Font.PLAIN, 12));
		label_3.setHorizontalAlignment(SwingConstants.RIGHT);
		label_3.setBounds(12, 31, 75, 15);
		panelNutzer.add(label_3);

		JLabel lblPasswort2 = new JLabel("Passwort");
		lblPasswort2.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblPasswort2.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPasswort2.setBounds(12, 105, 75, 15);
		panelNutzer.add(lblPasswort2);

		JLabel lblPasswortWdh = new JLabel("Passwort");
		lblPasswortWdh.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblPasswortWdh.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPasswortWdh.setBounds(12, 142, 75, 15);
		panelNutzer.add(lblPasswortWdh);

		pwdPasswort2 = new JPasswordField();
		pwdPasswort2.setBounds(105, 100, 200, 25);
		panelNutzer.add(pwdPasswort2);

		pwdPasswortWdh = new JPasswordField();
		pwdPasswortWdh.setBounds(105, 137, 200, 25);
		panelNutzer.add(pwdPasswortWdh);

		txtEMail2 = new JTextField();
		txtEMail2.setColumns(10);
		txtEMail2.setBounds(105, 63, 200, 25);
		panelNutzer.add(txtEMail2);

		JLabel lblEMail2 = new JLabel("E-Mail");
		lblEMail2.setHorizontalAlignment(SwingConstants.RIGHT);
		lblEMail2.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblEMail2.setBounds(12, 68, 75, 15);
		panelNutzer.add(lblEMail2);

		btnLoeschen3 = new JButton("Löschen");
		btnLoeschen3.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnLoeschen3.setBounds(185, 422, 120, 25);
		panelNutzer.add(btnLoeschen3);

		scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(55, 322, 250, 88);
		panelNutzer.add(scrollPane_2);

		listPersonen = new JList();
		scrollPane_2.setViewportView(listPersonen);
		updatePersonList();
		listPersonen.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listPersonen.setFont(new Font("Dialog", Font.PLAIN, 12));

		JPanel panelGruppen = new JPanel();
		panelGruppen.setBorder(new TitledBorder(null, "Gruppen",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelGruppen.setBounds(349, 12, 503, 242);
		panelAdministration.add(panelGruppen);
		panelGruppen.setLayout(null);

		scrollPane_3 = new JScrollPane();
		scrollPane_3.setBounds(105, 100, 250, 88);
		panelGruppen.add(scrollPane_3);

		listGruppen = new JList();
		scrollPane_3.setViewportView(listGruppen);
		updateGruppeList();
		listGruppen.setFont(new Font("Dialog", Font.PLAIN, 12));
		listGruppen.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		btnErstellen2 = new JButton("Erstellen");
		btnErstellen2.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnErstellen2.setBounds(367, 26, 120, 25);
		panelGruppen.add(btnErstellen2);

		btnLoeschen2 = new JButton("L\u00F6schen");
		btnLoeschen2.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnLoeschen2.setBounds(367, 100, 120, 25);
		panelGruppen.add(btnLoeschen2);

		txtName3 = new JTextField();
		txtName3.setColumns(10);
		txtName3.setBounds(105, 26, 250, 25);
		panelGruppen.add(txtName3);

		JLabel lblName3 = new JLabel("Name");
		lblName3.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblName3.setHorizontalAlignment(SwingConstants.RIGHT);
		lblName3.setBounds(12, 30, 75, 15);
		panelGruppen.add(lblName3);

		JLabel lblLeiter = new JLabel("Leiter");
		lblLeiter.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblLeiter.setHorizontalAlignment(SwingConstants.RIGHT);
		lblLeiter.setBounds(12, 68, 75, 15);
		panelGruppen.add(lblLeiter);

		comboLeiter = new JComboBox();
		comboLeiter.setFont(new Font("Dialog", Font.PLAIN, 12));
		updatePersonComboBox(comboLeiter);
		comboLeiter.setBounds(105, 63, 250, 25);
		panelGruppen.add(comboLeiter);

		txtReihenfolge = new JTextField();
		txtReihenfolge
				.setToolTipText("Bitte Gruppen-IDs mit Komma getrennt eingeben, z.B. 1,2,3");
		txtReihenfolge.setColumns(10);
		txtReihenfolge.setBounds(105, 200, 250, 25);
		panelGruppen.add(txtReihenfolge);

		JLabel lblReihenfolge = new JLabel("Reihenfolge");
		lblReihenfolge.setHorizontalAlignment(SwingConstants.RIGHT);
		lblReihenfolge.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblReihenfolge.setBounds(12, 205, 75, 15);
		panelGruppen.add(lblReihenfolge);

		btnAendern4 = new JButton("Ändern");
		btnAendern4.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnAendern4.setBounds(367, 200, 120, 25);
		panelGruppen.add(btnAendern4);

		JPanel panelTermin2 = new JPanel();
		panelTermin2.setBorder(new TitledBorder(null,
				"Termin \u00FCberspringen/aussetzen", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panelTermin2.setBounds(349, 266, 503, 209);
		panelAdministration.add(panelTermin2);
		panelTermin2.setLayout(null);

		btnUeberspringen = new JButton("\u00DCberspringen");
		btnUeberspringen.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnUeberspringen.setBounds(367, 26, 120, 25);
		panelTermin2.add(btnUeberspringen);

		btnAussetzen = new JButton("Aussetzen");
		btnAussetzen.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnAussetzen.setBounds(367, 63, 120, 25);
		panelTermin2.add(btnAussetzen);

		scrollPane_4 = new JScrollPane();
		scrollPane_4.setBounds(105, 26, 250, 131);
		panelTermin2.add(scrollPane_4);

		listTermine2 = new JList();
		scrollPane_4.setViewportView(listTermine2);
		listTermine2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listTermine2.setFont(new Font("Dialog", Font.PLAIN, 12));
		// }

		eventHandler();
	}

	private void eventHandler() {
		// Anmelden
		btnAnmelden.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int r = c.anmeldenClient(txtName.getText(), new String(
							pwdPasswort.getPassword()));
					switch (r) {
					case 0:
						panelMain.setVisible(true);
						String[] rechte = { "Mitglied", "Ansprechpartner",
								"Gruppenleiter", "Administrator" };
						lblWillkommen.setText("Angemeldet als "
								+ txtName.getText() + " (" + rechte[c.rechte]
								+ ")");
						tabbedPane.setSelectedIndex(0);
						panelAnmelden.setVisible(false);
						break;
					case 1:
						JOptionPane
								.showMessageDialog(
										frmMeetEat,
										"Anmeldung fehlgeschlagen. Bitte berprfen Sie\nNutzername und Passwort auf korrekte Schreibweise.",
										"Anmelden", JOptionPane.PLAIN_MESSAGE);
						break;
					case 2:
						JOptionPane
								.showMessageDialog(
										frmMeetEat,
										"Es konnte keine Verbindung zum Server hergestellt werden.",
										"Anmelden", JOptionPane.PLAIN_MESSAGE);
						break;
					}
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}
		});

		// Abmelden
		btnAbmelden.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*
				 * txtName.setText(""); pwdPasswort.setText("");
				 * panelAnmelden.setVisible(true); panelMain.setVisible(false);
				 */
				System.exit(0);
			}
		});

		// Reiter gewechselt
		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				switch (tabbedPane.getTitleAt(tabbedPane.getSelectedIndex())) {
				case "bersicht":
				case "Terminverwaltung":
					ladeTermine();
					updateTerminList();
					break;
				}
			}
		});

		// Termin ausgewaehlt
		listTermine.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				Termin tmpTermin = (Termin) listTermine.getSelectedValue();
				if (tmpTermin instanceof Termin) {
					SimpleDateFormat format = new SimpleDateFormat(
							"dd.MM.yyyy HH:mm");
					DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
					DateFormat tf = new SimpleDateFormat("HH:mm");
					try {
						spinDatumZeit.setValue(format.parseObject(df
								.format(tmpTermin.datum)
								+ " "
								+ tf.format(tmpTermin.zeit)));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					txtOrt.setText(tmpTermin.ort);
					txtEssen2.setText(tmpTermin.essen);
					updateTerminComboBox();
				}
			}
		});

		// Termin aendern
		btnAendern3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Termin tmpTermin = (Termin) listTermine.getSelectedValue();
				try {
					int r = c.aendernTermin(tmpTermin.id,
							(java.sql.Date) spinDatumZeit.getValue(),
							(java.sql.Time) spinDatumZeit.getValue(),
							txtOrt.getText(), txtEssen2.getText());
					switch (r) {
					case 0:
						JOptionPane.showMessageDialog(frmMeetEat,
								"Termin erfolgreich geändert.",
								"Termin ändern", JOptionPane.PLAIN_MESSAGE);
						break;
					case 1:
						JOptionPane.showMessageDialog(frmMeetEat,
								"Termin konnte nicht geändert werden.",
								"Termin ändern", JOptionPane.PLAIN_MESSAGE);
						break;
					case 2:
						JOptionPane.showMessageDialog(frmMeetEat,
								"Eingabe für Ort zu lang. Maximal " + laenge_ort + "Zeichen.", "Termin ändern",
								JOptionPane.PLAIN_MESSAGE);
						break;
					case 3:
						JOptionPane.showMessageDialog(frmMeetEat,
								"Eingabe für Essen zu lang. Maximal " + laenge_essen + "Zeichen.", "Termin ändern",
								JOptionPane.PLAIN_MESSAGE);
						break;
					case 4:
						JOptionPane
								.showMessageDialog(
										frmMeetEat,
										"Es gibt bereits einen Termin an diesem Datum.",
										"Termin ändern",
										JOptionPane.PLAIN_MESSAGE);
						break;
					case 5:
						JOptionPane
								.showMessageDialog(
										frmMeetEat,
										"Termine können nur bis spätestens 3 Tage vorher geändert werden.",
										"Termin ändern",
										JOptionPane.PLAIN_MESSAGE);
						break;
					}
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					JOptionPane.showMessageDialog(frmMeetEat,
							"Keine Serververbindung.", "Termin ändern",
							JOptionPane.PLAIN_MESSAGE);
				}
			}
		});

		// Tauschanfrage
		btnAnfragen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Termin tmpTermin1 = (Termin) listTermine.getSelectedValue();
				Termin tmpTermin2 = (Termin) comboTermin.getSelectedItem();
				try {
					int r = c.erstellenTauschanfrage(tmpTermin1.id,
							tmpTermin2.id);

					switch (r) {
					case -1:
						JOptionPane.showMessageDialog(frmMeetEat,
								"Tauschanfrage existiert bereits.",
								"Tauschanfrage", JOptionPane.PLAIN_MESSAGE);
						break;
					case -2:
						JOptionPane.showMessageDialog(frmMeetEat,
								"Tauschanfrage konnte nicht erstellt werden.",
								"Tauschanfrage", JOptionPane.PLAIN_MESSAGE);
						break;
					case -3:
						JOptionPane
								.showMessageDialog(
										frmMeetEat,
										"Zieltermin wurde bereits getauscht.\nTermine dürfen nur einmal getauscht werden.",
										"Tauschanfrage",
										JOptionPane.PLAIN_MESSAGE);
						break;
					default:
						JOptionPane.showMessageDialog(frmMeetEat,
								"Tauschanfrage erfolgreich erstellt.",
								"Tauschanfrage", JOptionPane.PLAIN_MESSAGE);
					}
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					JOptionPane.showMessageDialog(frmMeetEat,
							"Keine Serververbindung.", "Tauschanfrage",
							JOptionPane.PLAIN_MESSAGE);
				}
			}
		});

		// Ausserzyklischen Termin erstellen
		btnErstellen3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Gruppe tmpGruppe = (Gruppe) comboGruppe.getSelectedItem();
				try {
					int r = c.einfuegenAuZykTermin(
							(java.sql.Date) spinDatumZeit2.getValue(),
							(java.sql.Time) spinDatumZeit2.getValue(),
							txtOrt2.getText(), txtEssen3.getText(),
							tmpGruppe.id);

					switch (r) {
					case -1:
						JOptionPane.showMessageDialog(frmMeetEat,
								"Termin existiert bereits.",
								"Außerzyklischer Termin",
								JOptionPane.PLAIN_MESSAGE);
						break;
					case -2:
						JOptionPane.showMessageDialog(frmMeetEat,
								"Termin konnte nicht erstellt werden.",
								"Außerzyklischer Termin",
								JOptionPane.PLAIN_MESSAGE);
						break;
					case 2:
						JOptionPane.showMessageDialog(frmMeetEat,
								"Eingabe für Ort zu lang. Maximal " + laenge_ort + "Zeichen.",
								"Außerzyklischer Termin",
								JOptionPane.PLAIN_MESSAGE);
						break;
					case 3:
						JOptionPane.showMessageDialog(frmMeetEat,
								"Eingabe für Essen zu lang. Maximal " + laenge_essen + "Zeichen.",
								"Außerzyklischer Termin",
								JOptionPane.PLAIN_MESSAGE);
						break;
					case 4:
						JOptionPane
								.showMessageDialog(
										frmMeetEat,
										"Termine können nur mindestens 3 Tage vorher erstellt werden.",
										"Außerzyklischer Termin",
										JOptionPane.PLAIN_MESSAGE);
						break;
					default:
						JOptionPane.showMessageDialog(frmMeetEat,
								"Außerzyklischen Termin erfolgreich erstellt.",
								"Außerzyklischer Termin",
								JOptionPane.PLAIN_MESSAGE);
					}
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					JOptionPane
							.showMessageDialog(frmMeetEat,
									"Keine Serververbindung.",
									"Außerzyklischer Termin",
									JOptionPane.PLAIN_MESSAGE);
				}
			}
		});

		// Gruppe ausgewaehlt
		comboGruppe.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				updateMitgliedList();
			}
		});

		// Mitglied ausgewaehlt
		listMitglieder.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				Person tmpPerson = (Person) listMitglieder.getSelectedValue();
				if (tmpPerson instanceof Person)
					comboRechte.setSelectedIndex(tmpPerson.rechte);
			}
		});

		// Gruppenmitglied hinzufuegen
		btnHinzufuegen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Person tmpPerson = (Person) comboNutzer.getSelectedItem();
				try {
					if (c.hinzufuegenMitglied(tmpPerson.id) == 0) {
						JOptionPane.showMessageDialog(frmMeetEat,
								"Nutzer erfolgreich hinzugefgt.",
								"Gruppenmitglied hinzufügen",
								JOptionPane.PLAIN_MESSAGE);
					} else {
						JOptionPane
								.showMessageDialog(
										frmMeetEat,
										"Nutzer konnte nicht hinzugefgt werden. Es gab einen Fehler in der Datenbank",
										"Gruppenmitglied hinzufügen",
										JOptionPane.PLAIN_MESSAGE);
					}
					updatePersonComboBox(comboNutzer);
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					JOptionPane.showMessageDialog(frmMeetEat,
							"Keine Serververbindung.",
							"Gruppenmitglied hinzufügen",
							JOptionPane.PLAIN_MESSAGE);
				}
			}
		});

		// Rechte aendern
		btnAendern.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Person tmpPerson = (Person) listMitglieder.getSelectedValue();
				int r = comboRechte.getSelectedIndex();
				try {
					if (c.vergebenRechte(tmpPerson.id, r) == 0)
						JOptionPane.showMessageDialog(frmMeetEat,
								"Rechtevergabe war erfolgreich!",
								"Rechtevergabe", JOptionPane.PLAIN_MESSAGE);
					else
						JOptionPane.showMessageDialog(frmMeetEat,
								"Fehler bei der Rechtevergabe",
								"Rechtevergabe", JOptionPane.PLAIN_MESSAGE);
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					JOptionPane.showMessageDialog(frmMeetEat,
							"Keine Serververbindung.", "Rechtevergabe",
							JOptionPane.PLAIN_MESSAGE);
				}
			}
		});

		// Gruppenmitglied loeschen
		btnLoeschen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Person tmpPerson = (Person) listMitglieder.getSelectedValue();
				try {
					if (c.loeschenMitglied(tmpPerson.id) == 0) {
						JOptionPane.showMessageDialog(frmMeetEat,
								"Das Mitglied wurde erfolgreich gelöscht.",
								"Mitglied löschen", JOptionPane.PLAIN_MESSAGE);
					} else
						JOptionPane
								.showMessageDialog(
										frmMeetEat,
										"Es ist ein Fehler beim löschen des Mitgliedes aufgetreten.",
										"Mitglied löschen",
										JOptionPane.PLAIN_MESSAGE);
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					JOptionPane.showMessageDialog(frmMeetEat,
							"Keine Serververbindung.", "Mitglied löschen",
							JOptionPane.PLAIN_MESSAGE);
				}
			}
		});

		// Accountdaten aendern
		btnAendern2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int r;
				String output = "";
				String messageDialog[] = new String[5];
				try {
					if (!txtNickname.getText().isEmpty()) {
						r = c.aendernNickname(txtNickname.getText());
						switch (r) {
						case 0:
							updateMitgliedList();
							messageDialog[0] = "Nickname";
							// JOptionPane.showMessageDialog(frmMeetEat,
							// "Nickname erfolgreich geändert.", "ändern",
							// JOptionPane.PLAIN_MESSAGE);
							break;
						case 1:
							JOptionPane.showMessageDialog(frmMeetEat,
									"Nickname leider schon vergeben.",
									"Accountdaten ändern",
									JOptionPane.PLAIN_MESSAGE);
							break;
						case 2:
							JOptionPane
									.showMessageDialog(
											frmMeetEat,
											"Es gab einen Fehler beim ändern vom Nicknamen.",
											"Accountdaten ändern",
											JOptionPane.PLAIN_MESSAGE);
							break;
						case 3:
							JOptionPane.showMessageDialog(frmMeetEat,
									"Der von Ihnen eingegebene Nickname ist zu lang. Maximal "
											+ laenge_nickname + "Zeichen",
									"Accountdaten ändern",
									JOptionPane.PLAIN_MESSAGE);
							break;
						}
					}

					if (!txtEMail.getText().isEmpty()) {
						r = c.aendernEmail(txtEMail.getText());
						switch (r) {
						case 0:
							updateMitgliedList();
							messageDialog[1] = "Email";
							// JOptionPane.showMessageDialog(frmMeetEat,
							// "Nickname erfolgreich geändert.", "ändern",
							// JOptionPane.PLAIN_MESSAGE);
							break;
						case 1:
							JOptionPane
									.showMessageDialog(
											frmMeetEat,
											"Es gab einen Fehler beim ändern der Email.",
											"Accountdaten ändern",
											JOptionPane.PLAIN_MESSAGE);
							break;
						case 2:
							JOptionPane.showMessageDialog(frmMeetEat,
									"Die von Ihnen eingegebene Email ist zu lang. Maximal "
											+ laenge_email + "Zeichen",
									"Accountdaten ändern",
									JOptionPane.PLAIN_MESSAGE);
							break;
						}
					}

					if (!txtVorname.getText().isEmpty()) {
						r = c.aendernVorname(txtVorname.getText());
						switch (r) {
						case 0:
							updateMitgliedList();
							messageDialog[1] = "Vorname";
							// JOptionPane.showMessageDialog(frmMeetEat,
							// "Nickname erfolgreich geändert.", "ändern",
							// JOptionPane.PLAIN_MESSAGE);
							break;
						case 1:
							JOptionPane
									.showMessageDialog(
											frmMeetEat,
											"Es gab einen Fehler beim ändern des Vornamen.",
											"Accountdaten ändern",
											JOptionPane.PLAIN_MESSAGE);
							break;
						case 2:
							JOptionPane.showMessageDialog(frmMeetEat,
									"Der von Ihnen eingegebene Vorname ist zu lang. Maximal "
											+ laenge_vname + "Zeichen",
									"Accountdaten ändern",
									JOptionPane.PLAIN_MESSAGE);
							break;
						}
					}

					if (!txtNachname.getText().isEmpty()) {
						r = c.aendernNachname(txtNachname.getText());
						switch (r) {
						case 0:
							updateMitgliedList();
							messageDialog[2] = "Nachname";
							// JOptionPane.showMessageDialog(frmMeetEat,
							// "Nickname erfolgreich geändert.", "ändern",
							// JOptionPane.PLAIN_MESSAGE);
							break;
						case 1:
							JOptionPane
									.showMessageDialog(
											frmMeetEat,
											"Es gab einen Fehler beim ändern des Nachnamen.",
											"Accountdaten ändern",
											JOptionPane.PLAIN_MESSAGE);
							break;
						case 2:
							JOptionPane.showMessageDialog(frmMeetEat,
									"Der von Ihnen eingegebene Nachname ist zu lang. Maximal "
											+ laenge_name + "Zeichen",
									"Accountdaten ändern",
									JOptionPane.PLAIN_MESSAGE);
							break;
						}
					}

					if (dateGeburtstag.getSelectedDate() != null) {
						Date datum = dateGeburtstag.getSelectedDate();
						String DATE_FORMAT = "yyyy-MM-dd";
						SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
						String sDate = sdf.format(datum);
						r = c.aendernGebDat(sDate);
						switch (r) {
						case 0:
							updateMitgliedList();
							messageDialog[3] = "Geburtstag";
							// JOptionPane.showMessageDialog(frmMeetEat,
							// "Nickname erfolgreich geändert.", "ändern",
							// JOptionPane.PLAIN_MESSAGE);
							break;
						case 1:
							JOptionPane
									.showMessageDialog(
											frmMeetEat,
											"Es gab einen Fehler beim ändern des Geburtsdatums.",
											"Accountdaten ändern",
											JOptionPane.PLAIN_MESSAGE);
							break;
						}
					}

					for (int i = 0; i < 5; i++) {
						if (messageDialog[i] == null) {
							messageDialog[i] = "";
						}
						if (messageDialog[i].equals("")) {
							output = output + messageDialog[i];
						} else {
							output = output + messageDialog[i] + ",";
						}
					}
					if (output.equals("")) {
						JOptionPane
								.showMessageDialog(
										frmMeetEat,
										"Bitte geben sie die zu ändernden Daten eingeben!",
										"Accountdaten ändern",
										JOptionPane.PLAIN_MESSAGE);
					} else
						JOptionPane.showMessageDialog(frmMeetEat, output
								+ " erfolgreich geändert.",
								"Accountdaten ändern",
								JOptionPane.PLAIN_MESSAGE);
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					JOptionPane.showMessageDialog(frmMeetEat,
							"Keine Serververbindung.", "Accountdaten ändern",
							JOptionPane.PLAIN_MESSAGE);
				}
			}
		});

		// Nutzer erstellen
		btnErstellen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (dateGeburtstag2.getSelectedDate() != null) {
					String datum = dateGeburtstag2.getSelectedDate().getYear()
							+ "-"
							+ dateGeburtstag2.getSelectedDate().getMonth()
							+ "-" + dateGeburtstag2.getSelectedDate().getDay();

					int r = -1;
					try {
						r = c.erstellenPerson(txtNickname2.getText(),
								new String(pwdPasswort2.getPassword()),
								new String(pwdPasswortWdh.getPassword()),
								txtVorname2.getText(), txtNachname2.getText(),
								datum, txtEMail2.getText());
					} catch (RemoteException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						JOptionPane.showMessageDialog(frmMeetEat,
								"Keine Serververbindung.", "Nutzer erstellen",
								JOptionPane.PLAIN_MESSAGE);
					}

					switch (r) {
					case 0:
						updateMitgliedList();
						JOptionPane.showMessageDialog(frmMeetEat,
								"Erfolgreich erstellt.", "Nutzer erstellen",
								JOptionPane.PLAIN_MESSAGE);
						break;
					case 1:
						JOptionPane.showMessageDialog(frmMeetEat,
								"Bitte Nutzername angeben.",
								"Nutzer erstellen", JOptionPane.PLAIN_MESSAGE);
						break;
					case 2:
						JOptionPane.showMessageDialog(frmMeetEat,
								"Bitte Passwort angeben.", "Nutzer erstellen",
								JOptionPane.PLAIN_MESSAGE);
						break;
					case 3:
						JOptionPane.showMessageDialog(frmMeetEat,
								"Bitte Passwort-Wiederholung angeben.",
								"Nutzer erstellen", JOptionPane.PLAIN_MESSAGE);
						break;
					case 4:
						JOptionPane.showMessageDialog(frmMeetEat,
								"Bitte Vorname angeben.", "Nutzer erstellen",
								JOptionPane.PLAIN_MESSAGE);
						break;
					case 5:
						JOptionPane.showMessageDialog(frmMeetEat,
								"Bitte Nachname angeben.", "Nutzer erstellen",
								JOptionPane.PLAIN_MESSAGE);
						break;
					case 6:
						JOptionPane.showMessageDialog(frmMeetEat,
								"Passwrter stimmen nicht berein.",
								"Nutzer erstellen", JOptionPane.PLAIN_MESSAGE);
						break;
					case 7:
						JOptionPane.showMessageDialog(frmMeetEat,
								"Nutzername vergeben.", "Nutzer erstellen",
								JOptionPane.PLAIN_MESSAGE);
						break;
					case 8:
						JOptionPane.showMessageDialog(frmMeetEat,
								"Fehler beim Erstellen des Nutzers.",
								"Nutzer erstellen", JOptionPane.PLAIN_MESSAGE);
						break;
					}
				} else {
					JOptionPane.showMessageDialog(frmMeetEat,
							"Geburtstag angeben.", "Nutzer erstellen",
							JOptionPane.PLAIN_MESSAGE);
				}

				/*
				 * txtNickname2.getText();
				 * 
				 * new String(pwdPasswort2.getPassword()); new
				 * String(pwdPasswortWdh.getPassword());
				 * 
				 * txtVorname2.getText(); txtNachname2.getText();
				 * dateGeburtstag2.getSelectedDate().getYear();
				 * dateGeburtstag2.getSelectedDate().getMonth();
				 * dateGeburtstag2.getSelectedDate().getDay();
				 */

			}
		});

		// Nutzer loeschen
		btnLoeschen3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});

		// Gruppe erstellen
		btnErstellen2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Person tmpPerson = (Person) comboLeiter.getSelectedItem();
				try {
					int r = c.erstellenGruppe(txtName3.getText(),
							String.valueOf(tmpPerson.id));
					switch (r) {

					case -1:
						JOptionPane.showMessageDialog(frmMeetEat,
								"Dieser Gruppenname existiert leider schon.",
								"Gruppe erstellen", JOptionPane.PLAIN_MESSAGE);
						break;
					case -2:
						JOptionPane.showMessageDialog(frmMeetEat,
								"Es ist ein Fehler aufgetreten.", "Gruppe erstellen",
								JOptionPane.PLAIN_MESSAGE);
						break;
					case -3:
						JOptionPane.showMessageDialog(frmMeetEat,
								"Der von ihn angegebene Gruppenname ist zu lang. Maximal "
										+ laenge_gruppenname + "Zeichen",
								"Gruppe erstellen", JOptionPane.PLAIN_MESSAGE);
						break;
					default:
						JOptionPane.showMessageDialog(frmMeetEat,
								"Gruppenerstellung war erfolgreich.", "Gruppe erstellen",
								JOptionPane.PLAIN_MESSAGE);
						break;
					}
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					JOptionPane.showMessageDialog(frmMeetEat,
							"Keine Serververbindung.",
							"Gruppe erstellen",
							JOptionPane.PLAIN_MESSAGE);
				}
			}
		});

		// Gruppe loeschen
		btnLoeschen2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Gruppe tmpGruppe = (Gruppe) listGruppen.getSelectedValue();
				try {
					int r = c.loeschenGruppe(tmpGruppe.id);
					switch (r) {
					case 0:
						JOptionPane.showMessageDialog(frmMeetEat,
								"Löschen der Gruppe erfolgreich.",
								"Gruppe löschen", JOptionPane.PLAIN_MESSAGE);
						break;
					case 1:
						JOptionPane
								.showMessageDialog(
										frmMeetEat,
										"Es ist ein Fehler beim löschen der Gruppe ein Fehler aufgetreten.",
										"Gruppe löschen",
										JOptionPane.PLAIN_MESSAGE);
						break;
					}
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					JOptionPane.showMessageDialog(frmMeetEat,
							"Keine Serververbindung.",
							"Gruppe löschen",
							JOptionPane.PLAIN_MESSAGE);
				}

			}
		});

		// Reihenfolge aendern
		btnAendern4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});

	}

	private void ladeTermine() {
		try {
			c.ladeTermine();
		} catch (RemoteException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
			JOptionPane.showMessageDialog(frmMeetEat,
					"Termine konnten nicht geladen werden.", "Anmelden",
					JOptionPane.PLAIN_MESSAGE);
		}
	}

	private void updateTerminList() {
		try {
			listTermine.setModel(new AbstractListModel() {
				Termin[] values = c.anzeigenTermine();

				public int getSize() {
					return values.length;
				}

				public Object getElementAt(int index) {
					return values[index];
				}
			});
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		listTermine.setCellRenderer(new TerminCellRenderer());
		if (listTermine.getModel().getSize() > 0)
			listTermine.setSelectedIndex(0);
	}

	private void updateTerminComboBox() {
		try {
			comboTermin.setModel(new DefaultComboBoxModel() {
				Termin tmpTermin = (Termin) listTermine.getSelectedValue();
				Termin[] values = c.anzeigenTauschtermine(tmpTermin.id);

				public int getSize() {
					return values.length;
				}

				public Object getElementAt(int index) {
					return values[index];
				}
			});
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		comboTermin.setRenderer(new TerminCellRenderer());
		if (comboTermin.getItemCount() > 0)
			comboTermin.setSelectedIndex(0);
	}

	private class TerminCellRenderer extends DefaultListCellRenderer {
		public Component getListCellRendererComponent(JList<?> list,
				Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			super.getListCellRendererComponent(list, value, index, isSelected,
					cellHasFocus);
			if (value instanceof Termin) {
				DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
				DateFormat tf = new SimpleDateFormat("HH:mm");
				setText(((Termin) value).gruppenname + " am "
						+ df.format(((Termin) value).datum) + " "
						+ tf.format(((Termin) value).zeit));
			}
			return this;
		}
	}

	private void updateMitgliedList() {
		try {
			listMitglieder.setModel(new AbstractListModel() {
				Gruppe tmpGruppe = (Gruppe) comboGruppe.getSelectedItem();
				Person[] values = c.anzeigenMitglied(tmpGruppe.id);

				public int getSize() {
					return values.length;
				}

				public Object getElementAt(int index) {
					return values[index];
				}
			});
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		listMitglieder.setCellRenderer(new PersonCellRenderer());
		if (listMitglieder.getModel().getSize() > 0)
			listMitglieder.setSelectedIndex(0);
	}

	private void updatePersonList() {
		try {
			listPersonen.setModel(new AbstractListModel() {
				Person[] values = c.anzeigenPersonen();

				public int getSize() {
					return values.length;
				}

				public Object getElementAt(int index) {
					return values[index];
				}
			});
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		listPersonen.setCellRenderer(new PersonCellRenderer());
		if (listPersonen.getModel().getSize() > 0)
			listPersonen.setSelectedIndex(0);
	}

	private void updatePersonComboBox(JComboBox box) {
		try {
			box.setModel(new DefaultComboBoxModel() {
				Person[] values = c.anzeigenNichtMitglied();

				public int getSize() {
					return values.length;
				}

				public Object getElementAt(int index) {
					return values[index];
				}
			});
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		box.setRenderer(new PersonCellRenderer());
		if (box.getItemCount() > 0)
			box.setSelectedIndex(0);
	}

	private class PersonCellRenderer extends DefaultListCellRenderer {
		public Component getListCellRendererComponent(JList<?> list,
				Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			super.getListCellRendererComponent(list, value, index, isSelected,
					cellHasFocus);
			if (value instanceof Person) {
				setText(((Person) value).vorname + " "
						+ ((Person) value).nachname + " ("
						+ ((Person) value).nickname + ")");
			}
			return this;
		}
	}

	private void updateGruppeList() {
		try {
			listGruppen.setModel(new AbstractListModel() {
				Gruppe[] values = c.anzeigenGruppen();

				public int getSize() {
					return values.length;
				}

				public Object getElementAt(int index) {
					return values[index];
				}
			});
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		listGruppen.setCellRenderer(new GruppeCellRenderer1());
		if (listGruppen.getModel().getSize() > 0)
			listGruppen.setSelectedIndex(0);
	}

	private void updateGruppeComboBox() {
		try {
			comboGruppe.setModel(new DefaultComboBoxModel() {
				Gruppe[] values = c.anzeigenGruppen();

				public int getSize() {
					return values.length;
				}

				public Object getElementAt(int index) {
					return values[index];
				}
			});
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		comboGruppe.setRenderer(new GruppeCellRenderer2());
		if (comboGruppe.getItemCount() > 0)
			comboGruppe.setSelectedIndex(0);
	}

	private class GruppeCellRenderer1 extends DefaultListCellRenderer {
		public Component getListCellRendererComponent(JList<?> list,
				Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			super.getListCellRendererComponent(list, value, index, isSelected,
					cellHasFocus);
			if (value instanceof Gruppe) {
				setText(((Gruppe) value).name + " (" + ((Gruppe) value).id
						+ ")");
			}
			return this;
		}
	}
	
	private class GruppeCellRenderer2 extends DefaultListCellRenderer {
		public Component getListCellRendererComponent(JList<?> list,
				Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			super.getListCellRendererComponent(list, value, index, isSelected,
					cellHasFocus);
			if (value instanceof Gruppe) {
				setText(((Gruppe) value).name);
			}
			return this;
		}
	}

	private void changeTheAppearanceOfTheCells(JSCCalendar calendar) {
		calendar.setCalendarCellRenderer(new CustomCellRenderer());
	}

	private class CustomCellRenderer extends JLabel implements
			CalendarCellRenderer {

		// Icon xmasIcon;

		public CustomCellRenderer() {
			/*
			 * Image image; try { image =
			 * GraphicsUtilities.loadCompatibleImage(Thread
			 * .currentThread().getContextClassLoader
			 * ().getResourceAsStream("demo.png")); xmasIcon = new
			 * ImageIcon(image); } catch (IOException e) { e.printStackTrace();
			 * }
			 */

		}

		@Override
		public JComponent getHeadingCellRendererComponent(JSCCalendar calendar,
				String text) {
			// configure your customCellRenderer based on the supplied
			// information, ie String text
			setHorizontalAlignment(JLabel.LEFT);
			setVerticalAlignment(JLabel.CENTER);
			setFont(getFont().deriveFont(getFont().getStyle() | Font.BOLD));
			setText(text);
			setOpaque(false);
			setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
			setForeground(Color.BLACK);
			// setIcon(null);
			// our headings will have a transparent background and black
			// foreground
			return this;
		}

		@Override
		public JComponent getCellRendererComponent(
				CellRendererComponentParameter parameterObject) {
			// configure your customerCellRenderer based on the information
			// encapsulated in the parameterObject.
			// you must choose how to render your component based on your
			// business rules and the following parameters.

			// is your current cell that you are rendering a holiday
			boolean isHoliday = parameterObject.isHoliday;
			// is your current cell that you are rendering a weekend
			boolean isWeekend = parameterObject.isWeekend;
			// is the mouse over tthe current cell you are rendering
			boolean isMouseOver = parameterObject.isMouseOver;
			// is your current cell that you are rendering today
			boolean isToday = parameterObject.isToday;
			// is your current cell that you are rendering already selected
			boolean isSelected = parameterObject.isSelected;
			// is your current cell that you are rendering able to be selected
			boolean isSelectable = parameterObject.isAllowSelection();
			// is your current cell that you are rendering currently the
			// keyboard focus cell
			boolean hasKeyboardFocus = parameterObject.isHasFocus();
			// is your current cell that you are rendering in this current month
			boolean isCurrentMonth = parameterObject.isCurrentMonth;
			// the text of the cell
			String text = parameterObject.getText();
			// the date of the cell
			Date date = parameterObject.getDate();
			// the calendar
			JSCCalendar calendar = parameterObject.getCalendar();

			// for this example all dates will render the same except for the
			// unselectable dates.
			// we defined the unselectable dates in the calendarModel as
			// weekends and holidays.

			setHorizontalAlignment(JLabel.LEFT);
			setVerticalAlignment(JLabel.TOP);
			// setIcon(null);
			setFont(getFont().deriveFont(getFont().getStyle() & ~Font.BOLD));
			setText(text);
			setOpaque(false);
			setForeground(Color.BLACK);

			// if we are rendering the month of December, some of the cell in
			// the calendar
			// can relate to days in January and December. in this example we
			// will not draw them
			if (!isCurrentMonth) {
				// removing the text will make them appear empty
				setText("");
			}

			// selected dates will receive a black border
			if (isSelected) {
				setBorder(BorderFactory.createCompoundBorder(new LineBorder(
						Color.BLACK), new EmptyBorder(3, 4, 0, 0)));
			} else {
				if (isToday) {
					setBorder(BorderFactory.createCompoundBorder(
							new LineBorder(new Color(159, 216, 244)),
							new EmptyBorder(3, 4, 0, 0)));
				} else {
					setBorder(BorderFactory.createEmptyBorder(4, 5, 0, 0));
				}
			}

			return this;
		}
	};

	private void addBusinessRules(JSCCalendar calendar) {
		// in this example we will create a model that applies
		// the following rules:
		// 1. No weekends may be selected.
		// 2. No holidays may be selected.

		// As a learning exercise we will create an entire CalendarModel from
		// scratch.
		// However, please note that it would be easier to simply subclass the
		// DefaultCalendarModel and just override the isDateSelectable method.
		// Please read the AbstractCalendarModel javadoc for information on
		// available utility methods.
		AbstractCalendarModel newRules = new AbstractCalendarModel() {

			@Override
			public boolean isDateSelectable(Date date) {
				// this is the method that determines if a day may be selected.
				// Your business logic should live here.

				// so we return true if the date is not a holiday and is not a
				// weekend.
				// return !isDateHoliday(date) && !isDateWeekend(date);

				Termin tmpTermin = null;
				try {
					tmpTermin = c.anzeigenTermin(new java.sql.Date(date
							.getTime()));
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (tmpTermin != null) {
					return true;
				} else {
					return false;
				}
			}

			@Override
			public String getTextForHeading(DayOfWeek dayOfWeek) {
				// this will return the first letter of the day of the week for
				// the heading
				String firstLetter = dayOfWeek
						.getDisplayString(getCalendar().getLocale())
						.substring(0, 1).toUpperCase();
				String secondLetter = dayOfWeek.getDisplayString(
						getCalendar().getLocale()).substring(1, 2);
				return firstLetter + secondLetter;
			}

			@Override
			public String getTextForCell(Date date) {
				// this will return the day of the month for each cell
				Calendar calendar = createCalendar(date);

				Termin tmpTermin = null;
				try {
					tmpTermin = c.anzeigenTermin(new java.sql.Date(date
							.getTime()));
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (tmpTermin != null) {
					DateFormat tf = new SimpleDateFormat("HH:mm");
					return "<html>"
							+ Integer.toString(calendar
									.get(Calendar.DAY_OF_MONTH)) + "<br><b>"
							+ tf.format(tmpTermin.zeit) + " Uhr<br>"
							+ tmpTermin.gruppenname + "</b></html>";
				} else {
					return "<html>"
							+ Integer.toString(calendar
									.get(Calendar.DAY_OF_MONTH)) + "</html>";
				}
			}

			@Override
			public boolean isDateSelected(Date date) {
				// please note the utility method areDatesEqual,
				// it compares two dates to see if they occur on the
				// same day.
				for (Date selectedDate : getSelectedDates()) {
					if (areDatesEqual(selectedDate, date)) {
						return true;
					}
				}
				return false;
			}

			@Override
			public boolean isDateHoliday(Date date) {
				// please note the utility method areDatesEqual,
				// it compares two dates to see if they occur on the
				// same day.
				for (Holiday holiday : getHolidays()) {
					if (areDatesEqual(holiday.getDate(), date)) {
						return true;
					}
				}
				return false;
			}

			@Override
			public Holiday getHolidayForDate(Date date) {
				// get the holiday object for the supplied date.
				for (Holiday holiday : getHolidays()) {
					if (areDatesEqual(holiday.getDate(), date)) {
						return holiday;
					}
				}
				return null;
			}

			@Override
			public boolean isDateWeekend(Date date) {
				Calendar calendar = createCalendar(date);
				DayOfWeek dayOfWeek = DayOfWeek.getDayOfWeek(calendar
						.get(Calendar.DAY_OF_WEEK));
				return getWeekendDays().contains(dayOfWeek);
			}
		};

		// apply the calendarModel with the new rules to the calendar.
		calendar.setCalendarModel(newRules);
	}

	private void listenToChangesOnTheCalendar(JSCCalendar calendar) {
		calendar.addCalendarSelectionListener(new CalendarSelectionListener() {
			@Override
			public void selectedDatesChanged(
					CalendarSelectionEvent calendarSelectionEvent) {

				JSCCalendar calendar = calendarSelectionEvent.getCalendar();
				List<Date> selectedDates = calendarSelectionEvent
						.getSelectedDates();
				CalendarSelectionEventType selectionEventType = calendarSelectionEvent
						.getCalendarSelectionEventType();

				switch (selectionEventType) {
				case DATE_SELECTED: {
					Termin tmpTermin = null;
					try {
						tmpTermin = c.anzeigenTermin(new java.sql.Date(
								selectedDates.get(0).getTime()));
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					if (tmpTermin != null) {
						DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
						DateFormat tf = new SimpleDateFormat("HH:mm");
						txtDatumZeit.setText(df.format(tmpTermin.datum) + " "
								+ tf.format(tmpTermin.zeit));
						txtOrt3.setText(tmpTermin.ort);
						txtGruppe.setText(tmpTermin.gruppenname);
						txtEssen.setText(tmpTermin.essen);
					}
				}
				}
			}
		});
	}
}
