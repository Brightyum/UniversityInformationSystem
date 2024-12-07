package project.student;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class StudentUi extends JFrame {
    private final JTextField studentNumberField;
    private final JTextField studentNameField;
    private final JTextField departmentField;
    private final JTextField ssnField;
    private final StudentExcelHandler studentExcelHandler;

    public StudentUi() {
        super("학생 정보 관리 창");
        studentExcelHandler = new StudentExcelHandler();
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints detail = new GridBagConstraints();
        detail.fill = GridBagConstraints.HORIZONTAL;
        detail.insets = new Insets(10, 10, 10, 10);

        // 학생 정보 입력
        addLabelAndField("학생 번호:", 0, detail, studentNumberField = new JTextField(15));
        addLabelAndField("이름:", 1, detail, studentNameField = new JTextField(15));
        addLabelAndField("학과:", 2, detail, departmentField = new JTextField(15));
        addLabelAndField("주민등록번호:", 3, detail, ssnField = new JTextField(15));

        // 버튼 패널 추가
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10)); // 2x2 그리드
        addButton(buttonPanel, "학생 정보 등록", e -> registerStudent());
        addButton(buttonPanel, "학생 정보 수정", e -> updateStudent());
        addButton(buttonPanel, "학생 정보 조회", e -> searchStudent());
        addButton(buttonPanel, "학생 정보 삭제", e -> deleteStudent());

        detail.gridx = 0;
        detail.gridy = 4;
        detail.gridwidth = 2; // 패널이 전체 폭을 차지하도록 설정
        add(buttonPanel, detail);

        setVisible(true);
    }

    private void addLabelAndField(String label, int row, GridBagConstraints detail, JTextField field) {
        detail.gridx = 0;
        detail.gridy = row;
        add(new JLabel(label), detail);

        detail.gridx = 1;
        add(field, detail);
    }

    private void addButton(JPanel panel, String text, ActionListener action) {
        JButton button = new JButton(text);
        button.addActionListener(action);
        panel.add(button);
    }

    private void registerStudent() {
        boolean success = studentExcelHandler.registerStudent(
                studentNumberField.getText(),
                studentNameField.getText(),
                departmentField.getText(),
                ssnField.getText()
        );
        JOptionPane.showMessageDialog(this, success ? "학생 정보가 등록되었습니다." : "학생 정보 등록에 실패했습니다.");
    }

    private void updateStudent() {
        try {
            boolean success = studentExcelHandler.updateStudent(
                    studentNumberField.getText(),
                    studentNameField.getText(),
                    departmentField.getText(),
                    ssnField.getText()
            );
            showMessage(success, "학생 정보가 수정되었습니다.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void deleteStudent() {
        try {
            boolean success = studentExcelHandler.deleteStudent(studentNumberField.getText());
            showMessage(success, "학생 정보가 삭제되었습니다.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void searchStudent() {
        String result = studentExcelHandler.searchStudent(
                studentNumberField.getText(),
                studentNameField.getText()
        );
        JOptionPane.showMessageDialog(this, result != null ? "학생 정보: " + result : "해당 학생 정보를 찾을 수 없습니다.");
    }

    private void showMessage(boolean success, String successMsg) {
        JOptionPane.showMessageDialog(this, success ? successMsg : "해당 학생 정보를 찾을 수 없습니다.");
    }

}
