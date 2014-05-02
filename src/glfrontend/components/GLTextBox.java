package glfrontend.components;

import static com.workshop.set.view.SetScreen.newRatio;
import static glfrontend.components.GLComponent.TextAlignment.LEFT;
import static glfrontend.components.GLComponent.TextAlignment.RIGHT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glDisable;

import java.awt.Color;
import java.awt.Font;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.opengl.TextureImpl;

public class GLTextBox extends GLComponent {

	private String text;
	private Font awtFont;
	private UnicodeFont font;
	private Vector2f textLoc;
	private Color _foreground;
	private TextAlignment _textAlign;

	public GLTextBox() {
		super();
		init();
	}

	public GLTextBox(String text) {
		super();
		init();
		setText(text);
	}

	public void init() {
		text = "";
		_foreground = Color.BLACK;
		_textAlign = LEFT;
		
		setFont(new java.awt.Font("Sans Serif", java.awt.Font.BOLD, 13));
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
	
	public void setAlignment(TextAlignment align) {
		_textAlign = align;
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
		
		float x;
		int textHeight = font.getAscent() + font.getDescent();
		Vector2f mid = new Vector2f();
		Vector2f.sub(lr, ul, mid);
		
		if (_textAlign == LEFT) {
			x = 5;
		} else {
			int textWidth = font.getWidth(text);
			if (_textAlign == RIGHT) {
				x = lr.x - textWidth - 5;
			} else { // CENTER
				x = (mid.x - textWidth) / 2;
			}
		}
			
		
		
		textLoc = new Vector2f(x, (mid.y - textHeight) / 2);
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
	public void resize(Vector2f newSize) {
		switch (getResizeType()) {
		case FIT_LEFT:
			break;
		case FIT_RIGHT:
			setLocation(new Vector2f(ul.x - (getSize().x - newSize.x), ul.y));
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
