package project.CourseManagement;

import javax.swing.*;

public class StudentListUi extends JFrame {
    public StudentListUi() {
        setTitle("강좌별 학생 명단");
        setSize(600, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel label = new JLabel("학생 명단 어케 구상했는지 모름", SwingConstants.CENTER);
        add(label);

        setVisible(true);
    }
    
    public static void main(String[] args) {
        new StudentListUi(); 
    }
}
