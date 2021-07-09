package View_Controller;

import Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
/**controller class for the add product fmxl gui page*/
public class AddProductController implements Initializable {


    Stage stage;
    Parent scene;

    private ObservableList<Part> AddProductAssociatedPartsList = FXCollections.observableArrayList();
    private ObservableList<Part> AddProductPartsList = FXCollections.observableArrayList();

    @FXML
    private TextField addProductFieldID;
    @FXML
    private TextField addProductFieldName;
    @FXML
    private TextField addProductFieldInv;
    @FXML
    private TextField addProductFieldPrice;
    @FXML
    private TextField addProductFieldMax;
    @FXML
    private TextField addProductFieldMin;
    @FXML
    private TextField addProductFieldSearch;
    @FXML
    private TableView<Part> addProductPartsTable;
    @FXML
    private TableColumn<Part, Integer> addProductPartsTablePartID;
    @FXML
    private TableColumn<Part, String> addProductPartsTablePartName;
    @FXML
    private TableColumn<Part, Integer> addProductPartsTableInvCount;
    @FXML
    private TableColumn<Part, Integer> addProductPartsTablePPU;
    @FXML
    private TableView<Part> addProductsAssociatedPartTable;
    @FXML
    private TableColumn<Part, Integer> addProductsAssociatedPartTablePartID;
    @FXML
    private TableColumn<Part, String> addProductsAssociatedPartTablePartName;
    @FXML
    private TableColumn<Part, Integer> addProductsAssociatedPartTableInvCount;
    @FXML
    private TableColumn<Part, Integer> addProductsAssociatedPartTablePPU;
    /**Init method for the page.  Populates the Parts and Associated parts tables from the AllParts array in the parts class and the local AddProductAssociatedPartsList array*/
    @Override
    public void initialize (URL url, ResourceBundle rb) {
        AddProductPartsList.addAll(inventory.getAllParts());

        addProductPartsTable.setItems(AddProductPartsList);
        addProductPartsTablePartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        addProductPartsTablePartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        addProductPartsTableInvCount.setCellValueFactory(new PropertyValueFactory<>("stock"));
        addProductPartsTablePPU.setCellValueFactory(new PropertyValueFactory<>("price"));

        addProductsAssociatedPartTable.setItems(AddProductAssociatedPartsList);
        addProductsAssociatedPartTablePartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        addProductsAssociatedPartTablePartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        addProductsAssociatedPartTableInvCount.setCellValueFactory(new PropertyValueFactory<>("stock"));
        addProductsAssociatedPartTablePPU.setCellValueFactory(new PropertyValueFactory<>("price"));

    }
    /**Handler for the add products Parts table Search bar. It will search through the AllParts table either via an int or a String and return the search results or provide an error*/
    @FXML
    void addProductFieldSearch(ActionEvent event) {
        if (addProductFieldSearch.getText().isEmpty())
            addProductPartsTable.setItems(AddProductPartsList);
        else
        if ((addProductFieldSearch.getText()).matches("[0-9]+"))
            addProductPartsTable.getSelectionModel().select(lookupTempPart(Integer.parseInt(addProductFieldSearch.getText())));
        else
            addProductPartsTable.setItems(lookupTempPart(addProductFieldSearch.getText()));
    }
    /**Handler for the Add button.  When clicked, will add the selected part object in the modifyproductsparttable to the modifyProductAssociatedPartsList array and remove the selected product from the modifyProductPartsList */
    @FXML
    void addProductBtnAddPart(ActionEvent event) {
        if (addProductPartsTable.getSelectionModel().getSelectedIndex() != -1) {
            Part part = addProductPartsTable.getSelectionModel().getSelectedItem();
            AddProductAssociatedPartsList.add(part);
            AddProductPartsList.remove(part);
        }
    }
    /**controls the cancel button on the addproducts fxml gui. Closes the current window and returns to the mainscreen fxml page*/
    @FXML
    void addProductBtnCancel(ActionEvent event) throws IOException {
        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/View_Controller/MainScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    /**Handler for the remove button.  When clicked, will remove the selected part object in the modifyProductsAssociatedPartTable and add the selected product to the modifyProductPartsList */
    @FXML
    void addProductBtnRemoveAssPart(ActionEvent event) {
        if (addProductsAssociatedPartTable.getSelectionModel().getSelectedIndex() != -1) {
            Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to remove the Part: " + addProductsAssociatedPartTable.getSelectionModel().getSelectedItem().getName() + "?");
            Optional<ButtonType> result = alert1.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Part part = addProductsAssociatedPartTable.getSelectionModel().getSelectedItem();
                AddProductPartsList.add(part);
                AddProductAssociatedPartsList.remove(part);
            }
        }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Data Selection Error");
                alert.setContentText("Please select an item to Delete");
                alert.show();
            }
        }

    /**Handler for the save button on the addparts fxml gui. Uses data validation to detect correct entries into fields and, if correct, will attempt to update a product object in the allproducts array in the inventory class based on the modifyProductFieldIndex variable.*/
    @FXML
    void addProductBtnSave(ActionEvent event) throws IOException {
        AlertMessages.resetErrorFieldProduct(addProductFieldName, addProductFieldInv, addProductFieldPrice, addProductFieldMax, addProductFieldMin);
        /** @exception NumberFormatException received if attempting to save incorrect data.  Resolved by adding Try block*/
        double associatedPartsPrice = 0;
        for (Part part:AddProductAssociatedPartsList) {
            associatedPartsPrice += part.getPrice();
        }
        try {
            String name = addProductFieldName.getText().trim();
            int inv = Integer.parseInt(addProductFieldInv.getText().trim());
            double price = Double.parseDouble(addProductFieldPrice.getText().trim());
            int min = Integer.parseInt(addProductFieldMin.getText().trim());
            int max = Integer.parseInt(addProductFieldMax.getText().trim());

            if (addProductFieldName.getText().isEmpty()) {
                AlertMessages.partErrors(1);
                AlertMessages.highlightErrorField(addProductFieldName);
                return;
            }
            else if (max <= min){
                AlertMessages.highlightErrorField(addProductFieldMax);
                AlertMessages.highlightErrorField(addProductFieldMin);
                AlertMessages.partErrors(3);
                return;
            }
            else if (min <= 0 || max <= 0) {
                AlertMessages.highlightErrorField(addProductFieldMax);
                AlertMessages.highlightErrorField(addProductFieldMin);
                AlertMessages.partErrors(4);
                return;
            }
            else  if (inv < min || inv > max) {
                AlertMessages.partErrors(2);
                AlertMessages.highlightErrorField(addProductFieldInv);
                return;
            }
            else  if (price < associatedPartsPrice) {
                AlertMessages.partErrors(8);
                AlertMessages.highlightErrorField(addProductFieldPrice);
                addProductFieldPrice.setText(Double.toString(associatedPartsPrice));
                return;
            }

            Product product = new Product(name, price, inv, min, max);
            inventory.addProduct(product);

            for (Part part : AddProductAssociatedPartsList) {
                product.addAssociatedPart(part);
            }

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/View_Controller/MainScreen.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();

        }

        catch(NumberFormatException e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Data input error");
                alert.setContentText("Please enter a valid value for all text fields. (Inventory, Price, Max, Min, and Machine ID must be a valid non negative number)");
                alert.show();
            }
    }
    /**Search method for use with the modifyProductFieldSearch.  Will iterate through the modifyProductPartsList using an int to match the id and return a Part if one is found
     * @param partID int parameter for use with matching to the ID field of the parts class
     * @return will return a part object, if found, or a null object if nothing is matched
     * */
    private Part lookupTempPart(int partID) {
        for (Part part : AddProductPartsList) {
            if (part.getId() == partID)
                return part;
        }
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("No item found");
        alert.show();
        return null;

    }
    /**Search method for use with the modifyProductFieldSearch.  Will iterate through the modifyProductPartsList using a String to match the Name and return a Part if one is found
     * @param partName String parameter for use with matching to the name field of the parts class
     * @return will return an observable list array, if found
     * */
    private ObservableList<Part> lookupTempPart(String partName){
        ObservableList<Part> filteredPart = FXCollections.observableArrayList();

        for(Part part : AddProductPartsList) {
            if (part.getName().toLowerCase().contains(partName.toLowerCase()))
                filteredPart.add(part);
        }
        return filteredPart;
    }
}
