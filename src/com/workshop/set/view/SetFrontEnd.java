package com.workshop.set.view;

import glfrontend.GLFrontEnd;
import glfrontend.ScreenFrame;

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
		
		model.setScreen(main);

		return main;
	}

	
//	private GLButton makeButton(float x, float y, float w, float h, String text, Color color) {
//		GLButton button = new GLButton(text);
//		button.setLocation(x, y);
//		button.setSize(w, h);
//		button.setBackground(color);
//		button.addTriggerable(new Triggerable() {
//
//			@Override
//			public void trigger(TriggerEvent e) {
//				GLButton b = (GLButton) e.getSource();
//				label.setText(b.getText() + " pressed");
//			}
//
//		});
//		return button;
//	}
	
}
