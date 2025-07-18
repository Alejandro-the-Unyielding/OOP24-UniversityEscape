package it.unibo.exam.utility.generator;

import it.unibo.exam.model.entity.Npc;
import it.unibo.exam.model.entity.RoamingNpc;
import it.unibo.exam.utility.geometry.Point2D;
import it.unibo.exam.model.entity.enviroments.Room;
import it.unibo.exam.model.entity.strategy.MovementStrategy;
import it.unibo.exam.model.entity.strategy.RandomWalkStrategy;

/**
 * NPC generator.
 */
public final class NpcGenerator extends EntityGenerator<Npc> {

    private static final int X = 50;
    private static final int Y = 80;
    private static final int DELTA =  100;


    private static final String[] NAMES = {
        "Mario the Gardener",    // room 1
        "R2D2",     // room 2
        "Coach Andrew",                 // room 3
        "Barista Bro",           // room 4
        "Einstein",         // room 5
    };

    private static final String[] DESCRIPTIONS = {
        "Il giardiniere dell'università, esperto di piante e natura.",
        "R2D2 sempre pronto a risolvere problemi.",
        "Allenatore energico e motivante.",
        "Barista simpatica che conosce tutti gli studenti.",
        "Docente di matematica, appassionata e disponibile.",
    };

    private static final String[] DIALOGUES = {
        "Benvenuto nel giardino! Raccogli tutte le gocce d'acqua con la bottiglia per aiutare le piante. ",
        "Bzz bzz! Trova l'uscita dal labirinto del laboratorio informatico.",
        "Pronto per una sfida fisica? Colpisci tutti i dischi per vincere!",
        "Ciao! Mescola i drink correttamente per servire i clienti del bar.",
        "Ciao! Risolvi il quiz per dimostrare le tue conoscenze.",
    };

    /**
     * Constructor for NpcGenerator.
     *
     * @param environmentSize the size of the environment
     */
    public NpcGenerator(final Point2D environmentSize) {
        super(environmentSize);
    }

    /**
     * Generates an interactable NPC based on the given room ID.
     * Expects room IDs 1–5; maps to index ID-1 in the arrays.
     *
     * @param id the ID of the room (1–5)
     * @return the generated NPC
     * @throws IllegalArgumentException if id < 1 or id > NAMES.length
     */
    @Override
    public Npc generate(final int id) {
        if (id < 1 || id > NAMES.length) {
            throw new IllegalArgumentException("Invalid room ID for NPC: " + id);
        }
        final int idx = id - 1;                              // ADJUST INDEX
        final String name        = NAMES[idx];
        final String description = DESCRIPTIONS[idx];
        final String dialogue    = DIALOGUES[idx];
        return new Npc(super.getEnv(), name, description, dialogue);
    }

    /**
     * Generates a single non-interactable, roaming NPC for the given room.
     *
     * @param room the room to populate
     * @return a RoamingNpc that will wander within the environment bounds
     */
    public RoamingNpc generateRoamingNpc(final Room room) {
        final Point2D start = new Point2D(
            X + DELTA * room.getId(),
            Y
        );
        final MovementStrategy strategy = new RandomWalkStrategy(super.getEnv());
        return new RoamingNpc(start, super.getEnv(), strategy);
    }
}
