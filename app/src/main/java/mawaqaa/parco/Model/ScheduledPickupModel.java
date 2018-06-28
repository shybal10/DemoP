package mawaqaa.parco.Model;

/**
 * Created by HP on 4/15/2018.
 */

public class ScheduledPickupModel {
    String scheduledId, scheduledCity, scheduledStatus;

    public ScheduledPickupModel(String scheduledId, String scheduledCity, String scheduledStatus) {
        this.scheduledId = scheduledId;
        this.scheduledCity = scheduledCity;
        this.scheduledStatus = scheduledStatus;
    }

    public String getScheduledId() {
        return scheduledId;
    }

    public void setScheduledId(String scheduledId) {
        this.scheduledId = scheduledId;
    }

    public String getScheduledCity() {
        return scheduledCity;
    }

    public void setScheduledCity(String scheduledCity) {
        this.scheduledCity = scheduledCity;
    }

    public String getScheduledStatus() {
        return scheduledStatus;
    }

    public void setScheduledStatus(String scheduledStatus) {
        this.scheduledStatus = scheduledStatus;
    }
}
