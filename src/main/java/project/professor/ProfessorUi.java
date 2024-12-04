package project.professor;

import project.student.StudentExcelHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ProfessorUi extends JFrame {
    private final JTextField professorNumberField;
    private final JTextField professorNameField;
    private final JTextField departmentField;
    private final JTextField ssnField;

    private final JTextField courseNameField; // 강좌명 입력 필드
    private final JTextField studentNumberField; // 학생 번호 입력 필드
    private final JTextField scoreField; // 점수 입력 필드

    private final ProfessorExcelHandler professorExcelHandler;
    private final StudentExcelHandler studentExcelHandler;

    public ProfessorUi() {
        super("교수 정보 관리 창");
        professorExcelHandler = new ProfessorExcelHandler();
        studentExcelHandler = new StudentExcelHandler();

        setSize(600, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints detail = new GridBagConstraints();
        detail.fill = GridBagConstraints.HORIZONTAL;
        detail.insets = new Insets(10, 10, 10, 10);

        // 교수 정보 입력 필드
        professorNumberField = addLabelAndField("교수 번호:", 0, detail);
        professorNameField = addLabelAndField("이름:", 1, detail);
        departmentField = addLabelAndField("학과:", 2, detail);
        ssnField = addLabelAndField("주민등록번호:", 3, detail);

        // 버튼 추가
        addButton("교수 정보 등록", 4, detail, e -> registerProfessor());
        addButton("교수 정보 수정", 5, detail, e -> updateProfessor());
        addButton("교수 정보 조회", 6, detail, e -> searchProfessor());
        addButton("교수 정보 삭제", 7, detail, e -> deleteProfessor());

        // 점수 입력 기능 관련 필드
        studentNumberField = addLabelAndField("학생 번호:", 8, detail);
        courseNameField = addLabelAndField("강좌명:", 9, detail);
        scoreField = addLabelAndField("점수:", 10, detail);

        // 점수 입력 버튼
        addButton("점수 입력/수정", 11, detail, e -> addOrUpdateScore());

        setVisible(true);
    }

    private JTextField addLabelAndField(String label, int row, GridBagConstraints detail) {
        detail.gridx = 0;
        detail.gridy = row;
        add(new JLabel(label), detail);

        JTextField field = new JTextField(15);
        detail.gridx = 1;
        add(field, detail);

        return field;
    }

    private void addButton(String text, int row, GridBagConstraints detail, ActionListener action) {
        detail.gridx = 1;
        detail.gridy = row;
        JButton button = new JButton(text);
        button.addActionListener(action);
        add(button, detail);
    }

    private void registerProfessor() {
        boolean success = professorExcelHandler.registerProfessor(
                professorNumberField.getText(),
                professorNameField.getText(),
                departmentField.getText(),
                ssnField.getText()
        );
        JOptionPane.showMessageDialog(this, success ? "교수 정보가 등록되었습니다." : "교수 정보 등록에 실패했습니다.");
    }

    private void updateProfessor() {
        boolean success = professorExcelHandler.updateProfessor(
                professorNumberField.getText(),
                professorNameField.getText(),
                departmentField.getText(),
                ssnField.getText()
        );
        JOptionPane.showMessageDialog(this, success ? "교수 정보가 수정되었습니다." : "해당 교수 정보를 찾을 수 없습니다.");
    }

    private void deleteProfessor() {
        boolean success = professorExcelHandler.deleteProfessor(professorNumberField.getText());
        JOptionPane.showMessageDialog(this, success ? "교수 정보가 삭제되었습니다." : "해당 교수 정보를 찾을 수 없습니다.");
    }

    private void searchProfessor() {
        String result = professorExcelHandler.searchProfessor(
                professorNumberField.getText(),
                professorNameField.getText()
        );
        JOptionPane.showMessageDialog(this, result != null ? "교수 정보: " + result : "해당 교수 정보를 찾을 수 없습니다.");
    }

    private void addOrUpdateScore() {
        String studentNumber = studentNumberField.getText();
        String courseName = courseNameField.getText();
        String scoreText = scoreField.getText();

        if (studentNumber.isEmpty() || courseName.isEmpty() || scoreText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "학생 번호, 강좌명, 점수를 모두 입력해주세요.");
            return;
        }

        try {
            int score = Integer.parseInt(scoreText);
            boolean success = studentExcelHandler.addOrUpdateScore(studentNumber, courseName, score);
            JOptionPane.showMessageDialog(this, success ? "점수 입력/수정 완료." : "학생 정보를 찾을 수 없습니다.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "점수는 숫자로 입력해주세요.");
        } catch (IllegalStateException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ProfessorUi::new);
    }
}
