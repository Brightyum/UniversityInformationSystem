package project.professor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ProfessorUi extends JFrame {
    private final JTextField professorNumberField;
    private final JTextField professorNameField;
    private final JTextField departmentField;
    private final JTextField ssnField;
    private final JTextField gradeField; // 학점 입력 필드

    private final ProfessorExcelHandler professorExcelHandler;

    public ProfessorUi() {
        super("교수 정보 관리 창");
        professorExcelHandler = new ProfessorExcelHandler();

        setSize(400, 500); // UI 크기 조정
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
        gradeField = addLabelAndField("학점:", 4, detail); // 학점 입력 필드

        // 버튼을 그리드 형식으로 배치
        JPanel buttonPanel = new JPanel(new GridLayout(0, 2, 10, 10)); // 2열로 버튼 배치
        buttonPanel.add(createButton("교수 정보 등록", e -> registerProfessor()));
        buttonPanel.add(createButton("교수 정보 수정", e -> updateProfessor()));
        buttonPanel.add(createButton("교수 정보 조회", e -> searchProfessor()));
        buttonPanel.add(createButton("교수 정보 삭제", e -> deleteProfessor()));

        detail.gridx = 0;
        detail.gridy = 5;
        detail.gridwidth = 2; // 버튼 패널이 입력 필드와 동일한 너비를 차지하도록 설정
        add(buttonPanel, detail);

        // 점수 관리 창 열기 버튼을 제거하였습니다.

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

    private JButton createButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.addActionListener(action);
        return button;
    }

    private void registerProfessor() {
        boolean success = professorExcelHandler.registerProfessor(
                professorNumberField.getText(),
                professorNameField.getText(),
                departmentField.getText(),
                ssnField.getText(),
                gradeField.getText() // 학점 전달
        );
        JOptionPane.showMessageDialog(this, success ? "교수 정보가 등록되었습니다." : "교수 정보 등록에 실패했습니다.");
    }

    private void updateProfessor() {
        try {
            boolean success = professorExcelHandler.updateProfessor(
                    professorNumberField.getText(),
                    professorNameField.getText(),
                    departmentField.getText(),
                    ssnField.getText(),
                    gradeField.getText() // 학점 전달
            );
            JOptionPane.showMessageDialog(this, success ? "교수 정보가 수정되었습니다." : "해당 교수 정보를 찾을 수 없습니다.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void deleteProfessor() {
        try {
            boolean success = professorExcelHandler.deleteProfessor(professorNumberField.getText());
            JOptionPane.showMessageDialog(this, success ? "교수 정보가 삭제되었습니다." : "해당 교수 정보를 찾을 수 없습니다.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void searchProfessor() {
        String result = professorExcelHandler.searchProfessor(
                professorNumberField.getText(),
                professorNameField.getText()
        );
        JOptionPane.showMessageDialog(this, result != null ? "교수 정보: " + result : "해당 교수 정보를 찾을 수 없습니다.");
    }

    // 점수 관리 창 열기 메서드를 제거하였습니다.

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ProfessorUi::new);
    }
}
