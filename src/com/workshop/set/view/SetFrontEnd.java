package com.workshop.set.view;

import glfrontend.GLFrontEnd;
import glfrontend.ScreenFrame;
import glfrontend.Triggerable;
import glfrontend.components.GLButton;
import glfrontend.components.GLLabel;
import glfrontend.components.GLPanel;

import java.awt.Color;
import java.awt.Dimension;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

public class SetFrontEnd extends GLFrontEnd {

	public SetFrontEnd() {
		super("Set", new Dimension(750, 600));
		this.setMainScreen(setUpScreens());
	}
	
	private GLLabel label;

	private ScreenFrame setUpScreens() {
		try {
			Display.setFullscreen(true);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		int width = Display.getWidth();
		int height = Display.getHeight();

		SetScreen main = new SetScreen(width, height);

		Stage stage = new Stage(width, height * 4f / 5f);
		stage.setLocation(new Vector2f(0, 0));

		// main.add(stage);
		main.add(makeSelectionPanel(width, height));

		return main;
	}

	private GLPanel makeSelectionPanel(float screenWidth, float screenHeight) {
		float panelWidth = screenWidth / 5f;
		float panelHeight = screenHeight * 4f / 5f;

		GLPanel selectionPanel = new GLPanel();
		selectionPanel.setLocation(0, 0);
		selectionPanel.setSize(panelWidth, panelHeight);
		selectionPanel.setBackground(new Color(255, 255, 255, 100));
		selectionPanel.setResizable(false);

		label = new GLLabel("Label");
		label.setLocation(panelWidth / 10f, 300);
		label.setSize(panelWidth * 4 / 5, 40);
		label.setBackground(Color.DARK_GRAY);
		selectionPanel.add(label);
		
		float buttonWidth = panelWidth / 2f;
		float buttonHeight = panelHeight / 10f;

		selectionPanel.add(makeButton(0, 0, buttonWidth, buttonHeight, "Button 1", Color.ORANGE));
		selectionPanel.add(makeButton(0, buttonHeight, buttonWidth, buttonHeight, "Button 3", Color.ORANGE));
		selectionPanel.add(makeButton(buttonWidth, 0, buttonWidth, buttonHeight, "Button 2", Color.ORANGE));
		selectionPanel.add(makeButton(buttonWidth, buttonHeight, buttonWidth, buttonHeight, "Button 4", Color.ORANGE));


		return selectionPanel;
	}

	private GLButton makeButton(float x, float y, float w, float h, String text, Color color) {
		GLButton button = new GLButton(text);
		button.setLocation(x, y);
		button.setSize(w, h);
		button.setBackground(color);
		button.addTriggerable(new Triggerable() {

			@Override
			public void trigger(TriggerEvent e) {
				GLButton b = (GLButton) e.getSource();
				label.setText(b.getText() + " pressed");
			}
			
		});
		return button;
	}
}
