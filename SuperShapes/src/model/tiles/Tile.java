package model.tiles;

public abstract class Tile {
	private Boolean trav = true; // traversable
	private int path; // path-finding value
	private int[] loc = new int[2];
	protected int x;
	protected int y;
	private Boolean pathed = false;
	private Boolean occupied = false;
	private String text = "";
	private Boolean textRead = false;
	protected String image = "";
	protected String event = "";

	public Tile(int x, int y) {
		this.x = x;
		this.y = y;
		loc[0] = x;
		loc[1] = y;
		this.trav = true;
		textRead = true;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Boolean getTextRead() {
		return textRead;
	}

	public void setTextRead(Boolean textRead) {
		this.textRead = textRead;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
		if (text.equals(""))
			setTextRead(true);
		else
			setTextRead(false);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int[] GetLoc() {
		return loc;
	}

	public Boolean getOccupied() {
		return occupied;
	}

	public void setOccupied(Boolean occupied) {
		this.occupied = occupied;
	}

	public Boolean getPathed() {
		return pathed;
	}

	public void setPathed(Boolean pathed) {
		this.pathed = pathed;
	}

	public Boolean getTrav() {
		return trav;
	}

	public void setTrav(Boolean trav) {
		this.trav = trav;
	}

	public int getPath() {
		return path;
	}

	public void setPath(int path) {
		this.path = path;
	}

	public abstract String toString();
}
