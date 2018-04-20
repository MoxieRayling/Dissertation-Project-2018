package views.menus;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.FileManager;
import views.Window;

@SuppressWarnings("serial")
public class LoadMenu extends JPanel {

	private int sizex = 400;
	private int sizey = 400;
	private int buttonSizex = 150;
	private int buttonSizey = 40;
	private JLabel title;
	private String game = "";
	private JComboBox<String> saves;
	private MenuButton loadSave = new MenuButton("Load Save");
	private MenuButton back = new MenuButton("Back");
	private List<JComponent> loadMenu = new ArrayList<JComponent>();

	public LoadMenu(Window w) {
		this.setLayout(null);
		this.setBackground(new Color(0x990000));
		
		title = new JLabel("<html><div style='text-align: center;'>Load Game");
		title.setFont(new Font("Arial", Font.BOLD, 40));
		title.setForeground(Color.BLACK);
		title.setBounds(sizex / 2 - 100, 30, 400,
					100);
		title.setBackground(new Color(0x660000));
		this.add(title);
		saves = new JComboBox<String>(getSaves());
		for (int i = 0; i < saves.getComponentCount(); i++) {
			if (saves.getComponent(i) instanceof JComponent) {
				((JComponent) saves.getComponent(i)).setBorder(null);
			}
			if (saves.getComponent(i) instanceof AbstractButton) {
				((AbstractButton) saves.getComponent(i)).setBorderPainted(false);
			}
		}
		loadMenu.add(saves);
		loadMenu.add(loadSave);
		loadMenu.add(back);
		for (JComponent b : loadMenu) {
			b.setBounds(sizex / 2 - buttonSizex / 2, loadMenu.indexOf(b) * buttonSizey + 150, buttonSizex, buttonSizey);
			b.setFont(new Font("Arial", Font.PLAIN, 20));
			b.setBackground(new Color(0x660000));
			b.setForeground(new Color(0x000000).brighter());
			b.setBorder(null);
			if (b instanceof MenuButton) {
				((MenuButton) b).setHoverBackgroundColor(new Color(0x990000).brighter());
				((MenuButton) b).setPressedBackgroundColor(new Color(0xff2222));
			}

			this.add(b);
		}
		loadSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				FileManager.setSaveDir((String) saves.getSelectedItem());
				w.loadGame();
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
