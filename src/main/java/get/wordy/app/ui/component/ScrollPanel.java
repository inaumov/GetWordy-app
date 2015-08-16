package get.wordy.app.ui.component;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JScrollPane;

public class ScrollPanel extends JScrollPane {

    public ScrollPanel(JComponent textArea) {
        super(textArea);
        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        int width = getMaximumSize().width;
        int height = getMinimumSize().height;
        setMinimumSize(new Dimension(width, height));
        setBorder(BorderFactory.createLoweredBevelBorder());
    }

}