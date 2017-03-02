package com.quebec.services;

import org.json.JSONObject;

/**
 * Created by Andy on 15/02/2017.
 */

public class BaseDAO {
    private JSONObject _DAO_BODY;

    public BaseDAO(JSONObject body) {
        this._DAO_BODY = body;
    }

    public JSONObject get_DAO_BODY() {
        return (_DAO_BODY == null) ? new JSONObject() : _DAO_BODY;
    }

    public void set_DAO_BODY(JSONObject _DAO_BODY) {
        this._DAO_BODY = _DAO_BODY;
    }
}
