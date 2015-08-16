package get.wordy.app.ui.util;

import get.wordy.app.ui.UISettings;
import get.wordy.core.bean.Card;
import get.wordy.core.bean.Definition;
import get.wordy.core.bean.Meaning;
import get.wordy.core.bean.wrapper.GramUnit;

import javax.swing.text.*;
import java.awt.*;
import java.util.List;

public class MeaningPreviewHelper {

    public static final String NEW_LINE_DELIMITER = "\n";
    public static final String EXAMPLES_DELIMITER = ";";
    public static final String DASH_DELIMITER = "-";
    public static final String LONG_DASH_DELIMITER = "—";

    private interface WHITESPACE {
        String SINGLE = " ";
        String DOUBLE = SINGLE + SINGLE;
        String TRIPLE = DOUBLE + SINGLE;
        String QUADRO = DOUBLE + DOUBLE;
    }

    public enum DOCUMENT_PREVIEW {
        EDIT,
        EXERCISE,
        FULL
    }

    private static final String MAIN_NUMBER = "MainNumberStyle";
    private static final String PART_OF_SPEECH = "PartOfSpeechStyle";
    private static final String TRANSLATION = "TranslationStyle";
    private static final String SYN_ANT = "SynAntStyle";
    private static final String WORD = "WordStyle";
    private static final String EXAMPLE = "ExampleStyle";

    final private Document document;

    final private Style Style1;
    final private Style Style2;
    final private Style Style3;
    final private Style Style4;
    final private Style Style5;
    final private Style Style6;

    /**
     * Creates styled document
     *
     * @param settings
     */
    public MeaningPreviewHelper(UISettings settings) {
        Font font = settings.getFont();
        int fontSize = font.getSize();
        String fontName = font.getFontName();

        // Create the StyleContext, the document and the pane
        StyleContext sc = new StyleContext();
        document = new DefaultStyledDocument(sc);
        document.putProperty(DefaultEditorKit.EndOfLineStringProperty, '\n');

        // Create style of main number of part of speech with point
        Style1 = sc.addStyle(MAIN_NUMBER, null);
        StyleConstants.setFontFamily(Style1, fontName);
        StyleConstants.setFontSize(Style1, fontSize);
        StyleConstants.setBold(Style1, true);

        // Create style of part of speech caption with italic font
        Style2 = sc.addStyle(PART_OF_SPEECH, null);
        StyleConstants.setFontFamily(Style2, fontName);
        StyleConstants.setFontSize(Style2, fontSize);
        StyleConstants.setItalic(Style2, true);
        StyleConstants.setForeground(Style2, Color.GREEN);

        // Create style of number of translation variant with round bracket
        Style3 = sc.addStyle(TRANSLATION, null);
        StyleConstants.setFontFamily(Style3, fontName);
        StyleConstants.setFontSize(Style3, fontSize);

        // Create style of synonyms & antonyms caption
        Style4 = sc.addStyle(SYN_ANT, null);
        StyleConstants.setFontFamily(Style4, fontName);
        StyleConstants.setFontSize(Style4, fontSize);
        StyleConstants.setBold(Style4, true);
        StyleConstants.setForeground(Style4, Color.GRAY);

        // Create style of a word
        Style5 = sc.addStyle(WORD, null);
        StyleConstants.setFontFamily(Style5, fontName);
        StyleConstants.setFontSize(Style5, fontSize);
        StyleConstants.setForeground(Style5, Color.BLUE);

        // Create style of sentences examples
        Style6 = sc.addStyle(EXAMPLE, null);
        StyleConstants.setFontFamily(Style6, fontName);
        StyleConstants.setFontSize(Style6, fontSize);
        StyleConstants.setForeground(Style6, Color.LIGHT_GRAY);
    }

    private Document newTranslationPreview(Card card, DOCUMENT_PREVIEW documentPreview) {

        int documentLength = 0;

        try {
            int orderNumber = 1;

            if (documentPreview == DOCUMENT_PREVIEW.FULL) {
                String transcript = "[";
                transcript += card.getWord().getTranscription();
                transcript += "]";
                transcript += '\n';
                document.insertString(documentLength, transcript, Style1);
                documentLength += transcript.length();
            }

            List<Definition> definitions = card.getDefinitions();

            for (Definition definition : definitions) {

                GramUnit gramUnit = definition.getGramUnit();

                // create new paragraph
                String strStyle1 = "";
                if (orderNumber != 1) {
                    strStyle1 += '\n';
                }

                strStyle1 += String.valueOf(orderNumber++) + "." + WHITESPACE.SINGLE;

                document.insertString(documentLength, strStyle1, Style1);
                documentLength = documentLength + strStyle1.length();

                String strStyle2 = GramUnit.elementNameAt(gramUnit.getIndex()) + '\n';
                document.insertString(documentLength, strStyle2, Style2);
                documentLength = documentLength + strStyle2.length();

                // definition :
                String value = WHITESPACE.DOUBLE + definition.getValue();
                document.insertString(documentLength, value, Style1);
                documentLength = documentLength + value.length();

                // meanings :
                List<Meaning> meanings = definition.getMeanings();

                for (int j = 0; j < meanings.size(); j++) {

                    Meaning meaning = meanings.get(j);

                    switch (documentPreview) {
                        case EDIT:
                        case FULL:
                            final String translation = meaning.getTranslation();
                            if (translation != null && translation.length() > 0) {
                                String number = '\n' + WHITESPACE.DOUBLE
                                        + String.valueOf(j + 1) + ")";
                                document.insertString(documentLength, number, Style3);
                                documentLength = documentLength + number.length();

                                String words = WHITESPACE.SINGLE + translation;
                                document.insertString(documentLength, words, Style3);
                                documentLength = documentLength + words.length();
                            }
                    }

                    final String synonym = meaning.getSynonym();
                    if (synonym.length() > 0) {

                        String strStyle4 = '\n' + WHITESPACE.TRIPLE
                                + String.valueOf("Syn:");
                        document.insertString(documentLength, strStyle4, Style4);
                        documentLength = documentLength + strStyle4.length();

                        String strStyle5 = '\n' + WHITESPACE.TRIPLE + synonym;
                        document.insertString(documentLength, strStyle5, Style5);
                        documentLength = documentLength + strStyle5.length();
                    }

                    final String antonym = meaning.getAntonym();
                    if (antonym.length() > 0) {
                        String strStyle4 = '\n' + WHITESPACE.TRIPLE
                                + String.valueOf("Ant:");
                        document.insertString(documentLength, strStyle4, Style4);
                        documentLength = documentLength + strStyle4.length();

                        String strStyle5 = '\n' + WHITESPACE.TRIPLE + antonym;
                        document.insertString(documentLength, strStyle5, Style5);
                        documentLength = documentLength + strStyle5.length();
                    }

                    final String examples = meaning.getExample();
                    if (examples != null && examples.length() > 0) { // TODO string tools
                        String[] array = examples.split(EXAMPLES_DELIMITER);
                        for (String s : array) {
                            String str = '\n' + WHITESPACE.QUADRO + s;
                            switch (documentPreview) {
                                case EXERCISE: {
                                    String[] arr = s.split(LONG_DASH_DELIMITER);
                                    if (arr.length == 2)
                                        str = '\n' + WHITESPACE.QUADRO + arr[1];
                                }
                                default: {
                                    document.insertString(documentLength, str, Style6);
                                    documentLength = documentLength + str.length();
                                }
                            }
                        }
                    }
                }
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
            return null;
        }
        return document;
    }

    public Document createDocument(Card card, DOCUMENT_PREVIEW documentPreview) {
        return newTranslationPreview(card, documentPreview);
    }

}