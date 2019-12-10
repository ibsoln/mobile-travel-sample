//
// Copyright (c) 2019 Couchbase, Inc All rights reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
package com.couchbase.travelsample.db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.DataSource;
import com.couchbase.lite.Expression;
import com.couchbase.lite.QueryBuilder;
import com.couchbase.lite.Result;
import com.couchbase.lite.ResultSet;
import com.couchbase.lite.SelectResult;
import com.couchbase.travelsample.model.Flight;
import com.couchbase.travelsample.model.Hotel;


public class FlightsDao {
    public static final String ID_GUEST_DOC = "user::guest";
    public static final String TYPE_AIRPORT = "airport";
    public static final String PROP_TYPE = "type";
    public static final String PROP_AIRPORT_NAME = "airportname";


    private final LocalStore db;
    private final DBExecutor exec;

    @Inject
    public FlightsDao(@Nonnull LocalStore db, @Nonnull DBExecutor exec) {
        this.db = db;
        this.exec = exec;
    }

    public void getFlights(@Nonnull Consumer<List<Flight>> listener) {
        exec.submit(this::queryFlightsAsync, listener);
    }

    public void searchAirports(@Nonnull String name, @Nonnull Consumer<List<String>> listener) {
        exec.submit(() -> searchAirportsAsync(name), listener);
    }

    @Nullable
    private List<String> searchAirportsAsync(@Nonnull String name)
        throws CouchbaseLiteException {
        if (!db.isOpen()) { return null; }

        final ResultSet results = QueryBuilder.select(SelectResult
            .expression(Expression.property(PROP_AIRPORT_NAME)))
            .from(DataSource.database(db.getDatabase()))
            .where(Expression.property(PROP_TYPE).equalTo(Expression.string(TYPE_AIRPORT))
                .and(Expression.property(PROP_AIRPORT_NAME).like(Expression.string(name))))
            .execute();

        List<String> airports = new ArrayList<>();
        Result row;
        while ((row = results.next()) != null) {
            airports.add(row.getString(PROP_AIRPORT_NAME));
        }

        return airports;
    }

    @Nullable
    private List<Flight> queryFlightsAsync() throws CouchbaseLiteException {
        if (!db.isOpen()) { return Collections.emptyList(); }
        return Collections.emptyList();
    }
}
