package get.wordy.app.ui.action;

import get.wordy.app.ui.WordyMediator;
import get.wordy.app.ui.BasePresenter;
import get.wordy.app.ui.component.ResultTable;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class EditCardAction extends AbstractAction {

    private static final int SELECTED_WORD_COLUMN = 2;

    private static String selectedWord;

    private BasePresenter basePresenter;
    private WordyMediator mediator;

    public EditCardAction(BasePresenter basePresenter) {
        this.basePresenter = basePresenter;
        this.mediator = basePresenter.getMediator();
    }

    @Override
    public void actionPerformed(ActionEvent action) {
        if (isWordSelected()) {
            action();
        } else {
            basePresenter.showNoSelectedCardDialog();
        }
    }

    protected void selectWord() {
        ResultTable resultsTable = mediator.getResultsTable();
        int selected = resultsTable.getSelectedRow();
        String word = (String) resultsTable.getValueAt(selected, SELECTED_WORD_COLUMN);
        if (word.equals(selectedWord)) {
            return;
        }
        selectedWord = word;
    }

    protected void action() {
        basePresenter.loadCardToEdit(selectedWord);
    }

    protected boolean isWordSelected() {
        ResultTable resultsTable = mediator.getResultsTable();
        int selected = resultsTable.getSelectedRow();
        return selected >= 0;
    }

}