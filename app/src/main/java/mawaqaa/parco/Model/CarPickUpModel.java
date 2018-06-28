package mawaqaa.parco.Model;

/**
 * Created by Ayadi on 4/15/2018.
 */

public class CarPickUpModel  {
    private int id ;
    private String carName ,carType;

    public CarPickUpModel(int id, String carType, String carName) {
        this.id = id;
        this.carType = carType;
        this.carName = carName;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }
}
