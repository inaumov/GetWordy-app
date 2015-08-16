package get.wordy.app.settings;

import get.wordy.app.resources.R;
import get.wordy.app.resources.Resources;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static get.wordy.app.settings.FontUtil.MAX_SIZE;
import static get.wordy.app.settings.FontUtil.MIN_SIZE;

public final class SettingsManager {

    private static final String LOG_TAG = SettingsManager.class.getName();
    private static final Logger logger = Logger.getLogger(LOG_TAG);
    private static final String USER_HOME = System.getProperty("user.home");

    private Properties defaultProps = new Properties();
    private Properties appProps = null;

    private Map<Setting, List<PropertyChangeListener>> listeners = null;

    private static String USER_PROPERTIES_PATH;

    static {
        USER_PROPERTIES_PATH = new StringBuilder(USER_HOME)
                .append(File.separator)
                .append(R.filename.APP_FOLDER_NAME)
                .append(File.separator)
                .append(R.filename.PROPERTIES_FILENAME)
                .toString();
    }

    private boolean isLoaded;

    public SettingsManager() {
        load();
        Resources resources = Resources.getResources();
        resources.setMessageBundle(new LanguageManager(getLanguage()).getMessageBundle());
    }

    private void load() {
        try {
            checkAndCreateUserDir();
            // create and load default properties
            InputStream in = getClass().getClassLoader().getResourceAsStream(R.filename.PROPERTIES_FILENAME);
            defaultProps.load(in);
            in.close();

            // create application properties with default
            appProps = new Properties(defaultProps);

            // user properties
            in = new FileInputStream(USER_PROPERTIES_PATH);
            appProps.load(in);
            in.close();

            isLoaded = true;
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Error loading from property file", ex);
            isLoaded = false;
        }
        logger.log(Level.INFO, "Test loading from property file");
    }

    public void store() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                if (!isLoaded) {
                    try {
                        checkAndCreateUserDir();
                        File propertyFile = new File(USER_PROPERTIES_PATH);
                        propertyFile.createNewFile();
                    } catch (IOException ex) {
                        logger.log(Level.SEVERE, "Error creating property file", ex);
                    }
                }
                try {
                    FileOutputStream out = new FileOutputStream(USER_PROPERTIES_PATH);
                    appProps.store(out, "---User properties---");
                    out.close();
                } catch (IOException io) {
                    logger.log(Level.SEVERE, "Error modifying properties", io);
                }
            }
        }).start();
    }

    private void checkAndCreateUserDir() throws IOException {
        final File homeDir = new File(USER_HOME);
        final File dir = new File(homeDir, R.filename.APP_FOLDER_NAME);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("Unable to create " + dir.getAbsolutePath());
        }
    }

    public String getProperty(String key) {
        String val = null;
        if (key != null) {
            if (appProps != null)
                val = appProps.getProperty(key);
            if (val == null) {
                val = defaultProps.getProperty(key);
            }
        }
        return val;
    }

    public void setProperty(Setting key, String val) {

        ArrayList list;
        Object oldValue;

        oldValue = getProperty(key.name());

        appProps.setProperty(key.name(), val);
        if (listeners.containsKey(key)) {
            list = (ArrayList) listeners.get(key);
            int len = list.size();
            if (len > 0) {
                PropertyChangeEvent evt = new PropertyChangeEvent(this, key.name(), oldValue, val);
                for (int i = 0; i < len; i++) {
                    if (list.get(i) instanceof PropertyChangeListener) {
                        PropertyChangeListener listener = (PropertyChangeListener) list.get(i);
                        listener.propertyChange(evt);
                    }
                }
            }
        }
    }

    public boolean addListener(Setting key, PropertyChangeListener listener) {

        boolean added;

        if (listeners == null) {
            listeners = new HashMap<Setting, List<PropertyChangeListener>>();
        }

        List<PropertyChangeListener> list;

        if (!listeners.containsKey(key)) {
            list = new ArrayList<PropertyChangeListener>();
            added = list.add(listener);
            listeners.put(key, list);
        } else {
            list = listeners.get(key);
            added = list.add(listener);
        }

        return added;
    }

    public void removeListener(PropertyChangeListener listener) {
        if (listeners != null && listeners.size() > 0)
            listeners.remove(listener);
    }

    public Language getLanguage() {
        String key = Setting.LANGUAGE.name();
        String value = getProperty(key);
        return Language.valueOf(value);
    }

    /**
     * Sets <code>Language</code> object.
     */
    public void saveLanguage(final Language val) {
        checkNotNull(val);
        setProperty(Setting.LANGUAGE, val.name());
    }

    public Font getFont() {
        String key = Setting.FONT.name();
        String value = getProperty(key);
        String font[] = value.split(";");
        String fontName = font[0];
        String fontSize = font[1];
        return new Font(fontName, Font.PLAIN, Integer.valueOf(fontSize));
    }

    /**
     * Sets <code>Font</code> object.
     */
    public void saveFont(final Font val) {
        checkNotNull(val);
        setProperty(Setting.FONT, fontToString(val));
    }

    public Boolean isChecked(Setting key) {
        String str = getProperty(key.name());
        if (isBoolean(str))
            return Boolean.valueOf(str);
        else {
            return false;
        }
    }

    /**
     * Sets <code>Boolean</code> value.
     */
    public void saveChecked(final Setting key, final Boolean val) {
        checkNotNull(val);
        setProperty(key, String.valueOf(val));
    }

    public Number getNumber(Setting key) {
        String str = getProperty(key.name());
        if (isNumeric(str)) {
            int numb = Integer.valueOf(str);
            return numb;
        } else {
            return 0;
        }
    }

    /**
     * Sets <code>Number</code> value.
     */
    public void saveNumber(final Setting key, final Number val) {
        checkNotNull(val);
        setProperty(key, String.valueOf(val));
    }

    public String getDictionaryName() {
        String key = Setting.DICTIONARY_NAME.name();
        return getProperty(key);
    }

    public void saveDictionaryName(String val) {
        setProperty(Setting.DICTIONARY_NAME, val);
    }

    private static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }

    public static boolean isNumeric(Object obj) {
        String str = String.valueOf(obj);
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c))
                return false;
        }
        return true;
    }

    public static boolean isBoolean(String str) {
        return str.equalsIgnoreCase("false") || str.equalsIgnoreCase("true");
    }

    public Properties getAppProperties() {
        return appProps;
    }

    public static String fontToString(Font font) {
        String fontName = font.getFontName();
        int fontSize = font.getSize();

        if (fontSize < MIN_SIZE || fontSize > MAX_SIZE) {
            throw new IllegalArgumentException();
        }

        String size = String.valueOf(fontSize);
        String val = (fontName + ";" + size);
        return val;
    }

    public static Color findOppositeColor(Color color) {

        checkNotNull(color);

        class OppositeColorConverter {

            int red, green, blue;

            OppositeColorConverter(Color color) {
                red = find(color.getRed());
                green = find(color.getGreen());
                blue = find(color.getBlue());
            }

            int find(int num) {
                return (num ^ 0x80) & 0xff;
            }

            Color invert() {
                return new Color(red, green, blue);
            }
        }

        return new OppositeColorConverter(color).invert();
    }

}