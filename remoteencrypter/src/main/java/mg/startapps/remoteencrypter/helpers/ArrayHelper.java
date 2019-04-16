package mg.startapps.remoteencrypter.helpers;

public class ArrayHelper
{
	public static boolean contains(Object[] haystack, Object needle)
	{
		for(int i = 0; i < haystack.length; i++)
		{
			if(haystack[i].equals(needle))
			{
				return true;
			}
		}
		return false;
	}
}
