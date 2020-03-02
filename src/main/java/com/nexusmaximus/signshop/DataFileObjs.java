package com.nexusmaximus.signshop;

import java.io.Serializable;
import java.util.ArrayList;

import org.bukkit.Location;

// serializable makes it hecka easier to save data in a more obfuscated way, while using built-in Java IO mechanisms. 
public class DataFileObjs implements Serializable{
	static final long serialVersionUID = 420L;

	private ArrayList<String> signShopAdmins;
	
	// currently works, but try setting this to be Location objects instead
	private ArrayList<String> blessedSigns;
	// private String mostRecentError;

	/* Constructor */
	public DataFileObjs() {
		signShopAdmins = new ArrayList<String>();
		blessedSigns = new ArrayList<String>();
		// mostRecentError = new String("none");
	}

	// return true upon success; false upon failure
	// see mostRecentError for error message
	// or not haha jk 
	public boolean addAdmin(String admin) {

		if (!(this.getAdmins().contains(admin))) {
			this.getAdmins().add(admin);
		}
		return true;
	}
	public boolean isAdmin(String admin) {
		if (this.getAdmins().contains(admin))
			return true;
		return false;
		
	}
	public boolean removeAdmin(String admin) {
		if (this.getAdmins().contains(admin)) {
			this.getAdmins().remove(admin);
		}
		return true;
	}

	public boolean blessSign(Location location) {
		if (!(this.getBlessedSignLocations().contains(location.toString()))) {
			this.getBlessedSignLocations().add(location.toString());
		}
		return true;
	}
	
	public boolean isBlessedSign(Location location) {
		if (this.getBlessedSignLocations().contains(location.toString()))
			return true;
		return false;
	}

	public boolean deconsecrateSign(Location location) {
		if (this.getBlessedSignLocations().contains(location.toString())) {
			this.getBlessedSignLocations().remove(location.toString());
		}
		return true;
	}

	public ArrayList<String> getAdmins() {
		return this.signShopAdmins;
	}

	public ArrayList<String> getBlessedSignLocations() {
		return this.blessedSigns;
	}
	
	

	

} // end data file objs internal class
