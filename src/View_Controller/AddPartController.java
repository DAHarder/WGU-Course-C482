package View_Controller;
import Model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
/**Controller class for the add parts fxml gui page*/
public class AddPartController {

    Stage stage;
    Parent scene;

    @FXML
    private RadioButton addPartsRadioInHouse;
    @FXML
    private RadioButton addPartsRadioOutsourced;
    @FXML
    private TextField addPartsFieldID;
    @FXML
    private TextField addPartsFieldName;
    @FXML
    private TextField addPartsFieldInv;
    @FXML
    private TextField addPartsFieldPrice;
    @FXML
    private TextField addPartsFieldMax;
    @FXML
    private TextField addPartsFieldMachineID;
    @FXML
    private Label addPartsLabelMachineID;
    @FXML
    private TextField addPartsFieldMin;

    /**changes the machineID label based on the radio selection*/
    @FXML
    void addPartsRadioInHouseSelect(ActionEvent event) {
        addPartsLabelMachineID.setText("Machine ID");
    }
    /**changes the machineID label based on the radio selection*/
    @FXML
    void addPartsRadioOutsourcedSelect(ActionEvent event) {
        addPartsLabelMachineID.setText("Company Name");
    }
    /**controls the cancel button on the addparts fxml gui. Closes the current window and returns to the mainscreen fxml page*/
    @FXML
    void addPartsBtnCancel(ActionEvent event) throws IOException {
        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/View_Controller/MainScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    /**controls the save button on the addparts fxml gui. Uses data validation to detect correct entries into fields and, if correct, will insert a new Part object into the allparts array in the inventory class.
     * If validation fails, it calls methods from the AlertMessages class*/
    @FXML
    void addPartsBtnSave(ActionEvent event) throws IOException{
        AlertMessages.resetErrorFieldPart(addPartsRadioInHouse, addPartsRadioOutsourced, addPartsFieldName, addPartsFieldInv, addPartsFieldPrice, addPartsFieldMax, addPartsFieldMin, addPartsFieldMachineID);
        /** @exception NumberFormatException received if attempting to save incorrect data.  Resolved by adding Try block*/
        try {
            String name = addPartsFieldName.getText().trim();
            int inv = Integer.parseInt(addPartsFieldInv.getText().trim());
            double price = Double.parseDouble(addPartsFieldPrice.getText().trim());
            int min = Integer.parseInt(addPartsFieldMin.getText().trim());
            int max = Integer.parseInt(addPartsFieldMax.getText().trim());

            if (!addPartsRadioInHouse.isSelected() && !addPartsRadioOutsourced.isSelected()) {
                AlertMessages.partErrors(6);
                AlertMessages.highlightErrorField(addPartsRadioInHouse);
                AlertMessages.highlightErrorField(addPartsRadioOutsourced);
                return;
            }
            else  if (addPartsFieldName.getText().isEmpty()) {
                AlertMessages.partErrors(1);
                AlertMessages.highlightErrorField(addPartsFieldName);
                return;
            }
            else  if (addPartsFieldMachineID.getText().isEmpty()) {
                AlertMessages.partErrors(5);
                AlertMessages.highlightErrorField(addPartsFieldMachineID);
                return;
            }
            else  if ((addPartsRadioInHouse.isSelected() && !addPartsFieldMachineID.getText().matches("[0-9]+")) || Integer.parseInt(addPartsFieldMachineID.getText())  < 1)  {
                AlertMessages.partErrors(7);
                AlertMessages.highlightErrorField(addPartsFieldMachineID);
                return;
            }
            else if (max <= min){
                AlertMessages.highlightErrorField(addPartsFieldMax);
                AlertMessages.highlightErrorField(addPartsFieldMin);
                AlertMessages.partErrors(3);
                return;
            }
            else if (min <= 0 || max <= 0) {
                AlertMessages.highlightErrorField(addPartsFieldMax);
                AlertMessages.highlightErrorField(addPartsFieldMin);
                AlertMessages.partErrors(4);
                return;
            }
            else  if (inv < min || inv > max) {
                AlertMessages.partErrors(2);
                AlertMessages.highlightErrorField(addPartsFieldInv);
                return;
            }

            if (addPartsRadioInHouse.isSelected()) {
                int machineID = Integer.parseInt(addPartsFieldMachineID.getText());
                inventory.addPart(new InHouse(name, price, inv, min, max, machineID));
            }
            else if (addPartsRadioOutsourced.isSelected()) {
                String companyName = addPartsFieldMachineID.getText();
                inventory.addPart(new Outsourced(name, price, inv, min, max, companyName));
            }

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

}
