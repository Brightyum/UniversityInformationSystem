/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CourseManagement;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author user
 */
public class ProfessorCheck extends JFrame {
    public ProfessorCheck() {
        setTitle("출석부 확인");
        setSize(600, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        setLocationRelativeTo(null);
        
        JButton attendanceButton = new JButton("출석부 조회");
        attendanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
            }
        });
    }
}
