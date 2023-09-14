package datatransferobjects;

import main.clients.spells.Spell01;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Spell01DTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public float spellPosXWorld, spellPosYWorld;
    public float normalizedVectorX;
    public float normalizedVectorY;

    public final int spellID;
    public final int spellCasterClientID;

    public static List<Spell01DTO> listOfAllSpell01DTO = new ArrayList<>();

    public Spell01DTO(Spell01 spell01) {
        spellPosXWorld = spell01.spellPosXWorld;
        spellPosYWorld = spell01.spellPosYWorld;
        normalizedVectorX = spell01.normalizedVectorX;
        normalizedVectorY = spell01.normalizedVectorY;

        spellCasterClientID = spell01.spellCasterClientID;
        spellID = spell01.spellID;

        synchronized (listOfAllSpell01DTO) {
            listOfAllSpell01DTO.add(this);
        }
    }



}
