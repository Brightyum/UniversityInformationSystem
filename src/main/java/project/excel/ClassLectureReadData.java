package project.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author duatm
 */
public class ClassLectureReadData {
    private String lectureFilePath;
    private String professorFilePath;
    private int rowIndex;
    private int targetLectureColumnIndex;
    private int departmentIndex;
    private int targetProfessorColumnIndex;
    //private

    public ClassLectureReadData() {
        lectureFilePath = "LectureStaff_data.xlsx";
        professorFilePath = "Professor_data.xlsx";
        rowIndex = 0;
        targetLectureColumnIndex = 1;
        departmentIndex = 2;
        targetProfessorColumnIndex = 1;
    }

    public CopyOnWriteArrayList<String> readClassData() throws IOException {
        CopyOnWriteArrayList<String> data = new CopyOnWriteArrayList<>();
        FileInputStream file = new FileInputStream(lectureFilePath);
        Workbook workbook = new XSSFWorkbook(file);

        // 첫 번째 시트 가져오기
        Sheet sheet = workbook.getSheetAt(rowIndex); // 0번 시트를 선택

        // 모든 행 순회
        for (Row row : sheet) {
            Cell cell = row.getCell(targetLectureColumnIndex);
            if (cell != null) {
                data.add(cell.toString());
            }
        }

        return data;
    }
    //  public CopyOnWriteArrayList<String> readProfessorData(String classSelect) throws IOException{
    public CopyOnWriteArrayList<String> readProfessorData(String classSelect) throws IOException{
        String department = null;
        FileInputStream fileLecture = new FileInputStream(lectureFilePath);
        Workbook workbookLecture = new XSSFWorkbook(fileLecture);
        Sheet sheetLecture = workbookLecture.getSheetAt(rowIndex);
        
        for (Row row : sheetLecture) {
            Cell cell = row.getCell(targetLectureColumnIndex);
            if (cell != null && cell.toString().equals(classSelect)) {
                Cell dataCell = row.getCell(departmentIndex);
                if (dataCell != null) {
                    department = dataCell.toString();
                    System.out.println(department);
                    break;
                } 
            } 
        }
        if (department == null) {
            throw new IllegalArgumentException("강좌를 수정하세요 / " + classSelect);
        }
        CopyOnWriteArrayList<String> professorName = new CopyOnWriteArrayList<>();
        FileInputStream fileProfessor = new FileInputStream(professorFilePath);
        Workbook workbookProfessor = new XSSFWorkbook(fileProfessor);
        Sheet sheetProfessor = workbookProfessor.getSheetAt(rowIndex);
        
        for (Row row : sheetProfessor) {
            Cell cell = row.getCell(departmentIndex);
            String newDepartment = department.replaceAll("\\s+","");
            if (cell != null && cell.toString().equals(newDepartment)) {
                Cell dataCell = row.getCell(targetProfessorColumnIndex);
                if(dataCell != null) {
                    professorName.add(dataCell.toString());
                }
            }
        }
        if (professorName == null) {
            throw new IllegalArgumentException("강좌를 수정하세요 / " + classSelect);
        }
        System.out.println(professorName);
        return professorName;
    }
 
    public static void main(String[] args) throws IOException {
        ClassLectureReadData reader = new ClassLectureReadData();
        CopyOnWriteArrayList<String> columnData = reader.readClassData();
        System.out.println(columnData);
        String test = "남자";
        reader.readProfessorData(test);
    }
}
