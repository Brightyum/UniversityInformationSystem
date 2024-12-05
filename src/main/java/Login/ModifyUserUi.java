package Login;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

public class ModifyUserUi extends JFrame {

    private JTextField idField;
    private JPasswordField oldPasswordField;
    private JPasswordField newPasswordField;
    private JComboBox<String> roleComboBox;

    public ModifyUserUi() {
        setTitle("회원 정보 변경");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // UI 구성
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel roleLabel = new JLabel("관할:");
        roleComboBox = new JComboBox<>(RoleConfig.getRoles());

        JLabel idLabel = new JLabel("ID:");
        idField = new JTextField(20);

        JLabel oldPasswordLabel = new JLabel("현재 비밀번호:");
        oldPasswordField = new JPasswordField(20);

        JLabel newPasswordLabel = new JLabel("새 비밀번호:");
        newPasswordField = new JPasswordField(20);

        JButton changeButton = new JButton("변경");
        changeButton.addActionListener(new ChangePasswordAction());

        // UI 컴포넌트 추가
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(roleLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        panel.add(roleComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(idLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(idField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(oldPasswordLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(oldPasswordField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(newPasswordLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 3;
        panel.add(newPasswordField, gbc);

        gbc.gridx = 1; gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(changeButton, gbc);

        add(panel);
    }

    private class ChangePasswordAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String role = (String) roleComboBox.getSelectedItem();
            String rawId = idField.getText().trim();
            String oldPassword = new String(oldPasswordField.getPassword()).trim();
            String newPassword = new String(newPasswordField.getPassword()).trim();

            if (rawId.isEmpty() || oldPassword.isEmpty() || newPassword.isEmpty()) {
                JOptionPane.showMessageDialog(ModifyUserUi.this, "모든 필드를 입력해주세요.", "오류", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 아이디 검증을 위해 역할에 따른 접두사 제거
            String idWithoutPrefix = rawId.substring(1);

            // 비밀번호 유효성 검사
            if (!isValidPassword(newPassword)) {
                JOptionPane.showMessageDialog(ModifyUserUi.this, "새 비밀번호는 7자리의 영문자 및 숫자로 구성되어야 합니다.", "오류", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                boolean success = PasswordChanger.changePassword(role, idWithoutPrefix, oldPassword, newPassword);

                if (success) {
                    JOptionPane.showMessageDialog(ModifyUserUi.this, "비밀번호가 성공적으로 변경되었습니다.", "성공", JOptionPane.INFORMATION_MESSAGE);
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(ModifyUserUi.this, "ID 또는 현재 비밀번호가 잘못되었습니다.", "오류", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(ModifyUserUi.this, "오류가 발생했습니다: " + ex.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean isValidPassword(String password) {
        // 비밀번호는 7자리의 영문자 및 숫자로 구성
        return password.length() == 7 && Pattern.matches("^[a-zA-Z0-9]+$", password);
    }

    private void clearFields() {
        idField.setText("");
        oldPasswordField.setText("");
        newPasswordField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ModifyUserUi frame = new ModifyUserUi();
            frame.setVisible(true);
        });
    }
}
