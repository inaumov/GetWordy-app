package get.wordy.app.ui.menu;

import get.wordy.app.ui.BasePresenter;
import get.wordy.app.ui.action.*;

public class ActionFactory {

    private BasePresenter basePresenter;

    public ActionFactory(BasePresenter basePresenter) {
        this.basePresenter = basePresenter;
    }

    public StartExerciseAction newStartExerciseAction() {
        return new StartExerciseAction(basePresenter);
    }

    public NewCardAction newCardAction() {
        return new NewCardAction(basePresenter);
    }

    public CreateDictionaryAction newCreateDictionaryAction() {
        return new CreateDictionaryAction(basePresenter);
    }

    public ExportDictionaryAction newExportDictionaryAction() {
        return new ExportDictionaryAction(basePresenter);
    }

    public DeleteCardAction newDeleteCardAction() {
        return new DeleteCardAction(basePresenter);
    }

    public CopyToDictionaryAction newCopyToDictionaryAction() {
        return new CopyToDictionaryAction(basePresenter);
    }

    public ShowDictionaryListAction newShowDictionaryListAction() {
        return new ShowDictionaryListAction(basePresenter);
    }

    public ShowScoreDialogAction newShowScoreDialogAction() {
        return new ShowScoreDialogAction(basePresenter);
    }

    public ExitAction newExitAction() {
        return new ExitAction();
    }

    public EditCardAction newEditCardAction() {
        return new EditCardAction(basePresenter);
    }

    public TranslateInGoogleAction newTranslateInGoogleAction() {
        return new TranslateInGoogleAction(basePresenter);
    }

    public ShowOptionsAction newShowOptionsAction() {
        return new ShowOptionsAction(basePresenter);
    }

}