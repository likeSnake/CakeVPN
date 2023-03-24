package com.lazycoder.cakevpn.bean;

import java.io.Serializable;

public class ServerBean implements Serializable {
    String wa_ser_host;
    String wa_ser_cert;
    String wa_ser_city;
    String wa_ser_country;

    Boolean selected = false;

    public ServerBean(String wa_ser_host, String wa_ser_cert, String wa_ser_city, String wa_ser_country) {
        this.wa_ser_host = wa_ser_host;
        this.wa_ser_cert = wa_ser_cert;
        this.wa_ser_city = wa_ser_city;
        this.wa_ser_country = wa_ser_country;
    }

    public String getWa_ser_host() {
        return wa_ser_host;
    }

    public void setWa_ser_host(String wa_ser_host) {
        this.wa_ser_host = wa_ser_host;
    }

    public String getWa_ser_cert() {
        return wa_ser_cert;
    }

    public void setWa_ser_cert(String wa_ser_cert) {
        this.wa_ser_cert = wa_ser_cert;
    }

    public String getWa_ser_city() {
        return wa_ser_city;
    }

    public void setWa_ser_city(String wa_ser_city) {
        this.wa_ser_city = wa_ser_city;
    }

    public String getWa_ser_country() {
        return wa_ser_country;
    }

    public void setWa_ser_country(String wa_ser_country) {
        this.wa_ser_country = wa_ser_country;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
}
