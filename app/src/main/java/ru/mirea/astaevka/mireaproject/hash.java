package ru.mirea.astaevka.mireaproject;

import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class hash {
    private static final byte[] H = "ALFTJkbCL0aic8ON4XEYVfaYVbWa35nU".getBytes(StandardCharsets.UTF_8);;
    static byte[] Sum = new byte[32];
    static byte[] L = new byte[32];
    static byte[][] block = { { 0x4, 0xA, 0x9, 0x2, 0xD, 0x8, 0x0, 0xE, 0x6, 0xB, 0x1, 0xC, 0x7, 0xF, 0x5, 0x3 },
            { 0xE, 0xB, 0x4, 0xC, 0x6, 0xD, 0xF, 0xA, 0x2, 0x3, 0x8, 0x1, 0x0, 0x7, 0x5, 0x9 },
            { 0x5, 0x8, 0x1, 0xD, 0xA, 0x3, 0x4, 0x2, 0xE, 0xF, 0xC, 0x7, 0x6, 0x0, 0x9, 0xB },
            { 0x7, 0xD, 0xA, 0x1, 0x0, 0x8, 0x9, 0xF, 0xE, 0x4, 0x6, 0xC, 0xB, 0x2, 0x5, 0x3 },
            { 0x6, 0xC, 0x7, 0x1, 0x5, 0xF, 0xD, 0x8, 0x4, 0xA, 0x9, 0xE, 0x0, 0x3, 0xB, 0x2 },
            { 0x4, 0xB, 0xA, 0x0, 0x7, 0x2, 0x1, 0xD, 0x3, 0x6, 0x8, 0x5, 0x9, 0xC, 0xF, 0xE },
            { 0xD, 0xB, 0x4, 0x1, 0x3, 0xF, 0x5, 0x9, 0x0, 0xA, 0xE, 0x7, 0x6, 0x8, 0x2, 0xC },
            { 0x1, 0xF, 0xD, 0x0, 0x5, 0x7, 0xA, 0x4, 0x9, 0x2, 0x3, 0xE, 0x6, 0xB, 0x8, 0xC } };
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

        byte[] S = Plus(Plus(Plus(s4, s3), s2),s1);

        byte[] Hout = FiR(Xor(Hin, Fi(Xor(m, FiR(S, 12)))), 61);

        return Hout;
    }

    public byte[] E(byte[] h, byte[] k){
        // ГОСТ 28147—89
        byte[] A = Arrays.copyOfRange(h, 0, h.length/2-1);
        byte[] B = Arrays.copyOfRange(h, h.length/2, h.length-1);

        return h;
    }

    public byte[] FiR(byte[] Yb, int round){
        byte[] Y = Yb;

        for (int r = 0; r < round; r++){
            Y = Fi(Y);
        }

        return Y;
    }

    public byte[] Fi(byte[] Yb){
        byte[] xor = Arrays.copyOfRange(Yb, 30, 31);
        xor = Xor(xor, Arrays.copyOfRange(Yb, 28, 29));
        xor = Xor(xor, Arrays.copyOfRange(Yb, 26, 27));
        xor = Xor(xor, Arrays.copyOfRange(Yb, 24, 25));
        xor = Xor(xor, Arrays.copyOfRange(Yb, 3, 4));
        xor = Xor(xor, Arrays.copyOfRange(Yb, 0, 1));

        for (int i = 31; i > 2; i-=2){
            xor = Plus(xor, Arrays.copyOfRange(Yb, i-1, i));
        }

        return xor;
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
            Final = Final + Y.charAt(i+1+4*(k-1));
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

