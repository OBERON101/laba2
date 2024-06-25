package laba.project.excel;

import laba.project.info.Compute;
import laba.project.info.Import;
import laba.project.info.Issue;
import laba.project.exceptions.WrongException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Excel {
  private final String exportName = "Calc.xlsx";
  private FileInputStream fis;
  private FileOutputStream fos;
  private XSSFWorkbook wb;

  public Excel() {}

  public Import ImportFromFile(String filename, int sheetNumber)
      throws WrongException {
    initImportWorkbook(filename);
    Import data = fillDataObject(sheetNumber);
    try {
      wb.close();
      fis.close();
    } catch (IOException e) {
      throw new WrongException("Can't close");
    }
    return data;
  }

  public List<String> getSheetNames(String filename) throws WrongException {
    initImportWorkbook(filename);
    List<String> sheetNames = new ArrayList<>();
    for (int i = 0; i < wb.getNumberOfSheets(); i++) {
      sheetNames.add(wb.getSheetName(i));
    }
    try {
      wb.close();
      fis.close();
    } catch (IOException e) {
      throw new WrongException("Не удалось закрыть файл");
    }
    return sheetNames;
  }
  
  private void initImportWorkbook(String filename)
      throws WrongException {
    try {
      fis = new FileInputStream(filename);
      this.wb = new XSSFWorkbook(fis);
    } catch (NullPointerException e) {
      throw new WrongException("Excel file is not choosen");
    } catch (Exception e) {
      throw new WrongException(e.getMessage());
    }
  }

  private Import fillDataObject(int sheetNumber) throws WrongException {
    Import data = new Import();
    try {
      XSSFSheet sheet = wb.getSheetAt(sheetNumber);
      int num_rows = sheet.getPhysicalNumberOfRows();
      double[] xArray = new double[num_rows - 1];
      double[] yArray = new double[num_rows - 1];
      double[] zArray = new double[num_rows - 1];
      for (int i = 1; i < num_rows; i++) {
        xArray[i - 1] = sheet.getRow(i).getCell(0).getNumericCellValue();
        yArray[i - 1] = sheet.getRow(i).getCell(1).getNumericCellValue();
        zArray[i - 1] = sheet.getRow(i).getCell(2).getNumericCellValue();
      }
      data.setxArray(xArray);
      data.setyArray(yArray);
      data.setzArray(zArray);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      throw new WrongException("Something wrong with choosen file");
    }

    return data;
  }

  public void ExportToFile(String folder, Import data)
      throws WrongException {
    Compute calc = new Compute();
    try {
      initExportWorkbook(folder);
      fillCommonSheet(calc, "X", data.getxArray());
      fillCommonSheet(calc, "Y", data.getyArray());
      fillCommonSheet(calc, "Z", data.getzArray());
      fillCovarienceSheet(calc, data);
      wb.write(fos);
      wb.close();
      fos.close();
    } catch (MathIllegalArgumentException e) {
      throw new WrongException("Wrong data in file");
    } catch (IOException e) {
      throw new WrongException("Can't close");
    }
  }

  private void fillCommonSheet(Compute calc, String sheetname, double[] arr) {
    XSSFSheet sheet = wb.createSheet(sheetname);
    ArrayList<Issue> res = calc.calcPerArray(arr);
    int rownum = 0;
    Row row;
    Cell cell;
    for (Issue r : res) {
      row = sheet.createRow(rownum);
      cell = row.createCell(0);
      cell.setCellValue(r.name);
      cell = row.createCell(1);
      cell.setCellValue(r.value.toString());
      rownum++;
    }
  }

  private void fillCovarienceSheet(Compute calc, Import data) {
    XSSFSheet sheet = wb.createSheet("Covariance");
    ArrayList<Issue> res =
        calc.calcCov(data.getxArray(), data.getyArray(), data.getzArray());
    int rownum = 0;
    Row row;
    Cell cell;
    for (Issue r : res) {
      row = sheet.createRow(rownum);
      cell = row.createCell(0);
      cell.setCellValue(r.name);
      cell = row.createCell(1);
      cell.setCellValue(r.value.toString());
      rownum++;
    }
  }

  private void initExportWorkbook(String folder)
      throws WrongException {
    try {
      fos = new FileOutputStream(FilenameUtils.concat(folder, exportName));
      this.wb = new XSSFWorkbook();
    } catch (NullPointerException e) {
      throw new WrongException("Excel file is not choosen");
    } catch (SecurityException e) {
      throw new WrongException("Write permission error");
    } catch (FileNotFoundException e) {
      throw new WrongException("Something wrong with output file: " +
                                        e.getMessage());
    } catch (Exception e) {
      throw new WrongException(e.getMessage());
    }
  }
}
