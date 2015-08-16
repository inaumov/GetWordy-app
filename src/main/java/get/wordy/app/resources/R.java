package get.wordy.app.resources;

import get.wordy.app.ui.util.ScreenSize;

public interface R {

    public static final String _version = "v1.0.0";

    public static final class dimension {
        public static final int MAIN_FRAME_WIDTH        = ScreenSize.getWidth() / 2;
        public static final int MAIN_FRAME_HEIGHT       = ScreenSize.getHeight() / 2;
        public static final int OPTIONS_DIALOG_WIDTH    = 427;
        public static final int OPTIONS_DIALOG_HEIGHT   = 377;
    }

    public static final class icon {
        public static final String CREATE_CARD_SMALL    = "images/menu/create_card_small.png";
        public static final String EDIT_CARD_SMALL      = "images/menu/edit_card_small.png";
        public static final String DELETE_CARD_SMALL    = "images/menu/delete_card_small.png";
        public static final String EXIT_SMALL           = "images/menu/exit_small.png";
        public static final String TRANSLATE_IN_GOOGLE_SMALL = "images/menu/google_small.png";

        public static final String CREATE_CARD_LARGE    = "images/button/create_card.png";
        public static final String EDIT_CARD_LARGE      = "images/button/edit_card.png";
        public static final String DELETE_CARD_LARGE    = "images/button/delete_card.png";
        public static final String SCHEDULE_LARGE       = "images/button/schedule.png";
        public static final String ADD_MEANING_LARGE    = "images/button/add_meaning.png";

        public static final String MAIN_ICON            = "images/main_icon.png";

        public static final String SAD                  = "images/answer/sad.png";
        public static final String NORMAL               = "images/answer/normal.png";
        public static final String LAUGHING             = "images/answer/laughing.png";
    }

    public static final class filename {
        public static final String APP_FOLDER_NAME      = ".GetWordy";
        public static final String PROPERTIES_FILENAME  = "get-wordy.properties";
    }

}