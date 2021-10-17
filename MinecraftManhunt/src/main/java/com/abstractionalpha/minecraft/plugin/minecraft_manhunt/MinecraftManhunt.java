package com.abstractionalpha.minecraft.plugin.minecraft_manhunt;

import org.bukkit.plugin.java.JavaPlugin;

public class MinecraftManhunt extends JavaPlugin {
	
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new MMListener(), this);
		getCommand("manhunt").setExecutor(new MMCommandExecutor(this));
	}
	
	@Override
	public void onDisable() {
		// TODO insert shutdown logic
	}

}
