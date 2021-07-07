package com.katanamajesty;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings({"unused", "ConstantConditions"})
public class CraftableBundle extends JavaPlugin implements CommandExecutor, TabCompleter, Listener {

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        saveConfig();

        ItemStack bundle = new ItemStack(Material.BUNDLE);
        NamespacedKey key = new NamespacedKey(this, "bundle");
        ShapedRecipe bundleRecipe = new ShapedRecipe(key, bundle);

        bundleRecipe.shape("*%*", "% %", "%%%");
        bundleRecipe.setIngredient('*', Material.STRING);
        bundleRecipe.setIngredient('%', Material.RABBIT_HIDE);

        getServer().addRecipe(bundleRecipe);

        getCommand("craftablebundle").setExecutor(this);

        getServer().getPluginManager().registerEvents(this, this);

        System.out.println("[" + getDescription().getName() + "]" + " Plugin was successfully loaded!");

    }

    @Override
    public void onDisable() {
        System.out.println("[" + getDescription().getName() + "]" + " Plugin was successfully disabled");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String noPerm = colorFormat(getConfig().getString("NoPermission"));
        if (args.length == 1 && "reload".equals(args[0])) {
            if (sender.hasPermission("craftablebundle.reload")) {
                reloadConfig();
                sender.sendMessage(colorFormat(getConfig().getString("ConfigReloaded")));
                return true;
            } else {
                sender.sendMessage(noPerm);
                return false;
            }
        } else if (args.length == 1 && "version".equals(args[0])) {
            if (sender.hasPermission("craftablebundle.version")) {
                sender.sendMessage(ChatColor.GREEN + "Plugin Version: " + getDescription().getVersion());
                sender.sendMessage(ChatColor.GRAY + "Plugin made by KatanaMajesty ♥");
                return true;
            } else {
                sender.sendMessage(noPerm);
                return false;
            }
        } else {
            sender.sendMessage(colorFormat(getConfig().getString("UnknownCommands")));
            return false;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> commandList = new ArrayList<>();
        commandList.add("reload");
        commandList.add("version");
        return commandList.stream().sorted(Comparator.naturalOrder()).collect(Collectors.toList());
    }

    @EventHandler
    public void onBundleCraft(PrepareItemCraftEvent event) {

        if (!getConfig().getBoolean("BundleEnabled")) {
            if (event.getRecipe() != null) {
                if (event.getRecipe().getResult().isSimilar(new ItemStack(Material.BUNDLE))) {
                    event.getInventory().setResult(new ItemStack(Material.AIR));
                }
            }
        }

    }

    public String colorFormat(String s) {
        ChatColor.translateAlternateColorCodes('&',s);
        return s;
    }

}
