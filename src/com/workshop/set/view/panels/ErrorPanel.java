package com.workshop.set.view.panels;

import static glfrontend.GLFrontEnd.ERROR_FONT;
import glfrontend.components.ErrorLabel;
import glfrontend.components.GLPanel;

import java.awt.Color;

import org.lwjgl.util.vector.Vector2f;

public class ErrorPanel extends GLPanel {

	private ErrorLabel _label;
	private boolean _moving;
	private int _panelSpeed;

	private float moveLoc;

	public ErrorPanel(float w, float h) {
		moveLoc = w;
		this.setSize(20, 23);
		this.setLocation(w, 10);
		this.setBackground(new Color(0, 0, 0, 200));
		this.setBorder(new Color(255, 0, 0, 255));
		this.setResizeType(ResizeType.FIT_RIGHT);
		this.setVisible(false);

		_label = new ErrorLabel("");
		_label.setSize(20, 23);
		_label.setLocation(0, 0);
		_label.setBackground(new Color(0, 0, 0, 0));
		this.add(_label);

		_panelSpeed = -1000;
	}

	public void displayError(String text, float parentWidth) {
		float width = ERROR_FONT.getWidth(text) + 40f;
		float sizey = getSize().y;
		setSize(width, sizey);
		_label.setSize(width, sizey);
		_label.setText(text);

		moveLoc = parentWidth - width - 10;

		setVisible(true);
		_moving = true;
	}

	public void closeError() {
		_moving = true;
	}

	@Override
	public void animate(long millisSincePrev) {
		// System.out.println(getLocation());
		if (_moving) {

			float seconds = millisSincePrev / 1000f;

			Vector2f loc = getLocation();
			float xbuf = getSize().x + 10;

			float x = loc.x + _panelSpeed * seconds;

			if (x <= moveLoc) {
				x = moveLoc;
				_moving = false;
				_panelSpeed = -_panelSpeed;
			} else if (x >= moveLoc + xbuf) {
				x = moveLoc + xbuf;
				_moving = false;
				_panelSpeed = -_panelSpeed;
				setVisible(false);
			}
			setLocation(x, loc.y);
		}
	}

	@Override
	public void resize(Vector2f newSize) {}

}
