package get.wordy.app.ui.tablemodel;

import get.wordy.app.settings.LanguageManager;
import get.wordy.core.bean.Card;
import get.wordy.core.bean.Word;
import get.wordy.core.bean.wrapper.CardStatus;

import javax.swing.table.AbstractTableModel;
import java.text.DateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;

public class ResultTableModel extends AbstractTableModel {

    public static final int WORD_COLUMN_INDEX = 2;

    private Map<String, Card> data;

    public ResultTableModel(Map<String, Card> data) {
        this.data = data;
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Card card = (Card) data.values().toArray()[rowIndex];
        Word word = card.getWord();
        Object result = null;
        switch (columnIndex) {
            case 0: {
                result = CardStatus.getValue(card.getStatus());
                break;
            }
            case 1: {
                result = card.getRating();
                break;
            }
            case WORD_COLUMN_INDEX: {
                result = word.getValue();
                break;
            }
            case 3: {
                result = word.getTranscription();
                break;
            }
            case 4: {
                result = getLocalizedDate(card.getInsertTime());
                break;
            }
        }
        return result; // Apparently it's OK to return null for a "blank" cell
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public Set<String> getKeySet() {
        return data.keySet();
    }

    private Object getLocalizedDate(Date date) {
        DateFormat df = LanguageManager.getDateFormat();
        return df.format(date);
    }

}