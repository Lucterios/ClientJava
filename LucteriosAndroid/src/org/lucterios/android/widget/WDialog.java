package org.lucterios.android.widget;

import org.lucterios.graphic.ExceptionDlg;
import org.lucterios.gui.GUIButton;
import org.lucterios.gui.GUIContainer;
import org.lucterios.gui.GUIDialog;
import org.lucterios.gui.GUIGenerator;
import org.lucterios.gui.NotifyFrameObserver;
import org.lucterios.gui.GUIContainer.ContainerType;
import org.lucterios.utils.LucteriosException;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

public class WDialog extends AlertDialog.Builder implements GUIDialog {

	private NotifyFrameObserver mNotifyFrameClose = null;
	private DialogVisitor mDialogVisitor = null;
	private WContainer mContainer;
	private WGenerator mGenerator;

	private double mPosition = 0.5;
	private AlertDialog mAlertDialog = null;
	private String mTitle;
	private Context mContext;

	public WDialog(Context context, WGenerator generator) {
		super(context);
		mContext = context;
		mGenerator = generator;
		init();
	}

	public GUIDialog createDialog() {
		return new WDialog(getContext(), mGenerator);
	}

	public Context getContext() {
		return mContext;
	}

	public GUIGenerator getGenerator() {
		return mGenerator;
	}

	protected void init() {
		mContainer = new WContainer(getContext(), ContainerType.CT_NORMAL, null);
		setView(mContainer);
	}

	public void setDialogVisitor(DialogVisitor dialogVisitor) {
		mDialogVisitor = dialogVisitor;
	}

	public void setTextTitle(String title) {
		super.setTitle(title);
		mTitle = title;
	}

	public void setVisible(boolean aVisible) {
		if (aVisible) {
			if (mAlertDialog == null) {
				if (mDialogVisitor != null) {
					mDialogVisitor.execute(this);
				}
				this.initialPosition();
				mAlertDialog = show();
			}
		} else if (mAlertDialog != null)
			mAlertDialog.hide();
	}

	public void close() {
		if (mNotifyFrameClose != null) {
			mNotifyFrameClose.close(true);
			mNotifyFrameClose = null;
		}
		mNotifyFrameClose = null;
	}

	public void dispose() {
		if (mAlertDialog != null)
			mAlertDialog.dismiss();
	}

	public GUIContainer getContainer() {
		return mContainer;
	}

	public void refresh() {
		try {
			if (mNotifyFrameClose != null)
				mNotifyFrameClose.refresh();
		} catch (LucteriosException e) {
			ExceptionDlg.throwException(e);
		}
	}

	public void setActive(boolean aIsActive) {
		mContainer.setActive(aIsActive);
		if (mGenerator.getFrame() != null)
			mGenerator.getFrame().setActive(aIsActive);
	}

	public void setNotifyFrameClose(NotifyFrameObserver aNotifyFrameClose) {
		mNotifyFrameClose = aNotifyFrameClose;
	}

	public void setDefaultButton(GUIButton btnAdd) {
	}

	public int getSizeX() {
		if (mAlertDialog != null)
			return mAlertDialog.getWindow().getAttributes().width;
		else
			return 0;
	}

	public int getSizeY() {
		if (mAlertDialog != null)
			return mAlertDialog.getWindow().getAttributes().height;
		else
			return 0;
	}

	public void setResizable(boolean isResizable) {
	}

	public void refreshSize() {
	}

	public void setSize(int width, int height) {
		if (mAlertDialog != null) {
			mAlertDialog.getWindow().getAttributes().width = width;
			mAlertDialog.getWindow().getAttributes().height = height;
			mAlertDialog.getWindow().setAttributes(
					mAlertDialog.getWindow().getAttributes());
		}
	}

	public void setLocation(int x, int y) {
		if (mAlertDialog != null) {
			mAlertDialog.getWindow().getAttributes().x = x;
			mAlertDialog.getWindow().getAttributes().y = y;
			mAlertDialog.getWindow().setAttributes(
					mAlertDialog.getWindow().getAttributes());
		}
	}

	public void initialPosition() {
		if (mAlertDialog != null) {
			DisplayMetrics displaymetrics = new DisplayMetrics();
			mAlertDialog.getWindow().getWindowManager().getDefaultDisplay()
					.getMetrics(displaymetrics);
			setLocation((displaymetrics.widthPixels - mAlertDialog.getWindow()
					.getAttributes().width) / 2,
					(int) ((displaymetrics.heightPixels - mAlertDialog
							.getWindow().getAttributes().height) * mPosition));
		}
	}

	public void setPosition(double position) {
		mPosition = position;
	}

	public void pack() {
	}

	public void requestFocus() {
	}

	public void toFront() {
	}

	public String getTextTitle() {
		return mTitle;
	}

	private Drawable mDrawable=null;
	public Builder setIcon(Drawable drawable) {
		mDrawable=drawable;	
		return super.setIcon(drawable);
	}
	
	public Drawable getIcon() {
		return mDrawable;
	}

}
