package get.wordy.app.ui.component;

import get.wordy.app.ui.IReloadable;
import get.wordy.app.ui.UISettings;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class SearchField extends JTextField implements IReloadable {

    private UISettings settings;

    public SearchField(UISettings settings, SearchCallback searchCallback) {
        this.settings = settings;
        setBorder(createBorder());
        setFont(settings.getFont());
        getDocument().addDocumentListener(searchCallback);
    }

    private CompoundBorder createBorder() {
        Border emptyBorder = BorderFactory.createEmptyBorder(1, 2, 1, 2);
        Border insideBorder = BorderFactory.createLoweredBevelBorder();
        Border outsideBorder = BorderFactory.createRaisedBevelBorder();
        Border compoundBorder = new CompoundBorder(outsideBorder, insideBorder);
        return new CompoundBorder(compoundBorder, emptyBorder);
    }

    @Override
    public void reloadText() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void reloadFont() {
        setFont(settings.getFont());
    }

    public static abstract class SearchCallback implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            onSearch(e);
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            onSearch(e);
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
        }

        public abstract void onSearch(DocumentEvent e);

    }

}