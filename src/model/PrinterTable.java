package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

import javax.security.auth.callback.Callback;

import org.apache.log4j.helpers.DateTimeDateFormat;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

// duplica hijos en panel, entra varias veces. ver si el la copia de la tebla tambie se hace , muchas veces 

public class PrinterTable extends PrintPaper{
	
	private static char[] alphabet = "abcdefghijklmnñopqrstuvwxyz".toCharArray();
	private static int productoWidth = 0;
	private static int detalleWidth = 0;
	private static Pane pane;
	private static Label dateLb;
	private static Label numPageLb;
	private static double topMargin;
	private static double bottomMargin;
	private static double leftMargin;
	private static double rightMargin;
	private static double paperHeightPrint; 
	private static double paperWidthPrint;
	private static double paperHeight;
	private static double paperWidth;
	// solucion, ajustar el tamaño de todas las columnas, luego sumar el tamaño de las mismas y compararlo con el tamaño de la tabla.
	// si es mas chica le aumentamos el tamaño a la columna mas grande hasta llegar al tamaño de la tabla
	// si es ma chico le reducimos el tamaño a todas hasta llegar al tamaño de la tabla
	
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
		margin(pageLayout);
		sizePaper(pageLayout);
		pane = new Pane();
		pane.setPrefSize(paperWidthPrint, paperHeightPrint);
		datePrint(pane);
		TableView<Producto> copyView = createTableCopy(tableView, job,pageLayout);
		autoResizetext(tableView);
		printList(tableView, copyView, job, pageLayout,pane);
	
	}
	
	private static void printList(TableView<Producto> tableView, TableView<Producto> copyTableView,PrinterJob job,PageLayout pageLayout,Pane pane) {
				
		ArrayList<Producto> itemList;
		// ver como ir cambiando la letra
		for (int i = 0; i < alphabet.length; i++) {
			char letter = alphabet[i];
		
			itemList =  new ArrayList<Producto>(tableView.getItems()
					 									 .filtered(prod -> isLetterStart(prod.getIdNegocio(), letter)));
			while (itemList != null && !itemList.isEmpty()) {
				int num = 1;
				printPage(job, copyTableView, itemList,pageLayout,letter,pane,num);
			}
		}
	
	}
	
	protected static <T> void printPage(PrinterJob job, TableView<T> copyView, ArrayList<T> itemList,PageLayout pageLayout,char letter,Pane pane,int pageNum) {
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
				
		numPagePrint(pane, letter);
	
		if (pane.getChildren().indexOf(copyView) < 0)
			pane.getChildren().add(copyView);
		
		job.printPage(pageLayout,pane);
	
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected
	static <T> TableView<T> createTableCopy(TableView<T> tableView, PrinterJob job,PageLayout pageLayout) {
		TableView<T> copyView = new TableView<>();
							
		if (pageLayout.getPageOrientation().equals(PageOrientation.PORTRAIT)) {
			copyView.setPrefHeight(paperHeightPrint);
			copyView.setPrefWidth(paperWidthPrint);
			
		} else {
			copyView.setPrefHeight(paperWidthPrint);
			copyView.setPrefWidth(paperHeightPrint);
		}
		
		copyView.setTranslateY(topMargin + dateLb.getPrefHeight() + 10);
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
		copyView.getStylesheets().add(PrintPaper.class.getResource("PrinterTableStyle.css").toString());
		copyView.getStyleClass().add("tablePrinter");
		return copyView;
	}
	
	private static boolean isLetterStart(String id, Character letter) {
		char temp = Character.toUpperCase(letter);
		return id.toUpperCase().startsWith(Character.toString(temp));
	}
	
	private static <S> TableColumn<S, Producto> columnText(TableColumn cloneColumn) {
		String text = cloneColumn.getText().trim();
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
			if(detalleWidth < detalle)
				detalleWidth = detalle;
			else if (productoWidth < name)
				productoWidth = name;
		}
		
	}
	
	private static <T,S> void columnMaxWidth(TableView<T> tableView,TableColumn<S, Producto> cloneColumn,TableColumn<T, ?> colum) {
		maxWidth((TableView<Producto>) tableView);
		String title = colum.getText();
		double maxWhidth = colum.getMaxWidth();
		
		if (title.equals(ProductoTableExel.getRowIdEmpresa())) {
			cloneColumn.setMaxWidth(maxWhidth/2 + 1200);
			cloneColumn.setStyle("-fx-alignment: CENTER;");
		}
		else if (title.equals(ProductoTableExel.getRowProducto())) { 
			cloneColumn.setMaxWidth(maxWhidth/2);
			cloneColumn.setStyle("-fx-alignment: CENTER;");
		}
		else if (title.equals(ProductoTableExel.getRowDetalle())) 
			cloneColumn.setMaxWidth(maxWhidth + getDetalleWidth()+150);
		
		else { 
			cloneColumn.setMaxWidth(maxWhidth/3);
			cloneColumn.setStyle("-fx-alignment: CENTER;");
		}
	}
	
	private static void sizePaper(PageLayout pageLayout) {
		paperHeight = pageLayout.getPaper().getHeight();
		paperWidth = pageLayout.getPaper().getWidth();
		paperHeightPrint = pageLayout.getPrintableHeight() - bottomMargin - topMargin; 
		paperWidthPrint = pageLayout.getPrintableWidth();
	}
	
	private static void margin(PageLayout pageLayout) {
		topMargin 		= pixelRound(pageLayout.getTopMargin());
		bottomMargin	= pixelRound(pageLayout.getBottomMargin());
		leftMargin	 	= pixelRound(pageLayout.getLeftMargin());
		rightMargin	 	= pixelRound(pageLayout.getRightMargin());
	}
	
	
	private static void datePrint(Pane pane) {
		
		LocalDate lDate = LocalDate.now();
		String date = Producto.dateFormated(lDate).trim();
				
		double coordX =  paperWidthPrint - (paperWidthPrint / 4);
		if (pane.getChildren().indexOf(dateLb) < 0) {
			dateLb = new Label();
			dateLb.setTranslateX(coordX - date.length() );
			dateLb.setTranslateY(5);
			pane.getChildren().add(dateLb);
		}
		dateLb.setText(date);
	}
	
	private static void numPagePrint(Pane pane, char letter) {
		if (pane.getChildren().indexOf(numPageLb) < 0) {
			numPageLb = new Label();
			numPageLb.setTranslateX(leftMargin + rightMargin);
			numPageLb.setTranslateY(5);
			pane.getChildren().add(numPageLb);
		}
		numPageLb.setText(String.valueOf(letter).toUpperCase().trim());
		
		
	}
	
	public static void autoResizeColumns( TableView<?> table )
	{
		
	       //Set the right policy
	       table.setColumnResizePolicy( TableView.UNCONSTRAINED_RESIZE_POLICY);
	       table.getColumns().stream().forEach( (column) ->
	       {
	    	   String text = column.getText();
	    	   if ( !( text.equals(ProductoTableExel.ROW_DETALLE_ABBREVIATED) 
	    		)) {
	    		 
			    	   Text t = new Text( text );
			           double max = t.getLayoutBounds().getWidth();
			         
			          
			        	   for ( int i = 0; i < table.getItems().size(); i++ )
				           {
				               //cell must not be empty
				               if ( column.getCellData( i ) != null )
				               {
				                   t = new Text( column.getCellData( i ).toString() );
				                   double calcwidth = t.getLayoutBounds().getWidth();
				                   //remember new max-width
				                   if ( calcwidth > max )
				                	   max = calcwidth;
				                 
				               }
				            	            	   
				           }
				           //set the new max-widht with some extra space
				           column.setPrefWidth( max + 10.0d );
	    	   }   	           
	       } );
	      
	}
	
	private static void autoResizetext( TableView<Producto> table )
	{
		for (int i = 0; i < table.getItems().size(); i++) {
			Producto item = table.getItems().get(i);
			
			if (item != null) 
				table.getItems().set(i, productSizeText(item));
						
		}
     
	}
	
	private static String resizeText(String value,int max) {
		value = value.replaceAll("\n", " ").trim();
		if (value.length() > max) {
     	   
			String[] aux = value.split(" ");
			String temp = "";
			int textSize = 0;
	 	   	for (int i = 0; i < aux.length; i++) {
	 	   		
	 	   		int wordSize = aux[i].length();
	 	   		
	 	   		if (textSize + wordSize >= max && wordSize > 4) {
	 	   			temp += "\n"+aux[i];
					textSize = wordSize;
				}
	 	   		else {
	 	   			temp += " "+aux[i];
	 	   			textSize += wordSize;
	 	   		}		   
	 	   	}
			value = temp.trim();
		}
		return value;
	}	
	
	private static Producto productSizeText(Producto producto ) {
		int max = 16;
		String idNegocio = producto.getIdNegocio();
		String idEmpresa = producto.getIdEmpresa();
		String name = producto.getNombre();
		String detalle = producto.getDetalle();
			
		producto.setIdNegocio(resizeText(idNegocio, max));
		producto.setIdEmpresa(resizeText(idEmpresa, max));
		producto.setNombre(resizeText(name, max));
		producto.setDetalle(resizeText(detalle, max));
		return producto;
	
	}
	public static int getProductoWidth() { return productoWidth;	}

	public static void setProductoWidth(int productoWidth) { PrinterTable.productoWidth = productoWidth;	}

	public static int getDetalleWidth() { return detalleWidth;	}

	public static void setDetalleWidth(int detalleWidth) { PrinterTable.detalleWidth = detalleWidth;	}

	public static double getTopMargin() { return topMargin;	}

	public static void setTopMargin(double topMargin) { PrinterTable.topMargin = topMargin;	}

	public static double getBottomMargin() { return bottomMargin;	}

	public static void setBottomMargin(double bottomMargin) { PrinterTable.bottomMargin = bottomMargin;	}

	public static double getLeftMargin() { return leftMargin;	}

	public static void setLeftMargin(double leftMargin) { PrinterTable.leftMargin = leftMargin;	}

	public static double getRightMargin() { return rightMargin;	}

	public static void setRightMargin(double rightMargin) { PrinterTable.rightMargin = rightMargin;	}

	public static double getPaperHeightPrint() { return paperHeightPrint;	}

	public static void setPaperHeightPrint(double paperHeightPrint) { PrinterTable.paperHeightPrint = paperHeightPrint;	}

	public static double getPaperWidthPrint() { return paperWidthPrint;	}

	public static void setPaperWidthPrint(double paperWidthPrint) { PrinterTable.paperWidthPrint = paperWidthPrint;	}

	public static double getPaperHeight() { return paperHeight;	}

	public static void setPaperHeight(double paperHeight) { PrinterTable.paperHeight = paperHeight;	}

	public static double getPaperWidth() { return paperWidth;	}

	public static void setPaperWidth(double paperWidth) { PrinterTable.paperWidth = paperWidth;	}
	
	
}	
	
