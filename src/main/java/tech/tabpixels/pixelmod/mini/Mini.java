package tech.tabpixels.pixelmod.mini;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.swing.SwingConstants;
import javax.swing.JTextField;

public class Mini {

	public static JFrame frmMiniInstaller;
	public static JPanel contentPane;
	private static JTextField textField;
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public static void create() {
		frmMiniInstaller = new JFrame();
		frmMiniInstaller.setTitle("Mini Installer");
		frmMiniInstaller.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmMiniInstaller.setBounds(100, 100, 450, 128);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		contentPane.setLayout(new BorderLayout(0, 0));
		frmMiniInstaller.setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		new  FileDrop( panel, new FileDrop.Listener() {
			public void  filesDropped( java.io.File[] files ) {
				for (File file : files)
				{
					try {
							Files.copy(file.toPath(), new File(textField.getText()+"/mods/"+file.getName()).toPath());
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(null, "Sorry! An error occured while copying the mod, '"+file.getName()+"'");
						e1.printStackTrace();
					}
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
		textField.setText(System.getenv("AppData")+"/.minecraft");
		frmMiniInstaller.setVisible(true);
	}
}
