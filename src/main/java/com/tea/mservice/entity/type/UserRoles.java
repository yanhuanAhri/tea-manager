package com.tea.mservice.entity.type;

public enum UserRoles {

	buyer("buyer","买家"),seller("seller", "卖家"),testor("tester","测试者");

	public final String value;

	private final String desc;

//	public static final Transformer transformer = new AbstractTransformer() {
//		public String getDesc(Object type) {
//			return StatusType.getDesc(CoreUtils.object2int(type));
//		}
//	};

	private UserRoles(String value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	public static String getDesc(int type) {
		for (UserRoles enumType : UserRoles.values()) {
			if (enumType.value.equals( String.valueOf(type))) {
				return enumType.getDesc();
			}
		}
		return "" + type;
	}


}
