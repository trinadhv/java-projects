package visualservoing;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import de.yadrone.base.command.CommandManager;
import de.yadrone.base.navdata.ControlState;
import de.yadrone.base.navdata.DroneState;
import de.yadrone.base.navdata.StateListener;
import de.yadrone.base.video.ImageListener;

class DroneVideoListener extends JPanel

{

	private static final long serialVersionUID = 1L;
	private BufferedImage image;
	private double xavg, yavg;
	private int count = 0, posX, posY;
	private JLabel lblXaxisValue, lblYaxisValue, lblDroneStateValue, lblObjectTrackStatus;
	private final JFrame gui;

	private int minX, minY, maxX, maxY, height, width;

	private CommandManager cmd;

	private ControlState droneState;

	public DroneVideoListener(final de.yadrone.base.ARDrone drone, final JFrame frmGui) {
		super();

		gui = frmGui;

		cmd = drone.getCommandManager();

		setSize(640, 360);
		setVisible(true);

		xavg = 0;
		yavg = 0;
		posX = 0;
		posY = 0;

		initComponents();

		drone.getNavDataManager().addStateListener(new StateListener() {
			@Override
			public void stateChanged(DroneState state) {

			}

			@Override
			public void controlStateChanged(ControlState state) {

				droneState = state;
				lblDroneStateValue.setText(state.toString());
			}

		});

		drone.getVideoManager().addImageListener(new ImageListener() {
			Color white = new Color(255, 255, 255);
			int whiteRGB = white.getRGB();
			Color black = new Color(0,0,0);
			int blackRGB = black.getRed();

			@Override
			public void imageUpdated(BufferedImage newImage) {

				image = newImage;
				double sumx = 0, sumy = 0;
				count = 0;
				minX = 0;
				minY = 0;

				for (int x = 0; x < image.getWidth(); ++x) {
					for (int y = 0; y < image.getHeight(); ++y) {
						int rgb = image.getRGB(x, y);
						int r = (rgb >> 16) & 0xFF;
						int g = (rgb >> 8) & 0xFF;
						int b = (rgb & 0xFF);

						//int grayLevel = (r + g + b) / 3;
						//int gray = (grayLevel << 16) + (grayLevel << 8) + grayLevel;

						if (g > 120 && r < 90 && b < 90) {

							// If pixel matches threshold, start counting
							image.setRGB(x,y,whiteRGB);

							if (count == 0) {

								sumx = x;
								sumy = y;

								count++;

								minX = x;
								maxX = x;
								minY = y;
								maxY = y;

							}

							else {

								sumx += x;
								sumy += y;
								count++;

								if (maxX < x) {
									maxX = x;
								}

								if (maxY < y) {

									maxY = y;
								}

							}

						}

						// if pixel not matched
						else {

							image.setRGB(x, y, blackRGB);

						}

					} // y-for loop end

				} // x-for loop end

				height = maxY - minY;
				width = maxX - minX;

				if (height * width > 350) // if object area is > 350
				{

					xavg = (int) ((sumx / count));
					yavg = (int) ((sumy / count));
					posX = (int) xavg;
					posY = (int) yavg;

					updateLabels();
					controlDrone();

				}

				else

				{

					xavg = 0;
					yavg = 0;
					maxX = 0;
					maxY = 0;
					height = 0;
					width = 0;
					posX = 0;
					posY = 0;
					resetLabels();
				}

				count = 0; // donot remove this. Resets count for each loop.

				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						repaint();
					}
				});

			}

		});
	}

	public void initComponents() {

		lblXaxisValue = new JLabel("X-Axis Value");
		lblXaxisValue.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblXaxisValue.setBounds(137, 447, 115, 14);
		gui.getContentPane().add(lblXaxisValue);

		lblYaxisValue = new JLabel("Y-Axis Value");
		lblYaxisValue.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblYaxisValue.setBounds(137, 478, 115, 14);
		gui.getContentPane().add(lblYaxisValue);

		lblDroneStateValue = new JLabel("Drone State Value");
		lblDroneStateValue.setBounds(828, 260, 96, 14);
		gui.getContentPane().add(lblDroneStateValue);

		lblObjectTrackStatus = new JLabel("Object Track Status");
		lblObjectTrackStatus.setForeground(Color.RED);
		lblObjectTrackStatus.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblObjectTrackStatus.setBounds(137, 422, 140, 14);
		gui.getContentPane().add(lblObjectTrackStatus);
	}

	public void updateLabels() {

		lblXaxisValue.setText("" + posX);
		lblYaxisValue.setText("" + posY);
		lblObjectTrackStatus.setForeground(Color.GREEN);
		lblObjectTrackStatus.setText("Target Aquired");
	}

	public void resetLabels() {

		lblXaxisValue.setText("" + posX);
		lblYaxisValue.setText("" + posY);
		lblObjectTrackStatus.setForeground(Color.RED);
		lblObjectTrackStatus.setText("No Target");
	}

	public void controlDrone() {

		if (droneState.equals(ControlState.HOVERING) || droneState.equals(ControlState.FLYING)) {

			if (posY < 100 && posY > 0) {

				cmd.up(25).doFor(20);

			}

			if (posY > 260 && posY < 360) {

				cmd.down(25).doFor(20);

			}

			if (posX < 150 && posX > 0) {

				cmd.spinLeft(25).doFor(20);

			}

			if (posX > 500 && posX < 640) {

				cmd.spinRight(25).doFor(20);

			}

			else {
				cmd.hover();
			}

		}

	}

	@Override
	public void paintComponent(Graphics g) {

		if (image != null)
			g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);

		g.setColor(Color.GREEN);
		g.drawRect((int) xavg - width / 2, (int) yavg - height / 2, width, height);
		g.drawLine((int) xavg, 0, (int) xavg, 360);
		g.drawLine(0, (int) yavg, 640, (int) yavg);
		g.drawString("x:" + xavg + " , y: " + yavg, 300, 330);

	}

}
