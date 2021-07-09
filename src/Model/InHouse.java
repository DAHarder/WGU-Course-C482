package Model;

/**  Sub class for the Part class the details the in house part object.*/
public class InHouse extends Part{
    private int machineID;
/** Contructor for class.
 * @param machineID int detailed machine ID of an in house part
 * */
    public InHouse(String name, double price, int stock, int min, int max, int machineID) {
        super(name, price, stock, min, max);
        this.machineID = machineID;
    }
    /** MachineId setter for InHouse class*/
    public void setMachineID(int machineID) {
        this.machineID = machineID;
    }
    /** MachineId getter for InHouse class*/
    public int getMachineID() {
        return machineID;
    }
}
