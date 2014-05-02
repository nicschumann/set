package com.workshop.set.view.menus;

import static com.workshop.set.view.menus.OptionPanel.DEFAULT_SIZE;
import glfrontend.components.GLLabel;
import glfrontend.components.GLPanel;
import glfrontend.components.GLTextBox;

import java.awt.Color;

import com.workshop.set.model.VectorSpace.Geometry;
import com.workshop.set.model.VectorSpace.Point;
import com.workshop.set.model.VectorSpace.Relation;

public class GeomPanel extends GLPanel {

	private final Geometry _geom;
	private boolean _point;
	private GLLabel _typeLabel;
	private GLTextBox _nameBox;
	private GLPanel _bottomPanel;

	public GeomPanel(Geometry geom) {
		super();
		this.setSize(DEFAULT_SIZE);
		this.setBackground(new Color(0, 0, 0, 0));
		this.setResizeType(ResizeType.FIT_LEFT);
		this._geom = geom;
		this._point = (geom instanceof Point);

		initNameLabel();
		if (_point)
			_bottomPanel = new PointPanel((Point) geom);
		else
			_bottomPanel = new RelationPanel((Relation) geom);
	}

	private void initNameLabel() {

		if (_point)
			_typeLabel = new GLLabel("Point");
		else
			_typeLabel = new GLLabel("Relation");

		_typeLabel.setLocation(0, 0);
		_typeLabel.setSize(DEFAULT_SIZE.x / 2f, DEFAULT_SIZE.y / 2);
		_typeLabel.setBackground(new Color(128, 128, 128, 0));
//		_typeLabel.setForeground(Color.WHITE);
		
		_nameBox = new GLTextBox();
		_nameBox.setLocation(DEFAULT_SIZE.x / 2f, 3);
		_nameBox.setSize(DEFAULT_SIZE.x / 2f, DEFAULT_SIZE.y / 2f - 3);
		_nameBox.setText(_geom.name().toString());
		_nameBox.setBackground(new Color(0, 0, 0, 0));
//		_nameBox.setForeground(Color.WHITE);

		this.add(_typeLabel);
		this.add(_nameBox);
	}
	
	@Override
	public void animate(long millisSincePrev) {
		_nameBox.animate(millisSincePrev);
	}

}
