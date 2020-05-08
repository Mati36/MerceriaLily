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

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExelFile { // manejo de exel

	XSSFWorkbook book;
	Sheet sheet;
	Row row;
	Cell  cell;
	
	public ExelFile(XSSFWorkbook book, Sheet sheet, Row row,Cell cell) {
		this.book = book;
		this.sheet = sheet;
		this.row = row;
		this.cell = cell;
	}
	
	public ExelFile() {
		this.book = null;
		this.sheet = null;
		this.row = null;
		this.cell = null;
	}
	
	public void createBook() {
		this.book = new XSSFWorkbook();
	}
	
	public void createBook(File file) throws InvalidFormatException, IOException {
		this.book = new XSSFWorkbook(file);
	} 
	
	public void loadBook(FileInputStream fileInput) throws InvalidFormatException, IOException {
		this.book = new XSSFWorkbook(fileInput);
	} 
	
	public void closeBook() throws IOException {
		this.book.close();
	}
	
	public void writeBook(FileOutputStream fileOutput) throws IOException {
		book.write(fileOutput);
	}
	
	public void createSheet(XSSFWorkbook book,String title) {
		this.sheet = book.createSheet(title);
	}
	
	public void addRow(String sheetTitle,int index) {
		int getLastRow = getSheet(sheetTitle).getLastRowNum(); 
		this.row = getSheet(sheetTitle).createRow(getLastRow);
	}
	
	
	
	public void addRowIndex(String sheetTitle,int index) {
		this.row = getSheet(sheetTitle).createRow(index);
	}
	
	
	public void addCell(Row row, int index) {
		this.cell = row.createCell(index);
	}
	
	public void addCellAndValue(Row row, int index, Object value) {
		Cell cell = row.createCell(index);
		addCellValue(value, cell);
	}
		
	public void addCellValue(Object value,Cell cell) {
		
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
	
	
	public XSSFWorkbook getBook() {return book;	}

	public Sheet getLastSheet() {return this.book.getSheetAt(getLastSheetIndex());}
	
	public int getLastSheetIndex() {return book.getNumberOfSheets();}
	
	public Row getRow(String sheetTitle,int indexRow) {return getSheet(sheetTitle).getRow(indexRow);	}
	
	public Row getLatsRow(String sheetTitle) {return getRow(sheetTitle, getLastRowIndex(sheetTitle));}
	
	public int getLastRowIndex(String sheetTitle) {return getSheet(sheetTitle).getLastRowNum();	}
	
	public Sheet getSheet(int index) {return this.book.getSheetAt(index);	}

	public Sheet getSheet(String sheetTitle) {return this.book.getSheet(sheetTitle); }
	
	public Cell getCell(Row row, int index) {return row.getCell(index);	}

	public Cell getLastCell() {return row.getCell(getLastCellIndex());	}
	
	public int getLastCellIndex() {return row.getLastCellNum();	}

	public void setBook(XSSFWorkbook book) {
		this.book = book;
	}
		
			
	//	public static XSSFWorkbook createBook() {
	//		return new XSSFWorkbook();
	//	}
	//	
	//	public static XSSFWorkbook createBook(File file) throws InvalidFormatException, IOException {
	//		return new XSSFWorkbook(file);
	//	} 
	//	
	//	public static Sheet CreateSheet(XSSFWorkbook book,String title) {
	//		return book.createSheet(title);
	//	}
	//	
	//	public static Row addRow(Sheet sheet,int index) {
	//		return sheet.createRow(index);
	//	}
	//
	//	public static Cell addCell(Row row, int index) {
	//		return row.createCell(index);
	//	}
	//	
	//	public static void addCellAndValue(Row row, int index, Object value) {
	//		Cell cell = row.createCell(index);
	//		addCellValue(value, cell);
	//	}
	//		
	//	public static void addCellValue(Object value,Cell cell) {
	//		
	//		 if (value instanceof Integer)
	//			cell.setCellValue((Integer) value);
	//		else if (value instanceof Float)
	//			cell.setCellValue((Float) value);
	//		else if (value instanceof Double)
	//			cell.setCellValue((Double) value);
	//		else if (value instanceof Calendar)
	//			cell.setCellValue((Calendar) value);
	//		else if (value instanceof Boolean)
	//			cell.setCellValue((boolean) value);
	//		else if (value instanceof Date)
	//			cell.setCellValue((Date) value);
	//		else if (value instanceof LocalDate)
	//			cell.setCellValue((LocalDate) value);
	//		else if (value instanceof LocalDateTime)
	//			cell.setCellValue((LocalDateTime) value);
	//		else if (value instanceof RichTextString)
	//			cell.setCellValue((RichTextString) value);
	//		else 
	//			cell.setCellValue((String) value);
	//	}
		
	}
