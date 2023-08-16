import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class Guessthenumber extends JFrame {
    private int maxNumber = 100;
    private int attemptsLimit = 10;
    private Random random = new Random();
    private int generatedNumber;

    private JLabel titleLabel;
    private JLabel instructionLabel;
    private JLabel attemptsLabel;
    private JLabel timerLabel;
    private JTextField  guessTextField;
    private JButton submitButton;
    private Timer gameTimer;
    private int timeRemaining = 60;
    private final int CLOSE_THRESHOLD = 5;

    public Guessthenumber() {
        setTitle("Guess the Number Game");
        setSize(650, 350);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240, 240, 240));

        JPanel gradientPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setPaint(new GradientPaint(0, 0, new Color(250, 210, 200), getWidth(), getHeight(), new Color(240, 240, 240)));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        gradientPanel.setLayout(new GridLayout(5, 1, 10, 10));
        gradientPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(gradientPanel, BorderLayout.CENTER);

        titleLabel = new JLabel("Welcome to Number Guessing Game!");
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 28));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        instructionLabel = new JLabel("", SwingConstants.CENTER);
        instructionLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 22));

        attemptsLabel = new JLabel("", SwingConstants.CENTER);
        attemptsLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 26));

        timerLabel = new JLabel("", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 26));

        guessTextField = new JTextField();
        guessTextField.setFont(new Font("Comic Sans MS", Font.PLAIN, 22));

        submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));

        gradientPanel.add(titleLabel);
        gradientPanel.add(instructionLabel);
        gradientPanel.add(attemptsLabel);
        gradientPanel.add(timerLabel);
        gradientPanel.add(guessTextField);

        JPanel southPanel = new JPanel(new FlowLayout());
        southPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        add(southPanel, BorderLayout.SOUTH);
        southPanel.add(submitButton);

        generateNewNumber();
        setInstruction("I have generated a number between 1 and " + maxNumber + ".");
        setTimerText();

        gameTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeRemaining--;
                if (timeRemaining < 0) {
                    gameTimer.stop();
                    handleGameOver();
                } else {
                    setTimerText();
                }
            }
        });

        gameTimer.start();
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                checkGuess();
            }
        });
    }

    private void generateNewNumber() {
        generatedNumber = random.nextInt(maxNumber) + 1;
    }

    private void setInstruction(String text) {
        instructionLabel.setText(text);
    }

    private void setTimerText() {
        timerLabel.setText("Time remaining: " + timeRemaining + " seconds");
    }

    private void handleGameOver() {
        JOptionPane.showMessageDialog(this, "Sorry, you've run out of time."
                + "\nThe correct number was: " + generatedNumber);
        generateNewNumber();
        attemptsLimit = 10;
        timeRemaining = 60;
        setTimerText();
        setInstruction("I have generated a number between 1 and " + maxNumber + ".");
        guessTextField.setText("");
    }

    private void checkGuess() {
        int guess;
        try {
            guess = Integer.parseInt(guessTextField.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number.");
            return;
        }

        int difference = Math.abs(guess - generatedNumber);
        attemptsLimit--;

        if (guess == generatedNumber) {
            JOptionPane.showMessageDialog(this, "Congratulations! Your guess wass correct!");
            generateNewNumber();
            attemptsLimit = 10;
            timeRemaining = 60;
            setTimerText();
            setInstruction("I have generated a number between 1 and " + maxNumber + ".");
        } else if (attemptsLimit > 0) {
            if (difference <= CLOSE_THRESHOLD) {
                setInstruction("You are very close! Try again. Remaining attempts: " + attemptsLimit);
            } else if (guess < generatedNumber) {
                setInstruction("Your guess is too low. Try again. Remaining attempts: " + attemptsLimit);
            } else {
                setInstruction("Your guess is too high. Try again. Remaining attempts: " + attemptsLimit);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Sorry, you've reached the maximum number of attempts."
                    + "\nThe correct number was: " + generatedNumber);
            generateNewNumber();
            attemptsLimit = 10;
            timeRemaining = 60;
            setTimerText();
            setInstruction("I have generated a number between 1 and " + maxNumber + ".");
        }
        guessTextField.setText("");
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Guessthenumber().setVisible(true);
            }
        });
    }
}
