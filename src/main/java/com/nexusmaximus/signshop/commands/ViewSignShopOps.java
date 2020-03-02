package com.nexusmaximus.signshop.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.nexusmaximus.signshop.Main;

public class ViewSignShopOps implements CommandExecutor {

	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		String s = new String("SignShop ops: ");
		if (arg0 instanceof Player) {
			Player playa = (Player) arg0;

			for (String i : Main.getDFO().getAdmins()) {
				s += (i + ", ");
			}

			playa.sendMessage(ChatColor.BLUE + s);
		}

		else if (arg0 instanceof ConsoleCommandSender) {
			for (String i : Main.getDFO().getAdmins()) {
				s.concat(i + ", ");
			}
			System.out.println(ChatColor.BLUE + "SignShop ops: " + s);
		}
		return true;
	}
}
