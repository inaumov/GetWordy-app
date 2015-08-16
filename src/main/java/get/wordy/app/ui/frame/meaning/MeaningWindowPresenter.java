package get.wordy.app.ui.frame.meaning;

import get.wordy.app.ui.WordyMediator;
import get.wordy.app.ui.util.ExampleFormatter;
import get.wordy.core.bean.Card;
import get.wordy.core.bean.Definition;
import get.wordy.core.bean.Meaning;
import get.wordy.core.bean.wrapper.GramUnit;

import java.util.ArrayList;
import java.util.List;

public class MeaningWindowPresenter {

    private MeaningWindow meaningWindow;
    private WordyMediator mediator;
    private Card card;

    public MeaningWindowPresenter(Card card, WordyMediator mediator, MeaningWindow meaningWindow) {
        this.meaningWindow = meaningWindow;
        this.mediator = mediator;
        this.card = card;
    }

    public void addMeanings() {

        IUiMeaningValues meaningValues = meaningWindow.getUiMeaningValues();

        card.removeAllDefinitions();

        for (GramUnit gramUnit : GramUnit.values()) {

            ArrayList<Meaning> meanings = new ArrayList<Meaning>();

            List<Integer> ids = meaningValues.getMeaningIds(gramUnit);
            List<String> translations = meaningValues.getTranslationText(gramUnit);
            List<String> synonyms = meaningValues.getSynonymText(gramUnit);
            List<String> antonyms = meaningValues.getAntonymText(gramUnit);
            List<String> examples = meaningValues.getExamplesText(gramUnit);

            for (int i = 0; i < ids.size(); i++) {
                Meaning meaning = new Meaning();
                meaning.setId(ids.get(i));
                meaning.setTranslation(translations.get(i));
                meaning.setSynonym(synonyms.get(i));
                meaning.setAntonym(antonyms.get(i));
                meaning.setExample(format(examples.get(i)));
                meanings.add(meaning);
            }

            if (isEmpty(meaningValues.getDefinitionText(gramUnit)) && meanings.isEmpty()) {
                continue;
            }

            Definition definition = new Definition();
            definition.setId(meaningValues.getDefinitionId(gramUnit));
            definition.setGramUnit(gramUnit);
            definition.setValue(meaningValues.getDefinitionText(gramUnit));

            definition.addAll(meanings);

            card.add(definition);
        }

        mediator.previewCardDocumentOnEdit(card);
        meaningWindow.dispose();
    }

    public void populateMeaningsListPanel() {

        List<Definition> definitions = card.getDefinitions();
        GramUnit firstOccurred = null;
        for (Definition definition : definitions) {
            GramUnit gramUnit = definition.getGramUnit();
            String value = definition.getValue();
            if (!isEmpty(value) && firstOccurred == null) {
                firstOccurred = gramUnit;
            }
            meaningWindow.addDefinitionArea(definition.getId(), gramUnit, value);
            for (Meaning meaning : definition.getMeanings()) {
                int id = meaning.getId();
                String translation = meaning.getTranslation();
                String synonym = meaning.getSynonym();
                String antonym = meaning.getAntonym();
                String example = meaning.getExample();
                meaningWindow.addMeaningPanel(gramUnit, id, translation, antonym, synonym, formatBack(example));
                if (!isEmpty(meaning) && firstOccurred == null) {
                    firstOccurred = gramUnit;
                }
            }

        }
        meaningWindow.selectPartOfSpeech(firstOccurred.getIndex());
    }

    public void newMeaningsListPanel() {
        meaningWindow.selectPartOfSpeech(GramUnit.defaultIndex());
    }

    private boolean isEmpty(Meaning meaning) {
        return isEmpty(meaning.getTranslation())
                && isEmpty(meaning.getSynonym())
                && isEmpty(meaning.getAntonym())
                && isEmpty(meaning.getExample());
    }

    private static boolean isEmpty(String s) {
        return s == null || s.isEmpty();
    }

    private static String format(String example) {
        return new ExampleFormatter(example).forDatabase();
    }

    private static String formatBack(String example) {
        return new ExampleFormatter(example).forPreview();
    }

    public interface IUiMeaningValues {

        Integer getDefinitionId(GramUnit gramUnit);

        public String getDefinitionText(GramUnit gramUnit);

        public List<Integer> getMeaningIds(GramUnit gramUnit);

        public List<String> getTranslationText(GramUnit gramUnit);

        public List<String> getSynonymText(GramUnit gramUnit);

        public List<String> getAntonymText(GramUnit gramUnit);

        public List<String> getExamplesText(GramUnit gramUnit);
    }

}