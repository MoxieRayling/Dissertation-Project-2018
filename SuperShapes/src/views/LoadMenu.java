package views;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controller.Constants;

@SuppressWarnings("serial")
public class LoadMenu extends JPanel {

	private int sizex = 200;
	private int sizey = 200;
	private String game = "";
	private JComboBox<String> saves;
	private JButton loadSave = new JButton("Load Save");
	private JButton back = new JButton("Back");
	private List<Component> loadMenu = new ArrayList<Component>();

	LoadMenu(Window w) {
		this.setLayout(null);
		saves = new JComboBox<String>(getSaves());
		loadMenu.add(saves);
		loadMenu.add(loadSave);
		loadMenu.add(back);
		for (Component c : loadMenu) {
			c.setBounds(50, loadMenu.indexOf(c) * 30 + 20, 100, 20);
			this.add(c);
		}
		loadSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Constants.setSaveFile((String) saves.getSelectedItem());
				w.loadGame();
			}
		});
	}

	private String[] getSaves() {
		File file = new File(Constants.gameDir + "/saves");
		String[] directories = file.list(new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				return new File(current, name).isFile();
			}
		});
		for(int i = 0; i<directories.length; i++) {
			directories[i] = directories[i].substring(0,directories[i].indexOf("."));
		}
		
		return directories;
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(sizex, sizey);
	}
}
