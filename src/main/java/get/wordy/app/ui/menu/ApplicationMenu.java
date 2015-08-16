package get.wordy.app.ui.menu;

import get.wordy.app.impl.ServiceHolder;
import get.wordy.app.resources.R;
import get.wordy.app.resources.Resources;
import get.wordy.app.settings.Language;
import get.wordy.app.settings.LanguageManager;
import get.wordy.app.settings.FontName;
import get.wordy.app.settings.FontUtil;
import get.wordy.app.ui.IReloadable;
import get.wordy.app.ui.UISettings;
import get.wordy.app.ui.WordyMediator;
import get.wordy.app.ui.event.FontChangedEvent;
import get.wordy.app.ui.event.LocalizationChangedEvent;
import get.wordy.app.ui.localizable.Localized;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class ApplicationMenu extends JMenuBar implements IReloadable {

    private ActionFactory actionFactory;
    private WordyMediator mediator;
    private Resources resources;
    private UISettings settings;

    private CustomMenu actionsMenu;
    private CustomMenu settingsMenu;
    private CustomMenu helpMenu;

    private CustomMenu fontItem;
    private CustomMenu fontNameItem;
    private CustomMenu fontSizeItem;
    private CustomMenu languageItem;

    private List<IReloadable> menuItemsList = new ArrayList<IReloadable>();

    public ApplicationMenu(ActionFactory actions, WordyMediator mediator, Resources resources, UISettings settings) {
        this.actionFactory = actions;
        this.mediator = mediator;
        this.resources = resources;
        this.settings = settings;

        actionsMenu = new CustomMenu(Localized.MENU_ACTIONS);
        settingsMenu = new CustomMenu(Localized.MENU_SETTINGS);
        helpMenu = new CustomMenu(Localized.MENU_HELP);

        createActionsMenu();
        createSettingsMenu();
        createHelpMenu();

        menuItemsList.addAll(Arrays.asList(actionsMenu, settingsMenu, helpMenu));

        add(actionsMenu);
        add(settingsMenu);
        add(helpMenu);
    }

    private void createActionsMenu() {
        // Actions
        CustomMenuItem startItem = new CustomMenuItem(Localized.START_EXERCISE, actionFactory.newStartExerciseAction());
        CustomMenuItem saveDictionaryToFileItem = new CustomMenuItem(Localized.SAVE_DICTIONARY_TO_FILE, actionFactory.newExportDictionaryAction());
        CustomMenuItem createDictionaryItem = new CustomMenuItem(Localized.CREATE_DICTIONARY, actionFactory.newCreateDictionaryAction());
        CustomMenuItem createCard = new CustomMenuItem(Localized.CREATE_CARD, actionFactory.newCardAction());
        createCard.setIcon(new ImageIcon(R.icon.CREATE_CARD_SMALL));

        CustomMenuItem editCard = new CustomMenuItem(Localized.EDIT_CARD, actionFactory.newEditCardAction());
        editCard.setIcon(new ImageIcon(R.icon.EDIT_CARD_SMALL));
        CustomMenuItem deleteCard = new CustomMenuItem(Localized.DELETE_CARD, actionFactory.newDeleteCardAction());
        deleteCard.setIcon(new ImageIcon(R.icon.DELETE_CARD_SMALL));
        CustomMenuItem copyToAnotherDictionaryItem = new CustomMenuItem(Localized.COPY_TO_ANOTHER_DICTIONARY, actionFactory.newCopyToDictionaryAction());
        CustomMenuItem statisticsItem = new CustomMenuItem(Localized.STATISTICS, actionFactory.newShowScoreDialogAction());
        CustomMenuItem exitItem = new CustomMenuItem(Localized.EXIT, actionFactory.newExitAction());
        exitItem.setIcon(new ImageIcon(R.icon.EXIT_SMALL));

        startItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        createCard.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
        editCard.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK));
        deleteCard.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_MASK));
        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));

        actionsMenu.add(startItem);
        actionsMenu.addSeparator();
        actionsMenu.add(saveDictionaryToFileItem);
        actionsMenu.add(createDictionaryItem);
        actionsMenu.addSeparator();
        actionsMenu.add(createCard);
        actionsMenu.add(editCard);
        actionsMenu.add(deleteCard);
        actionsMenu.addSeparator();
        actionsMenu.add(copyToAnotherDictionaryItem);
        actionsMenu.addSeparator();
        actionsMenu.add(statisticsItem);
        actionsMenu.add(exitItem);

        menuItemsList.addAll(Arrays.asList(startItem,
                saveDictionaryToFileItem, createDictionaryItem, createCard,
                editCard, deleteCard, copyToAnotherDictionaryItem,
                statisticsItem, exitItem));

        copyToAnotherDictionaryItem.setEnabled(false);
        saveDictionaryToFileItem.setEnabled(false);
    }

    private void createSettingsMenu() {
        // Settings -> Font
        fontItem = new CustomMenu(Localized.FONT);
        menuItemsList.add(fontItem);

        // Settings -> Font -> Color
        CustomMenuItem fontColorItem = new CustomMenuItem(Localized.FONT_COLOR_BUTTON, new ChangeFontColorAction());
        fontColorItem.setEnabled(false);
        fontItem.add(fontColorItem);
        menuItemsList.add(fontColorItem);

        // Settings -> Font -> Name
        fontNameItem = new CustomMenu(Localized.FONT_NAME_BUTTON);
        FontName[] fontName = FontName.values();
        for (int i = 0; i < fontName.length; i++) {
            final String name = fontName[i].getName();
            CustomMenuItem item = new CustomMenuItem(name, false);
            item.addActionListener(new ChangeFontNameAction());
            fontNameItem.add(item);
            menuItemsList.add(item);
        }
        fontItem.add(fontNameItem);
        menuItemsList.add(fontNameItem);

        // Settings -> Font -> Size
        fontSizeItem = new CustomMenu(Localized.FONT_SIZE_BUTTON);
        for (int i = FontUtil.MIN_SIZE; i <= FontUtil.MAX_SIZE; i += 1) {
            CustomMenuItem item = new CustomMenuItem(String.valueOf(i), false);
            item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JMenuItem itm = (JMenuItem) e.getSource();
                    Integer newSize = Integer.valueOf(itm.getText());
                    Font font;
                    try {
                        font = FontUtil.changeSize(settings.getFont(), newSize);
                    } catch (FontUtil.WrongFontSizeException ex) {
                        font = ex.getResult();
                    }
                    ServiceHolder.getSettingManager().saveFont(font);
                    mediator.fireEvent(FontChangedEvent.class.getSimpleName());
                }
            });
            fontSizeItem.add(item);
            menuItemsList.add(item);
        }
        fontItem.add(fontSizeItem);
        menuItemsList.add(fontSizeItem);

        // Settings -> Language
        languageItem = new CustomMenu(Localized.LANGUAGE);
        for (Language lang : Language.values()) {
            final String displayLanguage = lang.getDisplayLanguage();
            CustomMenuItem item = new CustomMenuItem(displayLanguage, false);
            item.addActionListener(new ChangeLanguageAction());
            item.setActionCommand(lang.name());
            languageItem.add(item);
            menuItemsList.add(item);
        }
        menuItemsList.add(languageItem);

        // Settings -> Options
        CustomMenuItem dictionariesItem = new CustomMenuItem(Localized.DICTIONARIES, actionFactory.newShowDictionaryListAction());
        CustomMenuItem optionsItem = new CustomMenuItem(Localized.OPTIONS, actionFactory.newShowOptionsAction());
        optionsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
        menuItemsList.addAll(Arrays.asList(dictionariesItem, optionsItem));

        settingsMenu.add(fontItem);
        settingsMenu.add(languageItem);
        settingsMenu.addSeparator();
        settingsMenu.add(dictionariesItem);
        settingsMenu.add(optionsItem);
    }

    private void createHelpMenu() {
        // Help -> Google
        CustomMenuItem translateInGoogleItem = new CustomMenuItem(Localized.TRANSLATE_IN_GOOGLE, actionFactory.newTranslateInGoogleAction());
        translateInGoogleItem.setToolTipText(Localized.TOOLTIP_TRANSLATE_IN_GOOGLE);
        translateInGoogleItem.setIcon(new ImageIcon(R.icon.TRANSLATE_IN_GOOGLE_SMALL));
        translateInGoogleItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_MASK));

        helpMenu.add(translateInGoogleItem);

        translateInGoogleItem.setEnabled(false);
    }

    @Override
    public void reloadText() {
        for (IReloadable item : menuItemsList) {
            item.reloadText();
        }
    }

    @Override
    public void reloadFont() {
        for (IReloadable item : menuItemsList) {
            item.reloadFont();
        }
    }

    private class CustomMenu extends JMenu implements IReloadable {

        private String key;

        public CustomMenu(String label) {
            this.key = label;
            customize();
        }

        private void customize() {
            setText(resources.translate(key));
            setFont(settings.getFont());
        }

        @Override
        public void reloadText() {
            setText(resources.translate(key));
        }

        @Override
        public void reloadFont() {
            setFont(settings.getFont());
        }

    }

    private class CustomMenuItem extends JMenuItem implements IReloadable {

        private String key;
        private boolean notLocalize = false;

        public CustomMenuItem(String label, Action action) {
            super(action);
            this.key = label;
            customize();
        }

        public CustomMenuItem(String label) {
            this.key = label;
            customize();
        }

        public CustomMenuItem(String menu, boolean localized) {
            this.key = menu;
            this.notLocalize = !localized;
            customize();
        }

        private void customize() {
            setTxt();
            setFont(settings.getFont());
        }

        @Override
        public void reloadText() {
            if (notLocalize) {
                return;
            }
            setTxt();
        }

        private void setTxt() {
            setText(resources.translate(key));
        }

        @Override
        public void reloadFont() {
            setFont(settings.getFont());
        }

    }

    public class ChangeFontNameAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            JMenuItem itm = (JMenuItem) e.getSource();
            String newName = itm.getText();
            Font font = settings.getFont();
            font = FontUtil.changeName(font, newName);
            ServiceHolder.getSettingManager().saveFont(font);
            mediator.fireEvent(FontChangedEvent.class.getSimpleName());
        }

    }

    public class ChangeLanguageAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            String key = actionEvent.getActionCommand();
            Language language = Language.valueOf(key);

            LanguageManager languageManager = new LanguageManager(language);
            ResourceBundle messagesBundle = languageManager.getMessageBundle();
            Resources.getResources().setMessageBundle(messagesBundle);

            ServiceHolder.getSettingManager().saveLanguage(language);
            mediator.fireEvent(LocalizationChangedEvent.class.getSimpleName());

            mediator.updateTable();
        }
    }

    public class ChangeFontColorAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            // TODO: implement
        }
    }

}