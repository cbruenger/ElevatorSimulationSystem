package factories;

import enumerators.MyDirection;
import button.ButtonImpl;
import interfaces.ButtonInterface;

public class ButtonFactory {

	public static ButtonInterface build(MyDirection direction, int floorNum) {
		return new ButtonImpl(direction, floorNum);
	}
	
}
