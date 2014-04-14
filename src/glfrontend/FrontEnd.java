package glfrontend;

public interface FrontEnd {

	/**
	 * Renders the game objects and characters for each time step.
	 */
	public void onRender();

	/**
	 * Handles the game logic for each time step.
	 */
	public void onTick();

	/**
	 * Handles the keyboard and mouse input from each time step;
	 */
	public void checkInput();

	/**
	 * Starts the update/render loop and continues until the window is closed.
	 */
	public void enterLoop();

	/**
	 * Exits the program cleanly.
	 * 
	 * @param asCrash
	 *            - true if the game exits because of a crash
	 */
	public void cleanUp(boolean asCrash);

}
