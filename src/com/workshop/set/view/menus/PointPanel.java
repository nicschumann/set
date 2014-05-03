package com.workshop.set.view.menus;

import static com.workshop.set.view.menus.OptionPanel.DEFAULT_SIZE;
import glfrontend.Triggerable;
import glfrontend.components.GLLabel;
import glfrontend.components.GLLimitedTextBox;
import glfrontend.components.GLPanel;
import glfrontend.components.GLTextBox;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.lwjgl.util.vector.Vector2f;

import com.workshop.set.model.VectorSpace.GeometricFailure;
import com.workshop.set.model.VectorSpace.Point;

public class PointPanel extends GLPanel {

	public static final Vector2f SIZE = new Vector2f(DEFAULT_SIZE.x * 1.5f / 12f, DEFAULT_SIZE.y);
	public final Set<Character> allowedChars;

	private OptionPanel options;
	private Point _p;
	private List<GLLimitedTextBox> _tboxes;

	public PointPanel(Point p, OptionPanel options) {
		super();
		this.setSize(DEFAULT_SIZE.x * 1.5f, DEFAULT_SIZE.y);
		this.setLocation(DEFAULT_SIZE.x, 0);
		this.setBackground(new Color(0, 0, 0, 0));

		this._p = p;
		this.options = options;
		_tboxes = new ArrayList<>(3);

		allowedChars = new HashSet<>();
		allowedChars.addAll(Arrays
				.asList(new Character[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '.' }));

		try {
			initLabelAndPoint(0, 0, "X:", _p.getN_(1).get());
			initLabelAndPoint(DEFAULT_SIZE.x * 1.5f / 3f, 0, "Y:", _p.getN_(2).get());
			initLabelAndPoint(DEFAULT_SIZE.x * 1.5f * 2f / 3f, 0, "Z:", _p.getN_(3).get());
		} catch (GeometricFailure e) {
			e.printStackTrace();
		}

		setTriggers();
	}

	private void initLabelAndPoint(float x, float y, String labelText, double point) {
		GLLabel label = new GLLabel(labelText);
		label.setSize(SIZE);
		label.setLocation(x, y);
		label.setBackground(new Color(0, 0, 0, 0));
		label.setFocus(false);

		GLLimitedTextBox tbox = new GLLimitedTextBox(allowedChars, 25);
		tbox.setSize(SIZE.x * 3, SIZE.y - 6);
		tbox.setLocation(x + SIZE.x, y + 3);
		tbox.setBackground(new Color(0, 0, 0, 0));
		tbox.setText(String.format("%.2f", point));
		tbox.setFocus(false);

		this.add(label);
		this.add(tbox);
		_tboxes.add(tbox);
	}

	private void setTriggers() {

		int length = _tboxes.size();
		for (int i = 0; i < length; i++) {
			GLTextBox tbox = _tboxes.get(i);
			tbox.addTriggerable(new TriggerHandler(i + 1));
		}
	}

	@Override
	public void update() {
		try {
			_tboxes.get(0).setText(String.format("%.2f", _p.getN_(1).get()));
			_tboxes.get(1).setText(String.format("%.2f", _p.getN_(2).get()));
			_tboxes.get(2).setText(String.format("%.2f", _p.getN_(3).get()));
		} catch (GeometricFailure e) {
			e.printStackTrace();
		}
	}

	private class TriggerHandler implements Triggerable {

		private final int _index;

		public TriggerHandler(int index) {
			this._index = index;
		}

		@Override
		public void trigger(TriggerEvent e) {
			GLTextBox tb = (GLTextBox) e.getSource();
			try {
				// TODO: Error checking for boundaries?
				double newPoint = Double.parseDouble(tb.getText());
				_p.getN_(_index).set(newPoint);
				setFocus(false);
				options.updateGeomPanels();
			} catch (NumberFormatException | GeometricFailure nfe) {}
		}

	}

}
