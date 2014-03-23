import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class GameModel {

    public GameModel(int num_vertices, int num_faces) {
        transform_state = 1;
        visible = true;
        unknown_scene_boolean = false;
        transparent = false;
        key = -1;
        autocommit = false;
        isolated = false;
        unlit = false;
        pickable = false;
        projected = false;
        magic = 0xbc614e;
        diameter = 0xbc614e;
        light_direction_x = 180;
        light_direction_y = 155;
        light_direction_z = 95;
        light_direction_magnitude = 256;
        light_diffuse = 512;
        light_ambience = 32;
        allocate(num_vertices, num_faces);
        anIntArrayArray279 = new int[num_faces][1];
        for (int v = 0; v < num_faces; v++)
            anIntArrayArray279[v][0] = v;

    }

    public GameModel(int num_vertices, int num_faces, boolean autocommit, boolean isolated, boolean unlit, boolean pickable, boolean projected) {
        transform_state = 1;
        visible = true;
        unknown_scene_boolean = false;
        transparent = false;
        key = -1;
        magic = 0xbc614e;
        diameter = 0xbc614e;
        light_direction_x = 180;
        light_direction_y = 155;
        light_direction_z = 95;
        light_direction_magnitude = 256;
        light_diffuse = 512;
        light_ambience = 32;
        this.autocommit = autocommit;
        this.isolated = isolated;
        this.unlit = unlit;
        this.pickable = pickable;
        this.projected = projected;
        allocate(num_vertices, num_faces);
    }

    private void allocate(int num_v, int num_f) {
        vertex_x = new int[num_v];
        vertex_y = new int[num_v];
        vertex_z = new int[num_v];
        vertex_intensity = new int[num_v];
        vertex_ambience = new byte[num_v];
        face_num_vertices = new int[num_f];
        face_vertices = new int[num_f][];
        face_fill_front = new int[num_f];
        face_fill_back = new int[num_f];
        face_intensity = new int[num_f];
        face_camera_normal_scale = new int[num_f];
        face_camera_normal_magnitude = new int[num_f];
        if (!projected) {
            vertex_camera_x = new int[num_v];
            vertex_camera_y = new int[num_v];
            vertex_camera_z = new int[num_v];
            vertex_view_x = new int[num_v];
            vertex_view_y = new int[num_v];
        }
        if (!pickable) {
            face_sprite_type = new byte[num_f];
            face_tag = new int[num_f];
        }
        if (autocommit) {
            vertex_transformed_x = vertex_x;
            vertex_transformed_y = vertex_y;
            vertex_transformed_z = vertex_z;
        } else {
            vertex_transformed_x = new int[num_v];
            vertex_transformed_y = new int[num_v];
            vertex_transformed_z = new int[num_v];
        }
        if (!unlit || !isolated) {
            face_normal_x = new int[num_f];
            face_normal_y = new int[num_f];
            face_normal_z = new int[num_f];
        }
        if (!isolated) {
            face_bound_left = new int[num_f];
            face_bound_right = new int[num_f];
            face_bound_bottom = new int[num_f];
            face_bound_top = new int[num_f];
            face_bound_near = new int[num_f];
            face_bound_far = new int[num_f];
        }
        num_faces = 0;
        num_vertices = 0;
        max_verts = num_v;
        max_faces = num_f;
        base_x = base_y = base_z = 0;
        orientation_yaw = orientation_pitch = orientation_roll = 0;
        scale_fx = scale_fy = scale_fz = 256;
        shear_xy = shear_xz = shear_yx = shear_yz = shear_zx = shear_zy = 256;
        transform_kind = 0;
    }

    public void projection_prepare() {
        vertex_camera_x = new int[num_vertices];
        vertex_camera_y = new int[num_vertices];
        vertex_camera_z = new int[num_vertices];
        vertex_view_x = new int[num_vertices];
        vertex_view_y = new int[num_vertices];
    }

    public void clear() {
        num_faces = 0;
        num_vertices = 0;
    }

    public void reduce(int df, int dz) {
        num_faces -= df;
        if (num_faces < 0)
            num_faces = 0;
        num_vertices -= dz;
        if (num_vertices < 0)
            num_vertices = 0;
    }

    public GameModel(byte data[], int offset, boolean unused) {
        transform_state = 1;
        visible = true;
        unknown_scene_boolean = false;
        transparent = false;
        key = -1;
        autocommit = false;
        isolated = false;
        unlit = false;
        pickable = false;
        projected = false;
        magic = 0xbc614e;
        diameter = 0xbc614e;
        light_direction_x = 180;
        light_direction_y = 155;
        light_direction_z = 95;
        light_direction_magnitude = 256;
        light_diffuse = 512;
        light_ambience = 32;
        int j = Utility.getUnsignedShort(data, offset);
        offset += 2;
        int k = Utility.getUnsignedShort(data, offset);
        offset += 2;
        allocate(j, k);
        anIntArrayArray279 = new int[k][1];
        for (int l = 0; l < j; l++) {
            vertex_x[l] = Utility.getSignedShort(data, offset);
            offset += 2;
        }

        for (int i1 = 0; i1 < j; i1++) {
            vertex_y[i1] = Utility.getSignedShort(data, offset);
            offset += 2;
        }

        for (int j1 = 0; j1 < j; j1++) {
            vertex_z[j1] = Utility.getSignedShort(data, offset);
            offset += 2;
        }

        num_vertices = j;
        for (int k1 = 0; k1 < k; k1++)
            face_num_vertices[k1] = data[offset++] & 0xff;

        for (int l1 = 0; l1 < k; l1++) {
            face_fill_front[l1] = Utility.getSignedShort(data, offset);
            offset += 2;
            if (face_fill_front[l1] == 32767)
                face_fill_front[l1] = magic;
        }

        for (int i2 = 0; i2 < k; i2++) {
            face_fill_back[i2] = Utility.getSignedShort(data, offset);
            offset += 2;
            if (face_fill_back[i2] == 32767)
                face_fill_back[i2] = magic;
        }

        for (int j2 = 0; j2 < k; j2++) {
            int k2 = data[offset++] & 0xff;
            if (k2 == 0)
                face_intensity[j2] = 0;
            else
                face_intensity[j2] = magic;
        }

        for (int l2 = 0; l2 < k; l2++) {
            face_vertices[l2] = new int[face_num_vertices[l2]];
            for (int i3 = 0; i3 < face_num_vertices[l2]; i3++)
                if (j < 256) {
                    face_vertices[l2][i3] = data[offset++] & 0xff;
                } else {
                    face_vertices[l2][i3] = Utility.getUnsignedShort(data, offset);
                    offset += 2;
                }

        }

        num_faces = k;
        transform_state = 1;
    }

    public GameModel(String name) {
        transform_state = 1;
        visible = true;
        unknown_scene_boolean = false;
        transparent = false;
        key = -1;
        autocommit = false;
        isolated = false;
        unlit = false;
        pickable = false;
        projected = false;
        magic = 0xbc614e;
        diameter = 0xbc614e;
        light_direction_x = 180;
        light_direction_y = 155;
        light_direction_z = 95;
        light_direction_magnitude = 256;
        light_diffuse = 512;
        light_ambience = 32;
        byte data[];
        try {
            InputStream inputstream = Utility.openFile(name);
            DataInputStream datainputstream = new DataInputStream(inputstream);
            data = new byte[3];
            data_ptr = 0;
            for (int i = 0; i < 3; i += datainputstream.read(data, i, 3 - i)) ;
            int sz = read_base64(data);
            data = new byte[sz];
            data_ptr = 0;
            for (int j = 0; j < sz; j += datainputstream.read(data, j, sz - j)) ;
            datainputstream.close();
        } catch (IOException _ex) {
            num_vertices = 0;
            num_faces = 0;
            return;
        }
        int n_v = read_base64(data);
        int n_f = read_base64(data);
        allocate(n_v, n_f);
        anIntArrayArray279 = new int[n_f][];
        for (int j3 = 0; j3 < n_v; j3++) {
            int x = read_base64(data);
            int y = read_base64(data);
            int z = read_base64(data);
            vertex_at(x, y, z);
        }

        for (int k3 = 0; k3 < n_f; k3++) {
            int n = read_base64(data);
            int front = read_base64(data);
            int back = read_base64(data);
            int l2 = read_base64(data);
            light_diffuse = read_base64(data);
            light_ambience = read_base64(data);
            int gouraud = read_base64(data);
            int vs[] = new int[n];
            for (int i = 0; i < n; i++)
                vs[i] = read_base64(data);

            int ai1[] = new int[l2];
            for (int i4 = 0; i4 < l2; i4++)
                ai1[i4] = read_base64(data);

            int j4 = create_face(n, vs, front, back);
            anIntArrayArray279[k3] = ai1;
            if (gouraud == 0)
                face_intensity[j4] = 0;
            else
                face_intensity[j4] = magic;
        }

        transform_state = 1;
    }

    public GameModel(GameModel pieces[], int count, boolean autocommit, boolean isolated, boolean unlit, boolean pickable) {
        transform_state = 1;
        visible = true;
        unknown_scene_boolean = false;
        transparent = false;
        key = -1;
        projected = false;
        magic = 0xbc614e;
        diameter = 0xbc614e;
        light_direction_x = 180;
        light_direction_y = 155;
        light_direction_z = 95;
        light_direction_magnitude = 256;
        light_diffuse = 512;
        light_ambience = 32;
        this.autocommit = autocommit;
        this.isolated = isolated;
        this.unlit = unlit;
        this.pickable = pickable;
        merge(pieces, count, false);
    }

    public GameModel(GameModel pieces[], int count) {
        transform_state = 1;
        visible = true;
        unknown_scene_boolean = false;
        transparent = false;
        key = -1;
        autocommit = false;
        isolated = false;
        unlit = false;
        pickable = false;
        projected = false;
        magic = 0xbc614e;
        diameter = 0xbc614e;
        light_direction_x = 180;
        light_direction_y = 155;
        light_direction_z = 95;
        light_direction_magnitude = 256;
        light_diffuse = 512;
        light_ambience = 32;
        merge(pieces, count, true);
    }

    public void merge(GameModel pieces[], int count, boolean trans_state) {
        int num_f = 0;
        int num_v = 0;
        for (int i = 0; i < count; i++) {
            num_f += pieces[i].num_faces;
            num_v += pieces[i].num_vertices;
        }

        allocate(num_v, num_f);
        if (trans_state)
            anIntArrayArray279 = new int[num_f][];
        for (int i = 0; i < count; i++) {
            GameModel source = pieces[i];
            source.commit();
            light_ambience = source.light_ambience;
            light_diffuse = source.light_diffuse;
            light_direction_x = source.light_direction_x;
            light_direction_y = source.light_direction_y;
            light_direction_z = source.light_direction_z;
            light_direction_magnitude = source.light_direction_magnitude;
            for (int src_f = 0; src_f < source.num_faces; src_f++) {
                int dst_vs[] = new int[source.face_num_vertices[src_f]];
                int src_vs[] = source.face_vertices[src_f];
                for (int v = 0; v < source.face_num_vertices[src_f]; v++)
                    dst_vs[v] = vertex_at(source.vertex_x[src_vs[v]], source.vertex_y[src_vs[v]], source.vertex_z[src_vs[v]]);

                int dst_f = create_face(source.face_num_vertices[src_f], dst_vs, source.face_fill_front[src_f], source.face_fill_back[src_f]);
                face_intensity[dst_f] = source.face_intensity[src_f];
                face_camera_normal_scale[dst_f] = source.face_camera_normal_scale[src_f];
                face_camera_normal_magnitude[dst_f] = source.face_camera_normal_magnitude[src_f];
                if (trans_state)
                    if (count > 1) {
                        anIntArrayArray279[dst_f] = new int[source.anIntArrayArray279[src_f].length + 1];
                        anIntArrayArray279[dst_f][0] = i;
                        for (int i2 = 0; i2 < source.anIntArrayArray279[src_f].length; i2++)
                            anIntArrayArray279[dst_f][i2 + 1] = source.anIntArrayArray279[src_f][i2];

                    } else {
                        anIntArrayArray279[dst_f] = new int[source.anIntArrayArray279[src_f].length];
                        for (int j2 = 0; j2 < source.anIntArrayArray279[src_f].length; j2++)
                            anIntArrayArray279[dst_f][j2] = source.anIntArrayArray279[src_f][j2];

                    }
            }

        }

        transform_state = 1;
    }

    public int vertex_at(int x, int y, int z) {
        for (int l = 0; l < num_vertices; l++)
            if (vertex_x[l] == x && vertex_y[l] == y && vertex_z[l] == z)
                return l;

        if (num_vertices >= max_verts) {
            return -1;
        } else {
            vertex_x[num_vertices] = x;
            vertex_y[num_vertices] = y;
            vertex_z[num_vertices] = z;
            return num_vertices++;
        }
    }

    public int method180(int i, int j, int k) {
        if (num_vertices >= max_verts) {
            return -1;
        } else {
            vertex_x[num_vertices] = i;
            vertex_y[num_vertices] = j;
            vertex_z[num_vertices] = k;
            return num_vertices++;
        }
    }

    public int create_face(int n, int vs[], int front, int back) {
        if (num_faces >= max_faces) {
            return -1;
        } else {
            face_num_vertices[num_faces] = n;
            face_vertices[num_faces] = vs;
            face_fill_front[num_faces] = front;
            face_fill_back[num_faces] = back;
            transform_state = 1;
            return num_faces++;
        }
    }

    public GameModel[] split(int unused1, int unused2, int piece_dx, int piece_dz, int rows, int count, int piece_max_vertices, boolean pickable) {
        commit();
        int piece_n_v[] = new int[count];
        int piece_n_f[] = new int[count];
        for (int i = 0; i < count; i++) {
            piece_n_v[i] = 0;
            piece_n_f[i] = 0;
        }

        for (int f = 0; f < num_faces; f++) {
            int sum_x = 0;
            int sum_z = 0;
            int n = face_num_vertices[f];
            int vs[] = face_vertices[f];
            for (int i = 0; i < n; i++) {
                sum_x += vertex_x[vs[i]];
                sum_z += vertex_z[vs[i]];
            }

            int p = sum_x / (n * piece_dx) + (sum_z / (n * piece_dz)) * rows;
            piece_n_v[p] += n;
            piece_n_f[p]++;
        }

        GameModel pieces[] = new GameModel[count];
        for (int i = 0; i < count; i++) {
            if (piece_n_v[i] > piece_max_vertices)
                piece_n_v[i] = piece_max_vertices;
            pieces[i] = new GameModel(piece_n_v[i], piece_n_f[i], true, true, true, pickable, true);
            pieces[i].light_diffuse = light_diffuse;
            pieces[i].light_ambience = light_ambience;
        }

        for (int f = 0; f < num_faces; f++) {
            int sum_x = 0;
            int sum_z = 0;
            int n = face_num_vertices[f];
            int vs[] = face_vertices[f];
            for (int i = 0; i < n; i++) {
                sum_x += vertex_x[vs[i]];
                sum_z += vertex_z[vs[i]];
            }

            int p = sum_x / (n * piece_dx) + (sum_z / (n * piece_dz)) * rows;
            copy_lighting(pieces[p], vs, n, f);
        }

        for (int p = 0; p < count; p++)
            pieces[p].projection_prepare();

        return pieces;
    }

    public void copy_lighting(GameModel model, int src_vs[], int n_v, int in_f) {
        int dst_vs[] = new int[n_v];
        for (int in_v = 0; in_v < n_v; in_v++) {
            int out_v = dst_vs[in_v] = model.vertex_at(vertex_x[src_vs[in_v]], vertex_y[src_vs[in_v]], vertex_z[src_vs[in_v]]);
            model.vertex_intensity[out_v] = vertex_intensity[src_vs[in_v]];
            model.vertex_ambience[out_v] = vertex_ambience[src_vs[in_v]];
        }

        int out_f = model.create_face(n_v, dst_vs, face_fill_front[in_f], face_fill_back[in_f]);
        if (!model.pickable && !pickable)
            model.face_tag[out_f] = face_tag[in_f];
        model.face_intensity[out_f] = face_intensity[in_f];
        model.face_camera_normal_scale[out_f] = face_camera_normal_scale[in_f];
        model.face_camera_normal_magnitude[out_f] = face_camera_normal_magnitude[in_f];
    }

    public void set_light(boolean gouraud, int ambient, int diffuse, int x, int y, int z) {
        light_ambience = 256 - ambient * 4;
        light_diffuse = (64 - diffuse) * 16 + 128;
        if (unlit)
            return;
        for (int i = 0; i < num_faces; i++)
            if (gouraud)
                face_intensity[i] = magic;
            else
                face_intensity[i] = 0;

        light_direction_x = x;
        light_direction_y = y;
        light_direction_z = z;
        light_direction_magnitude = (int) Math.sqrt(x * x + y * y + z * z);
        light();
    }

    public void set_light(int ambience, int diffuse, int x, int y, int z) {
        light_ambience = 256 - ambience * 4;
        light_diffuse = (64 - diffuse) * 16 + 128;
        if (!unlit) {
            light_direction_x = x;
            light_direction_y = y;
            light_direction_z = z;
            light_direction_magnitude = (int) Math.sqrt(x * x + y * y + z * z);
            light();
        }
    }

    public void set_light(int x, int y, int z) {
        if (!unlit) {
            light_direction_x = x;
            light_direction_y = y;
            light_direction_z = z;
            light_direction_magnitude = (int) Math.sqrt(x * x + y * y + z * z);
            light();
        }
    }

    public void set_vertex_ambience(int v, int ambience) {
        vertex_ambience[v] = (byte) ambience;
    }

    public void rotate(int yaw, int pitch, int roll) {
        orientation_yaw = orientation_yaw + yaw & 0xff;
        orientation_pitch = orientation_pitch + pitch & 0xff;
        orientation_roll = orientation_roll + roll & 0xff;
        determine_transform_kind();
        transform_state = 1;
    }

    public void orient(int yaw, int pitch, int roll) {
        orientation_yaw = yaw & 0xff;
        orientation_pitch = pitch & 0xff;
        orientation_roll = roll & 0xff;
        determine_transform_kind();
        transform_state = 1;
    }

    public void translate(int x, int y, int z) {
        base_x += x;
        base_y += y;
        base_z += z;
        determine_transform_kind();
        transform_state = 1;
    }

    public void place(int x, int y, int z) {
        base_x = x;
        base_y = y;
        base_z = z;
        determine_transform_kind();
        transform_state = 1;
    }

    private void determine_transform_kind() {
        if (shear_xy != 256 || shear_xz != 256 || shear_yx != 256 || shear_yz != 256 || shear_zx != 256 || shear_zy != 256) {
            transform_kind = 4;
        } else if (scale_fx != 256 || scale_fy != 256 || scale_fz != 256) {
            transform_kind = 3;
        } else if (orientation_yaw != 0 || orientation_pitch != 0 || orientation_roll != 0) {
            transform_kind = 2;
        } else if (base_x != 0 || base_y != 0 || base_z != 0) {
            transform_kind = 1;
        } else {
            transform_kind = 0;
        }
    }

    private void apply_translate(int dx, int dy, int dz) {
        for (int v = 0; v < num_vertices; v++) {
            vertex_transformed_x[v] += dx;
            vertex_transformed_y[v] += dy;
            vertex_transformed_z[v] += dz;
        }

    }

    private void apply_rotation(int yaw, int roll, int pitch) {
        for (int v = 0; v < num_vertices; v++) {
            if (pitch != 0) {
                int sin = sine9[pitch];
                int cos = sine9[pitch + 256];
                int x = vertex_transformed_y[v] * sin + vertex_transformed_x[v] * cos >> 15;
                vertex_transformed_y[v] = vertex_transformed_y[v] * cos - vertex_transformed_x[v] * sin >> 15;
                vertex_transformed_x[v] = x;
            }
            if (yaw != 0) {
                int sin = sine9[yaw];
                int cos = sine9[yaw + 256];
                int y = vertex_transformed_y[v] * cos - vertex_transformed_z[v] * sin >> 15;
                vertex_transformed_z[v] = vertex_transformed_y[v] * sin + vertex_transformed_z[v] * cos >> 15;
                vertex_transformed_y[v] = y;
            }
            if (roll != 0) {
                int sin = sine9[roll];
                int cos = sine9[roll + 256];
                int x = vertex_transformed_z[v] * sin + vertex_transformed_x[v] * cos >> 15;
                vertex_transformed_z[v] = vertex_transformed_z[v] * cos - vertex_transformed_x[v] * sin >> 15;
                vertex_transformed_x[v] = x;
            }
        }

    }

    private void apply_shear(int xy, int xz, int yx, int yz, int zx, int zy) {
        for (int idx = 0; idx < num_vertices; idx++) {
            if (xy != 0)
                vertex_transformed_x[idx] += vertex_transformed_y[idx] * xy >> 8;
            if (xz != 0)
                vertex_transformed_z[idx] += vertex_transformed_y[idx] * xz >> 8;
            if (yx != 0)
                vertex_transformed_x[idx] += vertex_transformed_z[idx] * yx >> 8;
            if (yz != 0)
                vertex_transformed_y[idx] += vertex_transformed_z[idx] * yz >> 8;
            if (zx != 0)
                vertex_transformed_z[idx] += vertex_transformed_x[idx] * zx >> 8;
            if (zy != 0)
                vertex_transformed_y[idx] += vertex_transformed_x[idx] * zy >> 8;
        }

    }

    private void apply_scale(int fx, int fy, int fz) {
        for (int v = 0; v < num_vertices; v++) {
            vertex_transformed_x[v] = vertex_transformed_x[v] * fx >> 8;
            vertex_transformed_y[v] = vertex_transformed_y[v] * fy >> 8;
            vertex_transformed_z[v] = vertex_transformed_z[v] * fz >> 8;
        }

    }

    private void compute_bounds() {
        x1 = y1 = z1 = 0xf423f;
        diameter = x2 = y2 = z2 = 0xfff0bdc1;
        for (int face = 0; face < num_faces; face++) {
            int vs[] = face_vertices[face];
            int v = vs[0];
            int n = face_num_vertices[face];
            int x1;
            int x2 = x1 = vertex_transformed_x[v];
            int y1;
            int y2 = y1 = vertex_transformed_y[v];
            int z1;
            int z2 = z1 = vertex_transformed_z[v];
            for (int i = 0; i < n; i++) {
                v = vs[i];
                if (vertex_transformed_x[v] < x1)
                    x1 = vertex_transformed_x[v];
                else if (vertex_transformed_x[v] > x2)
                    x2 = vertex_transformed_x[v];
                if (vertex_transformed_y[v] < y1)
                    y1 = vertex_transformed_y[v];
                else if (vertex_transformed_y[v] > y2)
                    y2 = vertex_transformed_y[v];
                if (vertex_transformed_z[v] < z1)
                    z1 = vertex_transformed_z[v];
                else if (vertex_transformed_z[v] > z2)
                    z2 = vertex_transformed_z[v];
            }

            if (!isolated) {
                face_bound_left[face] = x1;
                face_bound_right[face] = x2;
                face_bound_bottom[face] = y1;
                face_bound_top[face] = y2;
                face_bound_near[face] = z1;
                face_bound_far[face] = z2;
            }
            if (x2 - x1 > diameter)
                diameter = x2 - x1;
            if (y2 - y1 > diameter)
                diameter = y2 - y1;
            if (z2 - z1 > diameter)
                diameter = z2 - z1;
            if (x1 < this.x1)
                this.x1 = x1;
            if (x2 > this.x2)
                this.x2 = x2;
            if (y1 < this.y1)
                this.y1 = y1;
            if (y2 > this.y2)
                this.y2 = y2;
            if (z1 < this.z1)
                this.z1 = z1;
            if (z2 > this.z2)
                this.z2 = z2;
        }

    }

    public void light() {
        if (unlit)
            return;
        int divisor = light_diffuse * light_direction_magnitude >> 8;
        for (int face = 0; face < num_faces; face++)
            if (face_intensity[face] != magic)
                face_intensity[face] = (face_normal_x[face] * light_direction_x + face_normal_y[face] * light_direction_y + face_normal_z[face] * light_direction_z) / divisor;

        int normal_x[] = new int[num_vertices];
        int normal_y[] = new int[num_vertices];
        int normal_z[] = new int[num_vertices];
        int normal_magnitude[] = new int[num_vertices];
        for (int k = 0; k < num_vertices; k++) {
            normal_x[k] = 0;
            normal_y[k] = 0;
            normal_z[k] = 0;
            normal_magnitude[k] = 0;
        }

        for (int face = 0; face < num_faces; face++)
            if (face_intensity[face] == magic) {
                for (int v = 0; v < face_num_vertices[face]; v++) {
                    int k1 = face_vertices[face][v];
                    normal_x[k1] += face_normal_x[face];
                    normal_y[k1] += face_normal_y[face];
                    normal_z[k1] += face_normal_z[face];
                    normal_magnitude[k1]++;
                }

            }

        for (int v = 0; v < num_vertices; v++)
            if (normal_magnitude[v] > 0)
                vertex_intensity[v] = (normal_x[v] * light_direction_x + normal_y[v] * light_direction_y + normal_z[v] * light_direction_z) / (divisor * normal_magnitude[v]);

    }

    public void relight() {
        if (unlit && isolated)
            return;
        for (int face = 0; face < num_faces; face++) {
            int verts[] = face_vertices[face];
            int a_x = vertex_transformed_x[verts[0]];
            int a_y = vertex_transformed_y[verts[0]];
            int a_z = vertex_transformed_z[verts[0]];
            int b_x = vertex_transformed_x[verts[1]] - a_x;
            int b_y = vertex_transformed_y[verts[1]] - a_y;
            int b_z = vertex_transformed_z[verts[1]] - a_z;
            int c_x = vertex_transformed_x[verts[2]] - a_x;
            int c_y = vertex_transformed_y[verts[2]] - a_y;
            int c_z = vertex_transformed_z[verts[2]] - a_z;
            int norm_x = b_y * c_z - c_y * b_z;
            int norm_y = b_z * c_x - c_z * b_x;
            int norm_z;
            for (norm_z = b_x * c_y - c_x * b_y; norm_x > 8192 || norm_y > 8192 || norm_z > 8192 || norm_x < -8192 || norm_y < -8192 || norm_z < -8192; norm_z >>= 1) {
                norm_x >>= 1;
                norm_y >>= 1;
            }

            int norm_mag = (int) (256D * Math.sqrt(norm_x * norm_x + norm_y * norm_y + norm_z * norm_z));
            if (norm_mag <= 0)
                norm_mag = 1;
            face_normal_x[face] = (norm_x * 0x10000) / norm_mag;
            face_normal_y[face] = (norm_y * 0x10000) / norm_mag;
            face_normal_z[face] = (norm_z * 65535) / norm_mag;
            face_camera_normal_scale[face] = -1;
        }

        light();
    }

    public void apply() {
        if (transform_state == 2) {
            transform_state = 0;
            for (int v = 0; v < num_vertices; v++) {
                vertex_transformed_x[v] = vertex_x[v];
                vertex_transformed_y[v] = vertex_y[v];
                vertex_transformed_z[v] = vertex_z[v];
            }

            x1 = y1 = z1 = 0xff676981;
            diameter = x2 = y2 = z2 = 0x98967f;
            return;
        }
        if (transform_state == 1) {
            transform_state = 0;
            for (int v = 0; v < num_vertices; v++) {
                vertex_transformed_x[v] = vertex_x[v];
                vertex_transformed_y[v] = vertex_y[v];
                vertex_transformed_z[v] = vertex_z[v];
            }

            if (transform_kind >= 2)
                apply_rotation(orientation_yaw, orientation_pitch, orientation_roll);
            if (transform_kind >= 3)
                apply_scale(scale_fx, scale_fy, scale_fz);
            if (transform_kind >= 4)
                apply_shear(shear_xy, shear_xz, shear_yx, shear_yz, shear_zx, shear_zy);
            if (transform_kind >= 1)
                apply_translate(base_x, base_y, base_z);
            compute_bounds();
            relight();
        }
    }

    public void project(int camera_x, int camera_y, int camera_z, int camera_pitch, int camera_roll, int camera_yaw, int view_dist, int clip_near) {
        apply();
        if (z1 > Scene.frustum_far || z2 < Scene.frustum_near || x1 > Scene.frustum_right || x2 < Scene.frustum_left || y1 > Scene.frustum_top || y2 < Scene.frustum_bottom) {
            visible = false;
            return;
        }
        visible = true;
        int yaw_sin = 0;
        int yaw_cos = 0;
        int pitch_sin = 0;
        int pitch_cos = 0;
        int roll_sin = 0;
        int roll_cos = 0;
        if (camera_yaw != 0) {
            yaw_sin = sine11[camera_yaw];
            yaw_cos = sine11[camera_yaw + 1024];
        }
        if (camera_roll != 0) {
            roll_sin = sine11[camera_roll];
            roll_cos = sine11[camera_roll + 1024];
        }
        if (camera_pitch != 0) {
            pitch_sin = sine11[camera_pitch];
            pitch_cos = sine11[camera_pitch + 1024];
        }
        for (int v = 0; v < num_vertices; v++) {
            int x = vertex_transformed_x[v] - camera_x;
            int y = vertex_transformed_y[v] - camera_y;
            int z = vertex_transformed_z[v] - camera_z;
            if (camera_yaw != 0) {
                int _x = y * yaw_sin + x * yaw_cos >> 15;
                y = y * yaw_cos - x * yaw_sin >> 15;
                x = _x;
            }
            if (camera_roll != 0) {
                int _x = z * roll_sin + x * roll_cos >> 15;
                z = z * roll_cos - x * roll_sin >> 15;
                x = _x;
            }
            if (camera_pitch != 0) {
                int _y = y * pitch_cos - z * pitch_sin >> 15;
                z = y * pitch_sin + z * pitch_cos >> 15;
                y = _y;
            }
            if (z >= clip_near)
                vertex_view_x[v] = (x << view_dist) / z;
            else
                vertex_view_x[v] = x << view_dist;
            if (z >= clip_near)
                vertex_view_y[v] = (y << view_dist) / z;
            else
                vertex_view_y[v] = y << view_dist;
            vertex_camera_x[v] = x;
            vertex_camera_y[v] = y;
            vertex_camera_z[v] = z;
        }

    }

    public void commit() {
        apply();
        for (int i = 0; i < num_vertices; i++) {
            vertex_x[i] = vertex_transformed_x[i];
            vertex_y[i] = vertex_transformed_y[i];
            vertex_z[i] = vertex_transformed_z[i];
        }

        base_x = base_y = base_z = 0;
        orientation_yaw = orientation_pitch = orientation_roll = 0;
        scale_fx = scale_fy = scale_fz = 256;
        shear_xy = shear_xz = shear_yx = shear_yz = shear_zx = shear_zy = 256;
        transform_kind = 0;
    }

    public GameModel copy() {
        GameModel pieces[] = new GameModel[1];
        pieces[0] = this;
        GameModel gameModel = new GameModel(pieces, 1);
        gameModel.depth = depth;
        gameModel.transparent = transparent;
        return gameModel;
    }

    public GameModel copy(boolean autocommit, boolean isolated, boolean unlit, boolean pickable) {
        GameModel pieces[] = new GameModel[1];
        pieces[0] = this;
        GameModel gameModel = new GameModel(pieces, 1, autocommit, isolated, unlit, pickable);
        gameModel.depth = depth;
        return gameModel;
    }

    public void copy_position(GameModel model) {
        orientation_yaw = model.orientation_yaw;
        orientation_pitch = model.orientation_pitch;
        orientation_roll = model.orientation_roll;
        base_x = model.base_x;
        base_y = model.base_y;
        base_z = model.base_z;
        determine_transform_kind();
        transform_state = 1;
    }

    public int read_base64(byte buff[]) {
        for (; buff[data_ptr] == 10 || buff[data_ptr] == 13; data_ptr++) ;
        int hi = base64_alphabet[buff[data_ptr++] & 0xff];
        int mid = base64_alphabet[buff[data_ptr++] & 0xff];
        int lo = base64_alphabet[buff[data_ptr++] & 0xff];
        int val = (hi * 4096 + mid * 64 + lo) - 0x20000;
        if (val == 0x1e240)
            val = magic;
        return val;
    }

    public int num_vertices;
    public int vertex_camera_x[];
    public int vertex_camera_y[];
    public int vertex_camera_z[];
    public int vertex_view_x[];
    public int vertex_view_y[];
    public int vertex_intensity[];
    public byte vertex_ambience[];
    public int num_faces;
    public int face_num_vertices[];
    public int face_vertices[][];
    public int face_fill_front[];
    public int face_fill_back[];
    public int face_camera_normal_magnitude[];
    public int face_camera_normal_scale[];
    public int face_intensity[];
    public int face_normal_x[];
    public int face_normal_y[];
    public int face_normal_z[];
    public int depth;
    public int transform_state;
    public boolean visible;
    public int x1;
    public int x2;
    public int y1;
    public int y2;
    public int z1;
    public int z2;
    public boolean unknown_scene_boolean;
    public boolean transparent;
    public int key;
    public int face_tag[];
    public byte face_sprite_type[];
    private boolean autocommit;
    public boolean isolated;
    public boolean unlit;
    public boolean pickable;
    public boolean projected;
    private static int sine9[];
    private static int sine11[];
    private static int base64_alphabet[];
    private int magic;
    public int max_verts;
    public int vertex_x[];
    public int vertex_y[];
    public int vertex_z[];
    public int vertex_transformed_x[];
    public int vertex_transformed_y[];
    public int vertex_transformed_z[];
    private int max_faces;
    private int anIntArrayArray279[][];
    private int face_bound_left[];
    private int face_bound_right[];
    private int face_bound_bottom[];
    private int face_bound_top[];
    private int face_bound_near[];
    private int face_bound_far[];
    private int base_x;
    private int base_y;
    private int base_z;
    private int orientation_yaw;
    private int orientation_pitch;
    private int orientation_roll;
    private int scale_fx;
    private int scale_fy;
    private int scale_fz;
    private int shear_xy;
    private int shear_xz;
    private int shear_yx;
    private int shear_yz;
    private int shear_zx;
    private int shear_zy;
    private int transform_kind;
    private int diameter;
    private int light_direction_x;
    private int light_direction_y;
    private int light_direction_z;
    private int light_direction_magnitude;
    protected int light_diffuse;
    protected int light_ambience;
    private int data_ptr;

    static {
        sine9 = new int[512];
        sine11 = new int[2048];

        base64_alphabet = new int[256];
        for (int i = 0; i < 256; i++) {
            sine9[i] = (int) (Math.sin((double) i * 0.02454369D) * 32768D);
            sine9[i + 256] = (int) (Math.cos((double) i * 0.02454369D) * 32768D);
        }

        for (int j = 0; j < 1024; j++) {
            sine11[j] = (int) (Math.sin((double) j * 0.00613592315D) * 32768D);
            sine11[j + 1024] = (int) (Math.cos((double) j * 0.00613592315D) * 32768D);
        }

        for (int j1 = 0; j1 < 10; j1++)
            base64_alphabet[48 + j1] = j1;

        for (int k1 = 0; k1 < 26; k1++)
            base64_alphabet[65 + k1] = k1 + 10;

        for (int l1 = 0; l1 < 26; l1++)
            base64_alphabet[97 + l1] = l1 + 36;

        base64_alphabet[163] = 62;
        base64_alphabet[36] = 63;
    }
}
