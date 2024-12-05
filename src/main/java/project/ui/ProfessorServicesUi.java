package project.ui;

import CourseManagement.AttendanceUi;
import CourseManagement.StudentListUi;
import project.professor.ProfessorServiceUi; // ProfessorServiceUi를 임포트합니다.
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.Insets;
import Login.ModifyUserUi;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import project.excel.LectureExcelReadData;

public class ProfessorServicesUi extends JFrame {
    private String select;
    public ProfessorServicesUi() throws IOException {
        setTitle("교수 창");
        setSize(400, 400); 
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setLayout(new GridBagLayout());
        GridBagConstraints detail = new GridBagConstraints();
        detail.fill = GridBagConstraints.HORIZONTAL;
        detail.insets = new Insets(10, 100, 10, 100); 
        
        LectureExcelReadData readObject = new LectureExcelReadData();
        
        JLabel lectureLabel = new JLabel("강의를 선택하세요.");
        detail.gridx = 0;
        detail.gridy = 0;
        add(lectureLabel, detail);
        
        JComboBox<String> professorComboBox = new JComboBox<>();
        detail.gridx = 1;
        detail.gridy = 0;
        
        String name = "염승욱"; //매개변수
        CopyOnWriteArrayList<String> lectureName = readObject.lectureName(name);
       
        for (String lecture : lectureName) {
            professorComboBox.addItem(lecture);
        }
        
        add(professorComboBox, detail);
        
        JButton studentListButton = new JButton("출석부 조회");
        studentListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    select = (String)professorComboBox.getSelectedItem();
                    Map<String, List<String>> data = readObject.getStudentInformation(name, select);
                    System.out.println(data);
                    StudentListUi object = new StudentListUi(select, data);
                    
                } catch (IOException ex) {
                    Logger.getLogger(ProfessorServicesUi.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        detail.gridx = 0;
        detail.gridy = 1;
        add(studentListButton, detail);
        
        JButton attendanceButton = new JButton("강좌별 학생 명단 조회");
        attendanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String name = "염승욱";
                    select = (String)professorComboBox.getSelectedItem();
                    Map<String, List<String>> data = readObject.getLectureStudent(name, select);
                    AttendanceUi object = new AttendanceUi(data, select); 
                    object.setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(ProfessorServicesUi.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        });
        detail.gridx = 1;
        detail.gridy = 1;
        add(attendanceButton, detail);
        
        JButton modifyUser = new JButton("회원 정보 변경");
        modifyUser.addActionListener(new ActionListener() {
            @Override
             public void actionPerformed(ActionEvent e) {
                 new ModifyUserUi().setVisible(true);
             }
        });
        detail.gridx = 0;
        detail.gridy = 2;
        add(modifyUser, detail);
        
        JButton scoreManagementButton = new JButton("점수 관리");
        scoreManagementButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ProfessorServiceUi(); 
            }
        });
        detail.gridx = 0;
        detail.gridy = 3;
        add(scoreManagementButton, detail);

        setVisible(true);
    }
    
    public static void main(String[] args) throws IOException {
        new ProfessorServicesUi(); 
    }
}
