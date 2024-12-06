/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project.classer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import static javax.swing.JOptionPane.*;
import project.excel.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author duatm
 */
public class ClassLecture extends JFrame {
    private String classSelect;
    private String professorSelect;
    private boolean check;
    public ClassLecture() {
        try {
            setTitle("수업 담당자창");
            setSize(600,550);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
            
            JLabel professorLabel = new JLabel("교수님을 선택하세요");
            detail.gridx = 0;
            detail.gridy = 1;
            add(professorLabel, detail);
            
            JComboBox<String> professorComboBox = new JComboBox<>();
            detail.gridx = 1;
            detail.gridy = 1;
            add(professorComboBox, detail);
            
            
            JButton classButton = new JButton("확인");
            detail.gridx = 2;
            detail.gridy = 0;
           classButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    classSelect = (String) comboBox.getSelectedItem();
                    System.out.println(classSelect);
                    try {
                        // 기존 항목 제거
                        professorComboBox.removeAllItems();

                        // 새 데이터를 추가
                        CopyOnWriteArrayList<String> professorName = data.readProfessorData(classSelect);
                        for (String i : professorName) {
                            professorComboBox.addItem(i);
                        }
                    } catch (IOException ioException) {
                        Logger.getLogger(ClassLecture.class.getName()).log(Level.SEVERE, null, ioException);
                    }
                }
            });
            add(classButton, detail);
            
            
            JLabel minStudentLabel = new JLabel("최소 학생을 지정하세요");
            detail.gridx = 0;
            detail.gridy = 2;
            add(minStudentLabel, detail);
            
            JTextField minStudentText = new JTextField(10);
            detail.gridx = 1;
            detail.gridy = 2;
            add(minStudentText, detail);
           
            JLabel maxStudentLabel = new JLabel("최대 학생을 지정하세요");
            detail.gridx = 0;
            detail.gridy = 3;
            add(maxStudentLabel, detail);
            
            JTextField maxStudentText = new JTextField(10);
            detail.gridx = 1;
            detail.gridy = 3;
            add(maxStudentText, detail);
            
            JButton minMaxConfirm = new JButton("강의 최종 확인");
            minMaxConfirm.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    String minInput = minStudentText.getText();
                    String maxInput = maxStudentText.getText();
                    professorSelect = (String)professorComboBox.getSelectedItem();
                    check = false;
                    LectureExcelSaveData saveLecture = new LectureExcelSaveData();
                    try {
                        check = saveLecture.finalConfirm(minInput, maxInput, professorSelect, classSelect);
                        if (check == true) {
                            showMessageDialog(null, "강의를 등록하셨습니다.");
                        } else {
                            showMessageDialog(null, "강좌 등록을 실패하셨습니다.");
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(ClassLecture.class.getName()).log(Level.SEVERE, null, ex);
                    }  
                }
            });
            detail.gridx = 2;
            detail.gridy = 3;
            add(minMaxConfirm, detail);
            
            
            setVisible(true);
        } catch (IOException ex) {
            Logger.getLogger(ClassLecture.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public static void main(String[] args){
        ClassLecture a = new ClassLecture();
        
    }
}
