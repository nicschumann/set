package glfrontend.components;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

import org.lwjgl.input.Keyboard;

public class GLLimitedTextBox extends GLTextBox {

	private Set<Character> _allowedChars;
	private int _maxSize;

	public GLLimitedTextBox(Set<Character> chars, int capacity) {
		super();
		setBackground(new Color(255, 255, 255, 0));
		_allowedChars = new HashSet<>();
		_maxSize = capacity;
		for (char c : chars)
			_allowedChars.add(Character.toLowerCase(c));
	}

	@Override
	public void keyPressed(KeyEvent e) {
		char c = Character.toLowerCase(e.keyChar);
		int code = e.keyCode;
		if (code == Keyboard.KEY_LEFT || code == Keyboard.KEY_RIGHT || code == Keyboard.KEY_BACK
				|| code == Keyboard.KEY_LSHIFT || code == Keyboard.KEY_RSHIFT)
			super.keyPressed(e);

		if (_allowedChars.contains(c) && getText().length() < _maxSize || isShiftDown())
			super.keyPressed(e);
	}

}
