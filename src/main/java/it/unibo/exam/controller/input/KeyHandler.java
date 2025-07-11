package it.unibo.exam.controller.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Handles keyboard input for the application.
 * Improved version with better interaction handling.
 */
public final class KeyHandler implements KeyListener {
    private boolean upPressed;
    private boolean downPressed;
    private boolean leftPressed;
    private boolean rightPressed;
    private boolean interactPressed;
    private boolean interactJustPressed; // For single-press actions
    private boolean spaceBarPressed;

    /**
     * @return true if up key is pressed
     */
    public boolean isUpPressed() {
        return upPressed;
    }

    /**
     * @return true if down key is pressed
     */
    public boolean isDownPressed() {
        return downPressed;
    } 

    /**
     * @return true if left key is pressed
     */
    public boolean isLeftPressed() {
        return leftPressed;
    }

    /**
     * @return true if right key is pressed
     */
    public boolean isRightPressed() {
        return rightPressed;
    }

    /**
     * @return true if interact key is pressed
     */
    public boolean isInteractPressed() {
        return interactPressed;
    }

    /**
     * @return true if interact key is pressed
     */
    public boolean isSpaceBarPressed() {
        if (spaceBarPressed) {
            spaceBarPressed = false; // Reset after reading
            return true;
        }
        return false;
    }

    /**
     * @return true if interact key was just pressed (single press detection)
     */
    public boolean isInteractJustPressed() {
        if (interactJustPressed) {
            interactJustPressed = false; // Reset after reading
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void keyTyped(final KeyEvent e) {
        // Not used but required by the KeyListener interface
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void keyPressed(final KeyEvent e) {
        final int code = e.getKeyCode();
        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            upPressed = true;
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            downPressed = true; 
        }
        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
            leftPressed = true;
        }
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            rightPressed = true;
        }
        if (code == KeyEvent.VK_E && !interactPressed) { // Only set just pressed if it wasn't already pressed
            interactJustPressed = true;
            interactPressed = true;
        }
        if (code == KeyEvent.VK_SPACE && !spaceBarPressed) {
            spaceBarPressed = true;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void keyReleased(final KeyEvent e) {
        final int code = e.getKeyCode();
        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            upPressed = false;
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            downPressed = false;
        }
        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
            leftPressed = false;
        }
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            rightPressed = false;
        }
        if (code == KeyEvent.VK_E) {
            interactPressed = false;
        }
        if (code == KeyEvent.VK_SPACE) {
            spaceBarPressed = false;
        }
    }
}
