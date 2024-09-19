package com.kzics.customitems.utils;

import me.ulrich.clans.data.ClanData;
import me.ulrich.clans.interfaces.ClanAPI;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

public class ClanUtils {

    private final ClanAPI clanAPI;

    public ClanUtils(ClanAPI clanAPI) {
        this.clanAPI = clanAPI;
    }

    public List<Player> getPlayersNotInSameClan(Player player, double radius) {

        Optional<ClanData> playerClan = clanAPI.getClan(player.getUniqueId());

        if (playerClan.isEmpty()) {
            return List.of();
        }

        ClanData clanData = playerClan.get();

        return player.getLocation().getNearbyPlayers(radius)
                .stream()
                .filter(p -> clanData.getMembers().contains(p.getUniqueId()))
                .toList();
    }

    public boolean isInClan(Player player, Player target){
        Optional<ClanData> playerClan = clanAPI.getClan(player.getUniqueId());

        if (playerClan.isEmpty()) {
            return false;
        }

        ClanData clanData = playerClan.get();

        return clanData.getMembers().contains(target.getUniqueId());
    }
}