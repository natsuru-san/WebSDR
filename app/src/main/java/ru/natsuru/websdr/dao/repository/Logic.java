package ru.natsuru.websdr.dao.repository;

class Logic {

    static void verifyLimit(){
        if(RepoAudio.getMaxBorder() > RepoAudio.MAX_BORDER_LIMIT){
            RepoAudio.setMaxBorder(RepoAudio.MAX_BORDER_LIMIT);
        }
        if(RepoAudio.getMaxBorder() < RepoAudio.MAX_BORDER_LIMIT_OUT){
            RepoAudio.setMaxBorder(RepoAudio.MAX_BORDER_LIMIT_OUT);
        }
        if(RepoAudio.getMinBorder() < RepoAudio.MIN_BORDER_LIMIT){
            RepoAudio.setMinBorder(RepoAudio.MIN_BORDER_LIMIT);
        }
        if(RepoAudio.getMinBorder() > RepoAudio.MIN_BORDER_LIMIT_OUT){
            RepoAudio.setMinBorder(RepoAudio.MIN_BORDER_LIMIT_OUT);
        }
    }

    static void prepareMode() {
        switch (RepoAudio.getMode()){
            case 0:
                RepoAudio.setModulation(4);
                RepoAudio.setMinBorder(-5f);
                RepoAudio.setMinBorder(5f);
                RepoAudio.setAudioDepth(false);
                break;
            case 1:
                RepoAudio.setModulation(1);
                RepoAudio.setMinBorder(-4.5f);
                RepoAudio.setMinBorder(4.5f);
                RepoAudio.setAudioDepth(true);
                break;
            case 2:
                RepoAudio.setModulation(1);
                RepoAudio.setMinBorder(-2.7f);
                RepoAudio.setMinBorder(-0.3f);
                RepoAudio.setAudioDepth(true);
                break;
            case 3:
                RepoAudio.setModulation(1);
                RepoAudio.setMinBorder(0.3f);
                RepoAudio.setMinBorder(2.7f);
                RepoAudio.setAudioDepth(true);
                break;
            case 4:
                RepoAudio.setModulation(1);
                RepoAudio.setMinBorder(-0.95f);
                RepoAudio.setMinBorder(-0.55f);
                RepoAudio.setAudioDepth(false);
                break;
        }
        checkDepth();
    }

    static void checkDepth() {
        if (RepoAudio.getMode() == 0) {
            RepoAudio.setAudioDepth(false);
        } else {
            RepoAudio.setAudioDepth(!(RepoAudio.getMinBorder() > -4.0f) && !(RepoAudio.getMaxBorder() < 4.0f));
        }
    }

    static void widthStream(boolean vector) {
        switch (RepoAudio.getMode()) {
            case 0:
            case 1:
                if (vector) {
                    RepoAudio.setMinBorder(RepoAudio.getMinBorder() - 0.5f);
                    RepoAudio.setMaxBorder(RepoAudio.getMaxBorder() + 0.5f);
                } else {
                    RepoAudio.setMinBorder(RepoAudio.getMinBorder() + 0.5f);
                    RepoAudio.setMaxBorder(RepoAudio.getMaxBorder() - 0.5f);
                }
                break;
            case 2:
                if (vector) {
                    RepoAudio.setMinBorder(RepoAudio.getMinBorder() - 0.1f);
                } else {
                    RepoAudio.setMinBorder(RepoAudio.getMinBorder() + 0.1f);
                }
                break;
            case 3:
                if (vector) {
                    RepoAudio.setMaxBorder(RepoAudio.getMaxBorder() + 0.1f);
                } else {
                    RepoAudio.setMaxBorder(RepoAudio.getMaxBorder() - 0.1f);
                }
                break;
            case 4:
                if (vector) {
                    RepoAudio.setMinBorder(RepoAudio.getMinBorder() - 0.05f);
                } else {
                    RepoAudio.setMinBorder(RepoAudio.getMinBorder() + 0.05f);
                }
                break;
        }
    }
}
