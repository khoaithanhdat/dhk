package vn.vissoft.dashboard.dto;

import java.util.ArrayList;

public class ConfigSalaryLeaderDTO {

    private ArrayList<SalConfigStaffTargetDTO> SalConfigStaffTargetModel;
    private ArrayList<SalConfigHsTinhLuongDTO> SalConfigHsTinhLuongModel;
    private ArrayList<SalConfigSaleFeeDTO> SalConfigSaleFeeModel;
    private ArrayList<SalConfigStaffGiffDTO> SalConfigStaffGiffModel;

    public ArrayList<SalConfigStaffTargetDTO> getSalConfigStaffTargetModel() {
        return SalConfigStaffTargetModel;
    }

    public void setSalConfigStaffTargetModel(ArrayList<SalConfigStaffTargetDTO> salConfigStaffTargetModel) {
        SalConfigStaffTargetModel = salConfigStaffTargetModel;
    }

    public ArrayList<SalConfigHsTinhLuongDTO> getSalConfigHsTinhLuongModel() {
        return SalConfigHsTinhLuongModel;
    }

    public void setSalConfigHsTinhLuongModel(ArrayList<SalConfigHsTinhLuongDTO> salConfigHsTinhLuongModel) {
        SalConfigHsTinhLuongModel = salConfigHsTinhLuongModel;
    }

    public ArrayList<SalConfigSaleFeeDTO> getSalConfigSaleFeeModel() {
        return SalConfigSaleFeeModel;
    }

    public void setSalConfigSaleFeeModel(ArrayList<SalConfigSaleFeeDTO> salConfigSaleFeeModel) {
        SalConfigSaleFeeModel = salConfigSaleFeeModel;
    }

    public ArrayList<SalConfigStaffGiffDTO> getSalConfigStaffGiffModel() {
        return SalConfigStaffGiffModel;
    }

    public void setSalConfigStaffGiffModel(ArrayList<SalConfigStaffGiffDTO> salConfigStaffGiffModel) {
        SalConfigStaffGiffModel = salConfigStaffGiffModel;
    }
}
