package com.aionemu.gameserver.services.minion;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.stats.calc.StatOwner;
import com.aionemu.gameserver.model.stats.calc.functions.IStatFunction;
import com.aionemu.gameserver.model.stats.calc.functions.StatAddFunction;
import com.aionemu.gameserver.model.stats.calc.functions.StatRateFunction;
import com.aionemu.gameserver.model.templates.minion.MinionStatAttribute;
import com.aionemu.gameserver.model.templates.minion.MinionTemplate;
import com.aionemu.gameserver.skillengine.change.Func;

import java.util.ArrayList;
import java.util.List;

public class MinionStatAddServices implements StatOwner {
    private List<IStatFunction> functions = new ArrayList<>();
    private MinionTemplate minionTemplate;

    public MinionStatAddServices() {
    }

    public boolean hasBuff() {
        return !functions.isEmpty();
    }

    public void applyEffect(Player player, int minionId, int minionPoint) {
        minionTemplate = DataManager.MINION_DATA.getMinionTemplate(minionId);
        if (minionTemplate == null || hasBuff()) {
            return;
        }
        List<MinionStatAttribute> attribute = null;
        if (player.isMagicalTypeClass()) {
            attribute = minionTemplate.getMagicalAttr();
        } else {
            attribute = minionTemplate.getPhysicalAttr();
        }

        for (MinionStatAttribute minionAttribute : attribute) {
            if (minionAttribute.getFunc().equals(Func.PERCENT)) {
                functions.add(new StatRateFunction(minionAttribute.getStat(), minionAttribute.getValue(), true));
            } else {
                int statVal = minionAttribute.getValue();

                if(minionPoint >= 25000 && minionPoint < 50000){
                    switch (minionAttribute.getStat()){
                        case MAXHP:
                            statVal = minionAttribute.getValue() + 267;
                            break;
                        case PHYSICAL_ATTACK:
                            statVal = minionAttribute.getValue() + 4;
                            break;
                        case PHYSICAL_CRITICAL:
                            statVal = minionAttribute.getValue() + 7;
                            break;
                        case PHYSICAL_ACCURACY:
                            statVal = minionAttribute.getValue() + 11;
                            break;
                        case BOOST_MAGICAL_SKILL:
                            statVal = minionAttribute.getValue() + 16;
                            break;
                        case MAGICAL_ACCURACY:
                            statVal = minionAttribute.getValue() + 9;
                            break;
                        case MAGICAL_CRITICAL:
                            statVal = minionAttribute.getValue() + 7;
                            break;
                    }
                }else if(minionPoint >= 50000){
                    switch (minionAttribute.getStat()){
                        case MAXHP:
                            statVal = minionAttribute.getValue() + 534;
                            break;
                        case PHYSICAL_ATTACK:
                            statVal = minionAttribute.getValue() + 8;
                            break;
                        case PHYSICAL_CRITICAL:
                            statVal = minionAttribute.getValue() + 14;
                            break;
                        case PHYSICAL_ACCURACY:
                            statVal += minionAttribute.getValue() + 22;
                            break;
                        case BOOST_MAGICAL_SKILL:
                            statVal = minionAttribute.getValue() + 32;
                            break;
                        case MAGICAL_ACCURACY:
                            statVal = minionAttribute.getValue() + 18;
                            break;
                        case MAGICAL_CRITICAL:
                            statVal = minionAttribute.getValue() + 14;
                            break;
                    }
                }
                functions.add(new StatAddFunction(minionAttribute.getStat(), statVal, true));
            }
        }
        player.setBonus(true);
        player.getGameStats().addEffect(this, functions);
    }

    public void endEffect(Player player) {
        functions.clear();
        player.setBonus(false);
        player.getGameStats().endEffect(this);
    }
}