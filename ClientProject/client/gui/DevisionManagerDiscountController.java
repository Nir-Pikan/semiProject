package gui;

import java.time.LocalDate;

import entities.DiscountEntity;
import io.clientController;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import module.GuiController;
import module.PopUp;
import modules.ServerRequest;
import modules.ServerRequest.Manager;

/** the DevisionManagerDiscount page controller */
public class DevisionManagerDiscountController implements GuiController {

	@FXML
	private TableView<DiscountEntity> discountsTable;

	@FXML
	private TableColumn<DiscountEntity, String> discountID;

	@FXML
	private TableColumn<DiscountEntity, Float> discountInPercentage;

	@FXML
	private TableColumn<DiscountEntity, LocalDate> discountStartDate;

	@FXML
	private TableColumn<DiscountEntity, LocalDate> discountEndDate;

	@FXML
	private TableColumn<DiscountEntity, Void> discountApproval;

	@Override
	public void init() {
		discountsTable.getStyleClass().add("michael-tree-table-view");
		discountID.setCellValueFactory(new PropertyValueFactory<DiscountEntity, String>("DiscountID"));
		discountInPercentage.setCellValueFactory(new PropertyValueFactory<DiscountEntity, Float>("DiscountValue"));
		discountStartDate.setCellValueFactory(new PropertyValueFactory<DiscountEntity, LocalDate>("startDateShow"));
		discountEndDate.setCellValueFactory(new PropertyValueFactory<DiscountEntity, LocalDate>("endDateShow"));

		ServerRequest sr = new ServerRequest(Manager.Discount, "getAllDiscount", "");
		clientController.client.sendRequest(sr);
		String respons = clientController.consumeResponse();

		DiscountEntity[] discountsList = (DiscountEntity[]) ServerRequest.gson.fromJson(respons,
				DiscountEntity[].class);
		if (discountsList == null) {
			PopUp.showInformation("Server Error Uploding Discounts", "Server Failure: Could not upload Discounts",
					"The server was unable to upload the discounts ");
			return;
		}
		discountsTable.setItems(FXCollections.observableArrayList(discountsList));

		addButtonToTable();

	}

	/** adds a new button to the existing table */
	private void addButtonToTable() {

		discountApproval = new TableColumn<DiscountEntity, Void>("Approve\n  Ignore");

		Callback<TableColumn<DiscountEntity, Void>, TableCell<DiscountEntity, Void>> cellFactory = new Callback<TableColumn<DiscountEntity, Void>, TableCell<DiscountEntity, Void>>() {
			@Override
			public TableCell<DiscountEntity, Void> call(final TableColumn<DiscountEntity, Void> param) {
				final TableCell<DiscountEntity, Void> cell = new TableCell<DiscountEntity, Void>() {

					private final Button btnV = new Button(" √ ");
					private final Button btnX = new Button(" X ");

					{
						btnV.setOnAction((ActionEvent event) -> {
							String discountID = getTableView().getItems().get(getIndex()).getDiscountID();

							ServerRequest sr = new ServerRequest(Manager.Discount, "ApproveDiscount", discountID);
							clientController.client.sendRequest(sr);
							String respons = clientController.consumeResponse();
							if (respons.equals("Discount was updated successfully")) {
								discountsTable.getItems().remove(getIndex());
								PopUp.showInformation("New Discount Approved Successfully",
										"New Discount Approved Successfully",
										"Discount was approved and added successfully");
							}

						});
						btnV.getStyleClass().add("btn_yes");

						btnX.setOnAction((ActionEvent event) -> {
							String discountID = getTableView().getItems().get(getIndex()).getDiscountID();

							ServerRequest sr = new ServerRequest(Manager.Discount, "DeleteDiscount", discountID);
							clientController.client.sendRequest(sr);
							String respons = clientController.consumeResponse();
							if (respons.equals("Discount was delete successfully")) {
								discountsTable.getItems().remove(getIndex());
								PopUp.showInformation("Discount was delete successfully",
										"Discount was delete successfully",
										"Discount was unapproved and deleted successfully");
							} else {
								PopUp.showInformation("Discount was delete failed", "Discount was delete failed",
										"Discount failed to deleted by server");
							}

						});
						btnX.getStyleClass().add("btn_no");
					}

					@Override
					public void updateItem(Void item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setGraphic(null);
						} else {
							HBox hBox = new HBox(10, btnV, btnX);
							setGraphic(hBox);
							hBox.setAlignment(Pos.CENTER);
						}
					}

				};

				return cell;
			}
		};
		discountApproval.setCellFactory(cellFactory);
		discountsTable.getColumns().add(discountApproval);

	}
}
