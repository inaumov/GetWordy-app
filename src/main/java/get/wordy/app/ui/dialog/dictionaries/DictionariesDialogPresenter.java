package get.wordy.app.ui.dialog.dictionaries;

import get.wordy.app.impl.ApplicationContext;
import get.wordy.app.impl.ServiceHolder;
import get.wordy.app.ui.IApplicationAction;
import get.wordy.app.ui.WordyMediator;
import get.wordy.core.api.IScore;
import get.wordy.core.bean.Dictionary;

import javax.swing.*;

import java.util.List;

import static get.wordy.app.impl.ServiceHolder.getDictionaryService;
import static get.wordy.app.impl.ServiceHolder.getSettingManager;

public class DictionariesDialogPresenter {

    private DictionariesDialog dictionariesDialog;
    private WordyMediator mediator;

    public DictionariesDialogPresenter(DictionariesDialog dictionariesDialog, WordyMediator mediator) {
        this.dictionariesDialog = dictionariesDialog;
        this.mediator = mediator;
    }

    void createNewDictionary() {
        mediator.showCreateDictionaryDialog(new IApplicationAction() {
            @Override
            public void onAction(String requestValue) {
                Dictionary dictionary = ServiceHolder.getDictionaryService().createDictionary(requestValue);
                if (dictionary != null) {
                    ApplicationContext.selectDictionary(dictionary);
                    ServiceHolder.getSettingManager().saveDictionaryName(requestValue);
                    List<Dictionary> dictionaries = getDictionaryService().getDictionaries();
                    dictionariesDialog.updateList(dictionaries);
                    dictionariesDialog.select(requestValue);
                }
            }
        });
    }

    void showScoreDialog(Dictionary dictionary) {
        IScore score = getDictionaryService().getScore(dictionary.getName());
        mediator.showScoreDialog(dictionary.getName(), score);
    }

    void selectDictionary(final Dictionary dictionary) {
        ApplicationContext.selectDictionary(dictionary);
        getSettingManager().saveDictionaryName(dictionary.getName());
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                mediator.updateTable();
            }
        });
    }

    void removeDictionary(Dictionary dictionary) {
        getDictionaryService().removeDictionary(dictionary.getName());
        List<Dictionary> dictionaries = getDictionaryService().getDictionaries();
        dictionariesDialog.updateList(dictionaries);
        dictionariesDialog.setRightButtonsEnabled(false);
    }

}