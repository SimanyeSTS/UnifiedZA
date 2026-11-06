package za.unifiedza;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DetailsDialog extends JDialog {
    public DetailsDialog(Window owner, Institution i) {
        super(owner,i.name(),ModalityType.APPLICATION_MODAL); setSize(760,650); setLocationRelativeTo(owner);
        JPanel root=new JPanel(new BorderLayout(12,12));root.setBackground(Main.PAPER);root.setBorder(new EmptyBorder(22,25,22,25));setContentPane(root);
        JPanel top=new JPanel();top.setOpaque(false);top.setLayout(new BoxLayout(top,BoxLayout.Y_AXIS));JLabel tag=new JLabel(i.shortName()+"  ·  "+i.type().toUpperCase());tag.setForeground(Main.GOLD);tag.setFont(new Font("SansSerif",Font.BOLD,12));top.add(tag);top.add(Box.createVerticalStrut(7));top.add(Main.heading(i.name(),24));top.add(Box.createVerticalStrut(6));top.add(Main.muted(i.city()+", "+i.province()+"   |   Indicative institution APS: "+i.aps()));top.add(Box.createVerticalStrut(12));JTextArea about=new JTextArea(i.about()+"\n\n"+i.description());about.setLineWrap(true);about.setWrapStyleWord(true);about.setEditable(false);about.setOpaque(false);about.setFont(new Font("SansSerif",Font.PLAIN,13));top.add(about);root.add(top,BorderLayout.NORTH);
        JTextArea info=new JTextArea();info.setEditable(false);info.setFont(new Font("SansSerif",Font.PLAIN,13));StringBuilder b=new StringBuilder();b.append("CAMPUSES\n");i.campuses().forEach(x->b.append(" • ").append(x).append('\n'));b.append("\nFACULTIES\n");i.faculties().forEach(x->b.append(" • ").append(x).append('\n'));b.append("\nSAMPLE COURSES\n");for(Institution.Course c:i.courses())b.append(" • ").append(c.name()).append(" — APS ").append(c.aps()).append(" | ").append(c.faculty()).append(" | Requirements: ").append(c.requirements()).append('\n');info.setText(b.toString());info.setCaretPosition(0);JScrollPane sc=new JScrollPane(info);sc.setBorder(BorderFactory.createLineBorder(new Color(220,222,220)));root.add(sc,BorderLayout.CENTER);
        JPanel buttons=new JPanel(new FlowLayout(FlowLayout.RIGHT));buttons.setOpaque(false);JButton website=Main.button("Open official website  ↗");website.addActionListener(e->{try{Desktop.getDesktop().browse(java.net.URI.create(i.website()));}catch(Exception ex){JOptionPane.showMessageDialog(this,"Could not open the website: "+ex.getMessage());}});JButton close=new JButton("Close");close.addActionListener(e->dispose());buttons.add(website);buttons.add(close);root.add(buttons,BorderLayout.SOUTH);
    }
}
