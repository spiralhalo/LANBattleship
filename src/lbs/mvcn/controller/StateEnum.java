/*
 *  LAN Battleship
 *  Copyright (c) 2015, spiralhalo
 *  You are free to modify and reuse this program code for commercial and non-commercial purpose
 *  with the condition that you removed this license header or replaced it with your own.
 */
package lbs.mvcn.controller;

/**
 *
 * @author spiralhalo
 */
public enum StateEnum {
    NOT_INGAME,
    PREGAME,
    ARRANGING_SHIPS,
    WAITING_FOR_PLAYER_DECISION,
    DECISION_MADE_WAITING_FOR_SERVER,
    SERVER_REPLIED_EXECUTING_BEGIN_NEXT_ROUND,
    SERVER_DECLARED_GAME_END;
}
