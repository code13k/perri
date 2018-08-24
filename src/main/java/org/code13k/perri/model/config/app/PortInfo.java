package org.code13k.perri.model.config.app;

import org.code13k.perri.model.BasicModel;

public class PortInfo extends BasicModel {
    private int main;
    private int apiHttp;
    private int apiWs;

    public int getMain() {
        return main;
    }

    public void setMain(int main) {
        this.main = main;
    }

    public int getApiHttp() {
        return apiHttp;
    }

    public void setApiHttp(int apiHttp) {
        this.apiHttp = apiHttp;
    }

    public int getApiWs() {
        return apiWs;
    }

    public void setApiWs(int apiWs) {
        this.apiWs = apiWs;
    }
}
