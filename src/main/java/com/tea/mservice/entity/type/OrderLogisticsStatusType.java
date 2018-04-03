package com.tea.mservice.entity.type;

public enum OrderLogisticsStatusType {

	noDeliver(0, "未发货"), hasTake(1, "已经收货"), hasDeliver(2, "已经发货");

	public final int value;

	private final String desc;

//	public static final Transformer transformer = new AbstractTransformer() {
//		public String getDesc(Object type) {
//			return OrderLogisticsStatusType.getDesc(CoreUtils.object2int(type));
//		}
//	};

	private OrderLogisticsStatusType(int value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	public static String getDesc(int type) {
		for (OrderLogisticsStatusType enumType : OrderLogisticsStatusType.values()) {
			if (enumType.value == type) {
				return enumType.getDesc();
			}
		}
		return "" + type;
	}

}
