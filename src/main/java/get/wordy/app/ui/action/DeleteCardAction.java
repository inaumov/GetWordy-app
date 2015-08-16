package get.wordy.app.ui.action;

import get.wordy.app.ui.BasePresenter;

public class DeleteCardAction extends EditCardAction {

    private BasePresenter basePresenter;

    public DeleteCardAction(BasePresenter basePresenter) {
        super(basePresenter);
        this.basePresenter = basePresenter;
    }

    @Override
    protected void action() {
        basePresenter.deleteCard();
    }

}