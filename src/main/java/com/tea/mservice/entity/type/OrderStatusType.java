package com.tea.mservice.entity.type;

public enum OrderStatusType {
	
	noConfirm(0, "未确认"), completTransaction(1, "交易完成"), hasConfirm(2, "已经确认"), delete(99, "删除");

	public final int value;

	private final String desc;

//	public static final Transformer transformer = new AbstractTransformer() {
//		public String getDesc(Object type) {
//			return OrderStatusType.getDesc(CoreUtils.object2int(type));
//		}
//	};

	private OrderStatusType(int value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	public static String getDesc(int type) {
		for (OrderStatusType enumType : OrderStatusType.values()) {
			if (enumType.value == type) {
				return enumType.getDesc();
			}
		}
		return "" + type;
	}

}
