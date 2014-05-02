package com.workshop.set.view.menus;

import glfrontend.components.GLPanel;

import java.awt.Button;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import com.workshop.set.model.VectorSpace.Geometry;

public class OptionPanel extends GLPanel {

	public static final Vector2f DEFAULT_SIZE = new Vector2f(300, 60);

	private int menuHeight = 300;

	private int _panelSpeed;
	private int _menuSpeed;
	private boolean _moving;
	private boolean _rollout;
	private List<GeomPanel> _geoms;
	private List<Button> _buttons;

	public OptionPanel() {
		super();
		this.setLocation(-DEFAULT_SIZE.x, 0);
		this.setSize(DEFAULT_SIZE);
		this.setBackground(new Color(255, 255, 255, 100));
		this.setResizeType(ResizeType.FIT_LEFT);
		this.setVisible(false);
		_panelSpeed = 1000; // pixels/second
		_menuSpeed = 1000; // pixels/second
		_moving = false;
		_rollout = false;

		_geoms = new ArrayList<>();

	}

	public void addGeomPanel(Geometry geom) {
		GeomPanel panel = new GeomPanel(geom);
		panel.setLocation(0, DEFAULT_SIZE.y * _geoms.size());
		_geoms.add(panel);
		this.add(panel);

		if (!isVisible())
			toggle();
	}

	public void removeGeomPanels() {
		_geoms.clear();
		if (!isMoving() && isVisible())
			toggle();
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

	// private void showMenu() {
	// if (!_menu.isVisible()) {
	// _menu.setVisible(true);
	// _rollout = true;
	// }
	// }

	@Override
	public void animate(long millisSincePrev) {
		if (_moving) {

			float seconds = millisSincePrev / 1000f;

			Vector2f loc = getLocation();

			float x = loc.x + _panelSpeed * seconds;

			if (x <= -DEFAULT_SIZE.x) {
				setVisible(false);
				x = -DEFAULT_SIZE.x;
				_moving = false;
				_panelSpeed = -_panelSpeed;
				// _menu.setSize(_menu.getSize().x, 0);
				// _menu.setVisible(false);
			} else if (x >= 0) {
				x = 0;
				_moving = false;
				_panelSpeed = -_panelSpeed;
				// showMenu();
			}
			setLocation(x, loc.y);
		}
		if (_rollout) {
			// float seconds = millisSincePrev / 1000f;

			// Vector2f size = _menu.getSize();
			//
			// float h = size.y + _menuSpeed * seconds;
			//
			// if (h >= menuHeight) {
			// h = menuHeight;
			// _rollout = false;
			// }
			// _menu.setSize(size.x, h);
		}

		for (GeomPanel panel : _geoms) {
			panel.animate(millisSincePrev);
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
