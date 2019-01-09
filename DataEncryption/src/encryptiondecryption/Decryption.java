package encryptiondecryption;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;

public class Decryption {

    private File modifiedData;
    private byte[] soundData;
    private byte[] dataSubchunkSize;

    private Decryption()
    {
    }

    public Decryption(File path)
    {
        modifiedData = path;
        dataSubchunkSize = new byte[4];
    }

    public String getEncryptData()
    {
        skippedHeader();

        String encodedData = "";

        for(int i=0; i<soundData.length; i++){
            if(i%5000==0) {
                int value = soundData[i];
                if(value == 46) break;
                encodedData = encodedData + (char)value;
            }
        }
        return encodedData;

    }

    private void skippedHeader()
    {
        DataInputStream input = null;

        try{
            input = new DataInputStream(new FileInputStream(modifiedData));

            input.read(new byte[4]);
            input.read(new byte[4]);
            input.read(new byte[4]);

            input.read(new byte[4]);
            input.read(new byte[4]);
            input.read(new byte[2]);
            input.read(new byte[2]);
            input.read(new byte[4]);
            input.read(new byte[4]);
            input.read(new byte[2]);
            input.read(new byte[2]);

            input.read(new byte[4]);
            input.read(dataSubchunkSize);
            soundData = new byte[(int)byteArrayToLong(dataSubchunkSize)];

            input.read(soundData);



        }catch(Exception e){}
    }


    private static long byteArrayToLong(byte[] b)
    {
        int start = 0;
        int i = 0;
        int len = 4;
        int cnt = 0;
        byte[] tmp = new byte[len];
        for (i = start; i < (start + len); i++)
        {
            tmp[cnt] = b[i];
            cnt++;
        }
        long accum = 0;
        i = 0;
        for ( int shiftBy = 0; shiftBy < 32; shiftBy += 8 )
        {
            accum |= ( (long)( tmp[i] & 0xff ) ) << shiftBy;
            i++;
        }
        return accum;
    }


}
