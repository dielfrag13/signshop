package com.nexusmaximus.signshop.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.nexusmaximus.signshop.Main;

public class SignShopOp implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {

		if (sender instanceof Player) {
			Player playa = (Player) sender;
			if (playa.isOp()) {
				if (args.length < 1) {
					playa.sendMessage(ChatColor.DARK_RED + "Please include the name of someone to op.");
					return false;
				}
				for (String s : args) {
					Main.getDFO().addAdmin(s);
					playa.sendMessage(ChatColor.GREEN + s + " added to SignShop admin list.");

				}
			} else {
				playa.sendMessage(ChatColor.DARK_RED + "Bruh, you gotta be a server op to add someone to the SignShop admin list.");
				return true;
			}
		}

		else if (sender instanceof ConsoleCommandSender) {

			for (String s : args) {
				Main.getDFO().addAdmin(s);
				System.out.println(ChatColor.GREEN + s + " added to SignShop admin list.");
			}

		}

		return true;
	}

}



