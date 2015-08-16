package get.wordy.app.ui.frame.card;

import get.wordy.app.resources.R;
import get.wordy.app.ui.WordyMediator;
import get.wordy.app.ui.action.listener.WordFieldListenerAdapter;
import get.wordy.app.ui.component.Button;
import get.wordy.app.ui.component.SwitchButton;
import get.wordy.core.bean.Card;
import get.wordy.core.bean.wrapper.CardStatus;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

public class ExerciseWindow extends BaseCardWindow {

    private ExerciseWindowPresenter presenter;

    private Button switchButton;
    private Button hintButton;

    private SwitchButton checkButton;
    private ImageLabel imageLabel;

    public ExerciseWindow(WordyMediator mediator, Collection<Card> exerciseList) {
        super(mediator);
        presenter = new ExerciseWindowPresenter(this, mediator, exerciseList);
    }

    @Override
    void init() {
        super.init();
        setTitle(resources.translate(MSG_EXERCISE_WINDOW));
    }

    @Override
    void createWordField() {
        super.createWordField();
        wordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_ENTER) {
                    // TODO WTF
                    Toolkit.getDefaultToolkit().beep();
                    checkButton.doClick();
                }
            }
        });
    }

    @Override
    void createStatusComboBox() {
        super.createStatusComboBox();
        statusComboBox.addItemListener(new ChangeCardStatusListener());
    }

    @Override
    protected void createAdditionalComponents() {
        String[] checkBtnKeys = {MSG_BUTTON_CHECK, MSG_BUTTON_NEXT};
        checkButton = new SwitchButton(new CheckCardAction(), resources, settings, checkBtnKeys);
        switchButton = new Button(new SwitchToDictionaryAction(), resources, settings, MSG_BUTTON_SWITCH_TO_DICTIONARY);
        hintButton = new Button(new HintCardAction(), resources, settings, MSG_BUTTON_HINT);
        hintButton.setPreferredSize(checkButton.getPreferredSize());
    }

    @Override
    protected Box createLayout() {
        Box mainBox = super.createLayout();
        JPanel firstBox = new JPanel(new BorderLayout());

        Box wordBox = Box.createVerticalBox();
        wordBox.add(getWordLabel());
        wordBox.add(getWordField());
        JPanel wordP = new JPanel(new BorderLayout());
        wordP.add(wordBox, BorderLayout.NORTH);

        firstBox.add(wordP, BorderLayout.WEST);
        Box pictureBox = Box.createVerticalBox();
        imageLabel = new ImageLabel();
        imageLabel.setImage(R.icon.NORMAL);
        pictureBox.add(imageLabel);
        firstBox.add(pictureBox, BorderLayout.EAST);
        mainBox.add(firstBox);
        mainBox.add(Box.createVerticalStrut(7));

        JPanel secondBox = new JPanel(new BorderLayout());
        Box btnBox = createButtonsBox(hintButton, checkButton);
        secondBox.add(btnBox, BorderLayout.EAST);
        mainBox.add(secondBox);
        mainBox.add(Box.createVerticalStrut(14));

        JPanel thirdBox = new JPanel(new BorderLayout());
        Box switchBox = Box.createHorizontalBox();
        switchBox.add(switchButton);
        switchBox.add(Box.createHorizontalGlue());
        thirdBox.add(switchBox, BorderLayout.WEST);
        Box statusBox = createStatusBox();
        thirdBox.add(statusBox, BorderLayout.EAST);
        mainBox.add(thirdBox);
        mainBox.add(Box.createVerticalStrut(7));

        mainBox.add(createDefinitionBox());

        return mainBox;
    }

    @Override
    protected WordFieldListenerAdapter getWordFieldListener() {
        return new WordFieldListenerAdapter() {
            @Override
            public void focusGained() {
                if (wordField.getText().equals(resources.translate((MSG_INIT_FIELD_WORD)))) {
                    wordField.setText(MSG_EMPTY_STRING);
                    wordField.setFont(settings.getFont());
                }
                wordField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                wordField.revalidate();
            }

            @Override
            public void focusLost() {
                wordField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

            }
        };
    }

    public String getAnswer() {
        return wordField.getText();
    }

    public void finishExercising() {
        setVisible(false);
    }

    public void lockHintButton() {
        hintButton.setEnabled(false);
    }

    public void unlockHintButton() {
        hintButton.setEnabled(true);
    }

    public void showMsgInWordField() {
        wordField.setFont(settings.getFont().deriveFont(Font.ITALIC));
        wordField.setText(resources.translate(MSG_INIT_FIELD_WORD));
        wordField.requestFocus();
        wordField.revalidate();
    }

    @Override
    public void reloadText() {
        setTitle(resources.translate(MSG_EXERCISE_WINDOW));
        super.reloadText();
        hintButton.reloadText();
        checkButton.reloadText();
        switchButton.reloadText();
    }

    @Override
    public void reloadFont() {
        setFont(settings.getFont());
        super.reloadFont();
        switchButton.reloadFont();
        hintButton.reloadFont();
        checkButton.reloadFont();
        imageLabel.update();
    }

    public void reset() {
        clear();
        setAnswerIcon(AnswerIcon.NORMAL);
        checkButton.setSelected(false);
        unlockHintButton();
    }

    public void hideWindow() {
        setVisible(false);
        dispose();
    }

    public void setFirstLetter(String letter) {
        wordField.setText(letter);
    }

    public void setAnswerIcon(AnswerIcon answerIcon) {
        switch (answerIcon) {
            case SAD: {
                imageLabel.setImage(R.icon.SAD);
                break;
            }
            case NORMAL: {
                imageLabel.setImage(R.icon.NORMAL);
                break;
            }
            case LAUGHING: {
                imageLabel.setImage(R.icon.LAUGHING);
                break;
            }
        }
    }

    public static enum AnswerIcon {
		SAD, NORMAL, LAUGHING
	}

    private class ImageLabel extends JLabel {

        private BufferedImage originalImage = null;

        private ImageLabel() {
            setLayout(null);
        }

        private BufferedImage resizeImage(BufferedImage originalImage, int IMG_WIDTH, int IMG_HEIGHT) {
            int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_RGB : originalImage.getType();
            BufferedImage image = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);

            Graphics2D g = image.createGraphics();
            g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, this);
            g.dispose();
            return image;
        }

        public void setImage(String path) {
            try {
                originalImage = ImageIO.read(new File(path));
            } catch (IOException e) {
                e.printStackTrace();
            }
            update();
        }

        public void update() {
            int fSize = settings.getFont().getSize();
            int width = 64 * fSize / 14;
            int height = 64 * fSize / 14;
            Dimension size = new Dimension(width, height);
            setPreferredSize(size);
            setMinimumSize(size);
            setMaximumSize(size);
            setSize(size);
            BufferedImage image = resizeImage(originalImage, width, height);
            setIcon(new ImageIcon(image));
            repaint();
        }
    }

    private class ChangeCardStatusListener implements ItemListener {

        private int selectedItem = CardStatus.TO_LEARN.getIndex();

        @Override
        public void itemStateChanged(final ItemEvent e) {
            JComboBox s = (JComboBox) e.getSource();
            int i = s.getSelectedIndex();
            if (i == selectedItem) {
                return;
            }
            if (e.getStateChange() == ItemEvent.SELECTED) {
                presenter.skipCardAndChangeStatus(CardStatus.elementAt(i));
            }

        }

    }

    public class CheckCardAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent action) {
            presenter.checkExerciseAnswer();
        }

    }

    public class SwitchToDictionaryAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent action) {
            presenter.switchToMainWindow();
        }

    }

    public class HintCardAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent action) {
            presenter.showHintForExercise();
        }

    }

}