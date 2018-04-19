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
public class SaveMenu extends JPanel {

	private int sizex = 200;
	private int sizey = 200;
	private JComboBox<String> saves;
	private JTextField newSaveText = new JTextField();
	private JButton overwrite = new JButton("Overwrite");
	private JButton newSave = new JButton("New Save");
	private JButton back = new JButton("Back");
	private List<Component> saveMenu = new ArrayList<Component>();

	public SaveMenu(Window w) {
		this.setLayout(null);

		saves = new JComboBox<String>(getSaves());
		saveMenu.add(saves);
		saveMenu.add(overwrite);
		saveMenu.add(newSaveText);
		saveMenu.add(newSave);
		saveMenu.add(back);

		for (Component c : saveMenu) {
			c.setBounds(50, saveMenu.indexOf(c) * 30 + 20, 100, 20);
			this.add(c);
		}
		overwrite.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String save = (String) saves.getSelectedItem();
				w.saveGame(save);
				w.back();
			}
		});
		newSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String save = newSaveText.getText();
				FileManager.setSaveDir(save);
				File file = new File(FileManager.getSaveDir() + "/save.txt");
				if (file.exists()) {
				} else if (!newSaveText.getText().isEmpty()) {
					FileManager.setSaveDir(save);
					w.saveGame(save);
					w.back();
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

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(sizex, sizey);
	}
}
