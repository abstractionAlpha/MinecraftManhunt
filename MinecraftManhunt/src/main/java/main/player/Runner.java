package main.player;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Runner {
	
	private Player p;
	
	private boolean alive;
	
	private HashMap<String, Location> locations;
	
	public Runner(Player p) {
		this.p = p;
		locations = new HashMap<String, Location>();
		alive = true;
		storeLocation();
	}
	
	public void storeLocation() {
		Location loc = p.getLocation();
		locations.put(loc.getWorld().getName(), loc);
	}
	
	public Location query(String worldName) {
		return locations.get(worldName);
	}
	
	public Player getPlayer() {
		return p;
	}
	
	public boolean isAlive() {
		return alive;
	}
	
	public void hasDied() {
		alive = false;
	}

}
