package com.tea.mservice.portal.entity;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.tea.mservice.entity.IdEntity;

/**
 * 商品
 */
@Entity
@Table(name = "commodity")
public class Commodity extends IdEntity {

	private String commodityNum; //商品编号
	private Long productNum;//库存
	private String search;
	private Long soldOutNum;//已售数量
	private String tradeName;//商品名
	private BigDecimal marketPrice;//市场价格
	private BigDecimal promotionPrice;//活动价格
	private Timestamp createTime;//创建时间
	private Long createUserId;//创建人
	private Timestamp updateTime;//更新时间
	private Long updateUserId;//修改人
	private String teaName;//茶名
	private String productType;//产品类型
	private String pickYear;//采摘年份
	private String pickSeason;//采摘季节
	private String goodsGrade;//商品等级;
	private Integer netContent;//净含量
	private Integer purpose;//用途
	private String specification;//规格
	private String originPlace;//产地
	private String foodProductionLicence;//食品生产许可证
	private String storeMethod;//储藏方法
	private Integer expirationData;//保质期 
	private String craft;//工艺
	private String packingSpecification;//包装规格
	private Integer status;//商品状态
	private Integer repertoryStatus;//商品货数状态
	public String getCommodityNum() {
		return commodityNum;
	}
	public void setCommodityNum(String commodityNum) {
		this.commodityNum = commodityNum;
	}
	public Long getProductNum() {
		return productNum;
	}
	public void setProductNum(Long productNum) {
		this.productNum = productNum;
	}
	public Long getSoldOutNum() {
		return soldOutNum;
	}
	public void setSoldOutNum(Long soldOutNum) {
		this.soldOutNum = soldOutNum;
	}
	public String getTradeName() {
		return tradeName;
	}
	public void setTradeName(String tradeName) {
		this.tradeName = tradeName;
	}
	public BigDecimal getMarketPrice() {
		return marketPrice;
	}
	public void setMarketPrice(BigDecimal marketPrice) {
		this.marketPrice = marketPrice;
	}
	public BigDecimal getPromotionPrice() {
		return promotionPrice;
	}
	public void setPromotionPrice(BigDecimal promotionPrice) {
		this.promotionPrice = promotionPrice;
	}
	
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public Long getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}
	public Timestamp getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
	public Long getUpdateUserId() {
		return updateUserId;
	}
	public void setUpdateUserId(Long updateUserId) {
		this.updateUserId = updateUserId;
	}
	public String getTeaName() {
		return teaName;
	}
	public void setTeaName(String teaName) {
		this.teaName = teaName;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getPickYear() {
		return pickYear;
	}
	public void setPickYear(String pickYear) {
		this.pickYear = pickYear;
	}
	public String getPickSeason() {
		return pickSeason;
	}
	public void setPickSeason(String pickSeason) {
		this.pickSeason = pickSeason;
	}
	public String getGoodsGrade() {
		return goodsGrade;
	}
	public void setGoodsGrade(String goodsGrade) {
		this.goodsGrade = goodsGrade;
	}
	public Integer getNetContent() {
		return netContent;
	}
	public void setNetContent(Integer netContent) {
		this.netContent = netContent;
	}
	public Integer getPurpose() {
		return purpose;
	}
	public void setPurpose(Integer purpose) {
		this.purpose = purpose;
	}
	public String getSpecification() {
		return specification;
	}
	public void setSpecification(String specification) {
		this.specification = specification;
	}
	public String getOriginPlace() {
		return originPlace;
	}
	public void setOriginPlace(String originPlace) {
		this.originPlace = originPlace;
	}
	public String getFoodProductionLicence() {
		return foodProductionLicence;
	}
	public void setFoodProductionLicence(String foodProductionLicence) {
		this.foodProductionLicence = foodProductionLicence;
	}
	public String getStoreMethod() {
		return storeMethod;
	}
	public void setStoreMethod(String storeMethod) {
		this.storeMethod = storeMethod;
	}
	public Integer getExpirationData() {
		return expirationData;
	}
	public void setExpirationData(Integer expirationData) {
		this.expirationData = expirationData;
	}
	public String getCraft() {
		return craft;
	}
	public void setCraft(String craft) {
		this.craft = craft;
	}
	public String getPackingSpecification() {
		return packingSpecification;
	}
	public void setPackingSpecification(String packingSpecification) {
		this.packingSpecification = packingSpecification;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getRepertoryStatus() {
		return repertoryStatus;
	}
	public void setRepertoryStatus(Integer repertoryStatus) {
		this.repertoryStatus = repertoryStatus;
	}
	public String getSearch() {
		return search;
	}
	public void setSearch(String search) {
		this.search = search;
	}
	
	
	
}
