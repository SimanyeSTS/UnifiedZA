package za.unifiedza;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class DirectoryPanel extends JPanel {
    private final JTextField search = new JTextField(22);
    private final JComboBox<String> province = new JComboBox<>(new String[]{"All provinces","Western Cape","Gauteng","Free State"});
    private final DefaultTableModel model = new DefaultTableModel(new Object[]{"Institution","Abbreviation","City","Province","Type","Indicative APS"},0) { public boolean isCellEditable(int r,int c){return false;} };
    private final JTable table = new JTable(model);
    private List<Institution> visible = InstitutionData.ALL;
    public DirectoryPanel() {
        setLayout(new BorderLayout(12,12)); setBackground(Main.PAPER); setBorder(new EmptyBorder(25,30,25,30));
        JPanel top=new JPanel(new BorderLayout(8,8)); top.setOpaque(false); JPanel title=new JPanel(); title.setOpaque(false); title.setLayout(new BoxLayout(title,BoxLayout.Y_AXIS)); title.add(Main.heading("Institution directory",25)); title.add(Main.muted("Search and explore South African higher education institutions.")); top.add(title,BorderLayout.NORTH);
        JPanel filters=new JPanel(new FlowLayout(FlowLayout.LEFT,8,5)); filters.setOpaque(false); search.setToolTipText("Search by institution, abbreviation, city or province"); search.setPreferredSize(new Dimension(300,34)); filters.add(new JLabel("Search")); filters.add(search); filters.add(new JLabel("Province")); filters.add(province); JButton reset=new JButton("Clear filters"); filters.add(reset); top.add(filters,BorderLayout.SOUTH); add(top,BorderLayout.NORTH);
        table.setRowHeight(34); table.setFont(new Font("SansSerif",Font.PLAIN,13)); table.getTableHeader().setFont(new Font("SansSerif",Font.BOLD,12)); table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); table.setAutoCreateRowSorter(true);
        JScrollPane scroll=new JScrollPane(table); scroll.setBorder(BorderFactory.createLineBorder(new Color(220,222,220))); add(scroll,BorderLayout.CENTER);
        JPanel bottom=new JPanel(new BorderLayout()); bottom.setOpaque(false); JLabel note=Main.muted("Select an institution, then open its profile for campuses, faculties and sample courses."); JButton details=Main.button("View institution details  →"); bottom.add(note,BorderLayout.CENTER); bottom.add(details,BorderLayout.EAST); add(bottom,BorderLayout.SOUTH);
        search.getDocument().addDocumentListener(new javax.swing.event.DocumentListener(){public void insertUpdate(javax.swing.event.DocumentEvent e){refresh();}public void removeUpdate(javax.swing.event.DocumentEvent e){refresh();}public void changedUpdate(javax.swing.event.DocumentEvent e){refresh();}});
        province.addActionListener(e->refresh()); reset.addActionListener(e->{search.setText("");province.setSelectedIndex(0);}); details.addActionListener(e->openDetails());
        refresh();
    }
    private void refresh(){String q=search.getText().toLowerCase(Locale.ROOT).trim();String p=(String)province.getSelectedItem();visible=InstitutionData.ALL.stream().filter(i->(p.equals("All provinces")||i.province().equals(p))).filter(i->(i.name()+" "+i.shortName()+" "+i.city()+" "+i.province()+" "+i.type()).toLowerCase(Locale.ROOT).contains(q)).collect(Collectors.toList());model.setRowCount(0);for(Institution i:visible)model.addRow(new Object[]{i.name(),i.shortName(),i.city(),i.province(),i.type(),i.aps()});}
    private void openDetails(){int row=table.getSelectedRow();if(row<0){JOptionPane.showMessageDialog(this,"Please select an institution first.","No institution selected",JOptionPane.INFORMATION_MESSAGE);return;}int modelRow=table.convertRowIndexToModel(row);new DetailsDialog(SwingUtilities.getWindowAncestor(this),visible.get(modelRow)).setVisible(true);}
}
