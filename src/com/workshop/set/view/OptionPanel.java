package com.workshop.set.view;

import glfrontend.components.GLLabel;
import glfrontend.components.GLPanel;

import java.awt.Color;

import org.lwjgl.util.vector.Vector2f;

public class OptionPanel extends GLPanel {

	private int speed;
	private boolean moving;
	private GLLabel label;

	public OptionPanel(float w, float h) {
		super();
		this.setLocation(-w, 0);
		this.setSize(w, h);
		this.setBackground(new Color(255, 255, 255, 0));
		this.setResizeType(ResizeType.FIT_LEFT);
		this.setVisible(false);
		speed = 1000; // pixels/second
		moving = false;

		initLabel();
	}

	private void initLabel() {
		label = new GLLabel("");
		label.setLocation(0, 0);
		label.setSize(getSize());
		label.setBackground(new Color(255, 255, 255, 0));
		label.setForeground(Color.WHITE);
		this.add(label);
	}

	public void setLabelText(String text) {
		label.setText(text);
	}
	
	public boolean isMoving() {
		return moving;
	}

	public void toggle() {
		if (moving) {
			speed = -speed;
		} else {
			setVisible(true);
			moving = true;
		}
	}

	@Override
	public void animate(long millisSincePrev) {
		if (!moving)
			return;

		float seconds = millisSincePrev / 1000f;

		Vector2f size = getSize();
		Vector2f loc = getLocation();

		float x = loc.x + speed * seconds;

		if (x <= -size.x) {
			setVisible(false);
			x = -size.x;
			moving = false;
			speed = -speed;
		} else if (x >= 0) {
			x = 0;
			moving = false;
			speed = -speed;
		}
		setLocation(x, loc.y);
	}

}
