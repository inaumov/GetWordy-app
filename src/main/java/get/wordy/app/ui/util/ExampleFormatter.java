package get.wordy.app.ui.util;

// TODO unit test
public class ExampleFormatter {

    private String string;

    public ExampleFormatter(String examplesLine) {
        this.string = examplesLine;
    }

    public String forPreview() {
        if (isEmpty()) {
            return null;
        }
        String[] result = string.split(MeaningPreviewHelper.EXAMPLES_DELIMITER);
        StringBuilder line = new StringBuilder();
        for (int x = 0; x < result.length; x++) {
            line.append(result[x]);
            line.append(MeaningPreviewHelper.NEW_LINE_DELIMITER);
        }
        String examplesLine = line.toString();

        return examplesLine;
    }

    public String forDatabase() {
        if (isEmpty()) {
            return null;
        }
        String[] result = string.split(MeaningPreviewHelper.NEW_LINE_DELIMITER);
        StringBuilder line = new StringBuilder();
        for (int x = 0; x < result.length; x++) {
            line.append(result[x].trim());
            line.append(MeaningPreviewHelper.EXAMPLES_DELIMITER);
        }
        String examplesLine = line.toString();

        return examplesLine;
    }

    private boolean isEmpty() {
        if (string == null || string.isEmpty()) {
            return true;
        }
        return false;
    }

}