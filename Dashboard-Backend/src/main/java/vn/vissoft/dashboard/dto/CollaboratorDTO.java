package vn.vissoft.dashboard.dto;

import java.sql.Timestamp;

public class CollaboratorDTO {

    private Integer id;
    private String code;
    private String name;
    private  String status;
    private Timestamp createdDate;
    private String createdUser;
    private Timestamp updatedDate ;
    private String updatedUser ;

    public Integer getId(){
        return  id;
    }
    public void  setId(Integer id){
        this.id = id;
    }

    public String getCode(){
        return code;
    }
    public void setCode(String code){
        this.code = code;
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getStatus(){
        return status;
    }
    public void setStatus(String status){
        this.status = status;
    }

    public Timestamp getCreatedDate(){
        return createdDate;
    }
    public void setCreatedDate(Timestamp createdDate){
        this.createdDate = createdDate;
    }

    public String getCreatedUser(){
        return createdUser;
    }
    public void setCreatedUser(String createdUser){
        this.createdUser = createdUser;
    }

    public Timestamp getUpdatedDate(){
        return updatedDate;
    }
    public void setUpdatedDate(Timestamp updatedDate){
        this.updatedDate = updatedDate;
    }

    public String getUpdatedUser(){
        return updatedUser;
    }
    public void setUpdatedUser(String updatedUser){
        this.updatedUser = updatedUser;
    }
}
