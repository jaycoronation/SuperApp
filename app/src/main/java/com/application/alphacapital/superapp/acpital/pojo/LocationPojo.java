package com.application.alphacapital.superapp.acpital.pojo;

/**
 * Created by Kuldeep Sakhiya on 25-Aug-2017.
 */

public class LocationPojo
{
    private String cityName = "";
    private String address = "";
    private String phone = "";
    private String fax = "";

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }
}
