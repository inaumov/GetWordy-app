package get.wordy.app.ui.frame.card;

import get.wordy.app.resources.R;
import get.wordy.app.resources.Resources;
import get.wordy.app.ui.IReloadable;
import get.wordy.app.ui.UISettings;
import get.wordy.app.ui.WordyMediator;
import get.wordy.app.ui.action.listener.WordFieldListenerAdapter;
import get.wordy.app.ui.component.ComboBox;
import get.wordy.app.ui.component.ExampleTextArea;
import get.wordy.app.ui.component.Label;
import get.wordy.app.ui.event.*;
import get.wordy.app.ui.localizable.Localized;
import get.wordy.app.ui.util.ComboListHelper.ComboList;
import get.wordy.core.bean.wrapper.CardStatus;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.Document;
import java.awt.*;
import java.util.ArrayList;

abstract class BaseCardWindow extends JFrame implements IReloadable, EventSubscriber, Localized {

    protected WordyMediator mediator;
    protected Resources resources;
    protected UISettings settings;

    protected JPanel displayPanel;

    protected Label wordLabel;
    protected CardTextField wordField;

    protected Label statusLabel;
    protected ComboBox statusComboBox;

    protected Label definitionsLabel;
    protected ExampleTextArea exampleTextArea;

    protected ArrayList<IReloadable> initLabels = new ArrayList<IReloadable>();
    protected ArrayList<IReloadable> initFields = new ArrayList<IReloadable>();

    public BaseCardWindow(WordyMediator mediator) {
        this.mediator = mediator;
        this.resources = mediator.getResources();
        this.settings = mediator.getUiSettings();

        init();
        subscribeForEvent(mediator);
    }

    void init() {
        createWordLabel();
        createWordField();
        createStatusLabel();
        createStatusComboBox();
        createDefinitionLabel();
        createExampleTextArea();
        createAdditionalComponents();

        createLayout();
        add(displayPanel);

        setSize(R.dimension.MAIN_FRAME_WIDTH, R.dimension.MAIN_FRAME_HEIGHT);
        setIconImage(new ImageIcon(R.icon.MAIN_ICON).getImage());
        setResizable(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    void createWordLabel() {
        wordLabel = new Label(resources, settings, MSG_LABEL_WORD);
        initLabels.add(wordLabel);
    }

    void createWordField() {
        wordField = new CardTextField(32);
        wordField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        wordField.addMouseListener(getWordFieldListener());
        wordField.addFocusListener(getWordFieldListener());
        wordField.addKeyListener(getWordFieldListener());
        initFields.add(wordField);
    }

    void createStatusLabel() {
        statusLabel = new Label(resources, settings, MSG_LABEL_CARD_STATUS);
        initLabels.add(statusLabel);
    }

    void createStatusComboBox() {
        statusComboBox = new ComboBox(resources, settings, ComboList.CARD_STATUS);
        initLabels.add(statusComboBox);
    }

    void createDefinitionLabel() {
        definitionsLabel = new Label(resources, settings, MSG_LABEL_DEFINITIONS);
        initLabels.add(definitionsLabel);
    }

    void createExampleTextArea() {
        exampleTextArea = new ExampleTextArea(resources, settings, false);
        initFields.add(exampleTextArea);
    }

    protected Box createLayout() {
        displayPanel = new JPanel();
        displayPanel.setLayout(new BorderLayout());
        Border border = BorderFactory.createEmptyBorder(7, 7, 9, 7);
        displayPanel.setBorder(border);

        Box mainBox = Box.createVerticalBox();
        displayPanel.add(mainBox, BorderLayout.NORTH);
        displayPanel.add(exampleTextArea.getScrolledArea(), BorderLayout.CENTER);
        return mainBox;
    }

    protected abstract void createAdditionalComponents();

    protected abstract WordFieldListenerAdapter getWordFieldListener();

    protected JPanel getWordLabel() {
        return wordLabel;
    }

    protected JTextField getWordField() {
        return wordField;
    }

    protected Box createStatusBox() {
        Box statusBox = Box.createHorizontalBox();
        statusBox.add(Box.createHorizontalStrut(32));
        statusBox.add(statusLabel);
        statusBox.add(Box.createHorizontalStrut(7));
        statusBox.add(statusComboBox);
        statusComboBox.setSelectedIndex(CardStatus.defaultIndex());
        return statusBox;
    }

    protected Box createButtonsBox(AbstractButton... cmp) {
        Box buttonsBox = Box.createHorizontalBox();
        buttonsBox.add(Box.createHorizontalGlue());
        buttonsBox.add(cmp[0]);
        buttonsBox.add(Box.createHorizontalStrut(7));
        buttonsBox.add(cmp[1]);
        return buttonsBox;
    }

    protected Box createDefinitionBox() {
        Box bottomBox = Box.createHorizontalBox();
        bottomBox.add(definitionsLabel);
        return bottomBox;
    }

    @Override
    public void reloadText() {
        for (IReloadable r : initLabels)
            r.reloadText();
    }

    @Override
    public void reloadFont() {
        for (IReloadable r : initLabels)
            r.reloadFont();
        for (IReloadable r : initFields)
            r.reloadFont();
    }

    public void clear() {
        wordField.setText(MSG_EMPTY_STRING);
        exampleTextArea.setText(MSG_EMPTY_STRING);
    }

    @Override
    public void subscribeForEvent(Observable observable) {
        observable.attach(this, new LocalizationChangedEvent(this));
        observable.attach(this, new FontChangedEvent(this));
    }

    public void setWord(String word) {
        wordField.setText(word);
    }

    public void setStatus(int index) {
        statusComboBox.setSelectedIndex(index);
    }

    public void setDocument(Document document) {
        exampleTextArea.setDocument(document);
    }

    protected class CardTextField extends JTextField implements IReloadable {

        protected CardTextField(int columns) {
            super(columns);
            Border loweredBevelBorder = BorderFactory.createLoweredBevelBorder();
            Border emptyBorder = BorderFactory.createEmptyBorder(1, 2, 4, 1);
            Border wordTextFieldCompoundBorder = BorderFactory.createCompoundBorder(loweredBevelBorder, emptyBorder);
            setBorder(wordTextFieldCompoundBorder);
            setFont(settings.getFont());
            int width = this.getMaximumSize().width;
            int height = this.getPreferredSize().height;
            setPreferredSize(new Dimension(200, height));
        }

        @Override
        public void reloadText() {
            // NOTE not supported operation!
        }

        @Override
        public void reloadFont() {
            setFont(settings.getFont());
        }

    }

}