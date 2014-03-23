import java.awt.*;
import java.awt.image.*;

public class Surface
        implements ImageProducer, ImageObserver {

    public Surface(int width, int height, int limit, Component component) {
        interlace = false;
        loggedIn = false;
        aComponent319 = component;
        bounds_bottom_y = height;
        bounds_bottom_x = width;
        width1 = width2 = width;
        height1 = height2 = height;
        area = width * height;
        pixels = new int[width * height];
        surface_set_pixels = new int[limit][];
        image_translate = new boolean[limit];
        aByteArrayArray322 = new byte[limit][];
        anIntArrayArray323 = new int[limit][];
        image_width = new int[limit];
        image_height = new int[limit];
        image_full_width = new int[limit];
        image_full_height = new int[limit];
        offset_x = new int[limit];
        offset_y = new int[limit];
        if (width > 1 && height > 1 && component != null) {
            color_model = new DirectColorModel(32, 0xff0000, 65280, 255);
            int l = width2 * height2;
            for (int i1 = 0; i1 < l; i1++)
                pixels[i1] = 0;

            image = component.createImage(this);
            setcomplete();
            component.prepareImage(image, component);
            setcomplete();
            component.prepareImage(image, component);
            setcomplete();
            component.prepareImage(image, component);
        }
    }

    public synchronized void addConsumer(ImageConsumer imageconsumer) {
        this.imageconsumer = imageconsumer;
        imageconsumer.setDimensions(width2, height2);
        imageconsumer.setProperties(null);
        imageconsumer.setColorModel(color_model);
        imageconsumer.setHints(14);
    }

    public synchronized boolean isConsumer(ImageConsumer imageconsumer) {
        return this.imageconsumer == imageconsumer;
    }

    public synchronized void removeConsumer(ImageConsumer imageconsumer) {
        if (this.imageconsumer == imageconsumer)
            this.imageconsumer = null;
    }

    public void startProduction(ImageConsumer imageconsumer) {
        addConsumer(imageconsumer);
    }

    public void requestTopDownLeftRightResend(ImageConsumer imageconsumer) {
        System.out.println("TDLR");
    }

    public synchronized void setcomplete() {
        if (imageconsumer == null) {
            return;
        } else {
            imageconsumer.setPixels(0, 0, width2, height2, color_model, pixels, 0, width2);
            imageconsumer.imageComplete(2);
            return;
        }
    }

    public void set_bounds(int x1, int y1, int x2, int y2) {
        if (x1 < 0)
            x1 = 0;
        if (y1 < 0)
            y1 = 0;
        if (x2 > width2)
            x2 = width2;
        if (y2 > height2)
            y2 = height2;
        bounds_top_x = x1;
        bounds_top_y = y1;
        bounds_bottom_x = x2;
        bounds_bottom_y = y2;
    }

    public void reset_bounds() {
        bounds_top_x = 0;
        bounds_top_y = 0;
        bounds_bottom_x = width2;
        bounds_bottom_y = height2;
    }

    public void draw(Graphics g, int x, int y) {
        setcomplete();
        g.drawImage(image, x, y, this);
    }

    public void black_screen() {
        int area = width2 * height2;
        if (!interlace) {
            for (int j = 0; j < area; j++)
                pixels[j] = 0;

            return;
        }
        int k = 0;
        for (int l = -height2; l < 0; l += 2) {
            for (int i1 = -width2; i1 < 0; i1++)
                pixels[k++] = 0;

            k += width2;
        }

    }

    public void draw_circle(int x, int y, int size, int colour, int alpha) {
        int bg_alpha = 256 - alpha;
        int red = (colour >> 16 & 0xff) * alpha;
        int green = (colour >> 8 & 0xff) * alpha;
        int blue = (colour & 0xff) * alpha;
        int i3 = y - size;
        if (i3 < 0)
            i3 = 0;
        int j3 = y + size;
        if (j3 >= height2)
            j3 = height2 - 1;
        byte vert_inc = 1;
        if (interlace) {
            vert_inc = 2;
            if ((i3 & 1) != 0)
                i3++;
        }
        for (int k3 = i3; k3 <= j3; k3 += vert_inc) {
            int l3 = k3 - y;
            int i4 = (int) Math.sqrt(size * size - l3 * l3);
            int j4 = x - i4;
            if (j4 < 0)
                j4 = 0;
            int k4 = x + i4;
            if (k4 >= width2)
                k4 = width2 - 1;
            int pixel_idx = j4 + k3 * width2;
            for (int i5 = j4; i5 <= k4; i5++) {
                int bg_red = (pixels[pixel_idx] >> 16 & 0xff) * bg_alpha;
                int bg_green = (pixels[pixel_idx] >> 8 & 0xff) * bg_alpha;
                int bg_blue = (pixels[pixel_idx] & 0xff) * bg_alpha;
                int new_colour = ((red + bg_red >> 8) << 16) + ((green + bg_green >> 8) << 8) + (blue + bg_blue >> 8);
                pixels[pixel_idx++] = new_colour;
            }

        }

    }

    public void draw_box_alpha(int x, int y, int width, int height, int colour, int alpha) {
        if (x < bounds_top_x) {
            width -= bounds_top_x - x;
            x = bounds_top_x;
        }
        if (y < bounds_top_y) {
            height -= bounds_top_y - y;
            y = bounds_top_y;
        }
        if (x + width > bounds_bottom_x)
            width = bounds_bottom_x - x;
        if (y + height > bounds_bottom_y)
            height = bounds_bottom_y - y;
        int bg_alpha = 256 - alpha;
        int red = (colour >> 16 & 0xff) * alpha;
        int green = (colour >> 8 & 0xff) * alpha;
        int blue = (colour & 0xff) * alpha;
        int j3 = width2 - width;
        byte vert_inc = 1;
        if (interlace) {
            vert_inc = 2;
            j3 += width2;
            if ((y & 1) != 0) {
                y++;
                height--;
            }
        }
        int pixel_idx = x + y * width2;
        for (int l3 = 0; l3 < height; l3 += vert_inc) {
            for (int i4 = -width; i4 < 0; i4++) {
                int bg_red = (pixels[pixel_idx] >> 16 & 0xff) * bg_alpha;
                int bg_green = (pixels[pixel_idx] >> 8 & 0xff) * bg_alpha;
                int bg_blue = (pixels[pixel_idx] & 0xff) * bg_alpha;
                int new_colour = ((red + bg_red >> 8) << 16) + ((green + bg_green >> 8) << 8) + (blue + bg_blue >> 8);
                pixels[pixel_idx++] = new_colour;
            }

            pixel_idx += j3;
        }

    }

    public void draw_gradient(int x, int y, int width, int height, int colour_top, int colour_bottom) {
        if (x < bounds_top_x) {
            width -= bounds_top_x - x;
            x = bounds_top_x;
        }
        if (x + width > bounds_bottom_x)
            width = bounds_bottom_x - x;
        int btm_red = colour_bottom >> 16 & 0xff;
        int btm_green = colour_bottom >> 8 & 0xff;
        int btm_blue = colour_bottom & 0xff;
        int top_red = colour_top >> 16 & 0xff;
        int top_green = colour_top >> 8 & 0xff;
        int top_blue = colour_top & 0xff;
        int i3 = width2 - width;
        byte vert_inc = 1;
        if (interlace) {
            vert_inc = 2;
            i3 += width2;
            if ((y & 1) != 0) {
                y++;
                height--;
            }
        }
        int pixel_idx = x + y * width2;
        for (int k3 = 0; k3 < height; k3 += vert_inc)
            if (k3 + y >= bounds_top_y && k3 + y < bounds_bottom_y) {
                int new_colour = ((btm_red * k3 + top_red * (height - k3)) / height << 16) + ((btm_green * k3 + top_green * (height - k3)) / height << 8) + (btm_blue * k3 + top_blue * (height - k3)) / height;
                for (int i4 = -width; i4 < 0; i4++)
                    pixels[pixel_idx++] = new_colour;

                pixel_idx += i3;
            } else {
                pixel_idx += width2;
            }

    }

    public void draw_box(int x, int y, int w, int h, int colour) {
        if (x < bounds_top_x) {
            w -= bounds_top_x - x;
            x = bounds_top_x;
        }
        if (y < bounds_top_y) {
            h -= bounds_top_y - y;
            y = bounds_top_y;
        }
        if (x + w > bounds_bottom_x)
            w = bounds_bottom_x - x;
        if (y + h > bounds_bottom_y)
            h = bounds_bottom_y - y;
        int j1 = width2 - w;
        byte vert_inc = 1;
        if (interlace) {
            vert_inc = 2;
            j1 += width2;
            if ((y & 1) != 0) {
                y++;
                h--;
            }
        }
        int pixel_idx = x + y * width2;
        for (int l1 = -h; l1 < 0; l1 += vert_inc) {
            for (int i2 = -w; i2 < 0; i2++)
                pixels[pixel_idx++] = colour;

            pixel_idx += j1;
        }

    }

    public void draw_box_edge(int x, int y, int width, int height, int colour) {
        draw_line_horiz(x, y, width, colour);
        draw_line_horiz(x, (y + height) - 1, width, colour);
        draw_line_vert(x, y, height, colour);
        draw_line_vert((x + width) - 1, y, height, colour);
    }

    public void draw_line_horiz(int x, int y, int width, int colour) {
        if (y < bounds_top_y || y >= bounds_bottom_y)
            return;
        if (x < bounds_top_x) {
            width -= bounds_top_x - x;
            x = bounds_top_x;
        }
        if (x + width > bounds_bottom_x)
            width = bounds_bottom_x - x;
        int i1 = x + y * width2;
        for (int j1 = 0; j1 < width; j1++)
            pixels[i1 + j1] = colour;

    }

    public void draw_line_vert(int x, int y, int height, int colour) {
        if (x < bounds_top_x || x >= bounds_bottom_x)
            return;
        if (y < bounds_top_y) {
            height -= bounds_top_y - y;
            y = bounds_top_y;
        }
        if (y + height > bounds_bottom_x)
            height = bounds_bottom_y - y;
        int i1 = x + y * width2;
        for (int j1 = 0; j1 < height; j1++)
            pixels[i1 + j1 * width2] = colour;

    }

    public void set_pixel(int x, int y, int colour) {
        if (x < bounds_top_x || y < bounds_top_y || x >= bounds_bottom_x || y >= bounds_bottom_y) {
            return;
        } else {
            pixels[x + y * width2] = colour;
            return;
        }
    }

    public void fade2black() {
        int k = width2 * height2;
        for (int j = 0; j < k; j++) {
            int i = pixels[j] & 0xffffff;
            pixels[j] = (i >>> 1 & 0x7f7f7f) + (i >>> 2 & 0x3f3f3f) + (i >>> 3 & 0x1f1f1f) + (i >>> 4 & 0xf0f0f);
        }

    }

    public void draw_line_alpha(int i, int j, int x, int y, int width, int height) {
        for (int k1 = x; k1 < x + width; k1++) {
            for (int l1 = y; l1 < y + height; l1++) {
                int i2 = 0;
                int j2 = 0;
                int k2 = 0;
                int l2 = 0;
                for (int i3 = k1 - i; i3 <= k1 + i; i3++)
                    if (i3 >= 0 && i3 < width2) {
                        for (int j3 = l1 - j; j3 <= l1 + j; j3++)
                            if (j3 >= 0 && j3 < height2) {
                                int k3 = pixels[i3 + width2 * j3];
                                i2 += k3 >> 16 & 0xff;
                                j2 += k3 >> 8 & 0xff;
                                k2 += k3 & 0xff;
                                l2++;
                            }

                    }

                pixels[k1 + width2 * l1] = (i2 / l2 << 16) + (j2 / l2 << 8) + k2 / l2;
            }

        }

    }

    public static int rgb2long(int red, int green, int blue) {
        return (red << 16) + (green << 8) + blue;
    }

    public void clear() {
        for (int i = 0; i < surface_set_pixels.length; i++) {
            surface_set_pixels[i] = null;
            image_width[i] = 0;
            image_height[i] = 0;
            aByteArrayArray322[i] = null;
            anIntArrayArray323[i] = null;
        }

    }

    public void load_sprite(int i, byte abyte0[], byte abyte1[], int frame_count) {
        int off = Utility.getUnsignedShort(abyte0, 0);
        int full_width = Utility.getUnsignedShort(abyte1, off);
        off += 2;
        int full_height = Utility.getUnsignedShort(abyte1, off);
        off += 2;
        int j1 = abyte1[off++] & 0xff;
        int ai[] = new int[j1];
        ai[0] = 0xff00ff;
        for (int k1 = 0; k1 < j1 - 1; k1++) {
            ai[k1 + 1] = ((abyte1[off] & 0xff) << 16) + ((abyte1[off + 1] & 0xff) << 8) + (abyte1[off + 2] & 0xff);
            off += 3;
        }

        int l1 = 2;
        for (int i2 = i; i2 < i + frame_count; i2++) {
            offset_x[i2] = abyte1[off++] & 0xff;
            offset_y[i2] = abyte1[off++] & 0xff;
            image_width[i2] = Utility.getUnsignedShort(abyte1, off);
            off += 2;
            image_height[i2] = Utility.getUnsignedShort(abyte1, off);
            off += 2;
            int j2 = abyte1[off++] & 0xff;
            int k2 = image_width[i2] * image_height[i2];
            aByteArrayArray322[i2] = new byte[k2];
            anIntArrayArray323[i2] = ai;
            image_full_width[i2] = full_width;
            image_full_height[i2] = full_height;
            surface_set_pixels[i2] = null;
            image_translate[i2] = false;
            if (offset_x[i2] != 0 || offset_y[i2] != 0)
                image_translate[i2] = true;
            if (j2 == 0) {
                for (int l2 = 0; l2 < k2; l2++) {
                    aByteArrayArray322[i2][l2] = abyte0[l1++];
                    if (aByteArrayArray322[i2][l2] == 0)
                        image_translate[i2] = true;
                }

            } else if (j2 == 1) {
                for (int i3 = 0; i3 < image_width[i2]; i3++) {
                    for (int j3 = 0; j3 < image_height[i2]; j3++) {
                        aByteArrayArray322[i2][i3 + j3 * image_width[i2]] = abyte0[l1++];
                        if (aByteArrayArray322[i2][i3 + j3 * image_width[i2]] == 0)
                            image_translate[i2] = true;
                    }

                }

            }
        }

    }

    public void read_sleep_word(int i, byte abyte0[]) {
        int ai[] = surface_set_pixels[i] = new int[10200];
        image_width[i] = 255;
        image_height[i] = 40;
        offset_x[i] = 0;
        offset_y[i] = 0;
        image_full_width[i] = 255;
        image_full_height[i] = 40;
        image_translate[i] = false;
        int j = 0;
        int k = 1;
        int l;
        for (l = 0; l < 255; ) {
            int i1 = abyte0[k++] & 0xff;
            for (int k1 = 0; k1 < i1; k1++)
                ai[l++] = j;

            j = 0xffffff - j;
        }

        for (int j1 = 1; j1 < 40; j1++) {
            for (int l1 = 0; l1 < 255; ) {
                int i2 = abyte0[k++] & 0xff;
                for (int j2 = 0; j2 < i2; j2++) {
                    ai[l] = ai[l - 255];
                    l++;
                    l1++;
                }

                if (l1 < 255) {
                    ai[l] = 0xffffff - ai[l - 255];
                    l++;
                    l1++;
                }
            }

        }

    }

    public void draw_world(int i) {
        int j = image_width[i] * image_height[i];
        int ai[] = surface_set_pixels[i];
        int ai1[] = new int[32768];
        for (int k = 0; k < j; k++) {
            int l = ai[k];
            ai1[((l & 0xf80000) >> 9) + ((l & 0xf800) >> 6) + ((l & 0xf8) >> 3)]++;
        }

        int ai2[] = new int[256];
        ai2[0] = 0xff00ff;
        int ai3[] = new int[256];
        for (int i1 = 0; i1 < 32768; i1++) {
            int j1 = ai1[i1];
            if (j1 > ai3[255]) {
                for (int k1 = 1; k1 < 256; k1++) {
                    if (j1 <= ai3[k1])
                        continue;
                    for (int i2 = 255; i2 > k1; i2--) {
                        ai2[i2] = ai2[i2 - 1];
                        ai3[i2] = ai3[i2 - 1];
                    }

                    ai2[k1] = ((i1 & 0x7c00) << 9) + ((i1 & 0x3e0) << 6) + ((i1 & 0x1f) << 3) + 0x40404;
                    ai3[k1] = j1;
                    break;
                }

            }
            ai1[i1] = -1;
        }

        byte abyte0[] = new byte[j];
        for (int l1 = 0; l1 < j; l1++) {
            int j2 = ai[l1];
            int k2 = ((j2 & 0xf80000) >> 9) + ((j2 & 0xf800) >> 6) + ((j2 & 0xf8) >> 3);
            int l2 = ai1[k2];
            if (l2 == -1) {
                int i3 = 0x3b9ac9ff;
                int j3 = j2 >> 16 & 0xff;
                int k3 = j2 >> 8 & 0xff;
                int l3 = j2 & 0xff;
                for (int i4 = 0; i4 < 256; i4++) {
                    int j4 = ai2[i4];
                    int k4 = j4 >> 16 & 0xff;
                    int l4 = j4 >> 8 & 0xff;
                    int i5 = j4 & 0xff;
                    int j5 = (j3 - k4) * (j3 - k4) + (k3 - l4) * (k3 - l4) + (l3 - i5) * (l3 - i5);
                    if (j5 < i3) {
                        i3 = j5;
                        l2 = i4;
                    }
                }

                ai1[k2] = l2;
            }
            abyte0[l1] = (byte) l2;
        }

        aByteArrayArray322[i] = abyte0;
        anIntArrayArray323[i] = ai2;
        surface_set_pixels[i] = null;
    }

    public void method227(int i) {
        if (aByteArrayArray322[i] == null)
            return;
        int j = image_width[i] * image_height[i];
        byte abyte0[] = aByteArrayArray322[i];
        int ai[] = anIntArrayArray323[i];
        int ai1[] = new int[j];
        for (int k = 0; k < j; k++) {
            int l = ai[abyte0[k] & 0xff];
            if (l == 0)
                l = 1;
            else if (l == 0xff00ff)
                l = 0;
            ai1[k] = l;
        }

        surface_set_pixels[i] = ai1;
        aByteArrayArray322[i] = null;
        anIntArrayArray323[i] = null;
    }

    public void method228(int i, int j, int k, int l, int i1) {
        image_width[i] = l;
        image_height[i] = i1;
        image_translate[i] = false;
        offset_x[i] = 0;
        offset_y[i] = 0;
        image_full_width[i] = l;
        image_full_height[i] = i1;
        int j1 = l * i1;
        int k1 = 0;
        surface_set_pixels[i] = new int[j1];
        for (int l1 = j; l1 < j + l; l1++) {
            for (int i2 = k; i2 < k + i1; i2++)
                surface_set_pixels[i][k1++] = pixels[l1 + i2 * width2];

        }

    }

    public void render(int i, int x, int y, int w, int h) {
        image_width[i] = w;
        image_height[i] = h;
        image_translate[i] = false;
        offset_x[i] = 0;
        offset_y[i] = 0;
        image_full_width[i] = w;
        image_full_height[i] = h;
        int area = w * h;
        int d = 0;
        surface_set_pixels[i] = new int[area];
        for (int yy = y; yy < y + h; yy++) {
            for (int xx = x; xx < x + w; xx++)
                surface_set_pixels[i][d++] = pixels[xx + yy * width2];

        }

    }

    public void draw_picture(int x, int y, int id) {
        if (image_translate[id]) {
            x += offset_x[id];
            y += offset_y[id];
        }
        int r_y = x + y * width2;
        int r_x = 0;
        int height = image_height[id];
        int width = image_width[id];
        int w2 = width2 - width;
        int h2 = 0;
        if (y < bounds_top_y) {
            int j2 = bounds_top_y - y;
            height -= j2;
            y = bounds_top_y;
            r_x += j2 * width;
            r_y += j2 * width2;
        }
        if (y + height >= bounds_bottom_y)
            height -= ((y + height) - bounds_bottom_y) + 1;
        if (x < bounds_top_x) {
            int k2 = bounds_top_x - x;
            width -= k2;
            x = bounds_top_x;
            r_x += k2;
            r_y += k2;
            h2 += k2;
            w2 += k2;
        }
        if (x + width >= bounds_bottom_x) {
            int l2 = ((x + width) - bounds_bottom_x) + 1;
            width -= l2;
            h2 += l2;
            w2 += l2;
        }
        if (width <= 0 || height <= 0)
            return;
        byte inc = 1;
        if (interlace) {
            inc = 2;
            w2 += width2;
            h2 += image_width[id];
            if ((y & 1) != 0) {
                r_y += width2;
                height--;
            }
        }
        if (surface_set_pixels[id] == null) {
            draw_picture_dunno(pixels, aByteArrayArray322[id], anIntArrayArray323[id], r_x, r_y, width, height, w2, h2, inc);
            return;
        } else {
            draw_picture_dunno_either(pixels, surface_set_pixels[id], 0, r_x, r_y, width, height, w2, h2, inc);
            return;
        }
    }

    public void sprite_clipping(int i, int j, int k, int l, int i1) {
        try {
            int j1 = image_width[i1];
            int k1 = image_height[i1];
            int l1 = 0;
            int i2 = 0;
            int j2 = (j1 << 16) / k;
            int k2 = (k1 << 16) / l;
            if (image_translate[i1]) {
                int l2 = image_full_width[i1];
                int j3 = image_full_height[i1];
                j2 = (l2 << 16) / k;
                k2 = (j3 << 16) / l;
                i += ((offset_x[i1] * k + l2) - 1) / l2;
                j += ((offset_y[i1] * l + j3) - 1) / j3;
                if ((offset_x[i1] * k) % l2 != 0)
                    l1 = (l2 - (offset_x[i1] * k) % l2 << 16) / k;
                if ((offset_y[i1] * l) % j3 != 0)
                    i2 = (j3 - (offset_y[i1] * l) % j3 << 16) / l;
                k = (k * (image_width[i1] - (l1 >> 16))) / l2;
                l = (l * (image_height[i1] - (i2 >> 16))) / j3;
            }
            int i3 = i + j * width2;
            int k3 = width2 - k;
            if (j < bounds_top_y) {
                int l3 = bounds_top_y - j;
                l -= l3;
                j = 0;
                i3 += l3 * width2;
                i2 += k2 * l3;
            }
            if (j + l >= bounds_bottom_y)
                l -= ((j + l) - bounds_bottom_y) + 1;
            if (i < bounds_top_x) {
                int i4 = bounds_top_x - i;
                k -= i4;
                i = 0;
                i3 += i4;
                l1 += j2 * i4;
                k3 += i4;
            }
            if (i + k >= bounds_bottom_x) {
                int j4 = ((i + k) - bounds_bottom_x) + 1;
                k -= j4;
                k3 += j4;
            }
            byte byte0 = 1;
            if (interlace) {
                byte0 = 2;
                k3 += width2;
                k2 += k2;
                if ((j & 1) != 0) {
                    i3 += width2;
                    l--;
                }
            }
            plot_scale(pixels, surface_set_pixels[i1], 0, l1, i2, i3, k3, k, l, j2, k2, j1, byte0);
            return;
        } catch (Exception _ex) {
            System.out.println("error in sprite clipping routine");
        }
    }

    public void method232(int x, int y, int idx, int some) {
        if (image_translate[idx]) {
            x += offset_x[idx];
            y += offset_y[idx];
        }
        int i1 = x + y * width2;
        int j1 = 0;
        int k1 = image_height[idx];
        int l1 = image_width[idx];
        int i2 = width2 - l1;
        int j2 = 0;
        if (y < bounds_top_y) {
            int k2 = bounds_top_y - y;
            k1 -= k2;
            y = bounds_top_y;
            j1 += k2 * l1;
            i1 += k2 * width2;
        }
        if (y + k1 >= bounds_bottom_y)
            k1 -= ((y + k1) - bounds_bottom_y) + 1;
        if (x < bounds_top_x) {
            int l2 = bounds_top_x - x;
            l1 -= l2;
            x = bounds_top_x;
            j1 += l2;
            i1 += l2;
            j2 += l2;
            i2 += l2;
        }
        if (x + l1 >= bounds_bottom_x) {
            int i3 = ((x + l1) - bounds_bottom_x) + 1;
            l1 -= i3;
            j2 += i3;
            i2 += i3;
        }
        if (l1 <= 0 || k1 <= 0)
            return;
        byte byte0 = 1;
        if (interlace) {
            byte0 = 2;
            i2 += width2;
            j2 += image_width[idx];
            if ((y & 1) != 0) {
                i1 += width2;
                k1--;
            }
        }
        if (surface_set_pixels[idx] == null) {
            method239(pixels, aByteArrayArray322[idx], anIntArrayArray323[idx], j1, i1, l1, k1, i2, j2, byte0, some);
            return;
        } else {
            method238(pixels, surface_set_pixels[idx], 0, j1, i1, l1, k1, i2, j2, byte0, some);
            return;
        }
    }

    public void sprite_aboveheadballoonthing(int i, int j, int k, int l, int i1, int j1) {
        try {
            int k1 = image_width[i1];
            int l1 = image_height[i1];
            int i2 = 0;
            int j2 = 0;
            int k2 = (k1 << 16) / k;
            int l2 = (l1 << 16) / l;
            if (image_translate[i1]) {
                int i3 = image_full_width[i1];
                int k3 = image_full_height[i1];
                k2 = (i3 << 16) / k;
                l2 = (k3 << 16) / l;
                i += ((offset_x[i1] * k + i3) - 1) / i3;
                j += ((offset_y[i1] * l + k3) - 1) / k3;
                if ((offset_x[i1] * k) % i3 != 0)
                    i2 = (i3 - (offset_x[i1] * k) % i3 << 16) / k;
                if ((offset_y[i1] * l) % k3 != 0)
                    j2 = (k3 - (offset_y[i1] * l) % k3 << 16) / l;
                k = (k * (image_width[i1] - (i2 >> 16))) / i3;
                l = (l * (image_height[i1] - (j2 >> 16))) / k3;
            }
            int j3 = i + j * width2;
            int l3 = width2 - k;
            if (j < bounds_top_y) {
                int i4 = bounds_top_y - j;
                l -= i4;
                j = 0;
                j3 += i4 * width2;
                j2 += l2 * i4;
            }
            if (j + l >= bounds_bottom_y)
                l -= ((j + l) - bounds_bottom_y) + 1;
            if (i < bounds_top_x) {
                int j4 = bounds_top_x - i;
                k -= j4;
                i = 0;
                j3 += j4;
                i2 += k2 * j4;
                l3 += j4;
            }
            if (i + k >= bounds_bottom_x) {
                int k4 = ((i + k) - bounds_bottom_x) + 1;
                k -= k4;
                l3 += k4;
            }
            byte byte0 = 1;
            if (interlace) {
                byte0 = 2;
                l3 += width2;
                l2 += l2;
                if ((j & 1) != 0) {
                    j3 += width2;
                    l--;
                }
            }
            tran_scale(pixels, surface_set_pixels[i1], 0, i2, j2, j3, l3, k, l, k2, l2, k1, byte0, j1);
            return;
        } catch (Exception _ex) {
            System.out.println("error in sprite clipping routine");
        }
    }

    public void sprite_clipping(int i, int j, int k, int l, int i1, int j1) {
        try {
            int k1 = image_width[i1];
            int l1 = image_height[i1];
            int i2 = 0;
            int j2 = 0;
            int k2 = (k1 << 16) / k;
            int l2 = (l1 << 16) / l;
            if (image_translate[i1]) {
                int i3 = image_full_width[i1];
                int k3 = image_full_height[i1];
                k2 = (i3 << 16) / k;
                l2 = (k3 << 16) / l;
                i += ((offset_x[i1] * k + i3) - 1) / i3;
                j += ((offset_y[i1] * l + k3) - 1) / k3;
                if ((offset_x[i1] * k) % i3 != 0)
                    i2 = (i3 - (offset_x[i1] * k) % i3 << 16) / k;
                if ((offset_y[i1] * l) % k3 != 0)
                    j2 = (k3 - (offset_y[i1] * l) % k3 << 16) / l;
                k = (k * (image_width[i1] - (i2 >> 16))) / i3;
                l = (l * (image_height[i1] - (j2 >> 16))) / k3;
            }
            int j3 = i + j * width2;
            int l3 = width2 - k;
            if (j < bounds_top_y) {
                int i4 = bounds_top_y - j;
                l -= i4;
                j = 0;
                j3 += i4 * width2;
                j2 += l2 * i4;
            }
            if (j + l >= bounds_bottom_y)
                l -= ((j + l) - bounds_bottom_y) + 1;
            if (i < bounds_top_x) {
                int j4 = bounds_top_x - i;
                k -= j4;
                i = 0;
                j3 += j4;
                i2 += k2 * j4;
                l3 += j4;
            }
            if (i + k >= bounds_bottom_x) {
                int k4 = ((i + k) - bounds_bottom_x) + 1;
                k -= k4;
                l3 += k4;
            }
            byte byte0 = 1;
            if (interlace) {
                byte0 = 2;
                l3 += width2;
                l2 += l2;
                if ((j & 1) != 0) {
                    j3 += width2;
                    l--;
                }
            }
            plot_scale(pixels, surface_set_pixels[i1], 0, i2, j2, j3, l3, k, l, k2, l2, k1, byte0, j1);
            return;
        } catch (Exception _ex) {
            System.out.println("error in sprite clipping routine");
        }
    }

    private void draw_picture_dunno_either(int ai[], int ai1[], int i, int j, int k, int l, int i1,
                                           int j1, int k1, int l1) {
        int i2 = -(l >> 2);
        l = -(l & 3);
        for (int j2 = -i1; j2 < 0; j2 += l1) {
            for (int k2 = i2; k2 < 0; k2++) {
                i = ai1[j++];
                if (i != 0)
                    ai[k++] = i;
                else
                    k++;
                i = ai1[j++];
                if (i != 0)
                    ai[k++] = i;
                else
                    k++;
                i = ai1[j++];
                if (i != 0)
                    ai[k++] = i;
                else
                    k++;
                i = ai1[j++];
                if (i != 0)
                    ai[k++] = i;
                else
                    k++;
            }

            for (int l2 = l; l2 < 0; l2++) {
                i = ai1[j++];
                if (i != 0)
                    ai[k++] = i;
                else
                    k++;
            }

            k += j1;
            j += k1;
        }

    }

    private void draw_picture_dunno(int pix[], byte u_pix[], int idunfknno[], int i, int j, int k, int l,
                                    int i1, int j1, int row_inc) {
        int l1 = -(k >> 2);
        k = -(k & 3);
        for (int i2 = -l; i2 < 0; i2 += row_inc) {
            for (int j2 = l1; j2 < 0; j2++) {
                byte byte0 = u_pix[i++];
                if (byte0 != 0)
                    pix[j++] = idunfknno[byte0 & 0xff];
                else
                    j++;
                byte0 = u_pix[i++];
                if (byte0 != 0)
                    pix[j++] = idunfknno[byte0 & 0xff];
                else
                    j++;
                byte0 = u_pix[i++];
                if (byte0 != 0)
                    pix[j++] = idunfknno[byte0 & 0xff];
                else
                    j++;
                byte0 = u_pix[i++];
                if (byte0 != 0)
                    pix[j++] = idunfknno[byte0 & 0xff];
                else
                    j++;
            }

            for (int k2 = k; k2 < 0; k2++) {
                byte byte1 = u_pix[i++];
                if (byte1 != 0)
                    pix[j++] = idunfknno[byte1 & 0xff];
                else
                    j++;
            }

            j += i1;
            i += j1;
        }

    }

    private void plot_scale(int ai[], int ai1[], int i, int j, int k, int l, int i1,
                            int j1, int k1, int l1, int i2, int j2, int k2) {
        try {
            int l2 = j;
            for (int i3 = -k1; i3 < 0; i3 += k2) {
                int j3 = (k >> 16) * j2;
                for (int k3 = -j1; k3 < 0; k3++) {
                    i = ai1[(j >> 16) + j3];
                    if (i != 0)
                        ai[l++] = i;
                    else
                        l++;
                    j += l1;
                }

                k += i2;
                j = l2;
                l += i1;
            }

            return;
        } catch (Exception _ex) {
            System.out.println("error in plot_scale");
        }
    }

    private void method238(int ai[], int ai1[], int i, int j, int k, int l, int i1,
                           int j1, int k1, int l1, int i2) {
        int j2 = 256 - i2;
        for (int k2 = -i1; k2 < 0; k2 += l1) {
            for (int l2 = -l; l2 < 0; l2++) {
                i = ai1[j++];
                if (i != 0) {
                    int i3 = ai[k];
                    ai[k++] = ((i & 0xff00ff) * i2 + (i3 & 0xff00ff) * j2 & 0xff00ff00) + ((i & 0xff00) * i2 + (i3 & 0xff00) * j2 & 0xff0000) >> 8;
                } else {
                    k++;
                }
            }

            k += j1;
            j += k1;
        }

    }

    private void method239(int ai[], byte abyte0[], int ai1[], int i, int j, int k, int l,
                           int i1, int j1, int k1, int l1) {
        int i2 = 256 - l1;
        for (int j2 = -l; j2 < 0; j2 += k1) {
            for (int k2 = -k; k2 < 0; k2++) {
                int l2 = abyte0[i++];
                if (l2 != 0) {
                    l2 = ai1[l2 & 0xff];
                    int i3 = ai[j];
                    ai[j++] = ((l2 & 0xff00ff) * l1 + (i3 & 0xff00ff) * i2 & 0xff00ff00) + ((l2 & 0xff00) * l1 + (i3 & 0xff00) * i2 & 0xff0000) >> 8;
                } else {
                    j++;
                }
            }

            j += i1;
            i += j1;
        }

    }

    private void tran_scale(int ai[], int ai1[], int i, int j, int k, int l, int i1,
                            int j1, int k1, int l1, int i2, int j2, int k2, int l2) {
        int i3 = 256 - l2;
        try {
            int j3 = j;
            for (int k3 = -k1; k3 < 0; k3 += k2) {
                int l3 = (k >> 16) * j2;
                for (int i4 = -j1; i4 < 0; i4++) {
                    i = ai1[(j >> 16) + l3];
                    if (i != 0) {
                        int j4 = ai[l];
                        ai[l++] = ((i & 0xff00ff) * l2 + (j4 & 0xff00ff) * i3 & 0xff00ff00) + ((i & 0xff00) * l2 + (j4 & 0xff00) * i3 & 0xff0000) >> 8;
                    } else {
                        l++;
                    }
                    j += l1;
                }

                k += i2;
                j = j3;
                l += i1;
            }

            return;
        } catch (Exception _ex) {
            System.out.println("error in tran_scale");
        }
    }

    private void plot_scale(int ai[], int ai1[], int i, int j, int k, int l, int i1,
                            int j1, int k1, int l1, int i2, int j2, int k2, int l2) {
        int i3 = l2 >> 16 & 0xff;
        int j3 = l2 >> 8 & 0xff;
        int k3 = l2 & 0xff;
        try {
            int l3 = j;
            for (int i4 = -k1; i4 < 0; i4 += k2) {
                int j4 = (k >> 16) * j2;
                for (int k4 = -j1; k4 < 0; k4++) {
                    i = ai1[(j >> 16) + j4];
                    if (i != 0) {
                        int l4 = i >> 16 & 0xff;
                        int i5 = i >> 8 & 0xff;
                        int j5 = i & 0xff;
                        if (l4 == i5 && i5 == j5)
                            ai[l++] = ((l4 * i3 >> 8) << 16) + ((i5 * j3 >> 8) << 8) + (j5 * k3 >> 8);
                        else
                            ai[l++] = i;
                    } else {
                        l++;
                    }
                    j += l1;
                }

                k += i2;
                j = l3;
                l += i1;
            }

            return;
        } catch (Exception _ex) {
            System.out.println("error in plot_scale");
        }
    }

    public void draw_minimap_sprite(int x, int y, int sprite, int rotation, int scale) {// "scale" is not actually scaling when it comes to the landscape
        int j1 = width2;
        int k1 = height2;
        if (landscape_colours == null) {
            landscape_colours = new int[512];
            for (int l1 = 0; l1 < 256; l1++) {
                landscape_colours[l1] = (int) (Math.sin((double) l1 * 0.02454369D) * 32768D);
                landscape_colours[l1 + 256] = (int) (Math.cos((double) l1 * 0.02454369D) * 32768D);
            }

        }
        int i2 = -image_full_width[sprite] / 2;
        int j2 = -image_full_height[sprite] / 2;
        if (image_translate[sprite]) {
            i2 += offset_x[sprite];
            j2 += offset_y[sprite];
        }
        int k2 = i2 + image_width[sprite];
        int l2 = j2 + image_height[sprite];
        int i3 = k2;
        int j3 = j2;
        int k3 = i2;
        int l3 = l2;
        rotation &= 0xff;
        int i4 = landscape_colours[rotation] * scale;
        int j4 = landscape_colours[rotation + 256] * scale;
        int k4 = x + (j2 * i4 + i2 * j4 >> 22);
        int l4 = y + (j2 * j4 - i2 * i4 >> 22);
        int i5 = x + (j3 * i4 + i3 * j4 >> 22);
        int j5 = y + (j3 * j4 - i3 * i4 >> 22);
        int k5 = x + (l2 * i4 + k2 * j4 >> 22);
        int l5 = y + (l2 * j4 - k2 * i4 >> 22);
        int i6 = x + (l3 * i4 + k3 * j4 >> 22);
        int j6 = y + (l3 * j4 - k3 * i4 >> 22);
        if (scale == 192 && (rotation & 0x3f) == (anInt348 & 0x3f))
            anInt346++;
        else if (scale == 128)
            anInt348 = rotation;
        else
            anInt347++;
        int k6 = l4;
        int l6 = l4;
        if (j5 < k6)
            k6 = j5;
        else if (j5 > l6)
            l6 = j5;
        if (l5 < k6)
            k6 = l5;
        else if (l5 > l6)
            l6 = l5;
        if (j6 < k6)
            k6 = j6;
        else if (j6 > l6)
            l6 = j6;
        if (k6 < bounds_top_y)
            k6 = bounds_top_y;
        if (l6 > bounds_bottom_y)
            l6 = bounds_bottom_y;
        if (anIntArray340 == null || anIntArray340.length != k1 + 1) {
            anIntArray340 = new int[k1 + 1];
            anIntArray341 = new int[k1 + 1];
            anIntArray342 = new int[k1 + 1];
            anIntArray343 = new int[k1 + 1];
            anIntArray344 = new int[k1 + 1];
            anIntArray345 = new int[k1 + 1];
        }
        for (int i7 = k6; i7 <= l6; i7++) {
            anIntArray340[i7] = 0x5f5e0ff;
            anIntArray341[i7] = 0xfa0a1f01;
        }

        int i8 = 0;
        int k8 = 0;
        int i9 = 0;
        int j9 = image_width[sprite];
        int k9 = image_height[sprite];
        i2 = 0;
        j2 = 0;
        i3 = j9 - 1;
        j3 = 0;
        k2 = j9 - 1;
        l2 = k9 - 1;
        k3 = 0;
        l3 = k9 - 1;
        if (j6 != l4) {
            i8 = (i6 - k4 << 8) / (j6 - l4);
            i9 = (l3 - j2 << 8) / (j6 - l4);
        }
        int j7;
        int k7;
        int l7;
        int l8;
        if (l4 > j6) {
            l7 = i6 << 8;
            l8 = l3 << 8;
            j7 = j6;
            k7 = l4;
        } else {
            l7 = k4 << 8;
            l8 = j2 << 8;
            j7 = l4;
            k7 = j6;
        }
        if (j7 < 0) {
            l7 -= i8 * j7;
            l8 -= i9 * j7;
            j7 = 0;
        }
        if (k7 > k1 - 1)
            k7 = k1 - 1;
        for (int l9 = j7; l9 <= k7; l9++) {
            anIntArray340[l9] = anIntArray341[l9] = l7;
            l7 += i8;
            anIntArray342[l9] = anIntArray343[l9] = 0;
            anIntArray344[l9] = anIntArray345[l9] = l8;
            l8 += i9;
        }

        if (j5 != l4) {
            i8 = (i5 - k4 << 8) / (j5 - l4);
            k8 = (i3 - i2 << 8) / (j5 - l4);
        }
        int j8;
        if (l4 > j5) {
            l7 = i5 << 8;
            j8 = i3 << 8;
            j7 = j5;
            k7 = l4;
        } else {
            l7 = k4 << 8;
            j8 = i2 << 8;
            j7 = l4;
            k7 = j5;
        }
        if (j7 < 0) {
            l7 -= i8 * j7;
            j8 -= k8 * j7;
            j7 = 0;
        }
        if (k7 > k1 - 1)
            k7 = k1 - 1;
        for (int i10 = j7; i10 <= k7; i10++) {
            if (l7 < anIntArray340[i10]) {
                anIntArray340[i10] = l7;
                anIntArray342[i10] = j8;
                anIntArray344[i10] = 0;
            }
            if (l7 > anIntArray341[i10]) {
                anIntArray341[i10] = l7;
                anIntArray343[i10] = j8;
                anIntArray345[i10] = 0;
            }
            l7 += i8;
            j8 += k8;
        }

        if (l5 != j5) {
            i8 = (k5 - i5 << 8) / (l5 - j5);
            i9 = (l2 - j3 << 8) / (l5 - j5);
        }
        if (j5 > l5) {
            l7 = k5 << 8;
            j8 = k2 << 8;
            l8 = l2 << 8;
            j7 = l5;
            k7 = j5;
        } else {
            l7 = i5 << 8;
            j8 = i3 << 8;
            l8 = j3 << 8;
            j7 = j5;
            k7 = l5;
        }
        if (j7 < 0) {
            l7 -= i8 * j7;
            l8 -= i9 * j7;
            j7 = 0;
        }
        if (k7 > k1 - 1)
            k7 = k1 - 1;
        for (int j10 = j7; j10 <= k7; j10++) {
            if (l7 < anIntArray340[j10]) {
                anIntArray340[j10] = l7;
                anIntArray342[j10] = j8;
                anIntArray344[j10] = l8;
            }
            if (l7 > anIntArray341[j10]) {
                anIntArray341[j10] = l7;
                anIntArray343[j10] = j8;
                anIntArray345[j10] = l8;
            }
            l7 += i8;
            l8 += i9;
        }

        if (j6 != l5) {
            i8 = (i6 - k5 << 8) / (j6 - l5);
            k8 = (k3 - k2 << 8) / (j6 - l5);
        }
        if (l5 > j6) {
            l7 = i6 << 8;
            j8 = k3 << 8;
            l8 = l3 << 8;
            j7 = j6;
            k7 = l5;
        } else {
            l7 = k5 << 8;
            j8 = k2 << 8;
            l8 = l2 << 8;
            j7 = l5;
            k7 = j6;
        }
        if (j7 < 0) {
            l7 -= i8 * j7;
            j8 -= k8 * j7;
            j7 = 0;
        }
        if (k7 > k1 - 1)
            k7 = k1 - 1;
        for (int k10 = j7; k10 <= k7; k10++) {
            if (l7 < anIntArray340[k10]) {
                anIntArray340[k10] = l7;
                anIntArray342[k10] = j8;
                anIntArray344[k10] = l8;
            }
            if (l7 > anIntArray341[k10]) {
                anIntArray341[k10] = l7;
                anIntArray343[k10] = j8;
                anIntArray345[k10] = l8;
            }
            l7 += i8;
            j8 += k8;
        }

        int l10 = k6 * j1;
        int ai[] = surface_set_pixels[sprite];
        for (int i11 = k6; i11 < l6; i11++) {
            int j11 = anIntArray340[i11] >> 8;
            int k11 = anIntArray341[i11] >> 8;
            if (k11 - j11 <= 0) {
                l10 += j1;
            } else {
                int l11 = anIntArray342[i11] << 9;
                int i12 = ((anIntArray343[i11] << 9) - l11) / (k11 - j11);
                int j12 = anIntArray344[i11] << 9;
                int k12 = ((anIntArray345[i11] << 9) - j12) / (k11 - j11);
                if (j11 < bounds_top_x) {
                    l11 += (bounds_top_x - j11) * i12;
                    j12 += (bounds_top_x - j11) * k12;
                    j11 = bounds_top_x;
                }
                if (k11 > bounds_bottom_x)
                    k11 = bounds_bottom_x;
                if (!interlace || (i11 & 1) == 0)
                    if (!image_translate[sprite])
                        draw_minimap_something(pixels, ai, 0, l10 + j11, l11, j12, i12, k12, j11 - k11, j9);
                    else
                        draw_minimap_something_translate(pixels, ai, 0, l10 + j11, l11, j12, i12, k12, j11 - k11, j9);
                l10 += j1;
            }
        }

    }

    private void draw_minimap_something(int ai[], int ai1[], int i, int j, int k, int l, int i1,
                                        int j1, int k1, int l1) {
        for (i = k1; i < 0; i++) {
            pixels[j++] = ai1[(k >> 17) + (l >> 17) * l1];
            k += i1;
            l += j1;
        }

    }

    private void draw_minimap_something_translate(int ai[], int ai1[], int i, int j, int k, int l, int i1,
                                                  int j1, int k1, int l1) {
        for (int i2 = k1; i2 < 0; i2++) {
            i = ai1[(k >> 17) + (l >> 17) * l1];
            if (i != 0)
                pixels[j++] = i;
            else
                j++;
            k += i1;
            l += j1;
        }

    }

    public void sprite_clipping(int i, int j, int k, int l, int i1, int j1, int k1) {
        sprite_clipping(i, j, k, l, i1);
    }

    public void sprite_clipping(int i, int j, int k, int l, int i1, int j1, int k1,
                                int l1, boolean flag) {
        try {
            if (j1 == 0)
                j1 = 0xffffff;
            if (k1 == 0)
                k1 = 0xffffff;
            int i2 = image_width[i1];
            int j2 = image_height[i1];
            int k2 = 0;
            int l2 = 0;
            int i3 = l1 << 16;
            int j3 = (i2 << 16) / k;
            int k3 = (j2 << 16) / l;
            int l3 = -(l1 << 16) / l;
            if (image_translate[i1]) {
                int i4 = image_full_width[i1];
                int k4 = image_full_height[i1];
                j3 = (i4 << 16) / k;
                k3 = (k4 << 16) / l;
                int j5 = offset_x[i1];
                int k5 = offset_y[i1];
                if (flag)
                    j5 = i4 - image_width[i1] - j5;
                i += ((j5 * k + i4) - 1) / i4;
                int l5 = ((k5 * l + k4) - 1) / k4;
                j += l5;
                i3 += l5 * l3;
                if ((j5 * k) % i4 != 0)
                    k2 = (i4 - (j5 * k) % i4 << 16) / k;
                if ((k5 * l) % k4 != 0)
                    l2 = (k4 - (k5 * l) % k4 << 16) / l;
                k = ((((image_width[i1] << 16) - k2) + j3) - 1) / j3;
                l = ((((image_height[i1] << 16) - l2) + k3) - 1) / k3;
            }
            int j4 = j * width2;
            i3 += i << 16;
            if (j < bounds_top_y) {
                int l4 = bounds_top_y - j;
                l -= l4;
                j = bounds_top_y;
                j4 += l4 * width2;
                l2 += k3 * l4;
                i3 += l3 * l4;
            }
            if (j + l >= bounds_bottom_y)
                l -= ((j + l) - bounds_bottom_y) + 1;
            int i5 = j4 / width2 & 1;
            if (!interlace)
                i5 = 2;
            if (k1 == 0xffffff) {
                if (surface_set_pixels[i1] != null)
                    if (!flag) {
                        transparent_sprite_plot(pixels, surface_set_pixels[i1], 0, k2, l2, j4, k, l, j3, k3, i2, j1, i3, l3, i5);
                        return;
                    } else {
                        transparent_sprite_plot(pixels, surface_set_pixels[i1], 0, (image_width[i1] << 16) - k2 - 1, l2, j4, k, l, -j3, k3, i2, j1, i3, l3, i5);
                        return;
                    }
                if (!flag) {
                    transparent_sprite_plot(pixels, aByteArrayArray322[i1], anIntArrayArray323[i1], 0, k2, l2, j4, k, l, j3, k3, i2, j1, i3, l3, i5);
                    return;
                } else {
                    transparent_sprite_plot(pixels, aByteArrayArray322[i1], anIntArrayArray323[i1], 0, (image_width[i1] << 16) - k2 - 1, l2, j4, k, l, -j3, k3, i2, j1, i3, l3, i5);
                    return;
                }
            }
            if (surface_set_pixels[i1] != null)
                if (!flag) {
                    transparent_sprite_plot(pixels, surface_set_pixels[i1], 0, k2, l2, j4, k, l, j3, k3, i2, j1, k1, i3, l3, i5);
                    return;
                } else {
                    transparent_sprite_plot(pixels, surface_set_pixels[i1], 0, (image_width[i1] << 16) - k2 - 1, l2, j4, k, l, -j3, k3, i2, j1, k1, i3, l3, i5);
                    return;
                }
            if (!flag) {
                transparent_sprite_plot(pixels, aByteArrayArray322[i1], anIntArrayArray323[i1], 0, k2, l2, j4, k, l, j3, k3, i2, j1, k1, i3, l3, i5);
                return;
            } else {
                transparent_sprite_plot(pixels, aByteArrayArray322[i1], anIntArrayArray323[i1], 0, (image_width[i1] << 16) - k2 - 1, l2, j4, k, l, -j3, k3, i2, j1, k1, i3, l3, i5);
                return;
            }
        } catch (Exception _ex) {
            System.out.println("error in sprite clipping routine");
        }
    }

    private void transparent_sprite_plot(int ai[], int ai1[], int i, int j, int k, int l, int i1,
                                         int j1, int k1, int l1, int i2, int j2, int k2, int l2,
                                         int i3) {
        int i4 = j2 >> 16 & 0xff;
        int j4 = j2 >> 8 & 0xff;
        int k4 = j2 & 0xff;
        try {
            int l4 = j;
            for (int i5 = -j1; i5 < 0; i5++) {
                int j5 = (k >> 16) * i2;
                int k5 = k2 >> 16;
                int l5 = i1;
                if (k5 < bounds_top_x) {
                    int i6 = bounds_top_x - k5;
                    l5 -= i6;
                    k5 = bounds_top_x;
                    j += k1 * i6;
                }
                if (k5 + l5 >= bounds_bottom_x) {
                    int j6 = (k5 + l5) - bounds_bottom_x;
                    l5 -= j6;
                }
                i3 = 1 - i3;
                if (i3 != 0) {
                    for (int k6 = k5; k6 < k5 + l5; k6++) {
                        i = ai1[(j >> 16) + j5];
                        if (i != 0) {
                            int j3 = i >> 16 & 0xff;
                            int k3 = i >> 8 & 0xff;
                            int l3 = i & 0xff;
                            if (j3 == k3 && k3 == l3)
                                ai[k6 + l] = ((j3 * i4 >> 8) << 16) + ((k3 * j4 >> 8) << 8) + (l3 * k4 >> 8);
                            else
                                ai[k6 + l] = i;
                        }
                        j += k1;
                    }

                }
                k += l1;
                j = l4;
                l += width2;
                k2 += l2;
            }

            return;
        } catch (Exception _ex) {
            System.out.println("error in transparent sprite plot routine");
        }
    }

    private void transparent_sprite_plot(int ai[], int ai1[], int i, int j, int k, int l, int i1,
                                         int j1, int k1, int l1, int i2, int j2, int k2, int l2,
                                         int i3, int j3) {
        int j4 = j2 >> 16 & 0xff;
        int k4 = j2 >> 8 & 0xff;
        int l4 = j2 & 0xff;
        int i5 = k2 >> 16 & 0xff;
        int j5 = k2 >> 8 & 0xff;
        int k5 = k2 & 0xff;
        try {
            int l5 = j;
            for (int i6 = -j1; i6 < 0; i6++) {
                int j6 = (k >> 16) * i2;
                int k6 = l2 >> 16;
                int l6 = i1;
                if (k6 < bounds_top_x) {
                    int i7 = bounds_top_x - k6;
                    l6 -= i7;
                    k6 = bounds_top_x;
                    j += k1 * i7;
                }
                if (k6 + l6 >= bounds_bottom_x) {
                    int j7 = (k6 + l6) - bounds_bottom_x;
                    l6 -= j7;
                }
                j3 = 1 - j3;
                if (j3 != 0) {
                    for (int k7 = k6; k7 < k6 + l6; k7++) {
                        i = ai1[(j >> 16) + j6];
                        if (i != 0) {
                            int k3 = i >> 16 & 0xff;
                            int l3 = i >> 8 & 0xff;
                            int i4 = i & 0xff;
                            if (k3 == l3 && l3 == i4)
                                ai[k7 + l] = ((k3 * j4 >> 8) << 16) + ((l3 * k4 >> 8) << 8) + (i4 * l4 >> 8);
                            else if (k3 == 255 && l3 == i4)
                                ai[k7 + l] = ((k3 * i5 >> 8) << 16) + ((l3 * j5 >> 8) << 8) + (i4 * k5 >> 8);
                            else
                                ai[k7 + l] = i;
                        }
                        j += k1;
                    }

                }
                k += l1;
                j = l5;
                l += width2;
                l2 += i3;
            }

            return;
        } catch (Exception _ex) {
            System.out.println("error in transparent sprite plot routine");
        }
    }

    private void transparent_sprite_plot(int ai[], byte abyte0[], int ai1[], int i, int j, int k, int l,
                                         int i1, int j1, int k1, int l1, int i2, int j2, int k2,
                                         int l2, int i3) {
        int i4 = j2 >> 16 & 0xff;
        int j4 = j2 >> 8 & 0xff;
        int k4 = j2 & 0xff;
        try {
            int l4 = j;
            for (int i5 = -j1; i5 < 0; i5++) {
                int j5 = (k >> 16) * i2;
                int k5 = k2 >> 16;
                int l5 = i1;
                if (k5 < bounds_top_x) {
                    int i6 = bounds_top_x - k5;
                    l5 -= i6;
                    k5 = bounds_top_x;
                    j += k1 * i6;
                }
                if (k5 + l5 >= bounds_bottom_x) {
                    int j6 = (k5 + l5) - bounds_bottom_x;
                    l5 -= j6;
                }
                i3 = 1 - i3;
                if (i3 != 0) {
                    for (int k6 = k5; k6 < k5 + l5; k6++) {
                        i = abyte0[(j >> 16) + j5] & 0xff;
                        if (i != 0) {
                            i = ai1[i];
                            int j3 = i >> 16 & 0xff;
                            int k3 = i >> 8 & 0xff;
                            int l3 = i & 0xff;
                            if (j3 == k3 && k3 == l3)
                                ai[k6 + l] = ((j3 * i4 >> 8) << 16) + ((k3 * j4 >> 8) << 8) + (l3 * k4 >> 8);
                            else
                                ai[k6 + l] = i;
                        }
                        j += k1;
                    }

                }
                k += l1;
                j = l4;
                l += width2;
                k2 += l2;
            }

            return;
        } catch (Exception _ex) {
            System.out.println("error in transparent sprite plot routine");
        }
    }

    private void transparent_sprite_plot(int ai[], byte abyte0[], int ai1[], int i, int j, int k, int l,
                                         int i1, int j1, int k1, int l1, int i2, int j2, int k2,
                                         int l2, int i3, int j3) {
        int j4 = j2 >> 16 & 0xff;
        int k4 = j2 >> 8 & 0xff;
        int l4 = j2 & 0xff;
        int i5 = k2 >> 16 & 0xff;
        int j5 = k2 >> 8 & 0xff;
        int k5 = k2 & 0xff;
        try {
            int l5 = j;
            for (int i6 = -j1; i6 < 0; i6++) {
                int j6 = (k >> 16) * i2;
                int k6 = l2 >> 16;
                int l6 = i1;
                if (k6 < bounds_top_x) {
                    int i7 = bounds_top_x - k6;
                    l6 -= i7;
                    k6 = bounds_top_x;
                    j += k1 * i7;
                }
                if (k6 + l6 >= bounds_bottom_x) {
                    int j7 = (k6 + l6) - bounds_bottom_x;
                    l6 -= j7;
                }
                j3 = 1 - j3;
                if (j3 != 0) {
                    for (int k7 = k6; k7 < k6 + l6; k7++) {
                        i = abyte0[(j >> 16) + j6] & 0xff;
                        if (i != 0) {
                            i = ai1[i];
                            int k3 = i >> 16 & 0xff;
                            int l3 = i >> 8 & 0xff;
                            int i4 = i & 0xff;
                            if (k3 == l3 && l3 == i4)
                                ai[k7 + l] = ((k3 * j4 >> 8) << 16) + ((l3 * k4 >> 8) << 8) + (i4 * l4 >> 8);
                            else if (k3 == 255 && l3 == i4)
                                ai[k7 + l] = ((k3 * i5 >> 8) << 16) + ((l3 * j5 >> 8) << 8) + (i4 * k5 >> 8);
                            else
                                ai[k7 + l] = i;
                        }
                        j += k1;
                    }

                }
                k += l1;
                j = l5;
                l += width2;
                l2 += i3;
            }

            return;
        } catch (Exception _ex) {
            System.out.println("error in transparent sprite plot routine");
        }
    }

    public static void createFont(String s, int i, GameApplet gameApplet_) {
        boolean flag = false;
        boolean flag1 = false;
        s = s.toLowerCase();
        if (s.startsWith("helvetica"))
            s = s.substring(9);
        if (s.startsWith("h"))
            s = s.substring(1);
        if (s.startsWith("f")) {
            s = s.substring(1);
            flag = true;
        }
        if (s.startsWith("d")) {
            s = s.substring(1);
            flag1 = true;
        }
        if (s.endsWith(".jf"))
            s = s.substring(0, s.length() - 3);
        int style = 0;// NORMAL
        if (s.endsWith("b")) {
            style = 1;// BOLD
            s = s.substring(0, s.length() - 1);
        }
        if (s.endsWith("p"))
            s = s.substring(0, s.length() - 1);
        int size = Integer.parseInt(s);
        Font font = new Font("Helvetica", style, size);
        FontMetrics fontmetrics = gameApplet_.getFontMetrics(font);
        String s1 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!\"\243$%^&*()-_=+[{]};:'@#~,<.>/?\\| ";
        anInt350 = 855;
        for (int l = 0; l < 95; l++)
            method252(font, fontmetrics, s1.charAt(l), l, gameApplet_, i, flag1);

        aByteArrayArray336[i] = new byte[anInt350];
        for (int i1 = 0; i1 < anInt350; i1++)
            aByteArrayArray336[i][i1] = aByteArray351[i1];

        if (style == 1 && aBooleanArray349[i]) {
            aBooleanArray349[i] = false;
            createFont("f" + size + "p", i, gameApplet_);
        }
        if (flag && !aBooleanArray349[i]) {
            aBooleanArray349[i] = false;
            createFont("d" + size + "p", i, gameApplet_);
        }
    }

    public static void method252(Font font, FontMetrics fontmetrics, char chr, int i, GameApplet gameApplet_, int j, boolean flag) {
        int k = fontmetrics.charWidth(chr);
        int l = k;
        if (flag)
            try {
                if (chr == '/')
                    flag = false;
                if (chr == 'f' || chr == 't' || chr == 'w' || chr == 'v' || chr == 'k' || chr == 'x' || chr == 'y' || chr == 'A' || chr == 'V' || chr == 'W')
                    k++;
            } catch (Exception _ex) {
            }
        int i1 = fontmetrics.getMaxAscent();
        int j1 = fontmetrics.getMaxAscent() + fontmetrics.getMaxDescent();
        int k1 = fontmetrics.getHeight();
        Image image = gameApplet_.createImage(k, j1);
        Graphics g = image.getGraphics();
        g.setColor(Color.black);
        g.fillRect(0, 0, k, j1);
        g.setColor(Color.white);
        g.setFont(font);
        g.drawString(String.valueOf(chr), 0, i1);
        if (flag)
            g.drawString(String.valueOf(chr), 1, i1);
        int ai[] = new int[k * j1];
        PixelGrabber pixelgrabber = new PixelGrabber(image, 0, 0, k, j1, ai, 0, k);
        try {
            pixelgrabber.grabPixels();
        } catch (InterruptedException _ex) {
            return;
        }
        image.flush();
        image = null;
        int l1 = 0;
        int i2 = 0;
        int j2 = k;
        int k2 = j1;
        label0:
        for (int l2 = 0; l2 < j1; l2++) {
            for (int i3 = 0; i3 < k; i3++) {
                int k3 = ai[i3 + l2 * k];
                if ((k3 & 0xffffff) == 0)
                    continue;
                i2 = l2;
                break label0;
            }

        }

        label1:
        for (int j3 = 0; j3 < k; j3++) {
            for (int l3 = 0; l3 < j1; l3++) {
                int j4 = ai[j3 + l3 * k];
                if ((j4 & 0xffffff) == 0)
                    continue;
                l1 = j3;
                break label1;
            }

        }

        label2:
        for (int i4 = j1 - 1; i4 >= 0; i4--) {
            for (int k4 = 0; k4 < k; k4++) {
                int i5 = ai[k4 + i4 * k];
                if ((i5 & 0xffffff) == 0)
                    continue;
                k2 = i4 + 1;
                break label2;
            }

        }

        label3:
        for (int l4 = k - 1; l4 >= 0; l4--) {
            for (int j5 = 0; j5 < j1; j5++) {
                int l5 = ai[l4 + j5 * k];
                if ((l5 & 0xffffff) == 0)
                    continue;
                j2 = l4 + 1;
                break label3;
            }

        }

        aByteArray351[i * 9] = (byte) (anInt350 / 16384);
        aByteArray351[i * 9 + 1] = (byte) (anInt350 / 128 & 0x7f);
        aByteArray351[i * 9 + 2] = (byte) (anInt350 & 0x7f);
        aByteArray351[i * 9 + 3] = (byte) (j2 - l1);
        aByteArray351[i * 9 + 4] = (byte) (k2 - i2);
        aByteArray351[i * 9 + 5] = (byte) l1;
        aByteArray351[i * 9 + 6] = (byte) (i1 - i2);
        aByteArray351[i * 9 + 7] = (byte) l;
        aByteArray351[i * 9 + 8] = (byte) k1;
        for (int k5 = i2; k5 < k2; k5++) {
            for (int i6 = l1; i6 < j2; i6++) {
                int j6 = ai[i6 + k5 * k] & 0xff;
                if (j6 > 30 && j6 < 230)
                    aBooleanArray349[j] = true;
                aByteArray351[anInt350++] = (byte) j6;
            }

        }

    }

    public void drawstring_right(String s, int i, int j, int k, int l) {
        drawstring(s, i - textWidth(s, k), j, k, l);
    }

    public void drawstring_center(String s, int i, int j, int k, int l) {
        drawstring(s, i - textWidth(s, k) / 2, j, k, l);
    }

    public void centrepara(String s, int i, int j, int k, int l, int i1) {
        try {
            int j1 = 0;
            byte abyte0[] = aByteArrayArray336[k];
            int k1 = 0;
            int l1 = 0;
            for (int i2 = 0; i2 < s.length(); i2++) {
                if (s.charAt(i2) == '@' && i2 + 4 < s.length() && s.charAt(i2 + 4) == '@')
                    i2 += 4;
                else if (s.charAt(i2) == '~' && i2 + 4 < s.length() && s.charAt(i2 + 4) == '~')
                    i2 += 4;
                else
                    j1 += abyte0[anIntArray337[s.charAt(i2)] + 7];
                if (s.charAt(i2) == ' ')
                    l1 = i2;
                if (s.charAt(i2) == '%') {
                    l1 = i2;
                    j1 = 1000;
                }
                if (j1 > i1) {
                    if (l1 <= k1)
                        l1 = i2;
                    drawstring_center(s.substring(k1, l1), i, j, k, l);
                    j1 = 0;
                    k1 = i2 = l1 + 1;
                    j += textHeightNumber(k);
                }
            }

            if (j1 > 0) {
                drawstring_center(s.substring(k1), i, j, k, l);
                return;
            }
        } catch (Exception exception) {
            System.out.println("centrepara: " + exception);
            exception.printStackTrace();
        }
    }

    public void drawstring(String s, int x, int y, int k, int colour) {
        try {
            byte abyte0[] = aByteArrayArray336[k];
            for (int i1 = 0; i1 < s.length(); i1++)
                if (s.charAt(i1) == '@' && i1 + 4 < s.length() && s.charAt(i1 + 4) == '@') {
                    if (s.substring(i1 + 1, i1 + 4).equalsIgnoreCase("red"))
                        colour = 0xff0000;
                    else if (s.substring(i1 + 1, i1 + 4).equalsIgnoreCase("lre"))
                        colour = 0xff9040;
                    else if (s.substring(i1 + 1, i1 + 4).equalsIgnoreCase("yel"))
                        colour = 0xffff00;
                    else if (s.substring(i1 + 1, i1 + 4).equalsIgnoreCase("gre"))
                        colour = 65280;
                    else if (s.substring(i1 + 1, i1 + 4).equalsIgnoreCase("blu"))
                        colour = 255;
                    else if (s.substring(i1 + 1, i1 + 4).equalsIgnoreCase("cya"))
                        colour = 65535;
                    else if (s.substring(i1 + 1, i1 + 4).equalsIgnoreCase("mag"))
                        colour = 0xff00ff;
                    else if (s.substring(i1 + 1, i1 + 4).equalsIgnoreCase("whi"))
                        colour = 0xffffff;
                    else if (s.substring(i1 + 1, i1 + 4).equalsIgnoreCase("bla"))
                        colour = 0;
                    else if (s.substring(i1 + 1, i1 + 4).equalsIgnoreCase("dre"))
                        colour = 0xc00000;
                    else if (s.substring(i1 + 1, i1 + 4).equalsIgnoreCase("ora"))
                        colour = 0xff9040;
                    else if (s.substring(i1 + 1, i1 + 4).equalsIgnoreCase("ran"))
                        colour = (int) (Math.random() * 16777215D);
                    else if (s.substring(i1 + 1, i1 + 4).equalsIgnoreCase("or1"))
                        colour = 0xffb000;
                    else if (s.substring(i1 + 1, i1 + 4).equalsIgnoreCase("or2"))
                        colour = 0xff7000;
                    else if (s.substring(i1 + 1, i1 + 4).equalsIgnoreCase("or3"))
                        colour = 0xff3000;
                    else if (s.substring(i1 + 1, i1 + 4).equalsIgnoreCase("gr1"))
                        colour = 0xc0ff00;
                    else if (s.substring(i1 + 1, i1 + 4).equalsIgnoreCase("gr2"))
                        colour = 0x80ff00;
                    else if (s.substring(i1 + 1, i1 + 4).equalsIgnoreCase("gr3"))
                        colour = 0x40ff00;
                    i1 += 4;
                } else if (s.charAt(i1) == '~' && i1 + 4 < s.length() && s.charAt(i1 + 4) == '~') {
                    char c = s.charAt(i1 + 1);
                    char c1 = s.charAt(i1 + 2);
                    char c2 = s.charAt(i1 + 3);
                    if (c >= '0' && c <= '9' && c1 >= '0' && c1 <= '9' && c2 >= '0' && c2 <= '9')
                        x = Integer.parseInt(s.substring(i1 + 1, i1 + 4));
                    i1 += 4;
                } else {
                    int j1 = anIntArray337[s.charAt(i1)];
                    if (loggedIn && !aBooleanArray349[k] && colour != 0)
                        method257(j1, x + 1, y, 0, abyte0, aBooleanArray349[k]);
                    if (loggedIn && !aBooleanArray349[k] && colour != 0)
                        method257(j1, x, y + 1, 0, abyte0, aBooleanArray349[k]);
                    method257(j1, x, y, colour, abyte0, aBooleanArray349[k]);
                    x += abyte0[j1 + 7];
                }

            return;
        } catch (Exception exception) {
            System.out.println("drawstring: " + exception);
            exception.printStackTrace();
            return;
        }
    }

    private void method257(int i, int j, int k, int l, byte abyte0[], boolean flag) {
        int i1 = j + abyte0[i + 5];
        int j1 = k - abyte0[i + 6];
        int k1 = abyte0[i + 3];
        int l1 = abyte0[i + 4];
        int i2 = abyte0[i] * 16384 + abyte0[i + 1] * 128 + abyte0[i + 2];
        int j2 = i1 + j1 * width2;
        int k2 = width2 - k1;
        int l2 = 0;
        if (j1 < bounds_top_y) {
            int i3 = bounds_top_y - j1;
            l1 -= i3;
            j1 = bounds_top_y;
            i2 += i3 * k1;
            j2 += i3 * width2;
        }
        if (j1 + l1 >= bounds_bottom_y)
            l1 -= ((j1 + l1) - bounds_bottom_y) + 1;
        if (i1 < bounds_top_x) {
            int j3 = bounds_top_x - i1;
            k1 -= j3;
            i1 = bounds_top_x;
            i2 += j3;
            j2 += j3;
            l2 += j3;
            k2 += j3;
        }
        if (i1 + k1 >= bounds_bottom_x) {
            int k3 = ((i1 + k1) - bounds_bottom_x) + 1;
            k1 -= k3;
            l2 += k3;
            k2 += k3;
        }
        if (k1 > 0 && l1 > 0) {
            if (flag) {
                method259(pixels, abyte0, l, i2, j2, k1, l1, k2, l2);
                return;
            }
            plotletter(pixels, abyte0, l, i2, j2, k1, l1, k2, l2);
        }
    }

    private void plotletter(int ai[], byte abyte0[], int i, int j, int k, int l, int i1,
                            int j1, int k1) {
        try {
            int l1 = -(l >> 2);
            l = -(l & 3);
            for (int i2 = -i1; i2 < 0; i2++) {
                for (int j2 = l1; j2 < 0; j2++) {
                    if (abyte0[j++] != 0)
                        ai[k++] = i;
                    else
                        k++;
                    if (abyte0[j++] != 0)
                        ai[k++] = i;
                    else
                        k++;
                    if (abyte0[j++] != 0)
                        ai[k++] = i;
                    else
                        k++;
                    if (abyte0[j++] != 0)
                        ai[k++] = i;
                    else
                        k++;
                }

                for (int k2 = l; k2 < 0; k2++)
                    if (abyte0[j++] != 0)
                        ai[k++] = i;
                    else
                        k++;

                k += j1;
                j += k1;
            }

            return;
        } catch (Exception exception) {
            System.out.println("plotletter: " + exception);
            exception.printStackTrace();
            return;
        }
    }

    private void method259(int ai[], byte abyte0[], int i, int j, int k, int l, int i1,
                           int j1, int k1) {
        for (int l1 = -i1; l1 < 0; l1++) {
            for (int i2 = -l; i2 < 0; i2++) {
                int j2 = abyte0[j++] & 0xff;
                if (j2 > 30) {
                    if (j2 >= 230) {
                        ai[k++] = i;
                    } else {
                        int k2 = ai[k];
                        ai[k++] = ((i & 0xff00ff) * j2 + (k2 & 0xff00ff) * (256 - j2) & 0xff00ff00) + ((i & 0xff00) * j2 + (k2 & 0xff00) * (256 - j2) & 0xff0000) >> 8;
                    }
                } else {
                    k++;
                }
            }

            k += j1;
            j += k1;
        }

    }

    public int textHeightNumber(int i) {
        if (i == 0)
            return 12;
        if (i == 1)
            return 14;
        if (i == 2)
            return 14;
        if (i == 3)
            return 15;
        if (i == 4)
            return 15;
        if (i == 5)
            return 19;
        if (i == 6)
            return 24;
        if (i == 7)
            return 29;
        else
            return method261(i);
    }

    public int method261(int i) {
        if (i == 0)
            return aByteArrayArray336[i][8] - 2;
        else
            return aByteArrayArray336[i][8] - 1;
    }

    public int textWidth(String s, int i) {
        int j = 0;
        byte abyte0[] = aByteArrayArray336[i];
        for (int k = 0; k < s.length(); k++)
            if (s.charAt(k) == '@' && k + 4 < s.length() && s.charAt(k + 4) == '@')
                k += 4;
            else if (s.charAt(k) == '~' && k + 4 < s.length() && s.charAt(k + 4) == '~')
                k += 4;
            else
                j += abyte0[anIntArray337[s.charAt(k)] + 7];

        return j;
    }

    public boolean imageUpdate(Image image, int i, int j, int k, int l, int i1) {
        return true;
    }

    public int width2;
    public int height2;
    public int area;
    public int width1;
    public int height1;
    ColorModel color_model;
    public int pixels[];
    ImageConsumer imageconsumer;
    private Component aComponent319;
    public Image image;
    public int surface_set_pixels[][];
    public byte aByteArrayArray322[][];
    public int anIntArrayArray323[][];
    public int image_width[];
    public int image_height[];
    public int offset_x[];
    public int offset_y[];
    public int image_full_width[];
    public int image_full_height[];
    public boolean image_translate[];
    private int bounds_top_y;
    private int bounds_bottom_y;
    private int bounds_top_x;
    private int bounds_bottom_x;
    public boolean interlace;
    static byte aByteArrayArray336[][] = new byte[50][];
    static int anIntArray337[];
    public boolean loggedIn;
    int landscape_colours[];
    int anIntArray340[];
    int anIntArray341[];
    int anIntArray342[];
    int anIntArray343[];
    int anIntArray344[];
    int anIntArray345[];
    public static int anInt346;
    public static int anInt347;
    public static int anInt348;
    private static boolean aBooleanArray349[] = new boolean[12];
    private static int anInt350;
    private static byte aByteArray351[] = new byte[0x186a0];
    public static int anInt352;

    static {
        String s = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!\"\243$%^&*()-_=+[{]};:'@#~,<.>/?\\| ";
        anIntArray337 = new int[256];
        for (int i = 0; i < 256; i++) {
            int j = s.indexOf(i);
            if (j == -1)
                j = 74;
            anIntArray337[i] = j * 9;
        }

    }
}
