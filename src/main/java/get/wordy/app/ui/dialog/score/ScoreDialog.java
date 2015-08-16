package get.wordy.app.ui.dialog.score;

import get.wordy.app.resources.R;
import get.wordy.app.resources.Resources;
import get.wordy.app.ui.MainWindow;
import get.wordy.app.ui.UISettings;
import get.wordy.app.ui.WordyMediator;
import get.wordy.app.ui.component.Button;
import get.wordy.app.ui.component.Label;
import get.wordy.app.ui.localizable.Localized;
import get.wordy.core.api.IScore;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ScoreDialog extends JDialog implements Localized {

    private final ScoreDialogPresenter presenter;

    private Resources resources;
    private UISettings settings;
    private String dictionaryName;

    private JPanel displayPanel;
    private StatusPairPanel editPanel;
    private Label cardsAndStatusesLabel;
    private StatusPairPanel postponedPanel;
    private StatusPairPanel learntPanel;
    private StatusPairPanel toLearnPanel;
    private StatusPairPanel totalCardsPanel;
    private Button okButton, resetButton;

    public ScoreDialog(MainWindow mainWindow, WordyMediator mediator, String dictionaryName, IScore score) {
        super(mainWindow, true);
        this.resources = mediator.getResources();
        this.settings = mediator.getUiSettings();
        this.dictionaryName = dictionaryName;

        this.presenter = new ScoreDialogPresenter(this, mediator);

        init(score);
    }

    private void init(IScore score) {
        totalCardsPanel = new StatusPairPanel(MSG_LABEL_CARDS_TOTAL, dictionaryName, score.getTotalCount());
        cardsAndStatusesLabel = new Label(resources, settings, MSG_LABEL_CARDS_AND_STATUSES);
        editPanel = new StatusPairPanel(EDIT, score.getEditCnt());
        postponedPanel = new StatusPairPanel(POSTPONED, score.getPostponedCnt());
        toLearnPanel = new StatusPairPanel(TO_LEARN, score.getToLearnCnt());
        learntPanel = new StatusPairPanel(LEARNT, score.getLearntCnt());

        ResetScoreAction action = new ResetScoreAction();
        okButton = new Button(action, resources, settings, MSG_BUTTON_OK);
        resetButton = new Button(action, resources, settings, MSG_BUTTON_RESET);

        createLayout();
        add(displayPanel);

        pack();
        setTitle(resources.translate(MSG_STATISTIC_WINDOW));
        setLocationRelativeTo(getOwner());
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setIconImage(new ImageIcon(R.icon.MAIN_ICON).getImage());
    }

    private void createLayout() {

        displayPanel = new JPanel();
        displayPanel.setLayout(new BorderLayout());
        displayPanel.setBorder(new EmptyBorder(7, 10, 7, 10));

        GridBagLayout gridBag = new GridBagLayout();
        JPanel centerPanel = new JPanel(gridBag);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;

        // #1
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 4;
        constraints.gridheight = 1;
        constraints.weightx = 1;
        constraints.weighty = 0;
        centerPanel.add(totalCardsPanel.getTextLabel(), constraints);

        constraints.gridx = 4;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 0;
        constraints.weighty = 0;
        centerPanel.add(totalCardsPanel.getScoreLabel(), constraints);

        // #2
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 4;
        constraints.gridheight = 1;
        constraints.weightx = 1;
        constraints.weighty = 0;
        constraints.insets = new Insets(7, 0, 7, 0);
        centerPanel.add(cardsAndStatusesLabel, constraints);
        constraints.insets = new Insets(0, 0, 0, 0);

        // #3
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 4;
        constraints.gridheight = 1;
        constraints.weightx = 1;
        constraints.weighty = 0;
        centerPanel.add(editPanel.getTextLabel(), constraints);

        constraints.gridx = 4;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 0;
        constraints.weighty = 0;
        centerPanel.add(editPanel.getScoreLabel(), constraints);

        // #4
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 4;
        constraints.gridheight = 1;
        constraints.weightx = 1;
        constraints.weighty = 0;
        centerPanel.add(postponedPanel.getTextLabel(), constraints);

        constraints.gridx = 4;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 0;
        constraints.weighty = 0;
        centerPanel.add(postponedPanel.getScoreLabel(), constraints);

        // #5
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 4;
        constraints.gridheight = 1;
        constraints.weightx = 1;
        constraints.weighty = 0;
        centerPanel.add(toLearnPanel.getTextLabel(), constraints);

        constraints.gridx = 4;
        constraints.gridy = 4;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 0;
        constraints.weighty = 0;
        centerPanel.add(toLearnPanel.getScoreLabel(), constraints);

        // #6
        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.gridwidth = 4;
        constraints.gridheight = 1;
        constraints.weightx = 1;
        constraints.weighty = 0;
        centerPanel.add(learntPanel.getTextLabel(), constraints);

        constraints.gridx = 4;
        constraints.gridy = 5;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 0;
        constraints.weighty = 0;
        centerPanel.add(learntPanel.getScoreLabel(), constraints);

        constraints.gridx = 5;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 6;
        constraints.weightx = 1;
        constraints.weighty = 0;
        centerPanel.add(new JPanel(), constraints);

        editPanel.withLeftPadding();
        postponedPanel.withLeftPadding();
        toLearnPanel.withLeftPadding();
        learntPanel.withLeftPadding();

        Box buttonPanel = Box.createHorizontalBox();
        buttonPanel.add(resetButton);
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(okButton);

        displayPanel.add(centerPanel, BorderLayout.NORTH);
        displayPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private class StatusPairPanel {

        private Label textLbl, scoreLbl;

        private static final int LEFT_PAD = 15;

        private StatusPairPanel(String textLabelKey, int scoreValue) {
            textLbl = new Label(resources, settings, textLabelKey);
            scoreLbl = new Label(resources, settings, String.valueOf(scoreValue));
        }

        private StatusPairPanel(String textLabelKey, String dictionaryName, int totalCount) {
            textLbl = new Label(resources, settings, textLabelKey, dictionaryName);
            scoreLbl = new Label(resources, settings, String.valueOf(totalCount));
        }

        Label getTextLabel() {
            return textLbl;
        }

        Label getScoreLabel() {
            return scoreLbl;
        }

        void withLeftPadding() {
            textLbl.setBorder(new EmptyBorder(0, LEFT_PAD, 0, 0));
        }

    }

    private class ResetScoreAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            if (source == resetButton) {
                presenter.resetScore(dictionaryName);
            }
            ScoreDialog.this.setVisible(false);
            ScoreDialog.this.dispose();
        }

    }

}
