package main.player;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;

public class Hunter {
	
	public static final String COMPASS_LORE = "Tracking Compass";
	
	private Player p;
	
	public Hunter(Player p) {
		this.p = p;
		p.getInventory().addItem(trackingCompass(null));
	}
	
	/** Returns a compass tracking the input Hunter. */
	public ItemStack trackingCompass(Runner r) {
		ItemStack is = new ItemStack(Material.COMPASS);
		CompassMeta im = (CompassMeta) is.getItemMeta();
		
		if (r == null) {
			im.setDisplayName("Pointing to No One");
		} else {
			im.setDisplayName(String.format("Pointing to %s", r.getPlayer().getDisplayName()));
			im.setLodestone(r.query(p.getWorld().getName()));
			im.setLodestoneTracked(true);
		}
		
		List<String> lore = new ArrayList<String>();
		lore.add(COMPASS_LORE);
		
		return is;
	}
	
	public Player getPlayer() {
		return p;
	}

}
