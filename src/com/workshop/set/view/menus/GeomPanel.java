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
	private GLPanel _bottomPanel;

	public GeomPanel(Geometry geom, OptionPanel options) {
		super();
		this.setSize(DEFAULT_SIZE);
		this.setBackground(new Color(0, 0, 0, 0));
		this.setResizeType(ResizeType.FIT_LEFT);
		this._geom = geom;
		this._point = (geom instanceof Point);
		
		this.options = options;

		initNameLabel();
		if (_point)
			_bottomPanel = new PointPanel((Point) geom, options);
		else
			_bottomPanel = new RelationPanel((Relation) geom, options);

		this.add(_bottomPanel);
	}

	private void initNameLabel() {

		if (_point)
			_typeLabel = new GLLabel("Point");
		else
			_typeLabel = new GLLabel("Relation");

		_typeLabel.setLocation(0, 0);
		_typeLabel.setSize(DEFAULT_SIZE.x / 2f, DEFAULT_SIZE.y / 2);
		_typeLabel.setBackground(new Color(128, 128, 128, 0));

		_nameBox = new GLTextBox();
		_nameBox.setLocation(DEFAULT_SIZE.x / 2f, 3);
		_nameBox.setSize(DEFAULT_SIZE.x / 2f, DEFAULT_SIZE.y / 2f - 3);
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

		this.add(_typeLabel);
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

		glColor4f(ORANGE[0], ORANGE[1], ORANGE[2], 0.5f);

		glLineWidth(5f);
		glBegin(GL_LINES);
		glVertex2f(ul.x, lr.y);
		glVertex2f(lr.x, lr.y);
		glEnd();

		draw();
	}

	@Override
	public void animate(long millisSincePrev) {
		_nameBox.animate(millisSincePrev);
		_bottomPanel.animate(millisSincePrev);
	}

	public void update() {
		_nameBox.setText(_geom.name().toString());
		_bottomPanel.update();
	}

}
