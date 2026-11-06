package za.unifiedza;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class ApsPanel extends JPanel {
    private static final String[] SUBJECTS={"","Home Language","First Additional Language","Mathematics","Mathematical Literacy","Physical Sciences","Life Sciences","Accounting","Business Studies","Geography","History","Information Technology","Economics","Life Orientation"};
    private final JComboBox<String>[] names=new JComboBox[7];
    private final JTextField[] marks=new JTextField[7];
    private final JLabel progress=new JLabel("0/7 complete");
    private final JLabel result=new JLabel(" ");
    private final JLabel error=new JLabel(" ");
    public ApsPanel(){
        setLayout(new BorderLayout(18,18));setBackground(Main.PAPER);setBorder(new EmptyBorder(25,30,25,30));
        JPanel title=new JPanel();title.setOpaque(false);title.setLayout(new BoxLayout(title,BoxLayout.Y_AXIS));title.add(Main.heading("Understand your marks. Explore your options.",24));title.add(Box.createVerticalStrut(7));title.add(Main.muted("Enter your seven NSC subjects to calculate an indicative base APS score, excluding Life Orientation."));add(title,BorderLayout.NORTH);
        JPanel body=new JPanel(new BorderLayout(16,0));body.setOpaque(false);add(body,BorderLayout.CENTER);
        JPanel form=new JPanel(new BorderLayout(8,8));form.setBackground(Color.WHITE);form.setBorder(new EmptyBorder(18,20,18,20));body.add(form,BorderLayout.CENTER);
        JPanel formHead=new JPanel(new BorderLayout());formHead.setOpaque(false);formHead.add(Main.heading("Your academic results",18),BorderLayout.WEST);progress.setForeground(Main.GOLD);progress.setFont(new Font("SansSerif",Font.BOLD,13));formHead.add(progress,BorderLayout.EAST);form.add(formHead,BorderLayout.NORTH);
        JPanel rows=new JPanel(new GridLayout(8,3,8,8));rows.setOpaque(false);JLabel h1=new JLabel("#"),h2=new JLabel("SUBJECT"),h3=new JLabel("MARK (%)");for(JLabel h:new JLabel[]{h1,h2,h3}){h.setFont(new Font("SansSerif",Font.BOLD,11));h.setForeground(new Color(90,100,110));}rows.add(h1);rows.add(h2);rows.add(h3);
        for(int x=0;x<7;x++){rows.add(new JLabel(String.format("%02d",x+1)));names[x]=new JComboBox<>(SUBJECTS);names[x].setPreferredSize(new Dimension(250,32));marks[x]=new JTextField();marks[x].setToolTipText("Enter a mark from 0 to 100");rows.add(names[x]);rows.add(marks[x]);names[x].addActionListener(e->clearResult());marks[x].getDocument().addDocumentListener(new javax.swing.event.DocumentListener(){public void insertUpdate(javax.swing.event.DocumentEvent e){clearResult();}public void removeUpdate(javax.swing.event.DocumentEvent e){clearResult();}public void changedUpdate(javax.swing.event.DocumentEvent e){clearResult();}});}
        form.add(rows,BorderLayout.CENTER);
        JPanel bottom=new JPanel();bottom.setOpaque(false);bottom.setLayout(new BoxLayout(bottom,BoxLayout.Y_AXIS));error.setForeground(new Color(175,55,45));error.setFont(new Font("SansSerif",Font.PLAIN,12));bottom.add(error);result.setForeground(new Color(30,105,70));result.setFont(new Font("SansSerif",Font.BOLD,18));bottom.add(result);JPanel actions=new JPanel(new FlowLayout(FlowLayout.RIGHT));actions.setOpaque(false);JButton reset=new JButton("Reset form");JButton calc=Main.button("Calculate my APS  →");reset.addActionListener(e->reset());calc.addActionListener(e->calculate());actions.add(reset);actions.add(calc);bottom.add(actions);form.add(bottom,BorderLayout.SOUTH);
        JPanel side=new JPanel();side.setBackground(new Color(239,237,230));side.setBorder(new EmptyBorder(18,18,18,18));side.setPreferredSize(new Dimension(240,300));side.setLayout(new BoxLayout(side,BoxLayout.Y_AXIS));side.add(Main.heading("How it works",18));side.add(Box.createVerticalStrut(8));side.add(Main.muted("Each percentage range is converted to points using a simple seven-point scale."));side.add(Box.createVerticalStrut(18));String[] ranges={"80–100%     7 points","70–79%       6 points","60–69%       5 points","50–59%       4 points","40–49%       3 points","30–39%       2 points","0–29%         1 point"};for(String r:ranges){JLabel l=new JLabel(r);l.setFont(new Font("Monospaced",Font.BOLD,12));l.setBorder(new EmptyBorder(5,0,5,0));side.add(l);}side.add(Box.createVerticalStrut(12));JLabel warning=new JLabel("<html><b>Important admission note</b><br><br>This is a demonstration calculator, not an official university APS result. Institution-specific rules and prerequisites must be checked.</html>");warning.setFont(new Font("SansSerif",Font.PLAIN,12));side.add(warning);body.add(side,BorderLayout.EAST);
    }
    private void clearResult(){error.setText(" ");result.setText(" ");int n=0;for(int x=0;x<7;x++)if(names[x].getSelectedIndex()>0&&!marks[x].getText().isBlank())n++;progress.setText(n+"/7 complete");}
    private int points(int mark){if(mark>=80)return 7;if(mark>=70)return 6;if(mark>=60)return 5;if(mark>=50)return 4;if(mark>=40)return 3;if(mark>=30)return 2;return 1;}
    private void calculate(){clearResult();Set<String> chosen=new HashSet<>();int total=0;boolean home=false,additional=false,math=false,mathLit=false;
        for(int x=0;x<7;x++){String subject=(String)names[x].getSelectedItem();String markText=marks[x].getText().trim();if(subject==null||subject.isBlank()||markText.isBlank()){error.setText("Please select a subject and enter a mark for all seven subjects.");return;}if(!chosen.add(subject)){error.setText("Please select seven different subjects. A subject cannot be entered more than once.");return;}int mark;try{mark=Integer.parseInt(markText);}catch(NumberFormatException ex){error.setText("Every subject mark must be a whole number between 0 and 100.");return;}if(mark<0||mark>100){error.setText("Every subject mark must be between 0 and 100.");return;}if(subject.equals("Home Language"))home=true;if(subject.equals("First Additional Language"))additional=true;if(subject.equals("Mathematics"))math=true;if(subject.equals("Mathematical Literacy"))mathLit=true;if(!subject.equals("Life Orientation"))total+=points(mark);}
        if(!home||!additional){error.setText("Your subjects must include a Home Language and a First Additional Language.");return;}if(math&&mathLit){error.setText("You cannot select both Mathematics and Mathematical Literacy.");return;}result.setText("Your indicative base APS score:  "+total+" points (Life Orientation excluded)");
    }
    private void reset(){for(int x=0;x<7;x++){names[x].setSelectedIndex(0);marks[x].setText("");}clearResult();}
}
