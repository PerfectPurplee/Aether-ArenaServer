package main;

import java.io.*;

public class PlayerMovementHandler implements Serializable {


    public EnumContainer.PlayerState Current_Player_State;
    public float playerPosXWorld, playerPosYWorld;
    public int mouseClickXPos, mouseClickYPos;
    public float normalizedVectorX, normalizedVectorY;
    private final int playerMovespeed = 2;
    public float playerMovementStartingPosX, playerMovementStartingPosY;

    //    counter how many players connected to move handler and based on that determine their starting position
    public static int counter = 0;
    public int clientID;


    public PlayerMovementHandler() {
        Current_Player_State = EnumContainer.PlayerState.IDLE_DOWN;
        playerStartingPosition();
        clientID = counter;
        counter++;

    }

    private void playerStartingPosition() {

        if (counter == 0) {
            playerPosXWorld = 100;
            playerPosYWorld = 100;
        } else if (counter == 1) {
            playerPosXWorld = 600;
            playerPosYWorld = 100;
        } else if (counter == 2) {
            playerPosXWorld = 100;
            playerPosYWorld = 600;

        }
    }


    public void moveController() {
//
//        Ruch na lewej gornej cwiartce liczac od postaci
        if (playerMovementStartingPosX > mouseClickXPos && playerMovementStartingPosY > mouseClickYPos) {
            if (playerPosXWorld > mouseClickXPos && playerPosYWorld > mouseClickYPos) {
                Current_Player_State = EnumContainer.PlayerState.MOVING_UP;
                playerPosXWorld += (playerMovespeed * normalizedVectorX);
                playerPosYWorld += (playerMovespeed * normalizedVectorY);
            }
        } else if (playerMovementStartingPosX < mouseClickXPos && playerMovementStartingPosY < mouseClickYPos) {
            if (playerPosXWorld < mouseClickXPos && playerPosYWorld < mouseClickYPos) {
                Current_Player_State = EnumContainer.PlayerState.MOVING_DOWN;
                playerPosXWorld += (playerMovespeed * normalizedVectorX);
                playerPosYWorld += (playerMovespeed * normalizedVectorY);
            }
        } else if (playerMovementStartingPosX < mouseClickXPos && playerMovementStartingPosY > mouseClickYPos) {
            if (playerPosXWorld < mouseClickXPos && playerPosYWorld > mouseClickYPos) {
                Current_Player_State = EnumContainer.PlayerState.MOVING_RIGHT;
                playerPosXWorld += (playerMovespeed * normalizedVectorX);
                playerPosYWorld += (playerMovespeed * normalizedVectorY);
            }
        } else if (playerMovementStartingPosX > mouseClickXPos && playerMovementStartingPosY < mouseClickYPos) {
            if (playerPosXWorld > mouseClickXPos && playerPosYWorld < mouseClickYPos) {
                Current_Player_State = EnumContainer.PlayerState.MOVING_LEFT;
                playerPosXWorld += (playerMovespeed * normalizedVectorX);
                playerPosYWorld += (playerMovespeed * normalizedVectorY);
            }
        }
    }

    public int playerSpriteController() {
        switch (Current_Player_State) {
            case IDLE_UP -> {
                return 1;
            }
            case IDLE_DOWN -> {
                return 2;
            }
            case IDLE_LEFT -> {
                return 3;
            }
            case IDLE_RIGHT -> {
                return 4;
            }
            case MOVING_UP -> {
                return 5;
            }
            case MOVING_DOWN -> {
                return 6;
            }
            case MOVING_LEFT -> {
                return 7;
            }
            case MOVING_RIGHT -> {
                return 8;
            }
            default -> {
                return 0;
            }
        }
    }

    public void setVectorForPlayerMovement() {
        float vectorX = mouseClickXPos - playerPosXWorld;
        float vectorY = mouseClickYPos - playerPosYWorld;
        float magnitude = (float) Math.sqrt(vectorX * vectorX + vectorY * vectorY);

        normalizedVectorX = (vectorX / magnitude);
        normalizedVectorY = (vectorY / magnitude);
    }

}
