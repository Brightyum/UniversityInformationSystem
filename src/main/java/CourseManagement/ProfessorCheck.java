package CourseManagement;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import project.excel.LectureExcelReadData;

public class ProfessorCheck extends JFrame {

    public ProfessorCheck() {
        setTitle("출석부 확인");
        setSize(600, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        setLocationRelativeTo(null);

        // GridBagLayout 설정
        setLayout(new GridBagLayout());
        GridBagConstraints detail = new GridBagConstraints();
        detail.fill = GridBagConstraints.HORIZONTAL; // 컴포넌트를 수평으로 확장
        detail.insets = new Insets(10, 10, 10, 10);  // 여백 설정

        // LectureExcelReadData 객체 생성
        LectureExcelReadData readObject = new LectureExcelReadData();

        // 버튼 생성 및 설정
        JButton attendanceButton = new JButton("출석부 조회");
        detail.gridx = 0; // 열 위치
        detail.gridy = 0; // 행 위치
        attendanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String name = "염승욱";
                    Map<String, String> data = readObject.getLectureStudent(name);
                    System.out.println(data); // 데이터 출력
                } catch (IOException ex) {
                    Logger.getLogger(ProfessorCheck.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        // GridBagLayout을 사용하여 버튼 추가
        add(attendanceButton, detail);

        setVisible(true); // 프레임 표시
    }

    public static void main(String[] args) {
        new ProfessorCheck();
    }
}
