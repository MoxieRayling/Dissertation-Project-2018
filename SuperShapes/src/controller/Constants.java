package controller;

public class Constants 
{
	public static String gameDir = "games/game1";
	public static String saveFile = "save";

	public static void setGameDir(String gameFile) {
		Constants.gameDir = "games/" + gameFile;
	}

	public static void setSaveFile(String saveFile) {
		Constants.saveFile = saveFile;
	}
}
