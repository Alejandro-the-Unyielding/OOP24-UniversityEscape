package it.unibo.exam.utility.factory;

import it.unibo.exam.model.entity.minigame.Minigame;
import it.unibo.exam.controller.minigame.bar.BarMinigame;

/**
 * Factory class for creating different types of minigames based on room ID.
 * Each room has its own specific minigame type.
 */
public final class MinigameFactory {

    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private MinigameFactory() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    /**
     * Creates the appropriate minigame for the specified room.
     * 
     * Room-Minigame mapping:
     * - Room 4: Sort & Serve (Bar puzzle)
     * 
     *
     * @param roomId the ID of the room (1–4)
     * @return the corresponding minigame instance
     * @throws IllegalArgumentException if the room ID is invalid
     */
    public static Minigame createMinigame(final int roomId) {
        switch (roomId) {
            // case 1: return new MazeMinigame();
            // case 2: return new QuizMinigame();
            // case 3: return new MemoryMatchMinigame();
            case 4:
                return new BarMinigame();    // ← hook in Sort & Serve for Bar room (id 4)
            default:
                throw new IllegalArgumentException(
                    "Invalid room ID for minigame: " + roomId
                  + ". Valid room IDs are 1–4 (with Bar puzzle now at id 4)."
                );
        }
    }

    /**
     * Gets the name of the minigame for a specific room without creating an instance.
     * 
     * @param roomId the ID of the room
     * @return the name of the minigame
     * @throws IllegalArgumentException if the room ID is invalid
     */
    public static String getMinigameName(final int roomId) {
        switch (roomId) {
            case 4:
                return "Sort & Serve";       // ← human-readable name for Bar
            // case 1: return "aMAZEing";
            // case 2: return "Quiz Kahoot";
            // case 3: return "Memory Match";
            default:
                throw new IllegalArgumentException("Invalid room ID: " + roomId);
        }
    }

    /**
     * Gets the description of the minigame for a specific room.
     * 
     * @param roomId the ID of the room
     * @return the description of the minigame
     * @throws IllegalArgumentException if the room ID is invalid
     */
    public static String getMinigameDescription(final int roomId) {
        switch (roomId) {
            case 4:
                return "Pour colored layers until each glass is uniform.";
            // case 1: return "Navigate the maze to reach the exit.";
            // case 2: return "Answer all questions correctly to win!";
            // case 3: return "Find all matching pairs before time runs out.";
            default:
                throw new IllegalArgumentException("Invalid room ID: " + roomId);
        }
    }

    /**
     * Checks if a room has a minigame available.
     * 
     * @param roomId the ID of the room
     * @return {@code true} if the room has a minigame, {@code false} otherwise
     */
    public static boolean hasMinigame(final int roomId) {
        // Only rooms 1–4 have minigames (excluding main room 0)
        return roomId >= 1 && roomId <= 4;
    }
}
