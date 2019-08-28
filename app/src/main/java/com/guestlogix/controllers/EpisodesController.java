package com.guestlogix.controllers;

import com.guestlogix.listeners.IParserListener;
import com.guestlogix.utils.Constants;
import com.guestlogix.services.BackgroundApiCallTask;

public class EpisodesController {

    private IParserListener iParserListener;

    public EpisodesController(IParserListener iParserListener) {
        this.iParserListener = iParserListener;
    }

    public void fetchEpisodesFromServer(String urlToLoad) {
        new BackgroundApiCallTask(iParserListener, Constants.WS_REQUEST_GET_EPISODES).execute(urlToLoad);
    }

}
