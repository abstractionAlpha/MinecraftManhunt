package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import main.player.Hunter;
import main.player.Runner;

public class Manhunt {
	
	private Map<Player, Hunter> hunters;
	
	private Map<Player, Runner> runners;
	
	private boolean running;
	
	private Manhunt(ArrayList<Player> hunters, ArrayList<Player> runners) {
		this.hunters = new HashMap<Player, Hunter>();
		for (Player p : hunters) {
			this.hunters.put(p, new Hunter(p));
		}
		
		this.runners = new HashMap<Player, Runner>();
		for (Player p : runners) {
			this.runners.put(p, new Runner(p));
		}
		
		running = true;
	}
	
	public void onDeath(Player p) {
		Runner runner = runners.get(p);
		if (runner != null) {
			runner.hasDied();
			for (Hunter h : hunters.values()) {
				h.getPlayer().sendMessage(ChatColor.GREEN + String.format("Runner %d has died!", p.getName()));
			}
			for (Runner r : runners.values()) {
				r.getPlayer().sendMessage(ChatColor.RED + String.format("Runner %d has died.", p.getName()));
			}
		}
		for (Runner r : runners.values()) {
			if (r.isAlive()) {
				return;
			}
		}
		terminate();
	}
	
	public boolean isRunning() {
		return running;
	}
	
	public void terminate() {
		running = false;
		for (Runner r : runners.values()) {
			if (r.getPlayer().getGameMode() == GameMode.SPECTATOR) {
				r.getPlayer().setGameMode(GameMode.SURVIVAL);
			}
		}
	}
	
	public static Manhunt instance(ArrayList<Player> hunters, ArrayList<Player> runners) {
		return new Manhunt(hunters, runners);
	}

}
