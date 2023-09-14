package main;

import main.clients.spells.Spell01;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class EnumContainer {


    public enum AllPlayerStates {
        IDLE_UP, IDLE_DOWN, IDLE_LEFT, IDLE_RIGHT,
        IDLE_UP_LEFT, IDLE_UP_RIGHT, IDLE_DOWN_LEFT, IDLE_DOWN_RIGHT,
        MOVING_UP, MOVING_DOWN, MOVING_LEFT, MOVING_RIGHT,
        MOVING_UP_LEFT, MOVING_UP_RIGHT, MOVING_DOWN_LEFT, MOVING_DOWN_RIGHT

    }

    public enum AllPlayableChampions {

        DON_OHL,
        BIG_HAIRY_SWEATY_DUDE

    }


    public abstract static class ServerClientConnectionCopyObjects {

        public static AllPlayerStates Current_Player_State_Shared;

        public static AllPlayableChampions PLayer_Champion_Shared;

        public static Boolean[] ArrayOfPlayerCreateSpellRequests = new Boolean[4];

        public static Point currentMousePosition;

        public static List<Spell01> listOfAllActiveSpellsCopy = new ArrayList<>();
    }
}
