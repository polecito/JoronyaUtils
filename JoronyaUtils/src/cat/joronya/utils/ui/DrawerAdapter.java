package cat.joronya.utils.ui;

import cat.joronya.utils.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DrawerAdapter extends ArrayAdapter<DrawerItem> 
{
    private LayoutInflater inflater;
    
    public DrawerAdapter(Context context, int textViewResourceId, DrawerItem[] objects ) 
    {
        super(context, textViewResourceId, objects);
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) 
    {
        View view = null ;
        DrawerItem menuItem = this.getItem(position);
        view = getItemView(convertView, parent, menuItem );
        return view ;
    }
    
    public View getItemView( View convertView, ViewGroup parentView, DrawerItem drawerItem ) 
    {
    	ItemHolder itemHolder = null;
        
        if (convertView == null) 
        {
            convertView = inflater.inflate( R.layout.drawer_item, parentView, false);
            TextView labelView = (TextView) convertView
                    .findViewById( R.id.navmenuitem_label );
            ImageView iconView = (ImageView) convertView
                    .findViewById( R.id.navmenuitem_icon );

            itemHolder = new ItemHolder();
            itemHolder.labelView = labelView ;
            itemHolder.iconView = iconView ;

            convertView.setTag(itemHolder);
        }

        if ( itemHolder == null ) 
        {
        	itemHolder = (ItemHolder) convertView.getTag();
        }
                    
        itemHolder.labelView.setText(drawerItem.getLabel());
        itemHolder.iconView.setImageResource(drawerItem.getIcon());
        
        return convertView ;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }
    
    @Override
    public int getItemViewType(int position) {
        return DrawerItem.ITEM;
    }
    
    @Override
    public boolean isEnabled(int position) {
        return getItem(position).isEnabled();
    }
    
    
    private static class ItemHolder {
        private TextView labelView;
        private ImageView iconView;
    }
}