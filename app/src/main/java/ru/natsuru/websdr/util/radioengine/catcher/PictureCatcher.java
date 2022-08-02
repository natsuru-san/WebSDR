package ru.natsuru.websdr.util.radioengine.catcher;

import android.graphics.Bitmap;
import ru.natsuru.websdr.service.repository.RepoService;

public class PictureCatcher extends AbstractCatcher<Bitmap> {

    private final RepoService repoService;

    public PictureCatcher(RepoService repoService) {
        this.repoService = repoService;
    }


    @Override
    public void recordingObject(Bitmap obj) {}

    @Override
    public void write(byte[] arr) {
        System.out.println("Received: " + arr.length);
        repoService.setBitmapArray(arr);
    }
}
