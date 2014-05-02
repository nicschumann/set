package glfrontend.components;

import static com.workshop.set.view.SetScreen.ORANGE;
import static com.workshop.set.view.SetScreen.newRatio;
import static glfrontend.GLFrontEnd.DEFAULT_FONT;
import static glfrontend.components.GLComponent.TextAlignment.LEFT;
import static glfrontend.components.GLComponent.TextAlignment.RIGHT;
import static org.lwjgl.input.Keyboard.KEY_BACK;
import static org.lwjgl.input.Keyboard.KEY_LCONTROL;
import static org.lwjgl.input.Keyboard.KEY_LEFT;
import static org.lwjgl.input.Keyboard.KEY_LSHIFT;
import static org.lwjgl.input.Keyboard.KEY_RCONTROL;
import static org.lwjgl.input.Keyboard.KEY_RETURN;
import static org.lwjgl.input.Keyboard.KEY_RIGHT;
import static org.lwjgl.input.Keyboard.KEY_RSHIFT;
import static org.lwjgl.input.Keyboard.KEY_TAB;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL11.glVertex2f;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.opengl.TextureImpl;

public class GLTextBox extends GLComponent {

	int edgeBuffer = 3;

	// private String _text;
	private Vector2f _textLoc;
	// private Color _foreground;
	private TextAlignment _textAlign;

	private boolean _ctrlDown;

	private int _cursor;
	private long _cursorTimer;
	private float _opacity;
	private StringBuilder _sb;
	private int _endSpaces;
	private float _spaceWidth;

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
		_textAlign = LEFT;

		setTextLoc();
		_spaceWidth = 4.2f;
		TextureImpl.bindNone();

		_ctrlDown = false;

		_cursor = 0;
		_cursorTimer = 500;
		_opacity = 0.8f;

		_sb = new StringBuilder();
		_endSpaces = 0;
	}

	public void setText(String text) {
		_sb = new StringBuilder(text);
		_cursor = _sb.length();
		_endSpaces = 0;
		for (int i = _cursor - 1; i >= 0 && _sb.charAt(i) == ' '; i--) {
			_endSpaces++;
		}
		setTextLoc();
	}

	public String getText() {
		return _sb.toString();
	}

	public void addCharacter(char c) {
		_sb.insert(_cursor++, c);
		if (_sb.length() == _cursor && c == ' ')
			_endSpaces++;
		else
			_endSpaces = 0;
	}

	public void removeCharacter() {
		_sb.deleteCharAt(--_cursor);
		if (_sb.length() == _cursor) {
			if (_endSpaces != 0)
				_endSpaces--;
			else {
				for (int i = _cursor - 1; i >= 0 && _sb.charAt(i) == ' '; i--) {
					_endSpaces++;
				}
			}
		}
	}

	public void setAlignment(TextAlignment align) {
		_textAlign = align;
	}

	public boolean isShiftDown() {
		return _ctrlDown;
	}

	private void setTextLoc() {
		float x;
		int textHeight = DEFAULT_FONT.getAscent() + DEFAULT_FONT.getDescent();
		Vector2f mid = new Vector2f();
		Vector2f.sub(lr, ul, mid);

		if (_textAlign == LEFT) {
			x = edgeBuffer;
		} else {
			int textWidth = DEFAULT_FONT.getWidth(getText());
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
			DEFAULT_FONT.drawString(_textLoc.x + ul.x, _textLoc.y + ul.y, getText());
			glDisable(GL_TEXTURE_2D);
		}

		int width = DEFAULT_FONT.getWidth(_sb.substring(0, _cursor)) + Math.round(_spaceWidth * _endSpaces);
		Vector2f cursorPos = new Vector2f(edgeBuffer + width, (getSize().y - edgeBuffer));

		glColor4f(ORANGE[0], ORANGE[1], ORANGE[2], _opacity);

		glLineWidth(3f);
		glBegin(GL_LINES);
		glVertex2f(ul.x + cursorPos.x, ul.y + edgeBuffer);
		glVertex2f(ul.x + cursorPos.x, ul.y + cursorPos.y);
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
				removeCharacter();
			break;
		case KEY_LCONTROL:
		case KEY_RCONTROL:
			_ctrlDown = true;
			break;
		case KEY_TAB:
		case KEY_RETURN:
		case KEY_LSHIFT:
		case KEY_RSHIFT:
			// do nothing
			break;
		default:
			addCharacter(e.keyChar);
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.keyCode == KEY_LCONTROL || e.keyCode == KEY_RCONTROL)
			_ctrlDown = false;
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
		if (isInFocus()) {
			_cursorTimer -= millisSincePrev;
			if (_cursorTimer < 0) {
				_cursorTimer = 500;
				if (_opacity == 0.8f)
					_opacity = 0.2f;
				else
					_opacity = 0.8f;
			}
		} else {
			_opacity = 0.0f;
			_cursorTimer = 300;
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
