package get.wordy.app.settings;

import java.util.Locale;

public enum Language {

    ENGLISH(Locale.US),
    RUSSIAN(new Locale("ru", "RU")),
    UKRAINIAN(new Locale("uk", "UA"));

    private Locale locale;

    private Language(Locale locale) {
        this.locale = locale;
    }

    public String getDisplayLanguage() {
        return locale.getDisplayName(locale);
    }

    public Locale getLocale() {
        return locale;
    }

}