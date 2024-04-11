package main.clients.spells;


import datatransferobjects.Spell01DTO;
import main.ServerEngine;
import main.clients.PlayerClass;

import java.awt.geom.Rectangle2D;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

import static main.EnumContainer.ServerClientConnectionCopyObjects;


public class QSpell {

    private Spell01DTO spell01DTO;

    private final int SPEED = 4;
    private final int RANGE = 1000;
    private float distanceTraveled = 0;
    //  object starting position on screen. Character pos + (vector * int)
    public float spellPosXWorld, spellPosYWorld;
    public float normalizedVectorX;
    public float normalizedVectorY;
    public int mousePosXWorld, mousePosYWorld;
    public double spriteAngle;


    PlayerClass playerCastingThisSpell;
    public Spell01Hitbox spell01Hitbox;

    public final int spellCasterClientID;
    public final int spellID;

    public boolean playerGotHit;
    private boolean flagForRemoval;

    int WidthAfterScaling;
    int HeightAfterScaling;


    public static CopyOnWriteArrayList<QSpell> listOfActiveQSpells = new CopyOnWriteArrayList<>();

    public QSpell(PlayerClass playerCastingThisSpell, int spellID, double spriteAngle) {
        setHitboxWidthandHeight();
        this.playerCastingThisSpell = playerCastingThisSpell;
        this.spellID = spellID;
        spellCasterClientID = playerCastingThisSpell.clientID;
        this.spriteAngle = spriteAngle;
        playerGotHit = false;
        flagForRemoval = false;

        getVector();
        setInitialProjectilePosition();

        spell01Hitbox = new Spell01Hitbox();


        listOfActiveQSpells.add(this);



//        spell01DTO = new Spell01DTO(this);
    }

    protected void setInitialProjectilePosition() {
        spellPosXWorld = ((playerCastingThisSpell.playerHitbox.x + playerCastingThisSpell.playerHitbox.width / 2 - 48
                + (normalizedVectorX * 10)));
        spellPosYWorld = (playerCastingThisSpell.playerHitbox.y + playerCastingThisSpell.playerHitbox.height / 2 - 48
                + (normalizedVectorY * 10));

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

    private void calculateDistanceTraveled(float x1, float x2, float y1, float y2) {
        distanceTraveled += (float) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    private void spellPositionUpdate() {
        calculateDistanceTraveled(spellPosXWorld, (spellPosXWorld + normalizedVectorX * SPEED), spellPosYWorld, (spellPosYWorld + normalizedVectorY * SPEED));
        if (distanceTraveled <= RANGE && !playerGotHit) {
            spellPosXWorld += (normalizedVectorX * SPEED);
            spellPosYWorld += (normalizedVectorY * SPEED);
        } else if (spell01Hitbox != null) {
            spell01Hitbox = null;
            flagForRemoval = true;
        }
    }

    public static void updateAllSpells01() {
        listOfActiveQSpells.removeIf(spell01 ->
                (spell01.spellPosXWorld < -64 ||
                        spell01.spellPosYWorld < -64 ||
                        spell01.spellPosXWorld > ServerEngine.gameMapWidth + 64 ||
                        spell01.spellPosYWorld > ServerEngine.gameMapHeight + 64 ||
                        spell01.flagForRemoval)
        );

        listOfActiveQSpells.forEach(spell01 -> {
            spell01.spellPositionUpdate();
            spell01.updateSpellHitboxWorld();
//                System.out.println("Caster:  " + spell01.spellCasterClientID +  "SpellID: " + spell01.spellID + "Pos X: "
//                        + spell01.spellPosXWorld + "Pos Y" + spell01.spellPosYWorld);

        });
    }


    public void updateSpellHitboxWorld() {
        if (Objects.nonNull(spell01Hitbox)) {
            spell01Hitbox.x = spellPosXWorld;
            spell01Hitbox.y = spellPosYWorld;
        }
    }


    protected void setHitboxWidthandHeight() {
        WidthAfterScaling = 96;
        HeightAfterScaling = 96;
    }


    public class Spell01Hitbox extends Rectangle2D.Float {

        Spell01Hitbox() {
            super(spellPosXWorld, spellPosYWorld, WidthAfterScaling, HeightAfterScaling);
        }

    }
}