# Android Remote Encrypter (ARE)

![Release](https://jitpack.io/v/kevin-rabefaritra/remote-encrypter.svg)

### What is ARE?
Android Remote Encrypter (ARE) is an Android application asymmetric encryption module on remote data read access. ARE constitutes an additional layer to the Android [ContentProvider][cp1].
Compared to existing database encryption modules, ARE is:
1) More flexible: encryption can be applied to specific columns
2) On-demand: encryption is applied when the ContentProvider query() method is being called
3) Light: Since the data is only encrypted through the ContentProvider, no decryption is needed for internal database reading.

### How does ARE works?
Assuming there are two applications A and B owned by different developers and B wants to access some data from application A's database. According to the Google Developers Guidelines, [creating a content provider][cp2] is suggested to do so, however the problem is malicious access by untrusted applications since it's not possible to restrict the access to pre-defined applications only.
ARE works from a public-private generated keypair, data read through application A's ContentProvider are encrypted and decrypted by application B.

![Android RemoteEncrypter](img/remote-encrypter.png?raw=true "Android RemoteEncrypter representation")

### Installation
**Add Jitpack to your project file:**
```javascript
allprojects {
    repositories {
    ...
    maven { url "https://jitpack.io" }
    }
}
```
**Add the dependencies to your gradle file (app gradle):**
```javascript
dependencies {
	implementation 'com.github.kevin-rabefaritra:remote-encrypter:1.0.2'
}
```

### How to use
Assuming you have generated your public-private keypair:
1) In your custom ContentProvider class (used by the application hosting the database), invoke the encrypt method to the [Cursor][c1]:
```java
@Override
public Cursor query(@NonNull Uri uri, String[] projections, String selection, String[] selectionArgs, String sortOrder) {
    Cursor cursor = // Your implementation here ...
    return CursorServices.encrypt(cursor, PUBLIC_KEY, ENCRYPTED_COLUMNS, ALGORITHM);
}
```
where:
- **PUBLIC_KEY**:String is the base64 representation of the public key
- **ENCRYPTED_COLUMNS**:String[] are the columns to be encrypted
- **ALGORITHM**:String is the used encryption algorithm

2) From the application reading the database through the ContentProvider, since a [Cursor][c1] object is returned by the query(...) method, decrypting the data can be done as the following:
```java
...
    Cursor encryptedCursor = contentProvider.query(...);
    Cursor decryptedCursor = CursorServices.decrypt(encryptedCursor, PRIVATE_KEY, ENCRYPTED_COLUMNS, COLUMNS_TYPES, ALGORITHM);
    
    // Read data
    while(decryptedCursor.hasNext()) {
        ...
    }
    ...
```
where:
- **PRIVATE_KEY**:String is the base64 representation of the private key
- **ENCRYPTED_COLUMNS**:String[] are the columns to be decrypted
- **COLUMNS_TYPES**: int[] are the columns types (this will be changed to an array of Enum) since Integer values are changed to String before being encrypted.
- **EncryptServices.ALGORITHM**:String is the used encryption algorithm
# Help me
Pull requests are encouraged, don't hesitate to contribute to this library !

# To do
- Add a string obfuscation technique in order to protect the private key from extraction

# License
	Copyright 2019 Kevin Rabefaritra

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

		http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.

[cp1]: <https://developer.android.com/reference/android/content/ContentProvider>
[cp2]: <https://developer.android.com/guide/topics/providers/content-provider-creating>
[c1]: <https://developer.android.com/reference/android/database/Cursor>
