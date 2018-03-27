package model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import observers.Observer;

public class EditorModel extends Model {

	List<Room> rooms = new ArrayList<Room>();

	public EditorModel(Observer v) {
		super(v);
		super.player = new Player("0,0", 5, 10, 5);
		super.player.addObserver(v);
		super.room = loadRoom("0,0");
		super.room.addObserver(v);
		super.room.notifyObserver();
	}

	public void addRoom() {
		int index = searchRooms(room.getX(), room.getY());
		if (index == -1)
			rooms.add(room);
		else
			rooms.set(index, room);
	}

	private int searchRooms(int x, int y) {
		for (Room r : rooms) {
			if (r.getX() == x && r.getY() == y) {
				return rooms.indexOf(r);
			}
		}
		return -1;
	}

	private int searchRooms(String id) {
		for (Room r : rooms) {
			System.out.println(id + "!=" + r.getId());
			if (r.getId() == id) {
				return rooms.indexOf(r);
			}
		}
		return -1;
	}

	public String[] getRoomIds() {
		int i = rooms.size();
		String[] roomIds = new String[i];
		for (Room r : rooms) {
			roomIds[rooms.indexOf(r)] = r.getId();
		}
		return roomIds;
	}

	public void exportRooms() {
		String fileName = "resource/world.txt";
		String line = null;
		List<String> lines = new ArrayList<String>();
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			while ((line = bufferedReader.readLine()) != null) {
				lines.add(line);
			}
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
		} catch (IOException ex) {
		}

		Boolean inRoom = false;
		for (String l : lines) {
			if (l.startsWith("r")) {
				String id = l.split(" ")[1];
				int i = searchRooms(id);
				if (i != -1) {
					inRoom = true;
					System.out.println("yes");
				}
			}
			if (inRoom) {
				System.out.println("yes");
				lines.set(lines.indexOf(l), "xxxxxx");
			}
			if (l.startsWith(";")) {
				inRoom = false;
			}
		}
		while (lines.contains("xxxxxx")) {
			lines.remove(lines.indexOf("xxxxxx"));
		}
		for (Room r : rooms) {
			for (String s : r.exportRoom()) {
				lines.add(s);
			}
		}
		writeToFile(lines, fileName);
	}

}
