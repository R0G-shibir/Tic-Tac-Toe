import javax.swing.*;
import java.awt.*;

public class TicTacToe {
    private static final int BOARD_SIZE = 3;
    private static final String PLAYER_X = "X";
    private static final String PLAYER_O = "O";
    private static final String EMPTY = "";
    
    private final int height = 800;
    private final int width = 600;

    private JFrame frame = new JFrame("Tic-Tac-Toe");
    private JLabel textLabel = new JLabel();
    private JLabel scoreLabel = new JLabel();
    private JPanel textPanel = new JPanel();
    private JPanel boardPanel = new JPanel();
    private JPanel controlPanel = new JPanel();
    private JButton[][] board = new JButton[BOARD_SIZE][BOARD_SIZE];
    private JButton resetButton = new JButton("New Game");

    private String currentPlayer = PLAYER_X;
    private boolean gameOver = false;
    private int turn = 0;
    
    private final Color bgColor = new Color(50, 50, 50);
    private final Color buttonColor = new Color(80, 80, 80);
    private final Color textColor = Color.WHITE;
    private final Color xColor = Color.RED;
    private final Color oColor = Color.BLUE;
    private final Color winColor = Color.GREEN;
    
    private int xWins = 0;
    private int oWins = 0;
    private int ties = 0;

    public TicTacToe() {
        setupFrame();
        createComponents();
        frame.setVisible(true);
    }

    private void setupFrame() {
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(bgColor);
    }

    private void createComponents() {
        createTextPanel();
        createBoardPanel();
        createControlPanel();
    }

    private void createTextPanel() {
        textLabel.setFont(new Font("Arial", Font.BOLD, 36));
        textLabel.setForeground(textColor);
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText(currentPlayer + "'s Turn");

        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        scoreLabel.setForeground(textColor);
        scoreLabel.setHorizontalAlignment(JLabel.CENTER);
        updateScoreDisplay();

        textPanel.setBackground(bgColor);
        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel, BorderLayout.CENTER);
        textPanel.add(scoreLabel, BorderLayout.SOUTH);
        textPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        frame.add(textPanel, BorderLayout.NORTH);
    }

    private void createBoardPanel() {
        boardPanel.setLayout(new GridLayout(BOARD_SIZE, BOARD_SIZE, 5, 5));
        boardPanel.setBackground(bgColor);
        boardPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                JButton btn = new JButton();
                btn.setFont(new Font("Arial", Font.BOLD, 80));
                btn.setBackground(buttonColor);
                btn.setForeground(textColor);
                btn.setFocusable(false);
                btn.addActionListener(e -> handleButtonClick(btn));
                
                board[i][j] = btn;
                boardPanel.add(btn);
            }
        }
        frame.add(boardPanel, BorderLayout.CENTER);
    }

    private void createControlPanel() {
        resetButton.setFont(new Font("Arial", Font.BOLD, 16));
        resetButton.setBackground(Color.BLUE);
        resetButton.setForeground(Color.WHITE);
        resetButton.setFocusable(false);
        resetButton.addActionListener(e -> resetGame());

        controlPanel.setBackground(bgColor);
        controlPanel.add(resetButton);
        controlPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        frame.add(controlPanel, BorderLayout.SOUTH);
    }

    private void handleButtonClick(JButton btn) {
        if (gameOver || !btn.getText().equals(EMPTY)) {
            return;
        }

        btn.setText(currentPlayer);
        btn.setForeground(currentPlayer.equals(PLAYER_X) ? xColor : oColor);
        turn++;
        
        checkWinner();
        
        if (!gameOver) {
            currentPlayer = currentPlayer.equals(PLAYER_X) ? PLAYER_O : PLAYER_X;
            textLabel.setText(currentPlayer + "'s Turn");
        }
    }

    private void checkWinner() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (checkLine(board[i][0], board[i][1], board[i][2])) {
                highlightWinningButtons(board[i][0], board[i][1], board[i][2]);
                declareWinner();
                return;
            }
        }

        for (int i = 0; i < BOARD_SIZE; i++) {
            if (checkLine(board[0][i], board[1][i], board[2][i])) {
                highlightWinningButtons(board[0][i], board[1][i], board[2][i]);
                declareWinner();
                return;
            }
        }

        if (checkLine(board[0][0], board[1][1], board[2][2])) {
            highlightWinningButtons(board[0][0], board[1][1], board[2][2]);
            declareWinner();
            return;
        }

        if (checkLine(board[0][2], board[1][1], board[2][0])) {
            highlightWinningButtons(board[0][2], board[1][1], board[2][0]);
            declareWinner();
            return;
        }

        if (turn == BOARD_SIZE * BOARD_SIZE) {
            declareTie();
        }
    }

    private boolean checkLine(JButton b1, JButton b2, JButton b3) {
        String text = b1.getText();
        return !text.equals(EMPTY) && text.equals(b2.getText()) && text.equals(b3.getText());
    }

    private void highlightWinningButtons(JButton... buttons) {
        for (JButton btn : buttons) {
            btn.setBackground(winColor);
            btn.setForeground(Color.WHITE);
        }
    }

    private void declareWinner() {
        textLabel.setText(currentPlayer + " Wins!");
        gameOver = true;
        
        if (currentPlayer.equals(PLAYER_X)) {
            xWins++;
        } else {
            oWins++;
        }
        updateScoreDisplay();
    }

    private void declareTie() {
        textLabel.setText("It's a Tie!");
        gameOver = true;
        ties++;
        updateScoreDisplay();
        
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j].setBackground(Color.YELLOW);
                board[i][j].setForeground(Color.BLACK);
            }
        }
    }

    private void updateScoreDisplay() {
        scoreLabel.setText("X: " + xWins + " | O: " + oWins + " | Ties: " + ties);
    }

    private void resetGame() {
        gameOver = false;
        turn = 0;
        currentPlayer = PLAYER_X;
        textLabel.setText(currentPlayer + "'s Turn");

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j].setText(EMPTY);
                board[i][j].setForeground(textColor);
                board[i][j].setBackground(buttonColor);
            }
        }
    }
}