package za.unifiedza;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Main {
    public static final Color NAVY = new Color(17, 31, 48);
    public static final Color GOLD = new Color(207, 166, 87);
    public static final Color PAPER = new Color(247, 247, 244);
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) { }
            new MainFrame().setVisible(true);
        });
    }

    static JLabel heading(String text, int size) {
        JLabel label = new JLabel(text); label.setFont(new Font("SansSerif", Font.BOLD, size)); label.setForeground(NAVY); return label;
    }
    static JLabel muted(String text) {
        JLabel label = new JLabel(text); label.setForeground(new Color(95, 105, 115)); label.setFont(new Font("SansSerif", Font.PLAIN, 13)); return label;
    }
    static JButton button(String text) {
        JButton b = new JButton(text); b.setFont(new Font("SansSerif", Font.BOLD, 13)); b.setBackground(NAVY); b.setForeground(Color.WHITE); b.setFocusPainted(false); b.setBorder(new EmptyBorder(10,16,10,16)); return b;
    }
}
