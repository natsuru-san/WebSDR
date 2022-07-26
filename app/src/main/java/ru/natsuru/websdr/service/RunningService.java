package ru.natsuru.websdr.service;

import ru.natsuru.websdr.radioengine.RadioService;

public class RunningService {

    private static RadioService service;

    public static void setService(RadioService service) {
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
}
