package tests;

import controller.Controller;
import model.Model;
import views.Window;

public class GUITest 
{
	public static void main(String[] args) 
	{
		Window w = new Window();
		Model m = new Model(w);
		Controller c = new Controller(w,m);	
		w.SetCon(c);
	}
}
