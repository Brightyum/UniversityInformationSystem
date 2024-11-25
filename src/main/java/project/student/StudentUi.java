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
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

        // 버튼 추가
        addButton("학생 정보 등록", 4, detail, e -> registerStudent());
        addButton("학생 정보 수정", 5, detail, e -> updateStudent());
        addButton("학생 정보 조회", 6, detail, e -> searchStudent());
        addButton("학생 정보 삭제", 7, detail, e -> deleteStudent());

        setVisible(true);
    }

    private void addLabelAndField(String label, int row, GridBagConstraints detail, JTextField field) {
        detail.gridx = 0;
        detail.gridy = row;
        add(new JLabel(label), detail);

        detail.gridx = 1;
        add(field, detail);
    }

    private void addButton(String text, int row, GridBagConstraints detail, ActionListener action) {
        detail.gridx = 1;
        detail.gridy = row;
        JButton button = new JButton(text);
        button.addActionListener(action);
        add(button, detail);
    }

    private void registerStudent() {
        studentExcelHandler.registerStudent(
                studentNumberField.getText(),
                studentNameField.getText(),
                departmentField.getText(),
                ssnField.getText()
        );
        JOptionPane.showMessageDialog(this, "학생 정보가 등록되었습니다.");
    }

    private void updateStudent() {
        boolean success = studentExcelHandler.updateStudent(
                studentNumberField.getText(),
                studentNameField.getText(),
                departmentField.getText(),
                ssnField.getText()
        );
        showMessage(success, "학생 정보가 수정되었습니다.");
    }

    private void deleteStudent() {
        boolean success = studentExcelHandler.deleteStudent(studentNumberField.getText());
        showMessage(success, "학생 정보가 삭제되었습니다.");
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentUi::new);
    }
}
