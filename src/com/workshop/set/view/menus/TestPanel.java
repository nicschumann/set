package com.workshop.set.view.menus;

import glfrontend.components.GLPanel;
import glfrontend.components.GLTextBox;

import java.awt.Color;

import org.lwjgl.util.vector.Vector2f;

public class TestPanel extends GLPanel {

	private GLTextBox _tbox;

	public TestPanel(float w, float h) {
		super();
		this.setLocation(0, 0);
		this.setSize(w, h);
		this.setBackground(new Color(255, 255, 255, 100));
		this.setResizeType(ResizeType.FIT_LEFT);

		initTextBox();
	}

	private void initTextBox() {
		// Set<Character> chars = new HashSet<>();
		// chars.addAll(Arrays.asList(new Character[] { '0', '1', '2', '3', '4', '5', '6', '7', '8',
		// '9', '-', '.' }));
		// _tbox = new GLLimitedTextBox(chars, 25);
		_tbox = new GLTextBox();
		_tbox.setLocation(0, 0);
		_tbox.setSize(getSize());
		_tbox.setBackground(new Color(0, 0, 0, 0));
		this.add(_tbox);
	}

	public void setTextBoxText(String text) {
		_tbox.setText(text);
	}

	@Override
	public void animate(long millisSincePrev) {
		_tbox.animate(millisSincePrev);
	}

	@Override
	public void resize(Vector2f newSize) {
		if (isVisible()) {
			setLocation(0, 0);
		} else {
			Vector2f size = getSize();
			setLocation(-size.x, 0);
		}
	}

}
