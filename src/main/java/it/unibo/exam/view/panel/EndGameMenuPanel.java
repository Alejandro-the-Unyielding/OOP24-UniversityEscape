package it.unibo.exam.view.panel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
// import java.util.ArrayList;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
// import javax.swing.table.DefaultTableModel;

// import it.unibo.exam.model.entity.PlayerScore;
// import it.unibo.exam.model.entity.enviroments.LeaderboardManage;

//  End game panel for the game with play again, exit buttons, leaderboard, time taken, score

public class EndGameMenuPanel extends JPanel {
    
     /**
     * Creation of the end game panel.
     *
     * @param window the parent JFrame window
     */
    private static final long serialVersionUID = 1L;
    private static final int WIDTHBUTTON = 800;
    private static final int HEIGHTBUTTON = 80;
    private static final int BUTTONFONTSIZE = 30;
    private static final int BUTTONSPACING = 20;

    public EndGameMenuPanel(final JFrame window) {
        createUI(window);
    }

    private void createUI(final JFrame window) {
        super.setLayout(new BorderLayout());
        super.setPreferredSize(window.getSize());

        // Creates the panel where the buttons will stay
        final JPanel buttonPanel = createButtonPanel();
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(BUTTONSPACING, 0, BUTTONSPACING, 0); // Space between buttons
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER; // Move the buttons ins the centre

        //Create the buttons
        final JButton playagainBut = new JButton("Gioca Ancora");
        final JButton exitBut= new JButton("Esci");
        final JButton optionsBut = new JButton("Opzioni");

        final Dimension buttonSize = new Dimension(WIDTHBUTTON, HEIGHTBUTTON);
        playagainBut.setPreferredSize(buttonSize);
        optionsBut.setPreferredSize(buttonSize);
        exitBut.setPreferredSize(buttonSize);

        //Font for the button
        final Font buttonFont = new Font("Arial", Font.BOLD, BUTTONFONTSIZE);
        playagainBut.setFont(buttonFont);
        optionsBut.setFont(buttonFont);
        exitBut.setFont(buttonFont);

        // Create the leaderboard
        // private final LeaderboardManage ldb = new LeaderboardManager();
        // private final DefaultTableModel ldbModel = new DefaultTableModel(
            // new String[]{"Position", "Player", "Time", "Score"}, 0
        // );

        //Adding the buttons to the panel
        buttonPanel.add(playagainBut, gbc);
        gbc.gridy++; //Move the next button lower
        buttonPanel.add(optionsBut, gbc);
        gbc.gridy++; //Move the next button lower
        buttonPanel.add(exitBut, gbc);

        //Adding the panel to the window
        super.add(buttonPanel, BorderLayout.CENTER);

        //Play again action
        playagainBut.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(final ActionEvent e){
                final int confirmedMessage = JOptionPane.showConfirmDialog(window, 
                                        "Sicuro di voler rigiocare?", "Gioca ancora", JOptionPane.YES_NO_OPTION);
                if(confirmedMessage == JOptionPane.YES_OPTION){
                    //Restart the game 
                    window.getContentPane().removeAll();
                    window.setExtendedState(JFrame.NORMAL);
                    window.revalidate();
                    window.repaint();
                }
           } 
        });

        //Exit action
        exitBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e){
            final int confirmedMessage = JOptionPane.showConfirmDialog(window, 
                                        "Sicuro di voler uscire?", "Vuoi uscire?", JOptionPane.YES_NO_OPTION);
            if(confirmedMessage == JOptionPane.YES_OPTION);
                //Exit to the main menu
                window.getContentPane().removeAll();
                window.getContentPane().add(new MainMenuPanel(window));
                window.revalidate();
                window.repaint();
            }    
        });

        //
    }

    private JPanel createButtonPanel() {
        return new JPanel(new GridBagLayout());
    }
}
