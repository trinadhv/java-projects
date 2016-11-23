package visualservoing;

import java.awt.EventQueue;
import de.yadrone.base.*;
import de.yadrone.base.command.CommandManager;
import de.yadrone.base.command.EnemyColor;
import de.yadrone.base.command.VideoChannel;
import de.yadrone.base.navdata.Altitude;
import de.yadrone.base.navdata.AltitudeListener;
import de.yadrone.base.navdata.AttitudeListener;
import de.yadrone.base.navdata.BatteryListener;
import de.yadrone.base.navdata.CadType;
import de.yadrone.base.navdata.WifiListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import java.awt.Color;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import java.awt.Font;

public class GUI {

	private JPanel videoPanel;
	public ARDrone drone;
	private JFrame frmGUI;
	private JLabel lblRollValue, lblPitch, lblYawValue, lblBatteryValue;
	private CommandManager cmd;

	private JTextArea textArea;
	private JLabel lblObjectTracked;
	private JLabel lblAreaValue, lblOrientationValue,
			lblAltitudeValue, lblSignalValue;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frmGUI.setVisible(true);
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

	public void getStats() {
		drone.getNavDataManager().addAttitudeListener(new AttitudeListener() {

			public void attitudeUpdated(float pitch, float roll, float yaw) {
				// System.out.println("Pitch: " + pitch + " Roll: " + roll + "
				// Yaw: " + yaw);

				lblPitch.setText("" + Math.round(pitch / 1000));
				lblRollValue.setText("" + Math.round(roll / 1000));
				lblYawValue.setText("" + Math.round(yaw / 1000));
			}

			public void attitudeUpdated(float pitch, float roll) {
			}

			public void windCompensation(float pitch, float roll) {
			}
		});

		drone.getNavDataManager().addBatteryListener(new BatteryListener() {

			public void batteryLevelChanged(int percentage) {
				// System.out.println("Battery: " + percentage + " %");
				lblBatteryValue.setText("" + percentage + "%");
			}

			public void voltageChanged(int vbat_raw) {
			}
		});

		drone.getCommandManager().setVideoChannel(VideoChannel.HORI);

		drone.getNavDataManager().addAltitudeListener(new AltitudeListener() {

			@Override
			public void receivedAltitude(int altitude) {

				// TODO Auto-generated method stub

			}

			@Override
			public void receivedExtendedAltitude(Altitude d) {
				// TODO Auto-generated method stub

			}

		});

		drone.getNavDataManager().addWifiListener(new WifiListener() {

			@Override
			public void received(long link_quality) {

				// TODO Auto-generated method stub
				lblSignalValue.setText("" + link_quality);

			}

		});

		videoPanel = new DroneVideoListener(drone, frmGUI);
		videoPanel.setBackground(Color.BLACK);
		videoPanel.setBounds(22, 22, 640, 360);

		frmGUI.getContentPane().add(videoPanel);
		cmd.setVisionOption(2);

	}

	public void connectDrone() {
		try {

			drone = new de.yadrone.base.ARDrone();
			drone.start();

			// disconnected=false;

			JOptionPane.showMessageDialog(null, "Connection Succesfull");
			cmd = drone.getCommandManager();
			cmd.flatTrim();
			cmd.setOutdoor(false, false);
			cmd.setEnemyColors(EnemyColor.ORANGE_BLUE);
			cmd.setDetectEnemyWithoutShell(false);
			cmd.setDetectionType(CadType.VERTICAL);

			cmd.setVisionParameters(10, 2, 2, 500, 500, 2, 600, 200, 20);
			getStats();

			// startTracking();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null,
					"Connection Failed.\n Make sure that drone is turned on and WiFi is connected", "Connection Error",
					JOptionPane.ERROR_MESSAGE);
			drone = null;
			e.printStackTrace();
		}

	}

	private void initialize() {
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
		frmGUI = new JFrame();
		frmGUI.getContentPane().setFont(new Font("Tahoma", Font.BOLD, 16));
		frmGUI.setTitle("Visual Servoing using Autonomous Quadcopter - Trinadh Venna");
		frmGUI.setResizable(false);
		frmGUI.setBounds(100, 100, 1200, 738);
		frmGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmGUI.getContentPane().setLayout(null);

		videoPanel = new JPanel();
		videoPanel.setBackground(Color.BLACK);
		videoPanel.setBounds(22, 22, 640, 360);
		frmGUI.getContentPane().add(videoPanel);

		JButton btnNewButton = new JButton("Connect");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				connectDrone();

			}
		});
		btnNewButton.setBounds(955, 22, 89, 23);
		frmGUI.getContentPane().add(btnNewButton);

		JButton btnDisconnect = new JButton("Disconnect");
		btnDisconnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (drone != null) {

					drone.stop();

					JOptionPane.showMessageDialog(null, "Disconnected from drone.");
				}

			}
		});
		btnDisconnect.setBounds(1084, 22, 89, 23);
		frmGUI.getContentPane().add(btnDisconnect);

		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(934, 11, 11, 687);
		frmGUI.getContentPane().add(separator);

		JButton btnUp = new JButton("Up");
		btnUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// cmd.up(5).doFor(500);
				drone.up();
			}
		});
		btnUp.setBounds(955, 312, 89, 23);
		frmGUI.getContentPane().add(btnUp);

		JButton btnTiltLeft = new JButton("Tilt Left");
		btnTiltLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cmd.goLeft(5).doFor(500);
			}
		});
		btnTiltLeft.setBounds(955, 366, 89, 23);
		frmGUI.getContentPane().add(btnTiltLeft);

		JButton btnTiltRight = new JButton("Tilt Right");
		btnTiltRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cmd.goRight(5).doFor(500);
			}
		});
		btnTiltRight.setBounds(1095, 366, 89, 23);
		frmGUI.getContentPane().add(btnTiltRight);

		JButton btnDown = new JButton("Down");
		btnDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// cmd.down(5).doFor(500);
				drone.down();
			}
		});
		btnDown.setBounds(955, 484, 89, 23);
		frmGUI.getContentPane().add(btnDown);

		JButton btnTakeOff = new JButton("Take Off");
		btnTakeOff.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				drone.takeOff();
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				drone.hover();
			}
		});
		btnTakeOff.setBounds(955, 181, 89, 23);
		frmGUI.getContentPane().add(btnTakeOff);

		JButton btnLand = new JButton("Land");
		btnLand.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drone.landing();
				cmd.flatTrim();
			}
		});
		btnLand.setBounds(1084, 181, 89, 23);
		frmGUI.getContentPane().add(btnLand);

		JButton btnTurnLeft = new JButton("Turn Left");
		btnTurnLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cmd.spinLeft(5).doFor(500);

			}
		});
		btnTurnLeft.setBounds(955, 418, 89, 23);
		frmGUI.getContentPane().add(btnTurnLeft);

		JButton btnTurnRight = new JButton("Turn Right");
		btnTurnRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cmd.spinRight(5).doFor(500);
			}
		});
		btnTurnRight.setBounds(1095, 418, 89, 23);
		frmGUI.getContentPane().add(btnTurnRight);

		JPanel panel = new JPanel();
		panel.setBounds(22, 527, 558, 144);
		frmGUI.getContentPane().add(panel);
		panel.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 558, 144);
		panel.add(scrollPane);

		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);

		JButton btnForward = new JButton("Forward");
		btnForward.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cmd.forward(5).doFor(1000);
			}
		});
		btnForward.setBounds(1095, 312, 89, 23);
		frmGUI.getContentPane().add(btnForward);

		JButton btnBackward = new JButton("Backward");
		btnBackward.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cmd.backward(5).doFor(1000);

			}
		});
		btnBackward.setBounds(1095, 484, 89, 23);
		frmGUI.getContentPane().add(btnBackward);

		lblRollValue = new JLabel("Roll Value");
		lblRollValue.setBounds(828, 54, 46, 14);
		frmGUI.getContentPane().add(lblRollValue);

		lblPitch = new JLabel("Pitch Value");
		lblPitch.setBounds(828, 96, 60, 14);
		frmGUI.getContentPane().add(lblPitch);

		lblYawValue = new JLabel("Yaw Value");
		lblYawValue.setBounds(828, 135, 60, 14);
		frmGUI.getContentPane().add(lblYawValue);

		JLabel lblDroneStats = new JLabel("Drone Stats");
		lblDroneStats.setBounds(775, 22, 79, 14);
		frmGUI.getContentPane().add(lblDroneStats);

		JLabel lblRoll = new JLabel("Roll :");
		lblRoll.setBounds(726, 54, 46, 14);
		frmGUI.getContentPane().add(lblRoll);

		JLabel lblPitch_1 = new JLabel("Pitch :");
		lblPitch_1.setBounds(726, 96, 46, 14);
		frmGUI.getContentPane().add(lblPitch_1);

		JLabel lblYaw = new JLabel("Yaw :");
		lblYaw.setBounds(726, 135, 46, 14);
		frmGUI.getContentPane().add(lblYaw);

		JLabel lblBatteryLeft = new JLabel("Battery Left:");
		lblBatteryLeft.setBounds(726, 219, 79, 14);
		frmGUI.getContentPane().add(lblBatteryLeft);

		lblBatteryValue = new JLabel("Battery Value");
		lblBatteryValue.setBounds(828, 219, 79, 14);
		frmGUI.getContentPane().add(lblBatteryValue);

		JButton btnHover = new JButton("Hover");
		btnHover.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cmd.hover();
			}
		});
		btnHover.setBounds(1020, 239, 89, 23);
		frmGUI.getContentPane().add(btnHover);

		JLabel lblDroneState = new JLabel("Drone State:");
		lblDroneState.setBounds(726, 260, 79, 14);
		frmGUI.getContentPane().add(lblDroneState);

		JLabel lblTrackingStatus = new JLabel("Tracking Status");
		lblTrackingStatus.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 13));
		lblTrackingStatus.setBounds(22, 397, 148, 14);
		frmGUI.getContentPane().add(lblTrackingStatus);

		lblObjectTracked = new JLabel("Object Tracked:");
		lblObjectTracked.setBounds(22, 422, 78, 14);
		frmGUI.getContentPane().add(lblObjectTracked);

		JLabel lblXaxis = new JLabel("X-Axis:");
		lblXaxis.setBounds(22, 447, 46, 14);
		frmGUI.getContentPane().add(lblXaxis);

		JLabel lblYaxis = new JLabel("Y-Axis:");
		lblYaxis.setBounds(22, 478, 46, 14);
		frmGUI.getContentPane().add(lblYaxis);

		JLabel lblArea = new JLabel("Area:");
		lblArea.setBounds(301, 422, 46, 14);
		frmGUI.getContentPane().add(lblArea);

		lblAreaValue = new JLabel("Area Value");
		lblAreaValue.setBounds(416, 422, 65, 14);
		frmGUI.getContentPane().add(lblAreaValue);

		JLabel lblOrientation = new JLabel("Orientation:");
		lblOrientation.setBounds(301, 447, 65, 14);
		frmGUI.getContentPane().add(lblOrientation);

		lblOrientationValue = new JLabel("Orientation Value");
		lblOrientationValue.setBounds(416, 447, 98, 14);
		frmGUI.getContentPane().add(lblOrientationValue);

		JLabel lblAltitude = new JLabel("Altitude:");
		lblAltitude.setBounds(726, 174, 46, 14);
		frmGUI.getContentPane().add(lblAltitude);

		lblAltitudeValue = new JLabel("Altitude Value");
		lblAltitudeValue.setBounds(828, 174, 79, 14);
		frmGUI.getContentPane().add(lblAltitudeValue);

		JLabel lblSignalStrength = new JLabel("Signal Strength:");
		lblSignalStrength.setBounds(726, 298, 79, 14);
		frmGUI.getContentPane().add(lblSignalStrength);

		lblSignalValue = new JLabel("SignalValue");
		lblSignalValue.setBounds(828, 298, 96, 14);
		frmGUI.getContentPane().add(lblSignalValue);

		frmGUI.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {
					drone.stop();

				} catch (NullPointerException ne) {
					System.exit(0);
				}

				System.exit(0);

			}
		});

	}
}
