package com.dannybit.tuneflow.events;

import com.dannybit.tuneflow.fragments.search.WebsiteSelection;

/**
 * Created by danielnamdar on 8/10/15.
 */
public class WebsiteSelectedEvent {

    private WebsiteSelection selection;

    public WebsiteSelectedEvent(WebsiteSelection selection){
        this.selection = selection;
    }

    public WebsiteSelection getSelection() {
        return selection;
    }
}
