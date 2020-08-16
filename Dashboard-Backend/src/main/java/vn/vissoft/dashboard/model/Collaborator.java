package vn.vissoft.dashboard.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "sal_collaborator")
public class Collaborator {

    private Integer id;
    private String code;
    private String name;
    private  String status;
    private Timestamp createdDate;
    private String createdUser;
    private Timestamp updatedDate ;
    private String updatedUser ;

    @Column(name = "id")
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "code")
    public String getCode(){
        return code;
    }
    public void setCode(String code){
        this.code = code;
    }

    @Column(name = "name")
    public String getName(){
        return  name;
    }
    public void setName(String name){
        this.name = name;
    }

    @Column(name = "status")
    public String getStatus(){
        return status;
    }
    public void setStatus(String status){
        this.status = status;
    }

    @Column(name = "created_date")
    public Timestamp getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    @Column(name = "created_user")
    public String getCreatedUser() {
        return createdUser;
    }
    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }

    @Column(name = "updated_date")
    public Timestamp getUpdatedDate() {
        return updatedDate;
    }
    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }

    @Column(name = "updated_user")
    public String getUpdatedUser() {
        return updatedUser;
    }
    public void setUpdatedUser(String updatedUser) {
        this.updatedUser = updatedUser;
    }

    public Collaborator(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public Collaborator(){

    }
}
