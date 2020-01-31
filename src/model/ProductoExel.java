package model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import javafx.collections.ObservableList;


public class ProductoExel extends ExelFile {
	
	// esta clase crea el archivo exel y lo lee y escribe
	
	public ProductoExel() {
		
	}
	
	public void exelSave(ObservableList<Producto> tableList,int idexLast,String fileName) throws IOException {
				
		String nameFile = fileName+".xls";
		openExelFile("Guardando archivo..",nameFile );
		setNameFile(nameFile);
		System.out.println("Inicio tabla"+tableList != null);
		if (isSaveApprove()) {
			setPath(getjFileChooser().getSelectedFile().getPath());
			createBook();
			createSheet("Productos");
			rowCellTitel();
			
			for (int i = 0; i < tableList.size(); i++) 
				rowCellValue(tableList.get(i),idexLast, i++);
			
			creteFile();
			bookWrite(getFile());		
			bookClose();
			fileClose();
			
		}
	}
	
	public void loadExel(ObservableList<Producto> tableList,int idexLast,String fileName) throws InvalidFormatException, FileNotFoundException, IOException {
		openExelFile("Abriendo archivo", fileName);
		setNameFile(fileName);
		
		if (isLoadApprove()) {
			openBook(new FileInputStream(getPath()));
			setSheet(getBook().getSheetAt(0));
			
			for (int i = 1; i < getSheet().getLastRowNum(); i++) {
				exelToTable(getSheet().getRow(i), tableList);
			}
			
			bookClose();
			fileClose();
		}
		
	}
	
	private void rowCellTitel() {
		createRow(0);
		
		for (int i = 0; i < SaveProducto.getRowName().length; i++)
			getRow(0).createCell(i).setCellValue(SaveProducto.getRowName()[i]);
	}
		
	private void rowCellValue(Producto producto,int indexLast,int idexRow) {
		Row row = createRow(idexRow);
		
		for (int j = 0; j < indexLast; j++) 
			setCellContent(j, producto,row);
				
	}
	
	private void exelToTable(Row row, ObservableList<Producto> tableList) throws IOException {
		if (row != null) {
			Producto producto = new Producto();
			for (int i = 0; i < row.getLastCellNum(); i++) {
				Cell cell = row.getCell(i); 
				getCellContent(i, cell, producto);
				tableList.add(producto);
			}
		}
		
	}
	
	private void setCellContent(int i, Producto producto,Row row) {
		if (i == SaveProducto.getIndexIdEmpresa()) 
			row.createCell(i).setCellValue(producto.getIdEmpresa());
		else if (i == SaveProducto.getIndexIdNegocio()) 
			row.createCell(i).setCellValue(producto.getIdNegocio());
		else if (i == SaveProducto.getIndexNombreProducto())
			row.createCell(i).setCellValue(producto.getNombre());
		else if (i == SaveProducto.getIndexPrecioCantidad() )
			row.createCell(i).setCellValue(producto.getPrecioCantidad());
		else if (i == SaveProducto.getIndexPrecioCosto())
			row.createCell(i).setCellValue(producto.getPrecioCosto());
		else if (i == SaveProducto.getIndexPrecioVenta() )
			row.createCell(i).setCellValue(producto.getPrecioVenta());
		else if (i == SaveProducto.getIndexRecargo())
			row.createCell(i).setCellValue(producto.getRecargo());
		else
			System.out.println("Error al ingresar datos en exel");
	}
	
	private void  getCellContent(int index,Cell cell, Producto producto) {
		if (index == SaveProducto.getIndexIdEmpresa()) 
			producto.setIdEmpresa(cell.getStringCellValue());	
		else if ( index == SaveProducto.getIndexIdNegocio()) 
			producto.setIdNegocio(cell.getStringCellValue());
		else if (index == SaveProducto.getIndexNombreProducto())
			producto.setNombre(cell.getStringCellValue());
		else if (index == SaveProducto.getIndexPrecioCantidad())
			producto.setPrecioCantidad(cell.getNumericCellValue());
		else if (index == SaveProducto.getIndexPrecioCosto())
			producto.setPrecioCosto((cell.getNumericCellValue()));
		else if (index == SaveProducto.getIndexPrecioVenta())
			producto.setPrecioCosto((cell.getNumericCellValue()));
		else if (index == SaveProducto.getIndexRecargo())
				producto.setRecargo((cell.getNumericCellValue()));		
		else
			System.out.println("Error al ingresar datos en exel");
	}
	
}
