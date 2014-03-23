public class BZLib {

    public static int decompress(byte out[], int out_size, byte in[], int in_size, int offset) {
        BZState block = new BZState();
        block.input = in;
        block.next_in = offset;
        block.output = out;
        block.avail_out = 0;
        block.avail_in = in_size;
        block.decompressed_size = out_size;
        block.bs_live = 0;
        block.bs_buff = 0;
        block.total_in_lo32 = 0;
        block.total_in_hi32 = 0;
        block.total_out_lo32 = 0;
        block.total_out_hi32 = 0;
        block.block_no = 0;
        decompress(block);
        out_size -= block.decompressed_size;
        return out_size;
    }

    private static void next_header(BZState state) {
        byte c_state_out_ch = state.state_out_ch;
        int c_state_out_len = state.state_out_len;
        int c_nblock_used = state.nblock_used;
        int c_k0 = state.k0;
        int c_tt[] = state.tt;
        int c_tpos = state.tpos;
        byte output[] = state.output;
        int cs_next_out = state.avail_out;
        int cs_avail_out = state.decompressed_size;
        int asdasdasd = cs_avail_out;
        int s_save_nblockPP = state.save_nblock + 1;
        return_notr:
        do {
            if (c_state_out_len > 0) {
                do {
                    if (cs_avail_out == 0)
                        break return_notr;
                    if (c_state_out_len == 1)
                        break;
                    output[cs_next_out] = c_state_out_ch;
                    c_state_out_len--;
                    cs_next_out++;
                    cs_avail_out--;
                } while (true);
                if (cs_avail_out == 0) {
                    c_state_out_len = 1;
                    break;
                }
                output[cs_next_out] = c_state_out_ch;
                cs_next_out++;
                cs_avail_out--;
            }
            boolean flag = true;
            while (flag) {
                flag = false;
                if (c_nblock_used == s_save_nblockPP) {
                    c_state_out_len = 0;
                    break return_notr;
                }
                c_state_out_ch = (byte) c_k0;
                c_tpos = c_tt[c_tpos];
                byte k1 = (byte) (c_tpos & 0xff);
                c_tpos >>= 8;
                c_nblock_used++;
                if (k1 != c_k0) {
                    c_k0 = k1;
                    if (cs_avail_out == 0) {
                        c_state_out_len = 1;
                    } else {
                        output[cs_next_out] = c_state_out_ch;
                        cs_next_out++;
                        cs_avail_out--;
                        flag = true;
                        continue;
                    }
                    break return_notr;
                }
                if (c_nblock_used != s_save_nblockPP)
                    continue;
                if (cs_avail_out == 0) {
                    c_state_out_len = 1;
                    break return_notr;
                }
                output[cs_next_out] = c_state_out_ch;
                cs_next_out++;
                cs_avail_out--;
                flag = true;
            }
            c_state_out_len = 2;
            c_tpos = c_tt[c_tpos];
            byte k2 = (byte) (c_tpos & 0xff);
            c_tpos >>= 8;
            if (++c_nblock_used != s_save_nblockPP)
                if (k2 != c_k0) {
                    c_k0 = k2;
                } else {
                    c_state_out_len = 3;
                    c_tpos = c_tt[c_tpos];
                    byte k3 = (byte) (c_tpos & 0xff);
                    c_tpos >>= 8;
                    if (++c_nblock_used != s_save_nblockPP)
                        if (k3 != c_k0) {
                            c_k0 = k3;
                        } else {
                            c_tpos = c_tt[c_tpos];
                            byte byte3 = (byte) (c_tpos & 0xff);
                            c_tpos >>= 8;
                            c_nblock_used++;
                            c_state_out_len = (byte3 & 0xff) + 4;
                            c_tpos = c_tt[c_tpos];
                            c_k0 = (byte) (c_tpos & 0xff);
                            c_tpos >>= 8;
                            c_nblock_used++;
                        }
                }
        } while (true);
        int i2 = state.total_out_lo32;
        state.total_out_lo32 += asdasdasd - cs_avail_out;
        if (state.total_out_lo32 < i2)
            state.total_out_hi32++;
        state.state_out_ch = c_state_out_ch;
        state.state_out_len = c_state_out_len;
        state.nblock_used = c_nblock_used;
        state.k0 = c_k0;
        state.tt = c_tt;
        state.tpos = c_tpos;
        state.output = output;
        state.avail_out = cs_next_out;
        state.decompressed_size = cs_avail_out;
    }

    private static void decompress(BZState state) {
        /*boolean flag = false;
        boolean flag1 = false;
        boolean flag2 = false;
        boolean flag3 = false;
        boolean flag4 = false;
        boolean flag5 = false;
        boolean flag6 = false;
        boolean flag7 = false;
        boolean flag8 = false;
        boolean flag9 = false;
        boolean flag10 = false;
        boolean flag11 = false;
        boolean flag12 = false;
        boolean flag13 = false;
        boolean flag14 = false;
        boolean flag15 = false;
        boolean flag16 = false;
        boolean flag17 = false;
        boolean flag18 = false;*/
        int g_min_len = 0;
        int g_limit[] = null;
        int g_base[] = null;
        int g_perm[] = null;
        state.blocksize100k = 1;
        if (state.tt == null)
            state.tt = new int[state.blocksize100k * 100000];
        boolean goingandshit = true;
        while (goingandshit) {
            byte uc = get_uchar(state);
            if (uc == 23)
                return;
            uc = get_uchar(state);
            uc = get_uchar(state);
            uc = get_uchar(state);
            uc = get_uchar(state);
            uc = get_uchar(state);
            state.block_no++;
            uc = get_uchar(state);
            uc = get_uchar(state);
            uc = get_uchar(state);
            uc = get_uchar(state);
            uc = get_bit(state);
            state.block_randomised = uc != 0;
            if (state.block_randomised)
                System.out.println("PANIC! RANDOMISED BLOCK!");
            state.orig_ptr = 0;
            uc = get_uchar(state);
            state.orig_ptr = state.orig_ptr << 8 | uc & 0xff;
            uc = get_uchar(state);
            state.orig_ptr = state.orig_ptr << 8 | uc & 0xff;
            uc = get_uchar(state);
            state.orig_ptr = state.orig_ptr << 8 | uc & 0xff;
            for (int i = 0; i < 16; i++) {
                uc = get_bit(state);
                state.in_use_16[i] = uc == 1;
            }

            for (int i = 0; i < 256; i++)
                state.in_use[i] = false;

            for (int i = 0; i < 16; i++)
                if (state.in_use_16[i]) {
                    for (int j = 0; j < 16; j++) {
                        uc = get_bit(state);
                        if (uc == 1)
                            state.in_use[i * 16 + j] = true;
                    }

                }

            make_maps(state);
            int alpha_size = state.n_in_use + 2;
            int n_groups = get_bits(3, state);
            int n_selectors = get_bits(15, state);
            for (int i = 0; i < n_selectors; i++) {
                int j = 0;
                do {
                    uc = get_bit(state);
                    if (uc == 0)
                        break;
                    j++;
                } while (true);
                state.selector_mtf[i] = (byte) j;
            }

            byte pos[] = new byte[6];
            for (byte v = 0; v < n_groups; v++)
                pos[v] = v;

            for (int i = 0; i < n_selectors; i++) {
                byte v = state.selector_mtf[i];
                byte tmp = pos[v];
                for (; v > 0; v--)
                    pos[v] = pos[v - 1];

                pos[0] = tmp;
                state.selector[i] = tmp;
            }

            for (int t = 0; t < n_groups; t++) {
                int curr = get_bits(5, state);
                for (int i = 0; i < alpha_size; i++) {
                    do {
                        uc = get_bit(state);
                        if (uc == 0)
                            break;
                        uc = get_bit(state);
                        if (uc == 0)
                            curr++;
                        else
                            curr--;
                    } while (true);
                    state.len[t][i] = (byte) curr;
                }

            }

            for (int t = 0; t < n_groups; t++) {
                byte min_len = 32;
                int max_len = 0;
                for (int l1 = 0; l1 < alpha_size; l1++) {
                    if (state.len[t][l1] > max_len)
                        max_len = state.len[t][l1];
                    if (state.len[t][l1] < min_len)
                        min_len = state.len[t][l1];
                }

                create_decode_tables(state.limit[t], state.base[t], state.perm[t], state.len[t], min_len, max_len, alpha_size);
                state.min_lens[t] = min_len;
            }

            int eob = state.n_in_use + 1;
            int nblock_max = 100000 * state.blocksize100k;
            int group_no = -1;
            int group_pos = 0;
            for (int i = 0; i <= 255; i++)
                state.unzftab[i] = 0;

            int kk = 4095; // MTFA_SIZE-1;
            for (int ii = 15; ii >= 0; ii--) {
                for (int jj = 15; jj >= 0; jj--) {
                    state.mtfa[kk] = (byte) (ii * 16 + jj);
                    kk--;
                }

                state.mtfbase[ii] = kk + 1;
            }

            int nblock = 0;
            // GET_MTF_VAL
            if (group_pos == 0) {
                group_no++;
                group_pos = 50; // BZ_G_SIZE
                byte g_sel = state.selector[group_no];
                g_min_len = state.min_lens[g_sel];
                g_limit = state.limit[g_sel];
                g_perm = state.perm[g_sel];
                g_base = state.base[g_sel];
            }
            group_pos--;
            int zn = g_min_len;
            int zvec;
            byte zj;
            for (zvec = get_bits(zn, state); zvec > g_limit[zn]; zvec = zvec << 1 | zj) {
                zn++;
                zj = get_bit(state);
            }

            for (int next_sym = g_perm[zvec - g_base[zn]]; next_sym != eob; )
                if (next_sym == 0 || next_sym == 1) { // BZ_RUNA, BZ_RUNB
                    int es = -1;
                    int N = 1;
                    do {
                        if (next_sym == 0)
                            es += N;
                        else if (next_sym == 1)
                            es += 2 * N;
                        N *= 2;
                        // GET_MTF_VAL, y da fuk did they not subroutine this
                        if (group_pos == 0) {
                            group_no++;
                            group_pos = 50;
                            byte g_sel = state.selector[group_no];
                            g_min_len = state.min_lens[g_sel];
                            g_limit = state.limit[g_sel];
                            g_perm = state.perm[g_sel];
                            g_base = state.base[g_sel];
                        }
                        group_pos--;
                        int zn_2 = g_min_len;
                        int zvec_2;
                        byte zj_2;
                        for (zvec_2 = get_bits(zn_2, state); zvec_2 > g_limit[zn_2]; zvec_2 = zvec_2 << 1 | zj_2) {
                            zn_2++;
                            zj_2 = get_bit(state);
                        }

                        next_sym = g_perm[zvec_2 - g_base[zn_2]];
                    } while (next_sym == 0 || next_sym == 1);
                    es++;
                    uc = state.set_to_unseq[state.mtfa[state.mtfbase[0]] & 0xff];
                    state.unzftab[uc & 0xff] += es;
                    for (; es > 0; es--) {
                        state.tt[nblock] = uc & 0xff;
                        nblock++;
                    }

                } else {
                    int nn = next_sym - 1;
                    if (nn < 16) { // MTFL_SIZE
                        int pp = state.mtfbase[0];
                        uc = state.mtfa[pp + nn];
                        for (; nn > 3; nn -= 4) {
                            int z = pp + nn;
                            state.mtfa[z] = state.mtfa[z - 1];
                            state.mtfa[z - 1] = state.mtfa[z - 2];
                            state.mtfa[z - 2] = state.mtfa[z - 3];
                            state.mtfa[z - 3] = state.mtfa[z - 4];
                        }

                        for (; nn > 0; nn--)
                            state.mtfa[pp + nn] = state.mtfa[(pp + nn) - 1];

                        state.mtfa[pp] = uc;
                    } else {
                        int lno = nn / 16;
                        int off = nn % 16;
                        int pp = state.mtfbase[lno] + off;
                        uc = state.mtfa[pp];
                        for (; pp > state.mtfbase[lno]; pp--)
                            state.mtfa[pp] = state.mtfa[pp - 1];

                        state.mtfbase[lno]++;
                        for (; lno > 0; lno--) {
                            state.mtfbase[lno]--;
                            state.mtfa[state.mtfbase[lno]] = state.mtfa[(state.mtfbase[lno - 1] + 16) - 1];
                        }

                        state.mtfbase[0]--;
                        state.mtfa[state.mtfbase[0]] = uc;
                        if (state.mtfbase[0] == 0) {
                            kk = 4095; // MTFA_SIZE - 1
                            for (int ii = 15; ii >= 0; ii--) {
                                for (int jj = 15; jj >= 0; jj--) {
                                    state.mtfa[kk] = state.mtfa[state.mtfbase[ii] + jj];
                                    kk--;
                                }

                                state.mtfbase[ii] = kk + 1;
                            }

                        }
                    }
                    state.unzftab[state.set_to_unseq[uc & 0xff] & 0xff]++;
                    state.tt[nblock] = state.set_to_unseq[uc & 0xff] & 0xff;
                    nblock++;
                    // GET_MTF_VAL here we go AGAIN
                    if (group_pos == 0) {
                        group_no++;
                        group_pos = 50;
                        byte g_sel = state.selector[group_no];
                        g_min_len = state.min_lens[g_sel];
                        g_limit = state.limit[g_sel];
                        g_perm = state.perm[g_sel];
                        g_base = state.base[g_sel];
                    }
                    group_pos--;
                    int zn_2 = g_min_len;
                    int zvec_2;
                    byte zj_2;
                    for (zvec_2 = get_bits(zn_2, state); zvec_2 > g_limit[zn_2]; zvec_2 = zvec_2 << 1 | zj_2) {
                        zn_2++;
                        zj_2 = get_bit(state);
                    }

                    next_sym = g_perm[zvec_2 - g_base[zn_2]];
                }

            state.state_out_len = 0;
            state.state_out_ch = 0;
            state.cftab[0] = 0;
            for (int i = 1; i <= 256; i++)
                state.cftab[i] = state.unzftab[i - 1];

            for (int i = 1; i <= 256; i++)
                state.cftab[i] += state.cftab[i - 1];

            for (int i = 0; i < nblock; i++) {
                uc = (byte) (state.tt[i] & 0xff);
                state.tt[state.cftab[uc & 0xff]] |= i << 8;
                state.cftab[uc & 0xff]++;
            }

            state.tpos = state.tt[state.orig_ptr] >> 8;
            state.nblock_used = 0;
            state.tpos = state.tt[state.tpos];
            state.k0 = (byte) (state.tpos & 0xff);
            state.tpos >>= 8;
            state.nblock_used++;
            state.save_nblock = nblock;
            next_header(state);
            goingandshit = state.nblock_used == state.save_nblock + 1 && state.state_out_len == 0;
        }
    }

    private static byte get_uchar(BZState state) {
        return (byte) get_bits(8, state);
    }

    private static byte get_bit(BZState state) {
        return (byte) get_bits(1, state);
    }

    private static int get_bits(int i, BZState state) {
        int vvv;
        do {
            if (state.bs_live >= i) {
                int v = state.bs_buff >> state.bs_live - i & (1 << i) - 1;
                state.bs_live -= i;
                vvv = v;
                break;
            }
            state.bs_buff = state.bs_buff << 8 | state.input[state.next_in] & 0xff;
            state.bs_live += 8;
            state.next_in++;
            state.avail_in--;
            state.total_in_lo32++;
            if (state.total_in_lo32 == 0)
                state.total_in_hi32++;
        } while (true);
        return vvv;
    }

    private static void make_maps(BZState state) {
        state.n_in_use = 0;
        for (int i = 0; i < 256; i++)
            if (state.in_use[i]) {
                state.set_to_unseq[state.n_in_use] = (byte) i;
                state.n_in_use++;
            }

    }

    private static void create_decode_tables(int limit[], int base[], int perm[], byte length[], int min_len, int max_len, int alpha_size) {
        int pp = 0;
        for (int i = min_len; i <= max_len; i++) {
            for (int j = 0; j < alpha_size; j++)
                if (length[j] == i) {
                    perm[pp] = j;
                    pp++;
                }

        }

        for (int i = 0; i < 23; i++)
            base[i] = 0;

        for (int i = 0; i < alpha_size; i++)
            base[length[i] + 1]++;

        for (int i = 1; i < 23; i++)
            base[i] += base[i - 1];

        for (int i = 0; i < 23; i++)
            limit[i] = 0;

        int vec = 0;
        for (int i = min_len; i <= max_len; i++) {
            vec += base[i + 1] - base[i];
            limit[i] = vec - 1;
            vec <<= 1;
        }

        for (int i = min_len + 1; i <= max_len; i++)
            base[i] = (limit[i - 1] + 1 << 1) - base[i];

    }
}
