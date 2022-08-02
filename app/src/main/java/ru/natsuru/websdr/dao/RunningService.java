package ru.natsuru.websdr.dao;

import ru.natsuru.websdr.view.Radio;

public class RunningService {

    private static Radio service;

    public static void setService(Radio service) {
        RunningService.service = service;
    }

    public static void setVolume() {
        service.setVolume();
    }

    public static void sendParams() {
        service.sendParams();
    }

    public static void setAudioMode() {
        service.setAudioMode();
    }

    public static void closeRadio() {
        service.closeRadio();
    }

    public static void resetRadio() {
        service.resetRadio();
    }

    public static void sendBitmapParams() {
        service.sendBitmapParams();
    }
}
