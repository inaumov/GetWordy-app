package get.wordy.app.settings;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;

public abstract class Utf8ResourceBundle {

    /**
     * Gets the unicode friendly resource bundle
     *
     * @param baseName
     * @param locale
     * @param loader
     * @return Unicode friendly resource bundle
     * @see ResourceBundle#getBundle(String, Locale, ClassLoader)
     */
    public static final ResourceBundle getBundle(String baseName, Locale locale, ClassLoader loader) {
        return new Utf8PropertyResourceBundle(ResourceBundle.getBundle(baseName, locale, loader));
    }

    /**
     * Resource Bundle that does the hard work
     */
    private static class Utf8PropertyResourceBundle extends ResourceBundle {

        /**
         * Bundle with unicode data
         */
        private final ResourceBundle bundle;

        /**
         * Initializing constructor
         *
         * @param bundle
         */
        private Utf8PropertyResourceBundle(final ResourceBundle bundle) {
            this.bundle = bundle;
        }

        @Override
        @SuppressWarnings("unchecked")
        public Enumeration getKeys() {
            return bundle.getKeys();
        }

        @Override
        protected Object handleGetObject(final String key) {
            final String value = bundle.getString(key);
            if (value == null)
                return null;
            try {
                return new String(value.getBytes("ISO-8859-1"), "UTF-8");
            } catch (final UnsupportedEncodingException e) {
                throw new RuntimeException("Encoding not supported", e);
            }
        }
    }
}  