package com.workshop.set.controller;

import com.workshop.set.controller.calls.Call;
import com.workshop.set.controller.responses.Response;
import com.workshop.set.view.View;

/**
 * Created by nicschumann on 4/16/14.
 */
public interface Controller {
    public Controller bind( View view );
    public Response submit( Call message );
}
