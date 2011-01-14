/*
 *    This file is part of Lucterios.
 *
 *    Lucterios is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 2 of the License, or
 *    (at your option) any later version.
 *
 *    Lucterios is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with Lucterios; if not, write to the Free Software
 *    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 *	Contributeurs: Fanny ALLEAUME, Pierre-Olivier VERSCHOORE, Laurent GAY
 */

package org.lucterios.client.presentation;

import java.util.Timer;
import java.util.TimerTask;

import org.lucterios.client.presentation.Observer.MapContext;
import org.lucterios.client.transport.HttpTransport;
import org.lucterios.utils.LucteriosException;

public class WatchDog extends TimerTask {

	public interface WatchDogRefresher{
		public void refreshClient() throws LucteriosException;
	}

	final static public int WATCH_DOG_TIME = 5 * 60 * 1000;
	private HttpTransport mHttpTransport;

	private WatchDog(HttpTransport aHttpTransport) {
		mHttpTransport = aHttpTransport;
	}

	public void run() {
		if (mTimer != null) {
			synchronized (mTimer) {
				try {
					if (mWatchDogRefresher!=null)
						mWatchDogRefresher.refreshClient();
					else
						mHttpTransport.transfertXMLFromServer(new MapContext());
				} catch (LucteriosException e) {
				}
			}
		}
	}

	static private Timer mTimer = null;

	static private WatchDogRefresher mWatchDogRefresher=null;

	public static void setWatchDogRefresher(WatchDogRefresher watchDogRefresher) {
		if (mTimer != null) {
			synchronized (mTimer) {
				mWatchDogRefresher = watchDogRefresher;
			}
		}
	}

	static public void runWatchDog(HttpTransport aHttpTransport) {
		if (aHttpTransport != null) {
			if (mTimer != null)
				runWatchDog(null);
			mTimer = new Timer();
			mTimer.schedule(new WatchDog(aHttpTransport), WATCH_DOG_TIME,
					WATCH_DOG_TIME);
		} else {
			mTimer.cancel();
			mTimer = null;
		}
	}
}
