/**
 * Control-React color program
 * @author Nicholas & Hector
 * @since 12/3/2025
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ColorTap {
    int boardWidth = 600;
    int boardHeight = 600;

    JFrame frame = new JFrame("ColorTap");
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();
    JLabel timerLabel = new JLabel(); 

    //countown timer starting value
    int seconds = 60;
    
    ColorTap(){
        // Create the window elements 
        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        textLabel.setBackground(Color.darkGray);
        textLabel.setForeground(Color.white);
        textLabel.setFont(new Font("Arial", Font.BOLD, 50));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Color Tap");
        textLabel.setOpaque(true);

        //timer label that shows the countdown
        timerLabel.setBackground(Color.darkGray);
        timerLabel.setForeground(Color.white);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 28));
        timerLabel.setHorizontalAlignment(JLabel.RIGHT);
        timerLabel.setText("Time: " + seconds + "s  ");
        timerLabel.setOpaque(true);

        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel);
        textPanel.add(timerLabel, BorderLayout.EAST);
        frame.add(textPanel, BorderLayout.NORTH);

        boardPanel.setLayout(new GridLayout(3, 3));
        boardPanel.setBackground(Color.darkGray);
        frame.add(boardPanel);

        //countdown timer that updates each second
        Timer countdown = new Timer(1000, e -> {
            seconds--;
            if (seconds >= 0) {
                timerLabel.setText("Time: " + seconds + "s  ");
            }

            if (seconds <= 0) {
                timerLabel.setText(" Time’s Up!  ");
                ((Timer)e.getSource()).stop();
                endGame();
            }

        /*JButton tile = new JButton();
                board[r][c] = tile;
                boardPanel.add(tile);

                tile.setBackground(Color.darkGray);
                tile.setForeground(Color.white);
                tile.setFont(new Font("Arial", Font.BOLD, 120));
                tile.setFocusable(false);

                tile.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e){
                        if (gameOver) return; // Prevent button clicks when the game is over
                        JButton tile = (JButton)e.getSource();
                        if (tile.getText() == ""){ // Prevent overriding occupied tiles by checking that there's no X or O already there
                            tile.setText(currentPlayer);
                            turns++;
                            checkWinner(); // After a move has been made, check if the game should keep going
                            if (!gameOver){
                                currentPlayer = currentPlayer == playerX ? playerO : playerX;
                                textLabel.setText(currentPlayer + "'s turn"); // Update the top label to match whose turn it is
                            }
                        }
                    }
                });
*/
    }

}

