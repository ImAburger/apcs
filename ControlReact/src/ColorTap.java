/**
 * Control-React color program
 * @author Nicholas & Hector
 * @since 12/3/2025
 */
//IMPORTS
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
//MAIN CLASS
public class ColorTap {
    int boardWidth = 600;
    int boardHeight = 600;
    int score = 0; //player score

    char curColor; //current color letter
    char curSize; //current square size
    char[] colorKeys = {'R', 'B', 'G', 'Y'}; //all possbile colors
    char[] sizeTypes = {'S', 'M', 'L', 'X'}; //all possible sizes: small, medium, large, xlarge
    ArrayList<Integer> reactionTimes = new ArrayList<>(); //stores each reaction time
    //GUI ELEMENTS
    JFrame frame = new JFrame("ColorTap");
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();
    JLayeredPane pane = new JLayeredPane();

    //the colored square
    ColoredSquarePanel square = new ColoredSquarePanel(Color.GREEN,40, 0, 400, false);
    
    // Card layout to switch between different screens
    CardLayout cardLayout = new CardLayout();
    JPanel mainPanel = new JPanel(cardLayout);
    JPanel resultsPanel = new JPanel();
    //START SCREEN ELEMENTS
    JPanel startScreen = new JPanel();
    JButton startButton = new JButton("START");
    //instructions for the game
    JLabel instructions = new JLabel();
    //RESULTS SCREEN ELEMENTS
    JLabel resultsText = new JLabel();
    //timer label that shows the countdown
    JLabel timerLabel = new JLabel(); 

    // Button that lets the player restart the game from the results screen
    JButton playAgainButton = new JButton("Play Again");
    
    Timer countdown; //1 second countdown timer
    Timer reactionTimer; //1 ms reaction timer
    int seconds = 30; //game time
    private int msElapsed = 0; //ms since color appeared

    

    ColorTap(){
        // Create the window elements 
        startScreen.setLayout(new GridBagLayout());
        startScreen.setBackground(Color.DARK_GRAY);
        //start button setup
        startButton.setFont(new Font("Arial", Font.BOLD, 40));
        startScreen.add(startButton);
        // start button action listener
        startButton.addActionListener(e -> { // Starting the game
            cardLayout.show(mainPanel, "GAME");
            textLabel.setText("Score: 0");
            //countdown timer that updates each second
            countdown = new Timer(1000, ev -> {
                seconds--;
                if (seconds >= 0) {
                    timerLabel.setText("Time: " + seconds + "s  ");
                }
                //timer stops when it reaches 0 seconds and switches to results screen
                if (seconds <= 0) {
                    timerLabel.setText("Time’s Up!  ");
                    endGame();
                    ((Timer)ev.getSource()).stop();
                }
            });
            countdown.start(); //start countdown timer

            //reaction timer
            reactionTimer = new Timer(1, ev -> {
                msElapsed++; //increases every millisecond
            });
            reactionTimer.start();

            setupKeyControls(); // Start checking for key inputs
            switchColor(); //show first random color
        });

        //set up the size for the square and background
        pane.setLayout(null);
        pane.setPreferredSize(new Dimension(boardWidth,boardHeight));

        //frame setup
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        //background panel
        ColoredSquarePanel bg = new ColoredSquarePanel(Color.BLACK, 0, 0, 600, true);

        //score label at the top
        textLabel.setBackground(Color.darkGray);
        textLabel.setForeground(Color.white);
        textLabel.setFont(new Font("Arial", Font.BOLD, 50));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Color Tap");
        textLabel.setOpaque(true);

        // Text panel setup
        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel);
        frame.add(textPanel, BorderLayout.NORTH);
        //set bounds for background and square
        bg.setBounds(0,0,600,600);
        square.setBounds(50,50,500,500);
        //add background and square to layered pane
        pane.add(bg, Integer.valueOf(0));
        pane.add(square, Integer.valueOf(1));
        frame.add(pane);

        //instructions on start screen
        instructions.setBackground(Color.darkGray);
        instructions.setForeground(Color.white);
        instructions.setPreferredSize(new Dimension(500, 300));
        instructions.setFont(new Font("Arial", Font.BOLD, 20));
        instructions.setText("<html>The goal of this game is to correctly press the right key on the keyboard that matches the 4 colors when a color is displayed on the screen.<br>" +
            "For example, if the color displayed on the screen is blue, you would press the key “B”.<br>" +
            " For each correct press, your score will go up. For each incorrect press, your score will go down.<br>" +
            " You will have a minute to try to correctly press the right key as fast as possible.</html>"
        );
        instructions.setOpaque(true);
        //results text setup
        resultsText.setPreferredSize(new Dimension(500, 300));
        resultsText.setBackground(Color.darkGray);
        resultsText.setForeground(Color.white);
        resultsText.setFont(new Font("Arial", Font.BOLD, 30));
        resultsText.setOpaque(true);

        //start screen layout
        startScreen.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.insets = new Insets(-250, 0, 0, 0);   // Move the instructions up 50 px
        startScreen.add(instructions, gbc);
        //start button layout
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 0, 0);   // Set the offset back to normal
        startScreen.add(startButton, gbc);

        //add all screens to card layout
        mainPanel.add(startScreen, "START");
        mainPanel.add(pane, "GAME");
        //results screen layout
        resultsPanel.setLayout(new GridBagLayout());
        GridBagConstraints r = new GridBagConstraints();
        //results text layout
        r.gridx = 0;
        r.gridy = 0;
        resultsPanel.add(resultsText, r); // Show results
        //play again button layout
        r.gridy = 1;
        r.insets = new Insets(20, 0, 0, 0); // Space between elements
        resultsPanel.add(playAgainButton, r); // Add Play Again button
        resultsPanel.setBackground(Color.DARK_GRAY);
        mainPanel.add(resultsPanel, "RESULTS");
        frame.add(mainPanel);
        // Focus settings
        pane.setFocusable(true);
        pane.requestFocusInWindow(); // Focus the window so we can detect key presses

        frame.setVisible(true);

         //timer label that shows the countdown
        timerLabel.setBackground(Color.darkGray);
        timerLabel.setForeground(Color.white);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 28));
        timerLabel.setHorizontalAlignment(JLabel.RIGHT);
        timerLabel.setText("Time: " + seconds + "s  ");
        timerLabel.setOpaque(true);
        //add timer label to the top panel
        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel);
        textPanel.add(timerLabel, BorderLayout.EAST);
        frame.add(textPanel, BorderLayout.NORTH);
        //board panel setup
        boardPanel.setLayout(new GridLayout(3, 3));
        boardPanel.setBackground(Color.darkGray);
        frame.add(boardPanel);

        // Restart the game when the Play Again button is clicked
         playAgainButton.addActionListener(e -> restartGame());
    }

    //END GAME
    private void endGame(){
        int totalTime = 0;
        for (int time : reactionTimes) totalTime += time;
        double avgReaction = reactionTimes.size() > 0 ? totalTime / (double) reactionTimes.size() : 0;
        //switch to results screen
        cardLayout.show(mainPanel, "RESULTS");
        resultsText.setText("<html>Your score: " + score + "<br>Average reaction time: " + (avgReaction > 0 ? (int) avgReaction + "ms" : "N/A"));
    }

    //used whenever the player presses a color key
    private void onKeyPress(char key){
        score += key == curColor ? 1 : -1; //add or subtract to the score
        
        switchColor(); //show a new color
        textLabel.setText("Score: " + score); // Update the score
    }

    //change to a new random color
    private void switchColor(){
        char newColor = curColor;
        //pick a different color than last time
        while (newColor == curColor) newColor = colorKeys[(int) (4 * Math.random())];
        //update square color
        if (newColor == 'R'){
            square.squareColor = Color.RED;//red
        } else if (newColor == 'B'){
            square.squareColor = Color.BLUE;//blue
        } else if (newColor == 'G'){
            square.squareColor = Color.GREEN;//green
        } else if (newColor == 'Y'){
            square.squareColor = Color.YELLOW;//yellow
        }
        switchSize(); // Switch the square's size
        switchPosition(); // Switch the square's position
                
        //set current color to new color
        curColor = newColor;
        square.repaint(); // Redraw the square with the new color

        //store reaction time
        reactionTimes.add(msElapsed);
        
        //reset timer for the next reaction measurement
        msElapsed = 0;
    }
    //move the square to a new random position
    private void switchPosition(){
        square.squareX = (int) ((Math.random() * (boardWidth - 500))); //x position
        square.squareY = (int) ((Math.random() * (boardHeight - 300)) - 100); //y position
    }

    //change to a new random size
    private void switchSize(){
        char newSize = curSize; //pick a different size than last time
        while (newSize == curSize) newSize = sizeTypes[(int) (4* Math.random())];//4 possible sizes
        //update square size
        if (newSize == 'S'){
            square.squareSize = 100; //small
        } else if (newSize == 'M'){
            square.squareSize = 250; //medium
        } else if (newSize == 'L'){
            square.squareSize = 400; //large
        } else if (newSize == 'X'){
            square.squareSize = 450; //extra large
        }
        curSize = newSize;//set current size to new size
    }
    
    //setup keyboard controls for R, B, G, Y
    private void setupKeyControls() {
        InputMap inputMap = pane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = pane.getActionMap();

        // Detect if R was pressed
        inputMap.put(KeyStroke.getKeyStroke("R"), "redPressed");
        actionMap.put("redPressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onKeyPress('R');
            }
        });

        // Detect if B was pressed
        inputMap.put(KeyStroke.getKeyStroke("B"), "bluePressed");
        actionMap.put("bluePressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onKeyPress('B');
            }
        });

        // Detect if G was pressed
        inputMap.put(KeyStroke.getKeyStroke("G"), "greenPressed");
        actionMap.put("greenPressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onKeyPress('G');
            }
        });

        // Detect if Y was pressed
        inputMap.put(KeyStroke.getKeyStroke("Y"), "yellowPressed");
        actionMap.put("yellowPressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onKeyPress('Y');
            }
            
        });


        
    }
    // Resets the game state and starts a new game
    private void restartGame() {
        score = 0;
        seconds = 30;
        reactionTimes.clear();
        msElapsed = 0;
        // Reset labels
        textLabel.setText("Score: 0");
        timerLabel.setText("Time: 30s");
        // Switch to game screen
        cardLayout.show(mainPanel, "GAME");
        // Restart timers
        countdown.start();
        reactionTimer.start();

        switchColor();
    }
}
    
//draws a colored square
class ColoredSquarePanel extends JPanel {
    public Color squareColor;
    public int squareX, squareY, squareSize;
    // Constructor to initialize the colored square panel
    public ColoredSquarePanel(Color color, int x, int y, int size, boolean isBackground) {
        this.squareColor = color;
        this.squareX = x;
        this.squareY = y;
        this.squareSize = size;

        setPreferredSize(new Dimension(size, size));
        setOpaque(isBackground);   //  ONLY background is opaque
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Always call super.paintComponent() first

        //draw the actual square
        g.setColor(squareColor); // Set the color for the square
        g.fillRect(squareX, squareY, squareSize, squareSize); // Draw a filled rectangle (square)
    }

}
