package View_Controller;

import Model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
/**controller class for the modify parts fmxl gui page*/
public class ModifyPartController {

    Stage stage;
    Parent scene;

    @FXML
    private RadioButton modifyPartRadioInHouse;
    @FXML
    private RadioButton modifyPartRadioOutsourced;
    @FXML
    private TextField modifyPartFieldID;
    @FXML
    private TextField modifyPartFieldName;
    @FXML
    private TextField modifyPartFieldInv;
    @FXML
    private TextField modifyPartFieldPrice;
    @FXML
    private TextField modifyPartFieldMax;
    @FXML
    private TextField modifyPartFieldMachineID;
    @FXML
    private Label modifyPartsLabelMachineId;
    @FXML
    private TextField modifyPartFieldMin;
    @FXML
    private TextField modifyPartFieldIndex;
    /**changes the machineID label based on the radio selection*/
    @FXML
    void modifyPartRadioInHouseSelect(ActionEvent event) {
        modifyPartsLabelMachineId.setText("Machine ID");
    }
    /**changes the machineID label based on the radio selection*/
    @FXML
    void modifyPartRadioOutsourcedSelect(ActionEvent event) {
        modifyPartsLabelMachineId.setText("Company Name");
    }
    /**controls the cancel button on the modifyParts fxml gui. Closes the current window and returns to the mainscreen fxml page*/
    @FXML
    void modifyPartBtnCancel(ActionEvent event) throws Exception{
        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/View_Controller/MainScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
/**controls the save button on the modifyParts fxml gui. Uses data validation to detect correct entries into fields and, if correct, will attempt to update a Part object in the allparts array in the inventory class based on the modifyPartFieldIndex variable.*/
    @FXML
    void modifyPartBtnSave(ActionEvent event) throws Exception{
        AlertMessages.resetErrorFieldPart(modifyPartRadioInHouse, modifyPartRadioOutsourced, modifyPartFieldName, modifyPartFieldInv, modifyPartFieldPrice, modifyPartFieldMax, modifyPartFieldMin, modifyPartFieldMachineID);
        /** @exception NumberFormatException received if attempting to save incorrect data.  Resolved by adding Try block*/
        try {
            int partID = Integer.parseInt(modifyPartFieldID.getText());
            String name = modifyPartFieldName.getText().trim();
            int inv = Integer.parseInt(modifyPartFieldInv.getText().trim());
            double price = Double.parseDouble(modifyPartFieldPrice.getText().trim());
            int min = Integer.parseInt(modifyPartFieldMin.getText().trim());
            int max = Integer.parseInt(modifyPartFieldMax.getText().trim());

            if (!modifyPartRadioInHouse.isSelected() && !modifyPartRadioOutsourced.isSelected()) {
                AlertMessages.partErrors(6);
                AlertMessages.highlightErrorField(modifyPartRadioInHouse);
                AlertMessages.highlightErrorField(modifyPartRadioOutsourced);
                return;
            }
            else  if (modifyPartFieldName.getText().isEmpty()) {
                AlertMessages.partErrors(1);
                AlertMessages.highlightErrorField(modifyPartFieldName);
                return;
            }
            else  if (modifyPartFieldMachineID.getText().isEmpty()) {
                AlertMessages.partErrors(5);
                AlertMessages.highlightErrorField(modifyPartFieldMachineID);
                return;
            }
            else  if ((modifyPartRadioInHouse.isSelected() && !modifyPartFieldMachineID.getText().matches("[0-9]+")) || Integer.parseInt(modifyPartFieldMachineID.getText())  < 1)  {
                AlertMessages.partErrors(7);
                AlertMessages.highlightErrorField(modifyPartFieldMachineID);
                return;
            }
            else if (max <= min){
                AlertMessages.highlightErrorField(modifyPartFieldMax);
                AlertMessages.highlightErrorField(modifyPartFieldMin);
                AlertMessages.partErrors(3);
                return;
            }
            else if (min <= 0 || max <= 0) {
                AlertMessages.highlightErrorField(modifyPartFieldMax);
                AlertMessages.highlightErrorField(modifyPartFieldMin);
                AlertMessages.partErrors(4);
                return;
            }
            else  if (inv < min || inv > max) {
                AlertMessages.partErrors(2);
                AlertMessages.highlightErrorField(modifyPartFieldInv);
                return;
            }

            if (modifyPartRadioInHouse.isSelected()) {
                int machineID = Integer.parseInt(modifyPartFieldMachineID.getText());
                InHouse updatedPart = new InHouse (name, price, inv, min, max, machineID);
                updatedPart.setId(partID);
                inventory.updatePart(Integer.parseInt(modifyPartFieldIndex.getText()),updatedPart);
            }
            else if (modifyPartRadioOutsourced.isSelected()) {
                String companyName = modifyPartFieldMachineID.getText();
                Outsourced updatedPart = new Outsourced (name, price, inv, min, max, companyName);
                updatedPart.setId(partID);
                inventory.updatePart(Integer.parseInt(modifyPartFieldIndex.getText()),updatedPart);
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
/**pulls data from the selected object in the MainScreenController gui page.
 * @param part Part object transfered from the MainScreenController page from the selection on the Parts Table
 * @param partIndex the index of the part object transferred from the MainScreenController from the Selection on the Parts table
 * */
    public void sendPart (Part part, int partIndex) {
        if(part instanceof InHouse) {
            modifyPartRadioInHouse.setSelected(true);
            modifyPartFieldMachineID.setText(String.valueOf(((InHouse) part).getMachineID()));
        }
        else if(part instanceof Outsourced) {
            modifyPartRadioOutsourced.setSelected(true);
            modifyPartFieldMachineID.setText(((Outsourced) part).getCompanyName());
            modifyPartsLabelMachineId.setText("Company Name");
        }

        modifyPartFieldID.setText((String.valueOf(part.getId())));
        modifyPartFieldName.setText(part.getName());
        modifyPartFieldPrice.setText(Double.toString((part.getPrice())));
        modifyPartFieldInv.setText(String.valueOf(part.getStock()));
        modifyPartFieldMin.setText(String.valueOf(part.getMin()));
        modifyPartFieldMax.setText(String.valueOf(part.getMax()));
        modifyPartFieldIndex.setText(String.valueOf(partIndex));

    }

}
