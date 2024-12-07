package project.courseManagement;

import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class StudentListUi extends JFrame {
    public StudentListUi(String select, Map<String, List<String>> data) {
        setTitle("출석부");
        setSize(600, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columnNames = {"강의명", "학생명", "학번", "학점"};
        DefaultTableModel defaultTable = new DefaultTableModel(columnNames, 0);

        // data를 순회하며 테이블 데이터 추가
        for (Map.Entry<String, List<String>> entry : data.entrySet()) {
            String studentName = entry.getKey(); // 학생명 (key)
            List<String> values = entry.getValue(); // 학번과 학점 (value)

            // value에서 학번과 학점을 가져옴
            String studentId = values.get(0); // 학번
            String credit = values.get(1); // 학점

            // 행 데이터를 추가
            defaultTable.addRow(new Object[]{select, studentName, studentId, credit});
        }


        // JTable 생성 및 모델 설정
        JTable table = new JTable(defaultTable);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane);

        setVisible(true);
    }
    
}
