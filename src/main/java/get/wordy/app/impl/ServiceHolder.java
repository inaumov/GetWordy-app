package get.wordy.app.impl;

import get.wordy.app.settings.SettingsManager;
import get.wordy.core.api.IDictionaryService;

public class ServiceHolder {

    private static ServiceHolder serviceHolder = new ServiceHolder();

    private IDictionaryService dictionaryService;
    private SettingsManager settingsManager;

    public static ServiceHolder getServiceHolder() {
        return serviceHolder;
    }

    private ServiceHolder() {
    }

    public void setDictionaryService(IDictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
    }

    public static IDictionaryService getDictionaryService() {
        return getServiceHolder().dictionaryService;
    }

    public void setSettingsManager(SettingsManager settingsManager) {
        this.settingsManager = settingsManager;
    }

    public static SettingsManager getSettingManager() {
        return getServiceHolder().settingsManager;
    }


}