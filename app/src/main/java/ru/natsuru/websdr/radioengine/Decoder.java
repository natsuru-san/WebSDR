//Copyright by Natsuru-san

package ru.natsuru.websdr.radioengine;
import android.media.AudioTrack;
import androidx.annotation.NonNull;

public class Decoder {
    private final AudioTrack audiotrack;
    private boolean decoderType = false;
    public Decoder(AudioTrack audiotrack){
        this.audiotrack = audiotrack;
    }
    //A-law декодер; осуществляет "перегон" A-law в 16bit-signed PCM аудио
    //За декодеры спасибо Marc Sweetgall, выложившему его на языке C# в публичный доступ ^_^
    @NonNull
    private short[] decodeAlaw(@NonNull byte[] input){
        short[] output = new short[input.length];
        for(int i = 0; i < input.length; i++){
            input[i] = (byte) (input[i] ^ 213); //Инвертируем каждый бит и подписываем (213 = 1101 0101)
            int sign = input[i] & 128; //Получаем значение знака
            int exponent = (input[i] & 112) >> 4; //Получаем и сдвигаем значение экспоненты
            int data = input[i] & 15;
            data <<= 4;
            data += 8;
            if (exponent != 0) {
                data += 256;
            }
            if (exponent > 1){
                data <<= (exponent - 1);
            }
            if(sign == 0){
                output[i] = (short) data;
            }else{
                output[i] = (short) -data;
            }
        }
        return output;
    }
    //µ-Law декодер; работает в качестве резервного декодера
    //За декодеры спасибо Marc Sweetgall, выложившему его на языке C# в публичный доступ ^_^
    @NonNull
    private short[] decodeUlaw(@NonNull byte[] input){
        short[] output = new short[input.length];
        for(int i = 0; i < input.length; i++){
            input[i] = (byte) ~input[i];
            int sign = input[i] & 0x80;
            int exponent = (input[i] & 0x70) >> 4;
            int data = input[i] & 0x0f;
            data |= 0x10;
            data <<= 1;
            data += 1;
            data <<= exponent + 2;
            int bias = 0x84;
            data -= bias;
            if(sign == 0){
                output[i] = (short) data;
            }else{
                output[i] = (short) -data;
            }
        }
        return output;
    }
    //Приём байтового потока, преобразование и его запись в аудиобуфер
    protected void playOutput(@NonNull byte[] currentArray){
        short[] decoded;
        if(decoderType){
            decoded = decodeUlaw(currentArray);
        }else{
            decoded = decodeAlaw(currentArray);
        }
        audiotrack.write(decoded, 0, decoded.length, AudioTrack.WRITE_NON_BLOCKING);
    }
    protected void setDecoder(boolean decoderType){
        this.decoderType = decoderType;
    }
}
