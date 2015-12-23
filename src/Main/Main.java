package Main;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;

public final class Main extends JavaPlugin implements Listener{

    private static final String CMD_PREFIX = "Classy.";
    private MESSAGE_MODE CURRENT_LEVEL;
    enum MESSAGE_MODE{
        DEBUG, INFORMATION
    }



    @Override
    public void onEnable(){
        CURRENT_LEVEL = MESSAGE_MODE.DEBUG;
        getLogger().info("Plugin Enabled!");
    }
    @Override
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
            ExecuteCommandWithArgs(command, player, args);
        else ExecuteCommand(command, server, player);

        return true;

    }

    private void ExecuteCommandWithArgs(String command, Player player, String[] args) {
        //Execute commands that require an argument.
        Player target = Bukkit.getPlayer(args[0]);
        switch (command) {
            case "hurt":
                player.damage(Double.parseDouble(args[0]));
                break;
            case "nick":
                player.setDisplayName(args[0]);
                InfoWrite(player, (ChatColor.GREEN + "You have just changed your name to " + ChatColor.GREEN + args[0]));
                break;
            case "ban":

                if (Bukkit.getPlayer(args[0]) != null) {
                    target.setBanned(true);
                    target.kickPlayer(String.format("%sYou have been banned", ChatColor.RED));
                } else {
                    if (Bukkit.getOfflinePlayer(args[0]) != null) {

                        OfflinePlayer offlinetarget = Bukkit.getOfflinePlayer(args[0]);
                        offlinetarget.setBanned(true);
                    }
                    InfoWrite(player, String.format("%sThat player has not been online", ChatColor.RED));
                }
                break;
            case "unban":
                try {
                    String tp = args[0].toLowerCase().trim();
                    DebugWrite(player, String.format("Looking for player %s", tp));
                    Set<OfflinePlayer> players = Bukkit.getBannedPlayers();

                    for (OfflinePlayer offlinePlayer : players) {

                        DebugWrite(player, String.format("Found %s", offlinePlayer.getName()));
                        if (offlinePlayer.getName().toLowerCase().trim().contains(tp)) {
                            offlinePlayer.setBanned(false);
                            InfoWrite(player, String.format("You have unbanned %s. and there were %s other banned players", tp, players.size()));
                        }
                    }
                    if ((players == null) || (players.size() == 0)) {
                        InfoWrite(player, ("Wrong lady"));
                    }
                } catch (Exception ex) {
                    InfoWrite(player, (ex.getMessage()));
                }

                break;
            case "trade":
                InfoWrite(player, player.getItemInHand().getItemMeta().getDisplayName());
                //ItemMeta itemMetadata = player.getItemInHand().getItemMeta();

                if (player.getItemInHand() instanceof ItemStack) {
                    ItemStack stack = player.getItemInHand();
                    if (stack.getAmount() > 1 && args[0] == null)
                        stack.setAmount(stack.getAmount() - 1);
                } else
                    player.setItemInHand(null);


                break;
            default:
                break;
        }
    }
    private void DebugWrite(Player player, String message){
        if(CURRENT_LEVEL == MESSAGE_MODE.DEBUG)
        player.sendMessage(message);
    }
    private void DebugWrite(Player player, String message, MESSAGE_MODE mode){
        switch(mode){
            case DEBUG:
                    DebugWrite(player, String.format("%s" + message , ChatColor.BLUE ));
                break;
            case INFORMATION:
                    DebugWrite(player, String.format("%s" + message , ChatColor.LIGHT_PURPLE ));
                break;
            default:
                break;
        }

    }
    private void InfoWrite(Player player, String message){
        DebugWrite(player, message, MESSAGE_MODE.INFORMATION);
    }




    private void ExecuteCommand(String command,Server server, Player player) {
        //Execute all other commands.
        switch (command) {
            case "making":
                InfoWrite(player, ("Hai bb"));
                break;

            case "bp":
                InfoWrite(player, String.format("%s", Bukkit.getBannedPlayers()));
                break;

            case "friends":
                Inventory anv = Bukkit.createInventory(null, InventoryType.ANVIL, "s");
                player.openInventory(anv);
                ItemStack is = new ItemStack(Material.DIAMOND);
                anv.addItem(is);

                break;
            case "test":
                player.sendRawMessage(String.format("Hey, the item you are holding's durability is %s", player.getItemInHand().getDurability()));
                break;
            case "vers":
                InfoWrite(player, String.format("%sYou are running version 2.0 of Classy", ChatColor.GREEN));
                break;
            case "fly":
                if(!player.getAllowFlight()){
                    InfoWrite(player, String.format("%sYou are now flying!", ChatColor.GOLD));
                    player.setAllowFlight(true);
                    player.setFlying(true);
                }
                else {
                    InfoWrite(player, String.format("%sYou are no longer flying!", ChatColor.GOLD));
                    player.setAllowFlight(false);
                    player.setFlying(false);
                }
                break;
            case "debug":
                if(CURRENT_LEVEL == MESSAGE_MODE.DEBUG) {
                    InfoWrite(player, "You have toggled Debug mode off.");
                    CURRENT_LEVEL = MESSAGE_MODE.INFORMATION;


                }
                else{
                    CURRENT_LEVEL = MESSAGE_MODE.DEBUG;
                    InfoWrite(player, "You have toggled Debug mode on.");
                }
            break;
            default:
                break;
        }
    }

    private boolean isValidCommandAndUser(CommandSender possiblePlayer, String reqPermission) {
        try {

            if (!(possiblePlayer instanceof Player)) {
                return false;
            }
            Player player = (Player)possiblePlayer;

            if (player.hasPermission(reqPermission) || player.isOp()) return true;

            InfoWrite(player, String.format("You do not have permission %s or you are not Op.", reqPermission));
            return false;
        } catch (Exception ex) {
            return false;
        }
    }
}