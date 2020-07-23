package model;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Scene;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;

public class PrinterTable extends PrintPaper {
	
	private static char[] alphabet = "abcdefghijklmnñopqrstuvwxyz".toCharArray();
	private static int productoWidth = 0;
	private static int detalleWidth = 0;
	private static Pane pane;
	
	public static <T> void printTable(TableView<Producto> tableView, PrinterJob jobArg) {
		boolean createJob = jobArg == null;
		PrinterJob job;
		
		if (createJob) {
			job = PrinterJob.createPrinterJob();
		} else {
			job = jobArg;
		}
		printWithJobTable(tableView, job);
		if (createJob) {
			job.endJob();
		}
		
	}

	private static void printWithJobTable(TableView<Producto> tableView, PrinterJob job) {
		
		Printer printer = job.getPrinter();
		PageLayout pageLayout = job.getJobSettings().getPageLayout();
			
		pageLayout = printer.createPageLayout(pageLayout.getPaper(),pageLayout.getPageOrientation(), 
												Printer.MarginType.HARDWARE_MINIMUM);
				
		TableView<Producto> copyView = createTableCopy(tableView, job,pageLayout);
		printList(tableView, copyView, job, pageLayout);
	
	}
	
	private static void printList(TableView<Producto> tableView, TableView<Producto> copyTableView,PrinterJob job,PageLayout pageLayout) {
				
		ArrayList<Producto> itemList;
		// ver como ir cambiando la letra
		for (int i = 0; i < alphabet.length; i++) {
			char letter = alphabet[i];
			itemList =  new ArrayList<Producto>(tableView.getItems()
														 .filtered(prod -> isLetterStart(prod.getIdNegocio(), letter)));
			while (itemList != null && !itemList.isEmpty()) {
				printPage(job, copyTableView, itemList,pageLayout,letter);
			}
		}
		
		
	}
	
	protected static <T> void printPage(PrinterJob job, TableView<T> copyView, ArrayList<T> itemList,PageLayout pageLayout,char letter) {
		ScrollBar verticalScrollbar = getVerticalScrollbar(copyView);
		ObservableList<T> batchItemList = FXCollections.observableArrayList();
		copyView.setItems(batchItemList);
		batchItemList.add(itemList.remove(0));
		while (!verticalScrollbar.isVisible() && itemList.size() > 0) {
			T item = itemList.remove(0);
			batchItemList.add(item);
			copyView.layout();
		}
		if (batchItemList.size() > 1 && verticalScrollbar.isVisible()) {
			T item = batchItemList.remove(batchItemList.size() - 1);
			itemList.add(0, item);
			copyView.layout();
		}

		
		job.printPage(pageLayout,pane);
	
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected
	static <T> TableView<T> createTableCopy(TableView<T> tableView, PrinterJob job,PageLayout pageLayout) {
		TableView<T> copyView = new TableView<>();
					
		double topMargin 	 = pixelRound(pageLayout.getTopMargin());
		double bottomMargin	 = pixelRound(pageLayout.getBottomMargin());
		double leftMargin	 = pixelRound(pageLayout.getLeftMargin());
		double rightMargin	 = pixelRound(pageLayout.getRightMargin());
		
		double paperHeightPrint = pageLayout.getPrintableHeight() - bottomMargin - topMargin; 
		double paperWidthPrint = pageLayout.getPrintableWidth() - rightMargin ;
					
		if (pageLayout.getPageOrientation().equals(PageOrientation.PORTRAIT)) {
			copyView.setPrefHeight(paperHeightPrint);
			copyView.setPrefWidth(paperWidthPrint);
			
		} else {
			copyView.setPrefHeight(paperWidthPrint);
			copyView.setPrefWidth(paperHeightPrint);
		}
		
		copyView.setTranslateY(topMargin);
		copyView.setColumnResizePolicy(tableView.getColumnResizePolicy());
		
		for (TableColumn<T, ?> colum : tableView.getColumns()) {
			if (!colum.getText().equals(ProductoTableExel.getRowPrecioCosto()) 
					&& !colum.getText().equals(ProductoTableExel.getRowRecargo())) {
				TableColumn cloneColumn = columnText(colum);
				
				columnMaxWidth(tableView, cloneColumn, colum);
				
				if (colum.getCellValueFactory() != null) 
					cloneColumn.setCellValueFactory(colum.getCellValueFactory());
				
				if (colum.getCellFactory() != null) 
					cloneColumn.setCellFactory(colum.getCellFactory());
				
				copyView.getColumns().add(cloneColumn);
			}
		}
		new Scene(copyView);
	//			copyView.getScene().getStylesheets().add(PrintPaper.class.getResource("TablePrint.css").toString());
		return copyView;
	}
	
	private static boolean isLetterStart(String id, Character letter) {
		char temp = Character.toUpperCase(letter);
		return id.toUpperCase().startsWith(Character.toString(temp));
	}
	
	private static <S> TableColumn<S, Producto> columnText(TableColumn cloneColumn) {
		String text = cloneColumn.getText();
		if (text.equals(ProductoTableExel.getRowIdEmpresa()))
			text = ProductoTableExel.ROW_ID_EMPRESA_ABBREVIATED;
		else if (text.equals(ProductoTableExel.getRowIdNegocio()))
			text = ProductoTableExel.ROW_ID_NEGOCIO_ABBREVIATED;
		else if (text.equals(ProductoTableExel.getRowNombreProducto()))
			text = ProductoTableExel.ROW_PRODUCTO_ABBREVIATED;
		else if (text.equals(ProductoTableExel.getRowDetalle()))
			text = ProductoTableExel.ROW_DETALLE_ABBREVIATED;
		else if (text.equals(ProductoTableExel.getRowPrecioCantidad()))
			text = ProductoTableExel.ROW_PRECIO_CANTIDAD_ABBREVIATED;
		else if (text.equals(ProductoTableExel.getRowPrecioVenta()))
			text = ProductoTableExel.ROW_PRECIO_VENTA_ABBREVIATED;
		else if (text.equals(ProductoTableExel.getRowUpdateAt()))
			text = ProductoTableExel.ROW_UPDATE_AT_ABBREVIATED;
		return new TableColumn<S, Producto>(text);
	}
	
	private static void maxWidth(TableView<Producto> tableView) {
		
		for (Producto prod : tableView.getItems()) {
			int name = prod.getNombre().length();
			int detalle = prod.getDetalle().length();
			if(getProductoWidth() < name)
				setProductoWidth(name);
			else if (getDetalleWidth() < detalle)
				setProductoWidth(detalle); 
		}
		
	}
	
	private static <T,S> void columnMaxWidth(TableView<T> tableView,TableColumn<S, Producto> cloneColumn,TableColumn<T, ?> colum) {
		maxWidth((TableView<Producto>) tableView);
		String title = colum.getText();
		double maxWhidth = colum.getMaxWidth();
		if (title.equals(ProductoTableExel.getRowProducto())) 
			cloneColumn.setMaxWidth(maxWhidth + getProductoWidth());
		
		else if (title.equals(ProductoTableExel.getRowDetalle()))
			cloneColumn.setMaxWidth(maxWhidth + getDetalleWidth());
		else 
			cloneColumn.setMaxWidth(maxWhidth);
		
		
	}
	
	public static int getProductoWidth() {
		return productoWidth;
	}

	public static void setProductoWidth(int productoWidth) {
		PrinterTable.productoWidth = productoWidth;
	}

	public static int getDetalleWidth() {
		return detalleWidth;
	}

	public static void setDetalleWidth(int detalleWidth) {
		PrinterTable.detalleWidth = detalleWidth;
	}
}	
	
