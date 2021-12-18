package com.eyezah.soundadditions;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.entity.Rabbit;

import java.util.List;
import java.util.Random;

public class Addition {
	public Addition(List<String> sounds, ConfigurationSection conditions, int minDelay, int bound) {
		this.sounds = sounds;
		this.yMax = conditions.getInt("yMax", 100000000);
		this.yMin = conditions.getInt("yMin", -100000000);
		this.minDelay = minDelay;
		this.bound = bound;
	}

	private final List<String> sounds;
	private final int yMax;
	private final int yMin;
	public final int minDelay;
	public final int bound;

	public String getSound(Random random) {
		return this.sounds.get(random.nextInt(this.sounds.size()));
	}

	public boolean testPlayer(Player player) {
		int y = player.getLocation().getBlockY();
		return this.yMin <= y && y <= this.yMax;
	}
}
