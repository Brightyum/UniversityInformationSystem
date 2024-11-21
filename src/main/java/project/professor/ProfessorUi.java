package project.professor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ProfessorUi extends JFrame {
    private final JTextField professorNumberField;
    private final JTextField professorNameField;
    private final JTextField departmentField;
    private final JTextField ssnField;
    private final ProfessorExcelHandler professorExcelHandler;

    public ProfessorUi() {
        super("교수 정보 관리 창");
        professorExcelHandler = new ProfessorExcelHandler();

        setSize(600, 600);
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
        button.addActionListener(action); // ActionListener로 변경
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ProfessorUi::new);
    }
}
