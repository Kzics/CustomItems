package com.kzics.customitems.commands.customitems.sub;

import com.kzics.customitems.CustomItems;
import com.kzics.customitems.commands.ICommand;
import org.bukkit.command.CommandSender;

public class GiveCommand implements ICommand {

    private final CustomItems customItems;
    public GiveCommand(CustomItems customItems){
        this.customItems = customItems;
    }

    @Override
    public String getName() {
        return "give";
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public String getPermission() {
        return "customitems.give";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if(!(args.length != 2)) return;


    }
}
