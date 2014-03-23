import java.awt.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;

public final class Client extends GameConnection {
	private static final long serialVersionUID = -2547107975604007657L;

	public static final void main(String args[]) {
        Client mc = new Client();
        mc.appletMode = false;
        if (args.length > 0 && args[0].equals("members"))
            mc.members = true;
        if (args.length > 1)
            mc.server = args[1];
        if (args.length > 2)
            mc.port = Integer.parseInt(args[2]);
        mc.startApplication(mc.game_width, mc.game_height + 11, "Runescape by Andrew Gower", false);
        mc.threadSleep = 10;
    }

    private final void playSoundFile(String s) {
        if (audioPlayer == null)
            return;
        if (!optionSoundDisabled)
            audioPlayer.writeStream(soundData, Utility.getDataFileOffset(s + ".pcm", soundData), Utility.getDataFileLength(s + ".pcm", soundData));
    }

    private final void draw_dialog_report_abuse() {
        report_abuse_offence = 0;
        int y = 135;
        for (int i = 0; i < 12; i++) {
            if (super.mouseX > 66 && super.mouseX < 446 && super.mouseY >= y - 12 && super.mouseY < y + 3)
                report_abuse_offence = i + 1;
            y += 14;
        }

        if (mouseButtonClick != 0 && report_abuse_offence != 0) {
            mouseButtonClick = 0;
            show_dialog_report_abuse_step = 2;
            super.input_text_current = "";
            super.input_text_final = "";
            return;
        }
        y += 15;
        if (mouseButtonClick != 0) {
            mouseButtonClick = 0;
            if (super.mouseX < 56 || super.mouseY < 35 || super.mouseX > 456 || super.mouseY > 325) {
                show_dialog_report_abuse_step = 0;
                return;
            }
            if (super.mouseX > 66 && super.mouseX < 446 && super.mouseY >= y - 15 && super.mouseY < y + 5) {
                show_dialog_report_abuse_step = 0;
                return;
            }
        }
        surface.draw_box(56, 35, 400, 290, 0);
        surface.draw_box_edge(56, 35, 400, 290, 0xffffff);
        y = 50;
        surface.drawstring_center("This form is for reporting players who are breaking our rules", 256, y, 1, 0xffffff);
        y += 15;
        surface.drawstring_center("Using it sends a snapshot of the last 60 secs of activity to us", 256, y, 1, 0xffffff);
        y += 15;
        surface.drawstring_center("If you misuse this form, you will be banned.", 256, y, 1, 0xff8000);
        y += 15;
        y += 10;
        surface.drawstring_center("First indicate which of our 12 rules is being broken. For a detailed", 256, y, 1, 0xffff00);
        y += 15;
        surface.drawstring_center("explanation of each rule please read the manual on our website.", 256, y, 1, 0xffff00);
        y += 15;
        int text_colour;
        if (report_abuse_offence == 1) {
            surface.draw_box_edge(66, y - 12, 380, 15, 0xffffff);
            text_colour = 0xff8000;
        } else {
            text_colour = 0xffffff;
        }
        surface.drawstring_center("1: Offensive language", 256, y, 1, text_colour);
        y += 14;
        if (report_abuse_offence == 2) {
            surface.draw_box_edge(66, y - 12, 380, 15, 0xffffff);
            text_colour = 0xff8000;
        } else {
            text_colour = 0xffffff;
        }
        surface.drawstring_center("2: Item scamming", 256, y, 1, text_colour);
        y += 14;
        if (report_abuse_offence == 3) {
            surface.draw_box_edge(66, y - 12, 380, 15, 0xffffff);
            text_colour = 0xff8000;
        } else {
            text_colour = 0xffffff;
        }
        surface.drawstring_center("3: Password scamming", 256, y, 1, text_colour);
        y += 14;
        if (report_abuse_offence == 4) {
            surface.draw_box_edge(66, y - 12, 380, 15, 0xffffff);
            text_colour = 0xff8000;
        } else {
            text_colour = 0xffffff;
        }
        surface.drawstring_center("4: Bug abuse", 256, y, 1, text_colour);
        y += 14;
        if (report_abuse_offence == 5) {
            surface.draw_box_edge(66, y - 12, 380, 15, 0xffffff);
            text_colour = 0xff8000;
        } else {
            text_colour = 0xffffff;
        }
        surface.drawstring_center("5: Jagex Staff impersonation", 256, y, 1, text_colour);
        y += 14;
        if (report_abuse_offence == 6) {
            surface.draw_box_edge(66, y - 12, 380, 15, 0xffffff);
            text_colour = 0xff8000;
        } else {
            text_colour = 0xffffff;
        }
        surface.drawstring_center("6: Account sharing/trading", 256, y, 1, text_colour);
        y += 14;
        if (report_abuse_offence == 7) {
            surface.draw_box_edge(66, y - 12, 380, 15, 0xffffff);
            text_colour = 0xff8000;
        } else {
            text_colour = 0xffffff;
        }
        surface.drawstring_center("7: Macroing", 256, y, 1, text_colour);
        y += 14;
        if (report_abuse_offence == 8) {
            surface.draw_box_edge(66, y - 12, 380, 15, 0xffffff);
            text_colour = 0xff8000;
        } else {
            text_colour = 0xffffff;
        }
        surface.drawstring_center("8: Mutiple logging in", 256, y, 1, text_colour);
        y += 14;
        if (report_abuse_offence == 9) {
            surface.draw_box_edge(66, y - 12, 380, 15, 0xffffff);
            text_colour = 0xff8000;
        } else {
            text_colour = 0xffffff;
        }
        surface.drawstring_center("9: Encouraging others to break rules", 256, y, 1, text_colour);
        y += 14;
        if (report_abuse_offence == 10) {
            surface.draw_box_edge(66, y - 12, 380, 15, 0xffffff);
            text_colour = 0xff8000;
        } else {
            text_colour = 0xffffff;
        }
        surface.drawstring_center("10: Misuse of customer support", 256, y, 1, text_colour);
        y += 14;
        if (report_abuse_offence == 11) {
            surface.draw_box_edge(66, y - 12, 380, 15, 0xffffff);
            text_colour = 0xff8000;
        } else {
            text_colour = 0xffffff;
        }
        surface.drawstring_center("11: Advertising / website", 256, y, 1, text_colour);
        y += 14;
        if (report_abuse_offence == 12) {
            surface.draw_box_edge(66, y - 12, 380, 15, 0xffffff);
            text_colour = 0xff8000;
        } else {
            text_colour = 0xffffff;
        }
        surface.drawstring_center("12: Real world item trading", 256, y, 1, text_colour);
        y += 14;
        y += 15;
        text_colour = 0xffffff;
        if (super.mouseX > 196 && super.mouseX < 316 && super.mouseY > y - 15 && super.mouseY < y + 5)
            text_colour = 0xffff00;
        surface.drawstring_center("Click here to cancel", 256, y, 1, text_colour);
    }

    private final boolean walkToActionSource(int startX, int startY, int x1, int y1, int x2, int y2, boolean checkObjects, boolean walkToAction) {
        int steps = world.route(startX, startY, x1, y1, x2, y2, walkPathX, walkPathY, checkObjects);
        if (steps == -1)
            if (walkToAction) {
                steps = 1;
                walkPathX[0] = x1;
                walkPathY[0] = y1;
            } else {
                return false;
            }
        steps--;
        startX = walkPathX[steps];
        startY = walkPathY[steps];
        steps--;
        if (walkToAction)
            super.clientStream.newPacket(16);
        else
            super.clientStream.newPacket(187);
        super.clientStream.addShort(startX + areaX);
        super.clientStream.addShort(startY + areaY);
        if (walkToAction && steps == -1 && (startX + areaX) % 5 == 0)
            steps = 0;
        for (int l1 = steps; l1 >= 0 && l1 > steps - 25; l1--) {
            super.clientStream.addByte(walkPathX[l1] - startX);
            super.clientStream.addByte(walkPathY[l1] - startY);
        }

        super.clientStream.sendPacket();
        mouseClickXStep = -24;
        mouseClickXX = super.mouseX;
        mouseClickXY = super.mouseY;
        return true;
    }

    private final boolean walkTo(int startX, int startY, int x1, int y1, int x2, int y2, boolean checkObjects,
                                 boolean walkToAction) {
        int steps = world.route(startX, startY, x1, y1, x2, y2, walkPathX, walkPathY, checkObjects);
        if (steps == -1)
            return false;
        steps--;
        startX = walkPathX[steps];
        startY = walkPathY[steps];
        steps--;
        if (walkToAction)
            super.clientStream.newPacket(16);
        else
            super.clientStream.newPacket(187);
        super.clientStream.addShort(startX + areaX);
        super.clientStream.addShort(startY + areaY);
        if (walkToAction && steps == -1 && (startX + areaX) % 5 == 0)
            steps = 0;
        for (int l1 = steps; l1 >= 0 && l1 > steps - 25; l1--) {
            super.clientStream.addByte(walkPathX[l1] - startX);
            super.clientStream.addByte(walkPathY[l1] - startY);
        }

        super.clientStream.sendPacket();
        mouseClickXStep = -24;
        mouseClickXX = super.mouseX;
        mouseClickXY = super.mouseY;
        return true;
    }

    public final String getParameter(String s) {
        if (link.mainapp != null)
            return link.mainapp.getParameter(s);
        else
            return super.getParameter(s);
    }

    private final void draw_minimap_entity(int x, int y, int c) {
        surface.set_pixel(x, y, c);
        surface.set_pixel(x - 1, y, c);
        surface.set_pixel(x + 1, y, c);
        surface.set_pixel(x, y - 1, c);
        surface.set_pixel(x, y + 1, c);
    }

    private final void updateBankItems() {
        bankItemCount = newBankItemCount;
        for (int i = 0; i < newBankItemCount; i++) {
            bank_items[i] = newBankItems[i];
            bankItemsCount[i] = newBankItemsCount[i];
        }

        for (int invidx = 0; invidx < inventory_items_count; invidx++) {
            if (bankItemCount >= bankItemsMax)
                break;
            int invid = inventory_item_id[invidx];
            boolean hasitemininv = false;
            for (int bankidx = 0; bankidx < bankItemCount; bankidx++) {
                if (bank_items[bankidx] != invid)
                    continue;
                hasitemininv = true;
                break;
            }

            if (!hasitemininv) {
                bank_items[bankItemCount] = invid;
                bankItemsCount[bankItemCount] = 0;
                bankItemCount++;
            }
        }

    }

    private final void draw_dialog_wild_warn() {
        int y = 97;
        surface.draw_box(86, 77, 340, 180, 0);
        surface.draw_box_edge(86, 77, 340, 180, 0xffffff);
        surface.drawstring_center("Warning! Proceed with caution", 256, y, 4, 0xff0000);
        y += 26;
        surface.drawstring_center("If you go much further north you will enter the", 256, y, 1, 0xffffff);
        y += 13;
        surface.drawstring_center("wilderness. This a very dangerous area where", 256, y, 1, 0xffffff);
        y += 13;
        surface.drawstring_center("other players can attack you!", 256, y, 1, 0xffffff);
        y += 22;
        surface.drawstring_center("The further north you go the more dangerous it", 256, y, 1, 0xffffff);
        y += 13;
        surface.drawstring_center("becomes, but the more treasure you will find.", 256, y, 1, 0xffffff);
        y += 22;
        surface.drawstring_center("In the wilderness an indicator at the bottom-right", 256, y, 1, 0xffffff);
        y += 13;
        surface.drawstring_center("of the screen will show the current level of danger", 256, y, 1, 0xffffff);
        y += 22;
        int j = 0xffffff;
        if (super.mouseY > y - 12 && super.mouseY <= y && super.mouseX > 181 && super.mouseX < 331)
            j = 0xff0000;
        surface.drawstring_center("Click here to close window", 256, y, 1, j);
        if (mouseButtonClick != 0) {
            if (super.mouseY > y - 12 && super.mouseY <= y && super.mouseX > 181 && super.mouseX < 331)
                show_ui_wild_warn = 2;
            if (super.mouseX < 86 || super.mouseX > 426 || super.mouseY < 77 || super.mouseY > 257)
                show_ui_wild_warn = 2;
            mouseButtonClick = 0;
        }
    }

    private final void drawAboveHeadStuff() {
        for (int msgidx = 0; msgidx < receivedMessagesCount; msgidx++) {
            int txtheight = surface.textHeightNumber(1);
            int x = receivedMessageX[msgidx];
            int y = receivedMessageY[msgidx];
            int mid = receivedMessageMidPoint[msgidx];
            int msgheight = receivedMessageHeight[msgidx];
            boolean flag = true;
            while (flag) {
                flag = false;
                for (int i4 = 0; i4 < msgidx; i4++)
                    if (y + msgheight > receivedMessageY[i4] - txtheight && y - txtheight < receivedMessageY[i4] + receivedMessageHeight[i4] && x - mid < receivedMessageX[i4] + receivedMessageMidPoint[i4] && x + mid > receivedMessageX[i4] - receivedMessageMidPoint[i4] && receivedMessageY[i4] - txtheight - msgheight < y) {
                        y = receivedMessageY[i4] - txtheight - msgheight;
                        flag = true;
                    }

            }
            receivedMessageY[msgidx] = y;
            surface.centrepara(receivedMessages[msgidx], x, y, 1, 0xffff00, 300);
        }

        for (int itemidx = 0; itemidx < itemsAboveHeadCount; itemidx++) {
            int x = itemAboveHeadX[itemidx];
            int y = itemAboveHeadY[itemidx];
            int scale = itemAboveHeadScale[itemidx];
            int id = itemAboveHeadID[itemidx];
            int scale_x = (39 * scale) / 100;
            int scale_y = (27 * scale) / 100;
            int k4 = y - scale_y;
            surface.sprite_aboveheadballoonthing(x - scale_x / 2, k4, scale_x, scale_y, sprite_media + 9, 85);
            int scale_x_clip = (36 * scale) / 100;
            int scale_y_clip = (24 * scale) / 100;
            surface.sprite_clipping(x - scale_x_clip / 2, (k4 + scale_y / 2) - scale_y_clip / 2, scale_x_clip, scale_y_clip, GameData.item_picture[id] + sprite_item, GameData.item_mask[id], 0, 0, false);
        }

        for (int j1 = 0; j1 < healthBarCount; j1++) {
            int i2 = healthBarX[j1];
            int l2 = healthBarY[j1];
            int k3 = healthBarMissing[j1];
            surface.draw_box_alpha(i2 - 15, l2 - 3, k3, 5, 65280, 192);
            surface.draw_box_alpha((i2 - 15) + k3, l2 - 3, 30 - k3, 5, 0xff0000, 192);
        }

    }

    protected final Socket createSocket(String addr, int port)
            throws IOException {
        if (link.mainapp != null) {
            Socket socket = link.opensocket(port);
            if (socket == null)
                throw new IOException();
            else
                return socket;
        }
        Socket socket1;
        if (getStartedAsApplet())
            socket1 = new Socket(InetAddress.getByName(getCodeBase().getHost()), port);
        else
            socket1 = new Socket(InetAddress.getByName(addr), port);
        socket1.setSoTimeout(30000);
        socket1.setTcpNoDelay(true);
        return socket1;
    }

    private final void walkToActionSource(int sx, int sy, int dx, int dy, boolean action) {
        walkToActionSource(sx, sy, dx, dy, dx, dy, false, action);
    }

    private final void createMessageTabPanel() {
        panelMessageTabs = new Panel(surface, 10);
        controlTextListChat = panelMessageTabs.add_text_list(5, 269, 502, 56, 1, 20, true);
        controlTextListAll = panelMessageTabs.add_text_list_input(7, 324, 498, 14, 1, 80, false, true);
        controlTextListQuest = panelMessageTabs.add_text_list(5, 269, 502, 56, 1, 20, true);
        controlTextListPrivate = panelMessageTabs.add_text_list(5, 269, 502, 56, 1, 20, true);
        panelMessageTabs.setFocus(controlTextListAll);
    }

    private final void dispose_and_collect() {
        try {
            if (surface != null) {
                surface.clear();
                surface.pixels = null;
                surface = null;
            }
            if (scene != null) {
                scene.clear();
                scene = null;
            }
            game_models = null;
            object_model = null;
            wall_object_model = null;
            player_server = null;
            players = null;
            npcs_server = null;
            npcs = null;
            local_player = null;
            if (world != null) {
                world.aGameModelArray599 = null;
                world.aGameModelArrayArray577 = null;
                world.aGameModelArrayArray583 = null;
                world.parent_model = null;
                world = null;
            }
            System.gc();
            return;
        } catch (Exception _ex) {
            return;
        }
    }

    private final void draw_ui() {
        if (logout_timeout != 0)
            draw_dialog_logout();
        else if (show_dialog_welcome)
            draw_dialog_welcome();
        else if (show_dialog_servermessage)
            draw_dialog_servermessage();
        else if (show_ui_wild_warn == 1)
            draw_dialog_wild_warn();
        else if (show_dialog_bank && combat_timeout == 0)
            draw_dialog_bank();
        else if (show_dialog_shop && combat_timeout == 0)
            draw_dialog_shop();
        else if (show_dialog_trade_confirm)
            draw_dialog_trade_confirm();
        else if (show_dialog_trade)
            draw_dialog_trade();
        else if (show_dialog_duel_confirm)
            draw_dialog_duel_confirm();
        else if (show_dialog_duel)
            draw_dialog_duel();
        else if (show_dialog_report_abuse_step == 1)
            draw_dialog_report_abuse();
        else if (show_dialog_report_abuse_step == 2)
            draw_dialog_report_abuse_input();
        else if (show_dialog_social_input != 0) {
            draw_dialog_social_input();
        } else {
            if (show_option_menu)
                draw_option_menu();
            if (local_player.animation_current == 8 || local_player.animation_current == 9)
                draw_dialog_combat_style();
            set_active_ui_tab();
            boolean nomenus = !show_option_menu && !show_right_click_menu;
            if (nomenus)
                menuItemsCount = 0;
            if (show_ui_tab == 0 && nomenus)
                create_right_click_menu();
            if (show_ui_tab == 1)
                draw_ui_tab_inventory(nomenus);
            if (show_ui_tab == 2)
                draw_ui_tab_minimap(nomenus);
            if (show_ui_tab == 3)
                draw_ui_tab_player_info(nomenus);
            if (show_ui_tab == 4)
                draw_ui_tab_magic(nomenus);
            if (show_ui_tab == 5)
                draw_ui_tab_social(nomenus);
            if (show_ui_tab == 6)
                draw_ui_tab_options(nomenus);
            if (!show_right_click_menu && !show_option_menu)
                create_top_mouse_menu();
            if (show_right_click_menu && !show_option_menu)
                draw_right_click_menu();
        }
        mouseButtonClick = 0;
    }

    private final void draw_dialog_trade() {
        if (mouseButtonClick != 0 && mouseButtonItemCountIncrement == 0)
            mouseButtonItemCountIncrement = 1;
        if (mouseButtonItemCountIncrement > 0) {
            int mouse_x = super.mouseX - 22;
            int mouse_y = super.mouseY - 36;
            if (mouse_x >= 0 && mouse_y >= 0 && mouse_x < 468 && mouse_y < 262) {
                if (mouse_x > 216 && mouse_y > 30 && mouse_x < 462 && mouse_y < 235) {
                    int slot = (mouse_x - 217) / 49 + ((mouse_y - 31) / 34) * 5;
                    if (slot >= 0 && slot < inventory_items_count) {
                        boolean send_update = false;
                        int item_count_add = 0;
                        int item_type = inventory_item_id[slot];
                        for (int item_index = 0; item_index < trade_items_count; item_index++)
                            if (trade_items[item_index] == item_type)
                                if (GameData.item_stackable[item_type] == 0) {
                                    for (int i4 = 0; i4 < mouseButtonItemCountIncrement; i4++) {
                                        if (trade_item_count[item_index] < inventory_item_stack_count[slot])
                                            trade_item_count[item_index]++;
                                        send_update = true;
                                    }

                                } else {
                                    item_count_add++;
                                }

                        if (getInventoryCount(item_type) <= item_count_add)
                            send_update = true;
                        if (GameData.item_special[item_type] == 1) { // quest items? or just tagged as 'special'
                            show_message("This object cannot be traded with other players", 3);
                            send_update = true;
                        }
                        if (!send_update && trade_items_count < 12) {
                            trade_items[trade_items_count] = item_type;
                            trade_item_count[trade_items_count] = 1;
                            trade_items_count++;
                            send_update = true;
                        }
                        if (send_update) {
                            super.clientStream.newPacket(46);
                            super.clientStream.addByte(trade_items_count);
                            for (int j4 = 0; j4 < trade_items_count; j4++) {
                                super.clientStream.addShort(trade_items[j4]);
                                super.clientStream.addInt(trade_item_count[j4]);
                            }

                            super.clientStream.sendPacket();
                            trade_recipient_accepted = false;
                            trade_accepted = false;
                        }
                    }
                }
                if (mouse_x > 8 && mouse_y > 30 && mouse_x < 205 && mouse_y < 133) {
                    int item_index = (mouse_x - 9) / 49 + ((mouse_y - 31) / 34) * 4;
                    if (item_index >= 0 && item_index < trade_items_count) {
                        int item_type = trade_items[item_index];
                        for (int i2 = 0; i2 < mouseButtonItemCountIncrement; i2++) {
                            if (GameData.item_stackable[item_type] == 0 && trade_item_count[item_index] > 1) {
                                trade_item_count[item_index]--;
                                continue;
                            }
                            trade_items_count--;
                            mouseButtonDownTime = 0;
                            for (int l2 = item_index; l2 < trade_items_count; l2++) {
                                trade_items[l2] = trade_items[l2 + 1];
                                trade_item_count[l2] = trade_item_count[l2 + 1];
                            }

                            break;
                        }

                        super.clientStream.newPacket(46);
                        super.clientStream.addByte(trade_items_count);
                        for (int i3 = 0; i3 < trade_items_count; i3++) {
                            super.clientStream.addShort(trade_items[i3]);
                            super.clientStream.addInt(trade_item_count[i3]);
                        }

                        super.clientStream.sendPacket();
                        trade_recipient_accepted = false;
                        trade_accepted = false;
                    }
                }
                if (mouse_x >= 217 && mouse_y >= 238 && mouse_x <= 286 && mouse_y <= 259) {
                    trade_accepted = true;
                    super.clientStream.newPacket(55);
                    super.clientStream.sendPacket();
                }
                if (mouse_x >= 394 && mouse_y >= 238 && mouse_x < 463 && mouse_y < 259) {
                    show_dialog_trade = false;
                    super.clientStream.newPacket(230);
                    super.clientStream.sendPacket();
                }
            } else if (mouseButtonClick != 0) {
                show_dialog_trade = false;
                super.clientStream.newPacket(230);
                super.clientStream.sendPacket();
            }
            mouseButtonClick = 0;
            mouseButtonItemCountIncrement = 0;
        }
        if (!show_dialog_trade)
            return;
        byte dialog_x = 22;
        byte dialog_y = 36;
        surface.draw_box(dialog_x, dialog_y, 468, 12, 192);
        surface.draw_box_alpha(dialog_x, dialog_y + 12, 468, 18, 0x989898, 160);
        surface.draw_box_alpha(dialog_x, dialog_y + 30, 8, 248, 0x989898, 160);
        surface.draw_box_alpha(dialog_x + 205, dialog_y + 30, 11, 248, 0x989898, 160);
        surface.draw_box_alpha(dialog_x + 462, dialog_y + 30, 6, 248, 0x989898, 160);
        surface.draw_box_alpha(dialog_x + 8, dialog_y + 133, 197, 22, 0x989898, 160);
        surface.draw_box_alpha(dialog_x + 8, dialog_y + 258, 197, 20, 0x989898, 160);
        surface.draw_box_alpha(dialog_x + 216, dialog_y + 235, 246, 43, 0x989898, 160);
        surface.draw_box_alpha(dialog_x + 8, dialog_y + 30, 197, 103, 0xd0d0d0, 160);
        surface.draw_box_alpha(dialog_x + 8, dialog_y + 155, 197, 103, 0xd0d0d0, 160);
        surface.draw_box_alpha(dialog_x + 216, dialog_y + 30, 246, 205, 0xd0d0d0, 160);
        for (int j2 = 0; j2 < 4; j2++)
            surface.draw_line_horiz(dialog_x + 8, dialog_y + 30 + j2 * 34, 197, 0);

        for (int j3 = 0; j3 < 4; j3++)
            surface.draw_line_horiz(dialog_x + 8, dialog_y + 155 + j3 * 34, 197, 0);

        for (int l3 = 0; l3 < 7; l3++)
            surface.draw_line_horiz(dialog_x + 216, dialog_y + 30 + l3 * 34, 246, 0);

        for (int k4 = 0; k4 < 6; k4++) {
            if (k4 < 5)
                surface.draw_line_vert(dialog_x + 8 + k4 * 49, dialog_y + 30, 103, 0);
            if (k4 < 5)
                surface.draw_line_vert(dialog_x + 8 + k4 * 49, dialog_y + 155, 103, 0);
            surface.draw_line_vert(dialog_x + 216 + k4 * 49, dialog_y + 30, 205, 0);
        }

        surface.drawstring("Trading with: " + trade_recipient_name, dialog_x + 1, dialog_y + 10, 1, 0xffffff);
        surface.drawstring("Your Offer", dialog_x + 9, dialog_y + 27, 4, 0xffffff);
        surface.drawstring("Opponent's Offer", dialog_x + 9, dialog_y + 152, 4, 0xffffff);
        surface.drawstring("Your Inventory", dialog_x + 216, dialog_y + 27, 4, 0xffffff);
        if (!trade_accepted)
            surface.draw_picture(dialog_x + 217, dialog_y + 238, sprite_media + 25);
        surface.draw_picture(dialog_x + 394, dialog_y + 238, sprite_media + 26);
        if (trade_recipient_accepted) {
            surface.drawstring_center("Other player", dialog_x + 341, dialog_y + 246, 1, 0xffffff);
            surface.drawstring_center("has accepted", dialog_x + 341, dialog_y + 256, 1, 0xffffff);
        }
        if (trade_accepted) {
            surface.drawstring_center("Waiting for", dialog_x + 217 + 35, dialog_y + 246, 1, 0xffffff);
            surface.drawstring_center("other player", dialog_x + 217 + 35, dialog_y + 256, 1, 0xffffff);
        }
        for (int item_index = 0; item_index < inventory_items_count; item_index++) {
            int slot_x = 217 + dialog_x + (item_index % 5) * 49;
            int slot_y = 31 + dialog_y + (item_index / 5) * 34;
            surface.sprite_clipping(slot_x, slot_y, 48, 32, sprite_item + GameData.item_picture[inventory_item_id[item_index]], GameData.item_mask[inventory_item_id[item_index]], 0, 0, false);
            if (GameData.item_stackable[inventory_item_id[item_index]] == 0)
                surface.drawstring(String.valueOf(inventory_item_stack_count[item_index]), slot_x + 1, slot_y + 10, 1, 0xffff00);
        }

        for (int item_index = 0; item_index < trade_items_count; item_index++) {
            int slot_x = 9 + dialog_x + (item_index % 4) * 49;
            int slot_y = 31 + dialog_y + (item_index / 4) * 34;
            surface.sprite_clipping(slot_x, slot_y, 48, 32, sprite_item + GameData.item_picture[trade_items[item_index]], GameData.item_mask[trade_items[item_index]], 0, 0, false);
            if (GameData.item_stackable[trade_items[item_index]] == 0)
                surface.drawstring(String.valueOf(trade_item_count[item_index]), slot_x + 1, slot_y + 10, 1, 0xffff00);
            if (super.mouseX > slot_x && super.mouseX < slot_x + 48 && super.mouseY > slot_y && super.mouseY < slot_y + 32)
                surface.drawstring(GameData.item_name[trade_items[item_index]] + ": @whi@" + GameData.item_description[trade_items[item_index]], dialog_x + 8, dialog_y + 273, 1, 0xffff00);
        }

        for (int item_index = 0; item_index < trade_recipient_items_count; item_index++) {
            int slot_x = 9 + dialog_x + (item_index % 4) * 49;
            int slot_y = 156 + dialog_y + (item_index / 4) * 34;
            surface.sprite_clipping(slot_x, slot_y, 48, 32, sprite_item + GameData.item_picture[trade_recipient_items[item_index]], GameData.item_mask[trade_recipient_items[item_index]], 0, 0, false);
            if (GameData.item_stackable[trade_recipient_items[item_index]] == 0)
                surface.drawstring(String.valueOf(trade_recipient_item_count[item_index]), slot_x + 1, slot_y + 10, 1, 0xffff00);
            if (super.mouseX > slot_x && super.mouseX < slot_x + 48 && super.mouseY > slot_y && super.mouseY < slot_y + 32)
                surface.drawstring(GameData.item_name[trade_recipient_items[item_index]] + ": @whi@" + GameData.item_description[trade_recipient_items[item_index]], dialog_x + 8, dialog_y + 273, 1, 0xffff00);
        }

    }

    protected final void resetGame() {
        systemUpdate = 0;
        combatStyle = 0;
        logout_timeout = 0;
        loginScreen = 0;
        loggedIn = 1;
        resetPMText();
        surface.black_screen();
        surface.draw(graphics, 0, 0);
        for (int i = 0; i < object_count; i++) {
            scene.free_model(object_model[i]);
            world.remove_object(object_x[i], object_y[i], object_id[i]);
        }

        for (int j = 0; j < wall_object_count; j++) {
            scene.free_model(wall_object_model[j]);
            world.remove_wall_object(wall_object_x[j], wall_object_y[j], wall_object_direction[j], wall_object_id[j]);
        }

        object_count = 0;
        wall_object_count = 0;
        ground_item_count = 0;
        player_count = 0;
        for (int k = 0; k < 4000; k++)
            player_server[k] = null;

        for (int l = 0; l < 500; l++)
            players[l] = null;

        npc_count = 0;
        for (int i1 = 0; i1 < 5000; i1++)
            npcs_server[i1] = null;

        for (int j1 = 0; j1 < 500; j1++)
            npcs[j1] = null;

        for (int k1 = 0; k1 < 50; k1++)
            prayerOn[k1] = false;

        mouseButtonClick = 0;
        super.lastMouseButtonDown = 0;
        super.mouseButtonDown = 0;
        show_dialog_shop = false;
        show_dialog_bank = false;
        isSleeping = false;
        super.friendListCount = 0;
    }

    private final void draw_ui_tab_social(boolean nomenus) {
        int ui_x = surface.width2 - 199;
        int ui_y = 36;
        surface.draw_picture(ui_x - 49, 3, sprite_media + 5);
        int ui_width = 196;//'\304';
        int ui_height = 182;//'\266';
        int l;
        int k = l = Surface.rgb2long(160, 160, 160);
        if (ui_tab_social_sub_tab == 0)
            k = Surface.rgb2long(220, 220, 220);
        else
            l = Surface.rgb2long(220, 220, 220);
        surface.draw_box_alpha(ui_x, ui_y, ui_width / 2, 24, k, 128);
        surface.draw_box_alpha(ui_x + ui_width / 2, ui_y, ui_width / 2, 24, l, 128);
        surface.draw_box_alpha(ui_x, ui_y + 24, ui_width, ui_height - 24, Surface.rgb2long(220, 220, 220), 128);
        surface.draw_line_horiz(ui_x, ui_y + 24, ui_width, 0);
        surface.draw_line_vert(ui_x + ui_width / 2, ui_y, 24, 0);
        surface.draw_line_horiz(ui_x, (ui_y + ui_height) - 16, ui_width, 0);
        surface.drawstring_center("Friends", ui_x + ui_width / 4, ui_y + 16, 4, 0);
        surface.drawstring_center("Ignore", ui_x + ui_width / 4 + ui_width / 2, ui_y + 16, 4, 0);
        panel_social.clear_list(control_socialpanel);
        if (ui_tab_social_sub_tab == 0) {
            for (int i1 = 0; i1 < super.friendListCount; i1++) {
                String s;
                if (super.friendListOnline[i1] == 255)
                    s = "@gre@";
                else if (super.friendListOnline[i1] > 0)
                    s = "@yel@";
                else
                    s = "@red@";
                panel_social.add_list_entry(control_socialpanel, i1, s + Utility.hash2username(super.friendListHashes[i1]) + "~439~@whi@Remove         WWWWWWWWWW");
            }

        }
        if (ui_tab_social_sub_tab == 1) {
            for (int j1 = 0; j1 < super.ignore_list_count; j1++)
                panel_social.add_list_entry(control_socialpanel, j1, "@yel@" + Utility.hash2username(super.ignore_list[j1]) + "~439~@whi@Remove         WWWWWWWWWW");

        }
        panel_social.drawPanel();
        if (ui_tab_social_sub_tab == 0) {
            int k1 = panel_social.get_list_entry_index(control_socialpanel);
            if (k1 >= 0 && super.mouseX < 489) {
                if (super.mouseX > 429)
                    surface.drawstring_center("Click to remove " + Utility.hash2username(super.friendListHashes[k1]), ui_x + ui_width / 2, ui_y + 35, 1, 0xffffff);
                else if (super.friendListOnline[k1] == 255)
                    surface.drawstring_center("Click to message " + Utility.hash2username(super.friendListHashes[k1]), ui_x + ui_width / 2, ui_y + 35, 1, 0xffffff);
                else if (super.friendListOnline[k1] > 0) {
                    if (super.friendListOnline[k1] < 200)
                        surface.drawstring_center(Utility.hash2username(super.friendListHashes[k1]) + " is on world " + (super.friendListOnline[k1] - 9), ui_x + ui_width / 2, ui_y + 35, 1, 0xffffff);
                    else
                        surface.drawstring_center(Utility.hash2username(super.friendListHashes[k1]) + " is on classic " + (super.friendListOnline[k1] - 219), ui_x + ui_width / 2, ui_y + 35, 1, 0xffffff);
                } else {
                    surface.drawstring_center(Utility.hash2username(super.friendListHashes[k1]) + " is offline", ui_x + ui_width / 2, ui_y + 35, 1, 0xffffff);
                }
            } else {
                surface.drawstring_center("Click a name to send a message", ui_x + ui_width / 2, ui_y + 35, 1, 0xffffff);
            }
            int colour;
            if (super.mouseX > ui_x && super.mouseX < ui_x + ui_width && super.mouseY > (ui_y + ui_height) - 16 && super.mouseY < ui_y + ui_height)
                colour = 0xffff00;
            else
                colour = 0xffffff;
            surface.drawstring_center("Click here to add a friend", ui_x + ui_width / 2, (ui_y + ui_height) - 3, 1, colour);
        }
        if (ui_tab_social_sub_tab == 1) {
            int l1 = panel_social.get_list_entry_index(control_socialpanel);
            if (l1 >= 0 && super.mouseX < 489 && super.mouseX > 429) {
                if (super.mouseX > 429)
                    surface.drawstring_center("Click to remove " + Utility.hash2username(super.ignore_list[l1]), ui_x + ui_width / 2, ui_y + 35, 1, 0xffffff);
            } else {
                surface.drawstring_center("Blocking messages from:", ui_x + ui_width / 2, ui_y + 35, 1, 0xffffff);
            }
            int l2;
            if (super.mouseX > ui_x && super.mouseX < ui_x + ui_width && super.mouseY > (ui_y + ui_height) - 16 && super.mouseY < ui_y + ui_height)
                l2 = 0xffff00;
            else
                l2 = 0xffffff;
            surface.drawstring_center("Click here to add a name", ui_x + ui_width / 2, (ui_y + ui_height) - 3, 1, l2);
        }
        if (!nomenus)
            return;
        ui_x = super.mouseX - (surface.width2 - 199);
        ui_y = super.mouseY - 36;
        if (ui_x >= 0 && ui_y >= 0 && ui_x < 196 && ui_y < 182) {
            panel_social.handle_mouse(ui_x + (surface.width2 - 199), ui_y + 36, super.lastMouseButtonDown, super.mouseButtonDown);
            if (ui_y <= 24 && mouseButtonClick == 1)
                if (ui_x < 98 && ui_tab_social_sub_tab == 1) {
                    ui_tab_social_sub_tab = 0;
                    panel_social.reset_list_props(control_socialpanel);
                } else if (ui_x > 98 && ui_tab_social_sub_tab == 0) {
                    ui_tab_social_sub_tab = 1;
                    panel_social.reset_list_props(control_socialpanel);
                }
            if (mouseButtonClick == 1 && ui_tab_social_sub_tab == 0) {
                int i2 = panel_social.get_list_entry_index(control_socialpanel);
                if (i2 >= 0 && super.mouseX < 489)
                    if (super.mouseX > 429)
                        friendRemove(super.friendListHashes[i2]);
                    else if (super.friendListOnline[i2] != 0) {
                        show_dialog_social_input = 2;
                        privateMessageTarget = super.friendListHashes[i2];
                        super.input_pm_current = "";
                        super.input_pm_final = "";
                    }
            }
            if (mouseButtonClick == 1 && ui_tab_social_sub_tab == 1) {
                int j2 = panel_social.get_list_entry_index(control_socialpanel);
                if (j2 >= 0 && super.mouseX < 489 && super.mouseX > 429)
                    ignoreRemove(super.ignore_list[j2]);
            }
            if (ui_y > 166 && mouseButtonClick == 1 && ui_tab_social_sub_tab == 0) {
                show_dialog_social_input = 1;
                super.input_text_current = "";
                super.input_text_final = "";
            }
            if (ui_y > 166 && mouseButtonClick == 1 && ui_tab_social_sub_tab == 1) {
                show_dialog_social_input = 3;
                super.input_text_current = "";
                super.input_text_final = "";
            }
            mouseButtonClick = 0;
        }
    }

    protected final void handleKeyPress(int i) {
        if (loggedIn == 0) {
            if (loginScreen == 0)
                panel_login_welcome.keyPress(i);
            if (loginScreen == 1)
                panel_login_newuser.keyPress(i);
            if (loginScreen == 2)
                panel_login_existinguser.keyPress(i);
        }
        if (loggedIn == 1) {
            if (showAppearanceChange) {
                panelAppearance.keyPress(i);
                return;
            }
            if (show_dialog_social_input == 0 && show_dialog_report_abuse_step == 0 && !isSleeping)
                panelMessageTabs.keyPress(i);
        }
    }

    private final void sendLogout() {
        if (loggedIn == 0)
            return;
        if (combat_timeout > 450) {
            show_message("@cya@You can't logout during combat!", 3);
            return;
        }
        if (combat_timeout > 0) {
            show_message("@cya@You can't logout for 10 seconds after combat", 3);
            return;
        } else {
            super.clientStream.newPacket(102);
            super.clientStream.sendPacket();
            logout_timeout = 1000;
            return;
        }
    }

    private final Character create_player(int server_index, int x, int y, int anim) {
        if (player_server[server_index] == null) {
            player_server[server_index] = new Character();
            player_server[server_index].server_index = server_index;
            player_server[server_index].server_id = 0;
        }
        Character character = player_server[server_index];
        boolean flag = false;
        for (int i1 = 0; i1 < known_player_count; i1++) {
            if (known_players[i1].server_index != server_index)
                continue;
            flag = true;
            break;
        }

        if (flag) {
            character.animation_next = anim;
            int j1 = character.waypoint_current;
            if (x != character.waypoints_x[j1] || y != character.waypoints_y[j1]) {
                character.waypoint_current = j1 = (j1 + 1) % 10;
                character.waypoints_x[j1] = x;
                character.waypoints_y[j1] = y;
            }
        } else {
            character.server_index = server_index;
            character.moving_step = 0;
            character.waypoint_current = 0;
            character.waypoints_x[0] = character.currentX = x;
            character.waypoints_y[0] = character.currentY = y;
            character.animation_next = character.animation_current = anim;
            character.stepCount = 0;
        }
        players[player_count++] = character;
        return character;
    }

    private final void draw_dialog_social_input() {
        if (mouseButtonClick != 0) {
            mouseButtonClick = 0;
            if (show_dialog_social_input == 1 && (super.mouseX < 106 || super.mouseY < 145 || super.mouseX > 406 || super.mouseY > 215)) {
                show_dialog_social_input = 0;
                return;
            }
            if (show_dialog_social_input == 2 && (super.mouseX < 6 || super.mouseY < 145 || super.mouseX > 506 || super.mouseY > 215)) {
                show_dialog_social_input = 0;
                return;
            }
            if (show_dialog_social_input == 3 && (super.mouseX < 106 || super.mouseY < 145 || super.mouseX > 406 || super.mouseY > 215)) {
                show_dialog_social_input = 0;
                return;
            }
            if (super.mouseX > 236 && super.mouseX < 276 && super.mouseY > 193 && super.mouseY < 213) {
                show_dialog_social_input = 0;
                return;
            }
        }
        int i = 145;
        if (show_dialog_social_input == 1) {
            surface.draw_box(106, i, 300, 70, 0);
            surface.draw_box_edge(106, i, 300, 70, 0xffffff);
            i += 20;
            surface.drawstring_center("Enter name to add to friends list", 256, i, 4, 0xffffff);
            i += 20;
            surface.drawstring_center(super.input_text_current + "*", 256, i, 4, 0xffffff);
            if (super.input_text_final.length() > 0) {
                String s = super.input_text_final.trim();
                super.input_text_current = "";
                super.input_text_final = "";
                show_dialog_social_input = 0;
                if (s.length() > 0 && Utility.username2hash(s) != local_player.hash)
                    friendAdd(s);
            }
        }
        if (show_dialog_social_input == 2) {
            surface.draw_box(6, i, 500, 70, 0);
            surface.draw_box_edge(6, i, 500, 70, 0xffffff);
            i += 20;
            surface.drawstring_center("Enter message to send to " + Utility.hash2username(privateMessageTarget), 256, i, 4, 0xffffff);
            i += 20;
            surface.drawstring_center(super.input_pm_current + "*", 256, i, 4, 0xffffff);
            if (super.input_pm_final.length() > 0) {
                String s1 = super.input_pm_final;
                super.input_pm_current = "";
                super.input_pm_final = "";
                show_dialog_social_input = 0;
                int k = ChatMessage.scramble(s1);
                sendPrivateMessage(privateMessageTarget, ChatMessage.scrambledbytes, k);
                s1 = ChatMessage.descramble(ChatMessage.scrambledbytes, 0, k);
                s1 = WordFilter.filter(s1);
                showServerMessage("@pri@You tell " + Utility.hash2username(privateMessageTarget) + ": " + s1);
            }
        }
        if (show_dialog_social_input == 3) {
            surface.draw_box(106, i, 300, 70, 0);
            surface.draw_box_edge(106, i, 300, 70, 0xffffff);
            i += 20;
            surface.drawstring_center("Enter name to add to ignore list", 256, i, 4, 0xffffff);
            i += 20;
            surface.drawstring_center(super.input_text_current + "*", 256, i, 4, 0xffffff);
            if (super.input_text_final.length() > 0) {
                String s2 = super.input_text_final.trim();
                super.input_text_current = "";
                super.input_text_final = "";
                show_dialog_social_input = 0;
                if (s2.length() > 0 && Utility.username2hash(s2) != local_player.hash)
                    ignoreAdd(s2);
            }
        }
        int j = 0xffffff;
        if (super.mouseX > 236 && super.mouseX < 276 && super.mouseY > 193 && super.mouseY < 213)
            j = 0xffff00;
        surface.drawstring_center("Cancel", 256, 208, 1, j);
    }

    private final void create_appearance_panel() {
        panelAppearance = new Panel(surface, 100);
        panelAppearance.add_text(256, 10, "Please design Your Character", 4, true);
        int i = 140;
        int j = 34;
        i += 116;
        j -= 10;
        panelAppearance.add_text(i - 55, j + 110, "Front", 3, true);
        panelAppearance.add_text(i, j + 110, "Side", 3, true);
        panelAppearance.add_text(i + 55, j + 110, "Back", 3, true);
        byte byte0 = 54;
        j += 145;
        panelAppearance.add_box_rounded(i - byte0, j, 53, 41);
        panelAppearance.add_text(i - byte0, j - 8, "Head", 1, true);
        panelAppearance.add_text(i - byte0, j + 8, "Type", 1, true);
        panelAppearance.add_image(i - byte0 - 40, j, Panel.baseSpriteStart + 7);
        controlButtonAppearanceHead1 = panelAppearance.add_button(i - byte0 - 40, j, 20, 20);
        panelAppearance.add_image((i - byte0) + 40, j, Panel.baseSpriteStart + 6);
        controlButtonAppearanceHead2 = panelAppearance.add_button((i - byte0) + 40, j, 20, 20);
        panelAppearance.add_box_rounded(i + byte0, j, 53, 41);
        panelAppearance.add_text(i + byte0, j - 8, "Hair", 1, true);
        panelAppearance.add_text(i + byte0, j + 8, "Color", 1, true);
        panelAppearance.add_image((i + byte0) - 40, j, Panel.baseSpriteStart + 7);
        controlButtonAppearanceHair1 = panelAppearance.add_button((i + byte0) - 40, j, 20, 20);
        panelAppearance.add_image(i + byte0 + 40, j, Panel.baseSpriteStart + 6);
        controlButtonAppearanceHair2 = panelAppearance.add_button(i + byte0 + 40, j, 20, 20);
        j += 50;
        panelAppearance.add_box_rounded(i - byte0, j, 53, 41);
        panelAppearance.add_text(i - byte0, j, "Gender", 1, true);
        panelAppearance.add_image(i - byte0 - 40, j, Panel.baseSpriteStart + 7);
        controlButtonAppearanceGender1 = panelAppearance.add_button(i - byte0 - 40, j, 20, 20);
        panelAppearance.add_image((i - byte0) + 40, j, Panel.baseSpriteStart + 6);
        controlButtonAppearanceGender2 = panelAppearance.add_button((i - byte0) + 40, j, 20, 20);
        panelAppearance.add_box_rounded(i + byte0, j, 53, 41);
        panelAppearance.add_text(i + byte0, j - 8, "Top", 1, true);
        panelAppearance.add_text(i + byte0, j + 8, "Color", 1, true);
        panelAppearance.add_image((i + byte0) - 40, j, Panel.baseSpriteStart + 7);
        controlButtonAppearanceTop1 = panelAppearance.add_button((i + byte0) - 40, j, 20, 20);
        panelAppearance.add_image(i + byte0 + 40, j, Panel.baseSpriteStart + 6);
        controlButtonAppearanceTop2 = panelAppearance.add_button(i + byte0 + 40, j, 20, 20);
        j += 50;
        panelAppearance.add_box_rounded(i - byte0, j, 53, 41);
        panelAppearance.add_text(i - byte0, j - 8, "Skin", 1, true);
        panelAppearance.add_text(i - byte0, j + 8, "Color", 1, true);
        panelAppearance.add_image(i - byte0 - 40, j, Panel.baseSpriteStart + 7);
        controlButtonAppearanceSkin1 = panelAppearance.add_button(i - byte0 - 40, j, 20, 20);
        panelAppearance.add_image((i - byte0) + 40, j, Panel.baseSpriteStart + 6);
        controlButtonAppearanceSkin2 = panelAppearance.add_button((i - byte0) + 40, j, 20, 20);
        panelAppearance.add_box_rounded(i + byte0, j, 53, 41);
        panelAppearance.add_text(i + byte0, j - 8, "Bottom", 1, true);
        panelAppearance.add_text(i + byte0, j + 8, "Color", 1, true);
        panelAppearance.add_image((i + byte0) - 40, j, Panel.baseSpriteStart + 7);
        controlButtonAppearanceBottom1 = panelAppearance.add_button((i + byte0) - 40, j, 20, 20);
        panelAppearance.add_image(i + byte0 + 40, j, Panel.baseSpriteStart + 6);
        controlButtonAppearanceBottom2 = panelAppearance.add_button(i + byte0 + 40, j, 20, 20);
        j += 82;
        j -= 35;
        panelAppearance.add_button_background(i, j, 200, 30);
        panelAppearance.add_text(i, j, "Accept", 4, false);
        controlButtonAppearanceAccept = panelAppearance.add_button(i, j, 200, 30);
    }

    private final void resetPMText() {
        super.input_pm_current = "";
        super.input_pm_final = "";
    }

    private final void draw_dialog_welcome() {
        int i = 65;
        if (welcomeRecoverySetDays != 201)
            i += 60;
        if (welcomeUnreadMessages > 0)
            i += 60;
        if (welcomeLastLoggedInIP != 0)
            i += 45;
        int j = 167 - i / 2;
        surface.draw_box(56, 167 - i / 2, 400, i, 0);
        surface.draw_box_edge(56, 167 - i / 2, 400, i, 0xffffff);
        j += 20;
        surface.drawstring_center("Welcome to RuneScape " + loginUser, 256, j, 4, 0xffff00);
        j += 30;
        String s;
        if (welcomeLastLoggedInDays == 0)
            s = "earlier today";
        else if (welcomeLastLoggedInDays == 1)
            s = "yesterday";
        else
            s = welcomeLastLoggedInDays + " days ago";
        if (welcomeLastLoggedInIP != 0) {
            surface.drawstring_center("You last logged in " + s, 256, j, 1, 0xffffff);
            j += 15;
            if (welcomeLastLoggedInHost == null)
                welcomeLastLoggedInHost = getHostnameIP(welcomeLastLoggedInIP);
            surface.drawstring_center("from: " + welcomeLastLoggedInHost, 256, j, 1, 0xffffff);
            j += 15;
            j += 15;
        }
        if (welcomeUnreadMessages > 0) {
            int k = 0xffffff;
            surface.drawstring_center("Jagex staff will NEVER email you. We use the", 256, j, 1, k);
            j += 15;
            surface.drawstring_center("message-centre on this website instead.", 256, j, 1, k);
            j += 15;
            if (welcomeUnreadMessages == 1)
                surface.drawstring_center("You have @yel@0@whi@ unread messages in your message-centre", 256, j, 1, 0xffffff);
            else
                surface.drawstring_center("You have @gre@" + (welcomeUnreadMessages - 1) + " unread messages @whi@in your message-centre", 256, j, 1, 0xffffff);
            j += 15;
            j += 15;
        }
        if (welcomeRecoverySetDays != 201) // this is an odd way of storing recovery day settings
        {
            if (welcomeRecoverySetDays == 200) // and this
            {
                surface.drawstring_center("You have not yet set any password recovery questions.", 256, j, 1, 0xff8000);
                j += 15;
                surface.drawstring_center("We strongly recommend you do so now to secure your account.", 256, j, 1, 0xff8000);
                j += 15;
                surface.drawstring_center("Do this from the 'account management' area on our front webpage", 256, j, 1, 0xff8000);
                j += 15;
            } else {
                String s1;
                if (welcomeRecoverySetDays == 0)
                    s1 = "Earlier today";
                else if (welcomeRecoverySetDays == 1)
                    s1 = "Yesterday";
                else
                    s1 = welcomeRecoverySetDays + " days ago";
                surface.drawstring_center(s1 + " you changed your recovery questions", 256, j, 1, 0xff8000);
                j += 15;
                surface.drawstring_center("If you do not remember making this change then cancel it immediately", 256, j, 1, 0xff8000);
                j += 15;
                surface.drawstring_center("Do this from the 'account management' area on our front webpage", 256, j, 1, 0xff8000);
                j += 15;
            }
            j += 15;
        }
        int l = 0xffffff;
        if (super.mouseY > j - 12 && super.mouseY <= j && super.mouseX > 106 && super.mouseX < 406)
            l = 0xff0000;
        surface.drawstring_center("Click here to close window", 256, j, 1, l);
        if (mouseButtonClick == 1) {
            if (l == 0xff0000)
                show_dialog_welcome = false;
            if ((super.mouseX < 86 || super.mouseX > 426) && (super.mouseY < 167 - i / 2 || super.mouseY > 167 + i / 2))
                show_dialog_welcome = false;
        }
        mouseButtonClick = 0;
    }

    private final void drawAppearancePanelCharacterSprites() {
        surface.interlace = false;
        surface.black_screen();
        panelAppearance.drawPanel();
        int i = 140;
        int j = 50;
        i += 116;
        j -= 25;
        surface.sprite_clipping(i - 32 - 55, j, 64, 102, GameData.animation_number[appearance2Colour], characterTopBottomColours[appearanceBottomColour]);
        surface.sprite_clipping(i - 32 - 55, j, 64, 102, GameData.animation_number[appearanceBodyGender], characterTopBottomColours[appearanceTopColour], characterSkinColours[appearanceSkinColour], 0, false);
        surface.sprite_clipping(i - 32 - 55, j, 64, 102, GameData.animation_number[appearanceHeadType], characterHairColours[appearanceHairColour], characterSkinColours[appearanceSkinColour], 0, false);
        surface.sprite_clipping(i - 32, j, 64, 102, GameData.animation_number[appearance2Colour] + 6, characterTopBottomColours[appearanceBottomColour]);
        surface.sprite_clipping(i - 32, j, 64, 102, GameData.animation_number[appearanceBodyGender] + 6, characterTopBottomColours[appearanceTopColour], characterSkinColours[appearanceSkinColour], 0, false);
        surface.sprite_clipping(i - 32, j, 64, 102, GameData.animation_number[appearanceHeadType] + 6, characterHairColours[appearanceHairColour], characterSkinColours[appearanceSkinColour], 0, false);
        surface.sprite_clipping((i - 32) + 55, j, 64, 102, GameData.animation_number[appearance2Colour] + 12, characterTopBottomColours[appearanceBottomColour]);
        surface.sprite_clipping((i - 32) + 55, j, 64, 102, GameData.animation_number[appearanceBodyGender] + 12, characterTopBottomColours[appearanceTopColour], characterSkinColours[appearanceSkinColour], 0, false);
        surface.sprite_clipping((i - 32) + 55, j, 64, 102, GameData.animation_number[appearanceHeadType] + 12, characterHairColours[appearanceHairColour], characterSkinColours[appearanceSkinColour], 0, false);
        surface.draw_picture(0, game_height, sprite_media + 22);
        surface.draw(graphics, 0, 0);
    }

    public final URL getDocumentBase() {
        if (link.mainapp != null)
            return link.mainapp.getDocumentBase();
        else
            return super.getDocumentBase();
    }

    public final Graphics getGraphics() {
        if (GameApplet.gameFrameReference != null)
            return GameApplet.gameFrameReference.getGraphics();
        if (link.mainapp != null)
            return link.mainapp.getGraphics();
        else
            return super.getGraphics();
    }

    public final URL getCodeBase() {
        if (link.mainapp != null)
            return link.mainapp.getCodeBase();
        else
            return super.getCodeBase();
    }

    final void draw_item(int i, int j, int k, int l, int i1, int j1, int k1) {
        int l1 = GameData.item_picture[i1] + sprite_item;
        int i2 = GameData.item_mask[i1];
        surface.sprite_clipping(i, j, k, l, l1, i2, 0, 0, false);
    }

    private final void handle_game_input() {
        if (systemUpdate > 1)
            systemUpdate--;
        checkConnection();
        if (logout_timeout > 0)
            logout_timeout--;
        if (super.lastMouseAction > 4500 && combat_timeout == 0 && logout_timeout == 0) {
            super.lastMouseAction -= 500;
            sendLogout();
            return;
        }
        if (local_player.animation_current == 8 || local_player.animation_current == 9)
            combat_timeout = 500;
        if (combat_timeout > 0)
            combat_timeout--;
        if (showAppearanceChange) {
            handleAppearancePanelControls();
            return;
        }
        for (int i = 0; i < player_count; i++) {
            Character character = players[i];
            int k = (character.waypoint_current + 1) % 10;
            if (character.moving_step != k) {
                int i1 = -1;
                int l2 = character.moving_step;
                int j4;
                if (l2 < k)
                    j4 = k - l2;
                else
                    j4 = (10 + k) - l2;
                int j5 = 4;
                if (j4 > 2)
                    j5 = (j4 - 1) * 4;
                if (character.waypoints_x[l2] - character.currentX > magicLoc * 3 || character.waypoints_y[l2] - character.currentY > magicLoc * 3 || character.waypoints_x[l2] - character.currentX < -magicLoc * 3 || character.waypoints_y[l2] - character.currentY < -magicLoc * 3 || j4 > 8) {
                    character.currentX = character.waypoints_x[l2];
                    character.currentY = character.waypoints_y[l2];
                } else {
                    if (character.currentX < character.waypoints_x[l2]) {
                        character.currentX += j5;
                        character.stepCount++;
                        i1 = 2;
                    } else if (character.currentX > character.waypoints_x[l2]) {
                        character.currentX -= j5;
                        character.stepCount++;
                        i1 = 6;
                    }
                    if (character.currentX - character.waypoints_x[l2] < j5 && character.currentX - character.waypoints_x[l2] > -j5)
                        character.currentX = character.waypoints_x[l2];
                    if (character.currentY < character.waypoints_y[l2]) {
                        character.currentY += j5;
                        character.stepCount++;
                        if (i1 == -1)
                            i1 = 4;
                        else if (i1 == 2)
                            i1 = 3;
                        else
                            i1 = 5;
                    } else if (character.currentY > character.waypoints_y[l2]) {
                        character.currentY -= j5;
                        character.stepCount++;
                        if (i1 == -1)
                            i1 = 0;
                        else if (i1 == 2)
                            i1 = 1;
                        else
                            i1 = 7;
                    }
                    if (character.currentY - character.waypoints_y[l2] < j5 && character.currentY - character.waypoints_y[l2] > -j5)
                        character.currentY = character.waypoints_y[l2];
                }
                if (i1 != -1)
                    character.animation_current = i1;
                if (character.currentX == character.waypoints_x[l2] && character.currentY == character.waypoints_y[l2])
                    character.moving_step = (l2 + 1) % 10;
            } else {
                character.animation_current = character.animation_next;
            }
            if (character.message_timeout > 0)
                character.message_timeout--;
            if (character.bubble_timeout > 0)
                character.bubble_timeout--;
            if (character.combat_timer > 0)
                character.combat_timer--;
            if (death_screen_timeout > 0) {
                death_screen_timeout--;
                if (death_screen_timeout == 0)
                    show_message("You have been granted another life. Be more careful this time!", 3);
                if (death_screen_timeout == 0)
                    show_message("You retain your skills. Your objects land where you died", 3);
            }
        }

        for (int j = 0; j < npc_count; j++) {
            Character character_1 = npcs[j];
            int j1 = (character_1.waypoint_current + 1) % 10;
            if (character_1.moving_step != j1) {
                int i3 = -1;
                int k4 = character_1.moving_step;
                int k5;
                if (k4 < j1)
                    k5 = j1 - k4;
                else
                    k5 = (10 + j1) - k4;
                int l5 = 4;
                if (k5 > 2)
                    l5 = (k5 - 1) * 4;
                if (character_1.waypoints_x[k4] - character_1.currentX > magicLoc * 3 || character_1.waypoints_y[k4] - character_1.currentY > magicLoc * 3 || character_1.waypoints_x[k4] - character_1.currentX < -magicLoc * 3 || character_1.waypoints_y[k4] - character_1.currentY < -magicLoc * 3 || k5 > 8) {
                    character_1.currentX = character_1.waypoints_x[k4];
                    character_1.currentY = character_1.waypoints_y[k4];
                } else {
                    if (character_1.currentX < character_1.waypoints_x[k4]) {
                        character_1.currentX += l5;
                        character_1.stepCount++;
                        i3 = 2;
                    } else if (character_1.currentX > character_1.waypoints_x[k4]) {
                        character_1.currentX -= l5;
                        character_1.stepCount++;
                        i3 = 6;
                    }
                    if (character_1.currentX - character_1.waypoints_x[k4] < l5 && character_1.currentX - character_1.waypoints_x[k4] > -l5)
                        character_1.currentX = character_1.waypoints_x[k4];
                    if (character_1.currentY < character_1.waypoints_y[k4]) {
                        character_1.currentY += l5;
                        character_1.stepCount++;
                        if (i3 == -1)
                            i3 = 4;
                        else if (i3 == 2)
                            i3 = 3;
                        else
                            i3 = 5;
                    } else if (character_1.currentY > character_1.waypoints_y[k4]) {
                        character_1.currentY -= l5;
                        character_1.stepCount++;
                        if (i3 == -1)
                            i3 = 0;
                        else if (i3 == 2)
                            i3 = 1;
                        else
                            i3 = 7;
                    }
                    if (character_1.currentY - character_1.waypoints_y[k4] < l5 && character_1.currentY - character_1.waypoints_y[k4] > -l5)
                        character_1.currentY = character_1.waypoints_y[k4];
                }
                if (i3 != -1)
                    character_1.animation_current = i3;
                if (character_1.currentX == character_1.waypoints_x[k4] && character_1.currentY == character_1.waypoints_y[k4])
                    character_1.moving_step = (k4 + 1) % 10;
            } else {
                character_1.animation_current = character_1.animation_next;
                if (character_1.npc_id == 43)
                    character_1.stepCount++;
            }
            if (character_1.message_timeout > 0)
                character_1.message_timeout--;
            if (character_1.bubble_timeout > 0)
                character_1.bubble_timeout--;
            if (character_1.combat_timer > 0)
                character_1.combat_timer--;
        }

        if (show_ui_tab != 2) {
            if (Surface.anInt346 > 0)
                sleepWordDelayTimer++;
            if (Surface.anInt347 > 0)
                sleepWordDelayTimer = 0;
            Surface.anInt346 = 0;
            Surface.anInt347 = 0;
        }
        for (int l = 0; l < player_count; l++) {
            Character character = players[l];
            if (character.projectile_range > 0)
                character.projectile_range--;
        }

        if (cameraAutoAngleDebug) {
            if (cameraAutoRotatePlayerX - local_player.currentX < -500 || cameraAutoRotatePlayerX - local_player.currentX > 500 || cameraAutoRotatePlayerY - local_player.currentY < -500 || cameraAutoRotatePlayerY - local_player.currentY > 500) {
                cameraAutoRotatePlayerX = local_player.currentX;
                cameraAutoRotatePlayerY = local_player.currentY;
            }
        } else {
            if (cameraAutoRotatePlayerX - local_player.currentX < -500 || cameraAutoRotatePlayerX - local_player.currentX > 500 || cameraAutoRotatePlayerY - local_player.currentY < -500 || cameraAutoRotatePlayerY - local_player.currentY > 500) {
                cameraAutoRotatePlayerX = local_player.currentX;
                cameraAutoRotatePlayerY = local_player.currentY;
            }
            if (cameraAutoRotatePlayerX != local_player.currentX)
                cameraAutoRotatePlayerX += (local_player.currentX - cameraAutoRotatePlayerX) / (16 + (cameraZoom - 500) / 15);
            if (cameraAutoRotatePlayerY != local_player.currentY)
                cameraAutoRotatePlayerY += (local_player.currentY - cameraAutoRotatePlayerY) / (16 + (cameraZoom - 500) / 15);
            if (optionCameraModeAuto) {
                int k1 = camera_angle * 32;
                int j3 = k1 - cameraRotation;
                byte byte0 = 1;
                if (j3 != 0) {
                    anInt707++;
                    if (j3 > 128) {
                        byte0 = -1;
                        j3 = 256 - j3;
                    } else if (j3 > 0)
                        byte0 = 1;
                    else if (j3 < -128) {
                        byte0 = 1;
                        j3 = 256 + j3;
                    } else if (j3 < 0) {
                        byte0 = -1;
                        j3 = -j3;
                    }
                    cameraRotation += ((anInt707 * j3 + 255) / 256) * byte0;
                    cameraRotation &= 255;// 0xff;
                } else {
                    anInt707 = 0;
                }
            }
        }
        if (sleepWordDelayTimer > 20) {
            sleepWordDelay = false;
            sleepWordDelayTimer = 0;
        }
        if (isSleeping) {
            if (super.input_text_final.length() > 0)
                if (super.input_text_final.equalsIgnoreCase("::lostcon") && !appletMode)
                    super.clientStream.closeStream();
                else if (super.input_text_final.equalsIgnoreCase("::closecon") && !appletMode) {
                    closeConnection();
                } else {
                    super.clientStream.newPacket(45);
                    super.clientStream.addString(super.input_text_final);
                    if (!sleepWordDelay) {
                        super.clientStream.addByte(0);
                        sleepWordDelay = true;
                    }
                    super.clientStream.sendPacket();
                    super.input_text_current = "";
                    super.input_text_final = "";
                    sleepingStatusText = "Please wait...";
                }
            if (super.lastMouseButtonDown == 1 && super.mouseY > 275 && super.mouseY < 310 && super.mouseX > 56 && super.mouseX < 456) {
                super.clientStream.newPacket(45);
                super.clientStream.addString("-null-");
                if (!sleepWordDelay) {
                    super.clientStream.addByte(0);
                    sleepWordDelay = true;
                }
                super.clientStream.sendPacket();
                super.input_text_current = "";
                super.input_text_final = "";
                sleepingStatusText = "Please wait...";
            }
            super.lastMouseButtonDown = 0;
            return;
        }
        if (super.mouseY > game_height - 4) {
            if (super.mouseX > 15 && super.mouseX < 96 && super.lastMouseButtonDown == 1)
                messageTabSelected = 0;
            if (super.mouseX > 110 && super.mouseX < 194 && super.lastMouseButtonDown == 1) {
                messageTabSelected = 1;
                panelMessageTabs.control_flash_text[controlTextListChat] = 999999;//0xf423f;
            }
            if (super.mouseX > 215 && super.mouseX < 295 && super.lastMouseButtonDown == 1) {
                messageTabSelected = 2;
                panelMessageTabs.control_flash_text[controlTextListQuest] = 999999;//0xf423f;
            }
            if (super.mouseX > 315 && super.mouseX < 395 && super.lastMouseButtonDown == 1) {
                messageTabSelected = 3;
                panelMessageTabs.control_flash_text[controlTextListPrivate] = 999999;//0xf423f;
            }
            if (super.mouseX > 417 && super.mouseX < 497 && super.lastMouseButtonDown == 1) {
                show_dialog_report_abuse_step = 1;
                report_abuse_offence = 0;
                super.input_text_current = "";
                super.input_text_final = "";
            }
            super.lastMouseButtonDown = 0;
            super.mouseButtonDown = 0;
        }
        panelMessageTabs.handle_mouse(super.mouseX, super.mouseY, super.lastMouseButtonDown, super.mouseButtonDown);
        if (messageTabSelected > 0 && super.mouseX >= 494 && super.mouseY >= game_height - 66)
            super.lastMouseButtonDown = 0;
        if (panelMessageTabs.isClicked(controlTextListAll)) {
            String s = panelMessageTabs.getText(controlTextListAll);
            panelMessageTabs.updateText(controlTextListAll, "");
            if (s.startsWith("::")) {
                if (s.equalsIgnoreCase("::closecon") && !appletMode)
                    super.clientStream.closeStream();
                else if (s.equalsIgnoreCase("::logout") && !appletMode)
                    closeConnection();
                else if (s.equalsIgnoreCase("::lostcon") && !appletMode)
                    lostConnection();
                else
                    sendCommandString(s.substring(2));
            } else {
                int k3 = ChatMessage.scramble(s);
                sendChatMessage(ChatMessage.scrambledbytes, k3);
                s = ChatMessage.descramble(ChatMessage.scrambledbytes, 0, k3);
                s = WordFilter.filter(s);
                local_player.message_timeout = 150;
                local_player.message = s;
                show_message(local_player.name + ": " + s, 2);
            }
        }
        if (messageTabSelected == 0) {
            for (int l1 = 0; l1 < 5; l1++)
                if (messageHistoryTimeout[l1] > 0)
                    messageHistoryTimeout[l1]--;

        }
        if (death_screen_timeout != 0)
            super.lastMouseButtonDown = 0;
        if (show_dialog_trade || show_dialog_duel) {
            if (super.mouseButtonDown != 0)
                mouseButtonDownTime++;
            else
                mouseButtonDownTime = 0;
            if (mouseButtonDownTime > 600)
                mouseButtonItemCountIncrement += 5000;
            else if (mouseButtonDownTime > 450)
                mouseButtonItemCountIncrement += 500;
            else if (mouseButtonDownTime > 300)
                mouseButtonItemCountIncrement += 50;
            else if (mouseButtonDownTime > 150)
                mouseButtonItemCountIncrement += 5;
            else if (mouseButtonDownTime > 50)
                mouseButtonItemCountIncrement++;
            else if (mouseButtonDownTime > 20 && (mouseButtonDownTime & 5) == 0)
                mouseButtonItemCountIncrement++;
        } else {
            mouseButtonDownTime = 0;
            mouseButtonItemCountIncrement = 0;
        }
        if (super.lastMouseButtonDown == 1)
            mouseButtonClick = 1;
        else if (super.lastMouseButtonDown == 2)
            mouseButtonClick = 2;
        scene.set_mouse_loc(super.mouseX, super.mouseY);
        super.lastMouseButtonDown = 0;
        if (optionCameraModeAuto) {
            if (anInt707 == 0 || cameraAutoAngleDebug) {
                if (super.key_left) {
                    camera_angle = camera_angle + 1 & 7;
                    super.key_left = false;
                    if (!fogOfWar) {
                        if ((camera_angle & 1) == 0)
                            camera_angle = camera_angle + 1 & 7;
                        for (int i2 = 0; i2 < 8; i2++) {
                            if (is_valid_camera_angle(camera_angle))
                                break;
                            camera_angle = camera_angle + 1 & 7;
                        }

                    }
                }
                if (super.key_right) {
                    camera_angle = camera_angle + 7 & 7;
                    super.key_right = false;
                    if (!fogOfWar) {
                        if ((camera_angle & 1) == 0)
                            camera_angle = camera_angle + 7 & 7;
                        for (int j2 = 0; j2 < 8; j2++) {
                            if (is_valid_camera_angle(camera_angle))
                                break;
                            camera_angle = camera_angle + 7 & 7;
                        }

                    }
                }
            }
        } else if (super.key_left)
            cameraRotation = cameraRotation + 2 & 255;// 0xff;
        else if (super.key_right)
            cameraRotation = cameraRotation - 2 & 255;// 0xff;
        if (fogOfWar && cameraZoom > 550)
            cameraZoom -= 4;
        else if (!fogOfWar && cameraZoom < 750)
            cameraZoom += 4;
        if (mouseClickXStep > 0)
            mouseClickXStep--;
        else if (mouseClickXStep < 0)
            mouseClickXStep++;
        scene.method301(17);
        objectAnimationCount++;
        if (objectAnimationCount > 5) {
            objectAnimationCount = 0;
            objectAnimationNumberFireLightningSpell = (objectAnimationNumberFireLightningSpell + 1) % 3;
            objectAnimationNumberTorch = (objectAnimationNumberTorch + 1) % 4;
            objectAnimationNumberClaw = (objectAnimationNumberClaw + 1) % 5;
        }
        for (int k2 = 0; k2 < object_count; k2++) {
            int l3 = object_x[k2];
            int l4 = object_y[k2];
            if (l3 >= 0 && l4 >= 0 && l3 < 96 && l4 < 96 && object_id[k2] == 74)
                object_model[k2].rotate(1, 0, 0);
        }

        for (int i4 = 0; i4 < teleport_bubble_count; i4++) {
            teleport_bubble_time[i4]++;
            if (teleport_bubble_time[i4] > 50) {
                teleport_bubble_count--;
                for (int i5 = i4; i5 < teleport_bubble_count; i5++) {
                    teleport_bubble_x[i5] = teleport_bubble_x[i5 + 1];
                    teleport_bubble_y[i5] = teleport_bubble_y[i5 + 1];
                    teleport_bubble_time[i5] = teleport_bubble_time[i5 + 1];
                    teleport_bubble_type[i5] = teleport_bubble_type[i5 + 1];
                }

            }
        }

    }

    private final void render_login_screen_viewports() {
        int rh = 0;
        byte rx = 50;
        byte ry = 50;
        world.load_section(rx * 48 + 23, ry * 48 + 23, rh);
        world.add_models(game_models);
        int x = 9728;// '\u2600'
        int y = 6400;// '\u1900'
        int zoom = 1100;// '\u044C'
        int rotation = 888;// '\u0378'
        scene.clip_far_3d = 4100;
        scene.clip_far_2d = 4100;
        scene.fog_z_falloff = 1;
        scene.fog_z_distance = 4000;
        surface.black_screen();
        scene.set_camera(x, -world.get_elevation(x, y), y, 912, rotation, 0, zoom * 2);
        scene.endscene();
        surface.fade2black();
        surface.fade2black();
        /*surface.draw_box(0, 0, gameWidth, 6, 0);
        for (int j = 6; j >= 1; j--)
            surface.draw_line_alpha(0, j, 0, j, gameWidth, 8);

        surface.draw_box(0, 194, 512, 20, 0);
        for (int k = 6; k >= 1; k--)
            surface.draw_line_alpha(0, k, 0, 194 - k, gameWidth, 8); */

        surface.draw_picture(game_width / 2 - surface.image_width[sprite_media + 10] / 2, 15, sprite_media + 10); // runescape logo
        surface.render(sprite_logo, 0, 0, game_width, game_height);
        surface.draw_world(sprite_logo);
        x = 9216;// '\u2400';
        y = 9216;// '\u2400';
        zoom = 1100;// '\u044C';
        rotation = 888;// '\u0378';
        scene.clip_far_3d = 4100;
        scene.clip_far_2d = 4100;
        scene.fog_z_falloff = 1;
        scene.fog_z_distance = 4000;
        surface.black_screen();
        scene.set_camera(x, -world.get_elevation(x, y), y, 912, rotation, 0, zoom * 2);
        scene.endscene();
        surface.fade2black();
        surface.fade2black();
        /*surface.draw_box(0, 0, gameWidth, 6, 0);
        for (int l = 6; l >= 1; l--)
            surface.draw_line_alpha(0, l, 0, l, gameWidth, 8);

        surface.draw_box(0, 194, gameWidth, 20, 0);
        for (int i1 = 6; i1 >= 1; i1--)
            surface.draw_line_alpha(0, i1, 0, 194 - i1, gameWidth, 8);*/

        surface.draw_picture(game_width / 2 - surface.image_width[sprite_media + 10] / 2, 15, sprite_media + 10);
        surface.render(sprite_logo + 1, 0, 0, game_width, game_height);  // h was 200
        surface.draw_world(sprite_logo + 1);

        // todo: wtf is this again?
        for (int j1 = 0; j1 < 64; j1++) {
            scene.free_model(world.aGameModelArrayArray583[0][j1]);
            scene.free_model(world.aGameModelArrayArray577[1][j1]);
            scene.free_model(world.aGameModelArrayArray583[1][j1]);
            scene.free_model(world.aGameModelArrayArray577[2][j1]);
            scene.free_model(world.aGameModelArrayArray583[2][j1]);
        }

        x = 11136;// '\u2B80';
        y = 10368;// '\u2880';
        zoom = 500;// '\u01F4';
        rotation = 376;// '\u0178';
        scene.clip_far_3d = 4100;
        scene.clip_far_2d = 4100;
        scene.fog_z_falloff = 1;
        scene.fog_z_distance = 4000;
        surface.black_screen();
        scene.set_camera(x, -world.get_elevation(x, y), y, 912, rotation, 0, zoom * 2);
        scene.endscene();
        surface.fade2black();
        surface.fade2black();
        /*surface.draw_box(0, 0, gameWidth, 6, 0);
        for (int k1 = 6; k1 >= 1; k1--)
            surface.draw_line_alpha(0, k1, 0, k1, gameWidth, 8);

        surface.draw_box(0, 194, gameWidth, 20, 0);
        for (int l1 = 6; l1 >= 1; l1--)
            surface.draw_line_alpha(0, l1, 0, 194, gameWidth, 8);  */

        surface.draw_picture(game_width / 2 - surface.image_width[sprite_media + 10] / 2, 15, sprite_media + 10);
        surface.render(sprite_media + 10, 0, 0, game_width, game_height);
        surface.draw_world(sprite_media + 10);
    }

    private final void create_login_panels() {
        panel_login_welcome = new Panel(surface, 50);
        int y = 40;
        int x = game_width / 2;
        if (!members) {
            panel_login_welcome.add_text(x, 200 + y, "Click on an option", 5, true);
            panel_login_welcome.add_button_background(x - 100, 240 + y, 120, 35);
            panel_login_welcome.add_button_background(x + 100, 240 + y, 120, 35);
            panel_login_welcome.add_text(x - 100, 240 + y, "New User", 5, false);
            panel_login_welcome.add_text(x + 100, 240 + y, "Existing User", 5, false);
            control_welcome_newuser = panel_login_welcome.add_button(x - 100, 240 + y, 120, 35);
            control_welcome_existinguser = panel_login_welcome.add_button(x + 100, 240 + y, 120, 35);
        } else {
            panel_login_welcome.add_text(x, 200 + y, "Welcome to RuneScape", 4, true);
            panel_login_welcome.add_text(x, 215 + y, "You need a member account to use this server", 4, true);
            panel_login_welcome.add_button_background(x, 250 + y, 200, 35);
            panel_login_welcome.add_text(x, 250 + y, "Click here to login", 5, false);
            control_welcome_existinguser = panel_login_welcome.add_button(x, 250 + y, 200, 35);
        }
        panel_login_newuser = new Panel(surface, 50);
        y = 230;
        if (referid == 0) {
            panel_login_newuser.add_text(x, y + 8, "To create an account please go back to the", 4, true);
            y += 20;
            panel_login_newuser.add_text(x, y + 8, "www.runescape.com front page, and choose 'create account'", 4, true);
        } else if (referid == 1) {
            panel_login_newuser.add_text(x, y + 8, "To create an account please click on the", 4, true);
            y += 20;
            panel_login_newuser.add_text(x, y + 8, "'create account' link below the game window", 4, true);
        } else {
            panel_login_newuser.add_text(x, y + 8, "To create an account please go back to the", 4, true);
            y += 20;
            panel_login_newuser.add_text(x, y + 8, "runescape front webpage and choose 'create account'", 4, true);
        }
        y += 30;
        panel_login_newuser.add_button_background(x, y + 17, 150, 34);
        panel_login_newuser.add_text(x, y + 17, "Ok", 5, false);
        control_login_new_ok = panel_login_newuser.add_button(x, y + 17, 150, 34);
        panel_login_existinguser = new Panel(surface, 50);
        y = 230;
        anInt761 = panel_login_existinguser.add_text(x, y - 10, "Please enter your username and password", 4, true);
        y += 28;
        panel_login_existinguser.add_button_background(x - 116, y, 200, 40);
        panel_login_existinguser.add_text(x - 116, y - 10, "Username:", 4, false);
        control_login_user = panel_login_existinguser.add_text_input(x - 116, y + 10, 200, 40, 4, 12, false, false);
        y += 47;
        panel_login_existinguser.add_button_background(x - 66, y, 200, 40);
        panel_login_existinguser.add_text(x - 66, y - 10, "Password:", 4, false);
        control_login_pass = panel_login_existinguser.add_text_input(x - 66, y + 10, 200, 40, 4, 20, true, false);
        y -= 55;
        panel_login_existinguser.add_button_background(x + 154, y, 120, 25);
        panel_login_existinguser.add_text(x + 154, y, "Ok", 4, false);
        control_login_ok = panel_login_existinguser.add_button(x + 154, y, 120, 25);
        y += 30;
        panel_login_existinguser.add_button_background(x + 154, y, 120, 25);
        panel_login_existinguser.add_text(x + 154, y, "Cancel", 4, false);
        control_login_cancel = panel_login_existinguser.add_button(x + 154, y, 120, 25);
        y += 30;
        panel_login_existinguser.setFocus(control_login_user);

        //create_player(int server_index, int x, int y, int anim)
        create_player(0, 50, 50, 0);
    }

    private final void draw_ui_tab_inventory(boolean nomenus) {
        int ui_x = surface.width2 - 248;
        surface.draw_picture(ui_x, 3, sprite_media + 1);
        for (int item_index = 0; item_index < inventory_max_item_count; item_index++) {
            int slot_x = ui_x + (item_index % 5) * 49;
            int slot_y = 36 + (item_index / 5) * 34;
            if (item_index < inventory_items_count && inventory_equipped[item_index] == 1)
                surface.draw_box_alpha(slot_x, slot_y, 49, 34, 0xff0000, 128);
            else
                surface.draw_box_alpha(slot_x, slot_y, 49, 34, Surface.rgb2long(181, 181, 181), 128);
            if (item_index < inventory_items_count) {
                surface.sprite_clipping(slot_x, slot_y, 48, 32, sprite_item + GameData.item_picture[inventory_item_id[item_index]], GameData.item_mask[inventory_item_id[item_index]], 0, 0, false);
                if (GameData.item_stackable[inventory_item_id[item_index]] == 0)
                    surface.drawstring(String.valueOf(inventory_item_stack_count[item_index]), slot_x + 1, slot_y + 10, 1, 0xffff00);
            }
        }

        for (int rows = 1; rows <= 4; rows++)
            surface.draw_line_vert(ui_x + rows * 49, 36, (inventory_max_item_count / 5) * 34, 0);

        for (int cols = 1; cols <= inventory_max_item_count / 5 - 1; cols++)
            surface.draw_line_horiz(ui_x, 36 + cols * 34, 245, 0);

        if (!nomenus)
            return;
        int mouse_x = super.mouseX - (surface.width2 - 248);
        int mouse_y = super.mouseY - 36;
        if (mouse_x >= 0 && mouse_y >= 0 && mouse_x < 248 && mouse_y < (inventory_max_item_count / 5) * 34) {
            int item_index = mouse_x / 49 + (mouse_y / 34) * 5;
            if (item_index < inventory_items_count) {
                int i2 = inventory_item_id[item_index];
                if (selectedSpell >= 0) {
                    if (GameData.spell_type[selectedSpell] == 3) {
                        menuItemText1[menuItemsCount] = "Cast " + GameData.spell_name[selectedSpell] + " on";
                        menuItemText2[menuItemsCount] = "@lre@" + GameData.item_name[i2];
                        menuItemID[menuItemsCount] = 600;
                        menuSourceType[menuItemsCount] = item_index;
                        menuSourceIndex[menuItemsCount] = selectedSpell;
                        menuItemsCount++;
                        return;
                    }
                } else {
                    if (selectedItemInventoryIndex >= 0) {
                        menuItemText1[menuItemsCount] = "Use " + selectedItemName + " with";
                        menuItemText2[menuItemsCount] = "@lre@" + GameData.item_name[i2];
                        menuItemID[menuItemsCount] = 610;
                        menuSourceType[menuItemsCount] = item_index;
                        menuSourceIndex[menuItemsCount] = selectedItemInventoryIndex;
                        menuItemsCount++;
                        return;
                    }
                    if (inventory_equipped[item_index] == 1) {
                        menuItemText1[menuItemsCount] = "Remove";
                        menuItemText2[menuItemsCount] = "@lre@" + GameData.item_name[i2];
                        menuItemID[menuItemsCount] = 620;
                        menuSourceType[menuItemsCount] = item_index;
                        menuItemsCount++;
                    } else if (GameData.anIntArray126[i2] != 0) {
                        if ((GameData.anIntArray126[i2] & 24) != 0)// 0x18
                            menuItemText1[menuItemsCount] = "Wield";
                        else
                            menuItemText1[menuItemsCount] = "Wear";
                        menuItemText2[menuItemsCount] = "@lre@" + GameData.item_name[i2];
                        menuItemID[menuItemsCount] = 630;
                        menuSourceType[menuItemsCount] = item_index;
                        menuItemsCount++;
                    }
                    if (!GameData.item_command[i2].equals("")) {
                        menuItemText1[menuItemsCount] = GameData.item_command[i2];
                        menuItemText2[menuItemsCount] = "@lre@" + GameData.item_name[i2];
                        menuItemID[menuItemsCount] = 640;
                        menuSourceType[menuItemsCount] = item_index;
                        menuItemsCount++;
                    }
                    menuItemText1[menuItemsCount] = "Use";
                    menuItemText2[menuItemsCount] = "@lre@" + GameData.item_name[i2];
                    menuItemID[menuItemsCount] = 650;
                    menuSourceType[menuItemsCount] = item_index;
                    menuItemsCount++;
                    menuItemText1[menuItemsCount] = "Drop";
                    menuItemText2[menuItemsCount] = "@lre@" + GameData.item_name[i2];
                    menuItemID[menuItemsCount] = 660;
                    menuSourceType[menuItemsCount] = item_index;
                    menuItemsCount++;
                    menuItemText1[menuItemsCount] = "Examine";
                    menuItemText2[menuItemsCount] = "@lre@" + GameData.item_name[i2];
                    menuItemID[menuItemsCount] = 3600;
                    menuSourceType[menuItemsCount] = i2;
                    menuItemsCount++;
                }
            }
        }
    }

    private final void autorotate_camera() {
        if ((camera_angle & 1) == 1 && is_valid_camera_angle(camera_angle))
            return;
        if ((camera_angle & 1) == 0 && is_valid_camera_angle(camera_angle)) {
            if (is_valid_camera_angle(camera_angle + 1 & 7)) {
                camera_angle = camera_angle + 1 & 7;
                return;
            }
            if (is_valid_camera_angle(camera_angle + 7 & 7))
                camera_angle = camera_angle + 7 & 7;
            return;
        }
        int ai[] = {
                1, -1, 2, -2, 3, -3, 4
        };
        for (int i = 0; i < 7; i++) {
            if (!is_valid_camera_angle(camera_angle + ai[i] + 8 & 7))
                continue;
            camera_angle = camera_angle + ai[i] + 8 & 7;
            break;
        }

        if ((camera_angle & 1) == 0 && is_valid_camera_angle(camera_angle)) {
            if (is_valid_camera_angle(camera_angle + 1 & 7)) {
                camera_angle = camera_angle + 1 & 7;
                return;
            }
            if (is_valid_camera_angle(camera_angle + 7 & 7))
                camera_angle = camera_angle + 7 & 7;
        }
    }

    private final void draw_right_click_menu() {
        if (mouseButtonClick != 0) {
            for (int i = 0; i < menuItemsCount; i++) {
                int k = menuX + 2;
                int i1 = menuY + 27 + i * 15;
                if (super.mouseX <= k - 2 || super.mouseY <= i1 - 12 || super.mouseY >= i1 + 4 || super.mouseX >= (k - 3) + menuWidth)
                    continue;
                menu_item_click(menuIndices[i]);
                break;
            }

            mouseButtonClick = 0;
            show_right_click_menu = false;
            return;
        }
        if (super.mouseX < menuX - 10 || super.mouseY < menuY - 10 || super.mouseX > menuX + menuWidth + 10 || super.mouseY > menuY + menuHeight + 10) {
            show_right_click_menu = false;
            return;
        }
        surface.draw_box_alpha(menuX, menuY, menuWidth, menuHeight, 0xd0d0d0, 160);
        surface.drawstring("Choose option", menuX + 2, menuY + 12, 1, 65535);
        for (int j = 0; j < menuItemsCount; j++) {
            int l = menuX + 2;
            int j1 = menuY + 27 + j * 15;
            int k1 = 0xffffff;
            if (super.mouseX > l - 2 && super.mouseY > j1 - 12 && super.mouseY < j1 + 4 && super.mouseX < (l - 3) + menuWidth)
                k1 = 0xffff00;
            surface.drawstring(menuItemText1[menuIndices[j]] + " " + menuItemText2[menuIndices[j]], l, j1, 1, k1);
        }

    }

    private final void draw_ui_tab_minimap(boolean nomenus) {
        int ui_x = surface.width2 - 199;
        int ui_width = 156;// '\234';
        int ui_height = 152;// '\230';
        surface.draw_picture(ui_x - 49, 3, sprite_media + 2);
        ui_x += 40;
        surface.draw_box(ui_x, 36, ui_width, ui_height, 0);
        surface.set_bounds(ui_x, 36, ui_x + ui_width, 36 + ui_height);
        int k = 192 + minimap_random_2;
        int i1 = cameraRotation + minimap_random_1 & 255;//0xff;
        int k1 = ((local_player.currentX - 6040) * 3 * k) / 2048;
        int i3 = ((local_player.currentY - 6040) * 3 * k) / 2048;
        int k4 = Scene.anIntArray384[1024 - i1 * 4 & 0x3ff];
        int i5 = Scene.anIntArray384[(1024 - i1 * 4 & 0x3ff) + 1024];
        int k5 = i3 * k4 + k1 * i5 >> 18;
        i3 = i3 * i5 - k1 * k4 >> 18;
        k1 = k5;
        surface.draw_minimap_sprite((ui_x + ui_width / 2) - k1, 36 + ui_height / 2 + i3, sprite_media - 1, i1 + 64 & 255, k);// landscape
        for (int i = 0; i < object_count; i++) {
            int l1 = (((object_x[i] * magicLoc + 64) - local_player.currentX) * 3 * k) / 2048;
            int j3 = (((object_y[i] * magicLoc + 64) - local_player.currentY) * 3 * k) / 2048;
            int l5 = j3 * k4 + l1 * i5 >> 18;
            j3 = j3 * i5 - l1 * k4 >> 18;
            l1 = l5;
            draw_minimap_entity(ui_x + ui_width / 2 + l1, (36 + ui_height / 2) - j3, 65535);
        }

        for (int j7 = 0; j7 < ground_item_count; j7++) {
            int i2 = (((ground_item_x[j7] * magicLoc + 64) - local_player.currentX) * 3 * k) / 2048;
            int k3 = (((ground_item_y[j7] * magicLoc + 64) - local_player.currentY) * 3 * k) / 2048;
            int i6 = k3 * k4 + i2 * i5 >> 18;
            k3 = k3 * i5 - i2 * k4 >> 18;
            i2 = i6;
            draw_minimap_entity(ui_x + ui_width / 2 + i2, (36 + ui_height / 2) - k3, 0xff0000);
        }

        for (int k7 = 0; k7 < npc_count; k7++) {
            Character character = npcs[k7];
            int j2 = ((character.currentX - local_player.currentX) * 3 * k) / 2048;
            int l3 = ((character.currentY - local_player.currentY) * 3 * k) / 2048;
            int j6 = l3 * k4 + j2 * i5 >> 18;
            l3 = l3 * i5 - j2 * k4 >> 18;
            j2 = j6;
            draw_minimap_entity(ui_x + ui_width / 2 + j2, (36 + ui_height / 2) - l3, 0xffff00);
        }

        for (int l7 = 0; l7 < player_count; l7++) {
            Character character_1 = players[l7];
            int k2 = ((character_1.currentX - local_player.currentX) * 3 * k) / 2048;
            int i4 = ((character_1.currentY - local_player.currentY) * 3 * k) / 2048;
            int k6 = i4 * k4 + k2 * i5 >> 18;
            i4 = i4 * i5 - k2 * k4 >> 18;
            k2 = k6;
            int j8 = 0xffffff;
            for (int k8 = 0; k8 < super.friendListCount; k8++) {
                if (character_1.hash != super.friendListHashes[k8] || super.friendListOnline[k8] != 255)
                    continue;
                j8 = 65280;
                break;
            }

            draw_minimap_entity(ui_x + ui_width / 2 + k2, (36 + ui_height / 2) - i4, j8);
        }

        surface.draw_circle(ui_x + ui_width / 2, 36 + ui_height / 2, 2, 0xffffff, 255);
        surface.draw_minimap_sprite(ui_x + 19, 55, sprite_media + 24, cameraRotation + 128 & 255, 128);// compass
        surface.set_bounds(0, 0, game_width, game_height + 12);
        if (!nomenus)
            return;
        int mouse_x = super.mouseX - (surface.width2 - 199);
        int mouse_y = super.mouseY - 36;
        if (mouse_x >= 40 && mouse_y >= 0 && mouse_x < 196 && mouse_y < 152) {
            int c1 = 156;// '\234';
            int c3 = 152;// '\230';
            int l = 192 + minimap_random_2;
            int j1 = cameraRotation + minimap_random_1 & 255;// 0xff
            int j = surface.width2 - 199;
            j += 40;
            int dx = ((super.mouseX - (j + c1 / 2)) * 16384) / (3 * l);
            int dy = ((super.mouseY - (36 + c3 / 2)) * 16384) / (3 * l);
            int l4 = Scene.anIntArray384[1024 - j1 * 4 & 1023];// 0x3ff
            int j5 = Scene.anIntArray384[(1024 - j1 * 4 & 1023) + 1024];// 0x3ff
            int l6 = dy * l4 + dx * j5 >> 15;
            dy = dy * j5 - dx * l4 >> 15;
            dx = l6;
            dx += local_player.currentX;
            dy = local_player.currentY - dy;
            if (mouseButtonClick == 1)
                walkToActionSource(region_x, region_y, dx / 128, dy / 128, false);
            mouseButtonClick = 0;
        }
    }

    private final void draw_dialog_trade_confirm() {
        byte dialog_x = 22;
        byte dialog_y = 36;
        surface.draw_box(dialog_x, dialog_y, 468, 16, 192);
        surface.draw_box_alpha(dialog_x, dialog_y + 16, 468, 246, 0x989898, 160);
        surface.drawstring_center("Please confirm your trade with @yel@" + Utility.hash2username(trade_recipient_confirm_hash), dialog_x + 234, dialog_y + 12, 1, 0xffffff);
        surface.drawstring_center("You are about to give:", dialog_x + 117, dialog_y + 30, 1, 0xffff00);
        for (int j = 0; j < trade_confirm_items_count; j++) {
            String s = GameData.item_name[trade_confirm_items[j]];
            if (GameData.item_stackable[trade_confirm_items[j]] == 0)
                s = s + " x " + formatNumber(trade_confirm_item_count[j]);
            surface.drawstring_center(s, dialog_x + 117, dialog_y + 42 + j * 12, 1, 0xffffff);
        }

        if (trade_confirm_items_count == 0)
            surface.drawstring_center("Nothing!", dialog_x + 117, dialog_y + 42, 1, 0xffffff);
        surface.drawstring_center("In return you will receive:", dialog_x + 351, dialog_y + 30, 1, 0xffff00);
        for (int k = 0; k < trade_recipient_confirm_items_count; k++) {
            String s1 = GameData.item_name[trade_recipient_confirm_items[k]];
            if (GameData.item_stackable[trade_recipient_confirm_items[k]] == 0)
                s1 = s1 + " x " + formatNumber(trade_recipient_confirm_item_count[k]);
            surface.drawstring_center(s1, dialog_x + 351, dialog_y + 42 + k * 12, 1, 0xffffff);
        }

        if (trade_recipient_confirm_items_count == 0)
            surface.drawstring_center("Nothing!", dialog_x + 351, dialog_y + 42, 1, 0xffffff);
        surface.drawstring_center("Are you sure you want to do this?", dialog_x + 234, dialog_y + 200, 4, 65535);
        surface.drawstring_center("There is NO WAY to reverse a trade if you change your mind.", dialog_x + 234, dialog_y + 215, 1, 0xffffff);
        surface.drawstring_center("Remember that not all players are trustworthy", dialog_x + 234, dialog_y + 230, 1, 0xffffff);
        if (!trade_confirm_accepted) {
            surface.draw_picture((dialog_x + 118) - 35, dialog_y + 238, sprite_media + 25);
            surface.draw_picture((dialog_x + 352) - 35, dialog_y + 238, sprite_media + 26);
        } else {
            surface.drawstring_center("Waiting for other player...", dialog_x + 234, dialog_y + 250, 1, 0xffff00);
        }
        if (mouseButtonClick == 1) {
            if (super.mouseX < dialog_x || super.mouseY < dialog_y || super.mouseX > dialog_x + 468 || super.mouseY > dialog_y + 262) {
                show_dialog_trade_confirm = false;
                super.clientStream.newPacket(230);
                super.clientStream.sendPacket();
            }
            if (super.mouseX >= (dialog_x + 118) - 35 && super.mouseX <= dialog_x + 118 + 70 && super.mouseY >= dialog_y + 238 && super.mouseY <= dialog_y + 238 + 21) {
                trade_confirm_accepted = true;
                super.clientStream.newPacket(104);
                super.clientStream.sendPacket();
            }
            if (super.mouseX >= (dialog_x + 352) - 35 && super.mouseX <= dialog_x + 353 + 70 && super.mouseY >= dialog_y + 238 && super.mouseY <= dialog_y + 238 + 21) {
                show_dialog_trade_confirm = false;
                super.clientStream.newPacket(230);
                super.clientStream.sendPacket();
            }
            mouseButtonClick = 0;
        }
    }

    private final void set_active_ui_tab() {
        if (show_ui_tab == 0 && super.mouseX >= surface.width2 - 35 && super.mouseY >= 3 && super.mouseX < surface.width2 - 3 && super.mouseY < 35)
            show_ui_tab = 1;
        if (show_ui_tab == 0 && super.mouseX >= surface.width2 - 35 - 33 && super.mouseY >= 3 && super.mouseX < surface.width2 - 3 - 33 && super.mouseY < 35) {
            show_ui_tab = 2;
            minimap_random_1 = (int) (Math.random() * 13D) - 6;
            minimap_random_2 = (int) (Math.random() * 23D) - 11;
        }
        if (show_ui_tab == 0 && super.mouseX >= surface.width2 - 35 - 66 && super.mouseY >= 3 && super.mouseX < surface.width2 - 3 - 66 && super.mouseY < 35)
            show_ui_tab = 3;
        if (show_ui_tab == 0 && super.mouseX >= surface.width2 - 35 - 99 && super.mouseY >= 3 && super.mouseX < surface.width2 - 3 - 99 && super.mouseY < 35)
            show_ui_tab = 4;
        if (show_ui_tab == 0 && super.mouseX >= surface.width2 - 35 - 132 && super.mouseY >= 3 && super.mouseX < surface.width2 - 3 - 132 && super.mouseY < 35)
            show_ui_tab = 5;
        if (show_ui_tab == 0 && super.mouseX >= surface.width2 - 35 - 165 && super.mouseY >= 3 && super.mouseX < surface.width2 - 3 - 165 && super.mouseY < 35)
            show_ui_tab = 6;
        if (show_ui_tab != 0 && super.mouseX >= surface.width2 - 35 && super.mouseY >= 3 && super.mouseX < surface.width2 - 3 && super.mouseY < 26)
            show_ui_tab = 1;
        if (show_ui_tab != 0 && show_ui_tab != 2 && super.mouseX >= surface.width2 - 35 - 33 && super.mouseY >= 3 && super.mouseX < surface.width2 - 3 - 33 && super.mouseY < 26) {
            show_ui_tab = 2;
            minimap_random_1 = (int) (Math.random() * 13D) - 6;
            minimap_random_2 = (int) (Math.random() * 23D) - 11;
        }
        if (show_ui_tab != 0 && super.mouseX >= surface.width2 - 35 - 66 && super.mouseY >= 3 && super.mouseX < surface.width2 - 3 - 66 && super.mouseY < 26)
            show_ui_tab = 3;
        if (show_ui_tab != 0 && super.mouseX >= surface.width2 - 35 - 99 && super.mouseY >= 3 && super.mouseX < surface.width2 - 3 - 99 && super.mouseY < 26)
            show_ui_tab = 4;
        if (show_ui_tab != 0 && super.mouseX >= surface.width2 - 35 - 132 && super.mouseY >= 3 && super.mouseX < surface.width2 - 3 - 132 && super.mouseY < 26)
            show_ui_tab = 5;
        if (show_ui_tab != 0 && super.mouseX >= surface.width2 - 35 - 165 && super.mouseY >= 3 && super.mouseX < surface.width2 - 3 - 165 && super.mouseY < 26)
            show_ui_tab = 6;
        if (show_ui_tab == 1 && (super.mouseX < surface.width2 - 248 || super.mouseY > 36 + (inventory_max_item_count / 5) * 34))
            show_ui_tab = 0;
        if (show_ui_tab == 3 && (super.mouseX < surface.width2 - 199 || super.mouseY > 316))
            show_ui_tab = 0;
        if ((show_ui_tab == 2 || show_ui_tab == 4 || show_ui_tab == 5) && (super.mouseX < surface.width2 - 199 || super.mouseY > 240))
            show_ui_tab = 0;
        if (show_ui_tab == 6 && (super.mouseX < surface.width2 - 199 || super.mouseY > 311))
            show_ui_tab = 0;
    }

    private final void draw_option_menu() {
        if (mouseButtonClick != 0) {
            for (int i = 0; i < option_menu_count; i++) {
                if (super.mouseX >= surface.textWidth(option_menu_entry[i], 1) || super.mouseY <= i * 12 || super.mouseY >= 12 + i * 12)
                    continue;
                super.clientStream.newPacket(116);
                super.clientStream.addByte(i);
                super.clientStream.sendPacket();
                break;
            }

            mouseButtonClick = 0;
            show_option_menu = false;
            return;
        }
        for (int j = 0; j < option_menu_count; j++) {
            int k = 65535;
            if (super.mouseX < surface.textWidth(option_menu_entry[j], 1) && super.mouseY > j * 12 && super.mouseY < 12 + j * 12)
                k = 0xff0000;
            surface.drawstring(option_menu_entry[j], 6, 12 + j * 12, 1, k);
        }

    }

    final void draw_npc(int i, int j, int k, int l, int i1, int j1, int k1) {
        Character character = npcs[i1];
        int l1 = character.animation_current + (cameraRotation + 16) / 32 & 7;
        boolean flag = false;
        int i2 = l1;
        if (i2 == 5) {
            i2 = 3;
            flag = true;
        } else if (i2 == 6) {
            i2 = 2;
            flag = true;
        } else if (i2 == 7) {
            i2 = 1;
            flag = true;
        }
        int j2 = i2 * 3 + npcWalkModel[(character.stepCount / GameData.npc_walk_model[character.npc_id]) % 4];
        if (character.animation_current == 8) {
            i2 = 5;
            l1 = 2;
            flag = false;
            i -= (GameData.npc_combat_animation[character.npc_id] * k1) / 100;
            j2 = i2 * 3 + npcCombatModelArray1[(loginTimer / (GameData.npc_combat_model[character.npc_id] - 1)) % 8];
        } else if (character.animation_current == 9) {
            i2 = 5;
            l1 = 2;
            flag = true;
            i += (GameData.npc_combat_animation[character.npc_id] * k1) / 100;
            j2 = i2 * 3 + npcCombatModelArray2[(loginTimer / GameData.npc_combat_model[character.npc_id]) % 8];
        }
        for (int k2 = 0; k2 < 12; k2++) {
            int l2 = npcAnimationArray[l1][k2];
            int k3 = GameData.npc_sprite[character.npc_id][l2];
            if (k3 >= 0) {
                int i4 = 0;
                int j4 = 0;
                int k4 = j2;
                if (flag && i2 >= 1 && i2 <= 3 && GameData.animation_has_f[k3] == 1)
                    k4 += 15;
                if (i2 != 5 || GameData.animation_has_a[k3] == 1) {
                    int l4 = k4 + GameData.animation_number[k3];
                    i4 = (i4 * k) / surface.image_full_width[l4];
                    j4 = (j4 * l) / surface.image_full_height[l4];
                    int i5 = (k * surface.image_full_width[l4]) / surface.image_full_width[GameData.animation_number[k3]];
                    i4 -= (i5 - k) / 2;
                    int col = GameData.animation_character_colour[k3];
                    int skincol = 0;
                    if (col == 1) {
                        col = GameData.npc_colour_hair[character.npc_id];
                        skincol = GameData.npc_colour_skin[character.npc_id];
                    } else if (col == 2) {
                        col = GameData.npc_colour_top[character.npc_id];
                        skincol = GameData.npc_colour_skin[character.npc_id];
                    } else if (col == 3) {
                        col = GameData.npc_color_bottom[character.npc_id];
                        skincol = GameData.npc_colour_skin[character.npc_id];
                    }
                    surface.sprite_clipping(i + i4, j + j4, i5, l, l4, col, skincol, j1, flag);
                }
            }
        }

        if (character.message_timeout > 0) {
            receivedMessageMidPoint[receivedMessagesCount] = surface.textWidth(character.message, 1) / 2;
            if (receivedMessageMidPoint[receivedMessagesCount] > 150)
                receivedMessageMidPoint[receivedMessagesCount] = 150;
            receivedMessageHeight[receivedMessagesCount] = (surface.textWidth(character.message, 1) / 300) * surface.textHeightNumber(1);
            receivedMessageX[receivedMessagesCount] = i + k / 2;
            receivedMessageY[receivedMessagesCount] = j;
            receivedMessages[receivedMessagesCount++] = character.message;
        }
        if (character.animation_current == 8 || character.animation_current == 9 || character.combat_timer != 0) {
            if (character.combat_timer > 0) {
                int i3 = i;
                if (character.animation_current == 8)
                    i3 -= (20 * k1) / 100;
                else if (character.animation_current == 9)
                    i3 += (20 * k1) / 100;
                int l3 = (character.health_current * 30) / character.health_max;
                healthBarX[healthBarCount] = i3 + k / 2;
                healthBarY[healthBarCount] = j;
                healthBarMissing[healthBarCount++] = l3;
            }
            if (character.combat_timer > 150) {
                int j3 = i;
                if (character.animation_current == 8)
                    j3 -= (10 * k1) / 100;
                else if (character.animation_current == 9)
                    j3 += (10 * k1) / 100;
                surface.draw_picture((j3 + k / 2) - 12, (j + l / 2) - 12, sprite_media + 12);
                surface.drawstring_center(String.valueOf(character.damage_taken), (j3 + k / 2) - 1, j + l / 2 + 5, 3, 0xffffff);
            }
        }
    }

    public final Image createImage(int i, int j) {
        if (GameApplet.gameFrameReference != null)
            return GameApplet.gameFrameReference.createImage(i, j);
        if (link.mainapp != null)
            return link.mainapp.createImage(i, j);
        else
            return super.createImage(i, j);
    }

    private final void method78(int i, int j, int k) {
        if (k == 0) {
            walkToActionSource(region_x, region_y, i, j - 1, i, j, false, true);
            return;
        }
        if (k == 1) {
            walkToActionSource(region_x, region_y, i - 1, j, i, j, false, true);
            return;
        } else {
            walkToActionSource(region_x, region_y, i, j, i, j, true, true);
            return;
        }
    }

    private final void load_game_config() {
        byte buff[] = readDataFile("config" + Version.CONFIG + ".jag", "Configuration", 10);
        if (buff == null) {
            error_loading_data = true;
            return;
        }
        GameData.loadData(buff, members);
        byte abyte1[] = readDataFile("filter" + Version.FILTER + ".jag", "Chat system", 15);
        if (abyte1 == null) {
            error_loading_data = true;
            return;
        } else {
            byte buffragments[] = Utility.loadData("fragmentsenc.txt", 0, abyte1);
            byte buffbandenc[] = Utility.loadData("badenc.txt", 0, abyte1);
            byte buffhostenc[] = Utility.loadData("hostenc.txt", 0, abyte1);
            byte bufftldlist[] = Utility.loadData("tldlist.txt", 0, abyte1);
            WordFilter.load_filter(new RSABuffer(buffragments), new RSABuffer(buffbandenc), new RSABuffer(buffhostenc), new RSABuffer(bufftldlist));
            return;
        }
    }

    private final Character add_npc(int server_index, int x, int y, int sprite, int type) {
        if (npcs_server[server_index] == null) {
            npcs_server[server_index] = new Character();
            npcs_server[server_index].server_index = server_index;
        }
        Character character = npcs_server[server_index];
        boolean found_npc = false;
        for (int i = 0; i < npc_cache_count; i++) {
            if (npcs_cache[i].server_index != server_index)
                continue;
            found_npc = true;
            break;
        }

        if (found_npc) {
            character.npc_id = type;
            character.animation_next = sprite;
            int waypoint_idx = character.waypoint_current;
            if (x != character.waypoints_x[waypoint_idx] || y != character.waypoints_y[waypoint_idx]) {
                character.waypoint_current = waypoint_idx = (waypoint_idx + 1) % 10;
                character.waypoints_x[waypoint_idx] = x;
                character.waypoints_y[waypoint_idx] = y;
            }
        } else {
            character.server_index = server_index;
            character.moving_step = 0;
            character.waypoint_current = 0;
            character.waypoints_x[0] = character.currentX = x;
            character.waypoints_y[0] = character.currentY = y;
            character.npc_id = type;
            character.animation_next = character.animation_current = sprite;
            character.stepCount = 0;
        }
        npcs[npc_count++] = character;
        return character;
    }

    protected final void resetLoginVars() {
        systemUpdate = 0;
        loginScreen = 0;
        loggedIn = 0;
        logout_timeout = 0;
    }

    private final void draw_dialog_bank() {
        int dialog_width = 408;// '\u0198';
        int dialog_height = 334;// '\u014E';
        if (bank_active_page > 0 && bankItemCount <= 48)
            bank_active_page = 0;
        if (bank_active_page > 1 && bankItemCount <= 96)
            bank_active_page = 1;
        if (bank_active_page > 2 && bankItemCount <= 144)
            bank_active_page = 2;
        if (bank_selected_item_slot >= bankItemCount || bank_selected_item_slot < 0)
            bank_selected_item_slot = -1;
        if (bank_selected_item_slot != -1 && bank_items[bank_selected_item_slot] != bank_selected_item) {
            bank_selected_item_slot = -1;
            bank_selected_item = -2;
        }
        if (mouseButtonClick != 0) {
            mouseButtonClick = 0;
            int mouse_x = super.mouseX - (256 - dialog_width / 2);
            int mouse_y = super.mouseY - (170 - dialog_height / 2);
            if (mouse_x >= 0 && mouse_y >= 12 && mouse_x < 408 && mouse_y < 280) {
                int i1 = bank_active_page * 48;
                for (int l1 = 0; l1 < 6; l1++) {
                    for (int j2 = 0; j2 < 8; j2++) {
                        int l6 = 7 + j2 * 49;
                        int j7 = 28 + l1 * 34;
                        if (mouse_x > l6 && mouse_x < l6 + 49 && mouse_y > j7 && mouse_y < j7 + 34 && i1 < bankItemCount && bank_items[i1] != -1) {
                            bank_selected_item = bank_items[i1];
                            bank_selected_item_slot = i1;
                        }
                        i1++;
                    }

                }

                mouse_x = 256 - dialog_width / 2;
                mouse_y = 170 - dialog_height / 2;
                int slot;
                if (bank_selected_item_slot < 0)
                    slot = -1;
                else
                    slot = bank_items[bank_selected_item_slot];
                if (slot != -1) {
                    int j1 = bankItemsCount[bank_selected_item_slot];
                    if (GameData.item_stackable[slot] == 1 && j1 > 1)
                        j1 = 1;
                    if (j1 >= 1 && super.mouseX >= mouse_x + 220 && super.mouseY >= mouse_y + 238 && super.mouseX < mouse_x + 250 && super.mouseY <= mouse_y + 249) {
                        super.clientStream.newPacket(22);
                        super.clientStream.addShort(slot);
                        super.clientStream.addShort(1);
                        super.clientStream.addInt(0x12345678);
                        super.clientStream.sendPacket();
                    }
                    if (j1 >= 5 && super.mouseX >= mouse_x + 250 && super.mouseY >= mouse_y + 238 && super.mouseX < mouse_x + 280 && super.mouseY <= mouse_y + 249) {
                        super.clientStream.newPacket(22);
                        super.clientStream.addShort(slot);
                        super.clientStream.addShort(5);
                        super.clientStream.addInt(0x12345678);
                        super.clientStream.sendPacket();
                    }
                    if (j1 >= 25 && super.mouseX >= mouse_x + 280 && super.mouseY >= mouse_y + 238 && super.mouseX < mouse_x + 305 && super.mouseY <= mouse_y + 249) {
                        super.clientStream.newPacket(22);
                        super.clientStream.addShort(slot);
                        super.clientStream.addShort(25);
                        super.clientStream.addInt(0x12345678);
                        super.clientStream.sendPacket();
                    }
                    if (j1 >= 100 && super.mouseX >= mouse_x + 305 && super.mouseY >= mouse_y + 238 && super.mouseX < mouse_x + 335 && super.mouseY <= mouse_y + 249) {
                        super.clientStream.newPacket(22);
                        super.clientStream.addShort(slot);
                        super.clientStream.addShort(100);
                        super.clientStream.addInt(0x12345678);
                        super.clientStream.sendPacket();
                    }
                    if (j1 >= 500 && super.mouseX >= mouse_x + 335 && super.mouseY >= mouse_y + 238 && super.mouseX < mouse_x + 368 && super.mouseY <= mouse_y + 249) {
                        super.clientStream.newPacket(22);
                        super.clientStream.addShort(slot);
                        super.clientStream.addShort(500);
                        super.clientStream.addInt(0x12345678);
                        super.clientStream.sendPacket();
                    }
                    if (j1 >= 2500 && super.mouseX >= mouse_x + 370 && super.mouseY >= mouse_y + 238 && super.mouseX < mouse_x + 400 && super.mouseY <= mouse_y + 249) {
                        super.clientStream.newPacket(22);
                        super.clientStream.addShort(slot);
                        super.clientStream.addShort(2500);
                        super.clientStream.addInt(0x12345678);
                        super.clientStream.sendPacket();
                    }
                    if (getInventoryCount(slot) >= 1 && super.mouseX >= mouse_x + 220 && super.mouseY >= mouse_y + 263 && super.mouseX < mouse_x + 250 && super.mouseY <= mouse_y + 274) {
                        super.clientStream.newPacket(23);
                        super.clientStream.addShort(slot);
                        super.clientStream.addShort(1);
                        super.clientStream.addInt(0x87654321);
                        super.clientStream.sendPacket();
                    }
                    if (getInventoryCount(slot) >= 5 && super.mouseX >= mouse_x + 250 && super.mouseY >= mouse_y + 263 && super.mouseX < mouse_x + 280 && super.mouseY <= mouse_y + 274) {
                        super.clientStream.newPacket(23);
                        super.clientStream.addShort(slot);
                        super.clientStream.addShort(5);
                        super.clientStream.addInt(0x87654321);
                        super.clientStream.sendPacket();
                    }
                    if (getInventoryCount(slot) >= 25 && super.mouseX >= mouse_x + 280 && super.mouseY >= mouse_y + 263 && super.mouseX < mouse_x + 305 && super.mouseY <= mouse_y + 274) {
                        super.clientStream.newPacket(23);
                        super.clientStream.addShort(slot);
                        super.clientStream.addShort(25);
                        super.clientStream.addInt(0x87654321);
                        super.clientStream.sendPacket();
                    }
                    if (getInventoryCount(slot) >= 100 && super.mouseX >= mouse_x + 305 && super.mouseY >= mouse_y + 263 && super.mouseX < mouse_x + 335 && super.mouseY <= mouse_y + 274) {
                        super.clientStream.newPacket(23);
                        super.clientStream.addShort(slot);
                        super.clientStream.addShort(100);
                        super.clientStream.addInt(0x87654321);
                        super.clientStream.sendPacket();
                    }
                    if (getInventoryCount(slot) >= 500 && super.mouseX >= mouse_x + 335 && super.mouseY >= mouse_y + 263 && super.mouseX < mouse_x + 368 && super.mouseY <= mouse_y + 274) {
                        super.clientStream.newPacket(23);
                        super.clientStream.addShort(slot);
                        super.clientStream.addShort(500);
                        super.clientStream.addInt(0x87654321);
                        super.clientStream.sendPacket();
                    }
                    if (getInventoryCount(slot) >= 2500 && super.mouseX >= mouse_x + 370 && super.mouseY >= mouse_y + 263 && super.mouseX < mouse_x + 400 && super.mouseY <= mouse_y + 274) {
                        super.clientStream.newPacket(23);
                        super.clientStream.addShort(slot);
                        super.clientStream.addShort(2500);
                        super.clientStream.addInt(0x87654321);
                        super.clientStream.sendPacket();
                    }
                }
            } else if (bankItemCount > 48 && mouse_x >= 50 && mouse_x <= 115 && mouse_y <= 12)
                bank_active_page = 0;
            else if (bankItemCount > 48 && mouse_x >= 115 && mouse_x <= 180 && mouse_y <= 12)
                bank_active_page = 1;
            else if (bankItemCount > 96 && mouse_x >= 180 && mouse_x <= 245 && mouse_y <= 12)
                bank_active_page = 2;
            else if (bankItemCount > 144 && mouse_x >= 245 && mouse_x <= 310 && mouse_y <= 12) {
                bank_active_page = 3;
            } else {
                super.clientStream.newPacket(212);
                super.clientStream.sendPacket();
                show_dialog_bank = false;
                return;
            }
        }
        int x = 256 - dialog_width / 2;
        int y = 170 - dialog_height / 2;
        surface.draw_box(x, y, 408, 12, 192);
        surface.draw_box_alpha(x, y + 12, 408, 17, 0x989898, 160);
        surface.draw_box_alpha(x, y + 29, 8, 204, 0x989898, 160);
        surface.draw_box_alpha(x + 399, y + 29, 9, 204, 0x989898, 160);
        surface.draw_box_alpha(x, y + 233, 408, 47, 0x989898, 160);
        surface.drawstring("Bank", x + 1, y + 10, 1, 0xffffff);
        int x_off = 50;
        if (bankItemCount > 48) {
            int l2 = 0xffffff;
            if (bank_active_page == 0)
                l2 = 0xff0000;
            else if (super.mouseX > x + x_off && super.mouseY >= y && super.mouseX < x + x_off + 65 && super.mouseY < y + 12)
                l2 = 0xffff00;
            surface.drawstring("<page 1>", x + x_off, y + 10, 1, l2);
            x_off += 65;
            l2 = 0xffffff;
            if (bank_active_page == 1)
                l2 = 0xff0000;
            else if (super.mouseX > x + x_off && super.mouseY >= y && super.mouseX < x + x_off + 65 && super.mouseY < y + 12)
                l2 = 0xffff00;
            surface.drawstring("<page 2>", x + x_off, y + 10, 1, l2);
            x_off += 65;
        }
        if (bankItemCount > 96) {
            int i3 = 0xffffff;
            if (bank_active_page == 2)
                i3 = 0xff0000;
            else if (super.mouseX > x + x_off && super.mouseY >= y && super.mouseX < x + x_off + 65 && super.mouseY < y + 12)
                i3 = 0xffff00;
            surface.drawstring("<page 3>", x + x_off, y + 10, 1, i3);
            x_off += 65;
        }
        if (bankItemCount > 144) {
            int j3 = 0xffffff;
            if (bank_active_page == 3)
                j3 = 0xff0000;
            else if (super.mouseX > x + x_off && super.mouseY >= y && super.mouseX < x + x_off + 65 && super.mouseY < y + 12)
                j3 = 0xffff00;
            surface.drawstring("<page 4>", x + x_off, y + 10, 1, j3);
            x_off += 65;
        }
        int colour = 0xffffff;
        if (super.mouseX > x + 320 && super.mouseY >= y && super.mouseX < x + 408 && super.mouseY < y + 12)
            colour = 0xff0000;
        surface.drawstring_right("Close window", x + 406, y + 10, 1, colour);
        surface.drawstring("Number in bank in green", x + 7, y + 24, 1, 65280);
        surface.drawstring("Number held in blue", x + 289, y + 24, 1, 65535);
        int k7 = bank_active_page * 48;
        for (int i8 = 0; i8 < 6; i8++) {
            for (int j8 = 0; j8 < 8; j8++) {
                int l8 = x + 7 + j8 * 49;
                int i9 = y + 28 + i8 * 34;
                if (bank_selected_item_slot == k7)
                    surface.draw_box_alpha(l8, i9, 49, 34, 0xff0000, 160);
                else
                    surface.draw_box_alpha(l8, i9, 49, 34, 0xd0d0d0, 160);
                surface.draw_box_edge(l8, i9, 50, 35, 0);
                if (k7 < bankItemCount && bank_items[k7] != -1) {
                    surface.sprite_clipping(l8, i9, 48, 32, sprite_item + GameData.item_picture[bank_items[k7]], GameData.item_mask[bank_items[k7]], 0, 0, false);
                    surface.drawstring(String.valueOf(bankItemsCount[k7]), l8 + 1, i9 + 10, 1, 65280);
                    surface.drawstring_right(String.valueOf(getInventoryCount(bank_items[k7])), l8 + 47, i9 + 29, 1, 65535);
                }
                k7++;
            }

        }

        surface.draw_line_horiz(x + 5, y + 256, 398, 0);
        if (bank_selected_item_slot == -1) {
            surface.drawstring_center("Select an object to withdraw or deposit", x + 204, y + 248, 3, 0xffff00);
            return;
        }
        int item_type;
        if (bank_selected_item_slot < 0)
            item_type = -1;
        else
            item_type = bank_items[bank_selected_item_slot];
        if (item_type != -1) {
            int item_count = bankItemsCount[bank_selected_item_slot];
            if (GameData.item_stackable[item_type] == 1 && item_count > 1)
                item_count = 1;
            if (item_count > 0) {
                surface.drawstring("Withdraw " + GameData.item_name[item_type], x + 2, y + 248, 1, 0xffffff);
                colour = 0xffffff;
                if (super.mouseX >= x + 220 && super.mouseY >= y + 238 && super.mouseX < x + 250 && super.mouseY <= y + 249)
                    colour = 0xff0000;
                surface.drawstring("One", x + 222, y + 248, 1, colour);
                if (item_count >= 5) {
                    colour = 0xffffff;
                    if (super.mouseX >= x + 250 && super.mouseY >= y + 238 && super.mouseX < x + 280 && super.mouseY <= y + 249)
                        colour = 0xff0000;
                    surface.drawstring("Five", x + 252, y + 248, 1, colour);
                }
                if (item_count >= 25) {
                    colour = 0xffffff;
                    if (super.mouseX >= x + 280 && super.mouseY >= y + 238 && super.mouseX < x + 305 && super.mouseY <= y + 249)
                        colour = 0xff0000;
                    surface.drawstring("25", x + 282, y + 248, 1, colour);
                }
                if (item_count >= 100) {
                    colour = 0xffffff;
                    if (super.mouseX >= x + 305 && super.mouseY >= y + 238 && super.mouseX < x + 335 && super.mouseY <= y + 249)
                        colour = 0xff0000;
                    surface.drawstring("100", x + 307, y + 248, 1, colour);
                }
                if (item_count >= 500) {
                    colour = 0xffffff;
                    if (super.mouseX >= x + 335 && super.mouseY >= y + 238 && super.mouseX < x + 368 && super.mouseY <= y + 249)
                        colour = 0xff0000;
                    surface.drawstring("500", x + 337, y + 248, 1, colour);
                }
                if (item_count >= 2500) {
                    colour = 0xffffff;
                    if (super.mouseX >= x + 370 && super.mouseY >= y + 238 && super.mouseX < x + 400 && super.mouseY <= y + 249)
                        colour = 0xff0000;
                    surface.drawstring("2500", x + 370, y + 248, 1, colour);
                }
            }
            if (getInventoryCount(item_type) > 0) {
                surface.drawstring("Deposit " + GameData.item_name[item_type], x + 2, y + 273, 1, 0xffffff);
                colour = 0xffffff;
                if (super.mouseX >= x + 220 && super.mouseY >= y + 263 && super.mouseX < x + 250 && super.mouseY <= y + 274)
                    colour = 0xff0000;
                surface.drawstring("One", x + 222, y + 273, 1, colour);
                if (getInventoryCount(item_type) >= 5) {
                    colour = 0xffffff;
                    if (super.mouseX >= x + 250 && super.mouseY >= y + 263 && super.mouseX < x + 280 && super.mouseY <= y + 274)
                        colour = 0xff0000;
                    surface.drawstring("Five", x + 252, y + 273, 1, colour);
                }
                if (getInventoryCount(item_type) >= 25) {
                    colour = 0xffffff;
                    if (super.mouseX >= x + 280 && super.mouseY >= y + 263 && super.mouseX < x + 305 && super.mouseY <= y + 274)
                        colour = 0xff0000;
                    surface.drawstring("25", x + 282, y + 273, 1, colour);
                }
                if (getInventoryCount(item_type) >= 100) {
                    colour = 0xffffff;
                    if (super.mouseX >= x + 305 && super.mouseY >= y + 263 && super.mouseX < x + 335 && super.mouseY <= y + 274)
                        colour = 0xff0000;
                    surface.drawstring("100", x + 307, y + 273, 1, colour);
                }
                if (getInventoryCount(item_type) >= 500) {
                    colour = 0xffffff;
                    if (super.mouseX >= x + 335 && super.mouseY >= y + 263 && super.mouseX < x + 368 && super.mouseY <= y + 274)
                        colour = 0xff0000;
                    surface.drawstring("500", x + 337, y + 273, 1, colour);
                }
                if (getInventoryCount(item_type) >= 2500) {
                    colour = 0xffffff;
                    if (super.mouseX >= x + 370 && super.mouseY >= y + 263 && super.mouseX < x + 400 && super.mouseY <= y + 274)
                        colour = 0xff0000;
                    surface.drawstring("2500", x + 370, y + 273, 1, colour);
                }
            }
        }
    }

    private final void draw_dialog_duel() {
        if (mouseButtonClick != 0 && mouseButtonItemCountIncrement == 0)
            mouseButtonItemCountIncrement = 1;
        if (mouseButtonItemCountIncrement > 0) {
            int i = super.mouseX - 22;
            int j = super.mouseY - 36;
            if (i >= 0 && j >= 0 && i < 468 && j < 262) {
                if (i > 216 && j > 30 && i < 462 && j < 235) {
                    int k = (i - 217) / 49 + ((j - 31) / 34) * 5;
                    if (k >= 0 && k < inventory_items_count) {
                        boolean flag1 = false;
                        int l1 = 0;
                        int k2 = inventory_item_id[k];
                        for (int k3 = 0; k3 < duel_offer_item_count; k3++)
                            if (duel_offer_item_id[k3] == k2)
                                if (GameData.item_stackable[k2] == 0) {
                                    for (int i4 = 0; i4 < mouseButtonItemCountIncrement; i4++) {
                                        if (duel_offer_item_stack[k3] < inventory_item_stack_count[k])
                                            duel_offer_item_stack[k3]++;
                                        flag1 = true;
                                    }

                                } else {
                                    l1++;
                                }

                        if (getInventoryCount(k2) <= l1)
                            flag1 = true;
                        if (GameData.item_special[k2] == 1) {
                            show_message("This object cannot be added to a duel offer", 3);
                            flag1 = true;
                        }
                        if (!flag1 && duel_offer_item_count < 8) {
                            duel_offer_item_id[duel_offer_item_count] = k2;
                            duel_offer_item_stack[duel_offer_item_count] = 1;
                            duel_offer_item_count++;
                            flag1 = true;
                        }
                        if (flag1) {
                            super.clientStream.newPacket(33);
                            super.clientStream.addByte(duel_offer_item_count);
                            for (int j4 = 0; j4 < duel_offer_item_count; j4++) {
                                super.clientStream.addShort(duel_offer_item_id[j4]);
                                super.clientStream.addInt(duel_offer_item_stack[j4]);
                            }

                            super.clientStream.sendPacket();
                            duel_offer_opponent_accepted = false;
                            duel_offer_accepted = false;
                        }
                    }
                }
                if (i > 8 && j > 30 && i < 205 && j < 129) {
                    int l = (i - 9) / 49 + ((j - 31) / 34) * 4;
                    if (l >= 0 && l < duel_offer_item_count) {
                        int j1 = duel_offer_item_id[l];
                        for (int i2 = 0; i2 < mouseButtonItemCountIncrement; i2++) {
                            if (GameData.item_stackable[j1] == 0 && duel_offer_item_stack[l] > 1) {
                                duel_offer_item_stack[l]--;
                                continue;
                            }
                            duel_offer_item_count--;
                            mouseButtonDownTime = 0;
                            for (int l2 = l; l2 < duel_offer_item_count; l2++) {
                                duel_offer_item_id[l2] = duel_offer_item_id[l2 + 1];
                                duel_offer_item_stack[l2] = duel_offer_item_stack[l2 + 1];
                            }

                            break;
                        }

                        super.clientStream.newPacket(33);
                        super.clientStream.addByte(duel_offer_item_count);
                        for (int i3 = 0; i3 < duel_offer_item_count; i3++) {
                            super.clientStream.addShort(duel_offer_item_id[i3]);
                            super.clientStream.addInt(duel_offer_item_stack[i3]);
                        }

                        super.clientStream.sendPacket();
                        duel_offer_opponent_accepted = false;
                        duel_offer_accepted = false;
                    }
                }
                boolean flag = false;
                if (i >= 93 && j >= 221 && i <= 104 && j <= 232) {
                    duel_settings_retreat = !duel_settings_retreat;
                    flag = true;
                }
                if (i >= 93 && j >= 240 && i <= 104 && j <= 251) {
                    duel_settings_magic = !duel_settings_magic;
                    flag = true;
                }
                if (i >= 191 && j >= 221 && i <= 202 && j <= 232) {
                    duel_settings_prayer = !duel_settings_prayer;
                    flag = true;
                }
                if (i >= 191 && j >= 240 && i <= 202 && j <= 251) {
                    duel_settings_weapons = !duel_settings_weapons;
                    flag = true;
                }
                if (flag) {
                    super.clientStream.newPacket(8);
                    super.clientStream.addByte(duel_settings_retreat ? 1 : 0);
                    super.clientStream.addByte(duel_settings_magic ? 1 : 0);
                    super.clientStream.addByte(duel_settings_prayer ? 1 : 0);
                    super.clientStream.addByte(duel_settings_weapons ? 1 : 0);
                    super.clientStream.sendPacket();
                    duel_offer_opponent_accepted = false;
                    duel_offer_accepted = false;
                }
                if (i >= 217 && j >= 238 && i <= 286 && j <= 259) {
                    duel_offer_accepted = true;
                    super.clientStream.newPacket(176);
                    super.clientStream.sendPacket();
                }
                if (i >= 394 && j >= 238 && i < 463 && j < 259) {
                    show_dialog_duel = false;
                    super.clientStream.newPacket(197);
                    super.clientStream.sendPacket();
                }
            } else if (mouseButtonClick != 0) {
                show_dialog_duel = false;
                super.clientStream.newPacket(197);
                super.clientStream.sendPacket();
            }
            mouseButtonClick = 0;
            mouseButtonItemCountIncrement = 0;
        }
        if (!show_dialog_duel)
            return;
        byte cx = 22;
        byte cy = 36;
        surface.draw_box(cx, cy, 468, 12, 0xc90b1d);
        surface.draw_box_alpha(cx, cy + 12, 468, 18, 0x989898, 160);
        surface.draw_box_alpha(cx, cy + 30, 8, 248, 0x989898, 160);
        surface.draw_box_alpha(cx + 205, cy + 30, 11, 248, 0x989898, 160);
        surface.draw_box_alpha(cx + 462, cy + 30, 6, 248, 0x989898, 160);
        surface.draw_box_alpha(cx + 8, cy + 99, 197, 24, 0x989898, 160);
        surface.draw_box_alpha(cx + 8, cy + 192, 197, 23, 0x989898, 160);
        surface.draw_box_alpha(cx + 8, cy + 258, 197, 20, 0x989898, 160);
        surface.draw_box_alpha(cx + 216, cy + 235, 246, 43, 0x989898, 160);
        surface.draw_box_alpha(cx + 8, cy + 30, 197, 69, 0xd0d0d0, 160);
        surface.draw_box_alpha(cx + 8, cy + 123, 197, 69, 0xd0d0d0, 160);
        surface.draw_box_alpha(cx + 8, cy + 215, 197, 43, 0xd0d0d0, 160);
        surface.draw_box_alpha(cx + 216, cy + 30, 246, 205, 0xd0d0d0, 160);
        for (int j2 = 0; j2 < 3; j2++)
            surface.draw_line_horiz(cx + 8, cy + 30 + j2 * 34, 197, 0);

        for (int j3 = 0; j3 < 3; j3++)
            surface.draw_line_horiz(cx + 8, cy + 123 + j3 * 34, 197, 0);

        for (int l3 = 0; l3 < 7; l3++)
            surface.draw_line_horiz(cx + 216, cy + 30 + l3 * 34, 246, 0);

        for (int k4 = 0; k4 < 6; k4++) {
            if (k4 < 5)
                surface.draw_line_vert(cx + 8 + k4 * 49, cy + 30, 69, 0);
            if (k4 < 5)
                surface.draw_line_vert(cx + 8 + k4 * 49, cy + 123, 69, 0);
            surface.draw_line_vert(cx + 216 + k4 * 49, cy + 30, 205, 0);
        }

        surface.draw_line_horiz(cx + 8, cy + 215, 197, 0);
        surface.draw_line_horiz(cx + 8, cy + 257, 197, 0);
        surface.draw_line_vert(cx + 8, cy + 215, 43, 0);
        surface.draw_line_vert(cx + 204, cy + 215, 43, 0);
        surface.drawstring("Preparing to duel with: " + aString736, cx + 1, cy + 10, 1, 0xffffff);
        surface.drawstring("Your Stake", cx + 9, cy + 27, 4, 0xffffff);
        surface.drawstring("Opponent's Stake", cx + 9, cy + 120, 4, 0xffffff);
        surface.drawstring("Duel Options", cx + 9, cy + 212, 4, 0xffffff);
        surface.drawstring("Your Inventory", cx + 216, cy + 27, 4, 0xffffff);
        surface.drawstring("No retreating", cx + 8 + 1, cy + 215 + 16, 3, 0xffff00);
        surface.drawstring("No magic", cx + 8 + 1, cy + 215 + 35, 3, 0xffff00);
        surface.drawstring("No prayer", cx + 8 + 102, cy + 215 + 16, 3, 0xffff00);
        surface.drawstring("No weapons", cx + 8 + 102, cy + 215 + 35, 3, 0xffff00);
        surface.draw_box_edge(cx + 93, cy + 215 + 6, 11, 11, 0xffff00);
        if (duel_settings_retreat)
            surface.draw_box(cx + 95, cy + 215 + 8, 7, 7, 0xffff00);
        surface.draw_box_edge(cx + 93, cy + 215 + 25, 11, 11, 0xffff00);
        if (duel_settings_magic)
            surface.draw_box(cx + 95, cy + 215 + 27, 7, 7, 0xffff00);
        surface.draw_box_edge(cx + 191, cy + 215 + 6, 11, 11, 0xffff00);
        if (duel_settings_prayer)
            surface.draw_box(cx + 193, cy + 215 + 8, 7, 7, 0xffff00);
        surface.draw_box_edge(cx + 191, cy + 215 + 25, 11, 11, 0xffff00);
        if (duel_settings_weapons)
            surface.draw_box(cx + 193, cy + 215 + 27, 7, 7, 0xffff00);
        if (!duel_offer_accepted)
            surface.draw_picture(cx + 217, cy + 238, sprite_media + 25);
        surface.draw_picture(cx + 394, cy + 238, sprite_media + 26);
        if (duel_offer_opponent_accepted) {
            surface.drawstring_center("Other player", cx + 341, cy + 246, 1, 0xffffff);
            surface.drawstring_center("has accepted", cx + 341, cy + 256, 1, 0xffffff);
        }
        if (duel_offer_accepted) {
            surface.drawstring_center("Waiting for", cx + 217 + 35, cy + 246, 1, 0xffffff);
            surface.drawstring_center("other player", cx + 217 + 35, cy + 256, 1, 0xffffff);
        }
        for (int i = 0; i < inventory_items_count; i++) {
            int x = 217 + cx + (i % 5) * 49;
            int y = 31 + cy + (i / 5) * 34;
            surface.sprite_clipping(x, y, 48, 32, sprite_item + GameData.item_picture[inventory_item_id[i]], GameData.item_mask[inventory_item_id[i]], 0, 0, false);
            if (GameData.item_stackable[inventory_item_id[i]] == 0)
                surface.drawstring(String.valueOf(inventory_item_stack_count[i]), x + 1, y + 10, 1, 0xffff00);
        }

        for (int i = 0; i < duel_offer_item_count; i++) {
            int x = 9 + cx + (i % 4) * 49;
            int y = 31 + cy + (i / 4) * 34;
            surface.sprite_clipping(x, y, 48, 32, sprite_item + GameData.item_picture[duel_offer_item_id[i]], GameData.item_mask[duel_offer_item_id[i]], 0, 0, false);
            if (GameData.item_stackable[duel_offer_item_id[i]] == 0)
                surface.drawstring(String.valueOf(duel_offer_item_stack[i]), x + 1, y + 10, 1, 0xffff00);
            if (super.mouseX > x && super.mouseX < x + 48 && super.mouseY > y && super.mouseY < y + 32)
                surface.drawstring(GameData.item_name[duel_offer_item_id[i]] + ": @whi@" + GameData.item_description[duel_offer_item_id[i]], cx + 8, cy + 273, 1, 0xffff00);
        }

        for (int i = 0; i < duel_offer_opponent_item_count; i++) {
            int x = 9 + cx + (i % 4) * 49;
            int y = 124 + cy + (i / 4) * 34;
            surface.sprite_clipping(x, y, 48, 32, sprite_item + GameData.item_picture[duel_offer_opponent_item_id[i]], GameData.item_mask[duel_offer_opponent_item_id[i]], 0, 0, false);
            if (GameData.item_stackable[duel_offer_opponent_item_id[i]] == 0)
                surface.drawstring(String.valueOf(duel_offer_opponent_item_stack[i]), x + 1, y + 10, 1, 0xffff00);
            if (super.mouseX > x && super.mouseX < x + 48 && super.mouseY > y && super.mouseY < y + 32)
                surface.drawstring(GameData.item_name[duel_offer_opponent_item_id[i]] + ": @whi@" + GameData.item_description[duel_offer_opponent_item_id[i]], cx + 8, cy + 273, 1, 0xffff00);
        }

    }

    private final boolean loadNextRegion(int lx, int ly) {
        if (death_screen_timeout != 0) {
            world.playerAlive = false;
            return false;
        }
        loading_area = false;
        lx += plane_width;
        ly += plane_height;
        if (lastHeightOffset == plane_index && lx > localLowerX && lx < localUpperX && ly > localLowerY && ly < localUpperY) {
            world.playerAlive = true;
            return false;
        }
        surface.drawstring_center("Loading... Please wait", 256, 192, 1, 0xffffff);
        drawChatMessageTabs();
        surface.draw(graphics, 0, 0);
        int ax = areaX;
        int ay = areaY;
        int sectionX = (lx + 24) / 48;
        int sectionY = (ly + 24) / 48;
        lastHeightOffset = plane_index;
        areaX = sectionX * 48 - 48;
        areaY = sectionY * 48 - 48;
        localLowerX = sectionX * 48 - 32;
        localLowerY = sectionY * 48 - 32;
        localUpperX = sectionX * 48 + 32;
        localUpperY = sectionY * 48 + 32;
        world.load_section(lx, ly, lastHeightOffset);
        areaX -= plane_width;
        areaY -= plane_height;
        int offsetx = areaX - ax;
        int offsety = areaY - ay;
        for (int objidx = 0; objidx < object_count; objidx++) {
            object_x[objidx] -= offsetx;
            object_y[objidx] -= offsety;
            int objx = object_x[objidx];
            int objy = object_y[objidx];
            int objid = object_id[objidx];
            GameModel gameModel = object_model[objidx];
            try {
                int objtype = object_direction[objidx];
                int objw;
                int objh;
                if (objtype == 0 || objtype == 4) {
                    objw = GameData.object_width[objid];
                    objh = GameData.object_height[objid];
                } else {
                    objh = GameData.object_width[objid];
                    objw = GameData.object_height[objid];
                }
                int j6 = ((objx + objx + objw) * magicLoc) / 2;
                int k6 = ((objy + objy + objh) * magicLoc) / 2;
                if (objx >= 0 && objy >= 0 && objx < 96 && objy < 96) {
                    scene.add_model(gameModel);
                    gameModel.place(j6, -world.get_elevation(j6, k6), k6);
                    world.method400(objx, objy, objid);
                    if (objid == 74)
                        gameModel.translate(0, -480, 0);
                }
            } catch (RuntimeException runtimeexception) {
                System.out.println("Loc Error: " + runtimeexception.getMessage());
                System.out.println("i:" + objidx + " obj:" + gameModel);
                runtimeexception.printStackTrace();
            }
        }

        for (int k2 = 0; k2 < wall_object_count; k2++) {
            wall_object_x[k2] -= offsetx;
            wall_object_y[k2] -= offsety;
            int i3 = wall_object_x[k2];
            int l3 = wall_object_y[k2];
            int j4 = wall_object_id[k2];
            int i5 = wall_object_direction[k2];
            try {
                world.set_object_adjacency(i3, l3, i5, j4);
                GameModel gameModel_1 = create_model(i3, l3, i5, j4, k2);
                wall_object_model[k2] = gameModel_1;
            } catch (RuntimeException runtimeexception1) {
                System.out.println("Bound Error: " + runtimeexception1.getMessage());
                runtimeexception1.printStackTrace();
            }
        }

        for (int j3 = 0; j3 < ground_item_count; j3++) {
            ground_item_x[j3] -= offsetx;
            ground_item_y[j3] -= offsety;
        }

        for (int i4 = 0; i4 < player_count; i4++) {
            Character character = players[i4];
            character.currentX -= offsetx * magicLoc;
            character.currentY -= offsety * magicLoc;
            for (int j5 = 0; j5 <= character.waypoint_current; j5++) {
                character.waypoints_x[j5] -= offsetx * magicLoc;
                character.waypoints_y[j5] -= offsety * magicLoc;
            }

        }

        for (int k4 = 0; k4 < npc_count; k4++) {
            Character character_1 = npcs[k4];
            character_1.currentX -= offsetx * magicLoc;
            character_1.currentY -= offsety * magicLoc;
            for (int l5 = 0; l5 <= character_1.waypoint_current; l5++) {
                character_1.waypoints_x[l5] -= offsetx * magicLoc;
                character_1.waypoints_y[l5] -= offsety * magicLoc;
            }

        }

        world.playerAlive = true;
        return true;
    }

    final void draw_player(int x, int y, int k, int l, int i1, int j1, int k1) {
        Character character = players[i1];
        if (character.colour_bottom == 255) // this means the character is invisible! MOD!!!
            return;
        int l1 = character.animation_current + (cameraRotation + 16) / 32 & 7;
        boolean flag = false;
        int i2 = l1;
        if (i2 == 5) {
            i2 = 3;
            flag = true;
        } else if (i2 == 6) {
            i2 = 2;
            flag = true;
        } else if (i2 == 7) {
            i2 = 1;
            flag = true;
        }
        int j2 = i2 * 3 + npcWalkModel[(character.stepCount / 6) % 4];
        if (character.animation_current == 8) {
            i2 = 5;
            l1 = 2;
            flag = false;
            x -= (5 * k1) / 100;
            j2 = i2 * 3 + npcCombatModelArray1[(loginTimer / 5) % 8];
        } else if (character.animation_current == 9) {
            i2 = 5;
            l1 = 2;
            flag = true;
            x += (5 * k1) / 100;
            j2 = i2 * 3 + npcCombatModelArray2[(loginTimer / 6) % 8];
        }
        for (int k2 = 0; k2 < 12; k2++) {
            int l2 = npcAnimationArray[l1][k2];
            int l3 = character.equipped_item[l2] - 1;
            if (l3 >= 0) {
                int k4 = 0;
                int i5 = 0;
                int j5 = j2;
                if (flag && i2 >= 1 && i2 <= 3)
                    if (GameData.animation_has_f[l3] == 1)
                        j5 += 15;
                    else if (l2 == 4 && i2 == 1) {
                        k4 = -22;
                        i5 = -3;
                        j5 = i2 * 3 + npcWalkModel[(2 + character.stepCount / 6) % 4];
                    } else if (l2 == 4 && i2 == 2) {
                        k4 = 0;
                        i5 = -8;
                        j5 = i2 * 3 + npcWalkModel[(2 + character.stepCount / 6) % 4];
                    } else if (l2 == 4 && i2 == 3) {
                        k4 = 26;
                        i5 = -5;
                        j5 = i2 * 3 + npcWalkModel[(2 + character.stepCount / 6) % 4];
                    } else if (l2 == 3 && i2 == 1) {
                        k4 = 22;
                        i5 = 3;
                        j5 = i2 * 3 + npcWalkModel[(2 + character.stepCount / 6) % 4];
                    } else if (l2 == 3 && i2 == 2) {
                        k4 = 0;
                        i5 = 8;
                        j5 = i2 * 3 + npcWalkModel[(2 + character.stepCount / 6) % 4];
                    } else if (l2 == 3 && i2 == 3) {
                        k4 = -26;
                        i5 = 5;
                        j5 = i2 * 3 + npcWalkModel[(2 + character.stepCount / 6) % 4];
                    }
                if (i2 != 5 || GameData.animation_has_a[l3] == 1) {
                    int k5 = j5 + GameData.animation_number[l3];
                    k4 = (k4 * k) / surface.image_full_width[k5];
                    i5 = (i5 * l) / surface.image_full_height[k5];
                    int l5 = (k * surface.image_full_width[k5]) / surface.image_full_width[GameData.animation_number[l3]];
                    k4 -= (l5 - k) / 2;
                    int i6 = GameData.animation_character_colour[l3];
                    int j6 = characterSkinColours[character.colour_skin];
                    if (i6 == 1)
                        i6 = characterHairColours[character.colour_hair];
                    else if (i6 == 2)
                        i6 = characterTopBottomColours[character.colour_top];
                    else if (i6 == 3)
                        i6 = characterTopBottomColours[character.colour_bottom];
                    surface.sprite_clipping(x + k4, y + i5, l5, l, k5, i6, j6, j1, flag);
                }
            }
        }

        if (character.message_timeout > 0) {
            receivedMessageMidPoint[receivedMessagesCount] = surface.textWidth(character.message, 1) / 2;
            if (receivedMessageMidPoint[receivedMessagesCount] > 150)
                receivedMessageMidPoint[receivedMessagesCount] = 150;
            receivedMessageHeight[receivedMessagesCount] = (surface.textWidth(character.message, 1) / 300) * surface.textHeightNumber(1);
            receivedMessageX[receivedMessagesCount] = x + k / 2;
            receivedMessageY[receivedMessagesCount] = y;
            receivedMessages[receivedMessagesCount++] = character.message;
        }
        if (character.bubble_timeout > 0) {
            itemAboveHeadX[itemsAboveHeadCount] = x + k / 2;
            itemAboveHeadY[itemsAboveHeadCount] = y;
            itemAboveHeadScale[itemsAboveHeadCount] = k1;
            itemAboveHeadID[itemsAboveHeadCount++] = character.bubble_id;
        }
        if (character.animation_current == 8 || character.animation_current == 9 || character.combat_timer != 0) {
            if (character.combat_timer > 0) {
                int i3 = x;
                if (character.animation_current == 8)
                    i3 -= (20 * k1) / 100;
                else if (character.animation_current == 9)
                    i3 += (20 * k1) / 100;
                int i4 = (character.health_current * 30) / character.health_max;
                healthBarX[healthBarCount] = i3 + k / 2;
                healthBarY[healthBarCount] = y;
                healthBarMissing[healthBarCount++] = i4;
            }
            if (character.combat_timer > 150) {
                int j3 = x;
                if (character.animation_current == 8)
                    j3 -= (10 * k1) / 100;
                else if (character.animation_current == 9)
                    j3 += (10 * k1) / 100;
                surface.draw_picture((j3 + k / 2) - 12, (y + l / 2) - 12, sprite_media + 11);
                surface.drawstring_center(String.valueOf(character.damage_taken), (j3 + k / 2) - 1, y + l / 2 + 5, 3, 0xffffff);
            }
        }
        if (character.bubble_visible == 1 && character.bubble_timeout == 0) {
            int k3 = j1 + x + k / 2;
            if (character.animation_current == 8)
                k3 -= (20 * k1) / 100;
            else if (character.animation_current == 9)
                k3 += (20 * k1) / 100;
            int j4 = (16 * k1) / 100;
            int l4 = (16 * k1) / 100;
            surface.sprite_clipping(k3 - j4 / 2, y - l4 / 2 - (10 * k1) / 100, j4, l4, sprite_media + 13);
        }
    }

    private final void loadMedia() {
        byte media[] = readDataFile("media" + Version.MEDIA + ".jag", "2d graphics", 20);
        if (media == null) {
            error_loading_data = true;
            return;
        }
        byte buff[] = Utility.loadData("index.dat", 0, media);
        surface.load_sprite(sprite_media, Utility.loadData("inv1.dat", 0, media), buff, 1);
        surface.load_sprite(sprite_media + 1, Utility.loadData("inv2.dat", 0, media), buff, 6);
        surface.load_sprite(sprite_media + 9, Utility.loadData("bubble.dat", 0, media), buff, 1);
        surface.load_sprite(sprite_media + 10, Utility.loadData("runescape.dat", 0, media), buff, 1);
        surface.load_sprite(sprite_media + 11, Utility.loadData("splat.dat", 0, media), buff, 3);
        surface.load_sprite(sprite_media + 14, Utility.loadData("icon.dat", 0, media), buff, 8);
        surface.load_sprite(sprite_media + 22, Utility.loadData("hbar.dat", 0, media), buff, 1);
        surface.load_sprite(sprite_media + 23, Utility.loadData("hbar2.dat", 0, media), buff, 1);
        surface.load_sprite(sprite_media + 24, Utility.loadData("compass.dat", 0, media), buff, 1);
        surface.load_sprite(sprite_media + 25, Utility.loadData("buttons.dat", 0, media), buff, 2);
        surface.load_sprite(sprite_util, Utility.loadData("scrollbar.dat", 0, media), buff, 2);
        surface.load_sprite(sprite_util + 2, Utility.loadData("corners.dat", 0, media), buff, 4);
        surface.load_sprite(sprite_util + 6, Utility.loadData("arrows.dat", 0, media), buff, 2);
        surface.load_sprite(sprite_projectile, Utility.loadData("projectile.dat", 0, media), buff, GameData.projectile_sprite);
        int i = GameData.item_sprite_count;
        for (int j = 1; i > 0; j++) {
            int k = i;
            i -= 30;
            if (k > 30)
                k = 30;
            surface.load_sprite(sprite_item + (j - 1) * 30, Utility.loadData("objects" + j + ".dat", 0, media), buff, k);
        }

        surface.method227(sprite_media);
        surface.method227(sprite_media + 9);
        for (int l = 11; l <= 26; l++)
            surface.method227(sprite_media + l);

        for (int i1 = 0; i1 < GameData.projectile_sprite; i1++)
            surface.method227(sprite_projectile + i1);

        for (int j1 = 0; j1 < GameData.item_sprite_count; j1++)
            surface.method227(sprite_item + j1);

    }

    private final void drawChatMessageTabs() {
        surface.draw_picture(0, game_height - 4, sprite_media + 23);
        int col = Surface.rgb2long(200, 200, 255);
        if (messageTabSelected == 0)
            col = Surface.rgb2long(255, 200, 50);
        if (messageTabFlashAll % 30 > 15)
            col = Surface.rgb2long(255, 50, 50);
        surface.drawstring_center("All messages", 54, game_height + 6, 0, col);
        col = Surface.rgb2long(200, 200, 255);
        if (messageTabSelected == 1)
            col = Surface.rgb2long(255, 200, 50);
        if (messageTabFlashHistory % 30 > 15)
            col = Surface.rgb2long(255, 50, 50);
        surface.drawstring_center("Chat history", 155, game_height + 6, 0, col);
        col = Surface.rgb2long(200, 200, 255);
        if (messageTabSelected == 2)
            col = Surface.rgb2long(255, 200, 50);
        if (messtageTabFlashQuest % 30 > 15)
            col = Surface.rgb2long(255, 50, 50);
        surface.drawstring_center("Quest history", 255, game_height + 6, 0, col);
        col = Surface.rgb2long(200, 200, 255);
        if (messageTabSelected == 3)
            col = Surface.rgb2long(255, 200, 50);
        if (messageTabFlashPrivate % 30 > 15)
            col = Surface.rgb2long(255, 50, 50);
        surface.drawstring_center("Private history", 355, game_height + 6, 0, col);
        surface.drawstring_center("Report abuse", 457, game_height + 6, 0, 0xffffff);
    }

    protected final void startThread(Runnable runnable) {
        if (link.mainapp != null) {
            link.startthread(runnable);
            return;
        } else {
            Thread thread = new Thread(runnable);
            thread.setDaemon(true);
            thread.start();
            return;
        }
    }

    protected final void startGame() {
        if (appletMode) {
            String s = getDocumentBase().getHost().toLowerCase();
            if (!s.endsWith("jagex.com") && !s.endsWith("jagex.co.uk") && !s.endsWith("runescape.com") && !s.endsWith("runescape.co.uk") && !s.endsWith("runescape.net") && !s.endsWith("runescape.org") && !s.endsWith("penguin") && !s.endsWith("puffin")) {
                errorLoadingCodebase = true;
                return;
            }
        }
        int i = 0;
        for (int j = 0; j < 99; j++) {
            int k = j + 1;
            int i1 = (int) ((double) k + 300D * Math.pow(2D, (double) k / 7D));
            i += i1;
            experience_array[j] = i & 0xffffffc;
        }

        try {
            String s1 = getParameter("referid");
            referid = Integer.parseInt(s1);
        } catch (Exception _ex) {
        }
        try {
            String s2 = getParameter("member");
            int j1 = Integer.parseInt(s2);
            if (j1 == 1)
                members = true;
        } catch (Exception _ex) {
        }
        if (appletMode)
            super.port = 43594;
        super.yOffset = 0;
        GameConnection.maxReadTries = 1000;
        GameConnection.clientVersion = Version.CLIENT;
        try {
            String s3 = getParameter("poff");
            int k1 = Integer.parseInt(s3);
            super.port += k1;
            System.out.println("Offset: " + k1);
        } catch (Exception _ex) {
        }
        load_game_config();
        if (error_loading_data)
            return;
        sprite_media = 2000;
        sprite_util = sprite_media + 100;
        sprite_item = sprite_util + 50;
        sprite_logo = sprite_item + 1000;
        sprite_projectile = sprite_logo + 10;
        sprite_texture = sprite_projectile + 50;
        sprite_texture_world = sprite_texture + 10;
        graphics = getGraphics();
        method8(50);
        surface = new Surface_Sprite(game_width, game_height + 12, 4000, this);
        surface.mudclientref = this;
        surface.set_bounds(0, 0, game_width, game_height + 12);
        Panel.draw_background_arrow = false;
        Panel.baseSpriteStart = sprite_util;
        panel_magic = new Panel(surface, 5);
        int x = surface.width2 - 199;
        byte y = 36;
        control_magicpanel = panel_magic.add_text_list_interactive(x, y + 24, 196, 90, 1, 500, true);
        panel_social = new Panel(surface, 5);
        control_socialpanel = panel_social.add_text_list_interactive(x, y + 40, 196, 126, 1, 500, true);
        panel_playerinfo = new Panel(surface, 5);
        control_playerinfopanel = panel_playerinfo.add_text_list_interactive(x, y + 24, 196, 251, 1, 500, true);
        loadMedia();
        if (error_loading_data)
            return;
        loadEntities();
        if (error_loading_data)
            return;
        scene = new Scene(surface, 15000, 15000, 1000);
        scene.set_midpoints(game_width / 2, game_height / 2, game_width / 2, game_height / 2, game_width, const_9);
        scene.clip_far_3d = 2400;
        scene.clip_far_2d = 2400;
        scene.fog_z_falloff = 1;
        scene.fog_z_distance = 2300;
        scene.method303(-50, -10, -50);
        world = new World(scene, surface);
        world.baseMediaSprite = sprite_media;
        load_textures();
        if (error_loading_data)
            return;
        loadModels();
        if (error_loading_data)
            return;
        loadMaps();
        if (error_loading_data)
            return;
        if (members)
            loadSounds();
        if (!error_loading_data) {
            showLoadingProgress(100, "Starting game...");
            createMessageTabPanel();
            create_login_panels();
            create_appearance_panel();
            reset_login_screen_variables();
            render_login_screen_viewports();
        }
    }

    private final void draw_ui_tab_magic(boolean nomenus) {
        int ui_x = surface.width2 - 199;
        int ui_y = 36;
        surface.draw_picture(ui_x - 49, 3, sprite_media + 4);
        int ui_width = 196;// '\304';
        int ui_height = 182;// '\266';
        int l;
        int k = l = Surface.rgb2long(160, 160, 160);
        if (tabMagicPrayer == 0)
            k = Surface.rgb2long(220, 220, 220);
        else
            l = Surface.rgb2long(220, 220, 220);
        surface.draw_box_alpha(ui_x, ui_y, ui_width / 2, 24, k, 128);
        surface.draw_box_alpha(ui_x + ui_width / 2, ui_y, ui_width / 2, 24, l, 128);
        surface.draw_box_alpha(ui_x, ui_y + 24, ui_width, 90, Surface.rgb2long(220, 220, 220), 128);
        surface.draw_box_alpha(ui_x, ui_y + 24 + 90, ui_width, ui_height - 90 - 24, Surface.rgb2long(160, 160, 160), 128);
        surface.draw_line_horiz(ui_x, ui_y + 24, ui_width, 0);
        surface.draw_line_vert(ui_x + ui_width / 2, ui_y, 24, 0);
        surface.draw_line_horiz(ui_x, ui_y + 113, ui_width, 0);
        surface.drawstring_center("Magic", ui_x + ui_width / 4, ui_y + 16, 4, 0);
        surface.drawstring_center("Prayers", ui_x + ui_width / 4 + ui_width / 2, ui_y + 16, 4, 0);
        if (tabMagicPrayer == 0) {
            panel_magic.clear_list(control_magicpanel);
            int i1 = 0;
            for (int spell = 0; spell < GameData.spell_count; spell++) {
                String s = "@yel@";
                for (int rune = 0; rune < GameData.spell_runes_required[spell]; rune++) {
                    int k4 = GameData.spell_runes_id[spell][rune];
                    if (hasInventoryItems(k4, GameData.spell_runes_count[spell][rune]))
                        continue;
                    s = "@whi@";
                    break;
                }

                int l4 = player_stat_current[6];
                if (GameData.spell_level[spell] > l4)
                    s = "@bla@";
                panel_magic.add_list_entry(control_magicpanel, i1++, s + "Level " + GameData.spell_level[spell] + ": " + GameData.spell_name[spell]);
            }

            panel_magic.drawPanel();
            int i3 = panel_magic.get_list_entry_index(control_magicpanel);
            if (i3 != -1) {
                surface.drawstring("Level " + GameData.spell_level[i3] + ": " + GameData.spell_name[i3], ui_x + 2, ui_y + 124, 1, 0xffff00);
                surface.drawstring(GameData.spell_description[i3], ui_x + 2, ui_y + 136, 0, 0xffffff);
                for (int i4 = 0; i4 < GameData.spell_runes_required[i3]; i4++) {
                    int i5 = GameData.spell_runes_id[i3][i4];
                    surface.draw_picture(ui_x + 2 + i4 * 44, ui_y + 150, sprite_item + GameData.item_picture[i5]);
                    int j5 = getInventoryCount(i5);
                    int k5 = GameData.spell_runes_count[i3][i4];
                    String s2 = "@red@";
                    if (hasInventoryItems(i5, k5))
                        s2 = "@gre@";
                    surface.drawstring(s2 + j5 + "/" + k5, ui_x + 2 + i4 * 44, ui_y + 150, 1, 0xffffff);
                }

            } else {
                surface.drawstring("Point at a spell for a description", ui_x + 2, ui_y + 124, 1, 0);
            }
        }
        if (tabMagicPrayer == 1) {
            panel_magic.clear_list(control_magicpanel);
            int j1 = 0;
            for (int j2 = 0; j2 < GameData.prayer_count; j2++) {
                String s1 = "@whi@";
                if (GameData.prayer_level[j2] > player_stat_base[5])
                    s1 = "@bla@";
                if (prayerOn[j2])
                    s1 = "@gre@";
                panel_magic.add_list_entry(control_magicpanel, j1++, s1 + "Level " + GameData.prayer_level[j2] + ": " + GameData.prayer_name[j2]);
            }

            panel_magic.drawPanel();
            int j3 = panel_magic.get_list_entry_index(control_magicpanel);
            if (j3 != -1) {
                surface.drawstring_center("Level " + GameData.prayer_level[j3] + ": " + GameData.prayer_name[j3], ui_x + ui_width / 2, ui_y + 130, 1, 0xffff00);
                surface.drawstring_center(GameData.prayer_description[j3], ui_x + ui_width / 2, ui_y + 145, 0, 0xffffff);
                surface.drawstring_center("Drain rate: " + GameData.prayer_drain[j3], ui_x + ui_width / 2, ui_y + 160, 1, 0);
            } else {
                surface.drawstring("Point at a prayer for a description", ui_x + 2, ui_y + 124, 1, 0);
            }
        }
        if (!nomenus)
            return;
        int mouse_x = super.mouseX - (surface.width2 - 199);
        int mouse_y = super.mouseY - 36;
        if (mouse_x >= 0 && mouse_y >= 0 && mouse_x < 196 && mouse_y < 182) {
            panel_magic.handle_mouse(mouse_x + (surface.width2 - 199), mouse_y + 36, super.lastMouseButtonDown, super.mouseButtonDown);
            if (mouse_y <= 24 && mouseButtonClick == 1)
                if (mouse_x < 98 && tabMagicPrayer == 1) {
                    tabMagicPrayer = 0;
                    panel_magic.reset_list_props(control_magicpanel);
                } else if (mouse_x > 98 && tabMagicPrayer == 0) {
                    tabMagicPrayer = 1;
                    panel_magic.reset_list_props(control_magicpanel);
                }
            if (mouseButtonClick == 1 && tabMagicPrayer == 0) {
                int idx = panel_magic.get_list_entry_index(control_magicpanel);
                if (idx != -1) {
                    int k2 = player_stat_current[6];
                    if (GameData.spell_level[idx] > k2) {
                        show_message("Your magic ability is not high enough for this spell", 3);
                    } else {
                        int k3;
                        for (k3 = 0; k3 < GameData.spell_runes_required[idx]; k3++) {
                            int j4 = GameData.spell_runes_id[idx][k3];
                            if (hasInventoryItems(j4, GameData.spell_runes_count[idx][k3]))
                                continue;
                            show_message("You don't have all the reagents you need for this spell", 3);
                            k3 = -1;
                            break;
                        }

                        if (k3 == GameData.spell_runes_required[idx]) {
                            selectedSpell = idx;
                            selectedItemInventoryIndex = -1;
                        }
                    }
                }
            }
            if (mouseButtonClick == 1 && tabMagicPrayer == 1) {
                int l1 = panel_magic.get_list_entry_index(control_magicpanel);
                if (l1 != -1) {
                    int l2 = player_stat_base[5];
                    if (GameData.prayer_level[l1] > l2)
                        show_message("Your prayer ability is not high enough for this prayer", 3);
                    else if (player_stat_current[5] == 0)
                        show_message("You have run out of prayer points. Return to a church to recharge", 3);
                    else if (prayerOn[l1]) {
                        super.clientStream.newPacket(254);
                        super.clientStream.addByte(l1);
                        super.clientStream.sendPacket();
                        prayerOn[l1] = false;
                        playSoundFile("prayeroff");
                    } else {
                        super.clientStream.newPacket(60);
                        super.clientStream.addByte(l1);
                        super.clientStream.sendPacket();
                        prayerOn[l1] = true;
                        playSoundFile("prayeron");
                    }
                }
            }
            mouseButtonClick = 0;
        }
    }

    private final void draw_dialog_shop() {
        if (mouseButtonClick != 0) {
            mouseButtonClick = 0;
            int mouse_x = super.mouseX - 52;
            int mouse_y = super.mouseY - 44;
            if (mouse_x >= 0 && mouse_y >= 12 && mouse_x < 408 && mouse_y < 246) {
                int item_index = 0;
                for (int row = 0; row < 5; row++) {
                    for (int col = 0; col < 8; col++) {
                        int slot_x = 7 + col * 49;
                        int slot_y = 28 + row * 34;
                        if (mouse_x > slot_x && mouse_x < slot_x + 49 && mouse_y > slot_y && mouse_y < slot_y + 34 && shop_item[item_index] != -1) {
                            shop_selected_item_index = item_index;
                            shop_selected_item_type = shop_item[item_index];
                        }
                        item_index++;
                    }

                }

                if (shop_selected_item_index >= 0) {
                    int item_type = shop_item[shop_selected_item_index];
                    if (item_type != -1) {
                        if (shop_item_count[shop_selected_item_index] > 0 && mouse_x > 298 && mouse_y >= 204 && mouse_x < 408 && mouse_y <= 215) {
                            int price_mod = shop_buy_price_mod + shop_item_price[shop_selected_item_index];
                            if (price_mod < 10)
                                price_mod = 10;
                            int item_price = (price_mod * GameData.item_base_price[item_type]) / 100;
                            super.clientStream.newPacket(236);
                            super.clientStream.addShort(shop_item[shop_selected_item_index]);
                            super.clientStream.addInt(item_price);
                            super.clientStream.sendPacket();
                        }
                        if (getInventoryCount(item_type) > 0 && mouse_x > 2 && mouse_y >= 229 && mouse_x < 112 && mouse_y <= 240) {
                            int price_mod = shop_sell_price_mod + shop_item_price[shop_selected_item_index];
                            if (price_mod < 10)
                                price_mod = 10;
                            int item_price = (price_mod * GameData.item_base_price[item_type]) / 100;
                            super.clientStream.newPacket(221);
                            super.clientStream.addShort(shop_item[shop_selected_item_index]);
                            super.clientStream.addInt(item_price);
                            super.clientStream.sendPacket();
                        }
                    }
                }
            } else {
                super.clientStream.newPacket(166);
                super.clientStream.sendPacket();
                show_dialog_shop = false;
                return;
            }
        }
        byte dialog_x = 52;
        byte dialog_y = 44;
        surface.draw_box(dialog_x, dialog_y, 408, 12, 192);
        surface.draw_box_alpha(dialog_x, dialog_y + 12, 408, 17, 0x989898, 160);
        surface.draw_box_alpha(dialog_x, dialog_y + 29, 8, 170, 0x989898, 160);
        surface.draw_box_alpha(dialog_x + 399, dialog_y + 29, 9, 170, 0x989898, 160);
        surface.draw_box_alpha(dialog_x, dialog_y + 199, 408, 47, 0x989898, 160);
        surface.drawstring("Buying and selling items", dialog_x + 1, dialog_y + 10, 1, 0xffffff);
        int colour = 0xffffff;
        if (super.mouseX > dialog_x + 320 && super.mouseY >= dialog_y && super.mouseX < dialog_x + 408 && super.mouseY < dialog_y + 12)
            colour = 0xff0000;
        surface.drawstring_right("Close window", dialog_x + 406, dialog_y + 10, 1, colour);
        surface.drawstring("Shops stock in green", dialog_x + 2, dialog_y + 24, 1, 65280);
        surface.drawstring("Number you own in blue", dialog_x + 135, dialog_y + 24, 1, 65535);
        surface.drawstring("Your money: " + getInventoryCount(10) + "gp", dialog_x + 280, dialog_y + 24, 1, 0xffff00);
        int item_index = 0;
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 8; col++) {
                int slot_x = dialog_x + 7 + col * 49;
                int slot_y = dialog_y + 28 + row * 34;
                if (shop_selected_item_index == item_index)
                    surface.draw_box_alpha(slot_x, slot_y, 49, 34, 0xff0000, 160);
                else
                    surface.draw_box_alpha(slot_x, slot_y, 49, 34, 0xd0d0d0, 160);
                surface.draw_box_edge(slot_x, slot_y, 50, 35, 0);
                if (shop_item[item_index] != -1) {
                    surface.sprite_clipping(slot_x, slot_y, 48, 32, sprite_item + GameData.item_picture[shop_item[item_index]], GameData.item_mask[shop_item[item_index]], 0, 0, false);
                    surface.drawstring(String.valueOf(shop_item_count[item_index]), slot_x + 1, slot_y + 10, 1, 65280);
                    surface.drawstring_right(String.valueOf(getInventoryCount(shop_item[item_index])), slot_x + 47, slot_y + 10, 1, 65535);
                }
                item_index++;
            }

        }

        surface.draw_line_horiz(dialog_x + 5, dialog_y + 222, 398, 0);
        if (shop_selected_item_index == -1) {
            surface.drawstring_center("Select an object to buy or sell", dialog_x + 204, dialog_y + 214, 3, 0xffff00);
            return;
        }
        int selected_item_type = shop_item[shop_selected_item_index];
        if (selected_item_type != -1) {
            if (shop_item_count[shop_selected_item_index] > 0) {
                int price_mod = shop_buy_price_mod + shop_item_price[shop_selected_item_index];
                if (price_mod < 10)
                    price_mod = 10;
                int item_price = (price_mod * GameData.item_base_price[selected_item_type]) / 100;
                surface.drawstring("Buy a new " + GameData.item_name[selected_item_type] + " for " + item_price + "gp", dialog_x + 2, dialog_y + 214, 1, 0xffff00);
                colour = 0xffffff;
                if (super.mouseX > dialog_x + 298 && super.mouseY >= dialog_y + 204 && super.mouseX < dialog_x + 408 && super.mouseY <= dialog_y + 215)
                    colour = 0xff0000;
                surface.drawstring_right("Click here to buy", dialog_x + 405, dialog_y + 214, 3, colour);
            } else {
                surface.drawstring_center("This item is not currently available to buy", dialog_x + 204, dialog_y + 214, 3, 0xffff00);
            }
            if (getInventoryCount(selected_item_type) > 0) {
                int price_mod = shop_sell_price_mod + shop_item_price[shop_selected_item_index];
                if (price_mod < 10)
                    price_mod = 10;
                int item_price = (price_mod * GameData.item_base_price[selected_item_type]) / 100;
                surface.drawstring_right("Sell your " + GameData.item_name[selected_item_type] + " for " + item_price + "gp", dialog_x + 405, dialog_y + 239, 1, 0xffff00);
                colour = 0xffffff;
                if (super.mouseX > dialog_x + 2 && super.mouseY >= dialog_y + 229 && super.mouseX < dialog_x + 112 && super.mouseY <= dialog_y + 240)
                    colour = 0xff0000;
                surface.drawstring("Click here to sell", dialog_x + 2, dialog_y + 239, 3, colour);
                return;
            }
            surface.drawstring_center("You do not have any of this item to sell", dialog_x + 204, dialog_y + 239, 3, 0xffff00);
        }
    }

    private final boolean hasInventoryItems(int id, int mincount) {
        if (id == 31 && (isItemEquipped(197) || isItemEquipped(615) || isItemEquipped(682)))
            return true;
        if (id == 32 && (isItemEquipped(102) || isItemEquipped(616) || isItemEquipped(683)))
            return true;
        if (id == 33 && (isItemEquipped(101) || isItemEquipped(617) || isItemEquipped(684)))
            return true;
        if (id == 34 && (isItemEquipped(103) || isItemEquipped(618) || isItemEquipped(685)))
            return true;
        return getInventoryCount(id) >= mincount;
    }

    private final String getHostnameIP(int i) // and this? re: vvvv
    {
        if (link.mainapp != null)
            return link.gethostname(Utility.ip2string(i));
        else
            return Utility.ip2string(i);
    }

    private static final String formatNumber(int i) // wonder why this wasn't in the utility class
    {
        String s = String.valueOf(i);
        for (int j = s.length() - 3; j > 0; j -= 3)
            s = s.substring(0, j) + "," + s.substring(j);

        if (s.length() > 8)
            s = "@gre@" + s.substring(0, s.length() - 8) + " million @whi@(" + s + ")";
        else if (s.length() > 4)
            s = "@cya@" + s.substring(0, s.length() - 4) + "K @whi@(" + s + ")";
        return s;
    }

    protected final void cant_logout() {
        logout_timeout = 0;
        show_message("@cya@Sorry, you can't logout at the moment", 3);
    }

    private final void draw_game() {
        if (death_screen_timeout != 0) {
            surface.fade2black();
            surface.drawstring_center("Oh dear! You are dead...", game_width / 2, game_height / 2, 7, 0xff0000);
            drawChatMessageTabs();
            surface.draw(graphics, 0, 0);
            return;
        }
        if (showAppearanceChange) {
            drawAppearancePanelCharacterSprites();
            return;
        }
        if (isSleeping) {
            surface.fade2black();
            if (Math.random() < 0.14999999999999999D)
                surface.drawstring_center("ZZZ", (int) (Math.random() * 80D), (int) (Math.random() * 334D), 5, (int) (Math.random() * 16777215D));
            if (Math.random() < 0.14999999999999999D)
                surface.drawstring_center("ZZZ", 512 - (int) (Math.random() * 80D), (int) (Math.random() * 334D), 5, (int) (Math.random() * 16777215D));
            surface.draw_box(game_width / 2 - 100, 160, 200, 40, 0);
            surface.drawstring_center("You are sleeping", game_width / 2, 50, 7, 0xffff00);
            surface.drawstring_center("Fatigue: " + (fatigueSleeping * 100) / 750 + "%", game_width / 2, 90, 7, 0xffff00);
            surface.drawstring_center("When you want to wake up just use your", game_width / 2, 140, 5, 0xffffff);
            surface.drawstring_center("keyboard to type the word in the box below", game_width / 2, 160, 5, 0xffffff);
            surface.drawstring_center(super.input_text_current + "*", game_width / 2, 180, 5, 65535);
            if (sleepingStatusText == null)
                surface.draw_picture(game_width / 2 - 127, 230, sprite_texture + 1);
            else
                surface.drawstring_center(sleepingStatusText, game_width / 2, 260, 5, 0xff0000);
            surface.draw_box_edge(game_width / 2 - 128, 229, 257, 42, 0xffffff);
            drawChatMessageTabs();
            surface.drawstring_center("If you can't read the word", game_width / 2, 290, 1, 0xffffff);
            surface.drawstring_center("@yel@click here@whi@ to get a different one", game_width / 2, 305, 1, 0xffffff);
            surface.draw(graphics, 0, 0);
            return;
        }
        if (!world.playerAlive)
            return;
        for (int i = 0; i < 64; i++) {
            scene.free_model(world.aGameModelArrayArray583[lastHeightOffset][i]);
            if (lastHeightOffset == 0) {
                scene.free_model(world.aGameModelArrayArray577[1][i]);
                scene.free_model(world.aGameModelArrayArray583[1][i]);
                scene.free_model(world.aGameModelArrayArray577[2][i]);
                scene.free_model(world.aGameModelArrayArray583[2][i]);
            }
            fogOfWar = true;
            if (lastHeightOffset == 0 && (world.object_adjacency[local_player.currentX / 128][local_player.currentY / 128] & 128) == 0) {// 0x80
                scene.add_model(world.aGameModelArrayArray583[lastHeightOffset][i]);
                if (lastHeightOffset == 0) {
                    scene.add_model(world.aGameModelArrayArray577[1][i]);
                    scene.add_model(world.aGameModelArrayArray583[1][i]);
                    scene.add_model(world.aGameModelArrayArray577[2][i]);
                    scene.add_model(world.aGameModelArrayArray583[2][i]);
                }
                fogOfWar = false;
            }
        }

        if (objectAnimationNumberFireLightningSpell != lastObjectAnimationNumberFireLightningSpell) {
            lastObjectAnimationNumberFireLightningSpell = objectAnimationNumberFireLightningSpell;
            for (int j = 0; j < object_count; j++) {
                if (object_id[j] == 97)
                    updateObjectAnimation(j, "firea" + (objectAnimationNumberFireLightningSpell + 1));
                if (object_id[j] == 274)
                    updateObjectAnimation(j, "fireplacea" + (objectAnimationNumberFireLightningSpell + 1));
                if (object_id[j] == 1031)
                    updateObjectAnimation(j, "lightning" + (objectAnimationNumberFireLightningSpell + 1));
                if (object_id[j] == 1036)
                    updateObjectAnimation(j, "firespell" + (objectAnimationNumberFireLightningSpell + 1));
                if (object_id[j] == 1147)
                    updateObjectAnimation(j, "spellcharge" + (objectAnimationNumberFireLightningSpell + 1));
            }

        }
        if (objectAnimationNumberTorch != lastObjectAnimationNumberTorch) {
            lastObjectAnimationNumberTorch = objectAnimationNumberTorch;
            for (int k = 0; k < object_count; k++) {
                if (object_id[k] == 51)
                    updateObjectAnimation(k, "torcha" + (objectAnimationNumberTorch + 1));
                if (object_id[k] == 143)
                    updateObjectAnimation(k, "skulltorcha" + (objectAnimationNumberTorch + 1));
            }

        }
        if (objectAnimationNumberClaw != lastOjectAnimationNumberClaw) {
            lastOjectAnimationNumberClaw = objectAnimationNumberClaw;
            for (int l = 0; l < object_count; l++)
                if (object_id[l] == 1142)
                    updateObjectAnimation(l, "clawspell" + (objectAnimationNumberClaw + 1));

        }
        scene.reduce_sprites(sprite_count);
        sprite_count = 0;
        for (int i1 = 0; i1 < player_count; i1++) {
            Character character = players[i1];
            if (character.colour_bottom != 255) {
                int x = character.currentX;
                int y = character.currentY;
                int elev = -world.get_elevation(x, y);
                int id = scene.draw_sprite(5000 + i1, x, elev, y, 145, 220, i1 + 10000);
                sprite_count++;
                if (character == local_player)
                    scene.method269(id);
                if (character.animation_current == 8)
                    scene.method270(id, -30);
                if (character.animation_current == 9)
                    scene.method270(id, 30);
            }
        }

        for (int i = 0; i < player_count; i++) {
            Character player = players[i];
            if (player.projectile_range > 0) {
                Character character = null;
                if (player.attacking_npc_server_index != -1)
                    character = npcs_server[player.attacking_npc_server_index];
                else if (player.attacking_player_server_index != -1)
                    character = player_server[player.attacking_player_server_index];
                if (character != null) {
                    int sx = player.currentX;
                    int sy = player.currentY;
                    int selev = -world.get_elevation(sx, sy) - 110;
                    int dx = character.currentX;
                    int dy = character.currentY;
                    int delev = -world.get_elevation(dx, dy) - GameData.npc_something_2[character.npc_id] / 2;
                    int rx = (sx * player.projectile_range + dx * (projectile_max_range - player.projectile_range)) / projectile_max_range;
                    int rz = (selev * player.projectile_range + delev * (projectile_max_range - player.projectile_range)) / projectile_max_range;
                    int ry = (sy * player.projectile_range + dy * (projectile_max_range - player.projectile_range)) / projectile_max_range;
                    scene.draw_sprite(sprite_projectile + player.incoming_projectile_sprite, rx, rz, ry, 32, 32, 0);
                    sprite_count++;
                }
            }
        }

        for (int i = 0; i < npc_count; i++) {
            Character character_3 = npcs[i];
            int i3 = character_3.currentX;
            int j4 = character_3.currentY;
            int i7 = -world.get_elevation(i3, j4);
            int i9 = scene.draw_sprite(20000 + i, i3, i7, j4, GameData.npc_something_1[character_3.npc_id], GameData.npc_something_2[character_3.npc_id], i + 30000);
            sprite_count++;
            if (character_3.animation_current == 8)
                scene.method270(i9, -30);
            if (character_3.animation_current == 9)
                scene.method270(i9, 30);
        }

        for (int i = 0; i < ground_item_count; i++) {
            int x = ground_item_x[i] * magicLoc + 64;
            int y = ground_item_y[i] * magicLoc + 64;
            scene.draw_sprite(40000 + ground_item_id[i], x, -world.get_elevation(x, y) - ground_item_z[i], y, 96, 64, i + 20000);
            sprite_count++;
        }

        for (int i = 0; i < teleport_bubble_count; i++) {
            int l4 = teleport_bubble_x[i] * magicLoc + 64;
            int j7 = teleport_bubble_y[i] * magicLoc + 64;
            int j9 = teleport_bubble_type[i];
            if (j9 == 0) {
                scene.draw_sprite(50000 + i, l4, -world.get_elevation(l4, j7), j7, 128, 256, i + 50000);
                sprite_count++;
            }
            if (j9 == 1) {
                scene.draw_sprite(50000 + i, l4, -world.get_elevation(l4, j7), j7, 128, 64, i + 50000);
                sprite_count++;
            }
        }

        surface.interlace = false;
        surface.black_screen();
        surface.interlace = super.interlace;
        if (lastHeightOffset == 3) {
            int i5 = 40 + (int) (Math.random() * 3D);
            int k7 = 40 + (int) (Math.random() * 7D);
            scene.method304(i5, k7, -50, -10, -50);
        }
        itemsAboveHeadCount = 0;
        receivedMessagesCount = 0;
        healthBarCount = 0;
        if (cameraAutoAngleDebug) {
            if (optionCameraModeAuto && !fogOfWar) {
                int j5 = camera_angle;
                autorotate_camera();
                if (camera_angle != j5) {
                    cameraAutoRotatePlayerX = local_player.currentX;
                    cameraAutoRotatePlayerY = local_player.currentY;
                }
            }
            scene.clip_far_3d = 3000;
            scene.clip_far_2d = 3000;
            scene.fog_z_falloff = 1;
            scene.fog_z_distance = 2800;
            cameraRotation = camera_angle * 32;
            int x = cameraAutoRotatePlayerX + camera_rotation_x;
            int y = cameraAutoRotatePlayerY + camera_rotation_y;
            scene.set_camera(x, -world.get_elevation(x, y), y, 912, cameraRotation * 4, 0, 2000);
        } else {
            if (optionCameraModeAuto && !fogOfWar)
                autorotate_camera();
            if (!super.interlace) {
                scene.clip_far_3d = 2400;
                scene.clip_far_2d = 2400;
                scene.fog_z_falloff = 1;
                scene.fog_z_distance = 2300;
            } else {
                scene.clip_far_3d = 2200;
                scene.clip_far_2d = 2200;
                scene.fog_z_falloff = 1;
                scene.fog_z_distance = 2100;
            }
            int x = cameraAutoRotatePlayerX + camera_rotation_x;
            int y = cameraAutoRotatePlayerY + camera_rotation_y;
            scene.set_camera(x, -world.get_elevation(x, y), y, 912, cameraRotation * 4, 0, cameraZoom * 2);
        }
        scene.endscene();
        drawAboveHeadStuff();
        if (mouseClickXStep > 0)
            surface.draw_picture(mouseClickXX - 8, mouseClickXY - 8, sprite_media + 14 + (24 - mouseClickXStep) / 6);
        if (mouseClickXStep < 0)
            surface.draw_picture(mouseClickXX - 8, mouseClickXY - 8, sprite_media + 18 + (24 + mouseClickXStep) / 6);
        if (systemUpdate != 0) {
            int i6 = systemUpdate / 50;
            int j8 = i6 / 60;
            i6 %= 60;
            if (i6 < 10)
                surface.drawstring_center("System update in: " + j8 + ":0" + i6, 256, game_height - 7, 1, 0xffff00);
            else
                surface.drawstring_center("System update in: " + j8 + ":" + i6, 256, game_height - 7, 1, 0xffff00);
        }
        if (!loading_area) {
            int j6 = 2203 - (region_y + plane_height + areaY);
            if (region_x + plane_width + areaX >= 2640)
                j6 = -50;
            if (j6 > 0) {
                int wildlvl = 1 + j6 / 6;
                surface.draw_picture(453, game_height - 56, sprite_media + 13);
                surface.drawstring_center("Wilderness", 465, game_height - 20, 1, 0xffff00);
                surface.drawstring_center("Level: " + wildlvl, 465, game_height - 7, 1, 0xffff00);
                if (show_ui_wild_warn == 0)
                    show_ui_wild_warn = 2;
            }
            if (show_ui_wild_warn == 0 && j6 > -10 && j6 <= 0)
                show_ui_wild_warn = 1;
        }
        if (messageTabSelected == 0) {
            for (int k6 = 0; k6 < 5; k6++)
                if (messageHistoryTimeout[k6] > 0) {
                    String s = messageHistory[k6];
                    surface.drawstring(s, 7, game_height - 18 - k6 * 12, 1, 0xffff00);
                }

        }
        panelMessageTabs.hide(controlTextListChat);
        panelMessageTabs.hide(controlTextListQuest);
        panelMessageTabs.hide(controlTextListPrivate);
        if (messageTabSelected == 1)
            panelMessageTabs.show(controlTextListChat);
        else if (messageTabSelected == 2)
            panelMessageTabs.show(controlTextListQuest);
        else if (messageTabSelected == 3)
            panelMessageTabs.show(controlTextListPrivate);
        Panel.text_list_entry_height_mod = 2;
        panelMessageTabs.drawPanel();
        Panel.text_list_entry_height_mod = 0;
        surface.method232(surface.width2 - 3 - 197, 3, sprite_media, 128);
        draw_ui();
        surface.loggedIn = false;
        drawChatMessageTabs();
        surface.draw(graphics, 0, 0);
    }

    private final void loadSounds() {
        try {
            soundData = readDataFile("sounds" + Version.SOUNDS + ".mem", "Sound effects", 90);
            audioPlayer = new StreamAudioPlayer();
            return;
        } catch (Throwable throwable) {
            System.out.println("Unable to init sounds:" + throwable);
        }
    }

    private final boolean isItemEquipped(int i) {
        for (int j = 0; j < inventory_items_count; j++)
            if (inventory_item_id[j] == i && inventory_equipped[j] == 1)
                return true;

        return false;
    }

    private final void loadEntities() {
        byte entity_buff[] = null;
        byte index_dat[] = null;
        entity_buff = readDataFile("entity" + Version.ENTITY + ".jag", "people and monsters", 30);
        if (entity_buff == null) {
            error_loading_data = true;
            return;
        }
        index_dat = Utility.loadData("index.dat", 0, entity_buff);
        byte entity_buff_mem[] = null;
        byte index_dat_mem[] = null;
        if (members) {
            entity_buff_mem = readDataFile("entity" + Version.ENTITY + ".mem", "member graphics", 45);
            if (entity_buff_mem == null) {
                error_loading_data = true;
                return;
            }
            index_dat_mem = Utility.loadData("index.dat", 0, entity_buff_mem);
        }
        int frameCount = 0;
        anInt659 = 0;
        anInt660 = anInt659;
        label0:
        for (int j = 0; j < GameData.animation_count; j++) {
            String s = GameData.animation_name[j];
            for (int k = 0; k < j; k++) {
                if (!GameData.animation_name[k].equalsIgnoreCase(s))
                    continue;
                GameData.animation_number[j] = GameData.animation_number[k];
                continue label0;
            }

            byte abyte7[] = Utility.loadData(s + ".dat", 0, entity_buff);
            byte abyte4[] = index_dat;
            if (abyte7 == null && members) {
                abyte7 = Utility.loadData(s + ".dat", 0, entity_buff_mem);
                abyte4 = index_dat_mem;
            }
            if (abyte7 != null) {
                surface.load_sprite(anInt660, abyte7, abyte4, 15);
                frameCount += 15;
                if (GameData.animation_has_a[j] == 1) {
                    byte a_dat[] = Utility.loadData(s + "a.dat", 0, entity_buff);
                    byte a_index_dat[] = index_dat;
                    if (a_dat == null && members) {
                        a_dat = Utility.loadData(s + "a.dat", 0, entity_buff_mem);
                        a_index_dat = index_dat_mem;
                    }
                    surface.load_sprite(anInt660 + 15, a_dat, a_index_dat, 3);
                    frameCount += 3;
                }
                if (GameData.animation_has_f[j] == 1) {
                    byte f_dat[] = Utility.loadData(s + "f.dat", 0, entity_buff);
                    byte f_dat_index[] = index_dat;
                    if (f_dat == null && members) {
                        f_dat = Utility.loadData(s + "f.dat", 0, entity_buff_mem);
                        f_dat_index = index_dat_mem;
                    }
                    surface.load_sprite(anInt660 + 18, f_dat, f_dat_index, 9);
                    frameCount += 9;
                }
                if (GameData.animation_something[j] != 0) {
                    for (int l = anInt660; l < anInt660 + 27; l++)
                        surface.method227(l);

                }
            }
            GameData.animation_number[j] = anInt660;
            anInt660 += 27;
        }

        //System.out.println("Loaded: " + frameCount + " frames of animation");
    }

    private final void handleAppearancePanelControls() {
        panelAppearance.handle_mouse(super.mouseX, super.mouseY, super.lastMouseButtonDown, super.mouseButtonDown);
        if (panelAppearance.isClicked(controlButtonAppearanceHead1))
            do
                appearanceHeadType = ((appearanceHeadType - 1) + GameData.animation_count) % GameData.animation_count;
            while ((GameData.animation_something[appearanceHeadType] & 3) != 1 || (GameData.animation_something[appearanceHeadType] & 4 * appearanceHeadGender) == 0);
        if (panelAppearance.isClicked(controlButtonAppearanceHead2))
            do
                appearanceHeadType = (appearanceHeadType + 1) % GameData.animation_count;
            while ((GameData.animation_something[appearanceHeadType] & 3) != 1 || (GameData.animation_something[appearanceHeadType] & 4 * appearanceHeadGender) == 0);
        if (panelAppearance.isClicked(controlButtonAppearanceHair1))
            appearanceHairColour = ((appearanceHairColour - 1) + characterHairColours.length) % characterHairColours.length;
        if (panelAppearance.isClicked(controlButtonAppearanceHair2))
            appearanceHairColour = (appearanceHairColour + 1) % characterHairColours.length;
        if (panelAppearance.isClicked(controlButtonAppearanceGender1) || panelAppearance.isClicked(controlButtonAppearanceGender2)) {
            for (appearanceHeadGender = 3 - appearanceHeadGender; (GameData.animation_something[appearanceHeadType] & 3) != 1 || (GameData.animation_something[appearanceHeadType] & 4 * appearanceHeadGender) == 0; appearanceHeadType = (appearanceHeadType + 1) % GameData.animation_count)
                ;
            for (; (GameData.animation_something[appearanceBodyGender] & 3) != 2 || (GameData.animation_something[appearanceBodyGender] & 4 * appearanceHeadGender) == 0; appearanceBodyGender = (appearanceBodyGender + 1) % GameData.animation_count)
                ;
        }
        if (panelAppearance.isClicked(controlButtonAppearanceTop1))
            appearanceTopColour = ((appearanceTopColour - 1) + characterTopBottomColours.length) % characterTopBottomColours.length;
        if (panelAppearance.isClicked(controlButtonAppearanceTop2))
            appearanceTopColour = (appearanceTopColour + 1) % characterTopBottomColours.length;
        if (panelAppearance.isClicked(controlButtonAppearanceSkin1))
            appearanceSkinColour = ((appearanceSkinColour - 1) + characterSkinColours.length) % characterSkinColours.length;
        if (panelAppearance.isClicked(controlButtonAppearanceSkin2))
            appearanceSkinColour = (appearanceSkinColour + 1) % characterSkinColours.length;
        if (panelAppearance.isClicked(controlButtonAppearanceBottom1))
            appearanceBottomColour = ((appearanceBottomColour - 1) + characterTopBottomColours.length) % characterTopBottomColours.length;
        if (panelAppearance.isClicked(controlButtonAppearanceBottom2))
            appearanceBottomColour = (appearanceBottomColour + 1) % characterTopBottomColours.length;
        if (panelAppearance.isClicked(controlButtonAppearanceAccept)) {
            super.clientStream.newPacket(235);
            super.clientStream.addByte(appearanceHeadGender);
            super.clientStream.addByte(appearanceHeadType);
            super.clientStream.addByte(appearanceBodyGender);
            super.clientStream.addByte(appearance2Colour);
            super.clientStream.addByte(appearanceHairColour);
            super.clientStream.addByte(appearanceTopColour);
            super.clientStream.addByte(appearanceBottomColour);
            super.clientStream.addByte(appearanceSkinColour);
            super.clientStream.sendPacket();
            surface.black_screen();
            showAppearanceChange = false;
        }
    }

    protected final void draw() {
        if (error_loading_data) {
            Graphics g = getGraphics();
            g.setColor(Color.black);
            g.fillRect(0, 0, 512, 356);
            g.setFont(new Font("Helvetica", 1, 16));
            g.setColor(Color.yellow);
            int i = 35;
            g.drawString("Sorry, an error has occured whilst loading RuneScape", 30, i);
            i += 50;
            g.setColor(Color.white);
            g.drawString("To fix this try the following (in order):", 30, i);
            i += 50;
            g.setColor(Color.white);
            g.setFont(new Font("Helvetica", 1, 12));
            g.drawString("1: Try closing ALL open web-browser windows, and reloading", 30, i);
            i += 30;
            g.drawString("2: Try clearing your web-browsers cache from tools->internet options", 30, i);
            i += 30;
            g.drawString("3: Try using a different game-world", 30, i);
            i += 30;
            g.drawString("4: Try rebooting your computer", 30, i);
            i += 30;
            g.drawString("5: Try selecting a different version of Java from the play-game menu", 30, i);
            method8(1);
            return;
        }
        if (errorLoadingCodebase) {
            Graphics g1 = getGraphics();
            g1.setColor(Color.black);
            g1.fillRect(0, 0, 512, 356);
            g1.setFont(new Font("Helvetica", 1, 20));
            g1.setColor(Color.white);
            g1.drawString("Error - unable to load game!", 50, 50);
            g1.drawString("To play RuneScape make sure you play from", 50, 100);
            g1.drawString("http://www.runescape.com", 50, 150);
            method8(1);
            return;
        }
        if (errorLoadingMemory) {
            Graphics g2 = getGraphics();
            g2.setColor(Color.black);
            g2.fillRect(0, 0, 512, 356);
            g2.setFont(new Font("Helvetica", 1, 20));
            g2.setColor(Color.white);
            g2.drawString("Error - out of memory!", 50, 50);
            g2.drawString("Close ALL unnecessary programs", 50, 100);
            g2.drawString("and windows before loading the game", 50, 150);
            g2.drawString("RuneScape needs about 48meg of spare RAM", 50, 200);
            method8(1);
            return;
        }
        try {
            if (loggedIn == 0) {
                surface.loggedIn = false;
                drawLoginScreens();
            }
            if (loggedIn == 1) {
                surface.loggedIn = true;
                draw_game();
                return;
            }
        } catch (OutOfMemoryError _ex) {
            dispose_and_collect();
            errorLoadingMemory = true;
        }
    }

    protected final void on_closing() {
        closeConnection();
        dispose_and_collect();
        if (audioPlayer != null)
            audioPlayer.stopPlayer();
    }

    protected final byte[] readDataFile(String file, String prettyname, int percent) {
        //if (!appletMode)
        //file = "../release/" + file;
        file = "./data/" + file;
        byte abyte0[] = link.getjag(file);
        if (abyte0 != null) {
            int j = ((abyte0[0] & 0xff) << 16) + ((abyte0[1] & 0xff) << 8) + (abyte0[2] & 0xff);
            int k = ((abyte0[3] & 0xff) << 16) + ((abyte0[4] & 0xff) << 8) + (abyte0[5] & 0xff);
            byte abyte1[] = new byte[abyte0.length - 6];
            for (int l = 0; l < abyte0.length - 6; l++)
                abyte1[l] = abyte0[l + 6];

            showLoadingProgress(percent, "Unpacking " + prettyname);
            if (k != j) {
                byte abyte2[] = new byte[j];
                BZLib.decompress(abyte2, j, abyte1, k, 0);
                return abyte2;
            } else {
                return abyte1;
            }
        } else {
            return super.readDataFile(file, prettyname, percent);
        }
    }

    private final void draw_dialog_duel_confirm() {
        byte dialog_x = 22;
        byte dialog_y = 36;
        surface.draw_box(dialog_x, dialog_y, 468, 16, 192);
        surface.draw_box_alpha(dialog_x, dialog_y + 16, 468, 246, 0x989898, 160);
        surface.drawstring_center("Please confirm your duel with @yel@" + Utility.hash2username(duel_opponent_name_hash), dialog_x + 234, dialog_y + 12, 1, 0xffffff);
        surface.drawstring_center("Your stake:", dialog_x + 117, dialog_y + 30, 1, 0xffff00);
        for (int item_index = 0; item_index < duel_items_count; item_index++) {
            String s = GameData.item_name[duel_items[item_index]];
            if (GameData.item_stackable[duel_items[item_index]] == 0)
                s = s + " x " + formatNumber(duel_item_count[item_index]);
            surface.drawstring_center(s, dialog_x + 117, dialog_y + 42 + item_index * 12, 1, 0xffffff);
        }

        if (duel_items_count == 0)
            surface.drawstring_center("Nothing!", dialog_x + 117, dialog_y + 42, 1, 0xffffff);
        surface.drawstring_center("Your opponent's stake:", dialog_x + 351, dialog_y + 30, 1, 0xffff00);
        for (int item_index = 0; item_index < duel_opponent_items_count; item_index++) {
            String s1 = GameData.item_name[duel_opponent_items[item_index]];
            if (GameData.item_stackable[duel_opponent_items[item_index]] == 0)
                s1 = s1 + " x " + formatNumber(duel_opponent_item_count[item_index]);
            surface.drawstring_center(s1, dialog_x + 351, dialog_y + 42 + item_index * 12, 1, 0xffffff);
        }

        if (duel_opponent_items_count == 0)
            surface.drawstring_center("Nothing!", dialog_x + 351, dialog_y + 42, 1, 0xffffff);
        if (duel_option_retreat == 0)
            surface.drawstring_center("You can retreat from this duel", dialog_x + 234, dialog_y + 180, 1, 65280);
        else
            surface.drawstring_center("No retreat is possible!", dialog_x + 234, dialog_y + 180, 1, 0xff0000);
        if (duel_option_magic == 0)
            surface.drawstring_center("Magic may be used", dialog_x + 234, dialog_y + 192, 1, 65280);
        else
            surface.drawstring_center("Magic cannot be used", dialog_x + 234, dialog_y + 192, 1, 0xff0000);
        if (duel_option_prayer == 0)
            surface.drawstring_center("Prayer may be used", dialog_x + 234, dialog_y + 204, 1, 65280);
        else
            surface.drawstring_center("Prayer cannot be used", dialog_x + 234, dialog_y + 204, 1, 0xff0000);
        if (duel_option_weapons == 0)
            surface.drawstring_center("Weapons may be used", dialog_x + 234, dialog_y + 216, 1, 65280);
        else
            surface.drawstring_center("Weapons cannot be used", dialog_x + 234, dialog_y + 216, 1, 0xff0000);
        surface.drawstring_center("If you are sure click 'Accept' to begin the duel", dialog_x + 234, dialog_y + 230, 1, 0xffffff);
        if (!duel_accepted) {
            surface.draw_picture((dialog_x + 118) - 35, dialog_y + 238, sprite_media + 25);
            surface.draw_picture((dialog_x + 352) - 35, dialog_y + 238, sprite_media + 26);
        } else {
            surface.drawstring_center("Waiting for other player...", dialog_x + 234, dialog_y + 250, 1, 0xffff00);
        }
        if (mouseButtonClick == 1) {
            if (super.mouseX < dialog_x || super.mouseY < dialog_y || super.mouseX > dialog_x + 468 || super.mouseY > dialog_y + 262) {
                show_dialog_duel_confirm = false;
                super.clientStream.newPacket(230);
                super.clientStream.sendPacket();
            }
            if (super.mouseX >= (dialog_x + 118) - 35 && super.mouseX <= dialog_x + 118 + 70 && super.mouseY >= dialog_y + 238 && super.mouseY <= dialog_y + 238 + 21) {
                duel_accepted = true;
                super.clientStream.newPacket(77);
                super.clientStream.sendPacket();
            }
            if (super.mouseX >= (dialog_x + 352) - 35 && super.mouseX <= dialog_x + 353 + 70 && super.mouseY >= dialog_y + 238 && super.mouseY <= dialog_y + 238 + 21) {
                show_dialog_duel_confirm = false;
                super.clientStream.newPacket(197);
                super.clientStream.sendPacket();
            }
            mouseButtonClick = 0;
        }
    }

    protected final int getLinkUID() {
        return link.uid;
    }

    private final void method98(int i, int j, int k, int l, boolean flag) {
        if (walkTo(i, j, k, l, k, l, false, flag)) {
            return;
        } else {
            walkToActionSource(i, j, k, l, k, l, true, flag);
            return;
        }
    }

    private final void loadModels() {
        GameData.getModelIndex("torcha2");
        GameData.getModelIndex("torcha3");
        GameData.getModelIndex("torcha4");
        GameData.getModelIndex("skulltorcha2");
        GameData.getModelIndex("skulltorcha3");
        GameData.getModelIndex("skulltorcha4");
        GameData.getModelIndex("firea2");
        GameData.getModelIndex("firea3");
        GameData.getModelIndex("fireplacea2");
        GameData.getModelIndex("fireplacea3");
        GameData.getModelIndex("firespell2");
        GameData.getModelIndex("firespell3");
        GameData.getModelIndex("lightning2");
        GameData.getModelIndex("lightning3");
        GameData.getModelIndex("clawspell2");
        GameData.getModelIndex("clawspell3");
        GameData.getModelIndex("clawspell4");
        GameData.getModelIndex("clawspell5");
        GameData.getModelIndex("spellcharge2");
        GameData.getModelIndex("spellcharge3");
        //if (getStartedAsApplet()) { // always show models on loading screen viewports
        if (true) {
            byte abyte0[] = readDataFile("models" + Version.MODELS + ".jag", "3d models", 60);
            if (abyte0 == null) {
                error_loading_data = true;
                return;
            }
            for (int j = 0; j < GameData.modelCount; j++) {
                int k = Utility.getDataFileOffset(GameData.modelName[j] + ".ob3", abyte0);
                if (k != 0)
                    game_models[j] = new GameModel(abyte0, k, true);
                else
                    game_models[j] = new GameModel(1, 1);
                if (GameData.modelName[j].equals("giantcrystal"))
                    game_models[j].transparent = true;
            }

            return;
        }
        showLoadingProgress(70, "Loading 3d models");
        for (int i = 0; i < GameData.modelCount; i++) {
            game_models[i] = new GameModel("../gamedata/models/" + GameData.modelName[i] + ".ob2");
            if (GameData.modelName[i].equals("giantcrystal"))
                game_models[i].transparent = true;
        }

    }

    private final void draw_dialog_servermessage() {
        int width = 400;// '\u0190';
        int height = 100;// 'd';
        if (serverMessageBoxTop) {
            height = 450;// '\u01C2'; // why is this set twice
            height = 300;// '\u012C';
        }
        surface.draw_box(256 - width / 2, 167 - height / 2, width, height, 0);
        surface.draw_box_edge(256 - width / 2, 167 - height / 2, width, height, 0xffffff);
        surface.centrepara(serverMessage, 256, (167 - height / 2) + 20, 1, 0xffffff, width - 40);
        int i = 157 + height / 2;
        int j = 0xffffff;
        if (super.mouseY > i - 12 && super.mouseY <= i && super.mouseX > 106 && super.mouseX < 406)
            j = 0xff0000;
        surface.drawstring_center("Click here to close window", 256, i, 1, j);
        if (mouseButtonClick == 1) {
            if (j == 0xff0000)
                show_dialog_servermessage = false;
            if ((super.mouseX < 256 - width / 2 || super.mouseX > 256 + width / 2) && (super.mouseY < 167 - height / 2 || super.mouseY > 167 + height / 2))
                show_dialog_servermessage = false;
        }
        mouseButtonClick = 0;
    }

    private final void draw_dialog_report_abuse_input() {
        if (super.input_text_final.length() > 0) {
            String s = super.input_text_final.trim();
            super.input_text_current = "";
            super.input_text_final = "";
            if (s.length() > 0) {
                long l = Utility.username2hash(s);
                super.clientStream.newPacket(206);
                super.clientStream.addLong(l);
                super.clientStream.addByte(report_abuse_offence);
                super.clientStream.addByte(report_abuse_mute ? 1 : 0);
                super.clientStream.sendPacket();
            }
            show_dialog_report_abuse_step = 0;
            return;
        }
        surface.draw_box(56, 130, 400, 100, 0);
        surface.draw_box_edge(56, 130, 400, 100, 0xffffff);
        int i = 160;
        surface.drawstring_center("Now type the name of the offending player, and press enter", 256, i, 1, 0xffff00);
        i += 18;
        surface.drawstring_center("Name: " + super.input_text_current + "*", 256, i, 4, 0xffffff);
        if (super.moderatorLevel > 0) {
            i = 207;
            if (report_abuse_mute)
                surface.drawstring_center("Moderator option: Mute player for 48 hours: <ON>", 256, i, 1, 0xff8000);
            else
                surface.drawstring_center("Moderator option: Mute player for 48 hours: <OFF>", 256, i, 1, 0xffffff);
            if (super.mouseX > 106 && super.mouseX < 406 && super.mouseY > i - 13 && super.mouseY < i + 2 && mouseButtonClick == 1) {
                mouseButtonClick = 0;
                report_abuse_mute = !report_abuse_mute;
            }
        }
        i = 222;
        int j = 0xffffff;
        if (super.mouseX > 196 && super.mouseX < 316 && super.mouseY > i - 13 && super.mouseY < i + 2) {
            j = 0xffff00;
            if (mouseButtonClick == 1) {
                mouseButtonClick = 0;
                show_dialog_report_abuse_step = 0;
            }
        }
        surface.drawstring_center("Click here to cancel", 256, i, 1, j);
        if (mouseButtonClick == 1 && (super.mouseX < 56 || super.mouseX > 456 || super.mouseY < 130 || super.mouseY > 230)) {
            mouseButtonClick = 0;
            show_dialog_report_abuse_step = 0;
        }
    }

    private final void show_message(String message, int type) {
        if (type == 2 || type == 4 || type == 6) {
            for (; message.length() > 5 && message.charAt(0) == '@' && message.charAt(4) == '@'; message = message.substring(5))
                ;
            int j = message.indexOf(":");
            if (j != -1) {
                String s1 = message.substring(0, j);
                long l = Utility.username2hash(s1);
                for (int i1 = 0; i1 < super.ignore_list_count; i1++)
                    if (super.ignore_list[i1] == l)
                        return;

            }
        }
        if (type == 2)
            message = "@yel@" + message;
        if (type == 3 || type == 4)
            message = "@whi@" + message;
        if (type == 6)
            message = "@cya@" + message;
        if (messageTabSelected != 0) {
            if (type == 4 || type == 3)
                messageTabFlashAll = 200;
            if (type == 2 && messageTabSelected != 1)
                messageTabFlashHistory = 200;
            if (type == 5 && messageTabSelected != 2)
                messtageTabFlashQuest = 200;
            if (type == 6 && messageTabSelected != 3)
                messageTabFlashPrivate = 200;
            if (type == 3 && messageTabSelected != 0)
                messageTabSelected = 0;
            if (type == 6 && messageTabSelected != 3 && messageTabSelected != 0)
                messageTabSelected = 0;
        }
        for (int k = 4; k > 0; k--) {
            messageHistory[k] = messageHistory[k - 1];
            messageHistoryTimeout[k] = messageHistoryTimeout[k - 1];
        }

        messageHistory[0] = message;
        messageHistoryTimeout[0] = 300;
        if (type == 2)
            if (panelMessageTabs.control_flash_text[controlTextListChat] == panelMessageTabs.control_list_entry_count[controlTextListChat] - 4)
                panelMessageTabs.remove_list_entry(controlTextListChat, message, true);
            else
                panelMessageTabs.remove_list_entry(controlTextListChat, message, false);
        if (type == 5)
            if (panelMessageTabs.control_flash_text[controlTextListQuest] == panelMessageTabs.control_list_entry_count[controlTextListQuest] - 4)
                panelMessageTabs.remove_list_entry(controlTextListQuest, message, true);
            else
                panelMessageTabs.remove_list_entry(controlTextListQuest, message, false);
        if (type == 6) {
            if (panelMessageTabs.control_flash_text[controlTextListPrivate] == panelMessageTabs.control_list_entry_count[controlTextListPrivate] - 4) {
                panelMessageTabs.remove_list_entry(controlTextListPrivate, message, true);
                return;
            }
            panelMessageTabs.remove_list_entry(controlTextListPrivate, message, false);
        }
    }

    private final void walkToObject(int x, int y, int id, int index) {
        int w;
        int h;
        if (id == 0 || id == 4) {
            w = GameData.object_width[index];
            h = GameData.object_height[index];
        } else {
            h = GameData.object_width[index];
            w = GameData.object_height[index];
        }
        if (GameData.object_type[index] == 2 || GameData.object_type[index] == 3) {
            if (id == 0) {
                x--;
                w++;
            }
            if (id == 2)
                h++;
            if (id == 4)
                w++;
            if (id == 6) {
                y--;
                h++;
            }
            walkToActionSource(region_x, region_y, x, y, (x + w) - 1, (y + h) - 1, false, true);
            return;
        } else {
            walkToActionSource(region_x, region_y, x, y, (x + w) - 1, (y + h) - 1, true, true);
            return;
        }
    }

    private final int getInventoryCount(int id) {
        int count = 0;
        for (int k = 0; k < inventory_items_count; k++)
            if (inventory_item_id[k] == id)
                if (GameData.item_stackable[id] == 1)
                    count++;
                else
                    count += inventory_item_stack_count[k];

        return count;
    }

    private final void drawLoginScreens() {
        welcomScreenAlreadyShown = false;
        surface.interlace = false;
        surface.black_screen();
        if (loginScreen == 0 || loginScreen == 1 || loginScreen == 2 || loginScreen == 3) {
            int i = (loginTimer * 2) % 3072;
            if (i < 1024) {
                surface.draw_picture(0, 10, sprite_logo);
                if (i > 768)
                    surface.method232(0, 10, sprite_logo + 1, i - 768);
            } else if (i < 2048) {
                surface.draw_picture(0, 10, sprite_logo + 1);
                if (i > 1792)
                    surface.method232(0, 10, sprite_media + 10, i - 1792);
            } else {
                surface.draw_picture(0, 10, sprite_media + 10);
                if (i > 2816)
                    surface.method232(0, 10, sprite_logo, i - 2816);
            }
        }
        if (loginScreen == 0)
            panel_login_welcome.drawPanel();
        if (loginScreen == 1)
            panel_login_newuser.drawPanel();
        if (loginScreen == 2)
            panel_login_existinguser.drawPanel();
        surface.draw_picture(0, game_height, sprite_media + 22); // blue bar
//        surface.draw_line_alpha(50, 50, 150, 150, 100, 100);
//        draw_ui_tab_minimap(true);
        surface.draw(graphics, 0, 0);
    }

    private final void draw_ui_tab_options(boolean flag) {
        int ui_x = surface.width2 - 199;
        int ui_y = 36;
        surface.draw_picture(ui_x - 49, 3, sprite_media + 6);
        int ui_width = 196;// '\304';
        surface.draw_box_alpha(ui_x, 36, ui_width, 65, Surface.rgb2long(181, 181, 181), 160);
        surface.draw_box_alpha(ui_x, 101, ui_width, 65, Surface.rgb2long(201, 201, 201), 160);
        surface.draw_box_alpha(ui_x, 166, ui_width, 95, Surface.rgb2long(181, 181, 181), 160);
        surface.draw_box_alpha(ui_x, 261, ui_width, 40, Surface.rgb2long(201, 201, 201), 160);
        int x = ui_x + 3;
        int y = ui_y + 15;
        surface.drawstring("Game options - click to toggle", x, y, 1, 0);
        y += 15;
        if (optionCameraModeAuto)
            surface.drawstring("Camera angle mode - @gre@Auto", x, y, 1, 0xffffff);
        else
            surface.drawstring("Camera angle mode - @red@Manual", x, y, 1, 0xffffff);
        y += 15;
        if (optionMouseButtonOne)
            surface.drawstring("Mouse buttons - @red@One", x, y, 1, 0xffffff);
        else
            surface.drawstring("Mouse buttons - @gre@Two", x, y, 1, 0xffffff);
        y += 15;
        if (members)
            if (optionSoundDisabled)
                surface.drawstring("Sound effects - @red@off", x, y, 1, 0xffffff);
            else
                surface.drawstring("Sound effects - @gre@on", x, y, 1, 0xffffff);
        y += 15;
        surface.drawstring("To change your contact details,", x, y, 0, 0xffffff);
        y += 15;
        surface.drawstring("password, recovery questions, etc..", x, y, 0, 0xffffff);
        y += 15;
        surface.drawstring("please select 'account management'", x, y, 0, 0xffffff);
        y += 15;
        if (referid == 0)
            surface.drawstring("from the runescape.com front page", x, y, 0, 0xffffff);
        else if (referid == 1)
            surface.drawstring("from the link below the gamewindow", x, y, 0, 0xffffff);
        else
            surface.drawstring("from the runescape front webpage", x, y, 0, 0xffffff);
        y += 15;
        y += 5;
        surface.drawstring("Privacy settings. Will be applied to", ui_x + 3, y, 1, 0);
        y += 15;
        surface.drawstring("all people not on your friends list", ui_x + 3, y, 1, 0);
        y += 15;
        if (super.settingsBlockChat == 0)
            surface.drawstring("Block chat messages: @red@<off>", ui_x + 3, y, 1, 0xffffff);
        else
            surface.drawstring("Block chat messages: @gre@<on>", ui_x + 3, y, 1, 0xffffff);
        y += 15;
        if (super.settingsBlockPrivate == 0)
            surface.drawstring("Block private messages: @red@<off>", ui_x + 3, y, 1, 0xffffff);
        else
            surface.drawstring("Block private messages: @gre@<on>", ui_x + 3, y, 1, 0xffffff);
        y += 15;
        if (super.settingsBlockTrade == 0)
            surface.drawstring("Block trade requests: @red@<off>", ui_x + 3, y, 1, 0xffffff);
        else
            surface.drawstring("Block trade requests: @gre@<on>", ui_x + 3, y, 1, 0xffffff);
        y += 15;
        if (members)
            if (super.settingsBlockDuel == 0)
                surface.drawstring("Block duel requests: @red@<off>", ui_x + 3, y, 1, 0xffffff);
            else
                surface.drawstring("Block duel requests: @gre@<on>", ui_x + 3, y, 1, 0xffffff);
        y += 15;
        y += 5;
        surface.drawstring("Always logout when you finish", x, y, 1, 0);
        y += 15;
        int k1 = 0xffffff;
        if (super.mouseX > x && super.mouseX < x + ui_width && super.mouseY > y - 12 && super.mouseY < y + 4)
            k1 = 0xffff00;
        surface.drawstring("Click here to logout", ui_x + 3, y, 1, k1);
        if (!flag)
            return;
        int mouse_x = super.mouseX - (surface.width2 - 199);
        int mouse_y = super.mouseY - 36;
        if (mouse_x >= 0 && mouse_y >= 0 && mouse_x < 196 && mouse_y < 265) {
            int l1 = surface.width2 - 199;
            byte byte0 = 36;
            int c1 = 196;// '\304';
            int l = l1 + 3;
            int j1 = byte0 + 30;
            if (super.mouseX > l && super.mouseX < l + c1 && super.mouseY > j1 - 12 && super.mouseY < j1 + 4 && mouseButtonClick == 1) {
                optionCameraModeAuto = !optionCameraModeAuto;
                super.clientStream.newPacket(111);
                super.clientStream.addByte(0);
                super.clientStream.addByte(optionCameraModeAuto ? 1 : 0);
                super.clientStream.sendPacket();
            }
            j1 += 15;
            if (super.mouseX > l && super.mouseX < l + c1 && super.mouseY > j1 - 12 && super.mouseY < j1 + 4 && mouseButtonClick == 1) {
                optionMouseButtonOne = !optionMouseButtonOne;
                super.clientStream.newPacket(111);
                super.clientStream.addByte(2);
                super.clientStream.addByte(optionMouseButtonOne ? 1 : 0);
                super.clientStream.sendPacket();
            }
            j1 += 15;
            if (members && super.mouseX > l && super.mouseX < l + c1 && super.mouseY > j1 - 12 && super.mouseY < j1 + 4 && mouseButtonClick == 1) {
                optionSoundDisabled = !optionSoundDisabled;
                super.clientStream.newPacket(111);
                super.clientStream.addByte(3);
                super.clientStream.addByte(optionSoundDisabled ? 1 : 0);
                super.clientStream.sendPacket();
            }
            j1 += 15;
            j1 += 15;
            j1 += 15;
            j1 += 15;
            j1 += 15;
            boolean flag1 = false;
            j1 += 35;
            if (super.mouseX > l && super.mouseX < l + c1 && super.mouseY > j1 - 12 && super.mouseY < j1 + 4 && mouseButtonClick == 1) {
                super.settingsBlockChat = 1 - super.settingsBlockChat;
                flag1 = true;
            }
            j1 += 15;
            if (super.mouseX > l && super.mouseX < l + c1 && super.mouseY > j1 - 12 && super.mouseY < j1 + 4 && mouseButtonClick == 1) {
                super.settingsBlockPrivate = 1 - super.settingsBlockPrivate;
                flag1 = true;
            }
            j1 += 15;
            if (super.mouseX > l && super.mouseX < l + c1 && super.mouseY > j1 - 12 && super.mouseY < j1 + 4 && mouseButtonClick == 1) {
                super.settingsBlockTrade = 1 - super.settingsBlockTrade;
                flag1 = true;
            }
            j1 += 15;
            if (members && super.mouseX > l && super.mouseX < l + c1 && super.mouseY > j1 - 12 && super.mouseY < j1 + 4 && mouseButtonClick == 1) {
                super.settingsBlockDuel = 1 - super.settingsBlockDuel;
                flag1 = true;
            }
            j1 += 15;
            if (flag1)
                sendPrivacySettings(super.settingsBlockChat, super.settingsBlockPrivate, super.settingsBlockTrade, super.settingsBlockDuel);
            j1 += 20;
            if (super.mouseX > l && super.mouseX < l + c1 && super.mouseY > j1 - 12 && super.mouseY < j1 + 4 && mouseButtonClick == 1)
                sendLogout();
            mouseButtonClick = 0;
        }
    }

    private final void load_textures() {
        byte buff_textures[] = readDataFile("textures" + Version.TEXTURES + ".jag", "Textures", 50);
        if (buff_textures == null) {
            error_loading_data = true;
            return;
        }
        byte buff_index[] = Utility.loadData("index.dat", 0, buff_textures);
        scene.initialise_textures(GameData.texture_count, 7, 11);
        for (int i = 0; i < GameData.texture_count; i++) {
            String name = GameData.texture_name[i];
            byte buff1[] = Utility.loadData(name + ".dat", 0, buff_textures);
            surface.load_sprite(sprite_texture, buff1, buff_index, 1);
            surface.draw_box(0, 0, 128, 128, 0xff00ff);
            surface.draw_picture(0, 0, sprite_texture);
            int wh = surface.image_full_width[sprite_texture];
            String name_sub = GameData.texture_subtype_name[i];
            if (name_sub != null && name_sub.length() > 0) {
                byte buff2[] = Utility.loadData(name_sub + ".dat", 0, buff_textures);
                surface.load_sprite(sprite_texture, buff2, buff_index, 1);
                surface.draw_picture(0, 0, sprite_texture);
            }
            surface.render(sprite_texture_world + i, 0, 0, wh, wh);
            int area = wh * wh;
            for (int j = 0; j < area; j++)
                if (surface.surface_set_pixels[sprite_texture_world + i][j] == 65280)
                    surface.surface_set_pixels[sprite_texture_world + i][j] = 0xff00ff;

            surface.draw_world(sprite_texture_world + i);
            scene.load_texture(i, surface.aByteArrayArray322[sprite_texture_world + i], surface.anIntArrayArray323[sprite_texture_world + i], wh / 64 - 1);
        }

    }

    protected final void handleMouseDown(int i, int j, int k) {
        mouseClickXHistory[mouseClickCount] = j;
        mouseClickYHistory[mouseClickCount] = k;
        mouseClickCount = mouseClickCount + 1 & 8191;// 0x1fff
        for (int l = 10; l < 4000; l++) {
            int i1 = mouseClickCount - l & 8191;// 0x1fff
            if (mouseClickXHistory[i1] == j && mouseClickYHistory[i1] == k) {
                boolean flag = false;
                for (int j1 = 1; j1 < l; j1++) {
                    int k1 = mouseClickCount - j1 & 8191;// 0x1fff
                    int l1 = i1 - j1 & 8191;// 0x1fff
                    if (mouseClickXHistory[l1] != j || mouseClickYHistory[l1] != k)
                        flag = true;
                    if (mouseClickXHistory[k1] != mouseClickXHistory[l1] || mouseClickYHistory[k1] != mouseClickYHistory[l1])
                        break;
                    if (j1 == l - 1 && flag && combat_timeout == 0 && logout_timeout == 0) {
                        sendLogout();
                        return;
                    }
                }

            }
        }

    }

    final void method108(int i, int j, int k, int l, int i1, int j1, int k1) {
        int l1 = teleport_bubble_type[i1];
        int i2 = teleport_bubble_time[i1];
        if (l1 == 0) {
            int j2 = 255 + i2 * 5 * 256;
            surface.draw_circle(i + k / 2, j + l / 2, 20 + i2 * 2, j2, 255 - i2 * 5);
        }
        if (l1 == 1) {
            int k2 = 0xff0000 + i2 * 5 * 256;
            surface.draw_circle(i + k / 2, j + l / 2, 10 + i2, k2, 255 - i2 * 5);
        }
    }

    protected final void showServerMessage(String s) {
        if (s.startsWith("@bor@")) {
            show_message(s, 4);
            return;
        }
        if (s.startsWith("@que@")) {
            show_message("@whi@" + s, 5);
            return;
        }
        if (s.startsWith("@pri@")) {
            show_message(s, 6);
            return;
        } else {
            show_message(s, 3);
            return;
        }
    }

    private final void updateObjectAnimation(int i, String s) { // looks like it just updates objects like torches etc to flip between the different models and appear "animated"
        int j = object_x[i];
        int k = object_y[i];
        int l = j - local_player.currentX / 128;
        int i1 = k - local_player.currentY / 128;
        byte byte0 = 7;
        if (j >= 0 && k >= 0 && j < 96 && k < 96 && l > -byte0 && l < byte0 && i1 > -byte0 && i1 < byte0) {
            scene.free_model(object_model[i]);
            int j1 = GameData.getModelIndex(s);
            GameModel gameModel = game_models[j1].copy();
            scene.add_model(gameModel);
            gameModel.set_light(true, 48, 48, -50, -10, -50);
            gameModel.copy_position(object_model[i]);
            gameModel.key = i;
            object_model[i] = gameModel;
        }
    }

    private final void create_top_mouse_menu() {
        if (selectedSpell >= 0 || selectedItemInventoryIndex >= 0) {
            menuItemText1[menuItemsCount] = "Cancel";
            menuItemText2[menuItemsCount] = "";
            menuItemID[menuItemsCount] = 4000;
            menuItemsCount++;
        }
        for (int i = 0; i < menuItemsCount; i++)
            menuIndices[i] = i;

        for (boolean flag = false; !flag; ) {
            flag = true;
            for (int j = 0; j < menuItemsCount - 1; j++) {
                int l = menuIndices[j];
                int j1 = menuIndices[j + 1];
                if (menuItemID[l] > menuItemID[j1]) {
                    menuIndices[j] = j1;
                    menuIndices[j + 1] = l;
                    flag = false;
                }
            }

        }

        if (menuItemsCount > 20)
            menuItemsCount = 20;
        if (menuItemsCount > 0) {
            int k = -1;
            for (int i1 = 0; i1 < menuItemsCount; i1++) {
                if (menuItemText2[menuIndices[i1]] == null || menuItemText2[menuIndices[i1]].length() <= 0)
                    continue;
                k = i1;
                break;
            }

            String s = null;
            if ((selectedItemInventoryIndex >= 0 || selectedSpell >= 0) && menuItemsCount == 1)
                s = "Choose a target";
            else if ((selectedItemInventoryIndex >= 0 || selectedSpell >= 0) && menuItemsCount > 1)
                s = "@whi@" + menuItemText1[menuIndices[0]] + " " + menuItemText2[menuIndices[0]];
            else if (k != -1)
                s = menuItemText2[menuIndices[k]] + ": @whi@" + menuItemText1[menuIndices[0]];
            if (menuItemsCount == 2 && s != null)
                s = s + "@whi@ / 1 more option";
            if (menuItemsCount > 2 && s != null)
                s = s + "@whi@ / " + (menuItemsCount - 1) + " more options";
            if (s != null)
                surface.drawstring(s, 6, 14, 1, 0xffff00);
            if (!optionMouseButtonOne && mouseButtonClick == 1 || optionMouseButtonOne && mouseButtonClick == 1 && menuItemsCount == 1) {
                menu_item_click(menuIndices[0]);
                mouseButtonClick = 0;
                return;
            }
            if (!optionMouseButtonOne && mouseButtonClick == 2 || optionMouseButtonOne && mouseButtonClick == 1) {
                menuHeight = (menuItemsCount + 1) * 15;
                menuWidth = surface.textWidth("Choose option", 1) + 5;
                for (int k1 = 0; k1 < menuItemsCount; k1++) {
                    int l1 = surface.textWidth(menuItemText1[k1] + " " + menuItemText2[k1], 1) + 5;
                    if (l1 > menuWidth)
                        menuWidth = l1;
                }

                menuX = super.mouseX - menuWidth / 2;
                menuY = super.mouseY - 7;
                show_right_click_menu = true;
                if (menuX < 0)
                    menuX = 0;
                if (menuY < 0)
                    menuY = 0;
                if (menuX + menuWidth > 510)
                    menuX = 510 - menuWidth;
                if (menuY + menuHeight > 315)
                    menuY = 315 - menuHeight;
                mouseButtonClick = 0;
            }
        }
    }

    private final void draw_dialog_logout() {
        surface.draw_box(126, 137, 260, 60, 0);
        surface.draw_box_edge(126, 137, 260, 60, 0xffffff);
        surface.drawstring_center("Logging out...", 256, 173, 5, 0xffffff);
    }

    private final void draw_dialog_combat_style() {
        byte byte0 = 7;
        byte byte1 = 15;
        int width = 175;// '\257';
        if (mouseButtonClick != 0) {
            for (int i = 0; i < 5; i++) {
                if (i <= 0 || super.mouseX <= byte0 || super.mouseX >= byte0 + width || super.mouseY <= byte1 + i * 20 || super.mouseY >= byte1 + i * 20 + 20)
                    continue;
                combatStyle = i - 1;
                mouseButtonClick = 0;
                super.clientStream.newPacket(29);
                super.clientStream.addByte(combatStyle);
                super.clientStream.sendPacket();
                break;
            }

        }
        for (int j = 0; j < 5; j++) {
            if (j == combatStyle + 1)
                surface.draw_box_alpha(byte0, byte1 + j * 20, width, 20, Surface.rgb2long(255, 0, 0), 128);
            else
                surface.draw_box_alpha(byte0, byte1 + j * 20, width, 20, Surface.rgb2long(190, 190, 190), 128);
            surface.draw_line_horiz(byte0, byte1 + j * 20, width, 0);
            surface.draw_line_horiz(byte0, byte1 + j * 20 + 20, width, 0);
        }

        surface.drawstring_center("Select combat style", byte0 + width / 2, byte1 + 16, 3, 0xffffff);
        surface.drawstring_center("Controlled (+1 of each)", byte0 + width / 2, byte1 + 36, 3, 0);
        surface.drawstring_center("Aggressive (+3 strength)", byte0 + width / 2, byte1 + 56, 3, 0);
        surface.drawstring_center("Accurate   (+3 attack)", byte0 + width / 2, byte1 + 76, 3, 0);
        surface.drawstring_center("Defensive  (+3 defense)", byte0 + width / 2, byte1 + 96, 3, 0);
    }

    private final void menu_item_click(int i) {
        int mx = menuItemX[i];
        int my = menuItemY[i];
        int midx = menuSourceType[i];
        int msrcidx = menuSourceIndex[i];
        int mtargetindex = menuTargetIndex[i];
        int mitemid = menuItemID[i];
        if (mitemid == 200) {
            method98(region_x, region_y, mx, my, true);
            super.clientStream.newPacket(249);
            super.clientStream.addShort(mx + areaX);
            super.clientStream.addShort(my + areaY);
            super.clientStream.addShort(midx);
            super.clientStream.addShort(msrcidx);
            super.clientStream.sendPacket();
            selectedSpell = -1;
        }
        if (mitemid == 210) {
            method98(region_x, region_y, mx, my, true);
            super.clientStream.newPacket(53);
            super.clientStream.addShort(mx + areaX);
            super.clientStream.addShort(my + areaY);
            super.clientStream.addShort(midx);
            super.clientStream.addShort(msrcidx);
            super.clientStream.sendPacket();
            selectedItemInventoryIndex = -1;
        }
        if (mitemid == 220) {
            method98(region_x, region_y, mx, my, true);
            super.clientStream.newPacket(247);
            super.clientStream.addShort(mx + areaX);
            super.clientStream.addShort(my + areaY);
            super.clientStream.addShort(midx);
            super.clientStream.addShort(msrcidx);
            super.clientStream.sendPacket();
        }
        if (mitemid == 3200)
            show_message(GameData.item_description[midx], 3);
        if (mitemid == 300) {
            method78(mx, my, midx);
            super.clientStream.newPacket(180);
            super.clientStream.addShort(mx + areaX);
            super.clientStream.addShort(my + areaY);
            super.clientStream.addByte(midx);
            super.clientStream.addShort(msrcidx);
            super.clientStream.sendPacket();
            selectedSpell = -1;
        }
        if (mitemid == 310) {
            method78(mx, my, midx);
            super.clientStream.newPacket(161);
            super.clientStream.addShort(mx + areaX);
            super.clientStream.addShort(my + areaY);
            super.clientStream.addByte(midx);
            super.clientStream.addShort(msrcidx);
            super.clientStream.sendPacket();
            selectedItemInventoryIndex = -1;
        }
        if (mitemid == 320) {
            method78(mx, my, midx);
            super.clientStream.newPacket(14);
            super.clientStream.addShort(mx + areaX);
            super.clientStream.addShort(my + areaY);
            super.clientStream.addByte(midx);
            super.clientStream.sendPacket();
        }
        if (mitemid == 2300) {
            method78(mx, my, midx);
            super.clientStream.newPacket(127);
            super.clientStream.addShort(mx + areaX);
            super.clientStream.addShort(my + areaY);
            super.clientStream.addByte(midx);
            super.clientStream.sendPacket();
        }
        if (mitemid == 3300)
            show_message(GameData.wall_object_description[midx], 3);
        if (mitemid == 400) {
            walkToObject(mx, my, midx, msrcidx);
            super.clientStream.newPacket(99);
            super.clientStream.addShort(mx + areaX);
            super.clientStream.addShort(my + areaY);
            super.clientStream.addShort(mtargetindex);
            super.clientStream.sendPacket();
            selectedSpell = -1;
        }
        if (mitemid == 410) {
            walkToObject(mx, my, midx, msrcidx);
            super.clientStream.newPacket(115);
            super.clientStream.addShort(mx + areaX);
            super.clientStream.addShort(my + areaY);
            super.clientStream.addShort(mtargetindex);
            super.clientStream.sendPacket();
            selectedItemInventoryIndex = -1;
        }
        if (mitemid == 420) {
            walkToObject(mx, my, midx, msrcidx);
            super.clientStream.newPacket(136);
            super.clientStream.addShort(mx + areaX);
            super.clientStream.addShort(my + areaY);
            super.clientStream.sendPacket();
        }
        if (mitemid == 2400) {
            walkToObject(mx, my, midx, msrcidx);
            super.clientStream.newPacket(79);
            super.clientStream.addShort(mx + areaX);
            super.clientStream.addShort(my + areaY);
            super.clientStream.sendPacket();
        }
        if (mitemid == 3400)
            show_message(GameData.object_description[midx], 3);
        if (mitemid == 600) {
            super.clientStream.newPacket(4);
            super.clientStream.addShort(midx);
            super.clientStream.addShort(msrcidx);
            super.clientStream.sendPacket();
            selectedSpell = -1;
        }
        if (mitemid == 610) {
            super.clientStream.newPacket(91);
            super.clientStream.addShort(midx);
            super.clientStream.addShort(msrcidx);
            super.clientStream.sendPacket();
            selectedItemInventoryIndex = -1;
        }
        if (mitemid == 620) {
            super.clientStream.newPacket(170);
            super.clientStream.addShort(midx);
            super.clientStream.sendPacket();
        }
        if (mitemid == 630) {
            super.clientStream.newPacket(169);
            super.clientStream.addShort(midx);
            super.clientStream.sendPacket();
        }
        if (mitemid == 640) {
            super.clientStream.newPacket(90);
            super.clientStream.addShort(midx);
            super.clientStream.sendPacket();
        }
        if (mitemid == 650) {
            selectedItemInventoryIndex = midx;
            show_ui_tab = 0;
            selectedItemName = GameData.item_name[inventory_item_id[selectedItemInventoryIndex]];
        }
        if (mitemid == 660) {
            super.clientStream.newPacket(246);
            super.clientStream.addShort(midx);
            super.clientStream.sendPacket();
            selectedItemInventoryIndex = -1;
            show_ui_tab = 0;
            show_message("Dropping " + GameData.item_name[inventory_item_id[midx]], 4);
        }
        if (mitemid == 3600)
            show_message(GameData.item_description[midx], 3);
        if (mitemid == 700) {
            int l1 = (mx - 64) / magicLoc;
            int l3 = (my - 64) / magicLoc;
            walkToActionSource(region_x, region_y, l1, l3, true);
            super.clientStream.newPacket(50);
            super.clientStream.addShort(midx);
            super.clientStream.addShort(msrcidx);
            super.clientStream.sendPacket();
            selectedSpell = -1;
        }
        if (mitemid == 710) {
            int i2 = (mx - 64) / magicLoc;
            int i4 = (my - 64) / magicLoc;
            walkToActionSource(region_x, region_y, i2, i4, true);
            super.clientStream.newPacket(135);
            super.clientStream.addShort(midx);
            super.clientStream.addShort(msrcidx);
            super.clientStream.sendPacket();
            selectedItemInventoryIndex = -1;
        }
        if (mitemid == 720) {
            int j2 = (mx - 64) / magicLoc;
            int j4 = (my - 64) / magicLoc;
            walkToActionSource(region_x, region_y, j2, j4, true);
            super.clientStream.newPacket(153);
            super.clientStream.addShort(midx);
            super.clientStream.sendPacket();
        }
        if (mitemid == 725) {
            int k2 = (mx - 64) / magicLoc;
            int k4 = (my - 64) / magicLoc;
            walkToActionSource(region_x, region_y, k2, k4, true);
            super.clientStream.newPacket(202);
            super.clientStream.addShort(midx);
            super.clientStream.sendPacket();
        }
        if (mitemid == 715 || mitemid == 2715) {
            int l2 = (mx - 64) / magicLoc;
            int l4 = (my - 64) / magicLoc;
            walkToActionSource(region_x, region_y, l2, l4, true);
            super.clientStream.newPacket(190);
            super.clientStream.addShort(midx);
            super.clientStream.sendPacket();
        }
        if (mitemid == 3700)
            show_message(GameData.npc_description[midx], 3);
        if (mitemid == 800) {
            int i3 = (mx - 64) / magicLoc;
            int i5 = (my - 64) / magicLoc;
            walkToActionSource(region_x, region_y, i3, i5, true);
            super.clientStream.newPacket(229);
            super.clientStream.addShort(midx);
            super.clientStream.addShort(msrcidx);
            super.clientStream.sendPacket();
            selectedSpell = -1;
        }
        if (mitemid == 810) {
            int j3 = (mx - 64) / magicLoc;
            int j5 = (my - 64) / magicLoc;
            walkToActionSource(region_x, region_y, j3, j5, true);
            super.clientStream.newPacket(113);
            super.clientStream.addShort(midx);
            super.clientStream.addShort(msrcidx);
            super.clientStream.sendPacket();
            selectedItemInventoryIndex = -1;
        }
        if (mitemid == 805 || mitemid == 2805) {
            int k3 = (mx - 64) / magicLoc;
            int k5 = (my - 64) / magicLoc;
            walkToActionSource(region_x, region_y, k3, k5, true);
            super.clientStream.newPacket(171);
            super.clientStream.addShort(midx);
            super.clientStream.sendPacket();
        }
        if (mitemid == 2806) {
            super.clientStream.newPacket(103);
            super.clientStream.addShort(midx);
            super.clientStream.sendPacket();
        }
        if (mitemid == 2810) {
            super.clientStream.newPacket(142);
            super.clientStream.addShort(midx);
            super.clientStream.sendPacket();
        }
        if (mitemid == 2820) {
            super.clientStream.newPacket(165);
            super.clientStream.addShort(midx);
            super.clientStream.sendPacket();
        }
        if (mitemid == 900) {
            walkToActionSource(region_x, region_y, mx, my, true);
            super.clientStream.newPacket(158);
            super.clientStream.addShort(mx + areaX);
            super.clientStream.addShort(my + areaY);
            super.clientStream.addShort(midx);
            super.clientStream.sendPacket();
            selectedSpell = -1;
        }
        if (mitemid == 920) {
            walkToActionSource(region_x, region_y, mx, my, false);
            if (mouseClickXStep == -24)
                mouseClickXStep = 24;
        }
        if (mitemid == 1000) {
            super.clientStream.newPacket(137);
            super.clientStream.addShort(midx);
            super.clientStream.sendPacket();
            selectedSpell = -1;
        }
        if (mitemid == 4000) {
            selectedItemInventoryIndex = -1;
            selectedSpell = -1;
        }
    }

    protected final void showLoginScreenStatus(String s, String s1) {
        if (loginScreen == 1)
            panel_login_newuser.updateText(anInt827, s + " " + s1);
        if (loginScreen == 2)
            panel_login_existinguser.updateText(anInt761, s + " " + s1);
        login_user_disp = s1;
        drawLoginScreens();
        resetTimings();
    }

    protected final void lostConnection() {
        systemUpdate = 0;
        if (logout_timeout != 0) {
            resetLoginVars();
            return;
        } else {
            super.lostConnection();
            return;
        }
    }

    private final boolean is_valid_camera_angle(int i) {
        int j = local_player.currentX / 128;
        int k = local_player.currentY / 128;
        for (int l = 2; l >= 1; l--) {
            if (i == 1 && ((world.object_adjacency[j][k - l] & 128) == 128 || (world.object_adjacency[j - l][k] & 128) == 128 || (world.object_adjacency[j - l][k - l] & 128) == 128))// 0x80
                return false;
            if (i == 3 && ((world.object_adjacency[j][k + l] & 128) == 128 || (world.object_adjacency[j - l][k] & 128) == 128 || (world.object_adjacency[j - l][k + l] & 128) == 128))// 0x80
                return false;
            if (i == 5 && ((world.object_adjacency[j][k + l] & 128) == 128 || (world.object_adjacency[j + l][k] & 128) == 128 || (world.object_adjacency[j + l][k + l] & 128) == 128))// 0x80
                return false;
            if (i == 7 && ((world.object_adjacency[j][k - l] & 128) == 128 || (world.object_adjacency[j + l][k] & 128) == 128 || (world.object_adjacency[j + l][k - l] & 128) == 128))// 0x80
                return false;
            if (i == 0 && (world.object_adjacency[j][k - l] & 128) == 128)// 0x80
                return false;
            if (i == 2 && (world.object_adjacency[j - l][k] & 128) == 128)// 0x80
                return false;
            if (i == 4 && (world.object_adjacency[j][k + l] & 128) == 128)// 0x80
                return false;
            if (i == 6 && (world.object_adjacency[j + l][k] & 128) == 128)// 0x80
                return false;
        }

        return true;
    }

    private final void reset_login_screen_variables() {
        loggedIn = 0;
        loginScreen = 0;
        loginUser = "";
        loginPass = "";
        login_user_desc = "Please enter a username:";
        login_user_disp = "*" + loginUser + "*";
        player_count = 0;
        npc_count = 0;
    }

    protected final void handleIncomingPacket(int commandid, int len, byte data[]) {
        try {
            if (commandid == 191) {
                known_player_count = player_count;
                for (int k = 0; k < known_player_count; k++)
                    known_players[k] = players[k];

                int k7 = 8;
                region_x = Utility.getBitMask(data, k7, 11);
                k7 += 11;
                region_y = Utility.getBitMask(data, k7, 13);
                k7 += 13;
                int l13 = Utility.getBitMask(data, k7, 4);
                k7 += 4;
                boolean flag1 = loadNextRegion(region_x, region_y);
                region_x -= areaX;
                region_y -= areaY;
                int l22 = region_x * magicLoc + 64;
                int l25 = region_y * magicLoc + 64;
                if (flag1) {
                    local_player.waypoint_current = 0;
                    local_player.moving_step = 0;
                    local_player.currentX = local_player.waypoints_x[0] = l22;
                    local_player.currentY = local_player.waypoints_y[0] = l25;
                }
                player_count = 0;
                local_player = create_player(local_player_server_index, l22, l25, l13);
                int i29 = Utility.getBitMask(data, k7, 8);
                k7 += 8;
                for (int l33 = 0; l33 < i29; l33++) {
                    Character character_3 = known_players[l33 + 1];
                    int k39 = Utility.getBitMask(data, k7, 1);
                    k7++;
                    if (k39 != 0) {
                        int k41 = Utility.getBitMask(data, k7, 1);
                        k7++;
                        if (k41 == 0) {
                            int l42 = Utility.getBitMask(data, k7, 3);
                            k7 += 3;
                            int l43 = character_3.waypoint_current;
                            int j44 = character_3.waypoints_x[l43];
                            int k44 = character_3.waypoints_y[l43];
                            if (l42 == 2 || l42 == 1 || l42 == 3)
                                j44 += magicLoc;
                            if (l42 == 6 || l42 == 5 || l42 == 7)
                                j44 -= magicLoc;
                            if (l42 == 4 || l42 == 3 || l42 == 5)
                                k44 += magicLoc;
                            if (l42 == 0 || l42 == 1 || l42 == 7)
                                k44 -= magicLoc;
                            character_3.animation_next = l42;
                            character_3.waypoint_current = l43 = (l43 + 1) % 10;
                            character_3.waypoints_x[l43] = j44;
                            character_3.waypoints_y[l43] = k44;
                        } else {
                            int i43 = Utility.getBitMask(data, k7, 4);
                            if ((i43 & 12) == 12) {// 0xc
                                k7 += 2;
                                continue;
                            }
                            character_3.animation_next = Utility.getBitMask(data, k7, 4);
                            k7 += 4;
                        }
                    }
                    players[player_count++] = character_3;
                }

                int count = 0;
                while (k7 + 24 < len * 8) {
                    int l39 = Utility.getBitMask(data, k7, 11);
                    k7 += 11;
                    int l41 = Utility.getBitMask(data, k7, 5);
                    k7 += 5;
                    if (l41 > 15)
                        l41 -= 32;
                    int j43 = Utility.getBitMask(data, k7, 5);
                    k7 += 5;
                    if (j43 > 15)
                        j43 -= 32;
                    int i14 = Utility.getBitMask(data, k7, 4);
                    k7 += 4;
                    int i44 = Utility.getBitMask(data, k7, 1);
                    k7++;
                    int i23 = (region_x + l41) * magicLoc + 64;
                    int i26 = (region_y + j43) * magicLoc + 64;
                    create_player(l39, i23, i26, i14);
                    if (i44 == 0)
                        player_server_indexes[count++] = l39;
                }
                if (count > 0) {
                    super.clientStream.newPacket(163);
                    super.clientStream.addShort(count);
                    for (int i = 0; i < count; i++) {
                        Character c = player_server[player_server_indexes[i]];
                        super.clientStream.addShort(c.server_index);
                        super.clientStream.addShort(c.server_id);
                    }

                    super.clientStream.sendPacket();
                    count = 0;
                }
                return;
            }
            if (commandid == 99) {
                for (int l = 1; l < len; )
                    if (Utility.getUnsignedByte(data[l]) == 255) {
                        int l7 = 0;
                        int j14 = region_x + data[l + 1] >> 3;
                        int i19 = region_y + data[l + 2] >> 3;
                        l += 3;
                        for (int j23 = 0; j23 < ground_item_count; j23++) {
                            int j26 = (ground_item_x[j23] >> 3) - j14;
                            int j29 = (ground_item_y[j23] >> 3) - i19;
                            if (j26 != 0 || j29 != 0) {
                                if (j23 != l7) {
                                    ground_item_x[l7] = ground_item_x[j23];
                                    ground_item_y[l7] = ground_item_y[j23];
                                    ground_item_id[l7] = ground_item_id[j23];
                                    ground_item_z[l7] = ground_item_z[j23];
                                }
                                l7++;
                            }
                        }

                        ground_item_count = l7;
                    } else {
                        int i8 = Utility.getUnsignedShort(data, l);
                        l += 2;
                        int k14 = region_x + data[l++];
                        int j19 = region_y + data[l++];
                        if ((i8 & 32768) == 0) {// 0x8000
                            ground_item_x[ground_item_count] = k14;
                            ground_item_y[ground_item_count] = j19;
                            ground_item_id[ground_item_count] = i8;
                            ground_item_z[ground_item_count] = 0;
                            for (int k23 = 0; k23 < object_count; k23++) {
                                if (object_x[k23] != k14 || object_y[k23] != j19)
                                    continue;
                                ground_item_z[ground_item_count] = GameData.object_something[object_id[k23]];
                                break;
                            }

                            ground_item_count++;
                        } else {
                            i8 &= 32767;// 0x7fff
                            int l23 = 0;
                            for (int k26 = 0; k26 < ground_item_count; k26++)
                                if (ground_item_x[k26] != k14 || ground_item_y[k26] != j19 || ground_item_id[k26] != i8) {
                                    if (k26 != l23) {
                                        ground_item_x[l23] = ground_item_x[k26];
                                        ground_item_y[l23] = ground_item_y[k26];
                                        ground_item_id[l23] = ground_item_id[k26];
                                        ground_item_z[l23] = ground_item_z[k26];
                                    }
                                    l23++;
                                } else {
                                    i8 = -123;
                                }

                            ground_item_count = l23;
                        }
                    }

                return;
            }
            if (commandid == 48) {
                for (int i1 = 1; i1 < len; )
                    if (Utility.getUnsignedByte(data[i1]) == 255) {
                        int j8 = 0;
                        int l14 = region_x + data[i1 + 1] >> 3;
                        int k19 = region_y + data[i1 + 2] >> 3;
                        i1 += 3;
                        for (int i24 = 0; i24 < object_count; i24++) {
                            int l26 = (object_x[i24] >> 3) - l14;
                            int k29 = (object_y[i24] >> 3) - k19;
                            if (l26 != 0 || k29 != 0) {
                                if (i24 != j8) {
                                    object_model[j8] = object_model[i24];
                                    object_model[j8].key = j8;
                                    object_x[j8] = object_x[i24];
                                    object_y[j8] = object_y[i24];
                                    object_id[j8] = object_id[i24];
                                    object_direction[j8] = object_direction[i24];
                                }
                                j8++;
                            } else {
                                scene.free_model(object_model[i24]);
                                world.remove_object(object_x[i24], object_y[i24], object_id[i24]);
                            }
                        }

                        object_count = j8;
                    } else {
                        int id = Utility.getUnsignedShort(data, i1);
                        i1 += 2;
                        int l_x = region_x + data[i1++];
                        int l_y = region_y + data[i1++];
                        int j24 = 0;
                        for (int i27 = 0; i27 < object_count; i27++)
                            if (object_x[i27] != l_x || object_y[i27] != l_y) {
                                if (i27 != j24) {
                                    object_model[j24] = object_model[i27];
                                    object_model[j24].key = j24;
                                    object_x[j24] = object_x[i27];
                                    object_y[j24] = object_y[i27];
                                    object_id[j24] = object_id[i27];
                                    object_direction[j24] = object_direction[i27];
                                }
                                j24++;
                            } else {
                                scene.free_model(object_model[i27]);
                                world.remove_object(object_x[i27], object_y[i27], object_id[i27]);
                            }

                        object_count = j24;
                        if (id != 60000) {
                            int direction = world.get_tile_direction(l_x, l_y);
                            int width;
                            int height;
                            if (direction == 0 || direction == 4) {
                                width = GameData.object_width[id];
                                height = GameData.object_height[id];
                            } else {
                                height = GameData.object_width[id];
                                width = GameData.object_height[id];
                            }
                            int m_x = ((l_x + l_x + width) * magicLoc) / 2;
                            int m_y = ((l_y + l_y + height) * magicLoc) / 2;
                            int model_idx = GameData.object_model_index[id];
                            GameModel model = game_models[model_idx].copy();
                            scene.add_model(model);
                            model.key = object_count;
                            model.rotate(0, direction * 32, 0);
                            model.translate(m_x, -world.get_elevation(m_x, m_y), m_y);
                            model.set_light(true, 48, 48, -50, -10, -50);
                            world.method400(l_x, l_y, id);
                            if (id == 74)
                                model.translate(0, -480, 0);
                            object_x[object_count] = l_x;
                            object_y[object_count] = l_y;
                            object_id[object_count] = id;
                            object_direction[object_count] = direction;
                            object_model[object_count++] = model;
                        }
                    }

                return;
            }
            if (commandid == 53) {
                int j1 = 1;
                inventory_items_count = data[j1++] & 0xff;
                for (int i = 0; i < inventory_items_count; i++) {
                    int idequip = Utility.getUnsignedShort(data, j1);
                    j1 += 2;
                    inventory_item_id[i] = idequip & 32767;// 0x7fff
                    inventory_equipped[i] = idequip / 32768;
                    if (GameData.item_stackable[idequip & 32767] == 0) {// 0x7fff
                        inventory_item_stack_count[i] = Utility.getUnsignedInt2(data, j1);
                        if (inventory_item_stack_count[i] >= 128)
                            j1 += 4;
                        else
                            j1++;
                    } else {
                        inventory_item_stack_count[i] = 1;
                    }
                }

                return;
            }
            if (commandid == 234) {
                int k1 = Utility.getUnsignedShort(data, 1);
                int offset = 3;
                for (int k15 = 0; k15 < k1; k15++) {
                    int player_id = Utility.getUnsignedShort(data, offset);
                    offset += 2;
                    Character character = player_server[player_id];
                    byte update_type = data[offset++];
                    if (update_type == 0) { // speech bubble with an item in it
                        int id = Utility.getUnsignedShort(data, offset);
                        offset += 2;
                        if (character != null) {
                            character.bubble_timeout = 150;
                            character.bubble_id = id;
                        }
                    } else if (update_type == 1) { // chat
                        byte message_length = data[offset];
                        offset++;
                        if (character != null) {
                            String filtered = WordFilter.filter(ChatMessage.descramble(data, offset, message_length));
                            boolean ignored = false;
                            for (int i = 0; i < super.ignore_list_count; i++)
                                if (super.ignore_list[i] == character.hash) {
                                    ignored = true;
                                    break;
                                }

                            if (!ignored) {
                                character.message_timeout = 150;
                                character.message = filtered;
                                show_message(character.name + ": " + character.message, 2);
                            }
                        }
                        offset += message_length;
                    } else if (update_type == 2) { // combat damage and hp
                        int damage = Utility.getUnsignedByte(data[offset]);
                        offset++;
                        int current = Utility.getUnsignedByte(data[offset]);
                        offset++;
                        int max = Utility.getUnsignedByte(data[offset]);
                        offset++;
                        if (character != null) {
                            character.damage_taken = damage;
                            character.health_current = current;
                            character.health_max = max;
                            character.combat_timer = 200;
                            if (character == local_player) {
                                player_stat_current[3] = current;
                                player_stat_base[3] = max;
                                show_dialog_welcome = false;
                                show_dialog_servermessage = false;
                            }
                        }
                    } else if (update_type == 3) { // new incoming projectile from npc?
                        int projectile_sprite = Utility.getUnsignedShort(data, offset);
                        offset += 2;
                        int npc_idx = Utility.getUnsignedShort(data, offset);
                        offset += 2;
                        if (character != null) {
                            character.incoming_projectile_sprite = projectile_sprite;
                            character.attacking_npc_server_index = npc_idx;
                            character.attacking_player_server_index = -1;
                            character.projectile_range = projectile_max_range;
                        }
                    } else if (update_type == 4) { // new incoming projectile from player
                        int projectile_sprite = Utility.getUnsignedShort(data, offset);
                        offset += 2;
                        int player_idx = Utility.getUnsignedShort(data, offset);
                        offset += 2;
                        if (character != null) {
                            character.incoming_projectile_sprite = projectile_sprite;
                            character.attacking_player_server_index = player_idx;
                            character.attacking_npc_server_index = -1;
                            character.projectile_range = projectile_max_range;
                        }
                    } else if (update_type == 5) {
                        if (character != null) {
                            character.server_id = Utility.getUnsignedShort(data, offset);
                            offset += 2;
                            character.hash = Utility.getUnsignedLong(data, offset);
                            offset += 8;
                            character.name = Utility.hash2username(character.hash);
                            int equipped_count = Utility.getUnsignedByte(data[offset]);
                            offset++;
                            for (int i = 0; i < equipped_count; i++) {
                                character.equipped_item[i] = Utility.getUnsignedByte(data[offset]);
                                offset++;
                            }

                            for (int i = equipped_count; i < 12; i++)
                                character.equipped_item[i] = 0;

                            character.colour_hair = data[offset++] & 0xff;
                            character.colour_top = data[offset++] & 0xff;
                            character.colour_bottom = data[offset++] & 0xff;
                            character.colour_skin = data[offset++] & 0xff;
                            character.level = data[offset++] & 0xff;
                            character.bubble_visible = data[offset++] & 0xff;
                        } else {
                            offset += 14;
                            int unused = Utility.getUnsignedByte(data[offset]);
                            offset += unused + 1;
                        }
                    } else if (update_type == 6) {
                        byte m_len = data[offset];
                        offset++;
                        if (character != null) {
                            String msg = ChatMessage.descramble(data, offset, m_len);
                            character.message_timeout = 150;
                            character.message = msg;
                            if (character == local_player)
                                show_message(character.name + ": " + character.message, 5);
                        }
                        offset += m_len;
                    }
                }

                return;
            }
            if (commandid == 91) {
                for (int offset = 1; offset < len; )
                    if (Utility.getUnsignedByte(data[offset]) == 255) {
                        int count = 0;
                        int l_x = region_x + data[offset + 1] >> 3;
                        int l_y = region_y + data[offset + 2] >> 3;
                        offset += 3;
                        for (int i = 0; i < wall_object_count; i++) {
                            int s_x = (wall_object_x[i] >> 3) - l_x;
                            int s_y = (wall_object_y[i] >> 3) - l_y;
                            if (s_x != 0 || s_y != 0) {
                                if (i != count) {
                                    wall_object_model[count] = wall_object_model[i];
                                    wall_object_model[count].key = count + 10000;
                                    wall_object_x[count] = wall_object_x[i];
                                    wall_object_y[count] = wall_object_y[i];
                                    wall_object_direction[count] = wall_object_direction[i];
                                    wall_object_id[count] = wall_object_id[i];
                                }
                                count++;
                            } else {
                                scene.free_model(wall_object_model[i]);
                                world.remove_wall_object(wall_object_x[i], wall_object_y[i], wall_object_direction[i], wall_object_id[i]);
                            }
                        }

                        wall_object_count = count;
                    } else {
                        int id = Utility.getUnsignedShort(data, offset);
                        offset += 2;
                        int l_x = region_x + data[offset++];
                        int l_y = region_y + data[offset++];
                        byte direction = data[offset++];
                        int count = 0;
                        for (int i = 0; i < wall_object_count; i++)
                            if (wall_object_x[i] != l_x || wall_object_y[i] != l_y || wall_object_direction[i] != direction) {
                                if (i != count) {
                                    wall_object_model[count] = wall_object_model[i];
                                    wall_object_model[count].key = count + 10000;
                                    wall_object_x[count] = wall_object_x[i];
                                    wall_object_y[count] = wall_object_y[i];
                                    wall_object_direction[count] = wall_object_direction[i];
                                    wall_object_id[count] = wall_object_id[i];
                                }
                                count++;
                            } else {
                                scene.free_model(wall_object_model[i]);
                                world.remove_wall_object(wall_object_x[i], wall_object_y[i], wall_object_direction[i], wall_object_id[i]);
                            }

                        wall_object_count = count;
                        if (id != 65535) {
                            world.set_object_adjacency(l_x, l_y, direction, id);
                            GameModel model = create_model(l_x, l_y, direction, id, wall_object_count);
                            wall_object_model[wall_object_count] = model;
                            wall_object_x[wall_object_count] = l_x;
                            wall_object_y[wall_object_count] = l_y;
                            wall_object_id[wall_object_count] = id;
                            wall_object_direction[wall_object_count++] = direction;
                        }
                    }

                return;
            }
            if (commandid == 79) {
                npc_cache_count = npc_count;
                npc_count = 0;
                for (int i2 = 0; i2 < npc_cache_count; i2++)
                    npcs_cache[i2] = npcs[i2];

                int offset = 8;
                int j16 = Utility.getBitMask(data, offset, 8);
                offset += 8;
                for (int l20 = 0; l20 < j16; l20++) {
                    Character character_1 = npcs_cache[l20];
                    int l27 = Utility.getBitMask(data, offset, 1);
                    offset++;
                    if (l27 != 0) {
                        int i32 = Utility.getBitMask(data, offset, 1);
                        offset++;
                        if (i32 == 0) {
                            int j35 = Utility.getBitMask(data, offset, 3);
                            offset += 3;
                            int i38 = character_1.waypoint_current;
                            int l40 = character_1.waypoints_x[i38];
                            int j42 = character_1.waypoints_y[i38];
                            if (j35 == 2 || j35 == 1 || j35 == 3)
                                l40 += magicLoc;
                            if (j35 == 6 || j35 == 5 || j35 == 7)
                                l40 -= magicLoc;
                            if (j35 == 4 || j35 == 3 || j35 == 5)
                                j42 += magicLoc;
                            if (j35 == 0 || j35 == 1 || j35 == 7)
                                j42 -= magicLoc;
                            character_1.animation_next = j35;
                            character_1.waypoint_current = i38 = (i38 + 1) % 10;
                            character_1.waypoints_x[i38] = l40;
                            character_1.waypoints_y[i38] = j42;
                        } else {
                            int k35 = Utility.getBitMask(data, offset, 4);
                            if ((k35 & 12) == 12) {// 0xc
                                offset += 2;
                                continue;
                            }
                            character_1.animation_next = Utility.getBitMask(data, offset, 4);
                            offset += 4;
                        }
                    }
                    npcs[npc_count++] = character_1;
                }

                while (offset + 34 < len * 8) {
                    int server_index = Utility.getBitMask(data, offset, 12);
                    offset += 12;
                    int area_x = Utility.getBitMask(data, offset, 5);
                    offset += 5;
                    if (area_x > 15)
                        area_x -= 32;
                    int area_y = Utility.getBitMask(data, offset, 5);
                    offset += 5;
                    if (area_y > 15)
                        area_y -= 32;
                    int sprite = Utility.getBitMask(data, offset, 4);
                    offset += 4;
                    int x = (region_x + area_x) * magicLoc + 64;
                    int y = (region_y + area_y) * magicLoc + 64;
                    int type = Utility.getBitMask(data, offset, 10);
                    offset += 10;
                    if (type >= GameData.npc_count)
                        type = 24;
                    add_npc(server_index, x, y, sprite, type);
                }
                return;
            }
            if (commandid == 104) {
                int j2 = Utility.getUnsignedShort(data, 1);
                int i10 = 3;
                for (int k16 = 0; k16 < j2; k16++) {
                    int i21 = Utility.getUnsignedShort(data, i10);
                    i10 += 2;
                    Character character = npcs_server[i21];
                    int j28 = Utility.getUnsignedByte(data[i10]);
                    i10++;
                    if (j28 == 1) {
                        int target = Utility.getUnsignedShort(data, i10);
                        i10 += 2;
                        byte byte9 = data[i10];
                        i10++;
                        if (character != null) {
                            String s4 = ChatMessage.descramble(data, i10, byte9);
                            character.message_timeout = 150;
                            character.message = s4;
                            if (target == local_player.server_index)
                                show_message("@yel@" + GameData.npc_name[character.npc_id] + ": " + character.message, 5);
                        }
                        i10 += byte9;
                    } else if (j28 == 2) {
                        int l32 = Utility.getUnsignedByte(data[i10]);
                        i10++;
                        int i36 = Utility.getUnsignedByte(data[i10]);
                        i10++;
                        int k38 = Utility.getUnsignedByte(data[i10]);
                        i10++;
                        if (character != null) {
                            character.damage_taken = l32;
                            character.health_current = i36;
                            character.health_max = k38;
                            character.combat_timer = 200;
                        }
                    }
                }

                return;
            }
            if (commandid == 245) {
                show_option_menu = true;
                int count = Utility.getUnsignedByte(data[1]);
                option_menu_count = count;
                int offset = 2;
                for (int i = 0; i < count; i++) {
                    int length = Utility.getUnsignedByte(data[offset]);
                    offset++;
                    option_menu_entry[i] = new String(data, offset, length);
                    offset += length;
                }

                return;
            }
            if (commandid == 252) {
                show_option_menu = false;
                return;
            }
            if (commandid == 25) {
                loading_area = true;
                local_player_server_index = Utility.getUnsignedShort(data, 1);
                plane_width = Utility.getUnsignedShort(data, 3);
                plane_height = Utility.getUnsignedShort(data, 5);
                plane_index = Utility.getUnsignedShort(data, 7);
                plane_multiplier = Utility.getUnsignedShort(data, 9);
                plane_height -= plane_index * plane_multiplier;
                return;
            }
            if (commandid == 156) {
                int offset = 1;
                for (int i = 0; i < 18; i++)
                    player_stat_current[i] = Utility.getUnsignedByte(data[offset++]);

                for (int i = 0; i < 18; i++)
                    player_stat_base[i] = Utility.getUnsignedByte(data[offset++]);

                for (int i = 0; i < 18; i++) {
                    player_experience[i] = Utility.getUnsignedInt(data, offset);
                    offset += 4;
                }

                player_quest_points = Utility.getUnsignedByte(data[offset++]);
                return;
            }
            if (commandid == 153) {
                for (int i3 = 0; i3 < 5; i3++)
                    player_stat_equipment[i3] = Utility.getUnsignedByte(data[1 + i3]);

                return;
            }
            if (commandid == 83) {
                death_screen_timeout = 250;
                return;
            }
            if (commandid == 211) {
                int j3 = (len - 1) / 4;
                for (int l10 = 0; l10 < j3; l10++) {
                    int j17 = region_x + Utility.getSignedShort(data, 1 + l10 * 4) >> 3;
                    int l21 = region_y + Utility.getSignedShort(data, 3 + l10 * 4) >> 3;
                    int i25 = 0;
                    for (int k28 = 0; k28 < ground_item_count; k28++) {
                        int i33 = (ground_item_x[k28] >> 3) - j17;
                        int j36 = (ground_item_y[k28] >> 3) - l21;
                        if (i33 != 0 || j36 != 0) {
                            if (k28 != i25) {
                                ground_item_x[i25] = ground_item_x[k28];
                                ground_item_y[i25] = ground_item_y[k28];
                                ground_item_id[i25] = ground_item_id[k28];
                                ground_item_z[i25] = ground_item_z[k28];
                            }
                            i25++;
                        }
                    }

                    ground_item_count = i25;
                    i25 = 0;
                    for (int j33 = 0; j33 < object_count; j33++) {
                        int k36 = (object_x[j33] >> 3) - j17;
                        int l38 = (object_y[j33] >> 3) - l21;
                        if (k36 != 0 || l38 != 0) {
                            if (j33 != i25) {
                                object_model[i25] = object_model[j33];
                                object_model[i25].key = i25;
                                object_x[i25] = object_x[j33];
                                object_y[i25] = object_y[j33];
                                object_id[i25] = object_id[j33];
                                object_direction[i25] = object_direction[j33];
                            }
                            i25++;
                        } else {
                            scene.free_model(object_model[j33]);
                            world.remove_object(object_x[j33], object_y[j33], object_id[j33]);
                        }
                    }

                    object_count = i25;
                    i25 = 0;
                    for (int l36 = 0; l36 < wall_object_count; l36++) {
                        int i39 = (wall_object_x[l36] >> 3) - j17;
                        int j41 = (wall_object_y[l36] >> 3) - l21;
                        if (i39 != 0 || j41 != 0) {
                            if (l36 != i25) {
                                wall_object_model[i25] = wall_object_model[l36];
                                wall_object_model[i25].key = i25 + 10000;
                                wall_object_x[i25] = wall_object_x[l36];
                                wall_object_y[i25] = wall_object_y[l36];
                                wall_object_direction[i25] = wall_object_direction[l36];
                                wall_object_id[i25] = wall_object_id[l36];
                            }
                            i25++;
                        } else {
                            scene.free_model(wall_object_model[l36]);
                            world.remove_wall_object(wall_object_x[l36], wall_object_y[l36], wall_object_direction[l36], wall_object_id[l36]);
                        }
                    }

                    wall_object_count = i25;
                }

                return;
            }
            if (commandid == 59) {
                showAppearanceChange = true;
                return;
            }
            if (commandid == 92) {
                int k3 = Utility.getUnsignedShort(data, 1);
                if (player_server[k3] != null)
                    trade_recipient_name = player_server[k3].name;
                show_dialog_trade = true;
                trade_recipient_accepted = false;
                trade_accepted = false;
                trade_items_count = 0;
                trade_recipient_items_count = 0;
                return;
            }
            if (commandid == 128) {
                show_dialog_trade = false;
                show_dialog_trade_confirm = false;
                return;
            }
            if (commandid == 97) {
                trade_recipient_items_count = data[1] & 0xff;
                int l3 = 2;
                for (int i11 = 0; i11 < trade_recipient_items_count; i11++) {
                    trade_recipient_items[i11] = Utility.getUnsignedShort(data, l3);
                    l3 += 2;
                    trade_recipient_item_count[i11] = Utility.getUnsignedInt(data, l3);
                    l3 += 4;
                }

                trade_recipient_accepted = false;
                trade_accepted = false;
                return;
            }
            if (commandid == 162) {
                byte byte0 = data[1];
                if (byte0 == 1) {
                    trade_recipient_accepted = true;
                    return;
                } else {
                    trade_recipient_accepted = false;
                    return;
                }
            }
            if (commandid == 101) {
                show_dialog_shop = true;
                int off = 1;
                int new_item_count = data[off++] & 0xff;
                byte shop_type = data[off++];
                shop_sell_price_mod = data[off++] & 0xff;
                shop_buy_price_mod = data[off++] & 0xff;
                for (int item_index = 0; item_index < 40; item_index++)
                    shop_item[item_index] = -1;

                for (int item_index = 0; item_index < new_item_count; item_index++) {
                    shop_item[item_index] = Utility.getUnsignedShort(data, off);
                    off += 2;
                    shop_item_count[item_index] = Utility.getUnsignedShort(data, off);
                    off += 2;
                    shop_item_price[item_index] = data[off++];
                }

                if (shop_type == 1) {// shop_type == 1 -> is a general shop
                    int l28 = 39;
                    for (int k33 = 0; k33 < inventory_items_count; k33++) {
                        if (l28 < new_item_count)
                            break;
                        boolean flag2 = false;
                        for (int j39 = 0; j39 < 40; j39++) {
                            if (shop_item[j39] != inventory_item_id[k33])
                                continue;
                            flag2 = true;
                            break;
                        }

                        if (inventory_item_id[k33] == 10)
                            flag2 = true;
                        if (!flag2) {
                            shop_item[l28] = inventory_item_id[k33] & 32767;// 0x7fff
                            shop_item_count[l28] = 0;
                            shop_item_price[l28] = 0;
                            l28--;
                        }
                    }

                }
                if (shop_selected_item_index >= 0 && shop_selected_item_index < 40 && shop_item[shop_selected_item_index] != shop_selected_item_type) {
                    shop_selected_item_index = -1;
                    shop_selected_item_type = -2;
                }
                return;
            }
            if (commandid == 137) {
                show_dialog_shop = false;
                return;
            }
            if (commandid == 15) {
                byte byte1 = data[1];
                if (byte1 == 1) {
                    trade_accepted = true;
                    return;
                } else {
                    trade_accepted = false;
                    return;
                }
            }
            if (commandid == 240) {
                optionCameraModeAuto = Utility.getUnsignedByte(data[1]) == 1;
                optionMouseButtonOne = Utility.getUnsignedByte(data[2]) == 1;
                optionSoundDisabled = Utility.getUnsignedByte(data[3]) == 1;
                return;
            }
            if (commandid == 206) {
                for (int j4 = 0; j4 < len - 1; j4++) {
                    boolean on = data[j4 + 1] == 1;
                    if (!prayerOn[j4] && on)
                        playSoundFile("prayeron");
                    if (prayerOn[j4] && !on)
                        playSoundFile("prayeroff");
                    prayerOn[j4] = on;
                }

                return;
            }
            if (commandid == 5) {
                for (int k4 = 0; k4 < 50; k4++)
                    questComplete[k4] = data[k4 + 1] == 1;

                return;
            }
            if (commandid == 42) {
                show_dialog_bank = true;
                int l4 = 1;
                newBankItemCount = data[l4++] & 0xff;
                bankItemsMax = data[l4++] & 0xff;
                for (int k11 = 0; k11 < newBankItemCount; k11++) {
                    newBankItems[k11] = Utility.getUnsignedShort(data, l4);
                    l4 += 2;
                    newBankItemsCount[k11] = Utility.getUnsignedInt2(data, l4);
                    if (newBankItemsCount[k11] >= 128)
                        l4 += 4;
                    else
                        l4++;
                }

                updateBankItems();
                return;
            }
            if (commandid == 203) {
                show_dialog_bank = false;
                return;
            }
            if (commandid == 33) {
                int i5 = data[1] & 0xff;
                player_experience[i5] = Utility.getUnsignedInt(data, 2);
                return;
            }
            if (commandid == 176) {
                int j5 = Utility.getUnsignedShort(data, 1);
                if (player_server[j5] != null)
                    aString736 = player_server[j5].name;
                show_dialog_duel = true;
                duel_offer_item_count = 0;
                duel_offer_opponent_item_count = 0;
                duel_offer_opponent_accepted = false;
                duel_offer_accepted = false;
                duel_settings_retreat = false;
                duel_settings_magic = false;
                duel_settings_prayer = false;
                duel_settings_weapons = false;
                return;
            }
            if (commandid == 225) {
                show_dialog_duel = false;
                show_dialog_duel_confirm = false;
                return;
            }
            if (commandid == 20) {
                show_dialog_trade_confirm = true;
                trade_confirm_accepted = false;
                show_dialog_trade = false;
                int k5 = 1;
                trade_recipient_confirm_hash = Utility.getUnsignedLong(data, k5);
                k5 += 8;
                trade_recipient_confirm_items_count = data[k5++] & 0xff;
                for (int l11 = 0; l11 < trade_recipient_confirm_items_count; l11++) {
                    trade_recipient_confirm_items[l11] = Utility.getUnsignedShort(data, k5);
                    k5 += 2;
                    trade_recipient_confirm_item_count[l11] = Utility.getUnsignedInt(data, k5);
                    k5 += 4;
                }

                trade_confirm_items_count = data[k5++] & 0xff;
                for (int k17 = 0; k17 < trade_confirm_items_count; k17++) {
                    trade_confirm_items[k17] = Utility.getUnsignedShort(data, k5);
                    k5 += 2;
                    trade_confirm_item_count[k17] = Utility.getUnsignedInt(data, k5);
                    k5 += 4;
                }

                return;
            }
            if (commandid == 6) {
                duel_offer_opponent_item_count = data[1] & 0xff;
                int l5 = 2;
                for (int i12 = 0; i12 < duel_offer_opponent_item_count; i12++) {
                    duel_offer_opponent_item_id[i12] = Utility.getUnsignedShort(data, l5);
                    l5 += 2;
                    duel_offer_opponent_item_stack[i12] = Utility.getUnsignedInt(data, l5);
                    l5 += 4;
                }

                duel_offer_opponent_accepted = false;
                duel_offer_accepted = false;
                return;
            }
            if (commandid == 30) {
                if (data[1] == 1)
                    duel_settings_retreat = true;
                else
                    duel_settings_retreat = false;
                if (data[2] == 1)
                    duel_settings_magic = true;
                else
                    duel_settings_magic = false;
                if (data[3] == 1)
                    duel_settings_prayer = true;
                else
                    duel_settings_prayer = false;
                if (data[4] == 1)
                    duel_settings_weapons = true;
                else
                    duel_settings_weapons = false;
                duel_offer_opponent_accepted = false;
                duel_offer_accepted = false;
                return;
            }
            if (commandid == 249) {
                int i6 = 1;
                int j12 = data[i6++] & 0xff;
                int l17 = Utility.getUnsignedShort(data, i6);
                i6 += 2;
                int j22 = Utility.getUnsignedInt2(data, i6);
                if (j22 >= 128)
                    i6 += 4;
                else
                    i6++;
                if (j22 == 0) {
                    newBankItemCount--;
                    for (int k25 = j12; k25 < newBankItemCount; k25++) {
                        newBankItems[k25] = newBankItems[k25 + 1];
                        newBankItemsCount[k25] = newBankItemsCount[k25 + 1];
                    }

                } else {
                    newBankItems[j12] = l17;
                    newBankItemsCount[j12] = j22;
                    if (j12 >= newBankItemCount)
                        newBankItemCount = j12 + 1;
                }
                updateBankItems();
                return;
            }
            if (commandid == 90) {
                int j6 = 1;
                int k12 = 1;
                int i18 = data[j6++] & 0xff;
                int k22 = Utility.getUnsignedShort(data, j6);
                j6 += 2;
                if (GameData.item_stackable[k22 & 32767] == 0) {// 0x7fff
                    k12 = Utility.getUnsignedInt2(data, j6);
                    if (k12 >= 128)
                        j6 += 4;
                    else
                        j6++;
                }
                inventory_item_id[i18] = k22 & 32767;// 0x7fff
                inventory_equipped[i18] = k22 / 32768;
                inventory_item_stack_count[i18] = k12;
                if (i18 >= inventory_items_count)
                    inventory_items_count = i18 + 1;
                return;
            }
            if (commandid == 123) {
                int k6 = data[1] & 0xff;
                inventory_items_count--;
                for (int l12 = k6; l12 < inventory_items_count; l12++) {
                    inventory_item_id[l12] = inventory_item_id[l12 + 1];
                    inventory_item_stack_count[l12] = inventory_item_stack_count[l12 + 1];
                    inventory_equipped[l12] = inventory_equipped[l12 + 1];
                }

                return;
            }
            if (commandid == 159) {
                int l6 = 1;
                int i13 = data[l6++] & 0xff;
                player_stat_current[i13] = Utility.getUnsignedByte(data[l6++]);
                player_stat_base[i13] = Utility.getUnsignedByte(data[l6++]);
                player_experience[i13] = Utility.getUnsignedInt(data, l6);
                l6 += 4;
                return;
            }
            if (commandid == 253) {
                byte byte2 = data[1];
                if (byte2 == 1) {
                    duel_offer_opponent_accepted = true;
                    return;
                } else {
                    duel_offer_opponent_accepted = false;
                    return;
                }
            }
            if (commandid == 210) {
                byte byte3 = data[1];
                if (byte3 == 1) {
                    duel_offer_accepted = true;
                    return;
                } else {
                    duel_offer_accepted = false;
                    return;
                }
            }
            if (commandid == 172) {
                show_dialog_duel_confirm = true;
                duel_accepted = false;
                show_dialog_duel = false;
                int off = 1;
                duel_opponent_name_hash = Utility.getUnsignedLong(data, off);
                off += 8;
                duel_opponent_items_count = data[off++] & 0xff;
                for (int j13 = 0; j13 < duel_opponent_items_count; j13++) {
                    duel_opponent_items[j13] = Utility.getUnsignedShort(data, off);
                    off += 2;
                    duel_opponent_item_count[j13] = Utility.getUnsignedInt(data, off);
                    off += 4;
                }

                duel_items_count = data[off++] & 0xff;
                for (int j18 = 0; j18 < duel_items_count; j18++) {
                    duel_items[j18] = Utility.getUnsignedShort(data, off);
                    off += 2;
                    duel_item_count[j18] = Utility.getUnsignedInt(data, off);
                    off += 4;
                }

                duel_option_retreat = data[off++] & 0xff;
                duel_option_magic = data[off++] & 0xff;
                duel_option_prayer = data[off++] & 0xff;
                duel_option_weapons = data[off++] & 0xff;
                return;
            }
            if (commandid == 204) {
                String s = new String(data, 1, len - 1);
                playSoundFile(s);
                return;
            }
            if (commandid == 36) {
                if (teleport_bubble_count < 50) { // is this projectiles?
                    int type = data[1] & 0xff;
                    int x = data[2] + region_x;
                    int y = data[3] + region_y;
                    teleport_bubble_type[teleport_bubble_count] = type;
                    teleport_bubble_time[teleport_bubble_count] = 0;
                    teleport_bubble_x[teleport_bubble_count] = x;
                    teleport_bubble_y[teleport_bubble_count] = y;
                    teleport_bubble_count++;
                }
                return;
            }
            if (commandid == 182) {
                if (!welcomScreenAlreadyShown) {
                    welcomeLastLoggedInIP = Utility.getUnsignedInt(data, 1);
                    welcomeLastLoggedInDays = Utility.getUnsignedShort(data, 5);
                    welcomeRecoverySetDays = data[7] & 0xff;
                    welcomeUnreadMessages = Utility.getUnsignedShort(data, 8);
                    show_dialog_welcome = true;
                    welcomScreenAlreadyShown = true;
                    welcomeLastLoggedInHost = null;
                }
                return;
            }
            if (commandid == 89) {
                serverMessage = new String(data, 1, len - 1);
                show_dialog_servermessage = true;
                serverMessageBoxTop = false;
                return;
            }
            if (commandid == 222) {
                serverMessage = new String(data, 1, len - 1);
                show_dialog_servermessage = true;
                serverMessageBoxTop = true;
                return;
            }
            if (commandid == 114) {
                statFatigue = Utility.getUnsignedShort(data, 1);
                return;
            }
            if (commandid == 117) {
                if (!isSleeping)
                    fatigueSleeping = statFatigue;
                isSleeping = true;
                super.input_text_current = "";
                super.input_text_final = "";
                surface.read_sleep_word(sprite_texture + 1, data); // assume this reads the sleep word from the data?
                sleepingStatusText = null;
                return;
            }
            if (commandid == 244) {
                fatigueSleeping = Utility.getUnsignedShort(data, 1);
                return;
            }
            if (commandid == 84) {
                isSleeping = false;
                return;
            }
            if (commandid == 194) {
                sleepingStatusText = "Incorrect - Please wait...";
                return;
            }
            if (commandid == 52) {
                systemUpdate = Utility.getUnsignedShort(data, 1) * 32;
                return;
            }
        } catch (RuntimeException runtimeexception) {
            if (packetErrorCount < 3) {
                super.clientStream.newPacket(3);
                super.clientStream.addString(runtimeexception.toString());
                super.clientStream.sendPacket();
                super.clientStream.newPacket(3);
                super.clientStream.addString("p-type:" + commandid + " p-size:" + len);
                super.clientStream.sendPacket();
                super.clientStream.newPacket(3);
                super.clientStream.addString("rx:" + region_x + " ry:" + region_y + " num3l:" + object_count);
                super.clientStream.sendPacket();
                String s1 = "";
                for (int l18 = 0; l18 < 80 && l18 < len; l18++)
                    s1 = s1 + data[l18] + " ";

                super.clientStream.newPacket(3);
                super.clientStream.addString(s1);
                super.clientStream.sendPacket();
                packetErrorCount++;
            }
        }
    }

    private final void draw_ui_tab_player_info(boolean nomenus) {
        int ui_x = surface.width2 - 199;
        int ui_y = 36;
        surface.draw_picture(ui_x - 49, 3, sprite_media + 3);
        int ui_width = 196;// '\304';
        int ui_height = 275;// '\u0113';
        int l;
        int k = l = Surface.rgb2long(160, 160, 160);
        if (ui_tab_player_info_sub_tab == 0)
            k = Surface.rgb2long(220, 220, 220);
        else
            l = Surface.rgb2long(220, 220, 220);
        surface.draw_box_alpha(ui_x, ui_y, ui_width / 2, 24, k, 128);
        surface.draw_box_alpha(ui_x + ui_width / 2, ui_y, ui_width / 2, 24, l, 128);
        surface.draw_box_alpha(ui_x, ui_y + 24, ui_width, ui_height - 24, Surface.rgb2long(220, 220, 220), 128);
        surface.draw_line_horiz(ui_x, ui_y + 24, ui_width, 0);
        surface.draw_line_vert(ui_x + ui_width / 2, ui_y, 24, 0);
        surface.drawstring_center("Stats", ui_x + ui_width / 4, ui_y + 16, 4, 0);
        surface.drawstring_center("Quests", ui_x + ui_width / 4 + ui_width / 2, ui_y + 16, 4, 0);
        if (ui_tab_player_info_sub_tab == 0) {
            int i1 = 72;
            int k1 = -1;
            surface.drawstring("Skills", ui_x + 5, i1, 3, 0xffff00);
            i1 += 13;
            for (int l1 = 0; l1 < 9; l1++) {
                int i2 = 0xffffff;
                if (super.mouseX > ui_x + 3 && super.mouseY >= i1 - 11 && super.mouseY < i1 + 2 && super.mouseX < ui_x + 90) {
                    i2 = 0xff0000;
                    k1 = l1;
                }
                surface.drawstring(skillNameShort[l1] + ":@yel@" + player_stat_current[l1] + "/" + player_stat_base[l1], ui_x + 5, i1, 1, i2);
                i2 = 0xffffff;
                if (super.mouseX >= ui_x + 90 && super.mouseY >= i1 - 13 - 11 && super.mouseY < (i1 - 13) + 2 && super.mouseX < ui_x + 196) {
                    i2 = 0xff0000;
                    k1 = l1 + 9;
                }
                surface.drawstring(skillNameShort[l1 + 9] + ":@yel@" + player_stat_current[l1 + 9] + "/" + player_stat_base[l1 + 9], (ui_x + ui_width / 2) - 5, i1 - 13, 1, i2);
                i1 += 13;
            }

            surface.drawstring("Quest Points:@yel@" + player_quest_points, (ui_x + ui_width / 2) - 5, i1 - 13, 1, 0xffffff);
            i1 += 12;
            surface.drawstring("Fatigue: @yel@" + (statFatigue * 100) / 750 + "%", ui_x + 5, i1 - 13, 1, 0xffffff);
            i1 += 8;
            surface.drawstring("Equipment Status", ui_x + 5, i1, 3, 0xffff00);
            i1 += 12;
            for (int j2 = 0; j2 < 3; j2++) {
                surface.drawstring(equipmentStatNames[j2] + ":@yel@" + player_stat_equipment[j2], ui_x + 5, i1, 1, 0xffffff);
                if (j2 < 2)
                    surface.drawstring(equipmentStatNames[j2 + 3] + ":@yel@" + player_stat_equipment[j2 + 3], ui_x + ui_width / 2 + 25, i1, 1, 0xffffff);
                i1 += 13;
            }

            i1 += 6;
            surface.draw_line_horiz(ui_x, i1 - 15, ui_width, 0);
            if (k1 != -1) {
                surface.drawstring(skillNameLong[k1] + " skill", ui_x + 5, i1, 1, 0xffff00);
                i1 += 12;
                int k2 = experience_array[0];
                for (int i3 = 0; i3 < 98; i3++)
                    if (player_experience[k1] >= experience_array[i3])
                        k2 = experience_array[i3 + 1];

                surface.drawstring("Total xp: " + player_experience[k1] / 4, ui_x + 5, i1, 1, 0xffffff);
                i1 += 12;
                surface.drawstring("Next level at: " + k2 / 4, ui_x + 5, i1, 1, 0xffffff);
            } else {
                surface.drawstring("Overall levels", ui_x + 5, i1, 1, 0xffff00);
                i1 += 12;
                int l2 = 0;
                for (int j3 = 0; j3 < 18; j3++)
                    l2 += player_stat_base[j3];

                surface.drawstring("Skill total: " + l2, ui_x + 5, i1, 1, 0xffffff);
                i1 += 12;
                surface.drawstring("Combat level: " + local_player.level, ui_x + 5, i1, 1, 0xffffff);
                i1 += 12;
            }
        }
        if (ui_tab_player_info_sub_tab == 1) {
            panel_playerinfo.clear_list(control_playerinfopanel);
            panel_playerinfo.add_list_entry(control_playerinfopanel, 0, "@whi@Quest-list (green=completed)");
            for (int j1 = 0; j1 < 50; j1++)
                panel_playerinfo.add_list_entry(control_playerinfopanel, j1 + 1, (questComplete[j1] ? "@gre@" : "@red@") + questName[j1]);

            panel_playerinfo.drawPanel();
        }
        if (!nomenus)
            return;
        int mouse_x = super.mouseX - (surface.width2 - 199);
        int mouse_y = super.mouseY - 36;
        if (mouse_x >= 0 && mouse_y >= 0 && mouse_x < ui_width && mouse_y < ui_height) {
            if (ui_tab_player_info_sub_tab == 1)
                panel_playerinfo.handle_mouse(mouse_x + (surface.width2 - 199), mouse_y + 36, super.lastMouseButtonDown, super.mouseButtonDown);
            if (mouse_y <= 24 && mouseButtonClick == 1) {
                if (mouse_x < 98) {
                    ui_tab_player_info_sub_tab = 0;
                    return;
                }
                if (mouse_x > 98)
                    ui_tab_player_info_sub_tab = 1;
            }
        }
    }

    private final void create_right_click_menu() {
        int i = 2203 - (region_y + plane_height + areaY);
        if (region_x + plane_width + areaX >= 2640)
            i = -50;
        int j = -1;
        for (int k = 0; k < object_count; k++)
            objectAlreadyInMenu[k] = false;

        for (int l = 0; l < wall_object_count; l++)
            wallObjectAlreadyInMenu[l] = false;

        int i1 = scene.method272();
        GameModel objs[] = scene.getObjectsUnderCursor();
        int plyrs[] = scene.getPlayersUndorCursor();
        for (int menuidx = 0; menuidx < i1; menuidx++) {
            if (menuItemsCount > 200)
                break;
            int pid = plyrs[menuidx];
            GameModel gameModel = objs[menuidx];
            if (gameModel.face_tag[pid] <= 65535 || gameModel.face_tag[pid] >= 200000 && gameModel.face_tag[pid] <= 300000)// 0x30d40    0x493e0
                if (gameModel == scene.currentModel) {
                    int idx = gameModel.face_tag[pid] % 10000;
                    int l2 = gameModel.face_tag[pid] / 10000;
                    if (l2 == 1) {
                        String s = "";
                        int k3 = 0;
                        if (local_player.level > 0 && players[idx].level > 0)
                            k3 = local_player.level - players[idx].level;
                        if (k3 < 0)
                            s = "@or1@";
                        if (k3 < -3)
                            s = "@or2@";
                        if (k3 < -6)
                            s = "@or3@";
                        if (k3 < -9)
                            s = "@red@";
                        if (k3 > 0)
                            s = "@gr1@";
                        if (k3 > 3)
                            s = "@gr2@";
                        if (k3 > 6)
                            s = "@gr3@";
                        if (k3 > 9)
                            s = "@gre@";
                        s = " " + s + "(level-" + players[idx].level + ")";
                        if (selectedSpell >= 0) {
                            if (GameData.spell_type[selectedSpell] == 1 || GameData.spell_type[selectedSpell] == 2) {
                                menuItemText1[menuItemsCount] = "Cast " + GameData.spell_name[selectedSpell] + " on";
                                menuItemText2[menuItemsCount] = "@whi@" + players[idx].name + s;
                                menuItemID[menuItemsCount] = 800;
                                menuItemX[menuItemsCount] = players[idx].currentX;
                                menuItemY[menuItemsCount] = players[idx].currentY;
                                menuSourceType[menuItemsCount] = players[idx].server_index;
                                menuSourceIndex[menuItemsCount] = selectedSpell;
                                menuItemsCount++;
                            }
                        } else if (selectedItemInventoryIndex >= 0) {
                            menuItemText1[menuItemsCount] = "Use " + selectedItemName + " with";
                            menuItemText2[menuItemsCount] = "@whi@" + players[idx].name + s;
                            menuItemID[menuItemsCount] = 810;
                            menuItemX[menuItemsCount] = players[idx].currentX;
                            menuItemY[menuItemsCount] = players[idx].currentY;
                            menuSourceType[menuItemsCount] = players[idx].server_index;
                            menuSourceIndex[menuItemsCount] = selectedItemInventoryIndex;
                            menuItemsCount++;
                        } else {
                            if (i > 0 && (players[idx].currentY - 64) / magicLoc + plane_height + areaY < 2203) {
                                menuItemText1[menuItemsCount] = "Attack";
                                menuItemText2[menuItemsCount] = "@whi@" + players[idx].name + s;
                                if (k3 >= 0 && k3 < 5)
                                    menuItemID[menuItemsCount] = 805;
                                else
                                    menuItemID[menuItemsCount] = 2805;
                                menuItemX[menuItemsCount] = players[idx].currentX;
                                menuItemY[menuItemsCount] = players[idx].currentY;
                                menuSourceType[menuItemsCount] = players[idx].server_index;
                                menuItemsCount++;
                            } else if (members) {
                                menuItemText1[menuItemsCount] = "Duel with";
                                menuItemText2[menuItemsCount] = "@whi@" + players[idx].name + s;
                                menuItemX[menuItemsCount] = players[idx].currentX;
                                menuItemY[menuItemsCount] = players[idx].currentY;
                                menuItemID[menuItemsCount] = 2806;
                                menuSourceType[menuItemsCount] = players[idx].server_index;
                                menuItemsCount++;
                            }
                            menuItemText1[menuItemsCount] = "Trade with";
                            menuItemText2[menuItemsCount] = "@whi@" + players[idx].name + s;
                            menuItemID[menuItemsCount] = 2810;
                            menuSourceType[menuItemsCount] = players[idx].server_index;
                            menuItemsCount++;
                            menuItemText1[menuItemsCount] = "Follow";
                            menuItemText2[menuItemsCount] = "@whi@" + players[idx].name + s;
                            menuItemID[menuItemsCount] = 2820;
                            menuSourceType[menuItemsCount] = players[idx].server_index;
                            menuItemsCount++;
                        }
                    } else if (l2 == 2) {
                        if (selectedSpell >= 0) {
                            if (GameData.spell_type[selectedSpell] == 3) {
                                menuItemText1[menuItemsCount] = "Cast " + GameData.spell_name[selectedSpell] + " on";
                                menuItemText2[menuItemsCount] = "@lre@" + GameData.item_name[ground_item_id[idx]];
                                menuItemID[menuItemsCount] = 200;
                                menuItemX[menuItemsCount] = ground_item_x[idx];
                                menuItemY[menuItemsCount] = ground_item_y[idx];
                                menuSourceType[menuItemsCount] = ground_item_id[idx];
                                menuSourceIndex[menuItemsCount] = selectedSpell;
                                menuItemsCount++;
                            }
                        } else if (selectedItemInventoryIndex >= 0) {
                            menuItemText1[menuItemsCount] = "Use " + selectedItemName + " with";
                            menuItemText2[menuItemsCount] = "@lre@" + GameData.item_name[ground_item_id[idx]];
                            menuItemID[menuItemsCount] = 210;
                            menuItemX[menuItemsCount] = ground_item_x[idx];
                            menuItemY[menuItemsCount] = ground_item_y[idx];
                            menuSourceType[menuItemsCount] = ground_item_id[idx];
                            menuSourceIndex[menuItemsCount] = selectedItemInventoryIndex;
                            menuItemsCount++;
                        } else {
                            menuItemText1[menuItemsCount] = "Take";
                            menuItemText2[menuItemsCount] = "@lre@" + GameData.item_name[ground_item_id[idx]];
                            menuItemID[menuItemsCount] = 220;
                            menuItemX[menuItemsCount] = ground_item_x[idx];
                            menuItemY[menuItemsCount] = ground_item_y[idx];
                            menuSourceType[menuItemsCount] = ground_item_id[idx];
                            menuItemsCount++;
                            menuItemText1[menuItemsCount] = "Examine";
                            menuItemText2[menuItemsCount] = "@lre@" + GameData.item_name[ground_item_id[idx]];
                            menuItemID[menuItemsCount] = 3200;
                            menuSourceType[menuItemsCount] = ground_item_id[idx];
                            menuItemsCount++;
                        }
                    } else if (l2 == 3) {
                        String s1 = "";
                        int leveldiff = -1;
                        int id = npcs[idx].npc_id;
                        if (GameData.npc_attackable[id] > 0) {
                            int npclevel = (GameData.npc_attack[id] + GameData.npc_defense[id] + GameData.npc_strength[id] + GameData.npc_hits[id]) / 4;
                            int playerlevel = (player_stat_base[0] + player_stat_base[1] + player_stat_base[2] + player_stat_base[3] + 27) / 4;
                            leveldiff = playerlevel - npclevel;
                            s1 = "@yel@";
                            if (leveldiff < 0)
                                s1 = "@or1@";
                            if (leveldiff < -3)
                                s1 = "@or2@";
                            if (leveldiff < -6)
                                s1 = "@or3@";
                            if (leveldiff < -9)
                                s1 = "@red@";
                            if (leveldiff > 0)
                                s1 = "@gr1@";
                            if (leveldiff > 3)
                                s1 = "@gr2@";
                            if (leveldiff > 6)
                                s1 = "@gr3@";
                            if (leveldiff > 9)
                                s1 = "@gre@";
                            s1 = " " + s1 + "(level-" + npclevel + ")";
                        }
                        if (selectedSpell >= 0) {
                            if (GameData.spell_type[selectedSpell] == 2) {
                                menuItemText1[menuItemsCount] = "Cast " + GameData.spell_name[selectedSpell] + " on";
                                menuItemText2[menuItemsCount] = "@yel@" + GameData.npc_name[npcs[idx].npc_id];
                                menuItemID[menuItemsCount] = 700;
                                menuItemX[menuItemsCount] = npcs[idx].currentX;
                                menuItemY[menuItemsCount] = npcs[idx].currentY;
                                menuSourceType[menuItemsCount] = npcs[idx].server_index;
                                menuSourceIndex[menuItemsCount] = selectedSpell;
                                menuItemsCount++;
                            }
                        } else if (selectedItemInventoryIndex >= 0) {
                            menuItemText1[menuItemsCount] = "Use " + selectedItemName + " with";
                            menuItemText2[menuItemsCount] = "@yel@" + GameData.npc_name[npcs[idx].npc_id];
                            menuItemID[menuItemsCount] = 710;
                            menuItemX[menuItemsCount] = npcs[idx].currentX;
                            menuItemY[menuItemsCount] = npcs[idx].currentY;
                            menuSourceType[menuItemsCount] = npcs[idx].server_index;
                            menuSourceIndex[menuItemsCount] = selectedItemInventoryIndex;
                            menuItemsCount++;
                        } else {
                            if (GameData.npc_attackable[id] > 0) {
                                menuItemText1[menuItemsCount] = "Attack";
                                menuItemText2[menuItemsCount] = "@yel@" + GameData.npc_name[npcs[idx].npc_id] + s1;
                                if (leveldiff >= 0)
                                    menuItemID[menuItemsCount] = 715;
                                else
                                    menuItemID[menuItemsCount] = 2715;
                                menuItemX[menuItemsCount] = npcs[idx].currentX;
                                menuItemY[menuItemsCount] = npcs[idx].currentY;
                                menuSourceType[menuItemsCount] = npcs[idx].server_index;
                                menuItemsCount++;
                            }
                            menuItemText1[menuItemsCount] = "Talk-to";
                            menuItemText2[menuItemsCount] = "@yel@" + GameData.npc_name[npcs[idx].npc_id];
                            menuItemID[menuItemsCount] = 720;
                            menuItemX[menuItemsCount] = npcs[idx].currentX;
                            menuItemY[menuItemsCount] = npcs[idx].currentY;
                            menuSourceType[menuItemsCount] = npcs[idx].server_index;
                            menuItemsCount++;
                            if (!GameData.npc_command[id].equals("")) {
                                menuItemText1[menuItemsCount] = GameData.npc_command[id];
                                menuItemText2[menuItemsCount] = "@yel@" + GameData.npc_name[npcs[idx].npc_id];
                                menuItemID[menuItemsCount] = 725;
                                menuItemX[menuItemsCount] = npcs[idx].currentX;
                                menuItemY[menuItemsCount] = npcs[idx].currentY;
                                menuSourceType[menuItemsCount] = npcs[idx].server_index;
                                menuItemsCount++;
                            }
                            menuItemText1[menuItemsCount] = "Examine";
                            menuItemText2[menuItemsCount] = "@yel@" + GameData.npc_name[npcs[idx].npc_id];
                            menuItemID[menuItemsCount] = 3700;
                            menuSourceType[menuItemsCount] = npcs[idx].npc_id;
                            menuItemsCount++;
                        }
                    }
                } else if (gameModel != null && gameModel.key >= 10000) {
                    int idx = gameModel.key - 10000;
                    int id = wall_object_id[idx];
                    if (!wallObjectAlreadyInMenu[idx]) {
                        if (selectedSpell >= 0) {
                            if (GameData.spell_type[selectedSpell] == 4) {
                                menuItemText1[menuItemsCount] = "Cast " + GameData.spell_name[selectedSpell] + " on";
                                menuItemText2[menuItemsCount] = "@cya@" + GameData.wall_object_name[id];
                                menuItemID[menuItemsCount] = 300;
                                menuItemX[menuItemsCount] = wall_object_x[idx];
                                menuItemY[menuItemsCount] = wall_object_y[idx];
                                menuSourceType[menuItemsCount] = wall_object_direction[idx];
                                menuSourceIndex[menuItemsCount] = selectedSpell;
                                menuItemsCount++;
                            }
                        } else if (selectedItemInventoryIndex >= 0) {
                            menuItemText1[menuItemsCount] = "Use " + selectedItemName + " with";
                            menuItemText2[menuItemsCount] = "@cya@" + GameData.wall_object_name[id];
                            menuItemID[menuItemsCount] = 310;
                            menuItemX[menuItemsCount] = wall_object_x[idx];
                            menuItemY[menuItemsCount] = wall_object_y[idx];
                            menuSourceType[menuItemsCount] = wall_object_direction[idx];
                            menuSourceIndex[menuItemsCount] = selectedItemInventoryIndex;
                            menuItemsCount++;
                        } else {
                            if (!GameData.wall_object_command1[id].equalsIgnoreCase("WalkTo")) {
                                menuItemText1[menuItemsCount] = GameData.wall_object_command1[id];
                                menuItemText2[menuItemsCount] = "@cya@" + GameData.wall_object_name[id];
                                menuItemID[menuItemsCount] = 320;
                                menuItemX[menuItemsCount] = wall_object_x[idx];
                                menuItemY[menuItemsCount] = wall_object_y[idx];
                                menuSourceType[menuItemsCount] = wall_object_direction[idx];
                                menuItemsCount++;
                            }
                            if (!GameData.wall_object_command2[id].equalsIgnoreCase("Examine")) {
                                menuItemText1[menuItemsCount] = GameData.wall_object_command2[id];
                                menuItemText2[menuItemsCount] = "@cya@" + GameData.wall_object_name[id];
                                menuItemID[menuItemsCount] = 2300;
                                menuItemX[menuItemsCount] = wall_object_x[idx];
                                menuItemY[menuItemsCount] = wall_object_y[idx];
                                menuSourceType[menuItemsCount] = wall_object_direction[idx];
                                menuItemsCount++;
                            }
                            menuItemText1[menuItemsCount] = "Examine";
                            menuItemText2[menuItemsCount] = "@cya@" + GameData.wall_object_name[id];
                            menuItemID[menuItemsCount] = 3300;
                            menuSourceType[menuItemsCount] = id;
                            menuItemsCount++;
                        }
                        wallObjectAlreadyInMenu[idx] = true;
                    }
                } else if (gameModel != null && gameModel.key >= 0) {
                    int idx = gameModel.key;
                    int id = object_id[idx];
                    if (!objectAlreadyInMenu[idx]) {
                        if (selectedSpell >= 0) {
                            if (GameData.spell_type[selectedSpell] == 5) {
                                menuItemText1[menuItemsCount] = "Cast " + GameData.spell_name[selectedSpell] + " on";
                                menuItemText2[menuItemsCount] = "@cya@" + GameData.object_name[id];
                                menuItemID[menuItemsCount] = 400;
                                menuItemX[menuItemsCount] = object_x[idx];
                                menuItemY[menuItemsCount] = object_y[idx];
                                menuSourceType[menuItemsCount] = object_direction[idx];
                                menuSourceIndex[menuItemsCount] = object_id[idx];
                                menuTargetIndex[menuItemsCount] = selectedSpell;
                                menuItemsCount++;
                            }
                        } else if (selectedItemInventoryIndex >= 0) {
                            menuItemText1[menuItemsCount] = "Use " + selectedItemName + " with";
                            menuItemText2[menuItemsCount] = "@cya@" + GameData.object_name[id];
                            menuItemID[menuItemsCount] = 410;
                            menuItemX[menuItemsCount] = object_x[idx];
                            menuItemY[menuItemsCount] = object_y[idx];
                            menuSourceType[menuItemsCount] = object_direction[idx];
                            menuSourceIndex[menuItemsCount] = object_id[idx];
                            menuTargetIndex[menuItemsCount] = selectedItemInventoryIndex;
                            menuItemsCount++;
                        } else {
                            if (!GameData.object_command1[id].equalsIgnoreCase("WalkTo")) {
                                menuItemText1[menuItemsCount] = GameData.object_command1[id];
                                menuItemText2[menuItemsCount] = "@cya@" + GameData.object_name[id];
                                menuItemID[menuItemsCount] = 420;
                                menuItemX[menuItemsCount] = object_x[idx];
                                menuItemY[menuItemsCount] = object_y[idx];
                                menuSourceType[menuItemsCount] = object_direction[idx];
                                menuSourceIndex[menuItemsCount] = object_id[idx];
                                menuItemsCount++;
                            }
                            if (!GameData.object_command2[id].equalsIgnoreCase("Examine")) {
                                menuItemText1[menuItemsCount] = GameData.object_command2[id];
                                menuItemText2[menuItemsCount] = "@cya@" + GameData.object_name[id];
                                menuItemID[menuItemsCount] = 2400;
                                menuItemX[menuItemsCount] = object_x[idx];
                                menuItemY[menuItemsCount] = object_y[idx];
                                menuSourceType[menuItemsCount] = object_direction[idx];
                                menuSourceIndex[menuItemsCount] = object_id[idx];
                                menuItemsCount++;
                            }
                            menuItemText1[menuItemsCount] = "Examine";
                            menuItemText2[menuItemsCount] = "@cya@" + GameData.object_name[id];
                            menuItemID[menuItemsCount] = 3400;
                            menuSourceType[menuItemsCount] = id;
                            menuItemsCount++;
                        }
                        objectAlreadyInMenu[idx] = true;
                    }
                } else {
                    if (pid >= 0)
                        pid = gameModel.face_tag[pid] - 200000;
                    if (pid >= 0)
                        j = pid;
                }
        }

        if (selectedSpell >= 0 && GameData.spell_type[selectedSpell] <= 1) {
            menuItemText1[menuItemsCount] = "Cast " + GameData.spell_name[selectedSpell] + " on self";
            menuItemText2[menuItemsCount] = "";
            menuItemID[menuItemsCount] = 1000;
            menuSourceType[menuItemsCount] = selectedSpell;
            menuItemsCount++;
        }
        if (j != -1) {
            int l1 = j;
            if (selectedSpell >= 0) {
                if (GameData.spell_type[selectedSpell] == 6) {
                    menuItemText1[menuItemsCount] = "Cast " + GameData.spell_name[selectedSpell] + " on ground";
                    menuItemText2[menuItemsCount] = "";
                    menuItemID[menuItemsCount] = 900;
                    menuItemX[menuItemsCount] = world.anIntArray600[l1];
                    menuItemY[menuItemsCount] = world.anIntArray587[l1];
                    menuSourceType[menuItemsCount] = selectedSpell;
                    menuItemsCount++;
                    return;
                }
            } else if (selectedItemInventoryIndex < 0) {
                menuItemText1[menuItemsCount] = "Walk here";
                menuItemText2[menuItemsCount] = "";
                menuItemID[menuItemsCount] = 920;
                menuItemX[menuItemsCount] = world.anIntArray600[l1];
                menuItemY[menuItemsCount] = world.anIntArray587[l1];
                menuItemsCount++;
            }
        }
    }

    protected final void handle_inputs() {
        if (errorLoadingCodebase)
            return;
        if (errorLoadingMemory)
            return;
        if (error_loading_data)
            return;
        try {
            loginTimer++;
            if (loggedIn == 0) {
                super.lastMouseAction = 0;
                handle_login_screen_input();
            }
            if (loggedIn == 1) {
                super.lastMouseAction++;
                handle_game_input();
            }
            super.lastMouseButtonDown = 0;
            //super.unusedKeyCode2 = 0;
            camera_rotation_time++;
            if (camera_rotation_time > 500) {
                camera_rotation_time = 0;
                int i = (int) (Math.random() * 4D);
                if ((i & 1) == 1)
                    camera_rotation_x += camera_rotation_x_increment;
                if ((i & 2) == 2)
                    camera_rotation_y += camera_rotation_y_increment;
            }
            if (camera_rotation_x < -50)
                camera_rotation_x_increment = 2;
            if (camera_rotation_x > 50)
                camera_rotation_x_increment = -2;
            if (camera_rotation_y < -50)
                camera_rotation_y_increment = 2;
            if (camera_rotation_y > 50)
                camera_rotation_y_increment = -2;
            if (messageTabFlashAll > 0)
                messageTabFlashAll--;
            if (messageTabFlashHistory > 0)
                messageTabFlashHistory--;
            if (messtageTabFlashQuest > 0)
                messtageTabFlashQuest--;
            if (messageTabFlashPrivate > 0) {
                messageTabFlashPrivate--;
                return;
            }
        } catch (OutOfMemoryError _ex) {
            dispose_and_collect();
            errorLoadingMemory = true;
        }
    }

    private final void handle_login_screen_input() {
        if (super.anInt626 > 0)
            super.anInt626--;
        if (loginScreen == 0) {
            panel_login_welcome.handle_mouse(super.mouseX, super.mouseY, super.lastMouseButtonDown, super.mouseButtonDown);
            if (panel_login_welcome.isClicked(control_welcome_newuser))
                loginScreen = 1;
            if (panel_login_welcome.isClicked(control_welcome_existinguser)) {
                loginScreen = 2;
                panel_login_existinguser.updateText(anInt761, "Please enter your username and password");
                panel_login_existinguser.updateText(control_login_user, "");
                panel_login_existinguser.updateText(control_login_pass, "");
                panel_login_existinguser.setFocus(control_login_user);
                return;
            }
        } else if (loginScreen == 1) {
            panel_login_newuser.handle_mouse(super.mouseX, super.mouseY, super.lastMouseButtonDown, super.mouseButtonDown);
            if (panel_login_newuser.isClicked(control_login_new_ok)) {
                loginScreen = 0;
                return;
            }
        } else if (loginScreen == 2) {
            panel_login_existinguser.handle_mouse(super.mouseX, super.mouseY, super.lastMouseButtonDown, super.mouseButtonDown);
            if (panel_login_existinguser.isClicked(control_login_cancel))
                loginScreen = 0;
            if (panel_login_existinguser.isClicked(control_login_user))
                panel_login_existinguser.setFocus(control_login_pass);
            if (panel_login_existinguser.isClicked(control_login_pass) || panel_login_existinguser.isClicked(control_login_ok)) {
                loginUser = panel_login_existinguser.getText(control_login_user);
                loginPass = panel_login_existinguser.getText(control_login_pass);
                login(loginUser, loginPass, false);
            }
        }
    }

    private final void loadMaps() {
        world.map_pack = readDataFile("maps" + Version.MAPS + ".jag", "map", 70);
        if (members)
            world.member_map_pack = readDataFile("maps" + Version.MAPS + ".mem", "members map", 75);
        world.landscape_pack = readDataFile("land" + Version.MAPS + ".jag", "landscape", 80);
        if (members)
            world.member_landscape_pack = readDataFile("land" + Version.MAPS + ".mem", "members landscape", 85);
    }

    private final GameModel create_model(int i, int j, int k, int l, int i1) {
        int j1 = i;
        int k1 = j;
        int l1 = i;
        int i2 = j;
        int j2 = GameData.wall_object_something2[l];
        int k2 = GameData.wall_object_something3[l];
        int l2 = GameData.wall_object_something1[l];
        GameModel gameModel = new GameModel(4, 1);
        if (k == 0)
            l1 = i + 1;
        if (k == 1)
            i2 = j + 1;
        if (k == 2) {
            j1 = i + 1;
            i2 = j + 1;
        }
        if (k == 3) {
            l1 = i + 1;
            i2 = j + 1;
        }
        j1 *= magicLoc;
        k1 *= magicLoc;
        l1 *= magicLoc;
        i2 *= magicLoc;
        int i3 = gameModel.vertex_at(j1, -world.get_elevation(j1, k1), k1);
        int j3 = gameModel.vertex_at(j1, -world.get_elevation(j1, k1) - l2, k1);
        int k3 = gameModel.vertex_at(l1, -world.get_elevation(l1, i2) - l2, i2);
        int l3 = gameModel.vertex_at(l1, -world.get_elevation(l1, i2), i2);
        int ai[] = {
                i3, j3, k3, l3
        };
        gameModel.create_face(4, ai, j2, k2);
        gameModel.set_light(false, 60, 24, -50, -10, -50);
        if (i >= 0 && j >= 0 && i < 96 && j < 96)
            scene.add_model(gameModel);
        gameModel.key = i1 + 10000;
        return gameModel;
    }

    public Client() {
        menuIndices = new int[250];
        cameraAutoAngleDebug = false;
        wall_object_direction = new int[500];
        wall_object_id = new int[500];
        camera_rotation_x_increment = 2;
        inventory_max_item_count = 30;
        bankItemsMax = 48;
        option_menu_entry = new String[5];
        newBankItems = new int[256];
        newBankItemsCount = new int[256];
        teleport_bubble_time = new int[50];
        show_dialog_trade_confirm = false;
        trade_confirm_accepted = false;
        receivedMessageX = new int[50];
        receivedMessageY = new int[50];
        receivedMessageMidPoint = new int[50];
        receivedMessageHeight = new int[50];
        local_player = new Character();
        local_player_server_index = -1;
        menuItemX = new int[250];
        menuItemY = new int[250];
        show_dialog_trade = false;
        bank_items = new int[256];
        bankItemsCount = new int[256];
        appearanceBodyGender = 1;
        appearance2Colour = 2;
        appearanceHairColour = 2;
        appearanceTopColour = 8;
        appearanceBottomColour = 14;
        appearanceHeadGender = 1;
        loginUser = "";
        loginPass = "";
        camera_angle = 1;
        members = false;
        optionSoundDisabled = false;
        show_right_click_menu = false;
        camera_rotation_y_increment = 2;
        objectAlreadyInMenu = new boolean[1500];
        menuItemText1 = new String[250];
        aString736 = "";
        lastObjectAnimationNumberFireLightningSpell = -1;
        lastObjectAnimationNumberTorch = -1;
        lastOjectAnimationNumberClaw = -1;
        plane_index = -1;
        //aString744 = "";// unused
        welcomScreenAlreadyShown = false;
        isSleeping = false;
        cameraRotation = 128;
        teleport_bubble_x = new int[50];
        error_loading_data = false;
        player_experience = new int[18];
        trade_recipient_accepted = false;
        trade_accepted = false;
        mouseClickXHistory = new int[8192];
        mouseClickYHistory = new int[8192];
        show_dialog_welcome = false;
        player_server_indexes = new int[500];
        teleport_bubble_y = new int[50];
        receivedMessages = new String[50];
        show_dialog_duel_confirm = false;
        duel_accepted = false;
        players = new Character[500];
        prayerOn = new boolean[50];
        //aString793 = "";// unused
        menuSourceType = new int[250];
        menuSourceIndex = new int[250];
        menuTargetIndex = new int[250];
        wallObjectAlreadyInMenu = new boolean[500];
        magicLoc = 128;
        errorLoadingMemory = false;
        fogOfWar = false;
        game_width = 512; // 512
        game_height = 334; // 334
        const_9 = 9;
        trade_confirm_items = new int[14];
        trade_confirm_item_count = new int[14];
        trade_recipient_name = "";
        selectedSpell = -1;
        show_option_menu = false;
        player_stat_current = new int[18];
        teleport_bubble_type = new int[50];
        errorLoadingCodebase = false;
        show_dialog_shop = false;
        shop_item = new int[256];
        shop_item_count = new int[256];
        shop_item_price = new int[256];
        duel_offer_opponent_accepted = false;
        duel_offer_accepted = false;
        game_models = new GameModel[1000];
        show_dialog_duel = false;
        serverMessage = "";
        serverMessageBoxTop = false;
        duel_opponent_items = new int[8];
        duel_opponent_item_count = new int[8];
        duel_items = new int[8];
        duel_item_count = new int[8];
        player_stat_base = new int[18];
        npcs_cache = new Character[500];
        appletMode = true;
        ground_item_x = new int[5000];
        ground_item_y = new int[5000];
        ground_item_id = new int[5000];
        ground_item_z = new int[5000];
        bank_selected_item_slot = -1;
        bank_selected_item = -2;
        duel_offer_opponent_item_id = new int[8];
        duel_offer_opponent_item_stack = new int[8];
        messageHistoryTimeout = new int[5];
        optionCameraModeAuto = true;
        object_x = new int[1500];
        object_y = new int[1500];
        object_id = new int[1500];
        object_direction = new int[1500];
        selectedItemInventoryIndex = -1;
        selectedItemName = "";
        loading_area = false;
        trade_recipient_confirm_items = new int[14];
        trade_recipient_confirm_item_count = new int[14];
        trade_recipient_items = new int[14];
        trade_recipient_item_count = new int[14];
        show_dialog_servermessage = false;
        menuItemID = new int[250];
        questComplete = new boolean[50];
        wall_object_model = new GameModel[500];
        itemAboveHeadX = new int[50];
        itemAboveHeadY = new int[50];
        cameraZoom = 550;
        trade_items = new int[14];
        trade_item_count = new int[14];
        lastHeightOffset = -1;
        //anInt917 = 0xbc614e;// unused
        duel_settings_retreat = false;
        duel_settings_magic = false;
        duel_settings_prayer = false;
        duel_settings_weapons = false;
        show_dialog_bank = false;
        login_user_desc = "";
        login_user_disp = "";
        optionMouseButtonOne = false;
        inventory_item_id = new int[35];
        inventory_item_stack_count = new int[35];
        inventory_equipped = new int[35];
        known_players = new Character[500];
        messageHistory = new String[5];
        report_abuse_mute = false;
        duel_offer_item_id = new int[8];
        duel_offer_item_stack = new int[8];
        itemAboveHeadScale = new int[50];
        itemAboveHeadID = new int[50];
        sleepWordDelay = true;
        showAppearanceChange = false;
        shop_selected_item_index = -1;
        shop_selected_item_type = -2;
        projectile_max_range = 40;
        npcs = new Character[500];
        experience_array = new int[99];
        healthBarX = new int[50];
        healthBarY = new int[50];
        healthBarMissing = new int[50];
        player_server = new Character[4000];
        walkPathX = new int[8000];
        walkPathY = new int[8000];
        wall_object_x = new int[500];
        wall_object_y = new int[500];
        menuItemText2 = new String[250];
        npcs_server = new Character[5000];
        player_stat_equipment = new int[5];
        object_model = new GameModel[1500];
    }

    /* unused
    private final int anInt640 = 250;
    private final int anInt641 = 5;
    private final int anInt642 = 50;
    private final int anInt643 = 18;
    private final int anInt644 = 500;
    private final int anInt645 = 1500;
    private final int anInt646 = 5000;
    private final int anInt647 = 5000;
    private final int anInt648 = 500;
    private final int anInt649 = 4000;
    private final int anInt650 = 500;
    private final int anInt651 = 8000; */
    private int packetErrorCount;
    private int menuIndices[];
    private boolean cameraAutoAngleDebug;
    private int mouseButtonDownTime;
    private int mouseButtonItemCountIncrement;
    private int wall_object_direction[];
    private int wall_object_id[];
    private int anInt659;
    private int anInt660;
    private int camera_rotation_x;
    private int camera_rotation_x_increment;
    private Scene scene;
    private int inventory_max_item_count;
    private int bankItemsMax;
    private String option_menu_entry[];
    private int newBankItems[];
    private int newBankItemsCount[];
    private int show_dialog_report_abuse_step;
    private int loginScreen;
    private int teleport_bubble_time[];
    private boolean show_dialog_trade_confirm;
    private boolean trade_confirm_accepted;
    private int receivedMessageX[];
    private int receivedMessageY[];
    private int receivedMessageMidPoint[];
    private int receivedMessageHeight[];
    private Character local_player;
    int region_x;
    int region_y;
    int local_player_server_index;
    private Surface_Sprite surface;
    private Panel panelMessageTabs;
    int controlTextListChat;
    int controlTextListAll;
    int controlTextListQuest;
    int controlTextListPrivate;
    int messageTabSelected;
    private int menuItemX[];
    private int menuItemY[];
    private boolean show_dialog_trade;
    private int bank_items[];
    private int bankItemsCount[];
    private StreamAudioPlayer audioPlayer;
    private int appearanceHeadType;
    private int appearanceBodyGender;
    private int appearance2Colour;
    private int appearanceHairColour;
    private int appearanceTopColour;
    private int appearanceBottomColour;
    private int appearanceSkinColour;
    private int appearanceHeadGender;
    private String loginUser;
    private String loginPass;
    private int show_dialog_social_input;
    private int camera_angle;
    private int anInt707;
    private boolean members;
    private int death_screen_timeout;
    private boolean optionSoundDisabled;
    private boolean show_right_click_menu;
    private int camera_rotation_y;
    private int camera_rotation_y_increment;
    private boolean objectAlreadyInMenu[];
    private int combatStyle;
    private String menuItemText1[];
    private int welcomeUnreadMessages;
    private int controlButtonAppearanceHead1;
    private int controlButtonAppearanceHead2;
    private int controlButtonAppearanceHair1;
    private int controlButtonAppearanceHair2;
    private int controlButtonAppearanceGender1;
    private int controlButtonAppearanceGender2;
    private int controlButtonAppearanceTop1;
    private int controlButtonAppearanceTop2;
    private int controlButtonAppearanceSkin1;
    private int controlButtonAppearanceSkin2;
    private int controlButtonAppearanceBottom1;
    private int controlButtonAppearanceBottom2;
    private int controlButtonAppearanceAccept;
    private int logout_timeout;
    private long trade_recipient_confirm_hash;
    private int loginTimer;
    private int npcCombatModelArray2[] = {
            0, 0, 0, 0, 0, 1, 2, 1
    };
    private int systemUpdate;
    private String aString736;
    private int lastObjectAnimationNumberFireLightningSpell;
    private int lastObjectAnimationNumberTorch;
    private int lastOjectAnimationNumberClaw;
    private Graphics graphics;
    private int areaX;
    private int areaY;
    private int plane_index;
    //private String aString744;// unused
    private boolean welcomScreenAlreadyShown;
    private int mouseButtonClick;
    private boolean isSleeping;
    private int cameraRotation;
    private final String questName[] = {
            "Black knight's fortress", "Cook's assistant", "Demon slayer", "Doric's quest", "The restless ghost", "Goblin diplomacy", "Ernest the chicken", "Imp catcher", "Pirate's treasure", "Prince Ali rescue",
            "Romeo & Juliet", "Sheep shearer", "Shield of Arrav", "The knight's sword", "Vampire slayer", "Witch's potion", "Dragon slayer", "Witch's house (members)", "Lost city (members)", "Hero's quest (members)",
            "Druidic ritual (members)", "Merlin's crystal (members)", "Scorpion catcher (members)", "Family crest (members)", "Tribal totem (members)", "Fishing contest (members)", "Monk's friend (members)", "Temple of Ikov (members)", "Clock tower (members)", "The Holy Grail (members)",
            "Fight Arena (members)", "Tree Gnome Village (members)", "The Hazeel Cult (members)", "Sheep Herder (members)", "Plague City (members)", "Sea Slug (members)", "Waterfall quest (members)", "Biohazard (members)", "Jungle potion (members)", "Grand tree (members)",
            "Shilo village (members)", "Underground pass (members)", "Observatory quest (members)", "Tourist trap (members)", "Watchtower (members)", "Dwarf Cannon (members)", "Murder Mystery (members)", "Digsite (members)", "Gertrude's Cat (members)", "Legend's Quest (members)"
    };
    private int teleport_bubble_x[];
    private boolean error_loading_data;
    private int player_experience[];
    private int healthBarCount;
    private int sprite_media;
    private int sprite_util;
    private int sprite_item;
    private int sprite_projectile;
    private int sprite_texture;
    private int sprite_texture_world;
    private int sprite_logo;
    private int anInt761;
    private int control_login_user;
    private int control_login_pass;
    private int control_login_ok;
    private int control_login_cancel;
    private boolean trade_recipient_accepted;
    private boolean trade_accepted;
    private int teleport_bubble_count;
    private int mouseClickCount;
    int mouseClickXHistory[];
    int mouseClickYHistory[];
    private int shop_sell_price_mod;
    private int shop_buy_price_mod;
    private boolean show_dialog_welcome;
    private int duel_option_retreat;
    private int duel_option_magic;
    private int duel_option_prayer;
    private int duel_option_weapons;
    private int player_server_indexes[];
    private int ground_item_count;
    private int teleport_bubble_y[];
    private int receivedMessagesCount;
    String receivedMessages[];
    private int messageTabFlashAll;
    private int messageTabFlashHistory;
    private int messtageTabFlashQuest;
    private int messageTabFlashPrivate;
    private boolean show_dialog_duel_confirm;
    private boolean duel_accepted;
    private Character players[];
    private int bankItemCount;
    private boolean prayerOn[];
    //private String aString793;// unused
    private int menuSourceType[];
    private int menuSourceIndex[];
    private int menuTargetIndex[];
    private boolean wallObjectAlreadyInMenu[];
    private int objectAnimationNumberFireLightningSpell;
    private int objectAnimationNumberTorch;
    private int objectAnimationNumberClaw;
    private int magicLoc;
    private int loggedIn;
    private int npc_count;
    private int npc_cache_count;
    private int objectAnimationCount;
    private boolean errorLoadingMemory;
    private boolean fogOfWar;
    private int game_width;
    private int game_height;
    private int const_9;
    private int trade_confirm_items_count;
    private int trade_confirm_items[];
    private int trade_confirm_item_count[];
    private String trade_recipient_name;
    private int selectedSpell;
    private boolean show_option_menu;
    private int mouseClickXStep;
    int mouseClickXX;
    int mouseClickXY;
    private int newBankItemCount;
    private int npcAnimationArray[][] = {
            {
                    11, 2, 9, 7, 1, 6, 10, 0, 5, 8,
                    3, 4
            }, {
            11, 2, 9, 7, 1, 6, 10, 0, 5, 8,
            3, 4
    }, {
            11, 3, 2, 9, 7, 1, 6, 10, 0, 5,
            8, 4
    }, {
            3, 4, 2, 9, 7, 1, 6, 10, 8, 11,
            0, 5
    }, {
            3, 4, 2, 9, 7, 1, 6, 10, 8, 11,
            0, 5
    }, {
            4, 3, 2, 9, 7, 1, 6, 10, 8, 11,
            0, 5
    }, {
            11, 4, 2, 9, 7, 1, 6, 10, 0, 5,
            8, 3
    }, {
            11, 2, 9, 7, 1, 6, 10, 0, 5, 8,
            4, 3
    }
    };
    private int player_stat_current[];
    private int control_welcome_newuser;
    private int control_welcome_existinguser;
    private int npcWalkModel[] = {
            0, 1, 2, 1
    };
    private int referid;
    private int anInt827;
    private int control_login_new_ok;
    private int teleport_bubble_type[];
    private Panel panel_login_welcome;
    private int combat_timeout;
    private Panel panel_login_newuser;
    private int option_menu_count;
    private boolean errorLoadingCodebase;
    private boolean show_dialog_shop;
    private int shop_item[];
    private int shop_item_count[];
    private int shop_item_price[];
    private boolean duel_offer_opponent_accepted;
    private boolean duel_offer_accepted;
    private GameModel game_models[];
    private int report_abuse_offence;
    private boolean show_dialog_duel;
    private String serverMessage;
    private boolean serverMessageBoxTop;
    private int camera_rotation_time;
    private int duel_opponent_items_count;
    private int duel_opponent_items[];
    private int duel_opponent_item_count[];
    private int duel_items_count;
    private int duel_items[];
    private int duel_item_count[];
    private Panel panel_social;
    int control_socialpanel;
    int ui_tab_social_sub_tab;
    long privateMessageTarget;
    private int player_stat_base[];
    private Character npcs_cache[];
    private boolean appletMode;
    private final int characterSkinColours[] = {
            0xecded0, 0xccb366, 0xb38c40, 0x997326, 0x906020
    };
    private int ground_item_x[];
    private int ground_item_y[];
    private int ground_item_id[];
    private int ground_item_z[];
    private int bank_selected_item_slot;
    private int bank_selected_item;
    private int duel_offer_opponent_item_count;
    private int duel_offer_opponent_item_id[];
    private int duel_offer_opponent_item_stack[];
    private int messageHistoryTimeout[];
    private boolean optionCameraModeAuto;
    private int object_x[];
    private int object_y[];
    private int object_id[];
    private int object_direction[];
    private final int characterTopBottomColours[] = {
            0xff0000, 0xff8000, 0xffe000, 0xa0e000, 57344, 32768, 41088, 45311, 33023, 12528,
            0xe000e0, 0x303030, 0x604000, 0x805000, 0xffffff
    };
    private int itemsAboveHeadCount;
    private int show_ui_wild_warn;
    private int selectedItemInventoryIndex;
    String selectedItemName;
    private byte soundData[];
    private int statFatigue;
    private int fatigueSleeping;
    private boolean loading_area;
    private int trade_recipient_confirm_items_count;
    private int trade_recipient_confirm_items[];
    private int trade_recipient_confirm_item_count[];
    private int trade_recipient_items_count;
    private int trade_recipient_items[];
    private int trade_recipient_item_count[];
    private boolean show_dialog_servermessage;
    private int menuItemID[];
    private boolean questComplete[];
    private GameModel wall_object_model[];
    private int menuX;
    private int menuY;
    private int menuWidth;
    private int menuHeight;
    private int menuItemsCount;
    private int itemAboveHeadX[];
    private int itemAboveHeadY[];
    private Panel panel_playerinfo;
    int control_playerinfopanel;
    int ui_tab_player_info_sub_tab;
    private int cameraZoom;
    private Panel panel_magic;
    int control_magicpanel;
    int tabMagicPrayer;
    private int show_ui_tab;
    private int trade_items_count;
    private int trade_items[];
    private int trade_item_count[];
    private int plane_width;
    private int plane_height;
    private int plane_multiplier;
    private int lastHeightOffset;
    //private int anInt917;// unused
    private boolean duel_settings_retreat;
    private boolean duel_settings_magic;
    private boolean duel_settings_prayer;
    private boolean duel_settings_weapons;
    private boolean show_dialog_bank;
    private int player_quest_points;
    private String login_user_desc;
    private String login_user_disp;
    private final int characterHairColours[] = {
            0xffc030, 0xffa040, 0x805030, 0x604020, 0x303030, 0xff6020, 0xff4000, 0xffffff, 65280, 65535
    };
    private int bank_active_page;
    private int welcomeLastLoggedInDays;
    private final String equipmentStatNames[] = {
            "Armour", "WeaponAim", "WeaponPower", "Magic", "Prayer"
    };
    private boolean optionMouseButtonOne;
    private int inventory_items_count;
    private int inventory_item_id[];
    private int inventory_item_stack_count[];
    private int inventory_equipped[];
    private final String skillNameShort[] = {
            "Attack", "Defense", "Strength", "Hits", "Ranged", "Prayer", "Magic", "Cooking", "Woodcut", "Fletching",
            "Fishing", "Firemaking", "Crafting", "Smithing", "Mining", "Herblaw", "Agility", "Thieving"
    };
    private Character known_players[];
    private String messageHistory[];
    private long duel_opponent_name_hash;
    private Panel panelAppearance;
    private int minimap_random_1;
    private int minimap_random_2;
    private Panel panel_login_existinguser;
    private boolean report_abuse_mute;
    private int object_count;
    private int duel_offer_item_count;
    private int duel_offer_item_id[];
    private int duel_offer_item_stack[];
    private int cameraAutoRotatePlayerX;
    private int cameraAutoRotatePlayerY;
    private int itemAboveHeadScale[];
    private int itemAboveHeadID[];
    private boolean sleepWordDelay;
    private boolean showAppearanceChange;
    private int shop_selected_item_index;
    private int shop_selected_item_type;
    private int projectile_max_range;
    private String sleepingStatusText;
    private int npcCombatModelArray1[] = {
            0, 1, 2, 1, 0, 0, 0, 0
    };
    private Character npcs[];
    private int experience_array[];
    private int healthBarX[];
    private int healthBarY[];
    private int healthBarMissing[];
    private final String skillNameLong[] = {
            "Attack", "Defense", "Strength", "Hits", "Ranged", "Prayer", "Magic", "Cooking", "Woodcutting", "Fletching",
            "Fishing", "Firemaking", "Crafting", "Smithing", "Mining", "Herblaw", "Agility", "Thieving"
    };
    private Character player_server[];
    private int player_count;
    private int known_player_count;
    private int sprite_count;
    private int walkPathX[];
    private int walkPathY[];
    private String welcomeLastLoggedInHost;
    private int wall_object_count;
    private int wall_object_x[];
    private int wall_object_y[];
    private int welcomeRecoverySetDays;
    private int localLowerX;
    private int localLowerY;
    private int localUpperX;
    private int localUpperY;
    private int welcomeLastLoggedInIP;
    private String menuItemText2[];
    private Character npcs_server[];
    private int sleepWordDelayTimer;
    private int player_stat_equipment[];
    private World world;
    private GameModel object_model[];
    /* unused
    private final String recovery_questions[] = {
            "Where were you born?", "What was your first teachers name?", "What is your fathers middle name?", "Who was your first best friend?", "What is your favourite vacation spot?", "What is your mothers middle name?", "What was your first pets name?", "What was the name of your first school?", "What is your mothers maiden name?", "Who was your first boyfriend/girlfriend?",
            "What was the first computer game you purchased?", "Who is your favourite actor/actress?", "Who is your favourite author?", "Who is your favourite musician?", "Who is your favourite cartoon character?", "What is your favourite book?", "What is your favourite food?", "What is your favourite movie?"
    };*/
}
