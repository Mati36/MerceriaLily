package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExelFile { // manejo de exel

	private  XSSFWorkbook book = null;
	private  Sheet sheet = null;
	private  Row row = null;
	private  Cell cell = null;
			
	public  void createBook() {
		book = new XSSFWorkbook();
	}
	
	public  void createBook(File file) throws InvalidFormatException, IOException {
		book = new XSSFWorkbook();
	} 
	
	public  void loadBook(FileInputStream fileInput) throws InvalidFormatException, IOException {
		ZipSecureFile.setMinInflateRatio(0);
		book = new XSSFWorkbook(fileInput);
	} 

	public  void closeBook() throws IOException {
		book.close();
	}
	
	public  void writeBook(FileOutputStream fileOutput) throws IOException {
		book.write(fileOutput);
	}
	
	public  void createSheet(XSSFWorkbook book,String title) {
		sheet = book.createSheet(title);
	}
	
	public  void addRow(String sheetTitle) {
		int lastRow = getSheet(sheetTitle).getLastRowNum();
		int index = lastRow == 0 && getLatsRow(sheetTitle) == null ? 0: lastRow+1; 
		row = getSheet(sheetTitle).createRow(index);
	}
		
	public  void addRowIndex(String sheetTitle,int index) {
		row = getSheet(sheetTitle).createRow(index);
	}
		
	public  void addCell(Row row, int index) {
		cell = row.createCell(index);
	}
	
	public  void addCellAndValue(Row row, int index, Object value) {
		Cell cell = row.createCell(index);
		addCellValue(value, cell);
		if (row.getRowNum() != 0)
			addFormula(cell, index);
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
		
	public  XSSFWorkbook getBook() {return this.book;	}

	public  Sheet getLastSheet() {return book.getSheetAt(getLastSheetIndex());}
	
	public  int getLastSheetIndex() {return book.getNumberOfSheets();}
	
	public  Row getRow(String sheetTitle,int indexRow) {return getSheet(sheetTitle).getRow(indexRow);	}
	
	public  Row getLatsRow(String sheetTitle) {return getRow(sheetTitle, getLastRowIndex(sheetTitle));}
	
	public  int getLastRowIndex(String sheetTitle) {return getSheet(sheetTitle).getLastRowNum();	}
	
	public  Sheet getSheet(int index) {return book.getSheetAt(index);	}

	public  Sheet getSheet(String sheetTitle) {return book.getSheet(sheetTitle); }
	
	public  Cell getCell(Row row, int index) {return row.getCell(index);	}

	public  Cell getLastCell() {return row.getCell(getLastCellIndex());	}
	
	public  int getLastCellIndex() {return row.getLastCellNum();	}

	public  void setBook(XSSFWorkbook newBook) {book = newBook; }
	
	public void addFormula(Cell cell,int index) {
		CellStyle style = book.createCellStyle();
		CreationHelper createHelper = book.getCreationHelper();
		String formula = "General";
		if (index == ProductoTableExel.getIndexPrecioCantidad() || index == ProductoTableExel.getIndexPrecioCosto() 
				|| index == ProductoTableExel.getIndexPrecioVenta())
			formula = "[$$-2C0A] #,##0.00;-[$$-2C0A] #,##0.00";
		else if (index == ProductoTableExel.getIndexCreatedAt() || index == ProductoTableExel.getIndexUpdatedAt())
			formula = "d/mm/yy";
		if (index == ProductoTableExel.getIndexRecargo()) 
			formula = "0%";
				
		style.setDataFormat(createHelper.createDataFormat().getFormat(formula));
		cell.setCellStyle(style);
	
	}
		
}
