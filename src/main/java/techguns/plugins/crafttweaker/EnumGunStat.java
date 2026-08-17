package techguns.plugins.crafttweaker;

public enum EnumGunStat {
	/**
	 * The damage dealt close range
	 */
	DAMAGE,
	/**
	 * the damage dealt at far range
	 */
	DAMAGE_MIN,
	/**
	 * distance (in blocks) when damage starts to drop, between START and END damage is linear interpolated between DAMAGE and DAMAGE_MIN
	 */
	DAMAGE_DROP_START,
	/**
	 * distance (in blocks) when damage drop ends, targets farther away will take DAMAGE_MIN damage,
	 */
	DAMAGE_DROP_END,
	/**
	 * how fast the projectile flies in blocks per tick
	 */
	BULLET_SPEED,
	/**
	 * how far the projectile can fly before it gets deleted
	 */
	BULLET_DISTANCE,
	/**
	 * Gravity strength of the projectile
	 */
	GRAVITY,
	/**
	 * Mining speed, only has an effect on tools
	 */
	MINING_SPEED,
	/**
	 * Minimum time between shots in ticks
	 */
	MIN_FIRE_TIME,
	/**
	 * Random spread for regular single-projectile shots
	 */
	ACCURACY,
	/**
	 * Random spread for shotgun/burst projectiles
	 */
	SPREAD,
	PENETRATION,
	CLIP_SIZE,
	AMMO_COUNT,
	BULLET_COUNT,
	RELOAD_TIME;
	
	public static EnumGunStat parseFromString(String s) {
		String normalized = s.replace("_", "");
		for (EnumGunStat e : EnumGunStat.values()) {
			if (e.name().equalsIgnoreCase(s) || e.name().replace("_", "").equalsIgnoreCase(normalized)) {
				return e;
			}
		}
		return null;
	}

}
