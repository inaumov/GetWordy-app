package get.wordy.app.settings;

import java.awt.*;

public class FontUtil {

    public static final int MIN_SIZE = 12;
    public static final int MAX_SIZE = 20;

    public static final String DEFAULT_NAME = FontName.TAHOMA.getName();
    public static final int DEFAULT_SIZE = 14;

    public static Font smaller(Font font, int fontSizeDiff) throws WrongFontSizeException {
        int newSize = font.getSize() - fontSizeDiff;
        if (newSize < MIN_SIZE) {
            throw new WrongFontSizeException("Font size [" + newSize + "] can't be smaller then [%s]", font, MIN_SIZE);
        }
        return new Font(font.getName(), Font.PLAIN, newSize);
    }

    public static Font changeName(Font font, String name) {
        if (FontName.matches(name)) {
            return new Font(name, font.getStyle(), font.getSize());
        } else {
            return font;
        }
    }

    public static Font changeStyle(Font font, int style) {
        return new Font(font.getName(), style, font.getSize());
    }

    public static Font changeSize(Font font, int newSize) throws WrongFontSizeException {
        if (newSize < MIN_SIZE) {
            throw new WrongFontSizeException("Font size [" + newSize + "] can't be smaller then [%s] ", font, MIN_SIZE);
        } else if (newSize > MAX_SIZE) {
            throw new WrongFontSizeException("Font size [" + newSize + "] can't be bigger then [%s]", font, MAX_SIZE);
        }
        return new Font(font.getName(), font.getStyle(), newSize);
    }

    public static class WrongFontSizeException extends Exception {

        private Font font;
        private int size;

        public WrongFontSizeException(String msg, Font font, int size) {
            super(String.format(msg, size));
            this.font = font;
            this.size = size;
        }

        public Font getResult() {
            return new Font(font.getName(), font.getStyle(), size);
        }
    }

}