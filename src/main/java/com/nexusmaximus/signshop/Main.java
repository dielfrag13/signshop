package com.nexusmaximus.signshop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
//import org.bukkit.event.player.PlayerBedLeaveEvent;


import com.nexusmaximus.signshop.commands.SignShopDeop;
import com.nexusmaximus.signshop.commands.SignShopOp;
import com.nexusmaximus.signshop.commands.ViewSignShopOps;
import com.nexusmaximus.signshop.events.SignInteraction;

import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin implements Listener {

	private static Economy econ;
	private static DataFileObjs dfo;
	// custom directory creation code expects this name to not change
	private String filename = new String("plugins\\signShop\\signShopData.dat");
	private File dataFile;


	@Override
	public void onEnable() {

		// set up vault api
		if (!setupEconomy()) {
			// log.severe(String.format("[%s] - Disabled due to no Vault dependency found!",
			// getDescription().getName()));
			System.out.println("[signshop] setupEconomy failed! Disabiling Vault.");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		// register events
		getServer().getPluginManager().registerEvents(new SignInteraction(), this);

		// load up the config file so SignInteraction.java can call it signShopData.dat
		dataFile = new File(filename);

		// if the dat file doesn't exist, make it.
		boolean newFile = false;
		if (!dataFile.exists()) {
			
			// make the directory (shouldn't do anything if directory is already made)
			new File("plugins\\signShop").mkdir();
			
			try {

				if (dataFile.createNewFile()) {
					System.out.print("File created: " + dataFile.getName());
					newFile = true;
				} else
					System.out.print("Found " + dataFile.getName() + ". Loading...");
			} catch (IOException e) {
				System.out.println("Can't make dat file. Riperino. ");
				e.printStackTrace();
			}
		}

		// if we made a new dat file, we don't want to read our dfo object from it; we
		// want to make a new one
		if (newFile) {
			dfo = new DataFileObjs();
		} else {
			System.out.print("[signShop loader] Found " + dataFile.getName() + ". Loading...");
			dfo = readDataFile(filename);
			System.out.print("[signShop loader] Done.");
		}
		
		
		// register commands associated with signshop
		this.getCommand("ViewSignShopOps").setExecutor(new ViewSignShopOps());
		this.getCommand("SignShopOp").setExecutor(new SignShopOp());
		this.getCommand("SignShopDeop").setExecutor(new SignShopDeop());


	} // end onEnable

	@Override
	public void onDisable() {
		// write the data file with whatever's in dfo
		writeDataFile();
	} // end onDisable();

	// ------------- Helper methods ------------- \\

	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}

		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	} // end setupEconomy();

	

	public static Economy getEconomy() {
		return econ;
	}
	
	public static DataFileObjs getDFO() {
		return dfo;
	}
	
	
	private DataFileObjs readDataFile(String filename) {
		DataFileObjs tmp = null;
		try {
			FileInputStream fis = new FileInputStream(filename);
			ObjectInputStream ois = new ObjectInputStream(fis);
			tmp = (DataFileObjs)ois.readObject();
			ois.close();

			// literally none of these exceptions should ever occur
		} catch (FileNotFoundException e) {
			System.out.println("[signShop Main.java] This should never happen #1");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("[signShop Main.java] This should never happen #2");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("[signShop Main.java] This should never happen #3");
			e.printStackTrace();
		}
		if (tmp == null) {
			System.out.println("[signShop Main.java] This should never happen #4");
		}
		return tmp;

	} // end readDataFile
	
	private void writeDataFile() {
		try {
			FileOutputStream fos = new FileOutputStream(filename);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(dfo);
			oos.close();

			// if we haven't created a signShopData.dat before, do so:
		} catch (FileNotFoundException fnfe) {
			System.out.println("[signShop Main.java] This should never happen #5");
			fnfe.printStackTrace();

		} catch (IOException e) {
			System.out.println("[signShop Main.java] This should never happen #6");
			e.printStackTrace();
		}
	} // end writeDataFile
	

}
