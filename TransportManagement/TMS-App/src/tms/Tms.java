package tms;

import java.awt.EventQueue;
import java.sql.*;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.BoxLayout;

import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Font;

public class Tms {

	private JFrame frmTransportManagementSystem;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField locationfield;
	private JTextField textField_5;
	private JTextField textField_6;
	private JTextField textField_7;
	private JTextField textField_8;
	private DbManager dbmanager;
	private JTextField textField_4;
	private JTextField textField_9;
	private JTextField textField_10;
	private JTextField textField_11;
	private JTextField textField_12;
	private JTextField textField_13;
	private JTextField textField_14;
	private JTextField textField_15;
	private JTextField addVehicleName;
	private JTextField addRegNo;
	private JTextField addCapacity;
	private JTextField addVtype;
	private JTextField editVehicleName;
	private JTextField editRegNo;
	private JTextField editCapacity;
	private JTextField editVtype;
	private JTextField addDriverName;
	private JTextField addLicense;
	private JTextField addContact;
	private JTextField editDriverName;
	private JTextField editLicenseNo;
	private JTextField editContact;
	private JComboBox editDriverNames, deleteDriverNames, addAssignDriverNames, editAssignDriverNames, editGender,
	selectVehicleNames, deleteVehicleNames;
	private JButton btnSaveDriver;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Tms window = new Tms();
					window.frmTransportManagementSystem.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Tms() {
		dbmanager = new DbManager();
		initialize();
		updateDriversList();
		disableDriverEdit();
		updateVehiclesList();
		disableVehicleEdit();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmTransportManagementSystem = new JFrame();
		frmTransportManagementSystem.setTitle("Transport Management System");
		frmTransportManagementSystem.setBounds(100, 100, 721, 498);
		frmTransportManagementSystem.setSize(1280, 720);
		frmTransportManagementSystem.setResizable(false);
		frmTransportManagementSystem.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTransportManagementSystem.getContentPane().setLayout(null);

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		JTabbedPane mainTabbedPane = new JTabbedPane(JTabbedPane.LEFT);
		mainTabbedPane.setBounds(58, 133, 1103, 558);
		frmTransportManagementSystem.getContentPane().add(mainTabbedPane);

		JPanel homePanel = new JPanel();
		homePanel.setBackground(Color.WHITE);
		mainTabbedPane.addTab("\r\nHome", null, homePanel, null);
		homePanel.setLayout(null);

		JButton btnViewDailySchedule = new JButton("View Daily Schedule");
		btnViewDailySchedule.setBounds(30, 39, 159, 40);
		homePanel.add(btnViewDailySchedule);

		JButton btnViewWeeklySchedule = new JButton("View Weekly Schedule");
		btnViewWeeklySchedule.setBounds(30, 130, 159, 40);
		homePanel.add(btnViewWeeklySchedule);

		JButton btnViewSpecialSchedule = new JButton("View Special Schedule");
		btnViewSpecialSchedule.setBounds(30, 219, 159, 40);
		homePanel.add(btnViewSpecialSchedule);

		JButton btnShowAllLocations = new JButton("Show All Locations");
		btnShowAllLocations.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnShowAllLocations.setBounds(236, 39, 159, 40);
		homePanel.add(btnShowAllLocations);

		JButton btnShowScheduleBy = new JButton("Schedule by Locations");
		btnShowScheduleBy.setBounds(236, 130, 159, 40);
		homePanel.add(btnShowScheduleBy);

		JButton btnScheduleByBus = new JButton("Schedule By Bus");
		btnScheduleByBus.setBounds(236, 219, 159, 40);
		homePanel.add(btnScheduleByBus);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(30, 310, 796, 211);
		homePanel.add(scrollPane);

		JTextArea outputConsole = new JTextArea();
		outputConsole.setText("Output Console");
		scrollPane.setViewportView(outputConsole);

		JButton btnShowAllDrivers = new JButton("Show All Drivers");
		btnShowAllDrivers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				String output = dbmanager.getDriversInfo();
				outputConsole.setText(output);
			}
		});
		btnShowAllDrivers.setBounds(451, 39, 159, 40);
		homePanel.add(btnShowAllDrivers);

		JPanel manageSchedulePanel = new JPanel();
		manageSchedulePanel.setBackground(Color.WHITE);
		mainTabbedPane.addTab("Manage Schedules", null, manageSchedulePanel, null);
		manageSchedulePanel.setLayout(null);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(42, 58, 921, 452);
		manageSchedulePanel.add(tabbedPane);

		JPanel dailyPanel = new JPanel();
		tabbedPane.addTab("Daily Schedule", null, dailyPanel, null);
		dailyPanel.setLayout(null);

		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(284, 11, 8, 402);
		dailyPanel.add(separator);

		JLabel lblCreateDailySchedule = new JLabel("Create a Daily Schedule");
		lblCreateDailySchedule.setBounds(62, 35, 130, 31);
		dailyPanel.add(lblCreateDailySchedule);

		JLabel lblStartLocation = new JLabel("Start Location");
		lblStartLocation.setBounds(21, 98, 83, 14);
		dailyPanel.add(lblStartLocation);

		JLabel lblEndLocation = new JLabel("End Location");
		lblEndLocation.setBounds(21, 157, 83, 14);
		dailyPanel.add(lblEndLocation);

		JLabel lblStartTime = new JLabel("Start Time");
		lblStartTime.setBounds(21, 221, 83, 14);
		dailyPanel.add(lblStartTime);

		JLabel lblEndTime = new JLabel("End Time");
		lblEndTime.setBounds(21, 273, 83, 14);
		dailyPanel.add(lblEndTime);

		JLabel lblVehicle = new JLabel("Vehicle\r\n");
		lblVehicle.setBounds(21, 319, 83, 14);
		dailyPanel.add(lblVehicle);

		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] { "", "Bus 1", "Bus 2", "Bus 3" }));
		comboBox.setBounds(151, 316, 107, 20);
		dailyPanel.add(comboBox);

		JComboBox comboBox_1 = new JComboBox();
		comboBox_1.setModel(new DefaultComboBoxModel(dbmanager.getLocations()));
		comboBox_1.setBounds(152, 95, 106, 20);
		dailyPanel.add(comboBox_1);

		JComboBox comboBox_2 = new JComboBox();
		comboBox_2.setBounds(152, 154, 106, 20);
		dailyPanel.add(comboBox_2);

		textField = new JTextField();
		textField.setBounds(151, 218, 86, 20);
		dailyPanel.add(textField);
		textField.setColumns(10);

		textField_1 = new JTextField();
		textField_1.setBounds(151, 270, 86, 20);
		dailyPanel.add(textField_1);
		textField_1.setColumns(10);

		JLabel lblEditADaily = new JLabel("Edit a Daily Schedule");
		lblEditADaily.setBounds(418, 11, 130, 31);
		dailyPanel.add(lblEditADaily);

		JLabel label = new JLabel("Start Location");
		label.setBounds(306, 52, 83, 14);
		dailyPanel.add(label);

		JComboBox comboBox_3 = new JComboBox();
		comboBox_3.setBounds(295, 73, 106, 20);
		dailyPanel.add(comboBox_3);

		JLabel label_1 = new JLabel("End Location");
		label_1.setBounds(445, 52, 83, 14);
		dailyPanel.add(label_1);

		JComboBox comboBox_4 = new JComboBox();
		comboBox_4.setBounds(435, 73, 106, 20);
		dailyPanel.add(comboBox_4);

		JLabel label_2 = new JLabel("Vehicle\r\n");
		label_2.setBounds(567, 52, 83, 14);
		dailyPanel.add(label_2);

		JComboBox comboBox_5 = new JComboBox();
		comboBox_5.setBounds(567, 70, 83, 20);
		dailyPanel.add(comboBox_5);

		JLabel label_3 = new JLabel("Start Location");
		label_3.setBounds(302, 158, 83, 14);
		dailyPanel.add(label_3);

		JComboBox comboBox_6 = new JComboBox();
		comboBox_6.setBounds(433, 155, 106, 20);
		dailyPanel.add(comboBox_6);

		JLabel label_4 = new JLabel("End Location");
		label_4.setBounds(302, 217, 83, 14);
		dailyPanel.add(label_4);

		JComboBox comboBox_7 = new JComboBox();
		comboBox_7.setBounds(433, 214, 106, 20);
		dailyPanel.add(comboBox_7);

		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(432, 278, 86, 20);
		dailyPanel.add(textField_2);

		JLabel label_5 = new JLabel("Start Time");
		label_5.setBounds(302, 281, 83, 14);
		dailyPanel.add(label_5);

		JLabel label_6 = new JLabel("End Time");
		label_6.setBounds(302, 333, 83, 14);
		dailyPanel.add(label_6);

		textField_3 = new JTextField();
		textField_3.setColumns(10);
		textField_3.setBounds(432, 330, 86, 20);
		dailyPanel.add(textField_3);

		JComboBox comboBox_8 = new JComboBox();
		comboBox_8.setBounds(432, 376, 107, 20);
		dailyPanel.add(comboBox_8);

		JLabel label_7 = new JLabel("Vehicle\r\n");
		label_7.setBounds(302, 379, 83, 14);
		dailyPanel.add(label_7);

		JSeparator separator_1 = new JSeparator();
		separator_1.setOrientation(SwingConstants.VERTICAL);
		separator_1.setBounds(687, 11, 8, 402);
		dailyPanel.add(separator_1);

		JButton btnSave = new JButton("Save");
		btnSave.setBounds(574, 375, 89, 23);
		dailyPanel.add(btnSave);

		JButton btnCreate = new JButton("Create");
		btnCreate.setBounds(103, 375, 89, 23);
		dailyPanel.add(btnCreate);

		JButton btnSelect = new JButton("Select");
		btnSelect.setBounds(445, 104, 89, 23);
		dailyPanel.add(btnSelect);

		JLabel lblDeleteADaily = new JLabel("Delete a Daily Schedule");
		lblDeleteADaily.setBounds(749, 11, 130, 31);
		dailyPanel.add(lblDeleteADaily);

		JComboBox comboBox_9 = new JComboBox();
		comboBox_9.setBounds(800, 73, 106, 20);
		dailyPanel.add(comboBox_9);

		JLabel label_8 = new JLabel("Start Location");
		label_8.setBounds(701, 73, 83, 14);
		dailyPanel.add(label_8);

		JLabel label_9 = new JLabel("End Location");
		label_9.setBounds(701, 125, 83, 14);
		dailyPanel.add(label_9);

		JComboBox comboBox_10 = new JComboBox();
		comboBox_10.setBounds(800, 125, 106, 20);
		dailyPanel.add(comboBox_10);

		JButton btnDelete = new JButton("Delete");
		btnDelete.setBounds(759, 240, 89, 23);
		dailyPanel.add(btnDelete);

		JLabel label_10 = new JLabel("Vehicle\r\n");
		label_10.setBounds(701, 176, 83, 14);
		dailyPanel.add(label_10);

		JComboBox comboBox_11 = new JComboBox();
		comboBox_11.setBounds(800, 173, 83, 20);
		dailyPanel.add(comboBox_11);

		JLabel lblHhmmAmpm = new JLabel("HH:MM AM/PM");
		lblHhmmAmpm.setBounds(151, 240, 86, 14);
		dailyPanel.add(lblHhmmAmpm);

		JLabel label_11 = new JLabel("HH:MM AM/PM");
		label_11.setBounds(151, 291, 86, 14);
		dailyPanel.add(label_11);

		JLabel label_12 = new JLabel("HH:MM AM/PM");
		label_12.setBounds(432, 305, 86, 14);
		dailyPanel.add(label_12);

		JLabel label_13 = new JLabel("HH:MM AM/PM");
		label_13.setBounds(432, 351, 86, 14);
		dailyPanel.add(label_13);

		JPanel weeklyPanel = new JPanel();
		tabbedPane.addTab("Weekly Schedule", null, weeklyPanel, null);
		weeklyPanel.setLayout(null);

		JLabel lblCreateAWeekly = new JLabel("Create a Weekly Schedule");
		lblCreateAWeekly.setBounds(62, 35, 130, 31);
		weeklyPanel.add(lblCreateAWeekly);

		JLabel label_15 = new JLabel("Start Location");
		label_15.setBounds(21, 98, 83, 14);
		weeklyPanel.add(label_15);

		JComboBox comboBox_12 = new JComboBox();
		comboBox_12.setBounds(152, 95, 106, 20);
		weeklyPanel.add(comboBox_12);

		JComboBox comboBox_13 = new JComboBox();
		comboBox_13.setBounds(152, 154, 106, 20);
		weeklyPanel.add(comboBox_13);

		JLabel label_16 = new JLabel("End Location");
		label_16.setBounds(21, 157, 83, 14);
		weeklyPanel.add(label_16);

		JLabel label_17 = new JLabel("Start Time");
		label_17.setBounds(21, 221, 83, 14);
		weeklyPanel.add(label_17);

		textField_5 = new JTextField();
		textField_5.setColumns(10);
		textField_5.setBounds(151, 218, 86, 20);
		weeklyPanel.add(textField_5);

		JLabel label_18 = new JLabel("HH:MM AM/PM");
		label_18.setBounds(151, 240, 86, 14);
		weeklyPanel.add(label_18);

		textField_6 = new JTextField();
		textField_6.setColumns(10);
		textField_6.setBounds(151, 270, 86, 20);
		weeklyPanel.add(textField_6);

		JLabel label_19 = new JLabel("HH:MM AM/PM");
		label_19.setBounds(151, 291, 86, 14);
		weeklyPanel.add(label_19);

		JLabel label_20 = new JLabel("End Time");
		label_20.setBounds(21, 273, 83, 14);
		weeklyPanel.add(label_20);

		JLabel label_21 = new JLabel("Vehicle\r\n");
		label_21.setBounds(21, 319, 83, 14);
		weeklyPanel.add(label_21);

		JComboBox comboBox_14 = new JComboBox();
		comboBox_14.setBounds(151, 316, 107, 20);
		weeklyPanel.add(comboBox_14);

		JButton button = new JButton("Create");
		button.setBounds(103, 375, 89, 23);
		weeklyPanel.add(button);

		JSeparator separator_2 = new JSeparator();
		separator_2.setOrientation(SwingConstants.VERTICAL);
		separator_2.setBounds(284, 11, 8, 402);
		weeklyPanel.add(separator_2);

		JLabel lblEditAWeekly = new JLabel("Edit a Weekly Schedule");
		lblEditAWeekly.setBounds(427, 11, 130, 31);
		weeklyPanel.add(lblEditAWeekly);

		JLabel label_23 = new JLabel("Start Location");
		label_23.setBounds(315, 52, 83, 14);
		weeklyPanel.add(label_23);

		JLabel label_24 = new JLabel("End Location");
		label_24.setBounds(454, 52, 83, 14);
		weeklyPanel.add(label_24);

		JComboBox comboBox_15 = new JComboBox();
		comboBox_15.setBounds(444, 73, 106, 20);
		weeklyPanel.add(comboBox_15);

		JComboBox comboBox_16 = new JComboBox();
		comboBox_16.setBounds(583, 73, 83, 20);
		weeklyPanel.add(comboBox_16);

		JComboBox comboBox_17 = new JComboBox();
		comboBox_17.setBounds(304, 73, 106, 20);
		weeklyPanel.add(comboBox_17);

		JButton button_1 = new JButton("Select");
		button_1.setBounds(498, 104, 89, 23);
		weeklyPanel.add(button_1);

		JLabel label_25 = new JLabel("Start Location");
		label_25.setBounds(302, 157, 83, 14);
		weeklyPanel.add(label_25);

		JComboBox comboBox_18 = new JComboBox();
		comboBox_18.setBounds(444, 154, 106, 20);
		weeklyPanel.add(comboBox_18);

		JComboBox comboBox_19 = new JComboBox();
		comboBox_19.setBounds(444, 193, 106, 20);
		weeklyPanel.add(comboBox_19);

		JLabel label_26 = new JLabel("End Location");
		label_26.setBounds(302, 193, 83, 14);
		weeklyPanel.add(label_26);

		JLabel label_27 = new JLabel("Start Time");
		label_27.setBounds(302, 276, 83, 14);
		weeklyPanel.add(label_27);

		textField_7 = new JTextField();
		textField_7.setColumns(10);
		textField_7.setBounds(441, 278, 86, 20);
		weeklyPanel.add(textField_7);

		JLabel label_28 = new JLabel("End Time");
		label_28.setBounds(311, 333, 83, 14);
		weeklyPanel.add(label_28);

		JLabel label_29 = new JLabel("HH:MM AM/PM");
		label_29.setBounds(441, 305, 86, 14);
		weeklyPanel.add(label_29);

		JLabel label_30 = new JLabel("HH:MM AM/PM");
		label_30.setBounds(441, 351, 86, 14);
		weeklyPanel.add(label_30);

		JLabel label_31 = new JLabel("Vehicle\r\n");
		label_31.setBounds(311, 379, 83, 14);
		weeklyPanel.add(label_31);

		JComboBox comboBox_20 = new JComboBox();
		comboBox_20.setBounds(441, 376, 107, 20);
		weeklyPanel.add(comboBox_20);

		JButton button_2 = new JButton("Save");
		button_2.setBounds(583, 375, 89, 23);
		weeklyPanel.add(button_2);

		JSeparator separator_3 = new JSeparator();
		separator_3.setOrientation(SwingConstants.VERTICAL);
		separator_3.setBounds(696, 11, 8, 402);
		weeklyPanel.add(separator_3);

		JLabel lblDayOfWeek = new JLabel("Day of Week");
		lblDayOfWeek.setBounds(21, 193, 83, 14);
		weeklyPanel.add(lblDayOfWeek);

		JComboBox comboBox_21 = new JComboBox();
		comboBox_21.setBounds(152, 187, 106, 20);
		weeklyPanel.add(comboBox_21);

		JLabel lblVehicle_1 = new JLabel("Vehicle");
		lblVehicle_1.setBounds(583, 52, 83, 14);
		weeklyPanel.add(lblVehicle_1);

		JLabel lblWeekday = new JLabel("Weekday");
		lblWeekday.setBounds(315, 108, 83, 14);
		weeklyPanel.add(lblWeekday);

		JComboBox comboBox_23 = new JComboBox();
		comboBox_23.setBounds(377, 105, 106, 20);
		weeklyPanel.add(comboBox_23);

		JSeparator separator_4 = new JSeparator();
		separator_4.setBounds(302, 138, 371, 8);
		weeklyPanel.add(separator_4);

		JLabel label_33 = new JLabel("Day of Week");
		label_33.setBounds(302, 240, 83, 14);
		weeklyPanel.add(label_33);

		JComboBox comboBox_24 = new JComboBox();
		comboBox_24.setBounds(444, 237, 106, 20);
		weeklyPanel.add(comboBox_24);

		textField_8 = new JTextField();
		textField_8.setColumns(10);
		textField_8.setBounds(441, 330, 86, 20);
		weeklyPanel.add(textField_8);

		JLabel lblDeleteAWeekly = new JLabel("Delete a Weekly Schedule");
		lblDeleteAWeekly.setBounds(742, 11, 130, 31);
		weeklyPanel.add(lblDeleteAWeekly);

		JLabel label_14 = new JLabel("Start Location");
		label_14.setBounds(714, 76, 83, 14);
		weeklyPanel.add(label_14);

		JLabel label_22 = new JLabel("End Location");
		label_22.setBounds(714, 157, 83, 14);
		weeklyPanel.add(label_22);

		JLabel label_32 = new JLabel("Vehicle");
		label_32.setBounds(714, 305, 83, 14);
		weeklyPanel.add(label_32);

		JComboBox comboBox_22 = new JComboBox();
		comboBox_22.setBounds(714, 107, 106, 20);
		weeklyPanel.add(comboBox_22);

		JComboBox comboBox_25 = new JComboBox();
		comboBox_25.setBounds(714, 193, 106, 20);
		weeklyPanel.add(comboBox_25);

		JComboBox comboBox_26 = new JComboBox();
		comboBox_26.setBounds(714, 330, 83, 20);
		weeklyPanel.add(comboBox_26);

		JLabel label_34 = new JLabel("Day of Week");
		label_34.setBounds(714, 240, 83, 14);
		weeklyPanel.add(label_34);

		JComboBox comboBox_27 = new JComboBox();
		comboBox_27.setBounds(714, 270, 106, 20);
		weeklyPanel.add(comboBox_27);

		JButton btnDelete_1 = new JButton("Delete");
		btnDelete_1.setBounds(766, 375, 89, 23);
		weeklyPanel.add(btnDelete_1);

		JPanel specialPanel = new JPanel();
		tabbedPane.addTab("Special Schedule", null, specialPanel, null);
		specialPanel.setLayout(null);

		JLabel lblCreateASpecial = new JLabel("Create a Special Schedule");
		lblCreateASpecial.setBounds(79, 28, 130, 31);
		specialPanel.add(lblCreateASpecial);

		JLabel label_36 = new JLabel("Start Location");
		label_36.setBounds(38, 91, 83, 14);
		specialPanel.add(label_36);

		JComboBox comboBox_28 = new JComboBox();
		comboBox_28.setBounds(169, 88, 106, 20);
		specialPanel.add(comboBox_28);

		JLabel label_37 = new JLabel("End Location");
		label_37.setBounds(38, 136, 83, 14);
		specialPanel.add(label_37);

		JComboBox comboBox_29 = new JComboBox();
		comboBox_29.setBounds(169, 133, 106, 20);
		specialPanel.add(comboBox_29);

		JLabel label_38 = new JLabel("Start Time");
		label_38.setBounds(38, 212, 83, 14);
		specialPanel.add(label_38);

		textField_4 = new JTextField();
		textField_4.setColumns(10);
		textField_4.setBounds(168, 209, 86, 20);
		specialPanel.add(textField_4);

		JLabel label_39 = new JLabel("HH:MM AM/PM");
		label_39.setBounds(168, 231, 86, 14);
		specialPanel.add(label_39);

		textField_9 = new JTextField();
		textField_9.setColumns(10);
		textField_9.setBounds(168, 263, 86, 20);
		specialPanel.add(textField_9);

		JLabel label_40 = new JLabel("End Time");
		label_40.setBounds(38, 266, 83, 14);
		specialPanel.add(label_40);

		JLabel label_41 = new JLabel("HH:MM AM/PM");
		label_41.setBounds(168, 284, 86, 14);
		specialPanel.add(label_41);

		JComboBox comboBox_30 = new JComboBox();
		comboBox_30.setBounds(168, 309, 107, 20);
		specialPanel.add(comboBox_30);

		JLabel label_42 = new JLabel("Vehicle\r\n");
		label_42.setBounds(38, 312, 83, 14);
		specialPanel.add(label_42);

		JButton button_3 = new JButton("Create");
		button_3.setBounds(120, 368, 89, 23);
		specialPanel.add(button_3);

		JSeparator separator_5 = new JSeparator();
		separator_5.setOrientation(SwingConstants.VERTICAL);
		separator_5.setBounds(300, 11, 8, 402);
		specialPanel.add(separator_5);

		JLabel lblEditASpecial = new JLabel("Edit a Special Schedule");
		lblEditASpecial.setBounds(400, 0, 130, 31);
		specialPanel.add(lblEditASpecial);

		JLabel lblDate = new JLabel("Date");
		lblDate.setBounds(38, 167, 83, 14);
		specialPanel.add(lblDate);

		textField_10 = new JTextField();
		textField_10.setColumns(10);
		textField_10.setBounds(168, 164, 86, 20);
		specialPanel.add(textField_10);

		JLabel lblMmddyyyy = new JLabel("MM/DD/YYYY");
		lblMmddyyyy.setBounds(168, 184, 86, 14);
		specialPanel.add(lblMmddyyyy);

		JLabel label_43 = new JLabel("Start Location");
		label_43.setBounds(342, 36, 83, 14);
		specialPanel.add(label_43);

		JComboBox comboBox_31 = new JComboBox();
		comboBox_31.setBounds(333, 61, 106, 20);
		specialPanel.add(comboBox_31);

		JComboBox comboBox_32 = new JComboBox();
		comboBox_32.setBounds(514, 61, 106, 20);
		specialPanel.add(comboBox_32);

		textField_11 = new JTextField();
		textField_11.setColumns(10);
		textField_11.setBounds(353, 88, 86, 20);
		specialPanel.add(textField_11);

		JLabel label_44 = new JLabel("End Location");
		label_44.setBounds(525, 36, 83, 14);
		specialPanel.add(label_44);

		JLabel label_45 = new JLabel("MM/DD/YYYY");
		label_45.setBounds(371, 111, 86, 14);
		specialPanel.add(label_45);

		JLabel label_46 = new JLabel("Date");
		label_46.setBounds(320, 94, 83, 14);
		specialPanel.add(label_46);

		JLabel label_47 = new JLabel("Vehicle\r\n");
		label_47.setBounds(467, 91, 83, 14);
		specialPanel.add(label_47);

		JComboBox comboBox_33 = new JComboBox();
		comboBox_33.setBounds(513, 88, 107, 20);
		specialPanel.add(comboBox_33);

		JButton btnSelect_1 = new JButton("Select");
		btnSelect_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnSelect_1.setBounds(443, 132, 89, 23);
		specialPanel.add(btnSelect_1);

		JSeparator separator_6 = new JSeparator();
		separator_6.setBounds(318, 167, 329, 6);
		specialPanel.add(separator_6);

		JLabel label_48 = new JLabel("Start Location");
		label_48.setBounds(320, 184, 83, 14);
		specialPanel.add(label_48);

		JComboBox comboBox_34 = new JComboBox();
		comboBox_34.setBounds(398, 184, 106, 20);
		specialPanel.add(comboBox_34);

		JLabel label_49 = new JLabel("End Location");
		label_49.setBounds(318, 215, 83, 14);
		specialPanel.add(label_49);

		JComboBox comboBox_35 = new JComboBox();
		comboBox_35.setBounds(396, 209, 106, 20);
		specialPanel.add(comboBox_35);

		JLabel label_50 = new JLabel("Date");
		label_50.setBounds(318, 252, 83, 14);
		specialPanel.add(label_50);

		textField_12 = new JTextField();
		textField_12.setColumns(10);
		textField_12.setBounds(396, 249, 86, 20);
		specialPanel.add(textField_12);

		JLabel label_51 = new JLabel("MM/DD/YYYY");
		label_51.setBounds(397, 269, 86, 14);
		specialPanel.add(label_51);

		JLabel label_52 = new JLabel("Vehicle\r\n");
		label_52.setBounds(318, 393, 83, 14);
		specialPanel.add(label_52);

		JComboBox comboBox_36 = new JComboBox();
		comboBox_36.setBounds(370, 393, 107, 20);
		specialPanel.add(comboBox_36);

		JButton btnSave_1 = new JButton("Save");
		btnSave_1.setBounds(489, 390, 89, 23);
		specialPanel.add(btnSave_1);

		JLabel label_53 = new JLabel("Start Time");
		label_53.setBounds(318, 290, 83, 14);
		specialPanel.add(label_53);

		textField_13 = new JTextField();
		textField_13.setColumns(10);
		textField_13.setBounds(396, 293, 86, 20);
		specialPanel.add(textField_13);

		JLabel label_54 = new JLabel("HH:MM AM/PM");
		label_54.setBounds(396, 315, 86, 14);
		specialPanel.add(label_54);

		textField_14 = new JTextField();
		textField_14.setColumns(10);
		textField_14.setBounds(396, 340, 86, 20);
		specialPanel.add(textField_14);

		JLabel label_55 = new JLabel("HH:MM AM/PM");
		label_55.setBounds(396, 361, 86, 14);
		specialPanel.add(label_55);

		JLabel label_56 = new JLabel("End Time");
		label_56.setBounds(318, 344, 83, 14);
		specialPanel.add(label_56);

		JSeparator separator_7 = new JSeparator();
		separator_7.setOrientation(SwingConstants.VERTICAL);
		separator_7.setBounds(679, 11, 8, 402);
		specialPanel.add(separator_7);

		JLabel lblDeleteASpecial = new JLabel("Delete a Special Schedule");
		lblDeleteASpecial.setBounds(734, 8, 130, 31);
		specialPanel.add(lblDeleteASpecial);

		JLabel label_57 = new JLabel("Start Location");
		label_57.setBounds(691, 64, 83, 14);
		specialPanel.add(label_57);

		JComboBox comboBox_37 = new JComboBox();
		comboBox_37.setBounds(780, 61, 106, 20);
		specialPanel.add(comboBox_37);

		JLabel label_58 = new JLabel("End Location");
		label_58.setBounds(691, 111, 83, 14);
		specialPanel.add(label_58);

		JComboBox comboBox_38 = new JComboBox();
		comboBox_38.setBounds(780, 108, 106, 20);
		specialPanel.add(comboBox_38);

		JLabel label_59 = new JLabel("Date");
		label_59.setBounds(691, 167, 83, 14);
		specialPanel.add(label_59);

		textField_15 = new JTextField();
		textField_15.setColumns(10);
		textField_15.setBounds(778, 164, 86, 20);
		specialPanel.add(textField_15);

		JLabel label_60 = new JLabel("MM/DD/YYYY");
		label_60.setBounds(778, 190, 86, 14);
		specialPanel.add(label_60);

		JLabel label_61 = new JLabel("Vehicle\r\n");
		label_61.setBounds(691, 231, 83, 14);
		specialPanel.add(label_61);

		JComboBox comboBox_39 = new JComboBox();
		comboBox_39.setBounds(779, 228, 107, 20);
		specialPanel.add(comboBox_39);

		JButton btnDelete_2 = new JButton("Delete");
		btnDelete_2.setBounds(748, 284, 89, 23);
		specialPanel.add(btnDelete_2);

		JLabel lblAddANew = new JLabel("Add a new Location");
		lblAddANew.setBounds(558, 24, 104, 14);
		manageSchedulePanel.add(lblAddANew);

		locationfield = new JTextField();
		locationfield.setBounds(672, 21, 135, 20);
		manageSchedulePanel.add(locationfield);
		locationfield.setColumns(10);

		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if (locationfield.getText().length() > 3) {
					dbmanager.addLocation(locationfield.getText());
					locationfield.setText("");
					comboBox_1.setModel(new DefaultComboBoxModel(dbmanager.getLocations()));

				} else {
					showErrorMessage("Please enter a valid location name");
				}

			}
		});

		btnAdd.setBounds(817, 20, 75, 27);
		manageSchedulePanel.add(btnAdd);

		JPanel manageVehiclesPanel = new JPanel();
		manageVehiclesPanel.setBackground(Color.WHITE);
		mainTabbedPane.addTab("Manage Vehicles", null, manageVehiclesPanel, null);
		manageVehiclesPanel.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(25, 30, 943, 501);
		manageVehiclesPanel.add(panel);
		panel.setLayout(null);

		JLabel lblAddAVehicle = new JLabel("Add a Vehicle");
		lblAddAVehicle.setBounds(114, 39, 87, 14);
		panel.add(lblAddAVehicle);

		JLabel lblVehicleName = new JLabel("Vehicle Name");
		lblVehicleName.setBounds(48, 88, 87, 14);
		panel.add(lblVehicleName);

		addVehicleName = new JTextField();
		addVehicleName.setBounds(177, 85, 107, 20);
		panel.add(addVehicleName);
		addVehicleName.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("Registration Number");
		lblNewLabel_1.setBounds(48, 143, 107, 14);
		panel.add(lblNewLabel_1);

		JLabel lblCapacity = new JLabel("Capacity");
		lblCapacity.setBounds(48, 196, 46, 14);
		panel.add(lblCapacity);

		JLabel lblNewLabel_2 = new JLabel("Vehicle Type");
		lblNewLabel_2.setBounds(48, 240, 87, 14);
		panel.add(lblNewLabel_2);

		JLabel lblAssignedDriver = new JLabel("Assigned Driver");
		lblAssignedDriver.setBounds(48, 286, 87, 14);
		panel.add(lblAssignedDriver);

		addRegNo = new JTextField();
		addRegNo.setColumns(10);
		addRegNo.setBounds(177, 140, 107, 20);
		panel.add(addRegNo);

		addCapacity = new JTextField();
		addCapacity.setColumns(10);
		addCapacity.setBounds(177, 193, 107, 20);
		panel.add(addCapacity);

		addVtype = new JTextField();
		addVtype.setColumns(10);
		addVtype.setBounds(177, 237, 107, 20);
		panel.add(addVtype);

		addAssignDriverNames = new JComboBox();
		addAssignDriverNames.setBounds(177, 283, 107, 20);
		panel.add(addAssignDriverNames);

		JButton btnAddVehicle = new JButton("Add Vehicle");
		btnAddVehicle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (addVehicleName.getText().toString().length() < 3) {

					showErrorMessage("Please enter a valid vehicle name");
				} else if (addRegNo.getText().toString().length() < 10) {

					showErrorMessage("Please enter a valid registration number");
				} else if (addCapacity.getText().toString().length() == 0) {

					showErrorMessage("Enter capacity for this vehicle");
				} else if (Integer.parseInt(addCapacity.getText()) < 3) {

					showErrorMessage("Capacity is too low for this vehicle.");
				} else if (addVtype.getText().length() < 3) {

					showErrorMessage("Vehicle type seems invalid. Please Check");
				} else if (addAssignDriverNames.getSelectedItem().toString().length() == 0) {

					showErrorMessage(
							"Please Assign a driver to this vehicle. \nWe discourage automatic driving vehicles");
				} else {

					// all validations passed
					dbmanager.addVehicle(addVehicleName.getText(), Long.parseLong(addRegNo.getText()),
							Integer.parseInt(addCapacity.getText()), addVtype.getText(),
							addAssignDriverNames.getSelectedItem().toString());
				}

			}
		});
		btnAddVehicle.setBounds(114, 343, 89, 23);
		panel.add(btnAddVehicle);

		JSeparator separator_8 = new JSeparator();
		separator_8.setOrientation(SwingConstants.VERTICAL);
		separator_8.setBounds(386, 39, 8, 434);
		panel.add(separator_8);

		JLabel lblEditAVehicle = new JLabel("Edit a Vehicle");
		lblEditAVehicle.setBounds(510, 50, 72, 14);
		panel.add(lblEditAVehicle);

		JLabel lblSelectAVehicle = new JLabel("Select a Vehicle");
		lblSelectAVehicle.setBounds(427, 88, 79, 14);
		panel.add(lblSelectAVehicle);

		selectVehicleNames = new JComboBox();
		selectVehicleNames.setBounds(584, 85, 87, 20);
		panel.add(selectVehicleNames);

		JButton btnEditSelect = new JButton("Select");
		btnEditSelect.setBounds(493, 113, 89, 23);
		panel.add(btnEditSelect);

		JSeparator separator_9 = new JSeparator();
		separator_9.setBounds(405, 155, 289, 2);
		panel.add(separator_9);

		JLabel label_35 = new JLabel("Vehicle Name");
		label_35.setBounds(427, 170, 87, 14);
		panel.add(label_35);

		editVehicleName = new JTextField();
		editVehicleName.setColumns(10);
		editVehicleName.setBounds(556, 167, 107, 20);
		panel.add(editVehicleName);

		JLabel label_62 = new JLabel("Registration Number");
		label_62.setBounds(427, 225, 107, 14);
		panel.add(label_62);

		editRegNo = new JTextField();
		editRegNo.setColumns(10);
		editRegNo.setBounds(556, 222, 107, 20);
		panel.add(editRegNo);

		JLabel label_63 = new JLabel("Capacity");
		label_63.setBounds(427, 278, 46, 14);
		panel.add(label_63);

		editCapacity = new JTextField();
		editCapacity.setColumns(10);
		editCapacity.setBounds(556, 275, 107, 20);
		panel.add(editCapacity);

		JLabel label_64 = new JLabel("Vehicle Type");
		label_64.setBounds(427, 322, 87, 14);
		panel.add(label_64);

		editVtype = new JTextField();
		editVtype.setColumns(10);
		editVtype.setBounds(556, 319, 107, 20);
		panel.add(editVtype);

		JLabel label_65 = new JLabel("Assigned Driver");
		label_65.setBounds(427, 368, 87, 14);
		panel.add(label_65);

		editAssignDriverNames = new JComboBox();
		editAssignDriverNames.setBounds(556, 365, 107, 20);
		panel.add(editAssignDriverNames);

		JButton btnSaveVehicle = new JButton("Save");
		btnSaveVehicle.setBounds(493, 425, 89, 23);
		panel.add(btnSaveVehicle);

		JSeparator separator_10 = new JSeparator();
		separator_10.setOrientation(SwingConstants.VERTICAL);
		separator_10.setBounds(720, 39, 8, 434);
		panel.add(separator_10);

		JLabel lblDeleteAVehicle = new JLabel("Delete a Vehicle");
		lblDeleteAVehicle.setBounds(792, 88, 107, 14);
		panel.add(lblDeleteAVehicle);

		JLabel label_67 = new JLabel("Select a Vehicle");
		label_67.setBounds(792, 143, 79, 14);
		panel.add(label_67);

		JButton btnDeleteVehicle = new JButton("Delete Vehicle");
		btnDeleteVehicle.setBounds(792, 282, 107, 23);
		panel.add(btnDeleteVehicle);

		deleteVehicleNames = new JComboBox();
		deleteVehicleNames.setBounds(792, 211, 87, 20);
		panel.add(deleteVehicleNames);

		JPanel manageUsersPanel = new JPanel();
		manageUsersPanel.setBackground(Color.WHITE);
		mainTabbedPane.addTab("Manage Drivers", null, manageUsersPanel, null);
		manageUsersPanel.setLayout(null);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(39, 35, 907, 489);
		manageUsersPanel.add(panel_1);
		panel_1.setLayout(null);

		JLabel lblAddADriver = new JLabel("Add a Driver");
		lblAddADriver.setBounds(137, 52, 81, 14);
		panel_1.add(lblAddADriver);

		JLabel lblDriverName = new JLabel("Driver Name");
		lblDriverName.setBounds(41, 109, 81, 14);
		panel_1.add(lblDriverName);

		addDriverName = new JTextField();
		addDriverName.setBounds(148, 106, 171, 20);
		panel_1.add(addDriverName);
		addDriverName.setColumns(10);

		JLabel lblLicenseNo = new JLabel("License No.");
		lblLicenseNo.setBounds(41, 172, 81, 14);
		panel_1.add(lblLicenseNo);

		addLicense = new JTextField();
		addLicense.setColumns(10);
		addLicense.setBounds(148, 169, 171, 20);
		panel_1.add(addLicense);

		JLabel lblGender = new JLabel("Gender");
		lblGender.setBounds(41, 250, 63, 14);
		panel_1.add(lblGender);

		JComboBox addGender = new JComboBox();
		addGender.setModel(new DefaultComboBoxModel(new String[] { "", "Male", "Female" }));
		addGender.setBounds(148, 247, 86, 20);
		panel_1.add(addGender);

		JLabel lblContact = new JLabel("Contact ");
		lblContact.setBounds(41, 335, 46, 14);
		panel_1.add(lblContact);

		addContact = new JTextField();
		addContact.setColumns(10);
		addContact.setBounds(148, 332, 171, 20);
		panel_1.add(addContact);

		JButton btnAddDriver = new JButton("Add Driver");
		btnAddDriver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if (addDriverName.getText().length() < 3) {

					showErrorMessage("Please enter a valid name for driver");
				} else if (addLicense.getText().length() < 10) {

					showErrorMessage("Please enter a Valid License Number");
				} else if (addGender.getSelectedItem().toString().length() == 0) {

					showErrorMessage("Please select gender");
				}

				else if (addContact.getText().length() < 10) {

					showErrorMessage("Please enter a valid contact number.");
				}

				else {
					// all validations passed

					dbmanager.addDriver(addDriverName.getText(), Long.parseLong(addLicense.getText()),
							addGender.getSelectedItem().toString(), Long.parseLong(addContact.getText()));
					// showSuccessMessage("Successfull entered data");
					addDriverName.setText("");
					addLicense.setText("");
					addGender.setSelectedIndex(0);
					addContact.setText("");
					updateDriversList();

				}

			}
		});
		btnAddDriver.setBounds(145, 396, 89, 23);
		panel_1.add(btnAddDriver);

		JSeparator separator_11 = new JSeparator();
		separator_11.setOrientation(SwingConstants.VERTICAL);
		separator_11.setBounds(396, 30, 9, 448);
		panel_1.add(separator_11);

		JLabel lblEditADriver = new JLabel("Edit a Driver");
		lblEditADriver.setBounds(493, 52, 81, 14);
		panel_1.add(lblEditADriver);

		JLabel lblSelectADriver = new JLabel("Select a Driver");
		lblSelectADriver.setBounds(422, 109, 81, 14);
		panel_1.add(lblSelectADriver);

		editDriverNames = new JComboBox();
		editDriverNames.setBounds(538, 106, 141, 20);
		panel_1.add(editDriverNames);

		JButton btnDriverSelect = new JButton("Select");
		btnDriverSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (editDriverNames.getSelectedItem().toString().length() == 0) {
					showErrorMessage("Please select a driver");

				} else {
					fillDriverData(editDriverNames.getSelectedItem().toString());
					enableDriverEdit();
				}
			}
		});

		btnDriverSelect.setBounds(485, 152, 89, 23);
		panel_1.add(btnDriverSelect);

		JSeparator separator_12 = new JSeparator();
		separator_12.setBounds(422, 216, 285, 2);
		panel_1.add(separator_12);

		JLabel label_66 = new JLabel("Driver Name");
		label_66.setBounds(415, 250, 81, 14);
		panel_1.add(label_66);

		editDriverName = new JTextField();
		editDriverName.setColumns(10);
		editDriverName.setBounds(522, 247, 171, 20);
		panel_1.add(editDriverName);

		JLabel label_68 = new JLabel("License No.");
		label_68.setBounds(415, 305, 81, 14);
		panel_1.add(label_68);

		editLicenseNo = new JTextField();
		editLicenseNo.setColumns(10);
		editLicenseNo.setBounds(522, 302, 171, 20);
		panel_1.add(editLicenseNo);

		JLabel label_69 = new JLabel("Gender");
		label_69.setBounds(415, 355, 63, 14);
		panel_1.add(label_69);

		editGender = new JComboBox();
		editGender.setModel(new DefaultComboBoxModel(new String[] { "", "Male", "Female" }));
		editGender.setBounds(522, 352, 86, 20);
		panel_1.add(editGender);

		JLabel label_70 = new JLabel("Contact ");
		label_70.setBounds(415, 399, 46, 14);
		panel_1.add(label_70);

		editContact = new JTextField();
		editContact.setColumns(10);
		editContact.setBounds(522, 396, 171, 20);
		panel_1.add(editContact);

		btnSaveDriver = new JButton("Save");
		btnSaveDriver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if (editDriverName.getText().length() < 3) {

					showErrorMessage("Please enter a valid name for driver");
				} else if (editLicenseNo.getText().length() < 10) {

					showErrorMessage("Please enter a Valid License Number");
				} else if (editGender.getSelectedItem().toString().length() == 0) {

					showErrorMessage("Please select gender");
				}

				else if (editContact.getText().length() < 10) {

					showErrorMessage("Please enter a valid contact number.");
				}

				else {
					// all validations passed

					dbmanager.editDriver(editDriverNames.getSelectedItem().toString(), editDriverName.getText(),
							Long.parseLong(editLicenseNo.getText()), editGender.getSelectedItem().toString(),
							Long.parseLong(editContact.getText()));
					editDriverName.setText("");
					editLicenseNo.setText("");
					editGender.setSelectedIndex(0);
					editContact.setText("");
					updateDriversList();
					disableDriverEdit();

				}

			}
		});
		btnSaveDriver.setBounds(519, 439, 89, 23);
		panel_1.add(btnSaveDriver);

		JSeparator separator_13 = new JSeparator();
		separator_13.setOrientation(SwingConstants.VERTICAL);
		separator_13.setBounds(731, 30, 9, 448);
		panel_1.add(separator_13);

		JButton btnDelete_3 = new JButton("Delete");
		btnDelete_3.setBounds(750, 216, 141, 23);
		panel_1.add(btnDelete_3);

		deleteDriverNames = new JComboBox();
		deleteDriverNames.setBounds(750, 153, 141, 20);
		panel_1.add(deleteDriverNames);

		JLabel label_71 = new JLabel("Select a Driver");
		label_71.setBounds(770, 109, 81, 14);
		panel_1.add(label_71);

		JLabel lblDeleteADriver = new JLabel("Delete a Driver");
		lblDeleteADriver.setBounds(770, 52, 81, 14);
		panel_1.add(lblDeleteADriver);

		JLabel iconLabel = new JLabel("");
		iconLabel.setIcon(new ImageIcon(
				"H:\\workspace\\Transport Management System\\src\\tms\\University_of_Bridgeport.svg.png"));
		iconLabel.setBounds(10, 11, 153, 121);
		frmTransportManagementSystem.getContentPane().add(iconLabel);

		JLabel lblNewLabel = new JLabel("College Shuttle/Transport Management System");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 36));
		lblNewLabel.setBounds(292, 36, 749, 54);
		frmTransportManagementSystem.getContentPane().add(lblNewLabel);

	}

	public void showSuccessMessage(String message) {
		JOptionPane.showMessageDialog(new JFrame(), message, "Warning", JOptionPane.INFORMATION_MESSAGE);

	}

	public void showErrorMessage(String message) {
		JOptionPane.showMessageDialog(new JFrame(), message, "Warning", JOptionPane.WARNING_MESSAGE);

	}

	public void updateDriversList() {

		editDriverNames.setModel(new DefaultComboBoxModel(dbmanager.getDrivers()));
		deleteDriverNames.setModel(new DefaultComboBoxModel(dbmanager.getDrivers()));
		addAssignDriverNames.setModel(new DefaultComboBoxModel(dbmanager.getDrivers()));
		editAssignDriverNames.setModel(new DefaultComboBoxModel(dbmanager.getDrivers()));

	}

	public void disableDriverEdit() {

		editDriverName.setEditable(false);
		editLicenseNo.setEditable(false);
		editGender.setEditable(false);
		editContact.setEditable(false);
		btnSaveDriver.setEnabled(false);

	}

	public void enableDriverEdit() {
		editDriverName.setEditable(true);
		editLicenseNo.setEditable(true);
		editGender.setEditable(true);
		editContact.setEditable(true);
		btnSaveDriver.setEnabled(true);

	}

	public void fillDriverData(String dname) {

		String driverData[] = dbmanager.getDriverData(dname);

		editDriverName.setText(driverData[0]);
		editLicenseNo.setText(driverData[1]);
		editGender.setSelectedItem(driverData[2]);
		editContact.setText(driverData[3]);

	}

	public void updateVehiclesList() {

		String[] vehicles = dbmanager.getVehicleNames();

		selectVehicleNames.setModel(new DefaultComboBoxModel(vehicles));
		deleteVehicleNames.setModel(new DefaultComboBoxModel(vehicles));

	}

	public void disableVehicleEdit() {
		editVehicleName.setEditable(false);
		editRegNo.setEditable(false);
		editCapacity.setEditable(false);
		editVtype.setEditable(false);
		editAssignDriverNames.setEditable(false);

	}

	public void enableVehicleEdit() {
		editVehicleName.setEditable(true);
		editRegNo.setEditable(true);
		editCapacity.setEditable(true);
		editVtype.setEditable(true);
		editAssignDriverNames.setEditable(true);

	}
}
