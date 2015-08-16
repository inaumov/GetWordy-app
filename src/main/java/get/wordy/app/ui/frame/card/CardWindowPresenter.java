package get.wordy.app.ui.frame.card;

import get.wordy.app.impl.ServiceHolder;
import get.wordy.app.ui.WordyMediator;
import get.wordy.core.api.IDictionaryService;
import get.wordy.core.bean.Word;
import get.wordy.core.bean.wrapper.CardStatus;
import get.wordy.core.bean.Card;

import javax.swing.text.Document;

public class CardWindowPresenter {

    private CardWindow cardWindow;
    private WordyMediator mediator;

    private Card card;

    public CardWindowPresenter(CardWindow cardWindow, WordyMediator mediator, Card card) {
        this.cardWindow = cardWindow;
        this.mediator = mediator;
        this.card = card;
        mediator.register(this);
    }

    public void saveCard() {
        if (!cardWindow.isContentValid()) {
            cardWindow.highlightWordFieldBorder();
            return;
        }

        IUiCardValues saveValues = cardWindow.getUiCardValues();
        card.getWord().setValue(saveValues.getWord());
        card.getWord().setTranscription(saveValues.getTranscription());
        card.setStatus(saveValues.getStatus());

        cardWindow.hideWindow();

        IDictionaryService dictionaryService = ServiceHolder.getDictionaryService();
        String dictionaryName = ServiceHolder.getSettingManager().getDictionaryName();
        boolean done = dictionaryService.saveOrUpdateCard(card, dictionaryName);
        if (done) {
            mediator.showMainWindow();
            mediator.updateTable();
        }

    }

    public void cancel() {
        cardWindow.hideWindow();
        mediator.showMainWindow();
    }

    public void fillData() {
        cardWindow.setWord(card.getWord().getValue());
        cardWindow.setTranscription(card.getWord().getTranscription());
        cardWindow.setStatus(card.getStatus().getIndex());
        mediator.previewCardDocumentOnEdit(card);
        cardWindow.setVisible(true);
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public void loadMeanings(boolean isNewDocument) {
        mediator.openMeaningWindow(card, isNewDocument);
    }

    public void setDocument(Document document) {
        cardWindow.setDocument(document);
    }

    public static interface IUiCardValues {

        public String getWord();

        public String getTranscription();

        public CardStatus getStatus();

    }

}