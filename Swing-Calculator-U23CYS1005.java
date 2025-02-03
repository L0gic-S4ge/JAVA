import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;

public class EnhancedSwingCalculator extends JFrame implements ActionListener {
    private JTextField display;
    private JTextArea historyArea;
    private StringBuilder input = new StringBuilder();
    private ArrayList<String> history = new ArrayList<>();
    private boolean darkMode = false;
    private double memory = 0.0;

    public EnhancedSwingCalculator() {
        setTitle("Enhanced Swing Calculator");
        setSize(400, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.weightx = 1;
        gbc.weighty = 1;

        display = new JTextField();
        display.setFont(new Font("Arial", Font.BOLD, 22));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        add(display, gbc);

        historyArea = new JTextArea(5, 20);
        historyArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(historyArea);
        gbc.gridy = 1;
        gbc.gridheight = 1;
        add(scrollPane, gbc);

        String[][] buttons = {
            {"7", "8", "9", "/"},
            {"4", "5", "6", "*"},
            {"1", "2", "3", "-"},
            {"C", "0", "=", "+"},
            {"M+", "M-", "MR", "MC"},
            {"Dark Mode", "History", "Clear History", "√"}
        };

        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                JButton button = new JButton(buttons[i][j]);
                button.setFont(new Font("Arial", Font.BOLD, 16));
                button.addActionListener(this);
                gbc.gridx = j;
                gbc.gridy = i + 2;
                gbc.gridwidth = 1;
                add(button, gbc);
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        switch (command) {
            case "C":
                input.setLength(0);
                display.setText("");
                break;
            case "=":
                try {
                    if (input.length() == 0 || isOperator(input.charAt(input.length() - 1))) {
                        display.setText("Error");
                        return;
                    }
                    double result = evaluateExpression(input.toString());
                    display.setText(String.valueOf(result));
                    history.add(input + " = " + result);
                    historyArea.append(input + " = " + result + "\n");
                    input.setLength(0);
                } catch (Exception ex) {
                    display.setText("Error");
                }
                break;
            case "History":
                JOptionPane.showMessageDialog(this, String.join("\n", history), "Calculation History", JOptionPane.INFORMATION_MESSAGE);
                break;
            case "Clear History":
                history.clear();
                historyArea.setText("");
                break;
            case "√":
                try {
                    double num = Double.parseDouble(display.getText());
                    double sqrtResult = Math.sqrt(num);
                    display.setText(String.valueOf(sqrtResult));
                    history.add("√(" + num + ") = " + sqrtResult);
                    historyArea.append("√(" + num + ") = " + sqrtResult + "\n");
                } catch (Exception ex) {
                    display.setText("Error");
                }
                break;
            case "M+":
                memory = Double.parseDouble(display.getText());
                break;
            case "M-":
                memory = 0.0;
                break;
            case "MR":
                display.setText(String.valueOf(memory));
                break;
            case "MC":
                memory = 0.0;
                display.setText("");
                break;
            case "Dark Mode":
                toggleDarkMode();
                break;
            default:
                if (command.equals(".")) {
                    if (!input.toString().contains(".")) {
                        input.append(".");
                    }
                } else {
                    input.append(command);
                }
                display.setText(input.toString());
                break;
        }
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    private double evaluateExpression(String expression) throws Exception {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
        return (double) engine.eval(expression);
    }

    private void toggleDarkMode() {
        darkMode = !darkMode;
        Color bgColor = darkMode ? Color.BLACK : Color.WHITE;
        Color textColor = darkMode ? Color.WHITE : Color.BLACK;
        display.setBackground(bgColor);
        display.setForeground(textColor);
        historyArea.setBackground(bgColor);
        historyArea.setForeground(textColor);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EnhancedSwingCalculator calculator = new EnhancedSwingCalculator();
            calculator.setVisible(true);
        });
    }
}
