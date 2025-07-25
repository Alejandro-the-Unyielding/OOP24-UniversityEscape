package it.unibo.exam.controller.minigame.garden;

import it.unibo.exam.model.entity.minigame.Minigame;
import it.unibo.exam.model.entity.minigame.MinigameCallback;
import it.unibo.exam.view.garden.CatchBallPanel;
import it.unibo.exam.model.entity.minigame.garden.CatchBallModel;
import it.unibo.exam.model.scoring.LifeScoringStrategy;
import it.unibo.exam.model.scoring.ScoringStrategy;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Objects;

/**
 * Controller for the CatchBall minigame (MVC pattern).
 */
public final class CatchBallMinigame implements Minigame {

    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    private static final int TIMER_DELAY = 16;

    private JFrame frame;
    private CatchBallModel model;
    private CatchBallPanel panel;
    private Timer gameTimer;
    private MinigameCallback callback;
    private long startTimeMillis;
    private final ScoringStrategy scoringStrategy;

    private boolean leftPressed;
    private boolean rightPressed;

    /**
     * No‐arg constructor for factory instantiation (uses default scoring).
     */
    public CatchBallMinigame() {
        this(new LifeScoringStrategy());
    }

    /**
     * Full constructor allows custom scoring strategy.
     *
     * @param scoringStrategy the strategy used to compute final score
     */
    public CatchBallMinigame(final ScoringStrategy scoringStrategy) {
        this.scoringStrategy = Objects.requireNonNull(scoringStrategy,
            "scoringStrategy must not be null");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(final JFrame parentFrame, final MinigameCallback onComplete) {
        this.callback = onComplete;
        this.model = new CatchBallModel();
        this.panel = new CatchBallPanel(model);

        frame = new JFrame("Catch the Balls");
        frame.setSize(WIDTH, HEIGHT);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(parentFrame);
        frame.setResizable(false);
        frame.add(panel);
        frame.setVisible(true);

        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(final KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_A) {
                    leftPressed = true;
                } else if (e.getKeyCode() == KeyEvent.VK_D) {
                    rightPressed = true;
                }
            }

            @Override
            public void keyReleased(final KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_A) {
                    leftPressed = false;
                } else if (e.getKeyCode() == KeyEvent.VK_D) {
                    rightPressed = false;
                }
            }
        });

        startTimeMillis = System.currentTimeMillis();
        gameTimer = new Timer(TIMER_DELAY, this::gameLoop);
        gameTimer.start();
    }

    private void gameLoop(final ActionEvent e) {
        Objects.requireNonNull(e); // Reference the parameter to satisfy PMD

        model.update(leftPressed, rightPressed);
        panel.repaint();

        if (model.hasWon()) {
            endGame(true);
        } else if (model.hasLost()) {
            endGame(false);
        }
    }

    private void endGame(final boolean success) {
        gameTimer.stop();
        frame.dispose();
        final long elapsedMillis = System.currentTimeMillis() - startTimeMillis;
        final int elapsedSeconds = (int) (elapsedMillis / 1000L);
        final int score = scoringStrategy.calculate(model.getLives());
        if (success) {
            JOptionPane.showMessageDialog(null, "You win!\nTime: " + elapsedSeconds 
            + " seconds\nScore: " + score, "Victory", JOptionPane.INFORMATION_MESSAGE);
            callback.onComplete(true, elapsedSeconds, score);
        } else {
            JOptionPane.showMessageDialog(null, "Game Over! You lost!", "Defeat", JOptionPane.ERROR_MESSAGE);
            callback.onComplete(false, elapsedSeconds, 0);
        }
    }

    @Override
    public void stop() {
        if (frame != null) {
            frame.dispose();
        }
        if (gameTimer != null) {
            gameTimer.stop();
        }
    }

    @Override
    public String getName() {
        return "Garden Minigame";
    }

    @Override
    public String getDescription() {
        return "Move the bottle to catch falling balls. Reach 5 to win!";
    }
}
