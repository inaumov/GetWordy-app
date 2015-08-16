package get.wordy.app.resources;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public final class Resources {

    private static final Resources resources = new Resources();

    private ResourceBundle messageBundle;

    private Resources() {
    }

    public static final Resources getResources() {
        return resources;
    }

    public void setMessageBundle(ResourceBundle messageBundle) {
        this.messageBundle = messageBundle;
    }

    public ResourceBundle getMessageBundle() {
        return messageBundle;
    }

    public String translate(String key) {
        try {
            return resources.messageBundle.getString(key);
        } catch (MissingResourceException ex) {
            return key;
        }
    }

}