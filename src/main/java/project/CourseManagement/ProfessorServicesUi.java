package project.CourseManagement;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.Insets;
import project.Login.ModifyUserUi;

public class ProfessorServicesUi extends JFrame {
    public ProfessorServicesUi() {
        setTitle("교수 창");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setLayout(new GridBagLayout());
        GridBagConstraints detail = new GridBagConstraints();
        detail.fill = GridBagConstraints.HORIZONTAL;
        detail.insets = new Insets(10, 100, 10, 100); 
        
        // 강좌별 학생 명단 버튼
        JButton studentListButton = new JButton("강좌별 학생 명단");
        studentListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new StudentListUi(); // 강좌별 학생 명단 창 열기
            }
        });
        detail.gridx = 0;
        detail.gridy = 0;
        add(studentListButton, detail);
        
        
        // 출석부 조회 버튼
        JButton attendanceButton = new JButton("출석부 조회");
        attendanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AttendanceUi(); // 새로운 출석부 조회 창 열기
            }
        });
        detail.gridx = 0;
        detail.gridy = 1;
        add(attendanceButton, detail);
        
        
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
        new ProfessorServicesUi(); 
    }
}
