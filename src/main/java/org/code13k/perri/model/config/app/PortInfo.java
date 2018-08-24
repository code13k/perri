package org.code13k.perri.model.config.app;

import org.code13k.perri.model.BasicModel;

public class PortInfo extends BasicModel {
    private int mainHttp;
    private int apiHttp;

    public int getMainHttp() {
        return mainHttp;
    }

    public void setMainHttp(int mainHttp) {
        this.mainHttp = mainHttp;
    }

    public int getApiHttp() {
        return apiHttp;
    }

    public void setApiHttp(int apiHttp) {
        this.apiHttp = apiHttp;
    }
}
