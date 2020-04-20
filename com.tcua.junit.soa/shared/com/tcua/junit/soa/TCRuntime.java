package com.tcua.junit.soa;

import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.ui.common.RACUIUtil;

public class TCRuntime {
	
	/**
	 * Currently active group
	 * 
	 * @return
	 */
	
	public static String currentGroup() {
		TCSession session = RACUIUtil.getTCSession();
		return session.getCurrentGroup().toDisplayString();
	}

	/**
	 * Currently active person/user
	 * 
	 * @return
	 */

	public static String user() {
		TCSession session = RACUIUtil.getTCSession();
		return session.getUser().toDisplayString();
	}
}
