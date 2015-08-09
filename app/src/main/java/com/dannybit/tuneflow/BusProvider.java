package com.dannybit.tuneflow;

import com.squareup.otto.Bus;

/**
 * Created by danielnamdar on 8/7/15.
 */
public class BusProvider {
    private static Bus instance = null;

    private BusProvider(){
        instance = new Bus();
    }

    public static Bus getInstance() {
        if (instance == null){
            instance = new Bus();
        }
        return instance;
    }

}
