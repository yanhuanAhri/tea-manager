package com.tea.mservice.entity.type;

public enum ProductAuditingStatusType {
	
	draft(0, "草稿 "), pass(1, "通过"),again(2, "审核不�?�过"), commitAudit(4, "提交审核");

	public final int value;

	private final String desc;

//	public static final Transformer transformer = new AbstractTransformer() {
//		public String getDesc(Object type) {
//			return ProductAuditingStatusType.getDesc(CoreUtils.object2int(type));
//		}
//	};

	private ProductAuditingStatusType(int value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	public static String getDesc(int type) {
		for (ProductAuditingStatusType enumType : ProductAuditingStatusType.values()) {
			if (enumType.value == type) {
				return enumType.getDesc();
			}
		}
		return "" + type;
	}

}
