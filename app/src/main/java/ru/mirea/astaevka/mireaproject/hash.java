package ru.mirea.astaevka.mireaproject;

import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class hash {
    private static final byte[] H = "ALFTJkbCL0aic8ON4XEYVfaYVbWa35nU".getBytes(StandardCharsets.US_ASCII);
    static byte[] Sum = new byte[32];
    static byte[] L = new byte[32];
    public static String GOST3411(String message) {
        byte[] convert = message.getBytes(StandardCharsets.US_ASCII);
        byte[] bytes = new byte[32];
        for (int i = 0; i < convert.length; i++) bytes[i] = convert[i];

        Sum = bytes;
        L = hexStringToByteArray("ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff");

        byte[] Hout = f(H, bytes);
        Hout = f(Hout, L);
        Hout = f(Hout, Sum);

        return new String(Hout);
    }

    public static byte[] f(byte[] Hin, byte[] m){
        byte[][] keys = keyGen(Hin, m);

        byte[] S = E(Hin, keys);

        byte[] Hout = FiR(Xor(Hin, Fi(Xor(m, FiR(S, 12)))), 61);

        return Hout;
    }

    public static byte[] E(byte[] Hin, byte[][] key){
        byte[] sT = new byte[32];
        byte[] h1 = new byte[8];
        byte[] h2 = new byte[8];
        byte[] h3 = new byte[8];
        byte[] h4 = new byte[8];

        Cipher encryptor = new Cipher();
        for (int i = 0; i < 8; i++) {
            h1[i] = Hin[i];
            h2[i] = Hin[i + 8];
            h3[i] = Hin[i + 16];
            h4[i] = Hin[i + 24];
        }
        System.arraycopy(encryptor.encrypt((h1), key[0]), 0, sT, 0, 8);
        System.arraycopy(encryptor.encrypt((h2), key[1]), 0, sT, 8, 8);
        System.arraycopy(encryptor.encrypt((h3), key[2]), 0, sT, 16, 8);
        System.arraycopy(encryptor.encrypt((h4), key[3]), 0, sT, 24, 8);

        return sT;
    }

    public static byte[] FiR(byte[] Yb, int round){
        byte[] Y = Yb;

        for (int r = 0; r < round; r++){
            Y = Fi(Y);
        }

        return Y;
    }

    public static byte[] Fi(byte[] Yb){
        byte[] xor = Arrays.copyOfRange(Yb, 30, 32);
        xor = Xor(xor, Arrays.copyOfRange(Yb, 28, 30));
        xor = Xor(xor, Arrays.copyOfRange(Yb, 26, 28));
        xor = Xor(xor, Arrays.copyOfRange(Yb, 24, 26));
        xor = Xor(xor, Arrays.copyOfRange(Yb, 3, 5));
        xor = Xor(xor, Arrays.copyOfRange(Yb, 0, 2));

        for (int i = 31; i > 2; i-=2){
            xor = Plus(xor, Arrays.copyOfRange(Yb, i-1, i+1));
        }

        return xor;
    }

    public static byte[][] keyGen(byte[] Hin, byte[] m){
        byte[] C2 = new byte[32];
        byte[] C3 = hexStringToByteArray("ff00ffff000000ffff0000ff00ffff0000ff00ff00ff00ffff00ff00ff00ff00");
        byte[] C4 = new byte[32];

        byte[] k1, k2, k3, k4;

        byte[] U = new byte[32];
        byte[] V = new byte[32];
        byte[] W;

        System.arraycopy(Hin, 0, U, 0, Hin.length);
        System.arraycopy(m, 0, V, 0, Hin.length);
        W = Xor(U, V);
        k1 = P(W);

        U = Xor(A(U),C2);
        V = A(A(V));
        W = Xor(U, V);
        k2 = P(W);

        U = Xor(A(U),C3);
        V = A(A(V));
        W = Xor(U, V);
        k3 = P(W);

        U = Xor(A(U),C4);
        V = A(A(V));
        W = Xor(U, V);
        k4 = P(W);


        return new byte[][] {k1, k2, k3, k4};
    }

    public static byte[] A(byte[] Y){
        byte[] y1, y2, y3, y4;
        y4 = Arrays.copyOfRange(Y, 0, 8);
        y3 = Arrays.copyOfRange(Y, 8, 16);
        y2 = Arrays.copyOfRange(Y, 16, 24);
        y1 = Arrays.copyOfRange(Y, 24, 32);

        return Plus(Plus(Plus(Xor(y1, y2), y4), y3), y2);
    }

    public static byte[] P(byte[] Y){
        byte[] Final = new byte[32];

        for (int i = 0; i <= 3; i++) {
            for (int k = 1; k <= 8; k++) {
                Final[i + 1 + 4 * (k - 1) - 1] = Y[8 * i + k - 1];
            }
        }

        return Final;
    }

    public static byte[] Plus(byte[] a, byte[] b) {
        byte[] resultArray = new byte[a.length + b.length];
        System.arraycopy(a, 0, resultArray, 0, a.length);
        System.arraycopy(b, 0, resultArray, a.length, b.length);

        return resultArray;
    }

    public static byte[] Xor(byte[] a, byte[] b) {
        byte[] c = new byte[a.length];
        for (int i = 0; i < a.length; i++){
            c[i] = (byte) (a[i] ^ b[i]);
        }
        return c;
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
}

