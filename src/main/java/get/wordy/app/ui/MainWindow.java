package get.wordy.app.ui;

import get.wordy.app.impl.ApplicationContext;
import get.wordy.app.resources.R;
import get.wordy.app.resources.Resources;
import get.wordy.app.ui.action.*;
import get.wordy.app.ui.action.listener.CustomTableMouseListener;
import get.wordy.app.ui.component.*;
import get.wordy.app.ui.component.Button;
import get.wordy.app.ui.event.*;
import get.wordy.app.ui.action.DeleteCardAction;
import get.wordy.app.ui.action.NewCardAction;
import get.wordy.app.ui.action.StartExerciseAction;
import get.wordy.app.ui.localizable.Localized;
import get.wordy.app.ui.menu.ActionFactory;
import get.wordy.app.ui.menu.ApplicationMenu;
import get.wordy.app.ui.tablemodel.ResultTableModel;
import get.wordy.core.api.IDictionaryService;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.text.BadLocationException;
import java.awt.event.ActionEvent;
import java.util.Iterator;
import java.util.Set;

public class MainWindow extends JFrame implements Localized, IReloadable, EventSubscriber {

    private WordyMediator mediator;
    private Resources resources;
    private UISettings settings;

    private JPanel displayPanel;
    private JScrollPane scrollingPanel;
    private SearchField searchField;
    private ResultTable cardsTable;

    private Button startExerciseButton;
    private JButton newCardButton;
    private JButton editCardButton;
    private JButton delCardButton;
    private JButton scheduleButton;

    private ApplicationMenu applicationMenu;
    private BasePresenter basePresenter;

    public MainWindow(WordyMediator mediator, Resources resources, UISettings settings) {
        this.mediator = mediator;
        this.resources = resources;
        this.settings = settings;
        init();
        subscribeForEvent(mediator);
    }

    private void init() {
        basePresenter = new BasePresenter(mediator);

        createCardsTable();
        createStartExerciseButton();
        createNewCardButton();
        createEditCardButton();
        createDeleteCardButton();
        createScheduleButton();
        createSearchField();
        createApplicationMenu();

        createLayout();
        add(displayPanel);

        setSize(R.dimension.MAIN_FRAME_WIDTH, R.dimension.MAIN_FRAME_HEIGHT);
        setIconImage(new ImageIcon(R.icon.MAIN_ICON).getImage());
        setTitle(resources.translate(MSG_MAIN_WINDOW) + " - " + R._version);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void createLayout() {
        displayPanel = new JPanel();
        displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.PAGE_AXIS));

        // Create TABLE BOX to show data from DataBase
        Box tableBox = Box.createVerticalBox();
        Border etchedBorder = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
        tableBox.setBorder(etchedBorder);
        tableBox.add(scrollingPanel);

        // Create BUTTONS BOX dedicated to main window
        Box buttonsBox = Box.createHorizontalBox();
        Border emptyBorder = BorderFactory.createEmptyBorder(3, 3, 3, 0);
        Border border = new CompoundBorder(etchedBorder, emptyBorder);
        buttonsBox.setBorder(border);
        buttonsBox.add(startExerciseButton);
        buttonsBox.add(Box.createHorizontalGlue());
        buttonsBox.add(newCardButton);
        buttonsBox.add(Box.createHorizontalStrut(3));
        buttonsBox.add(editCardButton);
        buttonsBox.add(Box.createHorizontalStrut(3));
        buttonsBox.add(delCardButton);
        buttonsBox.add(Box.createHorizontalStrut(15));
        buttonsBox.add(scheduleButton);

        displayPanel.add(tableBox);
        displayPanel.add(searchField);
        displayPanel.add(buttonsBox);
    }

    private void createApplicationMenu() {
        applicationMenu = new ApplicationMenu(new ActionFactory(basePresenter), mediator, resources, settings);
        setJMenuBar(applicationMenu);
    }

    private void createSearchField() {
        searchField = new SearchField(settings, new SearchListener());
    }

    private void createCardsTable() {
        IDictionaryService dictionaryService = mediator.getDictionaryService();
        String dictionaryName = ApplicationContext.getSelectedDictionary().getName();
        ResultTableModel tableModel = new ResultTableModel(dictionaryService.getCards(dictionaryName));
        cardsTable = new ResultTable(tableModel, resources, settings);
        cardsTable.addMouseListener(new CustomTableMouseListener(basePresenter));

        scrollingPanel = new JScrollPane(cardsTable);
        scrollingPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        mediator.registerCardListTable(cardsTable);
    }

    private void createStartExerciseButton() {
        StartExerciseAction exerciseAction = new StartExerciseAction(basePresenter);
        startExerciseButton = new Button(exerciseAction, resources, settings, MSG_BUTTON_START_EXERCISE);
        startExerciseButton.setToolTipText(TOOLTIP_START_BUTTON);
    }

    private void createNewCardButton() {
        newCardButton = new JButton(new NewCardAction(basePresenter));
        newCardButton.setToolTipText(TOOLTIP_CREATE_BUTTON);
        newCardButton.setIcon(new ImageIcon(R.icon.CREATE_CARD_LARGE));
    }

    private void createEditCardButton() {
        editCardButton = new JButton(new EditCardAction(basePresenter));
        editCardButton.setToolTipText(TOOLTIP_EDIT_BUTTON);
        editCardButton.setIcon(new ImageIcon(R.icon.EDIT_CARD_LARGE));
    }

    private void createDeleteCardButton() {
        delCardButton = new JButton(new DeleteCardAction(basePresenter));
        delCardButton.setToolTipText(TOOLTIP_DEL_BUTTON);
        delCardButton.setIcon(new ImageIcon(R.icon.DELETE_CARD_LARGE));
    }

    private void createScheduleButton() {
        scheduleButton = new JButton(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mediator.openScheduleDialog();
            }
        });
        scheduleButton.setToolTipText(resources.translate(TOOLTIP_SCHEDULE_BUTTON));
        scheduleButton.setIcon(new ImageIcon(R.icon.SCHEDULE_LARGE));
    }

    @Override
    public void reloadText() {
        setTitle(resources.translate(MSG_MAIN_WINDOW));
        applicationMenu.reloadText();
        cardsTable.reloadText();
        startExerciseButton.reloadText();
    }

    @Override
    public void reloadFont() {
        applicationMenu.reloadFont();
        cardsTable.reloadFont();
        startExerciseButton.reloadFont();
        searchField.reloadFont();
    }

    @Override
    public void subscribeForEvent(Observable observable) {
        observable.attach(this, new LocalizationChangedEvent(this));
        observable.attach(this, new FontChangedEvent(this));
    }

    public void hideWindow() {
        setVisible(false);
    }

    private class SearchListener extends SearchField.SearchCallback {

        @Override
        public void onSearch(DocumentEvent e) {
            final int textLength = e.getLength();
            String toSearch;
            try {
                toSearch = e.getDocument().getText(0, textLength);
            } catch (BadLocationException e1) {
                return;
            }
            Set<String> words = cardsTable.getWords();
            Iterator<String> it = words.iterator();
            int index = 0;
            while (it.hasNext()) {
                String toCompare = it.next().substring(0, textLength);
                if (toCompare.equalsIgnoreCase(toSearch)) {
                    cardsTable.getSelectionModel().setSelectionInterval(0, index);
                    cardsTable.scrollRectToVisible(cardsTable.getCellRect(index, cardsTable.getColumnCount(), true));
                    editMode(true);
                    return;
                }
                index++;
            }
            cardsTable.getSelectionModel().removeSelectionInterval(0, words.size());
            editMode(false);
        }

        private void editMode(boolean isEnabled) {
            newCardButton.setEnabled(isEnabled);
            delCardButton.setEnabled(isEnabled);
        }

    }

    public void updateTable() {
        AbstractTableModel model = (AbstractTableModel) cardsTable.getModel();
        model.fireTableDataChanged();
        cardsTable.revalidate();
    }

    public void updateModel() {
        IDictionaryService dictionaryService = mediator.getDictionaryService();
        String dictionaryName = ApplicationContext.getSelectedDictionary().getName();
        ResultTableModel tableModel = new ResultTableModel(dictionaryService.getCards(dictionaryName));
        cardsTable.setModel(tableModel);
        cardsTable.updateHeaders();
    }

    public String getSelectedRow() {
        return String.valueOf(cardsTable.getValueAt(cardsTable.getSelectedRow(), ResultTableModel.WORD_COLUMN_INDEX));
    }

}