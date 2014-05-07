package com.workshop.set.view.panels;

import glfrontend.Triggerable;
import glfrontend.components.GLButton;
import glfrontend.components.GLPanel;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.util.vector.Vector2f;

import com.workshop.set.control.TempEnvironment.RelationException;
import com.workshop.set.model.geometry.VectorSpace.GeometricFailure;
import com.workshop.set.model.geometry.VectorSpace.Geometry;
import com.workshop.set.model.interfaces.Model;
import com.workshop.set.model.interfaces.Model.Function;
import com.workshop.set.view.SetScreen;

public class OptionPanel extends GLPanel {

	public static final Vector2f DEFAULT_SIZE = new Vector2f(200, 23);
	public static final float BUF = 10f;

	private SetScreen _set;

	private int menuHeight = 0;
	private Model _model;

	private int _panelSpeed;
	private int _menuSpeed;
	private boolean _moving;
	private boolean _rollout;
	private List<GeomPanel> _geoms;
	private List<GLButton> _buttons;
	private Map<GLButton, Function> _functions;
	private GLPanel _buttonPanel;

	// private GLTextBox _focusBox;

	public OptionPanel(Model model, SetScreen set) {
		super();
		this.setLocation(-DEFAULT_SIZE.x, BUF);
		this.setSize(DEFAULT_SIZE);
		this.setBackground(new Color(0, 255, 0, 0));
		this.setResizeType(ResizeType.FIT_LEFT);
		this.setBorder(new Color(255, 128, 0));
		this.setBorderWidth(2f);
		this.setVisible(false);
		_panelSpeed = 1000; // pixels/second
		_menuSpeed = 1000; // pixels/second
		_moving = false;
		_rollout = false;

		_set = set;
		_model = model;

		_geoms = new ArrayList<>();
		_buttons = new ArrayList<>();
		_functions = new HashMap<>();

		_buttonPanel = new GLPanel();
		_buttonPanel.setLocation(0, DEFAULT_SIZE.y);
		_buttonPanel.setSize(DEFAULT_SIZE.x, 0);
		_buttonPanel.setBackground(new Color(0, 0, 0, 30));
		_buttonPanel.setResizeType(ResizeType.FIT_LEFT);
		_buttonPanel.setBorder(new Color(255, 128, 0));
		_buttonPanel.setBorderWidth(2f);
		_buttonPanel.setVisible(false);

		this.add(_buttonPanel);

		// _focusBox = null;
	}

	@Override
	public boolean contains(Vector2f p) {
		Vector2f p2 = new Vector2f(BUF, BUF);
		Vector2f.sub(p, p2, p2);

		for (GeomPanel gpane : _geoms) {
			if (gpane.contains(p2))
				return true;
		}
		return super.contains(p) || _buttonPanel.contains(p2);
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

		// System.out.println(height);
		_buttonPanel.setLocation(0, height);

		removeButtons();
		if (!isVisible()) {
			toggle();
		} else if (!_moving) {
			showMenu();
		}

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

		removeButtons();
	}

	private void removeButtons() {
		for (GLButton button : _buttons) {
			_buttonPanel.remove(button);
		}
		_buttons.clear();
		_functions.clear();
		_buttonPanel.setSize(DEFAULT_SIZE.x, 0);
		_buttonPanel.setVisible(false);
	}

	public void setButtons(List<Function> fxs) {
		for (Function fx : fxs) {
			addButton(fx);
		}
	}

	public void addButton(final Function fx) {
		GLButton button = new GLButton(fx.buttonText);
		button.setSize(DEFAULT_SIZE);
		button.setLocation(0, _buttons.size() * DEFAULT_SIZE.y);
		button.setBackground(new Color(255, 128, 0, 0));
		button.addTriggerable(new Triggerable() {

			@Override
			public void trigger(TriggerEvent e) {
				if (fx.isConstraint) {
					_model.createConstraint(fx);
				} else {
					try {
						_model.executeFunction(fx);
					} catch (GeometricFailure | RelationException e1) {
						_set.displayError(e1.getMessage());
					}
				}
				_model.update();
				updateGeomPanels();
			}

		});
		_buttons.add(button);
		_functions.put(button, fx);
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
		List<Function> functions = _model.getFunctions();
		if (functions != null) {
			setButtons(_model.getFunctions());
			menuHeight = Math.round(_buttons.size() * DEFAULT_SIZE.y);
			_buttonPanel.setVisible(true);
			_rollout = true;
		} else {
			_set.displayError("Relation already exists!");
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
			} else if (x >= BUF) {
				x = BUF;
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
		update();
	}

	@Override
	public void resize(Vector2f newSize) {
		if (isVisible()) {
			setLocation(BUF, BUF);
		} else {
			Vector2f size = getSize();
			setLocation(-size.x, BUF);
		}
	}

	@Override
	public void update() {
		_model.update();
	}

	public void removeGeomPanel(Geometry selected) {
		GeomPanel toRemove = null;
		for (GeomPanel geom : _geoms) {
			if (geom.getGeometry().equals(selected)) {
				toRemove = geom;
				break;
			}
		}
		if (toRemove != null) {
			if (_geoms.size() > 1) {
				this.remove(toRemove);
				_geoms.remove(toRemove);

				float height = _geoms.size() * DEFAULT_SIZE.y;
				this.setSize(getSize().x, height);
				removeButtons();
				showMenu();
			} else {
				removeGeomPanels(true);
			}
		}
	}

	public void checkHotKey(KeyEvent e) {
		for (GLButton button : _buttons) {
			if (_functions.get(button).key == e.keyCode) {
				button.triggerButton();
			}
		}
	}

}
