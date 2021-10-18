package com.abstractionalpha.minecraft.plugin.minecraft_manhunt.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
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
		Inventory inv = ice.getClickedInventory();
		if (ice.getView().getTitle().equals("Manhunt")) {
			ice.setCancelled(true);
		} else {
			return;
		}
		ItemStack is = ice.getCurrentItem();
		
		// Check if the clicked item matches any item lores
		List<String> lore = is.getItemMeta().getLore();
		switch (lore.get(0)) {
		case "Open Runners Window":
			// TODO add runners window
			inventoryClickSound((Player) ice.getWhoClicked(), ice.getWhoClicked().getLocation());
			break;
		case "Open Hunters Window":
			// TODO add hunters window
			inventoryClickSound((Player) ice.getWhoClicked(), ice.getWhoClicked().getLocation());
			break;
		case "Click to verify your selections":
			access((Player) ice.getWhoClicked(), false);
			inventoryClickSound((Player) ice.getWhoClicked(), ice.getWhoClicked().getLocation());
			break;
		case "Click to start":
			// TODO add code to start game
			// Probably method in main that will garbage collect this GUI
			inventoryClickSound((Player) ice.getWhoClicked(), ice.getWhoClicked().getLocation());
			break;
		}
	}
	
	// Player Join event
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent pje) {
		hunters.add(pje.getPlayer());
		hunters.sort(null);
	}
	
	// Player Quit event
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent pqe) {
		runners.remove(pqe.getPlayer());
		hunters.remove(pqe.getPlayer());
	}
	
	private class MMGUIRunner implements Listener {
		
		public MMGUIRunner(Player p) {
			access(p);
		}
		
		private void access(Player p) {
			
		}
		
		@EventHandler
		public void onInventoryClick(InventoryClickEvent ice) {
			// Get inventory and clicked item
			Inventory inv = ice.getClickedInventory();
			if (ice.getView().getTitle().equals("Manhunt / Runners")) {
				ice.setCancelled(true);
			} else {
				return;
			}
			ItemStack is = ice.getCurrentItem();
			
			if (is.getType().equals(Material.PLAYER_HEAD)) {
				SkullMeta sm = (SkullMeta) is.getItemMeta();
				Player player = (Player) sm.getOwningPlayer();
				if (player.isOnline()) {
					runners.remove(player);
					hunters.add(player);
					hunters.sort(null);
				}
				access((Player) ice.getWhoClicked());
				inventoryClickSound((Player) ice.getWhoClicked(), ice.getWhoClicked().getLocation());
			}
		}
	}

}
