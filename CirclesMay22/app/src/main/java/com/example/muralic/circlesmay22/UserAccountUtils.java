package com.example.muralic.circlesmay22;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Patterns;

//import com.google.android.gms.auth.GoogleAuthUtil;

import com.google.android.gms.auth.GoogleAuthUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Created by Muralic on 5/21/15.
 */

/**
 * Class which contains user and cell information.
 * Can also be devided into to class one per cell and one per user.
 */
public class UserAccountUtils {

    /**
     * Interface for interacting with the result of {@link UserAccountUtils#getUserAccount}.
     */
    public static class UserAccount {

        /**
         * Adds an email address to the list of  email addresses for the user
         * @param email the  email address
         */
        public void addemail(String email) {
            addemail(email, false);
        }

        /**
         * Adds an email address to the list of email addresses for the user. Retains information about whether this
         * email address is the primary email address of the user.
         * @param Email the email address
         * @param is_primary whether the email address is the primary email address
         */
        public void addemail(String Email, boolean is_primary) {
            if (Email == null) return;
            if (is_primary)  email = Email;
            emails.add(Email);
        }

        /**
         * Adds a name to the list of names for the user.
         * @param Name the name
         */
        public void addname(String Name) {
            addname(Name, false);
        }

        /**
         * Adds a name to the list of names for the user.
         * Retains information about whether this is the primary name.
         * @param Name the name
         * @param is_primary whether the name is hte primary
         */
        public void addname(String Name, boolean is_primary) {
            if (Name == null)  return;
            if (is_primary)  name = Name;
            names.add(Name);
        }

        /**
         * Adds a phone number to the list of phone numbers for the user.
         * @param PhoneNumber the phone number
         */
        public void addphonenumber(String PhoneNumber) {
            addphonenumber(PhoneNumber, false);
        }

        /**
         * Adds a phone number to the list of phone numbers for the user.  Retains information about whether this
         * phone number is the primary phone number of the user.
         * @param PhoneNumber the phone number
         * @param is_primary whether the phone number is teh primary phone number
         */
        public void addphonenumber(String PhoneNumber, boolean is_primary) {
            if (PhoneNumber == null) return;
            if (is_primary) phone_number = PhoneNumber;
            phone_numbers.add(PhoneNumber);

        }

        /**
         * Adds a device id of the device
         */
        public void adddeviceid(String DeviceId) {
            if (DeviceId != null) deviceid = DeviceId;
        }

        /**
         * Adds software version of the device
         */
        public void addsoftwareversion(String SoftwareVersion) {
            if (SoftwareVersion == null)
                softwareversion = SoftwareVersion;
        }

        /**
         * Sets the photo for the user.
         * @param Photo the photo
         */
        public void addphoto(Uri Photo) {
            if (photo != null) photo = Photo;
        }

        /**
         * Adds operator name of the device
         */
        public void addoperatorname(String OperatorName) {
            if (OperatorName != null) operatorname = OperatorName;
        }

        /**
         * Adds the sim country code
         */
        public void addcountrycode(String CountryCode) {
            if (CountryCode != null) simcountrycode = CountryCode;
        }

        /**
         * Adds the sim operator
         */
        public void addsimoperator(String SimOperator) {
            if (SimOperator != null) simoperator = SimOperator;
        }

        /**
         * Adds the sim Serial Number
         */
        public void addsimserialno(String SimSerialNo) {
            if (SimSerialNo != null) simserialno = SimSerialNo;
        }

        /**
         * Adds the subscriber id
         */
        public void addsubscriberid(String SubScriberId) {
            if (SubScriberId != null) subscriberid = SubScriberId;
        }

        /**
         * Retrieves the subscriber id
         * @retrun the subscriber id
         */
        public String getsubscriberid() { return subscriberid; }


        /**
         * Retrieves the sim serial no
         * @retrun the sim serial no
         */
        public String getsimserialno() { return simserialno; }

        /**
         * Retrieves the operator of the sim
         * @retrun the sim operator
         */
        public String getsimoperator() { return simoperator; }

        /**
         * Retrieves the Country Code of the sim
         * @retrun the country code
         */
        public String getcountrycode() { return simcountrycode; }

        /**
         * Retrieves the Operator Name
         * @retrun the operatorname
         */
        public String getoperatorname() { return operatorname; }

        /**
         * Retrieves the Device ID
         * @retrun the device id
         */
        public String getdeviceid() { return deviceid; }

        /**
         * Retrieves the Software Version
         * @retrun the SoftwareVersion
         */
        public String getsoftwareversion() { return softwareversion; }

        /**
         * Retrieves the list of email addresses.
         * @return the list of email addresses
         */
        public List<String> getemails() {
            return emails;
        }

        /**
         * Retrieves the list of names.
         * @return the list of names
         */
        public List<String> getnames() {
            return names;
        }

        /**
         * Retrieves the list of phone numbers
         * @return the list of phone numbers
         */
        public List<String> getphonenumbers() {
            return phone_numbers;
        }

        /**
         * Retrieves the photo.
         * @return the photo
         */
        public Uri getphoto() {
            return photo;
        }

        /**
         * Retrieves the primary email address.
         * @return the primary email address
         */
        public String getemail() {
            return email;
        }

        /**
         * Retrieves the primary phone number
         * @return the primary phone number
         */
        public String getphonenumber() {
            return phone_number;
        }

        /**
         * Retrives the primary name.
         * @return the primary name
         */
        public String getname() {
            return name;
        }

        /** The primary email address */
        private String email;
        /** The primary name */
        private String name;
        /** The primary phone number */
        private String phone_number;
        /** A list of email addresses for the user */
        private List<String> emails = new ArrayList<String>();
        /** A list of names for the user */
        private List<String> names = new ArrayList<String>();
        /** A list of phone numbers for the user */
        private List<String> phone_numbers = new ArrayList<String>();
        /** A photo for the user */
        private Uri photo;
        /** Device ID of the phone */
        private String deviceid;
        /** Software version */
        private String softwareversion;
        /** Operator who is providing the celphone services */
        private String operatorname;
        /** Country Code of the cell */
        private String simcountrycode;
        /** Sim Operator who is providing the service, what is diff of operator name, sim operator? */
        private String simoperator;
        /** serial no of the sim */
        private String simserialno;
        /** Subscriber id */
        private String subscriberid;
    }

    /**
     * Retrieves the user account information.
     * @param context the context from which to retrieve the user account
     * @return the user account
     */
    public static UserAccount getUserAccount(Context context) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH
                ? getUserAccountOnIcsDevice(context)
                : getUserAccountOnGingerbreadDevice(context);
    }

    /**
     * Retrieves the user account information in a manner supported by Gingerbread devices.
     * @param context the context from which to retrieve the user's email address and name
     * @return a list of the user's email address and name
     */
    private static UserAccount getUserAccountOnGingerbreadDevice(Context context) {
        // Other that using Patterns (API level 8) this works on devices down to API level 5
        final Matcher valid_email_address = Patterns.EMAIL_ADDRESS.matcher("");
        final Account[] accounts = AccountManager.get(context).getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
        final UserAccount user_account = new UserAccount();
        // As far as I can tell, there is no way to get the real name or phone number from the Google account
        for (Account account : accounts) {
            if (valid_email_address.reset(account.name).matches())
                user_account.addemail(account.name);
        }
        // Gets the phone number of the device if the device has one
        if (context.getPackageManager().hasSystemFeature(context.TELEPHONY_SERVICE)) {
            final TelephonyManager telephony = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
            user_account.addphonenumber(telephony.getLine1Number());
            user_account.addcountrycode(telephony.getSimCountryIso());
            user_account.adddeviceid(telephony.getDeviceId());
            user_account.addoperatorname(telephony.getNetworkOperator());
            user_account.addsimoperator(telephony.getSimOperator());
            user_account.addsimserialno(telephony.getSimSerialNumber());
            user_account.addsoftwareversion(telephony.getDeviceSoftwareVersion());
            user_account.addsubscriberid(telephony.getSubscriberId());
        }

        return user_account;
    }

    /**
     * Retrieves the user account information in a manner supported by Ice Cream Sandwich devices.
     * @param context the context from which to retrieve the user's email address and name
     * @return  a list of the user's email address and name
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private static UserAccount getUserAccountOnIcsDevice(Context context) {
        final ContentResolver content = context.getContentResolver();
        final Cursor cursor = content.query(
                // Retrieves data rows for the device user's 'account' contact
                Uri.withAppendedPath(
                        ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY
                ),
                accountQuery.PROJECTION,
                // Selects only email addresses or names
                ContactsContract.Contacts.Data.MIMETYPE + "=? OR "
                        + ContactsContract.Contacts.Data.MIMETYPE + "=? OR "
                        + ContactsContract.Contacts.Data.MIMETYPE + "=? OR "
                        + ContactsContract.Contacts.Data.MIMETYPE + "=?",
                new String[]{
                        ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE,
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
                        ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE
                },

                // Show primary rows first. Note that there won't be a primary email address if the
                // user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC"
        );

        final UserAccount user_account = new UserAccount();
        String mime_type;
        while (cursor.moveToNext()) {
            mime_type = cursor.getString(accountQuery.MIME_TYPE);
            if (mime_type.equals(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE))
                user_account.addemail(cursor.getString(accountQuery.EMAIL),
                        cursor.getInt(accountQuery.IS_PRIMARY_EMAIL) > 0);
            else if (mime_type.equals(ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE))
                user_account.addname(cursor.getString(accountQuery.GIVEN_NAME) + " " + cursor.getString(accountQuery.FAMILY_NAME));
            else if (mime_type.equals(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE))
                user_account.addphonenumber(cursor.getString(accountQuery.PHONE_NUMBER),
                        cursor.getInt(accountQuery.IS_PRIMARY_PHONE_NUMBER) > 0);
            else if (mime_type.equals(ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE))
                user_account.addphoto(Uri.parse(cursor.getString(accountQuery.PHOTO)));
        }

        cursor.close();

        return user_account;
    }

    /**
     * Contacts user account query interface.
     */
    private interface accountQuery {
        /** The set of columns to extract from the account query results */
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
                ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME,
                ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.IS_PRIMARY,
                ContactsContract.CommonDataKinds.Photo.PHOTO_URI,
                ContactsContract.Contacts.Data.MIMETYPE
        };

        /** Column index for the email address in the account query results */
        int EMAIL = 0;
        /** Column index for the primary email address indicator in the account query results */
        int IS_PRIMARY_EMAIL = 1;
        /** Column index for the family name in the account query results */
        int FAMILY_NAME = 2;
        /** Column index for the given name in the account query results */
        int GIVEN_NAME = 3;
        /** Column index for the phone number in the account query results */
        int PHONE_NUMBER = 4;
        /** Column index for the primary phone number in the account query results */
        int IS_PRIMARY_PHONE_NUMBER = 5;
        /** Column index for the photo in the account query results */
        int PHOTO = 6;
        /** Column index for the MIME type in the account query results */
        int MIME_TYPE = 7;
    }
}