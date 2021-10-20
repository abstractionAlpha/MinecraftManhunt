package com.abstractionalpha.minecraft.plugin.minecraft_manhunt.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class MMGUIMain implements Listener {
	
	protected static final int ROW_LENGTH = 9;
	
	private ArrayList<Player> hunters;
	
	private ArrayList<Player> runners;
	
	private JavaPlugin plugin;
	
	public MMGUIMain(JavaPlugin plugin, Player p) {
		hunters = new ArrayList<Player>();
		runners = new ArrayList<Player>();
		for (Player online : plugin.getServer().getOnlinePlayers()) {
			hunters.add(online);
		}
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		access(p, true);
	}
	
	private void access(Player p, boolean safe) {
		// Initialize GUI
		Inventory inv = Bukkit.createInventory(p, 3 * ROW_LENGTH, "Manhunt");
		
		// Create items
		
		ArrayList<String> lore;
		
		// Create runners button
		ItemStack isRunners = new ItemStack(Material.PLAYER_HEAD, 1);
		SkullMeta smRunners = (SkullMeta) isRunners.getItemMeta();
		if (runners.size() != 0) {
			smRunners.setOwningPlayer(runners.get(0));
		}
		smRunners.setDisplayName("Runners");
		lore = new ArrayList<String>();
		lore.add("Open Runners Window");
		smRunners.setLore(lore);
		isRunners.setItemMeta(smRunners);
		
		// Create hunters button
		ItemStack isHunters = new ItemStack(Material.PLAYER_HEAD, 1);
		SkullMeta smHunters = (SkullMeta) isHunters.getItemMeta();
		if (hunters.size() != 0) {
			smHunters.setOwningPlayer(hunters.get(0));
		}
		smHunters.setDisplayName("Hunters");
		lore = new ArrayList<String>();
		lore.add("Open Hunters Window");
		smHunters.setLore(lore);
		isHunters.setItemMeta(smHunters);
		
		// Create the functional object
		ItemStack functional;
		if (hunters.size() == 0 || runners.size() == 0) {
			functional = new ItemStack(Material.RED_TERRACOTTA, 1);
			ItemMeta functionalMeta = functional.getItemMeta();
			functionalMeta.setDisplayName("Cannot Start");
			lore = new ArrayList<String>();
			lore.add(
					hunters.size() == 0 ?
							"Add to Hunters" :
						    "Add to Runners"
			);
			functionalMeta.setLore(lore);
			functional.setItemMeta(functionalMeta);
		} else if (safe) {
			functional = new ItemStack(Material.LIME_TERRACOTTA, 1);
			ItemMeta functionalMeta = functional.getItemMeta();
			functionalMeta.setDisplayName("Confirm");
			lore = new ArrayList<String>();
			lore.add("Click to verify your selections");
			functionalMeta.setLore(lore);
			functional.setItemMeta(functionalMeta);
		} else {
			functional = new ItemStack(Material.BLUE_TERRACOTTA, 1);
			ItemMeta functionalMeta = functional.getItemMeta();
			functionalMeta.setDisplayName("Start");
			lore = new ArrayList<String>();
			lore.add("Click to start");
			functionalMeta.setLore(lore);
			functional.setItemMeta(functionalMeta);
		}
		
		// Add items
		inv.setItem(ROW_LENGTH + 1, isRunners);
		inv.setItem(ROW_LENGTH + 4, isHunters);
		inv.setItem(ROW_LENGTH + 7, functional);
		
		// Open GUI
		p.openInventory(inv);
	}
	
	private void inventoryClickSound(Player p, Location loc) {
		p.playSound(loc, Sound.BLOCK_AMETHYST_BLOCK_CHIME, 1f, 1f);
	}
	
	// Event Handlers
	
	// InventoryClickEvent for main GUI
	@EventHandler
	public void onInventoryClick(InventoryClickEvent ice) {
		// Get inventory and clicked item
		if (ice.getView().getTitle().equals("Manhunt")) {
			ice.setCancelled(true);
		} else {
			return;
		}
		ItemStack is = ice.getCurrentItem();
		
		// Check if the clicked item matches any item lores
		List<String> lore;
		try {
			lore = is.getItemMeta().getLore();
		} catch (NullPointerException npe) {
			// If no lore, that means this wasn't a clickable... we can return
			return;
		}
		switch (lore.get(0)) {
		case "Open Runners Window":
			plugin.getServer().getPluginManager().registerEvents(new MMGUIRunner((Player) ice.getWhoClicked()), plugin);
			inventoryClickSound((Player) ice.getWhoClicked(), ice.getWhoClicked().getLocation());
			break;
		case "Open Hunters Window":
			plugin.getServer().getPluginManager().registerEvents(new MMGUIHunter((Player) ice.getWhoClicked()), plugin);
			inventoryClickSound((Player) ice.getWhoClicked(), ice.getWhoClicked().getLocation());
			break;
		case "Click to verify your selections":
			access((Player) ice.getWhoClicked(), false);
			inventoryClickSound((Player) ice.getWhoClicked(), ice.getWhoClicked().getLocation());
			break;
		case "Click to start":
			// TODO add code to start game
			// Probably method in main that will garbage collect this GUI
			ice.getView().close();
			inventoryClickSound((Player) ice.getWhoClicked(), ice.getWhoClicked().getLocation());
			break;
		}
	}
	
	// Player Join event
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent pje) {
		hunters.add(pje.getPlayer());
		PlayerListSort(hunters);
	}
	
	// Player Quit event
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent pqe) {
		runners.remove(pqe.getPlayer());
		hunters.remove(pqe.getPlayer());
	}
	
	/** 
	 * I needed to write this method because Player objects aren't comparable... Makes sense. No natural sort for them sadly.
	 * This method implements a simple QuickSort algorithm.
	 * 
	 * @see "https://www.geeksforgeeks.org/bubble-sort/"
	 */
	private void PlayerListSort(List<Player> list) {
		int n = list.size();
		for (int i = 0; i < n - 1; i++) {
			for (int j = 0; j <  n - i - 1; j++) {
				if (list.get(j).getDisplayName().compareTo(list.get(j + 1).getDisplayName()) < 0) {
					Player temp = list.get(j);
					list.set(j, list.get(j + 1));
					list.set(j + 1, temp);
				}
			}
		}
	}
	
	private class MMGUIRunner implements Listener {
		
		public MMGUIRunner(Player p) {
			access(p);
		}
		
		private void access(Player p) {
			Inventory inv = Bukkit.createInventory(p, (runners.size() / ROW_LENGTH + 1) * ROW_LENGTH, "Manhunt / Runners");
			for (int i = 0; i < runners.size(); i++) {
				ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
				SkullMeta meta = (SkullMeta) skull.getItemMeta();
				meta.setOwningPlayer(runners.get(i));
				meta.setDisplayName(runners.get(i).getDisplayName());
				ArrayList<String> lore = new ArrayList<String>();
				lore.add("Click to add as a hunter.");
				meta.setLore(lore);
				skull.setItemMeta(meta);
				inv.setItem(i, skull);
			}
			ItemStack back = new ItemStack(Material.RED_TERRACOTTA);
			ItemMeta meta = back.getItemMeta();
			meta.setDisplayName("Back");
			List<String> lore = new ArrayList<String>();
			lore.add("Click to go back to main menu.");
			meta.setLore(lore);
			back.setItemMeta(meta);
			inv.setItem(inv.getSize() - 1, back);
			p.openInventory(inv);
		}
		
		@EventHandler
		public void onInventoryClick(InventoryClickEvent ice) {
			// Get inventory and clicked item
			if (ice.getView().getTitle().equals("Manhunt / Runners")) {
				ice.setCancelled(true);
			} else {
				return;
			}
			ItemStack is = ice.getCurrentItem();
			
			try {
				if (is.getType().equals(Material.PLAYER_HEAD)) {
					SkullMeta sm = (SkullMeta) is.getItemMeta();
					Player player = (Player) sm.getOwningPlayer();
					if (player.isOnline()) {
						runners.remove(player);
						hunters.add(player);
						PlayerListSort(hunters);
					}
					access((Player) ice.getWhoClicked());
					inventoryClickSound((Player) ice.getWhoClicked(), ice.getWhoClicked().getLocation());
					// TODO remove debug log
					plugin.getServer().getLogger().log(Level.INFO, "Hunters:");
					for (Player p : hunters) {
						plugin.getServer().getLogger().log(Level.INFO, p.getDisplayName());
					}
					plugin.getServer().getLogger().log(Level.INFO, "Runners:");
					for (Player p : runners) {
						plugin.getServer().getLogger().log(Level.INFO, p.getDisplayName());
					}
				} else if (is.getType().equals(Material.RED_TERRACOTTA)) {
					MMGUIMain.this.access((Player) ice.getWhoClicked(), true);
					inventoryClickSound((Player) ice.getWhoClicked(), ice.getWhoClicked().getLocation());
				}
			} catch (NullPointerException npe) {
				// Will be thrown if clicked ItemStack is null, in which case do nothing
				return;
			}
		}
	}
	
	private class MMGUIHunter implements Listener {
		
		public MMGUIHunter(Player p) {
			access(p);
		}
		
		private void access(Player p) {
			Inventory inv = Bukkit.createInventory(p, (hunters.size() / ROW_LENGTH + 1) * ROW_LENGTH, "Manhunt / Hunters");
			for (int i = 0; i < hunters.size(); i++) {
				ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
				SkullMeta meta = (SkullMeta) skull.getItemMeta();
				meta.setOwningPlayer(hunters.get(i));
				meta.setDisplayName(hunters.get(i).getDisplayName());
				ArrayList<String> lore = new ArrayList<String>();
				lore.add("Click to add as a runner.");
				meta.setLore(lore);
				skull.setItemMeta(meta);
				inv.setItem(i, skull);
			}
			ItemStack back = new ItemStack(Material.RED_TERRACOTTA);
			ItemMeta meta = back.getItemMeta();
			meta.setDisplayName("Back");
			List<String> lore = new ArrayList<String>();
			lore.add("Click to go back to main menu.");
			meta.setLore(lore);
			back.setItemMeta(meta);
			inv.setItem(inv.getSize() - 1, back);
			p.openInventory(inv);
		}
		
		@EventHandler
		public void onInventoryClick(InventoryClickEvent ice) {
			// Get inventory and clicked item
			if (ice.getView().getTitle().equals("Manhunt / Hunters")) {
				ice.setCancelled(true);
			} else {
				return;
			}
			ItemStack is = ice.getCurrentItem();
			
			try {
				if (is.getType().equals(Material.PLAYER_HEAD)) {
					SkullMeta sm = (SkullMeta) is.getItemMeta();
					Player player = (Player) sm.getOwningPlayer();
					if (player.isOnline()) {
						hunters.remove(player);
						runners.add(player);
						PlayerListSort(runners);
					}
					access((Player) ice.getWhoClicked());
					inventoryClickSound((Player) ice.getWhoClicked(), ice.getWhoClicked().getLocation());
					// TODO remove debug log
					plugin.getServer().getLogger().log(Level.INFO, "Hunters:");
					for (Player p : hunters) {
						plugin.getServer().getLogger().log(Level.INFO, p.getDisplayName());
					}
					plugin.getServer().getLogger().log(Level.INFO, "Runners:");
					for (Player p : runners) {
						plugin.getServer().getLogger().log(Level.INFO, p.getDisplayName());
					}
				} else if (is.getType().equals(Material.RED_TERRACOTTA)) {
					MMGUIMain.this.access((Player) ice.getWhoClicked(), true);
					inventoryClickSound((Player) ice.getWhoClicked(), ice.getWhoClicked().getLocation());
				}
			} catch (NullPointerException npe) {
				// NPE thrown if IS clicked is null in which case do nothing
				return;
			}
		}
	}

}
