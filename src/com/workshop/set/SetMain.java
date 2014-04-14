package com.workshop.set;

import glfrontend.FrontEnd;

import com.workshop.set.frontend.SetFrontEnd;

public class SetMain {

	public static void main(String[] args) {

		final SetMain set = new SetMain();

		// Cleans up the openAL sound object right before program exits.
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				// Save things and exit cleanly
				set.saveAndClose();
			}
		}));
	}

	private FrontEnd fe;

	public SetMain() {
		// remove flickering
		System.setProperty("sun.awt.noerasebackground", "true");
		// disable DirectDraw/Direct3D to avoid conflicts with OpenGL
		System.setProperty("sun.java2d.noddraw", "true");
		// enable OpenGL Hardware Rendering
		System.setProperty("sun.java2d.opengl", "true");

		fe = new SetFrontEnd();
		fe.enterLoop();
		fe.cleanUp(false);
		fe = null;
	}

	public void saveAndClose() {
		// save stuff if need be
		// exit if need be
		if (fe != null)
			fe.cleanUp(false);
	}
}
