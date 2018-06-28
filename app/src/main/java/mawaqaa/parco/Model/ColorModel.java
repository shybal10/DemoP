package mawaqaa.parco.Model;

/**
 * Created by Ayadi on 4/11/2018.
 */

public class ColorModel {
    private String idColor , nameColor , hashColor ;

    public ColorModel(String idColor, String nameColor, String hashColor) {
        this.idColor = idColor;
        this.nameColor = nameColor;
        this.hashColor = hashColor;
    }

    public String getIdColor() {
        return idColor;
    }

    public void setIdColor(String idColor) {
        this.idColor = idColor;
    }

    public String getNameColor() {
        return nameColor;
    }

    public void setNameColor(String nameColor) {
        this.nameColor = nameColor;
    }

    public String getHashColor() {
        return hashColor;
    }

    public void setHashColor(String hashColor) {
        this.hashColor = hashColor;
    }
}
