/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project.ui;

import javax.swing.*;
import java.awt.*;

//로그인 ui
public class HomeUi extends JFrame{
    public HomeUi() {
        setTitle("로그인");
        setSize(600,550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setLayout(new GridBagLayout());
        GridBagConstraints detail = new GridBagConstraints();
        detail.fill = GridBagConstraints.HORIZONTAL;
        detail.insets = new Insets(10,10,10,10);
        
        JLabel idLabel = new JLabel("아이디: ");
        detail.gridx = 0;
        detail.gridy = 0;
        detail.weightx = 0.3;
        detail.weighty = 0.1;
        add(idLabel, detail);
        
        JTextField id = new JTextField(4);
        detail.gridx = 1;
        detail.gridy = 0;
        detail.weightx = 0.7;
        detail.weighty = 0.1;
        add(id, detail);
       
        JLabel pwdLabel = new JLabel("비밀번호: ");
        detail.gridx = 0;
        detail.gridy = 1;
        detail.weightx = 0.3;
        detail.weighty = 0.1;
        add(pwdLabel, detail);
        
        JTextField pwd = new JTextField(7);
        detail.gridx = 1;
        detail.gridy = 1;
        detail.weightx = 0.7;
        detail.weighty = 0.1;
        add(pwd, detail);
        
        
        JButton figureId = new JButton("로그인");
        detail.gridx = 0;
        detail.gridy = 2;
        detail.gridwidth = 2;
        detail.weightx = 0.5;
        detail.weighty = 1.0;
        add(figureId, detail);
        
        setVisible(true);
        
    }
    public static void main(String[] args) {
        HomeUi frame = new HomeUi();  // JFrame 객체 생성
       // frame.setVisible(true);         // 창을 보이게 설정
    }

}

    