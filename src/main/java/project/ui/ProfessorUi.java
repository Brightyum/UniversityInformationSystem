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
public class ProfessorUi extends JFrame{
    public ProfessorUi() {
        setTitle("교수창");
        setSize(600,550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        //test
        setLayout(new GridBagLayout());
        GridBagConstraints detail = new GridBagConstraints();
        detail.fill = GridBagConstraints.HORIZONTAL;
        detail.insets = new Insets(10,10,10,10);
        
        JButton myStudents = new JButton("나의 강좌 학생 명단");
        detail.gridx = 0;
        detail.gridy = 1;
        add(myStudents, detail);
        
        JLabel lectureLabel = new JLabel("강좌 출석부");
        detail.gridx = 0;
        detail.gridy = 0;
        add(lectureLabel, detail);
        
        JTextField lecture = new JTextField(10);
        detail.gridx = 1;
        detail.gridy = 0;
        add(lecture, detail);
        
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
        ProfessorUi frame = new ProfessorUi();
    }
}
