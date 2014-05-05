package com.workshop.set.view;

import glfrontend.GLFrontEnd;
import glfrontend.ScreenFrame;

import java.awt.Dimension;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

import com.workshop.set.model.Interpreter;
import com.workshop.set.model.interfaces.Model;
import com.workshop.set.view.viewport.Stage;

public class SetFrontEnd extends GLFrontEnd {

	public SetFrontEnd(Model model) {
		super("Set", new Dimension(750, 600));
		this.setMainScreen(setUpScreens(model));
	}

	private ScreenFrame setUpScreens(Model model) {
		int width = Display.getWidth();
		int height = Display.getHeight();

		SetScreen main = new SetScreen(model, width, height);

		Stage stage = new Stage(width, height);
		stage.setLocation(new Vector2f(0, 0));

		main.setStage(stage);

		model.setScreen(main);

		return main;
	}

	public void enterLoop(Interpreter textual) {
		animationTime = System.currentTimeMillis();
		while (!Display.isCloseRequested() && textual.isRunning()) {
			onRender();
			onTick();
			checkInput();
			update();
		}
	}

}
