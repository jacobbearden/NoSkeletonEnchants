package com.aefonix.noskeletonenchants;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.Lists;

import java.util.List;

public class NoSkeletonEnchants extends JavaPlugin implements Listener {
  @Override
  public void onEnable() {
    this.getServer().getPluginManager().registerEvents(this, this);
  }

  @Override
  public void onDisable() {}

  @EventHandler
  public void onCreatureSpawn(CreatureSpawnEvent event) {
    if (!event.isCancelled()) {
      Entity entity = event.getEntity();

      if (entity instanceof Skeleton) {
        Skeleton skeleton = (Skeleton)entity;

        ItemStack item = skeleton.getEquipment().getItemInHand();
        ItemMeta meta = item.getItemMeta();

        List<String> lore = Lists.newArrayList();
        lore.add("Skeleton Bow");

        meta.setLore(lore);
        item.setItemMeta(meta);

        skeleton.getEquipment().setItemInHand(item);
      }
    }
  }

  @EventHandler
  public void onEntityDeath(EntityDeathEvent event) {
    Entity entity = event.getEntity();

    if (entity instanceof Skeleton && event.getDrops() != null) {
      Skeleton skeleton = (Skeleton)entity;
      List<ItemStack> drops = event.getDrops();

      for (int i = 0; i < drops.size(); i++) {
        ItemStack item = drops.get(i);
        ItemMeta meta = item.getItemMeta();

        // check for custom lore to prevent removing enchants from bows picked up off the ground
        if (item.getType() == Material.BOW && meta.getLore().contains("Skeleton Bow")) {
          for (Enchantment enchant : item.getItemMeta().getEnchants().keySet()) {
            meta.removeEnchant(enchant);
          }

          meta.setLore(Lists.newArrayList());
          item.setItemMeta(meta);
        }
      }
    }
  }
}
