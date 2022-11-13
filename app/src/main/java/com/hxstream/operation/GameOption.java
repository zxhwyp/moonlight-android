package com.hxstream.operation;

/**
 * Created by zhb5145 on 2020/12/28
 */
public class GameOption {

    public GameOption(int gameId) {
        this.gameId = gameId;
    }

    public final static int GAME_TYPE_STEAM = 0;
    public int gameType;
    public boolean offlineGame = false;
    public String steamAccount;
    public String steamPassword;
    public int gameId;
}
