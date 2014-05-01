package com.workshop.set.view;

import glfrontend.components.GLLabel;
import glfrontend.components.GLPanel;

import java.awt.Color;

import org.lwjgl.util.vector.Vector2f;

public class OptionPanel extends GLPanel {

	private int menuHeight = 300;

	private int _panelSpeed;
	private int _menuSpeed;
	private boolean _moving;
	private boolean _rollout;
	private GLLabel _label;
	private GLPanel _menu;

	public OptionPanel(float w, float h) {
		super();
		this.setLocation(-w, 0);
		this.setSize(w, h);
		this.setBackground(new Color(255, 255, 255, 0));
		this.setResizeType(ResizeType.FIT_LEFT);
		this.setVisible(false);
		_panelSpeed = 1000; // pixels/second
		_menuSpeed = 1000; // pixels/second
		_moving = false;
		_rollout = false;

		initLabel();
		initMenu();
	}

	private void initLabel() {
		_label = new GLLabel("");
		_label.setLocation(0, 0);
		_label.setSize(getSize());
		_label.setBackground(new Color(255, 255, 255, 0));
		_label.setForeground(Color.WHITE);
		this.add(_label);
	}

	private void initMenu() {
		_menu = new GLPanel();
		_menu.setSize(150f, 0f);
		_menu.setLocation(0f, 30f);
		_menu.setBackground(new Color(255, 255, 255, 100));
		_menu.setVisible(false);
		this.add(_menu);
	}

	public void setLabelText(String text) {
		_label.setText(text);
	}

	public boolean isMoving() {
		return _moving;
	}

	public void toggle() {
		if (_moving) {
			_panelSpeed = -_panelSpeed;
		} else {
			setVisible(true);
			_moving = true;
		}
	}

	private void showMenu() {
		if (!_menu.isVisible()) {
			_menu.setVisible(true);
			_rollout = true;
		}
	}

	@Override
	public void animate(long millisSincePrev) {
		if (_moving) {

			float seconds = millisSincePrev / 1000f;

			Vector2f size = getSize();
			Vector2f loc = getLocation();

			float x = loc.x + _panelSpeed * seconds;

			if (x <= -size.x) {
				setVisible(false);
				x = -size.x;
				_moving = false;
				_panelSpeed = -_panelSpeed;
				_menu.setSize(_menu.getSize().x, 0);
				_menu.setVisible(false);
			} else if (x >= 0) {
				x = 0;
				_moving = false;
				_panelSpeed = -_panelSpeed;
				showMenu();
			}
			setLocation(x, loc.y);
		}
		if (_rollout) {
			float seconds = millisSincePrev / 1000f;

			Vector2f size = _menu.getSize();

			float h = size.y + _menuSpeed * seconds;

			if (h >= menuHeight) {
				h = menuHeight;
				_rollout = false;
			}
			_menu.setSize(size.x, h);
		}
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
