package com.kzics.customitems.commands.customitems;

import com.kzics.customitems.CustomItems;
import com.kzics.customitems.commands.CommandBase;
import com.kzics.customitems.commands.customitems.sub.GiveCommand;
import com.kzics.customitems.commands.customitems.sub.ReloadCommand;

public class CustomItemsCommand extends CommandBase {

    public CustomItemsCommand(CustomItems customItems){
        registerSubCommand("give", new GiveCommand(customItems));
        registerSubCommand("reload", new ReloadCommand(customItems));
    }
}
