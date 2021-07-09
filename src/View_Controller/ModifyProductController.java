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
/**controller class for the modify product fmxl gui page*/
public class ModifyProductController implements Initializable {

    Stage stage;
    Parent scene;

    private ObservableList<Part> modifyProductAssociatedPartsList = FXCollections.observableArrayList();
    private ObservableList<Part> modifyProductPartsList = FXCollections.observableArrayList();

    @FXML
    private TextField modifyProductFieldid;
    @FXML
    private TextField modifyProductFieldName;
    @FXML
    private TextField modifyProductFieldInv;
    @FXML
    private TextField modifyProductFieldPrice;
    @FXML
    private TextField modifyProductFieldMax;
    @FXML
    private TextField modifyProductFieldMin;
    @FXML
    private TextField modifyProductFieldSearch;
    @FXML
    private TextField modifyProductFieldIndex;
    @FXML
    private TableView<Part> modifyProductPartsTable;
    @FXML
    private TableColumn<Part, Integer> modifyProductPartsTablePartID;
    @FXML
    private TableColumn<Part, String> modifyProductPartsTablePartName;
    @FXML
    private TableColumn<Part, Integer> modifyProductPartsTableInvCount;
    @FXML
    private TableColumn<Part, Integer> modifyProductPartsTablePPU;
    @FXML
    private TableView<Part> modifyProductsAssociatedPartTable;
    @FXML
    private TableColumn<Part, Integer> modifyProductsAssociatedPartTablePartID;
    @FXML
    private TableColumn<Part, String> modifyProductsAssociatedPartTablePartName;
    @FXML
    private TableColumn<Part, Integer> modifyProductsAssociatedPartTableInvCount;
    @FXML
    private TableColumn<Part, Integer> modifyProductsAssociatedPartTablePPU;
    /**Init method for the page.  Populates the Parts and Associated parts tables from the AllParts array in the parts class and the local AddProductAssociatedPartsList array*/
    @Override
    public void initialize (URL url, ResourceBundle rb) {
        modifyProductPartsList.addAll(inventory.getAllParts());

        modifyProductPartsTablePartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        modifyProductPartsTablePartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        modifyProductPartsTableInvCount.setCellValueFactory(new PropertyValueFactory<>("stock"));
        modifyProductPartsTablePPU.setCellValueFactory(new PropertyValueFactory<>("price"));

        modifyProductPartsTable.setItems(modifyProductPartsList);

        modifyProductsAssociatedPartTablePartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        modifyProductsAssociatedPartTablePartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        modifyProductsAssociatedPartTableInvCount.setCellValueFactory(new PropertyValueFactory<>("stock"));
        modifyProductsAssociatedPartTablePPU.setCellValueFactory(new PropertyValueFactory<>("price"));

    }
    /**Handler for the modify products Parts table Search bar. It will search through the AllParts table either via an int or a String and return the search results or provide an error*/
    @FXML
    void modifyProductFieldSearch(ActionEvent event) {
        if (modifyProductFieldSearch.getText().isEmpty())
            modifyProductPartsTable.setItems(modifyProductPartsList);
        else
        if ((modifyProductFieldSearch.getText()).matches("[0-9]+"))
            modifyProductPartsTable.getSelectionModel().select(lookupTempPart(Integer.parseInt(modifyProductFieldSearch.getText())));
        else
            modifyProductPartsTable.setItems(lookupTempPart(modifyProductFieldSearch.getText()));
    }
    @FXML
    /**Handler for the Add button.  When clicked, will add the selected part object in the modifyproductsparttable to the modifyProductAssociatedPartsList array and remove the selected product from the modifyProductPartsList */
    void modifyProductBtnAddPart(ActionEvent event) {
        if (modifyProductPartsTable.getSelectionModel().getSelectedIndex() != -1) {
            Part part = modifyProductPartsTable.getSelectionModel().getSelectedItem();
            modifyProductAssociatedPartsList.add(part);
            modifyProductPartsList.remove(part);
        }
    }
    /**controls the cancel button on the modifyproducts fxml gui. Closes the current window and returns to the mainscreen fxml page*/
    @FXML
    void modifyProductBtnCancel(ActionEvent event) throws IOException {
        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/View_Controller/MainScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    /**Handler for the remove button.  When clicked, will remove the selected part object in the modifyProductsAssociatedPartTable and add the selected product to the modifyProductPartsList */
    @FXML
    void modifyProductBtnRemoveAssPart(ActionEvent event) {
        if (modifyProductsAssociatedPartTable.getSelectionModel().getSelectedIndex() != -1) {
            Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to remove the Part: " + modifyProductsAssociatedPartTable.getSelectionModel().getSelectedItem().getName() + "?");
            Optional<ButtonType> result = alert1.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Part part = modifyProductsAssociatedPartTable.getSelectionModel().getSelectedItem();
                modifyProductPartsList.add(part);
               modifyProductAssociatedPartsList.remove(part);
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
    /**Handler for the save button on the modifyParts fxml gui. Uses data validation to detect correct entries into fields and, if correct, will attempt to update a product object in the allproducts array in the inventory class based on the modifyProductFieldIndex variable.*/
    @FXML
    void modifyProductBtnSave(ActionEvent event) throws  IOException{
        AlertMessages.resetErrorFieldProduct(modifyProductFieldName, modifyProductFieldInv, modifyProductFieldPrice, modifyProductFieldMax, modifyProductFieldMin);
        double associatedPartsPrice = 0;
        for (Part part:modifyProductAssociatedPartsList) {
            associatedPartsPrice += part.getPrice();
        }
        /** @exception NumberFormatException received if attempting to save incorrect data.  Resolved by adding Try block*/
        try {
            int id = Integer.parseInt(modifyProductFieldid.getText().trim());
            String name = modifyProductFieldName.getText().trim();
            int inv = Integer.parseInt(modifyProductFieldInv.getText().trim());
            double price = Double.parseDouble(modifyProductFieldPrice.getText().trim());
            int min = Integer.parseInt(modifyProductFieldMin.getText().trim());
            int max = Integer.parseInt(modifyProductFieldMax.getText().trim());

            if (modifyProductFieldName.getText().isEmpty()) {
                AlertMessages.partErrors(1);
                AlertMessages.highlightErrorField(modifyProductFieldName);
                return;
            }
            else if (max <= min){
                AlertMessages.highlightErrorField(modifyProductFieldMax);
                AlertMessages.highlightErrorField(modifyProductFieldMin);
                AlertMessages.partErrors(3);
                return;
            }
            else if (min <= 0 || max <= 0) {
                AlertMessages.highlightErrorField(modifyProductFieldMax);
                AlertMessages.highlightErrorField(modifyProductFieldMin);
                AlertMessages.partErrors(4);
                return;
            }
            else  if (inv < min || inv > max) {
                AlertMessages.partErrors(2);
                AlertMessages.highlightErrorField(modifyProductFieldInv);
                return;
            }
            else  if (price < associatedPartsPrice) {
                AlertMessages.partErrors(8);
                AlertMessages.highlightErrorField(modifyProductFieldPrice);
                modifyProductFieldPrice.setText(Double.toString(associatedPartsPrice));
                return;
            }

            Product updatedProduct = new Product (name, price, inv, min, max);
            updatedProduct.setId(id);

            updatedProduct.getAllAssociatedParts().clear();
            updatedProduct.getAllAssociatedParts().addAll(modifyProductAssociatedPartsList);

            inventory.updateProduct(Integer.parseInt(modifyProductFieldIndex.getText()),updatedProduct);


            stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/View_Controller/MainScreen.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
        catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Data input error");
            alert.setContentText("Please enter a valid value for all text fields. (Inventory, Price, Max, Min, and Machine ID must be a valid non negative number)");
            alert.show();
        }
    }

    /**pulls data from the selected object in the MainScreenController gui page. Pulls data from the selected product object in the MainScreenController page and additionally
     * copies the AssociatedParts array from the Products class into a the local array modifyProductAssociatedPartsList
     * @param product Product object transfered from the MainScreenController page from the selection on the Product Table
     * @param productIndex the index of the Product object transferred from the MainScreenController from the Selection on the Product table.  For use with the updateProduct method in the inventory class
     * */
    public void sendProduct (Product product, int productIndex) {
        modifyProductFieldid.setText((String.valueOf(product.getId())));
        modifyProductFieldName.setText(product.getName());
        modifyProductFieldPrice.setText(Double.toString((product.getPrice())));
        modifyProductFieldInv.setText(String.valueOf(product.getStock()));
        modifyProductFieldMin.setText(String.valueOf(product.getMin()));
        modifyProductFieldMax.setText(String.valueOf(product.getMax()));
        modifyProductFieldIndex.setText(String.valueOf(productIndex));

        modifyProductAssociatedPartsList.addAll(product.getAllAssociatedParts());

        modifyProductsAssociatedPartTable.setItems(modifyProductAssociatedPartsList);

        for (Part part : modifyProductAssociatedPartsList) {
            modifyProductPartsList.remove(part);
        }
    }
    /**Search method for use with the modifyProductFieldSearch.  Will iterate through the modifyProductPartsList using an int to match the id and return a Part if one is found
     * @param partID int parameter for use with matching to the ID field of the parts class
     * @return will return a part object, if found, or a null object if nothing is matched
     * */
    private Part lookupTempPart(int partID) {
        for (Part part : modifyProductPartsList) {
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

        for(Part part : modifyProductPartsList) {
            if (part.getName().toLowerCase().contains(partName.toLowerCase()))
                filteredPart.add(part);
        }
        return filteredPart;
    }
}