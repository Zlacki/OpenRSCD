import java.io.*;
import java.net.URL;

public class Utility {

    //public static String lastFile;

    public static InputStream openFile(String s)
            throws IOException {
        //lastFile = s;
        Object obj;
        if (appletCodeBase == null) {
            obj = new BufferedInputStream(new FileInputStream(s));
        } else {
            URL url = new URL(appletCodeBase, s);
            obj = url.openStream();
        }
        return ((InputStream) (obj));
    }

    public static void readFully(String s, byte abyte0[], int i)
            throws IOException {
        InputStream inputstream = openFile(s);
        DataInputStream datainputstream = new DataInputStream(inputstream);
        try {
            datainputstream.readFully(abyte0, 0, i);
        } catch (EOFException _ex) {
        }
        datainputstream.close();
    }

    public static int getUnsignedByte(byte byte0) {
        return byte0 & 0xff;
    }

    public static int getUnsignedShort(byte abyte0[], int i) {
        return ((abyte0[i] & 0xff) << 8) + (abyte0[i + 1] & 0xff);
    }

    public static int getUnsignedInt(byte abyte0[], int i) {
        return ((abyte0[i] & 0xff) << 24) + ((abyte0[i + 1] & 0xff) << 16) + ((abyte0[i + 2] & 0xff) << 8) + (abyte0[i + 3] & 0xff);
    }

    public static long getUnsignedLong(byte buff[], int off) {
        return (((long) getUnsignedInt(buff, off) & 0xffffffffL) << 32) + ((long) getUnsignedInt(buff, off + 4) & 0xffffffffL);
    }

    public static int getSignedShort(byte abyte0[], int i) {
        int j = getUnsignedByte(abyte0[i]) * 256 + getUnsignedByte(abyte0[i + 1]);
        if (j > 32767)
            j -= 0x10000;
        return j;
    }

    public static int getUnsignedInt2(byte abyte0[], int i) {
        if ((abyte0[i] & 0xff) < 128)
            return abyte0[i];
        else
            return ((abyte0[i] & 0xff) - 128 << 24) + ((abyte0[i + 1] & 0xff) << 16) + ((abyte0[i + 2] & 0xff) << 8) + (abyte0[i + 3] & 0xff);
    }

    public static int getBitMask(byte buff[], int off, int len) {
        int k = off >> 3;
        int l = 8 - (off & 7);
        int i1 = 0;
        for (; len > l; l = 8) {
            i1 += (buff[k++] & bitmask[l]) << len - l;
            len -= l;
        }

        if (len == l)
            i1 += buff[k] & bitmask[l];
        else
            i1 += buff[k] >> l - len & bitmask[len];
        return i1;
    }

    public static String formatAuthString(String s, int maxlen) {
        String s1 = "";
        for (int j = 0; j < maxlen; j++)
            if (j >= s.length()) {
                s1 = s1 + " ";
            } else {
                char c = s.charAt(j);
                if (c >= 'a' && c <= 'z')
                    s1 = s1 + c;
                else if (c >= 'A' && c <= 'Z')
                    s1 = s1 + c;
                else if (c >= '0' && c <= '9')
                    s1 = s1 + c;
                else
                    s1 = s1 + '_';
            }

        return s1;
    }

    public static String ip2string(int i) {
        return (i >> 24 & 0xff) + "." + (i >> 16 & 0xff) + "." + (i >> 8 & 0xff) + "." + (i & 0xff);
    }

    public static long username2hash(String s) {
        String s1 = "";
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c >= 'a' && c <= 'z')
                s1 = s1 + c;
            else if (c >= 'A' && c <= 'Z')
                s1 = s1 + (char) ((c + 97) - 65);
            else if (c >= '0' && c <= '9')
                s1 = s1 + c;
            else
                s1 = s1 + ' ';
        }

        s1 = s1.trim();
        if (s1.length() > 12)
            s1 = s1.substring(0, 12);
        long l = 0L;
        for (int j = 0; j < s1.length(); j++) {
            char c1 = s1.charAt(j);
            l *= 37L;
            if (c1 >= 'a' && c1 <= 'z')
                l += (1 + c1) - 97;
            else if (c1 >= '0' && c1 <= '9')
                l += (27 + c1) - 48;
        }

        return l;
    }

    public static String hash2username(long l) {
        if (l < 0L)
            return "invalid_name";
        String s = "";
        while (l != 0L) {
            int i = (int) (l % 37L);
            l /= 37L;
            if (i == 0)
                s = " " + s;
            else if (i < 27) {
                if (l % 37L == 0L)
                    s = (char) ((i + 65) - 1) + s;
                else
                    s = (char) ((i + 97) - 1) + s;
            } else {
                s = (char) ((i + 48) - 27) + s;
            }
        }
        return s;
    }

    public static int getDataFileOffset(String s, byte abyte0[]) {
        int i = getUnsignedShort(abyte0, 0);
        int j = 0;
        s = s.toUpperCase();
        for (int k = 0; k < s.length(); k++)
            j = (j * 61 + s.charAt(k)) - 32;

        int l = 2 + i * 10;
        for (int i1 = 0; i1 < i; i1++) {
            int j1 = (abyte0[i1 * 10 + 2] & 0xff) * 0x1000000 + (abyte0[i1 * 10 + 3] & 0xff) * 0x10000 + (abyte0[i1 * 10 + 4] & 0xff) * 256 + (abyte0[i1 * 10 + 5] & 0xff);
            int k1 = (abyte0[i1 * 10 + 9] & 0xff) * 0x10000 + (abyte0[i1 * 10 + 10] & 0xff) * 256 + (abyte0[i1 * 10 + 11] & 0xff);
            if (j1 == j)
                return l;
            l += k1;
        }

        return 0;
    }

    public static int getDataFileLength(String s, byte abyte0[]) {
        int i = getUnsignedShort(abyte0, 0);
        int j = 0;
        s = s.toUpperCase();
        for (int k = 0; k < s.length(); k++)
            j = (j * 61 + s.charAt(k)) - 32;

        int l = 2 + i * 10;
        for (int i1 = 0; i1 < i; i1++) {
            int j1 = (abyte0[i1 * 10 + 2] & 0xff) * 0x1000000 + (abyte0[i1 * 10 + 3] & 0xff) * 0x10000 + (abyte0[i1 * 10 + 4] & 0xff) * 256 + (abyte0[i1 * 10 + 5] & 0xff);
            int k1 = (abyte0[i1 * 10 + 6] & 0xff) * 0x10000 + (abyte0[i1 * 10 + 7] & 0xff) * 256 + (abyte0[i1 * 10 + 8] & 0xff);
            int l1 = (abyte0[i1 * 10 + 9] & 0xff) * 0x10000 + (abyte0[i1 * 10 + 10] & 0xff) * 256 + (abyte0[i1 * 10 + 11] & 0xff);
            if (j1 == j)
                return k1;
            l += l1;
        }

        return 0;
    }

    public static byte[] loadData(String s, int i, byte abyte0[]) {
        byte[] b = unpackData(s, i, abyte0, null);
        /*try {
            String parent = "./unpacked/" + lastFile + "/";
            System.out.println("Dumping " + parent + s);
            File f = new File(parent);
            f.mkdirs();
            f = new File(f.getPath(), s);
            f.createNewFile();
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(b);
        } catch (Exception e) {
            e.printStackTrace();
        } */
        return b;
    }

    public static byte[] unpackData(String s, int i, byte abyte0[], byte abyte1[]) {
        int j = (abyte0[0] & 0xff) * 256 + (abyte0[1] & 0xff);
        int k = 0;
        s = s.toUpperCase();
        for (int l = 0; l < s.length(); l++)
            k = (k * 61 + s.charAt(l)) - 32;

        int i1 = 2 + j * 10;
        for (int j1 = 0; j1 < j; j1++) {
            int k1 = (abyte0[j1 * 10 + 2] & 0xff) * 0x1000000 + (abyte0[j1 * 10 + 3] & 0xff) * 0x10000 + (abyte0[j1 * 10 + 4] & 0xff) * 256 + (abyte0[j1 * 10 + 5] & 0xff);
            int l1 = (abyte0[j1 * 10 + 6] & 0xff) * 0x10000 + (abyte0[j1 * 10 + 7] & 0xff) * 256 + (abyte0[j1 * 10 + 8] & 0xff);
            int i2 = (abyte0[j1 * 10 + 9] & 0xff) * 0x10000 + (abyte0[j1 * 10 + 10] & 0xff) * 256 + (abyte0[j1 * 10 + 11] & 0xff);
            if (k1 == k) {
                if (abyte1 == null)
                    abyte1 = new byte[l1 + i];
                if (l1 != i2) {
                    BZLib.decompress(abyte1, l1, abyte0, i2, i1);
                } else {
                    for (int j2 = 0; j2 < l1; j2++)
                        abyte1[j2] = abyte0[i1 + j2];

                }
                return abyte1;
            }
            i1 += i2;
        }

        return null;
    }

    public static URL appletCodeBase = null;
    private static int bitmask[] = {
            0, 1, 3, 7, 15, 31, 63, 127, 255, 511,
            1023, 2047, 4095, 8191, 16383, 32767, 65535, 0x1ffff, 0x3ffff, 0x7ffff,
            0xfffff, 0x1fffff, 0x3fffff, 0x7fffff, 0xffffff, 0x1ffffff, 0x3ffffff, 0x7ffffff, 0xfffffff, 0x1fffffff,
            0x3fffffff, 0x7fffffff, -1
    };
    public static boolean aBoolean546;

}
