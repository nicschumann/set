package com.workshop.set.view.menus;

import glfrontend.components.GLButton;
import glfrontend.components.GLPanel;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import com.workshop.set.model.geometry.VectorSpace.Geometry;
import com.workshop.set.model.interfaces.Model;

public class OptionPanel extends GLPanel {

	public static final Vector2f DEFAULT_SIZE = new Vector2f(200, 23);

	private int menuHeight = 0;
	private Model _model;

	private int _panelSpeed;
	private int _menuSpeed;
	private boolean _moving;
	private boolean _rollout;
	private List<GeomPanel> _geoms;
	private List<GLButton> _buttons;
	private GLPanel _buttonPanel;

	// private GLTextBox _focusBox;

	public OptionPanel(Model model) {
		super();
		this.setLocation(-DEFAULT_SIZE.x, 0);
		this.setSize(DEFAULT_SIZE);
		this.setBackground(new Color(0, 255, 0, 0));
		this.setResizeType(ResizeType.FIT_LEFT);
		this.setVisible(false);
		_panelSpeed = 1000; // pixels/second
		_menuSpeed = 1000; // pixels/second
		_moving = false;
		_rollout = false;
		
		_model = model;

		_geoms = new ArrayList<>();
		_buttons = new ArrayList<>();
		
		_buttonPanel = new GLPanel();
		_buttonPanel.setLocation(DEFAULT_SIZE);
		_buttonPanel.setSize(DEFAULT_SIZE.x, 0);
		_buttonPanel.setBackground(new Color(0, 0, 0, 30));
		_buttonPanel.setResizeType(ResizeType.FIT_LEFT);
		_buttonPanel.setVisible(false);
		
		this.add(_buttonPanel);

		// _focusBox = null;
	}
	
	@Override
	public boolean contains(Vector2f p) {
		for (GeomPanel gpane : _geoms) {
			if (gpane.contains(p))
				return true;
		}
		return super.contains(p) || _buttonPanel.contains(p);
	}

	public void updateGeomPanels() {
		for (GeomPanel panel : _geoms) {
			panel.update();
		}
	}

	public void addGeomPanel(Geometry geom) {
		GeomPanel panel = new GeomPanel(geom, this);
		panel.setLocation(0, DEFAULT_SIZE.y * _geoms.size());
		_geoms.add(panel);
		this.add(panel);

		float height = _geoms.size() * DEFAULT_SIZE.y;
		this.setSize(getSize().x, height);
		
//		System.out.println(height);
		_buttonPanel.setLocation(0, height);

		if (!isVisible())
			toggle();
		
	}

	public void removeGeomPanels(boolean toggle) {
		if (!toggle) {
			for (GeomPanel panel : _geoms) {
				this.remove(panel);
			}
			_geoms.clear();
			this.setSize(DEFAULT_SIZE);
		} else if (!isMoving() && isVisible()) {
			for (GeomPanel panel : _geoms) {
				panel.closeInfoPanel();
			}
			toggle();
		}
	}

	private void actuallyRemovePanels() {
		for (GeomPanel panel : _geoms) {
			this.remove(panel);
		}
		_geoms.clear();
		this.setSize(DEFAULT_SIZE);
		
		for (GLButton button : _buttons) {
			_buttonPanel.remove(button);
		}
		_buttons.clear();
		_buttonPanel.setSize(DEFAULT_SIZE.x, 0);
		_buttonPanel.setVisible(false);
	}
	
	public void setButtons() {
		addButton();
		addButton();
		addButton();
		addButton();
	}

	public void addButton() {
		GLButton button = new GLButton("Do Things");
		button.setSize(DEFAULT_SIZE);
		button.setLocation(0, _buttons.size() * DEFAULT_SIZE.y);
		button.setBackground(new Color(255, 128, 0, 0));
		_buttons.add(button);
//		this.add(button);

	}
	
	public void showButtons() {
		for (GLButton button : _buttons) {
			_buttonPanel.add(button);
		}
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
		if (!_buttonPanel.isVisible()) {
//			getOptions();
			setButtons();
			menuHeight = Math.round(_buttons.size() * DEFAULT_SIZE.y);
			_buttonPanel.setVisible(true);
			_rollout = true;
		}
	}

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
				actuallyRemovePanels();
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

			Vector2f size = _buttonPanel.getSize();

			float h = size.y + _menuSpeed * seconds;

			if (h >= menuHeight) {
				h = menuHeight;
				_rollout = false;
				showButtons();
			}
			_buttonPanel.setSize(size.x, h);
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
	
	@Override
	public void update() {
		_model.update();
	}

}
