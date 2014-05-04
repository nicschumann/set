package glfrontend.components;

import static com.workshop.set.view.SetScreen.newRatio;
import static glfrontend.GLFrontEnd.LABEL_FONT;
import static glfrontend.components.GLComponent.TextAlignment.CENTER;
import static glfrontend.components.GLComponent.TextAlignment.LEFT;
import static glfrontend.components.GLComponent.TextAlignment.RIGHT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glDisable;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.opengl.TextureImpl;

public class GLLabel extends GLComponent {

	private String text;
	private Vector2f textLoc;
	private TextAlignment _textAlign;

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
		_textAlign = CENTER;

		setTextLoc();
		TextureImpl.bindNone();
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

	private void setTextLoc() {
		float x;
		int textHeight = LABEL_FONT.getAscent() + LABEL_FONT.getDescent();
		Vector2f mid = new Vector2f();
		Vector2f.sub(lr, ul, mid);

		if (_textAlign == LEFT) {
			x = 5;
		} else {
			int textWidth = LABEL_FONT.getWidth(text);
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
			LABEL_FONT.drawString(textLoc.x + ul.x, textLoc.y + ul.y, text);
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
