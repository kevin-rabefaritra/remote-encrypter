package mg.startapps.remoteencrypter.services;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import mg.startapps.remoteencrypter.helpers.ArrayHelper;

public class CursorServices
{
	public static final int FIELD_NULL = Cursor.FIELD_TYPE_NULL;
	public static final int FIELD_INTEGER = Cursor.FIELD_TYPE_INTEGER;
	public static final int FIELD_FLOAT = Cursor.FIELD_TYPE_FLOAT;
	public static final int FIELD_STRING = Cursor.FIELD_TYPE_STRING;
	public static final int FIELD_BLOB = Cursor.FIELD_TYPE_BLOB;

	/**
	 * Encrypts a Cursor data
	 * @param cursor: the Cursor to be processed
	 * @param publicKey: Public key used for encryption
	 * @param columns: Selected columns to encrypt
	 * @return a Cursor with encrypted data
	 */
	public static Cursor encrypt(@NonNull Cursor cursor, @Nullable String publicKey, String[] columns, String algorithm) throws BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, UnsupportedEncodingException, NoSuchPaddingException, InvalidKeyException
	{
		MatrixCursor matrixCursor = new MatrixCursor(cursor.getColumnNames(), cursor.getCount());
		cursor.moveToNext();
		do
		{
			Object[] values = new Object[cursor.getColumnCount()];
			for(int i = 0; i < values.length; i++)
			{
				boolean encrypt = ArrayHelper.contains(columns, cursor.getColumnName(i));
				switch (cursor.getType(i))
				{
					case FIELD_STRING:
						values[i] = encrypt ? EncryptServices.encrypt(cursor.getString(i), EncryptServices.getPublicKey(publicKey), algorithm) : cursor.getString(i);
						break;
					case FIELD_BLOB:
						values[i] = cursor.getBlob(i);
						break;
					case FIELD_FLOAT:
						values[i] = encrypt ? EncryptServices.encrypt(cursor.getFloat(i), EncryptServices.getPublicKey(publicKey), algorithm) : cursor.getFloat(i);
						break;
					case FIELD_INTEGER:
						values[i] = encrypt ? EncryptServices.encrypt(cursor.getInt(i), EncryptServices.getPublicKey(publicKey), algorithm) : cursor.getInt(i);
						break;
					case FIELD_NULL:
						values[i] = null;
						break;
					default:
						break;
				}
			}
			matrixCursor.addRow(values);
		}
		while (cursor.moveToNext());

		matrixCursor.moveToFirst();
		return matrixCursor;
	}

	/**
	 * Decrypts a Cursor data using a provided privateKey
	 * @param cursor: The Cursor to be processed
	 * @param privateKey: The private key
	 * @param columns: the columns to be decrypted
	 * @param columnTypes: the columns types
	 * @return
	 */
	public static Cursor decrypt(@NonNull Cursor cursor, @NonNull String privateKey, String[] columns, int[] columnTypes, String algorithm) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException
	{
		MatrixCursor matrixCursor = new MatrixCursor(cursor.getColumnNames());
		cursor.moveToNext();
		do
		{
			Object[] values = new Object[cursor.getColumnCount()];
			for (int i = 0; i < values.length; i++)
			{
				boolean decrypt = ArrayHelper.contains(columns, cursor.getColumnName(i));
				switch (columnTypes[i])
				{
					case FIELD_STRING:
						values[i] = decrypt ? new String(EncryptServices.decrypt(cursor.getString(i), EncryptServices.getPrivateKey(privateKey), algorithm), EncryptServices.ENCODING) : cursor.getString(i);
						break;
					case FIELD_BLOB:
						values[i] = cursor.getBlob(i);
						break;
					case FIELD_FLOAT:
						values[i] = decrypt ? Float.parseFloat(new String(EncryptServices.decrypt(cursor.getString(i), EncryptServices.getPrivateKey(privateKey), algorithm), EncryptServices.ENCODING)) : cursor.getFloat(i);
						break;
					case FIELD_INTEGER:
						values[i] = decrypt ? Integer.parseInt(new String(EncryptServices.decrypt(cursor.getString(i), EncryptServices.getPrivateKey(privateKey), algorithm), EncryptServices.ENCODING)) : cursor.getInt(i);
						break;
					case FIELD_NULL:
						values[i] = null;
						break;
					default:
						break;
				}
				matrixCursor.addRow(values);
			}
		}
		while(cursor.moveToNext());

		matrixCursor.moveToFirst();
		return matrixCursor;
	}
}
