package CourseManagement;

import CourseManagement.GradesUi;
import CourseManagement.EnrollmentUi;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class StudentServicesUi extends JFrame {
    public StudentServicesUi() {
        setTitle("학생 창");
        setSize(600, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 창을 화면 중앙에 배치
        
        setLayout(new GridBagLayout());
        GridBagConstraints detail = new GridBagConstraints();
        detail.fill = GridBagConstraints.HORIZONTAL; // 버튼이 수평으로 늘어나도록 설정
        detail.insets = new java.awt.Insets(80, 100, 80, 100); // 버튼 간 여백
        
        // 수강신청 버튼 생성 및 추가
        JButton enrollButton = new JButton("수강신청");
        detail.gridx = 0; // 첫 번째 열
        detail.gridy = 0; // 첫 번째 행
        detail.weightx = 1.0; // 수평 비율
        detail.weighty = 1.0; // 수직 비율
        add(enrollButton, detail);
        
        // 성적 확인 버튼 생성 및 추가
        JButton gradesButton = new JButton("성적 확인");
        detail.gridx = 0; // 동일한 열
        detail.gridy = 1; // 두 번째 행
        add(gradesButton, detail);

        // 수강신청 버튼 클릭 이벤트 처리
        enrollButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new EnrollmentUi(); // 새로운 수강신청 창 열기
            }
        });
        
        // 성적 확인 버튼 클릭 이벤트 처리
        gradesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GradesUi(); // 새로운 성적 확인 창 열기
            }
        });

        setVisible(true);
    }
    
    public static void main(String[] args) {
        new StudentServicesUi(); // JFrame 객체 생성
    }
}
