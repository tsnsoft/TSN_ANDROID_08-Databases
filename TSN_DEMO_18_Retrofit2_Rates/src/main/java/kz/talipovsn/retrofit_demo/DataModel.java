package kz.talipovsn.retrofit_demo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

// КЛАСС-МОДЕЛЬ ДАННЫХ С САЙТА https://api.nbrb.by/exrates/rates?periodicity=0
// Пример элемента JSON:
// {"Cur_ID":431, "Cur_Abbreviation":"USD", "Cur_Scale":1, "Cur_Name":"Доллар США", "Cur_OfficialRate":3.25}

public class DataModel {

    @SerializedName("Cur_ID")
    @Expose
    private String id;

    @SerializedName("Cur_Abbreviation")
    @Expose
    private String kod;

    @SerializedName("Cur_Scale")
    @Expose
    private String sootnowenie;

    @SerializedName("Cur_Name")
    @Expose
    private String nameRus;

    @SerializedName("Cur_OfficialRate")
    @Expose
    private String kurs;

    // Поля по умолчанию для полной совместимости с DataAdapter
    private String nameKaz = "";
    private String edinicaIzmerenia = "BYN";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameKaz() {
        return nameKaz;
    }

    public void setNameKaz(String nameKaz) {
        this.nameKaz = nameKaz;
    }

    public String getEdinicaIzmerenia() {
        return edinicaIzmerenia;
    }

    public void setEdinicaIzmerenia(String edinicaIzmerenia) {
        this.edinicaIzmerenia = edinicaIzmerenia;
    }

    public String getSootnowenie() {
        return sootnowenie != null ? sootnowenie : "";
    }

    public void setSootnowenie(String sootnowenie) {
        this.sootnowenie = sootnowenie;
    }

    public String getNameRus() {
        return nameRus != null ? nameRus : "";
    }

    public void setNameRus(String nameRus) {
        this.nameRus = nameRus;
    }

    public String getKurs() {
        return kurs != null ? kurs : "";
    }

    public void setKurs(String kurs) {
        this.kurs = kurs;
    }

    public String getKod() {
        return kod;
    }

    public void setKod(String kod) {
        this.kod = kod;
    }

}