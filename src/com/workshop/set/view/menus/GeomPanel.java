package com.workshop.set.view.menus;

import static com.workshop.set.view.SetScreen.ORANGE;
import static com.workshop.set.view.menus.OptionPanel.DEFAULT_SIZE;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL11.glVertex2f;
import glfrontend.Triggerable;
import glfrontend.components.GLLabel;
import glfrontend.components.GLPanel;
import glfrontend.components.GLTextBox;

import java.awt.Color;

import org.lwjgl.util.vector.Vector2f;

import com.workshop.set.SetMain;
import com.workshop.set.model.VectorSpace.Geometry;
import com.workshop.set.model.VectorSpace.Point;
import com.workshop.set.model.VectorSpace.Relation;

public class GeomPanel extends GLPanel {

	private final Geometry _geom;
	private OptionPanel options;
	private boolean _point;
	private GLLabel _typeLabel;
	private GLTextBox _nameBox;
	private GLPanel _infoPanel;

	private boolean _moving;
	private int _expansionSpeed, _panelSpeed;

	public GeomPanel(Geometry geom, OptionPanel options) {
		super();
		this.setSize(DEFAULT_SIZE);
		this.setBackground(new Color(255, 255, 255, 30));
		this.setResizeType(ResizeType.FIT_LEFT);
		this._geom = geom;
		this._point = (geom instanceof Point);

		this.options = options;

		initNameLabel();
		if (_point)
			_infoPanel = new PointPanel((Point) geom, options);
		else
			_infoPanel = new RelationPanel((Relation) geom, options);

		_infoPanel.setVisible(false);
		this.add(_infoPanel);
		// this.setSize(DEFAULT_SIZE.x * 2f, DEFAULT_SIZE.y);
		// this.add(_bottomPanel);
		_moving = false;
		_expansionSpeed = 1500; // 1000 pixels/second
		_panelSpeed = -_expansionSpeed / 3;
	}

	private void initNameLabel() {

		GLLabel label;
		if (_point)
			label = new GLLabel("POINT");
		else
			label = new GLLabel("RELATION");

		label.setLocation(0, 0);
		label.setSize(DEFAULT_SIZE.x / 2f, DEFAULT_SIZE.y);
		label.setBackground(new Color(128, 128, 128, 0));
		this.add(label);

		if (_point)
			_typeLabel = new GLLabel("P:");
		else
			_typeLabel = new GLLabel("R:");
		_typeLabel.setLocation(DEFAULT_SIZE.x / 2f, 0);
		_typeLabel.setSize(20, DEFAULT_SIZE.y);
		_typeLabel.setBackground(new Color(128, 128, 128, 0));
		_typeLabel.setVisible(false);
//		this.add(_typeLabel);

		_nameBox = new GLTextBox();
		_nameBox.setLocation(DEFAULT_SIZE.x / 2f, 3);
		_nameBox.setSize(DEFAULT_SIZE.x / 2f, DEFAULT_SIZE.y - 3);
		_nameBox.setText(_geom.name().toString());
		_nameBox.setBackground(new Color(0, 0, 0, 0));
		_nameBox.addTriggerable(new Triggerable() {

			@Override
			public void trigger(TriggerEvent e) {
				String newName = _nameBox.getText();
				// TODO: Error checking for similar names?
				_geom.setName(SetMain.GENSYM.generate(newName));
				setFocus(false);
				options.updateGeomPanels();
			}

		});

		this.add(_nameBox);
	}

	@Override
	public void render2D() {
		if (!isVisible())
			return;

		// set color
		glColor4f(_color[0], _color[1], _color[2], _color[3]);

		// draw quad
		glBegin(GL_QUADS);
		glVertex2f(ul.x, ul.y);
		glVertex2f(ul.x, lr.y);
		glVertex2f(lr.x, lr.y);
		glVertex2f(lr.x, ul.y);
		glEnd();

		glColor4f(ORANGE[0], ORANGE[1], ORANGE[2], 1f);

		glLineWidth(5f);
		glBegin(GL_LINES);
		glVertex2f(ul.x, lr.y);
		glVertex2f(lr.x, lr.y);
		glEnd();

		draw();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		super.mousePressed(e);
		if (e.location.x < DEFAULT_SIZE.x / 2f) {
			if (_moving) {
				_expansionSpeed = -_expansionSpeed;
				_panelSpeed = -_panelSpeed;
			} else {
				if (_infoPanel.isVisible()) {
					_infoPanel.setVisible(false);
					_typeLabel.setVisible(false);
				}
				_moving = true;
			}
		}
	}

	public void closeInfoPanel() {
		if (_moving && _expansionSpeed > 0) {
			_expansionSpeed = -_expansionSpeed;
			_panelSpeed = -_panelSpeed;
		} else if (_infoPanel.isVisible()) {
			_infoPanel.setVisible(false);
			_typeLabel.setVisible(false);
			_moving = true;
		}
	}

	@Override
	public void animate(long millisSincePrev) {
		if (_moving) {

			float seconds = millisSincePrev / 1000f;

			Vector2f size = getSize();
//			Vector2f loc = getLocation();

			float w = size.x + _expansionSpeed * seconds;
//			float x = loc.x + _panelSpeed * seconds;

			if (w <= DEFAULT_SIZE.x) {
				w = DEFAULT_SIZE.x;
//				x = 0;
				_moving = false;
				_expansionSpeed = -_expansionSpeed;
//				_panelSpeed = -_panelSpeed;
			} else if (w >= DEFAULT_SIZE.x * 2.5f) {
				w = DEFAULT_SIZE.x * 2.5f;
//				x = -DEFAULT_SIZE.x / 2;
				_moving = false;
				_typeLabel.setVisible(true);
				_infoPanel.setVisible(true);
				_expansionSpeed = -_expansionSpeed;
//				_panelSpeed = -_panelSpeed;
			}
			setSize(w, size.y);
//			setLocation(x, loc.y);
		}
		_nameBox.animate(millisSincePrev);
		_infoPanel.animate(millisSincePrev);
	}

	public void update() {
		_nameBox.setText(_geom.name().toString());
		_infoPanel.update();
	}

}
