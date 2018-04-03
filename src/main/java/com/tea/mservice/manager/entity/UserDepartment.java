package com.tea.mservice.manager.entity;

import javax.persistence.*;

import com.tea.mservice.entity.IdEntity;

@Entity
@Table(name = "t_sys_user_department")
public class UserDepartment extends IdEntity {
    private Long depId;
    private Long userId;

    @Basic
    @Column(name = "dep_id", nullable = true)
    public Long getDepId() {
        return depId;
    }

    public void setDepId(Long depId) {
        this.depId = depId;
    }

    @Basic
    @Column(name = "user_id", nullable = true)
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserDepartment that = (UserDepartment) o;

        if (id != that.id) return false;
        if (depId != null ? !depId.equals(that.depId) : that.depId != null) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (depId != null ? depId.hashCode() : 0);
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        return result;
    }
}
