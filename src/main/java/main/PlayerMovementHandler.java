package main;

import java.io.*;

public class PlayerMovementHandler implements Serializable {


    public EnumContainer.AllPlayerStates Current_Player_State;
    public float playerPosXWorld, playerPosYWorld;
    public int mouseClickXPos, mouseClickYPos;
    public float normalizedVectorX, normalizedVectorY;
    private final int playerMovespeed = 2;
    public float playerMovementStartingPosX, playerMovementStartingPosY;
    public float distanceToTravel;
    public boolean isPlayerMoving;
    public int playerFeetX = 64, playerFeetY = 115;
    //    counter how many players connected to move handler and based on that determine their starting position
    public static int counter = 0;
    public int clientID;


    public PlayerMovementHandler() {
        Current_Player_State = EnumContainer.AllPlayerStates.IDLE_DOWN;
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
        float vectorX = mouseClickXPos - (playerPosXWorld + playerFeetX);
        float vectorY = mouseClickYPos - (playerPosYWorld + playerFeetY);
        float magnitude = (float) Math.sqrt(vectorX * vectorX + vectorY * vectorY);
        distanceToTravel = magnitude;

        normalizedVectorX = (vectorX / magnitude);
        normalizedVectorY = (vectorY / magnitude);
    }

}
