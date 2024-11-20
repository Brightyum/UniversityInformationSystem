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

public class StudentUi extends JFrame {
    private final JTextField studentNumberField;
    private final JTextField studentNameField;
    private final JTextField departmentField;
    private final JTextField ssnField;

    public StudentUi() {
        setTitle("학생 정보 관리 창");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints detail = new GridBagConstraints();
        detail.fill = GridBagConstraints.HORIZONTAL;
        detail.insets = new Insets(10, 10, 10, 10);

        // 학생 정보 입력
        JLabel studentNumberLabel = new JLabel("학생 번호:");
        detail.gridx = 0;
        detail.gridy = 0;
        add(studentNumberLabel, detail);

        studentNumberField = new JTextField(15);
        detail.gridx = 1;
        detail.gridy = 0;
        add(studentNumberField, detail);

        JLabel studentNameLabel = new JLabel("이름:");
        detail.gridx = 0;
        detail.gridy = 1;
        add(studentNameLabel, detail);

        studentNameField = new JTextField(15);
        detail.gridx = 1;
        detail.gridy = 1;
        add(studentNameField, detail);

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

        // 학생 정보 등록 버튼
        JButton registerStudentButton = new JButton("학생 정보 등록");
        detail.gridx = 1;
        detail.gridy = 4;
        registerStudentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerStudent();
            }
        });
        add(registerStudentButton, detail);

        // 학생 정보 수정 버튼
        JButton updateStudentButton = new JButton("학생 정보 수정");
        detail.gridx = 1;
        detail.gridy = 5;
        updateStudentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateStudent();
            }
        });
        add(updateStudentButton, detail);

        // 학생 정보 조회 버튼
        JButton searchStudentButton = new JButton("학생 정보 조회");
        detail.gridx = 1;
        detail.gridy = 6;
        searchStudentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchStudent();
            }
        });
        add(searchStudentButton, detail);

        // 학생 정보 삭제 버튼
        JButton deleteStudentButton = new JButton("학생 정보 삭제");
        detail.gridx = 1;
        detail.gridy = 7;
        deleteStudentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteStudent();
            }
        });
        add(deleteStudentButton, detail);

        setVisible(true);
    }

    private void registerStudent() {
        String studentNumber = studentNumberField.getText();
        String studentName = studentNameField.getText();
        String department = departmentField.getText();
        String ssn = ssnField.getText();

        List<String> studentData = Arrays.asList(studentNumber, studentName, department, ssn);
        LectureExcelSaveData saveData = new LectureExcelSaveData();
        saveData.saveData(new ArrayList<>(studentData));

        JOptionPane.showMessageDialog(this, "학생 정보가 등록되었습니다.");
    }

    private void updateStudent() {
        try {
            LectureExcelReadData excelReader = new LectureExcelReadData();
            CopyOnWriteArrayList<CopyOnWriteArrayList<Object>> data = excelReader.readLectureStaffExcel();
            String studentNumber = studentNumberField.getText();
            boolean updated = false;

            for (CopyOnWriteArrayList<Object> row : data) {
                if (row.get(0).toString().equals(studentNumber)) {
                    row.set(1, studentNameField.getText());
                    row.set(2, departmentField.getText());
                    row.set(3, ssnField.getText());
                    updated = true;
                    break;
                }
            }

            if (updated) {
                saveUpdatedData(data);
                JOptionPane.showMessageDialog(this, "학생 정보가 수정되었습니다.");
            } else {
                JOptionPane.showMessageDialog(this, "해당 학생 정보를 찾을 수 없습니다.");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "엑셀 데이터를 읽는 중 오류가 발생했습니다: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteStudent() {
        try {
            LectureExcelReadData excelReader = new LectureExcelReadData();
            CopyOnWriteArrayList<CopyOnWriteArrayList<Object>> data = excelReader.readLectureStaffExcel();
            String studentNumber = studentNumberField.getText();
            boolean deleted = false;

            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).get(0).toString().equals(studentNumber)) {
                    data.remove(i);
                    deleted = true;
                    break;
                }
            }

            if (deleted) {
                saveUpdatedData(data);
                JOptionPane.showMessageDialog(this, "학생 정보가 삭제되었습니다.");
            } else {
                JOptionPane.showMessageDialog(this, "해당 학생 정보를 찾을 수 없습니다.");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "엑셀 데이터를 읽는 중 오류가 발생했습니다: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveUpdatedData(CopyOnWriteArrayList<CopyOnWriteArrayList<Object>> data) throws IOException {
        String filePath = "LectureStaff_data.xlsx";
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

    private void searchStudent() {
        try {
            LectureExcelReadData excelReader = new LectureExcelReadData();
            CopyOnWriteArrayList<CopyOnWriteArrayList<Object>> data = excelReader.readLectureStaffExcel();
            String searchNumber = studentNumberField.getText();
            String searchName = studentNameField.getText();

            for (CopyOnWriteArrayList<Object> row : data) {
                if ((searchNumber.isEmpty() || row.get(0).toString().equals(searchNumber)) &&
                        (searchName.isEmpty() || row.get(1).toString().equals(searchName))) {
                    JOptionPane.showMessageDialog(this, "학생 정보: " + row);
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "해당 학생 정보를 찾을 수 없습니다.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "엑셀 데이터를 읽는 중 오류가 발생했습니다: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentUi());
    }
}