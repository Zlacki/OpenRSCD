import java.math.BigInteger;
import java.util.zip.CRC32;

public class RSABuffer {

    private RSABuffer() {
    }

    public RSABuffer(byte buff[]) {
        buffer = buff;
        offset = 0;
    }

    public void writeByte(int i) {
        buffer[offset++] = (byte) i;
    }

    public void writeInt(int i) {
        buffer[offset++] = (byte) (i >> 24);
        buffer[offset++] = (byte) (i >> 16);
        buffer[offset++] = (byte) (i >> 8);
        buffer[offset++] = (byte) i;
    }

    public void writeString(String s) {
        s.getBytes(0, s.length(), buffer, offset);
        offset += s.length();
        buffer[offset++] = 10; // null terminate
    }

    public void writeBytes(byte buff[], int off, int len) {
        for (int k = off; k < off + len; k++)
            buffer[offset++] = buff[k];

    }

    public int getUnsignedByte() {
        return buffer[offset++] & 0xff;
    }

    public int getUnsignedShort() {
        offset += 2;
        return ((buffer[offset - 2] & 0xff) << 8) + (buffer[offset - 1] & 0xff);
    }

    public int getUnsignedInt() {
        offset += 4;
        return ((buffer[offset - 4] & 0xff) << 24) + ((buffer[offset - 3] & 0xff) << 16) + ((buffer[offset - 2] & 0xff) << 8) + (buffer[offset - 1] & 0xff);
    }

    public void readBytes(byte buff[], int off, int len) {
        for (int k = off; k < off + len; k++)
            buff[k] = buffer[offset++];

    }

    public void encrypt(BigInteger exponent, BigInteger modulus) {
        int i = offset;
        offset = 0;
        byte buf[] = new byte[i];
        readBytes(buf, 0, i);
        BigInteger biginteger2 = new BigInteger(buf);
        BigInteger biginteger3 = biginteger2.modPow(exponent, modulus);
        byte abyte1[] = biginteger3.toByteArray();
        offset = 0;
        writeByte(abyte1.length);
        writeBytes(abyte1, 0, abyte1.length);
    }

    public byte buffer[];
    public int offset;
    static CRC32 unusedCRC = new CRC32();
    private static final int unusedArray[] = {
            0, 1, 3, 7, 15, 31, 63, 127, 255, 511,
            1023, 2047, 4095, 8191, 16383, 32767, 65535, 0x1ffff, 0x3ffff, 0x7ffff,
            0xfffff, 0x1fffff, 0x3fffff, 0x7fffff, 0xffffff, 0x1ffffff, 0x3ffffff, 0x7ffffff, 0xfffffff, 0x1fffffff,
            0x3fffffff, 0x7fffffff, -1
    };

}
