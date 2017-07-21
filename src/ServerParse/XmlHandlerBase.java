package ServerParse;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.content.Context;

import com.ihangjing.Model.BaseBean;

public abstract class XmlHandlerBase extends DefaultHandler implements
		XmlParserFactory {
	public XmlHandlerBase(Context paramContext) {
	}

	@Override
	public abstract void characters(char[] paramArrayOfChar, int paramInt1,
			int paramInt2) throws SAXException;

	@Override
	public abstract void endElement(String paramString1, String paramString2,
			String paramString3) throws SAXException;

	@Override
	public abstract BaseBean getResult();

	@Override
	public abstract void startElement(String paramString1, String paramString2,
			String paramString3, Attributes paramAttributes)
			throws SAXException;

	@Override
	public abstract boolean storeDataBean(BaseBean paramBaseBean);
}