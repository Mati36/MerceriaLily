package model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JFileChooser;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExelFile {

	// creara, y leera un archivo exel 
	
	private XSSFWorkbook book; // crea el libro
	private FileInputStream file; // abre el archivo
	private Sheet sheet; // crea la hoja
	private String nameFile;
	private String path;
	private JFileChooser jFileChooser;
	
	
	public ExelFile() {
		
	}
	// metodos 
	public void creteFile() throws FileNotFoundException {
		file = new FileInputStream(this.path);
	}
	
	public void createSheet(String titelSheet) {
		book.createSheet(titelSheet);
		
	}
	
	public void createBook() {
		book = new XSSFWorkbook();
	}
	
	public void openBook(FileInputStream fileOpen) throws InvalidFormatException, IOException {
		book = new XSSFWorkbook(fileOpen);
	}
	
	public void openExelFile(String content, String nameFile) {
		jFileChooser = new JFileChooser(nameFile);
		jFileChooser.setDialogTitle(content);
		
	}
	
	public Row createRow(int index) {
		Row row = getSheet().createRow(index);
		return row;
	}
	
	public void roxCreateCell(int roxIdex,int cellIndex) {
		sheet.getRow(roxIdex).createCell(cellIndex);
	}
	
	public void bookWrite(FileOutputStream file) throws IOException {
		book.write(file);
	}
	
	public Cell getRowCell(int roxIdex,int cellIndex) {
		return sheet.getRow(roxIdex).createCell(cellIndex);
	}
	
	public Row getRow(int roxIdex) {
		return sheet.getRow(roxIdex);
	}
	
	public boolean isSaveApprove() {
		return  jFileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION;
	}
	
	public boolean isLoadApprove() {
		return jFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION;
	}
	
	public boolean isExelFile() {
		return path.endsWith(".xls");
	}
	
	/// propiedades
	public void fileClose() throws IOException {
		file.close();
	}
	
	public void bookClose() throws IOException {
		book.close();
	}

	public XSSFWorkbook getBook() {
		return book;
	}

	public void setBook(XSSFWorkbook book) {
		this.book = book;
	}

	public FileInputStream getFile() {
		return file;
	}

	public void setFile(FileInputStream file) {
		this.file = file;
	}

	public Sheet getSheet() {
		return sheet;
	}

	public void setSheet(Sheet sheet) {
		this.sheet = sheet;
	}

	public String getNameFile() {
		return nameFile;
	}

	public void setNameFile(String nameFile) {
		this.nameFile = nameFile;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		if(!path.endsWith(".xls")) 	
			path+=".xls";
		this.path = path;
	}
	
	public void setfile(FileOutputStream file) {
		this.file = file;
	}

	public JFileChooser getjFileChooser() {
		return jFileChooser;
	}

	public void setjFileChooser(JFileChooser jFileChooser) {
		this.jFileChooser = jFileChooser;
	}
	
	
}
