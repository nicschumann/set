package com.workshop.set.view;

import com.workshop.set.controller.responses.Response;

/**
 * Created by nicschumann on 4/16/14.
 */
public interface View<T> {
    public boolean submit( Response<T> response );
}
