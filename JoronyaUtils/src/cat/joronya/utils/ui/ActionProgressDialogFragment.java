package cat.joronya.utils.ui;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class ActionProgressDialogFragment extends DialogFragment
{
	OnMessageDialogListener mOnMessageDialogListener;
	
	@Override
	public void onAttach(Activity activity) 
	{
		super.onAttach(activity);
		try {
			mOnMessageDialogListener = (OnMessageDialogListener) activity;
        } catch (ClassCastException e) {
        	mOnMessageDialogListener = null;
        }
	}
	
	public static ActionProgressDialogFragment newInstance(String message, 
			String negativeLabel, String positiveLabel, boolean cancelable)
	{
		ActionProgressDialogFragment frag = new ActionProgressDialogFragment();
		Bundle args = new Bundle();
        args.putString("cat.joronya.ui.progress.message", message);
        args.putString("cat.joronya.ui.progress.negative_label", negativeLabel);
        args.putString("cat.joronya.ui.progress.positive_label", positiveLabel);
        args.putBoolean("cat.joronya.ui.progress.cancelable", cancelable);
        frag.setArguments(args);
        return frag;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) 
	{
		String message = getArguments().getString("cat.joronya.ui.progress.message");
		String negativeLabel = getArguments().getString("cat.joronya.ui.progress.negative_label");
		String positiveLabel = getArguments().getString("cat.joronya.ui.progress.positive_label");
		boolean cancelable = getArguments().getBoolean("cat.joronya.ui.progress.cancelable");

		ProgressDialog progress = new ProgressDialog(getActivity());
		progress.setTitle("");
		progress.setMessage(message);
		progress.setMax(100);
		progress.setCancelable(cancelable);
		
		// si es cancelable, posem botons
		if( cancelable && mOnMessageDialogListener != null)
		{
			progress.setButton(DialogInterface.BUTTON_NEGATIVE, 
				negativeLabel, 
				new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						mOnMessageDialogListener.onDialogNegativeClick(ActionProgressDialogFragment.this);
					}
				});
			
			progress.setButton(DialogInterface.BUTTON_POSITIVE, 
				positiveLabel, 
				new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which) 
				{
					mOnMessageDialogListener.onDialogPositiveClick(ActionProgressDialogFragment.this);
				}
			});
		}
		
		progress.show();
		return progress;
	}
	
	public void setProgressValue(int value)
	{
		ProgressDialog pd = (ProgressDialog)getDialog();
		pd.setProgress(value);
	}
}
