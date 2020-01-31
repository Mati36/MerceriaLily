package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JFileChooser;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExelFile {

	// creara, y leera un archivo exel 
	private JFileChooser jFileChooser;
	private FileInputStream fileInput; // abre el archivo
	private FileOutputStream fileOutput; // abre el archivo
	private String nameFile;
	private String path;
	private Workbook workbook;
	private XSSFWorkbook book; // crea el libro
	private Sheet sheet; // crea la hoja
	private Row row;
	private Cell cell;
	
	public ExelFile() {	}

	public JFileChooser getjFileChooser() {return jFileChooser;	}

	public void setjFileChooser(JFileChooser jFileChooser) {this.jFileChooser = jFileChooser;	}

	public FileInputStream getFileInput() {return fileInput;	}

	public void setFileInput(FileInputStream fileInput) {this.fileInput = fileInput;	}

	public FileOutputStream getFileOutput() {return fileOutput;	}

	public void setFileOutput(FileOutputStream fileOutput) {this.fileOutput = fileOutput;}

	public String getNameFile() {return nameFile;}

	public void setNameFile(String nameFile) {this.nameFile = nameFile;}

	public String getPath() {return path;}

	public void setPath(String path) {
		this.path = path;
		if (!this.path.endsWith(".xls"))
			this.path+=".xls";
	}

	public Workbook getWorkbook() {return workbook;}

	public void setWorkbook(Workbook workbook) {this.workbook = workbook;}

	public XSSFWorkbook getBook() {return book;	}

	public void setBook(XSSFWorkbook book) {this.book = book;}

	public Sheet getSheet() {return sheet;}

	public void setSheet(Sheet sheet) {this.sheet = sheet;}
		
	public Row getRow(int index) {return sheet.getRow(index);}

	public void setRow(int index) {sheet.createRow(index);}

//	public Cell getCell(int index) {return row.getCell(index);	}
//
//	public void setCell(int index) {row.createCell(index);}
	// metodos
	public void createFile(String filePath) throws FileNotFoundException {
		fileOutput = new FileOutputStream(filePath);
	}
	
	public void fileOpen(String filePath) throws FileNotFoundException {
		fileInput = new FileInputStream(filePath);
	}
	
	public void fileClose() throws IOException {
		if (fileInput != null) 
			fileInput.close();
		
		if (fileOutput != null)
			fileOutput.close();
		
	}
	
	public void dialogSelectorFile(String content) {
		jFileChooser = new JFileChooser();
		jFileChooser.setDialogTitle(content);
	}
	
	public boolean isLoadFile() {
		return jFileChooser.showOpenDialog(null) == jFileChooser.APPROVE_OPTION;
	}
	
	public boolean isSaveFile() {
		return jFileChooser.showSaveDialog(null) == jFileChooser.APPROVE_OPTION;
	}
	
	public void openBook(FileInputStream file) throws IOException {
		book = new XSSFWorkbook(file);
	}
	
	public void createBook() {
		book = new XSSFWorkbook();
	}
	
	public void bookWrite(FileOutputStream fileOutput) throws IOException {
		book.write(fileOutput);
	}
	
	public void bookClose() throws IOException {
		book.close();
	}
	public void openSheet(int indexSheet) {
		sheet = book.getSheetAt(indexSheet);
	}
	
	public void createSheet(String nameSheet) {
		sheet = book.createSheet(nameSheet);
	}
	
	public void createRow(int index) {
		sheet.createRow(index);
	}
	

	
}
