package com.example.muralic.circlesmay22;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.content.CursorLoader;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Muralic on 6/1/15.
 */ // new ContactFetcher(this).fetchAll();
public class ContactFetcher {
    private Context context;

    public ContactFetcher(Context c) {
        this.context = c;
    }

    public ArrayList<Contact> fetchAll() {
        Log.d("ContactFetcher:", "calling fetchall");
        ArrayList<Contact> listContacts = new ArrayList<Contact>();
        CursorLoader cursorLoader = new CursorLoader(context, ContactsContract.RawContacts.CONTENT_URI,
                null, // the columns to retrieve (all)
                null, // the selection criteria (none)
                null, // the selection args (none)
                null // the sort order (default)
        );

        Cursor c = cursorLoader.loadInBackground();
        if (c.moveToFirst())
            Log.d("ContectFetcher:", "Fetchall got curserLoader");
        if (c.moveToFirst()) {
            do {
                Log.d("Contact Loader:", "Moving to next contact");
                Contact contact = loadContactData(c);
                listContacts.add(contact);
            } while (c.moveToNext());
        }
        c.close();
        return listContacts;
    }

    private Contact loadContactData(Cursor c) {
        // Get Contact ID
        int idIndex = c.getColumnIndex(ContactsContract.Contacts._ID);
        String contactId = c.getString(idIndex);
        Log.d("LoadContactData:", contactId);
        // Get Contact Name
        int nameIndex = c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        String contactDisplayName = c.getString(nameIndex);
        Log.d("LoadContactData:", contactDisplayName);
        Contact contact = new Contact(contactId, contactDisplayName);
        fetchContactNumbers(c, contact);
        fetchContactEmails(c, contact);
        return contact;
    }

    public void fetchContactNumbers(Cursor cursor, Contact contact) {
        // Get numbers
        final String[] numberProjection = new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.TYPE, };

        Cursor phone = new CursorLoader(context, ContactsContract.CommonDataKinds.Phone.CONTENT_URI, numberProjection,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?", new String[] { String.valueOf(contact.id) }, null)
                .loadInBackground();

        if (phone.moveToFirst()) {
            final int contactNumberColumnIndex = phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            final int contactTypeColumnIndex = phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);

            while (!phone.isAfterLast()) {
                final String number = phone.getString(contactNumberColumnIndex);
                final int type = phone.getInt(contactTypeColumnIndex);
                String customLabel = "Custom";
                CharSequence phoneType = ContactsContract.CommonDataKinds.Phone.getTypeLabel(
                        context.getResources(), type, customLabel);
                contact.addNumber(number, phoneType.toString());
                phone.moveToNext();
            }

        }
        phone.close();
    }

    public void fetchContactEmails(Cursor cursor, Contact contact) {
        // Get email
        final String[] emailProjection = new String[] { ContactsContract.CommonDataKinds.Email.DATA, ContactsContract.CommonDataKinds.Email.TYPE };

        Cursor email = new CursorLoader(context, ContactsContract.CommonDataKinds.Email.CONTENT_URI, emailProjection,
                ContactsContract.CommonDataKinds.Email.CONTACT_ID + "= ?", new String[] { String.valueOf(contact.id) }, null)
                .loadInBackground();

        if (email.moveToFirst()) {
            final int contactEmailColumnIndex = email.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);
            final int contactTypeColumnIndex = email.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE);

            while (!email.isAfterLast()) {
                final String address = email.getString(contactEmailColumnIndex);
                final int type = email.getInt(contactTypeColumnIndex);
                String customLabel = "Custom";
                CharSequence emailType = ContactsContract.CommonDataKinds.Email.getTypeLabel(
                        context.getResources(), type, customLabel);
                contact.addEmail(address, emailType.toString());
                email.moveToNext();
            }

        }

        email.close();
    }
}
