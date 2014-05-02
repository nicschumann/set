package com.workshop.set.view.menus;

import static com.workshop.set.view.menus.OptionPanel.DEFAULT_SIZE;
import glfrontend.Triggerable;
import glfrontend.components.GLLabel;
import glfrontend.components.GLPanel;
import glfrontend.components.GLTextBox;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import com.workshop.set.SetMain;
import com.workshop.set.model.VectorSpace.Relation;

public class RelationPanel extends GLPanel {

	public static final Vector2f SIZE = new Vector2f(DEFAULT_SIZE.x / 12, DEFAULT_SIZE.y / 2);

	private OptionPanel options;
	private Relation _r;
	private List<GLTextBox> _tboxes;

	public RelationPanel(Relation r, OptionPanel options) {
		super();
		this.setSize(DEFAULT_SIZE.x, DEFAULT_SIZE.y);
		this.setLocation(0, 0);
		this.setBackground(new Color(0, 0, 0, 0));

		this._r = r;
		this.options = options;
		_tboxes = new ArrayList<>(2);

		initLabelAndPoint(0, DEFAULT_SIZE.y / 2f, "A:", _r.domain().name().toString());
		initLabelAndPoint(DEFAULT_SIZE.x / 2f, DEFAULT_SIZE.y / 2f, "B:", _r.codomain().name().toString());

		setTriggers();
	}

	private void initLabelAndPoint(float x, float y, String labelText, String geomName) {
		GLLabel label = new GLLabel(labelText);
		label.setSize(SIZE);
		label.setLocation(x, y);
		label.setBackground(new Color(0, 0, 0, 0));
		label.setFocus(false);

		GLTextBox tbox = new GLTextBox();
		tbox.setSize(SIZE.x * 5, SIZE.y - 6);
		tbox.setLocation(x + SIZE.x, y + 3);
		tbox.setBackground(new Color(0, 0, 0, 0));
		tbox.setText(geomName);
		tbox.setFocus(false);

		this.add(label);
		this.add(tbox);
		_tboxes.add(tbox);
	}

	private void setTriggers() {
		_tboxes.get(0).addTriggerable(new TriggerHandler(true));
		_tboxes.get(1).addTriggerable(new TriggerHandler(false));
	}
	
	@Override
	public void update() {
		_tboxes.get(0).setText(_r.domain().name().toString());
		_tboxes.get(1).setText(_r.codomain().name().toString());
	}

	private class TriggerHandler implements Triggerable {

		private final boolean _domain;

		public TriggerHandler(boolean domain) {
			this._domain = domain;
		}

		@Override
		public void trigger(TriggerEvent e) {
			String newName = ((GLTextBox) e.getSource()).getText();
			// TODO: Error checking for similar names?
			if (_domain) {
				_r.domain().setName(SetMain.GENSYM.generate(newName));
			} else {
				_r.codomain().setName(SetMain.GENSYM.generate(newName));
			}
			setFocus(false);
			options.updateGeomPanels();
		}

	}

}
