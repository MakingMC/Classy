package Main;

import org.bukkit.*;
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

    private static final String CMD_PREFIX = "Classy.";

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
        String command = label.toLowerCase();
        String reqPermission = CMD_PREFIX + command;
        Server server = sender.getServer();

        //commands in this plugin require the Op or special command permissions
        if(!(isValidCommandAndUser(sender, reqPermission))) return true;

        //Now we know it is a safe player, then cast player to an object
        Player player = (Player)sender;

        if(args.length > 0)
            ExecuteCommandWithArgs(command, server, player, args);
        else
            ExecuteCommand(command, server, player);

        return true;

    }

    private void ExecuteCommandWithArgs(String command,Server server, Player player, String[] args) {
        //Execute commands that require an argument.
        Player target = Bukkit.getPlayer(args[0]);
        switch (command) {
            case "hurt":
                player.damage(Double.parseDouble(args[0]));
                break;
            case "nick":
                player.setDisplayName(args[0]);
                player.sendMessage(ChatColor.GREEN + "You have just changed your name to " + ChatColor.GREEN + args[0]);
                break;
            case "ban":

                if(Bukkit.getPlayer(args[0]) != null) {
                    target.setBanned(true);
                    target.kickPlayer("&4You have been banned");
                }
                else{
                    player.sendMessage("That player has not been online.");
                }
                break;
            case "unban":
                if(Bukkit.getOfflinePlayer(args[0]) != null) {
                    target.setBanned(false);
                    player.sendMessage(String.format("%s %s Has been unbanned!",ChatColor.RED, args[0]));
                }
                else{
                    player.sendMessage("That player has not been online.");
                }
                break;
            default:
                break;
        }
    }

    private void ExecuteCommand(String command,Server server, Player player) {
        //Execute all other commands.
        switch (command) {
            case "making":
                player.sendMessage("Hai bb");
                break;
            case "bp":
                player.sendMessage(String.format("%s"));
                break;
            case "test":
                player.sendRawMessage(String.format("Hey, the item you are holding's durability is %s", player.getItemInHand().getDurability()));
                break;
            case "fly":
                if(!player.getAllowFlight()){
                    player.sendMessage(String.format("%sYou are now flying!", ChatColor.GOLD));
                    player.setAllowFlight(true);
                    player.setFlying(true);
                }
                else {
                    player.sendMessage(String.format("%sYou are no longer flying!", ChatColor.GOLD));
                    player.setAllowFlight(false);
                    player.setFlying(false);
                }
                break;
            default:
                break;
        }
    }

    private boolean isValidCommandAndUser(CommandSender possiblePlayer, String reqPermission) {
        try {

            if (!(possiblePlayer instanceof Player)) {
                possiblePlayer.sendMessage("Only players can use this command!");
                return false;
            }
            Player player = (Player)possiblePlayer;

            if (player.hasPermission(reqPermission) || player.isOp()) return true;

            player.sendMessage(String.format("You do not have permission %s or you are not Op.", reqPermission));
            return false;
        } catch (Exception ex) {
            return false;
        }
    }
}