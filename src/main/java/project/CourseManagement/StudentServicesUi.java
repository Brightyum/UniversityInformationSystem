package project.CourseManagement;

import project.CourseManagement.GradesUi;
import project.CourseManagement.EnrollmentUi;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import project.Login.ModifyUserUi;

public class StudentServicesUi extends JFrame {
    public StudentServicesUi() {
        setTitle("학생 창");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setLayout(new GridBagLayout());
        GridBagConstraints detail = new GridBagConstraints();
        detail.fill = GridBagConstraints.HORIZONTAL; // 버튼이 수평으로 늘어나도록 설정
        detail.insets = new Insets(10, 100, 10, 100); // 버튼 간 여백
        
        // 수강신청 버튼
        JButton enrollButton = new JButton("수강신청");
        enrollButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new EnrollmentUi(); // 수강신청 창 열기
            }
        });
        detail.gridx = 0; 
        detail.gridy = 0; // 첫 번째 행
        add(enrollButton, detail);
        
        
        // 성적 확인 버튼
        JButton gradesButton = new JButton("성적 확인");
        gradesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GradesUi(); // 성적 확인 창 열기
            }
        });
        detail.gridx = 0; 
        detail.gridy = 1; // 두 번째 행
        add(gradesButton, detail);
        
        
        // 회원 정보 변경 버튼
        JButton modifyUser = new JButton("회원 정보 변경");
        modifyUser.addActionListener(new ActionListener() {
            @Override
             public void actionPerformed(ActionEvent e) {
                 new ModifyUserUi().setVisible(true);
             }
        });
        detail.gridx = 0;
        detail.gridy = 3;
        add(modifyUser, detail);

        setVisible(true);
    }
    
    public static void main(String[] args) {
        new StudentServicesUi();
    }
}
