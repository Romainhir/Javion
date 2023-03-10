package ch.epfl.javions.demodulation;

import ch.epfl.javions.Preconditions;

import java.io.*;

public final class PowerComputer {
    private final short[] batch;
    private final SamplesDecoder decoder;
    private short[] sample_data;

    public PowerComputer(InputStream stream, int batchSize) throws IOException {
        Preconditions.checkArgument((batchSize > 0) && (batchSize % 8 == 0));
        batch = new short[batchSize];
        decoder = new SamplesDecoder(stream, batchSize);
        sample_data = new short[8];
    }

    private int calculatePower(short[] data){
        int reel = 0;
        int imaginary = 0;
        for (int i = 0; i < data.length; i += 2){
            reel += data[i+1] * (int)(Math.pow(-1, i /2));
            imaginary += data[i] * (int)(Math.pow(-1, i /2));

        }

        return (int)(Math.pow(reel, 2) + Math.pow(imaginary, 2));
    }

    /**
     * Remove the last element of the list and move others to the next index
     * Add the data at the first index of the list
     * @param data_tab (short[]) The tab of data
     * @param data (short) data
     */
    private void addFirst(short[] data_tab, short data){
        for(int i = data_tab.length - 1; 0 < i; --i){
            data_tab[i] = data_tab[i-1];
        }
        data_tab[0] = data;
    }

    public int readBatch(int[] batch) throws IOException {
        Preconditions.checkArgument(batch.length == this.batch.length);
        int decodedNumber = decoder.readBatch(this.batch);
        for(int j = 0; j < decodedNumber; j += 2){
            for(int i = 0; i < 2; ++i){
                addFirst(sample_data ,this.batch[j+i]);
            }
            batch[j/2] = calculatePower(sample_data);
        }

        //System.out.println("decodedNumber: " + decodedNumber/2);
        return decodedNumber / 2;
    }

    public static void main(String[] args) throws Exception {
        short [] oui1 = new short[2402];

        int [] oui = new int[2800];
        try(InputStream in = new FileInputStream("resources/samples.bin")) {
            SamplesDecoder s = new SamplesDecoder(in, 2800);
            PowerComputer p = new PowerComputer(in, 2800);
            int k = s.readBatch(oui1);
            //p.readBatch(oui);
            System.out.println(k);
            for (int i = 0; i < 10; i++) {
                System.out.println(oui1[i]);
                //System.out.println(oui[i]);

            }
        }
    }
}
