package glfrontend.components;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

import static org.lwjgl.input.Keyboard.*;

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
		switch (e.keyCode) {
		case KEY_LEFT:
		case KEY_RIGHT:
		case KEY_BACK:
		case KEY_LCONTROL:
		case KEY_RCONTROL:
		case KEY_RETURN:
		case KEY_TAB:
		case KEY_LSHIFT:
		case KEY_RSHIFT:
			super.keyPressed(e);
			break;
		default:
			char c = Character.toLowerCase(e.keyChar);
			if (_allowedChars.contains(c) && getText().length() < _maxSize || isShiftDown())
				super.keyPressed(e);
			break;
		}

	}

}
