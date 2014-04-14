package com.workshop.set.view;

import glfrontend.GLFrontEnd;
import glfrontend.ScreenFrame;
import glfrontend.components.GLButton;
import glfrontend.components.GLPanel;

import java.awt.Color;
import java.awt.Dimension;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

public class SetFrontEnd extends GLFrontEnd {

	public SetFrontEnd() {
		super("Set", new Dimension(750, 600));
		this.setMainScreen(setUpScreens());
	}

	private ScreenFrame setUpScreens() {
		int width = Display.getWidth();
		int height = Display.getHeight();

		SetScreen main = new SetScreen(width, height);

		Stage stage = new Stage(width, height * 4f / 5f);
		stage.setLocation(new Vector2f(0, 0));

		GLPanel buttonPanel = new GLPanel(width, height / 5);
		buttonPanel.setLocation(0, height * 4 / 5);
		buttonPanel.setBackground(new Color(255, 255, 255, 100));

		GLButton button = new GLButton(60, 30);
		button.setLocation(30, 30);
		button.setBackground(Color.ORANGE);
		buttonPanel.add(button);

		// main.add(stage);
		main.add(buttonPanel);

		return main;
	}
}
