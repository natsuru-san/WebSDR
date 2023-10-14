package karelia.natsuru.websdr;

public class FreqStore{
    private final double freqInstance;
    public FreqStore(double freqInstance){
        this.freqInstance = freqInstance;
    }
    public double getFreqInstance(){
        return freqInstance;
    }
}
