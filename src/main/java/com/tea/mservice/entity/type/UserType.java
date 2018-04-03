package com.tea.mservice.entity.type;

public enum UserType {

	comsumer(0,"个人用户"),business(1, "企业用户");

	public final Integer value;

	private final String desc;

//	public static final Transformer transformer = new AbstractTransformer() {
//		public String getDesc(Object type) {
//			return StatusType.getDesc(CoreUtils.object2int(type));
//		}
//	};

	private UserType(Integer value, String desc) {
		this.value = value;
		this.desc = desc;
	}

//	public String getDesc() {
//		return I18nUtils.getMassage(desc);
//	}

//	public static String getDesc(int type) {
//		for (StatusType enumType : StatusType.values()) {
//			if (enumType.value == type) {
//				return enumType.getDesc();
//			}
//		}
//		return "" + type;
//	}


}
