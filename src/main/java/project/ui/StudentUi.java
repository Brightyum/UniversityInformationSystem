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
public class StudentUi extends JFrame{
    public StudentUi(){
        setTitle("학생창");
        setSize(600,550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setLayout(new GridBagLayout());
        GridBagConstraints detail = new GridBagConstraints();
        detail.fill = GridBagConstraints.HORIZONTAL;
        detail.insets = new Insets(10,10,10,10);
        
        JLabel applyLabel = new JLabel("수강 신청 관리");
        detail.gridx = 0;
        detail.gridy = 0;
        add(applyLabel, detail);
        
        JTextField apply = new JTextField(15);
        detail.gridx = 1;
        detail.gridy = 0;
        add(apply, detail);
        
        JButton scoreConfirm = new JButton("성적 확인");
        detail.gridx = 0;
        detail.gridy = 1;
        add(scoreConfirm, detail);
        
        JButton feeConfirm = new JButton("수강료 확인");
        detail.gridx = 0;
        detail.gridy = 2;
        add(feeConfirm, detail);
        
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
        StudentUi frame = new StudentUi();
    }
}
