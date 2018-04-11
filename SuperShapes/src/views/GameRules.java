package views;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JSlider;

@SuppressWarnings("serial")
public class GameRules extends JPanel {

	private int sizex = 600;
	private int sizey = 512;
	private String game;
	private JButton next = new JButton("Next");
	private JButton back = new JButton("Back");

	private JSlider playerLives;
	private JCheckBox infiniteLives;

	private JCheckBox fly;
	private JSlider flyDuration;
	private JCheckBox flyInfinite;
	private JSlider flyCooldown;
	private JCheckBox flyRoomCooldown;

	private JCheckBox shield;
	private JSlider shieldCooldown;
	private JCheckBox shieldRoomCooldown;

	private JCheckBox rewind;
	private JSlider rewindLength;
	private JSlider rewindCooldown;
	private JCheckBox rewindRoomCooldown;

	private JCheckBox pause;
	private JSlider pauseLength;
	private JCheckBox pauseInfinite;
	private JSlider pauseCooldown;
	private JCheckBox pauseRoomCooldown;

	GameRules(Window w) {
		this.setLayout(null);

		fly = new JCheckBox("Enable Flight");
		fly.setBounds(10, 10, 100, 20);
		this.add(fly);

		flyDuration = new JSlider(1, 100, 3);
		flyDuration.setBounds(120, 10, 100, 20);
		this.add(flyDuration);

		flyInfinite = new JCheckBox("Infinite Flight");
		flyInfinite.setBounds(230, 10, 100, 20);
		this.add(flyInfinite);

		flyCooldown = new JSlider(1, 100, 3);
		flyCooldown.setBounds(340, 10, 100, 20);
		this.add(flyCooldown);

		flyRoomCooldown = new JCheckBox("Cooldown on new room");
		flyRoomCooldown.setBounds(450, 10, 200, 20);
		this.add(flyRoomCooldown);

		shield = new JCheckBox("Enable Shields");
		shield.setBounds(10, 90, 150, 20);
		shield.setOpaque(false);
		this.add(shield);

		shieldCooldown = new JSlider(1, 100, 3);
		shieldCooldown.setBounds(120, 90, 100, 20);
		this.add(shieldCooldown);

		shieldRoomCooldown = new JCheckBox("Cooldown on new room");
		shieldRoomCooldown.setBounds(230, 90, 200, 20);
		this.add(shieldRoomCooldown);

		rewind = new JCheckBox("Enable Rewind");
		rewind.setBounds(10, 50, 150, 20);
		rewind.setOpaque(false);
		this.add(rewind);

		rewindLength = new JSlider(1, 100, 3);
		rewindLength.setBounds(120, 50, 100, 20);
		this.add(rewindLength);

		rewindCooldown = new JSlider(1, 100, 3);
		rewindCooldown.setBounds(230, 50, 100, 20);
		rewind.setToolTipText("");
		this.add(rewindCooldown);

		rewindRoomCooldown = new JCheckBox("Cooldown on new room");
		rewindRoomCooldown.setBounds(340, 50, 200, 20);
		this.add(rewindRoomCooldown);

		pause = new JCheckBox("Enable Pause");
		pause.setBounds(10, 130, 110, 20);
		pause.setOpaque(false);
		this.add(pause);

		pauseLength = new JSlider(1, 100, 3);
		pauseLength.setBounds(120, 130, 100, 20);
		this.add(pauseLength);

		pauseInfinite = new JCheckBox("Infinite Pause");
		pauseInfinite.setBounds(230, 130, 120, 20);
		pauseInfinite.setOpaque(false);
		this.add(pauseInfinite);

		pauseCooldown = new JSlider(1, 100, 3);
		pauseCooldown.setBounds(340, 130, 100, 20);
		this.add(pauseCooldown);

		pauseRoomCooldown = new JCheckBox("Cooldown on new room");
		pauseRoomCooldown.setBounds(450, 130, 200, 20);
		this.add(pauseRoomCooldown);

		playerLives = new JSlider(1, 100, 3);
		playerLives.setBounds(10, 160, 100, 20);
		this.add(playerLives);

		infiniteLives = new JCheckBox("Infinite Lives");
		infiniteLives.setBounds(120, 160, 100, 20);
		this.add(infiniteLives);

		next = new JButton("Next");
		next.setBounds(530, 450, 100, 20);
		next.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				w.editor(game);
				writeGameRules();
			}
		});
		this.add(next);

		back = new JButton("Back");
		back.setBounds(420, 450, 100, 20);
		back.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				w.createGame();
			}
		});
		this.add(back);
	}

	private void writeGameRules() {
		List<String> lines = new ArrayList<String>();
		
		lines.add("lives " + playerLives.getValue());
		if (infiniteLives.isSelected())
			lines.add("infinite lives");
		if (fly.isSelected()) {
			lines.add("flight");
			lines.add("flight duration " + flyDuration.getValue());
			if(flyInfinite.isSelected()){
				lines.add("infinite flight");
			}
			lines.add("flight cooldown " + flyCooldown.getValue());
			if(flyRoomCooldown.isSelected())
				lines.add("flight room cooldown");
		}	
		if(shield.isSelected()) {
			lines.add("shield");
			lines.add("shield cooldown " + shieldCooldown.getValue());
			if(shieldRoomCooldown.isSelected())
				lines.add("shield room cooldown");
		}
		if (rewind.isSelected()) {
			lines.add("rewind");
			lines.add("rewind length " + rewindLength.getValue());
			lines.add("rewind cooldown " + rewindCooldown.getValue());
			if(rewindRoomCooldown.isSelected())
				lines.add("rewind room cooldown");
		}	
		if (pause.isSelected()) {
			lines.add("pause");
			lines.add("pause length " + pauseLength.getValue());
			if(pauseInfinite.isSelected()){
				lines.add("infinite pause");
			}
			lines.add("pause cooldown " + pauseCooldown.getValue());
			if(pauseRoomCooldown.isSelected())
				lines.add("pause room cooldown");
		}	
		
		String fileName = "games/" + game + "/gamerules.txt";
		try {
			FileWriter fileWriter = new FileWriter(fileName);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			for (String l : lines) {
				bufferedWriter.write(l);
				bufferedWriter.newLine();
			}
			bufferedWriter.close();
		} catch (IOException ex) {
			System.out.println("file no work " + fileName);
		}

	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(sizex, sizey);
	}

	public void setGame(String dir) {
		this.game = dir;
	}
}
