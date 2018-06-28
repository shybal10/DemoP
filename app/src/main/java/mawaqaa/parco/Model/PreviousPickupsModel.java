package mawaqaa.parco.Model;

/**
 * Created by HP on 4/12/2018.
 */

public class PreviousPickupsModel {

    String Id, city, status;

    public PreviousPickupsModel(String id, String city, String status) {
        Id = id;
        this.city = city;
        this.status = status;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
