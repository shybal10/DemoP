package mawaqaa.parco.Model;

/**
 * Created by Ayadi on 4/17/2018.
 */

public class FuelModel {
    private String fuelId , fuelName ;

    public FuelModel(String fuelId, String fuelName) {
        this.fuelId = fuelId;
        this.fuelName = fuelName;
    }

    public String getFuelId() {
        return fuelId;
    }

    public void setFuelId(String fuelId) {
        this.fuelId = fuelId;
    }

    public String getFuelName() {
        return fuelName;
    }

    public void setFuelName(String fuelName) {
        this.fuelName = fuelName;
    }
}
