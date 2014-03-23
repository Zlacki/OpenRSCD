public class WordFilter {

    public static void load_filter(RSABuffer fragments, RSABuffer bad, RSABuffer host, RSABuffer tld) {
        load_bad(bad);
        load_host(host);
        load_fragments(fragments);
        load_tld(tld);
    }

    public static void load_tld(RSABuffer buffer) {
        int wordcount = buffer.getUnsignedInt();
        wordlist_tld = new char[wordcount][];
        dunno_tld = new int[wordcount];
        for (int idx = 0; idx < wordcount; idx++) {
            dunno_tld[idx] = buffer.getUnsignedByte();
            char ac[] = new char[buffer.getUnsignedByte()];
            for (int k = 0; k < ac.length; k++)
                ac[k] = (char) buffer.getUnsignedByte();

            wordlist_tld[idx] = ac;
        }

    }

    public static void load_bad(RSABuffer buffer) {
        int wordcount = buffer.getUnsignedInt();
        wordlist_bad = new char[wordcount][];
        dunno_bad = new byte[wordcount][][];
        read_buffer(buffer, wordlist_bad, dunno_bad);
    }

    public static void load_host(RSABuffer buffer) {
        int wordcount = buffer.getUnsignedInt();
        wordlist_host = new char[wordcount][];
        dunno_host = new byte[wordcount][][];
        //System.out.println("hostsenc len: " + buffer.buffer.length);
        //System.exit(1);
        read_buffer(buffer, wordlist_host, dunno_host);
    }

    public static void load_fragments(RSABuffer buffer) {
        hash_fragments = new int[buffer.getUnsignedInt()];
        for (int i = 0; i < hash_fragments.length; i++)
            hash_fragments[i] = buffer.getUnsignedShort();

    }

    public static void read_buffer(RSABuffer buffer, char wordlist[][], byte abyte0[][][]) {
        for (int i = 0; i < wordlist.length; i++) {
            char currentword[] = new char[buffer.getUnsignedByte()];
            for (int j = 0; j < currentword.length; j++)
                currentword[j] = (char) buffer.getUnsignedByte();

            //if(buffer.buffer.length == 6188)
                //System.out.println(i + "=" + new String(currentword));
            wordlist[i] = currentword;
            byte abyte1[][] = new byte[buffer.getUnsignedInt()][2];
            for (int k = 0; k < abyte1.length; k++) {
                abyte1[k][0] = (byte) buffer.getUnsignedByte();
                abyte1[k][1] = (byte) buffer.getUnsignedByte();
            }

            if (abyte1.length > 0)
                abyte0[i] = abyte1;
        }

    }

    public static String filter(String input) {
        char output_chars[] = input.toLowerCase().toCharArray();
        apply_dot_slash_filter(output_chars);
        apply_badwords_filter(output_chars);
        apply_host_filter(output_chars);
        method382(output_chars);
        for (int ignore_idx = 0; ignore_idx < wordlist_ignore.length; ignore_idx++) {
            for (int input_ignore_idx = -1; (input_ignore_idx = input.indexOf(wordlist_ignore[ignore_idx], input_ignore_idx + 1)) != -1; ) {
                char ignoreword_chars[] = wordlist_ignore[ignore_idx].toCharArray();
                for (int ignoreword_idx = 0; ignoreword_idx < ignoreword_chars.length; ignoreword_idx++)
                    output_chars[ignoreword_idx + input_ignore_idx] = ignoreword_chars[ignoreword_idx];

            }

        }

        if (aBoolean549) {// todo try this! value is never true
            strip_non_uc(input.toCharArray(), output_chars);
            method370(output_chars);
        }
        return new String(output_chars);
    }

    public static void strip_non_uc(char input[], char output[]) {
        for (int chr_idx = 0; chr_idx < input.length; chr_idx++)
            if (output[chr_idx] != '*' && is_uc_char(input[chr_idx]))
                output[chr_idx] = input[chr_idx];

    }

    public static void method370(char input[]) {
        boolean not_uc_char = true;
        for (int idx = 0; idx < input.length; idx++) {
            char current = input[idx];
            if (is_lc_uc_char(current)) {
                if (not_uc_char) {
                    if (is_lc_char(current))
                        not_uc_char = false;
                } else if (is_uc_char(current))
                    input[idx] = (char) ((current + 97) - 65);// 97=a - 65 = SPACE
            } else {
                not_uc_char = true;
            }
        }

    }

    public static void apply_badwords_filter(char input[]) {
        for (int i = 0; i < 2; i++) {
            for (int j = wordlist_bad.length - 1; j >= 0; j--)
                apply_word_filter(input, wordlist_bad[j], dunno_bad[j]);

        }

    }

    public static void apply_host_filter(char input[]) {
        for (int i = wordlist_host.length - 1; i >= 0; i--)
            apply_word_filter(input, wordlist_host[i], dunno_host[i]);

    }

    public static void apply_dot_slash_filter(char input[]) {
        char input1[] = input.clone();
        char dot[] = {
                'd', 'o', 't'
        };
        apply_word_filter(input1, dot, null);
        char input2[] = input.clone();
        char slash[] = {
                's', 'l', 'a', 's', 'h'
        };
        apply_word_filter(input2, slash, null);
        for (int i = 0; i < wordlist_tld.length; i++)
            apply_tld_filter(input, input1, input2, wordlist_tld[i], dunno_tld[i]);

    }

    public static void apply_tld_filter(char input[], char input1[], char input2[], char wordlist[], int hash_maybe) {
        if (wordlist.length > input.length)
            return;
        for (int idx = 0; idx <= input.length - wordlist.length; idx++) {
            int idx2 = idx;
            int l = 0;
            while (idx2 < input.length) {
                int i1 = 0;
                char current = input[idx2];
                char next = '\0';
                if (idx2 + 1 < input.length)
                    next = input[idx2 + 1];
                if (l < wordlist.length && (i1 = compare_letters_numbers(wordlist[l], current, next)) > 0) {
                    idx2 += i1;
                    l++;
                    continue;
                }
                if (l == 0)
                    break;
                if ((i1 = compare_letters_numbers(wordlist[l - 1], current, next)) > 0) {
                    idx2 += i1;
                    continue;
                }
                if (l >= wordlist.length || !is_not_letter_or_num(current))
                    break;
                idx2++;
            }
            if (l >= wordlist.length) {
                boolean flag = false;
                int j1 = method375(input, input1, idx);
                int k1 = method376(input, input2, idx2 - 1);
                if (DEBUG_TLD)
                    System.out.println("Potential tld: " + wordlist + " at char " + idx + " (type=" + hash_maybe + ", startmatch=" + j1 + ", endmatch=" + k1 + ")");
                if (hash_maybe == 1 && j1 > 0 && k1 > 0)
                    flag = true;
                if (hash_maybe == 2 && (j1 > 2 && k1 > 0 || j1 > 0 && k1 > 2))
                    flag = true;
                if (hash_maybe == 3 && j1 > 0 && k1 > 2)
                    flag = true;
                boolean _tmp = hash_maybe == 3 && j1 > 2 && k1 > 0;
                if (flag) {
                    if (DEBUG_TLD)
                        System.out.println("Filtered tld: " + wordlist + " at char " + idx);
                    int l1 = idx;
                    int i2 = idx2 - 1;
                    if (j1 > 2) {
                        if (j1 == 4) {
                            boolean flag1 = false;
                            for (int k2 = l1 - 1; k2 >= 0; k2--)
                                if (flag1) {
                                    if (input1[k2] != '*')
                                        break;
                                    l1 = k2;
                                } else if (input1[k2] == '*') {
                                    l1 = k2;
                                    flag1 = true;
                                }

                        }
                        boolean flag2 = false;
                        for (int l2 = l1 - 1; l2 >= 0; l2--)
                            if (flag2) {
                                if (is_not_letter_or_num(input[l2]))
                                    break;
                                l1 = l2;
                            } else if (!is_not_letter_or_num(input[l2])) {
                                flag2 = true;
                                l1 = l2;
                            }

                    }
                    if (k1 > 2) {
                        if (k1 == 4) {
                            boolean flag3 = false;
                            for (int i3 = i2 + 1; i3 < input.length; i3++)
                                if (flag3) {
                                    if (input2[i3] != '*')
                                        break;
                                    i2 = i3;
                                } else if (input2[i3] == '*') {
                                    i2 = i3;
                                    flag3 = true;
                                }

                        }
                        boolean flag4 = false;
                        for (int j3 = i2 + 1; j3 < input.length; j3++)
                            if (flag4) {
                                if (is_not_letter_or_num(input[j3]))
                                    break;
                                i2 = j3;
                            } else if (!is_not_letter_or_num(input[j3])) {
                                flag4 = true;
                                i2 = j3;
                            }

                    }
                    for (int j2 = l1; j2 <= i2; j2++)
                        input[j2] = '*';

                }
            }
        }

    }

    public static int method375(char input[], char input1[], int i) {
        if (i == 0)
            return 2;
        for (int j = i - 1; j >= 0; j--) {
            if (!is_not_letter_or_num(input[j]))
                break;
            if (input[j] == ',' || input[j] == '.')
                return 3;
        }

        int filtered = 0;
        for (int l = i - 1; l >= 0; l--) {
            if (!is_not_letter_or_num(input1[l]))
                break;
            if (input1[l] == '*')
                filtered++;
        }

        if (filtered >= 3)
            return 4;
        return !is_not_letter_or_num(input[i - 1]) ? 0 : 1;
    }

    public static int method376(char ac[], char ac1[], int i) {
        if (i + 1 == ac.length)
            return 2;
        for (int j = i + 1; j < ac.length; j++) {
            if (!is_not_letter_or_num(ac[j]))
                break;
            if (ac[j] == '\\' || ac[j] == '/')
                return 3;
        }

        int k = 0;
        for (int l = i + 1; l < ac.length; l++) {
            if (!is_not_letter_or_num(ac1[l]))
                break;
            if (ac1[l] == '*')
                k++;
        }

        if (k >= 5)
            return 4;
        return !is_not_letter_or_num(ac[i + 1]) ? 0 : 1;
    }

    public static void apply_word_filter(char ac[], char ac1[], byte abyte0[][]) {
        if (ac1.length > ac.length)
            return;
        for (int i = 0; i <= ac.length - ac1.length; i++) {
            int j = i;
            int k = 0;
            boolean flag = false;
            while (j < ac.length) {
                int l = 0;
                char c = ac[j];
                char c2 = '\0';
                if (j + 1 < ac.length)
                    c2 = ac[j + 1];
                if (k < ac1.length && (l = compare_letters_symbols(ac1[k], c, c2)) > 0) {
                    j += l;
                    k++;
                    continue;
                }
                if (k == 0)
                    break;
                if ((l = compare_letters_symbols(ac1[k - 1], c, c2)) > 0) {
                    j += l;
                    continue;
                }
                if (k >= ac1.length || !is_not_letter(c))
                    break;
                if (is_not_letter_or_num(c) && c != '\'')
                    flag = true;
                j++;
            }
            if (k >= ac1.length) {
                boolean flag1 = true;
                if (DEBUG_TLD)
                    System.out.println("Potential word: " + ac1 + " at char " + i);
                if (!flag) {
                    char c1 = ' ';
                    if (i - 1 >= 0)
                        c1 = ac[i - 1];
                    char c3 = ' ';
                    if (j < ac.length)
                        c3 = ac[j];
                    byte byte0 = encode_char(c1);
                    byte byte1 = encode_char(c3);
                    if (abyte0 != null && method378(abyte0, byte0, byte1))
                        flag1 = false;
                } else {
                    boolean flag2 = false;
                    boolean flag3 = false;
                    if (i - 1 < 0 || is_not_letter_or_num(ac[i - 1]) && ac[i - 1] != '\'')
                        flag2 = true;
                    if (j >= ac.length || is_not_letter_or_num(ac[j]) && ac[j] != '\'')
                        flag3 = true;
                    if (!flag2 || !flag3) {
                        boolean flag4 = false;
                        int j1 = i - 2;
                        if (flag2)
                            j1 = i;
                        for (; !flag4 && j1 < j; j1++)
                            if (j1 >= 0 && (!is_not_letter_or_num(ac[j1]) || ac[j1] == '\'')) {
                                char ac2[] = new char[3];
                                int k1;
                                for (k1 = 0; k1 < 3; k1++) {
                                    if (j1 + k1 >= ac.length || is_not_letter_or_num(ac[j1 + k1]) && ac[j1 + k1] != '\'')
                                        break;
                                    ac2[k1] = ac[j1 + k1];
                                }

                                boolean flag5 = true;
                                if (k1 == 0)
                                    flag5 = false;
                                if (k1 < 3 && j1 - 1 >= 0 && (!is_not_letter_or_num(ac[j1 - 1]) || ac[j1 - 1] == '\''))
                                    flag5 = false;
                                if (flag5 && !method391(ac2))
                                    flag4 = true;
                            }

                        if (!flag4)
                            flag1 = false;
                    }
                }
                if (flag1) {
                    if (DEBUG_WORD)
                        System.out.println("Filtered word: " + ac1 + " at char " + i);
                    for (int i1 = i; i1 < j; i1++)
                        ac[i1] = '*';

                }
            }
        }

    }

    public static boolean method378(byte abyte0[][], byte byte0, byte byte1) {
        int i = 0;
        if (abyte0[i][0] == byte0 && abyte0[i][1] == byte1)
            return true;
        int j = abyte0.length - 1;
        if (abyte0[j][0] == byte0 && abyte0[j][1] == byte1)
            return true;
        do {
            int k = (i + j) / 2;
            if (abyte0[k][0] == byte0 && abyte0[k][1] == byte1)
                return true;
            if (byte0 < abyte0[k][0] || byte0 == abyte0[k][0] && byte1 < abyte0[k][1])
                j = k;
            else
                i = k;
        } while (i != j && i + 1 != j);
        return false;
    }

    public static int compare_letters_numbers(char c, char c1, char c2) {
        if (c == c1)
            return 1;
        if (c == 'e' && c1 == '3')
            return 1;
        if (c == 't' && (c1 == '7' || c1 == '+'))
            return 1;
        if (c == 'a' && (c1 == '4' || c1 == '@'))
            return 1;
        if (c == 'o' && c1 == '0')
            return 1;
        if (c == 'i' && c1 == '1')
            return 1;
        if (c == 's' && c1 == '5')
            return 1;
        if (c == 'f' && c1 == 'p' && c2 == 'h')
            return 2;
        return c != 'g' || c1 != '9' ? 0 : 1;
    }

    public static int compare_letters_symbols(char c, char c1, char c2) {
        if (c == '*')
            return 0;
        if (c == c1)
            return 1;
        if (c >= 'a' && c <= 'z') {
            if (c == 'e')
                return c1 != '3' ? 0 : 1;
            if (c == 't')
                return c1 != '7' ? 0 : 1;
            if (c == 'a')
                return c1 != '4' && c1 != '@' ? 0 : 1;
            if (c == 'o') {
                if (c1 == '0' || c1 == '*')
                    return 1;
                return c1 != '(' || c2 != ')' ? 0 : 2;
            }
            if (c == 'i')
                return c1 != 'y' && c1 != 'l' && c1 != 'j' && c1 != '1' && c1 != '!' && c1 != ':' && c1 != ';' ? 0 : 1;
            if (c == 'n')
                return 0;
            if (c == 's')
                return c1 != '5' && c1 != 'z' && c1 != '$' ? 0 : 1;
            if (c == 'r')
                return 0;
            if (c == 'h')
                return 0;
            if (c == 'l')
                return c1 != '1' ? 0 : 1;
            if (c == 'd')
                return 0;
            if (c == 'c')
                return c1 != '(' ? 0 : 1;
            if (c == 'u')
                return c1 != 'v' ? 0 : 1;
            if (c == 'm')
                return 0;
            if (c == 'f')
                return c1 != 'p' || c2 != 'h' ? 0 : 2;
            if (c == 'p')
                return 0;
            if (c == 'g')
                return c1 != '9' && c1 != '6' ? 0 : 1;
            if (c == 'w')
                return c1 != 'v' || c2 != 'v' ? 0 : 2;
            if (c == 'y')
                return 0;
            if (c == 'b')
                return c1 != '1' || c2 != '3' ? 0 : 2;
            if (c == 'v')
                return 0;
            if (c == 'k')
                return 0;
            if (c == 'x')
                return c1 != ')' || c2 != '(' ? 0 : 2;
            if (c == 'j')
                return 0;
            if (c == 'q')
                return 0;
            if (c == 'z')
                return 0;
        }
        if (c >= '0' && c <= '9') {
            if (c == '0') {
                if (c1 == 'o' || c1 == 'O')
                    return 1;
                return c1 != '(' || c2 != ')' ? 0 : 2;
            }
            if (c == '1')
                return c1 != 'l' ? 0 : 1;
            if (c == '2')
                return 0;
            if (c == '3')
                return 0;
            if (c == '4')
                return 0;
            if (c == '5')
                return 0;
            if (c == '6')
                return 0;
            if (c == '7')
                return 0;
            if (c == '8')
                return 0;
            if (c == '9')
                return 0;
        }
        if (c == '-')
            return 0;
        if (c == ',')
            return c1 != '.' ? 0 : 1;
        if (c == '.')
            return c1 != ',' ? 0 : 1;
        if (c == '(')
            return 0;
        if (c == ')')
            return 0;
        if (c == '!')
            return c1 != 'i' ? 0 : 1;
        if (c == '\'')
            return 0;
        if (DEBUG_WORD)
            System.out.println("Letter=" + c + " not matched");
        return 0;
    }

    public static byte encode_char(char c) {// returns the "id" of a character
        if (c >= 'a' && c <= 'z')
            return (byte) ((c - 97) + 1);
        if (c == '\'')
            return 28;
        if (c >= '0' && c <= '9')
            return (byte) ((c - 48) + 29);
        else
            return 27;
    }

    public static void method382(char input[]) {
        int i = 0;
        int j = 0;
        int k = 0;
        int l = 0;
        while ((i = idx_of_num(input, j)) != -1) {
            boolean flag = false;
            for (int i1 = j; i1 >= 0 && i1 < i && !flag; i1++)
                if (!is_not_letter_or_num(input[i1]) && !is_not_letter(input[i1]))
                    flag = true;

            if (flag)
                k = 0;
            if (k == 0)
                l = i;
            j = idx_of_not_num(input, i);
            int j1 = 0;
            for (int k1 = i; k1 < j; k1++)
                j1 = (j1 * 10 + input[k1]) - 48;

            if (j1 > 255 || j - i > 8)
                k = 0;
            else
                k++;
            if (k == 4) {
                for (int l1 = l; l1 < j; l1++)
                    input[l1] = '*';

                k = 0;
            }
        }
    }

    public static int idx_of_num(char input[], int start) {
        for (int idx = start; idx < input.length && idx >= 0; idx++)
            if (input[idx] >= '0' && input[idx] <= '9')
                return idx;

        return -1;
    }

    public static int idx_of_not_num(char input[], int start) {
        for (int idx = start; idx < input.length && idx >= 0; idx++)
            if (input[idx] < '0' || input[idx] > '9')
                return idx;

        return input.length;
    }

    public static boolean is_not_letter_or_num(char c) {
        return !is_lc_uc_char(c) && !is_num_char(c);
    }

    public static boolean is_not_letter(char c) {
        if (c < 'a' || c > 'z')
            return true;
        return c == 'v' || c == 'x' || c == 'j' || c == 'q' || c == 'z';
    }

    public static boolean is_lc_uc_char(char c) {
        return c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z';
    }

    public static boolean is_num_char(char c) {
        return c >= '0' && c <= '9';
    }

    public static boolean is_lc_char(char c) {
        return c >= 'a' && c <= 'z';
    }

    public static boolean is_uc_char(char c) {
        return c >= 'A' && c <= 'Z';
    }

    public static boolean method391(char input[]) {
        boolean not_num = true;
        for (int i = 0; i < input.length; i++)
            if (!is_num_char(input[i]) && input[i] != 0)
                not_num = false;

        if (not_num)
            return true;
        int wordhash = word2hash(input);
        int k = 0;
        int fragment_len = hash_fragments.length - 1;
        if (wordhash == hash_fragments[k] || wordhash == hash_fragments[fragment_len])
            return true;
        do {
            int i1 = (k + fragment_len) / 2;
            if (wordhash == hash_fragments[i1])
                return true;
            if (wordhash < hash_fragments[i1])
                fragment_len = i1;
            else
                k = i1;
        } while (k != fragment_len && k + 1 != fragment_len);
        return false;
    }

    public static int word2hash(char ac[]) {
        if (ac.length > 6)
            return 0;
        int i = 0;
        for (int j = 0; j < ac.length; j++) {
            char c = ac[ac.length - j - 1];
            if (c >= 'a' && c <= 'z')
                i = i * 38 + ((c - 97) + 1);
            else if (c == '\'')
                i = i * 38 + 27;
            else if (c >= '0' && c <= '9')
                i = i * 38 + ((c - 48) + 28);
            else if (c != 0) {
                if (DEBUG_WORD)
                    System.out.println("word2hash failed on " + new String(ac));
                return 0;
            }
        }

        return i;
    }

    static boolean DEBUG_TLD;
    static boolean DEBUG_WORD;
    static boolean aBoolean549 = true;
    //static int unused = 3;
    static int hash_fragments[];
    static char wordlist_bad[][];
    static byte dunno_bad[][][];
    static char wordlist_host[][];
    static byte dunno_host[][][];
    static char wordlist_tld[][];
    static int dunno_tld[];
    static String wordlist_ignore[] = {
            "cook", "cook's", "cooks", "seeks", "sheet"
    };

}
