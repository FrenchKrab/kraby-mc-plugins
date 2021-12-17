package com.kraby.mcarcinizer.enchante.tweaks;

import java.util.List;

import com.kraby.mcarcinizer.CarcinizerMain;
import com.kraby.mcarcinizer.enchante.EnchanteSubplugin;
import com.kraby.mcarcinizer.enchante.config.TweaksConfig;
import com.kraby.mcarcinizer.enchante.config.betteranvil.ItemAttributeEnchanterData;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TweaksListener implements Listener  {
    
    @EventHandler
    public void onAnvilAttributeEnchantItem(PrepareAnvilEvent e) {
        AnvilInventory inv = e.getInventory();


        if (inv.getItem(1) != null && inv.getItem(0) != null && e.getResult() != null) {
            ItemStack result = e.getResult().clone();

            if (!result.getEnchantments().containsKey(Enchantment.MENDING))
                return;
    
            ItemMeta newMeta = result.getItemMeta();

            TweaksConfig cfg = EnchanteSubplugin.getSingleton().getTweaksConfig();
            for (Enchantment enchantment : cfg.getMendingBoundEnchants()) {
                newMeta.addEnchant(enchantment, 1, true);
            }

            result.setItemMeta(newMeta);
            e.setResult(result);
        }
    }

}
