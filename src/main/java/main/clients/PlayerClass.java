package main.clients;

import main.EnumContainer;
import main.clients.spells.Spell01;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerClass implements Serializable {


    public EnumContainer.AllPlayerStates Current_Player_State;
    public EnumContainer.AllPlayableChampions PlayerChampion;
    public float playerPosXWorld, playerPosYWorld;
    public int mouseClickXPos, mouseClickYPos;
    public float normalizedVectorX, normalizedVectorY;
    private final int playerMovespeed = 2;
    public float playerMovementStartingPosX, playerMovementStartingPosY;
    public float distanceToTravel;
    public boolean isPlayerMoving;
    private int playerFeetX, playerFeetY;
    //    counter how many players connected to move handler and based on that determine their starting position
    public static int counterOfConnectedClients = 0;
    public final int clientID;

    public int counterOfThisPlayerQSpells;

    public List<Spell01> listOfAllActive_Q_Spells = new ArrayList<>();


    public PlayerClass() {
        setPlayerChampion(EnumContainer.ServerClientConnectionCopyObjects.PLayer_Champion_Shared);
        Current_Player_State = EnumContainer.AllPlayerStates.IDLE_DOWN;
        clientID = counterOfConnectedClients;
        playerStartingPosition();

        counterOfConnectedClients++;

    }

    public void setPlayerChampion(EnumContainer.AllPlayableChampions champion) {
        PlayerChampion = champion;
        setPLayerFeetPos();
    }

    public void setPLayerFeetPos() {
        if (PlayerChampion.equals(EnumContainer.AllPlayableChampions.DON_OHL)) {
            playerFeetX = 36;
            playerFeetY = 68;
        } else if (PlayerChampion.equals(EnumContainer.AllPlayableChampions.BIG_HAIRY_SWEATY_DUDE)) {
            playerFeetX = 64;
            playerFeetY = 115;
        }
    }

    private void playerStartingPosition() {

        if (clientID == 0) {
            playerPosXWorld = 800;
            playerPosYWorld = 1900;
        } else if (clientID == 1) {
            playerPosXWorld = 600;
            playerPosYWorld = 1900;
        } else if (clientID == 2) {
            playerPosXWorld = 100;
            playerPosYWorld = 600;

        }
    }

    public void moveController() {

        if (distanceToTravel > 0) {

            playerPosXWorld += (playerMovespeed * normalizedVectorX);
            playerPosYWorld += (playerMovespeed * normalizedVectorY);
            distanceToTravel -= (float) (playerMovespeed * Math.sqrt(normalizedVectorX * normalizedVectorX + normalizedVectorY * normalizedVectorY));
            isPlayerMoving = true;
            setCurrent_Player_State();
        } else {
            isPlayerMoving = false;
            setCurrent_Player_State();
        }
    }

    public void setCurrent_Player_State() {
// double angle = atan2(y2 - y1, x2 - x1) * 180 / PI;".
        double movementAngle = Math.atan2(mouseClickYPos - playerMovementStartingPosY, mouseClickXPos - playerMovementStartingPosX);
        double angleDegrees = Math.toDegrees(movementAngle);
        if (angleDegrees < 0) {
            angleDegrees += 360;
        }
        if (isPlayerMoving) {
            if (angleDegrees >= 22.5 && angleDegrees < 67.5) {
                Current_Player_State = EnumContainer.AllPlayerStates.MOVING_DOWN_RIGHT;

            } else if (angleDegrees >= 67.5 && angleDegrees < 112.5) {
                Current_Player_State = EnumContainer.AllPlayerStates.MOVING_DOWN;

            } else if (angleDegrees >= 112.5 && angleDegrees < 157.5) {
                Current_Player_State = EnumContainer.AllPlayerStates.MOVING_DOWN_LEFT;

            } else if (angleDegrees >= 157.5 && angleDegrees < 202.5) {
                Current_Player_State = EnumContainer.AllPlayerStates.MOVING_LEFT;

            } else if (angleDegrees >= 202.5 && angleDegrees < 247.5) {
                Current_Player_State = EnumContainer.AllPlayerStates.MOVING_UP_LEFT;

            } else if (angleDegrees >= 247.5 && angleDegrees < 292.5) {
                Current_Player_State = EnumContainer.AllPlayerStates.MOVING_UP;

            } else if (angleDegrees >= 292.5 && angleDegrees < 337.5) {
                Current_Player_State = EnumContainer.AllPlayerStates.MOVING_UP_RIGHT;

            } else {
                Current_Player_State = EnumContainer.AllPlayerStates.MOVING_RIGHT;
            }
        } else {
            switch (Current_Player_State) {

                case MOVING_UP -> {
                    Current_Player_State = EnumContainer.AllPlayerStates.IDLE_UP;
                }
                case MOVING_DOWN -> {
                    Current_Player_State = EnumContainer.AllPlayerStates.IDLE_DOWN;
                }
                case MOVING_LEFT -> {
                    Current_Player_State = EnumContainer.AllPlayerStates.IDLE_LEFT;
                }
                case MOVING_RIGHT -> {
                    Current_Player_State = EnumContainer.AllPlayerStates.IDLE_RIGHT;
                }
                case MOVING_UP_LEFT -> {
                    Current_Player_State = EnumContainer.AllPlayerStates.IDLE_UP_LEFT;
                }
                case MOVING_UP_RIGHT -> {
                    Current_Player_State = EnumContainer.AllPlayerStates.IDLE_UP_RIGHT;
                }
                case MOVING_DOWN_LEFT -> {
                    Current_Player_State = EnumContainer.AllPlayerStates.IDLE_DOWN_LEFT;
                }
                case MOVING_DOWN_RIGHT -> {
                    Current_Player_State = EnumContainer.AllPlayerStates.IDLE_DOWN_RIGHT;
                }
            }

        }

    }


    public void setVectorForPlayerMovement() {
        setPlayerMovementStartingPosition(playerPosXWorld, playerPosYWorld);
        float vectorX = mouseClickXPos - (playerPosXWorld + playerFeetX);
        float vectorY = mouseClickYPos - (playerPosYWorld + playerFeetY);
        float magnitude = (float) Math.sqrt(vectorX * vectorX + vectorY * vectorY);
        distanceToTravel = magnitude;

        normalizedVectorX = (vectorX / magnitude);
        normalizedVectorY = (vectorY / magnitude);
    }

    public void setPlayerMovementStartingPosition(float playerPosXWorld, float playerPosYWorld) {
        this.playerMovementStartingPosX = playerPosXWorld + playerFeetX;
        this.playerMovementStartingPosY = playerPosYWorld + playerFeetY;
    }

    public void spellCastController() {

        if (EnumContainer.ServerClientConnectionCopyObjects.ArrayOfPlayerCreateSpellRequests[0]) {
            new Spell01(this);
        }
        if (EnumContainer.ServerClientConnectionCopyObjects.ArrayOfPlayerCreateSpellRequests[1]) {
            new Spell01(this);

        }
        if (EnumContainer.ServerClientConnectionCopyObjects.ArrayOfPlayerCreateSpellRequests[2]) {
            new Spell01(this);

        }
        if (EnumContainer.ServerClientConnectionCopyObjects.ArrayOfPlayerCreateSpellRequests[3]) {
            new Spell01(this);

        }
    }

}
