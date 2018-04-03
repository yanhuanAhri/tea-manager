package com.tea.mservice.entity.type;

public enum UserCheckStatus {

	unCheck(0, "未审核"), checkPass(1, "审核通过"),reCheckPass(12, "驳回");

	public final Integer value;

	private final String desc;

//	public static final Transformer transformer = new AbstractTransformer() {
//		public String getDesc(Object type) {
//			return StatusType.getDesc(CoreUtils.object2int(type));
//		}
//	};

	private UserCheckStatus(Integer value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	public static String getDesc(int type) {
		for (UserCheckStatus enumType : UserCheckStatus.values()) {
			if (enumType.value == type) {
				return enumType.getDesc();
			}
		}
		return "" + type;
	}


}
