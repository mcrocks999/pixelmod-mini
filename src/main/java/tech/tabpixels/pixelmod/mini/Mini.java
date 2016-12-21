package tech.tabpixels.pixelmod.mini;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JFileChooser;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class Mini {

	public static JFrame frame;
	public static JPanel contentPane;
	private static JTextField textField;
	public static JComboBox comboBox = new JComboBox();
    public static JComboBox comboBox_1 = new JComboBox();
	public static String workingDirectory = "";
	public static Boolean isSelected = false;
	public static Boolean isValid = false;
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public static void create() {
		frame = new JFrame();
		frame.setTitle("Mini Installer");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(100, 100, 450, 145);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		contentPane.setLayout(new BorderLayout(0, 0));
		frame.setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		new  FileDrop( panel, new FileDrop.Listener() {
			public void  filesDropped( java.io.File[] files ) {
				if (isSelected) {
					for (File file : files)
					{
						try {
								Files.copy(file.toPath(), new File(textField.getText()+"/"+file.getName()).toPath());
						} catch (IOException e1) {
							JOptionPane.showMessageDialog(null, "Sorry! An error occured while copying the mod, '"+file.getName()+"'");
							e1.printStackTrace();
						}
					}
				} else {
					JOptionPane.showMessageDialog(null, "A installation / modpack / instance is not selected properly.");
				}
			}
	      });
		
		JLabel lblDragAndDrop = new JLabel("Drag and drop mods to automatically install");
		lblDragAndDrop.setHorizontalAlignment(SwingConstants.CENTER);
		lblDragAndDrop.setVerticalAlignment(SwingConstants.TOP);
		lblDragAndDrop.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		lblDragAndDrop.setBounds(10, 11, 414, 26);
		panel.add(lblDragAndDrop);
		
		JLabel lblInstallingTo = new JLabel("Installing to:");
		lblInstallingTo.setBounds(10, 54, 75, 14);
		panel.add(lblInstallingTo);
		
		textField = new JTextField();
		textField.setBounds(95, 51, 329, 20);
		panel.add(textField);
		textField.setColumns(10);
		
		String OS = (System.getProperty("os.name")).toUpperCase();
		if (OS.contains("WIN")) {
		    workingDirectory = System.getenv("AppData");
		} else {
		    workingDirectory = System.getProperty("user.home");
		    workingDirectory += "/Library/Application Support";
		}
        Settings.technicDirectory = workingDirectory+"/.technic/modpacks";
        Settings.minecraftDirectory = workingDirectory+"/.minecraft/mods";
		
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Default Minecraft", "Technic Modpack", "MultiMC"}));
		comboBox.setBounds(10, 79, 156, 20);
		panel.add(comboBox);
		
		comboBox_1.setModel(new DefaultComboBoxModel(new String[] {".minecraft"}));
		comboBox_1.setBounds(176, 79, 248, 20);
		panel.add(comboBox_1);
		
		comboBox.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		        changeMode(-1);
		    }
		});
		comboBox_1.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		        changeInstance();
		    }
		});
		
		frame.setVisible(true);
		String[] options = new String[] {"Minecraft", "Technic", "MultiMC"};
	    int response = JOptionPane.showOptionDialog(null, "Please select an installation", "PixelMod Mini",
	        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
	        null, options, options[0]);
	    
	    changeMode(response);
	}
    
	public static void changeMode(int mode)
    {
		if (mode>-1) {comboBox.setSelectedIndex(mode);}
		
		isSelected = false;
		isValid = false;
        if (comboBox.getSelectedIndex()==0) {
        	Settings.selection = 0;
        	loadMinecraft();
        } else if (comboBox.getSelectedIndex()==1) {
        	Settings.selection = 1;
        	loadTechnic();
        } else {
        	Settings.selection = 2;
        	textField.setText("NO CURRENT SUPPORT FOR MULTIMC");
        }
    }
	
	public static void changeInstance()
    {
		if (isValid) {
			if (Settings.selection == 1) {
				textField.setText(Settings.technicDirectory + "/"+comboBox_1.getItemAt(comboBox_1.getSelectedIndex())+"/mods");
				isSelected = true;
	        }
		} else if (Settings.selection != 0) {
			JOptionPane.showMessageDialog(null, "Installation is not valid.");
		}
    }
    
    public static void loadMinecraft()
    {
        comboBox_1.setModel(new DefaultComboBoxModel(new String[] {".minecraft"}));
    	if (Files.exists(Paths.get(Settings.minecraftDirectory))) {
		    comboBox_1.setModel(new DefaultComboBoxModel(new String[]{"Using valid Minecraft installation"}));
	        textField.setText(Settings.minecraftDirectory);
		    isSelected = true;
	    } else {
	    	JFileChooser fileChooser = new JFileChooser();
	    	fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    	if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
	    		File file = fileChooser.getSelectedFile();
	    		Settings.minecraftDirectory = file.getAbsolutePath()+"/mods";
	    	}
	    	changeMode(0);
	    }
    }
    
    public static void loadTechnic()
    {
        if (Files.exists(Paths.get(Settings.technicDirectory))) {
        	String[] directories = new File(Settings.technicDirectory).list(new FilenameFilter() {
	     	  public boolean accept(File current, String name) {
	        	return new File(current, name).isDirectory();
	          }
	        });
		    comboBox_1.setModel(new DefaultComboBoxModel(directories));
	    	textField.setText("Please choose a modpack");
	    	isValid = true;
	    } else {
	    	textField.setText("Technic installation not found");
	    	comboBox_1.setModel(new DefaultComboBoxModel(new String[]{"Please see error above!"}));
	    	int result = JOptionPane.showConfirmDialog(null, "Technic not found, select directory?", null, JOptionPane.YES_NO_OPTION);
	    	if (result == JOptionPane.YES_OPTION) {
		    	JFileChooser fileChooser = new JFileChooser();
		    	fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		    	if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
		    		File file = fileChooser.getSelectedFile();
		    		Settings.technicDirectory = file.getAbsolutePath()+"/modpacks";
			    	changeMode(1);
			    	return;
		    	}
	    	}
	    	textField.setText("Invalid Technic installation");
	    }
    }
}