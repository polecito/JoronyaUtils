package cat.joronya.utils.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class MessageDialogFragment extends DialogFragment
{
	OnMessageDialogListener mOnMessageDialogListener;
	
	@Override
	public void onAttach(Activity activity) 
	{
		super.onAttach(activity);
		try 
		{
			mOnMessageDialogListener = (OnMessageDialogListener) activity;
        } 
		catch (ClassCastException e) 
        {
			throw new ClassCastException(activity.toString() + " must implement OnMessageDialogListener");
        }
	}
	
	public static MessageDialogFragment newInstance(String message, 
			String negativeLabel, String positiveLabel)
	{
		MessageDialogFragment frag = new MessageDialogFragment();
		Bundle args = new Bundle();
        args.putString("cat.joronya.ui.dialog.message", message);
        args.putString("cat.joronya.ui.dialog.negative_label", negativeLabel);
        args.putString("cat.joronya.ui.dialog.positive_label", positiveLabel);
        frag.setArguments(args);
        return frag;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) 
	{
		String message = getArguments().getString("cat.joronya.ui.dialog.message");
		String negativeLabel = getArguments().getString("cat.joronya.ui.dialog.negative_label");
		String positiveLabel = getArguments().getString("cat.joronya.ui.dialog.positive_label");
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("");
		builder.setMessage(message);
		
		builder.setNegativeButton(negativeLabel, 
			new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which) 
				{
					mOnMessageDialogListener.onDialogNegativeClick(MessageDialogFragment.this);
				}
			});
			
		builder.setPositiveButton(positiveLabel, 
			new DialogInterface.OnClickListener()
			{
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						mOnMessageDialogListener.onDialogPositiveClick(MessageDialogFragment.this);
					}
				
			});
		
		AlertDialog dialog = builder.create();
		return dialog;
	}
}
