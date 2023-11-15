package datatransferobjects;

import main.clients.spells.QSpell;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Spell01DTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public float spellPosXWorld, spellPosYWorld;
    public float normalizedVectorX;
    public float normalizedVectorY;

    public final int spellID;
    public final int spellCasterClientID;

    public static CopyOnWriteArrayList<Spell01DTO> listOfAllSpell01DTO = new CopyOnWriteArrayList<>();

    public Spell01DTO(QSpell QSpell) {
        spellPosXWorld = QSpell.spellPosXWorld;
        spellPosYWorld = QSpell.spellPosYWorld;
        normalizedVectorX = QSpell.normalizedVectorX;
        normalizedVectorY = QSpell.normalizedVectorY;

        spellCasterClientID = QSpell.spellCasterClientID;
        spellID = QSpell.spellID;


        listOfAllSpell01DTO.add(this);

    }


}
