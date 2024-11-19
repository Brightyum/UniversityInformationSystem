/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project.ui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.*;
import javax.swing.table.DefaultTableModel;
import project.excel.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author duatm
 */
public class ClassLecture extends JFrame {
    public ClassLecture() {
        try {
            setTitle("수업 담당자창");
            setSize(600,550);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            
            setLayout(new GridBagLayout());
            GridBagConstraints detail = new GridBagConstraints();
            detail.fill = GridBagConstraints.HORIZONTAL;
            detail.insets = new Insets(10,10,10,10);
            
            JLabel dataLabel = new JLabel("강좌를 선택하세요");
            detail.gridx = 0;
            detail.gridy = 0;
            add(dataLabel, detail);
            ClassLectureReadData data = new ClassLectureReadData();
            CopyOnWriteArrayList<String> classData = data.readClassData();
            
            JComboBox<String> comboBox = new JComboBox<>();
            
            for (String i : classData) {
                comboBox.addItem(i);
            }
            
            comboBox.setBounds(100, 50, 200, 30);
            add(comboBox);
            
            JComboBox<String> professorComboBox = new JComboBox<>();
            
            JButton makeLecture = new JButton("강의 개설하기");
            detail.gridx = 1;
            detail.gridy = 1;
            
            makeLecture.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    
                }
            });
            
            setVisible(true);
        } catch (IOException ex) {
            Logger.getLogger(ClassLecture.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public static void main(String[] args){
        ClassLecture a = new ClassLecture();
        
    }
}
