package com.ftn.slagalica.util;

import com.ftn.slagalica.data.model.Player;

import java.util.Comparator;

public class RankComparator implements Comparator<Player> {
    @Override
    public int compare(Player p1, Player p2) {
        int criteriaP1 = p1.getStars();
        int criteriaP2 = p2.getStars();
        if (criteriaP1 == criteriaP2) return 0;
        else return criteriaP1 > criteriaP2 ? -1 : 1;
    }
}
