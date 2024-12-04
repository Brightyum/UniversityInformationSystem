package project.professor;

import project.student.StudentExcelHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ProfessorServiceUi extends JFrame {
    private final JTextField courseNameField; // 강좌명 입력 필드
    private final JTextField studentNumberField; // 학생 번호 입력 필드
    private final JTextField scoreField; // 점수 입력 필드

    private final StudentExcelHandler studentExcelHandler;

    public ProfessorServiceUi() {
        super("점수 관리 창");
        studentExcelHandler = new StudentExcelHandler();

        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // 창 닫기 시 프로그램 종료하지 않음
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints detail = new GridBagConstraints();
        detail.fill = GridBagConstraints.HORIZONTAL;
        detail.insets = new Insets(10, 10, 10, 10);

        // 점수 입력 기능 관련 필드
        studentNumberField = addLabelAndField("학생 번호:", 0, detail);
        courseNameField = addLabelAndField("강좌명:", 1, detail);
        scoreField = addLabelAndField("점수:", 2, detail);

        // 점수 입력 버튼
        addButton("점수 입력/수정", 3, detail, e -> addOrUpdateScore());

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
}
