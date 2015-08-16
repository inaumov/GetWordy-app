package get.wordy.app.ui.frame.card;

import get.wordy.app.impl.ServiceHolder;
import get.wordy.app.settings.Setting;
import get.wordy.app.ui.WordyMediator;
import get.wordy.core.api.IDictionaryService;
import get.wordy.core.bean.Card;
import get.wordy.core.bean.wrapper.CardStatus;

import javax.swing.text.Document;
import java.util.Collection;
import java.util.Iterator;

public class ExerciseWindowPresenter {

    private ExerciseWindow exerciseWindow;
    private WordyMediator mediator;

    private IDictionaryService dictionaryService;
    private ExerciseProcessor exerciseProcessor;

    public ExerciseWindowPresenter(ExerciseWindow exerciseWindow, WordyMediator mediator, Collection<Card> cards) {
        this.exerciseWindow = exerciseWindow;
        this.mediator = mediator;
        this.dictionaryService = mediator.getDictionaryService();
        this.exerciseProcessor = new ExerciseProcessor(cards.iterator());
        mediator.register(this);

        loadFirstCardInBackground();
    }

    private void loadFirstCardInBackground() {
        Thread exerciseProcessorThread = new Thread(new Runnable() {

            @Override
            public void run() {
                exerciseProcessor.execute();
            }
        });
        exerciseProcessorThread.start();
    }

    public void skipCardAndChangeStatus(CardStatus cardStatus) {
        synchronized (exerciseProcessor) {
            exerciseProcessor.skip();
            exerciseProcessor.changeStatus(cardStatus);
        }
    }

    public void checkExerciseAnswer() {
        synchronized (exerciseProcessor) {
            if (exerciseProcessor.isFinished()) {
                mediator.updateTable();
                mediator.showMainWindow();
            }
            exerciseProcessor.resume();
        }
    }

    public void switchToMainWindow() {
        exerciseProcessor.stop();
        exerciseWindow.hideWindow();
        mediator.updateTable();
        mediator.showMainWindow();
    }

    public void showHintForExercise() {
        synchronized (exerciseProcessor) {
            String firstLetter = exerciseProcessor.getFirstLetter();
            exerciseWindow.setFirstLetter(firstLetter);
            exerciseWindow.lockHintButton();
        }
    }

    public void setDocument(Document document) {
        exerciseWindow.setDocument(document);
    }

    public class ExerciseProcessor {

        private Iterator<Card> iterator;
        private final Number repNumber;

        private Card card;
        private boolean isAnswerChecked;
        private boolean interrupted;

        public ExerciseProcessor(Iterator<Card> list) {
            this.iterator = list;
            this.repNumber = ServiceHolder.getSettingManager().getNumber(Setting.NUMBER_OF_REPETITIONS);
        }

        public void execute() {
            while (iterator.hasNext() && !interrupted) {
                setAnswerUnchecked();
                exerciseWindow.showMsgInWordField();
                card = iterator.next();
                synchronized (ExerciseProcessor.this) {
                    dictionaryService.loadCard(card.getWord().getValue());
                    exerciseWindow.setStatus(card.getStatus().getIndex());
                    mediator.previewCardDocumentOnExercise(card);
                    try {
                        ExerciseProcessor.this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public void resume() {
            if (!iterator.hasNext() && isAnswerChecked) {
                exerciseWindow.finishExercising();
                exerciseWindow.clear();
                exerciseWindow.unlockHintButton();
                exerciseWindow.showMsgInWordField();
            } else if (!isAnswerChecked) {
                if (checkIfAnswerCorrect()) {
                    dictionaryService.increaseScoreUp(card.getWord().getValue(), repNumber.intValue());
                }
                mediator.previewFullCardDocumentOnExercise(card);
            } else if (isAnswerChecked) {
                exerciseWindow.clear();
                exerciseWindow.unlockHintButton();
                exerciseWindow.setAnswerIcon(ExerciseWindow.AnswerIcon.NORMAL);
                ExerciseProcessor.this.notify();
            }
        }

        public String getFirstLetter() {
            return String.valueOf(getWord().charAt(0));
        }

        private boolean checkIfAnswerCorrect() {
            setAnswerChecked();
            String word = getWord();
            String answer = getAnswer();
            if (word.equalsIgnoreCase(answer)) {
                exerciseWindow.setAnswerIcon(ExerciseWindow.AnswerIcon.LAUGHING);
                return true;
            }
            exerciseWindow.setAnswerIcon(ExerciseWindow.AnswerIcon.SAD);
            return false;
        }

        private void setAnswerChecked() {
            isAnswerChecked = true;
        }

        private void setAnswerUnchecked() {
            isAnswerChecked = false;
        }

        private String getWord() {
            return card.getWord().getValue();
        }

        private String getAnswer() {
            return exerciseWindow.getAnswer();
        }

        public void skip() {
            exerciseWindow.clear();
            exerciseWindow.unlockHintButton();
            exerciseWindow.setAnswerIcon(ExerciseWindow.AnswerIcon.NORMAL);
            ExerciseProcessor.this.notify();
            if (!iterator.hasNext()) {
                exerciseWindow.finishExercising();
            }
        }

        public void changeStatus(CardStatus cardStatus) {
            String word = card.getWord().getValue();
            dictionaryService.changeStatus(word, cardStatus);
        }

        public boolean isFinished() {
            return !iterator.hasNext() & isAnswerChecked;
        }

        public void stop() {
            this.interrupted = true;
        }

    }

}