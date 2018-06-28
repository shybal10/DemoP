package mawaqaa.parco.Model;

/**
 * Created by HP on 4/15/2018.
 */

public class CurrentPickupModel {
    String currentId, currentCity, currentStatus;

    public CurrentPickupModel(String currentId, String currentCity, String currentStatus) {
        this.currentId = currentId;
        this.currentCity = currentCity;
        this.currentStatus = currentStatus;
    }

    public String getCurrentId() {
        return currentId;
    }

    public void setCurrentId(String currentId) {
        this.currentId = currentId;
    }

    public String getCurrentCity() {
        return currentCity;
    }

    public void setCurrentCity(String currentCity) {
        this.currentCity = currentCity;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }
}
