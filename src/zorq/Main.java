package zorq;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Top level class for starting the game. 
 * 
 * <p>First displays a JDialog to get the game settings, followed by 
 * another JDialog to get the controllers.
 * 
 * <p>When running in non-headless mode, this code creates a JFrame 
 * within which the game display panel (Visualizer) is embedded.
 * 
 * <p>This class also creates the game engine, connects it to the 
 * visualizer and controllers (which it retrieves), and starts the game.
 * 
 * <p>Using enum singleton pattern
 * 
 * @author hastings
 *
 */
//public class Main {
	public enum Main {
	    INSTANCE;

	Visualizer v;
	Engine e = null;
	Vector<String> controllers;
	private final JFrame mainFrame = new JFrame();

	protected JFrame getMainFrame() {return mainFrame; }
	private Main () {

		getSettings();
		controllers = getControllers();

		if (Constants.HEADLESS_MODE)
			v = null;
		else {
			v = Visualizer.INSTANCE;
			//mainFrame = MainFrame.INSTANCE;
			//mainFrame = new JFrame();
			Container c = mainFrame.getContentPane();
			c.setLayout(new BorderLayout());
			c.add(v, BorderLayout.SOUTH);
		}

		try { e = Engine.getInstance(v, controllers); }
		catch (Exception e) { System.err.println("Engine creation failed"); }

		if (mainFrame != null) {
			mainFrame.addKeyListener(e);

			mainFrame.addWindowListener(new java.awt.event.WindowAdapter() {
				@Override
				public void windowClosing(java.awt.event.WindowEvent windowEvent) {
					if (JOptionPane.showConfirmDialog(Main.this.mainFrame, 
							"Are you sure to close this window?", "Really Closing?", 
							JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
						System.exit(0);
					}
				}
			});
			mainFrame.pack();
			mainFrame.setVisible(true);
		}
	}

	private void getSettings() {	
		SettingsDialog sd = new SettingsDialog( );
		sd.setVisible(true);
	}

	// Are both of these needed???
	public void start() { e.run(); }
	public void stop() { e.stop(); }

	public static void main(String[] args) throws Exception {
		Main.INSTANCE.start();
	}

	private Vector<String> getControllers() {	
		ControllerSelecterFrame csf = new ControllerSelecterFrame( );
		csf.setVisible(true);
		return csf.getSelections();
	}

	private class ControllerSelecterFrame extends JDialog {

		Button okButton;
		GridBagLayout layout;
		JLabel heading; //, spacing;
		JList<String> jlist;

		// return a Vector containing the names of the selected controllers (as Strings)
		// Modified 11-18-2019 due to deprecation of getSelectedValues
		public Vector<String> getSelections() { 
			List<String> objselections = jlist.getSelectedValuesList();
			return new Vector<String>(objselections);
		}

		public ControllerSelecterFrame() {
			super( (Frame)null, "Welcome", true);
			setSize(400,400);

			Vector<String> controllers = new Vector<String>();

			try {
				// Find controllers in jar file
				Collection<Class> jarcontrollers = getClassesForPackage("zorq.controllers");
				for (Class c : jarcontrollers) {
					controllers.add(c.getName());
				}
				
				// Find controllers in src controller folder
				Collection<Class> srcClasses = getClasses("zorq.controllers");
				for (Class c : srcClasses)
					controllers.add(c.getName());
			}
			catch (Exception e) {e.printStackTrace();}

			this.setLayout(new BorderLayout(10, 10));
			layout = new GridBagLayout();

			JPanel north = new JPanel();
			north.setLayout(layout);
			JPanel center = new JPanel();
			center.setLayout(layout);
			JPanel south = new JPanel();
			south.setLayout(layout);

			Collections.sort(controllers, String.CASE_INSENSITIVE_ORDER);
			jlist = new JList<String>(controllers);

			final GridBagConstraints constraints = new GridBagConstraints();

			constraints.gridx = 0; // note: gridy == RELATIVE 
			constraints.fill = 0;

			heading = new JLabel("Please select the controller(s) that you wish to use:");

			okButton = new Button("OK");
			okButton.addActionListener(new
					ActionListener() 
			{
				public void actionPerformed(ActionEvent event) {
					ControllerSelecterFrame.this.dispose();  // close the dialog when the user clicks OK
				}
			} );

			Container contentPane = this.getContentPane();

			north.add(heading);
			layout.setConstraints(heading, constraints);

			//contentPane.add(jlist);
			JScrollPane scrollPane = new JScrollPane(jlist);   // make list of controllers scrollable
			scrollPane.setPreferredSize(new Dimension(500, 500));
			center.add(scrollPane);

			layout.setConstraints(jlist, constraints);

			south.add(okButton);
			layout.setConstraints(okButton, constraints);

			contentPane.add(north,BorderLayout.NORTH);
			contentPane.add(center,BorderLayout.CENTER);
			contentPane.add(south,BorderLayout.SOUTH);
			this.pack();

			// include to make window close when user hits X box
			addWindowListener(new WindowAdapter()
			{ public void windowClosing(WindowEvent e)
			{ System.exit(0); }
			} );

		}

		/**
		 * Scans the current class path for the ZorqControllers jar file, and then scans
		 * the for all the classes directly under the package name in question.
		 * Package name should be "zorq.controllers". Maybe should use URI instead of
		 * URL
		 * 
		 * Old approach which no longer works with Java 9 due to changes in Java:
		 * Scans all classloaders for the current thread for loaded jars, and then scans
		 * each jar for the package name in question, listing all classes directly under
		 * the package name in question. Assumes directory structure in jar file and class
		 * package naming follow java conventions (i.e. com.example.test.MyTest would be in
		 * /com/example/test/MyTest.class)
		 */
		public Collection<Class> getClassesForPackage(String packageName) throws Exception {
			Set<URL> jarUrls = new HashSet<URL>();
			String[] pathElements = System.getProperty("java.class.path").split(System.getProperty("path.separator"));
			for (String path: pathElements) {
				if (path.contains("ZorqControllers")) {
					File f = new File(path);
					URL u = f.toURI().toURL();  //modified 11-19-2019 due to warning
					jarUrls.add(u);
				}
			}
			String packagePath = packageName.replace(".", "/");

			Set<Class> classes = new HashSet<Class>();

			for (URL url : jarUrls) {
				JarInputStream stream = new JarInputStream(url.openStream()); // may want better way to open url connections
				JarEntry entry = stream.getNextJarEntry();

				while (entry != null) {
					String name = entry.getName();
					int i = name.lastIndexOf("/");
//System.out.println("name = " + name);
					if (i > 0 && name.endsWith(".class") && name.substring(0, i).equals(packagePath) && !name.contains("$")) 
						classes.add(Class.forName(name.substring(0, name.length() - 6).replace("/", ".")));

					entry = stream.getNextJarEntry();
				}
				stream.close();
			}
			return classes;
		}

		/**
		 * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
		 *
		 * @param packageName The base package
		 * @return The classes
		 * @throws ClassNotFoundException
		 * @throws IOException
		 */
		private Vector<Class> getClasses(String packageName)
				throws ClassNotFoundException, IOException {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			assert classLoader != null;
			String path = packageName.replace('.', '/');
			Enumeration<URL> resources = classLoader.getResources(path);
			List<File> dirs = new ArrayList<File>();
			while (resources.hasMoreElements()) {
				URL resource = resources.nextElement();
				try {
					File f = Paths.get(resource.toURI()).toFile();
					dirs.add(f);
				}
				catch (Exception e) {}
			}
			Vector<Class> classes = new Vector<Class>();
			for (File directory : dirs) {
				classes.addAll(findClasses(directory, packageName));
			}
			return classes;
		}

		/**
		 * Recursive method used to find all classes in a given directory and subdirs.
		 *
		 * @param directory   The base directory
		 * @param packageName The package name for classes found inside the base directory
		 * @return The classes
		 * @throws ClassNotFoundException
		 */
		private List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
			List<Class> classes = new ArrayList<Class>();

			if (!directory.exists()) {
				return classes;
			}
			File[] files = directory.listFiles();
			for (File file : files) {
				if (file.isDirectory()) {
					assert !file.getName().contains(".");
					classes.addAll(findClasses(file, packageName + "." + file.getName()));
				} else if (file.getName().endsWith(".class") && !file.getName().contains("$")) {
					// exclude controller anonymous inner classes
					//System.out.println("file = " + file);
					classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
				}
			}
			return classes;
		}
	}
}