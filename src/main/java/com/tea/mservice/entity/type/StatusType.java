package com.tea.mservice.entity.type;

public enum StatusType {
	//'0 禁用, 1 启用' 
	disable(0, "禁用 "), enable(1, "启用");

	public final int value;

	private final String desc;

//	public static final Transformer transformer = new AbstractTransformer() {
//		public String getDesc(Object type) {
//			return StatusType.getDesc(CoreUtils.object2int(type));
//		}
//	};

	private StatusType(int value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	public static String getDesc(int type) {
		for (StatusType enumType : StatusType.values()) {
			if (enumType.value == type) {
				return enumType.getDesc();
			}
		}
		return "" + type;
	}

}
