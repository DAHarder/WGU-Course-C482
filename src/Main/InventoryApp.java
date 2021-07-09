package Main;

import Model.InHouse;
import Model.Product;
import Model.inventory;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
/** Main class for Inventory Application.  Inventory Application provides an editable inventory table and list for Part and Product objects
 * @author Dan Adams
 * @version 1.0
 * */
public class InventoryApp extends Application {
    /** Start method for main class.  Loads main GUI page 'MainScreen.fxml; */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/View_Controller/MainScreen.fxml"));
        primaryStage.setTitle("Dan Adams - C482 - Inventory Management System");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }

    /** main method for main class.  Loads sample data for inventory application */
    public static void main(String[] args) {
        InHouse part1 = new InHouse("J Part",5.00,10,5,30,001);
        inventory.addPart(part1);
        InHouse part2 = new InHouse("L Part",6.00,15,5,30,002);
        inventory.addPart(part2);
        InHouse part3 = new InHouse("H Part",7.00,20,5,30,003);
        inventory.addPart(part3);

        Product product1 = new Product("Large Shelf",100.00,10,1,10);
        product1.addAssociatedPart(part1);
        inventory.addProduct(product1);

        launch(args);
    }
}