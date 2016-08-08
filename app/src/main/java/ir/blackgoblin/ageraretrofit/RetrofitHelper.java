/*
 * Copyright (c) 2016. Reza Bigdeli
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ir.blackgoblin.ageraretrofit;

import me.drakeet.retrofit2.adapter.agera.AgeraCallAdapterFactory;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * this class is responsible to initialize the Retrofit and provide the APIs
 */

public class RetrofitHelper {

    private static final String API_BASE_URL = "http://beautyshare.tk/";
    private Retrofit retrofit;

    /**
     * initializes the Retrofit to work with {@link AgeraCallAdapterFactory}
     */
    public RetrofitHelper() {
        retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .client(new OkHttpClient())
                .addCallAdapterFactory(AgeraCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /**
     * generates a service for a specific class
     *
     * @param serviceClass the class to create the service for
     * @param <T>          the type of the service
     * @return the service
     */
    public <T> T generateAPI(Class<T> serviceClass) {
        return retrofit.create(serviceClass);
    }
}