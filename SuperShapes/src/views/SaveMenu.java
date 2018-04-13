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

@SuppressWarnings("serial")
public class SaveMenu extends JPanel {

	private int sizex = 200;
	private int sizey = 200;
	private String game = "";
	private JComboBox<String> saves;
	private JTextField newSaveText;
	private JButton overwrite = new JButton("Overwrite");
	private JButton newSave = new JButton("New Save");
	private JButton back = new JButton("Back");
	private List<Component> saveMenu = new ArrayList<Component>();

	SaveMenu(Window w) {
		this.setLayout(null);

		saves = new JComboBox<String>(getSaves());
		saveMenu.add(saves);
		saveMenu.add(newSaveText);
		saveMenu.add(overwrite);
		saveMenu.add(newSave);
		saveMenu.add(back);

		for (Component c : saveMenu) {
			c.setBounds(50, saveMenu.indexOf(c) * 30 + 20, 100, 20);
			c.setVisible(false);
			this.add(c);
		}
		overwrite.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String save = (String) saves.getSelectedItem();
				w.saveGame(save);
			}
		});
		newSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String save = newSaveText.getText();
				File file = new File("games/" + game + "/saves/" + save + ".txt");
				if (file.exists()) {
				} else {
					w.saveGame(save);
				}
			}
		});
	}

	private String[] getSaves() {
		File file = new File("games/" + game + "/saves");
		String[] directories = file.list(new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				return new File(current, name).isFile();
			}
		});

		return directories;
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(sizex, sizey);
	}
}
