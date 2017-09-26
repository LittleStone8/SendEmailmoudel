package org.opentaps.gwt.common.client.suggest;

import org.opentaps.gwt.common.client.lookup.configuration.SalesOrderLookupConfiguration;

public class IsFixOrderAutocomplete extends EntityAutocomplete {

    /**
     * Default constructor.
     * @param fieldLabel the field label
     * @param name the field name used in the form
     * @param fieldWidth the field size in pixels
     */
    public IsFixOrderAutocomplete(String fieldLabel, String name, int fieldWidth) {
        super(fieldLabel, name, fieldWidth, SalesOrderLookupConfiguration.URL_ISFIXORDER,"");
    }
    
}
