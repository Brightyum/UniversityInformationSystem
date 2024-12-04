package CourseManagement;

import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class AttendanceUi extends JFrame {
    public AttendanceUi(Map<String, List<String>> data, String select) {
        setTitle("출석부 조회");
        setSize(600, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        String[] columnNames = {"강의명", "학생", "학과"};
        DefaultTableModel defaultTable = new DefaultTableModel(columnNames, 0);
        
        for (Map.Entry<String, List<String>> entry : data.entrySet()) {
            String department = entry.getKey(); // 학과(Key)
            List<String> students = entry.getValue(); // 학생 리스트(Value)

            for (String student : students) {
                // 강의명, 학생, 학과 정보를 행으로 추가
                defaultTable.addRow(new Object[]{select, student, department});
            }
        }

        // JTable 생성 및 모델 설정
        JTable table = new JTable(defaultTable);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane);

        setVisible(true);
    }
}
