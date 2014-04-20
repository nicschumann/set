package com.workshop.set.controller;

import com.workshop.set.controller.calls.Call;
import com.workshop.set.controller.responses.Response;
import com.workshop.set.model.Model;
import com.workshop.set.model.VectorSpace;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;
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


//    @Override
//    public Response<VectorSpace.Point> submit( Call<VectorSpace.Point> call ) {
//
//        FutureTask<VectorSpace.Point> f = new FutureTask<>( call );
//        return null;
//
//    }


	@Override
	public <T> Response<T> submit(Call<T> message) {
		// TODO Auto-generated method stub
		return null;
	}


}
