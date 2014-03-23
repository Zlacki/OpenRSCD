class BZState {

    BZState() {
        unzftab = new int[256];
        cftab = new int[257];
        //anIntArray488 = new int[257]; // unused
        in_use = new boolean[256];
        in_use_16 = new boolean[16];
        set_to_unseq = new byte[256];
        mtfa = new byte[4096];
        mtfbase = new int[16];
        selector = new byte[18002];
        selector_mtf = new byte[18002];
        len = new byte[6][258];
        limit = new int[6][258];
        base = new int[6][258];
        perm = new int[6][258];
        min_lens = new int[6];
    }

    /* unused
    final int anInt456 = 4096; // MTFA_SIZE ?
    final int anInt457 = 16; // MTFL_SIZE
    final int anInt458 = 258;
    final int anInt459 = 23;
    final int anInt460 = 1;
    final int anInt461 = 6; // BZ_N_GROUPS ?
    final int anInt462 = 50; // BZ_G_SIZE ?
    final int anInt463 = 4;
    final int anInt464 = 18002; */
    byte input[];
    int next_in;
    int avail_in;
    int total_in_lo32;
    int total_in_hi32;
    byte output[];
    int avail_out;
    int decompressed_size;
    int total_out_lo32;
    int total_out_hi32;
    byte state_out_ch;
    int state_out_len;
    boolean block_randomised;
    int bs_buff;
    int bs_live;
    int blocksize100k;
    int block_no;
    int orig_ptr;
    int tpos;
    int k0;
    int unzftab[];
    int nblock_used;
    int cftab[];
    //int anIntArray488[]; // unused
    public int tt[]; // this was static, wonder why?
    int n_in_use;
    boolean in_use[];
    boolean in_use_16[];
    byte set_to_unseq[];
    byte mtfa[];
    int mtfbase[];
    byte selector[];
    byte selector_mtf[];
    byte len[][];
    int limit[][];
    int base[][];
    int perm[][];
    int min_lens[];
    int save_nblock;
}
