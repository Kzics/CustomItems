package com.kzics.customitems.commands.customitems;

import com.kzics.customitems.commands.CommandBase;

public class CustomItemsCommand extends CommandBase {

    public CustomItemsCommand(){
        registerSubCommand("give");
        registerSubCommand("reload");
    }
}
