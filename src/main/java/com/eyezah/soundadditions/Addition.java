package com.eyezah.soundadditions;

import org.bukkit.SoundCategory;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Random;

public class Addition {
	public Addition(List<String> sounds, SoundCategory category, ConfigurationSection conditions, int minDelay, int bound) {
		this.sounds = sounds;
		if (conditions == null) conditions = new MemoryConfiguration();
		this.yMax = conditions.getInt("yMax", 100000000);
		this.yMin = conditions.getInt("yMin", -100000000);
		this.minDelay = minDelay;
		this.bound = bound;
		this.category = category;
	}

	public OptionalInt scheduleTime = OptionalInt.empty();

	private final List<String> sounds;
	private final int yMax;
	private final int yMin;
	public final int minDelay;
	public final int bound;
	public final SoundCategory category;

	public String getSound(Random random) {
		return this.sounds.get(random.nextInt(this.sounds.size()));
	}

	public boolean testPlayer(Player player) {
		int y = player.getLocation().getBlockY();
		return this.yMin <= y && y <= this.yMax;
	}
}
