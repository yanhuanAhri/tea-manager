package com.tea.mservice.entity.type;

public enum ProductStatusType {
	
	unShelve(0, "下架 "), putaway(1, "上架"),delete(99, "删除");

	public final int value;

	private final String desc;

//	public static final Transformer transformer = new AbstractTransformer() {
//		public String getDesc(Object type) {
//			return ProductStatusType.getDesc(CoreUtils.object2int(type));
//		}
//	};

	private ProductStatusType(int value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	public static String getDesc(int type) {
		for (ProductStatusType enumType : ProductStatusType.values()) {
			if (enumType.value == type) {
				return enumType.getDesc();
			}
		}
		return "" + type;
	}

}
