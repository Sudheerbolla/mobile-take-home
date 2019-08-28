package com.guestlogix.controllers;

import com.guestlogix.listeners.IParserListener;
import com.guestlogix.utils.Constants;
import com.guestlogix.services.BackgroundApiCallTask;

public class CharactersController {

    private IParserListener iParserListener;

    public CharactersController(IParserListener iParserListener) {
        this.iParserListener = iParserListener;
    }

    public void fetchCharactersFromServer(String urlToLoad) {
        new BackgroundApiCallTask(iParserListener, Constants.WS_REQUEST_GET_CHARACTERS).execute(urlToLoad);
    }

}
