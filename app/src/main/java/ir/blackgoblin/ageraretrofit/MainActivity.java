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

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.agera.Function;
import com.google.android.agera.Repositories;
import com.google.android.agera.Repository;
import com.google.android.agera.Result;
import com.google.android.agera.Supplier;
import com.google.android.agera.Updatable;

import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private Repository<Result<SomeObject>> repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SomeObjectAPI someObjectAPI = new RetrofitHelper().generateAPI(SomeObjectAPI.class);
        Supplier<Result<SomeObject>> someObjectResultSupplier = someObjectAPI.getSomeObject();

        repository = Repositories.repositoryWithInitialValue(Result.<SomeObject>absent())
                .observe()
                .onUpdatesPerLoop()
                .goTo(Executors.newSingleThreadExecutor())
                .attemptGetFrom(someObjectResultSupplier)
                .orEnd(new Function<Throwable, Result<SomeObject>>() {
                    @NonNull
                    @Override
                    public Result<SomeObject> apply(@NonNull Throwable input) {
                        return Result.failure(input);
                    }
                })
                .thenTransform(new Function<SomeObject, Result<SomeObject>>() {
                    @NonNull
                    @Override
                    public Result<SomeObject> apply(@NonNull SomeObject input) {
                        return Result.success(input);
                    }
                })
                .compile();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Start listening to the repository, triggering the flow
        repository.addUpdatable(myUpdatable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop listening to the repository, deactivating it
        repository.removeUpdatable(myUpdatable);
    }

    private Updatable myUpdatable = new Updatable() {
        @Override
        public void update() {
            Result<SomeObject> result = repository.get();

            if (result.succeeded()) {
                result.get(); // the data. do what ever you want
            } else {
                result.getFailure(); // the throwable. do what ever you want
            }
        }
    };
}