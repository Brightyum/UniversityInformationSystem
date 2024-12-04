package CourseManagement;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StudentExcelReader {

    public static List<String> readStudentList(String filePath) throws IOException {
        List<String> studentList = new ArrayList<>();
        FileInputStream fis = new FileInputStream(new File(filePath));
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            Cell cell = row.getCell(0);
            if (cell != null) {
                studentList.add(cell.getStringCellValue());
            }
        }
        workbook.close();
        fis.close();
        return studentList;
    }
}
