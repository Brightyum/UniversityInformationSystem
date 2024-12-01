package CourseManagement;

import javax.swing.*;

public class ProfessorServicesUi extends JFrame {
    public ProfessorServicesUi() {
        setTitle("강좌별 학생 명단 및 출석부 조회");
        setSize(600, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JLabel label = new JLabel("강좌별 학생 명단 및 출석부 조회 창입니다.");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(label);

        setVisible(true);
    }
    
    public static void main(String[] args) {
        new ProfessorServicesUi(); // JFrame 객체 생성
    }
}
