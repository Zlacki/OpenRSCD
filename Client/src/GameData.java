public class GameData {

    public static int getModelIndex(String s) {
        if (s.equalsIgnoreCase("na"))
            return 0;
        for (int i = 0; i < modelCount; i++)
            if (modelName[i].equalsIgnoreCase(s))
                return i;

        modelName[modelCount++] = s;
        return modelCount - 1;
    }

    public static int getUnsignedByte() {
        int i = data_integer[offset] & 0xff;
        offset++;
        return i;
    }

    public static int getUnsignedShort() {
        int i = Utility.getUnsignedShort(data_integer, offset);
        offset += 2;
        return i;
    }

    public static int getUnsignedInt() {
        int i = Utility.getUnsignedInt(data_integer, offset);
        offset += 4;
        if (i > 0x5f5e0ff)
            i = 0x5f5e0ff - i;
        return i;
    }

    public static String getString() {
        String s;
        for (s = ""; data_string[string_offset] != 0; s = s + (char) data_string[string_offset++]) ;
        string_offset++;
        return s;
    }

    public static void loadData(byte buffer[], boolean isMembers) {
        data_string = Utility.loadData("string.dat", 0, buffer);
        string_offset = 0;
        data_integer = Utility.loadData("integer.dat", 0, buffer);
        offset = 0;

        int i;

        item_count = getUnsignedShort();
        item_name = new String[item_count];
        item_description = new String[item_count];
        item_command = new String[item_count];
        item_picture = new int[item_count];
        item_base_price = new int[item_count];
        item_stackable = new int[item_count];
        anIntArray125 = new int[item_count];
        anIntArray126 = new int[item_count];
        item_mask = new int[item_count];
        item_special = new int[item_count];
        item_members = new int[item_count];
        for (i = 0; i < item_count; i++)
            item_name[i] = getString();

        for (i = 0; i < item_count; i++)
            item_description[i] = getString();

        for (i = 0; i < item_count; i++)
            item_command[i] = getString();

        for (i = 0; i < item_count; i++) {
            item_picture[i] = getUnsignedShort();
            if (item_picture[i] + 1 > item_sprite_count)
                item_sprite_count = item_picture[i] + 1;
        }

        for (i = 0; i < item_count; i++)
            item_base_price[i] = getUnsignedInt();

        for (i = 0; i < item_count; i++)
            item_stackable[i] = getUnsignedByte();

        for (i = 0; i < item_count; i++)
            anIntArray125[i] = getUnsignedByte();

        for (i = 0; i < item_count; i++)
            anIntArray126[i] = getUnsignedShort();

        for (i = 0; i < item_count; i++)
            item_mask[i] = getUnsignedInt();

        for (i = 0; i < item_count; i++)
            item_special[i] = getUnsignedByte();

        for (i = 0; i < item_count; i++)
            item_members[i] = getUnsignedByte();

        for (i = 0; i < item_count; i++)
            if (!isMembers && item_members[i] == 1) {
                item_name[i] = "Members object";
                item_description[i] = "You need to be a member to use this object";
                item_base_price[i] = 0;
                item_command[i] = "";
                anIntArray125[0] = 0;
                anIntArray126[i] = 0;
                item_special[i] = 1;
            }

        npc_count = getUnsignedShort();
        npc_name = new String[npc_count];
        npc_description = new String[npc_count];
        npc_command = new String[npc_count];
        npc_attack = new int[npc_count];
        npc_strength = new int[npc_count];
        npc_hits = new int[npc_count];
        npc_defense = new int[npc_count];
        npc_attackable = new int[npc_count];
        npc_sprite = new int[npc_count][12];
        npc_colour_hair = new int[npc_count];
        npc_colour_top = new int[npc_count];
        npc_color_bottom = new int[npc_count];
        npc_colour_skin = new int[npc_count];
        npc_something_1 = new int[npc_count];
        npc_something_2 = new int[npc_count];
        npc_walk_model = new int[npc_count];
        npc_combat_model = new int[npc_count];
        npc_combat_animation = new int[npc_count];
        for (i = 0; i < npc_count; i++)
            npc_name[i] = getString();

        for (i = 0; i < npc_count; i++)
            npc_description[i] = getString();

        for (i = 0; i < npc_count; i++)
            npc_attack[i] = getUnsignedByte();

        for (i = 0; i < npc_count; i++)
            npc_strength[i] = getUnsignedByte();

        for (i = 0; i < npc_count; i++)
            npc_hits[i] = getUnsignedByte();

        for (i = 0; i < npc_count; i++)
            npc_defense[i] = getUnsignedByte();

        for (i = 0; i < npc_count; i++)
            npc_attackable[i] = getUnsignedByte();

        for (i = 0; i < npc_count; i++) {
            for (int i5 = 0; i5 < 12; i5++) {
                npc_sprite[i][i5] = getUnsignedByte();
                if (npc_sprite[i][i5] == 255)
                    npc_sprite[i][i5] = -1;
            }

        }

        for (i = 0; i < npc_count; i++)
            npc_colour_hair[i] = getUnsignedInt();

        for (i = 0; i < npc_count; i++)
            npc_colour_top[i] = getUnsignedInt();

        for (i = 0; i < npc_count; i++)
            npc_color_bottom[i] = getUnsignedInt();

        for (i = 0; i < npc_count; i++)
            npc_colour_skin[i] = getUnsignedInt();

        for (i = 0; i < npc_count; i++)
            npc_something_1[i] = getUnsignedShort();

        for (i = 0; i < npc_count; i++)
            npc_something_2[i] = getUnsignedShort();

        for (i = 0; i < npc_count; i++)
            npc_walk_model[i] = getUnsignedByte();

        for (i = 0; i < npc_count; i++)
            npc_combat_model[i] = getUnsignedByte();

        for (i = 0; i < npc_count; i++)
            npc_combat_animation[i] = getUnsignedByte();

        for (i = 0; i < npc_count; i++)
            npc_command[i] = getString();

        texture_count = getUnsignedShort();
        texture_name = new String[texture_count];
        texture_subtype_name = new String[texture_count];
        for (i = 0; i < texture_count; i++)
            texture_name[i] = getString();

        for (i = 0; i < texture_count; i++)
            texture_subtype_name[i] = getString();

        animation_count = getUnsignedShort();
        animation_name = new String[animation_count];
        animation_character_colour = new int[animation_count];
        animation_something = new int[animation_count];
        animation_has_a = new int[animation_count];
        animation_has_f = new int[animation_count];
        animation_number = new int[animation_count];
        for (i = 0; i < animation_count; i++)
            animation_name[i] = getString();

        for (i = 0; i < animation_count; i++)
            animation_character_colour[i] = getUnsignedInt();

        for (i = 0; i < animation_count; i++)
            animation_something[i] = getUnsignedByte();

        for (i = 0; i < animation_count; i++)
            animation_has_a[i] = getUnsignedByte();

        for (i = 0; i < animation_count; i++)
            animation_has_f[i] = getUnsignedByte();

        for (i = 0; i < animation_count; i++)
            animation_number[i] = getUnsignedByte();

        object_count = getUnsignedShort();
        object_name = new String[object_count];
        object_description = new String[object_count];
        object_command1 = new String[object_count];
        object_command2 = new String[object_count];
        object_model_index = new int[object_count];
        object_width = new int[object_count];
        object_height = new int[object_count];
        object_type = new int[object_count];
        object_something = new int[object_count];
        for (i = 0; i < object_count; i++)
            object_name[i] = getString();

        for (i = 0; i < object_count; i++)
            object_description[i] = getString();

        for (i = 0; i < object_count; i++)
            object_command1[i] = getString();

        for (i = 0; i < object_count; i++)
            object_command2[i] = getString();

        for (i = 0; i < object_count; i++)
            object_model_index[i] = getModelIndex(getString());

        for (i = 0; i < object_count; i++)
            object_width[i] = getUnsignedByte();

        for (i = 0; i < object_count; i++)
            object_height[i] = getUnsignedByte();

        for (i = 0; i < object_count; i++)
            object_type[i] = getUnsignedByte();

        for (i = 0; i < object_count; i++)
            object_something[i] = getUnsignedByte();

        wall_object_count = getUnsignedShort();
        wall_object_name = new String[wall_object_count];
        wall_object_description = new String[wall_object_count];
        wall_object_command1 = new String[wall_object_count];
        wall_object_command2 = new String[wall_object_count];
        wall_object_something1 = new int[wall_object_count];
        wall_object_something2 = new int[wall_object_count];
        wall_object_something3 = new int[wall_object_count];
        wall_object_visible = new int[wall_object_count];
        wall_object_something5 = new int[wall_object_count];
        for (i = 0; i < wall_object_count; i++)
            wall_object_name[i] = getString();

        for (i = 0; i < wall_object_count; i++)
            wall_object_description[i] = getString();

        for (i = 0; i < wall_object_count; i++)
            wall_object_command1[i] = getString();

        for (i = 0; i < wall_object_count; i++)
            wall_object_command2[i] = getString();

        for (i = 0; i < wall_object_count; i++)
            wall_object_something1[i] = getUnsignedShort();

        for (i = 0; i < wall_object_count; i++)
            wall_object_something2[i] = getUnsignedInt();

        for (i = 0; i < wall_object_count; i++)
            wall_object_something3[i] = getUnsignedInt();

        for (i = 0; i < wall_object_count; i++)
            wall_object_visible[i] = getUnsignedByte();

        for (i = 0; i < wall_object_count; i++)
            wall_object_something5[i] = getUnsignedByte();

        anInt116 = getUnsignedShort();
        anIntArray101 = new int[anInt116];
        anIntArray102 = new int[anInt116];
        for (i = 0; i < anInt116; i++)
            anIntArray101[i] = getUnsignedByte();

        for (i = 0; i < anInt116; i++)
            anIntArray102[i] = getUnsignedByte();

        anInt88 = getUnsignedShort();
        anIntArray97 = new int[anInt88];
        anIntArray98 = new int[anInt88];
        anIntArray99 = new int[anInt88];
        for (i = 0; i < anInt88; i++)
            anIntArray97[i] = getUnsignedInt();

        for (i = 0; i < anInt88; i++)
            anIntArray98[i] = getUnsignedByte();

        for (i = 0; i < anInt88; i++)
            anIntArray99[i] = getUnsignedByte();

        projectile_sprite = getUnsignedShort();
        spell_count = getUnsignedShort();
        spell_name = new String[spell_count];
        spell_description = new String[spell_count];
        spell_level = new int[spell_count];
        spell_runes_required = new int[spell_count];
        spell_type = new int[spell_count];
        spell_runes_id = new int[spell_count][];
        spell_runes_count = new int[spell_count][];
        for (i = 0; i < spell_count; i++)
            spell_name[i] = getString();

        for (i = 0; i < spell_count; i++)
            spell_description[i] = getString();

        for (i = 0; i < spell_count; i++)
            spell_level[i] = getUnsignedByte();

        for (i = 0; i < spell_count; i++)
            spell_runes_required[i] = getUnsignedByte();

        for (i = 0; i < spell_count; i++)
            spell_type[i] = getUnsignedByte();

        for (i = 0; i < spell_count; i++) {
            int j = getUnsignedByte();
            spell_runes_id[i] = new int[j];
            for (int k = 0; k < j; k++)
                spell_runes_id[i][k] = getUnsignedShort();

        }

        for (i = 0; i < spell_count; i++) {
            int j = getUnsignedByte();
            spell_runes_count[i] = new int[j];
            for (int k = 0; k < j; k++)
                spell_runes_count[i][k] = getUnsignedByte();

        }

        prayer_count = getUnsignedShort();
        prayer_name = new String[prayer_count];
        prayer_description = new String[prayer_count];
        prayer_level = new int[prayer_count];
        prayer_drain = new int[prayer_count];
        for (i = 0; i < prayer_count; i++)
            prayer_name[i] = getString();

        for (i = 0; i < prayer_count; i++)
            prayer_description[i] = getString();

        for (i = 0; i < prayer_count; i++)
            prayer_level[i] = getUnsignedByte();

        for (i = 0; i < prayer_count; i++)
            prayer_drain[i] = getUnsignedByte();

        data_string = null;
        data_integer = null;
    }

    public static String modelName[] = new String[5000];
    public static String texture_name[];
    public static String texture_subtype_name[];
    public static int object_model_index[];
    public static int object_width[];
    public static int object_height[];
    public static int object_type[];
    public static int object_something[];
    public static int spell_count;
    public static int npc_something_1[];
    public static int npc_something_2[];
    public static int npc_sprite[][];
    public static int npc_attack[];
    public static int npc_strength[];
    public static int npc_hits[];
    public static int npc_defense[];
    public static int npc_attackable[];
    public static int spell_level[];
    public static int spell_runes_required[];
    public static int spell_type[];
    public static int spell_runes_id[][];
    public static int spell_runes_count[][];
    public static String aStringArray75[] = new String[5000];
    public static int item_count;
    public static int item_sprite_count;
    public static int npc_colour_hair[];
    public static int npc_colour_top[];
    public static int npc_color_bottom[];
    public static int npc_colour_skin[];
    public static int wall_object_something1[];
    public static int wall_object_something2[];
    public static int wall_object_something3[];
    public static int wall_object_visible[];
    public static int wall_object_something5[];
    public static String aStringArray87[] = new String[5000];
    public static int anInt88;
    public static int animation_character_colour[];
    public static int animation_something[];
    public static int animation_has_a[];
    public static int animation_has_f[];
    public static int animation_number[];
    public static int wall_object_count;
    public static int prayer_level[];
    public static int prayer_drain[];
    public static int anIntArray97[];
    public static int anIntArray98[];
    public static int anIntArray99[];
    public static int modelCount;
    public static int anIntArray101[];
    public static int anIntArray102[];
    public static int prayer_count;
    public static String item_name[];
    public static String item_description[];
    public static String item_command[];
    public static int projectile_sprite;
    public static int npc_count;
    public static String spell_name[];
    public static String spell_description[];
    public static int texture_count;
    public static String wall_object_name[];
    public static String wall_object_description[];
    public static String wall_object_command1[];
    public static String wall_object_command2[];
    public static int anInt116;
    public static int object_count;
    public static String npc_name[];
    public static String npc_description[];
    public static String npc_command[];
    public static String animation_name[];
    public static int item_picture[];
    public static int item_base_price[];
    public static int item_stackable[];
    public static int anIntArray125[];
    public static int anIntArray126[];
    public static int item_mask[];
    public static int item_special[];
    public static int item_members[];
    public static int animation_count;
    public static String prayer_name[];
    public static String prayer_description[];
    public static String object_name[];
    public static String object_description[];
    public static String object_command1[];
    public static String object_command2[];
    public static int npc_walk_model[];
    public static int npc_combat_model[];
    public static int npc_combat_animation[];
    static byte data_string[];
    static byte data_integer[];
    static int string_offset;
    static int offset;
    public static int anInt144;

}
