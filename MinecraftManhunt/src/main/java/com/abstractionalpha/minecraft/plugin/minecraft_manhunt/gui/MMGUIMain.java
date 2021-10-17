package com.abstractionalpha.minecraft.plugin.minecraft_manhunt.gui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
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
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		access(p, true);
	}
	
	private void access(Player p, boolean safe) {
		// Make sure lists are up-to-date
		check();
		
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
	}
	
	protected void check() {
		// Fill runners if lists are empty
		if (hunters.size() == 0 && runners.size() == 0) {
			for (Player p : plugin.getServer().getOnlinePlayers()) {
				hunters.add(p);
			}
			hunters.sort(null);
		}
		
		// Add any other players that aren't online
		if (hunters.size() + runners.size() != plugin.getServer().getOnlinePlayers().size()) {
			for (Player p : plugin.getServer().getOnlinePlayers()) {
				if (!(runners.contains(p) || hunters.contains(p))) {
					hunters.add(p);
				}
				hunters.sort(null);
			}
		}
		
		// Check for invalid items in lists
		for (Player hunter : hunters) {
			if (!plugin.getServer().getOnlinePlayers().contains(hunter)) {
				hunters.remove(hunter);
			}
		}
		for (Player runner : runners) {
			if (!plugin.getServer().getOnlinePlayers().contains(runner)) {
				runners.remove(runner);
			}
		}
	}

}
