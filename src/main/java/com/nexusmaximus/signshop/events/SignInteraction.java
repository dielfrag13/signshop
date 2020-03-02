package com.nexusmaximus.signshop.events;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.nexusmaximus.signshop.Main;

// TODO: implement sign registration for before sign becomes valid for use
// save the location of the sign in a file and check that it exists before performing commands
// only admins and plugins (like a shop) will be able to 'register' a sign
// that way people can't make their own sign

public class SignInteraction implements Listener {

	/* add more sign commands here as they become needed */
	public enum SignCommand {
		BUY, SELL, INVALID
	}

	// -------------------- Events -------------------- \\
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		Block b = e.getBlock();
		// if the block was a sign or has a sign blesse
		if (b.getType() == Material.WALL_SIGN || b.getType() == Material.SIGN 
			
			   ) {
			// and if the sign was blessed.....
			if (Main.getDFO().isBlessedSign(b.getLocation())) {
				// and if the player has permission....
				if (e.getPlayer().isOp() || Main.getDFO().isAdmin(e.getPlayer().getDisplayName())) {
					// deconsecrate the sign.
					Main.getDFO().deconsecrateSign(b.getLocation());
					Sign sign = (Sign) b.getState();
					for (int i = 0; i <= 3; i++) {
						sign.setLine(i, ChatColor.stripColor(sign.getLine(i)));
					}
					sign.update();
					e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 100, 0);
					e.getPlayer().sendMessage(ChatColor.GREEN + "Sign deconsecrated.");
				} else {
					e.getPlayer().sendMessage(ChatColor.DARK_RED + "Only admins can break blessed signs.");
					e.setCancelled(true);
				}

			} // else
				// e.getPlayer().sendMessage("Sign was not blessed; no action taken");

		} // say nothing if the block was not a sign.
		
		// but what if the block had a sign attached?
		else if ((b.getRelative(BlockFace.EAST).getType()==Material.WALL_SIGN && Main.getDFO().isBlessedSign(b.getRelative(BlockFace.EAST).getLocation()))
				|| (b.getRelative(BlockFace.WEST).getType()==Material.WALL_SIGN && Main.getDFO().isBlessedSign(b.getRelative(BlockFace.WEST).getLocation()))
				|| (b.getRelative(BlockFace.NORTH).getType()==Material.WALL_SIGN && Main.getDFO().isBlessedSign(b.getRelative(BlockFace.NORTH).getLocation()))
				|| (b.getRelative(BlockFace.SOUTH).getType()==Material.WALL_SIGN && Main.getDFO().isBlessedSign(b.getRelative(BlockFace.SOUTH).getLocation()))
				|| (b.getRelative(BlockFace.UP).getType()==Material.SIGN && Main.getDFO().isBlessedSign(b.getRelative(BlockFace.UP).getLocation()))

				) {
			
			// Deconsecrate each blessed sign attached to this block. 
			int count = 0;
			if (e.getPlayer().isOp() || Main.getDFO().isAdmin(e.getPlayer().getDisplayName())) {
				
				if (b.getRelative(BlockFace.EAST).getType()==Material.WALL_SIGN && Main.getDFO().isBlessedSign(b.getRelative(BlockFace.EAST).getLocation())) {
					Main.getDFO().deconsecrateSign(b.getRelative(BlockFace.EAST).getLocation());
					count++;
				}

				if (b.getRelative(BlockFace.WEST).getType()==Material.WALL_SIGN && Main.getDFO().isBlessedSign(b.getRelative(BlockFace.WEST).getLocation())) {
					Main.getDFO().deconsecrateSign(b.getRelative(BlockFace.WEST).getLocation());
					count++;
				}
				if (b.getRelative(BlockFace.NORTH).getType()==Material.WALL_SIGN && Main.getDFO().isBlessedSign(b.getRelative(BlockFace.NORTH).getLocation())) {
					Main.getDFO().deconsecrateSign(b.getRelative(BlockFace.NORTH).getLocation());
					count++;
				}
				if (b.getRelative(BlockFace.SOUTH).getType()==Material.WALL_SIGN && Main.getDFO().isBlessedSign(b.getRelative(BlockFace.SOUTH).getLocation())) {
					Main.getDFO().deconsecrateSign(b.getRelative(BlockFace.SOUTH).getLocation());
					count++;
				}
				if (b.getRelative(BlockFace.UP).getType()==Material.SIGN && Main.getDFO().isBlessedSign(b.getRelative(BlockFace.UP).getLocation())) {
					Main.getDFO().deconsecrateSign(b.getRelative(BlockFace.UP).getLocation());
					count++;
				}
				e.getPlayer().sendMessage(String.format(ChatColor.GREEN + "%d signs deconsecrated.", count));
				e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 100, 0);

			} else {
				e.getPlayer().sendMessage(ChatColor.DARK_RED + "Only admins can break blocks with blessed signs attached. Nice try though.");
				e.setCancelled(true);
			}
			
		}


	}

	// thank you https://bukkit.org/threads/how-to-detect-if-a-sign-breaks.103399/


	@EventHandler
	public void onSignClick(PlayerInteractEvent e) {

		if (!(e.getAction() == Action.RIGHT_CLICK_BLOCK))
			return;

		// get the player this event pertains to
		Player playa = e.getPlayer();
		// String pluginName = new String("[signshop] ");

		// some permission code i pulled from the spigot wiki. don't know in what case
		// it wouldn't be valid... yet...
		/*
		 * if (!playa.hasPermission("sign.use")) { playa.sendMessage(pluginName +
		 * "You don't have permission to touch that sign!"); return; }
		 */

		Block b = e.getClickedBlock();

		// stdout the process for coding and debugging processes
		// playa.sendMessage(pluginName + "clicked " + b.getType().toString() + " with "
		// + playa.getInventory().getItemInMainHand().toString());

		// signs on a wall or on the ground
		if (b.getType() == Material.WALL_SIGN || b.getType() == Material.SIGN) {

			// save the sign block...
			Sign sign = (Sign) b.getState();

			// if the sign is NOT blessed...
			if (!Main.getDFO().isBlessedSign(sign.getLocation())) {
				// check to see if we're blessing it
				// if playa has permission...
				if (playa.isOp() || Main.getDFO().isAdmin(playa.getDisplayName())) {
					if (playa.getInventory().getItemInMainHand().getType() == Material.POTION) {
						SignInfo si = new SignInfo(sign);
						if (si.isValid()) {
							Main.getDFO().blessSign(sign.getLocation());
							playa.playSound(playa.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 100, 0);
							ColorizeSign(sign);
							playa.sendMessage(ChatColor.GREEN
									+ "Sign blessed. Deconsecrate with an empty bottle or VIOLENT DESTRUCTION OF PROPERTY.");
							return;
						} else {
							playa.sendMessage(ChatColor.RED + "Couldn't bless sign....");
							playa.sendMessage(si.getError());
							return;
						}
					} else {
						if (new SignInfo(sign).isValid())
							playa.sendMessage(
									ChatColor.RED + "Signs must be blessed with holy water (i.e. a water bottle)");
						return;
					}
				} else {
					if (playa.getInventory().getItemInMainHand().getType() == Material.POTION)
						playa.sendMessage(ChatColor.DARK_RED + "Must be admin to bless signs");
					return;
				}

			} // continue if sign is blessed

			if (playa.isOp() || Main.getDFO().isAdmin(playa.getDisplayName())) {
				if (playa.getInventory().getItemInMainHand().getType() == Material.GLASS_BOTTLE) {
					Main.getDFO().deconsecrateSign(b.getLocation());
					for (int i = 0; i <= 3; i++) {
						sign.setLine(i, ChatColor.stripColor(sign.getLine(i)));
					}
					sign.update();
					playa.playSound(playa.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 100, 0);
					playa.sendMessage(ChatColor.GREEN + "Sign deconsecrated. Re-bless with holy water. ");
					return;
				}
			}

			// and check sign validity
			SignInfo si = new SignInfo(sign);

			if (!si.isValid()) {
				playa.sendMessage(si.getError());
				return;
			}

			// if the sign is NOT blessed, see if the player is performing a blessing (is an
			// op while holding something)
			// if neither of the above, then return

			// perform the command on the sign
			switch (si.getSignCommand()) {

			// selling to the sign
			case SELL:

				// need this many to sell
				int need = si.getQuantity();
				// counter for how many of this item is in inventory
				int have = 0;
				// count the number of items matching the sign item that the player has in
				// inventory
				ItemStack curItem;
				Material signItemType;
				for (int i = 0; i < 36; i++) {
					curItem = playa.getInventory().getItem(i);
					// if curItem is null, item inventory slot is empty
					if (curItem == null)
						continue;
					signItemType = si.getItem();

					// double equals ok; we're doing constant comparison

					// if item is air, this will NPE
					if (curItem.getType() == signItemType) {
						have += curItem.getAmount();
					}
				}

				// hindsight... there's a playa.getInventory().removeItem(ItemStack whatever) i
				// could have used
				if (have >= need) {
					// need becomes how much we need to deduct from the inventory of the player
					int leftToDeduct = need;
					while (leftToDeduct > 0) {
						// deduct items from player here
						for (int i = 0; i < 36; i++) {
							curItem = playa.getInventory().getItem(i);
							if (curItem == null) // if curItem is empty because no object is in that slot... continue
								continue;
							signItemType = si.getItem();

							if (curItem.getType() == signItemType) {
								// remove the whole stack in this case
								if (leftToDeduct > curItem.getAmount()) {
									leftToDeduct -= curItem.getAmount();
									playa.getInventory().getItem(i).setAmount(0);
								} // end if were removing the whole stack
									// remove a partial stack in this case
								else {
									playa.getInventory().getItem(i).setAmount(curItem.getAmount() - leftToDeduct);
									leftToDeduct = 0;
								} // end removing a partial stack
							} // end if inventory item matches sign item

						} // end full inventory check
						if (leftToDeduct != 0)
							playa.sendMessage("leftToDeduct is nonzero!! this should never happen!!!");

					}
					// pay the player
					Main.getEconomy().depositPlayer(playa, si.getPrice() * need);
					playa.sendMessage(ChatColor.GREEN + "Sell successful. Current Balance: "
							+ Main.getEconomy().getBalance(playa));

				} // end if (have >= need) is true
				else { // begin if (have > need) is false: does not possess enough material to sell to
						// this sign
					playa.sendMessage(String.format(
							ChatColor.RED + "Not enough %s to sell-- you currently have %d in your inventory!",
							si.getItem(), have));
				}
				break; // end sell switch case

			// buying from the sign

			case BUY:

				// in this case, have and need are monetary values instead of item counts
				double have2 = Main.getEconomy().getBalance(playa);

				int needCount = si.getQuantity();
				// if (needCount == -1) {
				// get user input
				// while (true) {

				// }

				// }
				double need2 = si.getPrice() * needCount;
				if (have2 > need2) {
					// may not work for >64 signs
					ItemStack sold = new ItemStack(si.getItem(), si.getQuantity());
					int totalSold = sold.getAmount();
					// don't change the following line without considering the if case below it
					/*
					 * Javadoc: as of v1.15.2
					 * https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/inventory/Inventory.html#
					 * addItem-org.bukkit.inventory.ItemStack...- "It is known that in some
					 * implementations this method will also set the inputted argument amount to the
					 * number of that item not placed in slots." Same thing for the reverse method
					 * (removeItem, which i will soon change the code to use)
					 */
					HashMap<Integer, ItemStack> overflow = playa.getInventory().addItem(sold);

					// items in overflow var need to not deduct from the account
					if (!overflow.isEmpty()) {
						// if this gets hit we need to figure out the number of items in the hashmap
						// there will only be one kind of item in the hashmap if the above code doesn't
						// change
						// unless the sign selling things sells more than 64 items (which may be fine)
						int counter = 0;

						// run through each key in the overflow hashmap
						for (Integer i : overflow.keySet()) {
							if (counter > 0)
								playa.sendMessage(
										"overflow hashmap > 1 item); this is WRONGO (unless well over 64 items being sold simultaneously)! FIX IMMEDIATE");
							totalSold = totalSold - overflow.get(i).getAmount();
							counter++;
						}
					}

					// deduct funds
					if (totalSold != 0) {
						Main.getEconomy().withdrawPlayer(playa, totalSold * si.getPrice());
						if (overflow.isEmpty())
							playa.sendMessage(ChatColor.GREEN + String.format("Buy successful. Current Balance: "
									+ Double.toString(Main.getEconomy().getBalance(playa))));
						else
							playa.sendMessage(ChatColor.YELLOW
									+ String.format("Partial buy (%d of %d) due to full inventory. Balance: %s",
											totalSold, si.getQuantity(), Main.getEconomy().getBalance(playa)));
					} else // if totalSold == 0
						playa.sendMessage(ChatColor.DARK_RED + "Inventory is completely full! Please clear room.");
				} else {
					playa.sendMessage(ChatColor.DARK_RED + String.format("Insufficient funds (%s) for transaction!",
							Double.toString(Main.getEconomy().getBalance(playa))));
				}
				break;

			// invalid sign
			case INVALID:
				playa.sendMessage(si.getError());
				break;

			} // end sign command implementation switch

		} // end sign click processing code

	} // end onSignClick()

	// -------------------- Helper Methods and Classes -------------------- \\

	public static void ColorizeSign(Sign sign) {

		String[] splitRes;
		String cur = new String("");
		// first line: green
		sign.setLine(0, ChatColor.GOLD + sign.getLine(0));

		// second line:
		splitRes = sign.getLine(1).split("x");

		// get only the last token of the split; reassemble other split tokens
		for (int i = 0; i < splitRes.length - 1; i++) {
			cur += splitRes[i] + "x";
		}
//		cur += splitRes[splitRes.length - 2];

		cur += ChatColor.BLUE + splitRes[splitRes.length - 1];
		sign.setLine(1, cur);
		// third line:

		splitRes = sign.getLine(2).split(" ");
		sign.setLine(2, ChatColor.GREEN + splitRes[0] + " " + ChatColor.BLACK + splitRes[1]);

		// fourth line:
		sign.update();
	}

	public static void getDeColorizedSign(Sign sign) {

	}

	// checks syntax of signs upon creation of a SignInfo object
	public class SignInfo {

		private Material item = null;
		private String error = null;
		private boolean valid = false; // do not change this value until we're about to leave the constructor
		private int price = -1;
		private int quantity = -1;
		SignCommand sc = null;
		String[] pieces = null;
		// private bool isRegistered = false;

		public SignInfo(Sign sign) {

			String buyString = new String("[buy]");
			String sellString = new String("[sell]");

			// Evaluate if line index 0 refers to a legit command
			if (ChatColor.stripColor(sign.getLine(0)).toLowerCase().equals(sellString)) {
				sc = SignCommand.SELL;
			} else if (ChatColor.stripColor(sign.getLine(0).toLowerCase()).equals(buyString)) {
				sc = SignCommand.BUY;
			} else {
				sc = SignCommand.INVALID;
				error = new String(ChatColor.DARK_RED + "Invalid sign command " + sign.getLine(0).toLowerCase() + "!");
				return;
			}

			// Parse line index 1 (if buy or sell sign)

			pieces = ChatColor.stripColor(sign.getLine(1)).split("x");
			// reassemble pieces up to the last token for items with an 'x' in the name
			String s = new String("");
			for (int i = 0; i < pieces.length - 2; i++) {
				s += pieces[i] + "x";
			}

			try {
				s += pieces[pieces.length - 2]; // last part of split tokens before the x [quantity]

				s = ChatColor.stripColor(s.trim());

				item = Material.valueOf(s.toUpperCase());
				// if (ChatColor.stripColor(sign.getLine(1).split("x")[pieces.length -
				// 1]).trim().equals("?")) {
				// quantity = -1;
				// } else
				quantity = Integer.parseInt(ChatColor.stripColor(sign.getLine(1)).split("x")[pieces.length - 1].trim());

				// quantity = Integer.parseInt(sign.getLine(1).split("x")[1]);
			} catch (ArrayIndexOutOfBoundsException a) {
				error = new String(
						ChatColor.DARK_RED + "Problem splitting second line of sign... invalid sign format!");
				return;
			} catch (NumberFormatException nfe) {
				error = new String(ChatColor.DARK_RED + "Could not convert quantity to number; invalid quantity!");
				return;
			} catch (IllegalArgumentException iae) {
				error = new String(ChatColor.DARK_RED + s + " is not a valid Minecraft item!");
				return;
			}

			// Parse line index 2 (if buy or sell sign)
			try {
				price = Integer.parseInt(ChatColor.stripColor(sign.getLine(2)).split(" ")[0].trim().substring(1));
			} catch (NumberFormatException nfe) {
				error = new String(ChatColor.DARK_RED + "Could not convert price to number; invalid price!");
				return;
			}

			// -1 is used to indicate X value
			if (price <= 0 && price != -1) {
				error = new String(ChatColor.DARK_RED + "Invalid price-- price must be positive!");
				return;
			}

			if (quantity < 1) {
				error = new String(ChatColor.DARK_RED + "Invalid quantity-- quantity must be positive!");
				return;
			}
			// made it this far? it's a valid sign!
			valid = true;
			return;

		} // end constructor

		// getters (no setters required... yet. Just u wait til we implement SHOP STOCK)
		public int getPrice() {
			return this.price;
		}

		public int getQuantity() {
			return this.quantity;
		}

		public SignCommand getSignCommand() {
			return this.sc;
		}

		public boolean isValid() {
			return this.valid;
		}

		public Material getItem() {
			return this.item;
		}

		public String getError() {
			return this.error;
		}

	}

} // end SignInteraction listener event handler
