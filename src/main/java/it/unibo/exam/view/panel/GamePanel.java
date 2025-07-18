package it.unibo.exam.view.panel;

import it.unibo.exam.controller.MainController;
import it.unibo.exam.utility.geometry.Point2D;
import it.unibo.exam.view.GameRenderer;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import javax.swing.JFrame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.logging.Logger;

/**
 * Main game panel that handles rendering of the game world.
 * Updated to support minigame integration with proper parent frame reference.
 */
@SuppressFBWarnings(value = {"SE_BAD_FIELD", "SE_BAD_FIELD_STORE"}, 
                   justification = "GamePanel is not intended to be serialized,"
                   + " contains game-specific non-serializable components")
public final class GamePanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(GamePanel.class.getName());

    private final MainController mainController;
    private final GameRenderer   gameRenderer;
    private final Point2D        initialSize;
    private final JFrame         parentFrame;

    /**
     * Constructor for GamePanel with parent frame reference.
     * Creates defensive copies of mutable parameters to prevent external modification.
     *
     * @param initialSize the initial size of the game panel (will be copied)
     * @param parentFrame the parent frame for minigame windows (reference retained)
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", 
                       justification = "JFrame reference intentionally retained for UI operations")
    public GamePanel(final Point2D initialSize, final JFrame parentFrame) {
        // Defensive copy of Point2D to prevent external modification
        this.initialSize   = new Point2D(initialSize.getX(), initialSize.getY());
        // JFrame reference retained intentionally for minigame support
        this.parentFrame   = parentFrame;
        this.mainController = new MainController(this.initialSize, parentFrame);
        this.gameRenderer  = mainController.getGameRenderer();

        // Defer initialization to avoid calling overridable methods during construction
        SwingUtilities.invokeLater(this::completeInitialization);
    }

    /**
     * Alternative constructor that takes Dimension and parent frame.
     *
     * @param initialSize the initial size as a Dimension
     * @param parentFrame the parent frame for minigame windows
     */
    public GamePanel(final Dimension initialSize, final JFrame parentFrame) {
        this(new Point2D(initialSize.width, initialSize.height), parentFrame);
    }

    /**
     * Backward compatibility constructor (without parent frame).
     * Minigames will not work properly without a parent frame.
     *
     * @param initialSize the initial size of the game panel
     * @deprecated Use constructor with parent frame for minigame support
     */
    @Deprecated
    public GamePanel(final Point2D initialSize) {
        this(initialSize, null);
        LOGGER.warning("GamePanel created without parent frame - minigames may not work properly");
    }

    /**
     * Backward compatibility constructor (without parent frame).
     *
     * @param initialSize the initial size as a Dimension
     * @deprecated Use constructor with parent frame for minigame support
     */
    @Deprecated
    public GamePanel(final Dimension initialSize) {
        this(new Point2D(initialSize.width, initialSize.height), null);
    }

    /**
     * Sets the parent frame after construction if not set in constructor.
     * This allows minigames to work properly.
     *
     * @param parentFrame the parent frame
     */
    public void setParentFrame(final JFrame parentFrame) {
        if (parentFrame != null) {
            mainController.setParentFrame(parentFrame);
            LOGGER.info("Parent frame set for GamePanel - minigames now enabled");
        }
    }

    /**
     * Completes the initialization after construction is finished.
     * This method is called via SwingUtilities.invokeLater to ensure
     * the object is fully constructed before calling overridable methods.
     */
    private void completeInitialization() {
        initializePanel();
        startGameController();
    }

    /**
     * Initializes the panel settings.
     * Called after construction is complete to avoid overridable method issues.
     */
    private void initializePanel() {
        setPreferredSize(new Dimension(initialSize.getX(), initialSize.getY()));
        setFocusable(true);
        setDoubleBuffered(true);

        // Setup resize listener
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                final Dimension newSize = getSize();
                final Point2D newPoint = new Point2D(newSize.width, newSize.height);
                mainController.resize(newPoint);
            }
        });

        // Add key listener from the controller
        addKeyListener(mainController.getKeyHandler());

        // Request focus after everything is set up
        requestFocusInWindow();
    }

    /**
     * Starts the game controller in a separate thread.
     */
    private void startGameController() {
        LOGGER.info("Starting game loop in thread " + Thread.currentThread().getName());
        final Thread gameThread = new Thread(mainController::start,
                                             "GameControllerThread");
        gameThread.setDaemon(true);
        gameThread.start();
    }

    /**
     * Stops the game controller and any running minigames.
     */
    public void stopGame() {
        mainController.stop();
    }

    /**
     * Overrides paintComponent to render the game.
     *
     * @param g the graphics context
     */
    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);

        final Graphics2D g2d = (Graphics2D) g.create();
        try {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                 RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                                 RenderingHints.VALUE_RENDER_QUALITY);

            gameRenderer.renderGame(g2d);
            gameRenderer.renderHud(g2d);
        } finally {
            g2d.dispose();
        }

        // Schedule next repaint for smooth animation
        repaint();
    }

    /**
     * Gets the main controller for external access if needed.
     *
     * @return the main controller
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP", 
                       justification = "MainController reference is needed for game control, defensive copy not appropriate")
    public MainController getMainController() {
        return mainController;
    }

    /**
     * Gets the parent frame reference for minigame operations.
     * 
     * @return the parent frame or null if not set
     * @implNote Returns direct reference to internal frame - caller should not modify
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP", 
                       justification = "Parent frame reference needed for minigame centering,"
                       + "modification by caller is acceptable")
    public JFrame getParentFrame() {
        return parentFrame;
    }

    @Override
    public void addNotify() {
        super.addNotify();
        SwingUtilities.invokeLater(() -> {
            final Dimension size = getSize();
            if (size.width > 0 && size.height > 0) {
                LOGGER.info("GamePanel addNotify - size: " + size.width + "x" + size.height);
                mainController.resize(new Point2D(size.width, size.height));
            }
        });
    }

    @Override
    public void paint(final Graphics g) {
        final Dimension currentSize = getSize();
        if (currentSize.width != initialSize.getX()
         || currentSize.height != initialSize.getY()) {
            // Notify controller of resize with new Point2D instance
            mainController.resize(new Point2D(currentSize.width, currentSize.height));
            // We don't modify initialSize anymore since it should remain immutable
            // The initial size is preserved as originally set
        }
        super.paint(g);
    }
}
