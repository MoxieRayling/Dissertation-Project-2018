package views.menus;

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

import model.FileManager;
import views.Window;

@SuppressWarnings("serial")
public class NewGameMenu extends JPanel {

	private int sizex = 200;
	private int sizey = 200;
	private String game = "";
	private JComboBox<String> saves;
	private JButton loadSave = new JButton("Load Save");
	private JButton back = new JButton("Back");
	private JTextField newSaveText = new JTextField();
	private JButton newSave = new JButton("New Save");
	private List<Component> loadMenu = new ArrayList<Component>();

	public NewGameMenu(Window w) {
		this.setLayout(null);
		saves = new JComboBox<String>(getSaves());
		loadMenu.add(saves);
		loadMenu.add(loadSave);
		loadMenu.add(newSaveText);
		loadMenu.add(newSave);
		loadMenu.add(back);
		for (Component c : loadMenu) {
			c.setBounds(50, loadMenu.indexOf(c) * 30 + 20, 100, 20);
			this.add(c);
		}
		loadSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				FileManager.setSaveDir((String) saves.getSelectedItem());
				w.loadGame();
			}
		});
		newSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String save = newSaveText.getText();
				File file = new File(FileManager.getGameDir() + "/saves/" + save);
				if (file.exists()) {
				} else if (!newSaveText.getText().isEmpty()) {
					FileManager.setSaveDir(save);
					w.makeNewSave();
					updateSaves();
					w.loadGame();
				}
			}
		});	
		back.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				w.back();
			}
		});
	}
	
	private String[] getSaves() {
		File file = new File(FileManager.getGameDir() + "/saves");
		String[] directories = file.list(new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				return new File(current, name).isDirectory();
			}
		});

		return directories;
	}

	public void updateSaves() {
		saves.removeAllItems();
		String[] saveNames = getSaves();
		for (int i = 0; i < saveNames.length; i++) {
			saves.addItem(saveNames[i]);
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(sizex, sizey);
	}
}
