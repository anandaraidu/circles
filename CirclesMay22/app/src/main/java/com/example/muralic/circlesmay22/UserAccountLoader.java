package com.example.muralic.circlesmay22;

import android.content.AsyncTaskLoader;
import android.content.Context;

import android.content.AsyncTaskLoader;
import android.content.Context;

/**
 * A custom {@link android.content.AsyncTaskLoader} that retrieves
 * the {@link UserAccountUtils.UserAccount} asynchronously.
 */
public class UserAccountLoader extends AsyncTaskLoader<UserAccountUtils.UserAccount> {

    public UserAccountLoader(Context context) {
        super(context);
    }

    @Override
    public UserAccountUtils.UserAccount loadInBackground() {
        return UserAccountUtils.getUserAccount(getContext());
    }

    @Override
    public void deliverResult(UserAccountUtils.UserAccount user_account) {
        _user_account = user_account;

        if (isStarted())
            // If the Loader is currently started, we can immediately
            // deliver its results.
            super.deliverResult(user_account);
    }

    @Override
    protected void onStartLoading() {
        if (_user_account != null)
            // Delivers the result immediately when it's already available
            deliverResult(_user_account);

        if (takeContentChanged() || _user_account == null) {
            // If the data has changed since the last time it was loaded
            // or is not currently available, start a load.
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        // Attempts to cancel the current load task if possible.
        cancelLoad();
    }

    /**
     * Handles a request to completely reset the Loader.
     */
    @Override protected void onReset() {
        super.onReset();

        // Ensures the loader is stopped
        onStopLoading();

        // Clears the stored list
        if (_user_account != null)
            _user_account = null;
    }

    /** The list of the user's possible email address and name */
    private UserAccountUtils.UserAccount _user_account;
}
