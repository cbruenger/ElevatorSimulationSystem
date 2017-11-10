package button;

import interfaces.ButtonInterface;
import enumerators.*;
import errors.AlreadyExistsException;
import factories.ButtonFactory;

public class Button implements ButtonInterface {
	
	private ButtonInterface delegate;

	public Button(MyDirection direction, int floorNum) {
		this.setDelegate(direction, floorNum);
	}
	
	private void setDelegate(MyDirection direction, int floorNum) {
		this.delegate = ButtonFactory.build(direction, floorNum);
	}
	
	@Override
	public void push() {
		this.delegate.push();
	}
	
	@Override
	public void reset() throws AlreadyExistsException {
		this.delegate.reset();
	}

}
