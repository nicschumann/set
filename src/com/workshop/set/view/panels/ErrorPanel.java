package com.workshop.set.view.panels;

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
		moveLoc = w - 210;
		this.setSize(0, 23);
		this.setLocation(w, 10);
		this.setBackground(new Color(0, 0, 0, 100));
		this.setBorder(new Color(255, 0, 0, 255));
		this.setResizeType(ResizeType.FIT_RIGHT);
		this.setVisible(false);

		_label = new ErrorLabel("Shit happened, son");
		_label.setSize(200, 23);
		_label.setLocation(0, 0);
		_label.setBackground(new Color(0, 0, 0, 0));
		this.add(_label);
	}
	
	public void displayError(String text) {
//		ERROR_FONT.get
		setVisible(true);
		_moving = true;
	}
	
	public void closeError() {
		_moving = true;
	}
	
	@Override
	public void animate(long millisSincePrev) {
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

}
