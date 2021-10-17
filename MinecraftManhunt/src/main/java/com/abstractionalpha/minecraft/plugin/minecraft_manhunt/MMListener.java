package com.abstractionalpha.minecraft.plugin.minecraft_manhunt;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class MMListener implements Listener {
	
	@EventHandler
	public void onHunterInteract(PlayerInteractEvent pie) {
		// TODO add interact code for hunter compass
	}
	
	@EventHandler
	public void onHunterDeath(PlayerDeathEvent pde) {
		// TODO add hunter death code
	}
	
	@EventHandler
	public void onRunnerDeath(PlayerDeathEvent pde) {
		// TODO add runner death code
	}
	
	@EventHandler
	public void onHunterJoin(PlayerJoinEvent pje) {
		// TODO add hunter join code
	}
	
	@EventHandler
	public void onRunnerJoin(PlayerJoinEvent pje) {
		// TODO add runner join code
	}
	
	@EventHandler
	public void onHunterQuit(PlayerQuitEvent pqe) {
		// TODO add hunter quit code
	}
	
	@EventHandler
	public void onRunnerQuit(PlayerQuitEvent pqe) {
		// TODO add runner quit code
	}

}
