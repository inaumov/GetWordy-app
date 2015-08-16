package get.wordy.app.ui.dialog.options;

import get.wordy.app.resources.R;
import get.wordy.app.resources.Resources;
import get.wordy.app.settings.Setting;
import get.wordy.app.settings.SettingsManager;
import get.wordy.app.settings.FontUtil;
import get.wordy.app.ui.IReloadable;
import get.wordy.app.ui.MainWindow;
import get.wordy.app.ui.UISettings;
import get.wordy.app.ui.WordyMediator;
import get.wordy.app.ui.component.Button;
import get.wordy.app.ui.component.*;
import get.wordy.app.ui.component.Label;
import get.wordy.app.ui.event.EventSubscriber;
import get.wordy.app.ui.event.FontChangedEvent;
import get.wordy.app.ui.event.LocalizationChangedEvent;
import get.wordy.app.ui.event.Observable;
import get.wordy.app.ui.localizable.Localized;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class OptionsDialog extends JDialog implements IReloadable, EventSubscriber, Localized {

    private static final int MARGIN_0 = 0;
    private static final int MARGIN_5 = 5;
    private static final int MARGIN_10 = 10;
    private static final int MARGIN_20 = 20;

    private static final int FONT_DIFF = 2;

    private final OptionsDialogPresenter presenter;

    private Resources resources;
    private UISettings settings;

    private JPanel displayPanel;

    private JTabbedPane tabs;

    private List<CheckBox> settingsCheckBox;
    private List<RadioButton> animationOrSoundRadioButtons;

    private Label cardsInExerciseLabel;
    private SplitLabel correctAnswersLabel;
    private CustomSpinner cardsInExerciseSpinner;
    private CustomSpinner correctAnswersSpinner;
    private CustomSpinner cardsToLearnSpinner;
    private TitledBorder titledBorder;
    private Label cardsToLearnLabel;

    private CheckBox scheduleCheckBox;
    private Label everyLabel;
    private CustomSpinner workScheduleSpinner;
    private Label minutesLabel;

    private Button saveButton, cancelButton;

    private List<IReloadable> components = new ArrayList<IReloadable>();

    public OptionsDialog(MainWindow mainWindow, WordyMediator mediator) {
        super(mainWindow, true);
        this.resources = mediator.getResources();
        this.settings = mediator.getUiSettings();

        init();

        presenter = new OptionsDialogPresenter(mediator, this);
        presenter.setCallback(new OptionsDialogCallback());
        presenter.restoreSettings();

        subscribeForEvent(mediator);
    }

    private void init() {

        tabs = new JTabbedPane();

        // TAB GENERAL
        settingsCheckBox = Arrays.asList(
                new CheckBox(resources, settings, MSG_CHECKBOX_DISPLAY_DIALOG_BOX),
                new CheckBox(resources, settings, MSG_CHECKBOX_HIDE_MINIMIZED),
                new CheckBox(resources, settings, MSG_CHECKBOX_AUTOMATIC_RUN)
        );
        components.addAll(settingsCheckBox);

        titledBorder = new TitledBorder(resources.translate(MSG_LABEL_ANSWER_SIGNAL));
        titledBorder.setTitleFont(getFontForTitle());
        animationOrSoundRadioButtons = Arrays.asList(
                new RadioButton(resources, settings, MSG_IMAGE_WITH_SOUND),
                new RadioButton(resources, settings, MSG_ANIMATED_IMAGE),
                new RadioButton(resources, settings, MSG_SOUND));
        ButtonGroup generalGroup = new ButtonGroup();
        for (RadioButton radioButton : animationOrSoundRadioButtons) {
            generalGroup.add(radioButton);
        }
        components.addAll(animationOrSoundRadioButtons);

        // TAB EXERCISE
        cardsInExerciseLabel = new Label(resources, settings, MSG_LABEL_CARDS_IN_EXERCISE);
        components.add(cardsInExerciseLabel);

        correctAnswersLabel = new SplitLabel(resources, settings, MSG_LABEL_CORRECT_ANSWERS, 2);
        components.add(correctAnswersLabel);

        cardsToLearnLabel = new Label(resources, settings, MSG_LABEL_AMOUNT_OF_CARDS_TO_LEARN);
        components.add(cardsToLearnLabel);

        List<CustomSpinner> spinners = Arrays.asList(
                cardsInExerciseSpinner = new CustomSpinner(1, 1, 100, 1),
                correctAnswersSpinner = new CustomSpinner(1, 1, 100, 1),
                cardsToLearnSpinner = new CustomSpinner(1, 1, 500, 1));
        components.addAll(spinners);

        // TAB SCHEDULE
        scheduleCheckBox = new CheckBox(resources, settings, MSG_CHECKBOX_SCHEDULING);
        components.add(scheduleCheckBox);

        everyLabel = new Label(resources, settings, MSG_LABEL_EVERY);
        minutesLabel = new Label(resources, settings, MSG_LABEL_MINUTES);
        components.add(everyLabel);
        components.add(minutesLabel);

        workScheduleSpinner = new CustomSpinner(15, 15, 990, 15);
        components.add(workScheduleSpinner);

        // CONFIRM BUTTONS
        SettingsAction settingsAction = new SettingsAction();
        saveButton = new Button(settingsAction, resources, settings, MSG_BUTTON_OPTIONS_SAVE);
        cancelButton = new Button(settingsAction, resources, settings, MSG_BUTTON_OPTIONS_CANCEL);
        components.add(saveButton);
        components.add(cancelButton);

        saveButton.setActionCommand(MSG_BUTTON_OPTIONS_SAVE);
        cancelButton.setActionCommand(MSG_BUTTON_OPTIONS_CANCEL);

        createLayout();
        setupListeners();

        setSize(R.dimension.OPTIONS_DIALOG_WIDTH, R.dimension.OPTIONS_DIALOG_HEIGHT);
        setIconImage(new ImageIcon(R.icon.MAIN_ICON).getImage());
        setTitle(resources.translate(MSG_OPTIONS_WINDOW));
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void createLayout() {
        displayPanel = new JPanel();
        displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.PAGE_AXIS));

        // GENERAL TAB
        JPanel generalTab = new JPanel();
        generalTab.setLayout(new BoxLayout(generalTab, BoxLayout.PAGE_AXIS));
        generalTab.setBorder(BorderFactory.createEmptyBorder(MARGIN_10, MARGIN_10, MARGIN_10, MARGIN_10));

        Box settingsHBox = Box.createHorizontalBox();
        Box settingsVBox = Box.createVerticalBox();
        for (CheckBox checkBox : settingsCheckBox) {
            settingsVBox.add(checkBox);
        }
        settingsHBox.add(settingsVBox);
        settingsHBox.add(Box.createHorizontalGlue());

        // Setup sound or animation
        Box animationOrSoundHBox = Box.createHorizontalBox();
        animationOrSoundHBox.setBorder(titledBorder);
        Box animationOrSoundVBox = Box.createVerticalBox();
        for (RadioButton radioButton : animationOrSoundRadioButtons) {
            animationOrSoundVBox.add(radioButton);
        }
        animationOrSoundHBox.add(animationOrSoundVBox);
        animationOrSoundHBox.add(Box.createHorizontalGlue());

        generalTab.add(settingsHBox);
        generalTab.add(Box.createVerticalStrut(MARGIN_10));
        generalTab.add(animationOrSoundHBox);


        // EXERCISE TAB
        JPanel exerciseTab = new JPanel(new BorderLayout());
        exerciseTab.setBorder(BorderFactory.createEmptyBorder(MARGIN_10, MARGIN_10, MARGIN_10, MARGIN_10));

        Box exerciseVBox = Box.createVerticalBox();
        exerciseVBox.add(getWrappedSpinnerComponent(cardsInExerciseLabel, cardsInExerciseSpinner, null));
        exerciseVBox.add(Box.createVerticalStrut(MARGIN_10));
        exerciseVBox.add(getWrappedSpinnerComponent(correctAnswersLabel, correctAnswersSpinner, null));
        exerciseVBox.add(Box.createVerticalStrut(MARGIN_10));
        exerciseVBox.add(getWrappedSpinnerComponent(cardsToLearnLabel, cardsToLearnSpinner, null));

        exerciseTab.add(exerciseVBox, BorderLayout.NORTH);


        // SCHEDULE TAB
        JPanel scheduleTab = new JPanel(new BorderLayout());
        scheduleTab.setBorder(BorderFactory.createEmptyBorder(MARGIN_10, MARGIN_10, MARGIN_10, MARGIN_10));

        Box scheduleVBox = Box.createVerticalBox();

        Box scheduleHBox = Box.createHorizontalBox();
        scheduleHBox.add(scheduleCheckBox);
        scheduleHBox.add(Box.createHorizontalGlue());

        scheduleVBox.add(scheduleHBox);
        scheduleVBox.add(Box.createVerticalStrut(MARGIN_10));
        scheduleVBox.add(getWrappedSpinnerComponent(everyLabel, workScheduleSpinner, minutesLabel));

        scheduleTab.add(scheduleVBox, BorderLayout.NORTH);

        // BUTTONS SECTION
        Box buttonBox = Box.createHorizontalBox();
        buttonBox.add(Box.createHorizontalGlue());
        buttonBox.add(saveButton);
        buttonBox.add(Box.createHorizontalStrut(MARGIN_10));
        buttonBox.add(cancelButton);

        // TABs initialization
        tabs.setFocusable(false);
        tabs.addTab(resources.translate(MSG_OPTIONS_WINDOW_TAB_GENERAL), generalTab);
        tabs.addTab(resources.translate(MSG_OPTIONS_WINDOW_TAB_EXERCISE), exerciseTab);
        tabs.addTab(resources.translate(MSG_OPTIONS_WINDOW_TAB_SCHEDULE), scheduleTab);

        displayPanel.add(tabs);
        displayPanel.add(buttonBox);

        add(displayPanel);
    }

    private void setupListeners() {
        scheduleCheckBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                AbstractButton abstractButton = (AbstractButton) e.getSource();
                boolean selected = abstractButton.getModel().isSelected();
                workScheduleSpinner.setEnabled(selected);
            }
        });
    }

    private Box getWrappedSpinnerComponent(JComponent leftComp, CustomSpinner spinner, JComponent rightComp) {

        spinner.setMaximumSize(spinner.getPreferredSize());

        Box box = Box.createHorizontalBox();
        box.add(leftComp);
        if (rightComp != null) {
            box.add(Box.createHorizontalStrut(MARGIN_10));
        } else {
            box.add(Box.createHorizontalGlue());
        }
        box.add(spinner);
        if (rightComp != null) {
            box.add(Box.createHorizontalStrut(MARGIN_10));
            box.add(rightComp);
            box.add(Box.createHorizontalGlue());
        }

        return box;
    }

    private Font getFontForTitle() {
        Font font;
        try {
            font = FontUtil.smaller(settings.getFont(), FONT_DIFF);
        } catch (FontUtil.WrongFontSizeException e) {
            font = e.getResult();
        }
        return font;
    }

    public void useScheduleTab() {
        this.tabs.setSelectedIndex(tabs.getTabCount() - 1);
    }

    public void hideWindow() {
        setVisible(false);
    }

    private class CustomSpinner extends JSpinner implements IReloadable {

        private JFormattedTextField tf;

        public CustomSpinner(int value, int minimum, int maximum, int stepSize) {
            super(new SpinnerNumberModel(value, minimum, maximum, stepSize));
            tf = ((JSpinner.DefaultEditor) getEditor()).getTextField();
            tf.setEditable(false);
            tf.setFont(settings.getFont());
            tf.setBackground(Color.WHITE);
            tf.setDisabledTextColor(SettingsManager.findOppositeColor(Color.BLACK));
        }

        @Override
        public void reloadText() {
            // nothing to do
        }

        @Override
        public void reloadFont() {
            tf.setFont(settings.getFont());
        }

    }

    @Override
    public void reloadText() {
        // TITLE OF FRAME
        setTitle(resources.translate(MSG_OPTIONS_WINDOW));

        // UPDATE NAME OF TABs
        tabs.setTitleAt(0, resources.translate(MSG_OPTIONS_WINDOW_TAB_GENERAL));
        tabs.setTitleAt(1, resources.translate(MSG_OPTIONS_WINDOW_TAB_EXERCISE));
        tabs.setTitleAt(2, resources.translate(MSG_OPTIONS_WINDOW_TAB_SCHEDULE));

        // GLOBAL UPDATE OF TAB COMPONENTS
        for (IReloadable r : components)
            r.reloadText();

        titledBorder.setTitle(resources.translate(MSG_LABEL_ANSWER_SIGNAL));
    }

    @Override
    public void reloadFont() {
        // UPDATE NAME OF TABs
        for (Component tab : tabs.getComponents())
            tab.setFont(settings.getFont());

        // GLOBAL UPDATE OF TAB COMPONENTS
        for (IReloadable r : components)
            r.reloadFont();

        titledBorder.setTitleFont(getFontForTitle());
        setSize(R.dimension.OPTIONS_DIALOG_WIDTH, R.dimension.OPTIONS_DIALOG_HEIGHT);
    }

    @Override
    public void subscribeForEvent(Observable observable) {
        observable.attach(this, new LocalizationChangedEvent(this));
        observable.attach(this, new FontChangedEvent(this));
    }

    private class OptionsDialogCallback implements OptionsDialogPresenter.ISettingCallback {

        @Override
        public Map onSave() {
            Map values = new HashMap();
            // General tab
            values.put(Setting.DISPLAY_ON_STARTUP, settingsCheckBox.get(0).isSelected());
            values.put(Setting.HIDE_WHEN_MINIMIZE, settingsCheckBox.get(1).isSelected());
            values.put(Setting.AUTO_RUN_ON_STARTUP, settingsCheckBox.get(2).isSelected());
            values.put(Setting.ANIMATION_AND_SOUND, animationOrSoundRadioButtons.get(0).isSelected());
            values.put(Setting.ANIMATION, animationOrSoundRadioButtons.get(1).isSelected());
            values.put(Setting.SOUND, animationOrSoundRadioButtons.get(2).isSelected());

            // Exercise tab
            values.put(Setting.NUMBER_OF_CARDS_IN_EXERCISE, ((SpinnerNumberModel) cardsInExerciseSpinner.getModel()).getNumber());
            values.put(Setting.NUMBER_OF_REPETITIONS, ((SpinnerNumberModel) correctAnswersSpinner.getModel()).getNumber());
            values.put(Setting.NUMBER_OF_CARDS_TO_LEARN, ((SpinnerNumberModel) cardsToLearnSpinner.getModel()).getNumber());

            // Schedule tab
            values.put(Setting.SCHEDULER_ENABLED, scheduleCheckBox.isSelected());
            values.put(Setting.SCHEDULER_TIME_FREQUENCY, ((SpinnerNumberModel) workScheduleSpinner.getModel()).getNumber());

            return values;
        }

        @Override
        public void onRestore(Properties values) {
            // General tab
            settingsCheckBox.get(0).setSelected(toBoolean(values, Setting.DISPLAY_ON_STARTUP));
            settingsCheckBox.get(1).setSelected(toBoolean(values, Setting.HIDE_WHEN_MINIMIZE));
            settingsCheckBox.get(2).setSelected(toBoolean(values, Setting.AUTO_RUN_ON_STARTUP));
            animationOrSoundRadioButtons.get(0).setSelected(toBoolean(values, Setting.ANIMATION_AND_SOUND));
            animationOrSoundRadioButtons.get(1).setSelected(toBoolean(values, Setting.ANIMATION));
            animationOrSoundRadioButtons.get(2).setSelected(toBoolean(values, Setting.SOUND));

            // Exercise tab
            cardsInExerciseSpinner.setValue(toNumber(values, Setting.NUMBER_OF_CARDS_IN_EXERCISE));
            correctAnswersSpinner.setValue(toNumber(values, Setting.NUMBER_OF_REPETITIONS));
            cardsToLearnSpinner.setValue(toNumber(values, Setting.NUMBER_OF_CARDS_TO_LEARN));

            // Schedule tab
            scheduleCheckBox.setSelected(toBoolean(values, Setting.SCHEDULER_ENABLED));
            workScheduleSpinner.setEnabled(scheduleCheckBox.isSelected());
            workScheduleSpinner.setValue(toNumber(values, Setting.SCHEDULER_TIME_FREQUENCY));
        }

        private boolean toBoolean(Properties values, Setting key) {
            Object o = values.getProperty(key.name());
            return Boolean.valueOf(o.toString());
        }

        private Number toNumber(Properties values, Setting key) {
            Object o = values.getProperty(key.name());
            return Integer.valueOf(o.toString());
        }

        @Override
        public void onCancel() {
            setVisible(false);
            dispose();
        }

    }

    public class SettingsAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent action) {
            String source = action.getActionCommand();
            if (source == Localized.MSG_BUTTON_OPTIONS_SAVE) {
                presenter.saveSettings();
            } else if (source == Localized.MSG_BUTTON_OPTIONS_CANCEL) {
                presenter.cancel();
            }
        }

    }

}