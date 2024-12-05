package project.ui;

import Login.NavigationManager;
import Login.LoginHandler;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HomeUi extends JFrame {
    public HomeUi() {
        // JFrame 설정
        setTitle("로그인"); // 창 제목 설정
        setSize(400, 200); // 창 크기 설정
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //닫기 버튼 클릭 시 프로그램 종료
        setLocationRelativeTo(null); // 창을 화면 중앙에 배치
        
        // 레이아웃 설정
        setLayout(new GridBagLayout());
        GridBagConstraints detail = new GridBagConstraints();
        detail.fill = GridBagConstraints.HORIZONTAL; // 컴포넌트 수평으로 늘릴 수 있도록 설정
        detail.insets = new Insets(25, 30, -20, 30); // (위쪽, 왼쪽, 아이디 비번 사이, 오른쪽)
        
        JLabel idLabel = new JLabel("아이디 : "); // 아이디 레이블
        detail.gridx = 0; // 첫 번째 열
        detail.gridy = 0; // 첫 번째 행
        detail.weightx = 0.1; // 열 비율
        detail.weighty = 0.1; // 행 비율
        add(idLabel, detail); // 레이블 추가
        
        JTextField id = new JTextField(4); // 아이디 입력 필드
        detail.gridx = 1;
        detail.gridy = 0; 
        detail.weightx = 0.1;
        detail.weighty = 0.1;
        add(id, detail); // 텍스트 필드 추가
        
        JLabel pwdLabel = new JLabel("비밀번호 : "); // 비밀번호 레이블
        detail.gridx = 0;
        detail.gridy = 1;
        detail.weightx = 0.3;
        detail.weighty = 0.1;
        add(pwdLabel, detail); // 레이블 추가
        
        JPasswordField pwd = new JPasswordField(7); // 비밀번호 입력 필드
        detail.gridx = 1;
        detail.gridy = 1;
        detail.weightx = 0.7;
        detail.weighty = 0.1;
        add(pwd, detail); // 텍스트 필드 추가
        
        JButton figureId = new JButton("로그인"); // 로그인 버튼
        detail.gridx = 0;
        detail.gridy = 2;
        detail.gridwidth = 2; // 두 열에 걸쳐 배치
        detail.weightx = 0.5;
        detail.weighty = 1.0;
        detail.insets = new Insets(10, 30, 0, 30); // 버튼 위쪽 여백을 더 줄임
        add(figureId, detail);
        
        // 버튼 클릭 이벤트 처리
        figureId.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String enteredId = id.getText().trim();
            String enteredPwd = pwd.getText().trim();

            // 로그인 검증
            Map<String, String> loginResult = LoginHandler.checkLogin(enteredId, enteredPwd);
            if (loginResult != null) {
                String role = loginResult.get("role");
                String name = loginResult.get("name"); // 이름 데이터 (없을 경우 null)

                dispose(); // 현재 창 닫기

                try {
                    // 역할에 따라 새 창 열기 (NavigationManager 수정 필요 시 반영)
                    NavigationManager.navigate(role, name); // 역할과 이름 전달
                } catch (IOException ex) {
                    Logger.getLogger(HomeUi.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                JOptionPane.showMessageDialog(null, "로그인 실패! 아이디 또는 비밀번호를 확인하세요.", "실패", JOptionPane.ERROR_MESSAGE);
            }
        }
    });

        
        setVisible(true); // 창 보이게
    }
    
    public static void main(String[] args) {
        new HomeUi();  // JFrame 객체 생성
    }
}
