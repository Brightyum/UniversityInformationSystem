package project.CourseManagement;

import javax.swing.*;

public class EnrollmentUi extends JFrame {
    public EnrollmentUi() {
        setTitle("수강신청");
        setSize(600, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        setLocationRelativeTo(null); 
        
        JLabel label = new JLabel("수강신청 화면 어케 구상했는지 모름");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(label);
        
        setVisible(true);
    }
    
    public static void main(String[] args) {
        new EnrollmentUi(); 
    }
}
