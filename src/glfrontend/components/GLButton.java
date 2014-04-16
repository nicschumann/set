package glfrontend.components;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.awt.Color;
import java.awt.Font;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.opengl.TextureImpl;

public class GLButton extends GLComponent {

	private float[] _brightColor = new float[4];
	private float[] _midColor = new float[4];
	private float bs; // border size
	private String text;
	private UnicodeFont font;
	private Vector2f textLoc;
	private boolean pressed;

	public GLButton() {
		super();
		init();
	}

	public GLButton(String text) {
		super();
		init();
		setText(text);
	}

	public GLButton(float x, float y) {
		super(x, y);
		init();
	}

	public GLButton(float x, float y, String text) {
		super(x, y);
		init();
		setText(text);
	}

	public GLButton(Vector2f dim) {
		super(dim);
		init();
	}

	public GLButton(Vector2f dim, String text) {
		super(dim);
		init();
		setText(text);
	}

	public void init() {
		text = "Button";
		setFont(new java.awt.Font("Times New Roman", java.awt.Font.BOLD, 14));
		TextureImpl.bindNone();
	}

	public void setText(String text) {
		this.text = text;
		setTextLoc();
	}

	public String getText() {
		return text;
	}

	@SuppressWarnings("unchecked")
	public void setFont(Font awtFont) {
        font = new UnicodeFont(awtFont);
        font.getEffects().add(new ColorEffect(java.awt.Color.black));
        font.addAsciiGlyphs();
        try {
            font.loadGlyphs();
        } catch (SlickException e) {
            e.printStackTrace();
        }
		setTextLoc();
	}

	private void setTextLoc() {
		if (font == null)
			return;
		int textWidth = font.getWidth(text);
		int textHeight = font.getAscent() + font.getDescent();
		Vector2f mid = new Vector2f();
		Vector2f.add(ul, lr, mid);
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

		temp = color.brighter().brighter();
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
	        font.drawString(textLoc.x + ul.x, textLoc.y + ul.y, text);
		}

	}

	@Override
	public void mousePressed(Vector2f p, MouseButton button) {
		if (!pressed) {
			float[] temp = _brightColor;
			_brightColor = _color;
			_color = temp;
			pressed = true;
			textLoc.x += bs;
			textLoc.y += bs;
		}
	}

	@Override
	public void mouseReleased(Vector2f p, MouseButton button) {
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
	public void mouseWheelScrolled(int amount) {}

	@Override
	public void keyPressed(int key) {}

	@Override
	public void keyReleased(int key) {}

	@Override
	public void resize(Vector2f newSize) {}

}
