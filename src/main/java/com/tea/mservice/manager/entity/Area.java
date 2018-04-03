package com.tea.mservice.manager.entity;

import javax.persistence.*;

import com.tea.mservice.entity.IdEntity;

@Entity
@Table(name = "t_sys_area")
public class Area extends IdEntity {
    private Long parentId;
    private String name;
    private String code;
    private Integer level;
    private Double latitude;
    private Double longitude;
    private Integer sortby;

    @Basic
    @Column(name = "parent_id", nullable = true)
    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    @Basic
    @Column(name = "name", nullable = true, length = 50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "code", nullable = true, length = 12)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Basic
    @Column(name = "level", nullable = true)
    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    @Basic
    @Column(name = "latitude", nullable = true, precision = 0)
    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @Basic
    @Column(name = "longitude", nullable = true, precision = 0)
    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Basic
    @Column(name = "sortby", nullable = true)
    public Integer getSortby() {
        return sortby;
    }

    public void setSortby(Integer sortby) {
        this.sortby = sortby;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Area area = (Area) o;

        if (id != area.id) return false;
        if (parentId != null ? !parentId.equals(area.parentId) : area.parentId != null) return false;
        if (name != null ? !name.equals(area.name) : area.name != null) return false;
        if (code != null ? !code.equals(area.code) : area.code != null) return false;
        if (level != null ? !level.equals(area.level) : area.level != null) return false;
        if (latitude != null ? !latitude.equals(area.latitude) : area.latitude != null) return false;
        if (longitude != null ? !longitude.equals(area.longitude) : area.longitude != null) return false;
        if (sortby != null ? !sortby.equals(area.sortby) : area.sortby != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (parentId != null ? parentId.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (level != null ? level.hashCode() : 0);
        result = 31 * result + (latitude != null ? latitude.hashCode() : 0);
        result = 31 * result + (longitude != null ? longitude.hashCode() : 0);
        result = 31 * result + (sortby != null ? sortby.hashCode() : 0);
        return result;
    }
}
