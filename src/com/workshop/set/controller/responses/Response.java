package com.workshop.set.controller.responses;

import java.util.concurrent.Callable;

/**
 * Created by nicschumann on 4/16/14.
 */
public interface Response<T> extends Callable<T> {
}
