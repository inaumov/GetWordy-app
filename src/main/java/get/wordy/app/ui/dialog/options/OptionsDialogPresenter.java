package get.wordy.app.ui.dialog.options;

import get.wordy.app.impl.ServiceHolder;
import get.wordy.app.settings.Setting;
import get.wordy.app.settings.SettingsManager;
import get.wordy.app.ui.WordyMediator;

import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class OptionsDialogPresenter {

    private OptionsDialog optionsDialog;
    private WordyMediator mediator;
    private SettingsManager settingsManager = ServiceHolder.getSettingManager();
    private ISettingCallback callback;

    public interface ISettingCallback {

        Map onSave();

        void onRestore(Properties values);

        void onCancel();
    }

    public OptionsDialogPresenter(WordyMediator mediator, OptionsDialog optionsDialog) {
        this.optionsDialog = optionsDialog;
        this.mediator = mediator;
    }

    /**
     * Set a callback object that will be notified of menu events
     * related to this specific presentation.
     * @param cb Callback that will be notified of future events
     */
    public void setCallback(ISettingCallback cb) {
        this.callback = cb;
    }

    public void saveSettings() {
        Map values = callback.onSave();
        Set<Map.Entry> set = values.entrySet();
        for (Map.Entry entry : set) {
            Setting key = (Setting) entry.getKey();
            if (entry.getValue() instanceof Boolean) {
                settingsManager.saveChecked(key, (Boolean) entry.getValue());
            } else if (entry.getValue() instanceof Number) {
                settingsManager.saveNumber(key, (Number) entry.getValue());
            }
        }
        settingsManager.store();
        optionsDialog.hideWindow();
    }

    public void restoreSettings() {
        callback.onRestore(settingsManager.getAppProperties());
    }

    public void cancel() {
        callback.onCancel();
    }

}