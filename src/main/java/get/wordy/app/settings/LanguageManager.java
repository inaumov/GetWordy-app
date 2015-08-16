package get.wordy.app.settings;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.DateFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageManager {

    private final static String LOG_TAG = LanguageManager.class.getName();

    private ResourceBundle messagesBundle;

    private static Locale currentLocale;
    private static DateFormat df;

    public LanguageManager(Language selectedLanguage) {

        try {
            messagesBundle = getResourceBundle(selectedLanguage);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        setCurrentLocale(selectedLanguage.getLocale());
    }

    public ResourceBundle getMessageBundle() {
        if (messagesBundle == null)
            throw new NullPointerException(LOG_TAG + ": " + messagesBundle.toString() + " is null");
        return messagesBundle;
    }

    private ResourceBundle getResourceBundle(Language language) throws IOException {
        Locale currentLocale = language.getLocale();
        File directory = new File("lang");
        URL[] urls = {
                directory.toURI().toURL()
        };
        ClassLoader loader = new URLClassLoader(urls);
        ResourceBundle rb = Utf8ResourceBundle.getBundle("text", currentLocale, loader);
        return rb;
    }

    public void setCurrentLocale(Locale locale) {
        LanguageManager.currentLocale = locale;
        df = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);
    }

    public Locale getCurrentLocale() {
        return LanguageManager.currentLocale;
    }

    public static DateFormat getDateFormat() {
        return LanguageManager.df;
    }

}