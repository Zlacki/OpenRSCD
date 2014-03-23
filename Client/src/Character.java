final class Character {

    Character() {
        waypoints_x = new int[10];
        waypoints_y = new int[10];
        equipped_item = new int[12];
        level = -1;
        aBoolean177 = false;
        anInt178 = -1;
    }

    public long hash;
    public String name;
    public int server_index;
    public int server_id;
    public int currentX;
    public int currentY;
    public int npc_id;
    public int stepCount;
    public int animation_current;
    public int animation_next;
    public int moving_step;
    public int waypoint_current;
    public int waypoints_x[];
    public int waypoints_y[];
    public int equipped_item[];
    public String message;
    public int message_timeout;
    public int bubble_id;
    public int bubble_timeout;
    public int damage_taken;
    public int health_current;
    public int health_max;
    public int combat_timer;
    public int level;
    public int colour_hair;
    public int colour_top;
    public int colour_bottom;
    public int colour_skin;
    public int incoming_projectile_sprite;
    public int attacking_player_server_index;
    public int attacking_npc_server_index;
    public int projectile_range;
    public boolean aBoolean177;
    public int anInt178;
    public int bubble_visible;
}
