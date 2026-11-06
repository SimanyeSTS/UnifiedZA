package za.unifiedza;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class HomePanel extends JPanel {
    public HomePanel(JTabbedPane tabs) {
        setLayout(new BorderLayout()); setBackground(Main.PAPER);
        JPanel hero = new JPanel(); hero.setBackground(Main.NAVY); hero.setLayout(new BoxLayout(hero,BoxLayout.Y_AXIS)); hero.setBorder(new EmptyBorder(55,65,55,65));
        JLabel eyebrow = new JLabel("YOUR NEXT CHAPTER STARTS HERE"); eyebrow.setForeground(Main.GOLD); eyebrow.setFont(new Font("SansSerif",Font.BOLD,12));
        JLabel title = new JLabel("Find your place. Shape your future."); title.setForeground(Color.WHITE); title.setFont(new Font("SansSerif",Font.BOLD,34));
        JLabel sub = new JLabel("Explore South African institutions and estimate your National Senior Certificate APS."); sub.setForeground(new Color(220,226,232)); sub.setFont(new Font("SansSerif",Font.PLAIN,15));
        JButton directory = Main.button("Explore institutions  →"); directory.addActionListener(e -> tabs.setSelectedIndex(1));
        JButton calculator = new JButton("Calculate my APS  →"); calculator.setFont(new Font("SansSerif",Font.BOLD,13)); calculator.setForeground(Color.WHITE); calculator.setBackground(new Color(80,93,108)); calculator.setFocusPainted(false); calculator.setBorder(new EmptyBorder(10,16,10,16)); calculator.addActionListener(e -> tabs.setSelectedIndex(2));
        hero.add(eyebrow); hero.add(Box.createVerticalStrut(16)); hero.add(title); hero.add(Box.createVerticalStrut(13)); hero.add(sub); hero.add(Box.createVerticalStrut(28)); JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT,10,0)); actions.setOpaque(false); actions.add(directory); actions.add(calculator); hero.add(actions); add(hero,BorderLayout.NORTH);
        JPanel content = new JPanel(new BorderLayout(15,15)); content.setOpaque(false); content.setBorder(new EmptyBorder(28,40,28,40));
        content.add(Main.heading("A clearer way to plan your studies",22),BorderLayout.NORTH);
        JPanel cards = new JPanel(new GridLayout(1,3,16,0)); cards.setOpaque(false);
        cards.add(infoCard("01", "Discover institutions", "Browse university profiles, campuses, faculties and sample courses."));
        cards.add(infoCard("02", "Estimate your APS", "Enter seven NSC subjects and calculate an indicative base APS score."));
        cards.add(infoCard("03", "Compare requirements", "Review example course entry points and visit official institution websites."));
        content.add(cards,BorderLayout.CENTER); add(content,BorderLayout.CENTER);
    }
    private JPanel infoCard(String n,String title,String body) {
        JPanel p=new JPanel(); p.setBackground(Color.WHITE); p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(225,226,223)),new EmptyBorder(18,18,18,18))); p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));
        JLabel number=new JLabel(n); number.setForeground(Main.GOLD); number.setFont(new Font("SansSerif",Font.BOLD,13)); p.add(number); p.add(Box.createVerticalStrut(10)); p.add(Main.heading(title,17)); p.add(Box.createVerticalStrut(8)); p.add(Main.muted("<html><div style='width:210px'>"+body+"</div></html>")); return p;
    }
}
