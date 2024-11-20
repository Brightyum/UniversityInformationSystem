package project.ui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import project.excel.LectureExcelReadData;
import project.excel.LectureExcelSaveData;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.io.File;

public class ProfessorUi extends JFrame {
    private final JTextField professorNumberField;
    private final JTextField professorNameField;
    private final JTextField departmentField;
    private final JTextField ssnField;

    public ProfessorUi() {
        setTitle("교수 정보 관리 창");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints detail = new GridBagConstraints();
        detail.fill = GridBagConstraints.HORIZONTAL;
        detail.insets = new Insets(10, 10, 10, 10);

        // 교수 정보 입력
        JLabel professorNumberLabel = new JLabel("교수 번호:");
        detail.gridx = 0;
        detail.gridy = 0;
        add(professorNumberLabel, detail);

        professorNumberField = new JTextField(15);
        detail.gridx = 1;
        detail.gridy = 0;
        add(professorNumberField, detail);

        JLabel professorNameLabel = new JLabel("이름:");
        detail.gridx = 0;
        detail.gridy = 1;
        add(professorNameLabel, detail);

        professorNameField = new JTextField(15);
        detail.gridx = 1;
        detail.gridy = 1;
        add(professorNameField, detail);

        JLabel departmentLabel = new JLabel("학과:");
        detail.gridx = 0;
        detail.gridy = 2;
        add(departmentLabel, detail);

        departmentField = new JTextField(15);
        detail.gridx = 1;
        detail.gridy = 2;
        add(departmentField, detail);

        JLabel ssnLabel = new JLabel("주민등록번호:");
        detail.gridx = 0;
        detail.gridy = 3;
        add(ssnLabel, detail);

        ssnField = new JTextField(15);
        detail.gridx = 1;
        detail.gridy = 3;
        add(ssnField, detail);

        // 교수 정보 등록 버튼
        JButton registerProfessorButton = new JButton("교수 정보 등록");
        detail.gridx = 1;
        detail.gridy = 4;
        registerProfessorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerProfessor();
            }
        });
        add(registerProfessorButton, detail);

        // 교수 정보 수정 버튼
        JButton updateProfessorButton = new JButton("교수 정보 수정");
        detail.gridx = 1;
        detail.gridy = 5;
        updateProfessorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateProfessor();
            }
        });
        add(updateProfessorButton, detail);

        // 교수 정보 조회 버튼
        JButton searchProfessorButton = new JButton("교수 정보 조회");
        detail.gridx = 1;
        detail.gridy = 6;
        searchProfessorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchProfessor();
            }
        });
        add(searchProfessorButton, detail);

        // 교수 정보 삭제 버튼
        JButton deleteProfessorButton = new JButton("교수 정보 삭제");
        detail.gridx = 1;
        detail.gridy = 7;
        deleteProfessorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteProfessor();
            }
        });
        add(deleteProfessorButton, detail);

        setVisible(true);
    }

    private void registerProfessor() {
        String professorNumber = professorNumberField.getText();
        String professorName = professorNameField.getText();
        String department = departmentField.getText();
        String ssn = ssnField.getText();

        List<String> professorData = Arrays.asList(professorNumber, professorName, department, ssn);
        LectureExcelSaveData saveData = new LectureExcelSaveData();
        saveData.saveData(new ArrayList<>(professorData));

        JOptionPane.showMessageDialog(this, "교수 정보가 등록되었습니다.");
    }

    private void updateProfessor() {
        try {
            LectureExcelReadData excelReader = new LectureExcelReadData();
            CopyOnWriteArrayList<CopyOnWriteArrayList<Object>> data = excelReader.readLectureStaffExcel();
            String professorNumber = professorNumberField.getText();
            boolean updated = false;

            for (CopyOnWriteArrayList<Object> row : data) {
                if (row.get(0).toString().equals(professorNumber)) {
                    row.set(1, professorNameField.getText());
                    row.set(2, departmentField.getText());
                    row.set(3, ssnField.getText());
                    updated = true;
                    break;
                }
            }

            if (updated) {
                saveUpdatedData(data);
                JOptionPane.showMessageDialog(this, "교수 정보가 수정되었습니다.");
            } else {
                JOptionPane.showMessageDialog(this, "해당 교수 정보를 찾을 수 없습니다.");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "엑셀 데이터를 읽는 중 오류가 발생했습니다: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteProfessor() {
        try {
            LectureExcelReadData excelReader = new LectureExcelReadData();
            CopyOnWriteArrayList<CopyOnWriteArrayList<Object>> data = excelReader.readLectureStaffExcel();
            String professorNumber = professorNumberField.getText();
            boolean deleted = false;

            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).get(0).toString().equals(professorNumber)) {
                    data.remove(i);
                    deleted = true;
                    break;
                }
            }

            if (deleted) {
                saveUpdatedData(data);
                JOptionPane.showMessageDialog(this, "교수 정보가 삭제되었습니다.");
            } else {
                JOptionPane.showMessageDialog(this, "해당 교수 정보를 찾을 수 없습니다.");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "엑셀 데이터를 읽는 중 오류가 발생했습니다: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveUpdatedData(CopyOnWriteArrayList<CopyOnWriteArrayList<Object>> data) throws IOException {
        String filePath = "./Professor_data.xlsx";
        FileInputStream fileIn = new FileInputStream(new File(filePath));
        Workbook workbook = new XSSFWorkbook(fileIn);
        Sheet sheet = workbook.getSheetAt(0);

        // Clear existing rows before writing new data
        int lastRowNum = sheet.getLastRowNum();
        for (int i = lastRowNum; i >= 0; i--) {
            Row row = sheet.getRow(i);
            if (row != null) {
                sheet.removeRow(row);
            }
        }

        int rowIndex = 0;
        for (CopyOnWriteArrayList<Object> rowData : data) {
            Row row = sheet.createRow(rowIndex++);
            int cellIndex = 0;
            for (Object cellData : rowData) {
                Cell cell = row.createCell(cellIndex++);
                cell.setCellValue(cellData.toString());
            }
        }

        fileIn.close();
        FileOutputStream fileOut = new FileOutputStream(new File(filePath));
        workbook.write(fileOut);
        fileOut.close();
        workbook.close();
    }

    private void searchProfessor() {
        try {
            LectureExcelReadData excelReader = new LectureExcelReadData();
            CopyOnWriteArrayList<CopyOnWriteArrayList<Object>> data = excelReader.readLectureStaffExcel();
            String searchNumber = professorNumberField.getText();
            String searchName = professorNameField.getText();

            for (CopyOnWriteArrayList<Object> row : data) {
                if ((searchNumber.isEmpty() || row.get(0).toString().equals(searchNumber)) &&
                        (searchName.isEmpty() || row.get(1).toString().equals(searchName))) {
                    JOptionPane.showMessageDialog(this, "교수 정보: " + row);
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "해당 교수 정보를 찾을 수 없습니다.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "엑셀 데이터를 읽는 중 오류가 발생했습니다: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ProfessorUi::new);
    }
}
