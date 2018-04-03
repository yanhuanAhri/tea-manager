package com.tea.mservice.core.sysconfig;

public class SysKey {

	/* Portal */
	public static final String PORTAL_HOST = "portal.host";
	public static final String PORTAL_HOME_PAGE_LINK = "ypjj.home.links";
	public static final String PORTAL_HOME_LINK_SWITCH = "portal.home.link.switch";

	/* UC */
	public static final String UC_HOST = "uc.host";

	/* AD */
	public static final String AD2_HOST = "ad2.host";

	/* page setting */
	public static final String PAGE_SIZE = "page.size";

	/* Image */
	public static final String IMAGE_HOST = "image.host";
	public static final String IMAGE_MAX_SIZE = "image.max.size";
	public static final String IMAGE_PAGE_SIZE = "image.page.size";
	public static final String IMAGE_USER_MAX = "image.user.max";

	/* FastDFS */
	public static final String FASTDFS_TRACKER_HTTP_HOST = "fastdfs.tracker.http.host";
	public static final String FASTDFS_TRACKER_HTTP_PORT = "fastdfs.tracker.http.port";
	public static final String FASTDFS_ANTI_STEAL_TOKEN = "fastdfs.anti.steal.token";

	/* api */
	public static final String API_HOST = "api.host";
	public static final String API_LOGISTIC_SEARCH_1ZT = "api.logistic.search.1zt";
	public static final String API_SMS_SEND = "api.sms.send";

	/* Product */
	public static final String PRODUCT_AUTO_OFF_DAY = "product.auto.off.day";

	/* Category */
	public static final String CATEGORY_LAMP_ID = "category.lamp.id";
	public static final String CATEGORY_FLOOR_CODE = "category.floor.code";

	/* RabbitMQ */
	public static final String MQ_HOST = "mq.host";
	public static final String MQ_PORT = "mq.port";
	public static final String MQ_USER_NAME = "mq.user.name";
	public static final String MQ_USER_PASSWORD = "mq.user.password";

	public static final String MQ_EXCHANGE_LOGISTICS_ORDER = "mq.exchange.logistics.order";
	public static final String MQ_EXCHANGE_SMS_NOTIFY_NAME = "mq.exchange.sms.notify.name";
	public static final String MQ_EXCHANGE_COUPON = "mq.exchange.coupon";
	public static final String MQ_ORDER_ROUTING_KEY = "mq.order.routing.key";
	public static final String MQ_COUPON_ROUTING_KEY = "mq.coupon.routing.key";
	public static final String MQ_NOTIFY_PAY_ROUTING_KEY = "mq.notify.pay.routing.key";
	public static final String MQ_NOTIFY_SUCC_ROUTING_KEY = "mq.notify.succ.routing.key";
	public static final String MQ_SMS_ROUTING_KEY = "mq.sms.routing.key";

	public static final String MQ_QUEUES_LOGISTICS_ORDER = "mq.queues.logistics.order";
	public static final String MQ_QUEUE_ORDER_NOTIFY_NAME = "mq.queue.order.notify.name";
	public static final String MQ_QUEUE_REGISTER_NOTIFY_NAME = "mq.queue.register.notify.name";
	public static final String MQ_QUEUES_COUPON = "mq.queues.coupon";
	public static final String MQ_QUEUE_SMS = "mq.queue.sms";

	public static final String MQ_ORDER_QUEUE_SERVICE = "mq.order.queue.service";
	public static final String MQ_REGUSTER_QUEUE_SERVICE = "mq.register.queue.service";

	/* node */
	public static final String NODE_SERIAL = "node.serial";
	/* register */
	public static final String REGISTER_SMS_EXPIRE = "register.sms.expire";

	/* Sdebank */
	public static final String BANK_SDEBANK_INTERFACE_URL = "bank.sdebank.interface.url";
	public static final String BANK_SDEBANK_CALLBACK_URL = "bank.sdebank.callback.url";

	public static final String BANK_SDEBANK_CASH_PRIMARY_ACCOUNT = "bank.sdebank.cash.primary.account";
	public static final String BANK_SDEBANK_CASH_AC_NAME = "bank.sdebank.cash.ac.name";
	public static final String BANK_SDEBANK_CASH_INTERFACE_IP = "bank.sdebank.cash.interface.ip";
	public static final String BANK_SDEBANK_CASH_INTERFACE_PORT = "bank.sdebank.cash.interface.port";
	public static final String BANK_SDEBANK_CASH_HTTP_URL = "bank.sdebank.cash.http.url";

	public static final String BANK_SDEBANK_CERT_ALIAS = "bank.sdebank.cert.alias";
	public static final String BANK_SDEBANK_CERT_PUBLIC_PASSWORD = "bank.sdebank.cert.public.password";
	public static final String BANK_SDEBANK_CERT_KEYSTORE_PATH = "bank.sdebank.cert.keyStore.path";
	public static final String BANK_SDEBANK_CERT_CERTIFICATE_PATH = "bank.sdebank.cert.certificatePath.path";
	public static final String BANK_SDEBANK_CERT_RANDOM_KEY = "bank.sdebank.cert.random.key";
	public static final String BANK_SDEBANK_CERT_PUBLIC_PATH = "bank.sdebank.cert.public.path"; 
	public static final String BANK_SDEBANK_CERT_PRIVATE_PATH = "bank.sdebank.cert.private.path";  

	/* Alipay */
	public static final String PAY_ALIPAY_NOTIFY_URL = "pay.alipay.notify.url";
	public static final String PAY_ALIPAY_RETURN_URL = "pay.alipay.return.url";
	public static final String PAY_ALIPAY_QUERY_URL = "pay.alipay.query.url";
	public static final String PAY_ALIPAY_QUERY_APPID = "pay.alipay.query.appid";
	public static final String PAY_ALIPAY_LOG_PATH = "pay.alipay.log.path";

	/* weixin */
	public static final String PAY_WEIXIN_APP_ID = "pay.weixin.app.id";
	public static final String PAY_WEIXIN_APP_SECRET = "pay.weixin.app.secret";
	public static final String PAY_WEIXIN_MCH_ID = "pay.weixin.mch.id";
	public static final String PAY_WEIXIN_API_KEY = "pay.weixin.api.key";
	public static final String PAY_WEIXIN_NOTIFY_URL = "pay.weixin.notify.url";
	public static final String PAY_WEIXIN_CREATE_IP = "pay.weixin.create.ip";
	public static final String PAY_WEIXIN_UFDODER_URL = "pay.weixin.ufdoder.url";
	public static final String PAY_WEIXIN_ORDERQUERY_URL = "pay.weixin.orderquery.url";

	public static final String PAY_WEIXIN_COUPON_IP = "pay.weixin.coupon.ip";
	public static final String PAY_WEIXIN_COUPON_URL = "pay.weixin.coupon.url";
	public static final String PAY_WEIXIN_COUPON_CERT_URL = "pay.weixin.coupon.cert.url";

	/* kwms */
	public static final String IMG_ROOT_DIR = "img.root.dir";
	public static final String API_ID = "app.id";
	public static final String KWMS_THEME = "kwms.theme";
	public static final String PRODUCT_DETAIL_URL = "product.detail.url";
	public static final String SOLUTION_PAYAMOUNT_RATE= "sulution.payamount.rate";
	public static final String SALES_ROLE_ID="sales.role.id";
	public static final String HOME_BRAND_PRODUCT_REF_COUNT="home.brand.product.ref.count";
	public static final String JL_USER_ID="jl.user.id";
	public static final String PROJECT_ENV="project.env";
	
	/* manager */
	public static final String MANAGER_URL = "manager.url";
	public static final String MANAGER_POSITION_URL = "manager.position.url";
	

	/* email */
	public static final String EMAIL_SERVICE = "email.service";
	public static final String EMAIL_FROM_MAIL = "email.from.mail";
	public static final String EMAIL_PASSWORD = "email.password";
	public static final String EMAIL_DEBUG = "email.debug";

	/* api */
	public static final String YZT_APP_KEY = "yzt.app.key";
	public static final String YZT_SECRET_KEY = "yzt.secret.key";
	public static final String YZT_ADD_ORDER_URL = "yzt.add.order.url";
	public static final String YZT_TRACK_ORDER_URL = "yzt.track.order.url";
	public static final String YZT_DEL_ORDER_URL = "yzt.del.order.url";
	public static final String YZT_COST_ORDER_URL = "yzt.cost.order.url";
	public static final String YZT_UPDATE_ORDER_URL = "yzt.update.order.url";

	/* WX */
	public static final String WEIXIN_URL = "weixin.url";
	public static final String WEIXIN_WEB_EMPOWER = "weixin.web.empower";
	public static final String WEIXIN_ACCESS_TOKEN = "weixin.access.token";
	public static final String WEIXIN_NOTIFY_URL = "weixin.notify.url";
	public static final String WEIXIN_LOGIN = "weixin.login";
	public static final String WEIXIN_AMOUNT_LIMIT = "weixin.amount.limit";

	/* wx-qw */
	public static final String WEIXIN_QW_APP_ID = "weixin.qw.app.id";
	public static final String WEIXIN_QW_APP_SECRET = "weixin.qw.app.secret";
	public static final String WEIXIN_QW_APP_KEY = "weixin.qw.app.key";
	public static final String WEIXIN_QW_MCH_ID = "weixin.qw.mch.id";
	public static final String WEIXIN_QW_NOTIFY_URL = "weixin.qw.notify.url";
	public static final String WEIXIN_COUPON_CERT_QW_URL = "weixin.coupon.cert.qw.url";

	/* order */
	public static final String ORDER_PAY_STATUS_CHECK_TIME = "order.pay.status.check.time";
	public static final String ORDER_LOGISTICS_STATUS_CHECK_TIME = "order.logistics.status.check.time";
	public static final String ORDER_PAY_DISCOUNT_RATE = "order.pay.discount.rate";

	/* programme */
	public static final String UC_PROGRAMME_PDF_FONT_PATH = "uc.programme.pdf.font.path";
	public static final String UC_PROGRAMME_PDF_FONT_NAME = "uc.programme.pdf.font.name";
	public static final String UC_PROGRAMME_PDF_LOGO_PATH = "uc.programme.pdf.logo.path";
	public static final String UC_PROGRAMME_PDF_CODE_PATH = "uc.programme.pdf.code.path";

	public static final String KWMS_PROGRAMME_PDF_FONT_APTH = "kwms.programme.pdf.font.path";
	public static final String KWMS_PROGRAMME_PDF_FONT_NAME = "kwms.programme.pdf.font.name";

	/* wugaun pdf */
	public static final String WUGUAN_PROGRAMME_PDF_FONT_APTH = "wuguan.programme.pdf.font.path";
	public static final String WUGUAN_PROGRAMME_PDF_FONT_NAME = "wuguan.programme.pdf.font.name";
	public static final String WUGUAN_PROGRAMME_PDF_LOGO_PATH = "wuguan.programme.pdf.logo.path";
	public static final String WUGUAN_PROGRAMME_PDF_CODE_PATH = "wuguan.programme.pdf.code.path";

	/* 友情链接 */
	public static final String YPJJ_HOME_LINKS = "ypjj.home.links";

	
	/*审核通过方案生成商品的字段配置*/
	public static final String SOLUTION_PRODUCT_CATEGORY_FIRST_ID = "solution.product.categoryfirstid";
	public static final String SOLUTION_PRODUCT_CATEGORY_SECOND_ID= "solution.product.categorysecondid";
	public static final String SOLUTION_PRODUCT_CATEGORY_THREE_ID= "solution.product.categorythreeid";
	public static final String SOLUTION_PRODUCT_BRAND_ID= "solution.product.brandid";
	public static final String SOLUTION_PRODUCT_SHIP_PROVINCE_ID= "solution.product.shipprovinceid";
	public static final String SOLUTION_PRODUCT_SHIP_CITY_ID= "solution.product.shipcityid";
	public static final String SOLUTION_PRODUCT_SHIP_DISTRICT_ID= "solution.product.shipdistrictid";
	public static final String SOLUTION_PRODUCT_SHIP_ADDRESS= "solution.product.shipaddress";
	public static final String SOLUTION_PRODUCT_VENDOR_ID= "solution.product.vendor.id";
	

	/* 短信开关 */
	public static final String SWITCH_SMS_WUGUAN_CREATEUSER = "switch.sms.wuguan.createUser";
	public static final String SWITCH_SMS_WUGUAN_SENDSOLUTION = "switch.sms.wuguan.sendSolution";

	public static final String SWITCH_SMS_KWMS_CREATEUSER = "switch.sms.kwms.createUser";

	public static final String SWITCH_SMS_PC_ORDER_CS = "switch.sms.pc.order.cs";
	public static final String SWITCH_SMS_WX_ORDER_CS = "switch.sms.wx.order.cs";

	public static final String SWITCH_SMS_PC_REGISTER_CS = "switch.sms.pc.register.cs";
	public static final String SWITCH_SMS_WX_REGISTER_CS = "switch.sms.wx.register.cs";
	public static final String SWITCH_SMS_MG_REGISTER_CS = "switch.sms.mg.register.cs";
	
	
	public static final String SWITCH_SMS_ASSET_SEND_NOTIFY = "switch.sms.asset.send.notify";
	public static final String SWITCH_SMS_ASSET_AUTH_NOTIFY = "switch.sms.asset.auth.notify";
	/* 搜索菜单 */
	public static final String YPJJ_SYS_SEARCH_CATEGORY = "ypjj.sys.search.category";
	
	public static final String PRODUCT_DETAIL_HOST_URL = "product.detail.host.url";
	
	
}
