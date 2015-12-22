package Main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin implements Listener{
    @Override
    public void onEnable(){
        getLogger().info("Plugin Enabled!");
    }

    private void teleportInWorld(Player player, int x, int y, int z){
        player.teleport(new Location(player.getWorld(), x, y, z));
    }
    private void openGUI(Player player) {
        Inventory inv = Bukkit.createInventory(null, 9, ChatColor.DARK_GREEN
                + "Server selector");
        ItemStack survival = new ItemStack(Material.DIAMOND_CHESTPLATE);
        ItemMeta survivalMeta = survival.getItemMeta();
        ItemStack friends = new ItemStack(Material.DIAMOND);
        ItemMeta friendsMeta = survival.getItemMeta();

        survivalMeta.setDisplayName(ChatColor.DARK_RED + "Survival");
        survival.setItemMeta(survivalMeta);

        friendsMeta.setDisplayName(ChatColor.GREEN + "Friends");
        friends.setItemMeta(friendsMeta);

        inv.setItem(3, friends);
        inv.setItem(5, survival);

        player.openInventory(inv);

    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        if(!ChatColor.stripColor(event.getInventory().getName())
                .equalsIgnoreCase("Server Selector"))
            return;
        Player player = (Player) event.getWhoClicked();
        event.setCancelled(true);

        if(event.getCurrentItem()== null
                || event.getCurrentItem().getType() == Material.AIR
                || !event.getCurrentItem().hasItemMeta()){
            player.closeInventory();
            return;

        }

        switch(event.getCurrentItem().getType()){
            case DIAMOND_CHESTPLATE:
                teleportInWorld(player, 0, 50, 0);
                player.sendMessage(String.format("%s Teleported to %sSurvival%s!", ChatColor.GOLD, ChatColor.DARK_RED, ChatColor.GOLD));

                break;
            case DIAMOND:
                teleportInWorld(player, 0, 50, 0);
                player.sendMessage(String.format("%s Opened %sFriends%s List!", ChatColor.GOLD, ChatColor.GREEN, ChatColor.GOLD));
                break;
            default:
                player.closeInventory();
                break;
        }

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().getInventory().addItem(new ItemStack(Material.COMPASS));
    }
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Action a = event.getAction();
        ItemStack is = event.getItem();

        if(a == Action.PHYSICAL || is == null || is.getType() == Material.AIR)
            return;
        if(is.getType() == Material.COMPASS)
            openGUI(event.getPlayer());
    }
    public void onDisable(){
        getLogger().info("Plugin Disabled!");
    }
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        if (cmd.getLabel().equalsIgnoreCase("Making") && player.hasPermission("classy.making") || player.isOp()){
            if (!(sender instanceof Player)) {
                sender.sendMessage("Only players can use this command!");
                return true;
            }
            player.sendMessage("Hai bb");
        }

        if (cmd.getLabel().equalsIgnoreCase("Hurt")){
            if (!(sender instanceof Player)) {
                sender.sendMessage("Only players can use this command!");
                return true;
            }

            player.damage(1);
        }
        if (cmd.getLabel().equalsIgnoreCase("nick") && args.length == 1){
            if (!(sender instanceof Player)) {
                sender.sendMessage("Only players can use this command!");
                return true;
            }

            player.setDisplayName(args[0]);
            player.sendMessage(ChatColor.GREEN + "You have just changed your name to " + ChatColor.GREEN + args[0]);


        }

        if (cmd.getLabel().equalsIgnoreCase("bp") && args.length == 1){
            if (!(sender instanceof Player)) {
                sender.sendMessage("Only players can use this command!");
                return true;
            }
            sender.getServer().getBannedPlayers();
        }
        if (cmd.getLabel().equalsIgnoreCase("ban") && args.length == 1){
            if (!(sender instanceof Player)) {
                sender.sendMessage("Only players can use this command!");
                return true;
            }





        }
        if (cmd.getLabel().equalsIgnoreCase("test")&& args.length == 0){
            if (!(sender instanceof Player)) {
                sender.sendMessage("Only players can use this command!");
                return true;
            }
            player.sendRawMessage(String.format("Hey, the item you are holding's durability is %s", player.getItemInHand().getDurability()));

        }
        if (cmd.getLabel().equalsIgnoreCase("fly")){
            if (!(sender instanceof Player)) {
                sender.sendMessage("Only players can use this command!");
                return true;
            }
            if(!player.getAllowFlight()){
                player.sendMessage(String.format("%sYou are now flying!", ChatColor.GOLD));
                player.setAllowFlight(true);
                player.setFlying(true);

            }
            else if(player.getAllowFlight()){
                player.sendMessage(String.format("%sYou are no longer flying!", ChatColor.GOLD));
                player.setAllowFlight(false);
                player.setFlying(false);

            }

        }


        return true;
    }
}