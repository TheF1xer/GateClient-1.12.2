package me.thef1xer.gateclient.managers;

import me.thef1xer.gateclient.GateClient;
import me.thef1xer.gateclient.util.DirectoryUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class FriendsManager {
    public final List<String> friendsNameList = new ArrayList<>();
    public final File FRIENDS_FILE = new File(DirectoryUtil.GATE_FOLDER, "friends.txt");

    public void init() {
        loadFriendList();
    }

    public void loadFriendList() {
        if (!FRIENDS_FILE.exists()) {
            saveFriendList();
            return;
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(FRIENDS_FILE));

            String currentLine = reader.readLine();
            while (currentLine != null) {
                friendsNameList.add(currentLine);
                currentLine = reader.readLine();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveFriendList() {
        try {
            FileWriter writer = new FileWriter(FRIENDS_FILE);

            for (String friendName : friendsNameList) {
                writer.write(friendName + "\n");
            }

            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isFriend(String name) {
        for (String friendName : GateClient.getGate().friendsManager.friendsNameList) {
            if (friendName.equalsIgnoreCase(name)) {
                return true;
            }
        }

        return false;
    }
}
