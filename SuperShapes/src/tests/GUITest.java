package tests;

import controller.Constants;
import controller.Controller;
import model.Model;
import views.Window;

public class GUITest implements Constants 
{
	public static void main(String[] args) 
	{
		Window w = new Window();
		Model m = new Model(w);
		Controller c = new Controller(w,m);	
		w.SetCon(c);
		// this is a new line
	}
}
