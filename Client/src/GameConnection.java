import java.awt.*;
import java.io.IOException;
import java.math.BigInteger;

public class GameConnection extends GameApplet {

    protected final void login(String u, String p, boolean reconnecting) {
        if (anInt626 > 0) {
            showLoginScreenStatus("Please wait...", "Connecting to server");
            try {
                Thread.sleep(2000L);
            } catch (Exception _ex) {
            }
            showLoginScreenStatus("Sorry! The server is currently full.", "Please try again later");
            return;
        }
        try {
            username = u;
            u = Utility.formatAuthString(u, 20);
            password = p;
            p = Utility.formatAuthString(p, 20);
            if (u.trim().length() == 0) {
                showLoginScreenStatus("You must enter both a username", "and a password - Please try again");
                return;
            }
            if (reconnecting)
                drawTextBox("Connection lost! Please wait...", "Attempting to re-establish");
            else
                showLoginScreenStatus("Please wait...", "Connecting to server");
            clientStream = new ClientStream(createSocket(server, port), this);
            clientStream.maxReadTries = maxReadTries;
            long l = Utility.username2hash(u);
            clientStream.newPacket(32);
            clientStream.addByte((int) (l >> 16 & 31L));
            clientStream.flushPacket();
            long sessid = clientStream.getLong();
            sessionID = sessid;
            if (sessid == 0L) {
                showLoginScreenStatus("Login server offline.", "Please try again in a few mins");
                return;
            }
            System.out.println("Verb: Session id: " + sessid);
            int limit30 = 0;
            try {
                if (getStartedAsApplet()) {
                    String s2 = getParameter("limit30");
                    if (s2.equals("1"))
                        limit30 = 1;
                }
            } catch (Exception _ex) {
            }
            int ai[] = new int[4];
            ai[0] = (int) (Math.random() * 99999999D);
            ai[1] = (int) (Math.random() * 99999999D);
            ai[2] = (int) (sessid >> 32);
            ai[3] = (int) sessid;
            RSABuffer RSABuffer = new RSABuffer(new byte[500]);
            RSABuffer.offset = 0;
            RSABuffer.writeByte(10);
            RSABuffer.writeInt(ai[0]);
            RSABuffer.writeInt(ai[1]);
            RSABuffer.writeInt(ai[2]);
            RSABuffer.writeInt(ai[3]);
            RSABuffer.writeInt(getLinkUID());
            RSABuffer.writeString(u);
            RSABuffer.writeString(p);
            RSABuffer.encrypt(rsa_exponent, rsa_modulus);
            clientStream.newPacket(0);
            clientStream.addInt(clientVersion);
            clientStream.addLong(Utility.username2hash(u));
            clientStream.addByte(p.length());
            clientStream.addString(p);
            clientStream.flushPacket();
//            clientStream.seedIsaac(ai);
            int resp = clientStream.readStream();
            System.out.println("login response:" + resp);
            if (resp == 25) {
                moderatorLevel = 1;
                anInt612 = 0;
                resetGame();
                return;
            }
            if (resp == 0) {
                moderatorLevel = 0;
                anInt612 = 0;
                resetGame();
                return;
            }
            if (resp == 1) {
                anInt612 = 0;
                method37();
                return;
            }
            if (reconnecting) {
                u = "";
                p = "";
                resetLoginVars();
                return;
            }
            if (resp == -1) {
                showLoginScreenStatus("Error unable to login.", "Server timed out");
                return;
            }
            if (resp == 3) {
                showLoginScreenStatus("Invalid username or password.", "Try again, or create a new account");
                return;
            }
            if (resp == 4) {
                showLoginScreenStatus("That username is already logged in.", "Wait 60 seconds then retry");
                return;
            }
            if (resp == 5) {
                showLoginScreenStatus("The client has been updated.", "Please reload this page");
                return;
            }
            if (resp == 6) {
                showLoginScreenStatus("You may only use 1 character at once.", "Your ip-address is already in use");
                return;
            }
            if (resp == 7) {
                showLoginScreenStatus("Login attempts exceeded!", "Please try again in 5 minutes");
                return;
            }
            if (resp == 8) {
                showLoginScreenStatus("Error unable to login.", "Server rejected session");
                return;
            }
            if (resp == 9) {
                showLoginScreenStatus("Error unable to login.", "Loginserver rejected session");
                return;
            }
            if (resp == 10) {
                showLoginScreenStatus("That username is already in use.", "Wait 60 seconds then retry");
                return;
            }
            if (resp == 11) {
                showLoginScreenStatus("Account temporarily disabled.", "Check your message inbox for details");
                return;
            }
            if (resp == 12) {
                showLoginScreenStatus("Account permanently disabled.", "Check your message inbox for details");
                return;
            }
            if (resp == 14) {
                showLoginScreenStatus("Sorry! This world is currently full.", "Please try a different world");
                anInt626 = 1500;
                return;
            }
            if (resp == 15) {
                showLoginScreenStatus("You need a members account", "to login to this world");
                return;
            }
            if (resp == 16) {
                showLoginScreenStatus("Error - no reply from loginserver.", "Please try again");
                return;
            }
            if (resp == 17) {
                showLoginScreenStatus("Error - failed to decode profile.", "Contact customer support");
                return;
            }
            if (resp == 18) {
                showLoginScreenStatus("Account suspected stolen.", "Press 'recover a locked account' on front page.");
                return;
            }
            if (resp == 20) {
                showLoginScreenStatus("Error - loginserver mismatch", "Please try a different world");
                return;
            }
            if (resp == 21) {
                showLoginScreenStatus("Unable to login.", "That is not an RS-Classic account");
                return;
            }
            if (resp == 22) {
                showLoginScreenStatus("Password suspected stolen.", "Press 'change your password' on front page.");
                return;
            } else {
                showLoginScreenStatus("Error unable to login.", "Unrecognised response code");
                return;
            }
        } catch (Exception exception) {
            System.out.println(String.valueOf(exception));
        }
        if (anInt612 > 0) {
            try {
                Thread.sleep(5000L);
            } catch (Exception _ex) {
            }
            anInt612--;
            login(username, password, reconnecting);
        }
        if (reconnecting) {
            username = "";
            password = "";
            resetLoginVars();
        } else {
            showLoginScreenStatus("Sorry! Unable to connect.", "Check internet settings or try another world");
        }
    }

    protected final void closeConnection() {
        if (clientStream != null)
            try {
                clientStream.newPacket(31);
                clientStream.flushPacket();
            } catch (IOException _ex) {
            }
        username = "";
        password = "";
        resetLoginVars();
    }

    protected void lostConnection() {
        System.out.println("Lost connection");
        anInt612 = 10;
        login(username, password, true);
    }

    protected final void drawTextBox(String s, String s1) {
        Graphics g = getGraphics();
        Font font = new Font("Helvetica", 1, 15);
        char c = '\u0200';
        char c1 = '\u0158';
        g.setColor(Color.black);
        g.fillRect(c / 2 - 140, c1 / 2 - 25, 280, 50);
        g.setColor(Color.white);
        g.drawRect(c / 2 - 140, c1 / 2 - 25, 280, 50);
        drawString(g, s, font, c / 2, c1 / 2 - 10);
        drawString(g, s1, font, c / 2, c1 / 2 + 10);
    }

    protected final void checkConnection() {
        long l = System.currentTimeMillis();
        if (clientStream.hasPacket())
            packetLastRead = l;
        if (l - packetLastRead > 5000L) {
            packetLastRead = l;
            clientStream.newPacket(67);
            clientStream.sendPacket();
        }
        try {
            clientStream.writePacket(20);
        } catch (IOException _ex) {
            lostConnection();
            return;
        }
        if (!method43())
            return;
        int len = clientStream.readPacket(incomingPacket);
        if (len > 0)
            handlePacket(incomingPacket[0] & 0xff, len);
    }

    private final void handlePacket(int commandid, int length) {
        //commandid = clientStream.isaacCommand(commandid);
        if (commandid == 131) {
            String s = new String(incomingPacket, 1, length - 1);
            showServerMessage(s);
        }
        if (commandid == 4)
            closeConnection();
        if (commandid == 183) {
            cant_logout();
            return;
        }
        if (commandid == 71) {
            friendListCount = Utility.getUnsignedByte(incomingPacket[1]);
            for (int k = 0; k < friendListCount; k++) {
                friendListHashes[k] = Utility.getUnsignedLong(incomingPacket, 2 + k * 9);
                friendListOnline[k] = Utility.getUnsignedByte(incomingPacket[10 + k * 9]);
            }

            sortFriendsList();
            return;
        }
        if (commandid == 149) {
            long hash = Utility.getUnsignedLong(incomingPacket, 1);
            int online = incomingPacket[9] & 0xff;
            for (int i2 = 0; i2 < friendListCount; i2++)
                if (friendListHashes[i2] == hash) {
                    if (friendListOnline[i2] == 0 && online != 0)
                        showServerMessage("@pri@" + Utility.hash2username(hash) + " has logged in");
                    if (friendListOnline[i2] != 0 && online == 0)
                        showServerMessage("@pri@" + Utility.hash2username(hash) + " has logged out");
                    friendListOnline[i2] = online;
                    length = 0; // not sure what this is for
                    sortFriendsList();
                    return;
                }

            friendListHashes[friendListCount] = hash;
            friendListOnline[friendListCount] = online;
            friendListCount++;
            sortFriendsList();
            return;
        }
        if (commandid == 109) {
            ignore_list_count = Utility.getUnsignedByte(incomingPacket[1]);
            for (int i1 = 0; i1 < ignore_list_count; i1++)
                ignore_list[i1] = Utility.getUnsignedLong(incomingPacket, 2 + i1 * 8);

            return;
        }
        if (commandid == 51) {
            settingsBlockChat = incomingPacket[1];
            settingsBlockPrivate = incomingPacket[2];
            settingsBlockTrade = incomingPacket[3];
            settingsBlockDuel = incomingPacket[4];
            return;
        }
        if (commandid == 120) {
            long from = Utility.getUnsignedLong(incomingPacket, 1);
            int k1 = Utility.getUnsignedInt(incomingPacket, 9); // is this some sort of message id ?
            for (int j2 = 0; j2 < 100; j2++)
                if (anIntArray629[j2] == k1)
                    return;

            anIntArray629[anInt630] = k1;
            anInt630 = (anInt630 + 1) % 100;
            String msg = WordFilter.filter(ChatMessage.descramble(incomingPacket, 13, length - 13));
            showServerMessage("@pri@" + Utility.hash2username(from) + ": tells you " + msg);
            return;
        } else {
            handleIncomingPacket(commandid, length, incomingPacket);
            return;
        }
    }

    private final void sortFriendsList() {
        boolean flag = true;
        while (flag) {
            flag = false;
            for (int i = 0; i < friendListCount - 1; i++)
                if (friendListOnline[i] != 255 && friendListOnline[i + 1] == 255 || friendListOnline[i] == 0 && friendListOnline[i + 1] != 0) {
                    int j = friendListOnline[i];
                    friendListOnline[i] = friendListOnline[i + 1];
                    friendListOnline[i + 1] = j;
                    long l = friendListHashes[i];
                    friendListHashes[i] = friendListHashes[i + 1];
                    friendListHashes[i + 1] = l;
                    flag = true;
                }

        }
    }

    protected final void sendPrivacySettings(int chat, int priv, int trade, int duel) {
        clientStream.newPacket(64);
        clientStream.addByte(chat);
        clientStream.addByte(priv);
        clientStream.addByte(trade);
        clientStream.addByte(duel);
        clientStream.sendPacket();
    }

    protected final void ignoreAdd(String s) {
        long l = Utility.username2hash(s);
        clientStream.newPacket(132);
        clientStream.addLong(l);
        clientStream.sendPacket();
        for (int i = 0; i < ignore_list_count; i++)
            if (ignore_list[i] == l)
                return;

        if (ignore_list_count >= 100) {
            return;
        } else {
            ignore_list[ignore_list_count++] = l;
            return;
        }
    }

    protected final void ignoreRemove(long l) {
        clientStream.newPacket(241);
        clientStream.addLong(l);
        clientStream.sendPacket();
        for (int i = 0; i < ignore_list_count; i++)
            if (ignore_list[i] == l) {
                ignore_list_count--;
                for (int j = i; j < ignore_list_count; j++)
                    ignore_list[j] = ignore_list[j + 1];

                return;
            }

    }

    protected final void friendAdd(String s) {
        clientStream.newPacket(195);
        clientStream.addLong(Utility.username2hash(s));
        clientStream.sendPacket();
        long l = Utility.username2hash(s);
        for (int i = 0; i < friendListCount; i++)
            if (friendListHashes[i] == l)
                return;

        if (friendListCount >= 100) {
            return;
        } else {
            friendListHashes[friendListCount] = l;
            friendListOnline[friendListCount] = 0;
            friendListCount++;
            return;
        }
    }

    protected final void friendRemove(long l) {
        clientStream.newPacket(167);
        clientStream.addLong(l);
        clientStream.sendPacket();
        for (int i = 0; i < friendListCount; i++) {
            if (friendListHashes[i] != l)
                continue;
            friendListCount--;
            for (int j = i; j < friendListCount; j++) {
                friendListHashes[j] = friendListHashes[j + 1];
                friendListOnline[j] = friendListOnline[j + 1];
            }

            break;
        }

        showServerMessage("@pri@" + Utility.hash2username(l) + " has been removed from your friends list");
    }

    protected final void sendPrivateMessage(long u, byte buff[], int len) {
        clientStream.newPacket(218);
        clientStream.addLong(u);
        clientStream.addBytes(buff, 0, len);
        clientStream.sendPacket();
    }

    protected final void sendChatMessage(byte buff[], int len) {
        clientStream.newPacket(216);
        clientStream.addBytes(buff, 0, len);
        clientStream.sendPacket();
    }

    protected final void sendCommandString(String s) {
        clientStream.newPacket(38);
        clientStream.addString(s);
        clientStream.sendPacket();
    }

    protected void showLoginScreenStatus(String s, String s1) {
    }

    protected void method37() {
    }

    protected void resetGame() {
    }

    protected void resetLoginVars() {
    }

    protected void cant_logout() {
    }

    protected void handleIncomingPacket(int commandid, int len, byte data[]) {
    }

    protected void showServerMessage(String s) {
    }

    protected boolean method43() {
        return true;
    }

    protected int getLinkUID() {
        return 0;
    }

    public GameConnection() {
        server = "127.0.0.1";
        port = 43591;
        username = "";
        password = "";
        incomingPacket = new byte[5000];
        friendListHashes = new long[200];
        friendListOnline = new int[200];
        ignore_list = new long[100];
        anIntArray629 = new int[100];
    }

    public static int clientVersion = 208;
    public static int maxReadTries;
    public String server;
    public int port;
    String username;
    String password;
    public ClientStream clientStream;
    byte incomingPacket[];
    int anInt612;
    long packetLastRead;
    public int friendListCount;
    public long friendListHashes[];
    public int friendListOnline[];
    public int ignore_list_count;
    public long ignore_list[];
    public int settingsBlockChat;
    public int settingsBlockPrivate;
    public int settingsBlockTrade;
    public int settingsBlockDuel;
    private static BigInteger rsa_exponent = new BigInteger("58778699976184461502525193738213253649000149147835990136706041084440742975821");
    //private static BigInteger rsa_modulus = new BigInteger("7162900525229798032761816791230527296329313291232324290237849263501208207972894053929065636522363163621000728841182238772712427862772219676577293600221789");
    private static BigInteger rsa_modulus = new BigInteger("7656522762491711741880224224809835569769759737077076094091609307381193032090602256314159126169417567841597729801408692196383745596665658895073411749475443");
    public long sessionID;
    public int anInt626;
    public int moderatorLevel;
    private final int anInt628 = 100;
    private int anIntArray629[];
    private int anInt630;

}
