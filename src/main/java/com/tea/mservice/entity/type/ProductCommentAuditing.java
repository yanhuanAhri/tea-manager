package com.tea.mservice.entity.type;

public enum ProductCommentAuditing {

	unCheck(0, "未通过"), checkPass(1, "通过");

	public final Integer value;

	private final String desc;

//	public static final Transformer transformer = new AbstractTransformer() {
//		public String getDesc(Object type) {
//			return StatusType.getDesc(CoreUtils.object2int(type));
//		}
//	};

	private ProductCommentAuditing(Integer value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	public static String getDesc(int type) {
		for (ProductCommentAuditing enumType : ProductCommentAuditing.values()) {
			if (enumType.value == type) {
				return enumType.getDesc();
			}
		}
		return "" + type;
	}


}
