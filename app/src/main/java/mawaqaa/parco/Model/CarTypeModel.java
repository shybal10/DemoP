package mawaqaa.parco.Model;

/**
 * Created by Ayadi on 4/24/2018.
 */

public class CarTypeModel {
    private String typeID , typeName , typeImage ;

    public CarTypeModel(String typeID, String typeName, String typeImage) {
        this.typeID = typeID;
        this.typeName = typeName;
        this.typeImage = typeImage;
    }

    public String getTypeID() {
        return typeID;
    }

    public void setTypeID(String typeID) {
        this.typeID = typeID;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeImage() {
        return typeImage;
    }

    public void setTypeImage(String typeImage) {
        this.typeImage = typeImage;
    }
}
