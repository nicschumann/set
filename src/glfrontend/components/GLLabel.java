package glfrontend.components;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glDisable;

import java.awt.Color;
import java.awt.Font;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.opengl.TextureImpl;

public class GLLabel extends GLComponent {

	private String text;
	private UnicodeFont font;
	private Vector2f textLoc;
	private Font awtFont;
	private Color _foreground;

	public GLLabel() {
		super();
		init();
	}

	public GLLabel(String text) {
		super();
		init();
		setText(text);
	}

	public void init() {
		text = "";
		_foreground = Color.BLACK;
		setFont(new java.awt.Font("Times New Roman", java.awt.Font.BOLD, 14));
		TextureImpl.bindNone();
	}
	
	public void setForeground(Color color) {
		_foreground = color;
		setFont(awtFont);
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
		this.awtFont = awtFont;
		font = new UnicodeFont(awtFont);
		font.getEffects().add(new ColorEffect(_foreground));
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
		Vector2f.sub(lr, ul, mid);
		textLoc = new Vector2f((mid.x - textWidth) / 2, (mid.y - textHeight) / 2);
	}

	@Override
	public void setSize(Vector2f dim) {
		Vector2f.add(ul, dim, lr);
		setTextLoc();
	}

	@Override
	public void draw() {
		if (textLoc != null) {
			font.drawString(textLoc.x + ul.x, textLoc.y + ul.y, text);
			glDisable(GL_TEXTURE_2D);
		}
	}

	@Override
	public void mousePressed(Vector2f p, MouseButton button) {}

	@Override
	public void mouseReleased(Vector2f p, MouseButton button) {}

	@Override
	public void mouseWheelScrolled(int amount) {}

	@Override
	public void keyPressed(int key) {}

	@Override
	public void keyReleased(int key) {}

	@Override
	public void resize(Vector2f newSize) {
		if (!isResizable())
			return;
		Vector2f size = new Vector2f();
		Vector2f.sub(lr, ul, size);

		Vector2f.add(ul, newSize, lr);
		setTextLoc();
	}

	@Override
	public void mouseMoved(Vector2f p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(Vector2f p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(Vector2f p) {
		// TODO Auto-generated method stub
		
	}

}
