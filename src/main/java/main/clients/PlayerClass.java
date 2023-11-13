package main.clients;

import main.EnumContainer;
import main.clients.spells.Spell01;

import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlayerClass implements Serializable {

    public PlayerHitbox playerHitbox;
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
        Current_Player_State = EnumContainer.AllPlayerStates.IDLE_RIGHT;
        clientID = counterOfConnectedClients;
        playerStartingPosition();
        playerHitbox = new PlayerHitbox();


        counterOfConnectedClients++;

    }

    public void setPlayerChampion(EnumContainer.AllPlayableChampions champion) {
        PlayerChampion = champion;
        setPLayerFeetPos();
    }

    public void setPLayerFeetPos() {
        playerFeetX = 128;
        playerFeetY = 220;
    }

    private void playerStartingPosition() {

        if (clientID == 0) {
            playerPosXWorld = 400;
            playerPosYWorld = 400;
        } else if (clientID == 1) {
            playerPosXWorld = 600;
            playerPosYWorld = 400;
        } else if (clientID == 2) {
            playerPosXWorld = 800;
            playerPosYWorld = 400;

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

        if (isPlayerMoving) {
            if (mouseClickXPos < playerPosXWorld + playerFeetX) {
                Current_Player_State = EnumContainer.AllPlayerStates.MOVING_LEFT;
            } else {
                Current_Player_State = EnumContainer.AllPlayerStates.MOVING_RIGHT;
            }
        } else {
            switch (Current_Player_State) {
                case MOVING_LEFT -> {
                    Current_Player_State = EnumContainer.AllPlayerStates.IDLE_LEFT;
                }
                case MOVING_RIGHT -> {
                    Current_Player_State = EnumContainer.AllPlayerStates.IDLE_RIGHT;
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

    public void spellCastController(
            boolean shouldCreateSpellQ, boolean shouldCreateSpellW,
            boolean shouldCreateSpellE, boolean shouldCreateSpellR, int spellID) {

        if (shouldCreateSpellQ) {
            new Spell01(this, spellID);
        }
        if (shouldCreateSpellW) {
            new Spell01(this, spellID);

        }
        if (shouldCreateSpellE) {
            new Spell01(this, spellID);

        }
        if (shouldCreateSpellR) {
            new Spell01(this, spellID);

        }
    }

    public void updatePlayerHitboxWorld() {
        playerHitbox.x = playerPosXWorld + hitboxOffsetX;
        playerHitbox.y = playerPosYWorld + hitboxOffsetYAbovePlayerSprite;

    }

    private final int hitboxOffsetX = 90;
    private final int hitboxOffsetYAbovePlayerSprite = 130;

    public class PlayerHitbox extends Rectangle2D.Float {

        PlayerHitbox() {
            super(
                    playerPosXWorld + hitboxOffsetX,
                    playerPosYWorld + hitboxOffsetYAbovePlayerSprite,
                    (256 - hitboxOffsetX * 2),
                    256 - (hitboxOffsetYAbovePlayerSprite + 25));
        }

    }

}
