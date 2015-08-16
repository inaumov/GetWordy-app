package get.wordy.app.ui;

import get.wordy.app.impl.Scheduler;
import get.wordy.app.impl.ServiceHolder;
import get.wordy.app.resources.Resources;
import get.wordy.app.settings.SettingsManager;
import get.wordy.core.DictionaryService;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.TimerTask;

public class GetWordyApplication {

    static {
        ServiceHolder serviceHolder = ServiceHolder.getServiceHolder();
        try {
            serviceHolder.setDictionaryService(new DictionaryService());
        } catch (IOException e) {
            e.printStackTrace();
            // TODO: tryReadPropertyFile(serviceHolder);
        }
        serviceHolder.setSettingsManager(new SettingsManager());
    }

    private void start() {
        final UISettings settings = new UISettings() {
            @Override
            public Font getFont() {
                return ServiceHolder.getSettingManager().getFont();
            }
        };

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                WordyMediator mediator = new WordyMediator(Resources.getResources(), settings);
                mediator.initMainWindow();
            }
        };
        Scheduler scheduler = new Scheduler(task);
        scheduler.start(0);
    }

    public static void main(String[] args) throws IOException {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        GetWordyApplication app = new GetWordyApplication();
        app.start();
    }

}