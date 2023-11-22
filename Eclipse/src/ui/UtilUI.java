package ui;

import java.util.Date;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import essenciais.Pagina;

public class UtilUI {
	public static <T> ObservableList<T> getObservableList(List<T> objetos) {
		return FXCollections.<T> observableArrayList(objetos);
	}

	public static TableColumn<Pagina, Date> getUltUtilCol() {
		TableColumn<Pagina, Date> paginaUltUtilCol = new TableColumn<>("Ultima Utilização");
		paginaUltUtilCol.setCellFactory(col -> {
			TableCell<Pagina, Date> cell = new TableCell<Pagina, Date>() {
				@Override
				public void updateItem(Date item, boolean empty) {
					super.updateItem(item, empty);
					this.setText(null);
					this.setGraphic(null);

					if (!empty) {
						this.setText(item.toString());
					}
				}
			};
			return cell;
		});
		return paginaUltUtilCol;
	}
}