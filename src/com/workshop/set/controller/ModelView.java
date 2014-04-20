package com.workshop.set.controller;

import com.workshop.set.controller.calls.Call;
import com.workshop.set.controller.responses.FutureResponse;
import com.workshop.set.controller.responses.Response;
import com.workshop.set.model.Model;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Created by nicschumann on 4/20/14.
 */
public class ModelView implements Controller {

    public ModelView( Model m ) {

        _model = m;
        _jobpool = new ScheduledThreadPoolExecutor( Runtime.getRuntime().availableProcessors() / 2 );
        _dispatchpool = new ScheduledThreadPoolExecutor( Runtime.getRuntime().availableProcessors() / 2 );

    }

    private Model           _model;
    private ExecutorService _jobpool;
    private ExecutorService _dispatchpool;


    public <T> Response<T> submit( Call<T> call ) {

        call.bind( _model );

        FutureResponse<T> r = new FutureResponse<>( _jobpool.submit( call ) );

        _dispatchpool.submit( r );

        return r;

    }




}
