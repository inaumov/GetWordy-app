package get.wordy.app.ui.util;

import get.wordy.app.resources.Resources;
import get.wordy.app.ui.localizable.Localized;

import java.util.ArrayList;

public final class ComboListHelper {

    private static ArrayList<String> partsOfSpeech = new ArrayList<String>();
    private static ArrayList<String> cardStatuses = new ArrayList<String>();

    public static Object[] getComboList(final Resources resources, ComboList comboList) {

        switch (comboList) {

            case PART_OF_SPEECH: {

                if (!partsOfSpeech.isEmpty()) {
                    partsOfSpeech.clear();
                }
                partsOfSpeech.add(resources.translate(Localized.MSG_PART_OF_SPEECH_NOUN));
                partsOfSpeech.add(resources.translate(Localized.MSG_PART_OF_SPEECH_ADJECTIVE));
                partsOfSpeech.add(resources.translate(Localized.MSG_PART_OF_SPEECH_VERB));
                partsOfSpeech.add(resources.translate(Localized.MSG_PART_OF_SPEECH_ADVERB));
                partsOfSpeech.add(resources.translate(Localized.MSG_PART_OF_SPEECH_PHR_VERB));

                partsOfSpeech.add(resources.translate(Localized.MSG_PART_OF_SPEECH_PHRASE));
                partsOfSpeech.add(resources.translate(Localized.MSG_PART_OF_SPEECH_IDIOM));
                partsOfSpeech.add(resources.translate(Localized.MSG_PART_OF_SPEECH_OTHER));

                return partsOfSpeech.toArray();
            }

            case CARD_STATUS: {

                if (!cardStatuses.isEmpty()) {
                    cardStatuses.clear();
                }
                cardStatuses.add(resources.translate(Localized.EDIT));
                cardStatuses.add(resources.translate(Localized.POSTPONED));
                cardStatuses.add(resources.translate(Localized.TO_LEARN));
                cardStatuses.add(resources.translate(Localized.LEARNT));

                return cardStatuses.toArray();
            }
        }

        return null;
    }

    public static int getComboListSize(ComboList comboList) {

        switch (comboList) {

            case PART_OF_SPEECH: {
                if (partsOfSpeech.isEmpty())
                    throw new NullPointerException(
                            "Parts Of Speech ComboBox list was not initialized. Call initComboListArray method first.");
                return partsOfSpeech.size();
            }

            case CARD_STATUS: {
                if (cardStatuses.isEmpty())
                    throw new NullPointerException(
                            "Cards Statuses ComboBox list was not initialized. Call initComboListArray method first.");
                return cardStatuses.size();
            }
        }
        return -1;
    }

    public enum ComboList {
        PART_OF_SPEECH, CARD_STATUS
    }

}
