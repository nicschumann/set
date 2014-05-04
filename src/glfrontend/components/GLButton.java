package glfrontend.components;

import static com.workshop.set.view.SetScreen.newRatio;
import static glfrontend.GLFrontEnd.BUTTON_FONT;
import static glfrontend.ScreenFrame.MouseButton.LEFT;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;
import glfrontend.Triggerable;
import glfrontend.Triggerable.TriggerEvent;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

public class GLButton extends GLComponent {

	private float[] _brightColor = new float[4];
	private float[] _midColor = new float[4];
	private float bs; // border size
	private String text;
	private Vector2f textLoc;
	private boolean pressed;
	private List<Triggerable> triggers;

	public GLButton() {
		super();
		init();
	}

	public GLButton(String text) {
		super();
		init();
		setText(text);
	}

	public void init() {
		text = "";
		setTextLoc();
		triggers = new ArrayList<>();
	}

	public void addTriggerable(Triggerable trigger) {
		triggers.add(trigger);
	}

	public void setText(String text) {
		this.text = text;
		setTextLoc();
	}

	public String getText() {
		return text;
	}

	private void setTextLoc() {
		int textWidth = BUTTON_FONT.getWidth(text);
		int textHeight = BUTTON_FONT.getAscent() + BUTTON_FONT.getDescent();
		Vector2f mid = new Vector2f();
		Vector2f.sub(lr, ul, mid);
		textLoc = new Vector2f((mid.x - textWidth) / 2, (mid.y - textHeight) / 2);
	}

	@Override
	public void setSize(Vector2f dim) {
		Vector2f.add(ul, dim, lr);
		bs = (lr.y - ul.y) / 15f;
		setTextLoc();
	}

	@Override
	public void setBackground(Color color) {
		Color temp = color.darker().darker();
		temp.getComponents(_color);

		temp = color.brighter();
		temp.getComponents(_brightColor);

		color.getComponents(_midColor);
	}

	@Override
	public void draw() {

		// set top border color
		glColor4f(_brightColor[0], _brightColor[1], _brightColor[2], _brightColor[3]);

		// draw top of border
		glBegin(GL_QUADS);
		glVertex2f(ul.x, ul.y);
		glVertex2f(ul.x, lr.y - bs);
		glVertex2f(lr.x - bs, lr.y - bs);
		glVertex2f(lr.x - bs, ul.y);
		glEnd();

		// set color
		glColor4f(_midColor[0], _midColor[1], _midColor[2], _midColor[3]);

		// draw button
		glBegin(GL_QUADS);
		glVertex2f(ul.x + bs, ul.y + bs);
		glVertex2f(ul.x + bs, lr.y - bs);
		glVertex2f(lr.x - bs, lr.y - bs);
		glVertex2f(lr.x - bs, ul.y + bs);
		glEnd();

		if (textLoc != null) {
			BUTTON_FONT.drawString(textLoc.x + ul.x, textLoc.y + ul.y, text);
			glDisable(GL_TEXTURE_2D);
		}

	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (!pressed && e.button == LEFT) {
			float[] temp = _brightColor;
			_brightColor = _color;
			_color = temp;
			pressed = true;
			textLoc.x += bs;
			textLoc.y += bs;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (pressed && e.button == LEFT) {
			float[] temp = _brightColor;
			_brightColor = _color;
			_color = temp;
			pressed = false;
			textLoc.x -= bs;
			textLoc.y -= bs;
			for (Triggerable trigger : triggers) {
				trigger.trigger(new TriggerEvent(this));
			}
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		if (!pressed && e.button == LEFT) {
			float[] temp = _brightColor;
			_brightColor = _color;
			_color = temp;
			pressed = true;
			textLoc.x += bs;
			textLoc.y += bs;
		}
		
	}

	@Override
	public void mouseExited(Vector2f p) {
		if (pressed) {
			float[] temp = _brightColor;
			_brightColor = _color;
			_color = temp;
			pressed = false;
			textLoc.x -= bs;
			textLoc.y -= bs;
		}
	}

	@Override
	public void resize(Vector2f newSize) {
		switch (getResizeType()) {
		case FIT_LEFT:
			setLocation(new Vector2f(ul.x - (getSize().x - newSize.x), ul.y));
			break;
		case FIT_RIGHT:
			break;
		case FIT_BOTTOM:
			break;
		case FIT_TOP:
			break;
		default: // RATIO Type
			setLocation(newRatio(getLocation(), getSize(), newSize));

			Vector2f size = new Vector2f();
			Vector2f.sub(lr, ul, size);

			Vector2f.add(ul, newSize, lr);
			setTextLoc();
			break;
		}
	}

}
