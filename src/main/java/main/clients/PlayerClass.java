package main.clients;

import main.EnumContainer;
import main.clients.spells.QSpell;
import main.clients.spells.Ultimate;

import java.awt.geom.Rectangle2D;
import java.io.Serializable;


public class PlayerClass implements Serializable {

    public PlayerHitbox playerHitbox;
    public EnumContainer.AllPlayerStates Current_Player_State;
    public EnumContainer.AllPlayableChampions PlayerChampion;
    public float playerPosXWorld, playerPosYWorld;
    public int mouseClickXPos, mouseClickYPos;
    public float normalizedVectorX, normalizedVectorY;
    private int playerMovespeed = 2;
    public float playerMovementStartingPosX, playerMovementStartingPosY;
    public float distanceToTravel;
    public boolean isPlayerMoving;
    private int playerFeetX, playerFeetY;
    //    counter how many players connected to move handler and based on that determine their starting position
    public static int counterOfConnectedClients = 0;
    public final int clientID;
    public final int maxHealth = 500;
    public int currentHealth;
    public boolean isPlayerStateLocked;
    public boolean isPlayerDead;

    private final long RESPAWN_COOLDOWN = 5000;
    private long playerDeathTime;
    public long RespawnCurrentCooldown;


    public int counterOfThisPlayerQSpells;

    private int animationTick;
    private final int animationSpeed = 15;
    public int animationIndexDashing;


    public PlayerClass() {
        setPlayerChampion(EnumContainer.ServerClientConnectionCopyObjects.PLayer_Champion_Shared);
        Current_Player_State = EnumContainer.AllPlayerStates.IDLE_RIGHT;
        clientID = counterOfConnectedClients;
        playerStartingPosition();
        playerHitbox = new PlayerHitbox();
        currentHealth = maxHealth;
        isPlayerStateLocked = false;

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

        if (distanceToTravel > 2) {

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

        if (!isPlayerStateLocked) {
            if (isPlayerMoving) {
                if (mouseClickXPos < playerPosXWorld + playerFeetX) {
                    Current_Player_State = EnumContainer.AllPlayerStates.MOVING_LEFT;
                } else {
                    Current_Player_State = EnumContainer.AllPlayerStates.MOVING_RIGHT;
                }
            } else {
                switch (Current_Player_State) {
                    case MOVING_LEFT, DASHING_LEFT, DEATH_LEFT -> {
                        Current_Player_State = EnumContainer.AllPlayerStates.IDLE_LEFT;
                    }
                    case MOVING_RIGHT, DASHING_RIGHT, DEATH_RIGHT -> {
                        Current_Player_State = EnumContainer.AllPlayerStates.IDLE_RIGHT;
                    }
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
            boolean shouldCreateSpellE, boolean shouldCreateSpellR, boolean shouldDash, int spellID, double spriteAngle) {

        if (shouldCreateSpellQ) {
            new QSpell(this, spellID, spriteAngle);
        }
        if (shouldCreateSpellW) {
            new QSpell(this, spellID, spriteAngle);

        }
        if (shouldCreateSpellE) {
            new QSpell(this, spellID, spriteAngle);

        }
        if (shouldCreateSpellR) {
            new Ultimate(this, spellID, spriteAngle);

        }
        if (shouldDash) {
            isPlayerStateLocked = true;
            Current_Player_State = setDashStateForAnimation();
            playerMovespeed = 4;
        }
    }

    private EnumContainer.AllPlayerStates setDashStateForAnimation() {
        switch (Current_Player_State) {

            case IDLE_LEFT, MOVING_LEFT -> {
                return EnumContainer.AllPlayerStates.DASHING_LEFT;
            }
            default -> {
                return EnumContainer.AllPlayerStates.DASHING_RIGHT;
            }
        }
    }

    // Animation controller in clinet. Length of dash animation determines how long it lasts.
    public void dashController() {
        if (Current_Player_State == EnumContainer.AllPlayerStates.DASHING_LEFT || Current_Player_State == EnumContainer.AllPlayerStates.DASHING_RIGHT) {
            animationTick++;
            if (animationTick >= animationSpeed) {
                if (animationIndexDashing < 3) animationIndexDashing++;
                else {
                    playerMovespeed = 2;
                    isPlayerStateLocked = false;
                    setCurrent_Player_State();
                    animationIndexDashing = 0;
                }

                animationTick = 0;
            }
        }
    }

    public void updateReviveCooldownAndRespawnPlayer() {
        RespawnCurrentCooldown = (RESPAWN_COOLDOWN - (System.currentTimeMillis() - playerDeathTime));

        if (RespawnCurrentCooldown <= 0) {
            isPlayerDead = false;
            currentHealth = maxHealth;

        }


    }

    public void checkIsPlayerDead() {
        if (currentHealth <= 0) {
            isPlayerDead = true;
            Current_Player_State = setDEATHStateForAnimation();
            playerDeathTime = System.currentTimeMillis();

        }
    }

    private EnumContainer.AllPlayerStates setDEATHStateForAnimation() {
        switch (Current_Player_State) {

            case IDLE_LEFT, MOVING_LEFT, DASHING_LEFT -> {
                return EnumContainer.AllPlayerStates.DEATH_LEFT;
            }
            case IDLE_RIGHT, MOVING_RIGHT, DASHING_RIGHT -> {
                return EnumContainer.AllPlayerStates.DEATH_RIGHT;
            }
            default -> {
                return EnumContainer.AllPlayerStates.DASHING_RIGHT;
            }
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
                    256 - (hitboxOffsetX * 2),
                    256 - (hitboxOffsetYAbovePlayerSprite + 40));
        }

    }

}
