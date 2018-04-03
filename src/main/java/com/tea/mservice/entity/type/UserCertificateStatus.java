package com.tea.mservice.entity.type;

public enum UserCertificateStatus {

	unCer(0, "未认证"), indeedCer(1, "已认证"),hasCommitData(4,"已提交"),reject(12,"驳回");

	public final Integer value;

	private final String desc;

//	public static final Transformer transformer = new AbstractTransformer() {
//		public String getDesc(Object type) {
//			return StatusType.getDesc(CoreUtils.object2int(type));
//		}
//	};

	private UserCertificateStatus(Integer value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	public static String getDesc(int type) {
		for (UserCertificateStatus enumType : UserCertificateStatus.values()) {
			if (enumType.value == type) {
				return enumType.getDesc();
			}
		}
		return "" + type;
	}


}
