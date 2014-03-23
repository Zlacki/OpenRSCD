public class Scene {

    public Scene(Surface surface, int i, int j, int k) {
        anInt374 = 50;
        anIntArray375 = new int[anInt374];
        anIntArrayArray376 = new int[anInt374][256];
        clip_near = 5;
        clip_far_3d = 1000;
        clip_far_2d = 1000;
        fog_z_falloff = 20;
        fog_z_distance = 10;
        aBoolean386 = false;
        aDouble387 = 1.1000000000000001D;
        anInt388 = 1;
        aBoolean389 = false;
        anInt393 = 100;
        aGameModelArray394 = new GameModel[anInt393];
        anIntArray395 = new int[anInt393];
        anInt396 = 512;
        anInt397 = 256;
        anInt398 = 192;
        anInt399 = 256;
        anInt400 = 256;
        view_dist = 8;
        anInt402 = 4;
        anIntArray441 = new int[40];
        anIntArray442 = new int[40];
        anIntArray443 = new int[40];
        anIntArray444 = new int[40];
        anIntArray445 = new int[40];
        anIntArray446 = new int[40];
        interlace = false;
        this.surface = surface;
        anInt397 = surface.width2 / 2;
        anInt398 = surface.height2 / 2;
        anIntArray437 = surface.pixels;
        model_count = 0;
        max_model_count = i;
        models = new GameModel[max_model_count];
        // anIntArray412 = new int[max_model_count]; // only set not used
        anInt413 = 0;
        aPolygonArray414 = new Polygon[j];
        for (int l = 0; l < j; l++)
            aPolygonArray414[l] = new Polygon();

        sprite_count = 0;
        currentModel = new GameModel(k * 2, k);
        sprite_x = new int[k];
        anIntArray420 = new int[k];
        anIntArray421 = new int[k];
        sprite_z = new int[k];
        sprite_y = new int[k];
        anIntArray419 = new int[k];
        anIntArray422 = new int[k];
        if (aByteArray434 == null)
            aByteArray434 = new byte[17691];
        camera_x = 0;
        camera_y = 0;
        camera_z = 0;
        camera_pitch = 0;
        camera_yaw = 0;
        camera_roll = 0;
        for (int i1 = 0; i1 < 256; i1++) {
            anIntArray385[i1] = (int) (Math.sin((double) i1 * 0.02454369D) * 32768D);
            anIntArray385[i1 + 256] = (int) (Math.cos((double) i1 * 0.02454369D) * 32768D);
        }

        for (int j1 = 0; j1 < 1024; j1++) {
            anIntArray384[j1] = (int) (Math.sin((double) j1 * 0.00613592315D) * 32768D);
            anIntArray384[j1 + 1024] = (int) (Math.cos((double) j1 * 0.00613592315D) * 32768D);
        }

    }

    public void add_model(GameModel model) {
        if (model == null)
            System.out.println("Warning tried to add null object!");
        if (model_count < max_model_count) {
            //anIntArray412[model_count] = 0; // only set, not used
            models[model_count++] = model;
        }
    }

    public void free_model(GameModel gameModel) {
        for (int i = 0; i < model_count; i++)
            if (models[i] == gameModel) {
                model_count--;
                for (int j = i; j < model_count; j++) {
                    models[j] = models[j + 1];
                    //anIntArray412[j] = anIntArray412[j + 1]; // only set, not used
                }

            }

    }

    public void clear() {
        method266();
        for (int i = 0; i < model_count; i++)
            models[i] = null;

        model_count = 0;
    }

    public void method266() {
        sprite_count = 0;
        currentModel.clear();
    }

    public void reduce_sprites(int i) {
        sprite_count -= i;
        currentModel.reduce(i, i * 2);
        if (sprite_count < 0)
            sprite_count = 0;
    }

    public int draw_sprite(int x, int z, int y, int l, int i1, int j1, int tag) {
        sprite_x[sprite_count] = x;
        sprite_z[sprite_count] = z;
        sprite_y[sprite_count] = y;
        anIntArray419[sprite_count] = l;
        anIntArray420[sprite_count] = i1;
        anIntArray421[sprite_count] = j1;
        anIntArray422[sprite_count] = 0;
        int l1 = currentModel.method180(z, y, l);
        int i2 = currentModel.method180(z, y - j1, l);
        int vs[] = {
                l1, i2
        };
        currentModel.create_face(2, vs, 0, 0);
        currentModel.face_tag[sprite_count] = tag;
        currentModel.face_sprite_type[sprite_count++] = 0;
        return sprite_count - 1;
    }

    public void method269(int i) {
        currentModel.face_sprite_type[i] = 1;
    }

    public void method270(int i, int j) {
        anIntArray422[i] = j;
    }

    public void set_mouse_loc(int x, int y) {
        mouse_x = x - anInt399;
        mouse_y = y;
        anInt392 = 0;
        aBoolean389 = true;
    }

    public int method272() {
        return anInt392;
    }

    public int[] getPlayersUndorCursor() {
        return anIntArray395;
    }

    public GameModel[] getObjectsUnderCursor() {
        return aGameModelArray394;
    }

    public void set_midpoints(int width1, int height1, int width2, int height2, int fullwidth, int const_9) {
        anInt397 = width2;
        anInt398 = height2;
        anInt399 = width1;
        anInt400 = height1;
        anInt396 = fullwidth;
        view_dist = const_9;
        aClass8Array438 = new Class8[height2 + height1];
        for (int k1 = 0; k1 < height2 + height1; k1++)
            aClass8Array438[k1] = new Class8();

    }

    private void qsort(Polygon polygons[], int low, int high) {
        if (low < high) {
            int min = low - 1;
            int max = high + 1;
            int mid = (low + high) / 2;
            Polygon polygon = polygons[mid];
            polygons[mid] = polygons[low];
            polygons[low] = polygon;
            int j1 = polygon.depth;
            while (min < max) {
                do
                    max--;
                while (polygons[max].depth < j1);
                do
                    min++;
                while (polygons[min].depth > j1);
                if (min < max) {
                    Polygon polygon_1 = polygons[min];
                    polygons[min] = polygons[max];
                    polygons[max] = polygon_1;
                }
            }
            qsort(polygons, low, max);
            qsort(polygons, max + 1, high);
        }
    }

    public void method277(int i, Polygon aclass7[], int j) {
        for (int k = 0; k <= j; k++) {
            aclass7[k].aBoolean367 = false;
            aclass7[k].anInt368 = k;
            aclass7[k].anInt369 = -1;
        }

        int l = 0;
        do {
            while (aclass7[l].aBoolean367)
                l++;
            if (l == j)
                return;
            Polygon polygon = aclass7[l];
            polygon.aBoolean367 = true;
            int i1 = l;
            int j1 = l + i;
            if (j1 >= j)
                j1 = j - 1;
            for (int k1 = j1; k1 >= i1 + 1; k1--) {
                Polygon polygon_1 = aclass7[k1];
                if (polygon.anInt353 < polygon_1.anInt355 && polygon_1.anInt353 < polygon.anInt355 && polygon.anInt354 < polygon_1.anInt356 && polygon_1.anInt354 < polygon.anInt356 && polygon.anInt368 != polygon_1.anInt369 && !method295(polygon, polygon_1) && method296(polygon_1, polygon)) {
                    method278(aclass7, i1, k1);
                    if (aclass7[k1] != polygon_1)
                        k1++;
                    i1 = anInt454;
                    polygon_1.anInt369 = polygon.anInt368;
                }
            }

        } while (true);
    }

    public boolean method278(Polygon aclass7[], int i, int j) {
        do {
            Polygon polygon = aclass7[i];
            for (int k = i + 1; k <= j; k++) {
                Polygon polygon_1 = aclass7[k];
                if (!method295(polygon_1, polygon))
                    break;
                aclass7[i] = polygon_1;
                aclass7[k] = polygon;
                i = k;
                if (i == j) {
                    anInt454 = i;
                    anInt455 = i - 1;
                    return true;
                }
            }

            Polygon polygon_2 = aclass7[j];
            for (int l = j - 1; l >= i; l--) {
                Polygon polygon_3 = aclass7[l];
                if (!method295(polygon_2, polygon_3))
                    break;
                aclass7[j] = polygon_3;
                aclass7[l] = polygon_2;
                j = l;
                if (i == j) {
                    anInt454 = j + 1;
                    anInt455 = j;
                    return true;
                }
            }

            if (i + 1 >= j) {
                anInt454 = i;
                anInt455 = j;
                return false;
            }
            if (!method278(aclass7, i + 1, j)) {
                anInt454 = i;
                return false;
            }
            j = anInt455;
        } while (true);
    }

    public void method279(int i, int j, int k) {
        int l = -camera_pitch + 1024 & 0x3ff;
        int i1 = -camera_yaw + 1024 & 0x3ff;
        int j1 = -camera_roll + 1024 & 0x3ff;
        if (j1 != 0) {
            int k1 = anIntArray384[j1];
            int j2 = anIntArray384[j1 + 1024];
            int i3 = j * k1 + i * j2 >> 15;
            j = j * j2 - i * k1 >> 15;
            i = i3;
        }
        if (l != 0) {
            int l1 = anIntArray384[l];
            int k2 = anIntArray384[l + 1024];
            int j3 = j * k2 - k * l1 >> 15;
            k = j * l1 + k * k2 >> 15;
            j = j3;
        }
        if (i1 != 0) {
            int i2 = anIntArray384[i1];
            int l2 = anIntArray384[i1 + 1024];
            int k3 = k * i2 + i * l2 >> 15;
            k = k * l2 - i * i2 >> 15;
            i = k3;
        }
        if (i < frustum_left)
            frustum_left = i;
        if (i > frustum_right)
            frustum_right = i;
        if (j < frustum_bottom)
            frustum_bottom = j;
        if (j > frustum_top)
            frustum_top = j;
        if (k < frustum_near)
            frustum_near = k;
        if (k > frustum_far)
            frustum_far = k;
    }

    public void endscene() {
        interlace = surface.interlace;
        int i3 = anInt397 * clip_far_3d >> view_dist;
        int j3 = anInt398 * clip_far_3d >> view_dist;
        frustum_left = 0;
        frustum_right = 0;
        frustum_bottom = 0;
        frustum_top = 0;
        frustum_near = 0;
        frustum_far = 0;
        method279(-i3, -j3, clip_far_3d);
        method279(-i3, j3, clip_far_3d);
        method279(i3, -j3, clip_far_3d);
        method279(i3, j3, clip_far_3d);
        method279(-anInt397, -anInt398, 0);
        method279(-anInt397, anInt398, 0);
        method279(anInt397, -anInt398, 0);
        method279(anInt397, anInt398, 0);
        frustum_left += camera_x;
        frustum_right += camera_x;
        frustum_bottom += camera_y;
        frustum_top += camera_y;
        frustum_near += camera_z;
        frustum_far += camera_z;
        models[model_count] = currentModel;
        currentModel.transform_state = 2;
        for (int i = 0; i < model_count; i++)
            models[i].project(camera_x, camera_y, camera_z, camera_pitch, camera_yaw, camera_roll, view_dist, clip_near);

        models[model_count].project(camera_x, camera_y, camera_z, camera_pitch, camera_yaw, camera_roll, view_dist, clip_near);
        anInt413 = 0;
        for (int count = 0; count < model_count; count++) {
            GameModel gameModel = models[count];
            if (gameModel.visible) {
                for (int j = 0; j < gameModel.num_faces; j++) {
                    int l3 = gameModel.face_num_vertices[j];
                    int ai1[] = gameModel.face_vertices[j];
                    boolean flag = false;
                    for (int k4 = 0; k4 < l3; k4++) {
                        int i1 = gameModel.vertex_camera_z[ai1[k4]];
                        if (i1 <= clip_near || i1 >= clip_far_3d)
                            continue;
                        flag = true;
                        break;
                    }

                    if (flag) {
                        int l1 = 0;
                        for (int k5 = 0; k5 < l3; k5++) {
                            int j1 = gameModel.vertex_view_x[ai1[k5]];
                            if (j1 > -anInt397)
                                l1 |= 1;
                            if (j1 < anInt397)
                                l1 |= 2;
                            if (l1 == 3)
                                break;
                        }

                        if (l1 == 3) {
                            int i2 = 0;
                            for (int l6 = 0; l6 < l3; l6++) {
                                int k1 = gameModel.vertex_view_y[ai1[l6]];
                                if (k1 > -anInt398)
                                    i2 |= 1;
                                if (k1 < anInt398)
                                    i2 |= 2;
                                if (i2 == 3)
                                    break;
                            }

                            if (i2 == 3) {
                                Polygon polygon_1 = aPolygonArray414[anInt413];
                                polygon_1.model = gameModel;
                                polygon_1.anInt360 = j;
                                method293(anInt413);
                                int l8;
                                if (polygon_1.anInt365 < 0)
                                    l8 = gameModel.face_fill_front[j];
                                else
                                    l8 = gameModel.face_fill_back[j];
                                if (l8 != 0xbc614e) {
                                    int j2 = 0;
                                    for (int l9 = 0; l9 < l3; l9++)
                                        j2 += gameModel.vertex_camera_z[ai1[l9]];

                                    int l2;
                                    polygon_1.depth = l2 = j2 / l3 + gameModel.depth;
                                    polygon_1.anInt366 = l8;
                                    anInt413++;
                                }
                            }
                        }
                    }
                }

            }
        }

        GameModel model_2d = currentModel;
        if (model_2d.visible) {
            for (int face = 0; face < model_2d.num_faces; face++) {
                int ai[] = model_2d.face_vertices[face];
                int j4 = ai[0];
                int l4 = model_2d.vertex_view_x[j4];
                int l5 = model_2d.vertex_view_y[j4];
                int i7 = model_2d.vertex_camera_z[j4];
                if (i7 > clip_near && i7 < clip_far_2d) {
                    int i8 = (anIntArray420[face] << view_dist) / i7;
                    int i9 = (anIntArray421[face] << view_dist) / i7;
                    if (l4 - i8 / 2 <= anInt397 && l4 + i8 / 2 >= -anInt397 && l5 - i9 <= anInt398 && l5 >= -anInt398) {
                        Polygon polygon_2 = aPolygonArray414[anInt413];
                        polygon_2.model = model_2d;
                        polygon_2.anInt360 = face;
                        method294(anInt413);
                        polygon_2.depth = (i7 + model_2d.vertex_camera_z[ai[1]]) / 2;
                        anInt413++;
                    }
                }
            }

        }
        if (anInt413 == 0)
            return;
        anInt378 = anInt413;
        qsort(aPolygonArray414, 0, anInt413 - 1);
        method277(100, aPolygonArray414, anInt413);
        for (int i4 = 0; i4 < anInt413; i4++) {
            Polygon polygon = aPolygonArray414[i4];
            GameModel gameModel_2 = polygon.model;
            int l = polygon.anInt360;
            if (gameModel_2 == currentModel) {
                int ai2[] = gameModel_2.face_vertices[l];
                int i6 = ai2[0];
                int j7 = gameModel_2.vertex_view_x[i6];
                int j8 = gameModel_2.vertex_view_y[i6];
                int j9 = gameModel_2.vertex_camera_z[i6];
                int i10 = (anIntArray420[l] << view_dist) / j9;
                int k10 = (anIntArray421[l] << view_dist) / j9;
                int i11 = j8 - gameModel_2.vertex_view_y[ai2[1]];
                int j11 = ((gameModel_2.vertex_view_x[ai2[1]] - j7) * i11) / k10;
                j11 = gameModel_2.vertex_view_x[ai2[1]] - j7;
                int l11 = j7 - i10 / 2;
                int j12 = (anInt400 + j8) - k10;
                surface.sprite_clipping(l11 + anInt399, j12, i10, k10, sprite_x[l], j11, (256 << view_dist) / j9);
                if (aBoolean389 && anInt392 < anInt393) {
                    l11 += (anIntArray422[l] << view_dist) / j9;
                    if (mouse_y >= j12 && mouse_y <= j12 + k10 && mouse_x >= l11 && mouse_x <= l11 + i10 && !gameModel_2.pickable && gameModel_2.face_sprite_type[l] == 0) {
                        aGameModelArray394[anInt392] = gameModel_2;
                        anIntArray395[anInt392] = l;
                        anInt392++;
                    }
                }
            } else {
                int k8 = 0;
                int j10 = 0;
                int l10 = gameModel_2.face_num_vertices[l];
                int ai3[] = gameModel_2.face_vertices[l];
                if (gameModel_2.face_intensity[l] != 0xbc614e)
                    if (polygon.anInt365 < 0)
                        j10 = gameModel_2.light_ambience - gameModel_2.face_intensity[l];
                    else
                        j10 = gameModel_2.light_ambience + gameModel_2.face_intensity[l];
                for (int k11 = 0; k11 < l10; k11++) {
                    int k2 = ai3[k11];
                    anIntArray444[k11] = gameModel_2.vertex_camera_x[k2];
                    anIntArray445[k11] = gameModel_2.vertex_camera_y[k2];
                    anIntArray446[k11] = gameModel_2.vertex_camera_z[k2];
                    if (gameModel_2.face_intensity[l] == 0xbc614e)
                        if (polygon.anInt365 < 0)
                            j10 = (gameModel_2.light_ambience - gameModel_2.vertex_intensity[k2]) + gameModel_2.vertex_ambience[k2];
                        else
                            j10 = gameModel_2.light_ambience + gameModel_2.vertex_intensity[k2] + gameModel_2.vertex_ambience[k2];
                    if (gameModel_2.vertex_camera_z[k2] >= clip_near) {
                        anIntArray441[k8] = gameModel_2.vertex_view_x[k2];
                        anIntArray442[k8] = gameModel_2.vertex_view_y[k2];
                        anIntArray443[k8] = j10;
                        if (gameModel_2.vertex_camera_z[k2] > fog_z_distance)
                            anIntArray443[k8] += (gameModel_2.vertex_camera_z[k2] - fog_z_distance) / fog_z_falloff;
                        k8++;
                    } else {
                        int k9;
                        if (k11 == 0)
                            k9 = ai3[l10 - 1];
                        else
                            k9 = ai3[k11 - 1];
                        if (gameModel_2.vertex_camera_z[k9] >= clip_near) {
                            int k7 = gameModel_2.vertex_camera_z[k2] - gameModel_2.vertex_camera_z[k9];
                            int i5 = gameModel_2.vertex_camera_x[k2] - ((gameModel_2.vertex_camera_x[k2] - gameModel_2.vertex_camera_x[k9]) * (gameModel_2.vertex_camera_z[k2] - clip_near)) / k7;
                            int j6 = gameModel_2.vertex_camera_y[k2] - ((gameModel_2.vertex_camera_y[k2] - gameModel_2.vertex_camera_y[k9]) * (gameModel_2.vertex_camera_z[k2] - clip_near)) / k7;
                            anIntArray441[k8] = (i5 << view_dist) / clip_near;
                            anIntArray442[k8] = (j6 << view_dist) / clip_near;
                            anIntArray443[k8] = j10;
                            k8++;
                        }
                        if (k11 == l10 - 1)
                            k9 = ai3[0];
                        else
                            k9 = ai3[k11 + 1];
                        if (gameModel_2.vertex_camera_z[k9] >= clip_near) {
                            int l7 = gameModel_2.vertex_camera_z[k2] - gameModel_2.vertex_camera_z[k9];
                            int j5 = gameModel_2.vertex_camera_x[k2] - ((gameModel_2.vertex_camera_x[k2] - gameModel_2.vertex_camera_x[k9]) * (gameModel_2.vertex_camera_z[k2] - clip_near)) / l7;
                            int k6 = gameModel_2.vertex_camera_y[k2] - ((gameModel_2.vertex_camera_y[k2] - gameModel_2.vertex_camera_y[k9]) * (gameModel_2.vertex_camera_z[k2] - clip_near)) / l7;
                            anIntArray441[k8] = (j5 << view_dist) / clip_near;
                            anIntArray442[k8] = (k6 << view_dist) / clip_near;
                            anIntArray443[k8] = j10;
                            k8++;
                        }
                    }
                }

                for (int i12 = 0; i12 < l10; i12++) {
                    if (anIntArray443[i12] < 0)
                        anIntArray443[i12] = 0;
                    else if (anIntArray443[i12] > 255)
                        anIntArray443[i12] = 255;
                    if (polygon.anInt366 >= 0)
                        if (anIntArray427[polygon.anInt366] == 1)
                            anIntArray443[i12] <<= 9;
                        else
                            anIntArray443[i12] <<= 6;
                }

                method281(0, 0, 0, 0, k8, anIntArray441, anIntArray442, anIntArray443, gameModel_2, l);
                if (anInt440 > anInt439)
                    method282(0, 0, l10, anIntArray444, anIntArray445, anIntArray446, polygon.anInt366, gameModel_2);
            }
        }

        aBoolean389 = false;
    }

    private void method281(int i, int j, int k, int l, int i1, int ai[], int ai1[],
                           int ai2[], GameModel gameModel, int j1) {
        if (i1 == 3) {
            int k1 = ai1[0] + anInt400;
            int k2 = ai1[1] + anInt400;
            int k3 = ai1[2] + anInt400;
            int k4 = ai[0];
            int l5 = ai[1];
            int j7 = ai[2];
            int l8 = ai2[0];
            int j10 = ai2[1];
            int j11 = ai2[2];
            int j12 = (anInt400 + anInt398) - 1;
            int l12 = 0;
            int j13 = 0;
            int l13 = 0;
            int j14 = 0;
            int l14 = 0xbc614e;
            int j15 = 0xff439eb2;
            if (k3 != k1) {
                j13 = (j7 - k4 << 8) / (k3 - k1);
                j14 = (j11 - l8 << 8) / (k3 - k1);
                if (k1 < k3) {
                    l12 = k4 << 8;
                    l13 = l8 << 8;
                    l14 = k1;
                    j15 = k3;
                } else {
                    l12 = j7 << 8;
                    l13 = j11 << 8;
                    l14 = k3;
                    j15 = k1;
                }
                if (l14 < 0) {
                    l12 -= j13 * l14;
                    l13 -= j14 * l14;
                    l14 = 0;
                }
                if (j15 > j12)
                    j15 = j12;
            }
            int l15 = 0;
            int j16 = 0;
            int l16 = 0;
            int j17 = 0;
            int l17 = 0xbc614e;
            int j18 = 0xff439eb2;
            if (k2 != k1) {
                j16 = (l5 - k4 << 8) / (k2 - k1);
                j17 = (j10 - l8 << 8) / (k2 - k1);
                if (k1 < k2) {
                    l15 = k4 << 8;
                    l16 = l8 << 8;
                    l17 = k1;
                    j18 = k2;
                } else {
                    l15 = l5 << 8;
                    l16 = j10 << 8;
                    l17 = k2;
                    j18 = k1;
                }
                if (l17 < 0) {
                    l15 -= j16 * l17;
                    l16 -= j17 * l17;
                    l17 = 0;
                }
                if (j18 > j12)
                    j18 = j12;
            }
            int l18 = 0;
            int j19 = 0;
            int l19 = 0;
            int j20 = 0;
            int l20 = 0xbc614e;
            int j21 = 0xff439eb2;
            if (k3 != k2) {
                j19 = (j7 - l5 << 8) / (k3 - k2);
                j20 = (j11 - j10 << 8) / (k3 - k2);
                if (k2 < k3) {
                    l18 = l5 << 8;
                    l19 = j10 << 8;
                    l20 = k2;
                    j21 = k3;
                } else {
                    l18 = j7 << 8;
                    l19 = j11 << 8;
                    l20 = k3;
                    j21 = k2;
                }
                if (l20 < 0) {
                    l18 -= j19 * l20;
                    l19 -= j20 * l20;
                    l20 = 0;
                }
                if (j21 > j12)
                    j21 = j12;
            }
            anInt439 = l14;
            if (l17 < anInt439)
                anInt439 = l17;
            if (l20 < anInt439)
                anInt439 = l20;
            anInt440 = j15;
            if (j18 > anInt440)
                anInt440 = j18;
            if (j21 > anInt440)
                anInt440 = j21;
            int l21 = 0;
            for (k = anInt439; k < anInt440; k++) {
                if (k >= l14 && k < j15) {
                    i = j = l12;
                    l = l21 = l13;
                    l12 += j13;
                    l13 += j14;
                } else {
                    i = 0xa0000;
                    j = 0xfff60000;
                }
                if (k >= l17 && k < j18) {
                    if (l15 < i) {
                        i = l15;
                        l = l16;
                    }
                    if (l15 > j) {
                        j = l15;
                        l21 = l16;
                    }
                    l15 += j16;
                    l16 += j17;
                }
                if (k >= l20 && k < j21) {
                    if (l18 < i) {
                        i = l18;
                        l = l19;
                    }
                    if (l18 > j) {
                        j = l18;
                        l21 = l19;
                    }
                    l18 += j19;
                    l19 += j20;
                }
                Class8 class8_6 = aClass8Array438[k];
                class8_6.anInt370 = i;
                class8_6.anInt371 = j;
                class8_6.anInt372 = l;
                class8_6.anInt373 = l21;
            }

            if (anInt439 < anInt400 - anInt398)
                anInt439 = anInt400 - anInt398;
        } else if (i1 == 4) {
            int l1 = ai1[0] + anInt400;
            int l2 = ai1[1] + anInt400;
            int l3 = ai1[2] + anInt400;
            int l4 = ai1[3] + anInt400;
            int i6 = ai[0];
            int k7 = ai[1];
            int i9 = ai[2];
            int k10 = ai[3];
            int k11 = ai2[0];
            int k12 = ai2[1];
            int i13 = ai2[2];
            int k13 = ai2[3];
            int i14 = (anInt400 + anInt398) - 1;
            int k14 = 0;
            int i15 = 0;
            int k15 = 0;
            int i16 = 0;
            int k16 = 0xbc614e;
            int i17 = 0xff439eb2;
            if (l4 != l1) {
                i15 = (k10 - i6 << 8) / (l4 - l1);
                i16 = (k13 - k11 << 8) / (l4 - l1);
                if (l1 < l4) {
                    k14 = i6 << 8;
                    k15 = k11 << 8;
                    k16 = l1;
                    i17 = l4;
                } else {
                    k14 = k10 << 8;
                    k15 = k13 << 8;
                    k16 = l4;
                    i17 = l1;
                }
                if (k16 < 0) {
                    k14 -= i15 * k16;
                    k15 -= i16 * k16;
                    k16 = 0;
                }
                if (i17 > i14)
                    i17 = i14;
            }
            int k17 = 0;
            int i18 = 0;
            int k18 = 0;
            int i19 = 0;
            int k19 = 0xbc614e;
            int i20 = 0xff439eb2;
            if (l2 != l1) {
                i18 = (k7 - i6 << 8) / (l2 - l1);
                i19 = (k12 - k11 << 8) / (l2 - l1);
                if (l1 < l2) {
                    k17 = i6 << 8;
                    k18 = k11 << 8;
                    k19 = l1;
                    i20 = l2;
                } else {
                    k17 = k7 << 8;
                    k18 = k12 << 8;
                    k19 = l2;
                    i20 = l1;
                }
                if (k19 < 0) {
                    k17 -= i18 * k19;
                    k18 -= i19 * k19;
                    k19 = 0;
                }
                if (i20 > i14)
                    i20 = i14;
            }
            int k20 = 0;
            int i21 = 0;
            int k21 = 0;
            int i22 = 0;
            int j22 = 0xbc614e;
            int k22 = 0xff439eb2;
            if (l3 != l2) {
                i21 = (i9 - k7 << 8) / (l3 - l2);
                i22 = (i13 - k12 << 8) / (l3 - l2);
                if (l2 < l3) {
                    k20 = k7 << 8;
                    k21 = k12 << 8;
                    j22 = l2;
                    k22 = l3;
                } else {
                    k20 = i9 << 8;
                    k21 = i13 << 8;
                    j22 = l3;
                    k22 = l2;
                }
                if (j22 < 0) {
                    k20 -= i21 * j22;
                    k21 -= i22 * j22;
                    j22 = 0;
                }
                if (k22 > i14)
                    k22 = i14;
            }
            int l22 = 0;
            int i23 = 0;
            int j23 = 0;
            int k23 = 0;
            int l23 = 0xbc614e;
            int i24 = 0xff439eb2;
            if (l4 != l3) {
                i23 = (k10 - i9 << 8) / (l4 - l3);
                k23 = (k13 - i13 << 8) / (l4 - l3);
                if (l3 < l4) {
                    l22 = i9 << 8;
                    j23 = i13 << 8;
                    l23 = l3;
                    i24 = l4;
                } else {
                    l22 = k10 << 8;
                    j23 = k13 << 8;
                    l23 = l4;
                    i24 = l3;
                }
                if (l23 < 0) {
                    l22 -= i23 * l23;
                    j23 -= k23 * l23;
                    l23 = 0;
                }
                if (i24 > i14)
                    i24 = i14;
            }
            anInt439 = k16;
            if (k19 < anInt439)
                anInt439 = k19;
            if (j22 < anInt439)
                anInt439 = j22;
            if (l23 < anInt439)
                anInt439 = l23;
            anInt440 = i17;
            if (i20 > anInt440)
                anInt440 = i20;
            if (k22 > anInt440)
                anInt440 = k22;
            if (i24 > anInt440)
                anInt440 = i24;
            int j24 = 0;
            for (k = anInt439; k < anInt440; k++) {
                if (k >= k16 && k < i17) {
                    i = j = k14;
                    l = j24 = k15;
                    k14 += i15;
                    k15 += i16;
                } else {
                    i = 0xa0000;
                    j = 0xfff60000;
                }
                if (k >= k19 && k < i20) {
                    if (k17 < i) {
                        i = k17;
                        l = k18;
                    }
                    if (k17 > j) {
                        j = k17;
                        j24 = k18;
                    }
                    k17 += i18;
                    k18 += i19;
                }
                if (k >= j22 && k < k22) {
                    if (k20 < i) {
                        i = k20;
                        l = k21;
                    }
                    if (k20 > j) {
                        j = k20;
                        j24 = k21;
                    }
                    k20 += i21;
                    k21 += i22;
                }
                if (k >= l23 && k < i24) {
                    if (l22 < i) {
                        i = l22;
                        l = j23;
                    }
                    if (l22 > j) {
                        j = l22;
                        j24 = j23;
                    }
                    l22 += i23;
                    j23 += k23;
                }
                Class8 class8_7 = aClass8Array438[k];
                class8_7.anInt370 = i;
                class8_7.anInt371 = j;
                class8_7.anInt372 = l;
                class8_7.anInt373 = j24;
            }

            if (anInt439 < anInt400 - anInt398)
                anInt439 = anInt400 - anInt398;
        } else {
            anInt440 = anInt439 = ai1[0] += anInt400;
            for (k = 1; k < i1; k++) {
                int i2;
                if ((i2 = ai1[k] += anInt400) < anInt439)
                    anInt439 = i2;
                else if (i2 > anInt440)
                    anInt440 = i2;
            }

            if (anInt439 < anInt400 - anInt398)
                anInt439 = anInt400 - anInt398;
            if (anInt440 >= anInt400 + anInt398)
                anInt440 = (anInt400 + anInt398) - 1;
            if (anInt439 >= anInt440)
                return;
            for (k = anInt439; k < anInt440; k++) {
                Class8 class8 = aClass8Array438[k];
                class8.anInt370 = 0xa0000;
                class8.anInt371 = 0xfff60000;
            }

            int j2 = i1 - 1;
            int i3 = ai1[0];
            int i4 = ai1[j2];
            if (i3 < i4) {
                int i5 = ai[0] << 8;
                int j6 = (ai[j2] - ai[0] << 8) / (i4 - i3);
                int l7 = ai2[0] << 8;
                int j9 = (ai2[j2] - ai2[0] << 8) / (i4 - i3);
                if (i3 < 0) {
                    i5 -= j6 * i3;
                    l7 -= j9 * i3;
                    i3 = 0;
                }
                if (i4 > anInt440)
                    i4 = anInt440;
                for (k = i3; k <= i4; k++) {
                    Class8 class8_2 = aClass8Array438[k];
                    class8_2.anInt370 = class8_2.anInt371 = i5;
                    class8_2.anInt372 = class8_2.anInt373 = l7;
                    i5 += j6;
                    l7 += j9;
                }

            } else if (i3 > i4) {
                int j5 = ai[j2] << 8;
                int k6 = (ai[0] - ai[j2] << 8) / (i3 - i4);
                int i8 = ai2[j2] << 8;
                int k9 = (ai2[0] - ai2[j2] << 8) / (i3 - i4);
                if (i4 < 0) {
                    j5 -= k6 * i4;
                    i8 -= k9 * i4;
                    i4 = 0;
                }
                if (i3 > anInt440)
                    i3 = anInt440;
                for (k = i4; k <= i3; k++) {
                    Class8 class8_3 = aClass8Array438[k];
                    class8_3.anInt370 = class8_3.anInt371 = j5;
                    class8_3.anInt372 = class8_3.anInt373 = i8;
                    j5 += k6;
                    i8 += k9;
                }

            }
            for (k = 0; k < j2; k++) {
                int k5 = k + 1;
                int j3 = ai1[k];
                int j4 = ai1[k5];
                if (j3 < j4) {
                    int l6 = ai[k] << 8;
                    int j8 = (ai[k5] - ai[k] << 8) / (j4 - j3);
                    int l9 = ai2[k] << 8;
                    int l10 = (ai2[k5] - ai2[k] << 8) / (j4 - j3);
                    if (j3 < 0) {
                        l6 -= j8 * j3;
                        l9 -= l10 * j3;
                        j3 = 0;
                    }
                    if (j4 > anInt440)
                        j4 = anInt440;
                    for (int l11 = j3; l11 <= j4; l11++) {
                        Class8 class8_4 = aClass8Array438[l11];
                        if (l6 < class8_4.anInt370) {
                            class8_4.anInt370 = l6;
                            class8_4.anInt372 = l9;
                        }
                        if (l6 > class8_4.anInt371) {
                            class8_4.anInt371 = l6;
                            class8_4.anInt373 = l9;
                        }
                        l6 += j8;
                        l9 += l10;
                    }

                } else if (j3 > j4) {
                    int i7 = ai[k5] << 8;
                    int k8 = (ai[k] - ai[k5] << 8) / (j3 - j4);
                    int i10 = ai2[k5] << 8;
                    int i11 = (ai2[k] - ai2[k5] << 8) / (j3 - j4);
                    if (j4 < 0) {
                        i7 -= k8 * j4;
                        i10 -= i11 * j4;
                        j4 = 0;
                    }
                    if (j3 > anInt440)
                        j3 = anInt440;
                    for (int i12 = j4; i12 <= j3; i12++) {
                        Class8 class8_5 = aClass8Array438[i12];
                        if (i7 < class8_5.anInt370) {
                            class8_5.anInt370 = i7;
                            class8_5.anInt372 = i10;
                        }
                        if (i7 > class8_5.anInt371) {
                            class8_5.anInt371 = i7;
                            class8_5.anInt373 = i10;
                        }
                        i7 += k8;
                        i10 += i11;
                    }

                }
            }

            if (anInt439 < anInt400 - anInt398)
                anInt439 = anInt400 - anInt398;
        }
        if (aBoolean389 && anInt392 < anInt393 && mouse_y >= anInt439 && mouse_y < anInt440) {
            Class8 class8_1 = aClass8Array438[mouse_y];
            if (mouse_x >= class8_1.anInt370 >> 8 && mouse_x <= class8_1.anInt371 >> 8 && class8_1.anInt370 <= class8_1.anInt371 && !gameModel.pickable && gameModel.face_sprite_type[j1] == 0) {
                aGameModelArray394[anInt392] = gameModel;
                anIntArray395[anInt392] = j1;
                anInt392++;
            }
        }
    }

    private void method282(int i, int j, int k, int ai[], int ai1[], int ai2[], int l,
                           GameModel gameModel) {
        if (l == -2)
            return;
        if (l >= 0) {
            if (l >= texture_count)
                l = 0;
            method299(l);
            int i1 = ai[0];
            int k1 = ai1[0];
            int j2 = ai2[0];
            int i3 = i1 - ai[1];
            int k3 = k1 - ai1[1];
            int i4 = j2 - ai2[1];
            k--;
            int i6 = ai[k] - i1;
            int j7 = ai1[k] - k1;
            int k8 = ai2[k] - j2;
            if (anIntArray427[l] == 1) {
                int l9 = i6 * k1 - j7 * i1 << 12;
                int k10 = j7 * j2 - k8 * k1 << (5 - view_dist) + 7 + 4;
                int i11 = k8 * i1 - i6 * j2 << (5 - view_dist) + 7;
                int k11 = i3 * k1 - k3 * i1 << 12;
                int i12 = k3 * j2 - i4 * k1 << (5 - view_dist) + 7 + 4;
                int k12 = i4 * i1 - i3 * j2 << (5 - view_dist) + 7;
                int i13 = k3 * i6 - i3 * j7 << 5;
                int k13 = i4 * j7 - k3 * k8 << (5 - view_dist) + 4;
                int i14 = i3 * k8 - i4 * i6 >> view_dist - 5;
                int k14 = k10 >> 4;
                int i15 = i12 >> 4;
                int k15 = k13 >> 4;
                int i16 = anInt439 - anInt400;
                int k16 = anInt396;
                int i17 = anInt399 + anInt439 * k16;
                byte byte1 = 1;
                l9 += i11 * i16;
                k11 += k12 * i16;
                i13 += i14 * i16;
                if (interlace) {
                    if ((anInt439 & 1) == 1) {
                        anInt439++;
                        l9 += i11;
                        k11 += k12;
                        i13 += i14;
                        i17 += k16;
                    }
                    i11 <<= 1;
                    k12 <<= 1;
                    i14 <<= 1;
                    k16 <<= 1;
                    byte1 = 2;
                }
                if (gameModel.unknown_scene_boolean) {
                    for (i = anInt439; i < anInt440; i += byte1) {
                        Class8 class8_3 = aClass8Array438[i];
                        j = class8_3.anInt370 >> 8;
                        int k17 = class8_3.anInt371 >> 8;
                        int k20 = k17 - j;
                        if (k20 <= 0) {
                            l9 += i11;
                            k11 += k12;
                            i13 += i14;
                            i17 += k16;
                        } else {
                            int i22 = class8_3.anInt372;
                            int k23 = (class8_3.anInt373 - i22) / k20;
                            if (j < -anInt397) {
                                i22 += (-anInt397 - j) * k23;
                                j = -anInt397;
                                k20 = k17 - j;
                            }
                            if (k17 > anInt397) {
                                int l17 = anInt397;
                                k20 = l17 - j;
                            }
                            method284(anIntArray437, anIntArrayArray429[l], 0, 0, l9 + k14 * j, k11 + i15 * j, i13 + k15 * j, k10, i12, k13, k20, i17 + j, i22, k23 << 2);
                            l9 += i11;
                            k11 += k12;
                            i13 += i14;
                            i17 += k16;
                        }
                    }

                    return;
                }
                if (!aBooleanArray430[l]) {
                    for (i = anInt439; i < anInt440; i += byte1) {
                        Class8 class8_4 = aClass8Array438[i];
                        j = class8_4.anInt370 >> 8;
                        int i18 = class8_4.anInt371 >> 8;
                        int l20 = i18 - j;
                        if (l20 <= 0) {
                            l9 += i11;
                            k11 += k12;
                            i13 += i14;
                            i17 += k16;
                        } else {
                            int j22 = class8_4.anInt372;
                            int l23 = (class8_4.anInt373 - j22) / l20;
                            if (j < -anInt397) {
                                j22 += (-anInt397 - j) * l23;
                                j = -anInt397;
                                l20 = i18 - j;
                            }
                            if (i18 > anInt397) {
                                int j18 = anInt397;
                                l20 = j18 - j;
                            }
                            method283(anIntArray437, anIntArrayArray429[l], 0, 0, l9 + k14 * j, k11 + i15 * j, i13 + k15 * j, k10, i12, k13, l20, i17 + j, j22, l23 << 2);
                            l9 += i11;
                            k11 += k12;
                            i13 += i14;
                            i17 += k16;
                        }
                    }

                    return;
                }
                for (i = anInt439; i < anInt440; i += byte1) {
                    Class8 class8_5 = aClass8Array438[i];
                    j = class8_5.anInt370 >> 8;
                    int k18 = class8_5.anInt371 >> 8;
                    int i21 = k18 - j;
                    if (i21 <= 0) {
                        l9 += i11;
                        k11 += k12;
                        i13 += i14;
                        i17 += k16;
                    } else {
                        int k22 = class8_5.anInt372;
                        int i24 = (class8_5.anInt373 - k22) / i21;
                        if (j < -anInt397) {
                            k22 += (-anInt397 - j) * i24;
                            j = -anInt397;
                            i21 = k18 - j;
                        }
                        if (k18 > anInt397) {
                            int l18 = anInt397;
                            i21 = l18 - j;
                        }
                        method285(anIntArray437, 0, 0, 0, anIntArrayArray429[l], l9 + k14 * j, k11 + i15 * j, i13 + k15 * j, k10, i12, k13, i21, i17 + j, k22, i24);
                        l9 += i11;
                        k11 += k12;
                        i13 += i14;
                        i17 += k16;
                    }
                }

                return;
            }
            int i10 = i6 * k1 - j7 * i1 << 11;
            int l10 = j7 * j2 - k8 * k1 << (5 - view_dist) + 6 + 4;
            int j11 = k8 * i1 - i6 * j2 << (5 - view_dist) + 6;
            int l11 = i3 * k1 - k3 * i1 << 11;
            int j12 = k3 * j2 - i4 * k1 << (5 - view_dist) + 6 + 4;
            int l12 = i4 * i1 - i3 * j2 << (5 - view_dist) + 6;
            int j13 = k3 * i6 - i3 * j7 << 5;
            int l13 = i4 * j7 - k3 * k8 << (5 - view_dist) + 4;
            int j14 = i3 * k8 - i4 * i6 >> view_dist - 5;
            int l14 = l10 >> 4;
            int j15 = j12 >> 4;
            int l15 = l13 >> 4;
            int j16 = anInt439 - anInt400;
            int l16 = anInt396;
            int j17 = anInt399 + anInt439 * l16;
            byte byte2 = 1;
            i10 += j11 * j16;
            l11 += l12 * j16;
            j13 += j14 * j16;
            if (interlace) {
                if ((anInt439 & 1) == 1) {
                    anInt439++;
                    i10 += j11;
                    l11 += l12;
                    j13 += j14;
                    j17 += l16;
                }
                j11 <<= 1;
                l12 <<= 1;
                j14 <<= 1;
                l16 <<= 1;
                byte2 = 2;
            }
            if (gameModel.unknown_scene_boolean) {
                for (i = anInt439; i < anInt440; i += byte2) {
                    Class8 class8_6 = aClass8Array438[i];
                    j = class8_6.anInt370 >> 8;
                    int i19 = class8_6.anInt371 >> 8;
                    int j21 = i19 - j;
                    if (j21 <= 0) {
                        i10 += j11;
                        l11 += l12;
                        j13 += j14;
                        j17 += l16;
                    } else {
                        int l22 = class8_6.anInt372;
                        int j24 = (class8_6.anInt373 - l22) / j21;
                        if (j < -anInt397) {
                            l22 += (-anInt397 - j) * j24;
                            j = -anInt397;
                            j21 = i19 - j;
                        }
                        if (i19 > anInt397) {
                            int j19 = anInt397;
                            j21 = j19 - j;
                        }
                        method287(anIntArray437, anIntArrayArray429[l], 0, 0, i10 + l14 * j, l11 + j15 * j, j13 + l15 * j, l10, j12, l13, j21, j17 + j, l22, j24);
                        i10 += j11;
                        l11 += l12;
                        j13 += j14;
                        j17 += l16;
                    }
                }

                return;
            }
            if (!aBooleanArray430[l]) {
                for (i = anInt439; i < anInt440; i += byte2) {
                    Class8 class8_7 = aClass8Array438[i];
                    j = class8_7.anInt370 >> 8;
                    int k19 = class8_7.anInt371 >> 8;
                    int k21 = k19 - j;
                    if (k21 <= 0) {
                        i10 += j11;
                        l11 += l12;
                        j13 += j14;
                        j17 += l16;
                    } else {
                        int i23 = class8_7.anInt372;
                        int k24 = (class8_7.anInt373 - i23) / k21;
                        if (j < -anInt397) {
                            i23 += (-anInt397 - j) * k24;
                            j = -anInt397;
                            k21 = k19 - j;
                        }
                        if (k19 > anInt397) {
                            int l19 = anInt397;
                            k21 = l19 - j;
                        }
                        method286(anIntArray437, anIntArrayArray429[l], 0, 0, i10 + l14 * j, l11 + j15 * j, j13 + l15 * j, l10, j12, l13, k21, j17 + j, i23, k24);
                        i10 += j11;
                        l11 += l12;
                        j13 += j14;
                        j17 += l16;
                    }
                }

                return;
            }
            for (i = anInt439; i < anInt440; i += byte2) {
                Class8 class8_8 = aClass8Array438[i];
                j = class8_8.anInt370 >> 8;
                int i20 = class8_8.anInt371 >> 8;
                int l21 = i20 - j;
                if (l21 <= 0) {
                    i10 += j11;
                    l11 += l12;
                    j13 += j14;
                    j17 += l16;
                } else {
                    int j23 = class8_8.anInt372;
                    int l24 = (class8_8.anInt373 - j23) / l21;
                    if (j < -anInt397) {
                        j23 += (-anInt397 - j) * l24;
                        j = -anInt397;
                        l21 = i20 - j;
                    }
                    if (i20 > anInt397) {
                        int j20 = anInt397;
                        l21 = j20 - j;
                    }
                    method288(anIntArray437, 0, 0, 0, anIntArrayArray429[l], i10 + l14 * j, l11 + j15 * j, j13 + l15 * j, l10, j12, l13, l21, j17 + j, j23, l24);
                    i10 += j11;
                    l11 += l12;
                    j13 += j14;
                    j17 += l16;
                }
            }

            return;
        }
        for (int j1 = 0; j1 < anInt374; j1++) {
            if (anIntArray375[j1] == l) {
                anIntArray377 = anIntArrayArray376[j1];
                break;
            }
            if (j1 == anInt374 - 1) {
                int l1 = (int) (Math.random() * (double) anInt374);
                anIntArray375[l1] = l;
                l = -1 - l;
                int k2 = (l >> 10 & 0x1f) * 8;
                int j3 = (l >> 5 & 0x1f) * 8;
                int l3 = (l & 0x1f) * 8;
                for (int j4 = 0; j4 < 256; j4++) {
                    int j6 = j4 * j4;
                    int k7 = (k2 * j6) / 0x10000;
                    int l8 = (j3 * j6) / 0x10000;
                    int j10 = (l3 * j6) / 0x10000;
                    anIntArrayArray376[l1][255 - j4] = (k7 << 16) + (l8 << 8) + j10;
                }

                anIntArray377 = anIntArrayArray376[l1];
            }
        }

        int i2 = anInt396;
        int l2 = anInt399 + anInt439 * i2;
        byte byte0 = 1;
        if (interlace) {
            if ((anInt439 & 1) == 1) {
                anInt439++;
                l2 += i2;
            }
            i2 <<= 1;
            byte0 = 2;
        }
        if (gameModel.transparent) {
            for (i = anInt439; i < anInt440; i += byte0) {
                Class8 class8 = aClass8Array438[i];
                j = class8.anInt370 >> 8;
                int k4 = class8.anInt371 >> 8;
                int k6 = k4 - j;
                if (k6 <= 0) {
                    l2 += i2;
                } else {
                    int l7 = class8.anInt372;
                    int i9 = (class8.anInt373 - l7) / k6;
                    if (j < -anInt397) {
                        l7 += (-anInt397 - j) * i9;
                        j = -anInt397;
                        k6 = k4 - j;
                    }
                    if (k4 > anInt397) {
                        int l4 = anInt397;
                        k6 = l4 - j;
                    }
                    method290(anIntArray437, -k6, l2 + j, 0, anIntArray377, l7, i9);
                    l2 += i2;
                }
            }

            return;
        }
        if (aBoolean386) {
            for (i = anInt439; i < anInt440; i += byte0) {
                Class8 class8_1 = aClass8Array438[i];
                j = class8_1.anInt370 >> 8;
                int i5 = class8_1.anInt371 >> 8;
                int l6 = i5 - j;
                if (l6 <= 0) {
                    l2 += i2;
                } else {
                    int i8 = class8_1.anInt372;
                    int j9 = (class8_1.anInt373 - i8) / l6;
                    if (j < -anInt397) {
                        i8 += (-anInt397 - j) * j9;
                        j = -anInt397;
                        l6 = i5 - j;
                    }
                    if (i5 > anInt397) {
                        int j5 = anInt397;
                        l6 = j5 - j;
                    }
                    method289(anIntArray437, -l6, l2 + j, 0, anIntArray377, i8, j9);
                    l2 += i2;
                }
            }

            return;
        }
        for (i = anInt439; i < anInt440; i += byte0) {
            Class8 class8_2 = aClass8Array438[i];
            j = class8_2.anInt370 >> 8;
            int k5 = class8_2.anInt371 >> 8;
            int i7 = k5 - j;
            if (i7 <= 0) {
                l2 += i2;
            } else {
                int j8 = class8_2.anInt372;
                int k9 = (class8_2.anInt373 - j8) / i7;
                if (j < -anInt397) {
                    j8 += (-anInt397 - j) * k9;
                    j = -anInt397;
                    i7 = k5 - j;
                }
                if (k5 > anInt397) {
                    int l5 = anInt397;
                    i7 = l5 - j;
                }
                method291(anIntArray437, -i7, l2 + j, 0, anIntArray377, j8, k9);
                l2 += i2;
            }
        }

    }

    private static void method283(int ai[], int ai1[], int i, int j, int k, int l, int i1, int j1,
                                  int k1, int l1, int i2, int j2, int k2, int l2) {
        if (i2 <= 0)
            return;
        int i3 = 0;
        int j3 = 0;
        int i4 = 0;
        if (i1 != 0) {
            i = k / i1 << 7;
            j = l / i1 << 7;
        }
        if (i < 0)
            i = 0;
        else if (i > 16256)
            i = 16256;
        k += j1;
        l += k1;
        i1 += l1;
        if (i1 != 0) {
            i3 = k / i1 << 7;
            j3 = l / i1 << 7;
        }
        if (i3 < 0)
            i3 = 0;
        else if (i3 > 16256)
            i3 = 16256;
        int k3 = i3 - i >> 4;
        int l3 = j3 - j >> 4;
        for (int j4 = i2 >> 4; j4 > 0; j4--) {
            i += k2 & 0x600000;
            i4 = k2 >> 23;
            k2 += l2;
            ai[j2++] = ai1[(j & 0x3f80) + (i >> 7)] >>> i4;
            i += k3;
            j += l3;
            ai[j2++] = ai1[(j & 0x3f80) + (i >> 7)] >>> i4;
            i += k3;
            j += l3;
            ai[j2++] = ai1[(j & 0x3f80) + (i >> 7)] >>> i4;
            i += k3;
            j += l3;
            ai[j2++] = ai1[(j & 0x3f80) + (i >> 7)] >>> i4;
            i += k3;
            j += l3;
            i = (i & 0x3fff) + (k2 & 0x600000);
            i4 = k2 >> 23;
            k2 += l2;
            ai[j2++] = ai1[(j & 0x3f80) + (i >> 7)] >>> i4;
            i += k3;
            j += l3;
            ai[j2++] = ai1[(j & 0x3f80) + (i >> 7)] >>> i4;
            i += k3;
            j += l3;
            ai[j2++] = ai1[(j & 0x3f80) + (i >> 7)] >>> i4;
            i += k3;
            j += l3;
            ai[j2++] = ai1[(j & 0x3f80) + (i >> 7)] >>> i4;
            i += k3;
            j += l3;
            i = (i & 0x3fff) + (k2 & 0x600000);
            i4 = k2 >> 23;
            k2 += l2;
            ai[j2++] = ai1[(j & 0x3f80) + (i >> 7)] >>> i4;
            i += k3;
            j += l3;
            ai[j2++] = ai1[(j & 0x3f80) + (i >> 7)] >>> i4;
            i += k3;
            j += l3;
            ai[j2++] = ai1[(j & 0x3f80) + (i >> 7)] >>> i4;
            i += k3;
            j += l3;
            ai[j2++] = ai1[(j & 0x3f80) + (i >> 7)] >>> i4;
            i += k3;
            j += l3;
            i = (i & 0x3fff) + (k2 & 0x600000);
            i4 = k2 >> 23;
            k2 += l2;
            ai[j2++] = ai1[(j & 0x3f80) + (i >> 7)] >>> i4;
            i += k3;
            j += l3;
            ai[j2++] = ai1[(j & 0x3f80) + (i >> 7)] >>> i4;
            i += k3;
            j += l3;
            ai[j2++] = ai1[(j & 0x3f80) + (i >> 7)] >>> i4;
            i += k3;
            j += l3;
            ai[j2++] = ai1[(j & 0x3f80) + (i >> 7)] >>> i4;
            i = i3;
            j = j3;
            k += j1;
            l += k1;
            i1 += l1;
            if (i1 != 0) {
                i3 = k / i1 << 7;
                j3 = l / i1 << 7;
            }
            if (i3 < 0)
                i3 = 0;
            else if (i3 > 16256)
                i3 = 16256;
            k3 = i3 - i >> 4;
            l3 = j3 - j >> 4;
        }

        for (int k4 = 0; k4 < (i2 & 0xf); k4++) {
            if ((k4 & 3) == 0) {
                i = (i & 0x3fff) + (k2 & 0x600000);
                i4 = k2 >> 23;
                k2 += l2;
            }
            ai[j2++] = ai1[(j & 0x3f80) + (i >> 7)] >>> i4;
            i += k3;
            j += l3;
        }

    }

    private static void method284(int ai[], int ai1[], int i, int j, int k, int l, int i1, int j1,
                                  int k1, int l1, int i2, int j2, int k2, int l2) {
        if (i2 <= 0)
            return;
        int i3 = 0;
        int j3 = 0;
        int i4 = 0;
        if (i1 != 0) {
            i = k / i1 << 7;
            j = l / i1 << 7;
        }
        if (i < 0)
            i = 0;
        else if (i > 16256)
            i = 16256;
        k += j1;
        l += k1;
        i1 += l1;
        if (i1 != 0) {
            i3 = k / i1 << 7;
            j3 = l / i1 << 7;
        }
        if (i3 < 0)
            i3 = 0;
        else if (i3 > 16256)
            i3 = 16256;
        int k3 = i3 - i >> 4;
        int l3 = j3 - j >> 4;
        for (int j4 = i2 >> 4; j4 > 0; j4--) {
            i += k2 & 0x600000;
            i4 = k2 >> 23;
            k2 += l2;
            ai[j2++] = (ai1[(j & 0x3f80) + (i >> 7)] >>> i4) + (ai[j2] >> 1 & 0x7f7f7f);
            i += k3;
            j += l3;
            ai[j2++] = (ai1[(j & 0x3f80) + (i >> 7)] >>> i4) + (ai[j2] >> 1 & 0x7f7f7f);
            i += k3;
            j += l3;
            ai[j2++] = (ai1[(j & 0x3f80) + (i >> 7)] >>> i4) + (ai[j2] >> 1 & 0x7f7f7f);
            i += k3;
            j += l3;
            ai[j2++] = (ai1[(j & 0x3f80) + (i >> 7)] >>> i4) + (ai[j2] >> 1 & 0x7f7f7f);
            i += k3;
            j += l3;
            i = (i & 0x3fff) + (k2 & 0x600000);
            i4 = k2 >> 23;
            k2 += l2;
            ai[j2++] = (ai1[(j & 0x3f80) + (i >> 7)] >>> i4) + (ai[j2] >> 1 & 0x7f7f7f);
            i += k3;
            j += l3;
            ai[j2++] = (ai1[(j & 0x3f80) + (i >> 7)] >>> i4) + (ai[j2] >> 1 & 0x7f7f7f);
            i += k3;
            j += l3;
            ai[j2++] = (ai1[(j & 0x3f80) + (i >> 7)] >>> i4) + (ai[j2] >> 1 & 0x7f7f7f);
            i += k3;
            j += l3;
            ai[j2++] = (ai1[(j & 0x3f80) + (i >> 7)] >>> i4) + (ai[j2] >> 1 & 0x7f7f7f);
            i += k3;
            j += l3;
            i = (i & 0x3fff) + (k2 & 0x600000);
            i4 = k2 >> 23;
            k2 += l2;
            ai[j2++] = (ai1[(j & 0x3f80) + (i >> 7)] >>> i4) + (ai[j2] >> 1 & 0x7f7f7f);
            i += k3;
            j += l3;
            ai[j2++] = (ai1[(j & 0x3f80) + (i >> 7)] >>> i4) + (ai[j2] >> 1 & 0x7f7f7f);
            i += k3;
            j += l3;
            ai[j2++] = (ai1[(j & 0x3f80) + (i >> 7)] >>> i4) + (ai[j2] >> 1 & 0x7f7f7f);
            i += k3;
            j += l3;
            ai[j2++] = (ai1[(j & 0x3f80) + (i >> 7)] >>> i4) + (ai[j2] >> 1 & 0x7f7f7f);
            i += k3;
            j += l3;
            i = (i & 0x3fff) + (k2 & 0x600000);
            i4 = k2 >> 23;
            k2 += l2;
            ai[j2++] = (ai1[(j & 0x3f80) + (i >> 7)] >>> i4) + (ai[j2] >> 1 & 0x7f7f7f);
            i += k3;
            j += l3;
            ai[j2++] = (ai1[(j & 0x3f80) + (i >> 7)] >>> i4) + (ai[j2] >> 1 & 0x7f7f7f);
            i += k3;
            j += l3;
            ai[j2++] = (ai1[(j & 0x3f80) + (i >> 7)] >>> i4) + (ai[j2] >> 1 & 0x7f7f7f);
            i += k3;
            j += l3;
            ai[j2++] = (ai1[(j & 0x3f80) + (i >> 7)] >>> i4) + (ai[j2] >> 1 & 0x7f7f7f);
            i = i3;
            j = j3;
            k += j1;
            l += k1;
            i1 += l1;
            if (i1 != 0) {
                i3 = k / i1 << 7;
                j3 = l / i1 << 7;
            }
            if (i3 < 0)
                i3 = 0;
            else if (i3 > 16256)
                i3 = 16256;
            k3 = i3 - i >> 4;
            l3 = j3 - j >> 4;
        }

        for (int k4 = 0; k4 < (i2 & 0xf); k4++) {
            if ((k4 & 3) == 0) {
                i = (i & 0x3fff) + (k2 & 0x600000);
                i4 = k2 >> 23;
                k2 += l2;
            }
            ai[j2++] = (ai1[(j & 0x3f80) + (i >> 7)] >>> i4) + (ai[j2] >> 1 & 0x7f7f7f);
            i += k3;
            j += l3;
        }

    }

    private static void method285(int ai[], int i, int j, int k, int ai1[], int l, int i1, int j1,
                                  int k1, int l1, int i2, int j2, int k2, int l2, int i3) {
        if (j2 <= 0)
            return;
        int j3 = 0;
        int k3 = 0;
        i3 <<= 2;
        if (j1 != 0) {
            j3 = l / j1 << 7;
            k3 = i1 / j1 << 7;
        }
        if (j3 < 0)
            j3 = 0;
        else if (j3 > 16256)
            j3 = 16256;
        for (int j4 = j2; j4 > 0; j4 -= 16) {
            l += k1;
            i1 += l1;
            j1 += i2;
            j = j3;
            k = k3;
            if (j1 != 0) {
                j3 = l / j1 << 7;
                k3 = i1 / j1 << 7;
            }
            if (j3 < 0)
                j3 = 0;
            else if (j3 > 16256)
                j3 = 16256;
            int l3 = j3 - j >> 4;
            int i4 = k3 - k >> 4;
            int k4 = l2 >> 23;
            j += l2 & 0x600000;
            l2 += i3;
            if (j4 < 16) {
                for (int l4 = 0; l4 < j4; l4++) {
                    if ((i = ai1[(k & 0x3f80) + (j >> 7)] >>> k4) != 0)
                        ai[k2] = i;
                    k2++;
                    j += l3;
                    k += i4;
                    if ((l4 & 3) == 3) {
                        j = (j & 0x3fff) + (l2 & 0x600000);
                        k4 = l2 >> 23;
                        l2 += i3;
                    }
                }

            } else {
                if ((i = ai1[(k & 0x3f80) + (j >> 7)] >>> k4) != 0)
                    ai[k2] = i;
                k2++;
                j += l3;
                k += i4;
                if ((i = ai1[(k & 0x3f80) + (j >> 7)] >>> k4) != 0)
                    ai[k2] = i;
                k2++;
                j += l3;
                k += i4;
                if ((i = ai1[(k & 0x3f80) + (j >> 7)] >>> k4) != 0)
                    ai[k2] = i;
                k2++;
                j += l3;
                k += i4;
                if ((i = ai1[(k & 0x3f80) + (j >> 7)] >>> k4) != 0)
                    ai[k2] = i;
                k2++;
                j += l3;
                k += i4;
                j = (j & 0x3fff) + (l2 & 0x600000);
                k4 = l2 >> 23;
                l2 += i3;
                if ((i = ai1[(k & 0x3f80) + (j >> 7)] >>> k4) != 0)
                    ai[k2] = i;
                k2++;
                j += l3;
                k += i4;
                if ((i = ai1[(k & 0x3f80) + (j >> 7)] >>> k4) != 0)
                    ai[k2] = i;
                k2++;
                j += l3;
                k += i4;
                if ((i = ai1[(k & 0x3f80) + (j >> 7)] >>> k4) != 0)
                    ai[k2] = i;
                k2++;
                j += l3;
                k += i4;
                if ((i = ai1[(k & 0x3f80) + (j >> 7)] >>> k4) != 0)
                    ai[k2] = i;
                k2++;
                j += l3;
                k += i4;
                j = (j & 0x3fff) + (l2 & 0x600000);
                k4 = l2 >> 23;
                l2 += i3;
                if ((i = ai1[(k & 0x3f80) + (j >> 7)] >>> k4) != 0)
                    ai[k2] = i;
                k2++;
                j += l3;
                k += i4;
                if ((i = ai1[(k & 0x3f80) + (j >> 7)] >>> k4) != 0)
                    ai[k2] = i;
                k2++;
                j += l3;
                k += i4;
                if ((i = ai1[(k & 0x3f80) + (j >> 7)] >>> k4) != 0)
                    ai[k2] = i;
                k2++;
                j += l3;
                k += i4;
                if ((i = ai1[(k & 0x3f80) + (j >> 7)] >>> k4) != 0)
                    ai[k2] = i;
                k2++;
                j += l3;
                k += i4;
                j = (j & 0x3fff) + (l2 & 0x600000);
                k4 = l2 >> 23;
                l2 += i3;
                if ((i = ai1[(k & 0x3f80) + (j >> 7)] >>> k4) != 0)
                    ai[k2] = i;
                k2++;
                j += l3;
                k += i4;
                if ((i = ai1[(k & 0x3f80) + (j >> 7)] >>> k4) != 0)
                    ai[k2] = i;
                k2++;
                j += l3;
                k += i4;
                if ((i = ai1[(k & 0x3f80) + (j >> 7)] >>> k4) != 0)
                    ai[k2] = i;
                k2++;
                j += l3;
                k += i4;
                if ((i = ai1[(k & 0x3f80) + (j >> 7)] >>> k4) != 0)
                    ai[k2] = i;
                k2++;
            }
        }

    }

    private static void method286(int ai[], int ai1[], int i, int j, int k, int l, int i1, int j1,
                                  int k1, int l1, int i2, int j2, int k2, int l2) {
        if (i2 <= 0)
            return;
        int i3 = 0;
        int j3 = 0;
        l2 <<= 2;
        if (i1 != 0) {
            i3 = k / i1 << 6;
            j3 = l / i1 << 6;
        }
        if (i3 < 0)
            i3 = 0;
        else if (i3 > 4032)
            i3 = 4032;
        for (int i4 = i2; i4 > 0; i4 -= 16) {
            k += j1;
            l += k1;
            i1 += l1;
            i = i3;
            j = j3;
            if (i1 != 0) {
                i3 = k / i1 << 6;
                j3 = l / i1 << 6;
            }
            if (i3 < 0)
                i3 = 0;
            else if (i3 > 4032)
                i3 = 4032;
            int k3 = i3 - i >> 4;
            int l3 = j3 - j >> 4;
            int j4 = k2 >> 20;
            i += k2 & 0xc0000;
            k2 += l2;
            if (i4 < 16) {
                for (int k4 = 0; k4 < i4; k4++) {
                    ai[j2++] = ai1[(j & 0xfc0) + (i >> 6)] >>> j4;
                    i += k3;
                    j += l3;
                    if ((k4 & 3) == 3) {
                        i = (i & 0xfff) + (k2 & 0xc0000);
                        j4 = k2 >> 20;
                        k2 += l2;
                    }
                }

            } else {
                ai[j2++] = ai1[(j & 0xfc0) + (i >> 6)] >>> j4;
                i += k3;
                j += l3;
                ai[j2++] = ai1[(j & 0xfc0) + (i >> 6)] >>> j4;
                i += k3;
                j += l3;
                ai[j2++] = ai1[(j & 0xfc0) + (i >> 6)] >>> j4;
                i += k3;
                j += l3;
                ai[j2++] = ai1[(j & 0xfc0) + (i >> 6)] >>> j4;
                i += k3;
                j += l3;
                i = (i & 0xfff) + (k2 & 0xc0000);
                j4 = k2 >> 20;
                k2 += l2;
                ai[j2++] = ai1[(j & 0xfc0) + (i >> 6)] >>> j4;
                i += k3;
                j += l3;
                ai[j2++] = ai1[(j & 0xfc0) + (i >> 6)] >>> j4;
                i += k3;
                j += l3;
                ai[j2++] = ai1[(j & 0xfc0) + (i >> 6)] >>> j4;
                i += k3;
                j += l3;
                ai[j2++] = ai1[(j & 0xfc0) + (i >> 6)] >>> j4;
                i += k3;
                j += l3;
                i = (i & 0xfff) + (k2 & 0xc0000);
                j4 = k2 >> 20;
                k2 += l2;
                ai[j2++] = ai1[(j & 0xfc0) + (i >> 6)] >>> j4;
                i += k3;
                j += l3;
                ai[j2++] = ai1[(j & 0xfc0) + (i >> 6)] >>> j4;
                i += k3;
                j += l3;
                ai[j2++] = ai1[(j & 0xfc0) + (i >> 6)] >>> j4;
                i += k3;
                j += l3;
                ai[j2++] = ai1[(j & 0xfc0) + (i >> 6)] >>> j4;
                i += k3;
                j += l3;
                i = (i & 0xfff) + (k2 & 0xc0000);
                j4 = k2 >> 20;
                k2 += l2;
                ai[j2++] = ai1[(j & 0xfc0) + (i >> 6)] >>> j4;
                i += k3;
                j += l3;
                ai[j2++] = ai1[(j & 0xfc0) + (i >> 6)] >>> j4;
                i += k3;
                j += l3;
                ai[j2++] = ai1[(j & 0xfc0) + (i >> 6)] >>> j4;
                i += k3;
                j += l3;
                ai[j2++] = ai1[(j & 0xfc0) + (i >> 6)] >>> j4;
            }
        }

    }

    private static void method287(int ai[], int ai1[], int i, int j, int k, int l, int i1, int j1,
                                  int k1, int l1, int i2, int j2, int k2, int l2) {
        if (i2 <= 0)
            return;
        int i3 = 0;
        int j3 = 0;
        l2 <<= 2;
        if (i1 != 0) {
            i3 = k / i1 << 6;
            j3 = l / i1 << 6;
        }
        if (i3 < 0)
            i3 = 0;
        else if (i3 > 4032)
            i3 = 4032;
        for (int i4 = i2; i4 > 0; i4 -= 16) {
            k += j1;
            l += k1;
            i1 += l1;
            i = i3;
            j = j3;
            if (i1 != 0) {
                i3 = k / i1 << 6;
                j3 = l / i1 << 6;
            }
            if (i3 < 0)
                i3 = 0;
            else if (i3 > 4032)
                i3 = 4032;
            int k3 = i3 - i >> 4;
            int l3 = j3 - j >> 4;
            int j4 = k2 >> 20;
            i += k2 & 0xc0000;
            k2 += l2;
            if (i4 < 16) {
                for (int k4 = 0; k4 < i4; k4++) {
                    ai[j2++] = (ai1[(j & 0xfc0) + (i >> 6)] >>> j4) + (ai[j2] >> 1 & 0x7f7f7f);
                    i += k3;
                    j += l3;
                    if ((k4 & 3) == 3) {
                        i = (i & 0xfff) + (k2 & 0xc0000);
                        j4 = k2 >> 20;
                        k2 += l2;
                    }
                }

            } else {
                ai[j2++] = (ai1[(j & 0xfc0) + (i >> 6)] >>> j4) + (ai[j2] >> 1 & 0x7f7f7f);
                i += k3;
                j += l3;
                ai[j2++] = (ai1[(j & 0xfc0) + (i >> 6)] >>> j4) + (ai[j2] >> 1 & 0x7f7f7f);
                i += k3;
                j += l3;
                ai[j2++] = (ai1[(j & 0xfc0) + (i >> 6)] >>> j4) + (ai[j2] >> 1 & 0x7f7f7f);
                i += k3;
                j += l3;
                ai[j2++] = (ai1[(j & 0xfc0) + (i >> 6)] >>> j4) + (ai[j2] >> 1 & 0x7f7f7f);
                i += k3;
                j += l3;
                i = (i & 0xfff) + (k2 & 0xc0000);
                j4 = k2 >> 20;
                k2 += l2;
                ai[j2++] = (ai1[(j & 0xfc0) + (i >> 6)] >>> j4) + (ai[j2] >> 1 & 0x7f7f7f);
                i += k3;
                j += l3;
                ai[j2++] = (ai1[(j & 0xfc0) + (i >> 6)] >>> j4) + (ai[j2] >> 1 & 0x7f7f7f);
                i += k3;
                j += l3;
                ai[j2++] = (ai1[(j & 0xfc0) + (i >> 6)] >>> j4) + (ai[j2] >> 1 & 0x7f7f7f);
                i += k3;
                j += l3;
                ai[j2++] = (ai1[(j & 0xfc0) + (i >> 6)] >>> j4) + (ai[j2] >> 1 & 0x7f7f7f);
                i += k3;
                j += l3;
                i = (i & 0xfff) + (k2 & 0xc0000);
                j4 = k2 >> 20;
                k2 += l2;
                ai[j2++] = (ai1[(j & 0xfc0) + (i >> 6)] >>> j4) + (ai[j2] >> 1 & 0x7f7f7f);
                i += k3;
                j += l3;
                ai[j2++] = (ai1[(j & 0xfc0) + (i >> 6)] >>> j4) + (ai[j2] >> 1 & 0x7f7f7f);
                i += k3;
                j += l3;
                ai[j2++] = (ai1[(j & 0xfc0) + (i >> 6)] >>> j4) + (ai[j2] >> 1 & 0x7f7f7f);
                i += k3;
                j += l3;
                ai[j2++] = (ai1[(j & 0xfc0) + (i >> 6)] >>> j4) + (ai[j2] >> 1 & 0x7f7f7f);
                i += k3;
                j += l3;
                i = (i & 0xfff) + (k2 & 0xc0000);
                j4 = k2 >> 20;
                k2 += l2;
                ai[j2++] = (ai1[(j & 0xfc0) + (i >> 6)] >>> j4) + (ai[j2] >> 1 & 0x7f7f7f);
                i += k3;
                j += l3;
                ai[j2++] = (ai1[(j & 0xfc0) + (i >> 6)] >>> j4) + (ai[j2] >> 1 & 0x7f7f7f);
                i += k3;
                j += l3;
                ai[j2++] = (ai1[(j & 0xfc0) + (i >> 6)] >>> j4) + (ai[j2] >> 1 & 0x7f7f7f);
                i += k3;
                j += l3;
                ai[j2++] = (ai1[(j & 0xfc0) + (i >> 6)] >>> j4) + (ai[j2] >> 1 & 0x7f7f7f);
            }
        }

    }

    private static void method288(int ai[], int i, int j, int k, int ai1[], int l, int i1, int j1,
                                  int k1, int l1, int i2, int j2, int k2, int l2, int i3) {
        if (j2 <= 0)
            return;
        int j3 = 0;
        int k3 = 0;
        i3 <<= 2;
        if (j1 != 0) {
            j3 = l / j1 << 6;
            k3 = i1 / j1 << 6;
        }
        if (j3 < 0)
            j3 = 0;
        else if (j3 > 4032)
            j3 = 4032;
        for (int j4 = j2; j4 > 0; j4 -= 16) {
            l += k1;
            i1 += l1;
            j1 += i2;
            j = j3;
            k = k3;
            if (j1 != 0) {
                j3 = l / j1 << 6;
                k3 = i1 / j1 << 6;
            }
            if (j3 < 0)
                j3 = 0;
            else if (j3 > 4032)
                j3 = 4032;
            int l3 = j3 - j >> 4;
            int i4 = k3 - k >> 4;
            int k4 = l2 >> 20;
            j += l2 & 0xc0000;
            l2 += i3;
            if (j4 < 16) {
                for (int l4 = 0; l4 < j4; l4++) {
                    if ((i = ai1[(k & 0xfc0) + (j >> 6)] >>> k4) != 0)
                        ai[k2] = i;
                    k2++;
                    j += l3;
                    k += i4;
                    if ((l4 & 3) == 3) {
                        j = (j & 0xfff) + (l2 & 0xc0000);
                        k4 = l2 >> 20;
                        l2 += i3;
                    }
                }

            } else {
                if ((i = ai1[(k & 0xfc0) + (j >> 6)] >>> k4) != 0)
                    ai[k2] = i;
                k2++;
                j += l3;
                k += i4;
                if ((i = ai1[(k & 0xfc0) + (j >> 6)] >>> k4) != 0)
                    ai[k2] = i;
                k2++;
                j += l3;
                k += i4;
                if ((i = ai1[(k & 0xfc0) + (j >> 6)] >>> k4) != 0)
                    ai[k2] = i;
                k2++;
                j += l3;
                k += i4;
                if ((i = ai1[(k & 0xfc0) + (j >> 6)] >>> k4) != 0)
                    ai[k2] = i;
                k2++;
                j += l3;
                k += i4;
                j = (j & 0xfff) + (l2 & 0xc0000);
                k4 = l2 >> 20;
                l2 += i3;
                if ((i = ai1[(k & 0xfc0) + (j >> 6)] >>> k4) != 0)
                    ai[k2] = i;
                k2++;
                j += l3;
                k += i4;
                if ((i = ai1[(k & 0xfc0) + (j >> 6)] >>> k4) != 0)
                    ai[k2] = i;
                k2++;
                j += l3;
                k += i4;
                if ((i = ai1[(k & 0xfc0) + (j >> 6)] >>> k4) != 0)
                    ai[k2] = i;
                k2++;
                j += l3;
                k += i4;
                if ((i = ai1[(k & 0xfc0) + (j >> 6)] >>> k4) != 0)
                    ai[k2] = i;
                k2++;
                j += l3;
                k += i4;
                j = (j & 0xfff) + (l2 & 0xc0000);
                k4 = l2 >> 20;
                l2 += i3;
                if ((i = ai1[(k & 0xfc0) + (j >> 6)] >>> k4) != 0)
                    ai[k2] = i;
                k2++;
                j += l3;
                k += i4;
                if ((i = ai1[(k & 0xfc0) + (j >> 6)] >>> k4) != 0)
                    ai[k2] = i;
                k2++;
                j += l3;
                k += i4;
                if ((i = ai1[(k & 0xfc0) + (j >> 6)] >>> k4) != 0)
                    ai[k2] = i;
                k2++;
                j += l3;
                k += i4;
                if ((i = ai1[(k & 0xfc0) + (j >> 6)] >>> k4) != 0)
                    ai[k2] = i;
                k2++;
                j += l3;
                k += i4;
                j = (j & 0xfff) + (l2 & 0xc0000);
                k4 = l2 >> 20;
                l2 += i3;
                if ((i = ai1[(k & 0xfc0) + (j >> 6)] >>> k4) != 0)
                    ai[k2] = i;
                k2++;
                j += l3;
                k += i4;
                if ((i = ai1[(k & 0xfc0) + (j >> 6)] >>> k4) != 0)
                    ai[k2] = i;
                k2++;
                j += l3;
                k += i4;
                if ((i = ai1[(k & 0xfc0) + (j >> 6)] >>> k4) != 0)
                    ai[k2] = i;
                k2++;
                j += l3;
                k += i4;
                if ((i = ai1[(k & 0xfc0) + (j >> 6)] >>> k4) != 0)
                    ai[k2] = i;
                k2++;
            }
        }

    }

    private static void method289(int ai[], int i, int j, int k, int ai1[], int l, int i1) {
        if (i >= 0)
            return;
        i1 <<= 1;
        k = ai1[l >> 8 & 0xff];
        l += i1;
        int j1 = i / 8;
        for (int k1 = j1; k1 < 0; k1++) {
            ai[j++] = k;
            ai[j++] = k;
            k = ai1[l >> 8 & 0xff];
            l += i1;
            ai[j++] = k;
            ai[j++] = k;
            k = ai1[l >> 8 & 0xff];
            l += i1;
            ai[j++] = k;
            ai[j++] = k;
            k = ai1[l >> 8 & 0xff];
            l += i1;
            ai[j++] = k;
            ai[j++] = k;
            k = ai1[l >> 8 & 0xff];
            l += i1;
        }

        j1 = -(i % 8);
        for (int l1 = 0; l1 < j1; l1++) {
            ai[j++] = k;
            if ((l1 & 1) == 1) {
                k = ai1[l >> 8 & 0xff];
                l += i1;
            }
        }

    }

    private static void method290(int ai[], int i, int j, int k, int ai1[], int l, int i1) {
        if (i >= 0)
            return;
        i1 <<= 2;
        k = ai1[l >> 8 & 0xff];
        l += i1;
        int j1 = i / 16;
        for (int k1 = j1; k1 < 0; k1++) {
            ai[j++] = k + (ai[j] >> 1 & 0x7f7f7f);
            ai[j++] = k + (ai[j] >> 1 & 0x7f7f7f);
            ai[j++] = k + (ai[j] >> 1 & 0x7f7f7f);
            ai[j++] = k + (ai[j] >> 1 & 0x7f7f7f);
            k = ai1[l >> 8 & 0xff];
            l += i1;
            ai[j++] = k + (ai[j] >> 1 & 0x7f7f7f);
            ai[j++] = k + (ai[j] >> 1 & 0x7f7f7f);
            ai[j++] = k + (ai[j] >> 1 & 0x7f7f7f);
            ai[j++] = k + (ai[j] >> 1 & 0x7f7f7f);
            k = ai1[l >> 8 & 0xff];
            l += i1;
            ai[j++] = k + (ai[j] >> 1 & 0x7f7f7f);
            ai[j++] = k + (ai[j] >> 1 & 0x7f7f7f);
            ai[j++] = k + (ai[j] >> 1 & 0x7f7f7f);
            ai[j++] = k + (ai[j] >> 1 & 0x7f7f7f);
            k = ai1[l >> 8 & 0xff];
            l += i1;
            ai[j++] = k + (ai[j] >> 1 & 0x7f7f7f);
            ai[j++] = k + (ai[j] >> 1 & 0x7f7f7f);
            ai[j++] = k + (ai[j] >> 1 & 0x7f7f7f);
            ai[j++] = k + (ai[j] >> 1 & 0x7f7f7f);
            k = ai1[l >> 8 & 0xff];
            l += i1;
        }

        j1 = -(i % 16);
        for (int l1 = 0; l1 < j1; l1++) {
            ai[j++] = k + (ai[j] >> 1 & 0x7f7f7f);
            if ((l1 & 3) == 3) {
                k = ai1[l >> 8 & 0xff];
                l += i1;
                l += i1;
            }
        }

    }

    private static void method291(int ai[], int i, int j, int k, int ai1[], int l, int i1) {
        if (i >= 0)
            return;
        i1 <<= 2;
        k = ai1[l >> 8 & 0xff];
        l += i1;
        int j1 = i / 16;
        for (int k1 = j1; k1 < 0; k1++) {
            ai[j++] = k;
            ai[j++] = k;
            ai[j++] = k;
            ai[j++] = k;
            k = ai1[l >> 8 & 0xff];
            l += i1;
            ai[j++] = k;
            ai[j++] = k;
            ai[j++] = k;
            ai[j++] = k;
            k = ai1[l >> 8 & 0xff];
            l += i1;
            ai[j++] = k;
            ai[j++] = k;
            ai[j++] = k;
            ai[j++] = k;
            k = ai1[l >> 8 & 0xff];
            l += i1;
            ai[j++] = k;
            ai[j++] = k;
            ai[j++] = k;
            ai[j++] = k;
            k = ai1[l >> 8 & 0xff];
            l += i1;
        }

        j1 = -(i % 16);
        for (int l1 = 0; l1 < j1; l1++) {
            ai[j++] = k;
            if ((l1 & 3) == 3) {
                k = ai1[l >> 8 & 0xff];
                l += i1;
            }
        }

    }

    public void set_camera(int x, int z, int y, int pitch, int yaw, int roll, int distance) {
        pitch &= 0x3ff;
        yaw &= 0x3ff;
        roll &= 0x3ff;
        camera_pitch = 1024 - pitch & 0x3ff;
        camera_yaw = 1024 - yaw & 0x3ff;
        camera_roll = 1024 - roll & 0x3ff;
        int l1 = 0;
        int i2 = 0;
        int j2 = distance;
        if (pitch != 0) {
            int k2 = anIntArray384[pitch];
            int j3 = anIntArray384[pitch + 1024];
            int i4 = i2 * j3 - j2 * k2 >> 15;
            j2 = i2 * k2 + j2 * j3 >> 15;
            i2 = i4;
        }
        if (yaw != 0) {
            int l2 = anIntArray384[yaw];
            int k3 = anIntArray384[yaw + 1024];
            int j4 = j2 * l2 + l1 * k3 >> 15;
            j2 = j2 * k3 - l1 * l2 >> 15;
            l1 = j4;
        }
        if (roll != 0) {
            int i3 = anIntArray384[roll];
            int l3 = anIntArray384[roll + 1024];
            int k4 = i2 * i3 + l1 * l3 >> 15;
            i2 = i2 * l3 - l1 * i3 >> 15;
            l1 = k4;
        }
        camera_x = x - l1;
        camera_y = z - i2;
        camera_z = y - j2;
    }

    private void method293(int i) {
        Polygon polygon = aPolygonArray414[i];
        GameModel gameModel = polygon.model;
        int j = polygon.anInt360;
        int ai[] = gameModel.face_vertices[j];
        int k = gameModel.face_num_vertices[j];
        int l = gameModel.face_camera_normal_scale[j];
        int j1 = gameModel.vertex_camera_x[ai[0]];
        int k1 = gameModel.vertex_camera_y[ai[0]];
        int l1 = gameModel.vertex_camera_z[ai[0]];
        int i2 = gameModel.vertex_camera_x[ai[1]] - j1;
        int j2 = gameModel.vertex_camera_y[ai[1]] - k1;
        int k2 = gameModel.vertex_camera_z[ai[1]] - l1;
        int l2 = gameModel.vertex_camera_x[ai[2]] - j1;
        int i3 = gameModel.vertex_camera_y[ai[2]] - k1;
        int j3 = gameModel.vertex_camera_z[ai[2]] - l1;
        int k3 = j2 * j3 - i3 * k2;
        int l3 = k2 * l2 - j3 * i2;
        int i4 = i2 * i3 - l2 * j2;
        if (l == -1) {
            l = 0;
            for (; k3 > 25000 || l3 > 25000 || i4 > 25000 || k3 < -25000 || l3 < -25000 || i4 < -25000; i4 >>= 1) {
                l++;
                k3 >>= 1;
                l3 >>= 1;
            }

            gameModel.face_camera_normal_scale[j] = l;
            gameModel.face_camera_normal_magnitude[j] = (int) ((double) anInt402 * Math.sqrt(k3 * k3 + l3 * l3 + i4 * i4));
        } else {
            k3 >>= l;
            l3 >>= l;
            i4 >>= l;
        }
        polygon.anInt365 = j1 * k3 + k1 * l3 + l1 * i4;
        polygon.anInt362 = k3;
        polygon.anInt363 = l3;
        polygon.anInt364 = i4;
        int j4 = gameModel.vertex_camera_z[ai[0]];
        int k4 = j4;
        int l4 = gameModel.vertex_view_x[ai[0]];
        int i5 = l4;
        int j5 = gameModel.vertex_view_y[ai[0]];
        int k5 = j5;
        for (int l5 = 1; l5 < k; l5++) {
            int i1 = gameModel.vertex_camera_z[ai[l5]];
            if (i1 > k4)
                k4 = i1;
            else if (i1 < j4)
                j4 = i1;
            i1 = gameModel.vertex_view_x[ai[l5]];
            if (i1 > i5)
                i5 = i1;
            else if (i1 < l4)
                l4 = i1;
            i1 = gameModel.vertex_view_y[ai[l5]];
            if (i1 > k5)
                k5 = i1;
            else if (i1 < j5)
                j5 = i1;
        }

        polygon.anInt357 = j4;
        polygon.anInt358 = k4;
        polygon.anInt353 = l4;
        polygon.anInt355 = i5;
        polygon.anInt354 = j5;
        polygon.anInt356 = k5;
    }

    private void method294(int i) {
        Polygon polygon = aPolygonArray414[i];
        GameModel gameModel = polygon.model;
        int j = polygon.anInt360;
        int ai[] = gameModel.face_vertices[j];
        int l = 0;
        int i1 = 0;
        int j1 = 1;
        int k1 = gameModel.vertex_camera_x[ai[0]];
        int l1 = gameModel.vertex_camera_y[ai[0]];
        int i2 = gameModel.vertex_camera_z[ai[0]];
        gameModel.face_camera_normal_magnitude[j] = 1;
        gameModel.face_camera_normal_scale[j] = 0;
        polygon.anInt365 = k1 * l + l1 * i1 + i2 * j1;
        polygon.anInt362 = l;
        polygon.anInt363 = i1;
        polygon.anInt364 = j1;
        int j2 = gameModel.vertex_camera_z[ai[0]];
        int k2 = j2;
        int l2 = gameModel.vertex_view_x[ai[0]];
        int i3 = l2;
        if (gameModel.vertex_view_x[ai[1]] < l2)
            l2 = gameModel.vertex_view_x[ai[1]];
        else
            i3 = gameModel.vertex_view_x[ai[1]];
        int j3 = gameModel.vertex_view_y[ai[1]];
        int k3 = gameModel.vertex_view_y[ai[0]];
        int k = gameModel.vertex_camera_z[ai[1]];
        if (k > k2)
            k2 = k;
        else if (k < j2)
            j2 = k;
        k = gameModel.vertex_view_x[ai[1]];
        if (k > i3)
            i3 = k;
        else if (k < l2)
            l2 = k;
        k = gameModel.vertex_view_y[ai[1]];
        if (k > k3)
            k3 = k;
        else if (k < j3)
            j3 = k;
        polygon.anInt357 = j2;
        polygon.anInt358 = k2;
        polygon.anInt353 = l2 - 20;
        polygon.anInt355 = i3 + 20;
        polygon.anInt354 = j3;
        polygon.anInt356 = k3;
    }

    private boolean method295(Polygon polygon, Polygon polygon_1) {
        if (polygon.anInt353 >= polygon_1.anInt355)
            return true;
        if (polygon_1.anInt353 >= polygon.anInt355)
            return true;
        if (polygon.anInt354 >= polygon_1.anInt356)
            return true;
        if (polygon_1.anInt354 >= polygon.anInt356)
            return true;
        if (polygon.anInt357 >= polygon_1.anInt358)
            return true;
        if (polygon_1.anInt357 > polygon.anInt358)
            return false;
        GameModel gameModel = polygon.model;
        GameModel gameModel_1 = polygon_1.model;
        int i = polygon.anInt360;
        int j = polygon_1.anInt360;
        int ai[] = gameModel.face_vertices[i];
        int ai1[] = gameModel_1.face_vertices[j];
        int k = gameModel.face_num_vertices[i];
        int l = gameModel_1.face_num_vertices[j];
        int k2 = gameModel_1.vertex_camera_x[ai1[0]];
        int l2 = gameModel_1.vertex_camera_y[ai1[0]];
        int i3 = gameModel_1.vertex_camera_z[ai1[0]];
        int j3 = polygon_1.anInt362;
        int k3 = polygon_1.anInt363;
        int l3 = polygon_1.anInt364;
        int i4 = gameModel_1.face_camera_normal_magnitude[j];
        int j4 = polygon_1.anInt365;
        boolean flag = false;
        for (int k4 = 0; k4 < k; k4++) {
            int i1 = ai[k4];
            int i2 = (k2 - gameModel.vertex_camera_x[i1]) * j3 + (l2 - gameModel.vertex_camera_y[i1]) * k3 + (i3 - gameModel.vertex_camera_z[i1]) * l3;
            if ((i2 >= -i4 || j4 >= 0) && (i2 <= i4 || j4 <= 0))
                continue;
            flag = true;
            break;
        }

        if (!flag)
            return true;
        k2 = gameModel.vertex_camera_x[ai[0]];
        l2 = gameModel.vertex_camera_y[ai[0]];
        i3 = gameModel.vertex_camera_z[ai[0]];
        j3 = polygon.anInt362;
        k3 = polygon.anInt363;
        l3 = polygon.anInt364;
        i4 = gameModel.face_camera_normal_magnitude[i];
        j4 = polygon.anInt365;
        flag = false;
        for (int l4 = 0; l4 < l; l4++) {
            int j1 = ai1[l4];
            int j2 = (k2 - gameModel_1.vertex_camera_x[j1]) * j3 + (l2 - gameModel_1.vertex_camera_y[j1]) * k3 + (i3 - gameModel_1.vertex_camera_z[j1]) * l3;
            if ((j2 >= -i4 || j4 <= 0) && (j2 <= i4 || j4 >= 0))
                continue;
            flag = true;
            break;
        }

        if (!flag)
            return true;
        int ai2[];
        int ai3[];
        if (k == 2) {
            ai2 = new int[4];
            ai3 = new int[4];
            int i5 = ai[0];
            int k1 = ai[1];
            ai2[0] = gameModel.vertex_view_x[i5] - 20;
            ai2[1] = gameModel.vertex_view_x[k1] - 20;
            ai2[2] = gameModel.vertex_view_x[k1] + 20;
            ai2[3] = gameModel.vertex_view_x[i5] + 20;
            ai3[0] = ai3[3] = gameModel.vertex_view_y[i5];
            ai3[1] = ai3[2] = gameModel.vertex_view_y[k1];
        } else {
            ai2 = new int[k];
            ai3 = new int[k];
            for (int j5 = 0; j5 < k; j5++) {
                int i6 = ai[j5];
                ai2[j5] = gameModel.vertex_view_x[i6];
                ai3[j5] = gameModel.vertex_view_y[i6];
            }

        }
        int ai4[];
        int ai5[];
        if (l == 2) {
            ai4 = new int[4];
            ai5 = new int[4];
            int k5 = ai1[0];
            int l1 = ai1[1];
            ai4[0] = gameModel_1.vertex_view_x[k5] - 20;
            ai4[1] = gameModel_1.vertex_view_x[l1] - 20;
            ai4[2] = gameModel_1.vertex_view_x[l1] + 20;
            ai4[3] = gameModel_1.vertex_view_x[k5] + 20;
            ai5[0] = ai5[3] = gameModel_1.vertex_view_y[k5];
            ai5[1] = ai5[2] = gameModel_1.vertex_view_y[l1];
        } else {
            ai4 = new int[l];
            ai5 = new int[l];
            for (int l5 = 0; l5 < l; l5++) {
                int j6 = ai1[l5];
                ai4[l5] = gameModel_1.vertex_view_x[j6];
                ai5[l5] = gameModel_1.vertex_view_y[j6];
            }

        }
        return !method309(ai2, ai3, ai4, ai5);
    }

    private boolean method296(Polygon polygon, Polygon polygon_1) {
        GameModel gameModel = polygon.model;
        GameModel gameModel_1 = polygon_1.model;
        int i = polygon.anInt360;
        int j = polygon_1.anInt360;
        int ai[] = gameModel.face_vertices[i];
        int ai1[] = gameModel_1.face_vertices[j];
        int k = gameModel.face_num_vertices[i];
        int l = gameModel_1.face_num_vertices[j];
        int i2 = gameModel_1.vertex_camera_x[ai1[0]];
        int j2 = gameModel_1.vertex_camera_y[ai1[0]];
        int k2 = gameModel_1.vertex_camera_z[ai1[0]];
        int l2 = polygon_1.anInt362;
        int i3 = polygon_1.anInt363;
        int j3 = polygon_1.anInt364;
        int k3 = gameModel_1.face_camera_normal_magnitude[j];
        int l3 = polygon_1.anInt365;
        boolean flag = false;
        for (int i4 = 0; i4 < k; i4++) {
            int i1 = ai[i4];
            int k1 = (i2 - gameModel.vertex_camera_x[i1]) * l2 + (j2 - gameModel.vertex_camera_y[i1]) * i3 + (k2 - gameModel.vertex_camera_z[i1]) * j3;
            if ((k1 >= -k3 || l3 >= 0) && (k1 <= k3 || l3 <= 0))
                continue;
            flag = true;
            break;
        }

        if (!flag)
            return true;
        i2 = gameModel.vertex_camera_x[ai[0]];
        j2 = gameModel.vertex_camera_y[ai[0]];
        k2 = gameModel.vertex_camera_z[ai[0]];
        l2 = polygon.anInt362;
        i3 = polygon.anInt363;
        j3 = polygon.anInt364;
        k3 = gameModel.face_camera_normal_magnitude[i];
        l3 = polygon.anInt365;
        flag = false;
        for (int j4 = 0; j4 < l; j4++) {
            int j1 = ai1[j4];
            int l1 = (i2 - gameModel_1.vertex_camera_x[j1]) * l2 + (j2 - gameModel_1.vertex_camera_y[j1]) * i3 + (k2 - gameModel_1.vertex_camera_z[j1]) * j3;
            if ((l1 >= -k3 || l3 <= 0) && (l1 <= k3 || l3 >= 0))
                continue;
            flag = true;
            break;
        }

        return !flag;
    }

    public void initialise_textures(int count, int something7, int something11) {
        texture_count = count;
        aByteArrayArray425 = new byte[count][];
        anIntArrayArray426 = new int[count][];
        anIntArray427 = new int[count];
        aLongArray428 = new long[count];
        aBooleanArray430 = new boolean[count];
        anIntArrayArray429 = new int[count][];
        aLong431 = 0L;
        anIntArrayArray432 = new int[something7][];
        anIntArrayArray433 = new int[something11][];
    }

    public void load_texture(int i, byte abyte0[], int ai[], int j) {
        aByteArrayArray425[i] = abyte0;
        anIntArrayArray426[i] = ai;
        anIntArray427[i] = j;
        aLongArray428[i] = 0L;
        aBooleanArray430[i] = false;
        anIntArrayArray429[i] = null;
        method299(i);
    }

    public void method299(int i) {
        if (i < 0)
            return;
        aLongArray428[i] = aLong431++;
        if (anIntArrayArray429[i] != null)
            return;
        if (anIntArray427[i] == 0) {
            for (int j = 0; j < anIntArrayArray432.length; j++)
                if (anIntArrayArray432[j] == null) {
                    anIntArrayArray432[j] = new int[16384];
                    anIntArrayArray429[i] = anIntArrayArray432[j];
                    method300(i);
                    return;
                }

            long l = 1L << 30;
            int i1 = 0;
            for (int k1 = 0; k1 < texture_count; k1++)
                if (k1 != i && anIntArray427[k1] == 0 && anIntArrayArray429[k1] != null && aLongArray428[k1] < l) {
                    l = aLongArray428[k1];
                    i1 = k1;
                }

            anIntArrayArray429[i] = anIntArrayArray429[i1];
            anIntArrayArray429[i1] = null;
            method300(i);
            return;
        }
        for (int k = 0; k < anIntArrayArray433.length; k++)
            if (anIntArrayArray433[k] == null) {
                anIntArrayArray433[k] = new int[0x10000];
                anIntArrayArray429[i] = anIntArrayArray433[k];
                method300(i);
                return;
            }

        long l1 = 1L << 30;
        int j1 = 0;
        for (int i2 = 0; i2 < texture_count; i2++)
            if (i2 != i && anIntArray427[i2] == 1 && anIntArrayArray429[i2] != null && aLongArray428[i2] < l1) {
                l1 = aLongArray428[i2];
                j1 = i2;
            }

        anIntArrayArray429[i] = anIntArrayArray429[j1];
        anIntArrayArray429[j1] = null;
        method300(i);
    }

    private void method300(int i) {
        char c;
        if (anIntArray427[i] == 0)
            c = '@';
        else
            c = '\200';
        int ai[] = anIntArrayArray429[i];
        int j = 0;
        for (int k = 0; k < c; k++) {
            for (int l = 0; l < c; l++) {
                int j1 = anIntArrayArray426[i][aByteArrayArray425[i][l + k * c] & 0xff];
                j1 &= 0xf8f8ff;
                if (j1 == 0)
                    j1 = 1;
                else if (j1 == 0xf800ff) {
                    j1 = 0;
                    aBooleanArray430[i] = true;
                }
                ai[j++] = j1;
            }

        }

        for (int i1 = 0; i1 < j; i1++) {
            int k1 = ai[i1];
            ai[j + i1] = k1 - (k1 >>> 3) & 0xf8f8ff;
            ai[j * 2 + i1] = k1 - (k1 >>> 2) & 0xf8f8ff;
            ai[j * 3 + i1] = k1 - (k1 >>> 2) - (k1 >>> 3) & 0xf8f8ff;
        }

    }

    public void method301(int i) {
        if (anIntArrayArray429[i] == null)
            return;
        int ai[] = anIntArrayArray429[i];
        for (int j = 0; j < 64; j++) {
            int k = j + 4032;
            int l = ai[k];
            for (int j1 = 0; j1 < 63; j1++) {
                ai[k] = ai[k - 64];
                k -= 64;
            }

            anIntArrayArray429[i][k] = l;
        }

        char c = '\u1000';
        for (int i1 = 0; i1 < c; i1++) {
            int k1 = ai[i1];
            ai[c + i1] = k1 - (k1 >>> 3) & 0xf8f8ff;
            ai[c * 2 + i1] = k1 - (k1 >>> 2) & 0xf8f8ff;
            ai[c * 3 + i1] = k1 - (k1 >>> 2) - (k1 >>> 3) & 0xf8f8ff;
        }

    }

    public int method302(int i) {
        if (i == 0xbc614e)
            return 0;
        method299(i);
        if (i >= 0)
            return anIntArrayArray429[i][0];
        if (i < 0) {
            i = -(i + 1);
            int j = i >> 10 & 0x1f;
            int k = i >> 5 & 0x1f;
            int l = i & 0x1f;
            return (j << 19) + (k << 11) + (l << 3);
        } else {
            return 0;
        }
    }

    public void method303(int i, int j, int k) {
        if (i == 0 && j == 0 && k == 0)
            i = 32;
        for (int l = 0; l < model_count; l++)
            models[l].set_light(i, j, k);

    }

    public void method304(int i, int j, int k, int l, int i1) {
        if (k == 0 && l == 0 && i1 == 0)
            k = 32;
        for (int j1 = 0; j1 < model_count; j1++)
            models[j1].set_light(i, j, k, l, i1);

    }

    public static int method305(int i, int j, int k) {
        return -1 - (i / 8) * 1024 - (j / 8) * 32 - k / 8;
    }

    public int method306(int i, int j, int k, int l, int i1) {
        if (l == j)
            return i;
        else
            return i + ((k - i) * (i1 - j)) / (l - j);
    }

    public boolean method307(int i, int j, int k, int l, boolean flag) {
        if (flag && i <= k || i < k) {
            if (i > l)
                return true;
            if (j > k)
                return true;
            if (j > l)
                return true;
            return !flag;
        }
        if (i < l)
            return true;
        if (j < k)
            return true;
        if (j < l)
            return true;
        else
            return flag;
    }

    public boolean method308(int i, int j, int k, boolean flag) {
        if (flag && i <= k || i < k) {
            if (j > k)
                return true;
            return !flag;
        }
        if (j < k)
            return true;
        else
            return flag;
    }

    public boolean method309(int ai[], int ai1[], int ai2[], int ai3[]) {
        int i = ai.length;
        int j = ai2.length;
        byte byte0 = 0;
        int i20;
        int k20 = i20 = ai1[0];
        int k = 0;
        int j20;
        int l20 = j20 = ai3[0];
        int i1 = 0;
        for (int i21 = 1; i21 < i; i21++)
            if (ai1[i21] < i20) {
                i20 = ai1[i21];
                k = i21;
            } else if (ai1[i21] > k20)
                k20 = ai1[i21];

        for (int j21 = 1; j21 < j; j21++)
            if (ai3[j21] < j20) {
                j20 = ai3[j21];
                i1 = j21;
            } else if (ai3[j21] > l20)
                l20 = ai3[j21];

        if (j20 >= k20)
            return false;
        if (i20 >= l20)
            return false;
        int l;
        int j1;
        boolean flag;
        if (ai1[k] < ai3[i1]) {
            for (l = k; ai1[l] < ai3[i1]; l = (l + 1) % i) ;
            for (; ai1[k] < ai3[i1]; k = ((k - 1) + i) % i) ;
            int k1 = method306(ai[(k + 1) % i], ai1[(k + 1) % i], ai[k], ai1[k], ai3[i1]);
            int k6 = method306(ai[((l - 1) + i) % i], ai1[((l - 1) + i) % i], ai[l], ai1[l], ai3[i1]);
            int l10 = ai2[i1];
            flag = (k1 < l10) | (k6 < l10);
            if (method308(k1, k6, l10, flag))
                return true;
            j1 = (i1 + 1) % j;
            i1 = ((i1 - 1) + j) % j;
            if (k == l)
                byte0 = 1;
        } else {
            for (j1 = i1; ai3[j1] < ai1[k]; j1 = (j1 + 1) % j) ;
            for (; ai3[i1] < ai1[k]; i1 = ((i1 - 1) + j) % j) ;
            int l1 = ai[k];
            int i11 = method306(ai2[(i1 + 1) % j], ai3[(i1 + 1) % j], ai2[i1], ai3[i1], ai1[k]);
            int l15 = method306(ai2[((j1 - 1) + j) % j], ai3[((j1 - 1) + j) % j], ai2[j1], ai3[j1], ai1[k]);
            flag = (l1 < i11) | (l1 < l15);
            if (method308(i11, l15, l1, !flag))
                return true;
            l = (k + 1) % i;
            k = ((k - 1) + i) % i;
            if (i1 == j1)
                byte0 = 2;
        }
        while (byte0 == 0)
            if (ai1[k] < ai1[l]) {
                if (ai1[k] < ai3[i1]) {
                    if (ai1[k] < ai3[j1]) {
                        int i2 = ai[k];
                        int l6 = method306(ai[((l - 1) + i) % i], ai1[((l - 1) + i) % i], ai[l], ai1[l], ai1[k]);
                        int j11 = method306(ai2[(i1 + 1) % j], ai3[(i1 + 1) % j], ai2[i1], ai3[i1], ai1[k]);
                        int i16 = method306(ai2[((j1 - 1) + j) % j], ai3[((j1 - 1) + j) % j], ai2[j1], ai3[j1], ai1[k]);
                        if (method307(i2, l6, j11, i16, flag))
                            return true;
                        k = ((k - 1) + i) % i;
                        if (k == l)
                            byte0 = 1;
                    } else {
                        int j2 = method306(ai[(k + 1) % i], ai1[(k + 1) % i], ai[k], ai1[k], ai3[j1]);
                        int i7 = method306(ai[((l - 1) + i) % i], ai1[((l - 1) + i) % i], ai[l], ai1[l], ai3[j1]);
                        int k11 = method306(ai2[(i1 + 1) % j], ai3[(i1 + 1) % j], ai2[i1], ai3[i1], ai3[j1]);
                        int j16 = ai2[j1];
                        if (method307(j2, i7, k11, j16, flag))
                            return true;
                        j1 = (j1 + 1) % j;
                        if (i1 == j1)
                            byte0 = 2;
                    }
                } else if (ai3[i1] < ai3[j1]) {
                    int k2 = method306(ai[(k + 1) % i], ai1[(k + 1) % i], ai[k], ai1[k], ai3[i1]);
                    int j7 = method306(ai[((l - 1) + i) % i], ai1[((l - 1) + i) % i], ai[l], ai1[l], ai3[i1]);
                    int l11 = ai2[i1];
                    int k16 = method306(ai2[((j1 - 1) + j) % j], ai3[((j1 - 1) + j) % j], ai2[j1], ai3[j1], ai3[i1]);
                    if (method307(k2, j7, l11, k16, flag))
                        return true;
                    i1 = ((i1 - 1) + j) % j;
                    if (i1 == j1)
                        byte0 = 2;
                } else {
                    int l2 = method306(ai[(k + 1) % i], ai1[(k + 1) % i], ai[k], ai1[k], ai3[j1]);
                    int k7 = method306(ai[((l - 1) + i) % i], ai1[((l - 1) + i) % i], ai[l], ai1[l], ai3[j1]);
                    int i12 = method306(ai2[(i1 + 1) % j], ai3[(i1 + 1) % j], ai2[i1], ai3[i1], ai3[j1]);
                    int l16 = ai2[j1];
                    if (method307(l2, k7, i12, l16, flag))
                        return true;
                    j1 = (j1 + 1) % j;
                    if (i1 == j1)
                        byte0 = 2;
                }
            } else if (ai1[l] < ai3[i1]) {
                if (ai1[l] < ai3[j1]) {
                    int i3 = method306(ai[(k + 1) % i], ai1[(k + 1) % i], ai[k], ai1[k], ai1[l]);
                    int l7 = ai[l];
                    int j12 = method306(ai2[(i1 + 1) % j], ai3[(i1 + 1) % j], ai2[i1], ai3[i1], ai1[l]);
                    int i17 = method306(ai2[((j1 - 1) + j) % j], ai3[((j1 - 1) + j) % j], ai2[j1], ai3[j1], ai1[l]);
                    if (method307(i3, l7, j12, i17, flag))
                        return true;
                    l = (l + 1) % i;
                    if (k == l)
                        byte0 = 1;
                } else {
                    int j3 = method306(ai[(k + 1) % i], ai1[(k + 1) % i], ai[k], ai1[k], ai3[j1]);
                    int i8 = method306(ai[((l - 1) + i) % i], ai1[((l - 1) + i) % i], ai[l], ai1[l], ai3[j1]);
                    int k12 = method306(ai2[(i1 + 1) % j], ai3[(i1 + 1) % j], ai2[i1], ai3[i1], ai3[j1]);
                    int j17 = ai2[j1];
                    if (method307(j3, i8, k12, j17, flag))
                        return true;
                    j1 = (j1 + 1) % j;
                    if (i1 == j1)
                        byte0 = 2;
                }
            } else if (ai3[i1] < ai3[j1]) {
                int k3 = method306(ai[(k + 1) % i], ai1[(k + 1) % i], ai[k], ai1[k], ai3[i1]);
                int j8 = method306(ai[((l - 1) + i) % i], ai1[((l - 1) + i) % i], ai[l], ai1[l], ai3[i1]);
                int l12 = ai2[i1];
                int k17 = method306(ai2[((j1 - 1) + j) % j], ai3[((j1 - 1) + j) % j], ai2[j1], ai3[j1], ai3[i1]);
                if (method307(k3, j8, l12, k17, flag))
                    return true;
                i1 = ((i1 - 1) + j) % j;
                if (i1 == j1)
                    byte0 = 2;
            } else {
                int l3 = method306(ai[(k + 1) % i], ai1[(k + 1) % i], ai[k], ai1[k], ai3[j1]);
                int k8 = method306(ai[((l - 1) + i) % i], ai1[((l - 1) + i) % i], ai[l], ai1[l], ai3[j1]);
                int i13 = method306(ai2[(i1 + 1) % j], ai3[(i1 + 1) % j], ai2[i1], ai3[i1], ai3[j1]);
                int l17 = ai2[j1];
                if (method307(l3, k8, i13, l17, flag))
                    return true;
                j1 = (j1 + 1) % j;
                if (i1 == j1)
                    byte0 = 2;
            }
        while (byte0 == 1)
            if (ai1[k] < ai3[i1]) {
                if (ai1[k] < ai3[j1]) {
                    int i4 = ai[k];
                    int j13 = method306(ai2[(i1 + 1) % j], ai3[(i1 + 1) % j], ai2[i1], ai3[i1], ai1[k]);
                    int i18 = method306(ai2[((j1 - 1) + j) % j], ai3[((j1 - 1) + j) % j], ai2[j1], ai3[j1], ai1[k]);
                    return method308(j13, i18, i4, !flag);
                }
                int j4 = method306(ai[(k + 1) % i], ai1[(k + 1) % i], ai[k], ai1[k], ai3[j1]);
                int l8 = method306(ai[((l - 1) + i) % i], ai1[((l - 1) + i) % i], ai[l], ai1[l], ai3[j1]);
                int k13 = method306(ai2[(i1 + 1) % j], ai3[(i1 + 1) % j], ai2[i1], ai3[i1], ai3[j1]);
                int j18 = ai2[j1];
                if (method307(j4, l8, k13, j18, flag))
                    return true;
                j1 = (j1 + 1) % j;
                if (i1 == j1)
                    byte0 = 0;
            } else if (ai3[i1] < ai3[j1]) {
                int k4 = method306(ai[(k + 1) % i], ai1[(k + 1) % i], ai[k], ai1[k], ai3[i1]);
                int i9 = method306(ai[((l - 1) + i) % i], ai1[((l - 1) + i) % i], ai[l], ai1[l], ai3[i1]);
                int l13 = ai2[i1];
                int k18 = method306(ai2[((j1 - 1) + j) % j], ai3[((j1 - 1) + j) % j], ai2[j1], ai3[j1], ai3[i1]);
                if (method307(k4, i9, l13, k18, flag))
                    return true;
                i1 = ((i1 - 1) + j) % j;
                if (i1 == j1)
                    byte0 = 0;
            } else {
                int l4 = method306(ai[(k + 1) % i], ai1[(k + 1) % i], ai[k], ai1[k], ai3[j1]);
                int j9 = method306(ai[((l - 1) + i) % i], ai1[((l - 1) + i) % i], ai[l], ai1[l], ai3[j1]);
                int i14 = method306(ai2[(i1 + 1) % j], ai3[(i1 + 1) % j], ai2[i1], ai3[i1], ai3[j1]);
                int l18 = ai2[j1];
                if (method307(l4, j9, i14, l18, flag))
                    return true;
                j1 = (j1 + 1) % j;
                if (i1 == j1)
                    byte0 = 0;
            }
        while (byte0 == 2)
            if (ai3[i1] < ai1[k]) {
                if (ai3[i1] < ai1[l]) {
                    int i5 = method306(ai[(k + 1) % i], ai1[(k + 1) % i], ai[k], ai1[k], ai3[i1]);
                    int k9 = method306(ai[((l - 1) + i) % i], ai1[((l - 1) + i) % i], ai[l], ai1[l], ai3[i1]);
                    int j14 = ai2[i1];
                    return method308(i5, k9, j14, flag);
                }
                int j5 = method306(ai[(k + 1) % i], ai1[(k + 1) % i], ai[k], ai1[k], ai1[l]);
                int l9 = ai[l];
                int k14 = method306(ai2[(i1 + 1) % j], ai3[(i1 + 1) % j], ai2[i1], ai3[i1], ai1[l]);
                int i19 = method306(ai2[((j1 - 1) + j) % j], ai3[((j1 - 1) + j) % j], ai2[j1], ai3[j1], ai1[l]);
                if (method307(j5, l9, k14, i19, flag))
                    return true;
                l = (l + 1) % i;
                if (k == l)
                    byte0 = 0;
            } else if (ai1[k] < ai1[l]) {
                int k5 = ai[k];
                int i10 = method306(ai[((l - 1) + i) % i], ai1[((l - 1) + i) % i], ai[l], ai1[l], ai1[k]);
                int l14 = method306(ai2[(i1 + 1) % j], ai3[(i1 + 1) % j], ai2[i1], ai3[i1], ai1[k]);
                int j19 = method306(ai2[((j1 - 1) + j) % j], ai3[((j1 - 1) + j) % j], ai2[j1], ai3[j1], ai1[k]);
                if (method307(k5, i10, l14, j19, flag))
                    return true;
                k = ((k - 1) + i) % i;
                if (k == l)
                    byte0 = 0;
            } else {
                int l5 = method306(ai[(k + 1) % i], ai1[(k + 1) % i], ai[k], ai1[k], ai1[l]);
                int j10 = ai[l];
                int i15 = method306(ai2[(i1 + 1) % j], ai3[(i1 + 1) % j], ai2[i1], ai3[i1], ai1[l]);
                int k19 = method306(ai2[((j1 - 1) + j) % j], ai3[((j1 - 1) + j) % j], ai2[j1], ai3[j1], ai1[l]);
                if (method307(l5, j10, i15, k19, flag))
                    return true;
                l = (l + 1) % i;
                if (k == l)
                    byte0 = 0;
            }
        if (ai1[k] < ai3[i1]) {
            int i6 = ai[k];
            int j15 = method306(ai2[(i1 + 1) % j], ai3[(i1 + 1) % j], ai2[i1], ai3[i1], ai1[k]);
            int l19 = method306(ai2[((j1 - 1) + j) % j], ai3[((j1 - 1) + j) % j], ai2[j1], ai3[j1], ai1[k]);
            return method308(j15, l19, i6, !flag);
        }
        int j6 = method306(ai[(k + 1) % i], ai1[(k + 1) % i], ai[k], ai1[k], ai3[i1]);
        int k10 = method306(ai[((l - 1) + i) % i], ai1[((l - 1) + i) % i], ai[l], ai1[l], ai3[i1]);
        int k15 = ai2[i1];
        return method308(j6, k10, k15, flag);
    }

    int anInt374;
    int anIntArray375[];
    int anIntArrayArray376[][];
    int anIntArray377[];
    public int anInt378;
    public int clip_near;
    public int clip_far_3d;
    public int clip_far_2d;
    public int fog_z_falloff;
    public int fog_z_distance;
    public static int anIntArray384[] = new int[2048];
    private static int anIntArray385[] = new int[512];
    public boolean aBoolean386;
    public double aDouble387;
    public int anInt388;
    private boolean aBoolean389;
    private int mouse_x;
    private int mouse_y;
    private int anInt392;
    private int anInt393;
    private GameModel aGameModelArray394[];
    private int anIntArray395[];
    private int anInt396;
    private int anInt397;
    private int anInt398;
    private int anInt399;
    private int anInt400;
    private int view_dist;
    private int anInt402;
    private int camera_x;
    private int camera_y;
    private int camera_z;
    private int camera_pitch;
    private int camera_yaw;
    private int camera_roll;
    public int model_count;
    public int max_model_count;
    public GameModel models[];
    // private int anIntArray412[]; // only set not used
    private int anInt413;
    private Polygon aPolygonArray414[];
    private int sprite_count;
    private int sprite_x[];
    private int sprite_z[];
    private int sprite_y[];
    private int anIntArray419[];
    private int anIntArray420[];
    private int anIntArray421[];
    private int anIntArray422[];
    public GameModel currentModel;
    int texture_count;
    byte aByteArrayArray425[][];
    int anIntArrayArray426[][];
    int anIntArray427[];
    long aLongArray428[];
    int anIntArrayArray429[][];
    boolean aBooleanArray430[];
    private static long aLong431;
    int anIntArrayArray432[][];
    int anIntArrayArray433[][];
    private static byte aByteArray434[];
    private static int anIntArray435[] = new int[256];
    Surface surface;
    public int anIntArray437[];
    Class8 aClass8Array438[];
    int anInt439;
    int anInt440;
    int anIntArray441[];
    int anIntArray442[];
    int anIntArray443[];
    int anIntArray444[];
    int anIntArray445[];
    int anIntArray446[];
    boolean interlace;
    static int frustum_left;
    static int frustum_right;
    static int frustum_bottom;
    static int frustum_top;
    static int frustum_near;
    static int frustum_far;
    int anInt454;
    int anInt455;

}
