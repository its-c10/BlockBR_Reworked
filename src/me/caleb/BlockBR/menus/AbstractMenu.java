package me.caleb.BlockBR.menus;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import me.caleb.BlockBR.Main;
import me.caleb.BlockBR.sql.DataManager;
import me.caleb.BlockBR.utils.Chat;

public abstract class AbstractMenu implements Listener, InventoryHolder{

	protected final Inventory inv;
	protected Main plugin;
	protected FileConfiguration config;
	protected String mineType;
	
	public AbstractMenu(Main plugin, String menuTitle, int numSlots) {	
		this.plugin = plugin;
		inv = Bukkit.createInventory(this, numSlots, Chat.chat(menuTitle));
		config = plugin.getConfig();
		mineType = config.getString("MineType");
	}
	
	public Inventory getInventory() {
		return inv;
	}
	
	public void openInventory(Player p) {
		p.openInventory(inv);
	}
	
	/*
	 * If the item has enchantments
	 */
	protected static ItemStack createGuiItem(Material mat, String name, Map<Enchantment,Integer> enchants, int amount, String...lore) {
		
		ItemStack item = new ItemStack(mat,1);
		ItemMeta meta = item.getItemMeta();
		ArrayList<Enchantment> itemEnchants = new ArrayList<Enchantment>();
		ArrayList<Integer> itemLevels = new ArrayList<Integer>();
		
		//Gets all the enchants from the map
		for(Entry<Enchantment, Integer> en : enchants.entrySet()){
			itemEnchants.add(en.getKey());
		}
		
		//Gets all the levels from the map
		for(Entry<Enchantment, Integer> en : enchants.entrySet()){
			itemLevels.add(en.getValue());
		}
		
		for(int x = 0; x < enchants.size(); x++) {
			
			if(!item.getType().equals(Material.NETHER_STAR)) {
				meta.addEnchant(itemEnchants.get(x), itemLevels.get(x), true);
			}else {
				EnchantmentStorageMeta metaE = (EnchantmentStorageMeta) item.getItemMeta();
				metaE.addStoredEnchant(itemEnchants.get(x), itemLevels.get(x), true);
				item.setItemMeta(metaE);
			}
			
		}
		
		meta.setDisplayName(name);
		item.setAmount(amount);
		
		ArrayList<String> metaLore = new ArrayList<String>();
		
		for(String lorecomments : lore) {
			metaLore.add(lorecomments);
		}
		
		if(!item.getType().equals(Material.ENCHANTED_BOOK)) {
			meta.setLore(metaLore);
			item.setItemMeta(meta);
		}
		
		return item;
	}
	
	/*
	 * No enchants. Regular items
	 */
	protected static ItemStack createGuiItem(Material material, String name,int amount, String...lore) {
		
		ItemStack item = new ItemStack(material,1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setAmount(amount);
		ArrayList<String> metaLore = new ArrayList<String>();
	
		if(!lore.equals("")) {
			for(String lorecomments : lore) {
				metaLore.add(lorecomments);
			}
			meta.setLore(metaLore);
		}else {
			meta.setLore(null);
		}
		
		item.setItemMeta(meta);
		return item;
		
	}
	
	protected static String chat(String s) {
		return Chat.chat(s);
	}
	
	//If you only want one item
	protected static ItemStack createGuiItem(Material material, String name, String...lore) {
		
		ItemStack item = new ItemStack(material,1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		
		ArrayList<String> metaLore = new ArrayList<String>();
		
		for(String lorecomments : lore) {
			
			metaLore.add(lorecomments);
			
		}
		
		meta.setLore(metaLore);
		item.setItemMeta(meta);
		return item;
	}
	
	//Filler item
	protected static ItemStack createGuiItem() {
		
		ItemStack item = new ItemStack(Material.CYAN_STAINED_GLASS_PANE,1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(" ");
		meta.setLore(null);
		item.setItemMeta(meta);
		
		return item;
	}
	
	public abstract void initializeItems(Player p);
	
	@EventHandler
	protected abstract void onInventoryClick(InventoryClickEvent e);
	
}