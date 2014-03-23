import java.applet.Applet;
import java.awt.*;
import java.awt.image.IndexColorModel;
import java.awt.image.MemoryImageSource;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;

public class GameApplet extends Applet
        implements Runnable {

    protected void startGame() {
    }

    protected synchronized void handle_inputs() {
    }

    protected void on_closing() {
    }

    protected synchronized void draw() {
    }

    protected final void startApplication(int w, int h, String title, boolean resizeable) {
        startedAsApplet = false;
        System.out.println("Started application");
        appletWidth = w;
        appletHeight = h;
        gameFrameReference = new GameFrame(this, w, h, title, resizeable, false);
        loadingStep = 1;
        appletThread = new Thread(this);
        appletThread.start();
        appletThread.setPriority(1);
    }

    protected final boolean getStartedAsApplet() {
        return startedAsApplet;
    }

    protected final void method8(int i) {
        anInt4 = 1000 / i;
    }

    protected final void resetTimings() {
        for (int i = 0; i < 10; i++)
            timings[i] = 0L;

    }

    public final synchronized boolean keyDown(Event event, int i) {
        handleKeyPress(i);
        //unusedKeyCode1 = i;
        //unusedKeyCode2 = i;
        lastMouseAction = 0;
        if (i == 1006)
            key_left = true;
        if (i == 1007)
            key_right = true;
        if (i == 1004)
            key_up = true;
        if (i == 1005)
            key_down = true;
        if ((char) i == ' ')
            key_space = true;
        /*if ((char) i == 'n' || (char) i == 'm')
            key_nm = true;
        if ((char) i == 'N' || (char) i == 'M')
            key_nm = true;
        if ((char) i == '{')
            key_lsb = true;
        if ((char) i == '}')
            key_rsb = true;*/
        if ((char) i == '\u03F0')
            interlace = !interlace;
        boolean found_text = false;
        for (int j = 0; j < char_map.length(); j++) {
            if (i != char_map.charAt(j))
                continue;
            found_text = true;
            break;
        }

        if (found_text && input_text_current.length() < 20)
            input_text_current += (char) i;
        if (found_text && input_pm_current.length() < 80)
            input_pm_current += (char) i;
        if (i == 8 && input_text_current.length() > 0)
            input_text_current = input_text_current.substring(0, input_text_current.length() - 1);
        if (i == 8 && input_pm_current.length() > 0)
            input_pm_current = input_pm_current.substring(0, input_pm_current.length() - 1);
        if (i == 10 || i == 13) {
            input_text_final = input_text_current;
            input_pm_final = input_pm_current;
        }
        return true;
    }

    protected void handleKeyPress(int i) {
    }

    public final synchronized boolean keyUp(Event event, int i) {
        //unusedKeyCode1 = 0;
        if (i == 1006)
            key_left = false;
        if (i == 1007)
            key_right = false;
        if (i == 1004)
            key_up = false;
        if (i == 1005)
            key_down = false;
        if ((char) i == ' ')
            key_space = false;
        /*if ((char) i == 'n' || (char) i == 'm')
            key_nm = false;
        if ((char) i == 'N' || (char) i == 'M')
            key_nm = false;
        if ((char) i == '{')
            key_lsb = false;
        if ((char) i == '}')
            key_rsb = false;*/
        return true;
    }

    public final synchronized boolean mouseMove(Event event, int i, int j) {
        mouseX = i;
        mouseY = j + yOffset;
        mouseButtonDown = 0;
        lastMouseAction = 0;
        return true;
    }

    public final synchronized boolean mouseUp(Event event, int i, int j) {
        mouseX = i;
        mouseY = j + yOffset;
        mouseButtonDown = 0;
        return true;
    }

    public final synchronized boolean mouseDown(Event event, int i, int j) {
        mouseX = i;
        mouseY = j + yOffset;
        if (event.metaDown())
            mouseButtonDown = 2;
        else
            mouseButtonDown = 1;
        lastMouseButtonDown = mouseButtonDown;
        lastMouseAction = 0;
        handleMouseDown(mouseButtonDown, i, j);
        return true;
    }

    protected void handleMouseDown(int i, int j, int k) {
    }

    public final synchronized boolean mouseDrag(Event event, int i, int j) {
        mouseX = i;
        mouseY = j + yOffset;
        if (event.metaDown())
            mouseButtonDown = 2;
        else
            mouseButtonDown = 1;
        return true;
    }

    public final void init() {
        startedAsApplet = true;
        System.out.println("Started applet");
        appletWidth = 512;
        appletHeight = 344;
        loadingStep = 1;
        Utility.appletCodeBase = getCodeBase();
        startThread(this);
    }

    public final void start() {
        if (stop_timeout >= 0)
            stop_timeout = 0;
    }

    public final void stop() {
        if (stop_timeout >= 0)
            stop_timeout = 4000 / anInt4;
    }

    public final void destroy() {
        stop_timeout = -1;
        try {
            Thread.sleep(5000L);
        } catch (Exception _ex) {
        }
        if (stop_timeout == -1) {
            System.out.println("5 seconds expired, forcing kill");
            close_program();
            if (appletThread != null) {
                appletThread.stop();
                appletThread = null;
            }
        }
    }

    private final void close_program() {
        stop_timeout = -2;
        System.out.println("Closing program");
        on_closing();
        try {
            Thread.sleep(1000L);
        } catch (Exception _ex) {
        }
        if (gameFrameReference != null)
            gameFrameReference.dispose();
        if (!startedAsApplet)
            System.exit(0);
    }

    public final void run() {
        if (loadingStep == 1) {
            loadingStep = 2;
            graphics = getGraphics();
            load_jagex();
            draw_loading_screen(0, "Loading...");
            startGame();
            loadingStep = 0;
        }
        int i = 0;
        int j = 256;
        int sleep = 1;
        int i1 = 0;
        for (int j1 = 0; j1 < 10; j1++)
            timings[j1] = System.currentTimeMillis();

        //long l = System.currentTimeMillis();
        while (stop_timeout >= 0) {
            if (stop_timeout > 0) {
                stop_timeout--;
                if (stop_timeout == 0) {
                    close_program();
                    appletThread = null;
                    return;
                }
            }
            int k1 = j;
            int last_sleep = sleep;
            j = 300;
            sleep = 1;
            long time = System.currentTimeMillis();
            if (timings[i] == 0L) {
                j = k1;
                sleep = last_sleep;
            } else if (time > timings[i])
                j = (int) ((long) (2560 * anInt4) / (time - timings[i]));
            if (j < 25)
                j = 25;
            if (j > 256) {
                j = 256;
                sleep = (int) ((long) anInt4 - (time - timings[i]) / 10L);
                if (sleep < threadSleep)
                    sleep = threadSleep;
            }
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException _ex) {
            }
            timings[i] = time;
            i = (i + 1) % 10;
            if (sleep > 1) {
                for (int j2 = 0; j2 < 10; j2++)
                    if (timings[j2] != 0L)
                        timings[j2] += sleep;

            }
            int k2 = 0;
            while (i1 < 256) {
                handle_inputs();
                i1 += j;
                if (++k2 > max_draw_time) {
                    i1 = 0;
                    interlace_timer += 6;
                    if (interlace_timer > 25) {
                        interlace_timer = 0;
                        interlace = true;
                    }
                    break;
                }
            }
            interlace_timer--;
            i1 &= 0xff;
            draw();
        }
        if (stop_timeout == -1)
            close_program();
        appletThread = null;
    }

    public final void update(Graphics g) {
        paint(g);
    }

    public final void paint(Graphics g) {
        if (loadingStep == 2 && image_logo != null) {
            draw_loading_screen(loadingProgressPercent, loadingProgessText);
            //return;
        }
        //if (loadingStep == 0)
        //    emptyMethod();
    }

    private final void load_jagex() {
        graphics.setColor(Color.black);
        graphics.fillRect(0, 0, appletWidth, appletHeight);
        byte buff[] = readDataFile("jagex.jag", "Jagex library", 0);
        if (buff != null) {
            byte logo[] = Utility.loadData("logo.tga", 0, buff);
            image_logo = createImage(logo);
            Surface.createFont("h11p", 0, this);
            Surface.createFont("h12b", 1, this);
            Surface.createFont("h12p", 2, this);
            Surface.createFont("h13b", 3, this);
            Surface.createFont("h14b", 4, this);
            Surface.createFont("h16b", 5, this);
            Surface.createFont("h20b", 6, this);
            Surface.createFont("h24b", 7, this);
        }
    }

    private final void draw_loading_screen(int percent, String text) {
        try {
            int midx = (appletWidth - 281) / 2;
            int midy = (appletHeight - 148) / 2;
            graphics.setColor(Color.black);
            graphics.fillRect(0, 0, appletWidth, appletHeight);
            if (!has_referer_logo_notused)
                graphics.drawImage(image_logo, midx, midy, this);
            midx += 2;
            midy += 90;
            loadingProgressPercent = percent;
            loadingProgessText = text;
            graphics.setColor(new Color(132, 132, 132));
            if (has_referer_logo_notused)
                graphics.setColor(new Color(220, 0, 0));
            graphics.drawRect(midx - 2, midy - 2, 280, 23);
            graphics.fillRect(midx, midy, (277 * percent) / 100, 20);
            graphics.setColor(new Color(198, 198, 198));
            if (has_referer_logo_notused)
                graphics.setColor(new Color(255, 255, 255));
            drawString(graphics, text, font_timesroman_15, midx + 138, midy + 10);
            if (!has_referer_logo_notused) {
                drawString(graphics, "Created by JAGeX - visit www.jagex.com", font_helvetica_13, midx + 138, midy + 30);
                drawString(graphics, "\2512001-2002 Andrew Gower and Jagex Ltd", font_helvetica_13, midx + 138, midy + 44);
            } else {
                graphics.setColor(new Color(132, 132, 152));
                drawString(graphics, "\2512001-2002 Andrew Gower and Jagex Ltd", font_helvetica_12, midx + 138, appletHeight - 20);
            }
            if (logo_header_text != null) {
                graphics.setColor(Color.white);
                drawString(graphics, logo_header_text, font_helvetica_13, midx + 138, midy - 120);
            }
        } catch (Exception _ex) {
        }
    }

    protected final void showLoadingProgress(int i, String s) {
        try {
            int j = (appletWidth - 281) / 2;
            int k = (appletHeight - 148) / 2;
            j += 2;
            k += 90;
            loadingProgressPercent = i;
            loadingProgessText = s;
            int l = (277 * i) / 100;
            graphics.setColor(new Color(132, 132, 132));
            if (has_referer_logo_notused)
                graphics.setColor(new Color(220, 0, 0));
            graphics.fillRect(j, k, l, 20);
            graphics.setColor(Color.black);
            graphics.fillRect(j + l, k, 277 - l, 20);
            graphics.setColor(new Color(198, 198, 198));
            if (has_referer_logo_notused)
                graphics.setColor(new Color(255, 255, 255));
            drawString(graphics, s, font_timesroman_15, j + 138, k + 10);
            return;
        } catch (Exception _ex) {
            return;
        }
    }

    protected final void drawString(Graphics g, String s, Font font, int i, int j) {
        Object obj;
        if (gameFrameReference == null)
            obj = this;
        else
            obj = gameFrameReference;
        FontMetrics fontmetrics = ((Component) (obj)).getFontMetrics(font);
        fontmetrics.stringWidth(s);
        g.setFont(font);
        g.drawString(s, i - fontmetrics.stringWidth(s) / 2, j + fontmetrics.getHeight() / 4);
    }

    private final Image createImage(byte buff[]) {
        int i = buff[13] * 256 + buff[12];
        int j = buff[15] * 256 + buff[14];
        byte abyte1[] = new byte[256];
        byte abyte2[] = new byte[256];
        byte abyte3[] = new byte[256];
        for (int k = 0; k < 256; k++) {
            abyte1[k] = buff[20 + k * 3];
            abyte2[k] = buff[19 + k * 3];
            abyte3[k] = buff[18 + k * 3];
        }

        IndexColorModel indexcolormodel = new IndexColorModel(8, 256, abyte1, abyte2, abyte3);
        byte abyte4[] = new byte[i * j];
        int l = 0;
        for (int i1 = j - 1; i1 >= 0; i1--) {
            for (int j1 = 0; j1 < i; j1++)
                abyte4[l++] = buff[786 + j1 + i1 * i];

        }

        MemoryImageSource memoryimagesource = new MemoryImageSource(i, j, indexcolormodel, abyte4, 0, i);
        return createImage(memoryimagesource);
    }

    protected byte[] readDataFile(String file, String prettyname, int percent) {
        //System.out.println("Using default load");
        int expected_total = 0;
        int total = 0;
        byte buff[] = null;
        try {
            showLoadingProgress(percent, "Loading " + prettyname + " - 0%");
            java.io.InputStream inputstream = Utility.openFile(file);
            DataInputStream datainputstream = new DataInputStream(inputstream);
            byte header[] = new byte[6];
            datainputstream.readFully(header, 0, 6);
            expected_total = ((header[0] & 0xff) << 16) + ((header[1] & 0xff) << 8) + (header[2] & 0xff);
            total = ((header[3] & 0xff) << 16) + ((header[4] & 0xff) << 8) + (header[5] & 0xff);
            showLoadingProgress(percent, "Loading " + prettyname + " - 5%");
            int read = 0;
            buff = new byte[total];
            while (read < total) {
                int size = total - read;
                if (size > 1000)
                    size = 1000;
                datainputstream.readFully(buff, read, size);
                read += size;
                showLoadingProgress(percent, "Loading " + prettyname + " - " + (5 + (read * 95) / total) + "%");
            }
            datainputstream.close();
        } catch (IOException _ex) {
        }
        showLoadingProgress(percent, "Unpacking " + prettyname);
        if (total != expected_total) {
            byte decompressed[] = new byte[expected_total];
            BZLib.decompress(decompressed, expected_total, buff, total, 0);
            return decompressed;
        } else {
            return buff;
        }
    }

    public Graphics getGraphics() {
        if (gameFrameReference != null)
            return gameFrameReference.getGraphics();
        else
            return super.getGraphics();
    }

    public Image createImage(int i, int j) {
        if (gameFrameReference != null)
            return gameFrameReference.createImage(i, j);
        else
            return super.createImage(i, j);
    }

    public URL getCodeBase() {
        return super.getCodeBase();
    }

    public URL getDocumentBase() {
        return super.getDocumentBase();
    }

    public String getParameter(String s) {
        return super.getParameter(s);
    }

    protected Socket createSocket(String s, int i)
            throws IOException {
        Socket socket;
        if (getStartedAsApplet())
            socket = new Socket(InetAddress.getByName(getCodeBase().getHost()), i);
        else
            socket = new Socket(InetAddress.getByName(s), i);
        socket.setSoTimeout(30000);
        socket.setTcpNoDelay(true);
        return socket;
    }

    protected void startThread(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.setDaemon(true);
        thread.start();
    }

    public GameApplet() {
        appletWidth = 512;
        appletHeight = 384;
        anInt4 = 20;
        max_draw_time = 1000;
        timings = new long[10];
        loadingStep = 1;
        has_referer_logo_notused = false;
        loadingProgessText = "Loading";
        font_timesroman_15 = new Font("TimesRoman", 0, 15);
        font_helvetica_13 = new Font("Helvetica", 1, 13);
        font_helvetica_12 = new Font("Helvetica", 0, 12);
        //key_lsb = false;
        //key_rsb = false;
        key_left = false;
        key_right = false;
        key_up = false;
        key_down = false;
        key_space = false;
        //key_nm = false;
        threadSleep = 1;
        interlace = false;
        input_text_current = "";
        input_text_final = "";
        input_pm_current = "";
        input_pm_final = "";
    }

    private int appletWidth;
    private int appletHeight;
    private Thread appletThread;
    private int anInt4;
    private int max_draw_time;
    private long timings[];
    public static GameFrame gameFrameReference = null;
    private boolean startedAsApplet;
    private int stop_timeout;
    private int interlace_timer;
    public int yOffset;
    public int lastMouseAction;
    public int loadingStep;
    public String logo_header_text;
    private boolean has_referer_logo_notused;
    private int loadingProgressPercent;
    private String loadingProgessText;
    private Font font_timesroman_15;
    private Font font_helvetica_13;
    private Font font_helvetica_12;
    private Image image_logo;
    private Graphics graphics;
    private static String char_map = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!\"\243$%^&*()-_=+[{]};:'@#~,<.>/?\\| ";
    //public boolean key_lsb;
    //public boolean key_rsb;
    public boolean key_left;
    public boolean key_right;
    public boolean key_up;
    public boolean key_down;
    public boolean key_space;
    //public boolean key_nm;
    public int threadSleep;
    public int mouseX;
    public int mouseY;
    public int mouseButtonDown;
    public int lastMouseButtonDown;
    //public int unusedKeyCode1;
    //public int unusedKeyCode2;
    public boolean interlace;
    public String input_text_current;
    public String input_text_final;
    public String input_pm_current;
    public String input_pm_final;

}
