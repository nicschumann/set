package glfrontend.components;

import static com.workshop.set.view.SetScreen.CYAN;
import static com.workshop.set.view.SetScreen.ORANGE;
import static com.workshop.set.view.SetScreen.newRatio;
import static glfrontend.components.GLComponent.TextAlignment.LEFT;
import static glfrontend.components.GLComponent.TextAlignment.RIGHT;
import static org.lwjgl.input.Keyboard.KEY_BACK;
import static org.lwjgl.input.Keyboard.KEY_LEFT;
import static org.lwjgl.input.Keyboard.KEY_LSHIFT;
import static org.lwjgl.input.Keyboard.KEY_RIGHT;
import static org.lwjgl.input.Keyboard.KEY_RSHIFT;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.awt.Color;
import java.awt.Font;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.opengl.TextureImpl;

public class GLTextBox extends GLComponent {

	int edgeBuffer = 5;

	// private String _text;
	private Font _awtFont;
	private UnicodeFont _font;
	private Vector2f _textLoc;
	private Color _foreground;
	private TextAlignment _textAlign;

	private boolean _shiftDown;

	private int _cursor;
	private long _cursorTimer;
	private float _opacity;
	private StringBuilder _sb;

	// private List<MyChar> _chars;

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
		_sb = new StringBuilder();
		_foreground = Color.BLACK;
		_textAlign = LEFT;

		setFont(new java.awt.Font("Sans Serif", java.awt.Font.BOLD, 13));
		TextureImpl.bindNone();

		_shiftDown = false;

		_cursor = 0;
		_cursorTimer = 500;
		_opacity = 0.8f;
		// _chars = new ArrayList<>();
	}

	public void setForeground(Color color) {
		_foreground = color;
		setFont(_awtFont);
	}

	public void setText(String text) {
		// this._text = text;
		setTextLoc();
	}

	public String getText() {
		return _sb.toString();
	}

	public void addCharacter(char c) {
		_sb.insert(_cursor++, c);
	}

	public void setAlignment(TextAlignment align) {
		_textAlign = align;
	}

	public boolean isShiftDown() {
		return _shiftDown;
	}

	@SuppressWarnings("unchecked")
	public void setFont(Font awtFont) {
		this._awtFont = awtFont;
		_font = new UnicodeFont(awtFont);
		_font.getEffects().add(new ColorEffect(_foreground));
		_font.addAsciiGlyphs();
		try {
			_font.loadGlyphs();
		} catch (SlickException e) {
			e.printStackTrace();
		}
		setTextLoc();
	}

	private void setTextLoc() {
		if (_font == null)
			return;

		float x;
		int textHeight = _font.getAscent() + _font.getDescent();
		Vector2f mid = new Vector2f();
		Vector2f.sub(lr, ul, mid);

		if (_textAlign == LEFT) {
			x = edgeBuffer;
		} else {
			int textWidth = _font.getWidth(getText());
			if (_textAlign == RIGHT) {
				x = lr.x - textWidth - edgeBuffer;
			} else { // CENTER
				x = (mid.x - textWidth) / 2;
			}
		}

		_textLoc = new Vector2f(x, (mid.y - textHeight) / 2);
	}

	@Override
	public void setSize(Vector2f dim) {
		Vector2f.add(ul, dim, lr);
		setTextLoc();
	}

	@Override
	public void draw() {

		glColor4f(ORANGE[0], ORANGE[1], ORANGE[2], 0.2f);
		glLineWidth(1f);
		glBegin(GL_LINES);
		glVertex2f(ul.x, ul.y);
		glVertex2f(ul.x, lr.y);

		glVertex2f(ul.x, lr.y);
		glVertex2f(lr.x, lr.y);

		glVertex2f(lr.x, lr.y);
		glVertex2f(lr.x, ul.y);

		glVertex2f(lr.x, ul.y);
		glVertex2f(ul.x, ul.y);
		glEnd();

		if (_textLoc != null) {
			_font.drawString(_textLoc.x + ul.x, _textLoc.y + ul.y, getText());
			glDisable(GL_TEXTURE_2D);
		}

		Vector2f cursorPos = new Vector2f(edgeBuffer + _font.getWidth(_sb.substring(0, _cursor)), getSize().y
				- edgeBuffer);

		glColor4f(ORANGE[0], ORANGE[1], ORANGE[2], _opacity);

		glLineWidth(3f);
		glBegin(GL_LINES);
		glVertex2f(cursorPos.x, edgeBuffer);
		glVertex2f(cursorPos.x, cursorPos.y);
		glEnd();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.keyCode) {
		case KEY_LEFT:
			_cursor--;
			if (_cursor < 0)
				_cursor = 0;
			break;
		case KEY_RIGHT:
			_cursor++;
			if (_cursor >= _sb.length())
				_cursor = _sb.length();
			break;
		case KEY_BACK:
			if (_sb.length() > 0 && _cursor > 0)
				_sb.deleteCharAt(--_cursor);
			break;
		case KEY_LSHIFT:
		case KEY_RSHIFT:
			_shiftDown = true;
			break;
		default:
			addCharacter(e.keyChar);
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.keyCode == KEY_LSHIFT || e.keyCode == KEY_RSHIFT)
			_shiftDown = false;
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

	@Override
	public void animate(long millisSincePrev) {
		_cursorTimer -= millisSincePrev;
		if (_cursorTimer < 0) {
			_cursorTimer = 500;
			if (_opacity == 0.8f)
				_opacity = 0.2f;
			else
				_opacity = 0.8f;
		}
	}

	// private static class MyChar {
	//
	// public final char dist;
	// public final int index;
	//
	// public MyChar(char dist, int index) {
	// this.dist = dist;
	// this.index = index;
	// }
	//
	// // public int getDistFromStart() {
	// // return distFromStart;
	// // }
	// //
	// // public void setDistFromStart(int distFromStart) {
	// // this.distFromStart = distFromStart;
	// // }
	// }

}
