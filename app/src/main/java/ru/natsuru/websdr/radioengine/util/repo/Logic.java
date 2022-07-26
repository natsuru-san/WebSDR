package ru.natsuru.websdr.radioengine.util.repo;

class Logic {

    static void verifyLimit(){
        if(Repository.getMaxBorder() > Repository.MAX_BORDER_LIMIT){
            Repository.setMaxBorder(Repository.MAX_BORDER_LIMIT);
        }
        if(Repository.getMaxBorder() < Repository.MAX_BORDER_LIMIT_OUT){
            Repository.setMaxBorder(Repository.MAX_BORDER_LIMIT_OUT);
        }
        if(Repository.getMinBorder() < Repository.MIN_BORDER_LIMIT){
            Repository.setMinBorder(Repository.MIN_BORDER_LIMIT);
        }
        if(Repository.getMinBorder() > Repository.MIN_BORDER_LIMIT_OUT){
            Repository.setMinBorder(Repository.MIN_BORDER_LIMIT_OUT);
        }
    }

    static void prepareMode() {
        switch (Repository.getMode()){
            case 0:
                Repository.setModulation(4);
                Repository.setMinBorder(-5f);
                Repository.setMinBorder(5f);
                Repository.setAudioDepth(false);
                break;
            case 1:
                Repository.setModulation(1);
                Repository.setMinBorder(-4.5f);
                Repository.setMinBorder(4.5f);
                Repository.setAudioDepth(true);
                break;
            case 2:
                Repository.setModulation(1);
                Repository.setMinBorder(-2.7f);
                Repository.setMinBorder(-0.3f);
                Repository.setAudioDepth(true);
                break;
            case 3:
                Repository.setModulation(1);
                Repository.setMinBorder(0.3f);
                Repository.setMinBorder(2.7f);
                Repository.setAudioDepth(true);
                break;
            case 4:
                Repository.setModulation(1);
                Repository.setMinBorder(-0.95f);
                Repository.setMinBorder(-0.55f);
                Repository.setAudioDepth(false);
                break;
        }
        checkDepth();
    }

    static void checkDepth() {
        if (Repository.getMode() == 0) {
            Repository.setAudioDepth(false);
        } else {
            Repository.setAudioDepth(!(Repository.getMinBorder() > -4.0f) && !(Repository.getMaxBorder() < 4.0f));
        }
    }

    static void widthStream(boolean vector) {
        switch (Repository.getMode()) {
            case 0:
            case 1:
                if (vector) {
                    Repository.setMinBorder(Repository.getMinBorder() - 0.5f);
                    Repository.setMaxBorder(Repository.getMaxBorder() + 0.5f);
                } else {
                    Repository.setMinBorder(Repository.getMinBorder() + 0.5f);
                    Repository.setMaxBorder(Repository.getMaxBorder() - 0.5f);
                }
                break;
            case 2:
                if (vector) {
                    Repository.setMinBorder(Repository.getMinBorder() - 0.1f);
                } else {
                    Repository.setMinBorder(Repository.getMinBorder() + 0.1f);
                }
                break;
            case 3:
                if (vector) {
                    Repository.setMaxBorder(Repository.getMaxBorder() + 0.1f);
                } else {
                    Repository.setMaxBorder(Repository.getMaxBorder() - 0.1f);
                }
                break;
            case 4:
                if (vector) {
                    Repository.setMinBorder(Repository.getMinBorder() - 0.05f);
                } else {
                    Repository.setMinBorder(Repository.getMinBorder() + 0.05f);
                }
                break;
        }
    }
}
