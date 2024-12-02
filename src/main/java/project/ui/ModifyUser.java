package project.ui;

import Login.PasswordChanger;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ModifyUser extends JFrame {

    private JTextField idField;
    private JPasswordField oldPasswordField;
    private JPasswordField newPasswordField;

    public ModifyUser() {
        setTitle("회원 정보 변경");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // UI 구성
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // 컴포넌트 간의 간격 설정
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel idLabel = new JLabel("ID:");
        idField = new JTextField(20); // 입력 칸 길이 설정
        JLabel oldPasswordLabel = new JLabel("현재 비밀번호:");
        oldPasswordField = new JPasswordField(20);
        JLabel newPasswordLabel = new JLabel("새 비밀번호:");
        newPasswordField = new JPasswordField(20);

        JButton changeButton = new JButton("변경");
        changeButton.addActionListener(new ChangePasswordAction());

        // UI 컴포넌트 추가
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(idLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        panel.add(idField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(oldPasswordLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(oldPasswordField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(newPasswordLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(newPasswordField, gbc);

        gbc.gridx = 1; gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(changeButton, gbc);

        add(panel);
    }

    // 비밀번호 변경 버튼 액션
    private class ChangePasswordAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String id = idField.getText().trim();
            String oldPassword = new String(oldPasswordField.getPassword()).trim();
            String newPassword = new String(newPasswordField.getPassword()).trim();

            // PasswordChanger 연결
            boolean success = PasswordChanger.changePassword(id, oldPassword, newPassword);

            if (success) {
                JOptionPane.showMessageDialog(ModifyUser.this, "비밀번호가 성공적으로 변경되었습니다.", "성공", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
            } else {
                JOptionPane.showMessageDialog(ModifyUser.this, "비밀번호 변경에 실패했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // 입력 필드 초기화
    private void clearFields() {
        idField.setText("");
        oldPasswordField.setText("");
        newPasswordField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ModifyUser frame = new ModifyUser();
            frame.setVisible(true);
        });
    }
}
