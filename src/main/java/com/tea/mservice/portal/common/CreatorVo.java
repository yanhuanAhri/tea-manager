package com.tea.mservice.portal.common;

import java.sql.Timestamp;

/**
 * Created by liuyihao on 2017/10/20.
 */
public class CreatorVo {
    protected Long creatorId;
    protected String creatorName;

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }


    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }
}
