package com.workshop.set.view.panels;

import static com.workshop.set.view.SetScreen.ORANGE;
import static com.workshop.set.view.panels.OptionPanel.DEFAULT_SIZE;
import static org.lwjgl.opengl.GL11.GL_LINES;
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
	private GLLabel _label;
	private GLTextBox _nameBox;
	private GLPanel _infoPanel;

	private boolean _moving;
	private int _expansionSpeed;

	public GeomPanel(Geometry geom, OptionPanel options) {
		super();
		this.setSize(DEFAULT_SIZE);
		this.setBackground(new Color(255, 255, 255, 30));
		this.setResizeType(ResizeType.FIT_LEFT);
		this.setBorder(new Color(255, 128, 0));
		this.setBorderWidth(2f);
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
	}

	private void initNameLabel() {

		String text = "";
		if (_geom.isPivot())
			text += "(P) ";
		
		if (_point)
			text += "POINT";
		else
			text += "RELATION";
		
		_label = new GLLabel(text);
		_label.setLocation(0, 0);
		_label.setSize(DEFAULT_SIZE.x / 2f, DEFAULT_SIZE.y);
		_label.setBackground(new Color(128, 128, 128, 0));
		this.add(_label);

		_nameBox = new GLTextBox();
		_nameBox.setLocation(DEFAULT_SIZE.x / 2f, 2);
		_nameBox.setSize(DEFAULT_SIZE.x / 2f, DEFAULT_SIZE.y - 4);
		_nameBox.setText(_geom.name().toString());
		_nameBox.setBackground(new Color(0, 0, 0, 0));
		_nameBox.setBorder(new Color(255, 128, 0));
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

		super.render2D();

		glColor4f(ORANGE[0], ORANGE[1], ORANGE[2], 1f);

		glLineWidth(5f);
		glBegin(GL_LINES);
		glVertex2f(ul.x, lr.y);
		glVertex2f(lr.x, lr.y);
		glEnd();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		super.mousePressed(e);
		if (e.location.x < DEFAULT_SIZE.x / 2f) {
			if (_moving) {
				_expansionSpeed = -_expansionSpeed;
			} else {
				if (_infoPanel.isVisible()) {
					_infoPanel.setVisible(false);
				}
				_moving = true;
			}
		}
	}

	public void closeInfoPanel() {
		if (_moving && _expansionSpeed > 0) {
			_expansionSpeed = -_expansionSpeed;
		} else if (_infoPanel.isVisible()) {
			_infoPanel.setVisible(false);
			_moving = true;
		}
	}

	@Override
	public void animate(long millisSincePrev) {
		if (_moving) {

			float seconds = millisSincePrev / 1000f;

			Vector2f size = getSize();

			float w = size.x + _expansionSpeed * seconds;

			if (w <= DEFAULT_SIZE.x) {
				w = DEFAULT_SIZE.x;
				_moving = false;
				_expansionSpeed = -_expansionSpeed;
			} else if (w >= DEFAULT_SIZE.x * 2.5f) {
				w = DEFAULT_SIZE.x * 2.5f;
				_moving = false;
				_infoPanel.setVisible(true);
				_expansionSpeed = -_expansionSpeed;
			}
			setSize(w, size.y);
		}
		_nameBox.animate(millisSincePrev);
		_infoPanel.animate(millisSincePrev);
	}

	public void update() {
		String text = "";
		if (_geom.isPivot())
			text += "(P) ";
		
		if (_point)
			text += "POINT";
		else
			text += "RELATION";
		
		_label.setText(text);
		_nameBox.setText(_geom.name().toString());
		_infoPanel.update();
	}

}
