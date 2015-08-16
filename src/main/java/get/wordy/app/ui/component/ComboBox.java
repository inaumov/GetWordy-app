package get.wordy.app.ui.component;

import get.wordy.app.resources.Resources;
import get.wordy.app.settings.FontUtil;
import get.wordy.app.ui.IReloadable;
import get.wordy.app.ui.UISettings;
import get.wordy.app.ui.util.ComboListHelper;
import get.wordy.app.ui.util.ComboListHelper.ComboList;

import javax.swing.*;
import java.awt.*;

public class ComboBox extends JComboBox implements IReloadable {

    private static final int FONT_DIFF = 1;

    private Resources resources;
    private UISettings settings;
    private ComboList comboList;

    public ComboBox(Resources resources, UISettings settings, ComboList comboList) {
        this.resources = resources;
        this.settings = settings;
        this.comboList = comboList;

        reloadFont();
        setFocusable(false);

        Object[] listItems = ComboListHelper.getComboList(resources, comboList);
        for (Object listItem : listItems) {
            addItem(listItem);
        }
    }

    @Override
    public void reloadText() {
        ComboBox newCombo = new ComboBox(resources, settings, comboList);
        int item = super.getSelectedIndex();
        super.setModel(newCombo.getModel());
        super.setSelectedIndex(item);
    }

    @Override
    public void reloadFont() {
        Font font;
        try {
            font = FontUtil.smaller(settings.getFont(), FONT_DIFF);
        } catch (FontUtil.WrongFontSizeException e) {
            font = e.getResult();
        }
        setFont(font);
    }

}