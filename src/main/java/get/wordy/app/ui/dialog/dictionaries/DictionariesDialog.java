package get.wordy.app.ui.dialog.dictionaries;

import get.wordy.app.resources.R;
import get.wordy.app.resources.Resources;
import get.wordy.app.ui.MainWindow;
import get.wordy.app.ui.UISettings;
import get.wordy.app.ui.WordyMediator;
import get.wordy.app.ui.component.Button;
import get.wordy.app.ui.localizable.Localized;
import get.wordy.core.bean.Dictionary;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;

public class DictionariesDialog extends JDialog implements Localized {

    private final DictionariesDialogPresenter presenter;

    private Resources resources;
    private UISettings settings;

    private JPanel displayPanel;

    private JList fileList;

    private Button createButton;
    private Button removeButton;
    private Button scoreButton;
    private Button selectButton;
    private Button closeButton;

    public DictionariesDialog(MainWindow mainWindow, WordyMediator mediator, List<Dictionary> dictionaries) {
        super(mainWindow, true);
        this.resources = mediator.getResources();
        this.settings = mediator.getUiSettings();

        this.presenter = new DictionariesDialogPresenter(this, mediator);

        init();
        updateList(dictionaries);
    }

    private void init() {
        createDictionaryList();
        createButtons();

        createLayout();
        add(displayPanel);

        pack();
        setTitle(resources.translate(MSG_TITLE_DICTIONARIES_FRAME));
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setIconImage(new ImageIcon(R.icon.MAIN_ICON).getImage());
    }

    private void createDictionaryList() {
        fileList = new JList();
        fileList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                setRightButtonsEnabled(e.getFirstIndex() >= 0);
            }
        });
    }

    private void createButtons() {
        // right
        DictionaryAction action = new DictionaryAction();
        createButton = new Button(action, resources, settings, MSG_BUTTON_CREATE);
        removeButton = new Button(action, resources, settings, MSG_BUTTON_REMOVE);
        removeButton.setEnabled(false);
        scoreButton = new Button(action, resources, settings, MSG_BUTTON_STATISTIC);
        scoreButton.setEnabled(false);

        // bottom
        selectButton = new Button(action, resources, settings, MSG_BUTTON_SELECT);
        selectButton.setEnabled(false);
        closeButton = new Button(action, resources, settings, MSG_BUTTON_CLOSE);
    }

    private void createLayout() {
        displayPanel = new JPanel(new BorderLayout());
        displayPanel.setBorder(new EmptyBorder(7, 10, 7, 10));

        // LIST PANEL
        JScrollPane listPanel = new JScrollPane(fileList);
        fileList.setBorder(new EmptyBorder(5, 5, 5, 5));
        fileList.setFont(settings.getFont());
        fileList.setVisibleRowCount(8);
        fileList.setFixedCellHeight(settings.getFont().getSize() + 7);
        fileList.setFixedCellWidth(200);

        // RIGHT BUTTONS PANEL
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0, 7, 7, 0);
        c.fill = GridBagConstraints.HORIZONTAL;

        // #2
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 1;
        c.weighty = 0;
        buttonsPanel.add(createButton, c);

        // #3
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 1;
        c.weighty = 0;
        buttonsPanel.add(removeButton, c);

        // #4
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 1;
        c.weighty = 0;
        buttonsPanel.add(scoreButton, c);

        JPanel buttonsPanelFix = new JPanel(new BorderLayout());
        buttonsPanelFix.add(buttonsPanel, BorderLayout.NORTH);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.PAGE_AXIS));

        Box buttonBox = Box.createHorizontalBox();
        buttonBox.add(Box.createHorizontalGlue());
        buttonBox.add(selectButton);
        buttonBox.add(Box.createHorizontalStrut(10));
        buttonBox.add(closeButton);
        bottomPanel.add(Box.createVerticalStrut(10));
        bottomPanel.add(buttonBox);

        displayPanel.add(listPanel, BorderLayout.CENTER);
        displayPanel.add(buttonsPanelFix, BorderLayout.EAST);
        displayPanel.add(bottomPanel, BorderLayout.SOUTH);
    }

    @Override
    protected JRootPane createRootPane() {
        rootPane = new JRootPane();
        KeyStroke stroke = KeyStroke.getKeyStroke((char) KeyEvent.VK_ESCAPE);
        InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(stroke, (char) KeyEvent.VK_ESCAPE);
        rootPane.getActionMap().put((char) KeyEvent.VK_ESCAPE, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                dispose();
            }
        });

        return rootPane;
    }


    void updateList(List<Dictionary> dictionaries) {
        fileList.setModel(new DictionariesListModel(dictionaries));
        fileList.revalidate();
        fileList.repaint();
    }

    public void setRightButtonsEnabled(boolean isEnabled) {
        removeButton.setEnabled(isEnabled);
        scoreButton.setEnabled(isEnabled);
        selectButton.setEnabled(isEnabled);
    }

    public void select(String dictionaryName) {
        fileList.setSelectedValue(dictionaryName, true);
    }

    private class DictionaryAction extends AbstractAction {

        @Override
        public void actionPerformed(final ActionEvent event) {

            final JButton source = (JButton) event.getSource();

            // CREATE
            if (source == createButton) {
                presenter.createNewDictionary();
            }

            // DELETE
            else if (source == removeButton) {
                if (isConfirmed()) {
                    presenter.removeDictionary(getSelectedDictionary());
                }
            }

            // SHOW SCORE
            else if (source == scoreButton) {
                presenter.showScoreDialog(getSelectedDictionary());
            }

            // SELECT DICTIONARY
            else if (source == selectButton) {
                presenter.selectDictionary(getSelectedDictionary());
                DictionariesDialog.this.setVisible(false);
                DictionariesDialog.this.dispose();
            }

            // CLOSE DIALOG
            else if (source == closeButton) {
                DictionariesDialog.this.setVisible(false);
                DictionariesDialog.this.dispose();
            }
        }

        private Dictionary getSelectedDictionary() {
            DictionariesListModel model = (DictionariesListModel) fileList.getModel();
            return model.getDictionary(String.valueOf(fileList.getSelectedValue()));
        }

        /**
         * Ask user if they sure to delete dictionary
         */
        private boolean isConfirmed() {
            Object[] options = {
                    resources.translate(Localized.MSG_DELETE_OK_CARD_MESSAGE_DIALOG),
                    resources.translate(Localized.MSG_DELETE_NO_CARD_MESSAGE_DIALOG),
            };

            final int answer = JOptionPane.showOptionDialog(
                    null,
                    resources.translate(Localized.MSG_DELETE_DICTIONARY_USER_QUESTION_MESSAGE_DIALOG),
                    resources.translate(Localized.MSG_DELETE_DICTIONARY_TITLE_MESSAGE_DIALOG),
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null, // do not use a custom Icon
                    options, // the titles of buttons
                    options[0]); // default button title

            return (answer == JOptionPane.YES_OPTION) ? true : false;
        }

    }

    private class DictionariesListModel extends AbstractListModel<String> {

        private List<Dictionary> dictionaries;

        public DictionariesListModel(List<Dictionary> dictionaries) {
            this.dictionaries = dictionaries;
        }

        @Override
        public int getSize() {
            return dictionaries.size();
        }

        @Override
        public String getElementAt(int index) {
            return dictionaries.get(index).getName();
        }

        private Dictionary getDictionary(String name) {
            for (Dictionary dictionary : dictionaries) {
                if (dictionary.getName().equals(name)) {
                    return dictionary;
                }
            }
            return new Dictionary();
        }

    }

}