package mawaqaa.parco.Model;

/**
 * Created by Ayadi on 4/11/2018.
 */

public class CarKindModel {
    private String idModel ;
    private String NameModel ;

    public CarKindModel(String idModel, String nameModel) {
        this.idModel = idModel;
        NameModel = nameModel;
    }

    public String getIdModel() {
        return idModel;
    }

    public void setIdModel(String idModel) {
        this.idModel = idModel;
    }

    public String getNameModel() {
        return NameModel;
    }

    public void setNameModel(String nameModel) {
        NameModel = nameModel;
    }
}
