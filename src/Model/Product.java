package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**Product class for product objects used in inventory application*/
public class Product {

    public static int generatedId = 0;

    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    public Product(String name, double price, int stock, int min, int max) {
        this.id = ++generatedId;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public void setMax(int max) {
        this.max = max;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }
/**Adds a part to the aassociated part array.  Uses a part object as a parameter to be added to the associatedparts array
 * @param part part object used as a parameter for inclusion into the associatedparts array
 * */
    public void addAssociatedPart(Part part) {
        associatedParts.add(part);
    }
    /**delets a part from the aassociated part array.  Uses a part object as a parameter to be deleted from the associatedparts array
     * @param selectedAssociatedPart part object used as a parameter for deletion from the associatedparts array
     * @return returns a boolean true or false based on successful deletion or failure
     * */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart) {
        return associatedParts.remove(selectedAssociatedPart);
    }
/**returns the associatedparts array object
 * @return returns the associatesparts array object
 * */
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }

}
