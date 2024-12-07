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
public class ClassModify extends JFrame {
    private String classSelect;
    private boolean check;
    private JComboBox<String> classComboBox;
    public ClassModify() throws IOException {
        setTitle("수업 담당자창");
        setSize(600,550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
            
        setLayout(new GridBagLayout());
        GridBagConstraints detail = new GridBagConstraints();
        detail.fill = GridBagConstraints.HORIZONTAL;
        detail.insets = new Insets(10,10,10,10);
        
        LectureExcelReadData classData = new LectureExcelReadData();
        CopyOnWriteArrayList<String> data = classData.getClasses();
        LectureExcelSaveData saveModify = new LectureExcelSaveData();
        
        JLabel modifyLabel = new JLabel("수정하실 강좌를 고르세요.");
        detail.gridx = 0;
        detail.gridy = 0;
        add(modifyLabel, detail);
        
        classComboBox = new JComboBox<>();
        
        detail.gridx = 1;
        detail.gridy = 0;
        
        for (String i : data) {
            classComboBox.addItem(i);
        }
        
        add(classComboBox, detail);
        
        JButton delete = new JButton("강좌 삭제하기");
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                classSelect = (String)classComboBox.getSelectedItem();
                try {
                    check = saveModify.deleteClass(classSelect);
                    if (check == true) {
                        showMessageDialog(null, "강좌를 삭제하셨습니다.");
                    } else {
                        showMessageDialog(null, "강좌를 삭제 실패하셨습니다.");
                    }
                } catch (IOException ex) {
                    Logger.getLogger(ClassModify.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        detail.gridx = 2;
        detail.gridy = 0;
        add(delete, detail);
        
        JLabel label = new JLabel("수정하실 강좌 이름, 담당학과,학점 수,강좌에 대한 내용순으로 적으세요(구분좌는 ',')");
        detail.gridx = 0;
        detail.gridy = 1;
        add(label, detail);
        
        JButton reload = new JButton("새로고침");
        reload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    CopyOnWriteArrayList<String> newData = classData.getClasses();
                    classComboBox.removeAllItems();
                    for (String i : newData) {
                        classComboBox.addItem(i);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(ClassModify.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        detail.gridx = 1;
        detail.gridy = 1;
        add(reload, detail);
        
        JTextField modifyField = new JTextField();
        detail.gridx = 0;
        detail.gridy = 2;
        detail.gridwidth = 1;
        add(modifyField, detail);
        
        JButton modifyButton = new JButton("수정하기");
        detail.gridx = 1;
        detail.gridy = 2;
        modifyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                classSelect = (String)classComboBox.getSelectedItem();
                String inputModifyData = modifyField.getText();
     
                try {
                    check = saveModify.modifyClass(classSelect, inputModifyData);
                    if (check == true) {
                        showMessageDialog(null, "강좌를 수정하셨습니다.");
                    } else {
                        showMessageDialog(null, "강좌를 수정 실패하셨습니다.");  
                    }
                } catch (IOException ex) {
                    Logger.getLogger(ClassModify.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        add(modifyButton, detail);
        setVisible(true);
    }
}
