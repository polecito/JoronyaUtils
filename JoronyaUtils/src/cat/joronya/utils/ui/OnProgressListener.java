package cat.joronya.utils.ui;

public interface OnProgressListener 
{
	public void showDialog(String message, String negativeButton, String positiveButton, boolean cancelable);
	public void dismissDialog();
	public void setProgressValue(int value);
	
}
