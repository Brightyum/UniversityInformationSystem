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
public class ClassesUi extends JFrame {
    public ClassesUi(){
        setTitle("수업 담당자창");
        setSize(600,550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setLayout(new GridBagLayout());
        GridBagConstraints detail = new GridBagConstraints();
        detail.fill = GridBagConstraints.HORIZONTAL;
        detail.insets = new Insets(10,10,10,10);
        
        JButton classSignUp = new JButton("새로운 강좌 등록");
        classSignUp.addActionListener(new ActionListener() {
            @Override
             public void actionPerformed(ActionEvent e) {
                 new ClassRegister().setVisible(true);
             }
        });
        detail.gridx = 0;
        detail.gridy = 0;
        add(classSignUp, detail);
        
        JButton newLecture = new JButton("새로운 강의 개설");
        detail.gridx = 0;
        detail.gridy = 1;
        add(newLecture, detail);
        
        JButton chargeCourse = new JButton("수강료 청구");
        detail.gridx = 0;
        detail.gridy = 2;
        add(chargeCourse, detail);
        
        JButton modifyUser = new JButton("회원 정보 변경");
        modifyUser.addActionListener(new ActionListener() {
            @Override
             public void actionPerformed(ActionEvent e) {
                 new ModifyUser().setVisible(true);
             }
        });
        detail.gridx = 0;
        detail.gridy = 3;
        add(modifyUser, detail);
        
        setVisible(true);
    }
    public static void main(String[] args){
        ClassesUi frame = new ClassesUi();
    }
}
