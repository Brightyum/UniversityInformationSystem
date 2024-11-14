/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project.ui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 *
 * @author user
 */
public class AcademicUi extends JFrame {
    public AcademicUi(){
        setTitle("학사 담당자창");
        setSize(600,550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setLayout(new GridBagLayout());
        GridBagConstraints detail = new GridBagConstraints();
        detail.fill = GridBagConstraints.HORIZONTAL;
        detail.insets = new Insets(10,10,10,10);
        
        JLabel newStudentLabel = new JLabel("새로운 학생 등록");
        detail.gridx = 0;
        detail.gridy = 0;
        add(newStudentLabel, detail);
        
        JTextField newStudent = new JTextField(30);
        detail.gridx = 1;
        detail.gridy = 0;
        add(newStudent, detail);
        
        JButton fixUser = new JButton("사용자 정보 수정");
        detail.gridx = 0;
        detail.gridy = 1;
        add(fixUser, detail);
        
        JButton modifyUser = new JButton("회원 정보 변경");
        modifyUser.addActionListener(new ActionListener() {
            @Override
             public void actionPerformed(ActionEvent e) {
                 new ModifyUser().setVisible(true);
             }
        });
        detail.gridx = 0;
        detail.gridy = 2;
        add(modifyUser, detail);
        
        setVisible(true);
        
    }
    public static void main(String[] args){
        AcademicUi frame = new AcademicUi();
    }
}
