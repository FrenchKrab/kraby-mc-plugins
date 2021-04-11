package com.kraby.mcarcinizer.mcarcinizer.commands;

import com.kraby.mcarcinizer.CarcinizerMain;
import com.kraby.mcarcinizer.carcinizer.expressions.variables.PlayerVariables;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.InventoryHolder;
import net.md_5.bungee.api.ChatColor;

public class InvSizeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        CarcinizerMain.getSingleton().reloadSubplugin();
        if (sender instanceof InventoryHolder) {
            InventoryHolder ih = (InventoryHolder)sender;
            double invSize = PlayerVariables.getInvSizeValue(ih.getInventory());
            sender.sendMessage(ChatColor.YELLOW + "Your inventory size is " + invSize);
        } else {
            sender.sendMessage(ChatColor.RED
                + "This command can only be called by an inventory holder");
        }
        return true;
    }

}
