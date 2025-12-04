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
    int score = 0;

    char curColor;
    char[] colorKeys = {'R', 'B', 'G', 'Y'};

    JFrame frame = new JFrame("ColorTap");
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();
    JLayeredPane pane = new JLayeredPane();

    ColoredSquarePanel square = new ColoredSquarePanel(Color.GREEN,90, 50, 400, false);
    
    CardLayout cardLayout = new CardLayout();
    JPanel mainPanel = new JPanel(cardLayout);
    JPanel resultsPanel = new JPanel();

    JPanel startScreen = new JPanel();
    JButton startButton = new JButton("START");

    JLabel instructions = new JLabel();

    JLabel resultsText = new JLabel();

    JLabel timerLabel = new JLabel(); 
    Timer countdown;
    private int seconds = 60;
    ColorTap(){
        // Create the window elements 
        startScreen.setLayout(new GridBagLayout());
        startScreen.setBackground(Color.DARK_GRAY);

        startButton.setFont(new Font("Arial", Font.BOLD, 40));
        startScreen.add(startButton);

        startButton.addActionListener(e -> { // Starting the game
            cardLayout.show(mainPanel, "GAME");
            //countdown timer that updates each second
            countdown = new Timer(1000, ev -> {
                seconds--;
                if (seconds >= 0) {
                    timerLabel.setText("Time: " + seconds + "s  ");
                }

                if (seconds <= 0) {
                    timerLabel.setText("⏰ Time’s Up!  ");
                    endGame();
                    ((Timer)ev.getSource()).stop();
                }
            });
            countdown.start();

            setupKeyControls(); // Start checking for key inputs
            switchColor();
        });

        pane.setLayout(null);
        pane.setPreferredSize(new Dimension(boardWidth,boardHeight));

        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        ColoredSquarePanel bg = new ColoredSquarePanel(Color.BLACK, 0, 0, 600, true);

        textLabel.setBackground(Color.darkGray);
        textLabel.setForeground(Color.white);
        textLabel.setFont(new Font("Arial", Font.BOLD, 50));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Color Tap");
        textLabel.setOpaque(true);

        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel);
        frame.add(textPanel, BorderLayout.NORTH);

        bg.setBounds(0,0,600,600);
        square.setBounds(0,0,600,600);

        pane.add(bg, Integer.valueOf(0));
        pane.add(square, Integer.valueOf(1));
        frame.add(pane);

        instructions.setBackground(Color.darkGray);
        instructions.setForeground(Color.white);
        instructions.setFont(new Font("Arial", Font.BOLD, 30));
        instructions.setText("The goal of this game is to correctly press the right key on the keyboard that matches the 4 colors when a color is displayed on the screen. \nFor example, if the color displayed on the screen is blue, you would press the key “B”.\n For each correct press, your score will go up. For each incorrect press, your score will go down.\n You will have a minute to try to correctly press the right key as fast as possible.");
        instructions.setOpaque(true);

        resultsText.setBackground(Color.darkGray);
        resultsText.setForeground(Color.white);
        resultsText.setFont(new Font("Arial", Font.BOLD, 30));
        resultsText.setText("your did it!!!!");
        resultsText.setOpaque(true);

        startScreen.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.insets = new Insets(-200, 0, 0, 0);   // Move the instructions up 50 px
        startScreen.add(instructions, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 0, 0);   // Set the offset back to normal
        startScreen.add(startButton, gbc);
        mainPanel.add(startScreen, "START");
        mainPanel.add(pane, "GAME");
        mainPanel.add(resultsPanel, "RESULTS");
        resultsPanel.add(resultsText);
        frame.add(mainPanel);

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

        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel);
        textPanel.add(timerLabel, BorderLayout.EAST);
        frame.add(textPanel, BorderLayout.NORTH);

        boardPanel.setLayout(new GridLayout(3, 3));
        boardPanel.setBackground(Color.darkGray);
        frame.add(boardPanel);
    }

    private void endGame(){
        cardLayout.show(mainPanel, "RESULTS");
    }

    private void onKeyPress(char key){
        //System.out.println("key: " + key);
        //System.out.println("curc: " + curColor);
        score += key == curColor ? 1 : -1;
        switchColor();
        System.out.println(score);
    }

    private void switchColor(){
        char newColor = curColor;
        while (newColor == curColor) newColor = colorKeys[(int) (4 * Math.random())];
        if (newColor == 'R'){
            square.squareColor = Color.RED;
        } else if (newColor == 'B'){
            square.squareColor = Color.BLUE;
        } else if (newColor == 'G'){
            square.squareColor = Color.GREEN;
        } else if (newColor == 'Y'){
            square.squareColor = Color.YELLOW;
        }
        curColor = newColor;
        square.repaint();
    }

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
}

class ColoredSquarePanel extends JPanel {
    public Color squareColor;
    private int squareX, squareY, squareSize;

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

        g.setColor(squareColor); // Set the color for the square
        g.fillRect(squareX, squareY, squareSize, squareSize); // Draw a filled rectangle (square)
    }

}
