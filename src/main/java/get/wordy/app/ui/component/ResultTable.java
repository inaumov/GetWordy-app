package get.wordy.app.ui.component;

import get.wordy.app.resources.Resources;
import get.wordy.app.ui.IReloadable;
import get.wordy.app.ui.UISettings;
import get.wordy.app.ui.localizable.Localized;
import get.wordy.app.ui.tablemodel.ResultTableModel;
import get.wordy.app.ui.util.ScreenSize;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.Enumeration;
import java.util.Set;

public class ResultTable extends JTable implements Localized, IReloadable {

    private static final int PLUS_HEIGHT = 7;

    private Resources resources;
    private UISettings settings;

    int SMALL = 75;
    int MIDDLE = 100;
    int BIG = 125;

    public ResultTable(AbstractTableModel tableModel, Resources resources, UISettings settings) {
        super(tableModel);
        this.resources = resources;
        this.settings = settings;
        customize();
    }

    private void customize() {
        // Allow selection to span one contiguous set of rows,
        // visible columns, or block of cells
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        setFocusable(false);
        setFont(settings.getFont());
        setShowGrid(false);
        setRowSelectionAllowed(true);
        setIntercellSpacing(new Dimension(0, 0));

        getTableHeader().setReorderingAllowed(false);
        getTableHeader().setFont(settings.getFont());

        updateHeaders();
        resizeColumns();
        resizeRows();
    }

    private void resizeColumns() {
        int count = getModel().getColumnCount();
        TableColumn[] column = new TableColumn[count];

        double v = (double) settings.getFont().getSize() / 14;
        for (int i = 0; i < count; i++) {
            column[i] = getColumnModel().getColumn(i);
            switch (i) {
                case 0: {
                    setColumnWidth(column[i], (int) (MIDDLE * v));
                    break;
                }
                case 1: {
                    setColumnWidth(column[i], (int) (SMALL * v));
                    break;
                }
                case 4: {
                    setColumnWidth(column[i], (int) (BIG * v));
                    break;
                }
                default: {
                    final int w = (int) (BIG * v * 2);
                    column[i].setPreferredWidth(w);
                    column[i].setMaxWidth(ScreenSize.getWidth() / 5 + w);
                    break;
                }
            }
        }
    }

    private void setColumnWidth(TableColumn tableColumn, final int w) {
        tableColumn.setPreferredWidth(w);
        tableColumn.setMaxWidth(w);
    }

    private void resizeRows() {
        final int size = settings.getFont().getSize();
        double rate = (double) size / 14;
        int dh = (int) (PLUS_HEIGHT * rate);
        setRowHeight(size + dh);
    }

    public Set<String> getWords() {
        ResultTableModel m = (ResultTableModel) this.getModel();
        return m.getKeySet();
    }

    public void updateHeaders() {
        Enumeration<TableColumn> column = getColumnModel().getColumns();
        setHeader(column.nextElement(), MSG_TABLEHEADER_STATUS);
        setHeader(column.nextElement(), MSG_TABLEHEADER_RATING);
        setHeader(column.nextElement(), MSG_TABLEHEADER_WORD);
        setHeader(column.nextElement(), MSG_TABLEHEADER_TRANSCRIPTION);
        setHeader(column.nextElement(), MSG_TABLEHEADER_DATE_ADDED);
    }

    private void setHeader(TableColumn column, String key) {
        column.setHeaderValue(resources.translate(key));
    }

    @Override
    public void reloadText() {
        updateHeaders();
        // Force the header to resize and repaint itself
        getTableHeader().resizeAndRepaint();
    }

    @Override
    public void reloadFont() {
        getTableHeader().setFont(settings.getFont());
        setFont(settings.getFont());
        resizeColumns();
        resizeRows();
    }

}