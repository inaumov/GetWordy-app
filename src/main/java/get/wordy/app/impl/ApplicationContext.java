package get.wordy.app.impl;

import get.wordy.core.bean.Dictionary;

public class ApplicationContext {

    private static Dictionary dictionary;

    public static void selectDictionary(Dictionary dictionary) {
        ApplicationContext.dictionary = dictionary;
    }

    public static Dictionary getSelectedDictionary() {
        return dictionary;
    }

}