package controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class Constants {
	public static String gameDir = "games/game1";
	public static String saveDir = "save";
	private static List<String> textureNames = new ArrayList<String>();
	private static List<BufferedImage> textures = new ArrayList<BufferedImage>();
	public static Boolean flight = true;
	public static Boolean pause = true;
	public static Boolean rewind = true;
	public static Boolean shield = true;
	public static int flightDuration = 3;
	public static Boolean infFlight = false;
	public static Boolean flightRoomCooldown = false;
	public static int flightCooldown = 10;
	public static int pauseDuration = 3;
	public static Boolean infPause = false;
	public static Boolean PauseRoomCooldown = false;
	public static int pauseCooldown = 10;
	public static int shieldCooldown = 10;
	public static Boolean infShield = false;
	public static Boolean shieldRoomCooldown = false;
	public static int rewindLength = 3;
	public static Boolean rewindRoomCooldown = false;
	public static int rewindCooldown = 10;
	public static String[] tiles = { "empty", "wall", "slide", "hole", "tele" };

	public static void setGameDir(String gameFile) {
		Constants.gameDir = "games/" + gameFile;
		setImages();
	}

	public static void setSaveDir(String save) {
		Constants.saveDir = gameDir + "/saves/" + save;
	}

	public static void setImages() {
		File file = new File(Constants.gameDir + "/textures");
		String[] files = file.list(new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				return new File(current, name).isFile();
			}
		});
		textureNames.clear();
		textures.clear();
		for (int i = 0; i < files.length; i++) {
			textureNames.add(files[i]);
			File imgSrc = new File(Constants.gameDir + "/textures/" + files[i]);
			try {
				textures.add(ImageIO.read(imgSrc));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static BufferedImage getImage(String file) {
		for(String s : textureNames) {
			if(s.equals(file)) {
				return textures.get(textureNames.indexOf(s));
			}
		}
		return null;
	}
}
