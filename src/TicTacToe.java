import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TicTacToe {
    // Constants for better maintainability
    private static final int BOARD_SIZE = 3;
    private static final String PLAYER_X = "X";
    private static final String PLAYER_O = "O";
    private static final String EMPTY = "";
    
    // Window dimensions
    private final int height = 1080;
    private final int width = 1030;

    // UI Components
    private JFrame frame = new JFrame("Tic-Tac-Toe");
    private JLabel textLabel = new JLabel();
    private JLabel scoreLabel = new JLabel();
    private JPanel textPanel = new JPanel();
    private JPanel boardPanel = new JPanel();
    private JPanel controlPanel = new JPanel();
    private JButton[][] board = new JButton[BOARD_SIZE][BOARD_SIZE];
    private JButton resetButton = new JButton("New Game");

    // Game state
    private String currentPlayer = PLAYER_X;
    private boolean gameOver = false;
    private int turn = 0;
    
    // Aesthetic color scheme
    private final Color primaryBg = new Color(45, 52, 70);        // Dark blue-gray
    private final Color secondaryBg = new Color(60, 68, 88);      // Lighter blue-gray
    private final Color accentColor = new Color(88, 166, 255);    // Bright blue
    private final Color textColor = new Color(255, 255, 255);     // White
    private final Color buttonBg = new Color(70, 78, 98);         // Button background
    private final Color hoverColor = new Color(100, 108, 128);    // Hover effect
    private final Color winHighlight = new Color(46, 204, 113);   // Green for wins
    private final Color tieHighlight = new Color(241, 196, 15);   // Yellow for ties
    private final Color xColor = new Color(231, 76, 60);          // Red for X
    private final Color oColor = new Color(52, 152, 219);         // Blue for O
    
    // Score tracking
    private int xWins = 0;
    private int oWins = 0;
    private int ties = 0;

    public TicTacToe() {
        initializeFrame();
        createTextPanel();
        createBoardPanel();
        createControlPanel();
        frame.setVisible(true);
    }

    private void initializeFrame() {
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(primaryBg);
    }

    private void createTextPanel() {
        textLabel.setFont(new Font("Segoe UI", Font.BOLD, 50));
        textLabel.setForeground(textColor);
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setOpaque(true);
        textLabel.setBackground(primaryBg);
        textLabel.setText(currentPlayer + "'s Turn");
        textLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        scoreLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        scoreLabel.setForeground(new Color(189, 195, 199));
        scoreLabel.setHorizontalAlignment(JLabel.CENTER);
        scoreLabel.setOpaque(true);
        scoreLabel.setBackground(primaryBg);
        scoreLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        updateScoreDisplay();

        textPanel.setLayout(new BorderLayout());
        textPanel.setBackground(primaryBg);
        textPanel.add(textLabel, BorderLayout.CENTER);
        textPanel.add(scoreLabel, BorderLayout.SOUTH);
        frame.add(textPanel, BorderLayout.NORTH);
    }

    private void createBoardPanel() {
        boardPanel.setLayout(new GridLayout(BOARD_SIZE, BOARD_SIZE, 8, 8));
        boardPanel.setBackground(primaryBg);
        boardPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                JButton btn = createGameButton(i, j);
                board[i][j] = btn;
                boardPanel.add(btn);
            }
        }
        frame.add(boardPanel, BorderLayout.CENTER);
    }

    private JButton createGameButton(int row, int col) {
        JButton btn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                // Enable anti-aliasing for smooth text
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                super.paintComponent(g2d);
                g2d.dispose();
            }
        };
        
        btn.setFont(new Font("Segoe UI", Font.BOLD, 100));
        btn.setBackground(buttonBg);
        btn.setForeground(textColor);
        btn.setFocusable(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(secondaryBg, 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect with smooth transition
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (btn.getText().equals(EMPTY) && !gameOver) {
                    btn.setBackground(hoverColor);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (btn.getText().equals(EMPTY) && !gameOver) {
                    btn.setBackground(buttonBg);
                }
            }
        });

        btn.addActionListener(e -> handleButtonClick(btn));
        return btn;
    }

    private void createControlPanel() {
        resetButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        resetButton.setBackground(accentColor);
        resetButton.setForeground(Color.WHITE);
        resetButton.setFocusable(false);
        resetButton.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));
        resetButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect to reset button
        resetButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                resetButton.setBackground(new Color(70, 140, 220));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                resetButton.setBackground(accentColor);
            }
        });
        
        resetButton.addActionListener(e -> resetGame());

        controlPanel.setBackground(primaryBg);
        controlPanel.setLayout(new FlowLayout());
        controlPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));
        controlPanel.add(resetButton);
        frame.add(controlPanel, BorderLayout.SOUTH);
    }

    private void handleButtonClick(JButton btn) {
        if (gameOver || !btn.getText().equals(EMPTY)) {
            return;
        }

        btn.setText(currentPlayer);
        btn.setBackground(buttonBg);
        
        // Set color based on player
        if (currentPlayer.equals(PLAYER_X)) {
            btn.setForeground(xColor);
        } else {
            btn.setForeground(oColor);
        }
        
        turn++;
        checkWinner();
        
        if (!gameOver) {
            switchPlayer();
        }
    }

    private void switchPlayer() {
        currentPlayer = currentPlayer.equals(PLAYER_X) ? PLAYER_O : PLAYER_X;
        textLabel.setText(currentPlayer + "'s Turn");
    }

    private void checkWinner() {
        // Check rows
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (checkLine(board[i][0], board[i][1], board[i][2])) {
                highlightWinningButtons(board[i][0], board[i][1], board[i][2]);
                declareWinner();
                return;
            }
        }

        // Check columns
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (checkLine(board[0][i], board[1][i], board[2][i])) {
                highlightWinningButtons(board[0][i], board[1][i], board[2][i]);
                declareWinner();
                return;
            }
        }

        // Check main diagonal
        if (checkLine(board[0][0], board[1][1], board[2][2])) {
            highlightWinningButtons(board[0][0], board[1][1], board[2][2]);
            declareWinner();
            return;
        }

        // Check anti-diagonal
        if (checkLine(board[0][2], board[1][1], board[2][0])) {
            highlightWinningButtons(board[0][2], board[1][1], board[2][0]);
            declareWinner();
            return;
        }

        // Check for tie
        if (turn == BOARD_SIZE * BOARD_SIZE) {
            declareTie();
        }
    }

    private boolean checkLine(JButton b1, JButton b2, JButton b3) {
        String text = b1.getText();
        return !text.equals(EMPTY) && 
               text.equals(b2.getText()) && 
               text.equals(b3.getText());
    }

    private void highlightWinningButtons(JButton... buttons) {
        for (JButton btn : buttons) {
            btn.setBackground(winHighlight);
            btn.setForeground(Color.WHITE);
            btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 3),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));
        }
    }

    private void declareWinner() {
        textLabel.setText(currentPlayer + " Wins!");
        gameOver = true;
        
        // Update score
        if (currentPlayer.equals(PLAYER_X)) {
            xWins++;
        } else {
            oWins++;
        }
        updateScoreDisplay();
        
        // Add celebration effect
        Timer timer = new Timer(150, new ActionListener() {
            int count = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                if (count % 2 == 0) {
                    textLabel.setForeground(winHighlight);
                } else {
                    textLabel.setForeground(textColor);
                }
                count++;
                if (count >= 6) {
                    ((Timer) e.getSource()).stop();
                    textLabel.setForeground(textColor);
                }
            }
        });
        timer.start();
    }

    private void declareTie() {
        textLabel.setText("It's a Tie!");
        gameOver = true;
        ties++;
        updateScoreDisplay();
        
        // Highlight all buttons for tie
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j].setBackground(tieHighlight);
                board[i][j].setForeground(Color.WHITE);
                board[i][j].setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.WHITE, 2),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
            }
        }
    }

    private void updateScoreDisplay() {
        scoreLabel.setText(String.format("X: %d | O: %d | Ties: %d", xWins, oWins, ties));
    }

    private void resetGame() {
        gameOver = false;
        turn = 0;
        currentPlayer = PLAYER_X;
        textLabel.setText(currentPlayer + "'s Turn");
        textLabel.setForeground(textColor);

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j].setText(EMPTY);
                board[i][j].setForeground(textColor);
                board[i][j].setBackground(buttonBg);
                board[i][j].setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(secondaryBg, 2),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
            }
        }
    }


}