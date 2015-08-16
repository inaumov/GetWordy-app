package get.wordy.app.ui.frame.card;

import get.wordy.app.ui.WordyMediator;
import get.wordy.app.ui.action.listener.FocusListenerAdapter;
import get.wordy.app.ui.action.listener.WordFieldListenerAdapter;
import get.wordy.app.ui.component.Button;
import get.wordy.app.ui.component.ExampleTextArea;
import get.wordy.app.ui.component.Label;
import get.wordy.core.bean.wrapper.CardStatus;
import get.wordy.core.bean.Card;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class CardWindow extends BaseCardWindow {

    private CardWindowPresenter presenter;

    private Label transcriptionLabel;
    private CardTextField transcriptionField;
    private Button saveButton, cancelButton;
    private SaveValues saveValues = new SaveValues();
    private Card card;

    public CardWindow(Card card, WordyMediator mediator) {
        super(mediator);

        this.card = card;
        presenter.setCard(card);
        presenter.fillData();
    }

    void init() {
        presenter = new CardWindowPresenter(this, mediator, null);

        super.init();
        setTitle(resources.translate(MSG_CARD_WINDOW));
    }

    @Override
    protected void createAdditionalComponents() {
        createTranscriptionLabel();
        createTranscriptionField();
        createSaveButton();
        createCancelButton();
    }

    private void createTranscriptionLabel() {
        transcriptionLabel = new Label(resources, settings, MSG_LABEL_TRANSCRIPTION);
        initLabels.add(transcriptionLabel);
    }

    private void createTranscriptionField() {
        transcriptionField = new CardTextField(32);
        transcriptionField.setBackground(Color.WHITE);
        transcriptionField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        transcriptionField.addFocusListener(new FocusListenerAdapter() {
            @Override
            public void focusGained() {
                transcriptionField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            }

            @Override
            public void focusLost() {
                transcriptionField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            }
        });

        initFields.add(transcriptionField);
    }

    private void createSaveButton() {
        saveButton = new Button(new SaveCardAction(), resources, settings, MSG_BUTTON_OPTIONS_SAVE);
        saveButton.setToolTipText(resources.translate(TOOLTIP_SAVE_CARD));
    }

    private void createCancelButton() {
        cancelButton = new Button(new CancelCardAction(), resources, settings, MSG_BUTTON_OPTIONS_CANCEL);
        cancelButton.setToolTipText(resources.translate(TOOLTIP_CANCEL_CARD));
    }

    @Override
    void createExampleTextArea() {
        super.createExampleTextArea();
        exampleTextArea.addMouseListener(new ExampleAreaClickListener());
    }

    @Override
    protected Box createLayout() {
        Box mainBox = super.createLayout();
        mainBox.add(getWordLabel());

        JPanel secondBox = new JPanel(new BorderLayout());
        final JTextField wordField = getWordField();
        secondBox.add(wordField, BorderLayout.WEST);
        Box statusBox = createStatusBox();
        secondBox.add(statusBox, BorderLayout.EAST);
        mainBox.add(secondBox);
        mainBox.add(Box.createVerticalStrut(7));

        Box lblTranscript = Box.createHorizontalBox();
        lblTranscript.add(transcriptionLabel);
        lblTranscript.add(Box.createHorizontalGlue());
        mainBox.add(lblTranscript);

        JPanel thirdBox = new JPanel(new BorderLayout());
        thirdBox.add(transcriptionField, BorderLayout.WEST);
        Box btnBox = createButtonsBox(saveButton, cancelButton);
        thirdBox.add(btnBox, BorderLayout.EAST);
        mainBox.add(thirdBox);
        mainBox.add(Box.createVerticalStrut(7));

        mainBox.add(createDefinitionBox());
        return mainBox;
    }

    public boolean isContentValid() {
        return wordField.getText().length() >= 2;
    }

    public void highlightWordFieldBorder() {
        wordField.setBorder(BorderFactory.createLineBorder(Color.RED));
    }

    @Override
    public void reloadText() {
        setTitle(resources.translate(MSG_CARD_WINDOW));
        super.reloadText();
        saveButton.reloadText();
        cancelButton.reloadText();
    }

    @Override
    public void reloadFont() {
        setFont(settings.getFont());
        super.reloadFont();
        saveButton.reloadFont();
        cancelButton.reloadFont();
    }

    public void hideWindow() {
        setVisible(false);
        dispose();
    }

    public void setTranscription(String s) {
        transcriptionField.setText(s);
    }

    @Override
    protected WordFieldListenerAdapter getWordFieldListener() {
        return new WordFieldListenerAdapter() {
            @Override
            public void focusGained() {
                wordField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            }

            @Override
            public void focusLost() {
                wordField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            }
        };
    }

    public CardWindowPresenter.IUiCardValues getUiCardValues() {
        return saveValues;
    }

    private class SaveValues implements CardWindowPresenter.IUiCardValues {

        @Override
        public String getWord() {
            return wordField.getText();
        }

        @Override
        public String getTranscription() {
            return transcriptionField.getText();
        }

        @Override
        public CardStatus getStatus() {
            CardStatus status = CardStatus.elementAt(statusComboBox.getSelectedIndex());
            return status;
        }
    }

    private class ExampleAreaClickListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent event) {
            if (event.getClickCount() == 2 && !event.isConsumed()) {
                event.consume();
                ExampleTextArea textArea = (ExampleTextArea) event.getSource();
                boolean isNew = textArea.getDocument().getLength() == 0;
                presenter.loadMeanings(isNew);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

    }

    private class CancelCardAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent action) {
            presenter.cancel();
        }

    }

    private class SaveCardAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent action) {
            presenter.saveCard();
        }

    }

}