package main.clients.spells;

import main.clients.PlayerClass;

public class Ultimate extends QSpell {
    public Ultimate(PlayerClass playerCastingThisSpell, int spellID, double spriteAngle) {
        super(playerCastingThisSpell, spellID, spriteAngle);
    }


    @Override
    protected void setInitialProjectilePosition() {
        spellPosXWorld = ((playerCastingThisSpell.playerHitbox.x + playerCastingThisSpell.playerHitbox.width / 2 - 144
                + (normalizedVectorX * 10)));
        spellPosYWorld = (playerCastingThisSpell.playerHitbox.y + playerCastingThisSpell.playerHitbox.height / 2 - 144
                + (normalizedVectorY * 10));
    }

    @Override
    protected void setHitboxWidthandHeight() {
        WidthAfterScaling = 288;
        HeightAfterScaling = 288;
    }
}
