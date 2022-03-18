//Copyright by Natsuru-san

package ru.natsuru.websdr.radioengine;

import android.media.AudioTrack;
import androidx.annotation.NonNull;

public class AudioHolder {
    private final AudioTrack audiotrack;
    private int play = 0;
    private byte[] middle;
    public AudioHolder(AudioTrack audiotrack){
        this.audiotrack = audiotrack;
    }
    //Объединяем байтовые массивы
    private byte[] concatArrays(byte[] a, byte[] b){
        if(a == null) {
            return b;
        }
        if(b == null){
            return a;
        }
        byte[] result = new byte[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }
    //A-law декодер; осуществляет "перегон" A-law в 16bit-signed PCM аудио
    //За декодер спасибо Marc Sweetgall, выложившему его на языке C# в публичный доступ ^_^
    @NonNull
    private short[] decodeSound(@NonNull byte[] input){
        short[] output = new short[input.length];
        for(int i = 0; i < output.length; i++){
            input[i] = (byte) (input[i] ^ 0xD5);
            int sign = input[i] & 0x80;
            int exponent = (input[i] & 0x70) >> 4;
            int data = input[i] & 0x0f;
            data <<= 4;
            data += 8;
            if (exponent != 0) {
                data += 0x100;
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
    //Приём байтового потока и его запись в аудиобуфер
    public void playOutput(@NonNull byte[] currentArray){
        if(play < 16){
            if(play == 0){
                middle = null;
            }
            middle = concatArrays(middle, currentArray);
            play++;
        }else{
            play = 0;
            short[] decoded = decodeSound(middle);
            audiotrack.write(decoded, 0, decoded.length, AudioTrack.WRITE_BLOCKING);
        }
    }
}
