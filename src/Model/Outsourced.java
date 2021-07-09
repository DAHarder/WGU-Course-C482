package Model;

/**  Sub class for the Part class the details the outsourced part object.*/
public class Outsourced extends Part {
    private String companyName;
    /** Contructor for class.
     * @param companyName string detailed company name of an outsourced part
     * */
    public Outsourced(String name, double price, int stock, int min, int max, String companyName) {
        super(name, price, stock, min, max);
        this.companyName = companyName;
    }
    /** company name setter for outsourced class*/
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    /** company name getter for outsourced class*/
    public String getCompanyName() {
        return companyName;
    }
}
