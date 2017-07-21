package ServerParse;

import java.util.Hashtable;

import android.content.Context;
import android.os.Handler;

import com.ihangjing.Model.BaseBean;

public abstract class DataToObject {
	public DataToObject(Context paramContext) {
	}

	public abstract BaseBean getMsgDBandNetwork(Handler paramHandler,
			String paramString, Hashtable<String, String> paramHashtable)
			throws Exception;

	public abstract BaseBean getMsgFromCacheDB(Handler paramHandler,
			String paramString, Hashtable<String, String> paramHashtable)
			throws Exception;

	public abstract BaseBean getMsgFromNetwork(Handler paramHandler,
			String paramString, Hashtable<String, String> paramHashtable)
			throws Exception;

	public abstract XmlHandlerBase implXmlHander();
}
