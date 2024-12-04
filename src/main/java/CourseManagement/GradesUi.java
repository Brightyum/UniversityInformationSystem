package CourseManagement;

import java.util.Map;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class GradesUi extends JFrame {
    public GradesUi(Map<String, String> scoreData) {
        setTitle("성적 확인");
        setSize(600, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        setLocationRelativeTo(null);
        
        String[] columnNames = {"강의명", "성적"};
        DefaultTableModel defaultTable = new DefaultTableModel(columnNames,0);
        
        for (Map.Entry<String, String> i : scoreData.entrySet()) {
            defaultTable.addRow(new Object[]{i.getKey(), i.getValue()});
        }
        
       JTable table = new JTable(defaultTable);
       JScrollPane scrollPane = new JScrollPane(table);
        
       add(scrollPane);
        setVisible(true);
    } 
}
