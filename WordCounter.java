import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WordCounter extends JFrame implements ActionListener {
    private JTextArea textArea;
    private JButton countButton;
    private JButton uploadButton;
    private JLabel resultLabel;

    public WordCounter() {
        setTitle("Word Counter");
        setSize(800, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel contentPane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(240, 240, 240));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        setContentPane(contentPane);

        textArea = new JTextArea();
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));

        countButton = new JButton("Count Words");
        countButton.setFont(new Font("Arial", Font.BOLD, 16));
        countButton.addActionListener(this);

        uploadButton = new JButton("Upload File");
        uploadButton.setFont(new Font("Arial", Font.BOLD, 16));
        uploadButton.addActionListener(this);

        Timer animationTimer = new Timer(1000, new ActionListener() {
            private Color color1 = new Color(85, 172, 238);
            private Color color2 = new Color(30, 162, 155);
            private boolean isColor1 = true;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (isColor1) {
                    countButton.setBackground(color2);
                    uploadButton.setBackground(color2);
                } else {
                    countButton.setBackground(color1);
                    uploadButton.setBackground(color1);
                }
                isColor1 = !isColor1;
            }
        });
        animationTimer.start();

        resultLabel = new JLabel();
        resultLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(new JScrollPane(textArea), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.add(countButton);
        buttonPanel.add(uploadButton);

        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BorderLayout());
        resultPanel.setOpaque(false);
        resultPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        resultPanel.add(resultLabel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.CENTER);
        add(resultPanel, BorderLayout.EAST);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WordCounter());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == countButton) {
            countWords();
        } else if (e.getSource() == uploadButton) {
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(this);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(selectedFile));
                    StringBuilder text = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        text.append(line).append("\n");
                    }
                    reader.close();
                    textArea.setText(text.toString());
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error reading file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void countWords() {
        String inputText = textArea.getText();

        if (inputText.isEmpty()) {
            resultLabel.setText("Please enter some text.");
            return;
        }

        String[] words = inputText.split("\\W+");
        int totalWords = words.length;

        Map<String, Integer> wordFrequency = new HashMap<>();
        for (String word : words) {
            word = word.toLowerCase();
            if (!isCommonWord(word)) {
                wordFrequency.put(word, wordFrequency.getOrDefault(word, 0) + 1);
            }
        }

        resultLabel.setText("<html>Total words: " + totalWords + "<br>"
                + "Unique words: " + wordFrequency.size() + "<br>"
                + "Word frequency: " + wordFrequency + "</html>");

        JOptionPane.showMessageDialog(this, resultLabel.getText());
    }

    private boolean isCommonWord(String word) {
        String[] commonWords = {"the", "and", "of", "in", "to", "as", "is", "it", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
        for (String common : commonWords) {
            if (common.equalsIgnoreCase(word)) {
                return true;
            }
        }
        return false;
    }
}
