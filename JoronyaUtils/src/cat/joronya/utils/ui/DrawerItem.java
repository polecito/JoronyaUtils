package cat.joronya.utils.ui;

/**
 * Item with data for the drawer. 
 * @author pol
 */
public class DrawerItem
{
	public static int ITEM = 0;
	
	protected String label;	
	protected int icon;
	
	public DrawerItem()
	{
		label = "";
		icon = 0;
	}
	
    public boolean isEnabled() {
        return true;
    }
	
	public static DrawerItem create(String label, int icon)
	{
		DrawerItem drawerItem = new DrawerItem();
		drawerItem.label = label;
		drawerItem.icon = icon;
		return drawerItem;
	}
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}
}