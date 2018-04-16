package controller;

public class Constants {
	public static String gameDir = "games/game1";
	public static String saveDir = "save";
	public static Boolean flight = false;
	public static Boolean pause = false;
	public static Boolean rewind = false;
	public static Boolean shield = false;
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
	}

	public static void setSaveDir(String save) {
		Constants.saveDir = gameDir + "/saves/" + save;
	}
}
