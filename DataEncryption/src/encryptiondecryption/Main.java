package encryptiondecryption;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        Encryption test = new Encryption(new File("src/Filtered_100_06.wav"));
        test.injectData("Hey, I am Nirab.");

        Decryption test2 = new Decryption(new File("src/Filtered_100_06.wav_output.wav"));
        System.out.println("Data is: " + test2.getEncryptData());
    }
}
