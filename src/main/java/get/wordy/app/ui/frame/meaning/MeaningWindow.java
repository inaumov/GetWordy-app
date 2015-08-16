package get.wordy.app.ui.frame.meaning;

import get.wordy.app.resources.R;
import get.wordy.app.resources.Resources;
import get.wordy.app.settings.FontUtil;
import get.wordy.app.ui.IReloadable;
import get.wordy.app.ui.UISettings;
import get.wordy.app.ui.WordyMediator;
import get.wordy.app.ui.component.Button;
import get.wordy.app.ui.component.ComboBox;
import get.wordy.app.ui.component.Label;
import get.wordy.app.ui.event.EventSubscriber;
import get.wordy.app.ui.event.FontChangedEvent;
import get.wordy.app.ui.event.LocalizationChangedEvent;
import get.wordy.app.ui.event.Observable;
import get.wordy.app.ui.localizable.Localized;
import get.wordy.app.ui.util.ComboListHelper;
import get.wordy.app.ui.util.ComboListHelper.ComboList;
import get.wordy.core.bean.Card;
import get.wordy.core.bean.wrapper.GramUnit;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

import static get.wordy.app.settings.SettingsManager.findOppositeColor;

public class MeaningWindow extends JFrame implements IReloadable, EventSubscriber, Localized {

    private static final int MARGIN_10 = 10;
    private static final int MARGIN_20 = 20;
    private static final int FONT_ODDS = 1;
    private static final int EXAMPLES_ROWS_COUNT = 5;

    private Resources resources;
    private UISettings settings;
    private JPanel displayPanel;
    private ComboBox gramUnitCmbx;
    private Button addMoreButton;
    private Button saveButton;
    private Button cancelButton;

    private Map<GramUnit, DefinitionTextArea> definitionTextAreasMap = new HashMap<>();
    private Map<GramUnit, List<MeaningPanel>> meaningPanelsMap = new HashMap<>();

    private Map<GramUnit, GramUnitLabel> gramUnitLabels = new HashMap<>();
    private List<Label> definitionLabels = new ArrayList<Label>();

    private MeaningWindowPresenter presenter;
    private GramUnit selectedGramUnit;
    private SaveValues saveValues = new SaveValues();
    private JPanel meaningsListPanel;

    public MeaningWindow(WordyMediator mediator, Card card, boolean isNew) {

        this.resources = mediator.getResources();
        this.settings = mediator.getUiSettings();

        presenter = new MeaningWindowPresenter(card, mediator, this);
        init();
        loadMeaningsListPanel(isNew);
        subscribeForEvent(mediator);
    }

    private void init() {
        gramUnitCmbx = new ComboBox(resources, settings, ComboList.PART_OF_SPEECH);
        gramUnitCmbx.addActionListener(new PartOfSpeechComboBoxListener());

        meaningsListPanel = new JPanel();
        meaningsListPanel.setLayout(new BoxLayout(meaningsListPanel, BoxLayout.Y_AXIS));
        Border border = new EmptyBorder(0, 0, 0, 50);
        meaningsListPanel.setBorder(border);

        addMoreButton = new Button(new AddMeaning(), resources, settings, MSG_BUTTON_ADD_MEANING);
        addMoreButton.setIcon(new ImageIcon(R.icon.ADD_MEANING_LARGE));

        saveButton = new Button(new SaveMeanings(), resources, settings, MSG_BUTTON_OPTIONS_SAVE);
        cancelButton = new Button(new CancelAction(), resources, settings, MSG_BUTTON_OPTIONS_CANCEL);

        createLayout();
        add(displayPanel);

        setSize(R.dimension.MAIN_FRAME_WIDTH, R.dimension.MAIN_FRAME_HEIGHT);
        setTitle(resources.translate(MSG_TRANSLATION_WINDOW));
        setResizable(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setIconImage(new ImageIcon(R.icon.MAIN_ICON).getImage());
    }

    private void createLayout() {
        displayPanel = new JPanel();
        displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.Y_AXIS));
        Border emptyBorder = BorderFactory.createEmptyBorder(MARGIN_10, MARGIN_10, MARGIN_10, 0);
        displayPanel.setBorder(emptyBorder);

        // management panel
        JPanel managePanel = new JPanel();
        managePanel.setLayout(new BoxLayout(managePanel, BoxLayout.X_AXIS));
        managePanel.add(gramUnitCmbx);
        managePanel.add(Box.createHorizontalStrut(MARGIN_20));
        managePanel.add(addMoreButton);
        managePanel.add(Box.createHorizontalGlue());

        // grammatical units
        JPanel gramUnitPanel = new JPanel(new GridLayout(0, GramUnit.count()));
        for (GramUnit gramUnit : GramUnit.values()) {
            GramUnitLabel gramUnitLabel = new GramUnitLabel(gramUnit.getIndex());
            gramUnitPanel.add(gramUnitLabel);
            gramUnitLabels.put(gramUnit, gramUnitLabel);
        }

        // meanings
        JScrollPane scrollableMeaningsListPanel = new JScrollPane(meaningsListPanel);
        scrollableMeaningsListPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollableMeaningsListPanel.setBorder(null);

        // buttons
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
        buttonsPanel.add(cancelButton);
        buttonsPanel.add(saveButton);

        displayPanel.add(managePanel);
        displayPanel.add(Box.createVerticalStrut(MARGIN_10));
        displayPanel.add(gramUnitPanel);
        displayPanel.add(Box.createVerticalStrut(MARGIN_10));
        displayPanel.add(scrollableMeaningsListPanel);
        displayPanel.add(Box.createVerticalStrut(MARGIN_10));
        displayPanel.add(buttonsPanel);
    }

    public void addEmptyDefinitionArea(GramUnit gramUnit) {
        addDefinitionArea(0, gramUnit, null);
    }

    public void addDefinitionArea(int id, GramUnit gramUnit, String definition) {
        addDefinitionArea(meaningsListPanel, id, gramUnit, definition);
    }

    private void addDefinitionArea(JPanel panel, int id, GramUnit gramUnit, String definition) {
        Label definitionLabel = new Label(resources, settings, MSG_LABEL_OXFORD_DEFINITION);
        panel.add(definitionLabel);
        definitionLabels.add(definitionLabel);

        DefinitionTextArea definitionTextArea = new DefinitionTextArea();
        definitionTextArea.setDefinitionId(id);
        definitionTextArea.setText(definition);
        JScrollPane scr = new JScrollPane(definitionTextArea);
        panel.add(scr);

        definitionTextAreasMap.put(gramUnit, definitionTextArea);
    }

    public void addEmptyMeaningPanel(GramUnit gramUnit) {
        addMeaningPanel(gramUnit, 0, null, null, null, null);
    }

    public void addMeaningPanel(GramUnit gramUnit, int id, String translation, String antonym, String synonym, String examples) {
        addMeaningPanel(meaningsListPanel, gramUnit, id, translation, antonym, synonym, examples);
    }

    private void addMeaningPanel(JPanel panel, GramUnit gramUnit, int id,
                                 String translation, String antonym, String synonym, String examples) {
        MeaningPanel meaningPanel = new MeaningPanel();
        meaningPanel.setId(id);
        meaningPanel.setTranslationText(translation);
        meaningPanel.setAntonymText(antonym);
        meaningPanel.setSynonymText(synonym);
        meaningPanel.setExamplesText(examples);
        panel.add(Box.createVerticalStrut(MARGIN_20));
        panel.add(meaningPanel);

        List<MeaningPanel> list = meaningPanelsMap.get(gramUnit);
        if (list == null) {
            list = new ArrayList<>();
            meaningPanelsMap.put(gramUnit, list);
        }
        list.add(meaningPanel);
    }

    public void selectPartOfSpeech(int index) {
        gramUnitCmbx.setSelectedIndex(index);
    }

    private Font getBoldFont() {
        try {
            Font font = FontUtil.changeStyle(settings.getFont(), Font.BOLD);
            return FontUtil.smaller(font, FONT_ODDS);
        } catch (FontUtil.WrongFontSizeException e) {
            return e.getResult();
        }
    }

    private Font getItalicBoldFont() {
        try {
            Font font = FontUtil.changeStyle(settings.getFont(), Font.BOLD + Font.ITALIC);
            return FontUtil.smaller(font, FONT_ODDS);
        } catch (FontUtil.WrongFontSizeException ex) {
            return ex.getResult();
        }
    }

    @Override
    public void reloadText() {
        setTitle(resources.translate(MSG_TRANSLATION_WINDOW));
        gramUnitCmbx.reloadText();
        addMoreButton.reloadText();
        for (List<MeaningPanel> panels : meaningPanelsMap.values()) {
            for (MeaningPanel panel : panels) {
                panel.reloadText();
            }
        }
        for (IReloadable posLabel : gramUnitLabels.values()) {
            posLabel.reloadText();
        }
        for (Label label : definitionLabels) {
            label.reloadText();
        }
    }

    @Override
    public void reloadFont() {
        gramUnitCmbx.reloadFont();
        addMoreButton.reloadFont();
        for (List<MeaningPanel> panels : meaningPanelsMap.values()) {
            for (MeaningPanel panel : panels) {
                panel.reloadFont();
            }
        }
        setFontOfSelectedPosLabel();
        for (Label label : definitionLabels) {
            label.reloadFont();
        }
        setSize(R.dimension.MAIN_FRAME_WIDTH, R.dimension.MAIN_FRAME_HEIGHT);
    }

    private void loadMeaningsListPanel(boolean isNewWindow) {
        if (isNewWindow) {
            presenter.newMeaningsListPanel();
        } else {
            presenter.populateMeaningsListPanel();
        }
    }

    @Override
    public void subscribeForEvent(Observable observable) {
        observable.attach(this, new LocalizationChangedEvent(this));
        observable.attach(this, new FontChangedEvent(this));
    }

    private void setFontOfSelectedPosLabel() {
        for (GramUnitLabel lb : gramUnitLabels.values())
            lb.reloadFont();
    }

    private void setFontColorOfSelectedPosLabel() {
        for (GramUnitLabel lb : gramUnitLabels.values())
            lb.reloadColor();
    }

    public MeaningWindowPresenter.IUiMeaningValues getUiMeaningValues() {
        return saveValues;
    }

    private class MeaningPanel extends JPanel implements IReloadable {

        public static final int FIELD_LENGTH = 64;

        private int id;
        private JTextField translationField;
        private JTextField synonymField;
        private JTextField antonymField;
        private JTextArea examplesTextArea;

        private List<Label> labels = new ArrayList<>();

        private MeaningPanel() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

            translationField = addField(MSG_TITLE_TRANSLATION);
            synonymField = addField(MSG_TITLE_SYNONYM);
            antonymField = addField(MSG_TITLE_ANTONYM);

            Label examplesLabel = new Label(resources, settings, MSG_TITLE_EXAMPLE);
            examplesTextArea = new JTextArea();
            examplesTextArea.setFont(settings.getFont());
            examplesTextArea.setLineWrap(true);
            examplesTextArea.setRows(EXAMPLES_ROWS_COUNT);
            JScrollPane examplesScrollPanel = new JScrollPane(examplesTextArea);
            add(examplesLabel);
            labels.add(examplesLabel);
            add(examplesScrollPanel);
        }

        private JTextField addField(String techLabel) {
            Label label = new Label(resources, settings, techLabel);
            JTextField field = new JTextField(FIELD_LENGTH);
            field.setFont(settings.getFont());
            add(label);
            labels.add(label);
            add(field);
            return field;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTranslationText() {
            return translationField.getText();
        }

        public void setTranslationText(String translation) {
            this.translationField.setText(translation);
        }

        public String getSynonymText() {
            return synonymField.getText();
        }

        public void setSynonymText(String synonym) {
            this.synonymField.setText(synonym);
        }

        public String getAntonymText() {
            return antonymField.getText();
        }

        public void setAntonymText(String antonym) {
            this.antonymField.setText(antonym);
        }

        public String getExamplesText() {
            return examplesTextArea.getText();
        }

        public void setExamplesText(String examples) {
            this.examplesTextArea.setText(examples);
        }

        @Override
        public void reloadText() {
            for (Label label : labels) {
                label.reloadText();
            }
        }

        @Override
        public void reloadFont() {
            for (Label label : labels) {
                label.reloadFont();
            }
        }

    }

    private class GramUnitLabel extends JLabel implements IReloadable {

        private static final long serialVersionUID = 1L;
        private int index;

        private GramUnitLabel(int index) {
            this.index = index;
            int length = ComboListHelper.getComboListSize(ComboList.PART_OF_SPEECH);
            if (index > length) throw new IndexOutOfBoundsException();

            setLabel(index);
            setHorizontalAlignment(SwingConstants.CENTER);
            setFont(getBoldFont());
        }

        private void setLabel(int index) {
            Object[] newItemsArr = ComboListHelper.getComboList(resources, ComboList.PART_OF_SPEECH);
            setText(String.valueOf(newItemsArr[index]));
        }

        @Override
        public void reloadText() {
            setLabel(index);
        }

        @Override
        public void reloadFont() {
            if (index == selectedGramUnit.getIndex()) {
                setFont(getItalicBoldFont());
            } else {
                setFont(getBoldFont());
            }
        }

        private void reloadColor() {
            if (index == selectedGramUnit.getIndex()) {
                Color oppositeColor = findOppositeColor(Color.BLACK);
                setForeground(oppositeColor);
            } else {
                setForeground(Color.BLACK);
            }
        }

    }

    private class PartOfSpeechComboBoxListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JComboBox cb = (JComboBox) e.getSource();
            int posCbIndex = cb.getSelectedIndex();
            selectedGramUnit = GramUnit.elementAt(posCbIndex);
            setFontOfSelectedPosLabel();
            setFontColorOfSelectedPosLabel();
            gramUnitLabels.get(selectedGramUnit).setFont(getItalicBoldFont());
            Color oppositeColor = findOppositeColor(Color.BLACK);
            gramUnitLabels.get(selectedGramUnit).setForeground(oppositeColor);

            meaningsListPanel.removeAll();

            DefinitionTextArea textArea = definitionTextAreasMap.get(selectedGramUnit);
            if (textArea == null) {
                addEmptyDefinitionArea(selectedGramUnit);
            } else {
                addDefinitionArea(textArea.definitionId, selectedGramUnit, textArea.getText());
            }

            List<MeaningPanel> meaningPanels = meaningPanelsMap.get(selectedGramUnit);
            if (meaningPanels == null) {
                addEmptyMeaningPanel(selectedGramUnit);
            } else for (MeaningPanel meaningPanel : meaningPanels) {
                meaningsListPanel.add(meaningPanel);
            }

            displayPanel.revalidate();
            displayPanel.repaint(200);
        }

    }

    private class SaveMeanings extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent event) {
            presenter.addMeanings();
        }

    }

    private class CancelAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
            dispose();
        }
    }

    private class AddMeaning extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent event) {
            addEmptyMeaningPanel(selectedGramUnit);
            meaningsListPanel.revalidate();
        }

    }

    private class SaveValues implements MeaningWindowPresenter.IUiMeaningValues {

        @Override
        public Integer getDefinitionId(GramUnit gramUnit) {
            DefinitionTextArea textArea = definitionTextAreasMap.get(gramUnit);
            if (textArea == null) {
                return 0;
            }
            return textArea.getDefinitionId();
        }

        @Override
        public String getDefinitionText(GramUnit gramUnit) {
            DefinitionTextArea textArea = definitionTextAreasMap.get(gramUnit);
            if (textArea == null) {
                return null;
            }
            return textArea.getText();
        }

        @Override
        public List<Integer> getMeaningIds(GramUnit gramUnit) {
            return getFieldValues(gramUnit, new GetValue<Integer>() {
                @Override
                public Integer get(MeaningPanel panel) {
                    return panel.getId();
                }
            });
        }

        @Override
        public List<String> getTranslationText(GramUnit gramUnit) {
            return getFieldValues(gramUnit, new GetValue<String>() {

                @Override
                public String get(MeaningPanel panel) {
                    return panel.getTranslationText();
                }
            });
        }

        @Override
        public List<String> getSynonymText(GramUnit gramUnit) {
            return getFieldValues(gramUnit, new GetValue<String>() {

                @Override
                public String get(MeaningPanel panel) {
                    return panel.getSynonymText();
                }
            });
        }

        @Override
        public List<String> getAntonymText(GramUnit gramUnit) {
            return getFieldValues(gramUnit, new GetValue<String>() {

                @Override
                public String get(MeaningPanel meaningPanel) {
                    return meaningPanel.getAntonymText();
                }
            });
        }

        @Override
        public List<String> getExamplesText(GramUnit gramUnit) {
            return getFieldValues(gramUnit, new GetValue<String>() {

                @Override
                public String get(MeaningPanel meaningPanel) {
                    return meaningPanel.getExamplesText();
                }
            });
        }

        private <T> List<T> getFieldValues(GramUnit gramUnit, GetValue<T> getValue) {
            List<MeaningPanel> list = meaningPanelsMap.get(gramUnit);

            if (list == null) {
                return Collections.<T>emptyList();
            }

            List<T> elements = new ArrayList<T>(list.size());
            for (int i = 0; i < list.size(); i++) {
                elements.add(getValue.get(list.get(i)));
            }

            return elements;
        }

    }

    private interface GetValue<T> {
        T get(MeaningPanel panel);
    }

    public class DefinitionTextArea extends JTextArea implements IReloadable {

        private static final int DEFAULT_ROW_NUMBER = 3;
        private Integer definitionId;

        private DefinitionTextArea() {
            customize();
        }

        public Integer getDefinitionId() {
            return definitionId;
        }

        public void setDefinitionId(Integer definitionId) {
            this.definitionId = definitionId;
        }

        private void customize() {
            setFont(settings.getFont());
            setLineWrap(true);
            setRows(DEFAULT_ROW_NUMBER);
            setBorder(null);
        }

        @Override
        public void reloadText() {
            // TODO Auto-generated method stub
        }

        @Override
        public void reloadFont() {
            setFont(settings.getFont());
        }

    }

}