import java.awt.*;

public class GameFrame extends Frame {

    public GameFrame(GameApplet gameApplet_, int i, int j, String s, boolean flag, boolean flag1) {
        anInt47 = 28;
        anInt44 = i;
        anInt45 = j;
        anGameApplet__48 = gameApplet_;
        if (flag1)
            anInt47 = 48;
        else
            anInt47 = 28;
        setTitle(s);
        setResizable(flag);
        show();
        toFront();
        resize(anInt44, anInt45);
        aGraphics49 = getGraphics();
    }

    public Graphics getGraphics() {
        Graphics g = super.getGraphics();
        if (anInt46 == 0)
            g.translate(0, 24);
        else
            g.translate(-5, 0);
        return g;
    }

    public void resize(int i, int j) {
        super.resize(i, j + anInt47);
    }

    public boolean handleEvent(Event event) {
        if (event.id == 401)
            anGameApplet__48.keyDown(event, event.key);
        else if (event.id == 402)
            anGameApplet__48.keyUp(event, event.key);
        else if (event.id == 501)
            anGameApplet__48.mouseDown(event, event.x, event.y - 24);
        else if (event.id == 506)
            anGameApplet__48.mouseDrag(event, event.x, event.y - 24);
        else if (event.id == 502)
            anGameApplet__48.mouseUp(event, event.x, event.y - 24);
        else if (event.id == 503)
            anGameApplet__48.mouseMove(event, event.x, event.y - 24);
        else if (event.id == 201)
            anGameApplet__48.destroy();
        else if (event.id == 1001)
            anGameApplet__48.action(event, event.target);
        else if (event.id == 403)
            anGameApplet__48.keyDown(event, event.key);
        else if (event.id == 404)
            anGameApplet__48.keyUp(event, event.key);
        return true;
    }

    public final void paint(Graphics g) {
        anGameApplet__48.paint(g);
    }

    int anInt44;
    int anInt45;
    int anInt46;
    int anInt47;
    GameApplet anGameApplet__48;
    Graphics aGraphics49;
}
