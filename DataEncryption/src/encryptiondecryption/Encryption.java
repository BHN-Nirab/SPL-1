package encryptiondecryption;

import java.io.*;

public class Encryption {

    private File orginalFile;

    private String chunkIdentifier;
    private byte[] chunkSize;
    private String format;

    private String subchunkIdentifier;
    private byte[] subchunkSize;
    private byte[] audioFormat;
    private byte[] numberOfChannels;
    private byte[] sampleRate;
    private byte[] byteRate;
    private byte[] blockAlign;
    private byte[] bitsPerSample;

    private String dataSubchunkIdentifier;
    private byte[] dataSubchunkSize;
    private byte[] soundData;

    private Encryption()
    {
    }

    public Encryption(File path)
    {
        orginalFile = path;
        chunkSize = new byte[4];
        subchunkSize = new byte[4];
        audioFormat = new byte[2];
        numberOfChannels = new byte[2];
        sampleRate = new byte[4];
        byteRate = new byte[4];
        blockAlign = new byte[2];
        bitsPerSample = new byte[2];
        dataSubchunkSize = new byte[4];

        readOrginalFile();
    }

    private void readOrginalFile()
    {
        DataInputStream input = null;

        try{

            input = new DataInputStream(new FileInputStream(orginalFile));

            chunkIdentifier = "" + (char)input.readByte() + (char)input.readByte() + (char)input.readByte() + (char)input.readByte();
            input.read(chunkSize);
            format = "" + (char)input.readByte() + (char)input.readByte() + (char)input.readByte() + (char)input.readByte();

            subchunkIdentifier = "" + (char)input.readByte() + (char)input.readByte() + (char)input.readByte() + (char)input.readByte();
            input.read(subchunkSize);
            input.read(audioFormat);
            input.read(numberOfChannels);
            input.read(sampleRate);
            input.read(byteRate);
            input.read(blockAlign);

            input.read(bitsPerSample);
            dataSubchunkIdentifier = "" + (char)input.readByte() + (char)input.readByte() + (char)input.readByte() + (char)input.readByte();
            input.read(dataSubchunkSize);

            soundData = new byte[(int)byteArrayToLong(dataSubchunkSize)];
            input.read(soundData);

        }catch(Exception e){}

    }

    private void writeOrginalFile()
    {
        try {

            DataOutputStream output = new DataOutputStream(new FileOutputStream(new File(orginalFile.getAbsolutePath().toString() + "_output.wav")));

            output.writeBytes(chunkIdentifier);
            output.write(chunkSize);
            output.writeBytes(format);
            output.writeBytes(subchunkIdentifier);
            output.write(subchunkSize);
            output.write(audioFormat);
            output.write(numberOfChannels);
            output.write(sampleRate);
            output.write(byteRate);
            output.write(blockAlign);
            output.write(bitsPerSample);
            output.writeBytes(dataSubchunkIdentifier);
            output.write(dataSubchunkSize);
            output.write(soundData);

        }catch(Exception e){}
    }

    public boolean injectData(String data)
    {
        if(data.length() >= soundData.length/5000) return false;

        for(int i=0,j=0; i<soundData.length && j<data.length(); i++)
        {
            if(i%5000==0){
                int dataAtj = (int)data.charAt(j);
                soundData[i] = Byte.valueOf(Integer.toString(dataAtj));
                j++;
            }
        }
        writeOrginalFile();
        return true;
    }

    public int getAvailableSpaces()
    {
        return soundData.length/5000;
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
