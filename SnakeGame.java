import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    private static final int TILE_SIZE = 25;
    private static final int WIDTH = 25;
    private static final int HEIGHT = 25;
    private static final int DELAY = 150;

    private final int[] snakeX = new int[WIDTH * HEIGHT];
    private final int[] snakeY = new int[WIDTH * HEIGHT];
    private int snakeLength;

    private int foodX, foodY;
    private char direction = 'R'; // U, D, L, R
    private boolean running = false;
    private Timer timer;
    private Random random;
    private int score;

    public SnakeGame() {
        setPreferredSize(new Dimension(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE));
        setBackground(new Color(30, 30, 30));
        setFocusable(true);
        addKeyListener(this);
        random = new Random();
        startGame();
    }

    private void startGame() {
        snakeLength = 3;
        direction = 'R';
        score = 0;
        for (int i = 0; i < snakeLength; i++) {
            snakeX[i] = 100 - i * TILE_SIZE;
            snakeY[i] = 100;
        }
        placeFood();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    private void placeFood() {
        foodX = random.nextInt(WIDTH) * TILE_SIZE;
        foodY = random.nextInt(HEIGHT) * TILE_SIZE;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        if (running) {
            // Food
            g.setColor(new Color(255, 80, 80));
            g.fillOval(foodX, foodY, TILE_SIZE, TILE_SIZE);

            // Snake
            for (int i = 0; i < snakeLength; i++) {
                if (i == 0) g.setColor(new Color(0, 200, 255)); // head
                else g.setColor(new Color(0, 150, 0)); // body
                g.fillRoundRect(snakeX[i], snakeY[i], TILE_SIZE, TILE_SIZE, 8, 8);
            }

            // Score
            g.setColor(Color.WHITE);
            g.setFont(new Font("Poppins", Font.BOLD, 18));
            g.drawString("Score: " + score, 10, 20);
        } else {
            gameOver(g);
        }
    }

    private void move() {
        for (int i = snakeLength; i > 0; i--) {
            snakeX[i] = snakeX[i - 1];
            snakeY[i] = snakeY[i - 1];
        }

        switch (direction) {
            case 'U' -> snakeY[0] -= TILE_SIZE;
            case 'D' -> snakeY[0] += TILE_SIZE;
            case 'L' -> snakeX[0] -= TILE_SIZE;
            case 'R' -> snakeX[0] += TILE_SIZE;
        }
    }

    private void checkFood() {
        if (snakeX[0] == foodX && snakeY[0] == foodY) {
            snakeLength++;
            score += 10;
            placeFood();
        }
    }

    private void checkCollision() {
        // Hit body
        for (int i = 1; i < snakeLength; i++) {
            if (snakeX[0] == snakeX[i] && snakeY[0] == snakeY[i]) {
                running = false;
                break;
            }
        }

        // Hit wall
        if (snakeX[0] < 0 || snakeX[0] >= WIDTH * TILE_SIZE ||
            snakeY[0] < 0 || snakeY[0] >= HEIGHT * TILE_SIZE) {
            running = false;
        }

        if (!running) timer.stop();
    }

    private void gameOver(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Poppins", Font.BOLD, 30));
        g.drawString("Game Over!", WIDTH * TILE_SIZE / 2 - 90, HEIGHT * TILE_SIZE / 2 - 20);
        g.setFont(new Font("Poppins", Font.PLAIN, 20));
        g.drawString("Score: " + score, WIDTH * TILE_SIZE / 2 - 35, HEIGHT * TILE_SIZE / 2 + 10);
        g.drawString("Press ENTER to Restart", WIDTH * TILE_SIZE / 2 - 120, HEIGHT * TILE_SIZE / 2 + 50);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkFood();
            checkCollision();
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT -> { if (direction != 'R') direction = 'L'; }
            case KeyEvent.VK_RIGHT -> { if (direction != 'L') direction = 'R'; }
            case KeyEvent.VK_UP -> { if (direction != 'D') direction = 'U'; }
            case KeyEvent.VK_DOWN -> { if (direction != 'U') direction = 'D'; }
            case KeyEvent.VK_ENTER -> {
                if (!running) startGame();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("🐍 Snake Game");
        SnakeGame game = new SnakeGame();
        frame.add(game);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
