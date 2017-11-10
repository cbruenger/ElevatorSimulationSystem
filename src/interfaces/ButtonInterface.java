package interfaces;

import errors.AlreadyExistsException;

public interface ButtonInterface {
	public void push();
	public void reset() throws AlreadyExistsException;
}
