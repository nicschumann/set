package com.workshop.set.controller;

import com.workshop.set.controller.calls.Call;
import com.workshop.set.controller.responses.Response;
import com.workshop.set.view.View;

/**
 * Created by nicschumann on 4/16/14.
 */
public interface Controller {
    public <T> Response<T> submit( Call<T> message );
}
