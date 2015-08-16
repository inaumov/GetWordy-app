package get.wordy.app.ui.dialog.score;

import get.wordy.app.impl.ServiceHolder;
import get.wordy.app.ui.WordyMediator;

public class ScoreDialogPresenter {

    private ScoreDialog scoreDialog;
    private WordyMediator mediator;

    public ScoreDialogPresenter(ScoreDialog scoreDialog, WordyMediator mediator) {
        this.scoreDialog = scoreDialog;
        this.mediator = mediator;
    }

    void resetScore(String dictionaryName) {
        ServiceHolder.getDictionaryService().resetScore(dictionaryName);
        mediator.updateTable();
    }

}