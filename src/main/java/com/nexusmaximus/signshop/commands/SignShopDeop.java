package com.nexusmaximus.signshop.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.nexusmaximus.signshop.Main;

public class SignShopDeop implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {

		if (sender instanceof Player) {
			Player playa = (Player) sender;
			if (playa.isOp()) {
				if (args.length < 1) {
					playa.sendMessage(ChatColor.DARK_RED + "Please include the name of someone to de-op.");
					return false;
				}
				for (String s : args) {
					if (Main.getDFO().isAdmin(s)) {
						Main.getDFO().removeAdmin(s);
						playa.sendMessage(ChatColor.GREEN + s + " removed from SignShop admin list.");
					} else
						playa.sendMessage(ChatColor.YELLOW + s + " is not on the SignShop admin list.");

				}
			} else {
				playa.sendMessage(ChatColor.DARK_RED
						+ "Bruh, you gotta be a server op to remove someone to the SignShop admin list.");
				return true;
			}
		}

		else if (sender instanceof ConsoleCommandSender) {

			if (args.length < 1) {
				System.out.println(ChatColor.DARK_RED + "Please include the name of someone to de-op.");
				return false;
			}
			for (String s : args) {
				if (Main.getDFO().isAdmin(s)) {
					Main.getDFO().removeAdmin(s);
					System.out.println(ChatColor.GREEN + s + " removed from SignShop admin list.");
				} else
					System.out.println(ChatColor.YELLOW + s + " is not on the SignShop admin list.");
			}

		}

		return true;
	}
}
