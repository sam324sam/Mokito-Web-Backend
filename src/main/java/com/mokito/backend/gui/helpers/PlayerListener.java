package com.mokito.backend.gui.helpers;

public interface PlayerListener {
    void onPlayerConnected(String playerName);
    void onPlayerDisconnected(String playerName);
}
