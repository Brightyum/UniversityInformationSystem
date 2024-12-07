package project.ui;

import project.courseManagement.GradesUi;
import project.courseManagement.StudentBill;
import project.login.ModifyUserUi;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Map;
import javax.swing.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.swing.JOptionPane.showMessageDialog;
import project.excel.*;

public class StudentServicesUi extends JFrame {
    private CopyOnWriteArrayList<String> lectureData;
    private String select;
    private boolean check;

    public StudentServicesUi(String name) throws IOException {
        setTitle("학생 창");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setLayout(new GridBagLayout());
        GridBagConstraints detail = new GridBagConstraints();
        detail.fill = GridBagConstraints.HORIZONTAL; // 버튼이 수평으로 늘어나도록 설정
        detail.insets = new Insets(5, 10, 5, 10); // 버튼 간 여백 (좌, 상, 우, 하)
        detail.weightx = 1.0; // 가로 공간 균등 분배
        detail.weighty = 1.0; // 세로 공간 균등 분배
        
        LectureExcelReadData readObject = new LectureExcelReadData();
        LectureExcelSaveData saveObject = new LectureExcelSaveData();
        
        lectureData = readObject.getPossibleLecture(name);
        
        JLabel lectureLabel = new JLabel("수강할 강좌를 고르세요(강좌명/ 담당 교수)");
        detail.gridx = 0;
        detail.gridy = 0;
        detail.gridwidth = 2;
        add(lectureLabel, detail);
        
        JComboBox<String> lectureComboBox = new JComboBox<>();
        if (lectureData != null) {
            for (String i : lectureData) {
                lectureComboBox.addItem(i);
            }
        }
        detail.gridx = 0;
        detail.gridy = 1;
        detail.gridwidth = 1;
        add(lectureComboBox, detail);
        
        // 수강신청 버튼
        JButton enrollButton = new JButton("수강신청");
        detail.gridx = 1; // 첫 번째 열
        detail.gridy = 1; // 첫 번째 행
        
        enrollButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    //String name = "염승욱";
                    select = (String) lectureComboBox.getSelectedItem();
                    check = saveObject.addStudent(name, select);
                    System.out.println(check);
                    if (check == true) {
                        boolean duplicateClass = saveObject.setStudentData(name, select);
                        if (duplicateClass == true) {
                            showMessageDialog(null, "수강신청완료.");
                        }
                    }
                } catch (IOException ex) {
                    Logger.getLogger(StudentServicesUi.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        add(enrollButton, detail);
        
        // 성적 확인 버튼
        JButton gradesButton = new JButton("성적 확인");
        gradesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    //String name = "염승욱";  // 로그인 했을때 데이터 받아오기
                    Map<String, String> scoreData = readObject.getScore(name);
                    if (scoreData != null) {
                        GradesUi object = new GradesUi(scoreData); // 성적 확인 창 열기
                        object.setVisible(true);
                    } else {
                        showMessageDialog(null, "확인하실 성적이 없습니다.");
                    }
                    
                    
                } catch (IOException ex) {
                    Logger.getLogger(StudentServicesUi.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        detail.gridx = 0; 
        detail.gridy = 2; // 두 번째 행
        add(gradesButton, detail);
        
        // 수강료 확인 버튼
        JButton getBillButton = new JButton("수강료 확인하기");
        detail.gridx = 1;
        detail.gridy = 2;
        
        getBillButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    //String name = "염승욱"; // 학생 이름
                    CopyOnWriteArrayList<String> data = readObject.getConfirmLecture(name); // 데이터를 가져옴

                    StudentBill object = new StudentBill(name, data);
                    object.setVisible(true);
                } catch (IOException ex) {
                    // 예외 발생 시 로그 출력
                    
                }
            }
        });
        add(getBillButton, detail);
        
        JButton modifyUser = new JButton("회원 정보 변경");
         modifyUser.addActionListener(new ActionListener() {
            @Override
             public void actionPerformed(ActionEvent e) {
                 new ModifyUserUi().setVisible(true);
             }
        });
        detail.gridx = 0;
        detail.gridy = 3;
        add(modifyUser, detail);
        setVisible(true);
    }
}
