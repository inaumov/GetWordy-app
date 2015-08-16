package get.wordy.app.settings;

public enum FontName {

    ARIAL("Arial"),
    COURIER_NEW("Courier New"),
    HELVETICA("Helvetica"),
    LUCIDA_SANS_UNICODE("Lucida Sans Unicode"),
    MICROSOFT_SANS_SERIF("Microsoft Sans Serif"),
    TAHOMA("Tahoma"),
    TIMES_NEW_ROMAN("Times New Roman");

    private String fontName;

    FontName(String fontName) {
        this.fontName = fontName;
    }

    public String getName() {
        return this.fontName;
    }

    public static boolean matches(String name) {
        for (FontName f : FontName.values()) {
            if (f.getName().equals(name))
                return true;
        }
        return false;
    }

}