package zorq;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JCheckBox;
import java.awt.GridBagLayout;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public final class SettingsDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel upperPanel = new JPanel();

	private JCheckBox headlessCheckBox;
	private JCheckBox scrollingCheckBox;
	private JCheckBox logCheckBox;
	private JCheckBox multithreadCheckBox;
	JSlider universeWidthSlider;

	/*
	public static void main(String[] args) {
		try {
			SettingsDialog dialog = new SettingsDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	 */

	/**
	 * Create the dialog.
	 */
	public SettingsDialog() {
		super( (Frame)null, "ZORQ Settings", true);
		float ratio = (float)Constants.DISPLAY_HEIGHT/(float)Constants.DISPLAY_WIDTH;

		setBounds(100, 100, 500, 400);
		//getContentPane().setLayout(new BorderLayout());
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

		/*
		{
			JLabel lblSettings = new JLabel("Settings:");
			getContentPane().add(lblSettings);
		}
		 */
		upperPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(upperPanel); //, BorderLayout.CENTER);
		//upperPanel.setLayout(new GridLayout(2, 0, 0, 0));
		upperPanel.setLayout(new BoxLayout(upperPanel, BoxLayout.Y_AXIS));
		{
			JPanel settingsPane = new JPanel();
			upperPanel.add(settingsPane);
			settingsPane.setLayout(new GridLayout(1,2, 0, 0));
			{
				JPanel leftPane = new JPanel();
				settingsPane.add(leftPane);
				leftPane.setLayout(new GridLayout(10,1, 0, 0));
				{
					headlessCheckBox = new JCheckBox("Headless mode");
					headlessCheckBox.setSelected(Constants.HEADLESS_MODE);
					leftPane.add(headlessCheckBox);
				}
				{
					logCheckBox = new JCheckBox("Log game");
					logCheckBox.setSelected(Constants.LOG);
					leftPane.add(logCheckBox);
				}
				{
					scrollingCheckBox = new JCheckBox("Scrolling background");
					scrollingCheckBox.setSelected(Constants.SCROLLING_BACKGROUND);
					leftPane.add(scrollingCheckBox);
				}
				{
					multithreadCheckBox = new JCheckBox("Multithread");
					multithreadCheckBox.setSelected(Constants.MULTITHREADED);
					leftPane.add(multithreadCheckBox);
				}
				{
					leftPane.add(new JCheckBox("tbd"));
					leftPane.add(new JCheckBox("tbd"));
					leftPane.add(new JCheckBox("tbd"));
					leftPane.add(new JCheckBox("tbd"));
					leftPane.add(new JCheckBox("tbd"));
					leftPane.add(new JCheckBox("tbd"));
				}
			}
			{
				JPanel rightPane = new JPanel();
				settingsPane.add(rightPane);
				rightPane.setLayout(new GridLayout(4, 2, 0, 0));
				{
					JLabel spawnLabel1 = new JLabel("tbd spawn rate1:");
					rightPane.add(spawnLabel1);
				}
				{
					JSlider spawn1slider = new JSlider();
					rightPane.add(spawn1slider);
				}
				{
					JLabel spawnLabel2 = new JLabel("tbd spawn rate2:");
					rightPane.add(spawnLabel2);
				}
				{
					JSlider spawn2slider = new JSlider();
					rightPane.add(spawn2slider);
				}
				{
					JPanel sizeLabelPanel = new JPanel();
					rightPane.add(sizeLabelPanel);
					sizeLabelPanel.setLayout(new GridLayout(2,1, 0, 0));
					int width = Universe.getWidth();
					int height = (int)(width*ratio);
					universeWidthSlider = new JSlider(Universe.MIN_WIDTH,Universe.MAX_WIDTH,Universe.getWidth());
					JLabel universeWidthLabel = new JLabel("Width:"+universeWidthSlider.getValue());
					JLabel universeHeightLabel = new JLabel("Height:"+height);
					universeWidthSlider.addChangeListener(new ChangeListener() {
						public void stateChanged(ChangeEvent e) {
							int width = ((JSlider)e.getSource()).getValue();
							int height = (int)(width*ratio);
							universeWidthLabel.setText("Width:" + width);
							universeHeightLabel.setText("Height:" + height);
						}
					});
					sizeLabelPanel.add(universeWidthLabel);
					sizeLabelPanel.add(universeHeightLabel);
					rightPane.add(sizeLabelPanel);
					rightPane.add(universeWidthSlider);
				}
			}
		}
		{
			JPanel settingsSavePane = new JPanel();
			upperPanel.add(settingsSavePane);
			settingsSavePane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			{
				JButton settingsSaveButton = new JButton("Save");
				settingsSaveButton.setActionCommand("Save");
				settingsSavePane.add(settingsSaveButton);
				settingsSaveButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						JOptionPane.showMessageDialog(SettingsDialog.this,"Not implemented");
					}
				});
			}
			{
				JButton settingsRestoreButton = new JButton("Restore");
				settingsRestoreButton.setActionCommand("Restore");
				settingsSavePane.add(settingsRestoreButton);
				settingsRestoreButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						JOptionPane.showMessageDialog(SettingsDialog.this,"Not implemented");
					}
				});
			}
		}
		{
			JSeparator s = new JSeparator();
			s.setOrientation(SwingConstants.HORIZONTAL);
			upperPanel.add(s);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {

						Constants.HEADLESS_MODE = headlessCheckBox.isSelected();
						Constants.SCROLLING_BACKGROUND = scrollingCheckBox.isSelected();
						Constants.LOG = logCheckBox.isSelected();
						Constants.MULTITHREADED = multithreadCheckBox.isSelected();
						Universe.setWidth(universeWidthSlider.getValue());
						Universe.setHeight( (int)(universeWidthSlider.getValue()*ratio) );			        	 SettingsDialog.this.dispose();
					}
				});
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						System.exit(0);
						//JOptionPane.showMessageDialog(SettingsDialog.this,"Not implemented");
					}
				});
			}
		}
		// Close the program if X is selected the close the window
		addWindowListener(new WindowAdapter()
		{ public void windowClosing(WindowEvent e)
		{ System.exit(0); }
		} );
	}

}
