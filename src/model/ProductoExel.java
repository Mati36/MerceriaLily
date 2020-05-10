package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import javafx.collections.ObservableList;


public class ProductoExel{

	

	// esta clase crea el archivo exel y lo lee y escribe

	
	
	public ProductoExel() {
		
	}

	
	public void saveExel(ObservableList<Producto> tableList,File file) throws IOException, InvalidFormatException {
		
		FileOutputStream fileOutput = new FileOutputStream(file); // tratar de elegir la ruta
		ExelFile.createBook();
		
	// solo la primera vez que lo guardo mejorar
		ProductoTableExel.createTable(ExelFile.getBook());
		
	// Esto es bien 
		String sheet_name = ProductoTableExel.getSheetName();
		
		for (Producto producto : tableList) {
			
			if ( !seach( producto.getIdNegocio(),ExelFile.getSheet(sheet_name) ) ) {
			
				ExelFile.addRow(sheet_name, ExelFile.getLastRowIndex(sheet_name) + 1);
				for (int cell = 0; cell < ProductoTableExel.getIndexLast(); cell++)
					addCellContent(ExelFile.getLatsRow(sheet_name), tableList.get(cell),cell);
			}
		}
		
		ExelFile.writeBook(fileOutput);
		ExelFile.closeBook();
		fileOutput.close();
	}
	
	public void printExelSave(ObservableList<Producto> tableList,File file) throws IOException, InvalidFormatException {
		
		FileOutputStream fileOutput = new FileOutputStream(file); // tratar de elegir la ruta
		

	// solo la primera vez que lo guardo mejorar
		ExelFile.createBook();
		ProductoTableExel.createTable(ExelFile.getBook());
	// Esto es bien 
		String sheet_name = ProductoTableExel.getSheetName();

		for (Producto producto : tableList) {
			
			if ( !seach( producto.getIdNegocio(),ExelFile.getSheet(sheet_name) ) ) {
			
				ExelFile.addRow(sheet_name, ExelFile.getLastRowIndex(sheet_name) + 1);
				for (int cell = 0; cell < ProductoTableExel.getIndexLast(); cell++)
					if (cell != ProductoTableExel.getIndexRecargo() && cell != ProductoTableExel.getIndexPrecioCosto())
						addCellContent(ExelFile.getLatsRow(sheet_name), tableList.get(cell),cell);
			}
		}
		
		ExelFile.writeBook(fileOutput);
		ExelFile.closeBook();
		fileOutput.close();
	}


	public void loadExel(ObservableList<Producto> tableList,File file) throws InvalidFormatException, FileNotFoundException, IOException {
		// ver como leer un archivo
		FileInputStream fileInput = new FileInputStream(file);
		ExelFile.loadBook(fileInput);

		for (Row row : ExelFile.getSheet(ProductoTableExel.getSheetName()))
			exelToTable(row, tableList);

		fileInput.close();

	}
	
		
	public void readExel() {

	}

	public boolean seach(String idProducto,Sheet sheet) {

		for (Row row : sheet) {
			if (isProducto(row,0,idProducto));
				return true;
		}
		return false;
	}
	
	private void exelToTable(Row row, ObservableList<Producto> tableList) throws IOException {
		
		if (row != null) {
			
			Producto producto = new Producto();
			for (int cell = 0; cell < row.getLastCellNum(); cell++) {
				getCellContent(cell, row.getCell(cell), producto);
				tableList.add(producto);
			}
		}
		else
			System.out.println("vacio");
	}

	private boolean isProducto(Row row,int indexRow ,String idProd) {
		return row.getCell(indexRow).getStringCellValue().equals(idProd);
	}
	
	private void addCellContent(Row row,Producto producto,int cell) {
		if (cell == ProductoTableExel.getIndexIdEmpresa())
			ExelFile.addCellAndValue(row, cell,producto.getIdEmpresa());
		else if (cell == ProductoTableExel.getIndexIdNegocio())
			ExelFile.addCellAndValue(row, cell,producto.getIdNegocio());
		else if (cell == ProductoTableExel.getIndexNombreProducto())
			ExelFile.addCellAndValue(row, cell,producto.getNombre());
		else if (cell == ProductoTableExel.getIndexPrecioCantidad() )
			ExelFile.addCellAndValue(row, cell,producto.getPrecioCantidad());
		else if (cell == ProductoTableExel.getIndexPrecioCosto())
			ExelFile.addCellAndValue(row, cell,producto.getPrecioCosto());
		else if (cell == ProductoTableExel.getIndexPrecioVenta() )
			ExelFile.addCellAndValue(row, cell,producto.getPrecioVenta());
		else if (cell == ProductoTableExel.getIndexRecargo())
			row.createCell(cell).setCellValue(producto.getRecargo());
		else
			System.out.println("Error al ingresar datos en exel");
	}

	private void getCellContent(int index,Cell cell, Producto producto) {
		if (index == ProductoTableExel.getIndexIdEmpresa())
			producto.setIdEmpresa(cell.getStringCellValue());
		else if ( index == ProductoTableExel.getIndexIdNegocio())
			producto.setIdNegocio(cell.getStringCellValue());
		else if (index == ProductoTableExel.getIndexNombreProducto())
			producto.setNombre(cell.getStringCellValue());
		else if (index == ProductoTableExel.getIndexPrecioCantidad())
			producto.setPrecioCantidad(cell.getNumericCellValue());
		else if (index == ProductoTableExel.getIndexPrecioCosto())
			producto.setPrecioCosto((cell.getNumericCellValue()));
		else if (index == ProductoTableExel.getIndexPrecioVenta())
			producto.setPrecioCosto((cell.getNumericCellValue()));
		else if (index == ProductoTableExel.getIndexRecargo())
				producto.setRecargo((cell.getNumericCellValue()));
		else
			System.out.println("Error al ingresar datos en exel");
	}

}
