package get.wordy.app.ui;

import get.wordy.app.impl.ApplicationContext;
import get.wordy.app.impl.ServiceHolder;
import get.wordy.app.resources.Resources;
import get.wordy.app.settings.Setting;
import get.wordy.app.settings.SettingsManager;
import get.wordy.app.ui.component.ResultTable;
import get.wordy.app.ui.dialog.dictionaries.DictionariesDialog;
import get.wordy.app.ui.dialog.options.OptionsDialog;
import get.wordy.app.ui.event.EventObserver;
import get.wordy.app.ui.event.Observable;
import get.wordy.app.ui.dialog.score.ScoreDialog;
import get.wordy.app.ui.frame.card.CardWindow;
import get.wordy.app.ui.frame.card.CardWindowPresenter;
import get.wordy.app.ui.frame.card.ExerciseWindow;
import get.wordy.app.ui.frame.card.ExerciseWindowPresenter;
import get.wordy.app.ui.frame.meaning.MeaningWindow;
import get.wordy.app.ui.util.MeaningPreviewHelper;
import get.wordy.app.ui.localizable.Localized;
import get.wordy.core.api.IDictionaryService;
import get.wordy.core.api.IScore;
import get.wordy.core.bean.Card;
import get.wordy.core.bean.Dictionary;

import javax.swing.*;
import javax.swing.text.Document;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;
import java.util.List;

public class WordyMediator implements Observable {

    private Resources resources;
    private UISettings settings;
    private SettingsManager settingsManager = (SettingsManager) ServiceHolder.getSettingManager();

    private ResultTable resultsTable;

    private Map<String, List<EventObserver>> subscribers = new HashMap<String, List<EventObserver>>();
    private MainWindow mainWindow;
    private CardWindowPresenter cardWindowPresenter;
    private ExerciseWindowPresenter exerciseWindowPresenter;

    public WordyMediator(Resources resources, UISettings settings) {
        this.resources = resources;
        this.settings = settings;
        setupSettingListeners();
    }

    private void setupSettingListeners() {
        settingsManager.addListener(Setting.DICTIONARY_NAME, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                // TODO reconsider this block. use presenter
                settingsManager.store();
                mainWindow.updateModel();
                updateTable();
            }
        });
    }

    private void createMainWindow() {
        if (mainWindow == null) {
            mainWindow = new MainWindow(this, resources, settings);
        }
    }

    public Resources getResources() {
        return resources;
    }

    public UISettings getUiSettings() {
        return settings;
    }

    public IDictionaryService getDictionaryService() {
        return ServiceHolder.getDictionaryService();
    }

    public void showScoreDialog(String dictionaryName, IScore score) {
        ScoreDialog d = new ScoreDialog(mainWindow, this, dictionaryName, score);
        d.setVisible(true);
    }

    public void showDictionariesDialog(List<Dictionary> dictionaries) {
        Dialog dict = new DictionariesDialog(mainWindow, this, dictionaries);
        dict.setVisible(true);
    }

    public void initMainWindow() {

        List<Dictionary> dictionaries = getDictionaryService().getDictionaries();

        if (dictionaries.isEmpty()) {
            showCreateDictionaryDialog(null);
        }

        String dictionaryName = settingsManager.getDictionaryName();

        Dictionary dictionary = null;

        if (dictionaryName == null) {
            dictionary = dictionaries.iterator().next();
        } else {
            for (Dictionary d : dictionaries) {
                if(d.getName().equals(dictionaryName)) {
                    dictionary = d;
                    break;
                }
            }
        }

        ApplicationContext.selectDictionary(dictionary);

        createMainWindow();
        mainWindow.updateTable();

        if (dictionaryName == null) {
            String first = String.valueOf(dictionary.getName());
            settingsManager.saveDictionaryName(first);
        }
    }

    public void showMainWindow() {
        mainWindow.setVisible(true);
    }

    public void showCreateDictionaryDialog(IApplicationAction action) {
        final String newName = JOptionPane.showInputDialog(null,
                resources.translate(Localized.MSG_INPUT_NEW_DICTIONARY),
                resources.translate(Localized.MSG_CREATE_DICTIONARY),
                JOptionPane.OK_CANCEL_OPTION);
        if (newName == null) {
            return;
        }
        action.onAction(newName);
    }

    public void updateTable() {
        mainWindow.updateTable();
    }

    public void hideMainWindow() {
        mainWindow.hideWindow();
    }

    public void showMessageDialog(String techLabel) {
        JOptionPane.showMessageDialog(null, resources.translate(techLabel));
    }

    public void registerCardListTable(ResultTable resultsTable) {
        this.resultsTable = resultsTable;
    }

    public ResultTable getResultsTable() {
        return resultsTable;
    }

    public void openOptionsDialog() {
        OptionsDialog optionsDialog = new OptionsDialog(mainWindow, this);
        optionsDialog.setVisible(true);
    }

    public void openScheduleDialog() {
        OptionsDialog optionsDialog = new OptionsDialog(mainWindow, this);
        optionsDialog.setVisible(true);
        optionsDialog.useScheduleTab();
    }

    public void showCardWindow(Card card) {
        CardWindow window = new CardWindow(card, this);
        window.setVisible(true);
    }

    public void showExerciseWindow(Collection<Card> exerciseList) {
        ExerciseWindow exerciseWindow = new ExerciseWindow(this, exerciseList);
        exerciseWindow.setVisible(true);
    }

    @Override
    public boolean attach(IReloadable IReloadable, EventObserver eventObserver) {
        String eventName = eventObserver.getName();
        java.util.List<EventObserver> observers = subscribers.get(eventName);
        if (observers == null) {
            observers = new ArrayList<EventObserver>();
        } else for
                (EventObserver ob : observers) {
            if (IReloadable == ob.getObserver())
                return false;
        }
        observers.add(eventObserver);
        subscribers.put(eventName, observers);
        return true;
    }

    @Override
    public boolean detach(IReloadable IReloadable, EventObserver eventObserver) {
        String eventName = eventObserver.getName();
        java.util.List<EventObserver> observers = subscribers.get(eventName);
        if (observers == null)
            return false;
        else
            for (EventObserver ob : observers) {
                if (IReloadable == ob.getObserver())
                    observers.remove(ob);
            }
        subscribers.put(eventName, observers);
        return true;
    }

    @Override
    public void fireEvent(String eventName) {
        java.util.List<EventObserver> list = subscribers.get(eventName);
        if (list != null) {
            Iterator<EventObserver> it = list.iterator();
            while (it.hasNext()) {
                EventObserver eventObserver = it.next();
                eventObserver.update();
            }
        }
    }

    public void openMeaningWindow(Card card, boolean isNewDocument) {
        MeaningWindow window = new MeaningWindow(this, card, isNewDocument);
        window.setVisible(true);
    }

    public void register(CardWindowPresenter presenter) {
        this.cardWindowPresenter = presenter;
    }

    public void register(ExerciseWindowPresenter presenter) {
        exerciseWindowPresenter = presenter;
    }

    public void previewCardDocumentOnEdit(Card card) {
        MeaningPreviewHelper previewHelper = new MeaningPreviewHelper(getUiSettings());
        Document document = previewHelper.createDocument(card, MeaningPreviewHelper.DOCUMENT_PREVIEW.EDIT);
        cardWindowPresenter.setDocument(document);
    }

    public void previewCardDocumentOnExercise(Card card) {
        MeaningPreviewHelper previewHelper = new MeaningPreviewHelper(getUiSettings());
        Document document = previewHelper.createDocument(card, MeaningPreviewHelper.DOCUMENT_PREVIEW.EXERCISE);
        exerciseWindowPresenter.setDocument(document);
    }

    public void previewFullCardDocumentOnExercise(Card card) {
        MeaningPreviewHelper previewHelper = new MeaningPreviewHelper(getUiSettings());
        Document document = previewHelper.createDocument(card, MeaningPreviewHelper.DOCUMENT_PREVIEW.FULL);
        exerciseWindowPresenter.setDocument(document);
    }

    public void showDeleteCardDialog(IApplicationAction action) {
        String selected = mainWindow.getSelectedRow();
        Object[] options = {
                resources.translate(Localized.MSG_DELETE_OK_CARD_MESSAGE_DIALOG),
                resources.translate(Localized.MSG_DELETE_NO_CARD_MESSAGE_DIALOG),
        };

        final int v = JOptionPane.showOptionDialog(null,
                resources.translate(Localized.MSG_DELETE_CARD_USER_QUESTION_MESSAGE_DIALOG),
                resources.translate(Localized.MSG_DELETE_CARD_TITLE_MESSAGE_DIALOG),
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, // do not use a custom Icon
                options, // the titles of buttons
                options[0]); // default button title
        if (v == 0) {
            action.onAction(selected);
        }
    }

}