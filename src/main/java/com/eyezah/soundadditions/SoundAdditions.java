package com.eyezah.soundadditions;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.Random;

public final class SoundAdditions extends JavaPlugin implements Listener {
	private final Map<String, List<Addition>> sounds = new HashMap<>();

	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);

		this.saveDefaultConfig();
		FileConfiguration config = this.getConfig();

		for (String worldName : config.getKeys(false)) {
			ConfigurationSection worldConfig = config.getConfigurationSection(worldName);
			List<Addition> instances = new ArrayList<>();

			for (String k_ : worldConfig.getKeys(false)) {
				ConfigurationSection repeatingInstance = worldConfig.getConfigurationSection(k_);

				int min = repeatingInstance.getInt("delayMin", 5);
				int max = repeatingInstance.getInt("delayMax", 20);

				// no
				if (min > max) {
					throw new IllegalArgumentException("delayMin cannot be greater than delayMax (in addition " + k_ + "for world " + worldName + ")");
				}

				instances.add(new Addition(repeatingInstance.getStringList("sounds"), repeatingInstance.getConfigurationSection("conditions"), min, max - min + 1));
			}

			this.sounds.put(worldName, instances);
		}
	}

	@Override
	public void onDisable() {
		HandlerList.unregisterAll((Plugin) this);
	}

	@EventHandler
	public void onTick(ServerTickEndEvent event) {
		int time = event.getTickNumber();

		for (World world : Bukkit.getWorlds()) {
			String name = world.getName();

			if (this.sounds.containsKey(name)) {
				// sound stuff
				List<Addition> additions = this.sounds.get(name);

				for (Addition addition : additions) {
					if (addition.scheduleTime.isEmpty() || time - addition.scheduleTime.getAsInt() >= 0) { // in case of overflow
						// reschedule
						addition.scheduleTime = OptionalInt.of(time + addition.minDelay + RANDOM.nextInt(addition.bound));

						// play sound
						String sound = addition.getSound(RANDOM);

						for (Player player : world.getPlayers()) {
							if (addition.testPlayer(player)) {
								player.playSound(player.getLocation(), sound, 1.0f, 1.0f);
							}
						}
					}
				}
			}
		}
	}

	private static final Random RANDOM = new Random();
}
