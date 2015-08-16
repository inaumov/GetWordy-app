package get.wordy.app.ui.util;

import java.awt.*;

public class ScreenSize {

    private static Dimension screenSize;

    static {
        Toolkit kit = Toolkit.getDefaultToolkit();
        screenSize = kit.getScreenSize();
    }

    public static int getHeight() {
        return screenSize.height;
    }

    public static int getWidth() {
        return screenSize.width;
    }


}