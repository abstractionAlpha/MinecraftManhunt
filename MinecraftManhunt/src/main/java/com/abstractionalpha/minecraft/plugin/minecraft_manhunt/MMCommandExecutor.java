package com.abstractionalpha.minecraft.plugin.minecraft_manhunt;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.abstractionalpha.minecraft.plugin.minecraft_manhunt.gui.MMGUIMain;

public class MMCommandExecutor implements CommandExecutor {
	
	private JavaPlugin plugin;
	
	public MMCommandExecutor(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			plugin.getServer().getPluginManager().registerEvents(new MMGUIMain(plugin, (Player) sender), plugin);
		}
		
		return true;
	}

}
