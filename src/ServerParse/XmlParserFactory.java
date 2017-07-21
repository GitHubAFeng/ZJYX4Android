package ServerParse;

import com.ihangjing.Model.BaseBean;

public abstract interface XmlParserFactory {
	public abstract BaseBean getResult();

	public abstract boolean storeDataBean(BaseBean paramBaseBean);
}