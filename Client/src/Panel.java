public class Panel {

    public Panel(Surface surface, int max) {
        focus_control_index = -1;
        aBoolean219 = true;
        this.surface = surface;
        max_controls = max;
        control_shown = new boolean[max];
        control_list_scrollbar_handle_dragged = new boolean[max];
        control_mask_text = new boolean[max];
        control_clicked = new boolean[max];
        control_use_alternative_colour = new boolean[max];
        control_flash_text = new int[max];// not so sure
        control_list_entry_count = new int[max];
        control_list_entry_mouse_button_down = new int[max];
        control_list_entry_mouse_over = new int[max];
        control_x = new int[max];
        control_y = new int[max];
        control_type = new int[max];
        control_width = new int[max];
        control_height = new int[max];
        control_input_max_len = new int[max];
        control_text_size = new int[max];
        control_text = new String[max];
        control_list_entries = new String[max][];
        colour_scrollbar_top = rgb2long_mod(114, 114, 176);
        colour_scrollbar_bottom = rgb2long_mod(14, 14, 62);
        colour_scrollbar_handle_left = rgb2long_mod(200, 208, 232);
        colour_scrollbar_handle_mid = rgb2long_mod(96, 129, 184);
        colour_scrollbar_handle_right = rgb2long_mod(53, 95, 115);
        colour_rounded_box_out = rgb2long_mod(117, 142, 171);
        colour_rounded_box_mid = rgb2long_mod(98, 122, 158);
        colour_rounded_box_in = rgb2long_mod(86, 100, 136);
        colour_box_top_n_bottom = rgb2long_mod(135, 146, 179);
        colour_box_top_n_bottom2 = rgb2long_mod(97, 112, 151);
        colour_box_left_n_right2 = rgb2long_mod(88, 102, 136);
        colour_box_left_n_right = rgb2long_mod(84, 93, 120);
    }

    public int rgb2long_mod(int i, int j, int k) {
        return Surface.rgb2long((red_mod * i) / 114, (green_mod * j) / 114, (blue_mod * k) / 176);
    }

    public void handle_mouse(int mx, int my, int lastmb, int mbdown) {
        mouse_x = mx;
        mouse_y = my;
        mouse_button_down = mbdown;
        if (lastmb != 0)
            mouse_last_button_down = lastmb;
        if (lastmb == 1) {
            for (int i1 = 0; i1 < control_count; i1++) {
                if (control_shown[i1] && control_type[i1] == 10 && mouse_x >= control_x[i1] && mouse_y >= control_y[i1] && mouse_x <= control_x[i1] + control_width[i1] && mouse_y <= control_y[i1] + control_height[i1])
                    control_clicked[i1] = true;
                if (control_shown[i1] && control_type[i1] == 14 && mouse_x >= control_x[i1] && mouse_y >= control_y[i1] && mouse_x <= control_x[i1] + control_width[i1] && mouse_y <= control_y[i1] + control_height[i1])
                    control_list_entry_mouse_button_down[i1] = 1 - control_list_entry_mouse_button_down[i1];
            }

        }
        if (mbdown == 1)
            mouse_meta_button_held++;
        else
            mouse_meta_button_held = 0;
        if (lastmb == 1 || mouse_meta_button_held > 20) {
            for (int j1 = 0; j1 < control_count; j1++)
                if (control_shown[j1] && control_type[j1] == 15 && mouse_x >= control_x[j1] && mouse_y >= control_y[j1] && mouse_x <= control_x[j1] + control_width[j1] && mouse_y <= control_y[j1] + control_height[j1])
                    control_clicked[j1] = true;

            mouse_meta_button_held -= 5;
        }
    }

    public boolean isClicked(int i) {
        if (control_shown[i] && control_clicked[i]) {
            control_clicked[i] = false;
            return true;
        } else {
            return false;
        }
    }

    public void keyPress(int key) {
        if (key == 0)
            return;
        if (focus_control_index != -1 && control_text[focus_control_index] != null && control_shown[focus_control_index]) {
            int input_len = control_text[focus_control_index].length();
            if (key == 8 && input_len > 0)
                control_text[focus_control_index] = control_text[focus_control_index].substring(0, input_len - 1);
            if ((key == 10 || key == 13) && input_len > 0)
                control_clicked[focus_control_index] = true;
            String s = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!\"\243$%^&*()-_=+[{]};:'@#~,<.>/?\\| ";
            if (input_len < control_input_max_len[focus_control_index]) {
                for (int k = 0; k < s.length(); k++)
                    if (key == s.charAt(k))
                        control_text[focus_control_index] += (char) key;

            }
            if (key == 9)
                do
                    focus_control_index = (focus_control_index + 1) % control_count;
                while (control_type[focus_control_index] != 5 && control_type[focus_control_index] != 6);
        }
    }

    public void drawPanel() {
        for (int i = 0; i < control_count; i++)
            if (control_shown[i])
                if (control_type[i] == 0)// text
                    draw_text(i, control_x[i], control_y[i], control_text[i], control_text_size[i]);
                else if (control_type[i] == 1)// text (centered)
                    draw_text(i, control_x[i] - surface.textWidth(control_text[i], control_text_size[i]) / 2, control_y[i], control_text[i], control_text_size[i]);
                else if (control_type[i] == 2)// component gradient bg
                    draw_box(control_x[i], control_y[i], control_width[i], control_height[i]);
                else if (control_type[i] == 3)// horiz line
                    draw_line_horiz(control_x[i], control_y[i], control_width[i]);
                else if (control_type[i] == 4)// text list (no interaction)
                    draw_text_list(i, control_x[i], control_y[i], control_width[i], control_height[i], control_text_size[i], control_list_entries[i], control_list_entry_count[i], control_flash_text[i]);
                else if (control_type[i] == 5 || control_type[i] == 6)// input text
                    draw_text_input(i, control_x[i], control_y[i], control_width[i], control_height[i], control_text[i], control_text_size[i]);
                else if (control_type[i] == 7)// option list horiz
                    draw_option_list_horiz(i, control_x[i], control_y[i], control_text_size[i], control_list_entries[i]);
                else if (control_type[i] == 8)// option list vert
                    draw_option_list_vert(i, control_x[i], control_y[i], control_text_size[i], control_list_entries[i]);
                else if (control_type[i] == 9)// text list (interaction)
                    draw_text_list_interactive(i, control_x[i], control_y[i], control_width[i], control_height[i], control_text_size[i], control_list_entries[i], control_list_entry_count[i], control_flash_text[i]);
                else if (control_type[i] == 11)// rounded box
                    draw_rounded_box(control_x[i], control_y[i], control_width[i], control_height[i]);
                else if (control_type[i] == 12)// image
                    draw_picture(control_x[i], control_y[i], control_text_size[i]);
                else if (control_type[i] == 14)// checkbox
                    draw_checkbox(i, control_x[i], control_y[i], control_width[i], control_height[i]);

        mouse_last_button_down = 0;
    }

    protected void draw_checkbox(int control, int x, int y, int width, int height) {
        surface.draw_box(x, y, width, height, 0xffffff);
        surface.draw_line_horiz(x, y, width, colour_box_top_n_bottom);
        surface.draw_line_vert(x, y, height, colour_box_top_n_bottom);
        surface.draw_line_horiz(x, (y + height) - 1, width, colour_box_left_n_right);
        surface.draw_line_vert((x + width) - 1, y, height, colour_box_left_n_right);
        if (control_list_entry_mouse_button_down[control] == 1) {
            for (int j1 = 0; j1 < height; j1++) {
                surface.draw_line_horiz(x + j1, y + j1, 1, 0);
                surface.draw_line_horiz((x + width) - 1 - j1, y + j1, 1, 0);
            }

        }
    }

    protected void draw_text(int control, int x, int y, String text, int text_size) {
        int y2 = y + surface.textHeightNumber(text_size) / 3;
        drawstring(control, x, y2, text, text_size);
    }

    protected void drawstring(int control, int x, int y, String text, int text_size) {
        int i1;
        if (control_use_alternative_colour[control])
            i1 = 0xffffff;
        else
            i1 = 0;
        surface.drawstring(text, x, y, text_size, i1);
    }

    protected void draw_text_input(int control, int x, int y, int width, int height, String text, int text_size) {
        if (control_mask_text[control]) {
            int len = text.length();
            text = "";
            for (int i2 = 0; i2 < len; i2++)
                text = text + "X";

        }
        if (control_type[control] == 5) {// "list input"
            if (mouse_last_button_down == 1 && mouse_x >= x && mouse_y >= y - height / 2 && mouse_x <= x + width && mouse_y <= y + height / 2)
                focus_control_index = control;
        } else if (control_type[control] == 6) {// "text input"
            if (mouse_last_button_down == 1 && mouse_x >= x - width / 2 && mouse_y >= y - height / 2 && mouse_x <= x + width / 2 && mouse_y <= y + height / 2)
                focus_control_index = control;
            x -= surface.textWidth(text, text_size) / 2;
        }
        if (focus_control_index == control)
            text = text + "*";
        int y2 = y + surface.textHeightNumber(text_size) / 3;
        drawstring(control, x, y2, text, text_size);
    }

    public void draw_box(int x, int y, int width, int height) {
        surface.set_bounds(x, y, x + width, y + height);
        surface.draw_gradient(x, y, width, height, colour_box_left_n_right, colour_box_top_n_bottom);
        if (draw_background_arrow) {// set to false inside startGame, draw some kindof an arrow??
            for (int i1 = x - (y & 0x3f); i1 < x + width; i1 += 128) {
                for (int j1 = y - (y & 0x1f); j1 < y + height; j1 += 128)
                    surface.method232(i1, j1, 6 + baseSpriteStart, 128);

            }

        }
        surface.draw_line_horiz(x, y, width, colour_box_top_n_bottom);
        surface.draw_line_horiz(x + 1, y + 1, width - 2, colour_box_top_n_bottom);
        surface.draw_line_horiz(x + 2, y + 2, width - 4, colour_box_top_n_bottom2);
        surface.draw_line_vert(x, y, height, colour_box_top_n_bottom);
        surface.draw_line_vert(x + 1, y + 1, height - 2, colour_box_top_n_bottom);
        surface.draw_line_vert(x + 2, y + 2, height - 4, colour_box_top_n_bottom2);
        surface.draw_line_horiz(x, (y + height) - 1, width, colour_box_left_n_right);
        surface.draw_line_horiz(x + 1, (y + height) - 2, width - 2, colour_box_left_n_right);
        surface.draw_line_horiz(x + 2, (y + height) - 3, width - 4, colour_box_left_n_right2);
        surface.draw_line_vert((x + width) - 1, y, height, colour_box_left_n_right);
        surface.draw_line_vert((x + width) - 2, y + 1, height - 2, colour_box_left_n_right);
        surface.draw_line_vert((x + width) - 3, y + 2, height - 4, colour_box_left_n_right2);
        surface.reset_bounds();
    }

    public void draw_rounded_box(int x, int y, int width, int height) {
        surface.draw_box(x, y, width, height, 0);
        surface.draw_box_edge(x, y, width, height, colour_rounded_box_out);
        surface.draw_box_edge(x + 1, y + 1, width - 2, height - 2, colour_rounded_box_mid);
        surface.draw_box_edge(x + 2, y + 2, width - 4, height - 4, colour_rounded_box_in);
        surface.draw_picture(x, y, 2 + baseSpriteStart);
        surface.draw_picture((x + width) - 7, y, 3 + baseSpriteStart);
        surface.draw_picture(x, (y + height) - 7, 4 + baseSpriteStart);
        surface.draw_picture((x + width) - 7, (y + height) - 7, 5 + baseSpriteStart);
    }

    protected void draw_picture(int x, int y, int size) {
        surface.draw_picture(x, y, size);
    }

    protected void draw_line_horiz(int x, int y, int width) {
        surface.draw_line_horiz(x, y, width, 0xffffff);
    }

    protected void draw_text_list(int control, int x, int y, int width, int height, int text_size,
                                  String list_entries[], int list_entry_count, int l1) {
        int displayed_entry_count = height / surface.textHeightNumber(text_size);
        if (l1 > list_entry_count - displayed_entry_count)
            l1 = list_entry_count - displayed_entry_count;
        if (l1 < 0)
            l1 = 0;
        control_flash_text[control] = l1;
        if (displayed_entry_count < list_entry_count) {
            int corner_top_right = (x + width) - 12;
            int corner_bottom_left = ((height - 27) * displayed_entry_count) / list_entry_count;
            if (corner_bottom_left < 6)
                corner_bottom_left = 6;
            int j3 = ((height - 27 - corner_bottom_left) * l1) / (list_entry_count - displayed_entry_count);
            if (mouse_button_down == 1 && mouse_x >= corner_top_right && mouse_x <= corner_top_right + 12) {
                if (mouse_y > y && mouse_y < y + 12 && l1 > 0)
                    l1--;
                if (mouse_y > (y + height) - 12 && mouse_y < y + height && l1 < list_entry_count - displayed_entry_count)
                    l1++;
                control_flash_text[control] = l1;
            }
            if (mouse_button_down == 1 && (mouse_x >= corner_top_right && mouse_x <= corner_top_right + 12 || mouse_x >= corner_top_right - 12 && mouse_x <= corner_top_right + 24 && control_list_scrollbar_handle_dragged[control])) {
                if (mouse_y > y + 12 && mouse_y < (y + height) - 12) {
                    control_list_scrollbar_handle_dragged[control] = true;
                    int l3 = mouse_y - y - 12 - corner_bottom_left / 2;
                    l1 = (l3 * list_entry_count) / (height - 24);
                    if (l1 > list_entry_count - displayed_entry_count)
                        l1 = list_entry_count - displayed_entry_count;
                    if (l1 < 0)
                        l1 = 0;
                    control_flash_text[control] = l1;
                }
            } else {
                control_list_scrollbar_handle_dragged[control] = false;
            }
            j3 = ((height - 27 - corner_bottom_left) * l1) / (list_entry_count - displayed_entry_count);
            draw_list_container(x, y, width, height, j3, corner_bottom_left);
        }
        int entry_list_start_y = height - displayed_entry_count * surface.textHeightNumber(text_size);
        int y2 = y + (surface.textHeightNumber(text_size) * 5) / 6 + entry_list_start_y / 2;
        for (int entry = l1; entry < list_entry_count; entry++) {
            drawstring(control, x + 2, y2, list_entries[entry], text_size);
            y2 += surface.textHeightNumber(text_size) - text_list_entry_height_mod;
            if (y2 >= y + height)
                return;
        }

    }

    protected void draw_list_container(int x, int y, int width, int height, int corner1, int corner2) {
        int x2 = (x + width) - 12;
        surface.draw_box_edge(x2, y, 12, height, 0);
        surface.draw_picture(x2 + 1, y + 1, baseSpriteStart);// up arrow?
        surface.draw_picture(x2 + 1, (y + height) - 12, 1 + baseSpriteStart);// down arrow?
        surface.draw_line_horiz(x2, y + 13, 12, 0);
        surface.draw_line_horiz(x2, (y + height) - 13, 12, 0);
        surface.draw_gradient(x2 + 1, y + 14, 11, height - 27, colour_scrollbar_top, colour_scrollbar_bottom);
        surface.draw_box(x2 + 3, corner1 + y + 14, 7, corner2, colour_scrollbar_handle_mid);
        surface.draw_line_vert(x2 + 2, corner1 + y + 14, corner2, colour_scrollbar_handle_left);
        surface.draw_line_vert(x2 + 2 + 8, corner1 + y + 14, corner2, colour_scrollbar_handle_right);
    }

    protected void draw_option_list_horiz(int control, int x, int y, int text_size, String list_entries[]) {
        int list_total_text_width = 0;
        int list_entry_count = list_entries.length;
        for (int idx = 0; idx < list_entry_count; idx++) {
            list_total_text_width += surface.textWidth(list_entries[idx], text_size);
            if (idx < list_entry_count - 1)
                list_total_text_width += surface.textWidth("  ", text_size);
        }

        int left = x - list_total_text_width / 2;
        int bottom = y + surface.textHeightNumber(text_size) / 3;
        for (int idx = 0; idx < list_entry_count; idx++) {
            int colour;
            if (control_use_alternative_colour[control])
                colour = 0xffffff;
            else
                colour = 0;
            if (mouse_x >= left && mouse_x <= left + surface.textWidth(list_entries[idx], text_size) && mouse_y <= bottom && mouse_y > bottom - surface.textHeightNumber(text_size)) {
                if (control_use_alternative_colour[control])
                    colour = 0x808080;
                else
                    colour = 0xffffff;
                if (mouse_last_button_down == 1) {
                    control_list_entry_mouse_button_down[control] = idx;
                    control_clicked[control] = true;
                }
            }
            if (control_list_entry_mouse_button_down[control] == idx)
                if (control_use_alternative_colour[control])
                    colour = 0xff0000;
                else
                    colour = 0xc00000;
            surface.drawstring(list_entries[idx], left, bottom, text_size, colour);
            left += surface.textWidth(list_entries[idx] + "  ", text_size);
        }

    }

    protected void draw_option_list_vert(int control, int x, int y, int text_size, String list_entries[]) {
        int list_entry_count = list_entries.length;
        int list_total_text_height_mid = y - (surface.textHeightNumber(text_size) * (list_entry_count - 1)) / 2;
        for (int idx = 0; idx < list_entry_count; idx++) {
            int colour;
            if (control_use_alternative_colour[control])
                colour = 0xffffff;
            else
                colour = 0;
            int entry_text_width = surface.textWidth(list_entries[idx], text_size);
            if (mouse_x >= x - entry_text_width / 2 && mouse_x <= x + entry_text_width / 2 && mouse_y - 2 <= list_total_text_height_mid && mouse_y - 2 > list_total_text_height_mid - surface.textHeightNumber(text_size)) {
                if (control_use_alternative_colour[control])
                    colour = 0x808080;
                else
                    colour = 0xffffff;
                if (mouse_last_button_down == 1) {
                    control_list_entry_mouse_button_down[control] = idx;
                    control_clicked[control] = true;
                }
            }
            if (control_list_entry_mouse_button_down[control] == idx)
                if (control_use_alternative_colour[control])
                    colour = 0xff0000;
                else
                    colour = 0xc00000;
            surface.drawstring(list_entries[idx], x - entry_text_width / 2, list_total_text_height_mid, text_size, colour);
            list_total_text_height_mid += surface.textHeightNumber(text_size);
        }

    }

    protected void draw_text_list_interactive(int control, int x, int y, int width, int height, int text_size,
                                              String list_entries[], int list_entry_count, int l1) {
        int displayed_entry_count = height / surface.textHeightNumber(text_size);
        if (displayed_entry_count < list_entry_count) {
            int right = (x + width) - 12;
            int l2 = ((height - 27) * displayed_entry_count) / list_entry_count;
            if (l2 < 6)
                l2 = 6;
            int j3 = ((height - 27 - l2) * l1) / (list_entry_count - displayed_entry_count);
            if (mouse_button_down == 1 && mouse_x >= right && mouse_x <= right + 12) {
                if (mouse_y > y && mouse_y < y + 12 && l1 > 0)
                    l1--;
                if (mouse_y > (y + height) - 12 && mouse_y < y + height && l1 < list_entry_count - displayed_entry_count)
                    l1++;
                control_flash_text[control] = l1;
            }
            if (mouse_button_down == 1 && (mouse_x >= right && mouse_x <= right + 12 || mouse_x >= right - 12 && mouse_x <= right + 24 && control_list_scrollbar_handle_dragged[control])) {
                if (mouse_y > y + 12 && mouse_y < (y + height) - 12) {
                    control_list_scrollbar_handle_dragged[control] = true;
                    int l3 = mouse_y - y - 12 - l2 / 2;
                    l1 = (l3 * list_entry_count) / (height - 24);
                    if (l1 < 0)
                        l1 = 0;
                    if (l1 > list_entry_count - displayed_entry_count)
                        l1 = list_entry_count - displayed_entry_count;
                    control_flash_text[control] = l1;
                }
            } else {
                control_list_scrollbar_handle_dragged[control] = false;
            }
            j3 = ((height - 27 - l2) * l1) / (list_entry_count - displayed_entry_count);
            draw_list_container(x, y, width, height, j3, l2);
        } else {
            l1 = 0;
            control_flash_text[control] = 0;
        }
        control_list_entry_mouse_over[control] = -1;
        int k2 = height - displayed_entry_count * surface.textHeightNumber(text_size);
        int i3 = y + (surface.textHeightNumber(text_size) * 5) / 6 + k2 / 2;
        for (int k3 = l1; k3 < list_entry_count; k3++) {
            int i4;
            if (control_use_alternative_colour[control])
                i4 = 0xffffff;
            else
                i4 = 0;
            if (mouse_x >= x + 2 && mouse_x <= x + 2 + surface.textWidth(list_entries[k3], text_size) && mouse_y - 2 <= i3 && mouse_y - 2 > i3 - surface.textHeightNumber(text_size)) {
                if (control_use_alternative_colour[control])
                    i4 = 0x808080;
                else
                    i4 = 0xffffff;
                control_list_entry_mouse_over[control] = k3;
                if (mouse_last_button_down == 1) {
                    control_list_entry_mouse_button_down[control] = k3;
                    control_clicked[control] = true;
                }
            }
            if (control_list_entry_mouse_button_down[control] == k3 && aBoolean219)
                i4 = 0xff0000;
            surface.drawstring(list_entries[k3], x + 2, i3, text_size, i4);
            i3 += surface.textHeightNumber(text_size);
            if (i3 >= y + height)
                return;
        }

    }

    public int add_text(int x, int y, String text, int size, boolean flag) {
        control_type[control_count] = 1;
        control_shown[control_count] = true;
        control_clicked[control_count] = false;
        control_text_size[control_count] = size;
        control_use_alternative_colour[control_count] = flag;
        control_x[control_count] = x;
        control_y[control_count] = y;
        control_text[control_count] = text;
        return control_count++;
    }

    public int add_button_background(int x, int y, int width, int height) {
        control_type[control_count] = 2;
        control_shown[control_count] = true;
        control_clicked[control_count] = false;
        control_x[control_count] = x - width / 2;
        control_y[control_count] = y - height / 2;
        control_width[control_count] = width;
        control_height[control_count] = height;
        return control_count++;
    }

    public int add_box_rounded(int x, int y, int width, int height) {
        control_type[control_count] = 11;
        control_shown[control_count] = true;
        control_clicked[control_count] = false;
        control_x[control_count] = x - width / 2;
        control_y[control_count] = y - height / 2;
        control_width[control_count] = width;
        control_height[control_count] = height;
        return control_count++;
    }

    public int add_image(int x, int y, int image_id) {
        int img_width = surface.image_width[image_id];
        int img_height = surface.image_height[image_id];
        control_type[control_count] = 12;
        control_shown[control_count] = true;
        control_clicked[control_count] = false;
        control_x[control_count] = x - img_width / 2;
        control_y[control_count] = y - img_height / 2;
        control_width[control_count] = img_width;
        control_height[control_count] = img_height;
        control_text_size[control_count] = image_id;
        return control_count++;
    }

    public int add_text_list(int x, int y, int width, int height, int size, int max_length, boolean flag) {
        control_type[control_count] = 4;
        control_shown[control_count] = true;
        control_clicked[control_count] = false;
        control_x[control_count] = x;
        control_y[control_count] = y;
        control_width[control_count] = width;
        control_height[control_count] = height;
        control_use_alternative_colour[control_count] = flag;
        control_text_size[control_count] = size;
        control_input_max_len[control_count] = max_length;
        control_list_entry_count[control_count] = 0;
        control_flash_text[control_count] = 0;
        control_list_entries[control_count] = new String[max_length];
        return control_count++;
    }

    public int add_text_list_input(int x, int y, int width, int height, int size, int max_length, boolean flag,
                                   boolean flag1) {
        control_type[control_count] = 5;
        control_shown[control_count] = true;
        control_mask_text[control_count] = flag;
        control_clicked[control_count] = false;
        control_text_size[control_count] = size;
        control_use_alternative_colour[control_count] = flag1;
        control_x[control_count] = x;
        control_y[control_count] = y;
        control_width[control_count] = width;
        control_height[control_count] = height;
        control_input_max_len[control_count] = max_length;
        control_text[control_count] = "";
        return control_count++;
    }

    public int add_text_input(int x, int y, int width, int height, int size, int max_length, boolean flag,
                              boolean flag1) {
        control_type[control_count] = 6;
        control_shown[control_count] = true;
        control_mask_text[control_count] = flag;
        control_clicked[control_count] = false;
        control_text_size[control_count] = size;
        control_use_alternative_colour[control_count] = flag1;
        control_x[control_count] = x;
        control_y[control_count] = y;
        control_width[control_count] = width;
        control_height[control_count] = height;
        control_input_max_len[control_count] = max_length;
        control_text[control_count] = "";
        return control_count++;
    }

    public int add_text_list_interactive(int x, int y, int width, int height, int text_size, int max_length, boolean flag) {
        control_type[control_count] = 9;
        control_shown[control_count] = true;
        control_clicked[control_count] = false;
        control_text_size[control_count] = text_size;
        control_use_alternative_colour[control_count] = flag;
        control_x[control_count] = x;
        control_y[control_count] = y;
        control_width[control_count] = width;
        control_height[control_count] = height;
        control_input_max_len[control_count] = max_length;
        control_list_entries[control_count] = new String[max_length];
        control_list_entry_count[control_count] = 0;
        control_flash_text[control_count] = 0;
        control_list_entry_mouse_button_down[control_count] = -1;
        control_list_entry_mouse_over[control_count] = -1;
        return control_count++;
    }

    public int add_button(int x, int y, int width, int height) {
        control_type[control_count] = 10;
        control_shown[control_count] = true;
        control_clicked[control_count] = false;
        control_x[control_count] = x - width / 2;
        control_y[control_count] = y - height / 2;
        control_width[control_count] = width;
        control_height[control_count] = height;
        return control_count++;
    }

    public int add_line_horiz(int x, int y, int width) {
        control_type[control_count] = 3;
        control_shown[control_count] = true;
        control_x[control_count] = x;
        control_y[control_count] = y;
        control_width[control_count] = width;
        return control_count++;
    }

    public int add_option_list_horiz(int x, int y, int text_size, int max_list_count,
                                     boolean use_alt_colour) {
        control_type[control_count] = 7;
        control_shown[control_count] = true;
        control_x[control_count] = x;
        control_y[control_count] = y;
        control_text_size[control_count] = text_size;
        control_list_entries[control_count] = new String[max_list_count];
        control_list_entry_count[control_count] = 0;
        control_use_alternative_colour[control_count] = use_alt_colour;
        control_clicked[control_count] = false;
        return control_count++;
    }

    public int add_option_list_vert(int x, int y, int text_size, int max_list_count,
                                    boolean use_alt_colour) {
        control_type[control_count] = 8;
        control_shown[control_count] = true;
        control_x[control_count] = x;
        control_y[control_count] = y;
        control_text_size[control_count] = text_size;
        control_list_entries[control_count] = new String[max_list_count];
        control_list_entry_count[control_count] = 0;
        control_use_alternative_colour[control_count] = use_alt_colour;
        control_clicked[control_count] = false;
        return control_count++;
    }

    public int add_checkbox(int x, int y, int width, int height) {
        control_type[control_count] = 14;
        control_shown[control_count] = true;
        control_x[control_count] = x;
        control_y[control_count] = y;
        control_width[control_count] = width;
        control_height[control_count] = height;
        control_list_entry_mouse_button_down[control_count] = 0;
        return control_count++;
    }

    public void clear_list(int control) {
        control_list_entry_count[control] = 0;
    }

    public void reset_list_props(int control) {
        control_flash_text[control] = 0;
        control_list_entry_mouse_over[control] = -1;
    }

    public void add_list_entry(int control, int index, String text) {
        control_list_entries[control][index] = text;
        if (index + 1 > control_list_entry_count[control])
            control_list_entry_count[control] = index + 1;
    }

    public void remove_list_entry(int control, String text, boolean flag) {
        int j = control_list_entry_count[control]++;
        if (j >= control_input_max_len[control]) {
            j--;
            control_list_entry_count[control]--;
            for (int k = 0; k < j; k++)
                control_list_entries[control][k] = control_list_entries[control][k + 1];

        }
        control_list_entries[control][j] = text;
        if (flag)
            control_flash_text[control] = 999999;// 0xf423f;
    }

    public void updateText(int control, String s) {
        control_text[control] = s;
    }

    public String getText(int control) {
        if (control_text[control] == null)
            return "null";
        else
            return control_text[control];
    }

    public void show(int control) {
        control_shown[control] = true;
    }

    public void hide(int control) {
        control_shown[control] = false;
    }

    public void setFocus(int control) {
        focus_control_index = control;
    }

    public int get_list_entry_index(int control) {
        return control_list_entry_mouse_over[control];
    }

    protected Surface surface;
    int control_count;
    int max_controls;
    public boolean control_shown[];
    public boolean control_list_scrollbar_handle_dragged[];
    public boolean control_mask_text[];
    public boolean control_clicked[];
    public int control_flash_text[];
    public int control_list_entry_count[];
    public int control_list_entry_mouse_button_down[];
    public int control_list_entry_mouse_over[];
    boolean control_use_alternative_colour[];
    int control_x[];
    int control_y[];
    int control_type[];
    int control_width[];
    int control_height[];
    int control_input_max_len[];
    int control_text_size[];
    String control_text[];
    String control_list_entries[][];
    int mouse_x;
    int mouse_y;
    int mouse_last_button_down;
    int mouse_button_down;
    int focus_control_index;
    int mouse_meta_button_held;
    int colour_scrollbar_top;
    int colour_scrollbar_bottom;
    int colour_scrollbar_handle_left;
    int colour_scrollbar_handle_mid;
    int colour_scrollbar_handle_right;
    int colour_rounded_box_out;
    int colour_rounded_box_mid;
    int colour_rounded_box_in;
    int colour_box_top_n_bottom;
    int colour_box_top_n_bottom2;
    int colour_box_left_n_right2;
    int colour_box_left_n_right;
    public boolean aBoolean219;
    public static boolean draw_background_arrow = true;
    public static int baseSpriteStart;
    public static int red_mod = 114;
    public static int green_mod = 114;
    public static int blue_mod = 176;
    public static int text_list_entry_height_mod;

}
