package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
/** Top level class that holds the Part and Products array objects to portray an inventory */
public class inventory {

    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();
/** adds a part object to the allparts array.
 * @param newPart part object to be added to the array
 * */
    public static void addPart(Part newPart){
        allParts.add(newPart);
    }
    /** adds a product object to the allproducts array
     * @param newProduct product object to be added to the array
     * */
    public static void addProduct(Product newProduct){
        allProducts.add(newProduct);
    }
    /** iterates through allparts.  Looks for a single match based on the int ID attribute and returns the entire object match
     * @param partID int parameter to be used for finding a match with the ID attribute of the part class object
     * @return method returns either a part object or a null object
     * */
    public static Part lookupPart(int partID){
         for(Part part : inventory.getAllParts()){
             if (part.getId() == partID)
                 return part;
         }
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("No item found");
        alert.show();
         return null;
    }
    /** iterates through allproducts.  Looks for a single match based on the int ID attribute and returns the entire object match
     * @param productID int parameter to be used for finding a match with the ID attribute of the products class object
     * @return method returns either a product object or a null object
     * */
    public static Product lookupProduct(int productID){
        for(Product product : inventory.getAllProducts()){
            if (product.getId() == productID)
                return product;
        }
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("No item found");
        alert.show();
        return null;
    }
    /** iterates through allparts.  Looks for up to multiple matchs based on the string name attribute
     * @param partName string parameter to be used for finding a match with the name attribute of the part class
     * @return method returns an observable list of one or more part objects
     * */
    public static ObservableList<Part> lookupPart(String partName){
        ObservableList<Part> filteredPart = FXCollections.observableArrayList();

        for(Part part : inventory.getAllParts()) {
            if (part.getName().toLowerCase().contains(partName.toLowerCase()))
                filteredPart.add(part);
        }
        return filteredPart;
    }
    /** iterates through allproducts.  Looks for up to multiple matchs based on the string name attribute
     * @param productName string parameter to be used for finding a match with the name attribute of the product class
     * @return method returns an observable list of one or more product objects
     * */
    public static ObservableList<Product> lookupProduct(String productName){
        ObservableList<Product> filteredProduct = FXCollections.observableArrayList();

        for(Product product : inventory.getAllProducts()) {
            if (product.getName().toLowerCase().contains(productName.toLowerCase()))
                filteredProduct.add(product);
        }
        return filteredProduct;
    }
    /** replaces a part object.  Replaces a part object with another part object sent as a parameter.  Used for updating part objects in the array
     * @param index int parameter used to specify which object within the array to replace
     * @param newPart part object parameter which is used to replace a part object within the allparts array
     * */
    public static void updatePart(int index, Part newPart){
        inventory.getAllParts().set(index, newPart);
    }
    /** replaces a product object.  Replaces a product object with another product object sent as a parameter.  Used for updating product objects in the array
     * @param index int parameter used to specify which object within the array to replace
     * @param newProduct product object parameter which is used to replace a product object within the allproducts array
     * */
    public static void updateProduct(int index, Product newProduct){
        inventory.getAllProducts().set(index, newProduct);
    }
    /** deletes a part object.  Used to delete a part object from the allparts array
     * @param selectedPart part object parameter used to determine which part object for deletion
     * @return returns boolean true or false
     * */
    public static boolean deletePart(Part selectedPart){
        return inventory.getAllParts().remove(selectedPart);
    }
    /** deletes a product object.  Used to delete a product object from the allproducts array
     * @param selectedProduct product object parameter used to determine which product object for deletion
     * @return returns boolean true or false
     * */
    public static boolean deleteProduct(Product selectedProduct){
        return inventory.getAllProducts().remove(selectedProduct);
    }
    /** Returns all the part objects in the allparts array
     * @return returns the allparts array object
     * */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }
    /** Returns all the product objects in the allproducts array
     * @return returns the allproducts array object
     * */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }
}
