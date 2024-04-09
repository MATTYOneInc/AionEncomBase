package com.aionemu.gameserver.model.gameobjects;

import gnu.trove.map.hash.TIntObjectHashMap;

public enum MinionAction {
    ADOPT(0),
    DELETE(1),
    RENAME(2),
    LOCK(3),
    SPAWN(4),
    DISMISS(5),
    GROWTH(6),
    EVOLVE(7),
    COMBINE(8),
    SET_FUNCTION(9),
    USE_FUNCTION(10),
    CHARGE(11),
    FUNCTION_RENEW(12),
    STOP_FUNCTION(13),
    VIEW_MINION(14),
    UNKNOWN(255);
    private static TIntObjectHashMap<MinionAction> minionActions;

    static {
        minionActions = new TIntObjectHashMap<>();
        for (MinionAction action : values()) {
            minionActions.put(action.getActionId(), action);
        }
    }

    private int actionId;

    MinionAction(int actionId) {
        this.actionId = actionId;
    }

    public static MinionAction getActionById(int actionId) {
        MinionAction action = minionActions.get(actionId);
        return action != null ? action : UNKNOWN;
    }

    public int getActionId() {
        return actionId;
    }
}
