package com.tea.mservice.entity.type;

public enum OrderPayStatusType {

	noPay(0, "未付款"), hasPay(1, "已经付款");

	public final int value;

	private final String desc;

//	public static final Transformer transformer = new AbstractTransformer() {
//		public String getDesc(Object type) {
//			return OrderPayStatusType.getDesc(CoreUtils.object2int(type));
//		}
//	};

	private OrderPayStatusType(int value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	public static String getDesc(int type) {
		for (OrderPayStatusType enumType : OrderPayStatusType.values()) {
			if (enumType.value == type) {
				return enumType.getDesc();
			}
		}
		return "" + type;
	}

}
