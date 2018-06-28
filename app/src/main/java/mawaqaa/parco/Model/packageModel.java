package mawaqaa.parco.Model;

/**
 * Created by Ayadi on 4/19/2018.
 */

public class packageModel {
    private String ID , PackageName , PackageDescription , ParkingCount , CarWashCount , FuelingAmountRate , FuelingCount , CreditAmount , PackagePrice;

    public packageModel(String ID, String packageName, String packageDescription, String parkingCount, String carWashCount, String fuelingAmountRate, String fuelingCount, String creditAmount, String packagePrice) {
        this.ID = ID;
        PackageName = packageName;
        PackageDescription = packageDescription;
        ParkingCount = parkingCount;
        CarWashCount = carWashCount;
        FuelingAmountRate = fuelingAmountRate;
        FuelingCount = fuelingCount;
        CreditAmount = creditAmount;
        PackagePrice = packagePrice;
    }


    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPackageName() {
        return PackageName;
    }

    public void setPackageName(String packageName) {
        PackageName = packageName;
    }

    public String getPackageDescription() {
        return PackageDescription;
    }

    public void setPackageDescription(String packageDescription) {
        PackageDescription = packageDescription;
    }

    public String getParkingCount() {
        return ParkingCount;
    }

    public void setParkingCount(String parkingCount) {
        ParkingCount = parkingCount;
    }

    public String getCarWashCount() {
        return CarWashCount;
    }

    public void setCarWashCount(String carWashCount) {
        CarWashCount = carWashCount;
    }

    public String getFuelingAmountRate() {
        return FuelingAmountRate;
    }

    public void setFuelingAmountRate(String fuelingAmountRate) {
        FuelingAmountRate = fuelingAmountRate;
    }

    public String getFuelingCount() {
        return FuelingCount;
    }

    public void setFuelingCount(String fuelingCount) {
        FuelingCount = fuelingCount;
    }

    public String getCreditAmount() {
        return CreditAmount;
    }

    public void setCreditAmount(String creditAmount) {
        CreditAmount = creditAmount;
    }

    public String getPackagePrice() {
        return PackagePrice;
    }

    public void setPackagePrice(String packagePrice) {
        PackagePrice = packagePrice;
    }
}
