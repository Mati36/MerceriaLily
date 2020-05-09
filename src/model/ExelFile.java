package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import javax.imageio.stream.FileImageOutputStream;

import org.apache.commons.collections4.map.StaticBucketMap;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExelFile { // manejo de exel

	private static XSSFWorkbook book;
	private static Sheet sheet;
	private static Row row;
	private static Cell cell;
			
	public static void createBook() {
		book = new XSSFWorkbook();
	}
	
	public static void createBook(File file) throws InvalidFormatException, IOException {
		book = new XSSFWorkbook();
	} 
	
	public static void loadBook(FileInputStream fileInput) throws InvalidFormatException, IOException {
		book = new XSSFWorkbook(fileInput);
	} 
	
	public static void closeBook() throws IOException {
		book.close();
	}
	
	public static void writeBook(FileOutputStream fileOutput) throws IOException {
		book.write(fileOutput);
	}
	
	public static void createSheet(XSSFWorkbook book,String title) {
		sheet = book.createSheet(title);
	}
	
	public static void addRow(String sheetTitle,int index) {
		int getLastRow = getSheet(sheetTitle).getLastRowNum(); 
		row = getSheet(sheetTitle).createRow(getLastRow);
	}
		
	public static void addRowIndex(String sheetTitle,int index) {
		row = getSheet(sheetTitle).createRow(index);
	}
		
	public static void addCell(Row row, int index) {
		cell = row.createCell(index);
	}
	
	public static void addCellAndValue(Row row, int index, Object value) {
		Cell cell = row.createCell(index);
		addCellValue(value, cell);
	}
		
	public static void addCellValue(Object value,Cell cell) {
		
		 if (value instanceof Integer)
			cell.setCellValue((Integer) value);
		else if (value instanceof Float)
			cell.setCellValue((Float) value);
		else if (value instanceof Double)
			cell.setCellValue((Double) value);
		else if (value instanceof Calendar)
			cell.setCellValue((Calendar) value);
		else if (value instanceof Boolean)
			cell.setCellValue((boolean) value);
		else if (value instanceof Date)
			cell.setCellValue((Date) value);
		else if (value instanceof LocalDate)
			cell.setCellValue((LocalDate) value);
		else if (value instanceof LocalDateTime)
			cell.setCellValue((LocalDateTime) value);
		else if (value instanceof RichTextString)
			cell.setCellValue((RichTextString) value);
		else 
			cell.setCellValue((String) value);
	}
		
	public static XSSFWorkbook getBook() {return book;	}

	public static Sheet getLastSheet() {return book.getSheetAt(getLastSheetIndex());}
	
	public static int getLastSheetIndex() {return book.getNumberOfSheets();}
	
	public static Row getRow(String sheetTitle,int indexRow) {return getSheet(sheetTitle).getRow(indexRow);	}
	
	public static Row getLatsRow(String sheetTitle) {return getRow(sheetTitle, getLastRowIndex(sheetTitle));}
	
	public static int getLastRowIndex(String sheetTitle) {return getSheet(sheetTitle).getLastRowNum();	}
	
	public static Sheet getSheet(int index) {return book.getSheetAt(index);	}

	public static Sheet getSheet(String sheetTitle) {return book.getSheet(sheetTitle); }
	
	public static Cell getCell(Row row, int index) {return row.getCell(index);	}

	public static Cell getLastCell() {return row.getCell(getLastCellIndex());	}
	
	public static int getLastCellIndex() {return row.getLastCellNum();	}

	public static void setBook(XSSFWorkbook newBook) {book = newBook; }
		
		
}
