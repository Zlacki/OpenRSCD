import java.io.IOException;

public class Packet {

    public void seedIsaac(int seed[]) {
        isaacIncoming = new ISAAC(seed);
        isaacOutgoing = new ISAAC(seed);
    }

    public void closeStream() {
    }

    public void readBytes(int len, byte buff[])
            throws IOException {
        readStreamBytes(len, 0, buff);
    }

    public int readPacket(byte buff[]) {
        try {
            readTries++;
            if (maxReadTries > 0 && readTries > maxReadTries) {
                socketException = true;
                socketExceptionMessage = "time-out";
                maxReadTries += maxReadTries;
                return 0;
            }
            if (length == 0 && availableStream() >= 2)
		length = ((((readStream() & 0xFF) << 8) | (readStream() & 0xFF)));
            if (length > 0 && availableStream() >= length) {
                readBytes(length, buff);
                int i = length;
                length = 0;
                readTries = 0;
                return i;
            }
        } catch (IOException ioexception) {
            socketException = true;
            socketExceptionMessage = ioexception.getMessage();
        }
        return 0;
    }

    public int availableStream()
            throws IOException {
        return 0;
    }

    public void readStreamBytes(int i, int j, byte abyte0[])
            throws IOException {
    }

    public boolean hasPacket() {
        return packetStart > 0;
    }

    public void writePacket(int i)
            throws IOException {
        if (socketException) {
            packetStart = 0;
            packetEnd = 3;
            socketException = false;
            throw new IOException(socketExceptionMessage);
        }
        delay++;
        if (delay < i)
            return;
        if (packetStart > 0) {
            delay = 0;
            writeStreamBytes(packetData, 0, packetStart);
        }
        packetStart = 0;
        packetEnd = 3;
    }

    public void sendPacket() {
/*        if (isaacOutgoing != null) {
            int i = packetData[packetStart + 2] & 0xff;
            packetData[packetStart + 2] = (byte) (i + isaacOutgoing.getNextValue());
        }
        if (packet8Check != 8) // what the fuck is this even for? legacy?
            packetEnd++;
*/
        int j = packetEnd - packetStart - 2;
        packetData[packetStart] = (byte) (j >> 8);
        packetData[packetStart + 1] = (byte) j;
/*
        if (packetMaxLength <= 10000) // this seems largely useless and doesn't appear to do anything
        {
            int k = packetData[packetStart + 2] & 0xff;
            anIntArray537[k]++;
            anIntArray541[k] += packetEnd - packetStart;
        }
*/
        packetStart = packetEnd;
    }

    public void addBytes(byte abyte0[], int i, int j) {
        for (int k = 0; k < j; k++)
            packetData[packetEnd++] = abyte0[i + k];

    }

    public void addLong(long l) {
        addInt((int) (l >> 32));
        addInt((int) (l & -1L));
    }

    public void newPacket(int i) {
        if (packetStart > (packetMaxLength * 4) / 5)
            try {
                writePacket(0);
            } catch (IOException ioexception) {
                socketException = true;
                socketExceptionMessage = ioexception.getMessage();
            }
        if (packetData == null)
            packetData = new byte[packetMaxLength];
        packetData[packetStart + 2] = (byte) i;
        packetData[packetStart + 3] = 0;
        packetEnd = packetStart + 3;
        packet8Check = 8;
    }

    public void writeStreamBytes(byte abyte0[], int i, int j)
            throws IOException {
    }

    public int readStream()
            throws IOException {
        return 0;
    }

    public long getLong()
            throws IOException {
        long l = getShort();
        long l1 = getShort();
        long l2 = getShort();
        long l3 = getShort();
        return (l << 48) + (l1 << 32) + (l2 << 16) + l3;
    }

    public void addShort(int i) {
        packetData[packetEnd++] = (byte) (i >> 8);
        packetData[packetEnd++] = (byte) i;
    }

    public void addInt(int i) {
        packetData[packetEnd++] = (byte) (i >> 24);
        packetData[packetEnd++] = (byte) (i >> 16);
        packetData[packetEnd++] = (byte) (i >> 8);
        packetData[packetEnd++] = (byte) i;
    }

    public int getShort()
            throws IOException {
        int i = getByte();
        int j = getByte();
        return i * 256 + j;
    }

    public void addString(String s) {
        s.getBytes(0, s.length(), packetData, packetEnd);
        packetEnd += s.length();
    }

    public void addByte(int i) {
        packetData[packetEnd++] = (byte) i;
    }

    public int isaacCommand(int i) {
        return i - isaacIncoming.getNextValue() & 0xff;
    }

    public int getByte()
            throws IOException {
        return readStream();
    }

    public void flushPacket()
            throws IOException {
        sendPacket();
        writePacket(0);
    }

    public Packet() {
        packetEnd = 3;
        packet8Check = 8;
        packetMaxLength = 5000;
        socketException = false;
        socketExceptionMessage = "";
    }

    protected int length;
    public int readTries;
    public int maxReadTries;
    public int packetStart;
    private int packetEnd;
    private int packet8Check;
    public byte packetData[];
    /*private static int anIntArray521[] = {
        0, 1, 3, 7, 15, 31, 63, 127, 255, 511, 
        1023, 2047, 4095, 8191, 16383, 32767, 65535, 0x1ffff, 0x3ffff, 0x7ffff, 
        0xfffff, 0x1fffff, 0x3fffff, 0x7fffff, 0xffffff, 0x1ffffff, 0x3ffffff, 0x7ffffff, 0xfffffff, 0x1fffffff, 
        0x3fffffff, 0x7fffffff, -1
    };
    final int anInt522 = 61;
    final int anInt523 = 59;
    final int anInt524 = 42;
    final int anInt525 = 43;
    final int anInt526 = 44;
    final int anInt527 = 45;
    final int anInt528 = 46;
    final int anInt529 = 47;
    final int anInt530 = 92;
    final int anInt531 = 32;
    final int anInt532 = 124;
    final int anInt533 = 34;    */
    public ISAAC isaacIncoming;
    public ISAAC isaacOutgoing;
    //static char charMap[];
    public static int anIntArray537[] = new int[256];
    protected int packetMaxLength;
    protected boolean socketException;
    protected String socketExceptionMessage;
    public static int anIntArray541[] = new int[256];
    protected int delay;
    // public static int anInt543;

    /*static
    {
        charMap = new char[256];
        for(int i = 0; i < 256; i++)
            charMap[i] = (char)i;

        charMap[61] = '=';
        charMap[59] = ';';
        charMap[42] = '*';
        charMap[43] = '+';
        charMap[44] = ',';
        charMap[45] = '-';
        charMap[46] = '.';
        charMap[47] = '/';
        charMap[92] = '\\';
        charMap[124] = '|';
        charMap[33] = '!';
        charMap[34] = '"';
    }            */
}
