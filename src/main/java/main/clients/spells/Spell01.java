package main.clients.spells;


import datatransferobjects.Spell01DTO;
import main.ServerEngine;
import main.clients.PlayerClass;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static main.EnumContainer.ServerClientConnectionCopyObjects;


public class Spell01 {

    private Spell01DTO spell01DTO;

    private final int NUMBER_OF_SPRITES = 4;
    private final int SPEED = 2;
    //  object starting position on screen. Character pos + (vector * int)
    public float spellPosXWorld, spellPosYWorld;
    public float normalizedVectorX;
    public float normalizedVectorY;
    public int mousePosXWorld, mousePosYWorld;


    PlayerClass playerCastingThisSpell;

    public final int spellCasterClientID;
    public final int spellID;

    public static List<Spell01> listOfActiveSpell01s = new ArrayList<>();

    public Spell01(PlayerClass playerCastingThisSpell, int spellID) {
        this.playerCastingThisSpell = playerCastingThisSpell;
        this.spellID = spellID;
        spellCasterClientID = playerCastingThisSpell.clientID;

        getVector();
        spellPosXWorld = ((playerCastingThisSpell.playerHitbox.x + (playerCastingThisSpell.playerHitbox.width / 2 - 32)
                + (normalizedVectorX * 125)));
        spellPosYWorld = ((playerCastingThisSpell.playerHitbox.y + playerCastingThisSpell.playerHitbox.height / 2 - 32)
                + (normalizedVectorY * 125));



        synchronized (listOfActiveSpell01s) {
            listOfActiveSpell01s.add(this);
        }
        synchronized (playerCastingThisSpell.listOfAllActive_Q_Spells) {
            playerCastingThisSpell.listOfAllActive_Q_Spells.add(this);
        }

//        spell01DTO = new Spell01DTO(this);
    }


    private void getVector() {
        mousePosXWorld = (int) (ServerClientConnectionCopyObjects.currentMousePosition.getX());
        mousePosYWorld = (int) (ServerClientConnectionCopyObjects.currentMousePosition.getY());
        float vectorX = (float)
                (mousePosXWorld - (playerCastingThisSpell.playerHitbox.x + playerCastingThisSpell.playerHitbox.width / 2));
        float vectorY = (float)
                (mousePosYWorld - (playerCastingThisSpell.playerHitbox.y + playerCastingThisSpell.playerHitbox.height / 2));
        float magnitude = (float) Math.sqrt(vectorX * vectorX + vectorY * vectorY);
        normalizedVectorX = (vectorX / magnitude);
        normalizedVectorY = (vectorY / magnitude);
    }

    private void spellPositionUpdate() {
        spellPosXWorld += (normalizedVectorX * SPEED);
        spellPosYWorld += (normalizedVectorY * SPEED);
//        spell01DTO.spellPosXWorld = spellPosXWorld;
//        spell01DTO.spellPosYWorld = spellPosYWorld;

    }

    public static void updateAllSpells01() {
        synchronized (listOfActiveSpell01s) {
            listOfActiveSpell01s = listOfActiveSpell01s.stream().filter(spell01 ->
                    spell01.spellPosXWorld >= -64 && spell01.spellPosYWorld >= -64 &&
                            spell01.spellPosXWorld <= ServerEngine.gameMapWidth + 64 &&
                            spell01.spellPosYWorld <= ServerEngine.gameMapHeight + 64).collect(Collectors.toList());

            listOfActiveSpell01s.forEach(spell01 -> {
                spell01.spellPositionUpdate();
//                System.out.println("Caster:  " + spell01.spellCasterClientID +  "SpellID: " + spell01.spellID + "Pos X: "
//                        + spell01.spellPosXWorld + "Pos Y" + spell01.spellPosYWorld);

            });
        }
    }
}