package CourseManagement;

import javax.swing.*;

public class AttendanceUi extends JFrame {
    public AttendanceUi() {
        setTitle("출석부 조회");
        setSize(600, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel label = new JLabel("출석부 조회 화면 어케 구상했는지 모름", SwingConstants.CENTER);
        add(label);

        setVisible(true);
    }
    
    public static void main(String[] args) {
        new AttendanceUi(); 
    }
}
