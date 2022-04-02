package utils;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;

public class GetExcel {

    public static String getData(int page, int row, int col) throws FileNotFoundException {
        String excelPath = "src/test/resources/testData.xlsx"; // path where the xlsx file is saved
        FileInputStream excelFile = new FileInputStream(excelPath);
        XSSFWorkbook workBook = null; // initializing it null so the exception could be caught.
        try {
            workBook = new XSSFWorkbook(excelFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert workBook != null; // asserting that workbook is not empty
        XSSFSheet sheet = workBook.getSheetAt(page); // to get on the sheet which contains data
        XSSFCell cell = sheet.getRow(row).getCell(col); // to get on the cell from which we have to extract data

        // to typecast the cell data to string using DataFormatter
        DataFormatter formatter = new DataFormatter();
        return formatter.formatCellValue(cell);
    }
}