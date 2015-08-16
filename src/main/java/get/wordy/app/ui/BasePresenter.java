package get.wordy.app.ui;

import get.wordy.app.impl.ApplicationContext;
import get.wordy.app.impl.ServiceHolder;
import get.wordy.app.settings.Setting;
import get.wordy.app.settings.SettingsManager;
import get.wordy.app.ui.localizable.Localized;
import get.wordy.core.api.IDictionaryService;
import get.wordy.core.api.IScore;
import get.wordy.core.bean.Card;
import get.wordy.core.bean.Dictionary;
import get.wordy.core.bean.Word;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class BasePresenter {

    private WordyMediator mediator;
    private IDictionaryService dictionaryService;

    public BasePresenter(WordyMediator mediator) {
        this.mediator = mediator;
        this.dictionaryService = mediator.getDictionaryService();
    }

    public WordyMediator getMediator() {
        return mediator;
    }

    public void startExercise() {
        mediator.hideMainWindow();
        List<Card> exerciseList = getCardsForExercise();
        if (exerciseList.isEmpty()) {
            mediator.showMessageDialog(Localized.MSG_NO_CARDS_TO_LEARN);
            return;
        }
        Collections.shuffle(exerciseList);
        mediator.showExerciseWindow(exerciseList);
    }

    private List<Card> getCardsForExercise() {
        SettingsManager settingsManager = ServiceHolder.getSettingManager();
        int cardsLimit = (Integer) settingsManager.getNumber(Setting.NUMBER_OF_CARDS_IN_EXERCISE);
        Map<String, Card> map = dictionaryService.getCardsForExercise(settingsManager.getDictionaryName(), cardsLimit);
        return new ArrayList<Card>(map.values());
    }

    public void createNewCard() {
        Card card = new Card();
        card.setWord(new Word());
        card.setDictionaryId(ApplicationContext.getSelectedDictionary().getId());
        mediator.showCardWindow(card);
    }

    public void createNewDictionary(IApplicationAction applicationAction) {
        mediator.showCreateDictionaryDialog(applicationAction);
    }

    public void deleteCard() {
        mediator.showDeleteCardDialog(new IApplicationAction() {
            @Override
            public void onAction(String requestValue) {
                dictionaryService.removeCard(requestValue);
                mediator.updateTable();
            }
        });
    }

    public void showScoreDialog() {
        String dictionaryName = ServiceHolder.getSettingManager().getDictionaryName();
        IScore score = dictionaryService.getScore(dictionaryName);
        mediator.showScoreDialog(dictionaryName, score);
    }

    public void showDictionariesDialog() {
        List<Dictionary> dictionaries = dictionaryService.getDictionaries();
        mediator.showDictionariesDialog(dictionaries);
    }

    public void showNoSelectedCardDialog() {
        mediator.showMessageDialog(Localized.MSG_NO_CARD_SELECTED_MESSAGE_DIALOG);
    }

    public void loadCardToEdit(String selectedWord) {
        Card card = dictionaryService.loadCard(selectedWord);
        mediator.showCardWindow(card);
    }

    public void showOptionsDialog() {
        mediator.openOptionsDialog();
    }

}