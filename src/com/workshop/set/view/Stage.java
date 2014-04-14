package com.workshop.set.view;

import glfrontend.components.GLComponent;

import org.lwjgl.util.vector.Vector2f;

public class Stage extends GLComponent {


	public Stage() {
		super();
	}

	public Stage(float x, float y) {
		super(x, y);
	}

	public Stage(Vector2f size) {
		super(size);
	}

	@Override
	public void draw() {
//		be.getRenderObjects();
	}

	@Override
	public void resize(Vector2f dim) {
		if (!isResizable())
			return;
		Vector2f size = new Vector2f();
		Vector2f.sub(lr, ul, size);

		Vector2f.add(ul, dim, lr);
	}

}
