package views;
import controller.Controller;
import observers.Observer;

public interface View extends Observer
{
	void SetCon(Controller controller);
}
