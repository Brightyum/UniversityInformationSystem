package project.ui;

import project.professor.ProfessorUi;
import project.student.StudentUi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import project.login.ModifyUserUi;

public class AcademicUi extends JFrame {
    public AcademicUi() {
        setTitle("학사 담당자 창");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints detail = new GridBagConstraints();
        detail.fill = GridBagConstraints.HORIZONTAL;
        detail.insets = new Insets(10, 10, 10, 10);

        // 학생 관리 버튼
        JButton manageStudentButton = new JButton("학생 관리");
        manageStudentButton.addActionListener(e -> {
            new StudentUi().setVisible(true); // Student 관리 UI 열기
        });
        detail.gridx = 0;
        detail.gridy = 0;
        add(manageStudentButton, detail);

        // 교수 관리 버튼
        JButton manageProfessorButton = new JButton("교수 관리");
        manageProfessorButton.addActionListener(e -> {
            new ProfessorUi().setVisible(true); // Professor 관리 UI 열기
        });
        detail.gridx = 0;
        detail.gridy = 1;
        add(manageProfessorButton, detail);
        
        JButton modifyUser = new JButton("회원 정보 변경");
        modifyUser.addActionListener(new ActionListener() {
            @Override
             public void actionPerformed(ActionEvent e) {
                 new ModifyUserUi().setVisible(true);

             }
        });
        detail.gridx = 0;
        detail.gridy = 2;
        add(modifyUser, detail);
        setVisible(true);
    }
}
