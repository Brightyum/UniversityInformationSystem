package CourseManagement;

import CourseManagement.GradesUi;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.table.DefaultTableModel;
import project.excel.*;

public class StudentServicesUi extends JFrame {
    private CopyOnWriteArrayList<String> lectureData;
    private String select;
    private boolean check;
    public StudentServicesUi() throws IOException {
        setTitle("학생 창");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setLayout(new GridBagLayout());
        GridBagConstraints detail = new GridBagConstraints();
        detail.fill = GridBagConstraints.HORIZONTAL; // 버튼이 수평으로 늘어나도록 설정
        detail.insets = new Insets(10, 100, 10, 100); // 버튼 간 여백
       
        LectureExcelReadData readObject = new LectureExcelReadData();
        LectureExcelSaveData saveObject = new LectureExcelSaveData();
        
        lectureData = readObject.getPossibleLecture();
        
        JLabel lectureLabel = new JLabel("수강할 강좌를 고르세요(강좌명/ 담당 교수)");
        detail.gridx = 0;
        detail.gridy = 0;
        add(lectureLabel, detail);
        JComboBox<String> lectureComboBox = new JComboBox<>();
        if (lectureData != null) {
            for (String i : lectureData) {
                lectureComboBox.addItem(i);
            }
        }
        detail.gridx = 1;
        detail.gridy = 0;
        add(lectureComboBox, detail);
        
        // 수강신청 버튼 생성 및 추가
        JButton enrollButton = new JButton("수강신청");
        detail.gridx = 2; // 첫 번째 열
        detail.gridy = 0; // 첫 번째 행
        
        enrollButton.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
              try {
                  // 테스트 데이터 이후에 생성자에서 매개변수로 학생을 구분할 수 있는 구분좌를 설정
                  String name = "염승욱";
                  select = (String)lectureComboBox.getSelectedItem();
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
        
        // 성적 확인 버튼 생성 및 추가
        JButton gradesButton = new JButton("성적 확인");
        detail.gridx = 0; 
        detail.gridy = 1; // 두 번째 행
        add(gradesButton, detail);
        
  
        
        // 성적 확인 버튼 클릭 이벤트 처리
        gradesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GradesUi(); // 새로운 성적 확인 창 열기
            }
        });
        
        JButton getBillButton = new JButton("수강료 확인하기");
        detail.gridx = 1;
        detail.gridy = 1;

        getBillButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String name = "염승욱"; // 학생 이름
                    CopyOnWriteArrayList<String> data = readObject.getConfirmLecture(name); // 데이터를 가져옴

                    StudentBill object = new StudentBill(name, data);
                    object.setVisible(true);
                } catch (Exception ex) {
                    ex.printStackTrace(); // 예외 발생 시 로그 출력
                }
            }
        });
        add(getBillButton, detail);

        setVisible(true);
    }
    
    public static void main(String[] args) throws IOException {
        new StudentServicesUi();
    }
}
