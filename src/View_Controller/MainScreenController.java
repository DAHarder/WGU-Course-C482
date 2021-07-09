package View_Controller;

import Model.Part;
import Model.Product;
import Model.inventory;
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
/**Main screen gui page for the start of the application*/
public class MainScreenController implements Initializable {

    Stage stage;
    Parent scene;


    @FXML
    private TableView<Part> mainScreenPartsTable;
    @FXML
    private TableColumn<Part, Integer> partsTablePartID;
    @FXML
    private TableColumn<Part, String> partsTablePartName;
    @FXML
    private TableColumn<Part, Integer> partsTableInventoryCount;
    @FXML
    private TableColumn<Part, Integer> partsTablePPU;
    @FXML
    private TextField partsSearchFieldID;
    @FXML
    private TableView<Product> mainScreenProductsTable;
    @FXML
    private TableColumn<Product, Integer> productsTablePartID;
    @FXML
    private TableColumn<Product, String> productsTablePartName;
    @FXML
    private TableColumn<Product, Integer> productsTableInventoryCount;
    @FXML
    private TableColumn<Product, Integer> productsTablePPU;
    @FXML
    private TextField productsSearchFieldID;

    /**handler for the exit button. When clicked, the button will display a dialog box asking for confirmation, and if given, will close the program*/
    @FXML
    void mainScreenBtnExitHandler(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Exit the program?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK)
            System.exit(0);
    }
/**handler for the add parts button.  When clicked, it will open the AddPart.fxml gui page*/
    @FXML
    void partsBtnAddHandler(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/View_Controller/AddPart.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    /**handler for the parts table delete button. When clicked, it will ask for confirmation via a dialog box, and if given, will attempt to delete the selected Part object from the AllParts array in the Inventory class
     * Future update should allow for bulk-deletion through multiple selection
     * */
    @FXML
    void partsBtnDeleteHandler(ActionEvent event) {
        if (mainScreenPartsTable.getSelectionModel().getSelectedIndex() != -1) {
            Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to Delete the part: " + mainScreenPartsTable.getSelectionModel().getSelectedItem().getName() + "?");
            Optional<ButtonType> result = alert1.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Part part = mainScreenPartsTable.getSelectionModel().getSelectedItem();

                for (Product products : inventory.getAllProducts()) {
                    for (int i = 0; i < products.getAllAssociatedParts().size(); i++) {
                        if (part == products.getAllAssociatedParts().get(i)) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText("Deletion Error");
                            alert.setContentText("There are one or more products that are associated to this Part. The Part cannot be deleted until there are no associated Products. \n\nAssociated Product: "+products.getAllAssociatedParts().get(i).getName());
                            alert.show();
                            return;
                        }
                    }
                }
                    if (!inventory.deletePart(part)) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Deletion Error");
                        alert.setContentText("Unable to delete item");
                        alert.show();
                    }
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Data Selection Error");
                alert.setContentText("Please select an item to Delete");
                alert.show();
            }
        }

    /**Handler for the parts table modify button. When clicked, it will open the ModifyPart.fxml page and transfer information from the selected object to the new page*/
    @FXML
    void partsBtnModifyHandler(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/View_Controller/ModifyPart.fxml"));
            loader.load();

            ModifyPartController MPController = loader.getController();
            MPController.sendPart(mainScreenPartsTable.getSelectionModel().getSelectedItem(), mainScreenPartsTable.getSelectionModel().getSelectedIndex());

            stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
        catch (NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Data selection error");
            alert.setContentText("Please select an item");
            alert.show();
        }
    }
    /**Handler for the Parts table Search bar. It will search through the AllParts table either via an int or a String and return the search results or provide an error*/
    @FXML
    void partsSearchHandler(ActionEvent event) {
        if (partsSearchFieldID.getText().isEmpty())
            mainScreenPartsTable.setItems(inventory.getAllParts());
        else
            if ((partsSearchFieldID.getText()).matches("[0-9]+"))
                mainScreenPartsTable.getSelectionModel().select(inventory.lookupPart(Integer.parseInt(partsSearchFieldID.getText())));
            else
            mainScreenPartsTable.setItems(inventory.lookupPart(partsSearchFieldID.getText()));
    }
    /**handler for the add products button.  When clicked, it will open the AddProduct.fxml gui page*/
    @FXML
    void productsBtnAddHandler(ActionEvent event) throws IOException {
        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/View_Controller/AddProduct.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    /**handler for the products table delete button. When clicked, it will ask for confirmation via a dialog box, and if given, will attempt to delete the selected Products object from the AllProducts array in the Inventory class
     * Future update should allow for bulk-deletion through multiple selection
     * */
    @FXML
    void productsBtnDeleteHandler(ActionEvent event) {
        if (mainScreenProductsTable.getSelectionModel().getSelectedIndex() != -1) {
            Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to Delete the Product: " +mainScreenProductsTable.getSelectionModel().getSelectedItem().getName()+"?");
            Optional<ButtonType> result = alert1.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Product product = mainScreenProductsTable.getSelectionModel().getSelectedItem();
                    if (!product.getAllAssociatedParts().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Deletion Error");
                    alert.setContentText("Product has an associated Part.  Please remove the associated Part before deletion.");
                    alert.show();
                    return;
                }
                if (!inventory.deleteProduct(product)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Deletion Error");
                    alert.setContentText("Unable to delete item");
                    alert.show();
                }
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
    /**Handler for the products table modify button. When clicked, it will open the modifyProduct.fxml page and transfer information from the selected object to the new page*/
    @FXML
    void productsBtnModifyHandler(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/View_Controller/ModifyProduct.fxml"));
            loader.load();

            ModifyProductController MPController = loader.getController();
            MPController.sendProduct(mainScreenProductsTable.getSelectionModel().getSelectedItem(), mainScreenProductsTable.getSelectionModel().getSelectedIndex());

            stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
        catch (NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Data selection error");
            alert.setContentText("Please select an item");
            alert.show();
        }
    }
    /**Handler for the products table Search bar. It will search through the allProducts table either via an int or a String and return the search results or provide an error*/
    @FXML
    void productsSearchHandler(ActionEvent event) {
        if (productsSearchFieldID.getText().isEmpty())
            mainScreenProductsTable.setItems(inventory.getAllProducts());
        else
        if ((productsSearchFieldID.getText()).matches("[0-9]+"))
            mainScreenProductsTable.getSelectionModel().select(inventory.lookupProduct(Integer.parseInt(productsSearchFieldID.getText())));
        else
            mainScreenProductsTable.setItems(inventory.lookupProduct(productsSearchFieldID.getText()));
    }

/**init method for the gui page.  Pulls information from the allParts and AllProducts arrays in the inventory class and populates the parts and products
 * Future update should include a method to sort the part and product items by ID or alphabetic order
 *
 * */
    @Override
    public void initialize (URL url, ResourceBundle rb) {
        mainScreenPartsTable.setItems(inventory.getAllParts());
        partsTablePartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        partsTablePartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partsTableInventoryCount.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partsTablePPU.setCellValueFactory(new PropertyValueFactory<>("price"));

        mainScreenProductsTable.setItems(inventory.getAllProducts());
        productsTablePartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        productsTablePartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        productsTableInventoryCount.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productsTablePPU.setCellValueFactory(new PropertyValueFactory<>("price"));

    }
}
