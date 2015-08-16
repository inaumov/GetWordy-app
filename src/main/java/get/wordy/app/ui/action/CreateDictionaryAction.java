package get.wordy.app.ui.action;

import get.wordy.app.impl.ApplicationContext;
import get.wordy.app.impl.ServiceHolder;
import get.wordy.app.ui.BasePresenter;
import get.wordy.app.ui.IApplicationAction;
import get.wordy.core.bean.Dictionary;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class CreateDictionaryAction extends AbstractAction {

    private BasePresenter basePresenter;

    public CreateDictionaryAction(BasePresenter basePresenter) {
        this.basePresenter = basePresenter;
    }

    @Override
    public void actionPerformed(ActionEvent action) {
        basePresenter.createNewDictionary(new IApplicationAction() {
            @Override
            public void onAction(String requestValue) {
                Dictionary dictionary = ServiceHolder.getDictionaryService().createDictionary(requestValue);
                if (dictionary != null) {
                    ApplicationContext.selectDictionary(dictionary);
                    ServiceHolder.getSettingManager().saveDictionaryName(requestValue);
                }
            }
        });
    }

}