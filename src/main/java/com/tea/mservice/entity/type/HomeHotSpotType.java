package com.tea.mservice.entity.type;

public enum HomeHotSpotType {

	left("left","rtl-slide"),right("right", "ltr-slide"),top("top", "btt-slide"),bottom("bottom", "ttb-silde");

	public final String value;

	private final String desc;

//	public static final Transformer transformer = new AbstractTransformer() {
//		public String getDesc(Object type) {
//			return StatusType.getDesc(CoreUtils.object2int(type));
//		}
//	};

	private HomeHotSpotType(String value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	public String getDesc() {
//		return I18nUtils.getMassage(desc);
		return desc;
	}

	public static String getDesc(int type) {
		for (HomeHotSpotType enumType : HomeHotSpotType.values()) {
			if (enumType.value.equals(String.valueOf(type))) {
				return enumType.getDesc();
			}
		}
		return "" + type;
	}


}
