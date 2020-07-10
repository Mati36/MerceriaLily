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

import Exeptions.ExelExeption;
import javafx.collections.ObservableList;


public class ProductoExel{
	
	public ProductoExel() {	}

	
	public void saveExel(ObservableList<Producto> tableList,File file) throws IOException, InvalidFormatException {
		FileOutputStream fileOutput = new FileOutputStream(file); // tratar de elegir la ruta
		ExelFile exel = new ExelFile();
		exel.createBook();
		
	// solo la primera vez que lo guardo mejorar
		ProductoTableExel.createTable(exel,exel.getBook());
		
		
	//	 Esto es bien 
		String sheet_name = ProductoTableExel.getSheetName();
	
		for (Producto producto : tableList) {
			if ( !seach( producto.getIdNegocio(),exel.getSheet(sheet_name) ) ) {
				exel.addRow(sheet_name);
				for (int cell = 0; cell < ProductoTableExel.getIndexLast(); cell++)
					addCellContent(exel,exel.getLatsRow(sheet_name), producto,cell);
			}
		}
		
		exel.writeBook(fileOutput);
		exel.closeBook();
		fileOutput.close();
	}
	
	public void printExelSave(ObservableList<Producto> tableList,File file) throws IOException, InvalidFormatException {
		
		FileOutputStream fileOutput = new FileOutputStream(file); // tratar de elegir la ruta
		ExelFile exel = new ExelFile();
		
	// solo la primera vez que lo guardo mejorar
		exel.createBook();
		ProductoTableExel.createPrintTable(exel,exel.getBook());
	// Esto es bien 
		String sheet_name = ProductoTableExel.getSheetName();
		
		for (Producto producto : tableList) {
			if ( !seach( producto.getIdNegocio(),exel.getSheet(sheet_name) ) ) {
				exel.addRow(sheet_name);
				for (int cell = 0; cell < ProductoTableExel.getIndexLast(); cell++)
					if (cell != ProductoTableExel.getIndexRecargo() && cell != ProductoTableExel.getIndexPrecioCosto())
						addCellContent(exel,exel.getLatsRow(sheet_name), tableList.get(cell),cell);
			}
		}
		
		exel.writeBook(fileOutput);
		exel.closeBook();
		fileOutput.close();
	}


	public void loadExel(ObservableList<Producto> tableList,File file) throws InvalidFormatException, FileNotFoundException, IOException {
		// ver como leer un archivo
		ExelFile exel = new ExelFile();
		FileInputStream fileInput = new FileInputStream(file);
		exel.loadBook(fileInput);
		for (Row row : exel.getSheet(ProductoTableExel.getSheetName())) { 
			if (row.getRowNum() != 0) 
				exelToTable(row, tableList);
		}
		
		fileInput.close();
		exel.closeBook();
	}
		
	public void readExel() {

	}

	public boolean seach(String idProducto,Sheet sheet) {
		for (Row row : sheet) {
			if (isProducto(row,ProductoTableExel.getIndexIdNegocio(),idProducto)) {
				System.out.println("esta");
				return true;
			}
		}
		return false;
	}
	
	private void exelToTable(Row row, ObservableList<Producto> tableList) throws IOException {
		
		if (row != null) {
			
			Producto producto = new Producto();
			for (int cell = 0; cell < row.getLastCellNum(); cell++) 
				getCellContent(cell, row.getCell(cell), producto);
		
			if (!isProductoTable(producto,tableList)) 
				tableList.add(producto);
		}
		else
			new ExelExeption("Fila vacia");
	}

	private boolean isProducto(Row row,int indexCell ,String idProd) {
		return row.getCell(indexCell).getStringCellValue().equals(idProd);
	}
	
	public boolean isProductoTable(Producto producto, ObservableList<Producto>table) {
		for (Producto prod : table) {
			if (prod.getIdNegocio().equals(producto.getIdNegocio())) 
				return true;
		}
		return false;
	}
	
	private void addCellContent(ExelFile exel,Row row,Producto producto,int cell) {
		if (row != null && exel != null && producto != null) {
			if (cell == ProductoTableExel.getIndexIdEmpresa())
				exel.addCellAndValue(row, cell,producto.getIdEmpresa());
			else if (cell == ProductoTableExel.getIndexIdNegocio())
				exel.addCellAndValue(row, cell,producto.getIdNegocio());
			else if (cell == ProductoTableExel.getIndexNombreProducto())
				exel.addCellAndValue(row, cell,producto.getNombre());
			else if (cell == ProductoTableExel.getIndexPrecioCantidad() )
				exel.addCellAndValue(row, cell,producto.getPrecioCantidad());
			else if (cell == ProductoTableExel.getIndexPrecioCosto())
				exel.addCellAndValue(row, cell,producto.getPrecioCosto());
			else if (cell == ProductoTableExel.getIndexPrecioVenta() )
				exel.addCellAndValue(row, cell,producto.getPrecioVenta());
			else if (cell == ProductoTableExel.getIndexRecargo())
				row.createCell(cell).setCellValue(producto.getRecargo());
			else if (cell == ProductoTableExel.getIndexDetalle() )
				exel.addCellAndValue(row, cell, producto.getDetalle());
			else if (cell == ProductoTableExel.getIndexCreatedAt())
				exel.addCellAndValue(row, cell, producto.getCreatedAt());
			else if (cell == ProductoTableExel.getIndexUpdatedAt())
				exel.addCellAndValue(row, cell, producto.getUpdateAt());
			else
				new ExelExeption("No se pudo ingresar ingresar datos en la celda "+cell
							+" puede que se paso de rango o el numero de celda no coincide con la base de datos");
		}
	}

	private void getCellContent(int index,Cell cell, Producto producto) {
		if (cell != null && producto != null) {
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
			else if (index == ProductoTableExel.getIndexDetalle())
				producto.setDetalle(cell.getStringCellValue());
			else if (index == ProductoTableExel.getIndexCreatedAt())
				producto.setCreatedAt(cell.getLocalDateTimeCellValue().toLocalDate());
			else if (index == ProductoTableExel.getIndexUpdatedAt())
				producto.setUpdatedAt(cell.getLocalDateTimeCellValue().toLocalDate());
			else
				new ExelExeption("No se pudo extraer datos en la celda "+cell+" inidice"+ index 
						+" puede que se paso de rango o el numero de celda no coincide con la base de datos");
		}
	}

}
