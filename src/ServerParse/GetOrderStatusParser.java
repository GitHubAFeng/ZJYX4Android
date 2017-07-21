package ServerParse;

import java.io.InputStream;
import java.util.Hashtable;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.content.Context;
import android.os.Handler;

import com.ihangjing.Model.BaseBean;
import com.ihangjing.Model.GetOrderStatusBean;
import com.ihangjing.common.NetManager;

public class GetOrderStatusParser extends DataToObject {
	private Context ctx;

	public GetOrderStatusParser(Context paramContext) {
		super(paramContext);
		this.ctx = paramContext;
	}

	@Override
	public BaseBean getMsgDBandNetwork(Handler paramHandler,
			String paramString, Hashtable<String, String> paramHashtable)
			throws Exception {
		return null;
	}

	@Override
	public BaseBean getMsgFromCacheDB(Handler paramHandler, String paramString,
			Hashtable<String, String> paramHashtable) throws Exception {
		return null;
	}

	@Override
	public GetOrderStatusBean getMsgFromNetwork(Handler paramHandler,
			String paramString, Hashtable<String, String> paramHashtable)
			throws Exception {

		NetManager localNetManager = NetManager.getInstance(this.ctx);
		SAXParserFactory localSAXParserFactory = SAXParserFactory.newInstance();
		InputStream localInputStream = localNetManager.dogetAsInputStream("http://www.baidu.com");
		SAXParser localSAXParser = localSAXParserFactory.newSAXParser();
		GetOrderStatusHandler localGetOrderStatusHandler = implXmlHander();
		localSAXParser.parse(localInputStream, localGetOrderStatusHandler);
		if (localInputStream != null)
			localInputStream.close();
		return localGetOrderStatusHandler.getResult();
	}

	@Override
	public GetOrderStatusHandler implXmlHander() {
		Context localContext = this.ctx;
		return new GetOrderStatusHandler(localContext);
	}

	class GetOrderStatusHandler extends XmlHandlerBase {
		private Context ctx;
		private boolean isOrder;
		private boolean isStatus;
		private GetOrderStatusBean osb;

		public GetOrderStatusHandler(Context arg2) {
			super(arg2);
			GetOrderStatusBean localGetOrderStatusBean = new GetOrderStatusBean();
			this.osb = localGetOrderStatusBean;
			this.isStatus = false;
			this.isOrder = false;
			//this.ctx = localContext;
		}

		@Override
		public void characters(char[] paramArrayOfChar, int paramInt1,
				int paramInt2) throws SAXException {
			if (this.isStatus) {
				GetOrderStatusBean localGetOrderStatusBean1 = this.osb;
				String str1 = new String(paramArrayOfChar, paramInt1, paramInt2)
						.trim();
				localGetOrderStatusBean1.setResultStatus(str1);
				return;
			}
			if (!this.isOrder)
				return;
			GetOrderStatusBean localGetOrderStatusBean2 = this.osb;
			String str2 = new String(paramArrayOfChar, paramInt1, paramInt2)
					.trim();
			localGetOrderStatusBean2.setOrderNote(str2);
		}

		@Override
		public void endElement(String paramString1, String paramString2,
				String paramString3) throws SAXException {
			if (paramString2.equals("status")) {
				this.isStatus = false;
				return;
			}
			if (!paramString2.equals("order"))
				return;
			this.isOrder = false;
		}

		@Override
		public GetOrderStatusBean getResult() {
			return this.osb;
		}

		@Override
		public void startElement(String paramString1, String paramString2,
				String paramString3, Attributes paramAttributes)
				throws SAXException {
			if (paramString2.equals("status")) {
				this.isStatus = true;
				return;
			}
			if (paramString2.equals("doingOrder")) {
				String str1 = paramAttributes.getValue("num");
				int i = 0;
				if (str1.matches("[0-9]*"))
					i = Integer.valueOf(str1).intValue();
				this.osb.setDoingOrderNum(i);
				return;
			}
			if (!paramString2.equals("order"))
				return;
			this.isOrder = true;
			GetOrderStatusBean localGetOrderStatusBean1 = this.osb;
			String str2 = paramAttributes.getValue("id");
			localGetOrderStatusBean1.setOrderId(str2);
			String str1 = paramAttributes.getValue("status");
			if (str1.matches("[0-9]*")) {
				GetOrderStatusBean localGetOrderStatusBean2 = this.osb;
				int j = Integer.valueOf(str1).intValue();
				localGetOrderStatusBean2.setOrderStatus(j);
				return;
			}
			this.osb.setOrderStatus(1);
		}

		@Override
		public boolean storeDataBean(BaseBean paramBaseBean) {
			return false;
		}
	}
}
