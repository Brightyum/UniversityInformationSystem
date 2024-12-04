package project.CourseManagement;

import javax.swing.*;

public class GradesUi extends JFrame {
    public GradesUi() {
        setTitle("성적 확인");
        setSize(600, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        setLocationRelativeTo(null);
        
        JLabel label = new JLabel("성적 확인 화면 어케 구상했는지 모름");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(label);
        
        setVisible(true);
    }
    
    public static void main(String[] args) {
        new GradesUi(); 
    }
}
