package za.unifiedza;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        super("Unified ZA | University Directory & APS Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); setSize(1100, 760); setMinimumSize(new Dimension(850, 620)); setLocationRelativeTo(null);
        getContentPane().setBackground(Main.PAPER); setLayout(new BorderLayout());
        JPanel header = new JPanel(new BorderLayout()); header.setBackground(Main.NAVY); header.setBorder(new EmptyBorder(18,28,18,28));
        JLabel brand = new JLabel("UNIFIED <span>ZA</span>"); brand.setForeground(Color.WHITE); brand.setFont(new Font("SansSerif",Font.BOLD,23));
        JLabel tagline = new JLabel("SOUTH AFRICAN HIGHER EDUCATION GUIDE"); tagline.setForeground(new Color(220,203,170)); tagline.setFont(new Font("SansSerif",Font.PLAIN,10));
        JPanel branding = new JPanel(); branding.setOpaque(false); branding.setLayout(new BoxLayout(branding,BoxLayout.Y_AXIS)); branding.add(brand); branding.add(tagline); header.add(branding,BorderLayout.WEST);
        JLabel right = new JLabel("EXPLORE  ·  COMPARE  ·  PLAN"); right.setForeground(Color.WHITE); right.setFont(new Font("SansSerif",Font.BOLD,11)); header.add(right,BorderLayout.EAST); add(header,BorderLayout.NORTH);
        JTabbedPane tabs = new JTabbedPane(); tabs.setFont(new Font("SansSerif",Font.BOLD,14)); tabs.setBackground(Main.PAPER);
        tabs.addTab("Home", new HomePanel(tabs)); tabs.addTab("Institutions", new DirectoryPanel()); tabs.addTab("APS Calculator", new ApsPanel());
        add(tabs,BorderLayout.CENTER);
        JLabel footer = new JLabel("  UNIFIED ZA  |  Information is indicative. Always confirm admission requirements with the institution."); footer.setBorder(new EmptyBorder(9,12,9,12)); footer.setForeground(new Color(105,112,119)); footer.setFont(new Font("SansSerif",Font.PLAIN,11)); add(footer,BorderLayout.SOUTH);
    }
}
