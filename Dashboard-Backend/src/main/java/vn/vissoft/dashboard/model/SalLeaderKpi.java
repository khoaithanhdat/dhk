package vn.vissoft.dashboard.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "sal_leader_kpi")
public class SalLeaderKpi {
    private Integer id;
    private Integer prdId;
    private String vdsChannelCode;
    private String shopCode;
    private String leaderCode;
    private Double schedule;
    private Double perform;
    private Double proportion;
    private Timestamp createdDate;
    private String createdUser;

    public SalLeaderKpi() {
    }

    public SalLeaderKpi(Integer prdId, String vdsChannelCode, String shopCode, String leaderCode, Double schedule, Double perform, Double proportion, Timestamp createdDate, String createdUser) {
        this.prdId = prdId;
        this.vdsChannelCode = vdsChannelCode;
        this.shopCode = shopCode;
        this.leaderCode = leaderCode;
        this.schedule = schedule;
        this.perform = perform;
        this.proportion = proportion;
        this.createdDate = createdDate;
        this.createdUser = createdUser;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "prd_id")
    public Integer getPrdId() {
        return prdId;
    }

    public void setPrdId(Integer prdId) {
        this.prdId = prdId;
    }

    @Column(name = "vds_channel_code")
    public String getVdsChannelCode() {
        return vdsChannelCode;
    }

    public void setVdsChannelCode(String vdsChannelCode) {
        this.vdsChannelCode = vdsChannelCode;
    }

    @Column(name = "shop_code")
    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    @Column(name = "leader_code")
    public String getLeaderCode() {
        return leaderCode;
    }

    public void setLeaderCode(String leaderCode) {
        this.leaderCode = leaderCode;
    }

    @Column(name = "kh")
    public Double getSchedule() {
        return schedule;
    }

    public void setSchedule(Double schedule) {
        this.schedule = schedule;
    }

    @Column(name = "th")
    public Double getPerform() {
        return perform;
    }

    public void setPerform(Double perform) {
        this.perform = perform;
    }

    @Column(name = "proportion")
    public Double getProportion() {
        return proportion;
    }

    public void setProportion(Double proportion) {
        this.proportion = proportion;
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
}
