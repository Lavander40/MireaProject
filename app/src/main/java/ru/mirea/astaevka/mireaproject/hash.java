package ru.mirea.astaevka.mireaproject;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class hash {
    private static final byte[] H = "ALFTJkbCL0aic8ON4XEYVfaYVbWa35nU".getBytes(StandardCharsets.UTF_8);;
    static byte[] Sum = new byte[32];
    static byte[] L = new byte[32];
    public String GOST3411(String message) {
        byte[] convert = message.getBytes(StandardCharsets.UTF_8);
        byte[] bytes = new byte[32];
        for (int i = 0; i < 4; i++) bytes[i] = convert[i];

        Sum = bytes;
        L = hexStringToByteArray("ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff");

        byte[] Hout = f(H, bytes);
        Hout = f(Hout, L);
        Hout = f(Hout, Sum);

        return new String(Hout, StandardCharsets.UTF_8);
    }

    public byte[] f(byte[] Hin, byte[] m){
        byte[][] keys = keyGen(Hin, m);

        byte[] s1 = E(Arrays.copyOfRange(Hin, 24, 31), keys[1]);
        byte[] s2 = E(Arrays.copyOfRange(Hin, 16, 23), keys[2]);
        byte[] s3 = E(Arrays.copyOfRange(Hin, 8, 15), keys[3]);
        byte[] s4 = E(Arrays.copyOfRange(Hin, 0, 7), keys[4]);

        return Plus(Plus(Plus(s4, s3), s2),s1);
    }

    public byte[] E(byte[] h, byte[] k){

    }

    public byte[][] keyGen(byte[] Hin, byte[] m){
        byte[] C2 = new byte[32];
        byte[] C3 = hexStringToByteArray("ff00ffff000000ffff0000ff00ffff0000ff00ff00ff00ffff00ff00ff00ff00");
        byte[] C4 = new byte[32];

        byte[] k1, k2, k3, k4;

        byte[] U = Hin;
        byte[] V = m;
        byte[] W = Xor(U, V);
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

    public byte[] A(byte[] Y){
        byte[] y1, y2, y3, y4;
        y4 = Arrays.copyOfRange(Y, 0, 7);
        y3 = Arrays.copyOfRange(Y, 8, 15);
        y2 = Arrays.copyOfRange(Y, 16, 23);
        y1 = Arrays.copyOfRange(Y, 24, 31);

        return Plus(Plus(Plus(Xor(y1, y2), y4), y3), y2);
    }

    public byte[] P(byte[] Yb){
        String Y = new String(Yb, StandardCharsets.UTF_8);
        String Final = "";
        Integer i = 0;
        Integer k = 1;

        for (int j = Y.length() - 1; j >= 0; j--) {
            i = (i <= 3) ? i++ : 0;
            k = (k <= 8) ? k++ : 0;
            Final = Final + Y.charAt(8*i-k);
        }

        return Final.getBytes(StandardCharsets.UTF_8);
    }

    public byte[] Plus(byte[] a, byte[] b) {
        byte[] resultArray = new byte[a.length + b.length];
        System.arraycopy(a, 0, resultArray, 0, a.length);
        System.arraycopy(b, 0, resultArray, a.length, b.length);

        return resultArray;
    }

    public byte[] Xor(byte[] a, byte[] b) {

        byte[] c = new byte[a.length];

        int i = 0;
        for (byte by : a)
            c[i] = (byte) (by ^ b[i++]);

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

