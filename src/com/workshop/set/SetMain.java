package com.workshop.set;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import com.workshop.set.model.Interpreter;
import com.workshop.set.model.Solver;
import com.workshop.set.model.geometry.VectorSpace;
import com.workshop.set.model.interfaces.Gensym;
import com.workshop.set.model.interfaces.Model;
import com.workshop.set.model.lang.core.TNameGenerator;
import com.workshop.set.view.SetFrontEnd;

public class SetMain {

	public static final Gensym GENSYM = new TNameGenerator();
	public static final VectorSpace VEC_SPACE_3D = new VectorSpace(3, GENSYM);

	public static void main(String[] args) {

		final SetMain set = new SetMain();

		// Called right before program exits.
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				// Save things and exit cleanly
				set.saveAndClose();
			}
		}));
	}

	private SetFrontEnd fe;
    private Interpreter interp;
	// private Control c;
	private Model model;

    private ExecutorService _pool;

	public SetMain() {
		// remove flickering
		System.setProperty("sun.awt.noerasebackground", "true");
		// disable DirectDraw/Direct3D to avoid conflicts with OpenGL
		System.setProperty("sun.java2d.noddraw", "true");
		// enable OpenGL Hardware Rendering
		System.setProperty("sun.java2d.opengl", "true");

        _pool = new ScheduledThreadPoolExecutor( Runtime.getRuntime().availableProcessors() );

		model = new Solver(VEC_SPACE_3D, GENSYM);
        fe = new SetFrontEnd(model);
        interp = new Interpreter( (Solver)model, System.in, System.out, System.err );


        interp.enterLoop();
        fe.enterLoop( interp );

        fe.cleanUp( false );
        interp = null;
        fe = null;
	}

	public void saveAndClose() {
		// save stuff if need be
		// exit if need be
		if (fe != null && !interp.isRunning() )
			fe.cleanUp(false);

	}
}
