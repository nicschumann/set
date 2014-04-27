package com.workshop.set.view;

import glfrontend.GLFrontEnd;
import glfrontend.ScreenFrame;
import glfrontend.ScreenFrame.ResizeType;
import glfrontend.Triggerable;
import glfrontend.components.GLButton;
import glfrontend.components.GLLabel;
import glfrontend.components.GLPanel;

import java.awt.Color;
import java.awt.Dimension;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

import com.workshop.set.model.interfaces.Model;

public class SetFrontEnd extends GLFrontEnd {

	public static Color ORANGE = new Color(255, 128, 0);

	public SetFrontEnd(Model model) {
		super("Set", new Dimension(750, 600));
		this.setMainScreen(setUpScreens(model));
	}

	private GLLabel label;

	private ScreenFrame setUpScreens(Model model) {
		try {
			Display.setFullscreen(true);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		int width = Display.getWidth();
		int height = Display.getHeight();

		SetScreen main = new SetScreen(model, width, height);	

		Stage stage = new Stage(width, height);
		stage.setLocation(new Vector2f(0, 0));

		main.setStage(stage);
		//main.add(stage);
		main.add(makeSelectionPanel(width, height));
		main.add(makeConstraintPanel(width, height));

		return main;
	}

	private GLPanel makeSelectionPanel(float screenWidth, float screenHeight) {
		float panelWidth = screenWidth / 5f;
		float panelHeight = screenHeight * 4f / 5f;

		GLPanel selectionPanel = new GLPanel();
		selectionPanel.setLocation(0, 0);
		selectionPanel.setSize(panelWidth, panelHeight);
		selectionPanel.setBackground(new Color(255, 255, 255, 100));
		selectionPanel.setResizeType(ResizeType.FIT_LEFT);

		label = new GLLabel("Label");
		label.setLocation(panelWidth / 10f, 300);
		label.setSize(panelWidth * 4 / 5, 40);
		label.setBackground(Color.DARK_GRAY);
		label.setForeground(ORANGE);
		selectionPanel.add(label);

		float buttonWidth = panelWidth / 2f;
		float buttonHeight = panelHeight / 10f;

		selectionPanel.add(makeButton(0, 0, buttonWidth, buttonHeight, "Button 1", ORANGE));
		selectionPanel.add(makeButton(0, buttonHeight, buttonWidth, buttonHeight, "Button 3", ORANGE));
		selectionPanel.add(makeButton(0, 2 * buttonHeight, buttonWidth, buttonHeight, "Button 5", ORANGE));
		selectionPanel.add(makeButton(buttonWidth, 0, buttonWidth, buttonHeight, "Button 2", ORANGE));
		selectionPanel.add(makeButton(buttonWidth, buttonHeight, buttonWidth, buttonHeight, "Button 4", ORANGE));
		selectionPanel.add(makeButton(buttonWidth, 2 * buttonHeight, buttonWidth, buttonHeight, "Button 6", ORANGE));

		return selectionPanel;
	}

	private GLPanel makeConstraintPanel(float screenWidth, float screenHeight) {
		float panelWidth = screenWidth / 5f;
		float panelHeight = screenHeight * 4f / 5f;

		GLPanel constraintPanel = new GLPanel();
		constraintPanel.setLocation(screenWidth * 4f / 5f, 0);
		constraintPanel.setSize(panelWidth, panelHeight);
		constraintPanel.setBackground(new Color(255, 255, 255, 100));
		constraintPanel.setResizeType(ResizeType.FIT_RIGHT);

		return constraintPanel;
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
